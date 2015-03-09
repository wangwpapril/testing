package com.cuc.miti.phone.xmc.domain;

import java.io.Serializable;

import com.cuc.miti.phone.xmc.domain.Enums.UploadTaskStatus;

/**
 * ����ϴ�ʵ����
 * @author SongQing
 *
 */
public class UploadTask implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;												//����
	private String createtime;								//���񴴽�ʱ��
	private String finishtime;								//�������ʱ��
	private String manuscriptId;							//�����Ӧ�ĸ��ID
	private UploadTaskStatus status;					//����״̬_���enum UploadTaskStatus��
	private int lastblocknum;								//���ɹ����͵�BLOCK���
	private int blocksize;									//�����ļ��ķֿ��С(��Ϊ�û��п��ܸı�ֿ��С�����ã�������ʱ��Ҫ֪���ϴδ�����õķֿ��С)
	private int progress;										//�ϴ�����Ľ��
	private String message;								//����ϴ�����е���Ϣ��¼
	private String priority;									//����ϴ����ȼ�
	private String loginname;								//�û���½��
	private int totalsize;										//�ܴ�С(����͸��xml)
	private int repeattimes;								//�Ѿ��ظ��Ĵ���(�û����������ط�����)
	private int uploadedsize;								//�Ѿ��ϴ��ļ��Ĵ�С
	private String fileurl;										//��������·�������ļ���(����Ϊ�ն������ָ��)
	private String xmlurl;									//�����ϢXML·�������ļ���(����Ϊ��)
	private String fileid;										//�ϴ��ļ�id������

	public UploadTask() {
		this.id=0;
		this.createtime = "";
		this.finishtime = "";
		this.manuscriptId = "";
		this.status = null;
		this.lastblocknum = -1;
		this.blocksize=0;
		this.progress = 0;
		this.priority="";
		this.loginname="";
		this.totalsize=0;
		this.repeattimes=0;
		this.fileurl="";
		this.xmlurl="";	
		this.fileid="";
	}
	
	public int getBlocksize() {
		return blocksize;
	}

	public void setBlocksize(int blocksize) {
		this.blocksize = blocksize;
	}

	public int getRepeattimes() {
		return repeattimes;
	}

	public void setRepeattimes(int repeattimes) {
		this.repeattimes = repeattimes;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getFinishtime() {
		return finishtime;
	}

	public void setFinishtime(String finishtime) {
		this.finishtime = finishtime;
	}

	public String getManuscriptId() {
		return manuscriptId;
	}

	public void setManuscriptId(String manuscriptId) {
		this.manuscriptId = manuscriptId;
	}

	public UploadTaskStatus getStatus() {
		return status;
	}

	public void setStatus(UploadTaskStatus status) {
		this.status = status;
	}

	public int getLastblocknum() {
		return lastblocknum;
	}

	public void setLastblocknum(int lastblocknum) {
		this.lastblocknum = lastblocknum;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getLoginname() {
		return loginname;
	}
	
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public int getTotalsize() {
		return totalsize;
	}

	public void setTotalsize(int totalsize) {
		this.totalsize = totalsize;
	}
	
	public String getFileurl() {
		return fileurl;
	}

	public void setFileurl(String fileurl) {
		this.fileurl = fileurl;
	}

	public String getXmlurl() {
		return xmlurl;
	}

	public void setXmlurl(String xmlurl) {
		this.xmlurl = xmlurl;
	}

	public int getUploadedsize() {
		return uploadedsize;
	}

	public void setUploadedsize(int uploadedsize) {
		this.uploadedsize = uploadedsize;
	}

	public String getFileid() {
		return fileid;
	}

	public void setFileid(String fileid) {
		this.fileid = fileid;
	}
	
}
