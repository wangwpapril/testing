package com.cuc.miti.phone.xmc.domain;

//�ܵ�ַ������δ���巢���ַ
//@author GuanWei

public class SendToAddress {
	// ����
	private int sta_id;
	// �����ַ����
	private String code;
	// �����ַ��
	private String name;
	// ����
	private String language;
	// ����
	private String order;
	// ����
	private String type;
	//ƴ��(������ƴ����Ӣ�ľ���ԭ��)
	private String pinyin;
	//��������ĸ
	private String headchar;

	public SendToAddress() {
	}

	public SendToAddress(int staId, String code, String name, String language,
			String order, String type,String pinyin) {
		super();
		sta_id = staId;
		this.code = code;
		this.name = name;
		this.language = language;
		this.order = order;
		this.type = type;
		this.pinyin = pinyin;
	}

	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public int getSta_id() {
		return sta_id;
	}
	public void setSta_id(int sta_id) {
		this.sta_id = sta_id;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
		return "SendToAddress [code=" + code + ", language=" + language
				+ ", name=" + name + ", order=" + order + ", type=" + type
				+ ", pinyin=" + pinyin + ", headchar=" + headchar
				+ ", sta_id=" + sta_id + "]";
	}

}
