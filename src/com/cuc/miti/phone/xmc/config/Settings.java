package com.cuc.miti.phone.xmc.config;

import com.cuc.miti.phone.xmc.auth.Session;
import com.cuc.miti.phone.xmc.utils.Util;

import android.app.Activity;
import android.location.Location;


public class Settings {
	
	public static Location location;
	
	public static void systemInit(Activity context) {
		// iniStrictModet();
		initTables();
//		LeyouUtils.setDisplay(context);
		// Session.getLoginObserver().addExecutors(new LoginForServerDataObserver());
		Session.autoLogin(IngleApi.LOGIN, "0");
		location = Util.getLocation(context);
	}
	
	/*private static void iniStrictModet() {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
			.detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog()
			.build());
			
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
			.detectLeakedSqlLiteObjects().penaltyDeath().penaltyLog()
			.build());
	}*/
	
	private static void initTables() {
//		AreaTable.getInstance().init();
	}

}
