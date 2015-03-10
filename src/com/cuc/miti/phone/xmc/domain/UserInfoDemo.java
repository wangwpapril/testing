package com.cuc.miti.phone.xmc.domain;

import java.io.Serializable;

import com.google.mitijson.annotations.SerializedName;

public class UserInfoDemo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6549582935618961748L;
	
	@SerializedName("user")
	private UserDemo user;

	public UserDemo getUser() {
		return user;
	}

	public void setUser(UserDemo user) {
		this.user = user;
	}
	
}