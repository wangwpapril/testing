package com.cuc.miti.phone.xmc.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Advertise {

	public static final int TEST_ACTION = -1;
    public static final int WAP_ACTION = 0;
    public static final int BRAND_ACTION = 1;
    public static final int TICKET_ACTION = 2;
    public static final int LAUNCH_ACTION = 3;


    /**
     * 0 jump wap
     * 1 brand
     * 2 ticket
     * 3 other
     */
    public int action;

    // use by pv
    public int messageId;

    public String data;
    public String title;
    public String image;
    public String bigImgUrl;
    public String context;
    public boolean isNew;

    public Advertise(String url) {
    	bigImgUrl = url;
    }
    
    public Advertise(JSONObject object) throws JSONException {
        action = object.getInt("action");
        data = object.getString("data");
        image = object.getString("image");
        title = object.getString("title");
        isNew = object.optBoolean("isNew");
        
        context = object.optString("context");
        messageId = object.optInt("messageId");
    }
    
}
