package com.cuc.miti.phone.xmc.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.cuc.miti.phone.xmc.R;
import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Enums.LoginStatus;
import com.cuc.miti.phone.xmc.domain.Enums.NetStatus;
import com.cuc.miti.phone.xmc.domain.User;
import com.cuc.miti.phone.xmc.logic.UserService;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.ToastHelper;

/**
 * ������������������Ӳ�����ʱ����ʾ�û�
 * @author SongQing
 *
 */
public class ConnectionChangeReceiver extends BroadcastReceiver {

	public static final String ACTION_NETWORK_CONNECTION = "connection_change_receiver";
	private Context mContext = IngleApplication.getInstance();
	private UserService userService = null;
	private static final String CONNECTED="Connected";
	private static final String DISCONNECT = "Disconnect";
	private String lastConnectionStatus = DISCONNECT;

	@Override
	public void onReceive(Context context, Intent intent) {

		ConnectivityManager connMgr = null;
		NetworkInfo activeInfo = null;
		userService=new UserService(mContext);
		User user= null;
		
		try {
			connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			activeInfo = connMgr.getActiveNetworkInfo(); 			//��ǰ�������������ж�
			Log.d(ACTION_NETWORK_CONNECTION, "��⵽����仯");
		} catch (Exception e) {
			Logger.e(e);
		}
		
		synchronized (IngleApplication.mLock) {
			
			if (activeInfo != null && activeInfo.isConnected()) {	//����������(����ʲô��������)
				switch (activeInfo.getType()) {
					case ConnectivityManager.TYPE_MOBILE:			//�ƶ�����
						if(lastConnectionStatus.equals(CONNECTED)){
							break;
						}
						lastConnectionStatus = CONNECTED;
						IngleApplication.setNetStatus(NetStatus.MOBILE);
						Log.d(ACTION_NETWORK_CONNECTION, "�ƶ�����");
						ToastHelper.showToast(mContext.getResources().getString(R.string.network_mobile_connected),Toast.LENGTH_SHORT);
						
						user = IngleApplication.getInstance().currentUserInfo;
						if(user!=null &&  user.getUsername()!=null){
							userService.userLoginOnline(user.getUsername(),user.getPassword());
						}
//						else{
	//						uploadTaskService.getUploadTaskForStart();
//						}
						break;
					case ConnectivityManager.TYPE_WIFI:				//Wi-Fi����
						if(lastConnectionStatus.equals(CONNECTED)){
							break;
						}
						lastConnectionStatus = CONNECTED;
						IngleApplication.setNetStatus(NetStatus.WIFI);
						Log.d(ACTION_NETWORK_CONNECTION, "Wi-Fi����");
						ToastHelper.showToast(mContext.getResources().getString(R.string.network_wifi_connected),Toast.LENGTH_SHORT);
						
						user = IngleApplication.getInstance().currentUserInfo;
						if(user!=null && user.getUsername()!=null){
							userService.userLoginOnline(user.getUsername(),user.getPassword());
						}
//						else{
	//						uploadTaskService.getUploadTaskForStart();
//						}
						
						break;
					default:
						if(lastConnectionStatus.equals(DISCONNECT)){
							break;
						}
						lastConnectionStatus = DISCONNECT;
						user = IngleApplication.getInstance().currentUserInfo;
						if(user!=null && user.getUsername()!=null){
							IngleApplication.setLoginStatus(LoginStatus.offline);
						}
						IngleApplication.setNetStatus(NetStatus.Disable);
						Log.d(ACTION_NETWORK_CONNECTION, "���粻����");
						ToastHelper.showToast(mContext.getResources().getString(R.string.network_disconnected),Toast.LENGTH_SHORT);
												
						break;
					}
			}
			else{		//���粻����
				if(lastConnectionStatus.equals(DISCONNECT)){
					return;
				}
				lastConnectionStatus = DISCONNECT;
				user = IngleApplication.getInstance().currentUserInfo;
				if(user!=null && user.getUsername()!=null){
					IngleApplication.setLoginStatus(LoginStatus.offline);
				}
				IngleApplication.setNetStatus(NetStatus.Disable);
				Log.d(ACTION_NETWORK_CONNECTION, "���粻����");
				ToastHelper.showToast(mContext.getResources().getString(R.string.network_disconnected),Toast.LENGTH_SHORT);
				
				
			}
			
			IngleApplication.mLock.notifyAll(); // to main

		}

	}
}
