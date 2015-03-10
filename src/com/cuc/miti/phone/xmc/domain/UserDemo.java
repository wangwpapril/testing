package com.cuc.miti.phone.xmc.domain;

import java.io.Serializable;

import com.google.mitijson.annotations.SerializedName;

public class UserDemo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3466675892389991082L;
	
	@SerializedName("email")
	private String email;
	
	@SerializedName("password")
	private String psw;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPsw() {
		return psw;
	}

	public void setPsw(String psw) {
		this.psw = psw;
	}

	
}