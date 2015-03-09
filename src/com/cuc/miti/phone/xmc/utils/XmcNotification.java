package com.cuc.miti.phone.xmc.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Enums.MessageType;
import com.cuc.miti.phone.xmc.ui.LoginActivity;
import com.cuc.miti.phone.xmc.ui.MainActivity;
import com.cuc.miti.phone.xmc.ui.MessageActivity;
import com.cuc.miti.phone.xmc.ui.MessageSecondForInstantActivity;
import com.cuc.miti.phone.xmc.ui.MessageThirdForInstantActivity;
import com.cuc.miti.phone.xmc.ui.SentManuscriptsActivity;
import com.cuc.miti.phone.xmc.ui.SplashScreenActivity;
import com.cuc.miti.phone.xmc.ui.StandToManuscriptsActivity;
import com.cuc.miti.phone.xmc.R;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class XmcNotification extends Notification {

	private Context context;
	private Notification notification;

	public static final int NOTIFICATION_BACK = 1;
	public static final int NOTIFICATION_UPLOADING = 2;
	public static final int NOTIFICATION_UPLOADED = 3;
	public static final int NOTIFICATION_UPLOAD_FAILED = 4;
	public static final int NOTIFICATION_NEWMSG = 5; // ��������Ϣ����ʱ

	private static final Random random = new Random(System.currentTimeMillis());

	public XmcNotification(Context context) {

		this.context = context;

	}

	public void showNotification(int type) {

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		switch (type) {
		case NOTIFICATION_BACK:

			notification = new Notification(R.drawable.logo_1, "eNews",
					System.currentTimeMillis());
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			// �����ڵ����֪ͨ���е�"���֪ͨ"�󣬴�֪ͨ��������FLAG_ONGOING_EVENTһ��ʹ��
			notification.flags |= Notification.FLAG_NO_CLEAR;
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
			ArrayList<Activity> activities = IngleApplication
					.getInstance().getActivities();
			Intent backIntent = new Intent();
			if (activities != null && activities.size() > 0) {
				backIntent = new Intent(context, activities.get(
						activities.size() - 1).getClass());
			} else {
				if (IngleApplication.getInstance()
						.getCurrentUser() != null
						&& IngleApplication.getInstance()
								.getCurrentUser() != "") {
					backIntent = new Intent(context, MainActivity.class);
				} else { // û�е�¼
					backIntent = new Intent(context, SplashScreenActivity.class);
				}

			}
			backIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			backIntent.addCategory(Intent.CATEGORY_HOME);
			PendingIntent contentBackIntent = PendingIntent.getActivity(
					context, 0, backIntent, PendingIntent.FLAG_CANCEL_CURRENT);
			notification.setLatestEventInfo(context, "eNews",
					"�»��粩�ڲɼ�ϵͳ����������...", contentBackIntent);

			notificationManager.notify(XmcNotification.NOTIFICATION_BACK,
					notification);
			break;
		case NOTIFICATION_UPLOADING:

			// notification = new
			// Notification(android.R.drawable.stat_sys_upload,
			// "Uploading", System.currentTimeMillis());
			// notification.flags |= Notification.FLAG_ONGOING_EVENT;
			// // �����ڵ����֪ͨ���е�"���֪ͨ"�󣬴�֪ͨ��������FLAG_ONGOING_EVENTһ��ʹ��
			// notification.flags |= Notification.FLAG_NO_CLEAR;
			// notification.flags |= Notification.FLAG_AUTO_CANCEL;
			//
			// Intent uploadingIntent;
			// if (IngleApplication.getInstance()
			// .getCurrentUser() != null
			// && IngleApplication.getInstance()
			// .getCurrentUser() != "") {
			// uploadingIntent = new Intent(context,
			// StandToManuscriptsActivity.class);
			// } else { // û�е�¼
			// uploadingIntent = new Intent(context, LoginActivity.class);
			// }
			//
			// uploadingIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			// uploadingIntent.addCategory(Intent.CATEGORY_HOME);
			// PendingIntent contentUploadingIntent = PendingIntent.getActivity(
			// context, 0, uploadingIntent,
			// PendingIntent.FLAG_CANCEL_CURRENT);
			// notification.setLatestEventInfo(context, "Uploading", "�����ϴ�",
			// contentUploadingIntent);
			//
			// notificationManager.notify(XmcNotification.NOTIFICATION_UPLOADING,
			// notification);

			break;
		case NOTIFICATION_UPLOADED:
			// notification = new Notification(
			// android.R.drawable.stat_sys_upload_done, "Uploaded", System
			// .currentTimeMillis());
			// notification.flags |= Notification.FLAG_AUTO_CANCEL;
			//
			// Intent uploadedIntent;
			// if (IngleApplication.getInstance()
			// .getCurrentUser() != null
			// && IngleApplication.getInstance()
			// .getCurrentUser() != "") {
			// uploadedIntent = new Intent(context,
			// SentManuscriptsActivity.class);
			// } else { // û�е�¼
			// uploadedIntent = new Intent(context, LoginActivity.class);
			// }
			//
			// uploadedIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			// uploadedIntent.addCategory(Intent.CATEGORY_HOME);
			// PendingIntent contentUploadedIntent = PendingIntent.getActivity(
			// context, 0, uploadedIntent,
			// PendingIntent.FLAG_CANCEL_CURRENT);
			// notification.setLatestEventInfo(context, "Uploaded", "�ϴ��ɹ�",
			// contentUploadedIntent);
			//
			// notificationManager.notify(XmcNotification.NOTIFICATION_UPLOADING,
			// notification);

			break;
		case NOTIFICATION_UPLOAD_FAILED:
			// notification = new Notification(
			// android.R.drawable.stat_sys_upload_done, "Upload Error",
			// System.currentTimeMillis());
			// notification.flags |= Notification.FLAG_AUTO_CANCEL;
			//
			// Intent uploadFailedIntent;
			// if (IngleApplication.getInstance()
			// .getCurrentUser() != null
			// && IngleApplication.getInstance()
			// .getCurrentUser() != "") {
			// uploadFailedIntent = new Intent(context,
			// StandToManuscriptsActivity.class);
			// } else { // û�е�¼
			// uploadFailedIntent = new Intent(context, LoginActivity.class);
			// }
			//
			// uploadFailedIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			// uploadFailedIntent.addCategory(Intent.CATEGORY_HOME);
			// PendingIntent contentUploadFailedIntent = PendingIntent
			// .getActivity(context, 0, uploadFailedIntent,
			// PendingIntent.FLAG_CANCEL_CURRENT);
			// notification.setLatestEventInfo(context, "Upload error", "�ϴ�ʧ��",
			// contentUploadFailedIntent);
			//
			// notificationManager.notify(XmcNotification.NOTIFICATION_UPLOADING,
			// notification);

			break;
		default:
			break;
		}

	}

	public void showNotification(int type, String messageSender,
			String messageContent) {

		switch (type) {
		case NOTIFICATION_NEWMSG: // �����˵���̨����ϵͳ״̬���г��ֳ���ͼ�꣬���Ե�����µ�������

			// ���涼�����õ��Notificationͼ�����Ҫ��س���򿪵�Activity
			ArrayList<Activity> activities = IngleApplication
					.getInstance().getActivities();
			if (activities != null && activities.size() > 0) {

				if (activities.get(activities.size() - 1).getClass()
						.equals(MessageThirdForInstantActivity.class)) {
					MessageThirdForInstantActivity activity = (MessageThirdForInstantActivity) activities
							.get(activities.size() - 1);
					if (activity.getMsgFrom().equals(
							messageSender.substring(0,
									messageSender.indexOf("@"))))
						activity.update();

				} else if (activities.get(activities.size() - 1).getClass()
						.equals(MessageSecondForInstantActivity.class)) {
					MessageSecondForInstantActivity activity = (MessageSecondForInstantActivity) activities
							.get(activities.size() - 1);
					activity.updateMsgListViewForRemove();

				} else if (activities.get(activities.size() - 1).getClass()
						.equals(MessageActivity.class)) {
					MessageActivity activity = (MessageActivity) activities
							.get(activities.size() - 1);
					activity.update();
				} else if (activities.get(activities.size() - 1).getClass()
						.equals(MainActivity.class)) {
					MainActivity activity = (MainActivity) activities
							.get(activities.size() - 1);
					activity.setNewMessageNumber();
				}

			}
			// ��֪ͨ�ķ�ʽչʾ���û�
			notification = new Notification(R.drawable.logo_1, messageContent,
					System.currentTimeMillis());
			notification.defaults |= Notification.DEFAULT_VIBRATE;// �����
			notification.defaults |= Notification.DEFAULT_LIGHTS;// ��ӵƹ�
			notification.flags |= Notification.FLAG_AUTO_CANCEL; // /��֪ͨ���ϵ����֪ͨ���Զ�����֪ͨ
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
			Intent notificationIntent = new Intent(context,
					MessageThirdForInstantActivity.class);

			Bundle bundle = new Bundle();
			bundle.putString("msgTypeStr", MessageType.XMPPMsg.toString());
			bundle.putString("msgFromStr",
					messageSender.substring(0, messageSender.indexOf("@")));
			bundle.putString("msgNew", "messageNew");
			notificationIntent.putExtras(bundle);

			notificationIntent.setAction(Intent.ACTION_MAIN);
			notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
					notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

			notification.setLatestEventInfo(context,
					context.getString(R.string.msgFrom) + messageSender,
					messageContent, contentIntent);

			NotificationManager notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);

			notificationManager.notify(random.nextInt(), notification);

			break;

		default:
			break;
		}
	}

	/**
	 * ϵͳ�˳�ʱ������е�Notification
	 */
	public void cancelNotification() {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);

		/*
		 * notificationManager.cancel(NOTIFICATION_BACK);
		 * notificationManager.cancel(NOTIFICATION_UPLOADING);
		 * notificationManager.cancel(NOTIFICATION_UPLOADED);
		 * notificationManager.cancel(NOTIFICATION_UPLOAD_FAILED);
		 */
		notificationManager.cancelAll();
	}

	public void cancelNotification(int type) {

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		switch (type) {
		case NOTIFICATION_BACK:
			notificationManager.cancel(NOTIFICATION_BACK);
			break;
		case NOTIFICATION_UPLOADING:
			notificationManager.cancel(NOTIFICATION_UPLOADING);
			break;
		default:
			break;
		}
	}
}
