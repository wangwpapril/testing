package com.cuc.miti.phone.xmc.domain;

public class ComeFromAddress {
	
	
	//	id
	private String ca_id;
	//	������
	private String code;
	//	�������
	private String name;
	//	����
	private String language;
	//����
	private String desc;
	//ƴ��(������ƴ����Ӣ�ľ���ԭ��)
	private String pinyin;
	//���ֵ�����ĸ
	private String headchar;
	
	public String getCa_id() {
		return ca_id;
	}
	public void setCa_id(String caId) {
		ca_id = caId;
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
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
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

	public ComeFromAddress(String caId, String code, String name,
			String language, String desc,String pinyin,String headchar) {
		super();
		ca_id = caId;
		this.code = code;
		this.name = name;
		this.language = language;
		this.desc = desc;
		this.pinyin= pinyin;
		this.headchar = headchar;
	}
	
	public ComeFromAddress(){}

	@Override
	public String toString() {
		return "ComeFromAddress [ca_id=" + ca_id + ", code=" + code + ", desc="
				
				+ desc + ", pinyin=" + pinyin +  ", headchar=" + headchar +  ", language=" + language + ", name=" + name + "]";
	}
	
}
