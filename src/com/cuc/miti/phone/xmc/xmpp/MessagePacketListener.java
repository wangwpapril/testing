package com.cuc.miti.phone.xmc.xmpp;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.cuc.miti.phone.xmc.domain.MessageForUs;
import com.cuc.miti.phone.xmc.domain.Enums.MessageType;
import com.cuc.miti.phone.xmc.domain.Enums.MsgOwnerType;
import com.cuc.miti.phone.xmc.domain.Enums.MsgSendStatus;
import com.cuc.miti.phone.xmc.domain.Enums.ReadOrNotType;
import com.cuc.miti.phone.xmc.domain.Enums.SendOrReceiveType;
import com.cuc.miti.phone.xmc.logic.MessageService;
import com.cuc.miti.phone.xmc.utils.TimeFormatHelper;

import android.content.Intent;
import android.util.Log;
import android.util.Xml;

/**
 * This class notifies the receiver of incoming message packets asynchronously.
 * 
 * @author SongQing
 */
public class MessagePacketListener implements PacketListener {

	private static final String LOGTAG = LogUtil
			.makeLogTag(MessagePacketListener.class);

	private final XmppManager xmppManager;
	private MessageService messageService;

	public MessagePacketListener(XmppManager xmppManager) {
		this.xmppManager = xmppManager;
		messageService = new MessageService(xmppManager.getContext());
	}

	public void processPacket(Packet packet) {
		Log.d(LOGTAG, "MessagePacketListener.processPacket()...");
		Log.d(LOGTAG, "packet.toXML()=" + packet.toXML());

		if (packet instanceof org.jivesoftware.smack.packet.Message) {
			org.jivesoftware.smack.packet.Message message = (org.jivesoftware.smack.packet.Message) packet;

			String messageId = message.getPacketID();
			String messageType = message.getType().toString();
			String messageSender = message.getFrom();
			String messageReceiver = message.getTo();
			String messageContent = message.getBody();
			String messageTime = (String) message.getProperty("MsgTime");

			// �˴����Ƚ��п���Ϣ������Ϣ��Ϊ�գ�����
			if (!"".equals(messageContent) && messageContent != null) {
				// �ѵõ�����Ϣ���б��س־û��洢
				MessageForUs msg = new MessageForUs();

				msg.setMsgContent(messageContent);
				msg.setLoginName(messageReceiver.substring(0, messageReceiver
						.indexOf("@")));
				msg.setMsgFrom(messageSender.substring(0, messageSender
						.indexOf("@")));
				msg.setMsgOwner(messageReceiver.substring(0, messageReceiver
						.indexOf("@")));
				msg.setMsgOwnerType(MsgOwnerType.Person);
				if (messageTime != null) {
					msg.setMsgSendOrReceiveTime(messageTime);
				} else {
					Date date = new Date();
					SimpleDateFormat sFormat = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");

					msg.setMsgSendOrReceiveTime(String.valueOf(TimeFormatHelper
							.convertDateToLong(sFormat.format(date))));
				}
				msg.setMsgType(MessageType.XMPPMsg);
				msg.setReadOrNotType(ReadOrNotType.New);
				msg.setSendOrReceiveType(SendOrReceiveType.Receive);
				msg.setId(messageId);
				msg.setMsgSendStatus(MsgSendStatus.Success);
				messageService.addMessage(msg);

				// �µõ�����ϢҪ������ʾ��ʾ
				Intent intent = new Intent(Constants.ACTION_SHOW_NOTIFICATION);
				intent.putExtra(Constants.MESSAGE_ID, messageId);
				intent.putExtra(Constants.MESSAGE_TYPE, messageType);
				intent.putExtra(Constants.MESSAGE_SENDER, messageSender);
				intent.putExtra(Constants.MESSAGE_RECEIVER, messageReceiver);
				intent.putExtra(Constants.MESSAGE_CONTENT, messageContent);
				xmppManager.getContext().sendBroadcast(intent);
			}
		}

		// org.jivesoftware.smack.packet.Presence
		// aa=(org.jivesoftware.smack.packet.Presence) packet;

		/*
		 * <message id="rm2632_25"
		 * to="testapple@chat.xinhuaenews.com/AndroidpnClient"
		 * from="testapple@chat.xinhuaenews.com/Rooyee" type="chat">
		 * <body>������</body> <rmhtml
		 * xmlns="http://www.w3.org/1999/xhtml-urlencode"></rmhtml></message>
		 */

		/*
		 * String xml=packet.toXML(); MessageIQ message;
		 * 
		 * 
		 * try { XmlPullParser parser = Xml.newPullParser(); parser.setInput(new
		 * ByteArrayInputStream(xml.getBytes("UTF-8")), "UTF-8"); int eventType
		 * = parser.getEventType(); message = new MessageIQ(); while (eventType
		 * != XmlPullParser.END_DOCUMENT) {
		 * 
		 * switch (eventType) { case XmlPullParser.START_DOCUMENT:
		 * 
		 * break; case XmlPullParser.START_TAG: if
		 * (parser.getName().equals("message")) {
		 * 
		 * message.setId(parser.getAttributeValue("", "id"));
		 * message.setFrom(parser.getAttributeValue("", "from"));
		 * message.setTo(parser.getAttributeValue("", "to"));
		 * message.setSender(parser.getAttributeValue("", "from"));
		 * message.setReceiver(parser.getAttributeValue("", "to"));
		 * message.setMsgtype(parser.getAttributeValue("", "type")); } else if
		 * (parser.getName().equals("body")) { eventType = parser.next();
		 * message.setContent(parser.getText().toString()); } break; case
		 * XmlPullParser.END_TAG:
		 * 
		 * break; } eventType = parser.next(); }
		 * 
		 * Intent intent = new Intent(Constants.ACTION_SHOW_NOTIFICATION);
		 * intent.putExtra(Constants.MESSAGE_ID, message.getId());
		 * intent.putExtra(Constants.MESSAGE_TYPE,message.getMsgtype());
		 * intent.putExtra(Constants.MESSAGE_SENDER,message.getFrom());
		 * intent.putExtra(Constants.MESSAGE_RECEIVER,message.getTo());
		 * intent.putExtra(Constants.MESSAGE_CONTENT, message.getContent());
		 * xmppManager.getContext().sendBroadcast(intent);
		 * 
		 * 
		 * } catch (Exception e) { e.printStackTrace(); }
		 */

	}

}
