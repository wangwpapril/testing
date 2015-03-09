package com.cuc.miti.phone.xmc.domain;

import java.util.UUID;

import java.util.Date;

import android.graphics.Bitmap;

import com.cuc.miti.phone.xmc.domain.Enums.ManuscriptStatus;
import com.cuc.miti.phone.xmc.utils.TimeFormatHelper;

/**
 * �����Ϣ
 * 
 * @author GuanWei
 */

public class Manuscripts {
	// ���id ����
	private String m_id;

	// ������
	private String createid;
	// ǩ�����
	private String releid;
	// �ش����
	private String newsid;
	// �������
	private String title;
	// 3T�������
	private String title3T;
	// ������
	private String usernameC;
	// Ӣ����
	private String usernameE;
	// ���������У�
	private String groupnameC;
	// ��������
	private String groupcode;
	// ��������Ӣ��
	private String groupnameE;
	// ��¼��
	private String loginname;
	// �������
	private String newstype;
	// �������id
	private String newstypeID;
	// �������
	private String comment;

	// �������ʧ��ʱ��
	private String rejecttime;
	// ���ǩ��ʱ��
	private String reletime;
	// �ɹ�����ʱ��
	private String senttime;
	// �ط�ʱ�� ������û���ã�������������̭ʱ�䣩
	private String rereletime;

	// �������
	private String contents;
	// 3T�������
	private String contents3T;
	// �������ʱ��
	private String receiveTime;
	// ��Żش�ʱ��
	private String newsIDBackTime;
	// ���״̬��Ϣ
	private ManuscriptStatus manuscriptsStatus;

	// ��γ����Ϣ
	private String location;

	// ����ʱ��
	private String createtime;

	// ����
	private String author;

	// ��ǩģ��
	private ManuscriptTemplate manuscriptTemplate;

	// ===================��������================
	// ���Ԥ��ͼ
	private Bitmap preViewImage;
	// ѡ��״̬
	private boolean check;
	
	//�Ƿ��и���
	private boolean hasAccessories;

	public Manuscripts() {
		m_id = "";
		this.createid = "";
		this.releid = "";
		this.newsid = "";
		this.title = "";
		this.title3T = "";
		this.usernameC = "";
		this.usernameE = "";
		this.groupnameC = "";
		this.groupcode = "";
		this.groupnameE = "";
		this.newstype = "";
		this.newstypeID = "";
		this.comment = "";
		this.rejecttime = "";
		this.reletime = "";
		this.senttime = "";
		this.rereletime = "";
		this.contents = "";
		this.contents3T = "";
		this.receiveTime = "";
		this.newsIDBackTime = "";
		this.manuscriptsStatus = ManuscriptStatus.Editing;
		this.manuscriptTemplate = null;
		this.preViewImage = null;
		this.check = false;
		this.location = "";
		this.createtime = TimeFormatHelper.getFormatTime(new Date());
		this.author = "";
		this.setLoginname("");
		this.hasAccessories = false;
	}

	public Manuscripts(String mId, String createid, String releid,
			String newsid, String title, String title3t, String usernameC,
			String usernameE, String groupnameC, String groupcode,
			String groupnameE, String newstype, String newstypeID,
			String comment, String rejecttime, String reletime,
			String senttime, String rereletime, String contents,
			String contents3t, String receiveTime, String newsIDBackTime,
			ManuscriptStatus manuscriptsStatus, String location,
			String createtime, String author,
			ManuscriptTemplate manuscriptTemplate, Bitmap preViewImage,
			boolean check, String loginname) {
		super();
		this.m_id = mId;
		this.createid = createid;
		this.releid = releid;
		this.newsid = newsid;
		this.title = title;
		this.title3T = title3t;
		this.usernameC = usernameC;
		this.usernameE = usernameE;
		this.groupnameC = groupnameC;
		this.groupcode = groupcode;
		this.groupnameE = groupnameE;
		this.newstype = newstype;
		this.newstypeID = newstypeID;
		this.comment = comment;
		this.rejecttime = rejecttime;
		this.reletime = reletime;
		this.senttime = senttime;
		this.rereletime = rereletime;
		this.contents = contents;
		this.contents3T = contents3t;
		this.receiveTime = receiveTime;
		this.newsIDBackTime = newsIDBackTime;
		this.manuscriptsStatus = manuscriptsStatus;
		this.location = location;
		this.createtime = createtime;
		this.author = author;
		this.manuscriptTemplate = manuscriptTemplate;
		this.preViewImage = preViewImage;
		this.check = check;
		this.setLoginname(loginname);
		this.hasAccessories = false;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getM_id() {
		return m_id;
	}

	public void setM_id(String mId) {
		m_id = mId;
	}

	public String getCreateid() {
		return createid;
	}

	public void setCreateid(String createid) {
		this.createid = createid;
	}

	public String getReleid() {
		return releid;
	}

	public void setReleid(String releid) {
		this.releid = releid;
	}

	public String getNewsid() {
		return newsid;
	}

	public void setNewsid(String newsid) {
		this.newsid = newsid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle3T() {
		return title3T;
	}

	public void setTitle3T(String title3t) {
		title3T = title3t;
	}

	public String getUsernameC() {
		return usernameC;
	}

	public void setUsernameC(String usernameC) {
		this.usernameC = usernameC;
	}

	public String getUsernameE() {
		return usernameE;
	}

	public void setUsernameE(String usernameE) {
		this.usernameE = usernameE;
	}

	public String getGroupnameC() {
		return groupnameC;
	}

	public void setGroupnameC(String groupnameC) {
		this.groupnameC = groupnameC;
	}

	public String getGroupcode() {
		return groupcode;
	}

	public void setGroupcode(String groupcode) {
		this.groupcode = groupcode;
	}

	public String getGroupnameE() {
		return groupnameE;
	}

	public void setGroupnameE(String groupnameE) {
		this.groupnameE = groupnameE;
	}

	public String getNewstype() {
		return newstype;
	}

	public void setNewstype(String newstype) {
		this.newstype = newstype;
	}

	public String getNewstypeID() {
		return newstypeID;
	}

	public void setNewstypeID(String newstypeID) {
		this.newstypeID = newstypeID;
	}

	public String getRejecttime() {
		return rejecttime;
	}

	public void setRejecttime(String rejecttime) {
		this.rejecttime = rejecttime;
	}

	public String getReletime() {
		return reletime;
	}

	public void setReletime(String reletime) {
		this.reletime = reletime;
	}

	public String getSenttime() {
		return senttime;
	}

	public void setSenttime(String senttime) {
		this.senttime = senttime;
	}

	public String getRereletime() {
		return rereletime;
	}

	public void setRereletime(String rereletime) {
		this.rereletime = rereletime;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getContents3T() {
		return contents3T;
	}

	public void setContents3T(String contents3t) {
		contents3T = contents3t;
	}

	public String getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getNewsIDBackTime() {
		return newsIDBackTime;
	}

	public void setNewsIDBackTime(String newsIDBackTime) {
		this.newsIDBackTime = newsIDBackTime;
	}

	public ManuscriptStatus getManuscriptsStatus() {
		return manuscriptsStatus;
	}

	public void setManuscriptsStatus(ManuscriptStatus manuscriptsStatus) {
		this.manuscriptsStatus = manuscriptsStatus;
	}

	public ManuscriptTemplate getManuscriptTemplate() {
		return manuscriptTemplate;
	}

	public void setManuscriptTemplate(ManuscriptTemplate manuscriptTemplate) {
		this.manuscriptTemplate = manuscriptTemplate;
	}

	public void setPreViewImage(Bitmap preViewImage) {
		this.preViewImage = preViewImage;
	}

	public Bitmap getPreViewImage() {
		return preViewImage;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Override
	public String toString() {
		return "Manuscripts [author=" + author + ", check=" + check
				+ ", comment=" + comment + ", contents=" + contents
				+ ", contents3T=" + contents3T + ", createid=" + createid
				+ ", createtime=" + createtime + ", groupcode=" + groupcode
				+ ", groupnameC=" + groupnameC + ", groupnameE=" + groupnameE
				+ ", location=" + location + ", m_id=" + m_id
				+ ", manuscriptTemplate=" + manuscriptTemplate
				+ ", manuscriptsStatus=" + manuscriptsStatus
				+ ", newsIDBackTime=" + newsIDBackTime + ", newsid=" + newsid
				+ ", newstype=" + newstype + ", newstypeID=" + newstypeID
				+ ", preViewImage=" + preViewImage + ", receiveTime="
				+ receiveTime + ", rejecttime=" + rejecttime + ", releid="
				+ releid + ", reletime=" + reletime + ", rereletime="
				+ rereletime + ", senttime=" + senttime + ", title=" + title
				+ ", title3T=" + title3T + ", usernameC=" + usernameC
				+ ", usernameE=" + usernameE + "]";
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public boolean isCheck() {
		return check;
	}

	public void copyTo(Manuscripts mu) {

		if (mu == null)
			return;

		mu.setCheck(this.isCheck());
		mu.setComment(this.getComment());
		mu.setContents(this.getContents());
		mu.setContents3T(this.getContents3T());
		mu.setCreateid(this.getCreateid());
		mu.setGroupcode(this.getGroupcode());
		mu.setGroupnameC(this.getGroupnameC());
		mu.setGroupnameE(this.getGroupnameE());
		mu.setLocation(this.getLocation());
		mu.setCreatetime(this.getCreatetime());
		mu.setM_id(this.getM_id().toString());
		mu.setManuscriptsStatus(this.getManuscriptsStatus());
		mu.setNewsid(this.getNewsid());
		mu.setNewsIDBackTime(this.getNewsIDBackTime());
		mu.setNewstype(this.getNewstype());
		mu.setNewstypeID(this.getNewstypeID());
		mu.setReceiveTime(this.getReceiveTime());
		mu.setRejecttime(this.getRejecttime());
		mu.setReleid(this.getReleid());
		mu.setReletime(this.getReletime());
		mu.setRereletime(this.getRereletime());
		mu.setSenttime(this.getSenttime());
		mu.setTitle(this.getTitle());
		mu.setTitle3T(this.getTitle3T());
		mu.setUsernameC(this.getUsernameC());
		mu.setUsernameE(this.getUsernameE());
		mu.setAuthor(this.getAuthor());
		mu.setLoginname(this.getLoginname());

		ManuscriptTemplate template = this.getManuscriptTemplate();
		if (template != null){
			mu.setManuscriptTemplate(new ManuscriptTemplate());
			template.copyTo(mu.getManuscriptTemplate());
		}
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setHasAccessories(boolean hasAccessories) {
		this.hasAccessories = hasAccessories;
	}

	public boolean isHasAccessories() {
		return hasAccessories;
	}
}
