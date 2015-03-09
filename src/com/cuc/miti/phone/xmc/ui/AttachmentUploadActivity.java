package com.cuc.miti.phone.xmc.ui;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.MessageForPush;
import com.cuc.miti.phone.xmc.domain.UserAttribute;
import com.cuc.miti.phone.xmc.domain.XmcException;
import com.cuc.miti.phone.xmc.http.DoRemoteResult;
import com.cuc.miti.phone.xmc.http.RemoteCaller;
import com.cuc.miti.phone.xmc.utils.DownloadHelper;
import com.cuc.miti.phone.xmc.utils.Format;
import com.cuc.miti.phone.xmc.utils.XMLDataHandle;
import com.cuc.miti.phone.xmc.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class AttachmentUploadActivity extends BaseActivity implements
		OnClickListener {

	TextView filePathTextView, resultTextView, sessionidTextView,
			topicTextView, d_proTextView;
	Button uploadButton, chooseButton, loginButton, getUserInfoButton,
			xmlUpdateButton, downloadButton,pauseButton;
	String path;
	public ProgressBar taskProgressBar, d_taskProgressBar;

	public static int FOLDER_FILE = 1;
	public static int CAMERA_FILE = 2;

	public static final int MSG_LOGIN = 3;
	public static final int MSG_GETUSERIINFO = 4;
	public static final int MSG_GETTOPICLIST = 5;
	public static final int MSG_GETTOPICLIST_START = 6;
	public static final int MSG_GETTOPICLIST_PRO = 7;
	public static final int MSG_GETTOPICLIST_END = 8;
	public static final int MSG_GETMESSAGE=9;

	public String topicList = "";	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//
//		IngleApplication.getInstance().getActivities()
//				.add(this);
//		setContentView(R.layout.attachment_upload);
//
//		setupViews();
//		IngleApplication.getInstance().addActivity(this);
	}

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	protected void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//	}
//
//	@Override
//	protected void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
//	}
//
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//	}
//
//	@Override
//	protected void onStart() {
//		// TODO Auto-generated method stub
//		super.onStart();
//	}
//
//	@Override
//	protected void onStop() {
//		// TODO Auto-generated method stub
//		super.onStop();
//	}
//
//	private void setupViews() {
//		filePathTextView = (TextView) findViewById(R.id.filepath_textview);
//		resultTextView = (TextView) findViewById(R.id.result_textview);
//		sessionidTextView = (TextView) findViewById(R.id.sessionid_textview);
//		topicTextView = (TextView) findViewById(R.id.topiclist_textview);
//		d_proTextView = (TextView) findViewById(R.id.d_result_textview);
//		uploadButton = (Button) findViewById(R.id.uploadBtn);
//		chooseButton = (Button) findViewById(R.id.chooseBtn);
//		loginButton = (Button) findViewById(R.id.loginBtn);
//		getUserInfoButton = (Button) findViewById(R.id.getUserInfoBtn);
//		xmlUpdateButton = (Button) findViewById(R.id.XMLUpdateBtn);
//		downloadButton = (Button) findViewById(R.id.downloadBtn);
//		pauseButton=(Button)findViewById(R.id.pauseBtn);
//
//		uploadButton.setOnClickListener(this);
//		chooseButton.setOnClickListener(this);
//		loginButton.setOnClickListener(this);
//		getUserInfoButton.setOnClickListener(this);
//		xmlUpdateButton.setOnClickListener(this);
//		downloadButton.setOnClickListener(this);
//		pauseButton.setOnClickListener(this);
//
//		taskProgressBar = (ProgressBar) findViewById(R.id.task_progress);
//		d_taskProgressBar = (ProgressBar) findViewById(R.id.d_task_progress);
//
//	}
//
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch (v.getId()) {
//		case R.id.loginBtn:
//			this.Login();
//
//			break;
//		case R.id.getUserInfoBtn:
//
//			this.GetUserInfo();
//			break;
//		case R.id.XMLUpdateBtn:
//
//			this.GetTopicList();
//
//			break;
//		case R.id.downloadBtn:
//			GetMessage();
//		
//			break;
//		case R.id.uploadBtn:
//
//			final String[] urls = { path, "/mnt/sdcard/com.cuc.miti.phone.xmc/test1/Accessories/Cache/Mnews2B009001_20120618_ENPSN0.xml" };
////			job = new UploadJob(urls);
////
////			FileUploadHelper.addToUploads(job);
//
//			break;
//		case R.id.pauseBtn:
//			/*if("��ͣ".equals(pauseButton.getText())){
//				job.getUploadTask().pause();
//				pauseButton.setText("����");
//			}else if("����".equals(pauseButton.getText())){
//				job.getUploadTask().continued();
//				pauseButton.setText("��ͣ");
//			}*/
//			
////			job.getUploadTask().cancel(false);
//			
//			break;
//		case R.id.chooseBtn:
//			Intent intent = new Intent();
//			intent.setType("image/*");
//			intent.setAction(Intent.ACTION_GET_CONTENT);
//
//			startActivityForResult(Intent.createChooser(intent, "Select File"),
//					FOLDER_FILE);
//			break;
//		default:
//			break;
//		}
//	}
//	
//	private void GetMessage(){
//		
//		new  Thread(new Runnable() {
//			
//			public void run() {
//				// TODO Auto-generated method stub
//				
//				ArrayList<MessageForPush> messages=new ArrayList<MessageForPush>();
//				try {
//					
////					messages=RemoteCaller.getMessage(sessionId, loginname, lasttime)("", "test1");
//					if(messages!=null){
//						sendMessage(MSG_GETMESSAGE, messages.get(0).getMsgOwner());
//					}else {
//						sendMessage(MSG_GETMESSAGE, "Messages is null");
//					}
//					
//					
//					
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					sendMessage(MSG_GETMESSAGE, e.getMessage());
//				}
//				
//			}
//		}).start();
//	}
//
//	private void Login() {
//
//		new Thread(new Runnable() {
//
//			String[] ss;
//
//			public void run() {
//				// TODO Auto-generated method stub
//				try {
//					ss = DoRemoteResult.doResult(RemoteCaller.Login("test1",
//							"test1"));
//
//					if ("0".equals(ss[0])) {
//						IngleApplication.getInstance()
//								.setSessionId(Format.replaceBlank(ss[1]));
//					}
//
//					Message msg = mHandler.obtainMessage();
//					msg.what = AttachmentUploadActivity.MSG_LOGIN;
//					msg.obj = ss[1];
//					mHandler.sendMessage(msg);
//
//				} catch (XmcException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//			}
//		}).start();
//	}
//
//	private void GetTopicList() {
//
//		new Thread(new Runnable() {
//
//			public void run() {
//				// TODO Auto-generated method stub
//				// BaseDataService service=new
//				// BaseDataService(AttachmentUploadActivity.this);
//				// String topicList=service.GetCurrentBDFilesNameString();
//
//				String topicList = "topiclist.cnml-Department-85.xml,topiclist.cnml-Language-6.xml,topiclist.cnml-MediaType-1.xml,"
//						+ "topiclist.cnml-Priority-1.xml,topiclist.cnml-WorldLocationCategory-1.xml,topiclist.cnml-XH_GeographyCategory-5.xml,"
//						+ "topiclist.cnml-XH_InternalInternational-4.xml,topiclist.cnml-XH_NewsCategory-7.xml";
//
//				try {
//
//					String result = RemoteCaller.GetTopicList(topicList);
//					if (StringUtils.isNotBlank(result)) {
//						// ����
//						 String[] fileNames = result.split(",");
//
//						for (int i = 0; i < fileNames.length; i++) {
//							DownloadBaseData(fileNames[i]);
//						}
//						sendMessage(MSG_GETTOPICLIST_END, "�������");
//
//					} else {
//						sendMessage(MSG_GETTOPICLIST, null);
//					}
//
//				} catch (XmcException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					sendMessage(MSG_GETTOPICLIST, null);
//				}
//
//			}
//		}).start();
//	}
//
//	private void GetUserInfo() {
//
//		new Thread(new Runnable() {
//
//			String[] ss;
//			String sessionId = IngleApplication
//					.getInstance().getSessionId();
//
//			public void run() {
//				// TODO Auto-generated method stub
//				try {
//					ss = DoRemoteResult.doResult(RemoteCaller.GetUserInfo(
//							sessionId, "testapp"));
//
//					UserAttribute userAttribute = XMLDataHandle
//							.ParserUserInfo(new ByteArrayInputStream(ss[1]
//									.getBytes()));
//
//					if (Integer.valueOf(ss[0]) != 0) {
//						sendMessage(MSG_GETUSERIINFO, "GET Error");
//					} else {
//						sendMessage(MSG_GETUSERIINFO, userAttribute.getUserNameC());
//					}
//
//				} catch (XmcException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			}
//		}).start();
//	}
//
//	Handler mHandler = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			// TODO Auto-generated method stub
//
//			switch (msg.what) {
//			case MSG_LOGIN:
//				sessionidTextView.setText(msg.obj.toString());
//				Log.i(IngleApplication.TAG, "��¼�ɹ��� "
//						+ msg.obj.toString());
//
//				break;
//			case MSG_GETUSERIINFO:
//				Toast.makeText(AttachmentUploadActivity.this,
//						"The Operation is done  " + msg.obj.toString(),
//						Toast.LENGTH_SHORT).show();
//				break;
//
//			case MSG_GETTOPICLIST_START:
//
//				topicTextView.setText(msg.obj.toString());
//				break;
//			case MSG_GETTOPICLIST_PRO:
//
//				d_proTextView.setText(msg.obj.toString() + "%");
//				d_taskProgressBar.setProgress(Integer.valueOf(msg.obj
//						.toString()));
//				break;
//			case MSG_GETTOPICLIST_END:
//
//				topicTextView.setText(msg.obj.toString());
//				break;
//			case MSG_GETMESSAGE:
//				
//				topicTextView.setText(msg.obj.toString());
//				break;
//			default:
//				break;
//			}
//			super.handleMessage(msg);
//
//		}
//
//	};
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//		if (resultCode == RESULT_OK) {
//			if (requestCode == FOLDER_FILE) {
//				Uri returnPicUri = data.getData();
//				final Cursor cursor = getContentResolver().query(returnPicUri,
//						null, null, null, null);
//				cursor.moveToFirst();
//
//				path = cursor.getString(1);
//
//				filePathTextView.setText(path);
//			}
//		}
//
//	}
//
//	/**
//	 * @Description ʹ��GET������ȡ�����XML�ļ�
//	 * @param topicList
//	 *            ,XML�ļ����ļ���
//	 */
//
//	private void DownloadBaseData(String fileName) {
//
//		String path = "http://10.2.1.100/s/topiclist/" + fileName;
//		String downPath=DownloadHelper.getDownloadPath();
//		boolean createDir = new File(downPath).mkdirs();
//		if(createDir){
//			Log.i(IngleApplication.TAG, "��������·��");
//		}
//
//		try {
//			Log.i(IngleApplication.TAG, "��ʼ����" + fileName
//					+ "....");
//
//			sendMessage(MSG_GETTOPICLIST_START, fileName);
//
//			URL url = new URL(path);
//			HttpURLConnection connection = (HttpURLConnection) url
//					.openConnection();
//			connection.setRequestMethod("GET");
//			connection.setDoOutput(true);
//			connection.connect();
//
//			FileOutputStream fStream = new FileOutputStream(new File(Format.replaceBlank(downPath+fileName)));
//			InputStream inputStream = connection.getInputStream();
//			int fileLength = connection.getContentLength();
//			int downloadFileSize = 0;
//
//			byte[] buffer = new byte[1024];
//			int length = 0;
//			while ((length = inputStream.read(buffer)) > 0) {
//				fStream.write(buffer, 0, length);
//				downloadFileSize += length;
//				sendMessage(MSG_GETTOPICLIST_PRO, downloadFileSize * 100
//						/ fileLength);
//			}
//			fStream.close();
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			sendMessage(MSG_GETTOPICLIST_END, e.getMessage());
//		}
//	}
//
//	private void sendMessage(int what, Object obj) {
//		Message msg = mHandler.obtainMessage();
//		msg.what = what;
//		msg.obj = obj;
//		mHandler.sendMessage(msg);
//	}
//
//	@Override
//	public void onBackPressed() {
//		// TODO Auto-generated method stub
//		ArrayList<Activity> activities = IngleApplication
//				.getInstance().getActivities();
//		activities.remove(activities.size() - 1);
//
//		super.onBackPressed();
//		
//	}

}
