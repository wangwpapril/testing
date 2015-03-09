package com.cuc.miti.phone.xmc.domain;

//���ȼ�
//@author GuanWei

public class NewsPriority {
//	����
	private String np_id;
//	�������ȼ����
	private String code;
//	�������ȼ����
	private String name;
//	����
	private String language;
	
	
	public NewsPriority() {}
	
	
	public NewsPriority(String npId, String code, String name, String language) {
		super();
		np_id = npId;
		this.code = code;
		this.name = name;
		this.language = language;
	}


	public String getNp_id() {
		return np_id;
	}
	public void setNp_id(String np_id) {
		this.np_id = np_id;
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
		return "NewsPriority [code=" + code + ", language=" + language
				+ ", name=" + name + ", np_id=" + np_id + "]";
	}
	
}
