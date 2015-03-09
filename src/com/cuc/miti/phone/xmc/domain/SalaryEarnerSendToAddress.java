package com.cuc.miti.phone.xmc.domain;

//�û���ַ���������͵�ַ
//@author GuanWei
public class SalaryEarnerSendToAddress {
	//	����
	private int ssta_id;
	//	�����ַ����
	private String code;
	//	��¼��
	private String loginname;
	//	�����ַ��
	private String name;
	//	����
	private String language;
	//����
	private String order;
	
    public int getSsta_id() {
		return ssta_id;
	}
	public void setSsta_id(int ssta_id) {
		this.ssta_id = ssta_id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
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
	
	public SalaryEarnerSendToAddress(){}
	public SalaryEarnerSendToAddress(int sstaId, String code, String loginname,
			String name, String language, String order) {
		this.ssta_id = sstaId;
		this.code = code;
		this.loginname = loginname;
		this.name = name;
		this.language = language;
		this.order = order;
	}
	@Override
	public String toString() {
		return "SalaryEarnerSendToAddress [code=" + code + ", language="
				+ language + ", loginname=" + loginname + ", name=" + name
				+ ", order=" + order + ", ssta_id=" + ssta_id + "]";
	}
	
	
	
	
}
