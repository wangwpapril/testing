package com.cuc.miti.phone.xmc.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.adapter.MessageThirdNowAdapter;
import com.cuc.miti.phone.xmc.domain.MessageForUs;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.Enums.MessageType;
import com.cuc.miti.phone.xmc.domain.Enums.MsgOwnerType;
import com.cuc.miti.phone.xmc.domain.Enums.MsgSendStatus;
import com.cuc.miti.phone.xmc.domain.Enums.NetStatus;
import com.cuc.miti.phone.xmc.domain.Enums.ReadOrNotType;
import com.cuc.miti.phone.xmc.domain.Enums.SendOrReceiveType;
import com.cuc.miti.phone.xmc.http.RemoteCaller;
import com.cuc.miti.phone.xmc.logic.MessageService;

import com.cuc.miti.phone.xmc.ui.control.PullDownListView;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.SmackUtils;
import com.cuc.miti.phone.xmc.utils.TimeFormatHelper;
import com.cuc.miti.phone.xmc.utils.ToastHelper;
import com.cuc.miti.phone.xmc.xmpp.XmppManager;
import com.cuc.miti.phone.xmc.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

public class MessageThirdForInstantActivity extends BaseActivity implements
		OnClickListener, PullDownListView.OnRefreshListener {
	private ImageButton iBtnBack;
	private PullDownListView lvMessageTHNow;
	private LinearLayout linearLayoutProgress;
	private LinearLayout linearLayoutContent;
	private TextView textViewMsgFrom;
	private Button btnReply;

	private MessageThirdNowAdapter adapter;
	private String sessionId = "";
	private String loginname = "";
	private Pager pager = null;
	private MessageService messageService;
	private MessageType messageType;
	List<MessageForUs> msgList;
	private MessageForUs mItem;

	private String messageNomorepageEnd;

	MessageHandler mHandler = null;

	private Button btnEdit_MCA; // �༭
	private TextView textViewTitleSC;
	private TextView textViewCount_MCA;
	private ImageButton ibtnCheckMessage_MCA, ibtnRemoveMessage_MCA; // ѡ�򡢻���վ
	String msgTypeStr = "";
	// ����ǵ�һ�λ����ٴλظ���Ϣ
	private String MsgOwnerStr = "";

	// ȡ����ǰ�����˵���Ϣ��Դ��
	private String msgFromStr = "";

	private final int MSG_UPDATE = 10;
	private final int MSG_REFRESH = 11;
	private final int MSG_MORE = 12;
	private final int MSG_UPDATE_FAILED = 13;

	private static final int REQUEST_UPDATE_VIEW = 1;
	private static final int CONTEXT_CUSTOM_DELETE_ITEM_MSG = 5; // ɾ����Ϣ
	private static final int CONTEXT_CUSTOM_SET_ITEM_DEFAULT_MSG = 4; // �ظ���Ϣ
	private static final int CONTEXT_CUSTOM_REREPLY_ITEM_DEFAULT_MSG = 3; // ���»ظ�
	protected static final int MSG_SEND_SUCCESS = 6;
	protected static final int MSG_SEND_ERROR = 7;
	protected static final int MSG_SEND_FAILED = 8;
	protected static final int MSG_SEND_FAILED_ByNetStatus = 9;

	private EditText etContent; // ����Ļظ�����
	// �Ƿ�����˱༭״̬
	private boolean editState = false;
	// �Ƿ�ȫѡ
	private boolean checkState = false;
	private int limit; // ��Ϣ��ʾ������
	private static final int PAGESIZE = 5;
	private String messageNew;// ��־�Ƿ���Ҫˢ������ҳ��

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_third_instant);

		this.initialize();

		IngleApplication.getInstance().addActivity(this);
	}

	private void initialize() {
		sessionId = IngleApplication.getSessionId();
		loginname = IngleApplication.getInstance()
				.getCurrentUser();
		messageService = new MessageService(MessageThirdForInstantActivity.this);

		mHandler = new MessageHandler();
		pager = Pager.getDefault();
		limit = pager.getPageSize();
		messageNomorepageEnd = getResources().getString(
				R.string.oa_details_nomorepage_end);
		this.setUpViews();
		this.initializeListView();
	}

	private void setUpViews() {
		lvMessageTHNow = (PullDownListView) findViewById(R.id.lvMessageTHNow_MCA);
		lvMessageTHNow
				.setonRefreshListener((PullDownListView.OnRefreshListener) this);
		lvMessageTHNow.setDivider(null);

		iBtnBack = (ImageButton) findViewById(R.id.iBtnBackTHNow_MCA);
		iBtnBack.setOnClickListener(this);
		textViewMsgFrom = (TextView) findViewById(R.id.textViewMsgFromTHNow_MCA);
		btnReply = (Button) findViewById(R.id.btnReply_MCA);
		btnReply.setOnClickListener(this);
		linearLayoutContent = (LinearLayout) findViewById(R.id.linearLayoutContent_MCA);
		linearLayoutProgress = (LinearLayout) findViewById(R.id.linearLayoutProgress_MCA);
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
			msgFromStr = mBundle.getString("msgFromStr");
			msgTypeStr = mBundle.getString("msgTypeStr");
			messageType = MessageType.valueOf(msgTypeStr);
			messageNew = mBundle.getString("msgNew");

			textViewMsgFrom.setText(msgFromStr);

			msgList = new ArrayList<MessageForUs>();
			onUpdate();

			msgList = messageService.getMessageByPageAndMsgFrom(pager,
					messageType, msgFromStr,
					IngleApplication.getInstance()
							.getCurrentUser());

			// ��ListViewע��Context Menu����ϵͳ��⵽�û�����ĳ��Ԫʱ������Context Menu����
			registerForContextMenu(lvMessageTHNow);

		} catch (Exception e) {
			Logger.e(e);
		}

		adapter = new MessageThirdNowAdapter(msgList,
				MessageThirdForInstantActivity.this, R.drawable.message_check,
				R.drawable.message_uncheck);
		lvMessageTHNow.setAdapter(adapter);

		textViewCount_MCA.setText(String.valueOf(adapter.getCount()));

		// �趨����¼�
		lvMessageTHNow.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				if (editState) { // �ж��Ƿ�����Ϣ�б�༭״̬��
					adapter.checkItem(position - 1); // �ǣ�ֻ�ܽ�����Ϣѡ��
				}
			}
		});
		linearLayoutContent.setVisibility(View.INVISIBLE);
		linearLayoutProgress.setVisibility(View.VISIBLE);
	}

	private void updateMsgListView() {

		msgList = messageService.getMessageByPageAndMsgFrom(pager, messageType,
				msgFromStr, IngleApplication.getInstance()
						.getCurrentUser());

		adapter.setMessageList(msgList);

		textViewCount_MCA.setText(String.valueOf(adapter.getCount()));

		// �ָ���ѡ״̬
		checkState = false;
		setCheckState(checkState);

	}

	public void update() {
		updateMsgListView();
		adapter.notifyDataSetChanged();
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
			update();
			switch (msg.what) {
			case MSG_SEND_SUCCESS:
				// Toast.makeText(MessageThirdNowActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
				ToastHelper.showToast(msg.obj.toString(), Toast.LENGTH_SHORT);
				break;

			case MSG_SEND_ERROR:
				// Toast.makeText(MessageThirdNowActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
				ToastHelper.showToast(msg.obj.toString(), Toast.LENGTH_SHORT);
				break;
			case MSG_SEND_FAILED:
				// Toast.makeText(MessageThirdNowActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
				ToastHelper.showToast(msg.obj.toString(), Toast.LENGTH_SHORT);
				break;
			case MSG_SEND_FAILED_ByNetStatus:

				// Toast.makeText(MessageThirdNowActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
				ToastHelper.showToast(msg.obj.toString(), Toast.LENGTH_SHORT);
				break;
			case MSG_UPDATE: {
				linearLayoutContent.setVisibility(View.VISIBLE);
				linearLayoutProgress.setVisibility(View.GONE);
				ToastHelper.showToast(getResources().getString(
						R.string.msgUpdateFinished_MCA), Toast.LENGTH_SHORT);
				// ��ݼ������;
				lvMessageTHNow.onRefreshComplete();
				lvMessageTHNow.onUpdateComplete();
				break;
			}
				// ����ˢ�£����б�ǰ������������
			case MSG_REFRESH: {
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
				lvMessageTHNow.onRefreshComplete();
				lvMessageTHNow.onUpdateComplete();
				break;
			}
				// �鿴������Ϣ�������б����
			case MSG_MORE: {
				if (msg.obj != null) {
					// �������ȡ������
					lvMessageTHNow.onMoreComplete();
				} else {
					ToastHelper.showToast(messageNomorepageEnd,
							Toast.LENGTH_SHORT);
					// �������޸�����Ϣ
					lvMessageTHNow.noMore();
				}

				break;
			}
			case MSG_UPDATE_FAILED:
				linearLayoutContent.setVisibility(View.VISIBLE);
				linearLayoutProgress.setVisibility(View.GONE);
				ToastHelper.showToast(getResources().getString(
						R.string.msgUpdateFailed_MCA), Toast.LENGTH_SHORT);
				lvMessageTHNow.onRefreshComplete();
				lvMessageTHNow.onUpdateComplete();
				break;

			default:
				break;
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// KeyEvent.KEYCODE_BACK��?�ز���.
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			List<MessageForUs> messageForUsList = null;

			messageForUsList = messageService.getMessageByMsgFrom(messageType,
					msgFromStr, loginname);

			for (MessageForUs mfu : messageForUsList) {
				if (ReadOrNotType.New.toString().equals(
						mfu.getReadOrNotType().toString())) {

					mfu.setReadOrNotType(ReadOrNotType.Read);
					messageService.updateMessage(mfu);
				}
			}
			back();
			setResult(RESULT_OK);
			finish();

		}
		return true;
	}

	public void back() {
		if (messageNew != null&&messageType.equals(MessageType.XMPPMsg)) {
			ArrayList<Activity> activities = IngleApplication
					.getInstance().getActivities();
			if (activities != null && activities.size() > 1) {

				if (activities.get(activities.size() - 2).getClass().equals(
						MessageSecondForInstantActivity.class)) {
					MessageSecondForInstantActivity activity = (MessageSecondForInstantActivity) activities
							.get(activities.size() - 2);
					activity.updateMsgListViewForRemove();

				} else if (activities.get(activities.size() - 2).getClass()
						.equals(MessageActivity.class)) {
					MessageActivity activity = (MessageActivity) activities
							.get(activities.size() - 2);
					activity.update();
				} else if (activities.get(activities.size() - 2).getClass()
						.equals(MainActivity.class)) {
					MainActivity activity = (MainActivity) activities
							.get(activities.size() - 2);
					activity.setNewMessageNumber();
				}

			}
		}
	}

	public void onClick(View view) {
		int currentPage = 0;

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

			updateMsgListView();

			break;
		case R.id.iBtnBackTHNow_MCA:

			List<MessageForUs> messageForUsList = null;

			messageForUsList = messageService.getMessageByMsgFrom(messageType,
					msgFromStr, loginname);

			for (MessageForUs mfu : messageForUsList) {
				if (ReadOrNotType.New.toString().equals(
						mfu.getReadOrNotType().toString())) {
					mfu.setReadOrNotType(ReadOrNotType.Read);
					messageService.updateMessage(mfu);
				}
			}
			back();
			setResult(RESULT_OK);
			finish();

			break;

		case R.id.btnReply_MCA:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			etContent = new EditText(MessageThirdForInstantActivity.this);
			etContent.setHeight(300);
			etContent.setGravity(Gravity.TOP);
			builder.setTitle(R.string.btnReplyTitle_MCA).setView(etContent)
					.setPositiveButton(R.string.btnReplyTitle_MCA,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									if (etContent.getText().toString().trim()
											.length() > 0) {
										MsgOwnerStr = msgFromStr;
										sendMessageThread();

									} else {
										// Toast.makeText(MessageThirdNowActivity.this,"������ظ����ݣ�",Toast.LENGTH_SHORT).show();
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
			break;

		default:
			break;
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

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		MessageForUs messageForUs = (MessageForUs) lvMessageTHNow
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
			etContent = new EditText(MessageThirdForInstantActivity.this);
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
										// Toast.makeText(MessageThirdNowActivity.this,"������ظ����ݣ�",Toast.LENGTH_SHORT).show();
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
			etContent = new EditText(MessageThirdForInstantActivity.this);
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
										// Toast.makeText(MessageThirdNowActivity.this,"������ظ����ݣ�",Toast.LENGTH_SHORT).show();
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
					// Date date = new Date(System.currentTimeMillis());
					// SimpleDateFormat sFormat = new SimpleDateFormat(
					// "yyyy-MM-dd HH:MM:SS");
					// message.setMsgSendOrReceiveTime(String
					// .valueOf(TimeFormatHelper.convertDateToLong(sFormat
					// .format(date))));
					message.setMsgSendOrReceiveTime(String.valueOf(System
							.currentTimeMillis()));
					message.setMsgType(messageType);
					message.setReadOrNotType(ReadOrNotType.Read);
					message.setSendOrReceiveType(SendOrReceiveType.Send);
					message.setId(UUID.randomUUID().toString());
					message.setMsgSendStatus(MsgSendStatus.failed);

					// if("".equals("sd"))
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
					}
					else if (messageType.equals(MessageType.XMPPMsg)) {
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
					// Date date = new Date(System.currentTimeMillis());
					// SimpleDateFormat sFormat = new SimpleDateFormat(
					// "yyyy-MM-dd HH:MM:SS");
					//
					// message.setMsgSendOrReceiveTime(String
					// .valueOf(TimeFormatHelper.convertDateToLong(sFormat
					// .format(date))));
					message.setMsgSendOrReceiveTime(String.valueOf(System
							.currentTimeMillis()));
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
					}
					else if (messageType.equals(MessageType.XMPPMsg)) {
					Chat newChat = SmackUtils.createChat(
							IngleApplication
									.getConnection(), MsgOwnerStr + "@"
									+ XmppManager.XMPPHOST_STRING,
							new MessageListener() {

								public void processMessage(
										Chat arg0,
										org.jivesoftware.smack.packet.Message msg) {
									if (msg != null && msg.getBody() != null) {
										// ������������������û���Ϣ�Ĵ���
										// System.out.println("�յ���Ϣ:" +
										// msg.getBody());
										Log.d("MsgDetailForInstant", "�յ���Ϣ:"
												+ msg.getBody());

									}

								}
							});

					if (IngleApplication.getNetStatus() != NetStatus.Disable) {
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
								if (messageService.deleteMessageById(mItem
										.getMsg_id())) {
									adapter.deleteItem((int) rowId);

									updateMsgListView();
									adapter.notifyDataSetChanged();

								}
							}
						}
					}).setNegativeButton(R.string.cancel_button, null).show();
		}

	}

	public void onMore() {
		limit = this.pager.getPageSize();
		if (adapter.getCount() < pager.getTotalNum()) {
			limit = limit * 2;
			pager.setPageSize(limit);
			sendMessage(MSG_MORE, msgList);
		} else {
			sendMessage(MSG_MORE, null);
		}

	}

	/**
	 * ˢ����Ϣ�б�(�����ñ����ڴ��е���Ϣ��Ĭ�ϰ��շ�ҳ����ȡ��)
	 */
	public void onUpdate() {
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
				sendMessage(MSG_UPDATE, null);
				// getMessageThreadForUpdate();
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
				pager = Pager.getDefault();
				getMessageThreadForRefresh();
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

	public String getMsgFrom() {
		return msgFromStr;
	}
}
