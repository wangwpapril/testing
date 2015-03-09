package com.cuc.miti.phone.xmc.xmpp;

import java.util.ArrayList;
import java.util.Collection;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import com.cuc.miti.phone.xmc.ui.ManageFriendsActivity;
import com.cuc.miti.phone.xmc.utils.XmcNotification;
import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.xmpp.XmppManager;
import com.cuc.miti.phone.xmc.utils.SmackUtils;
import com.cuc.miti.phone.xmc.R;

/**
 * Broadcast receiver that handles push notification messages from the server.
 * This should be registered as receiver in AndroidManifest.xml.
 * 
 * @author SongQing
 */
public final class MessageReceiver extends BroadcastReceiver {

	private static final String LOGTAG = LogUtil
			.makeLogTag(MessageReceiver.class);
	Roster roster;
	Collection<RosterEntry> entries;
	XmcNotification notification;
	String username;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(LOGTAG, "MessageReceiver.onReceive()...");
		String action = intent.getAction();
		Log.d(LOGTAG, "action=" + action);
		notification = new XmcNotification(context);
		roster = IngleApplication.getConnection()
				.getRoster();
		username = IngleApplication.getInstance()
				.getCurrentUser()
				+ "@" + XmppManager.XMPPHOST_STRING;
		ArrayList<Activity> activities = IngleApplication
				.getInstance().getActivities();
		Activity activity = activities.get(activities.size() - 1);

		AlertDialog.Builder builder = new AlertDialog.Builder(activity
				.getWindow().getContext());
		if (Constants.ACTION_SHOW_NOTIFICATION.equals(action)) {
			String messageId = intent.getStringExtra(Constants.MESSAGE_ID);
			String messageType = intent.getStringExtra(Constants.MESSAGE_TYPE);
			String messageSender = intent
					.getStringExtra(Constants.MESSAGE_SENDER);
			String messageReceiver = intent
					.getStringExtra(Constants.MESSAGE_RECEIVER);
			String messageContent = intent
					.getStringExtra(Constants.MESSAGE_CONTENT);

			Log.d(LOGTAG, "messageId=" + messageId);
			Log.d(LOGTAG, "messageType=" + messageType);
			Log.d(LOGTAG, "messageSender=" + messageSender);
			Log.d(LOGTAG, "messageReceiver=" + messageReceiver);
			Log.d(LOGTAG, "messageContent=" + messageContent);

			String username = IngleApplication
					.getInstance().getCurrentUser();
			if (messageReceiver.substring(0, messageReceiver.indexOf("@"))
					.equals(username)
					&& messageSender.substring(0, messageReceiver.indexOf("@"))
							.equals(username) == false)
				notification.showNotification(
						XmcNotification.NOTIFICATION_NEWMSG, messageSender,
						messageContent);
		} else if (Constants.ACTION_SHOW_NOTIFICATION_ADDF.equals(action)) {
			final String from = intent.getStringExtra(Constants.MESSAGE_SENDER);
			Log.d(LOGTAG, "from=" + from);

			String status = isFriend(from);
			if (status.equals("to")) {
				builder.setMessage(
						from.substring(0, from.indexOf("@")) + "��ͬ����ĺ�������")
						.setPositiveButton(R.string.confirm_button,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								}).create().show();
			} else if (status.equals("both") == false)

				builder.setMessage(
						from.substring(0, from.indexOf("@")) + "�������Ϊ����")
						.setPositiveButton(R.string.agree_button,
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										// ȷ����Ӻ��ѵĲ���

										SmackUtils.addUser(
												IngleApplication
														.getConnection()
														.getRoster(), from, null);
										

										dialog.dismiss();

									}
								}).setNegativeButton(R.string.disagree_button,
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {

										sendPresence(username, from,
												Presence.Type.unsubscribed);

										dialog.dismiss();

									}
								}).create().show();

		} else if (Constants.ACTION_SHOW_NOTIFICATION_ADDEDF.equals(action)) {

		} else if (Constants.ACTION_SHOW_NOTIFICATION_DENYTOADDF.equals(action)) {
			String from = intent.getStringExtra(Constants.MESSAGE_SENDER);
			Log.d(LOGTAG, "from=" + from);

			if (isFriend(from).equals("none")) {

				builder.setMessage(
						from.substring(0, from.indexOf("@")) + "�ܾ�����ĺ�������")
						.setPositiveButton(R.string.confirm_button,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								}).create().show();
			}

		} else if (Constants.ACTION_SHOW_NOTIFICATION_DELETEF.equals(action)) {
			String from = intent.getStringExtra(Constants.MESSAGE_SENDER);
			Log.d(LOGTAG, "from=" + from);

			builder
					.setMessage(
							from.substring(0, from.indexOf("@")) + "�Ѱ����߳�����")
					.setPositiveButton(R.string.confirm_button,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).create().show();

		}
	}

	private String isFriend(String from) {
		roster = IngleApplication.getConnection()
				.getRoster();

		RosterEntry entry = roster.getEntry(from);
		if (entry != null) {

			return entry.getType().name();
		} else
			return "";
	}

	private void sendPresence(String from, String to, Presence.Type type) {
		Presence presence = new Presence(type);
		presence.setFrom(from);
		presence.setTo(to);
		IngleApplication.getConnection().sendPacket(
				presence);
	}
}
