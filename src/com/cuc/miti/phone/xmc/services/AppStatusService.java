package com.cuc.miti.phone.xmc.services;

import java.util.List;

import com.cuc.miti.phone.xmc.MainHandler;
import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.utils.XmcNotification;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * @Description: ��������Ƿ��ں�̨����
 * 
 * @author Shiqing
 * 
 * @Date:2012-05-21
 */
public class AppStatusService extends Service {

	private ActivityManager activityManager;
	private String packageName;
	private boolean isStop;

	private MainHandler mainHandler;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		activityManager = (ActivityManager) this
				.getSystemService(Context.ACTIVITY_SERVICE);
		packageName = this.getPackageName();
		mainHandler = new MainHandler(getMainLooper());
		isStop = false;

		/*Toast.makeText(
				IngleApplication.getInstance(),
				"service" + String.valueOf(isStop) + " handler"
						+ String.valueOf(IngleApplication.isStop), Toast.LENGTH_LONG).show();*/

		new Thread() {
			public void run() {
				if (Thread.interrupted()) {
					return;
				}
				try {

					while (!isStop) {
						Thread.sleep(1000);

						if (isAppOnForeground()) {
							Message message = mainHandler.obtainMessage();
							message.what = MainHandler.MSG_ISNOTBACKRUN;
							mainHandler.sendMessage(message);

						} else {
							Message message = mainHandler.obtainMessage();
							message.what = MainHandler.MSG_ISBACKRUN;
							mainHandler.sendMessage(message);

						}

					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}.start();
		return super.onStartCommand(intent, flags, startId);

	}

	public boolean isAppOnForeground() {
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(IngleApplication.TAG,
				"AppStatusService onDestory()");
		super.onDestroy();
		isStop = true;
	}

}
