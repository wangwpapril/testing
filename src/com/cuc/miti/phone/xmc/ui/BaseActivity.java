package com.cuc.miti.phone.xmc.ui;

import java.util.List;






import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.R;
import com.cuc.miti.phone.xmc.utils.Logger;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class BaseActivity extends Activity{

	ActivityManager activityManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// �����ޱ���
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Log.i(IngleApplication.TAG, "Base Activity onCreate()");
		Logger.logHeap(this.getClass()); 
		activityManager=(ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
//		XinhuaManuscriptCollectionApplication.getInstance().addActivity(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		this.getMenuInflater().inflate(R.menu.options_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_logout:
			
			IngleApplication.isStop=true;
			
			IngleApplication.getInstance().exit();
			break;

		default:
			break;
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(IngleApplication.TAG, "Base Activity onDestory()");
		Logger.logHeap(this.getClass()); 
		
	}

	
	
}
