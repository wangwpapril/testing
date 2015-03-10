package com.cuc.miti.phone.xmc.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.conn.ConnectTimeoutException;

import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.ViewDebug.FlagToString;
import android.widget.Toast;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Enums.DoPostType;
import com.cuc.miti.phone.xmc.domain.Enums.NetStatus;
import com.cuc.miti.phone.xmc.domain.XmcException;
import com.cuc.miti.phone.xmc.utils.Logger;


/*
 * ��������
 */
public class HttpClient {

	private static final int OK = 200;// OK: Success!
	private static final int NOT_MODIFIED = 304;
	private static final int BAD_REQUEST = 400;
	private static final int NOT_AUTHORIZED = 401;
	private static final int FORBIDDEN = 403;
	private static final int NOT_FOUND = 404;
	private static final int NOT_ACCEPTABLE = 406;
	private static final int INTERNAL_SERVER_ERROR = 500;
	private static final int BAD_GATEWAY = 502;
	private static final int SERVICE_UNAVAILABLE = 503;
	
	private static final int NETWORK_DISABLED=601;

	String multipart_form_data = "multipart/form-data";
	String boundary = "7d33a816d302b6";
	String twoHyphens="--";
	String lineEnd="\r\n";

	private int retryCount = Configuration.getRetryCount();

	
	/**
	 * Post����
	 * @param postParams
	 * @param connectionUrl
	 * @param connecTimeout
	 * @return
	 * @throws XmcException 
	 */
	public String doPost(PostParameter[] postParams,String connectionUrl,int connectTimeout) throws XmcException{
		NetStatus netStatus = IngleApplication.getNetStatus();
		if(netStatus == NetStatus.Disable){
			return String.valueOf(NETWORK_DISABLED);
		}
		
		int retriedCount = 0;
		Response response = null;

		for (retriedCount = 0; retriedCount < retryCount; retriedCount++) {

			int responseCode = -1;
			HttpURLConnection connection = null;
			OutputStream os = null;
			try {
				connection = (HttpURLConnection) new URL(connectionUrl).openConnection();
				if(connectTimeout !=0){
					connection.setConnectTimeout(connectTimeout);
				}
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
				connection.setDoOutput(true);
				
				String postParam = "";
				if (postParams != null) {
					postParam = encodeParameters(postParams);
				}
				byte[] bytes = postParam.getBytes("UTF-8");
				connection.setRequestProperty("Content-Length",Integer.toString(bytes.length));
				os = connection.getOutputStream();
				os.write(bytes);
				os.flush();
				os.close();
				response = new Response(connection);
				responseCode = response.getStatusCode();

				if (responseCode != OK) {
					if (responseCode < INTERNAL_SERVER_ERROR || retriedCount == retryCount)
							//throw new XmcException(getCause(responseCode) + "\n" + response.asString(), responseCode);
						throw new XmcException(getCause(responseCode));
				} else {break;}				
			}catch(ConnectTimeoutException e){
				throw new XmcException(XmcException.CONNECT_TIMEOUT_EXCEPTION,e);
			}catch(InterruptedIOException e){	
				throw new XmcException(XmcException.INTERRUPTED_IO_EXCEPTION,e);
			}catch (Exception e) {
				throw new XmcException(e.getMessage(), e);
			}
		}
		return response.asString();
	}

	/**
	 * Get����
	 * @param getParams
	 * @param connectionUrl
	 * @param connecTimeout
	 * @return
	 * @throws XmcException
	 */
	public String doGet(PostParameter[] getParams, String connectionUrl,int connectTimeout) throws XmcException{
		NetStatus netStatus = IngleApplication.getNetStatus();
		if(netStatus == NetStatus.Disable){
			return String.valueOf(NETWORK_DISABLED);
		}
	
		int retriedCount = 0;
		Response response = null;
		
		for(retriedCount = 0; retriedCount < retryCount; retriedCount++){
			
			int responseCode = -1;
			try {
				HttpURLConnection connection = null;
				try {

//					connection = (HttpURLConnection) new URL(connectionUrl+"?"+encodeParameters(getParams)).openConnection();
					connection = (HttpURLConnection) new URL(connectionUrl).openConnection();
					if(connectTimeout !=0){
						connection.setConnectTimeout(connectTimeout);
					}
					response = new Response(connection);
					responseCode = response.getStatusCode();

					if (responseCode != OK) {
						if (responseCode < INTERNAL_SERVER_ERROR || retriedCount == retryCount)
							//throw new XmcException(getCause(responseCode)+ "\n" + response.asString(), responseCode);
							throw new XmcException(getCause(responseCode));
					} else {
						break;
					}

				} finally {
					
				}
			}catch(ConnectTimeoutException e){
				throw new XmcException(XmcException.CONNECT_TIMEOUT_EXCEPTION,e);
			}catch(InterruptedIOException e){	
				throw new XmcException(XmcException.INTERRUPTED_IO_EXCEPTION,e);
			}catch (Exception e) {
				throw new XmcException(e.getMessage(), e);
			}
		}
		
		return response.asString();
	}
	/**
	 * Json��ʽ��POST����
	 * @param entity
	 * @param connectionUrl
	 * @param connectTimeout
	 * @return
	 * @throws XmcException
	 */
	public String post(String entity, String connectionUrl,
			int connectTimeout) throws XmcException{
		int retriedCount = 0;
		Response response = null;

		HttpHelper.downloadSize = 0;

		for (retriedCount = 0; retriedCount < retryCount; retriedCount++) {

			int responseCode = -1;
			HttpURLConnection connection = null;
			OutputStream os = null;
			try {
				connection = (HttpURLConnection) new URL(connectionUrl)
						.openConnection();
				if (connectTimeout != 0) {
					connection.setConnectTimeout(connectTimeout);
				}
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type",
						"application/json");
				connection.setRequestProperty("Accept", "application/json");

				connection.setDoOutput(true);

				String postParam = "";

				if (entity != null) {
					postParam = entity;
				}

				byte[] bytes = postParam.getBytes("UTF-8");
				connection.setRequestProperty("Content-Length",
						Integer.toString(bytes.length));
				os = connection.getOutputStream();
				os.write(bytes);
				os.flush();
				os.close();
				response = new Response(connection);
				responseCode = response.getStatusCode();

				if (responseCode != HttpHelper.OK) {
					if (responseCode < HttpHelper.INTERNAL_SERVER_ERROR
							|| retriedCount == retryCount)
						// throw new XmcException(getCause(responseCode) + "\n"
						// + response.asString(), responseCode);
						Log.v(HttpHelper.TAG, response.asString());
					throw new XmcException(getCause(responseCode));
				} else {
					break;
				}
			} catch (ConnectTimeoutException e) {
				throw new XmcException(
						String.valueOf(HttpHelper.CONNECT_TIMEOUT_EXCEPTION), e);
			} catch (InterruptedIOException e) {
				throw new XmcException(
						String.valueOf(HttpHelper.INTERRUPTED_IO_EXCEPTION), e);
			} catch (Exception e) {
				throw new XmcException(e.getMessage(), e);
			}
		}

		// ��ȡ���ص��ֽ���
		HttpHelper.downloadSize = response.asString().getBytes().length;
		return response.asString();
	}
	/**
	 * �ļ��ֿ��ϴ�ר��
	 * @param formPostParams
	 * @param contentPostParams
	 * @param fileData
	 * @return
	 * @throws XmcException
	 */
	public String multipartPost(PostParameter[] formPostParams,
			PostParameter[] contentPostParams,byte[] fileData) throws XmcException{

		int retriedCount = 0;
		Response response = null;

		String user_agent = "Mozilla/4.0 (compatible; OpenOffice.org)";

		for (retriedCount = 0; retriedCount < retryCount; retriedCount++) {
			int responseCode = -1;
			DataOutputStream os = null;
			HttpURLConnection connection = null;
			try {			
				connection = (HttpURLConnection) new URL(Configuration.getBaseUrl()).openConnection();
				connection.setDoOutput(true);
				connection.setUseCaches(false);
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Connection", "keep-alive");
				connection.setRequestProperty("Content-Type",multipart_form_data + ";boundary=" + boundary);
				connection.setRequestProperty("User-Agent", user_agent);
				
				String postParam = "";
				if (formPostParams != null&&contentPostParams!=null) {
					postParam=addFormField(formPostParams)+addContentField(contentPostParams);
				}
					
				byte[] bytes = postParam.getBytes("UTF-8");
				byte[] endBytes=(lineEnd+twoHyphens+boundary+twoHyphens+lineEnd).getBytes("UTF-8");

				connection.setRequestProperty("Content-Length",Integer.toString(bytes.length+fileData.length+endBytes.length));
				connection.connect();
					
				os = new DataOutputStream(connection.getOutputStream());
				os.write(bytes);
				os.write(fileData);
				os.write(endBytes);
				os.flush();
				os.close();
				response = new Response(connection);
				responseCode = response.getStatusCode();

				if (responseCode != OK) {
					if (responseCode < INTERNAL_SERVER_ERROR || retriedCount == retryCount)
							//throw new XmcException(getCause(responseCode) + "\n" + response.asString(), responseCode);
						throw new XmcException(getCause(responseCode));
				} else {break;}
			}catch(ConnectTimeoutException e){
				throw new XmcException(XmcException.CONNECT_TIMEOUT_EXCEPTION,e);
			}catch(InterruptedIOException e){	
				throw new XmcException(XmcException.INTERRUPTED_IO_EXCEPTION,e);
			}catch (Exception e) {
				throw new XmcException(e.getMessage(), e);
			}
		}
		return response.asString();
	}

	private String addFormField(PostParameter[] formPostParams) throws Exception {

		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < formPostParams.length; i++) {

			buffer.append(twoHyphens+boundary+lineEnd);
			buffer.append(
					"Content-Disposition: form-data; name=\""
							+ URLEncoder.encode(formPostParams[i].getName(), "UTF-8")+ "\""+lineEnd);
			buffer.append(lineEnd);
			buffer.append(URLEncoder.encode(formPostParams[i].getObject().toString(), "UTF-8")+lineEnd);
		}
		return buffer.toString();
	}

	private String addContentField(PostParameter[] contentPostParams) {

		StringBuffer buffer = new StringBuffer();
		
		buffer.append(twoHyphens+boundary+lineEnd);
		buffer.append("Content-Disposition: form-data; name=\"" + contentPostParams[0].getObject().toString()
						+"/"+contentPostParams[1].getObject().toString() +"\"; filename=\"" 
						+ contentPostParams[2].getObject().toString() + "\"" + lineEnd); 
		buffer.append("Content-Type: " + contentPostParams[3].getObject().toString() + lineEnd); 
		buffer.append(lineEnd);

		return buffer.toString();
	}

	private static String encodeParameters(PostParameter[] postParams) throws Exception {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < postParams.length; i++) {
			if (i != 0){
				buffer.append("&");
			}
			buffer.append(URLEncoder.encode(postParams[i].getName(), "UTF-8"))
					.append("=")
					.append(URLEncoder.encode(postParams[i].getObject().toString(), "UTF-8"));
		}

		return buffer.toString();
	}

	private static String getCause(int statusCode) {
		String cause = null;
		switch (statusCode) {
		case NOT_MODIFIED:
			break;
		case BAD_REQUEST:
			cause = "The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
			break;
		case NOT_AUTHORIZED:
			cause = "Authentication credentials were missing or incorrect.";
			break;
		case FORBIDDEN:
			cause = "The request is understood, but it has been refused.  An accompanying error message will explain why.";
			break;
		case NOT_FOUND:
			cause = "The URI requested is invalid or the resource requested, such as a user, does not exists.";
			break;
		case NOT_ACCEPTABLE:
			cause = "Returned by the Search API when an invalid format is specified in the request.";
			break;
		case INTERNAL_SERVER_ERROR:
			cause = "Something is broken.  Please post to the group so the Weibo team can investigate.";
			break;
		case BAD_GATEWAY:
			cause = "Weibo is down or being upgraded.";
			break;
		case SERVICE_UNAVAILABLE:
			cause = "Service Unavailable: The Weibo servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.";
			break;
		default:
			cause = "";
		}
		return statusCode + ":" + cause;
	}
}
