package com.cuc.miti.phone.xmc.domain;

import java.io.Serializable;

import android.graphics.Bitmap;

import com.cuc.miti.phone.xmc.domain.Enums.MessageType;
import com.cuc.miti.phone.xmc.domain.Enums.MsgOwnerType;
import com.cuc.miti.phone.xmc.domain.Enums.MsgSendStatus;
import com.cuc.miti.phone.xmc.domain.Enums.ReadOrNotType;
import com.cuc.miti.phone.xmc.domain.Enums.SendOrReceiveType;

//��Ϣ
//@author llna

public class MessageForUs implements Serializable {
//	msg_id
	private int msg_id;
//	id
	private String id;
//	����
	private String msgContent;
//	�û���
	private String loginName;
//����|����	
	private SendOrReceiveType sendOrReceiveType;
//��Ϣ������	
	private String msgOwner;
//��Ϣ���������� ȫ�壨0�� ���� ��1�� ��ɫ ��2�� ���ˣ�3��
	private MsgOwnerType msgOwnerType;
//	��Ϣ���ͣ�ϵͳ��Ϣ(0) ������Ϣ(1) ��ʱ��Ϣ(2)	�ѷ���Ϣ(3)
	private MessageType msgType;
//��Ϣ������	
	private String msgFrom;
//��Ϣ����|����ʱ��	
	private String msgSendOrReceiveTime;
//�Ƿ����Ķ�	
	private ReadOrNotType readOrNotType;
//�����Ƿ�ɹ�	
	private MsgSendStatus msgSendStatus;
// ѡ��״̬
	private boolean check;
    
	//���캯�����ã���ʼ��
	public MessageForUs() {
		this.check = false;
	}	
	public MessageForUs(int msgId, String id, String msgContent,
			String loginName, SendOrReceiveType sendOrReceiveType,
			String msgOwner, MsgOwnerType msgOwnerType, MessageType msgType,
			String msgFrom, String msgSendOrReceiveTime,
			ReadOrNotType readOrNotType, MsgSendStatus msgSendStatus,boolean check) {
		super();
		msg_id = msgId;
		this.id = id;
		this.msgContent = msgContent;
		this.loginName = loginName;
		this.sendOrReceiveType = sendOrReceiveType;
		this.msgOwner = msgOwner;
		this.msgOwnerType = msgOwnerType;
		this.msgType = msgType;
		this.msgFrom = msgFrom;
		this.msgSendOrReceiveTime = msgSendOrReceiveTime;
		this.readOrNotType = readOrNotType;
		this.msgSendStatus = msgSendStatus;
		this.check = check;
	}	

	@Override
	public String toString() {
		return "MessageForUs [id=" + id + ", check=" + check
				+ ", loginName=" + loginName
				+ ", msgContent=" + msgContent + ", msgFrom=" + msgFrom
				+ ", msgOwner=" + msgOwner + ", msgOwnerType=" + msgOwnerType
				+ ", msgSendOrReceiveTime=" + msgSendOrReceiveTime
				+ ", msgSendStatus=" + msgSendStatus + ", msgType=" + msgType
				+ ", msg_id=" + msg_id + ", readOrNotType=" + readOrNotType
				+ ", sendOrReceiveType=" + sendOrReceiveType + "]";
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public boolean isCheck() {
		return check;
	}
	public int getMsg_id() {
		return msg_id;
	}


	public void setMsg_id(int msgId) {
		msg_id = msgId;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getMsgContent() {
		return msgContent;
	}


	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}


	public String getLoginName() {
		return loginName;
	}


	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}


	public SendOrReceiveType getSendOrReceiveType() {
		return sendOrReceiveType;
	}


	public void setSendOrReceiveType(SendOrReceiveType sendOrReceiveType) {
		this.sendOrReceiveType = sendOrReceiveType;
	}


	public String getMsgOwner() {
		return msgOwner;
	}


	public void setMsgOwner(String msgOwner) {
		this.msgOwner = msgOwner;
	}


	public MsgOwnerType getMsgOwnerType() {
		return msgOwnerType;
	}


	public void setMsgOwnerType(MsgOwnerType msgOwnerType) {
		this.msgOwnerType = msgOwnerType;
	}


	public MessageType getMsgType() {
		return msgType;
	}


	public void setMsgType(MessageType msgType) {
		this.msgType = msgType;
	}


	public String getMsgFrom() {
		return msgFrom;
	}


	public void setMsgFrom(String msgFrom) {
		this.msgFrom = msgFrom;
	}


	public String getMsgSendOrReceiveTime() {
		return msgSendOrReceiveTime;
	}


	public void setMsgSendOrReceiveTime(String msgSendOrReceiveTime) {
		this.msgSendOrReceiveTime = msgSendOrReceiveTime;
	}


	public ReadOrNotType getReadOrNotType() {
		return readOrNotType;
	}


	public void setReadOrNotType(ReadOrNotType readOrNotType) {
		this.readOrNotType = readOrNotType;
	}


	public MsgSendStatus getMsgSendStatus() {
		return msgSendStatus;
	}


	public void setMsgSendStatus(MsgSendStatus msgSendStatus) {
		this.msgSendStatus = msgSendStatus;
	}	

}
