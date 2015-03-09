package com.cuc.miti.phone.xmc.domain;

//�û���ַ������3T�����ַ
//@author GuanWei

public class EmployeeSendTo3TAddress {
	//	����
	private int es3_id;
	//	�����ַ����
	private String code;
	//	��¼��
	private String loginname;
	//��ַ��
	private String name;
	//����
	private String order;
	//�����ַ��������
	private String language;
	
	public EmployeeSendTo3TAddress(){}
	public EmployeeSendTo3TAddress(int es3Id, String name,String language,String code, String loginname,
			String order) {
		this.es3_id = es3Id;
		this.code = code;
		this.loginname = loginname;
		this.order = order;
		this.name = name;
		this.setLanguage(language);
	}

	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public int getEs3_id() {
		return es3_id;
	}
	public void setEs3_id(int es3_id) {
		this.es3_id = es3_id;
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
	@Override
	public String toString() {
		return "EmployeeSendTo3TAddress [code=" + code + ", es3_id=" + es3_id + ", name=" + name
				+ ", language=" + language + ", loginname=" + loginname + ", order=" + order + "]";
	}

	



	
	

}
