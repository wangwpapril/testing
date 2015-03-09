package com.cuc.miti.phone.xmc.domain;

/**
 * �û�ʵ����
 * @author SongQing
 *
 */
public class User {

	/**
	 * ����
	 */
	private int u_id;
	/**
	 * �û���
	 */
	private String username;
	/**
	 * ����
	 */
	private String password;
	/**
	 * �û���Ϣ
	 */
	private UserAttribute userattribute;
	/**
	 * @return the u_id
	 */
	public int getU_id() {
		return u_id;
	}
	/**
	 * @param u_id the u_id to set
	 */
	public void setU_id(int u_id) {
		this.u_id = u_id;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the userattribute
	 */
	public UserAttribute getUserattribute() {
		return userattribute;
	}
	/**
	 * @param userattribute the userattribute to set
	 */
	public void setUserattribute(UserAttribute userattribute) {
		this.userattribute = userattribute;
	}
	@Override
	public String toString() {
		return "User [password=" + password + ", u_id=" + u_id
				+ ", userattribute=" + userattribute + ", username=" + username
				+ "]";
	}
	
	
	
	
}
