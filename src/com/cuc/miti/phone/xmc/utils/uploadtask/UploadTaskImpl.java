package com.cuc.miti.phone.xmc.utils.uploadtask;

import android.content.Intent;
import android.os.Bundle;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.services.FileUploadService;

public class UploadTaskImpl implements UploadTaskInterface {

	public void addToUploadQueue(UploadTaskJob job) {
		// TODO Auto-generated method stub

		Intent intent = new Intent(IngleApplication.getInstance(),FileUploadService.class);
		
//		HashMap<String, Object> map=IngleApplication.getInstance().getIntentHashMap();		
//		map.put(FileUploadService.EXTRA_UPLOADJOB, job);
		
		Bundle bundle=new Bundle();
		bundle.putSerializable(FileUploadService.SER_KEY, job);
		intent.putExtras(bundle);
		intent.setAction(FileUploadService.ACTION_ADD_TO_UPLOAD);
		IngleApplication.getInstance().startService(intent);
	}

}
