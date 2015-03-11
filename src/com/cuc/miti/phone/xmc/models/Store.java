package com.cuc.miti.phone.xmc.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Store implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String id;
	
	public String img;
	
	public String address;
	
	public String tel;
	
	public String latitude;
	
	public String longitude;
	
	public String name;
	
	public Store() {}
	
	public Store(JSONObject obj) throws JSONException {
		id = obj.getString("id");
		img = obj.getString("img");
		name = obj.optString("name");
		address = obj.getString("address");
		tel = obj.getString("tel");
		latitude = obj.getString("latitude");
		longitude = obj.getString("longitude");
	}

}
