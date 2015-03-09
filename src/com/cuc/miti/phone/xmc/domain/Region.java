package com.cuc.miti.phone.xmc.domain;

//����
//@author GuanWei

public class Region {
//	����
	private String r_id;
//	������
	private String code;
//	�������
	private String name;
//	����
	private String language;
	
	public Region(){}
	
	public Region(String rId, String code, String name, String language){
		super();
		r_id = rId;
		this.code = code;
		this.name = name;
		this.language = language;
		
	}
	public String getR_id() {
		return r_id;
	}
	public void setR_id(String r_id) {
		this.r_id = r_id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public String toString() {
		return "Region [code=" + code + ", language=" + language + ", name="
				+ name + ", r_id=" + r_id + "]";
	}

}
