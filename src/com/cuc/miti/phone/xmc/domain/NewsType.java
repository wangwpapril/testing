package com.cuc.miti.phone.xmc.domain;

//�������
//@author GuanWei

public class NewsType {
//	id
	private String nt_id;
//	������ͱ���
	private String code;
//	����������
	private String name;
//	����
	private String language;
	
	public NewsType() {}
	
	

	public NewsType(String ntId, String code, String name, String language) {
		super();
		nt_id = ntId;
		this.code = code;
		this.name = name;
		this.language = language;
	}



	public String getNt_id() {
		return nt_id;
	}



	public void setNt_id(String ntId) {
		nt_id = ntId;
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
		return "NewsType [code=" + code + ", language=" + language + ", name="
				+ name + ", nt_id=" + nt_id + "]";
	}

}
