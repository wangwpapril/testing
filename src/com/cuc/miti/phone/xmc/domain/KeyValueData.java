package com.cuc.miti.phone.xmc.domain;

public class KeyValueData {
	
	private String key;
	
	private String value;

	
	public KeyValueData(){}


	public String getKey() {
		return key;
	}


	public void setKey(String key) {
		this.key = key;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public KeyValueData(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if(o == null){return false;}
		
		return this.key.equals(((KeyValueData)o).key);
	}


	
	
}
