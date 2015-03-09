package com.cuc.miti.phone.xmc.services;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceKeys;
import com.cuc.miti.phone.xmc.domain.PositionInfo;
import com.cuc.miti.phone.xmc.domain.XmcException;
import com.cuc.miti.phone.xmc.http.RemoteCaller;
import com.cuc.miti.phone.xmc.utils.BaiduLocationHelper;
import com.cuc.miti.phone.xmc.utils.DeviceInfoHelper;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.SharedPreferencesHelper;
import com.cuc.miti.phone.xmc.utils.ToastHelper;

/**
 * @Description: ��λ�����ں�̨�Զ�����
 * 
 * @author Caojuan
 * 
 * @Date:2013-10-21
 */
public class LocationService extends Service {
	private static final int MESSAGE_AUTO_SEND_LOCATION = 1; // �Զ����͵���λ����Ϣ��������
	
	private boolean isStop;
	// ���嶨ʱ��
	protected Timer timer = new Timer();
	private TimerTask task;
	private BaiduLocationHelper locationHelper;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		isStop = false;

		/*
		 * Toast.makeText( IngleApplication.getInstance(),
		 * "service" + String.valueOf(isStop) + " handler" +
		 * String.valueOf(IngleApplication.isStop),
		 * Toast.LENGTH_LONG).show();
		 */

		iniAutoSave();
		
		locationHelper = new BaiduLocationHelper(IngleApplication.getInstance());
		//�򿪶�λ
		locationHelper.startLocationClient();
		return super.onStartCommand(intent, flags, startId);

	}

	/**
	 * ��ʼ���Զ����涨ʱ��
	 */
	private void iniAutoSave() {
		// ������ʱ������������ÿ��30�뱣��һ��
		task = new TimerTask() {
			@Override
			public void run() {
				Message message = new Message();
				message.what = MESSAGE_AUTO_SEND_LOCATION;
				handler.sendMessage(message);
			}
		};

		// �������л�ȡ�Զ�����ʱ����
		SharedPreferencesHelper helper = new SharedPreferencesHelper(this);

		int interval = 0;
		try {
			String strInterval = helper.GetUserPreferenceValue(PreferenceKeys.User_AutoSendLocationInterval
							.toString());
			if (!strInterval.equals("0"))
				interval = Integer.parseInt(strInterval);
		} catch (Exception e) {
			interval = 0;
		}

		if (interval != 0) {
			interval = interval * 1000;
			timer.schedule(task, 30000, interval);
		}
	}

	@Override
	public void onDestroy() {
		Log.i(IngleApplication.TAG,"LocationService onDestory()");
		super.onDestroy();
		isStop = true;
		
		//�رն�λ
		locationHelper.stopLocationClient();
	}

	/**
	 * ί�У���ݶ�ʱ����֪ͨ���б���������
	 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// ���浱ǰ���
			if (msg.what == MESSAGE_AUTO_SEND_LOCATION) {
				if (!isStop) {
//					LocationHelper lctHelper = new LocationHelper(LocationService.this);
//				final Location loc = lctHelper.getCurrentLocation();
				final PositionInfo positionInfo = locationHelper.getCurrentLocation();
				
				new Thread(new Runnable() {
					String[] ss;

					public void run() {
						try {					
							if(positionInfo!=null){
								String source = "AndroidPhone"
										+ DeviceInfoHelper
												.getAppVersionName(LocationService.this)[1];
								ss = RemoteCaller.Location(
										IngleApplication.getInstance().getCurrentUser(),
												IngleApplication.getSessionId(),
												positionInfo,
												"", "", source);

								if ("success".equals(ss[0])) {

									ToastHelper.showToast("���Ͷ�λ��Ϣ�ɹ�",Toast.LENGTH_SHORT);

								} else {
									// TODO ��ʻ�
									ToastHelper.showToast("���Ͷ�λ��Ϣʧ��",Toast.LENGTH_SHORT);

								}
							}else
							 {
								// TODO ��ʻ�
								ToastHelper.showToast("���Ͷ�λ��Ϣʧ��",Toast.LENGTH_SHORT);

							}
						} catch (XmcException e) {

							Logger.e(e);

						}
					}
				}).start();
			}}
			super.handleMessage(msg);
		}

	};
}
