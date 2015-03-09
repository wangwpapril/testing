package com.cuc.miti.phone.xmc.domain;

//�ص�
//@author GuanWei

public class Place {
	//	����
	private String p_id;
	//	�ص���
	private String code;
	//	�ص����
	private String name;
	//	����
	private String language;
	//����
	private String desc;
	//ƴ��(������ƴ����Ӣ�ľ���ԭ��)
	private String pinyin;
	//��������ĸ
	private String headchar;
	
	public Place(){}
	
	
	
	
	public Place(String pId, String code, String name, String language,
			String desc,String pinyin,String headchar) {
		super();
		p_id = pId;
		this.code = code;
		this.name = name;
		this.language = language;
		this.desc = desc;
		this.pinyin = pinyin;
		this.headchar = headchar;
	}

	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getP_id() {
		return p_id;
	}
	public void setP_id(String p_id) {
		this.p_id = p_id;
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
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}	public String getHeadchar() {
		return headchar;
	}
	public void setHeadchar(String headchar) {
		this.headchar = headchar;
	}

	@Override
	public String toString() {
		return "Place [code=" + code + ", desc=" + desc + ", language=" 
				+ language + ", headchar=" + headchar + ", name=" + name + ", pinyin=" + pinyin + ", p_id=" + p_id + "]";
	}
}
