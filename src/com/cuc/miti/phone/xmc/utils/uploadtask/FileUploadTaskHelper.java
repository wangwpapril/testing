package com.cuc.miti.phone.xmc.utils.uploadtask;

import android.util.Log;

import com.cuc.miti.phone.xmc.IngleApplication;

public class FileUploadTaskHelper {

	public static void addToUploads(UploadTaskJob job){
		
		
		Log.i(IngleApplication.TAG, "addToUploads"+job.getMUploadTask().getXmlurl());
		UploadTaskInterface uploadInterface=(UploadTaskInterface) IngleApplication.getInstance().getUploadTaskInterface();
		uploadInterface.addToUploadQueue(job);
		
	}
}
