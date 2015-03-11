package com.cuc.miti.phone.xmc.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Area implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String id;
	
	public String name;
	
	public String parentId;
	
	public Area() {}
	
	public Area(JSONObject obj) throws JSONException {
		id = obj.getString("id");
		parentId = obj.getString("parentid");
		name = obj.getString("name");
	}
	
    @Override
    public boolean equals(Object obj) {
    	Area p = (Area)obj;
        return this.id.equals(p.id)?true:false;
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
