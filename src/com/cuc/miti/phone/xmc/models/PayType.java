package com.cuc.miti.phone.xmc.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

public class PayType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String name;
	public String paytypeId;
	
	public PayType(String name){
		this.name = name;
	}
	
	public PayType(JSONObject obj) throws JSONException {
		paytypeId = obj.optString("paytypeId");
		if (TextUtils.isEmpty(paytypeId))
			paytypeId = obj.optString("payMethodCode");
		
		name = obj.optString("name");
		if (TextUtils.isEmpty(name))
			name = obj.optString("payMethodName");
	}

}
