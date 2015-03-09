package com.cuc.miti.phone.xmc.domain;

public class SpinnerItem{

	private String key;
	private String value;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String toString(){
		return value;
	}
	
	public SpinnerItem(String key,String value){
		this.key = key;
		this.value = value;
	}
	
}
