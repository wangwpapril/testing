package com.cuc.miti.phone.xmc.xmpp;

import java.util.ArrayList;
import java.util.Random;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.ui.MainActivity;
import com.cuc.miti.phone.xmc.ui.SplashScreenActivity;
import com.cuc.miti.phone.xmc.xmpp.Constants;
import com.cuc.miti.phone.xmc.xmpp.LogUtil;
import com.cuc.miti.phone.xmc.xmpp.Notifier;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/** 
 * This class is to notify the user of messages with NotificationManager.
 *
 * @author SongQing
 */
public class Notifier {

	 private static final String LOGTAG = LogUtil.makeLogTag(Notifier.class);

	    private static final Random random = new Random(System.currentTimeMillis());

	    private Context context;

	    private SharedPreferences sharedPrefs;

	    private NotificationManager notificationManager;
	    
	    

	    public Notifier(Context context) {
	        this.context = context;
	        this.sharedPrefs = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
	        this.notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
	    }

	    public void notify(String messageId, String messageType, String messageSender,String messageReceiver, String messageContent) {
	        Log.d(LOGTAG, "notify()...");

	        Log.d(LOGTAG, "messageId=" + messageId);
	        Log.d(LOGTAG, "messageType=" + messageType);
	        Log.d(LOGTAG, "messageSender=" + messageSender);
	        Log.d(LOGTAG, "messageReceiver=" + messageReceiver);
	        Log.d(LOGTAG, "messageContent=" + messageContent);

	       /* if (isNotificationEnabled()) {
	            // Show the toast
	            if (isNotificationToastEnabled()) {			//չʾ��ʱ��Ϣ
	                ToastHelper.showToast(messageContent, Toast.LENGTH_SHORT);
	            }

	            // Notification
	            Notification notification = new Notification();
	            notification.icon = getNotificationIcon();
	            notification.defaults = Notification.DEFAULT_LIGHTS;
	            if (isNotificationSoundEnabled()) {
	                notification.defaults |= Notification.DEFAULT_SOUND;
	            }
	            if (isNotificationVibrateEnabled()) {
	                notification.defaults |= Notification.DEFAULT_VIBRATE;
	            }
	            notification.flags |= Notification.FLAG_AUTO_CANCEL;
	            notification.when = System.currentTimeMillis();
	            notification.tickerText = messageContent;

	            Intent intent = new Intent(context,MessageDetailsActivity.class);
	            intent.putExtra(Constants.MESSAGE_ID, messageId);
	            intent.putExtra(Constants.MESSAGE_TYPE,messageType);
	            intent.putExtra(Constants.MESSAGE_SENDER,messageSender);
	            intent.putExtra(Constants.MESSAGE_RECEIVER,messageReceiver);
	            intent.putExtra(Constants.MESSAGE_CONTENT, messageContent);
	            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	            intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
	            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
	            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	          
	            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
	            
	            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,this.getActivityIntentForBack(), PendingIntent.FLAG_CANCEL_CURRENT);

	            notification.setLatestEventInfo(context, messageSender, messageContent,contentIntent);
	            notificationManager.notify(random.nextInt(), notification);
	            
	            
	        } else {
	            Log.w(LOGTAG, "Notificaitons disabled.");
	        }*/
	       
	    		 	Notification notification = new Notification();
	    	        notification.icon = getNotificationIcon();
	    	        notification.defaults = Notification.DEFAULT_LIGHTS;
	    	        if (isNotificationSoundEnabled()) {
	    	            notification.defaults |= Notification.DEFAULT_SOUND;
	    	        }
	    	        if (isNotificationVibrateEnabled()) {
	    	            notification.defaults |= Notification.DEFAULT_VIBRATE;
	    	        }
	    	        notification.flags |= Notification.FLAG_AUTO_CANCEL;
	    	        notification.when = System.currentTimeMillis();
	    	        notification.tickerText = messageContent;
	    	        
	    	      //���涼�����õ��Notificationͼ�����Ҫ��س���򿪵�Activity
	    			ArrayList<Activity> activities = IngleApplication.getInstance().getActivities();
	    			Intent backIntent = new Intent();
	    			if (activities != null && activities.size()>0) {
	    				backIntent = new Intent(context, activities.get(activities.size() - 1).getClass());
	    			} else {
	    				if(IngleApplication.getInstance().getCurrentUser() !=null &&
	    						IngleApplication.getInstance().getCurrentUser()!=""){
	    					backIntent = new Intent(context, MainActivity.class);
	    				}else{//û�е�¼
	    					backIntent = new Intent(context, SplashScreenActivity.class);
	    				}	
	    			}

	    	        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,backIntent, PendingIntent.FLAG_CANCEL_CURRENT);

	    	        notification.setLatestEventInfo(context, messageSender, messageContent,contentIntent);
	    	        notificationManager.notify(random.nextInt(), notification);
	    }

	    private int getNotificationIcon() {
	        return sharedPrefs.getInt(Constants.NOTIFICATION_ICON, 0);
	    }

	    private boolean isNotificationEnabled() {
	        return sharedPrefs.getBoolean(Constants.SETTINGS_NOTIFICATION_ENABLED,true);
	    }

	    private boolean isNotificationSoundEnabled() {
	        return sharedPrefs.getBoolean(Constants.SETTINGS_SOUND_ENABLED, true);
	    }

	    private boolean isNotificationVibrateEnabled() {
	        return sharedPrefs.getBoolean(Constants.SETTINGS_VIBRATE_ENABLED, true);
	    }

	    private boolean isNotificationToastEnabled() {
	        return sharedPrefs.getBoolean(Constants.SETTINGS_TOAST_ENABLED, false);
	    }

	    

		/*------------------------------------˽�з���------------------------------------*/
		
		/**
		 * ��ɵ��ϵͳ״̬���еĳ���ͼ�����Ҫ�򿪵�Activity
		 * @return
		 */
		private Intent getActivityIntentForBack(){
			//���涼�����õ��Notificationͼ�����Ҫ��س���򿪵�Activity
			ArrayList<Activity> activities = IngleApplication.getInstance().getActivities();
			Intent backIntent = new Intent();
			if (activities != null && activities.size()>0) {
				backIntent = new Intent(context, activities.get(activities.size() - 1).getClass());
			} else {
				if(IngleApplication.getInstance().getCurrentUser() !=null &&
						IngleApplication.getInstance().getCurrentUser()!=""){
					backIntent = new Intent(context, MainActivity.class);
				}else{//û�е�¼
					backIntent = new Intent(context, SplashScreenActivity.class);
				}	
			}
			
			//��ת����activity������ջ�д��ڣ������ϵ�activity�������
			backIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);	
			
			return backIntent;
		}	
}
