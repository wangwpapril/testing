package com.cuc.miti.phone.xmc.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.cuc.miti.phone.xmc.domain.Enums.XmcBool;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.TimeFormatHelper;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

//��ǩģ��
//@author GuanWei

public class ManuscriptTemplate implements Parcelable {
	// ����
	private String mt_id;
	// ģ����
	private String name;
	// ��¼��
	private String loginname;
	// ��Դ
	private String comefromDept;
	// ��Դid
	private String comefromDeptID;
	// ������
	private String region;
	// ����id
	private String regionID;
	// �������
	private String doctype;
	// �������id
	private String doctypeID;
	// �������
	private String provtype;
	// ����id
	private String provtypeID;
	// �ؼ���
	private String keywords;
	// ����
	private String language;
	// ����id
	private String languageID;
	// ���ȼ�
	private String priority;
	// ���ȼ�ID
	private String priorityID;
	// ����ص�
	private String sendarea;
	// �·��ص�
	private String happenplace;
	// �����ص�
	private String reportplace;
	// �����ַ
	private String address;
	// �����ַid
	private String addressID;
	// �Ƿ�3T���
	private XmcBool is3Tnews;
	// �Ƿ�Ĭ�ϸ�ǩ
	private XmcBool isdefault;
	// ����ʱ��
	private String createtime;
	// �������
	private String reviewstatus;
	// ���Ĭ�ϱ���
	private String defaulttitle;
	// ���Ĭ������
	private String defaultcontents;
	// �Ƿ�ϵͳĬ��ģ��
	private String issystemoriginal;
	// ����
	private String author;

	public ManuscriptTemplate() {
		mt_id = "";
		this.name = "";
		this.loginname = "";
		this.comefromDept = "";
		this.comefromDeptID = "";
		this.region = "";
		this.regionID = "";
		this.doctype = "";
		this.doctypeID = "";
		this.provtype = "";
		this.provtypeID = "";
		this.keywords = "";
		this.language = "";
		this.languageID = "";
		this.priority = "";
		this.priorityID = "";
		this.sendarea = "";
		this.happenplace = "";
		this.reportplace = "";
		this.address = "";
		this.addressID = "";
		this.is3Tnews = XmcBool.False;
		this.isdefault = XmcBool.False;
		this.createtime = TimeFormatHelper.getFormatTime(new Date());
		this.reviewstatus = "";
		this.defaulttitle = "";
		this.defaultcontents = "";
		this.issystemoriginal = "";
		this.author = "";
	}

	/**
	 * ���û���½ʱ���½�ϵͳĬ�ϵ�4����Ѷר�ù��캯��
	 * 
	 * @param loginName
	 *            ��½��
	 * @param issystemoriginal
	 *            ��Ѷ����(PICTURE/WORD/VIDEO/VOICE)
	 * @param name
	 *            ��Ѷ���
	 */
	public ManuscriptTemplate(String loginName, String issystemoriginal,
			String name) {
		mt_id = UUID.randomUUID().toString();
		this.name = name;
		this.loginname = loginName;
		this.comefromDept = "";
		this.comefromDeptID = "";
		this.region = "";
		this.regionID = "";
		this.doctype = "";
		this.doctypeID = "";
		this.provtype = "";
		this.provtypeID = "";
		this.keywords = "";
		this.language = "";
		this.languageID = "";
		this.priority = "";
		this.priorityID = "";
		this.sendarea = "";
		this.happenplace = "";
		this.reportplace = "";
		this.address = "";
		this.addressID = "";
		this.is3Tnews = XmcBool.False;
		this.isdefault = XmcBool.False;
		this.createtime = TimeFormatHelper.getFormatTime(new Date());
		this.reviewstatus = "";
		this.defaulttitle = "";
		this.defaultcontents = "";
		this.issystemoriginal = issystemoriginal;
		this.author = "";
	}

	public ManuscriptTemplate(String mtId, String name, String loginname,
			String comefromDept, String comefromDeptID, String region,
			String regionID, String doctype, String doctypeID, String provtype,
			String provtypeID, String keywords, String language,
			String languageID, String priority, String priorityID,
			String sendarea, String happenplace, String reportplace,
			String address, String addressID, XmcBool is3Tnews,
			XmcBool isdefault, String createtime, String reviewstatus,
			String defaulttitle, String defaultcontents,
			String issystemoriginal, String author) {
		super();
		this.mt_id = mtId;
		this.name = name;
		this.loginname = loginname;
		this.comefromDept = comefromDept;
		this.comefromDeptID = comefromDeptID;
		this.region = region;
		this.regionID = regionID;
		this.doctype = doctype;
		this.doctypeID = doctypeID;
		this.provtype = provtype;
		this.provtypeID = provtypeID;
		this.keywords = keywords;
		this.language = language;
		this.languageID = languageID;
		this.priority = priority;
		this.priorityID = priorityID;
		this.sendarea = sendarea;
		this.happenplace = happenplace;
		this.reportplace = reportplace;
		this.address = address;
		this.addressID = addressID;
		this.is3Tnews = is3Tnews;
		this.isdefault = isdefault;
		this.createtime = createtime;
		this.reviewstatus = reviewstatus;
		this.defaulttitle = defaulttitle;
		this.defaultcontents = defaultcontents;
		this.issystemoriginal = issystemoriginal;
		this.author = author;
	}

	public String getMt_id() {
		return mt_id;
	}

	public void setMt_id(String mt_id) {
		this.mt_id = mt_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getComefromDept() {
		return comefromDept;
	}

	public void setComefromDept(String comefromDept) {
		this.comefromDept = comefromDept;
	}

	public String getComefromDeptID() {
		return comefromDeptID;
	}

	public void setComefromDeptID(String comefromDeptID) {
		this.comefromDeptID = comefromDeptID;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getRegionID() {
		return regionID;
	}

	public void setRegionID(String regionID) {
		this.regionID = regionID;
	}

	public String getDoctype() {
		return doctype;
	}

	public void setDoctype(String doctype) {
		this.doctype = doctype;
	}

	public String getDoctypeID() {
		return doctypeID;
	}

	public void setDoctypeID(String doctypeID) {
		this.doctypeID = doctypeID;
	}

	public String getProvtype() {
		return provtype;
	}

	public void setProvtype(String provtype) {
		this.provtype = provtype;
	}

	public String getProvtypeID() {
		return provtypeID;
	}

	public void setProvtypeID(String provtypeID) {
		this.provtypeID = provtypeID;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLanguageID() {
		return languageID;
	}

	public void setLanguageID(String languageID) {
		this.languageID = languageID;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getPriorityID() {
		return priorityID;
	}

	public void setPriorityID(String priorityID) {
		this.priorityID = priorityID;
	}

	public String getSendarea() {
		return sendarea;
	}

	public void setSendarea(String sendarea) {
		this.sendarea = sendarea;
	}

	public String getHappenplace() {
		return happenplace;
	}

	public void setHappenplace(String happenplace) {
		this.happenplace = happenplace;
	}

	public String getReportplace() {
		return reportplace;
	}

	public void setReportplace(String reportplace) {
		this.reportplace = reportplace;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddressID() {
		return addressID;
	}

	public void setAddressID(String addressID) {
		this.addressID = addressID;
	}

	public XmcBool getIs3Tnews() {
		return is3Tnews;
	}

	public void setIs3Tnews(XmcBool is3Tnews) {
		this.is3Tnews = is3Tnews;
	}

	public XmcBool getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(XmcBool isdefault) {
		this.isdefault = isdefault;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getReviewstatus() {
		return reviewstatus;
	}

	public void setReviewstatus(String reviewstatus) {
		this.reviewstatus = reviewstatus;
	}

	public String getDefaulttitle() {
		return defaulttitle;
	}

	public void setDefaulttitle(String defaulttitle) {
		this.defaulttitle = defaulttitle;
	}

	public String getDefaultcontents() {
		return defaultcontents;
	}

	public void setDefaultcontents(String defaultcontents) {
		this.defaultcontents = defaultcontents;
	}

	public String getIssystemoriginal() {
		return issystemoriginal;
	}

	public void setIssystemoriginal(String issystemoriginal) {
		this.issystemoriginal = issystemoriginal;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	/**
	 * ��¡����
	 */
	@Override
	public ManuscriptTemplate clone()  {	
		try {
			return (ManuscriptTemplate)super.clone();
		} catch (CloneNotSupportedException e) {
			Logger.e(e);
			return null;
		}
	}

	@Override
	public String toString() {
		return "ManuscriptTemplate [address=" + address + ", addressID="
				+ addressID + ", author=" + author + ", comefromDept="
				+ comefromDept + ", comefromDeptID=" + comefromDeptID
				+ ", createtime=" + createtime + ", defaultcontents="
				+ defaultcontents + ", defaulttitle=" + defaulttitle
				+ ", doctype=" + doctype + ", doctypeID=" + doctypeID
				+ ", happenplace=" + happenplace + ", is3Tnews=" + is3Tnews
				+ ", isdefault=" + isdefault + ", issystemoriginal="
				+ issystemoriginal + ", keywords=" + keywords + ", language="
				+ language + ", languageID=" + languageID + ", loginname="
				+ loginname + ", mt_id=" + mt_id + ", name=" + name
				+ ", priority=" + priority + ", priorityID=" + priorityID
				+ ", provtype=" + provtype + ", provtypeID=" + provtypeID
				+ ", region=" + region + ", regionID=" + regionID
				+ ", reportplace=" + reportplace + ", reviewstatus="
				+ reviewstatus + ", sendarea=" + sendarea + "]";
	}

	/**
	 * Intent�䴫�ݶ���
	 * 
	 * @param dest
	 * @param arg1
	 */
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeString(this.mt_id);
		dest.writeString(this.address);
		dest.writeString(this.addressID);
		dest.writeString(this.author);
		dest.writeString(this.comefromDept);
		dest.writeString(this.comefromDeptID);
		dest.writeString(this.createtime);
		dest.writeString(this.defaultcontents);
		dest.writeString(this.defaulttitle);
		dest.writeString(this.doctype);
		dest.writeString(this.doctypeID);
		dest.writeString(this.happenplace);
		dest.writeString(this.is3Tnews.getValue());
		dest.writeString(this.isdefault.getValue());
		dest.writeString(this.issystemoriginal);
		dest.writeString(this.keywords);
		dest.writeString(this.language);
		dest.writeString(this.languageID);
		dest.writeString(this.loginname);
		dest.writeString(this.mt_id);
		dest.writeString(this.name);
		dest.writeString(this.priority);
		dest.writeString(this.priorityID);
		dest.writeString(this.provtype);
		dest.writeString(this.provtypeID);
		dest.writeString(this.region);
		dest.writeString(this.regionID);
		dest.writeString(this.reportplace);
		dest.writeString(this.reviewstatus);
		dest.writeString(this.sendarea);
	}

	public static final Parcelable.Creator<ManuscriptTemplate> CREATOR = new Creator<ManuscriptTemplate>() {
		public ManuscriptTemplate[] newArray(int size) {

			return new ManuscriptTemplate[size];
		}

		public ManuscriptTemplate createFromParcel(Parcel source) {
			ManuscriptTemplate temp = new ManuscriptTemplate();

			temp.mt_id = source.readString();
			temp.address = source.readString();
			temp.addressID = source.readString();
			temp.author = source.readString();
			temp.comefromDept = source.readString();
			temp.comefromDeptID = source.readString();
			temp.createtime = source.readString();
			temp.defaultcontents = source.readString();
			temp.defaulttitle = source.readString();
			temp.doctype = source.readString();
			temp.doctypeID = source.readString();
			temp.happenplace = source.readString();
			temp.is3Tnews = XmcBool.parseFromValue(source.readString());
			temp.isdefault = XmcBool.parseFromValue(source.readString());
			temp.issystemoriginal = source.readString();
			temp.keywords = source.readString();
			temp.language = source.readString();
			temp.languageID = source.readString();
			temp.loginname = source.readString();
			temp.mt_id = source.readString();
			temp.name = source.readString();
			temp.priority = source.readString();
			temp.priorityID = source.readString();
			temp.provtype = source.readString();
			temp.provtypeID = source.readString();
			temp.region = source.readString();
			temp.regionID = source.readString();
			temp.reportplace = source.readString();
			temp.reviewstatus = source.readString();
			temp.sendarea = source.readString();
			return temp;
		}
	};

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void copyTo(ManuscriptTemplate template) {

		if(template == null)
			return;
		
		template.setAddress(this.getAddress());
		template.setAddressID(this.getAddressID());
		template.setAuthor(this.getAuthor());
		template.setComefromDept(this.getComefromDept());
		template.setComefromDeptID(this.getComefromDeptID());
		template.setCreatetime(this.getCreatetime());
		template.setDefaultcontents(this.getDefaultcontents());
		template.setDefaulttitle(this.getDefaulttitle());
		template.setDoctype(this.getDoctype());
		template.setDoctypeID(this.getDoctypeID());
		template.setHappenplace(this.getHappenplace());
		template.setIs3Tnews(this.getIs3Tnews());
		template.setIsdefault(this.getIsdefault());
		template.setIssystemoriginal(this.getIssystemoriginal());
		template.setKeywords(this.getKeywords());
		template.setLanguage(this.getLanguage());
		template.setLanguageID(this.getLanguageID());
		template.setLoginname(this.getLoginname());
		template.setMt_id(this.getMt_id());
		template.setName(this.getName());
		template.setPriority(this.getPriority());
		template.setPriorityID(this.getPriorityID());
		template.setProvtype(this.getProvtype());
		template.setProvtypeID(this.getProvtypeID());
		template.setRegion(this.getRegion());
		template.setRegionID(this.getRegionID());
		template.setReportplace(this.getReportplace());
		template.setReviewstatus(this.getReviewstatus());
		template.setSendarea(this.getSendarea());
	}

}
