package com.cuc.miti.phone.xmc.domain;

public class Language {
	
//	id
	private String l_id;
//	������
	private String code;
//	�������
	private String name;
//	����
	private String language;
	
//����
	private String order;

	public String getL_id() {
		return l_id;
	}

	public void setL_id(String lId) {
		l_id = lId;
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

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Language(String lId, String code, String name, String language,
			String order) {
		super();
		l_id = lId;
		this.code = code;
		this.name = name;
		this.language = language;
		this.order = order;
	}
	
	public Language(){
		this.code = "";
		this.name = "";
		this.language = "";
		this.order = "0";
		this.l_id="0";
	}

	@Override
	public String toString() {
		return "Language [code=" + code + ", l_id=" + l_id + ", language="
				+ language + ", name=" + name + ", order=" + order + "]";
	}
	
	
	
}
