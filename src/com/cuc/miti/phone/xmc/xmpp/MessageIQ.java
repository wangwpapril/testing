package com.cuc.miti.phone.xmc.xmpp;

import org.jivesoftware.smack.packet.IQ;

/** 
 * This class represents a message IQ packet.
 *
 * @author SongQing
 */
public class MessageIQ extends IQ {
	
	/*
	 * ��������͵��ͻ��˵���Ϣ��ʾ��
	 * 
	 * <message xmlns="jabber:client" 
	 * 					type="chat" id="purple3b635d04" 
	 * 					to="testapple@chat.xinhuaenews.com/7e887ecc" 
	 * 					from="test1@chat.xinhuaenews.com/904c9520">
	 * 		<body>cvxcvcxvcxvxcv</body>
	 * </message>
	 * */
    private String id;				//��Ϣ��ʶ  		id="purple3b635d04"
    private String sender;   	//������			from="test1@chat.xinhuaenews.com/904c9520"
    private String receiver;  	//�ռ���			to="testapple@chat.xinhuaenews.com/7e887ecc"
    private String content;		//����				<body>cvxcvcxvcxvxcv</body>
    private String msgtype;			//��Ϣ����   		type="chat"
    
    private static final String NAMESPACE = "jabber:client";  
  
    public MessageIQ() {
    }

    @Override
    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append("message").append(" xmlns=\"").append(NAMESPACE).append("\">");		//<message xmls="jabber:client">
        if (id != null) {
            buf.append("<id>").append(id).append("</id>");																			//<id>xxxx</id>
        }
        buf.append("</").append("message").append("> ");																			//</message>
        return buf.toString();
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMsgtype() {
		return msgtype;
	}
	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

}
