package com.cuc.miti.phone.xmc.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.pubsub.Subscription;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SearchViewCompat;
import android.support.v4.widget.SearchViewCompat.OnQueryTextListenerCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.adapter.ManagementFriendsAdapter;
import com.cuc.miti.phone.xmc.domain.Enums.MessageType;
import com.cuc.miti.phone.xmc.utils.DeviceInfoHelper;
import com.cuc.miti.phone.xmc.utils.SmackUtils;
import com.cuc.miti.phone.xmc.utils.ToastHelper;
import com.cuc.miti.phone.xmc.xmpp.XmppManager;
import com.cuc.miti.phone.xmc.R;

public class ManageFriendsActivity extends BaseActivity implements
		OnClickListener {

	private ListView lvOnline_MF;
	private ListView lvOffline_MF;
	private ListView lvResult_MF;
	private LinearLayout linearLayoutOnlineHeader_MF;
	private LinearLayout linearLayoutOfflineHeader_MF;
	private LinearLayout linearLayoutResultHeader_MF;
	private List<RosterEntry> userNameList; // ����ѯ�û�ʱʹ��
	private TextView textViewresultNull_MF;
	private ImageButton iBtnSearchToAdd;
	private ImageButton iBtnBackSC_MF;
	private ImageButton iBtnsearch;
	private EditText searchContent;

	// ��ȡ�����б�
	Roster roster;
	Collection<RosterEntry> entries;

	List<RosterEntry> onlineList;
	List<RosterEntry> offlineList;
	List<RosterEntry> resultList;

	private ManagementFriendsAdapter onlineAdapter;
	private ManagementFriendsAdapter offlineAdapter;
	private ManagementFriendsAdapter resultAdapter;

	ManageFriendsHandler mHandler = null;
	protected static final int UPDATE_FLIST = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.manage_friends);
		initialize();
		IngleApplication.getInstance().addActivity(this);
	}

	/**
	 * ��ʼ����ؿؼ�
	 */
	private void initialize() {
		mHandler = new ManageFriendsHandler();
		setUpViews();
		initializeListView();
		addListenerForPresence();
	}

	public void onQueryTextSubmit(String query) {
		String userName = "";

		resultList.clear();
		if ("".equals(query)) {
			initializeListView();
		} else {
			if (userNameList != null && userNameList.size() > 0) {
				for (int i = 0; i < userNameList.size(); i++) {
					String user = userNameList.get(i).getUser();
					if (userNameList.get(i).getName() == null) {
						userName = user.substring(0, user.indexOf("@"));
					} else {
						userName = userNameList.get(i).getName();
					}

					if (userName.contains(query)
							|| userName.contentEquals(query)) {
						resultList.add(userNameList.get(i));
					}

				}
				linearLayoutOfflineHeader_MF.setVisibility(View.GONE);
				linearLayoutOnlineHeader_MF.setVisibility(View.GONE);
				linearLayoutResultHeader_MF.setVisibility(View.VISIBLE);
				showResult();

			}
		}
	}

	private void showResult() {
		if (resultList.size() > 0) {
			textViewresultNull_MF.setVisibility(View.GONE);
			lvResult_MF.setVisibility(View.VISIBLE);
			resultAdapter = new ManagementFriendsAdapter(resultList, this);
			lvResult_MF.setAdapter(resultAdapter);
			lvResult_MF.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					String msgOwnerName = resultList.get(position).getUser()
							.substring(
									0,
									resultList.get(position).getUser().indexOf(
											"@"));
					if (msgOwnerName
							.equals(IngleApplication
									.getInstance().getCurrentUser()) == false) {
						Intent mIntent = new Intent(ManageFriendsActivity.this,
								MessageThirdForInstantActivity.class);
						Bundle mBundle = new Bundle();

						mBundle.putString("msgTypeStr", MessageType.XMPPMsg.toString());
						mBundle.putString("msgFromStr", msgOwnerName);
						mBundle.putString("msgNew", "messageNew");
						mIntent.putExtras(mBundle);
						startActivity(mIntent);
						finish();
					}
				}
			});
			lvResult_MF
					.setOnItemLongClickListener(new OnItemLongClickListener() {

						public boolean onItemLongClick(AdapterView<?> parent,
								View view, int position, long id) {
							final String msgOwnerName = resultList
									.get(position).getUser();
							if (msgOwnerName.substring(0,
									msgOwnerName.indexOf("@")).equals(
									IngleApplication
											.getInstance().getCurrentUser()) == false) {
								AlertDialog.Builder builder = new AlertDialog.Builder(
										ManageFriendsActivity.this);
								builder
										.setTitle(
												getString(R.string.delFriend)
														+ msgOwnerName
																.substring(
																		0,
																		msgOwnerName
																				.indexOf("@")))
										.setPositiveButton(
												R.string.yes,
												new DialogInterface.OnClickListener() {

													public void onClick(
															DialogInterface dialog,
															int which) {
														SmackUtils
																.removeUser(
																		IngleApplication
																				.getConnection()
																				.getRoster(),
																		msgOwnerName);

													}
												})
										.setNegativeButton(
												R.string.no,
												new DialogInterface.OnClickListener() {

													public void onClick(
															DialogInterface dialog,
															int which) {
														dialog.dismiss();

													}
												}).create().show();
							}
							return true;
						}

					});
		} else {
			lvResult_MF.setVisibility(View.GONE);
			textViewresultNull_MF.setVisibility(View.VISIBLE);
		}

	}

	private void setUpViews() {
		lvOnline_MF = (ListView) findViewById(R.id.lvOnline_MF);
		lvOffline_MF = (ListView) findViewById(R.id.lvOffline_MF);
		lvResult_MF = (ListView) findViewById(R.id.lvResult_MF);
		linearLayoutOnlineHeader_MF = (LinearLayout) findViewById(R.id.linearLayoutOnlineHeader_MF);
		linearLayoutOfflineHeader_MF = (LinearLayout) findViewById(R.id.linearLayoutOfflineHeader_MF);
		linearLayoutResultHeader_MF = (LinearLayout) findViewById(R.id.linearLayoutResultHeader_MF);
		textViewresultNull_MF = (TextView) findViewById(R.id.textViewresultNull_MF);
		iBtnSearchToAdd = (ImageButton) findViewById(R.id.iBtnSearchToAdd);
		iBtnSearchToAdd.setOnClickListener(this);
		iBtnBackSC_MF = (ImageButton) findViewById(R.id.iBtnBackSC_MF);
		iBtnBackSC_MF.setOnClickListener(this);
		iBtnsearch = (ImageButton) findViewById(R.id.iBtnsearch);
		iBtnsearch.setOnClickListener(this);
		searchContent = (EditText) findViewById(R.id.searchContent);
	}

	private void initializeListView() {

		roster = IngleApplication.getConnection()
				.getRoster();

		if (roster != null) {
			entries = roster.getEntries();
		}

		onlineList = new ArrayList<RosterEntry>();
		offlineList = new ArrayList<RosterEntry>();
		userNameList = new ArrayList<RosterEntry>();
		resultList = new ArrayList<RosterEntry>();

		if (entries.size() > 0 && entries != null) {
			for (RosterEntry entry : entries) {
				// ����ʾ��Щ�������˵ĺ����б�
				if ("both".equals(entry.getType().name())) {
					Presence presence = roster.getPresence(entry.getUser());
					if (presence.isAvailable() == false) {// �жϺ����Ƿ�����
						Log.i("---", "name: " + entry.getName() + "_OffLine");
						offlineList.add(entry);

					} else {
						Log.i("---", "name: " + entry.getName() + "_OnLine");
						onlineList.add(entry);
					}

				}

			}
		}

		if (onlineList.size() == 0) {
			linearLayoutOnlineHeader_MF.setVisibility(View.GONE);
		} else {
			linearLayoutOnlineHeader_MF.setVisibility(View.VISIBLE);
		}
		if (offlineList.size() == 0) {
			linearLayoutOfflineHeader_MF.setVisibility(View.GONE);
		} else {
			linearLayoutOfflineHeader_MF.setVisibility(View.VISIBLE);
		}

		linearLayoutResultHeader_MF.setVisibility(View.GONE);

		for (RosterEntry entry : entries) {
			if ( "both".equals(entry.getType().name())) {
				userNameList.add(entry);
			}
		}

		onlineAdapter = new ManagementFriendsAdapter(onlineList, this);

		lvOnline_MF.setAdapter(onlineAdapter);
		lvOnline_MF.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String msgOwnerName = onlineList
						.get(position)
						.getUser()
						.substring(0,
								onlineList.get(position).getUser().indexOf("@"));
				if (msgOwnerName.equals(IngleApplication
						.getInstance().getCurrentUser()) == false) {
					Intent mIntent = new Intent(ManageFriendsActivity.this,
							MessageThirdForInstantActivity.class);
					Bundle mBundle = new Bundle();

					mBundle.putString("msgTypeStr", MessageType.XMPPMsg.toString());
					mBundle.putString("msgFromStr", msgOwnerName);
					mBundle.putString("msgNew", "messageNew");
					mIntent.putExtras(mBundle);
					startActivity(mIntent);
					finish();
				}
			}
		});

		offlineAdapter = new ManagementFriendsAdapter(offlineList, this);
		lvOffline_MF.setAdapter(offlineAdapter);

		lvOffline_MF.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String msgOwnerName = offlineList.get(position).getUser()
						.substring(
								0,
								offlineList.get(position).getUser()
										.indexOf("@"));
				if (msgOwnerName.equals(IngleApplication
						.getInstance().getCurrentUser()) == false) {
					Intent mIntent = new Intent(ManageFriendsActivity.this,
							MessageThirdForInstantActivity.class);
					Bundle mBundle = new Bundle();

					mBundle.putString("msgTypeStr", MessageType.XMPPMsg.toString());
					mBundle.putString("msgFromStr", msgOwnerName);
					mBundle.putString("msgNew", "messageNew");
					mIntent.putExtras(mBundle);
					startActivity(mIntent);
					finish();
				}
			}
		});
		lvOnline_MF.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final String msgOwnerName = onlineList.get(position).getUser();
				if (msgOwnerName.substring(0, msgOwnerName.indexOf("@"))
						.equals(
								IngleApplication
										.getInstance().getCurrentUser()) == false) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							ManageFriendsActivity.this);
					builder.setTitle(
							getString(R.string.delFriend)
									+ msgOwnerName.substring(0, msgOwnerName
											.indexOf("@"))).setPositiveButton(
							R.string.yes,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									SmackUtils.removeUser(
											IngleApplication
													.getConnection()
													.getRoster(), msgOwnerName);
//									Presence presence = new Presence(
//											Presence.Type.unsubscribe);
//									presence
//											.setFrom(IngleApplication
//													.getInstance()
//													.getCurrentUser()
//													+ "@"
//													+ XmppManager.XMPPHOST_STRING);
//									presence.setTo(msgOwnerName);
//									IngleApplication
//											.getConnection().sendPacket(
//													presence);
								}
							}).setNegativeButton(R.string.no,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();

								}
							}).create().show();
				}
				return true;
			}

		});
		lvOffline_MF.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final String msgOwnerName = offlineList.get(position).getUser();
				if (msgOwnerName.substring(0, msgOwnerName.indexOf("@"))
						.equals(
								IngleApplication
										.getInstance().getCurrentUser()) == false) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							ManageFriendsActivity.this);
					builder.setTitle(
							getString(R.string.delFriend)
									+ msgOwnerName.substring(0, msgOwnerName
											.indexOf("@"))).setPositiveButton(
							R.string.yes,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									SmackUtils.removeUser(
											IngleApplication
													.getConnection()
													.getRoster(), msgOwnerName);
								}
							}).setNegativeButton(R.string.no,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();

								}
							}).create().show();
				}
				return true;
			}

		});

	}

	private void addListenerForPresence() {

		// Ϊ������Ӽ������Լ����������ߵȵı仯
		roster.addRosterListener(new RosterListener() {

			public void presenceChanged(Presence presence) {

				sendMessage(UPDATE_FLIST, 0);

			}

			public void entriesUpdated(Collection<String> arg0) {
				sendMessage(UPDATE_FLIST, 0);

			}

			public void entriesDeleted(Collection<String> arg0) {
				// TODO Auto-generated method stub
				sendMessage(UPDATE_FLIST, 0);
			}

			public void entriesAdded(Collection<String> arg0) {
				// TODO Auto-generated method stub
				sendMessage(UPDATE_FLIST, 0);
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// KeyEvent.KEYCODE_BACK��?�ز���.
		if (keyCode == KeyEvent.KEYCODE_SEARCH) {
			onQueryTextSubmit(searchContent.getText().toString());
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(RESULT_OK);
			finish();
		}
		return true;
	}

	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.iBtnSearchToAdd: // ��������
			intent = new Intent(this, SearchFriendsActivity.class);
			startActivity(intent);
			break;
		case R.id.iBtnBackSC_MF:
			setResult(RESULT_OK);
			finish();
			break;
		case R.id.iBtnsearch:
			onQueryTextSubmit(searchContent.getText().toString());
			break;
		default:
			break;
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

	/**
	 * Handler class implementation to handle the message
	 * 
	 * @author SongQing
	 * 
	 */
	private class ManageFriendsHandler extends Handler {

		// This method is used to handle received messages
		public void handleMessage(Message msg) {
			// roster =
			// IngleApplication.getConnection().getRoster();

			switch (msg.what) {
			case UPDATE_FLIST:
				initializeListView();
				break;

			default:
				break;
			}
		}
	}
	public void update() {
		sendMessage(UPDATE_FLIST, 0);
	}
}
