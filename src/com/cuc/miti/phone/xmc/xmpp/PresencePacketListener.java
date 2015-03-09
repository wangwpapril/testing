package com.cuc.miti.phone.xmc.xmpp;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import com.cuc.miti.phone.xmc.utils.ToastHelper;
import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This class notifies the receiver of incoming message packets asynchronously.
 * 
 * @author SongQing
 */
public class PresencePacketListener implements PacketListener {

	private static final String LOGTAG = LogUtil
			.makeLogTag(PresencePacketListener.class);

	private final XmppManager xmppManager;
	private EditText etContent;

	public PresencePacketListener(XmppManager xmppManager) {
		this.xmppManager = xmppManager;
		etContent = new EditText(xmppManager.getContext());
		etContent.setHeight(300);
		etContent.setGravity(Gravity.TOP);
	}

	public void processPacket(Packet packet) {
		Log.d(LOGTAG, "PresencePacketListener.processPacket()...");
		Log.d(LOGTAG, "packet.toXML()=" + packet.toXML());

		if (packet instanceof org.jivesoftware.smack.packet.Presence) {
			org.jivesoftware.smack.packet.Presence presence = (org.jivesoftware.smack.packet.Presence) packet;

//			String to = presence.getTo();
//			if (to.equals(IngleApplication.getInstance()
//					.getCurrentUser()
//					+ "@" + XmppManager.XMPPHOST_STRING)) {

				if (Presence.Type.subscribe.equals(presence.getType())) {
					String from = presence.getFrom();

					// ��������������ʾ��ʾ
					Intent intent = new Intent(
							Constants.ACTION_SHOW_NOTIFICATION_ADDF);
					intent.putExtra(Constants.MESSAGE_SENDER, from);
					xmppManager.getContext().sendBroadcast(intent);
				} else if (Presence.Type.subscribed.equals(presence.getType())) {
					String from = presence.getFrom();

					// ͬ�����������ʾ��ʾ
					Intent intent = new Intent(
							Constants.ACTION_SHOW_NOTIFICATION_ADDEDF);
					intent.putExtra(Constants.MESSAGE_SENDER, from);
					xmppManager.getContext().sendBroadcast(intent);
				} else if (Presence.Type.unsubscribe.equals(presence.getType())) {
					String from = presence.getFrom();
					// �޳�������ʾ��ʾ
					Intent intent = new Intent(
							Constants.ACTION_SHOW_NOTIFICATION_DELETEF);
					intent.putExtra(Constants.MESSAGE_SENDER, from);
					xmppManager.getContext().sendBroadcast(intent);
				} else if (Presence.Type.unsubscribed
						.equals(presence.getType())) {
					String from = presence.getFrom();
					// �ܾ���Ӻ�����ʾ��ʾ
					Intent intent = new Intent(
							Constants.ACTION_SHOW_NOTIFICATION_DENYTOADDF);
					intent.putExtra(Constants.MESSAGE_SENDER, from);
					xmppManager.getContext().sendBroadcast(intent);
//				}
			}
		}

	}

}
