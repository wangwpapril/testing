package com.cuc.miti.phone.xmc.utils;

import android.os.Environment;

public class DownloadHelper {

	public static String getDownloadPath(){
		return Environment.getExternalStorageDirectory().getAbsolutePath()+"/shiqing/";
	}
}
