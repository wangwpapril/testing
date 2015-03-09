package com.cuc.miti.phone.xmc.xmpp;

import android.content.Intent;

/**
 * Static constants for this XMPP package.
 * 
 * @author SongQing
 */
public class Constants {

    public static final String SHARED_PREFERENCE_NAME = "client_preferences";

    // PREFERENCE KEYS
    public static final String CALLBACK_ACTIVITY_PACKAGE_NAME = "CALLBACK_ACTIVITY_PACKAGE_NAME";
    public static final String CALLBACK_ACTIVITY_CLASS_NAME = "CALLBACK_ACTIVITY_CLASS_NAME";
    public static final String API_KEY = "API_KEY";   
    public static final String VERSION = "VERSION";
    public static final String XMPP_HOST = "XMPP_HOST";
    public static final String XMPP_PORT = "XMPP_PORT";
    public static final String XMPP_USERNAME = "XMPP_USERNAME";
    public static final String XMPP_PASSWORD = "XMPP_PASSWORD";

    public static final String DEVICE_ID = "DEVICE_ID";
    public static final String EMULATOR_DEVICE_ID = "EMULATOR_DEVICE_ID";
    public static final String NOTIFICATION_ICON = "NOTIFICATION_ICON";
    public static final String SETTINGS_NOTIFICATION_ENABLED = "SETTINGS_NOTIFICATION_ENABLED";
    public static final String SETTINGS_SOUND_ENABLED = "SETTINGS_SOUND_ENABLED";
    public static final String SETTINGS_VIBRATE_ENABLED = "SETTINGS_VIBRATE_ENABLED";
    public static final String SETTINGS_TOAST_ENABLED = "SETTINGS_TOAST_ENABLED";
    
    public static final String NOTIFICATION_NAMESPACE = "jabber:client"; 

    // Message Fields
    public static final String MESSAGE_ID = "MESSAGE_ID";
    public static final String MESSAGE_TYPE = "MESSAGE_TYPE";
    public static final String MESSAGE_CONTENT = "MESSAGE_CONTENT";
    public static final String MESSAGE_SENDER = "MESSAGE_SENDER";
    public static final String MESSAGE_RECEIVER= "MESSAGE_RECEIVER";

    // INTENT ACTIONS
//    public static final String ACTION_SHOW_NOTIFICATION = "com.cuc.miti.tablet.xmc.xmpp.SHOW_NOTIFICATION";
//    public static final String ACTION_NOTIFICATION_CLICKED = "com.cuc.miti.tablet.xmc.xmpp.NOTIFICATION_CLICKED";
//    public static final String ACTION_NOTIFICATION_CLEARED = "com.cuc.miti.tablet.xmc.xmpp.NOTIFICATION_CLEARED";
    public static final String ACTION_SHOW_NOTIFICATION = "org.androidpn.client.SHOW_NOTIFICATION";
    public static final String ACTION_SHOW_NOTIFICATION_OTHER = "org.androidpn.client.SHOW_NOTIFICATION_OTHER";
    public static final String ACTION_NOTIFICATION_CLICKED = "org.androidpn.client.NOTIFICATION_CLICKED";
    public static final String ACTION_NOTIFICATION_CLEARED = "org.androidpn.client.NOTIFICATION_CLEARED";

	public static final String ACTION_SHOW_NOTIFICATION_ADDF = "org.androidpn.client.SHOW_NOTIFICATION_ADDF";
	public static final String ACTION_SHOW_NOTIFICATION_ADDEDF = "org.androidpn.client.SHOW_NOTIFICATION_ADDEDF";
	public static final String ACTION_SHOW_NOTIFICATION_DENYTOADDF = "org.androidpn.client.SHOW_NOTIFICATION_DENYTOADDF";
	public static final String ACTION_SHOW_NOTIFICATION_DELETEF = "org.androidpn.client.SHOW_NOTIFICATION_DELETEF";
	
	
}
