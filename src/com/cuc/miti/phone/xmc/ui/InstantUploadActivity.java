package com.cuc.miti.phone.xmc.ui;

import java.io.File;
import java.net.InetAddress;

import com.cuc.miti.phone.xmc.R;
import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Globals;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.domain.ManuscriptTemplate;
import com.cuc.miti.phone.xmc.domain.Enums.NetStatus;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceKeys;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceType;
import com.cuc.miti.phone.xmc.domain.Enums.TemplateType;
import com.cuc.miti.phone.xmc.http.RemoteCaller;
import com.cuc.miti.phone.xmc.logic.ManuscriptTemplateService;
import com.cuc.miti.phone.xmc.services.FTPServerService;
import com.cuc.miti.phone.xmc.services.FtpAddNewAccObserver;
import com.cuc.miti.phone.xmc.ui.FtpUsernameActivity;
import com.cuc.miti.phone.xmc.ui.MainActivity;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.SendManuscriptsHelper;
import com.cuc.miti.phone.xmc.utils.SharedPreferencesHelper;
import com.cuc.miti.phone.xmc.utils.StandardizationDataHelper;
import com.cuc.miti.phone.xmc.utils.ToastHelper;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;

public class InstantUploadActivity extends PreferenceActivity implements
		OnPreferenceChangeListener, OnPreferenceClickListener {
	EditTextPreference username_pref, portnum_pref, mPassWordPref;
//	private ListPreference lpCompressRatioChoose;
	Preference chroot_pref, ftp_choosename;
	CheckBoxPreference cbRunning_state, cbShow_password, wakelock_pref,
			cbInstantUpload_pref;
	private FtpAddNewAccObserver ftpObserver;
	SharedPreferencesHelper sharedPreferencesHelper;
	Resources resources;
	private ManuscriptTemplateService mtService = null;
	private ManuscriptTemplate mManuTemplate = null;
	private String sessionId = "";
	private String loginname = "";
	protected static final int MSG_VALIDATE_SUCCESS = 3;
	protected static final int MSG_VALIDATE_FAILED = 4;
	protected static final int MSG_VALIDATE_FAILED_ByNetStatus = 5;
	protected static final int MSG_VALIDATE_ERROR = 6;
	PrefsFtpServerSetHandler mHandler = null;
	private Boolean status = false;

	// private OnGOTOListener mCallback;
	// Ftp_ChooseUsername("ѡ��FTP�û���")
	// pFtpUserName=(Preference)findPreference(PreferenceKeys.Ftp_ChooseUsername.toString());
	// pFtpUserName.setOnPreferenceClickListener((OnPreferenceClickListener)
	// this);
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.ftpserver_set_config_preference);
		initialize();
	}

	/**
	 * ��ʼ��
	 */
	private void initialize() {
		Globals.setContext(this);
		sharedPreferencesHelper = new SharedPreferencesHelper(this);
		resources = getResources();
		mtService = new ManuscriptTemplateService(this);
		ftpObserver = IngleApplication.getListenerFtp();

		if (ftpObserver == null) {
			ftpObserver = new FtpAddNewAccObserver(
					StandardizationDataHelper.getFtpFileFolderStorePath()
							+ "//", this);
		}
		mHandler = new PrefsFtpServerSetHandler();
		this.SetPreferenceItem();
		this.SetPreferenceSourceList();

		sessionId = IngleApplication.getSessionId();
		loginname = IngleApplication.getInstance()
				.getCurrentUser();
	}

	/**
	 * ʵ��Preference��
	 */
	private void SetPreferenceItem() {

		cbRunning_state = (CheckBoxPreference) findPreference(PreferenceKeys.running_state
				.toString());
		cbRunning_state
				.setOnPreferenceClickListener((OnPreferenceClickListener) this);
		cbRunning_state
				.setOnPreferenceChangeListener((OnPreferenceChangeListener) this);
		username_pref = (EditTextPreference) findPreference(PreferenceKeys.username
				.toString());
		username_pref
				.setOnPreferenceChangeListener((OnPreferenceChangeListener) this);
		mPassWordPref = (EditTextPreference) findPreference(PreferenceKeys.password
				.toString());
		mPassWordPref
				.setOnPreferenceChangeListener((OnPreferenceChangeListener) this);
		cbShow_password = (CheckBoxPreference) findPreference(PreferenceKeys.show_password
				.toString());
		cbShow_password.setChecked(false);
		cbShow_password
				.setOnPreferenceChangeListener((OnPreferenceChangeListener) this);

		portnum_pref = (EditTextPreference) findPreference(PreferenceKeys.portNum
				.toString());
		portnum_pref
				.setOnPreferenceChangeListener((OnPreferenceChangeListener) this);
		chroot_pref = (Preference) findPreference(PreferenceKeys.chrootDir
				.toString());
		chroot_pref
				.setOnPreferenceChangeListener((OnPreferenceChangeListener) this);
		ftp_choosename = (Preference) findPreference(PreferenceKeys.Ftp_ChooseUsername
				.toString());
		ftp_choosename
				.setOnPreferenceClickListener((OnPreferenceClickListener) this);
		// wakelock_pref = (CheckBoxPreference)
		// findPreference(PreferenceKeys.stayAwake.toString());
		// wakelock_pref.setOnPreferenceChangeListener((OnPreferenceChangeListener)
		// this);
		cbInstantUpload_pref = (CheckBoxPreference) findPreference(PreferenceKeys.instantUpload
				.toString());
		cbInstantUpload_pref
				.setOnPreferenceChangeListener((OnPreferenceChangeListener) this);

//		lpCompressRatioChoose = (ListPreference) findPreference(PreferenceKeys.User_UploadPicCompressRatio
//				.toString());
//		lpCompressRatioChoose
//				.setOnPreferenceClickListener((OnPreferenceClickListener) this);
//		lpCompressRatioChoose
//				.setOnPreferenceChangeListener((OnPreferenceChangeListener) this);

	}

	/**
	 * ����Preference���Դ
	 */
	private void SetPreferenceSourceList() {
		// ϵͳ����
		this.SetRunningStateSourceList();
		this.SetUsernameSourceList();
		this.setPasswordSourceList();
		this.setPortNumSourceList();
		this.setChrootDirSourceList();
		// this.SetStayAwakeSourceList();
		this.setInstantUploadStateSource();
//		this.setCompressRatioSourceList();

	}

//	private void setCompressRatioSourceList() {
//		CharSequence[] entries = lpCompressRatioChoose.getEntries();
//		int newIndex = 0;
//		CharSequence newText = "";
//		String selectedValue = sharedPreferencesHelper
//				.GetUserPreferenceValue(PreferenceKeys.User_UploadPicCompressRatio
//						.toString());
//
//		if (!selectedValue.equals("")) {
//			lpCompressRatioChoose.setValue(selectedValue);
//			newIndex = lpCompressRatioChoose
//					.findIndexOfValue(lpCompressRatioChoose.getValue());
//			newText = entries[newIndex];
//		} else {
//			String value = "50";
//			newIndex = lpCompressRatioChoose.findIndexOfValue(value);
//			newText = entries[newIndex];
//			lpCompressRatioChoose.setValue(value);
//			sharedPreferencesHelper.SaveUserPreferenceSettings(
//					PreferenceKeys.User_UploadPicCompressRatio.toString(),
//					PreferenceType.String, value);
//		}
//
//		lpCompressRatioChoose.setSummary(newText.toString());
//
//	}

	private void setInstantUploadStateSource() {
		/*
		 * FtpAddNewAccObserver listenerFtp=new
		 * FtpAddNewAccObserver(StandardizationDataHelper
		 * .getFtpFileFolderStorePath()+ "//", mContext );
		 * if(cbInstantUpload_pref.isChecked()) { listenerFtp.startWatching();
		 * }else{ listenerFtp.stopWatching(); }
		 */
		// cbInstantUpload_pref.setChecked(false);

		String selectedValue = sharedPreferencesHelper
				.GetUserPreferenceValue(PreferenceKeys.instantUpload.toString());
		if (!selectedValue.equals("")) {
			if ("true".equals(selectedValue)) {
				valiadateInstantTemplate();
				cbInstantUpload_pref.setChecked(true);

			} else {
				cbInstantUpload_pref.setChecked(false);
			}
		} else {// �������û��״ε�¼ʱselectedValue=����������Ĭ��Ӧ��ֵ��Ϊfalse
			cbInstantUpload_pref.setChecked(false);
		}
	}


	private void setChrootDirSourceList() {

		String ftpFolder = StandardizationDataHelper
				.getFtpFileFolderStorePath() + "//";

		chroot_pref.setSummary(ftpFolder.replaceAll("//", "/"));
	}

	private void setPortNumSourceList() {
		String selectedValue = sharedPreferencesHelper
				.GetUserPreferenceValue(PreferenceKeys.portNum.toString());
		if (!selectedValue.equals("")) {
			portnum_pref.setSummary(selectedValue);
			portnum_pref.setText(selectedValue);
		} else {
			portnum_pref.setSummary(resources
					.getString(R.string.portnumber_default));
			portnum_pref.setText(resources
					.getString(R.string.portnumber_default));
		}
	}

	private void setPasswordSourceList() {
		String password = resources.getString(R.string.password_default);

		String selectedValue = sharedPreferencesHelper
				.GetUserPreferenceValue(PreferenceKeys.password.toString());

		if (!selectedValue.equals("")) {
			password = selectedValue;
		} else {

			password = resources.getString(R.string.password_default);
		}
		mPassWordPref.setSummary(transformPassword(password, true));
		mPassWordPref.setText(password);
	}

	private void SetUsernameSourceList() {
		String selectedValue = sharedPreferencesHelper
				.GetUserPreferenceValue(PreferenceKeys.username.toString());
		if (!selectedValue.equals("")) {
			username_pref.setSummary(selectedValue);
			username_pref.setText(selectedValue);
		} else {

			username_pref.setSummary(resources
					.getString(R.string.username_default));
			username_pref.setText(resources
					.getString(R.string.username_default));
		}

	}

	private void SetRunningStateSourceList() {

		if (FTPServerService.isRunning() == true) {
			cbRunning_state.setChecked(true);
			// Fill in the FTP server address
			InetAddress address = FTPServerService.getWifiIp();
			if (address == null) {
				// Log.v(TAG, "Unable to retreive wifi ip address");
				cbRunning_state.setSummary(R.string.cant_get_url);
				return;
			}
			String iptext = "ftp://" + address.getHostAddress() + ":"
					+ FTPServerService.getPort() + "/";
			String summary = resources.getString(
					R.string.running_summary_started, iptext);
			cbRunning_state.setSummary(summary);
		} else {
			cbRunning_state.setChecked(false);
		}

	}

	private void startServer() {
		Intent serverService = new Intent(this, FTPServerService.class);
		if (!FTPServerService.isRunning()) {
			warnIfNoExternalStorage();
			this.startService(serverService);
		}
	}

	private void stopServer() {
		Intent serverService = new Intent(this, FTPServerService.class);
		this.stopService(serverService);
	}

	@Override
	public void onResume() {
		// Log.v(TAG, "onResume");
		super.onResume();

		// make this class listen for preference changes
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(null);

		// Log.v(TAG, "Registering the FTP server actions");
		IntentFilter filter = new IntentFilter();
		filter.addAction(FTPServerService.ACTION_STARTED);
		filter.addAction(FTPServerService.ACTION_STOPPED);
		filter.addAction(FTPServerService.ACTION_FAILEDTOSTART);
		this.registerReceiver(ftpServerReceiver, filter);
	}

	@Override
	public void onPause() {
		// Log.v(TAG, "onPause");
		super.onPause();

		// Log.v(TAG, "Unregistering the FTPServer actions");
		this.unregisterReceiver(ftpServerReceiver);

		// unregister the listener
		SharedPreferences sprefs = getPreferenceScreen().getSharedPreferences();
		sprefs.unregisterOnSharedPreferenceChangeListener(null);
	}

	/**
	 * This receiver will check FTPServer.ACTION* messages and will update the
	 * button, running_state, if the server is running and will also display at
	 * what url the server is running.
	 */
	BroadcastReceiver ftpServerReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Log.v(TAG, "FTPServerService action received: " +
			// intent.getAction());
			if (intent.getAction().equals(FTPServerService.ACTION_STARTED)) {
				cbRunning_state.setChecked(true);
				// Fill in the FTP server address
				InetAddress address = FTPServerService.getWifiIp();
				if (address == null) {
					// Log.v(TAG, "Unable to retreive wifi ip address");
					cbRunning_state.setSummary(R.string.cant_get_url);
					return;
				}
				String iptext = "ftp://" + address.getHostAddress() + ":"
						+ FTPServerService.getPort() + "/";
				Resources resources = getResources();
				String summary = resources.getString(
						R.string.running_summary_started, iptext);
				cbRunning_state.setSummary(summary);
			} else if (intent.getAction().equals(
					FTPServerService.ACTION_STOPPED)) {
				cbRunning_state.setChecked(false);
				cbRunning_state.setSummary(R.string.running_summary_stopped);
			} else if (intent.getAction().equals(
					FTPServerService.ACTION_FAILEDTOSTART)) {
				cbRunning_state.setChecked(false);
				cbRunning_state.setSummary(R.string.running_summary_failed);
			}
		}
	};

	/**
	 * Will check if the device contains external storage (sdcard) and display a
	 * warning for the user if there is no external storage. Nothing more.
	 */
	private void warnIfNoExternalStorage() {
		String storageState = Environment.getExternalStorageState();
		if (!storageState.equals(Environment.MEDIA_MOUNTED)) {
			// Log.v(TAG, "Warning due to storage state " + storageState);
			/*
			 * Toast toast = Toast.makeText(this, R.string.storage_warning,
			 * Toast.LENGTH_LONG); toast.setGravity(Gravity.CENTER, 0, 0);
			 * toast.show();
			 */
			ToastHelper.showToast(ToastHelper
					.getStringFromResources(R.string.storage_warning),
					Toast.LENGTH_SHORT);
		}
	}

	/**
	 * ת������ɼ�Ͳ��ɼ�
	 * 
	 * @param password
	 *            ����
	 * @param initialize
	 *            ��ʼ����ʶ(��ʼ���͵����������)
	 * @return
	 */
	private static String transformPassword(String password, boolean initialize) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(Globals.getContext());
		Resources res = Globals.getContext().getResources();
		boolean showPassword = res.getString(R.string.show_password_default)
				.equals("true") ? true : false;
		showPassword = sp.getBoolean(PreferenceKeys.show_password.toString(),
				showPassword);
		if (initialize) {
			StringBuilder sb = new StringBuilder(password.length());
			for (int i = 0; i < password.length(); ++i)
				sb.append('*');
			return sb.toString();
		} else {
			if (!showPassword)
				return password;
			else {
				StringBuilder sb = new StringBuilder(password.length());
				for (int i = 0; i < password.length(); ++i)
					sb.append('*');
				return sb.toString();
			}
		}
	}

	public boolean onPreferenceClick(Preference preference) {
		// TODO Auto-generated method stub
		String preferenceKey = preference.getKey();
		if (preferenceKey.equals(PreferenceKeys.Ftp_ChooseUsername.toString())) {
			Intent intent = new Intent();
			intent.setClass(this, FtpUsernameActivity.class);
			startActivity(intent);
			//
			// Intent mIntent = new Intent(mContext,FtpUsernameActivity.class);
			// startActivity(mIntent);
		}
		return true;
	}

	public boolean onPreferenceChange(Preference preference, Object newValue) {
		String preferenceKey = preference.getKey();
		if (preferenceKey.equals(PreferenceKeys.show_password.toString())) {
			Resources res = Globals.getContext().getResources();
			String password = res.getString(R.string.password_default);
			String userP = sharedPreferencesHelper
					.GetUserPreferenceValue(PreferenceKeys.password.toString());
			if (!"".equals(userP)) {
				password = userP;
			} else {
				password = res.getString(R.string.password_default);
			}
			mPassWordPref.setSummary(transformPassword(password, false));
			mPassWordPref.setText(password);
			return this.SaveUserSettings(preferenceKey, PreferenceType.String,
					password);
		} else if (preferenceKey
				.equals(PreferenceKeys.running_state.toString())) {
			if ((Boolean) newValue) {
				startServer();
				// ��ftp�������������ļ��м���
				ftpObserver.startWatching();
			} else {
				stopServer();
				// �ر�ftp���������ر��ļ��м���
				ftpObserver.stopWatching();
			}
			return true;
		} else if (preferenceKey.equals(PreferenceKeys.username.toString())) {
			String newUsername = (String) newValue;
			if (preference.getSummary().equals(newUsername))
				return false;
			if (!newUsername.matches("[a-zA-Z0-9]+")) {
				ToastHelper
						.showToast(
								ToastHelper
										.getStringFromResources(R.string.username_validation_error),
								Toast.LENGTH_SHORT);
				return false;
			}
			preference.setSummary(newUsername);
			stopServer();
			return this.SaveUserSettings(preferenceKey, PreferenceType.String,
					newUsername);
		} else if (preferenceKey.equals(PreferenceKeys.password.toString())) {
			String newPassword = (String) newValue;
			if (!newPassword.matches("[a-zA-Z0-9]+")) {
				ToastHelper
						.showToast(
								ToastHelper
										.getStringFromResources(R.string.password_validation_error),
								Toast.LENGTH_SHORT);
				return false;
			}
			preference.setSummary(transformPassword(newPassword, false));
			stopServer();
			return this.SaveUserSettings(preferenceKey, PreferenceType.String,
					newPassword);
		} else if (preferenceKey.equals(PreferenceKeys.portNum.toString())) {
			String newPortnumString = (String) newValue;
			if (preference.getSummary().equals(newPortnumString))
				return false;
			int portnum = 0;
			try {
				portnum = Integer.parseInt(newPortnumString);
			} catch (Exception e) {
			}
			if (portnum <= 0 || 65535 < portnum) {
				ToastHelper
						.showToast(
								ToastHelper
										.getStringFromResources(R.string.port_validation_error),
								Toast.LENGTH_SHORT);
				return false;
			}
			preference.setSummary(newPortnumString);
			stopServer();
			return this.SaveUserSettings(preferenceKey, PreferenceType.String,
					newPortnumString);
		} else if (preferenceKey.equals(PreferenceKeys.chrootDir.toString())) {
			String newChroot = (String) newValue;
			if (preference.getSummary().equals(newChroot))
				return false;
			// now test the new chroot directory
			File chrootTest = new File(newChroot);
			if (!chrootTest.isDirectory() || !chrootTest.canRead())
				return false;
			preference.setSummary(newChroot);
			stopServer();
			return true;
		} else if (preferenceKey.equals(PreferenceKeys.stayAwake.toString())) {
			stopServer();
			return true;
		} else if (preferenceKey
				.equals(PreferenceKeys.instantUpload.toString())) {
			// FtpAddNewAccObserver listenerFtp=new
			// FtpAddNewAccObserver(StandardizationDataHelper.getFtpFileFolderStorePath()+
			// "//" ,mContext);
			Boolean prefsValue = (Boolean) newValue;
			if ((Boolean) newValue) {

				// ���Ƚ��е�ǰ��¼�û���У�飬�����Ƿ����Ȩ�޽��м��ļ�������
				validateInstantUploadThread();			
		

				return this.SaveUserSettings(preferenceKey,
						PreferenceType.String, String.valueOf(this.status));
			} else {
				// ��Ϊ��ʱֹͣservice
				// ftpObserver.stopWatching();
				return this.SaveUserSettings(preferenceKey,
						PreferenceType.String, String.valueOf(prefsValue));
			}
		} 
//		else if (preferenceKey
//				.equals(PreferenceKeys.User_UploadPicCompressRatio.toString())) {
//			String prefsValue = (String) newValue;
//			CharSequence[] entries = lpCompressRatioChoose.getEntries();
//			int newIndex = lpCompressRatioChoose.findIndexOfValue(prefsValue);
//			CharSequence newText = entries[newIndex];
//			lpCompressRatioChoose.setSummary(newText.toString());
//			// Ϊ������ListPreference �� summary
//			return this.SaveUserSettings(preferenceKey, PreferenceType.String,
//					prefsValue);
//		}

		return true;
	}

	/**
	 * ͨ��SharedPreferences.Editor���޸����ò���
	 * 
	 * @param key
	 *            Preferences Key Ψһ��ʾһ��������
	 * @param pType
	 *            Preference֧�ֶ�������������ͣ�Boolean/Int/Float/Long/String
	 * @param value
	 *            Preference�������Ӧֵ
	 */
	private boolean SaveUserSettings(String key, PreferenceType pType,
			String value) {
		return sharedPreferencesHelper.SaveUserPreferenceSettings(key, pType,
				value);
	}

	public void valiadateInstantTemplate() {
		// ��Ϊ��ʱӦ�������ļ���service������⵽��ͼƬ�ϴ�����ʱ�ʹ򿪼��ļ������������ر�д�ϴ�����
		mManuTemplate = mtService.getManuscriptTemplateSystem(
				IngleApplication.getInstance()
						.getCurrentUser(), TemplateType.INSTANT.toString());
		if(mManuTemplate==null)
		 	mManuTemplate = new ManuscriptTemplate();
		KeyValueData resultMessage = new KeyValueData("", "");
		boolean result = SendManuscriptsHelper.validateForInstantMT(
				mManuTemplate, resultMessage, this);

		if (result == true) {

			// ftpObserver.startWatching();

		} else {
			// mCallback.onGOTOManageTemplate();
			/*
			 * Toast.makeText(mContext, "���Ȳ�ȫ���ļ�����ǩģ��",
			 * Toast.LENGTH_LONG).show();
			 */
			ToastHelper
					.showToast(
							ToastHelper
									.getStringFromResources(R.string.instantUploadTemplateNull_label),
							Toast.LENGTH_SHORT);

			Intent mIntent = new Intent(this, ManagementTemplateActivity.class);
			startActivity(mIntent);

		}
	}

	private void validateInstantUploadThread() {
		new Thread(new Runnable() {
			public void run() {
				try {

					if (IngleApplication.getNetStatus() != NetStatus.Disable) {
						if (RemoteCaller.validateInstantUpload(sessionId,
								loginname)) {
							sendMessage(MSG_VALIDATE_SUCCESS, null);
						} else {
							sendMessage(MSG_VALIDATE_FAILED, "��ǰ��¼�û��޼��ļ���Ȩ��");

						}
					} else {
						sendMessage(MSG_VALIDATE_FAILED_ByNetStatus,
								"��������ԭ���޷�����");
					}
					// setResult(RESULT_OK);
					// finish();
				} catch (Exception e) {
					Logger.e(e);
					sendMessage(MSG_VALIDATE_ERROR, e.getMessage());
					e.printStackTrace();
				}

			}
		}).start();
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

	private class PrefsFtpServerSetHandler extends Handler {

		// This method is used to handle received messages
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case MSG_VALIDATE_SUCCESS:
				valiadateInstantTemplate();
				setStatusASTF(true);
				break;

			case MSG_VALIDATE_ERROR:
				ToastHelper.showToast(msg.obj.toString(), Toast.LENGTH_SHORT);
				break;
			case MSG_VALIDATE_FAILED:
				ToastHelper.showToast(msg.obj.toString(), Toast.LENGTH_SHORT);
				setStatusASTF(false);
				break;
			case MSG_VALIDATE_FAILED_ByNetStatus:
				ToastHelper.showToast(msg.obj.toString(), Toast.LENGTH_SHORT);
				break;
			default:
				break;
			}

		}

	}

	private void setStatusASTF(boolean b) {
		this.status = b;
		if (b == false) {
			this.SaveUserSettings(PreferenceKeys.instantUpload.toString(),
					PreferenceType.String, String.valueOf(this.status));
			cbInstantUpload_pref.setChecked(false);
		} else {
			this.SaveUserSettings(PreferenceKeys.instantUpload.toString(),
					PreferenceType.String, String.valueOf(this.status));
			cbInstantUpload_pref.setChecked(true);
		}

	}
}
