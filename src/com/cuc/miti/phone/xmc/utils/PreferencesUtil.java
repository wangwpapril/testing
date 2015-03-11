package com.cuc.miti.phone.xmc.utils;


import com.cuc.miti.phone.xmc.IngleApplication;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtil {
	
	private static final String CONFIG_FILE_NAME = "ingle_prefs";

	public static void saveValue(String string, String optString) {
		SharedPreferences.Editor editor = IngleApplication.getInstance().getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(string, optString);
        editor.commit();
	}

	public static String getValue(String sinaTag) {
		return IngleApplication.getInstance().getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE).getString(sinaTag, "");
	}

}
