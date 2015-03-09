package com.cuc.miti.phone.xmc.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ConnectTimeoutException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.ComeFromAddress;
import com.cuc.miti.phone.xmc.domain.Enums.BaseDataFileType;
import com.cuc.miti.phone.xmc.domain.Enums.NetStatus;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceKeys;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceType;
import com.cuc.miti.phone.xmc.domain.Enums.TemplateType;
import com.cuc.miti.phone.xmc.domain.Enums.XmcBool;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.domain.Language;
import com.cuc.miti.phone.xmc.domain.ManuscriptTemplate;
import com.cuc.miti.phone.xmc.domain.NewsCategory;
import com.cuc.miti.phone.xmc.domain.NewsPriority;
import com.cuc.miti.phone.xmc.domain.Place;
import com.cuc.miti.phone.xmc.domain.ProvideType;
import com.cuc.miti.phone.xmc.domain.Region;
import com.cuc.miti.phone.xmc.domain.SendToAddress;
import com.cuc.miti.phone.xmc.domain.XmcException;
import com.cuc.miti.phone.xmc.http.Configuration;
import com.cuc.miti.phone.xmc.http.RemoteCaller;
import com.cuc.miti.phone.xmc.logic.BaseDataService;
import com.cuc.miti.phone.xmc.logic.ComeFromAddressService;
import com.cuc.miti.phone.xmc.logic.LanguageService;
import com.cuc.miti.phone.xmc.logic.ManuscriptTemplateService;
import com.cuc.miti.phone.xmc.logic.NewsCategoryService;
import com.cuc.miti.phone.xmc.logic.NewsPriorityService;
import com.cuc.miti.phone.xmc.logic.PlaceService;
import com.cuc.miti.phone.xmc.logic.ProvideTypeService;
import com.cuc.miti.phone.xmc.logic.RegionService;
import com.cuc.miti.phone.xmc.logic.SendToAddressService;
import com.cuc.miti.phone.xmc.logic.UserService;
import com.cuc.miti.phone.xmc.services.LocationService;
import com.cuc.miti.phone.xmc.utils.DBManager;
import com.cuc.miti.phone.xmc.utils.DeviceInfoHelper;
import com.cuc.miti.phone.xmc.utils.Format;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.SharedPreferencesHelper;
import com.cuc.miti.phone.xmc.utils.StandardizationDataHelper;
import com.cuc.miti.phone.xmc.utils.ToastHelper;
import com.cuc.miti.phone.xmc.R;

public class SystemConfigActivity extends PreferenceActivity implements
		OnPreferenceChangeListener, OnPreferenceClickListener {

	private static final int MSG_GETTOPICLIST_ERROR = 1;
	private static final int MSG_GETTOPICLIST_END = 4;
	private static final int MSG_VALIDATE_VERSION_FINISH = 5;
	private static final int MSG_VALIDATE_VERSION_ERROR = 6;
	private static final int MSG_VERSION_UPDATE_START = 7;
	private static final int MSG_CLEAN_DATA_FINISH = 8;
	private static final int MSG_CLEAN_DATA_ERROR = 9;
	private static final int MSG_SYN_TEMP_FINISH = 10;
	private static final int MSG_SYN_TEMP_ERROR = 11;

	private static final int MSG_NETWORK_TIMEOUT = -1;
	private static int NORMAL_TIMEOUT = 6000; // ��ͨ��������ʱ

	private ComeFromAddressService mComeFromAddressService;
	private LanguageService mLanguageService;
	private PlaceService mPlaceServices;
	private NewsCategoryService mNewsCategoryServices;
	private NewsPriorityService mNewsPriorityService;
	private ProvideTypeService mProvideTypeService;
	private RegionService mRegionService;
	private SendToAddressService mSendToAddressService;
	private BaseDataService mBaseDataService;

	private SystemConfigHandler mHandler = null;

	/*
	 * private RelativeLayout uploadLayout; private TextView
	 * fileNameTextView,proTextView; private ProgressBar progressBar;
	 */
	private ProgressDialog dialog = null;
	private AlertDialog dialogAlert = null;

	public static final String NO_SELECTION = "0";
	/*
	 * public static final String SELECT_TEMPLATE_KEY = "temlplate_select";
	 * public static final String SELECT_PACKSIZE_KEY="filepacksize_select";
	 * public static final String SET_AUTOSAVEINTERVAL_KEY =
	 * "autosaveinterval_set"; public static final String
	 * SELECT_NEWSSENDPOLICY_KEY="newssendpolicy_select"; public static final
	 * String SELECT_DELETEPOLICY_KEY="deletepolicy_select"; public static final
	 * String SELECT_FAILUREPOLICY_KEY="failurepolicy_select"; public static
	 * final String SET_SENDNUM_KEY="sendnum_set"; public static final String
	 * SELECT_ENCRYPTPOLICY_KEY="encryptpolicy_select"; public static final
	 * String SELECT_SENDBYWIFI_KEY="sendbywifi_select"; public static final
	 * String SET_VERSION_KEY = "version_set"; public static final String
	 * SET_CURRENTUSER_KEY="currentuser_set"; public static final String
	 * SET_CURRENTSERVER_KEY="currentserver_set";
	 */
	// public static final String SET_SENDBYWIFI_KEY="sendbywifi_set";

	private ManuscriptTemplateService mtServices;

	// �û��Զ���SharedPreferences ��������
	private SharedPreferencesHelper sharedPreferencesHelper;
	private ListPreference lpTemplateChoose, lpFilePackSizeChoose,
			lpNewsSendPolicyChoose, lpSendByWifiChoose;
	private ListPreference lpAutoSaveIntervalChoose,lpCompressRatioChoose,
			lpAutoSendLocationIntervalChoose, lpSavePasswordChoose;
	private Preference pVersion, pCurrentUser, pDeviceID, pNetStatus,
			pLoginType, pUpdateBaseData, pSynManuTemp, pUpdateSysVersion,
			pCleanData, pCurrentServer;
	private EditTextPreference pLoginServer;

	private UserService userServices = new UserService(
			SystemConfigActivity.this);
	private DeviceInfoHelper deviceInfoHelper = new DeviceInfoHelper();

	private static String apkUrl = ""; // ϵͳ��װ��·��
	private static String savePath = ""; // ���ذ�װ·��
	private static String saveFileName = ""; // ����·����(�����ļ���)

	/* �������֪ͨuiˢ�µ�handler��msg���� */
	private ProgressBar mProgress;
	private static final int DOWN_UPDATE = 12;
	private static final int DOWN_OVER = 13;
	protected static final int MSG_SHOWNOTICEDIALOG = 14;
	protected static final int MSG_SHOWNOTICEDIALOGOPTION = 15;
	private int progress;
	private boolean interceptFlag = false;
	private AlertDialog.Builder builder = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.userpreference);
		// ��ȡһ�����Preferences ����
		sharedPreferencesHelper = new SharedPreferencesHelper(
				SystemConfigActivity.this);
		mtServices = new ManuscriptTemplateService(SystemConfigActivity.this);
		this.SetPreferenceItem();
		this.SetPreferenceSourceList();

		IngleApplication.getInstance().addActivity(this);

		apkUrl = Configuration.getSystemUpgradeUrl();
		// For Test
		// apkUrl =
		// "http://www.happytjp.com/UploadFile/XinhuaManuscriptCollection.apk";
		savePath = StandardizationDataHelper.GetBaseDataFileStorePath();
		saveFileName = savePath
				+ apkUrl.substring(apkUrl.lastIndexOf("/"), apkUrl.length());

		this.setUpViews();

	}

	/**
	 * ��ʼ��ҳ��ؼ�
	 */
	private void setUpViews() {
		mHandler = new SystemConfigHandler();
		/*
		 * uploadLayout=(RelativeLayout)findViewById(R.id.updateLayout_sysconfig)
		 * ; progressBar=(ProgressBar)findViewById(R.id.proBar_sysconfig);
		 * proTextView=(TextView)findViewById(R.id.proText_sysconfig);
		 * fileNameTextView=(TextView)findViewById(R.id.fileName_sysconfig);
		 */
	}

	private void SetPreferenceItem() {
		lpTemplateChoose = (ListPreference) findPreference(PreferenceKeys.User_DefaultTemplate
				.toString());
		lpTemplateChoose
				.setOnPreferenceClickListener((OnPreferenceClickListener) this);
		lpTemplateChoose
				.setOnPreferenceChangeListener((OnPreferenceChangeListener) this);

		lpFilePackSizeChoose = (ListPreference) findPreference(PreferenceKeys.User_FileBlockSize
				.toString());
		lpFilePackSizeChoose
				.setOnPreferenceClickListener((OnPreferenceClickListener) this);
		lpFilePackSizeChoose
				.setOnPreferenceChangeListener((OnPreferenceChangeListener) this);

		lpSavePasswordChoose = (ListPreference) findPreference(PreferenceKeys.User_SavePassword
				.toString());
		lpSavePasswordChoose
				.setOnPreferenceClickListener((OnPreferenceClickListener) this);
		lpSavePasswordChoose
				.setOnPreferenceChangeListener((OnPreferenceChangeListener) this);
		lpCompressRatioChoose = (ListPreference) findPreference(PreferenceKeys.User_UploadPicCompressRatio
				.toString());
		lpCompressRatioChoose
				.setOnPreferenceClickListener((OnPreferenceClickListener) this);
		lpCompressRatioChoose
				.setOnPreferenceChangeListener((OnPreferenceChangeListener) this);
		// lpNewsSendPolicyChoose =
		// (ListPreference)findPreference(PreferenceKeys.User_ManuscriptSendPolicy.toString());
		// lpNewsSendPolicyChoose.setOnPreferenceClickListener((OnPreferenceClickListener)
		// this);
		// lpNewsSendPolicyChoose.setOnPreferenceChangeListener((OnPreferenceChangeListener)
		// this);
		//
		// lpFailurePolicyChoose =
		// (ListPreference)findPreference(PreferenceKeys.User_FailurePolicy.toString());
		// lpFailurePolicyChoose.setOnPreferenceClickListener((OnPreferenceClickListener)
		// this);
		// lpFailurePolicyChoose.setOnPreferenceChangeListener((OnPreferenceChangeListener)
		// this);
		//
		// lpSendByWifiChoose =
		// (ListPreference)findPreference(PreferenceKeys.User_SendByWifi.toString());
		// lpSendByWifiChoose.setOnPreferenceClickListener((OnPreferenceClickListener)
		// this);
		// lpSendByWifiChoose.setOnPreferenceChangeListener((OnPreferenceChangeListener)
		// this);

		lpAutoSaveIntervalChoose = (ListPreference) findPreference(PreferenceKeys.User_AutoSaveInterval
				.toString());
		lpAutoSaveIntervalChoose
				.setOnPreferenceClickListener((OnPreferenceClickListener) this);
		lpAutoSaveIntervalChoose
				.setOnPreferenceChangeListener((OnPreferenceChangeListener) this);

		lpAutoSendLocationIntervalChoose = (ListPreference) findPreference(PreferenceKeys.User_AutoSendLocationInterval
				.toString());
		lpAutoSendLocationIntervalChoose
				.setOnPreferenceClickListener((OnPreferenceClickListener) this);
		lpAutoSendLocationIntervalChoose
				.setOnPreferenceChangeListener((OnPreferenceChangeListener) this);

		/*
		 * lpDeletePolicyChoose =
		 * (ListPreference)findPreference(SELECT_DELETEPOLICY_KEY);
		 * lpDeletePolicyChoose
		 * .setOnPreferenceClickListener((OnPreferenceClickListener) this);
		 * lpDeletePolicyChoose
		 * .setOnPreferenceChangeListener((OnPreferenceChangeListener) this);
		 * 
		 * lpEncryptPolicyChoose =
		 * (ListPreference)findPreference(SELECT_ENCRYPTPOLICY_KEY);
		 * lpEncryptPolicyChoose
		 * .setOnPreferenceClickListener((OnPreferenceClickListener) this);
		 * lpEncryptPolicyChoose
		 * .setOnPreferenceChangeListener((OnPreferenceChangeListener) this);
		 * 
		 * 
		 * lpSendNumChoose = (ListPreference)findPreference(SET_SENDNUM_KEY);
		 * lpSendNumChoose
		 * .setOnPreferenceClickListener((OnPreferenceClickListener) this);
		 * lpSendNumChoose
		 * .setOnPreferenceChangeListener((OnPreferenceChangeListener) this);
		 */

		pVersion = (Preference) findPreference(PreferenceKeys.Sys_SystemVersion
				.toString());
		pCurrentUser = (Preference) findPreference(PreferenceKeys.Sys_CurrentUser
				.toString());
		pDeviceID = (Preference) findPreference(PreferenceKeys.Sys_DeviceID
				.toString());
		pNetStatus = (Preference) findPreference(PreferenceKeys.Sys_NetStatus
				.toString());
		pLoginType = (Preference) findPreference(PreferenceKeys.Sys_LoginType
				.toString());

		pUpdateBaseData = (Preference) findPreference(PreferenceKeys.Sys_BaseData
				.toString());
		pUpdateBaseData
				.setOnPreferenceClickListener((OnPreferenceClickListener) this);

		pSynManuTemp = (Preference) findPreference(PreferenceKeys.Sys_SynManuTemp
				.toString());
		pSynManuTemp
				.setOnPreferenceClickListener((OnPreferenceClickListener) this);

		pUpdateSysVersion = (Preference) findPreference(PreferenceKeys.Sys_VersionUp
				.toString());
		pUpdateSysVersion
				.setOnPreferenceClickListener((OnPreferenceClickListener) this);

		pCleanData = (Preference) findPreference(PreferenceKeys.Sys_CleanData
				.toString());
		pCleanData
				.setOnPreferenceClickListener((OnPreferenceClickListener) this);

		pCurrentServer = (Preference) findPreference(PreferenceKeys.Sys_CurrentServer
				.toString());
		pCurrentServer
				.setOnPreferenceClickListener((OnPreferenceClickListener) this);
		pCurrentServer
				.setOnPreferenceChangeListener((OnPreferenceChangeListener) this);

		pLoginServer = (EditTextPreference) findPreference(PreferenceKeys.Sys_LoginServer
				.toString());
		pLoginServer
				.setOnPreferenceClickListener((OnPreferenceClickListener) this);
		pLoginServer
				.setOnPreferenceChangeListener((OnPreferenceChangeListener) this);

		/*
		 * cpSendByWifiSet =
		 * (CheckBoxPreference)findPreference(SELECT_SENDBYWIFI_KEY);
		 * cpSendByWifiSet
		 * .setOnPreferenceClickListener((OnPreferenceClickListener) this);
		 * cpSendByWifiSet
		 * .setOnPreferenceChangeListener((OnPreferenceChangeListener) this);
		 */

	}

	private void SetPreferenceSourceList() {
		try {
			// �û�����
			this.SetManuscriptTemplatePreferenceSourceList();
			this.SetFilePackSizePreferenceSourceList();
			// this.SetNewsSendPolicyPreferenceSourceList();
			// this.SetDeletePolicyPreferenceSourceList();
			// this.SetFailurePolicyPreferenceSourceList();
			// this.SetEncryptPolicyPreferenceSourceList();
			// this.SetSendByWifiPreferenceSourceList();
			this.SetAutoSaveIntervalPreferenceSourceList();
			// this.SetSendNumPreferenceSourceList();
			this.SetAutoSendLocationIntervalPreferenceSourceList();

			this.SetSavePasswordPreferenceSourceList();
			this.setCompressRatioSourceList();

			// ϵͳ����
			this.SetVersionceSourceList();
			this.SetCurrentUserSourceList();
			this.setCurrentDeviceID();
			this.setCurrentNetStatus();
			this.setCurrentLoginType();
			this.SetCurrentServerSourceList();
			this.SetLoginServerSourceList();
		} catch (Exception e) {
			Logger.e(e);
		}

	}

	private void SetManuscriptTemplatePreferenceSourceList() {
		// ��ݵ�ǰ��½���û�ȡ�����е��û��Զ����ǩģ��
		String currentUser = userServices.getCurrentUserFromPreference();
		List<ManuscriptTemplate> mtList = mtServices
				.getManuscriptTemplateCustomList(currentUser,
						TemplateType.NORMAL.toString());
		if (mtList != null && mtList.size() > 0) {
			CharSequence[] entries = new CharSequence[mtList.size()];
			CharSequence[] entryValues = new CharSequence[mtList.size()];
			int counter = 0;
			String defaultTemplateIdString = "";
			for (ManuscriptTemplate mt : mtList) {
				if (mt.getIsdefault() == XmcBool.True) {
					defaultTemplateIdString = mt.getMt_id();
				}
				entries[counter] = mt.getName();
				entryValues[counter] = mt.getMt_id();
				counter++;
			}

			lpTemplateChoose.setEntries(entries);
			lpTemplateChoose.setEntryValues(entryValues);

			// ToDo..������Ҫ�ع���Ŀǰ�ķ�����Ȼʵ���˻�ȡEntry��Text�����ǱȽ��鷳��Ӧ���н�Ϊ���ķ���
			entries = lpTemplateChoose.getEntries();

			// String value=mtServices.getDefaultManuscriptTemplate(currentUser,
			// XmcBool.True.getValue()).getMt_id();

			String valueXML = sharedPreferencesHelper
					.GetUserPreferenceValue(PreferenceKeys.User_DefaultTemplate
							.toString());

			int newIndex = 0;
			CharSequence newText = "";
			/*
			 * if(lpTemplateChoose.getValue()!=null){ newIndex =
			 * lpTemplateChoose.findIndexOfValue(lpTemplateChoose.getValue());
			 * newText = entries[newIndex]; }
			 */

			if (valueXML != null && !valueXML.equals("")) {
				newIndex = lpTemplateChoose.findIndexOfValue(valueXML);
				newText = entries[newIndex];
				lpTemplateChoose.setValue(valueXML);
			} else {
				if (!defaultTemplateIdString.equals("")) {
					newIndex = lpTemplateChoose
							.findIndexOfValue(defaultTemplateIdString);
					newText = entries[newIndex];
					lpTemplateChoose.setValue(defaultTemplateIdString);
				}
			}

			lpTemplateChoose.setSummary(newText.toString());
			// Ϊ������ListPreference �� summary
			// lpTemplateChoose.setDefaultValue(defaultTemplateIdString);

		} else {
			lpTemplateChoose.setSummary(R.string.prefs_noTemplateToSelect);
			lpTemplateChoose.setEnabled(false);
		}
	}

	private void SetFilePackSizePreferenceSourceList() {
		CharSequence[] entries = lpFilePackSizeChoose.getEntries();
		int newIndex = 0;
		CharSequence newText = "";
		String selectedValue = sharedPreferencesHelper
				.GetUserPreferenceValue(PreferenceKeys.User_FileBlockSize
						.toString());

		if (!selectedValue.equals("")) {
			lpFilePackSizeChoose.setValue(selectedValue);
			newIndex = lpFilePackSizeChoose
					.findIndexOfValue(lpFilePackSizeChoose.getValue());
			newText = entries[newIndex];
		} else {
			String value = "131072";
			newIndex = lpFilePackSizeChoose.findIndexOfValue(value);
			newText = entries[newIndex];
			lpFilePackSizeChoose.setValue(value);
			sharedPreferencesHelper.SaveUserPreferenceSettings(
					PreferenceKeys.User_FileBlockSize.toString(),
					PreferenceType.String, value);
		}

		lpFilePackSizeChoose.setSummary(newText.toString());
	}

	/*
	 * private void SetNewsSendPolicyPreferenceSourceList() { CharSequence[]
	 * entries=lpNewsSendPolicyChoose.getEntries(); int newIndex = 0;
	 * CharSequence newText = ""; if(lpNewsSendPolicyChoose.getValue()!=null){
	 * newIndex =
	 * lpNewsSendPolicyChoose.findIndexOfValue(lpNewsSendPolicyChoose.
	 * getValue()); newText = entries[newIndex]; }
	 * lpNewsSendPolicyChoose.setSummary(newText.toString());
	 * //Ϊ������ListPreference �� summary
	 * lpNewsSendPolicyChoose.setDefaultValue(NO_SELECTION); }
	 * 
	 * private void SetFailurePolicyPreferenceSourceList() { CharSequence[]
	 * entries=lpFailurePolicyChoose.getEntries(); int newIndex = 0;
	 * CharSequence newText = ""; if(lpFailurePolicyChoose.getValue()!=null){
	 * newIndex =
	 * lpFailurePolicyChoose.findIndexOfValue(lpFailurePolicyChoose.getValue());
	 * newText = entries[newIndex]; }
	 * lpFailurePolicyChoose.setSummary(newText.toString());
	 * //Ϊ������ListPreference �� summary
	 * lpFailurePolicyChoose.setDefaultValue(NO_SELECTION); }
	 * 
	 * private void SetDeletePolicyPreferenceSourceList() { CharSequence[]
	 * entries=lpDeletePolicyChoose.getEntries(); int newIndex = 0; CharSequence
	 * newText = ""; if(lpDeletePolicyChoose.getValue()!=null){ newIndex =
	 * lpDeletePolicyChoose.findIndexOfValue(lpDeletePolicyChoose.getValue());
	 * newText = entries[newIndex]; }
	 * lpDeletePolicyChoose.setSummary(newText.toString()); //Ϊ������ListPreference
	 * �� summary lpDeletePolicyChoose.setDefaultValue(NO_SELECTION); }
	 * 
	 * private void SetEncryptPolicyPreferenceSourceList() { CharSequence[]
	 * entries=lpEncryptPolicyChoose.getEntries(); int newIndex = 0;
	 * CharSequence newText = ""; if(lpEncryptPolicyChoose.getValue()!=null){
	 * newIndex =
	 * lpEncryptPolicyChoose.findIndexOfValue(lpEncryptPolicyChoose.getValue());
	 * newText = entries[newIndex]; }
	 * lpEncryptPolicyChoose.setSummary(newText.toString());
	 * //Ϊ������ListPreference �� summary
	 * lpEncryptPolicyChoose.setDefaultValue(NO_SELECTION); }
	 * 
	 * private void SetSendNumPreferenceSourceList() { CharSequence[]
	 * entries=lpSendNumChoose.getEntries(); int newIndex = 0; CharSequence
	 * newText = ""; if(lpSendNumChoose.getValue()!=null){ newIndex =
	 * lpSendNumChoose.findIndexOfValue(lpSendNumChoose.getValue()); newText =
	 * entries[newIndex]; } lpSendNumChoose.setSummary(newText.toString());
	 * //Ϊ������ListPreference �� summary
	 * lpSendNumChoose.setDefaultValue(NO_SELECTION); }
	 * 
	 * private void SetSendByWifiPreferenceSourceList() { CharSequence[]
	 * entries=lpSendByWifiChoose.getEntries(); int newIndex = 0; CharSequence
	 * newText = ""; if(lpSendByWifiChoose.getValue()!=null){ newIndex =
	 * lpSendByWifiChoose.findIndexOfValue(lpSendByWifiChoose.getValue());
	 * newText = entries[newIndex]; }
	 * lpSendByWifiChoose.setSummary(newText.toString()); //Ϊ������ListPreference ��
	 * summary lpSendByWifiChoose.setDefaultValue(NO_SELECTION);
	 * 
	 * 
	 * //String
	 * value=sharedPreferencesHelper.GetUserPreferenceValue("SET_SENDBYWIFI_KEY"
	 * ); // cpSendByWifiSet.setChecked(Boolean.valueOf(value).booleanValue());
	 * 
	 * }
	 */

	private void SetAutoSaveIntervalPreferenceSourceList() {
		CharSequence[] entries = lpAutoSaveIntervalChoose.getEntries();
		int newIndex = 0;
		CharSequence newText = "";
		String selectedValue = sharedPreferencesHelper
				.GetUserPreferenceValue(PreferenceKeys.User_AutoSaveInterval
						.toString());
		if (!selectedValue.equals("")) {
			lpAutoSaveIntervalChoose.setValue(selectedValue);
			newIndex = lpAutoSaveIntervalChoose.findIndexOfValue(selectedValue);
			newText = entries[newIndex];
		} else {
			String value = "0";
			newIndex = lpAutoSaveIntervalChoose.findIndexOfValue(value);
			newText = entries[newIndex];
			lpAutoSaveIntervalChoose.setValue(value);

			sharedPreferencesHelper.SaveUserPreferenceSettings(
					PreferenceKeys.User_AutoSaveInterval.toString(),
					PreferenceType.String, value);
		}

		lpAutoSaveIntervalChoose.setSummary(newText.toString());
	}

	private void SetAutoSendLocationIntervalPreferenceSourceList() {
		CharSequence[] entries = lpAutoSendLocationIntervalChoose.getEntries();
		int newIndex = 0;
		CharSequence newText = "";
		String selectedValue = sharedPreferencesHelper
				.GetUserPreferenceValue(PreferenceKeys.User_AutoSendLocationInterval
						.toString());
		if (!selectedValue.equals("")) {
			lpAutoSendLocationIntervalChoose.setValue(selectedValue);
			newIndex = lpAutoSendLocationIntervalChoose
					.findIndexOfValue(selectedValue);
			newText = entries[newIndex];
		} else {
			String value = "0";
			newIndex = lpAutoSendLocationIntervalChoose.findIndexOfValue(value);
			newText = entries[newIndex];
			lpAutoSendLocationIntervalChoose.setValue(value);

			sharedPreferencesHelper.SaveUserPreferenceSettings(
					PreferenceKeys.User_AutoSendLocationInterval.toString(),
					PreferenceType.String, value);
		}

		lpAutoSendLocationIntervalChoose.setSummary(newText.toString());
	}

	private void SetSavePasswordPreferenceSourceList() {

		CharSequence[] entries = lpSavePasswordChoose.getEntries();
		int newIndex = 0;
		CharSequence newText = "";
		String selectedValue = sharedPreferencesHelper
				.GetUserPreferenceValue(PreferenceKeys.User_SavePassword
						.toString());
		if (!selectedValue.equals("")) {
			lpSavePasswordChoose.setValue(selectedValue);
			newIndex = lpSavePasswordChoose.findIndexOfValue(selectedValue);
			newText = entries[newIndex];
		} else {
			String value = "1";
			newIndex = lpSavePasswordChoose.findIndexOfValue(value);
			newText = entries[newIndex];
			lpSavePasswordChoose.setValue(value);

			sharedPreferencesHelper.SaveUserPreferenceSettings(
					PreferenceKeys.User_SavePassword.toString(),
					PreferenceType.String, value);
		}

		lpSavePasswordChoose.setSummary(newText.toString());
	}

	private void SetVersionceSourceList() {

		String CurrentVersionName = DeviceInfoHelper
				.getAppVersionName(SystemConfigActivity.this)[1];

		// sharedPreferencesHelper.SaveCommonPreferenceSettings(PreferenceKeys.Sys_SystemVersion.toString(),
		// PreferenceType.String, CurrentVersionName);

		// String
		// value=sharedPreferencesHelper.getPreferenceValue(PreferenceKeys.Sys_SystemVersion.toString());

		// pVersion.setSummary(value);
		pVersion.setSummary(CurrentVersionName);
	}

	private void SetCurrentUserSourceList() {
		String currentUser = userServices.getCurrentUserFromPreference();
		pCurrentUser.setSummary(currentUser);
	}

	private void setCurrentDeviceID() {
		String currentDeviceID = deviceInfoHelper.translateDeviceId();
		pDeviceID.setSummary(currentDeviceID);

	}

	private void setCurrentNetStatus() {
		String currentNetStatus = IngleApplication
				.getNetStatus().getValue();

		pNetStatus.setSummary(currentNetStatus);
	}

	// ToDO
	private void setCurrentLoginType() {
		String currentLoginStatus = IngleApplication
				.getLoginStatus().getValue();

		pLoginType.setSummary(currentLoginStatus);

	}

	private void SetCurrentServerSourceList() {
		String currentServer = sharedPreferencesHelper
				.getPreferenceValue(PreferenceKeys.Sys_CurrentServer.toString());
		if (currentServer == null || currentServer.equals("")) { // ����û�û��������ڷ���������Ĭ��ϵͳ��д���ķ�������½
			currentServer = Configuration.getDefaultServer();
			sharedPreferencesHelper.SaveCommonPreferenceSettings(
					PreferenceKeys.Sys_CurrentServer.toString(),
					PreferenceType.String, currentServer);
		}
		pCurrentServer.setSummary(currentServer);
	}

	private void SetLoginServerSourceList() {
		String loginServer = sharedPreferencesHelper
				.getPreferenceValue(PreferenceKeys.Sys_LoginServer.toString());
		// if(loginServer ==null || loginServer.equals("")){
		// loginServer = Configuration.getDefaultServer();
		// sharedPreferencesHelper.SaveCommonPreferenceSettings(PreferenceKeys.Sys_LoginServer.toString(),
		// PreferenceType.String, loginServer);
		// }
		pLoginServer.setSummary(loginServer);
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
		// String currentUser = userServices.getCurrentUserFromPreference();
		// if(currentUser !=null && currentUser !=""){
		// return
		// sharedPreferencesHelper.SaveUserPreferenceSettings(currentUser+"_preference",
		// key, pType, value);
		// }

		// return false;

		// TODO ����ʱ����
		return sharedPreferencesHelper.SaveUserPreferenceSettings(key, pType,
				value);
	}
	private void setCompressRatioSourceList() {
		CharSequence[] entries = lpCompressRatioChoose.getEntries();
		int newIndex = 0;
		CharSequence newText = "";
		String selectedValue = sharedPreferencesHelper
				.GetUserPreferenceValue(PreferenceKeys.User_UploadPicCompressRatio
						.toString());

		if (!selectedValue.equals("")) {
			lpCompressRatioChoose.setValue(selectedValue);
			newIndex = lpCompressRatioChoose
					.findIndexOfValue(lpCompressRatioChoose.getValue());
			newText = entries[newIndex];
		} else {
			String value = "50";
			newIndex = lpCompressRatioChoose.findIndexOfValue(value);
			newText = entries[newIndex];
			lpCompressRatioChoose.setValue(value);
			sharedPreferencesHelper.SaveUserPreferenceSettings(
					PreferenceKeys.User_UploadPicCompressRatio.toString(),
					PreferenceType.String, value);
		}

		lpCompressRatioChoose.setSummary(newText.toString());

	}
	/**
	 * ��Ӧpreference��ֵ���ı���¼�����ʱ���ı��ֵ��Ҫ�Լ�����SharedPreferences��
	 */
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		// TODO Auto-generated method stub
		String preferenceKey = preference.getKey();

		if (preferenceKey
				.equals(PreferenceKeys.User_DefaultTemplate.toString())) {
			String prefsValue = newValue.toString();
			// �����õ�Ĭ��ģ��浽��ݿ�
			ManuscriptTemplate mtItem = mtServices
					.getManuscriptTemplateById(prefsValue);
			mtServices.setTemplateAsDefault(mtItem);
			// ToDo..������Ҫ�ع���Ŀǰ�ķ�����Ȼʵ���˻�ȡEntry��Text�����ǱȽ��鷳��Ӧ���н�Ϊ���ķ���
			CharSequence[] entries = lpTemplateChoose.getEntries();
			int newIndex = lpTemplateChoose.findIndexOfValue(prefsValue);
			CharSequence newText = entries[newIndex];
			lpTemplateChoose.setSummary(newText.toString());
			// Ϊ������ListPreference �� summary
			return this.SaveUserSettings(preferenceKey, PreferenceType.String,
					prefsValue);
		} else if (preferenceKey.equals(PreferenceKeys.User_FileBlockSize
				.toString())) {
			String prefsValue = newValue.toString();
			// ToDo..������Ҫ�ع���Ŀǰ�ķ�����Ȼʵ���˻�ȡEntry��Text�����ǱȽ��鷳��Ӧ���н�Ϊ���ķ���
			CharSequence[] entries = lpFilePackSizeChoose.getEntries();
			int newIndex = lpFilePackSizeChoose.findIndexOfValue(prefsValue);
			CharSequence newText = entries[newIndex];
			lpFilePackSizeChoose.setSummary(newText.toString());
			// Ϊ������ListPreference �� summary
			return this.SaveUserSettings(preferenceKey, PreferenceType.String,
					prefsValue);
		} else if (preferenceKey
				.equals(PreferenceKeys.User_ManuscriptSendPolicy.toString())) {
			String prefsValue = newValue.toString();
			// ToDo..������Ҫ�ع���Ŀǰ�ķ�����Ȼʵ���˻�ȡEntry��Text�����ǱȽ��鷳��Ӧ���н�Ϊ���ķ���
			CharSequence[] entries = lpNewsSendPolicyChoose.getEntries();
			int newIndex = lpNewsSendPolicyChoose.findIndexOfValue(prefsValue);
			CharSequence newText = entries[newIndex];
			lpNewsSendPolicyChoose.setSummary(newText.toString());
			// Ϊ������ListPreference �� summary
			return this.SaveUserSettings(preferenceKey, PreferenceType.String,
					prefsValue);
		}
		/*
		 * else if(preferenceKey.equals(SELECT_DELETEPOLICY_KEY)) { String
		 * prefsValue = newValue.toString();
		 * //ToDo..������Ҫ�ع���Ŀǰ�ķ�����Ȼʵ���˻�ȡEntry��Text�����ǱȽ��鷳��Ӧ���н�Ϊ���ķ���
		 * CharSequence[] entries = lpDeletePolicyChoose.getEntries(); int
		 * newIndex = lpDeletePolicyChoose.findIndexOfValue(prefsValue);
		 * CharSequence newText = entries[newIndex];
		 * lpDeletePolicyChoose.setSummary(newText.toString());
		 * //Ϊ������ListPreference �� summary return
		 * this.SaveUserSettings(preferenceKey, PreferenceType.String,
		 * prefsValue); }
		 */
		/*
		 * else if(preferenceKey.equals(SELECT_ENCRYPTPOLICY_KEY)) { String
		 * prefsValue = newValue.toString();
		 * //ToDo..������Ҫ�ع���Ŀǰ�ķ�����Ȼʵ���˻�ȡEntry��Text�����ǱȽ��鷳��Ӧ���н�Ϊ���ķ���
		 * CharSequence[] entries = lpEncryptPolicyChoose.getEntries(); int
		 * newIndex = lpEncryptPolicyChoose.findIndexOfValue(prefsValue);
		 * CharSequence newText = entries[newIndex];
		 * lpEncryptPolicyChoose.setSummary(newText.toString());
		 * //Ϊ������ListPreference �� summary return
		 * this.SaveUserSettings(preferenceKey, PreferenceType.String,
		 * prefsValue); }
		 */
		else if (preferenceKey
				.equals(PreferenceKeys.User_SendByWifi.toString())) {
			String prefsValue = newValue.toString();
			// ToDo..������Ҫ�ع���Ŀǰ�ķ�����Ȼʵ���˻�ȡEntry��Text�����ǱȽ��鷳��Ӧ���н�Ϊ���ķ���
			CharSequence[] entries = lpSendByWifiChoose.getEntries();
			int newIndex = lpSendByWifiChoose.findIndexOfValue(prefsValue);
			CharSequence newText = entries[newIndex];
			lpSendByWifiChoose.setSummary(newText.toString());
			// Ϊ������ListPreference �� summary
			return this.SaveUserSettings(preferenceKey, PreferenceType.String,
					prefsValue);
		}
		/*
		 * else if(preferenceKey.equals(SELECT_SENDBYWIFI_KEY)) { String
		 * prefsValue = newValue.toString(); return
		 * this.SaveUserSettings(preferenceKey, PreferenceType.Boolean,
		 * prefsValue); }
		 */
		else if (preferenceKey.equals(PreferenceKeys.User_AutoSaveInterval
				.toString())) {
			String prefsValue = newValue.toString();
			// ToDo..������Ҫ�ع���Ŀǰ�ķ�����Ȼʵ���˻�ȡEntry��Text�����ǱȽ��鷳��Ӧ���н�Ϊ���ķ���
			CharSequence[] entries = lpAutoSaveIntervalChoose.getEntries();
			int newIndex = lpAutoSaveIntervalChoose
					.findIndexOfValue(prefsValue);
			CharSequence newText = entries[newIndex];
			lpAutoSaveIntervalChoose.setSummary(newText.toString());
			// Ϊ������ListPreference �� summary
			return this.SaveUserSettings(preferenceKey, PreferenceType.String,
					prefsValue);
		} else if (preferenceKey
				.equals(PreferenceKeys.User_AutoSendLocationInterval.toString())) {
			String prefsValue = newValue.toString();
			// ToDo..������Ҫ�ع���Ŀǰ�ķ�����Ȼʵ���˻�ȡEntry��Text�����ǱȽ��鷳��Ӧ���н�Ϊ���ķ���
			CharSequence[] entries = lpAutoSendLocationIntervalChoose
					.getEntries();
			int newIndex = lpAutoSendLocationIntervalChoose
					.findIndexOfValue(prefsValue);
			CharSequence newText = entries[newIndex];
			lpAutoSendLocationIntervalChoose.setSummary(newText.toString());
			// Ϊ������ListPreference �� summary
			if (this.SaveUserSettings(preferenceKey, PreferenceType.String,
					prefsValue)) {
				Intent locationIntent = new Intent(IngleApplication.getInstance(), LocationService.class);
				stopService(locationIntent);
				startService(locationIntent);
				return true;
			} else
				return false;
		} else if (preferenceKey.equals(PreferenceKeys.Sys_LoginServer
				.toString())) {
			String prefsValue = newValue.toString();
			String ipStr = prefsValue.trim();
			return deviceInfoHelper.validateServerAddressForSet(ipStr,
					pLoginServer, sharedPreferencesHelper,
					SystemConfigActivity.this);

		} else if (preferenceKey.equals(PreferenceKeys.User_SavePassword
				.toString())) {
			String prefsValue = newValue.toString();
			CharSequence[] entries = lpSavePasswordChoose.getEntries();
			int newIndex = lpSavePasswordChoose.findIndexOfValue(prefsValue);
			CharSequence newText = entries[newIndex];
			lpSavePasswordChoose.setSummary(newText.toString());
			// Ϊ������ListPreference �� summary
			return this.SaveUserSettings(preferenceKey, PreferenceType.String,
					prefsValue);
		}else if (preferenceKey
				.equals(PreferenceKeys.User_UploadPicCompressRatio.toString())) {
			String prefsValue = (String) newValue;
			CharSequence[] entries = lpCompressRatioChoose.getEntries();
			int newIndex = lpCompressRatioChoose.findIndexOfValue(prefsValue);
			CharSequence newText = entries[newIndex];
			lpCompressRatioChoose.setSummary(newText.toString());
			// Ϊ������ListPreference �� summary
			return this.SaveUserSettings(preferenceKey, PreferenceType.String,
					prefsValue);
		}
		/*
		 * else if(preferenceKey.equals(SET_SENDNUM_KEY)) { String prefsValue =
		 * newValue.toString();
		 * //ToDo..������Ҫ�ع���Ŀǰ�ķ�����Ȼʵ���˻�ȡEntry��Text�����ǱȽ��鷳��Ӧ���н�Ϊ���ķ���
		 * CharSequence[] entries = lpSendNumChoose.getEntries(); int newIndex =
		 * lpSendNumChoose.findIndexOfValue(prefsValue); CharSequence newText =
		 * entries[newIndex]; lpSendNumChoose.setSummary(newText.toString());
		 * //Ϊ������ListPreference �� summary return
		 * this.SaveUserSettings(preferenceKey, PreferenceType.String,
		 * prefsValue); }
		 */
		return true;
	}

	public boolean onPreferenceClick(Preference preference) {

		String preferenceKey = preference.getKey();
		if (preferenceKey.equals(PreferenceKeys.Sys_BaseData.toString())) {
			if (IngleApplication.getNetStatus() != NetStatus.Disable) {
				showLoadingDialog();
				getTopicList();
			} else {
				// Toast.makeText(SystemConfigActivity.this, "���粻���ã�",
				// Toast.LENGTH_SHORT).show();
				ToastHelper.showToast(ToastHelper
						.getStringFromResources(R.string.network_disconnected),
						Toast.LENGTH_SHORT);
			}

		} else if (preferenceKey.equals(PreferenceKeys.Sys_SynManuTemp
				.toString())) {
			if (IngleApplication.getNetStatus() != NetStatus.Disable) {
				showLoadingDialog();
				synManuscriptsTemplate();
			} else {
				// Toast.makeText(SystemConfigActivity.this, "���粻���ã�",
				// Toast.LENGTH_SHORT).show();
				ToastHelper.showToast(ToastHelper
						.getStringFromResources(R.string.network_disconnected),
						Toast.LENGTH_SHORT);
			}
			

		} else if (preferenceKey
				.equals(PreferenceKeys.Sys_VersionUp.toString())) {
			compareVerson();
		} else if (preferenceKey
				.equals(PreferenceKeys.Sys_CleanData.toString())) {
			cleanData();

		} else if (preferenceKey.equals(PreferenceKeys.Sys_LoginServer

		.toString())) {
			String tempString = sharedPreferencesHelper
					.getPreferenceValue(PreferenceKeys.Sys_LoginServer
							.toString());
			pLoginServer.getEditText().setText(tempString);
		}

	
		return true;
	}

	/**
	 * ������
	 */
	private void cleanData() {
		new Thread(new Runnable() {

			public void run() {
				try {
					DBManager db = new DBManager(SystemConfigActivity.this);
					db.cleanData();
					sendMessage(MSG_CLEAN_DATA_FINISH, ToastHelper
							.getStringFromResources(R.string.cleanDataFinish));
				} catch (Exception e) {
					Logger.e(e);
					sendMessage(MSG_CLEAN_DATA_ERROR, ToastHelper
							.getStringFromResources(R.string.cleanDataError));
				}
			}
		}).start();
	}

	/**
	 * �汾У�飬���������Ҫ���ǿ����
	 */
	private void compareVerson() {
		new Thread(new Runnable() {

			public void run() {
				try {
					synchronized (IngleApplication.mLock) {
						while (IngleApplication
								.getNetStatus() == NetStatus.Disable) {
							sendMessage(
									MSG_VALIDATE_VERSION_FINISH,
									ToastHelper
											.getStringFromResources(R.string.network_disconnected));
							IngleApplication.mLock.wait();
						}
					}
				} catch (Exception e) {
					Logger.e(e);
					e.printStackTrace();
				}

				try {
					String[] versionServer = RemoteCaller
							.getAndroidAppVersion();
					String[] versionLocal = DeviceInfoHelper
							.getAppVersionName(SystemConfigActivity.this);
					Float versionLow = Float.parseFloat(versionServer[0]);
					Float versionHigh = Float.parseFloat(versionServer[1]);
					Float versionCurrent = Float.parseFloat(versionLocal[1]);
					apkUrl = versionServer[2];

					if (versionCurrent < versionLow) // ��ǰ�汾������Ͱ汾ʱ���������
					{
						sendMessage(MSG_SHOWNOTICEDIALOG, null);
					} else if (versionCurrent < versionHigh) // ��ǰ�汾������߰汾ʱ��ѡ�����
					{
						sendMessage(MSG_SHOWNOTICEDIALOGOPTION, null);
					} else {
						sendMessage(MSG_VALIDATE_VERSION_FINISH, ToastHelper
								.getStringFromResources(R.string.noNewVersion));
					}

				} catch (XmcException e) {
					if (e.getMessage().equals(XmcException.TIMEOUT)) {
						sendMessage(MSG_NETWORK_TIMEOUT, e.getMessage());
					} else {
						sendMessage(MSG_VALIDATE_VERSION_ERROR, e.getMessage());
					}
				} catch (Exception e) {
					Logger.e(e);
					sendMessage(MSG_VALIDATE_VERSION_ERROR, e.getMessage());
				}
			}
		}).start();
	}

	/**
	 * ǿ����Ի���(ֻ��ȷ����û��ȡ��)
	 */
	public void showNoticeDialog() {
		builder = new AlertDialog.Builder(SystemConfigActivity.this);
		builder.setTitle(R.string.updateSysVersionDialogTitle)
				.setPositiveButton(
						R.string.updateSysVersionDialogPositiveButton,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								sendMessage(MSG_VERSION_UPDATE_START, null);
							}
							// }).setMessage("�汾��ͣ�������").setCancelable(false).create().show();
						})
				.setMessage(R.string.updateSysVersionDialogAbsoluteMsg)
				.setCancelable(false);
		dialogAlert = builder.create();
		dialogAlert.show();
	}

	/**
	 * ��ѡ��Ի���
	 */
	public void showNoticeDialogOption() {
		builder = new AlertDialog.Builder(SystemConfigActivity.this);
		dialogAlert = builder
				.setTitle(R.string.updateSysVersionDialogTitle)
				.setPositiveButton(
						R.string.updateSysVersionDialogPositiveButton,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								sendMessage(MSG_VERSION_UPDATE_START, null);
							}
						})
				.setNegativeButton(R.string.cancel_button,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								sendMessage(MSG_VALIDATE_VERSION_FINISH, null);
							}
							// }).setMessage("���°汾������").setCancelable(false).create().show();
						})
				.setMessage(R.string.updateSysVersionDialogOpotionMsg)
				.setCancelable(false).create();
		dialogAlert.show();
	}

	/**
	 * ���ؽ�ȶԻ���
	 */
	public void showDownloadDialog() {
		builder = new AlertDialog.Builder(SystemConfigActivity.this);
		builder.setTitle(R.string.updateSysVersionDialogTitle);
		final LayoutInflater inflater = LayoutInflater
				.from(SystemConfigActivity.this);
		View v = inflater.inflate(R.layout.progress_system_update, null);
		mProgress = (ProgressBar) v
				.findViewById(R.id.progressBar_SystemUpdate_Splash);
		// builder.setView(v).setCancelable(false).create().show();
		builder.setView(v).setCancelable(false);
		dialogAlert = builder.create();
		dialogAlert.show();
		DownloadAPKThread();
	}

	/**
	 * ����ϵͳ������߳�
	 */
	private void DownloadAPKThread() {
		new Thread(new Runnable() {
			public void run() {
				try {
					URL url = new URL(apkUrl);

					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setConnectTimeout(NORMAL_TIMEOUT);
					conn.setReadTimeout(NORMAL_TIMEOUT);
					conn.connect();

					int length = conn.getContentLength();
					InputStream is = conn.getInputStream();

					File file = new File(savePath);
					if (!file.exists()) {
						file.mkdir();
					}
					String apkFile = saveFileName;
					File ApkFile = new File(apkFile);
					FileOutputStream fos = new FileOutputStream(ApkFile);

					int count = 0;
					byte buf[] = new byte[1024];

					do {
						int numread = is.read(buf);
						count += numread;
						progress = (int) (((float) count / length) * 100);
						// ���½��
						sendMessage(DOWN_UPDATE, null);
						if (numread <= 0) {
							// �������֪ͨ��װ
							sendMessage(DOWN_OVER, null);

							break;
						}
						fos.write(buf, 0, numread);
					} while (!interceptFlag);// ���ȡ���ֹͣ����.

					fos.close();
					is.close();
				} catch (ConnectTimeoutException e) {
					Logger.e(e);
					sendMessage(MSG_NETWORK_TIMEOUT, e.getMessage());
				} catch (InterruptedIOException e) {
					Logger.e(e);
					sendMessage(MSG_NETWORK_TIMEOUT, e.getMessage());
				} catch (Exception e) {
					Logger.e(e);
					sendMessage(MSG_VALIDATE_VERSION_ERROR, e.getMessage());
				}
			}

		}).start();
	}

	/**
	 * ��װapk
	 * 
	 * @param url
	 */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		SystemConfigActivity.this.startActivity(i);
	}

	/**
	 * �ֻ�����¼����
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// KeyEvent.KEYCODE_BACK��?�ز���.
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return true;
	}

	private void showLoadingDialog() {
		String message = ToastHelper.getStringFromResources(R.string.updating);
		dialog = ProgressDialog.show(SystemConfigActivity.this, "", message,
				true);
	}

	/**
	 * Handler class implementation to handle the message
	 * 
	 * @author SongQing
	 * 
	 */
	private class SystemConfigHandler extends Handler {

		// This method is used to handle received messages
		public void handleMessage(Message msg) {
			// switch to identify the message by its code
			switch (msg.what) {
			/*
			 * case MSG_GETTOPICLIST_START: if(msg.obj==null)
			 * uploadLayout.setVisibility(View.VISIBLE); else {
			 * fileNameTextView.setText(msg.obj.toString()); } break; case
			 * MSG_GETTOPICLIST_PRO: proTextView.setText(msg.obj.toString() +
			 * "%");
			 * progressBar.setProgress(Integer.valueOf(msg.obj.toString()));
			 * break;
			 */
			case MSG_GETTOPICLIST_END:
				if (msg.obj != null) {
					// Toast.makeText(SystemConfigActivity.this,
					// msg.obj.toString(), Toast.LENGTH_SHORT).show();
					ToastHelper.showToast(msg.obj.toString(),
							Toast.LENGTH_SHORT);
				} else {
					// Toast.makeText(SystemConfigActivity.this, "�޸���",
					// Toast.LENGTH_SHORT).show();
					ToastHelper.showToast(ToastHelper
							.getStringFromResources(R.string.noMoreUpdate),
							Toast.LENGTH_SHORT);
				}

				break;
			case MSG_GETTOPICLIST_ERROR:
				if (msg.obj != null) {
					// Toast.makeText(SystemConfigActivity.this,
					// msg.obj.toString(), Toast.LENGTH_SHORT).show();
					ToastHelper.showToast(msg.obj.toString(),
							Toast.LENGTH_SHORT);
				}

				break;
			case MSG_SHOWNOTICEDIALOG:
				showNoticeDialog();
				break;
			case MSG_SHOWNOTICEDIALOGOPTION:
				showNoticeDialogOption();
				break;
			case MSG_VERSION_UPDATE_START:
				showDownloadDialog();
				break;
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				break;
			case DOWN_OVER:
				installApk();
				SystemConfigActivity.this.finish();
				break;
			case MSG_VALIDATE_VERSION_FINISH:
				if (msg.obj != null) {
					// Toast.makeText(SystemConfigActivity.this,msg.obj.toString(),
					// Toast.LENGTH_SHORT).show();
					ToastHelper.showToast(msg.obj.toString(),
							Toast.LENGTH_SHORT);
				}
				break;
			case MSG_VALIDATE_VERSION_ERROR:
				if (msg.obj != null) {
					// Toast.makeText(SystemConfigActivity.this,msg.obj.toString(),
					// Toast.LENGTH_SHORT).show();
					ToastHelper.showToast(msg.obj.toString(),
							Toast.LENGTH_SHORT);
				}
				break;
			case MSG_CLEAN_DATA_FINISH:
				if (msg.obj != null) {
					ToastHelper.showToast(msg.obj.toString(),
							Toast.LENGTH_SHORT);
				}
				break;
			case MSG_CLEAN_DATA_ERROR:
				if (msg.obj != null) {
					ToastHelper.showToast(msg.obj.toString(),
							Toast.LENGTH_SHORT);
				}
				break;
			case MSG_SYN_TEMP_FINISH:
				if (msg.obj != null) {
					ToastHelper.showToast(msg.obj.toString(),
							Toast.LENGTH_SHORT);
				}
				break;
			case MSG_SYN_TEMP_ERROR:
				if (msg.obj != null) {
					ToastHelper.showToast(msg.obj.toString(),
							Toast.LENGTH_SHORT);
				}
				break;
			case MSG_NETWORK_TIMEOUT:
				// Toast.makeText(SystemConfigActivity.this,"��������ʱ,������������!",
				// Toast.LENGTH_SHORT).show();
				ToastHelper.showToast(ToastHelper
						.getStringFromResources(R.string.requestNetTimeout),
						Toast.LENGTH_SHORT);
				if (dialogAlert != null) {
					dialogAlert.dismiss();
				}
				break;
			default:
				break;
			}

			if (dialog != null) {
				dialog.dismiss();
			}
		}
	}

	/**
	 * ͬ���������Ϣ
	 */
	private void getTopicList() {

		new Thread(new Runnable() {

			public void run() {
				BaseDataService service = new BaseDataService(
						SystemConfigActivity.this);
				String topicList = service.GetCurrentBDFilesNameString();

				// ---------------------------------------------------For
				// Test------------------------------------------------------------------
				// String topicList =
				// "topiclist.cnml-Department-85.xml,topiclist.cnml-Language-1.xml,topiclist.cnml-MediaType-1.xml,"
				// +
				// "topiclist.cnml-Priority-1.xml,topiclist.cnml-WorldLocationCategory-1.xml,topiclist.cnml-XH_GeographyCategory-5.xml,"
				// +
				// "topiclist.cnml-XH_InternalInternational-4.xml,topiclist.cnml-XH_NewsCategory-7.xml";
				// ---------------------------------------------------For
				// Test------------------------------------------------------------------

				try {
					synchronized (IngleApplication.mLock) {
						while (IngleApplication
								.getNetStatus() == NetStatus.Disable) {
							try {
								IngleApplication.mLock
										.wait();
							} catch (Exception e) {
								Logger.e(e);
								e.printStackTrace();
							}
						}
					}

					String result = RemoteCaller.GetTopicList(topicList);

					if (StringUtils.isNotBlank(result)) {
						// ����
						if ("601".equals(result)) { // TODO ��ʻ�
							sendMessage(
									MSG_GETTOPICLIST_END,
									ToastHelper
											.getStringFromResources(R.string.requestNetError));
						} else {
							// sendMessage(MSG_GETTOPICLIST_START, null);

							String[] fileNames = result.trim().split(",");

							for (int i = 0; i < fileNames.length; i++) {

								// sendMessage(MSG_GETTOPICLIST_START,
								// fileNames[i]);
								if (DownloadBaseData(fileNames[i])) {// ������سɹ����������ݿ�
									updateBasedataInDB(fileNames[i]);
								}
							}
							sendMessage(
									MSG_GETTOPICLIST_END,
									ToastHelper
											.getStringFromResources(R.string.updateFinished));
						}

					} else {
						sendMessage(MSG_GETTOPICLIST_END, null);
					}

				} catch (XmcException e) {
					if (e.getMessage().equals(XmcException.TIMEOUT)) {
						sendMessage(MSG_NETWORK_TIMEOUT, e.getMessage());
					} else {
						sendMessage(MSG_VALIDATE_VERSION_ERROR, e.getMessage());
					}
				} catch (Exception e) {
					Logger.e(e);
					sendMessage(MSG_GETTOPICLIST_ERROR, e.getMessage());
				}
			}
		}).start();
	}

	/**
	 * @Description ʹ��GET������ȡ�����XML�ļ�
	 * @param topicList
	 *            XML�ļ����ļ���
	 */

	private boolean DownloadBaseData(String fileName) {
		// Eg :
		// http://202.84.17.51/ms/topiclist/topiclist.cnml-XH_NewsCategory-7.xml

		// ��ȡ�ļ�����·��
		String path = Configuration.getTopicDownloadUrl() + fileName;
		String downPath = StandardizationDataHelper.GetBaseDataFileStorePath();
		boolean createDir = new File(downPath).mkdirs();
		if (createDir) {
			Log.i(IngleApplication.TAG, "��������·��");
		}

		try {
			Log.i(IngleApplication.TAG, "��ʼ����" + fileName
					+ "....");
			URL url = new URL(path);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(NORMAL_TIMEOUT);
			connection.setReadTimeout(NORMAL_TIMEOUT);
			connection.setDoOutput(true);
			connection.connect();

			FileOutputStream fStream = new FileOutputStream(new File(downPath,
					Format.replaceBlank(fileName)));
			InputStream inputStream = connection.getInputStream();
			int fileLength = connection.getContentLength();
			int downloadFileSize = 0;

			byte[] buffer = new byte[1024];
			int length = 0;
			while ((length = inputStream.read(buffer)) > 0) {
				fStream.write(buffer, 0, length);
				downloadFileSize += length;
				// sendMessage(MSG_GETTOPICLIST_PRO, downloadFileSize * 100/
				// fileLength);
			}
			fStream.close();

			return true;

		} catch (ConnectTimeoutException e) {
			Logger.e(e);
			sendMessage(MSG_NETWORK_TIMEOUT, e.getMessage());
			return false;
		} catch (InterruptedIOException e) {
			Logger.e(e);
			sendMessage(MSG_NETWORK_TIMEOUT, e.getMessage());
			return false;
		} catch (Exception e) {
			Logger.e(e);
			e.printStackTrace();
			sendMessage(MSG_GETTOPICLIST_ERROR, e.getMessage());
			return false;
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
	 * ������ݿ��еĻ����
	 * 
	 * @throws IOException
	 */
	private void updateBasedataInDB(String fileName) throws IOException {

		InputStream is = null;
		String downPath = StandardizationDataHelper.GetBaseDataFileStorePath();
		File dataFile = null;

		try {

			if (fileName.contains(BaseDataFileType.Department.toString())) {

				dataFile = new File(downPath + "/" + fileName);
				if (!dataFile.exists()) {
					return;
				}
				is = new FileInputStream(dataFile);
				List<ComeFromAddress> comeFromAddressList = mComeFromAddressService
						.GetComeFromAddressFromXMLFile(is);

				if (mComeFromAddressService.deleteAllComeFromAddress()) {
					if (mComeFromAddressService
							.multiInsert(comeFromAddressList)) {
						mBaseDataService.SetCurrentBDFileName(
								BaseDataFileType.Department, fileName);
					}
				}
			} else if (fileName.contains(BaseDataFileType.Language.toString())) {
				dataFile = new File(downPath + "/" + fileName);
				if (!dataFile.exists()) {
					return;
				}
				is = new FileInputStream(dataFile);
				List<Language> languageList = mLanguageService
						.GetLanguageFromXMLFile(is);

				if (mLanguageService.deleteAllLanguage()) {
					if (mLanguageService.multiInsert(languageList)) {
						mBaseDataService.SetCurrentBDFileName(
								BaseDataFileType.Language, fileName);
					}
				}
			} else if (fileName.contains(BaseDataFileType.Priority.toString())) {
				dataFile = new File(downPath + "/" + fileName);
				if (!dataFile.exists()) {
					return;
				}
				is = new FileInputStream(dataFile);
				List<NewsPriority> newsPriorityList = mNewsPriorityService
						.GetNewsPriorityFromXMLFile(is);

				if (mNewsPriorityService.deleteAllNewsPriority()) {
					if (mNewsPriorityService.multiInsert(newsPriorityList)) {
						mBaseDataService.SetCurrentBDFileName(
								BaseDataFileType.Priority, fileName);
					}
				}
			} else if (fileName.contains(BaseDataFileType.XH_GeographyCategory
					.toString())) {
				dataFile = new File(downPath + "/" + fileName);
				if (!dataFile.exists()) {
					return;
				}
				is = new FileInputStream(dataFile);
				List<Region> regionList = mRegionService
						.GetRegionFromXMLFile(is);

				if (mRegionService.deleteAllRegion()) {
					if (mRegionService.multiInsert(regionList)) {
						mBaseDataService
								.SetCurrentBDFileName(
										BaseDataFileType.XH_GeographyCategory,
										fileName);
					}
				}
			} else if (fileName.contains(BaseDataFileType.WorldLocationCategory
					.toString())) {
				dataFile = new File(downPath + "/" + fileName);
				if (!dataFile.exists()) {
					return;
				}
				is = new FileInputStream(dataFile);
				List<Place> placeList = mPlaceServices.GetPlaceFromXMLFile(is);

				if (mPlaceServices.deleteAllPlace()) {
					if (mPlaceServices.multiInsert(placeList)) {
						mBaseDataService.SetCurrentBDFileName(
								BaseDataFileType.WorldLocationCategory,
								fileName);
					}
				}
			} else if (fileName
					.contains(BaseDataFileType.XH_InternalInternational
							.toString())) {
				dataFile = new File(downPath + "/" + fileName);
				if (!dataFile.exists()) {
					return;
				}
				is = new FileInputStream(dataFile);
				List<ProvideType> provideTypeList = mProvideTypeService
						.GetProvideTypeFromXMLFile(is);

				if (mProvideTypeService.deleteAllProvideType()) {
					if (mProvideTypeService.multiInsert(provideTypeList)) {
						mBaseDataService.SetCurrentBDFileName(
								BaseDataFileType.XH_InternalInternational,
								fileName);
					}
				}
			} else if (fileName.contains(BaseDataFileType.XH_NewsCategory
					.toString())) {
				dataFile = new File(downPath + "/" + fileName);
				if (!dataFile.exists()) {
					return;
				}
				is = new FileInputStream(dataFile);
				List<NewsCategory> newsCategorieList = mNewsCategoryServices
						.GetNewsCategoryDataFromXMLFile(is);

				if (mNewsCategoryServices.deteteAllNewsCategory()) {
					if (mNewsCategoryServices.multiInsert(newsCategorieList)) {
						mBaseDataService.SetCurrentBDFileName(
								BaseDataFileType.XH_NewsCategory, fileName);
					}
				}
			} else if (fileName.contains(BaseDataFileType.SendAddress
					.toString())) {
				dataFile = new File(downPath + "/" + fileName);
				if (!dataFile.exists()) {
					return;
				}
				is = new FileInputStream(dataFile);
				List<SendToAddress> sendToAddresseList = mSendToAddressService
						.GetSendToAddressDataFromXMLFile(is);

				if (mSendToAddressService.deleteAllSendToAddress()) {
					if (mSendToAddressService.multiInsert(sendToAddresseList)) {
						mBaseDataService.SetCurrentBDFileName(
								BaseDataFileType.SendAddress, fileName);
					}
				}
			} else {
			}
		} catch (Exception e) {
			Logger.e(e);
		}
	}

	private void synManuscriptsTemplate() {
		new Thread(new Runnable() {
			public void run() {

				List<ManuscriptTemplate> manuscriptTemplates = null;
				ManuscriptTemplate mtTemplate = null;
				try {
					manuscriptTemplates = RemoteCaller.GetTemplate(
							IngleApplication
									.getSessionId(),
							IngleApplication.getInstance()
									.getCurrentUser());

					for (TemplateType tt : TemplateType.values()) { // ����Ϊ���û�����ĸ���ϵͳĬ�Ͽ�Ѷģ���һ�����û��Զ���ģ��
						mtTemplate = new ManuscriptTemplate(
								IngleApplication
										.getInstance().getCurrentUser(), tt
										.toString(), tt.getValue());
						mtTemplate.setDefaulttitle(tt.getValue()); // ��Ѷģ���Ĭ�ϱ���Ϊ��Ѷ���
						if (mtServices.isNameExsit(mtTemplate.getName(),
								IngleApplication
										.getInstance().getCurrentUser()) == false)
							mtServices.addManuscriptTemplate(mtTemplate);
					}

					if (manuscriptTemplates == null
							|| manuscriptTemplates.size() == 0) {
						return;
					}
					for (ManuscriptTemplate mt : manuscriptTemplates) { // ��Ӵӷ�������ȡ���ĸ�ǩģ��
						mt.setLoginname(IngleApplication
								.getInstance().getCurrentUser());
						if (IngleApplication.getInstance().currentUserInfo
								.getUserattribute().getRightTransferNews()) { // �����û��ǹ�Ա����ʱ��ͬ����ǩʱ��Ҫ�����û���ǩģ���еķ����ַ�Ƿ��������·����ַȨ�ޣ�������Ҫ���бȽϣ�ȥ�������������
							String[] addressArrayStrings = validateSendToAddress(
									mt.getAddress(), mt.getAddressID());
							mt.setAddress(addressArrayStrings[0]);
							mt.setAddressID(addressArrayStrings[1]);
						}
						if (mtServices.isNameExsit(mt.getName(),
								IngleApplication
										.getInstance().getCurrentUser()) == false)
							mtServices.addManuscriptTemplate(mt);
					}
					sendMessage(MSG_SYN_TEMP_FINISH, ToastHelper
							.getStringFromResources(R.string.synTempFinish));

				} catch (XmcException e) {
					sendMessage(MSG_SYN_TEMP_ERROR, ToastHelper
							.getStringFromResources(R.string.synTempError));
					Logger.e(e);
					e.printStackTrace();
				}
			}
		}).start();
	}

	private String[] validateSendToAddress(String sendToaddressName,
			String sendToaddressCode) {
		String[] addressArrayStrings = new String[] { "", "" };
		if (sendToaddressName.length() != 0) {
			String[] nameListStrings = sendToaddressName.split(",");
			String[] codeListStrings = sendToaddressCode.split(",");

			ArrayList<KeyValueData> transferAddressList = IngleApplication
					.getInstance().currentUserInfo.getUserattribute()
					.getTransferAddressList();

			if (transferAddressList != null && transferAddressList.size() != 0) {
				boolean mark = false;
				for (int i = 0; i < nameListStrings.length; i++) {
					for (KeyValueData kv : transferAddressList) {
						if (nameListStrings[i].equals(kv.getKey())
								&& codeListStrings[i].equals(kv.getValue())) {
							mark = true;
							break;
						}
					}

					if (mark) {
						addressArrayStrings[0] = nameListStrings[i] + ",";
						addressArrayStrings[1] = codeListStrings[i] + ",";
					}
				}

				if (addressArrayStrings[0].length() > 0
						&& addressArrayStrings[1].length() > 0) {
					addressArrayStrings[0] = addressArrayStrings[0].substring(
							0, addressArrayStrings[0].length() - 1);
					addressArrayStrings[1] = addressArrayStrings[1].substring(
							0, addressArrayStrings[1].length() - 1);
				}
			}
		}

		return addressArrayStrings;
	}

}
