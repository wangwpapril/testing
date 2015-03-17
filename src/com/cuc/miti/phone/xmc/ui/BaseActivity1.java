package com.cuc.miti.phone.xmc.ui;

import java.util.List;

import com.cuc.miti.phone.xmc.MainHandler;
import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.services.AppStatusService;
import com.cuc.miti.phone.xmc.services.ReceiverService;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.XmcNotification;
import com.cuc.miti.phone.xmc.R;
import com.cuc.miti.phone.xmc.R.id;
import com.cuc.miti.phone.xmc.R.menu;

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
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class BaseActivity1 extends Activity implements OnClickListener{

	ActivityManager activityManager;

	protected BaseActivity1 context;
	protected TextView tvTitleName;
	protected ImageView ivTitelName;
	protected ImageView ivTitleBack;
	protected ImageView ivTitleRight;
	protected EditText ivTitleMiddle;
	protected TextView tvTitleRight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// �����ޱ���
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Log.i(IngleApplication.TAG, "Base Activity onCreate()");
		Logger.logHeap(this.getClass()); 
		activityManager=(ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
		IngleApplication.getInstance().addActivity(this);
		context = this;

	}
	
	protected void initTitleView() {
		tvTitleName = (TextView) findViewById(R.id.title_name);
		ivTitelName = (ImageView)findViewById(R.id.title_name_iv);
		ivTitleBack = (ImageView) findViewById(R.id.title_iv_back);
		ivTitleRight = (ImageView) findViewById(R.id.title_iv_right);
		ivTitleMiddle = (EditText) findViewById(R.id.title_name_et);
		tvTitleRight = (TextView) findViewById(R.id.title_tv_right);
		initTitle();
	}

	protected abstract void initTitle();
	

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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	public void onBackClick(View view){
		onBackPressed();
	}

	
}
