package com.cuc.miti.phone.xmc.services;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.receiver.ConnectionChangeReceiver;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;

public class ReceiverService extends Service {

	// �������
	private ConnectionChangeReceiver mReceiver;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		Log.i(IngleApplication.TAG,
				"NetworkService onCreate()");
		if (mReceiver == null) {
			mReceiver = new ConnectionChangeReceiver();
		}

		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, filter);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		Log.i(IngleApplication.TAG,
				"NetworkService onDestroy()");
		this.unregisterReceiver(mReceiver);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Log.i(IngleApplication.TAG,
				"NetworkService onStart()");
		
	}

}
