package com.cuc.miti.phone.xmc;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.jivesoftware.smack.XMPPConnection;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cuc.miti.phone.xmc.domain.Enums.LoginStatus;
import com.cuc.miti.phone.xmc.domain.Enums.NetStatus;
import com.cuc.miti.phone.xmc.domain.ManuscriptTemplate;
import com.cuc.miti.phone.xmc.domain.User;
import com.cuc.miti.phone.xmc.domain.XmcException;
import com.cuc.miti.phone.xmc.http.RemoteCaller;
import com.cuc.miti.phone.xmc.logic.UserService;
import com.cuc.miti.phone.xmc.services.AppStatusService;
import com.cuc.miti.phone.xmc.services.FileUploadService;
import com.cuc.miti.phone.xmc.services.FtpAddNewAccObserver;
import com.cuc.miti.phone.xmc.services.LocationService;
import com.cuc.miti.phone.xmc.services.ReceiverService;
import com.cuc.miti.phone.xmc.utils.DeviceInfoHelper;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.XmcNotification;
import com.cuc.miti.phone.xmc.utils.uploadtask.UploadTaskImpl;
import com.cuc.miti.phone.xmc.utils.uploadtask.UploadTaskInterface;
import com.cuc.miti.phone.xmc.utils.uploadtask.UploadTaskJob;
import com.cuc.miti.phone.xmc.xmpp.NotificationService;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceKeys;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceType;
import com.cuc.miti.phone.xmc.utils.SharedPreferencesHelper;


public class IngleApplication extends Application implements UncaughtExceptionHandler{
    
	public static String TAG="XINHUA'S PROJECT";
	public static boolean isStop=false;
	public User currentUserInfo = null;
	private UserService userService = null;
	private static DeviceInfoHelper deviceInfoHelper =null;
	
	private ArrayList<UploadTaskJob> mQueuedUploads;
	private ArrayList<UploadTaskJob> mCompletedUploads;
	private ArrayList<UploadTaskJob> mUploadings;
	private ArrayList<Activity> activityList;				//�򿪹��Activity�б�
	
	private static LoginStatus loginStatus = null;		//��ǰ��¼״̬
	private static NetStatus netStatus = null;									//��ǰ����״̬
	private static String sessionId;													//���������ص�SessionId
	public static Object mLock;
	
	public static ManuscriptTemplate mtTemplate =null;										//ȫ�ָ�ǩ�����𱣴��ϴη���ɹ��ĸ�ǩ��
	
	private static XMPPConnection connection;
	
	private static FtpAddNewAccObserver listenerFtp;
	
	// ���嶨ʱ��
	protected Timer timer = new Timer();
	private TimerTask updateUserInfoTask;						//��ʱ���������UserInfo,ÿ24Сʱһ��
	private TimerTask keepAliveTask;									//��ʱ�����񱣳���������ĻỰ״̬(KeepAlive ÿСʱһ��)
	
	private static final int MESSAGE_KEEPALIVE = 0;										//��ʱ������������ĻỰ״̬
	private static final int MESSAGE_UPDATE_USERINFO = 1;						//��ʱ����UserInfo

	//Ӧ��ʵ��
	private static IngleApplication instance;
	
	public static IngleApplication getInstance(){
		return instance;
	}

	public void exit(){
		for(Activity activity : activityList){
			try {
				activity.finish();
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
		}
		
		IngleApplication.isStop=true;
		
		Intent appStatusIntent=new Intent(this,AppStatusService.class);
		stopService(appStatusIntent);
		
		Intent locationIntent = new Intent(this, LocationService.class);
		stopService(locationIntent);
		
//		Intent notificationIntent = new Intent(this, NotificationService.class);
	//	stopService(notificationIntent);
		
		Intent receiverServiceIntent=new Intent(this,ReceiverService.class);
		stopService(receiverServiceIntent);
		
		Intent uploadIntent=new Intent(this,FileUploadService.class);
		stopService(uploadIntent);
	
		
		XmcNotification xmcNotification =new XmcNotification(this);
		xmcNotification.cancelNotification();
		
		System.exit(0);		
	}
	
	/**
	 * ��ȡ�豸�����Ϣ������
	 * @return
	 */
	public DeviceInfoHelper getDeviceInfoHelper(){
		deviceInfoHelper = new DeviceInfoHelper();
		return deviceInfoHelper;
	}
	
	public static String getSessionId() {
		return sessionId;
	}

	public static void setSessionId(String sessionId) {
		IngleApplication.sessionId = sessionId;
	}
	
	public static NetStatus getNetStatus() {
		return netStatus;
	}

	public static void setNetStatus(NetStatus netStatus) {
		IngleApplication.netStatus = netStatus;
	}
	
	public static LoginStatus getLoginStatus() {
		return loginStatus;
	}

	public static void setLoginStatus(LoginStatus loginStatus) {
		IngleApplication.loginStatus = loginStatus;
	}
	public static ManuscriptTemplate getMtTemplate() {
		return mtTemplate;
	}

	public static void setMtTemplate(ManuscriptTemplate mtTemplate) {
		IngleApplication.mtTemplate = mtTemplate;
	}
	
	//���Activity��������
	public void addActivity(Activity activity){
		activityList.add(activity);
	}
	
	public ArrayList<Activity> getActivities(){
		refreshActivities();
		return activityList;
	}
	public void refreshActivities(){
		for(int i=0;i<activityList.size();i++){
			if(activityList.get(i).isFinishing())
				activityList.remove(i);
		}
	}
	//Intent�����
	HashMap<String, Object> intentHashMap;

	public HashMap<String, Object> getIntentHashMap() {
		if(intentHashMap==null){
			intentHashMap=new HashMap<String, Object>();
		}
		return intentHashMap;
	}

	public void setIntentHashMap(HashMap<String, Object> intentHashMap) {
		this.intentHashMap = intentHashMap;
	}
	
	UploadTaskInterface uploadTaskInterface;
	
	public UploadTaskInterface getUploadTaskInterface() {
		if(uploadTaskInterface==null){
			uploadTaskInterface=new UploadTaskImpl();
		}
		return uploadTaskInterface;
	}
	
	public ArrayList<UploadTaskJob> getQueuedUploads(){
		return mQueuedUploads;
	}
	
	public void setQueuedUploads(ArrayList<UploadTaskJob> mQueuedUploads){
		this.mQueuedUploads=mQueuedUploads;
	}

	public ArrayList<UploadTaskJob> getCompletedUploads() {
		return mCompletedUploads;
	}

	public void setCompletedUploads(ArrayList<UploadTaskJob> mCompletedUploads) {
		this.mCompletedUploads = mCompletedUploads;
	}
	
	public ArrayList<UploadTaskJob> getUploadings() {
		return mUploadings;
	}

	public void setUploadings(ArrayList<UploadTaskJob> mUploadings) {
		this.mUploadings = mUploadings;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "Application onCreate()");
		instance=this;

		ServiceManager.init(this);

		mLock=new Object();
		intentHashMap=new HashMap<String, Object>();
		currentUserInfo = new User();
		mQueuedUploads=new ArrayList<UploadTaskJob>();
		mCompletedUploads=new ArrayList<UploadTaskJob>();
		mUploadings = new ArrayList<UploadTaskJob>();
		userService =new UserService(this);
		loginStatus = LoginStatus.none;
		activityList = new ArrayList<Activity>();
		
//		Intent receiverServiceIntent=new Intent(this,ReceiverService.class);
	//	startService(receiverServiceIntent);
		
		isStop=false;
		Intent appStatusIntent=new Intent(this,AppStatusService.class);
		startService(appStatusIntent);	
		
//		Intent notificationIntent=new Intent(this,NotificationService.class);
//		startService(notificationIntent);
		
		//�����쳣��ֹʱȡ��ϵͳ�����ĳ���Ի���
	    Thread.setDefaultUncaughtExceptionHandler(this);  
			
	}

	/**
	 * ��ʼ����ʱ��
	 */
	public void initialzeUpdateData() {
		// ������ʱ������������ÿ��30�뱣��һ��
		updateUserInfoTask = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message message = new Message();
				message.what = MESSAGE_UPDATE_USERINFO;
				handler.sendMessage(message);
			}
		};		
		timer.schedule(updateUserInfoTask, 50000, 86400000);			//ÿ24Сʱִ��һ��
					
		keepAliveTask = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message message = new Message();
				message.what = MESSAGE_KEEPALIVE;
				handler.sendMessage(message);
			}
		};		
		timer.schedule(keepAliveTask, 100000, 3600000);						//ÿСʱִ��һ��	
	}

	/**
	 * ί�У���ݶ�ʱ����֪ͨ���б�����������Ự״̬�͸����û���Ϣ����
	 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// �����������֮��ĻỰ
			if (msg.what == MESSAGE_KEEPALIVE) {
				keepAliveThread();
			}else if(msg.what == MESSAGE_UPDATE_USERINFO){
				userService.getUserInfo(sessionId, currentUserInfo);
			}
			super.handleMessage(msg);
		}
	};
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		
		// �رն�ʱ������
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}
	
	/**
	 * ���������˻Ự�߳�
	 */
	private void keepAliveThread(){
		new Thread(new Runnable() {
			public void run() {
				try {
					RemoteCaller.KeepAlive(sessionId);
					String[] versionServer = RemoteCaller
							.getAndroidAppVersion();
					SharedPreferencesHelper mSharedPreferencesHelper = new SharedPreferencesHelper(
							getInstance());
					mSharedPreferencesHelper.SaveCommonPreferenceSettings(
							PreferenceKeys.Sys_VersionLowest.toString(),
							PreferenceType.String, versionServer[0]);
					mSharedPreferencesHelper.SaveCommonPreferenceSettings(
							PreferenceKeys.Sys_VersionCurrent.toString(),
							PreferenceType.String, versionServer[1]);
				} catch (XmcException e) {
					e.printStackTrace();
				}
			}

		}).start();
	}
	
	/**
	 * ��ȡ��ǰ�û���
	 * @return
	 */
	public String getCurrentUser(){
		//return "test";
		return currentUserInfo.getUsername();
	}
	public static XMPPConnection getConnection() {
		return connection;
	}

	public static void setConnection(XMPPConnection connection) {
		IngleApplication.connection = connection;
	}
	public static FtpAddNewAccObserver getListenerFtp() {
		return listenerFtp;
	}

	public static void setListenerFtp(FtpAddNewAccObserver listenerFtp) {
		IngleApplication.listenerFtp = listenerFtp;
	}


	public void uncaughtException(Thread thread, Throwable ex) {
		Logger.e(ex);
		exit();
        android.os.Process.killProcess(android.os.Process.myPid());  
        System.exit(1);	
		
	}

	public int getVersionCode() {
		// TODO Auto-generated method stub
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return -1;
        }
	}
}