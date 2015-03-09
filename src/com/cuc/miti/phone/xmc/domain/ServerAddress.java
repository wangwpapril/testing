package com.cuc.miti.phone.xmc.domain;

//��������ַ
//@author GuanWei

public class ServerAddress {
//	����
	private int sa_id;
//	�ո������ip��ַ
	private String code;
//	�ո���������
	private String name;
//	����
	private String language;
//����	
	private String order;
	
	
	
	public ServerAddress(){}
	
	
	public ServerAddress(int saId, String code, String name, String language,
		String order) {
	super();
	sa_id = saId;
	this.code = code;
	this.name = name;
	this.language = language;
	this.order = order;
}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}

	
	

	public int getSa_id() {
		return sa_id;
	}
	
	public void setSa_id(int sa_id) {
		this.sa_id = sa_id;
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
		return "ServerAddress [code=" + code + ", language=" + language
				+ ", name=" + name + ", sa_id=" + sa_id + "]";
	}


}
