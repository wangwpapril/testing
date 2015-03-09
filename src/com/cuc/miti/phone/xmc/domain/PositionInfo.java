package com.cuc.miti.phone.xmc.domain;

/**
 * λ�ö���
 * @author SongQing
 *
 */
public class PositionInfo {

	private double latitude ;			//γ��
	private double longitude;		//����
	private double altitude;			//�߶�
	private String address;				//�ص����
	
	/**
	 * ���캯��
	 */
	public PositionInfo(){
		this.latitude = 0;
		this.longitude = 0;
		this.altitude = 0;
		this.address = "";
	}
	
	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
}
