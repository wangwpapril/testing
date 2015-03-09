package com.cuc.miti.phone.xmc.domain;

public class UserBean {
	private String userName;
	private String name;
	private String email;
	private String jid;
	
	public UserBean(){};
	public UserBean(String userName,String name,String email,String jid){
		this.userName = userName;
		this.name = name;
		this.email = email;
		this.jid=jid;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getJid() {
		return jid;
	}
	public void setJid(String jid) {
		this.jid = jid;
	}
	
}
