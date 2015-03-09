package com.cuc.miti.phone.xmc;

import java.util.ArrayList;

import com.cuc.miti.phone.xmc.utils.XmcNotification;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class MainHandler extends Handler {

	public static final int MSG_ISBACKRUN = 4;
	public static final int MSG_ISNOTBACKRUN = -4;

	//public static boolean isStop = false;

	public MainHandler(Looper looper) {
		super(looper);
	}

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);

//		UploadTask task;
		XmcNotification notification = new XmcNotification(
				IngleApplication.getInstance());

		switch (msg.what) {

		case MSG_ISBACKRUN:

			if(!IngleApplication.isStop)
				notification.showNotification(XmcNotification.NOTIFICATION_BACK);
			break;
		case MSG_ISNOTBACKRUN:
			notification.cancelNotification(XmcNotification.NOTIFICATION_BACK);
			break;
		default:
			break;
		}
	}

}
