package com.cuc.miti.phone.xmc.domain;

import java.io.Serializable;

import com.cuc.miti.phone.xmc.domain.Enums.MessageType;

/**
 * @Description ��ϢMessage
 * @author shiqing
 * 
 */
public class MessageForPush implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO
	 */

	private static final long serialVersionUID = 1L;

	// ID
	private String m_id;

	// ��Ϣ����
	private String msgContent;

	// ��Ϣ������
	private String msgOwner;

	// ��Ϣ���������ͣ�ȫ�� ��ɫ ���� ����
	private String msgOwnerType;

	// ��Ϣ����ʱ��
	private String msgSendTime;

	// ��Ϣ���ͣ�ϵͳ��Ϣ ������Ϣ ��ʱ��Ϣ
	private MessageType msgType;

	public MessageForPush() {
	}

	

	public MessageForPush(String mId, String msgContent, String msgOwner,
			String msgOwnerType, String msgSendTime, MessageType msgType) {
		super();
		m_id = mId;
		this.msgContent = msgContent;
		this.msgOwner = msgOwner;
		this.msgOwnerType = msgOwnerType;
		this.msgSendTime = msgSendTime;
		this.msgType = msgType;
	}



	public String getM_id() {
		return m_id;
	}

	public void setM_id(String m_id) {
		this.m_id = m_id;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String getMsgOwner() {
		return msgOwner;
	}

	public void setMsgOwner(String msgOwner) {
		this.msgOwner = msgOwner;
	}

	public String getMsgOwnerType() {
		return msgOwnerType;
	}

	public void setMsgOwnerType(String msgOwnerType) {
		this.msgOwnerType = msgOwnerType;
	}

	public String getMsgSendTime() {
		return msgSendTime;
	}

	public void setMsgSendTime(String msgSendTime) {
		this.msgSendTime = msgSendTime;
	}



	public MessageType getMsgType() {
		return msgType;
	}



	public void setMsgType(MessageType msgType) {
		this.msgType = msgType;
	}

	
}
