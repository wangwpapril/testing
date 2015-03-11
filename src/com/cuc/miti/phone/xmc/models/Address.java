package com.cuc.miti.phone.xmc.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Address implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String id;
	
	public String sId;
	
	public String cId;
	
	public String aId;
	
	public String name;
	
	public String tel;
	
	public String mobilePhone;
	
	public String addressDesc;
	
	public String zipCode;
	
	public boolean isAction;
	
	public Address(String id, String name, String phone, String desc,
			String sId, String cId, String aId, String zipCode, boolean isAction) {
		this.id = id;
		this.sId = sId;
		this.cId = cId;
		this.aId = aId;
		this.name = name;
		this.mobilePhone = phone;
		this.addressDesc = desc;
		this.zipCode = zipCode;
		this.isAction = isAction;
	}
	
	public Address(JSONObject obj) throws JSONException {
		id = obj.getString("id");
		sId = obj.getString("sid");
		cId = obj.getString("cid");
		aId = obj.getString("aid");
		name = obj.getString("name");
		mobilePhone = obj.getString("mobilePhone");
		addressDesc = obj.getString("addressDesc");
		isAction = obj.getBoolean("isAction");
	}

}
