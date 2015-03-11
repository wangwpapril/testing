package com.cuc.miti.phone.xmc.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SendType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String shiptypeId;
	public String name;
	public double yunfei;
	public String description;
	public String wapUrl;
	
	public List<PayType> payList;
	
	public SendType(String name){
		this.name = name;
	}
	
	public SendType(JSONObject obj) throws JSONException {
		shiptypeId = obj.getString("shipId");
		name = obj.getString("shipName");
		yunfei = obj.optDouble("shipMoney");
		
		description = obj.optString("description");
		wapUrl = obj.optString("wapUrl");
		
		if (obj.has("payMethod")) {
			JSONArray array = obj.getJSONArray("payMethod");
			int len = array.length();
			
			if (len > 0) {
				payList = new ArrayList<PayType>(len);
				for (int i = 0; i < len; i ++) {
					payList.add(new PayType(array.getJSONObject(i)));
				}
			}
		}
	}
	
	

}
