package com.cuc.miti.phone.xmc.domain;

//�ؼ��
//@author llna

public class Keywords {
	//	id
	private String k_id;
	//	�����
	private String code;
	//	�����
	private String name;
	//	����
	private String language;
	//ƴ��(������ƴ����Ӣ�ľ���ԭ��)
	private String pinyin;
	//��������ĸ
	private String headchar;
	
	public Keywords() {}

	public Keywords(String k_id, String code, String name, String language,String pinyin,String headchar) {
		super();
		this.k_id = k_id;
		this.code = code;
		this.name = name;
		this.language = language;
		this.pinyin = pinyin;
		this.setHeadchar(headchar);
	}

	public String getK_id() {
		return k_id;
	}
	public void setK_id(String kId) {
		k_id = kId;
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
	}
	public String getHeadchar() {
		return headchar;
	}
	public void setHeadchar(String headchar) {
		this.headchar = headchar;
	}

	@Override
	public String toString() {
		return "NewsType [code=" + code + ", language=" + language  + ", headchar=" + headchar + ", name="
				+ name + ", k_id=" + k_id + "]";
	}
}
