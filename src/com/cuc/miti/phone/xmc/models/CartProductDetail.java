package com.cuc.miti.phone.xmc.models;

import org.json.JSONException;
import org.json.JSONObject;

public class CartProductDetail extends ProductDetail {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int count;
	
	public boolean isPromotion;
	
	public int gift;
	
	public CartProductDetail(JSONObject obj) throws JSONException {
		super(obj);
		gift = obj.optInt("gift");
		count = obj.getInt("count");
		isPromotion = obj.getBoolean("isPromotion");
	}
	
	public CartProductDetail() {
	}
	
}
