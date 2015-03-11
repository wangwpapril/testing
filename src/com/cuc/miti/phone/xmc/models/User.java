package com.cuc.miti.phone.xmc.models;

import java.io.Serializable;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String id;

	public String name;

	public String password;

	public String accessToken;

	public boolean isLogin;
	
	public boolean isAutoLogin;
	
	public String phoneNumber;

}
