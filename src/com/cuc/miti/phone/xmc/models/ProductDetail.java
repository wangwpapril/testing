package com.cuc.miti.phone.xmc.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductDetail extends Product {

	private static final long serialVersionUID = 1L;
	
	public int hasCount;
	
	public int integral; // 对应的积分
	
	public String couponMsg; // 对应的优惠信息
	
	public String favoComment; // 好评率
	
	public int questionCount; // 疑难问答
	
	public String description; // 商品介绍
	
	public boolean isFollowed; // 是否已经关注
	
	public boolean isClothing; // 是否是服装类别
	
	public String[] imgs; // 图片列表
	
	public String size;
	
	public String color;
	
	public ProductDetail() {}
	
	public ProductDetail(JSONObject obj) throws JSONException {
		super(obj);
		
		hasCount = obj.optInt("hasCount");
		integral = obj.getInt("integral");
		couponMsg = obj.optString("couponMsg");
		favoComment = obj.optString("favoComment");
		questionCount = obj.optInt("questionCount");
		isFollowed = obj.optBoolean("isFollow");
		isClothing = obj.optBoolean("isClothing");
		description = obj.optString("description");
		size = obj.optString("size");
		color = obj.optString("color");
		
		JSONArray array = obj.optJSONArray("imgs");
		if (array != null && array.length() > 0) {
			int len = array.length();
			imgs = new String[len];
			for (int i = 0; i < len; i ++) {
				imgs[i] = array.getString(i);
			}
		}
	}

}
