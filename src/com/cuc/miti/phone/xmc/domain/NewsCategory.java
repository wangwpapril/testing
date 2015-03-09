package com.cuc.miti.phone.xmc.domain;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class NewsCategory {	
	private String nc_id;
	//private String topicId;
	//private String deleteFlag;
	//�������ͱ��
	private String code;
	//�������
	private String name;
	//����
	private String language;
	//private Hashtable<String, String> languageList;
	
	private String desc;
	
	public NewsCategory() {}
	
	
	
	
	
	
	public NewsCategory(String ncId, String code, String name, String language,
			String desc) {
		super();
		nc_id = ncId;
		this.code = code;
		this.name = name;
		this.language = language;
		this.desc = desc;
	}






	public String getDesc() {
		return desc;
	}






	public void setDesc(String desc) {
		this.desc = desc;
	}






	public String getNc_id() {
		return nc_id;
	}
	public void setNc_id(String ncId) {
		nc_id = ncId;
	}
	/*public String getTopicId() {
		return topicId;
	}
	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
	public String getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	} 
	public Hashtable<String, String> getLanguageList() {
		return languageList;
	}
	public void setLanguageList(Hashtable<String, String> languageList) {
		this.languageList = languageList;
	}
	
	public List<SpinnerItem> GetListForBind(List<NewsCategory> ncList){
		List<SpinnerItem> sList = null;
		try {
			if(ncList!=null && !ncList.isEmpty()){
				sList = new ArrayList<SpinnerItem>();
				SpinnerItem si = null;
				for(NewsCategory nc : ncList){
					if(nc.getLanguageList().get("en") != null){
						si = new SpinnerItem(nc.topicId.toString(),nc.getLanguageList().get("en").toString());
						sList.add(si);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sList;
	}
*/
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
		return "NewsCategory [code=" + code + ", desc=" + desc + ", language="
				+ language + ", name=" + name + ", nc_id=" + nc_id + "]";
	}
	
	
	
	
}
