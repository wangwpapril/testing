package com.cuc.miti.phone.xmc.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.config.IngleApi;
import com.cuc.miti.phone.xmc.utils.PreferencesUtil;
import com.cuc.miti.phone.xmc.utils.StringUtil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.os.Handler;
import android.text.TextUtils;


public class NetworkService {
	private static final int CONNECTION_TIMEOUT = 30000;
	private static final int SOCKET_TIMEOUT = 60000;

    public static final int NATIVE_ERROR = 600;
    public static final int UNKNOWN_HOST = 601;
    public static final int PARSE_ERROR = 602;

    private DefaultHttpClient httpClient;

    private ExecutorService threadPool;

    public NetworkService() {
        // fixed thread pool
        threadPool = Executors.newFixedThreadPool(5);
        // Initialize http client
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
        httpClient = new DefaultHttpClient(cm, params);
    }

    public DefaultHttpClient getHttpClient() {
        setProxy();
        return httpClient;
    }

    /**
     * Simple asynchronous get with response, must be used in a looped thread(like ui thread)
     * @param url
     * @param responseCallback
     */
    public void get(final String url, final ICallback responseCallback) {
        setProxy();
        if (null != responseCallback) {
            final Handler handler = new Handler();
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        HttpGet m = new HttpGet(url);
                        addChannelHeader(m);
                        // Add gzip support
                        m.addHeader("Accept-Encoding", "gzip");
                        final HttpResponse response = httpClient.execute(m);
                        // Run in called thread
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                HttpEntity entity = response.getEntity();
                                int status = response.getStatusLine().getStatusCode();
                                String result = null;

                                // Gzip support
                                Header contentEncoding = response.getFirstHeader("Content-Encoding");
                                try {
                                    if (null != contentEncoding && "gzip".equalsIgnoreCase(contentEncoding.getValue())) {
                                        InputStream in = new GZIPInputStream(entity.getContent());
                                        result = StringUtil.getFromStream(in);
                                    } else {
                                        result = EntityUtils.toString(entity);
                                    }

                                } catch (IOException e) {
 //                                   Logg.e(e);
                                    status = NATIVE_ERROR;
                                    result = e.getMessage();
                                }
                                
                                refreshToken(result);
                                responseCallback.onResponse(status, result);
                            }
                        });
                    } catch (final Exception e) {
 //                       Logg.e(e);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                int errorCode = NATIVE_ERROR;
                                if (e instanceof UnknownHostException) {
                                    errorCode = UNKNOWN_HOST;
                                }
                                responseCallback.onResponse(errorCode, e.getMessage());
                            }
                        });
                    }
                }
            });
        }
    }

    /**
     * Synchronous get a HttpResponse object from url
     * @param url
     * @return
     * @throws IOException
     */
    public HttpResponse getResponse(String url) throws IOException {
        setProxy();
        HttpGet m = new HttpGet(url);
        addChannelHeader(m);
        // Add gzip support
        m.addHeader("Accept-Encoding", "gzip");
        return httpClient.execute(m);
    }

    /**
     * Synchronous get response with last modified header
     * @param url
     * @param lastModified
     * @return
     * @throws IOException
     */
    public HttpResponse getResponse(String url, String lastModified) throws IOException {
        setProxy();
        HttpGet m = new HttpGet(url);
        addChannelHeader(m);
        // Add gzip support
        m.addHeader("Accept-Encoding", "gzip");
        if (!StringUtil.isEmpty(lastModified))
            m.addHeader("If-Modified-Since", lastModified);

        return httpClient.execute(m);
    }

    /**
     * Synchronous get string result from url
     * @param url
     * @return
     */
    public String getSync(String url) throws IOException, NetworkException {
        String result = null;
        HttpResponse response = getResponse(url);

        if (null != response && response.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();
            // Gzip support
            Header contentEncoding = response.getFirstHeader("Content-Encoding");
            if (null != contentEncoding && "gzip".equalsIgnoreCase(contentEncoding.getValue())) {
                InputStream in = new GZIPInputStream(entity.getContent());
                result = StringUtil.getFromStream(in);
            } else {
                result = EntityUtils.toString(entity);
            }
            
            refreshToken(result);
            return result;
        } else {
            throw new NetworkException(NATIVE_ERROR, "status");
        }
    }

    /**
     * Asynchronous post with response callback, must be used in a looped thread(like ui thread)
     * @param url
     * @param params
     * @param responseCallback
     */
    public void post(final String url, final Map<String, String> params, final ICallback responseCallback) {
        setProxy();
        if (null != responseCallback) {
            final Handler handler = new Handler();
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        HttpPost m = new HttpPost(url);
                        addChannelHeader(m);
                        addFormEntityToHttpPost(m, params);
                        final HttpResponse response = httpClient.execute(m);
                        // Run in called thread
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                HttpEntity entity = response.getEntity();
                                int status = response.getStatusLine().getStatusCode();
                                String result = null;

                                try {
                                    result = EntityUtils.toString(entity);
                                } catch (IOException e) {
//                                    Logg.e(e);
                                    status = NATIVE_ERROR;
                                    result = e.getMessage();
                                }
                                
                                refreshToken(result);
                                responseCallback.onResponse(status, result);
                            }
                        });
                    } catch (final Exception e) {
  //                      Logg.e(e);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                int errorCode = NATIVE_ERROR;
                                if (e instanceof UnknownHostException) {
                                    errorCode = UNKNOWN_HOST;
                                }
                                responseCallback.onResponse(errorCode, e.getMessage());
                            }
                        });
                    }
                }
            });
        }
    }

    private void setProxy() {
//        if (!Config.USE_PROXY) return;
        NetworkInfo networkInfo = ((ConnectivityManager) IngleApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        try {
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String proxy = Proxy.getDefaultHost();
                int port = (Proxy.getDefaultPort() == -1 ? 80 : Proxy.getDefaultPort());
                if (null != proxy) {
                    HttpHost httpHost = new HttpHost(proxy, port, HttpHost.DEFAULT_SCHEME_NAME);
                    httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, httpHost);
                } else {
                    httpClient.getParams().removeParameter(ConnRoutePNames.DEFAULT_PROXY);
                }
            }
        } catch (Exception e) {
 //           Logg.e(e);
        }
    }
    
    private void addChannelHeader(HttpUriRequest request) {
/*    	request.addHeader("source_ext", "0");
    	request.addHeader("source", LeyouApplication.instance.channelId);
    	request.addHeader("mac", DeviceInfo.getMacAddress(LeyouApplication.instance.globleContext));
 */   }
    
    private void addFormEntityToHttpPost(HttpPost m, Map<String, String> params) throws UnsupportedEncodingException{
        if (null != params && params.size() > 0) {
            List<NameValuePair> lstNVPs = new ArrayList<NameValuePair>();
            for (String key : params.keySet()) {
                String value = params.get(key);
                lstNVPs.add(new BasicNameValuePair(key, value));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(lstNVPs, HTTP.UTF_8);
            m.setEntity(entity);
        }
    }
    
    public boolean refreshToken(String oldToken) {
    	if (TextUtils.isEmpty(oldToken) || !oldToken.contains(IngleApi.FAIL_CODE_100)) {
    		return true;
    	}
    	
    	threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpGet m = new HttpGet(IngleApi.GET_TOKEN);
                    final HttpResponse response = httpClient.execute(m);
                    
                    HttpEntity entity = response.getEntity();
                    String result = EntityUtils.toString(entity);
                    if (!TextUtils.isEmpty(result)) {
                    	PreferencesUtil.saveValue("token", new JSONObject(result).optString("token"));
                    }
                } catch (Exception e) {
  //              	Logg.e(e);
                }
            }
    	});
    	
	    return false;
    }

    /**
     * Asynchronous http get/post callback
     */
    public interface ICallback {
        public void onResponse(int status, String result);
    }

    public class NetworkException extends Exception {

        private static final long serialVersionUID = -2841294936395077461L;

        public int status = NATIVE_ERROR;

        public NetworkException() {
            super();
        }

        public NetworkException(int status) {
            super();
            this.status = status;
        }

        public NetworkException(String message) {
            super(message);
        }

        public NetworkException(int status, String message) {
            super(message);
            this.status = status;
        }

        public NetworkException(String message, Throwable cause) {
            super(message, cause);
        }

        public NetworkException(Throwable cause) {
            super(cause);
        }
    }
}