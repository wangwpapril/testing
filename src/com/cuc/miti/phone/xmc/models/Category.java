package com.cuc.miti.phone.xmc.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Category implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String id;
	
	public String name;
	
	public String imgUrl;
	
	public String bCategoryId;
	
	// 所属类别:0 默认类别,1品牌类别
	public String type;
	
	public Category(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Category(JSONObject obj) throws JSONException {
		id = obj.getString("id");
		name = obj.getString("name");
		imgUrl = obj.getString("image");
	}

}
