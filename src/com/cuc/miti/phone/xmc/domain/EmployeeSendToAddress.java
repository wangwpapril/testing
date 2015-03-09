package com.cuc.miti.phone.xmc.domain;

//�û���ַ������δ���巢���ַ
//@author GuanWei

public class EmployeeSendToAddress {
	//	����
	private int esa_id;
	//	�����ַ����
	private String code;
	//	��¼��
	private String loginname;
	//����
	private String order;
	//���
	private String name;
	//�����ַ��������
	private String language;
	//ƴ��(������ƴ����Ӣ�ľ���ԭ��)
	private String pinyin;
	//��������ĸ
	private String headchar;

	public EmployeeSendToAddress()
	{}
	
	
	
	public EmployeeSendToAddress(int esaId, String name,String language,String code, String loginname,
		String order,String pinyin,String headchar) {
		this.esa_id = esaId;
		this.code = code;
		this.loginname = loginname;
		this.order = order;
		this.name = name;
		this.language = language;
		this.pinyin= pinyin;
		this.headchar = headchar;
}
	public int getEsa_id() {
		return esa_id;
	}
	public void setEsa_id(int esa_id) {
		this.esa_id = esa_id;
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
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public String getHeadchar() {
		return headchar;
	}
	public void setHeadchar(String headchar) {
		this.headchar = headchar;
	}

	@Override
	public String toString() {
		return "EmployeeSendToAddress [code=" + code + ", esa_id=" + esa_id  + ", name=" + name
				+ ", language=" + language + ", loginname=" + loginname + ", pinyin=" + pinyin  + ", headchar=" + headchar + ", order=" + order + "]";
	}
}
