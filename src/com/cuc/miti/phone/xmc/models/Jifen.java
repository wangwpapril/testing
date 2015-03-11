package com.cuc.miti.phone.xmc.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Jifen implements Serializable {
	
	public int count;
	
	public String cardNo;
	
	public String source;
	
	public String startTime;
	
	public String descption;
	
	public Jifen(JSONObject obj) throws JSONException {
		count = obj.getInt("count");
		cardNo = obj.getString("cardNo");
		source = obj.getString("Source");
		startTime = obj.getString("time");
		descption = obj.getString("Descption");
	}

}
