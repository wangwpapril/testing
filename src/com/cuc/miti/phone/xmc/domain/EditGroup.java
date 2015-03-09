package com.cuc.miti.phone.xmc.domain;

//�ؼ��
//@author llna

public class EditGroup {
//	id
	private String eg_id;
//	�����
	private String code;
//	�����
	private String name;
//	����
	private String language;
	
	public EditGroup() {}
	
	

	public EditGroup(String eg_id, String code, String name, String language) {
		super();
		this.eg_id = eg_id;
		this.code = code;
		this.name = name;
		this.language = language;
	}



	



	


	public String getEg_id() {
		return eg_id;
	}



	public void setEg_id(String egId) {
		eg_id = egId;
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
				+ name + ", eg_id=" + eg_id + "]";
	}

}
