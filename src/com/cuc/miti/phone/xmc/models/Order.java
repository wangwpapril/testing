package com.cuc.miti.phone.xmc.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

public class Order implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String id;
	
	public String orderId;
	
	public String num;
	
	public String tn;
	
	public String totalPrice;
	
	public String createTime;
	
	public String status; // 未支付，已确认，在途
	
	public String consignee;
	
	public String phone;
	
	public String address;
	
	public String shiptypeId;
	
	public String paytypeId;
	
	public String sendTime;
	
	public boolean cancelable;
	
	public String message;
	
	// 用于显示在订单详情
	public String tsorderid; // 订单号
	public String tsorderstatus; // 订单状态;
	public String tstotalmoney; // 总金额
	public String tssendmethod; // 配送方式
	public String tspaymethod; // 支付方式
	
	public Order() {}
	
	public Order(JSONObject obj) throws JSONException {
		tn = obj.optString("tn");
		id = obj.optString("id");
		orderId = obj.optString("orderId");
		num = obj.optString("orderNum");
		totalPrice = obj.optString("totalPrice");
		createTime = obj.optString("createTime");
		status = obj.optString("state");
		if (TextUtils.isEmpty(status)) status = obj.optString("status");
		consignee = obj.optString("consignee");
		phone = obj.optString("phone");
		address = obj.optString("address");
		
		shiptypeId = obj.optString("shiptypeId");
		paytypeId = obj.optString("paytypeId");
		sendTime = obj.optString("sendTime");
		
		cancelable = obj.optBoolean("cancel");
		
		message = obj.optString("message");
		
		tsorderid = obj.optString("tsorderid");
		tsorderstatus = obj.optString("tsorderstatus");
		tstotalmoney = obj.optString("tstotalmoney");
		tssendmethod = obj.optString("tssendmethod");
		tspaymethod = obj.optString("tspaymethod");
	}

}