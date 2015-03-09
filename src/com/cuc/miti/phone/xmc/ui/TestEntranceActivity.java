package com.cuc.miti.phone.xmc.ui;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.utils.ToastHelper;
import com.cuc.miti.phone.xmc.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class TestEntranceActivity extends BaseActivity implements OnClickListener {

	Button songButton, wenButton, shiButton,guanBtn,llnBtn,caoBtn;

	final int TAKE_PICTURE = 1;

	private static final int RESULT_CAPTURE_IMAGE = 1;// �����requestCode
	private static final int REQUEST_CODE_TAKE_VIDEO = 2;// ����������requestCode
	private static final int RESULT_CAPTURE_RECORDER_SOUND = 3;// ¼����requestCode

	private String strImgPath = "";// ��Ƭ�ļ����·��
	private String strVideoPath = "";// ��Ƶ�ļ��ľ��·��
	private String strRecorderPath = "";// ¼���ļ��ľ��·��

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.testentrance);
		
		Log.i(IngleApplication.TAG, "Test Activity onCreate()");
		
		IngleApplication.getInstance().getActivities().add(this);
		

		// test
		songButton = (Button) findViewById(R.id.button1);
		wenButton = (Button) findViewById(R.id.button2);
		shiButton = (Button) findViewById(R.id.button3);
		guanBtn = (Button)findViewById(R.id.button4);
		llnBtn = (Button)findViewById(R.id.button5);
		caoBtn = (Button)findViewById(R.id.button6);
		

		songButton.setOnClickListener(this);
		wenButton.setOnClickListener(this);
		shiButton.setOnClickListener(this);
		guanBtn.setOnClickListener(this);
		llnBtn.setOnClickListener(this);
		caoBtn.setOnClickListener(this);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(IngleApplication.TAG, "Test Activity onDestory()");
		super.onDestroy();
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			Intent songIntent = new Intent(this, LoginActivity.class);
			startActivity(songIntent);
			break;
		case R.id.button2:
			Intent wenIntent = new Intent(this, WyjTestActivity.class);
			startActivity(wenIntent);
			break;
		case R.id.button3:
			Intent shiIntent=new Intent(this,AttachmentUploadActivity.class);
//			Intent shiIntent=new Intent(this,HomeActivity.class);
			startActivity(shiIntent);
			break;
		case R.id.button4:
			Intent guanIntent = new Intent(this, GuanTestActivity.class);
			startActivity(guanIntent);
			break;
		case R.id.button5:
			Intent llnIntent = new Intent(this, LlnTestActivity.class);
			startActivity(llnIntent);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if (resultCode == Activity.RESULT_OK) {
				if (RESULT_CAPTURE_IMAGE == requestCode) {

					//Toast.makeText(this, strImgPath, Toast.LENGTH_SHORT).show();
					ToastHelper.showToast(strImgPath,Toast.LENGTH_SHORT);
				}
				if (requestCode == REQUEST_CODE_TAKE_VIDEO) {

					Uri uriVideo = data.getData();
					Cursor cursor=this.getContentResolver().query(uriVideo, null, null, null, null);
					
					if (cursor.moveToNext()) {

						strVideoPath = cursor.getString(cursor.getColumnIndex("_data"));
						//Toast.makeText(this, strVideoPath, Toast.LENGTH_SHORT).show();
						ToastHelper.showToast(strVideoPath,Toast.LENGTH_SHORT);
					}
					cursor.close();
				}
				if(requestCode == RESULT_CAPTURE_RECORDER_SOUND){
					Uri uriRecorder = data.getData();
					Cursor cursor=this.getContentResolver().query(uriRecorder, null, null, null, null);
					
					if (cursor.moveToNext()) {
						strRecorderPath = cursor.getString(cursor.getColumnIndex("_data"));
						//Toast.makeText(this, strRecorderPath, Toast.LENGTH_SHORT).show();
						ToastHelper.showToast(strRecorderPath,Toast.LENGTH_SHORT);
					}
					cursor.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void cameraMethod() {
		Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		strImgPath = Environment.getExternalStorageDirectory().toString()
				+ "/CONSDCGMPIC/";// �����Ƭ���ļ���
		String fileName = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date()) + ".jpg";// ��Ƭ����

		File out = new File(strImgPath);
		if (!out.exists()) {
			out.mkdirs();
		}
		out = new File(strImgPath, fileName);
		strImgPath = strImgPath + fileName;//����Ƭ�ľ��·��
		Uri uri = Uri.fromFile(out);
		imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		imageCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult(imageCaptureIntent, RESULT_CAPTURE_IMAGE);
	}
	
	private void videoMethod() {
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
		startActivityForResult(intent, REQUEST_CODE_TAKE_VIDEO);
	}
	
	private void soundRecorderMethod() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("audio/amr");
		startActivityForResult(intent, RESULT_CAPTURE_RECORDER_SOUND);
	}
}
