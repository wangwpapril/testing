package com.cuc.miti.phone.xmc.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.json.JSONException;
import org.json.JSONObject;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.adapter.ManagementTemplateAdapter;
import com.cuc.miti.phone.xmc.adapter.MessageAdapter;
import com.cuc.miti.phone.xmc.adapter.MessageSecondAdapter;
import com.cuc.miti.phone.xmc.domain.ManuscriptTemplate;
import com.cuc.miti.phone.xmc.domain.Manuscripts;
import com.cuc.miti.phone.xmc.domain.MessageForUs;
import com.cuc.miti.phone.xmc.domain.MessageForPush;
import com.cuc.miti.phone.xmc.domain.NewsCategory;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.XmcException;
import com.cuc.miti.phone.xmc.domain.Enums.InterfaceType;
import com.cuc.miti.phone.xmc.domain.Enums.LoginStatus;
import com.cuc.miti.phone.xmc.domain.Enums.MessageType;
import com.cuc.miti.phone.xmc.domain.Enums.MsgOwnerType;
import com.cuc.miti.phone.xmc.domain.Enums.MsgSendStatus;
import com.cuc.miti.phone.xmc.domain.Enums.NetStatus;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceKeys;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceType;
import com.cuc.miti.phone.xmc.domain.Enums.ReadOrNotType;
import com.cuc.miti.phone.xmc.domain.Enums.SendOrReceiveType;
import com.cuc.miti.phone.xmc.domain.Enums.TemplateType;
import com.cuc.miti.phone.xmc.http.Configuration;
import com.cuc.miti.phone.xmc.http.RemoteCaller;
import com.cuc.miti.phone.xmc.logic.MessageService;
import com.cuc.miti.phone.xmc.logic.UserService;
import com.cuc.miti.phone.xmc.ui.control.PullDownListView;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.SharedPreferencesHelper;
import com.cuc.miti.phone.xmc.utils.SmackUtils;
import com.cuc.miti.phone.xmc.utils.StandardizationDataHelper;
import com.cuc.miti.phone.xmc.utils.TimeFormatHelper;
import com.cuc.miti.phone.xmc.utils.ToastHelper;
import com.cuc.miti.phone.xmc.ui.ManageFriendsActivity;
import com.cuc.miti.phone.xmc.xmpp.XmppManager;
import com.cuc.miti.phone.xmc.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class MessageSecondForInstantActivity extends BaseActivity implements
		OnClickListener, PullDownListView.OnRefreshListener {
	private ImageButton iBtnBack;
	private PullDownListView lvMessageSC;

	private LinearLayout linearLayoutProgress;
	private LinearLayout linearLayoutContent;

	private final int MSG_UPDATE = 0;
	private final int MSG_REFRESH = 1;
	private final int MSG_MORE = 2;
	private final int MSG_UPDATE_FAILED = 3;
	protected static final int REQUEST_UPDATE_MsgNewCount = 4;
	private static final int REQUEST_UPDATE_VIEW = 5;
	protected static final int MSG_SEND_SUCCESS = 6;
	protected static final int MSG_SEND_ERROR = 7;
	protected static final int MSG_SEND_FAILED = 8;
	protected static final int MSG_SEND_FAILED_ByNetStatus = 9;
	private static final int CONTEXT_CUSTOM_DELETE_ITEM_MSG = 10;// ɾ����Ϣ
	private static final int CONTEXT_CUSTOM_DELETEALL_ITEM_MSG = 11;// ɾ����Ϣ
	private static final int CONTEXT_CUSTOM_SET_ITEM_DEFAULT_MSG = 12; // �ظ���Ϣ
	private static final int CONTEXT_CUSTOM_REREPLY_ITEM_DEFAULT_MSG = 13; // ���»ظ�

	private Button btnNewBuild;

	private MessageSecondAdapter adapter;

	private String messageNomorepageEnd;

	private int limit; // ��Ϣ��ʾ��ҳ��

	private Button btnEdit_MCA; // �༭
	private TextView textViewTitleSC;
	private TextView textViewCount_MCA;
	private ImageButton ibtnCheckMessage_MCA, ibtnRemoveMessage_MCA; // ѡ�򡢻���վ

	private MessageService messageService;

	private EditText etContent; // ����Ļظ�����
	private String sessionId = "";
	private String loginname = "";
	// �Ƿ�����˱༭״̬
	private boolean editState = false;
	// �Ƿ�ȫѡ
	private boolean checkState = false;

	private MessageType messageType;
	private String sendOrReceive;
	List<MessageForUs> msgList;
	MessageForUs mItem;
	MessageHandler mHandler = null;

	// ����ǵ�һ�λ����ٴλظ���Ϣ
	private String MsgOwnerStr = "";
	// ��ϢID
	int msgId = 0;
	Intent mIntent;
	private static final int PAGESIZE = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_second_instant);

		this.initialize();

		IngleApplication.getInstance().addActivity(this);
	}

	private void initialize() {
		sessionId = IngleApplication.getSessionId();
		loginname = IngleApplication.getInstance()
				.getCurrentUser();
		messageService = new MessageService(
				MessageSecondForInstantActivity.this);

		mHandler = new MessageHandler();
		limit = 1;
		messageNomorepageEnd = getResources().getString(
				R.string.oa_details_nomorepage_end);
		this.setUpViews();
		this.initializeListView();
	}

	private void setUpViews() {
		lvMessageSC = (PullDownListView) findViewById(R.id.lvMessageSC_MCA);
		lvMessageSC
				.setonRefreshListener((PullDownListView.OnRefreshListener) this);

		linearLayoutContent = (LinearLayout) findViewById(R.id.linearLayoutContent_MCA);
		linearLayoutProgress = (LinearLayout) findViewById(R.id.linearLayoutProgress_MCA);

		iBtnBack = (ImageButton) findViewById(R.id.iBtnBackSC_MCA);
		iBtnBack.setOnClickListener(this);

		btnNewBuild = (Button) findViewById(R.id.btnNewBuildSC_MCA);
		btnNewBuild.setOnClickListener(this);

		textViewTitleSC = (TextView) findViewById(R.id.textViewTitleSC_MCA);
		textViewCount_MCA = (TextView) findViewById(R.id.textViewCount_MCA);

		btnEdit_MCA = (Button) findViewById(R.id.btnEdit_MCA);
		btnEdit_MCA.setOnClickListener(this);
		ibtnCheckMessage_MCA = (ImageButton) findViewById(R.id.ibtnCheckMessage_MCA);
		ibtnCheckMessage_MCA.setOnClickListener(this);
		ibtnRemoveMessage_MCA = (ImageButton) findViewById(R.id.ibtnRemoveMessage_MCA);
		ibtnRemoveMessage_MCA.setOnClickListener(this);
	}

	private void initializeListView() {
		try {
			Bundle mBundle = this.getIntent().getExtras();
			String msgType = mBundle.getString("msgType");
			messageType = MessageType.valueOf(msgType);
			// ����Ǽ�ë�Ż��Ǽ�ʱ��Ϣ
			if (messageType.equals(MessageType.XMPPMsg)) {
				btnNewBuild.setVisibility(View.VISIBLE);
				textViewTitleSC.setText(R.string.XMPPMsg_MCA);

			} else {
				btnNewBuild.setVisibility(View.INVISIBLE);
				textViewTitleSC.setText(R.string.instantMsg_MCA);
			}

			List<MessageForUs> tempList = null;
			//msgList = new ArrayList<MessageForUs>();
			onUpdate();

			msgList = messageService.getMessageAllForInst(messageType,
					loginname);
			if(msgList==null) 			
				msgList = new ArrayList<MessageForUs>();
			if (msgList != null) {
				if (msgList.size() < limit * PAGESIZE)
					tempList = msgList.subList(0, msgList.size());
				else
					tempList = msgList.subList(0, limit * PAGESIZE);
			}
			adapter = new MessageSecondAdapter(tempList,
					MessageSecondForInstantActivity.this,
					R.drawable.message_check, R.drawable.message_uncheck);
			lvMessageSC.setAdapter(adapter);
			if (tempList == null) {
				tempList = new ArrayList<MessageForUs>();
			}
			textViewCount_MCA.setText(String.valueOf(tempList.size()));

			// ��ListViewע��Context Menu����ϵͳ��⵽�û�����ĳ��Ԫʱ������Context Menu����
			registerForContextMenu(lvMessageSC);

		} catch (Exception e) {
			Logger.e(e);
		}

		// �趨����¼�
		lvMessageSC.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				if (editState) { // �ж��Ƿ��ڸ���б�༭״̬��
					adapter.checkItem(position - 1); // �ǣ�ֻ�ܽ��и��ѡ��
				} else { // �񣬵������ܸ�Ϊ�༭���

					msgId = adapter.getItem(position - 1).getMsg_id();
					MessageForUs mTemp = messageService.getMessageById(msgId);
					String msgFromStr = "", msgTypeStr = "";

					if (loginname.equals(mTemp.getMsgFrom())) {
						msgFromStr = adapter.getItem(position - 1)
								.getMsgOwner();
					} else {
						msgFromStr = adapter.getItem(position - 1).getMsgFrom();
					}
					msgTypeStr = adapter.getItem(position - 1).getMsgType()
							.toString();

					mIntent = new Intent(MessageSecondForInstantActivity.this,
							MessageThirdForInstantActivity.class);
					Bundle iBundle = new Bundle();
					iBundle.putInt("msgID", msgId);
					iBundle.putString("msgFromStr", msgFromStr);
					iBundle.putString("msgTypeStr", msgTypeStr);

					mIntent.putExtras(iBundle);
					startActivityForResult(mIntent, REQUEST_UPDATE_VIEW);

				}
			}
		});
		linearLayoutContent.setVisibility(View.INVISIBLE);
		linearLayoutProgress.setVisibility(View.VISIBLE);
	}

	/**
	 * Handler class implementation to handle the message
	 * 
	 * @author SongQing
	 * 
	 */
	private class MessageHandler extends Handler {

		// This method is used to handle received messages
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case MSG_SEND_SUCCESS:
				updateMsgListViewForRemove();
				// Toast.makeText(MessageSecondActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
				ToastHelper.showToast(msg.obj.toString(), Toast.LENGTH_SHORT);
				break;

			case MSG_SEND_ERROR:
				updateMsgListViewForRemove();
				// Toast.makeText(MessageSecondActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
				ToastHelper.showToast(msg.obj.toString(), Toast.LENGTH_SHORT);
				break;
			case MSG_SEND_FAILED:
				updateMsgListViewForRemove();
				// Toast.makeText(MessageSecondActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
				ToastHelper.showToast(msg.obj.toString(), Toast.LENGTH_SHORT);
				break;
			case MSG_SEND_FAILED_ByNetStatus:
				updateMsgListViewForRemove();
				// Toast.makeText(MessageSecondActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
				ToastHelper.showToast(msg.obj.toString(), Toast.LENGTH_SHORT);
				break;
			case MSG_UPDATE: {
				updateMsgListViewForRemove();
				linearLayoutContent.setVisibility(View.VISIBLE);
				linearLayoutProgress.setVisibility(View.GONE);
				ToastHelper.showToast(getResources().getString(
						R.string.msgUpdateFinished_MCA), Toast.LENGTH_SHORT);
				// ��ݼ������;
				lvMessageSC.onRefreshComplete();
				lvMessageSC.onUpdateComplete();
				break;
			}
				// ����ˢ�£����б�ǰ������������
			case MSG_REFRESH: {
				updateMsgListViewForRemove();
				linearLayoutContent.setVisibility(View.VISIBLE);
				linearLayoutProgress.setVisibility(View.GONE);
				if (msg.obj != null)
					ToastHelper
							.showToast(getResources().getString(
									R.string.msgUpdateFinished_MCA),
									Toast.LENGTH_SHORT);
				else
					ToastHelper.showToast(getResources().getString(
							R.string.details_nonews), Toast.LENGTH_SHORT);
				// ������������
				lvMessageSC.onRefreshComplete();
				lvMessageSC.onUpdateComplete();
				break;
			}
				// �鿴������Ϣ�������б����
			case MSG_MORE: {
				if (msg.obj != null) {
					updateMsgListViewOnMore();
					// �������ȡ������
					lvMessageSC.onMoreComplete();
				} else {
					ToastHelper.showToast(messageNomorepageEnd,
							Toast.LENGTH_SHORT);
					// �������޸�����Ϣ
					lvMessageSC.noMore();
				}

				break;
			}
			case MSG_UPDATE_FAILED:
				linearLayoutContent.setVisibility(View.VISIBLE);
				linearLayoutProgress.setVisibility(View.GONE);
				ToastHelper.showToast(getResources().getString(
						R.string.msgUpdateFailed_MCA), Toast.LENGTH_SHORT);
				lvMessageSC.onRefreshComplete();
				lvMessageSC.onUpdateComplete();
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {

			updateMsgListViewForRemove();
			// switch (requestCode) {
			// case REQUEST_UPDATE_VIEW:
			// // initializeListView();
			// updateMsgListViewForRemove();
			// break;
			// default:
			// break;
			// }
		}
	}

	public void onClick(View view) {

		switch (view.getId()) {
		// ���ñ༭״̬����ť����
		case R.id.btnEdit_MCA:
			editState = !editState;
			if (editState) {
				this.ibtnCheckMessage_MCA.setVisibility(View.VISIBLE);
				this.ibtnRemoveMessage_MCA.setVisibility(View.VISIBLE);
				this.btnEdit_MCA
						.setText(R.string.editButton_false_manuscriptList);
			} else {
				this.ibtnCheckMessage_MCA.setVisibility(View.GONE);
				this.ibtnRemoveMessage_MCA.setVisibility(View.GONE);
				this.btnEdit_MCA
						.setText(R.string.editButton_true_manuscriptList);
			}

			adapter.setEditState(editState);
			break;
		// �����ѡ��ť����ѡ���ø��
		case R.id.ibtnCheckMessage_MCA:

			setCheckState(!checkState);
			adapter.checkAll(checkState);
			break;
		// ɾ��
		case R.id.ibtnRemoveMessage_MCA:
			deleteAllCheck();

			updateMsgListViewForRemove();

			break;
		case R.id.iBtnBackSC_MCA:
			setResult(RESULT_OK);
			finish();
			break;

		case R.id.btnNewBuildSC_MCA:
			if (IngleApplication.getNetStatus().equals(
					NetStatus.Disable)) {
				ToastHelper.showToast(getResources().getString(
						R.string.network_disconnected), Toast.LENGTH_SHORT);
			} else {
				if (IngleApplication.getConnection() != null) {
					Intent intent = new Intent(this,
							ManageFriendsActivity.class);
					startActivityForResult(intent, REQUEST_UPDATE_VIEW);
				} else {
					ToastHelper.showToast(getResources().getString(
							R.string.msg_serverconnectionfailed),
							Toast.LENGTH_SHORT);
				}
			}
			break;
		default:
			break;
		}
	}

	public void updateMsgListViewForRemove() {
		List<MessageForUs> tempList = null;
		msgList = messageService.getMessageAllForInst(messageType, loginname);
		if(msgList==null) 			
			msgList = new ArrayList<MessageForUs>();
		if (msgList != null) {
			if (msgList.size() < limit * PAGESIZE)
				tempList = msgList.subList(0, msgList.size());
			else
				tempList = msgList.subList(0, limit * PAGESIZE);
		}
		adapter.setMessageList(tempList);
		adapter.notifyDataSetChanged();
		if (tempList == null) {
			tempList = new ArrayList<MessageForUs>();
		}
		textViewCount_MCA.setText(String.valueOf(tempList.size()));

		// �ָ���ѡ״̬
		checkState = false;
		setCheckState(checkState);
	}

	public void updateMsgListViewOnMore() {
		List<MessageForUs> tempList = null;
		if (msgList.size() < limit * PAGESIZE)
			tempList = msgList.subList(0, msgList.size());
		else
			tempList = msgList.subList(0, limit * PAGESIZE);
		adapter.setMessageList(tempList);
		adapter.notifyDataSetChanged();
		if (tempList == null) {
			tempList = new ArrayList<MessageForUs>();
		}
		textViewCount_MCA.setText(String.valueOf(tempList.size()));

		// ˳�� ��adapterSC.notifyDataSetChanged();
		// �ָ���ѡ״̬
		checkState = false;
		setCheckState(checkState);
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

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		MessageForUs messageForUs = (MessageForUs) lvMessageSC
				.getItemAtPosition(info.position);
		// ��ʱ��Ϊ�յ�����Ϣ
		// if(messageForUs.getSendOrReceiveType().toString().equals(SendOrReceiveType.Send.toString())){
		if (messageForUs.getMsgType().toString().equals(messageType)
				&& messageForUs.getSendOrReceiveType().toString().equals(
						SendOrReceiveType.Receive.toString())) {
			menu.add(Menu.NONE, CONTEXT_CUSTOM_SET_ITEM_DEFAULT_MSG, Menu.NONE,
					R.string.btnReplyTitle_MCA);
			menu.add(Menu.NONE, CONTEXT_CUSTOM_DELETE_ITEM_MSG, Menu.NONE,
					R.string.btnDelTitle_MCA);
		} else if (messageForUs.getMsgType().toString().equals(messageType)
				&& messageForUs.getMsgSendStatus().toString().equals(
						MsgSendStatus.failed.toString())) {
			menu.add(Menu.NONE, CONTEXT_CUSTOM_REREPLY_ITEM_DEFAULT_MSG,
					Menu.NONE, R.string.btnReplyAgainTitle_MCA);
			menu.add(Menu.NONE, CONTEXT_CUSTOM_DELETE_ITEM_MSG, Menu.NONE,
					R.string.btnDelTitle_MCA);
		} else {
			menu.add(Menu.NONE, CONTEXT_CUSTOM_DELETE_ITEM_MSG, Menu.NONE,
					R.string.btnDelTitle_MCA);
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();

		switch (item.getItemId()) {
		case CONTEXT_CUSTOM_DELETE_ITEM_MSG:
			deleteMessage(info.id); // info.id����ListView��������RowId
			break;
		case CONTEXT_CUSTOM_SET_ITEM_DEFAULT_MSG:
			replyMessage(info.id);
			break;
		case CONTEXT_CUSTOM_REREPLY_ITEM_DEFAULT_MSG:
			replyMessageAgain(info.id);
			break;

		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	private void replyMessageAgain(final long rowId) {
		if (rowId >= 0) {
			mItem = (MessageForUs) adapter.getItem((int) rowId);

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			etContent = new EditText(MessageSecondForInstantActivity.this);
			etContent.setHeight(300);
			etContent.setGravity(Gravity.TOP);
			etContent.setText(mItem.getMsgContent());
			builder.setTitle(R.string.btnReplyTitle_MCA).setView(etContent)
					.setPositiveButton(R.string.btnReplyTitle_MCA,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									if (etContent.getText().toString().trim()
											.length() > 0) {
										if (mItem != null) {
											MsgOwnerStr = mItem.getMsgOwner();
											messageService
													.deleteMessageById(mItem
															.getMsg_id());
											sendMessageAgainThread();

										}
									} else {
										// Toast.makeText(MessageSecondActivity.this,"������ظ����ݣ�",Toast.LENGTH_SHORT).show();
										ToastHelper
												.showToast(
														getResources()
																.getString(
																		R.string.inputReplyContent_MCA),
														Toast.LENGTH_SHORT);
									}

								}
							}).setNegativeButton(R.string.cancel_button,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();

								}
							}).create().show();

		}

	}

	private void replyMessage(final long rowId) {
		if (rowId >= 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			etContent = new EditText(MessageSecondForInstantActivity.this);
			etContent.setHeight(300);
			etContent.setGravity(Gravity.TOP);
			builder.setTitle(R.string.btnReplyTitle_MCA).setView(etContent)
					.setPositiveButton(R.string.btnReplyTitle_MCA,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									if (etContent.getText().toString().trim()
											.length() > 0) {
										mItem = (MessageForUs) adapter
												.getItem((int) rowId);
										if (mItem != null) {
											MsgOwnerStr = mItem.getMsgFrom();
											sendMessageThread();

										}
									} else {
										// Toast.makeText(MessageSecondActivity.this,"������ظ����ݣ�",Toast.LENGTH_SHORT).show();
										ToastHelper
												.showToast(
														getResources()
																.getString(
																		R.string.inputReplyContent_MCA),
														Toast.LENGTH_SHORT);
									}

								}
							}).setNegativeButton(R.string.cancel_button,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();

								}
							}).create().show();

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

	private void sendMessageThread() {
		new Thread(new Runnable() {
			public void run() {

				try {
					MessageForUs message = new MessageForUs();

					message.setMsgContent(etContent.getText().toString());
					message.setLoginName(loginname);
					message.setMsgFrom(loginname);
					message.setMsgOwner(MsgOwnerStr);
					message.setMsgOwnerType(MsgOwnerType.Person);
					Date date = new Date();
					SimpleDateFormat sFormat = new SimpleDateFormat(
							"yyyy-MM-dd HH:MM:SS");
					message.setMsgSendOrReceiveTime(String
							.valueOf(TimeFormatHelper.convertDateToLong(sFormat
									.format(date))));
					message.setMsgType(messageType);
					message.setReadOrNotType(ReadOrNotType.Read);
					message.setSendOrReceiveType(SendOrReceiveType.Send);
					message.setId(UUID.randomUUID().toString());
					message.setMsgSendStatus(MsgSendStatus.failed);
					if (messageType.equals(MessageType.InstantMsg)) {
						if (IngleApplication
								.getNetStatus() != NetStatus.Disable) {
							if (RemoteCaller.sendMessage(sessionId,
									messageType, MsgOwnerStr,
									MsgOwnerType.Person, etContent.getText()
											.toString())) {

								message.setMsgSendStatus(MsgSendStatus.Success);
								messageService.addMessage(message);
								sendMessage(
										MSG_SEND_SUCCESS,
										ToastHelper
												.getStringFromResources(R.string.replySucceed_MCA));

							} else {
								messageService.addMessage(message);
								sendMessage(
										MSG_SEND_FAILED,
										ToastHelper
												.getStringFromResources(R.string.replyFailed_MCA));
							}
						} else {
							messageService.addMessage(message);
							sendMessage(
									MSG_SEND_FAILED_ByNetStatus,
									ToastHelper
											.getStringFromResources(R.string.replyFailedAsNetProblem_MCA));
						}
					} else if (messageType.equals(MessageType.XMPPMsg)) {
						Chat newChat = SmackUtils.createChat(
								IngleApplication
										.getConnection(), MsgOwnerStr + "@"
										+ XmppManager.XMPPHOST_STRING,
								new MessageListener() {

									public void processMessage(
											Chat arg0,
											org.jivesoftware.smack.packet.Message msg) {
										if (msg != null
												&& msg.getBody() != null) {
											// ������������������û���Ϣ�Ĵ���
											// System.out.println("�յ���Ϣ:" +
											// msg.getBody());
											Log.d("MsgDetailForInstant",
													"�յ���Ϣ:" + msg.getBody());

										}

									}
								});

						if (IngleApplication
								.getNetStatus() != NetStatus.Disable) {
							org.jivesoftware.smack.packet.Message msg = new org.jivesoftware.smack.packet.Message();
							msg.setBody(message.getMsgContent());
							msg.setProperty("MsgTime", message
									.getMsgSendOrReceiveTime());
							newChat.sendMessage(msg);

							message.setMsgSendStatus(MsgSendStatus.Success);
							messageService.addMessage(message);

							sendMessage(
									MSG_SEND_SUCCESS,
									ToastHelper
											.getStringFromResources(R.string.replySucceed_MCA));

						} else {
							messageService.addMessage(message);
							sendMessage(
									MSG_SEND_FAILED_ByNetStatus,
									ToastHelper
											.getStringFromResources(R.string.replyFailedAsNetProblem_MCA));
						}
					}

				} catch (Exception e) {
					Logger.e(e);
					sendMessage(MSG_SEND_ERROR, e.getMessage());
					e.printStackTrace();
				}

			}
		}).start();
	}

	private void sendMessageAgainThread() {
		new Thread(new Runnable() {
			public void run() {

				try {
					MessageForUs message = new MessageForUs();

					message.setMsgContent(etContent.getText().toString());
					message.setLoginName(loginname);
					message.setMsgFrom(loginname);
					message.setMsgOwner(MsgOwnerStr);
					message.setMsgOwnerType(MsgOwnerType.Person);
					Date date = new Date();
					SimpleDateFormat sFormat = new SimpleDateFormat(
							"yyyy-MM-dd HH:MM:SS");

					message.setMsgSendOrReceiveTime(String
							.valueOf(TimeFormatHelper.convertDateToLong(sFormat
									.format(date))));
					message.setMsgType(messageType);
					message.setReadOrNotType(ReadOrNotType.Read);
					message.setSendOrReceiveType(SendOrReceiveType.Send);
					message.setId(UUID.randomUUID().toString());
					message.setMsgSendStatus(MsgSendStatus.failed);

					// messageService.addMessage(message);
					if (messageType.equals(MessageType.InstantMsg)) {
						if (IngleApplication
								.getNetStatus() != NetStatus.Disable) {
							if (RemoteCaller.sendMessage(sessionId,
									messageType, MsgOwnerStr,
									MsgOwnerType.Person, etContent.getText()
											.toString())) {

								message.setMsgSendStatus(MsgSendStatus.Success);
								messageService.addMessage(message);
								sendMessage(
										MSG_SEND_SUCCESS,
										ToastHelper
												.getStringFromResources(R.string.replySucceed_MCA));

							} else {
								messageService.addMessage(message);
								sendMessage(
										MSG_SEND_FAILED,
										ToastHelper
												.getStringFromResources(R.string.replyFailed_MCA));
							}
						} else {
							messageService.addMessage(message);
							sendMessage(
									MSG_SEND_FAILED_ByNetStatus,
									ToastHelper
											.getStringFromResources(R.string.replyFailedAsNetProblem_MCA));
						}
					} else if (messageType.equals(MessageType.XMPPMsg)) {
						if (IngleApplication
								.getNetStatus() != NetStatus.Disable) {
							if (RemoteCaller.sendMessage(sessionId,
									messageType, MsgOwnerStr,
									MsgOwnerType.Person, etContent.getText()
											.toString())) {
								message.setMsgSendStatus(MsgSendStatus.Success);
								messageService.addMessage(message);
								sendMessage(
										MSG_SEND_SUCCESS,
										ToastHelper
												.getStringFromResources(R.string.replySucceed_MCA));
							} else {
								messageService.addMessage(message);
								sendMessage(
										MSG_SEND_FAILED,
										ToastHelper
												.getStringFromResources(R.string.replyFailed_MCA));
							}
						} else {
							messageService.addMessage(message);
							sendMessage(
									MSG_SEND_FAILED_ByNetStatus,
									ToastHelper
											.getStringFromResources(R.string.replyFailedAsNetProblem_MCA));
						}
					}
					setResult(RESULT_OK);
					finish();
				} catch (Exception e) {
					Logger.e(e);
					sendMessage(MSG_SEND_ERROR, e.getMessage());
					e.printStackTrace();
				}

			}
		}).start();
	}

	private void deleteMessage(final long rowId) {
		if (rowId >= 0) {
			// TODO ��ʻ�
			new AlertDialog.Builder(this).setTitle(
					R.string.btnDelOrNotTitle_MCA).setPositiveButton(
					R.string.confirm_button,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							mItem = (MessageForUs) adapter.getItem((int) rowId);
							if (mItem != null) {

								if (messageService
										.deleteMessageByMsgFromOwner(mItem
												.getMsgFrom(), mItem
												.getMsgOwner())) {
									adapter.deleteItem((int) rowId);

									updateMsgListViewForRemove();
									adapter.notifyDataSetChanged();

								}

							}
						}
					}).setNegativeButton(R.string.cancel_button, null).show();
		}

	}

	private void setCheckState(boolean checkState) {

		this.checkState = checkState;

		// ����ͼ�Ķ�ѡ��ťͼƬ
		if (checkState)
			ibtnCheckMessage_MCA.setImageBitmap(BitmapFactory.decodeResource(
					this.getResources(), R.drawable.message_check));
		else
			ibtnCheckMessage_MCA.setImageBitmap(BitmapFactory.decodeResource(
					this.getResources(), R.drawable.message_uncheck));
	}

	private void deleteAllCheck() {
		adapter.deleteSelectedItems();

	}

	/**
	 * ˢ����Ϣ�б�(�����ñ����ڴ��е���Ϣ��Ĭ�ϰ��շ�ҳ����ȡ��)
	 */
	private void onUpdate() {
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
				limit = 1;
				getMessageThreadForUpdate();
			}
		}).start();
	}

	private void getMessageThreadForUpdate() {
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

	private void getMessageThreadForRefresh() {
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
				int temp = messageService.getMessageFromRemote();
				if (temp == 1) {
					sendMessage(MSG_REFRESH, msgList);

				} else if (temp == 0) {
					sendMessage(MSG_REFRESH, null);

				}

			}
		}).start();
	}

	public void onMore() {
		new Thread(new Runnable() {

			public void run() {
				//Update By SongQing.2013.11.27 msgList�п���Ϊ�գ�������Ҫ����msgList!=null��ǰ���ж�
				if (msgList !=null && adapter.getCount() < msgList.size()) {
					limit++;
					sendMessage(MSG_MORE, msgList);
				} else {
					sendMessage(MSG_MORE, null);
				}

			}
		}).start();

	}

	public void onRefresh() {
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
				limit = 1;
				getMessageThreadForRefresh();
			}

		}).start();
	}

}
