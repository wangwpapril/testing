package com.cuc.miti.phone.xmc.http;

/*
 * ����
 */
public class PostParameter {

	private String name;
	private Object object;
	
	public PostParameter(String name,Object object){
		this.name=name;
		this.object=object;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	
	
}
