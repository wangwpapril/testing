package com.cuc.miti.phone.xmc.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class JifenProduct implements Serializable {
	
	public String id;
	
	public String interal;
	
	public String imgUrl;
	
	public JifenProduct(JSONObject obj) throws JSONException {
		id = obj.getString("Id");
		interal = obj.getString("interal");
		imgUrl = obj.getString("imgUrl");
	}

}
