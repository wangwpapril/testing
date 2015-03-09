package com.cuc.miti.phone.xmc.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.adapter.ManagementTemplateAdapter;
import com.cuc.miti.phone.xmc.adapter.MessageAdapter;
import com.cuc.miti.phone.xmc.domain.ManuscriptTemplate;
import com.cuc.miti.phone.xmc.domain.MessageForUs;
import com.cuc.miti.phone.xmc.domain.MessageForPush;
import com.cuc.miti.phone.xmc.domain.XmcException;
import com.cuc.miti.phone.xmc.domain.Enums.MessageType;
import com.cuc.miti.phone.xmc.domain.Enums.MsgOwnerType;
import com.cuc.miti.phone.xmc.domain.Enums.MsgSendStatus;
import com.cuc.miti.phone.xmc.domain.Enums.NetStatus;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceKeys;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceType;
import com.cuc.miti.phone.xmc.domain.Enums.ReadOrNotType;
import com.cuc.miti.phone.xmc.domain.Enums.SendOrReceiveType;
import com.cuc.miti.phone.xmc.domain.Enums.TemplateType;
import com.cuc.miti.phone.xmc.http.RemoteCaller;
import com.cuc.miti.phone.xmc.logic.MessageService;
import com.cuc.miti.phone.xmc.logic.UserService;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.SharedPreferencesHelper;
import com.cuc.miti.phone.xmc.utils.TimeFormatHelper;
import com.cuc.miti.phone.xmc.utils.ToastHelper;
import com.cuc.miti.phone.xmc.utils.TouchEffect;
import com.cuc.miti.phone.xmc.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MessageActivity extends BaseActivity implements OnClickListener {
	protected static final int REQUEST_UPDATE_MsgNewCount = 4;
	protected static final int MSG_UPDATE = 0;
	protected static final int MSG_UPDATE_FAILED = 1;

	private ImageButton iBtnBack;
	private ListView lvMessage; // �û��Զ���ģ��ListView
	private Button btnRefresh;
	private MessageAdapter adapter;

	private List<String> messageTypeList;

	private UserService userService;

	private MessageService messageService;
	MainHandler mHandler = null;
	Intent mIntent;
	private MessageType messageType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message);

		this.initialize();

		IngleApplication.getInstance().addActivity(this);
	}

	/**
	 * ��ʼ��
	 */
	private void initialize() {

		userService = new UserService(MessageActivity.this);
		messageService = new MessageService(MessageActivity.this);
		mHandler = new MainHandler();

		this.setUpViews();
		this.initializeListView();

	}

	/**
	 * ��ʼ��ҳ��ؼ�
	 */
	private void setUpViews() {
		lvMessage = (ListView) findViewById(R.id.lvMessage_MCA);
		iBtnBack = (ImageButton) findViewById(R.id.iBtnBack_MCA);
		iBtnBack.setOnClickListener(this);
		iBtnBack.setOnTouchListener(TouchEffect.TouchDark);

		btnRefresh = (Button) findViewById(R.id.btnRefresh_MCA);
		btnRefresh.setOnClickListener(this);
	}

	/**
	 * ��ʼ��ListView
	 */
	private void initializeListView() {
		try {

			messageTypeList = new ArrayList<String>();
			for (MessageType mt : MessageType.values()) {
				messageTypeList.add(mt.toString());
			}

		} catch (Exception e) {
			Logger.e(e);
		}

		adapter = new MessageAdapter(messageTypeList, MessageActivity.this);
		lvMessage.setAdapter(adapter);

		lvMessage.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String msgType = messageTypeList.get(position).toString();
				messageType = MessageType.valueOf(msgType);

				switch (messageType) {
				case SystemMsg:
					mIntent = new Intent(MessageActivity.this,
							MessageSecondActivity.class);
					Bundle mBundle = new Bundle();
					mBundle.putString("msgType", msgType);
					mIntent.putExtras(mBundle);
					startActivityForResult(mIntent, REQUEST_UPDATE_MsgNewCount);
					break;
				case RecommendMsg:
					mIntent = new Intent(MessageActivity.this,
							MessageSecondForRecommendActivity.class);
					Bundle lBundle = new Bundle();
					lBundle.putString("msgType", msgType);
					mIntent.putExtras(lBundle);
					startActivityForResult(mIntent, REQUEST_UPDATE_MsgNewCount);
					break;
				case InstantMsg:
				case XMPPMsg:
					mIntent = new Intent(MessageActivity.this,
							MessageSecondForInstantActivity.class);
					Bundle iBundle = new Bundle();
					iBundle.putString("msgType", msgType);
					mIntent.putExtras(iBundle);
					startActivityForResult(mIntent, REQUEST_UPDATE_MsgNewCount);
					break;
				
				case OaMsg:
					mIntent = new Intent(MessageActivity.this,
							MessageListForOaActivity.class);
					startActivityForResult(mIntent, REQUEST_UPDATE_MsgNewCount);

				default:
					break;

				}

			}
		});

	}

	private void updateMsgListView() {
		adapter.notifyDataSetChanged();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);


		if (resultCode == Activity.RESULT_OK) {
			initializeListView();
			updateMsgListView();
//			switch (requestCode) {
//			case REQUEST_UPDATE_MsgNewCount:
//				initializeListView();
//				updateMsgListView();
//				break;
//			default:
//				break;
//			}
		}
	}

	public void update() {
		initializeListView();
		updateMsgListView();
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iBtnBack_MCA:
			setResult(RESULT_OK);
			finish();
			break;
		case R.id.btnRefresh_MCA:
			getMessageThread();
			break;
		default:
			break;
		}

	}

	/**
	 * Handler class implementation to handle the message
	 * 
	 * @author SongQing
	 * 
	 */
	private class MainHandler extends Handler {

		// This method is used to handle received messages
		public void handleMessage(Message msg) {
			// switch to identify the message by its code
			switch (msg.what) {
			case MSG_UPDATE:
				// Toast.makeText(MessageActivity.this,"��Ϣ������ϣ�",Toast.LENGTH_SHORT).show();
				ToastHelper.showToast(getResources().getString(
						R.string.msgUpdateFinished_MCA), Toast.LENGTH_SHORT);
				break;
			case MSG_UPDATE_FAILED:
				// Toast.makeText(MessageActivity.this,"��Ϣ����ʧ�ܣ�",Toast.LENGTH_SHORT).show();
				ToastHelper.showToast(getResources().getString(
						R.string.msgUpdateFailed_MCA), Toast.LENGTH_SHORT);
				break;

			default:
				break;
			}
		}
	}

	/**
	 * ��Handler����������Ϣ
	 * 
	 * @param what
	 * @param obj
	 */
	private void sendMessage(int what, Object obj) {
		Message msg = mHandler.obtainMessage();
		msg.what = what;
		msg.obj = obj;
		mHandler.sendMessageDelayed(msg, 500);
	}

	private void getMessageThread() {
		new Thread(new Runnable() {
			public void run() {

				try {
					synchronized (IngleApplication.mLock) {
						while (IngleApplication
								.getNetStatus() == NetStatus.Disable) {
							sendMessage(MSG_UPDATE_FAILED, null);
							IngleApplication.mLock.wait();
						}

					}
				} catch (Exception e) {
					Logger.e(e);
					e.printStackTrace();
				}

				messageService.getMessageFromRemote();
				sendMessage(MSG_UPDATE, null);
			}
		}).start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// KeyEvent.KEYCODE_BACK��?�ز���.
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(RESULT_OK);
			finish();
		}
		return true;
	}

}
