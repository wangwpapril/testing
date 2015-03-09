package com.cuc.miti.phone.xmc.domain;

public class LocationDomain {
	String loginname;
	String sss;
	String method;
	double latitude;
	double longtitude;
	double depth;
	String location;
	String memo;
	String source;

	public LocationDomain(String loginname, String sss, String method,
			double latitude, double longtitude, double depth, String location,
			String memo, String source) {
		super();
		this.loginname = loginname;
		this.sss = sss;
		this.method = method;
		this.latitude = latitude;
		this.longtitude = longtitude;
		this.depth = depth;
		this.location = location;
		this.memo = memo;
		this.source = source;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getSss() {
		return sss;
	}

	public void setSss(String sss) {
		this.sss = sss;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}

	public double getDepth() {
		return depth;
	}

	public void setDepth(double depth) {
		this.depth = depth;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
