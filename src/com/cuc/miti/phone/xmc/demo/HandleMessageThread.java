package com.cuc.miti.phone.xmc.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

/**
 * The relationship between Handler,Message,MessageQueue and Looper
 * 
 * @author SongQing Android��UIϵͳ�Ƿ��̰߳�ȫ�ģ�ֻ�д���UI���߳�(Ҳ�������߳�)�ſ��Զ�UI��̲�����
 *         ��������������߳���ִ����һЩ����������Щ�����Ľ������Ҫͨ��UIչ�ָ��û�������������UI�Ĳ���ת�Ƶ���UI�߳���ִ�С�
 */
public class HandleMessageThread extends Activity {

	final static int SHOW_ALERT = 1025;
	Activity mActivity;
	TextView displayText;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case SHOW_ALERT:
				showAlert((String) msg.obj);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mActivity = this;
		displayText = new TextView(mActivity);
		setContentView(displayText);
		displayText.setText("Ignore me,Please!");
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				String Greetings = "Greetings from another Thread";                
				mHandler.obtainMessage(SHOW_ALERT, Greetings).sendToTarget();
			}
		}.start();
	}

	/**
	 * show message
	 * 
	 * @param content
	 */
	private void showAlert(String content) {
		AlertDialog.Builder builder = new Builder(mActivity);
		builder.setMessage(content);
		builder.create().show();
	}
}
