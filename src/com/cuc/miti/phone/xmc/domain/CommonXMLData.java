package com.cuc.miti.phone.xmc.domain;

//import java.util.Hashtable;
import java.util.List;

public class CommonXMLData {

	private String topics;
	private String name;
	private String deleteFlag;
	private String id;
	private String topicId;
	private String order;
	
	
	
	//llna2012-6-1���
	private List<KeyValueData> languageList;
	private List<KeyValueData> descriptionList ;
		
	public List<KeyValueData> getLanguageList() {
		return languageList;
	}


	public void setLanguageList(List<KeyValueData> languageList) {
		this.languageList = languageList;
	}


	
	
	public List<KeyValueData> getDescriptionList() {
		return descriptionList;
	}


	public void setDescriptionList(List<KeyValueData> descriptionList) {
		this.descriptionList = descriptionList;
	}


	public CommonXMLData(){}
	

	@Override
	public String toString() {
		return "CommonXMLData [deleteFlag=" + deleteFlag + ", descriptionList="
				+ descriptionList + ", id=" + id + ", languageList="
				+ languageList + ", name=" + name + ", topicId=" + topicId
				+ ", topics=" + topics + "]";
	}


	public CommonXMLData(String topics, String name, String deleteFlag,
			String id, String topicId, List<KeyValueData> languageList,String order,
			List<KeyValueData> descriptionList) {
		super();
		this.topics = topics;
		this.name = name;
		this.deleteFlag = deleteFlag;
		this.id = id;
		this.topicId = topicId;
		this.languageList = languageList;
		this.descriptionList = descriptionList;
	}


	/**
	private Hashtable<String, String> languageList;
	public CommonXMLData(String topics, String name, String deleteFlag,
			String id, String topicId, Hashtable<String, String> languageList) {
		super();
		this.topics = topics;
		this.name = name;
		this.deleteFlag = deleteFlag;
		this.id = id;
		this.topicId = topicId;
		this.languageList = languageList;
	}
	
	public Hashtable<String, String> getLanguageList() {
		return languageList;
	}
	public void setLanguageList(Hashtable<String, String> languageList) {
		this.languageList = languageList;
	}


	@Override
	public String toString() {
		return "CommonXMLData [deleteFlag=" + deleteFlag + ", id=" + id
				+ ", languageList=" + languageList + ", name=" + name
				+ ", topicId=" + topicId + ", topics=" + topics + "]";
	}
	
	*/
	
	



	/**
	 * @return the topics
	 */
	public String getTopics() {
		return topics;
	}
	/**
	 * @param topics the topics to set
	 */
	public void setTopics(String topics) {
		this.topics = topics;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the deleteFlag
	 */
	public String getDeleteFlag() {
		return deleteFlag;
	}
	/**
	 * @param deleteFlag the deleteFlag to set
	 */
	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the topicId
	 */
	public String getTopicId() {
		return topicId;
	}
	/**
	 * @param topicId the topicId to set
	 */
	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}


	public String getOrder() {
		return order;
	}


	public void setOrder(String order) {
		this.order = order;
	}


		
	
	
}
