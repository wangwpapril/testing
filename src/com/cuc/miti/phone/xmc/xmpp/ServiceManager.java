package com.cuc.miti.phone.xmc.xmpp;

import java.util.Properties;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/** 
 * androidpn�ͻ��˵�����������NotificationServiceͨ�����ں�̨ά��androidpn����
 * This class is to manage the notification service and to load the configuration.
 *
 * @author SongQing
 */
public final class ServiceManager {

    private static final String LOGTAG = LogUtil.makeLogTag(ServiceManager.class);

    private Context mContext;
    private SharedPreferences sharedPrefs;
    private Properties props;
    private String version = "0.5.0";
    private String apiKey;
    private String xmppHost;
    private String xmppPort;
    private String callbackActivityPackageName;
    private String callbackActivityClassName;

    public ServiceManager(Context context) {
        this.mContext = context;

        if (context instanceof Activity) {
            Log.i(LOGTAG, "Callback Activity...");
            Activity callbackActivity = (Activity) context;
            callbackActivityPackageName = callbackActivity.getPackageName();
            callbackActivityClassName = callbackActivity.getClass().getName();
        }

        props = loadProperties();
        apiKey = props.getProperty("apiKey", "");
        //xmppHost = props.getProperty("xmppHost", "218.94.104.166");
        xmppHost = props.getProperty("xmppHost", "chat.xinhuaenews.com");
        
        xmppPort = props.getProperty("xmppPort", "15222");
        Log.i(LOGTAG, "apiKey=" + apiKey);
        Log.i(LOGTAG, "xmppHost=" + xmppHost);
        Log.i(LOGTAG, "xmppPort=" + xmppPort);

        sharedPrefs = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString(Constants.API_KEY, apiKey);
        editor.putString(Constants.VERSION, version);
        editor.putString(Constants.XMPP_HOST, xmppHost);
        editor.putInt(Constants.XMPP_PORT, Integer.parseInt(xmppPort));
        editor.putString(Constants.CALLBACK_ACTIVITY_PACKAGE_NAME,callbackActivityPackageName);
        editor.putString(Constants.CALLBACK_ACTIVITY_CLASS_NAME,callbackActivityClassName);
        editor.commit();
    }
    
    /**
     * ��res/raw�м���xmpp.propertites
     * @return
     */
    private Properties loadProperties() {
        Properties props = new Properties();
        try {
            int id = mContext.getResources().getIdentifier("xmpp", "raw",mContext.getPackageName());
            props.load(mContext.getResources().openRawResource(id));
        } catch (Exception e) {
            Log.e(LOGTAG, "Could not find the properties file.", e);
        }
        return props;
    }

    public void startService() {
        Thread serviceThread = new Thread(new Runnable() {
            public void run() {
//                Intent intent = NotificationService.getIntent();
  //              mContext.startService(intent);
            }
        });
        serviceThread.start();
    }

    public void stopService() {
        Intent intent = NotificationService.getIntent();
        mContext.stopService(intent);
    }
    
    public void setNotificationIcon(int iconId) {
        Editor editor = sharedPrefs.edit();
        editor.putInt(Constants.NOTIFICATION_ICON, iconId);
        editor.commit();
    }

   
    public static void viewNotificationSettings(Context context) {
 //       Intent intent = new Intent().setClass(context,NotificationSettingsActivity.class);
   //     context.startActivity(intent);
    }

}
