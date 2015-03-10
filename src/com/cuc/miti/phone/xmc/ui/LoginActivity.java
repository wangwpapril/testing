package com.cuc.miti.phone.xmc.ui;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cuc.miti.phone.xmc.R;
import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.LocationDomain;
import com.cuc.miti.phone.xmc.domain.PositionInfo;
import com.cuc.miti.phone.xmc.domain.PostDomain;
import com.cuc.miti.phone.xmc.domain.UserDemo;
import com.cuc.miti.phone.xmc.domain.UserInfoDemo;
import com.cuc.miti.phone.xmc.domain.XmcException;
import com.cuc.miti.phone.xmc.domain.Enums.InterfaceType;
import com.cuc.miti.phone.xmc.domain.Enums.NetStatus;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceKeys;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceType;
import com.cuc.miti.phone.xmc.http.Configuration;
import com.cuc.miti.phone.xmc.http.HttpClient;
import com.cuc.miti.phone.xmc.http.PostParameter;
import com.cuc.miti.phone.xmc.logic.LocationService;
import com.cuc.miti.phone.xmc.logic.UserService;
import com.cuc.miti.phone.xmc.services.AppStatusService;
import com.cuc.miti.phone.xmc.services.ReceiverService;
import com.cuc.miti.phone.xmc.utils.DeviceInfoHelper;
import com.cuc.miti.phone.xmc.utils.SharedPreferencesHelper;
import com.cuc.miti.phone.xmc.utils.StandardizationDataHelper;
import com.cuc.miti.phone.xmc.utils.ToastHelper;
import com.cuc.miti.phone.xmc.xmpp.NotificationService;
import com.google.mitijson.Gson;

public class LoginActivity extends BaseActivity implements OnClickListener {
	private SharedPreferencesHelper sharedPreferencesHelper;
	private DeviceInfoHelper deviceInfoHelper;
	Button imBtnSignIn;
	EditText editTextUserName, editTextPassword;
	TextView txViewServer, txViewConfig;

	UserService userServices = new UserService(LoginActivity.this);

	Boolean onlineState = true;

	Drawable draw = null;
	private EditText etServer; // 锟斤拷锟斤拷姆锟斤拷锟斤拷锟斤拷锟街�
	private EditText etDeviceid; // 锟介看锟借备锟斤拷识
	private EditText etVersionid; // 锟介看锟斤拷前系统锟芥本锟斤拷

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);

		this.setUpViews();

		IngleApplication.getInstance().addActivity(this);
	}

	/**
	 * 锟斤拷始锟斤拷锟截硷拷
	 */
	private void setUpViews() {

		sharedPreferencesHelper = new SharedPreferencesHelper(
				LoginActivity.this);

		imBtnSignIn = (Button) findViewById(R.id.butSignIn);
		editTextUserName = (EditText) findViewById(R.id.signinEmailEditText);
		editTextPassword = (EditText) findViewById(R.id.signinPasswordEditText);
		editTextUserName.setText(sharedPreferencesHelper
				.getPreferenceValue(PreferenceKeys.Sys_CurrentUser.toString()));
		if ("0"
				.equals(sharedPreferencesHelper
						.getPreferenceValue(PreferenceKeys.User_SavePassword
								.toString()))) {
			editTextPassword.setText("");
		} else {
			editTextPassword.setText(sharedPreferencesHelper
					.getPreferenceValue(PreferenceKeys.Sys_CurrentPassword
							.toString()));

		}

		imBtnSignIn.setOnClickListener(this);
		editTextPassword.setTransformationMethod(PasswordTransformationMethod
				.getInstance());

/*		txViewConfig = (TextView) findViewById(R.id.textViewConfig_Login);
		txViewConfig.setText(R.string.setting);
		if (Integer.parseInt(DeviceInfoHelper.getDeviceVersionSDK()) >= 14) {
			txViewConfig.setOnClickListener(this);
		} else {
			txViewConfig.setVisibility(View.GONE);
		}
*/
	}

	public void onClick(View v) {
		switch (v.getId()) {
		// 锟斤拷录
		case R.id.butSignIn:
			try {
				login();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.textViewConfig_Login:
			showSystemConfigDialog();
			break;
		default:
			break;
		}

	}

	/**
	 * 锟斤拷锟斤拷状态转锟斤拷
	 * 
	 * @param os
	 *            锟斤拷锟斤拷状态
	 */
	private void OnlineStateSwitch(Boolean os) {
		// 锟斤拷锟斤拷转为锟斤拷锟斤拷锟斤拷
		if (os == true) {
			onlineState = false;

		}
		// 锟斤拷锟斤拷锟斤拷转为锟斤拷锟斤拷
		else if (os == false) {
			onlineState = true;

		}
	}

	/**
	 * 锟斤拷录
	 */
	private void login() throws Exception {

		try {
			// 锟矫伙拷锟斤拷锟斤拷锟斤拷锟轿拷锟绞憋拷锟绞�
			if ("".equals(editTextUserName.getText().toString().trim())
					|| "".equals(editTextPassword.getText().toString().trim())) {
				// toast = Toast.makeText(LoginActivity.this,
				// "锟矫伙拷锟斤拷锟斤拷锟斤拷氩伙拷锟轿拷锟�,Toast.LENGTH_SHORT);
				// toast.show();

				// Modify by SongQing.20120807
				// Toast锟斤拷为AlertDialog锟皆达到锟矫伙拷锟睫革拷锟斤拷锟斤拷:锟斤拷录页锟斤拷锟斤拷锟绞撅拷锟揭拷锟斤拷锟斤拷曰锟斤拷锟饺达拷锟矫伙拷确锟斤拷
				showAlertDialog(getResources().getString(
						R.string.loginAlertDialogTitle_Login), getResources()
						.getString(R.string.loginAlertDialogMsg_Login));
			} else {// 锟矫伙拷锟斤拷锟斤拷锟斤拷攵硷拷锟轿拷锟绞�
				NetStatus netStatus = IngleApplication
						.getNetStatus();
				if (netStatus != NetStatus.Disable) {
					onlineState = true;
				} else {
					onlineState = false;
				}

				if (onlineState == true) {// 锟斤拷锟斤拷锟铰�
					userServices.userLoginOnline(editTextUserName.getText()
							.toString().trim(), editTextPassword.getText()
							.toString().trim());					
					
					
				} else {// 锟斤拷锟斤拷锟斤拷锟铰�
					userServices.userLoginOffline(editTextUserName.getText()
							.toString().trim(), editTextPassword.getText()
							.toString().trim());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			/*
			 * Toast toast = Toast.makeText(LoginActivity.this,
			 * "系统锟届常",Toast.LENGTH_SHORT); toast.show();
			 */
			ToastHelper.showToast(getResources().getString(
					R.string.loginFailure_Login), Toast.LENGTH_SHORT);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getMenuInflater().inflate(R.menu.login_options_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_optionSet:
			showSystemConfigDialog();

			/*
			 * 锟斤拷锟斤拷姆锟斤拷锟斤拷锟斤拷锟斤拷璞革拷锟斤拷锟�case R.id.menu_deviceid: deviceInfoHelper=new
			 * DeviceInfoHelper(); AlertDialog.Builder builderDeviceID = new
			 * AlertDialog.Builder(this); etDeviceid = new
			 * EditText(LoginActivity.this);
			 * etDeviceid.setText(deviceInfoHelper.getDeviceId());
			 * //etDeviceid.setEnabled(false); builderDeviceID.setTitle("锟借备ID")
			 * .setView(etDeviceid).setNegativeButton("锟截憋拷", new
			 * DialogInterface.OnClickListener() {
			 * 
			 * public void onClick(DialogInterface dialog, int which) {
			 * dialog.dismiss();
			 * 
			 * } }).create().show(); break; case R.id.menu_serverSet:
			 * 
			 * sharedPreferencesHelper = new SharedPreferencesHelper(
			 * LoginActivity.this);
			 * 
			 * AlertDialog.Builder builder = new AlertDialog.Builder(this);
			 * etServer = new EditText(LoginActivity.this); String loginURL =
			 * sharedPreferencesHelper
			 * .getPreferenceValue(PreferenceKeys.Sys_LoginServer.toString());
			 * if(loginURL == null || loginURL.equals("")){
			 * etServer.setText(Configuration.getDefaultServer()); }else{
			 * etServer
			 * .setText(sharedPreferencesHelper.getPreferenceValue(PreferenceKeys
			 * .Sys_LoginServer.toString())); }
			 * 
			 * builder.setTitle("锟斤拷锟矫凤拷锟斤拷锟斤拷锟斤拷址") .setView(etServer)
			 * .setPositiveButton("确锟斤拷", new DialogInterface.OnClickListener() {
			 * 
			 * public void onClick(DialogInterface dialog,int which) { if
			 * (etServer.getText().toString().trim().length() > 0) {
			 * 
			 * Patternp=Pattern.compile(
			 * "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)){3}"
			 * ); String ipStr=etServer.getText().toString().trim(); Matcher
			 * m=p.matcher(ipStr); String ipRight=""; if(m.find()) {
			 * if(!ipStr.endsWith("/")){
			 * ipRight=ipStr.substring(0,m.start()).toLowerCase
			 * ()+m.group()+ipStr.substring(m.end(),ipStr.length())+"/"; }else {
			 * ipRight
			 * =ipStr.substring(0,m.start()).toLowerCase()+m.group()+ipStr
			 * .substring(m.end(),ipStr.length());
			 * 
			 * }
			 * 
			 * }else {
			 * Toast.makeText(LoginActivity.this,"锟斤拷锟斤拷锟絀P锟斤拷址锟斤拷锟较凤拷锟斤拷",Toast.LENGTH_SHORT
			 * ).show(); }
			 * 
			 * 
			 * sharedPreferencesHelper.SaveCommonPreferenceSettings(PreferenceKeys
			 * .Sys_LoginServer.toString(),PreferenceType.String,ipRight);
			 * 
			 * 
			 * //sharedPreferencesHelper.SaveCommonPreferenceSettings(PreferenceKeys
			 * .Sys_LoginServer.toString(),PreferenceType.String,ipStr);
			 * 
			 * 
			 * Configuration.setInitialUrl(ipRight +
			 * StandardizationDataHelper.getServerInterface(ipRight,
			 * InterfaceType.initialUrl)); Toast.makeText(LoginActivity.this,
			 * sharedPreferencesHelper
			 * .getPreferenceValue(PreferenceKeys.Sys_LoginServer
			 * .toString()),Toast.LENGTH_SHORT).show();
			 * 
			 * //TODO 锟斤拷式锟斤拷锟斤拷锟斤拷锟睫革拷 Intent intent = new
			 * Intent(LoginActivity.this,SplashScreenActivity.class);
			 * startActivity(intent); LoginActivity.this.finish(); }
			 * 
			 * } }) .setNegativeButton("取锟斤拷", new
			 * DialogInterface.OnClickListener() {
			 * 
			 * public void onClick(DialogInterface dialog, int which) {
			 * dialog.dismiss();
			 * 
			 * } }).create().show();
			 */

			break;
		case R.id.menu_logout:

			IngleApplication.isStop = true;

			Intent appStatusIntent = new Intent(IngleApplication.getInstance(), AppStatusService.class);

			stopService(appStatusIntent);
			
			Intent notificationIntent = new Intent(IngleApplication.getInstance(), NotificationService.class);
			stopService(notificationIntent);
			
			Intent receiverServiceIntent = new Intent(IngleApplication.getInstance(), ReceiverService.class);
			stopService(receiverServiceIntent);

			for (Activity activity : IngleApplication
					.getInstance().getActivities()) {
				activity.finish();
			}

			System.exit(0);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// KeyEvent.KEYCODE_BACK锟斤拷?锟截诧拷锟斤拷.
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			IngleApplication.getInstance().exit();
		}
		return false;
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷示锟皆伙拷锟斤拷
	 * 
	 * @param strTitle
	 *            锟皆伙拷锟斤拷锟斤拷锟�
	 * @param strMessage
	 *            锟斤拷示锟斤拷锟斤拷
	 */
	private void showAlertDialog(String strTitle, String strMessage) {
		AlertDialog.Builder builder = new Builder(LoginActivity.this);
		builder.setMessage(strMessage);
		builder.setTitle(strTitle);
		builder.setPositiveButton(R.string.confirm_button,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		AlertDialog alertDialog = builder.create();

		// 锟斤拷锟斤拷锟斤拷锟斤拷曰锟斤拷锟斤拷鼙锟斤拷没锟斤拷锟絒锟斤拷锟截硷拷]锟斤拷取锟斤拷锟�锟斤拷锟斤拷锟皆凤拷锟斤拷锟斤拷锟斤拷没锟斤拷锟斤拷锟終eyEvent.KEYCODE_SEARCH,锟皆伙拷锟斤拷锟角伙拷Dismiss锟斤拷
		alertDialog.setCancelable(false);
		// 锟斤拷锟斤拷锟斤拷锟斤拷alertDialog.setCancelable(false);
		// 锟斤拷锟斤拷锟斤拷锟斤拷没锟斤拷锟斤拷锟終eyEvent.KEYCODE_SEARCH,锟皆伙拷锟斤拷锟角伙拷Dismiss锟斤拷,锟斤拷锟斤拷锟絪etOnKeyListener锟斤拷锟矫撅拷锟斤拷锟斤拷锟斤拷锟矫伙拷锟斤拷锟斤拷KeyEvent.KEYCODE_SEARCH
		alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_SEARCH) {
					return true;
				} else {
					return false; // 默锟较凤拷锟斤拷 false
				}
			}
		});

		alertDialog.show();
	}

	/**
	 * 锟斤拷示系统锟斤拷锟矫对伙拷锟斤拷
	 */
	private void showSystemConfigDialog() {
		sharedPreferencesHelper = new SharedPreferencesHelper(
				LoginActivity.this);
		deviceInfoHelper = new DeviceInfoHelper();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		View laView = this.getLayoutInflater().inflate(
				R.layout.login_setcurrentserver_dialog, null);

		etDeviceid = (EditText) laView.findViewById(R.id.editTextDevice_Login);
		etDeviceid.setText(deviceInfoHelper.translateDeviceId());
		etDeviceid.setFilters(new InputFilter[] { new InputFilter() {
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				return source.length() < 1 ? dest.subSequence(dstart, dend)
						: "";
			}
		} });
		// etDeviceid.setKeyListener(null); //锟斤拷锟街凤拷锟斤拷锟结导锟斤拷EditText锟叫碉拷锟斤拷锟斤拷锟睫凤拷全选锟斤拷锟斤拷

		etVersionid = (EditText) laView
				.findViewById(R.id.editTextVersion_Login);
		etVersionid.setText(DeviceInfoHelper
				.getAppVersionName(LoginActivity.this)[1]);
		// etVersionid.setKeyListener(null);
		etVersionid.setFilters(new InputFilter[] { new InputFilter() {
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				return source.length() < 1 ? dest.subSequence(dstart, dend)
						: "";
			}
		} });

		etServer = (EditText) laView
				.findViewById(R.id.editTextSetCurrentServer_Login);
		String loginURL = sharedPreferencesHelper
				.getPreferenceValue(PreferenceKeys.Sys_LoginServer.toString());
		if (loginURL == null || loginURL.equals("")) {
			etServer.setText(Configuration.getDefaultServer());
		} else {
			etServer.setText(sharedPreferencesHelper
					.getPreferenceValue(PreferenceKeys.Sys_LoginServer
							.toString()));
		}

		builder.setTitle(R.string.menu_optionSetTitle).setView(laView)
				.setPositiveButton(R.string.confirm_button,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								if (etServer.getText().toString().trim()
										.length() > 0) {
									String ipStr = etServer.getText()
											.toString().trim();
									String ipRight = deviceInfoHelper
											.validateServerAddressForLogin(
													ipStr, LoginActivity.this);

									sharedPreferencesHelper
											.SaveCommonPreferenceSettings(
													PreferenceKeys.Sys_LoginServer
															.toString(),
													PreferenceType.String,
													ipRight);

									// sharedPreferencesHelper.SaveCommonPreferenceSettings(PreferenceKeys.Sys_LoginServer.toString(),PreferenceType.String,ipStr);

									Configuration
											.setInitialUrl(ipRight
													+ StandardizationDataHelper
															.getServerInterface(
																	ipRight,
																	InterfaceType.initialUrl));
									// Toast.makeText(LoginActivity.this,
									// sharedPreferencesHelper.getPreferenceValue(PreferenceKeys.Sys_LoginServer.toString()),Toast.LENGTH_SHORT).show();
									ToastHelper
											.showToast(
													sharedPreferencesHelper
															.getPreferenceValue(PreferenceKeys.Sys_LoginServer
																	.toString()),
													Toast.LENGTH_SHORT);

									// TODO 锟斤拷式锟斤拷锟斤拷锟斤拷锟睫革拷
									Intent intent = new Intent(
											LoginActivity.this,
											SplashScreenActivity.class);
									startActivity(intent);
									LoginActivity.this.finish();
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
