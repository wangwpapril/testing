package com.cuc.miti.phone.xmc.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class BigCategory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String id;
	public String name;
	public String imgUrl;
	public boolean isNew;
	
	public BigCategory() {}
	
	public BigCategory(JSONObject obj) throws JSONException {
		id = obj.getString("id");
		name = obj.getString("name");
		imgUrl = obj.getString("image");
		isNew = obj.getBoolean("isNew");
	}
	
	public BigCategory(String id, String name) {
		this.id = id;
		this.name = name;
	}
}
