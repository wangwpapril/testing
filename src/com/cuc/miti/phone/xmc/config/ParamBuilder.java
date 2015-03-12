package com.cuc.miti.phone.xmc.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.cuc.miti.phone.xmc.utils.IngleUtils;
import com.cuc.miti.phone.xmc.utils.PreferencesUtil;


/**
 * Created with IntelliJ IDEA.
 * User: kait
 * Date: 12-6-26
 * Time: 下午3:48
 * To change this template use File | Settings | File Templates.
 */
public class ParamBuilder {
	
	public static final int MAX_PAGE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    public static final int DEFAULT_PAGE_SIZE = 10;

    public static final String PAGE_INDEX = "pageIndex";
    public static final String PAGE_SIZE = "pageSize";
	public static final String USER_NAME = "username";
	public static final String PASSWORD = "password";
	public static final String CAPTCHA = "captcha";
	public static final String EMAIL = "eMail";
	public static final String TOKEN = "token";
	public static final String TYPE = "type";
	
	/**
     * Create a container to hold request parameters.
     */
    private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

    public void append(String param, String value) {
        params.add(new BasicNameValuePair(param, value));
    }

    public void append(String param, long value) {
        params.add(new BasicNameValuePair(param, String.valueOf(value)));
    }

    public void append(String param, int value) {
        params.add(new BasicNameValuePair(param, String.valueOf(value)));
    }

    public void append(String param, double value) {
        params.add(new BasicNameValuePair(param, String.valueOf(value)));
    }

    public void append(String param, boolean value) {
        params.add(new BasicNameValuePair(param, String.valueOf(value)));
    }

    public void remove(String param){
        params.remove(param);
    }

    public List<NameValuePair> getParamList() {
        return params;
    }

    /**
     * Clear cache data.
     */
    public void clear() {
        params.clear();
    }

    /**
     * Build a complete request URL base on a request parameter list.
     *
     * @param params
     * @return
     */
    public static String parseUrl(String resouce, List<NameValuePair> params) {
    	StringBuffer sBuffer = new StringBuffer(resouce);
//    	sBuffer.append("?token=").append(PreferencesUtil.getValue("token"));
    	
        if (params == null || params.size() == 0) {
        	return sBuffer.toString();
        } else {
            int cntParams = params.size();
            for (int i = 0; i < cntParams; i++) {
                NameValuePair param = params.get(i);
                sBuffer.append("&");
                sBuffer.append(param.getName()).append("=").append(IngleUtils.urlEncode(param.getValue()));
            }
            
           return sBuffer.toString();
        }
    }
    
}
