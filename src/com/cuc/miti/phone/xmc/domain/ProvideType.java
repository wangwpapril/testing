package com.cuc.miti.phone.xmc.domain;

//�������
//@author GuanWei

public class ProvideType {
//	����
	private String pt_id;
//	���������
	private String code;
//	����������
	private String name;
//	����
	private String language;
	
	
	public ProvideType(){}
	
	public ProvideType(String ptId, String code, String name, String language){
		super();
		pt_id = ptId;
		this.code = code;
		this.name = name;
		this.language = language;
	}

	public String getPt_id() {
		return pt_id;
	}
	
	public void setPt_id(String pt_id) {
		this.pt_id = pt_id;
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
		return "ProvideType [code=" + code + ", language=" + language
				+ ", name=" + name + ", pt_id=" + pt_id + "]";
	}

}
