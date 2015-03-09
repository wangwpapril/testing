package com.cuc.miti.phone.xmc.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.adapter.SearchFriendsAdapter;
import com.cuc.miti.phone.xmc.domain.UserBean;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceType;
import com.cuc.miti.phone.xmc.utils.DeviceInfoHelper;
import com.cuc.miti.phone.xmc.utils.SmackUtils;
import com.cuc.miti.phone.xmc.utils.ToastHelper;
import com.cuc.miti.phone.xmc.xmpp.XmppManager;
import com.cuc.miti.phone.xmc.R;

public class SearchFriendsActivity extends BaseActivity implements
		OnClickListener {

	private ListView lvResult_SF;
	private TextView textViewresultNull_SF;

	private List<UserBean> results;
	private SearchFriendsAdapter resultAdapter;

	private ImageButton iBtnBackSC_SF;
	private ImageButton iBtnsearch;
	private EditText searchContent;

	// ��ȡ�����б�
	Roster roster;
	//Collection<RosterEntry> entries;

	// ����״̬isFriendStatus����Ϊ1ʱ��ʾ�Ѿ��Ǻ���
	//private int isFriendStatus = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.search_friends);

		initialize();

		IngleApplication.getInstance().addActivity(this);
	}

	/**
	 * ��ʼ����ؿؼ�
	 */
	private void initialize() {

		roster = IngleApplication.getConnection()
				.getRoster();

//		if (roster != null) {
//			entries = roster.getEntries();
//		}
		setUpViews();
		initializeListView();

	}

	public void onQueryTextSubmit(String query) {

		results.clear();
		try {
			results = SmackUtils.searchUsers(
					IngleApplication.getConnection(),
					"search.chat.xinhuaenews.com", query);

			showResult();

		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void showResult() {
		if (results != null && results.size() > 0) {
			textViewresultNull_SF.setVisibility(View.INVISIBLE);
			lvResult_SF.setVisibility(View.VISIBLE);
			resultAdapter = new SearchFriendsAdapter(results, this);
			lvResult_SF.setAdapter(resultAdapter);
			lvResult_SF.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					// finish();
					AlertDialog.Builder builder = new AlertDialog.Builder(
							SearchFriendsActivity.this);
					// У�鵱ǰҪ��Ϊ���ѵ��û��Ƿ��Ѿ��Ǻ��ѣ����������ظ���ӣ�����ɷ��ͺ�������
					
					if (roster != null) {
						RosterEntry entry = roster.getEntry(results.get(position).getJid());
					

					if (entry!=null&&entry.getType().name().equals("both")) {
						builder.setMessage("���û�������ĺ��ѣ������ظ���ӣ�")
								.setPositiveButton(R.string.confirm_button,
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
											}
										}).create().show();
//						// ToastHelper.showToast("���������Ѿ�������",Toast.LENGTH_SHORT);
//						// ������״̬λΪ�㣬�Է������������Ϊ����ʱ������ǰһ�ε���1���´���
//						isFriendStatus = 0;
					} else {
						if (SmackUtils.addUser(
								IngleApplication
										.getConnection().getRoster(),
								results.get(position).getJid(),null)) {
//							ToastHelper.showToast("���������Ѿ�������",
//									Toast.LENGTH_SHORT);
							builder.setMessage("���������Ѿ�������").setPositiveButton(
									R.string.confirm_button,
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									}).create().show();
						}

						

					}
					}
				}
			});
		} else {
			lvResult_SF.setVisibility(View.INVISIBLE);
			textViewresultNull_SF.setVisibility(View.VISIBLE);
		}

	}

	private void setUpViews() {
		lvResult_SF = (ListView) findViewById(R.id.lvResult_SF);
		textViewresultNull_SF = (TextView) findViewById(R.id.textViewresultNull_SF);
		textViewresultNull_SF.setVisibility(View.INVISIBLE);
		iBtnBackSC_SF = (ImageButton) findViewById(R.id.iBtnBackSC_SF);
		iBtnBackSC_SF.setOnClickListener(this);
		iBtnsearch = (ImageButton) findViewById(R.id.iBtnsearch_SF);
		iBtnsearch.setOnClickListener(this);
		searchContent = (EditText) findViewById(R.id.searchContent_SF);
	}

	private void initializeListView() {

		results = new ArrayList<UserBean>();

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

		switch (v.getId()) {
		case R.id.iBtnBackSC_SF:
			setResult(RESULT_OK);
			finish();
			break;
		case R.id.iBtnsearch_SF:
			onQueryTextSubmit(searchContent.getText().toString());
			break;
		default:
			break;
		}
	}

}
