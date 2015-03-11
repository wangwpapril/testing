package com.cuc.miti.phone.xmc.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Product implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final String P_ACTION_NO_FARE = "0";
	public static final String P_ACTION_NO_FANQUAN = "1";
	public static final String P_ACTION_NO_TEJIA = "2";
	
	public String id;
	
	public String name;
	
	public String imgUrl;
	
	// 商品的一些特性:0免运费，1返券，2特价   "0,1"
	public String action;
	
	public String marketPrice;
	
	public String lePrice;
	
	public int count; // 次商品在购物车中的件数
	
	public Product() {}
	
	public Product(JSONObject obj) throws JSONException {
		id = obj.getString("id");
		name = obj.getString("name");
		imgUrl = obj.getString("imgUrl");
		action = obj.optString("action");
		marketPrice = obj.getString("marketPrice");
		lePrice = obj.getString("lePrice");
		
		count = obj.optInt("quantity");
	}

}
