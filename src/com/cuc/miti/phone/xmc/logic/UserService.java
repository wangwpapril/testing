package com.cuc.miti.phone.xmc.logic;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.os.Handler;
import android.os.Message;
import android.sax.Element;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.dao.UserDao;
import com.cuc.miti.phone.xmc.domain.Enums.AccessoryType;
import com.cuc.miti.phone.xmc.domain.Enums.InterfaceType;
import com.cuc.miti.phone.xmc.domain.Enums.LoginStatus;
import com.cuc.miti.phone.xmc.domain.Enums.NetStatus;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceKeys;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceType;
import com.cuc.miti.phone.xmc.domain.Enums.TemplateType;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.domain.ManuscriptTemplate;
import com.cuc.miti.phone.xmc.domain.PositionInfo;
import com.cuc.miti.phone.xmc.domain.SendToAddress;
import com.cuc.miti.phone.xmc.domain.UploadTask;
import com.cuc.miti.phone.xmc.domain.User;
import com.cuc.miti.phone.xmc.domain.UserAttribute;
import com.cuc.miti.phone.xmc.domain.UserDemo;
import com.cuc.miti.phone.xmc.domain.UserInfoDemo;
import com.cuc.miti.phone.xmc.domain.XmcException;
import com.cuc.miti.phone.xmc.http.Configuration;
import com.cuc.miti.phone.xmc.http.DoRemoteResult;
import com.cuc.miti.phone.xmc.http.HttpClient;
import com.cuc.miti.phone.xmc.http.PostParameter;
import com.cuc.miti.phone.xmc.http.RemoteCaller;
import com.cuc.miti.phone.xmc.models.AssistanceProvider;
import com.cuc.miti.phone.xmc.models.Company;
import com.cuc.miti.phone.xmc.models.Destination;
import com.cuc.miti.phone.xmc.models.User1;
import com.cuc.miti.phone.xmc.store.beans.UserTable1;
import com.cuc.miti.phone.xmc.ui.AttachmentUploadActivity;
import com.cuc.miti.phone.xmc.ui.LoginActivity;
import com.cuc.miti.phone.xmc.ui.MainActivity;
import com.cuc.miti.phone.xmc.ui.SplashScreenActivity;
import com.cuc.miti.phone.xmc.ui.TestEntranceActivity;
import com.cuc.miti.phone.xmc.ui.TripsListActivity;
import com.cuc.miti.phone.xmc.ui.WelcomeActivity;
import com.cuc.miti.phone.xmc.utils.DeviceInfoHelper;
import com.cuc.miti.phone.xmc.utils.Encrypt;
import com.cuc.miti.phone.xmc.utils.Format;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.SharedPreferencesHelper;
import com.cuc.miti.phone.xmc.utils.StandardizationDataHelper;
import com.cuc.miti.phone.xmc.utils.ToastHelper;
import com.cuc.miti.phone.xmc.utils.XMLDataHandle;
import com.cuc.miti.phone.xmc.R;
import com.google.mitijson.Gson;

public class UserService {

	private UserDao userDao;
	private ManuscriptTemplateService manuscriptTemplateService;
	private UploadTaskService uploadTaskService = null;
	private SharedPreferencesHelper sharedPreferencesHelper;
	private Context context;
	private SharedPreferencesHelper spHelper;
	private String CURRENT_USER = "currentuser";
	private String USER = "userAttribute";
	private static final String TAG = "UserService";
	public static final int MSG_LOGIN = 1;
	public static final int FAIL_LOGIN = 2;
	public static final int MSG_GETAPPSERVER_ERROR = 3;
	public static final int MSG_GETAPPSERVER_FINISH = 4;
	public static final int MSG_GETUSERINFO_FINISH = 5;
	public static final int MSG_GETUSERINFO_ERROR = 6;
	public static final int MSG_LOGIN_NETWORK_NONE = 0;

	private ProgressDialog dialog = null;

	public UserService(Context context) {
		this.context = context;
		this.userDao = new UserDao(context);
		this.manuscriptTemplateService = new ManuscriptTemplateService(context);
		this.uploadTaskService = new UploadTaskService(context);
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			Intent intent = null;
			User user = null;
			switch (msg.what) {
			case MSG_LOGIN:
				// ˵���˴ε�¼������ͨ��¼�������û�����״̬�����緢��ı䣬ϵͳ�Զ���¼�ģ�����Ҫҳ����ת
				if (IngleApplication.getLoginStatus()
						.equals(LoginStatus.offline)) {
					IngleApplication.getInstance()
							.initialzeUpdateData(); // ����ȫ�ּ�ʱ������ά����������ĻỰ״̬�Ͷ�ʱ�����û���Ϣ
					IngleApplication
							.setLoginStatus(LoginStatus.online); // ��ʶΪ�����½
					uploadTaskService = new UploadTaskService(context);
					uploadTaskService.getUploadTaskForStart();
				} else {
					Log.i(IngleApplication.TAG, "�����¼�ɹ�");

					IngleApplication.getInstance()
							.initialzeUpdateData(); // ����ȫ�ּ�ʱ������ά����������ĻỰ״̬�Ͷ�ʱ�����û���Ϣ
					IngleApplication
							.setLoginStatus(LoginStatus.online); // ��ʶΪ�����½
					uploadTaskService = new UploadTaskService(context);
					uploadTaskService.loadUploadTaskIntoWaitingQueue(); // �����ϴ�����
					intent = new Intent(context, MainActivity.class);
					context.startActivity(intent);
					((Activity) context).finish();
				}
				break;
			case FAIL_LOGIN:
				// Toast.makeText(context, msg.obj.toString(),
				// Toast.LENGTH_SHORT).show();
				// Modify by SongQing.20120807
				// Toast��ΪAlertDialog�Դﵽ�û��޸�����:��¼ҳ�����ʾ��Ҫ�����Ի���ȴ��û�ȷ��
				showAlertDialog("��¼��ʾ", msg.obj.toString());
				break;
			case MSG_GETUSERINFO_FINISH: // ��ȡ�û���Ϣ�ɹ�
				user = (User) msg.obj;
				userInfoUpdate(user);
				storeCurrentUserToPreference(user.getUsername());
				break;
			case MSG_GETUSERINFO_ERROR: // ��ȡ�û���Ϣʧ��
				break;
			case MSG_LOGIN_NETWORK_NONE: // �����¼������ֳ�ʱ����Ϊ���ߵ�¼
				user = (User) msg.obj;
				userLoginOffline(user.getUsername(), user.getPassword());
				break;
			}
			if (dialog != null) {
				dialog.dismiss();
			}
			super.handleMessage(msg);

		}
	};

	/**
	 * ��½�ɹ�ʱ���»�������û���Ϣ��������ݿ�
	 * 
	 * @param userInfo
	 */
	private void userInfoUpdate(User userInfo) {
		if (userInfo == null) {
			return;
		}

		// ����ɹ���½���û�������빩�´ε�½ҳ���ס�˺ź�����
		spHelper.SaveCommonPreferenceSettings(
				PreferenceKeys.Sys_CurrentUser.toString(),
				PreferenceType.String, userInfo.getUsername());
		spHelper.SaveCommonPreferenceSettings(
				PreferenceKeys.Sys_CurrentPassword.toString(),
				PreferenceType.String, userInfo.getPassword());

		if (userDao.existUser(userInfo.getUsername())) {
			userDao.updateUser(userInfo);
		} else { // ��һ�ε�½
			userDao.addUser(userInfo);
			// Ϊ�û������ĸ��յĿ�Ѷģ���ͬ���������ϵ��û�ģ��

			new Thread(new Runnable() {
				public void run() {
					initializeTemplateForNewUser();
				}
			}).start();
		}
	}

	/**
	 * �����¼����֤��Ҫ����Http���еĽӿڵ��÷������ɹ���½���¼����±�����ݿ��û��������
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public void userLoginOnline(String username, String password) {
		final User user = new User();
		if (username != null && password != null) {
			if (IngleApplication.getLoginStatus().equals(
					LoginStatus.none)) {
				showLoadingDialog(); // TODO ��ʻ�
			}

			user.setUsername(username);
			// user.setPassword(Encrypt.toMD5(password));
			user.setPassword(password);


			UserInfoDemo demo = new UserInfoDemo();
			UserDemo User = new UserDemo();
			User.setEmail(username);
			User.setPsw(password);
			demo.setUser(User);
			Gson gson = new Gson();
			final String parasString = gson.toJson(demo);
			
			
			new Thread(new Runnable() {
				String[] ss;

				public void run() {
					
			///////////					
									String[] returnValueString = {"",""};
									
									try {
//										if(lct!=null){
//										PostDomain postDomain = new PostDomain(editTextUserName.getText().toString().trim(),
				//								editTextPassword.getText().toString().trim());
//										PostDomain postDomain = new PostDomain("wwang@peakcontact.com","12345678");

										UserInfoDemo demo = new UserInfoDemo();
										UserDemo user = new UserDemo();
										user.setEmail("wwang@peakcontact.com");
										user.setPsw("12345678");
										demo.setUser(user);
										Gson gson = new Gson();
										String parasString = gson.toJson(demo);

										HttpClient httpClient = new HttpClient();
										PostParameter[] postParams = null;
									
//										String JSONResult = null;
										String JSONResult = httpClient.post(parasString, "https://api.intrepid247.com/v1/users/login", 6000);	
										
							            JSONObject jsonObj = new JSONObject(JSONResult);	
							            JSONObject userObj = jsonObj.getJSONObject("user");
							            User1 user1 = new User1(userObj);
							            String userid = user1.id;
//							            user1.id = "1160591404";
							            UserTable1.getInstance().saveUser(user1);
							            User1 ww = null;
							            ww = UserTable1.getInstance().getUser1(userid);
							            
//							            showPreviewDialog(user1);

										JSONResult = httpClient.doGet(postParams, 
												"https://api.intrepid247.com/v1/destinations?short_list=true&token=ce6f284088d8c6bf88802f51f6d49776", 6000);
//										"https://api.intrepid247.com/v1/destinations?token=ce6f284088d8c6bf88802f51f6d49776", 12000);

										JSONObject des = new JSONObject(JSONResult);
//										JSONObject dd = des.getJSONObject("destinations");
										JSONArray array = des.getJSONArray("destinations");
										int len = array.length();
										List<Destination> desList = new ArrayList<Destination>(len);
										for (int i =0;i < len; i++){
											desList.add(new Destination(array.getJSONObject(i)));
										}
										
										Intent i = new Intent();
										i.setClass(context, TripsListActivity.class);
										i.putExtra("destinations", (Serializable )desList);
										i.putExtra("user", user1);
										context.startActivity(i);
						
									   ((Activity) context).finish();
									} catch (Exception e) {
//										exceptionTypeJudge(e);
									}


								 ////////////					


/*					HashMap<String, String> hsHashMap = Configuration
							.getHashMapAppAddress();
					sharedPreferencesHelper = new SharedPreferencesHelper(
							context);
					if (hsHashMap == null || hsHashMap.size() == 0) { // ���������Ҫ��Ϊ�˽���û��ѻ�����½����¼ҳ���ִ��������ӣ������½����Ҫ���»�ȡ�Ƽ�������
						GetAppServer();
					}
					hsHashMap = Configuration.getHashMapAppAddress();
					if (hsHashMap != null && hsHashMap.size() > 0) {
						Iterator<Entry<String, String>> iterator = hsHashMap
								.entrySet().iterator();
						while (iterator.hasNext()) {
							Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator
									.next();
							// TODO �������÷��ʷ���������ʽ����ʱ�޸�
							sharedPreferencesHelper
									.SaveCommonPreferenceSettings(
											PreferenceKeys.Sys_CurrentServer
													.toString(),
											PreferenceType.String, entry
													.getValue().toString());
							StandardizationDataHelper.setUrlForTest(entry
									.getValue().toString());
							if (login(user)) {
								break;
							}
						}
					}// URL��ַ���ò���ȷ�����ܵ�¼
						// else{
					// login(user);
					// }*/
				}
			}).start();
		}
	}
	
	private void showPreviewDialog(User1 user1)
	{
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("Preview User..");
		View view = LayoutInflater.from(context).inflate(R.layout.previewresume, null);
		TextView prevText = (TextView)view.findViewById(R.id.previewResumeText);
		
		String tmptext = "";
		
			tmptext += "User id: " + user1.id + 
					"\n" + "User Name: "+ user1.userName +
					"\n" + "Email: " + user1.email;
			tmptext += "\n\n\n";
		

		
		prevText.setText(tmptext);
		
		builder.setView(view);
		
		builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
			}
		});
		
		Dialog dialog = builder.create();
		dialog.show();
	}

	/**
	 * ���ߵ�¼,��һ�α��������¼
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public void userLoginOffline(String username, final String password) {
		if (username != null && password != null) {
			User user = null;
			user = userDao.getUser(username);

			if (user == null) { // ˵����δ�ɹ������½��ϵͳ����ʾ�û���һ�ε�½��������
				// TODO ��ʻ�
				Log.i(IngleApplication.TAG,
						"��¼ʧ��_��һ�ε�¼�������� ");

				// Toast.makeText(context, "��һ�ε�¼����������",
				// Toast.LENGTH_SHORT).show();
				// Modify by SongQing.20120807
				// Toast��ΪAlertDialog�Դﵽ�û��޸�����:��¼ҳ�����ʾ��Ҫ�����Ի���ȴ��û�ȷ��
				showAlertDialog(
						ToastHelper.getStringFromResources(R.string.loginAlert),
						ToastHelper
								.getStringFromResources(R.string.firstLoginMustHasNet));

			} else {
				if (user != null && user.getPassword().equals(password)) {
					IngleApplication.getInstance().currentUserInfo = user;
					spHelper = new SharedPreferencesHelper(context);
					String s = spHelper.GetUserPreferenceValue(USER);
					// �ӱ���xml�ļ��л�ȡuserAttribute������ֵ
					try {
						user.setUserattribute(XMLDataHandle
								.ParserUserInfo(new ByteArrayInputStream(s
										.getBytes())));
						IngleApplication.getInstance().currentUserInfo = user;
					} catch (Exception e) {
						Logger.e(e);
					}

					Log.i(IngleApplication.TAG, "��¼�ɹ�");
					IngleApplication
							.setLoginStatus(LoginStatus.offline); // ��ʶΪ���ߵ�½
					uploadTaskService = new UploadTaskService(context);
					uploadTaskService.loadUploadTaskIntoWaitingQueue(); // �����ϴ�����
					Intent intent = new Intent(context, WelcomeActivity.class);
					context.startActivity(intent);
					// Toast.makeText(context, "���ߵ�¼�ɹ�",
					// Toast.LENGTH_SHORT).show();
					ToastHelper
							.showToast(
									ToastHelper
											.getStringFromResources(R.string.offlineLoginSucceed),
									Toast.LENGTH_SHORT);

					((Activity) context).finish();
				} else { // �û�������У�鲻��ȷ
					Log.i(IngleApplication.TAG,
							"��¼ʧ��_�û�������У�����");

					// Intent intent = new
					// Intent(context,TestEntranceActivity.class);
					// context.startActivity(intent);
					// Toast.makeText(context, "�û����������",
					// Toast.LENGTH_SHORT).show();
					// Modify by SongQing.20120807
					// Toast��ΪAlertDialog�Դﵽ�û��޸�����:��¼ҳ�����ʾ��Ҫ�����Ի���ȴ��û�ȷ��
					showAlertDialog(
							ToastHelper
									.getStringFromResources(R.string.loginAlert),
							ToastHelper
									.getStringFromResources(R.string.usernameOrPassError));
				}
			}
		}
	}

	/**
	 * ͨ���½�ɹ��󷵻ص�SessionID��ȡ�û���Ϣ
	 * 
	 * @param sessionId
	 */
	public void getUserInfo(final String sessionId, final User user) {
		new Thread(new Runnable() {
			public void run() {

				try {
					String[] ss = null;
					ss = DoRemoteResult.doResult(RemoteCaller.GetUserInfo(
							sessionId, user.getUsername()));
					if ("0".equals(ss[0])) {
						user.setUserattribute(XMLDataHandle
								.ParserUserInfo(new ByteArrayInputStream(ss[1]
										.getBytes())));
						IngleApplication.getInstance().currentUserInfo = user;
						spHelper = new SharedPreferencesHelper(context);
						spHelper.SaveUserPreferenceSettings(USER,
								PreferenceType.String, ss[1]);

						sendMessage(MSG_GETUSERINFO_FINISH, user);
						if (IngleApplication
								.getLoginStatus().equals(LoginStatus.none))
							sendMessage(MSG_LOGIN, user);
					}
				} catch (Exception e) {
					Logger.e(e);
					sendMessage(MSG_GETUSERINFO_ERROR, ToastHelper
							.getStringFromResources(R.string.failedToGetUser));
				}
			}
		}).start();
	}

	/**
	 * �ɹ���½����Ҫ�������浱ǰ�û���ϵͳ����ƫ�������ļ�
	 * 
	 * @param username
	 * @return
	 */
	public boolean storeCurrentUserToPreference(String username) {
		if (username != null && username != "") {
			spHelper = new SharedPreferencesHelper(context);

			return spHelper.SaveCommonPreferenceSettings(CURRENT_USER,
					PreferenceType.String, username);
		}
		return false;
	}

	/**
	 * ��ȡϵͳ����ƫ���б���ĵ�ǰ�û���
	 * 
	 * @return
	 */
	public String getCurrentUserFromPreference() {
		spHelper = new SharedPreferencesHelper(context);
		return spHelper.getPreferenceValue(CURRENT_USER);
	}

	public List<User> getLocalUserList() {
		return null;
	}

	private void sendMessage(int what, Object obj) {
		Message msg = mHandler.obtainMessage();
		msg.what = what;
		msg.obj = obj;
		mHandler.sendMessage(msg);
	}

	/**
	 * ��ȡ����ѡ��Ļ���ݶ����б�
	 * 
	 * @return
	 */
	public List<KeyValueData> getBaseDataForBind() {
		// TODO ��Ҫ��ȡ��ǰ�ֻ��ϵͳ���������ò�ͬ�����ֲ�ѯ��Ŀǰ��д�������ġ�
		User user = IngleApplication.getInstance().currentUserInfo;
		List<KeyValueData> keyValueDataList = new ArrayList<KeyValueData>();
		KeyValueData kvDataInfo = null;
		if (user != null && user.getUserattribute() != null) {

			kvDataInfo = new KeyValueData();
			kvDataInfo.setKey(user.getUserattribute().getUserNameC());
			kvDataInfo.setValue(user.getUserattribute().getUserNameC());

			keyValueDataList.add(kvDataInfo);

			kvDataInfo = new KeyValueData();
			kvDataInfo.setKey(user.getUserattribute().getUserNameE());
			kvDataInfo.setValue(user.getUserattribute().getUserNameE());

			keyValueDataList.add(kvDataInfo);
		} else {
			kvDataInfo = new KeyValueData();
			kvDataInfo.setKey(user.getUsername());
			kvDataInfo.setValue(user.getUsername());
			keyValueDataList.add(kvDataInfo);
		}

		return keyValueDataList;
	}

	private void showLoadingDialog() {
		String message = ToastHelper.getStringFromResources(R.string.logining);
		dialog = ProgressDialog.show(context, "", message, true);
	}

	/**
	 * ��½����
	 */
	private boolean login(User user) {
		String[] ss;
		try {
			String clientVersion;
			uploadTaskService = new UploadTaskService(context,
					user.getUsername());
			List<UploadTask> tasks = uploadTaskService
					.getListForStandToManuscript();
			if (tasks != null && tasks.size() > 0)
				clientVersion = "-1";
			else {
				String[] versionStrings = DeviceInfoHelper
						.getAppVersionName(IngleApplication
								.getInstance());
				if (versionStrings != null) {
					clientVersion = versionStrings[1];
				} else {
					clientVersion = "1.101";
				}
			}
			// For IMEI code
			String deviceIMEI = IngleApplication
					.getInstance().getDeviceInfoHelper().translateDeviceId();
			ss = DoRemoteResult.doResult(RemoteCaller.Login(user.getUsername(),
					user.getPassword(), deviceIMEI, clientVersion));
			// ss =
			// DoRemoteResult.doResult(RemoteCaller.Login(user.getUsername(),user.getPassword()));

			if ("601".equals(ss[0])) {
				sendMessage(
						FAIL_LOGIN,
						ToastHelper
								.getStringFromResources(R.string.network_disconnected));
				return false;
			}

			else if ("0".equals(ss[0])) {

				String sessionId = Format.replaceBlank(ss[1]);
				IngleApplication.setSessionId(sessionId);
				getUserInfo(sessionId, user);
				// sendMessage(MSG_LOGIN,null);
				return true;
			} else {
				// TODO ��ʻ�
				sendMessage(FAIL_LOGIN, ss[1]);
				return false;
			}

		} catch (XmcException e) {
			if (e.getMessage().equals(XmcException.TIMEOUT)) {
				sendMessage(MSG_LOGIN_NETWORK_NONE, user);
			} else {
				sendMessage(FAIL_LOGIN, e.getMessage());
			}
			return false;
		} catch (Exception e) {
			Logger.e(e);
			sendMessage(FAIL_LOGIN, e.getMessage());
			return false;
		}
	}

	/**
	 * �û����ε�½ʱ��ϵͳ��ͬ����������������ϵĸ�ǩ���½�4��Ĭ�Ͽ�Ѷ��ǩ�����п�Ѷ��ǩΪ��
	 * 
	 * @param userInfo
	 */
	private void initializeTemplateForNewUser() {
		List<ManuscriptTemplate> manuscriptTemplates = null;
		ManuscriptTemplate mtTemplate = null;
		try {
			manuscriptTemplates = RemoteCaller.GetTemplate(
					IngleApplication.getSessionId(),
					IngleApplication.getInstance()
							.getCurrentUser());

			for (TemplateType tt : TemplateType.values()) { // ����Ϊ���û�����ĸ���ϵͳĬ�Ͽ�Ѷģ���һ�����û��Զ���ģ��
				mtTemplate = new ManuscriptTemplate(
						IngleApplication.getInstance()
								.getCurrentUser(), tt.toString(), tt.getValue());
				mtTemplate.setDefaulttitle(tt.getValue()); // ��Ѷģ���Ĭ�ϱ���Ϊ��Ѷ���
				manuscriptTemplateService.addManuscriptTemplate(mtTemplate);
			}

			if (manuscriptTemplates == null || manuscriptTemplates.size() == 0) {
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
				manuscriptTemplateService.addManuscriptTemplate(mt);
			}

		} catch (XmcException e) {
			Logger.e(e);
			e.printStackTrace();
		}
	}

	/**
	 * ���´�������ص�ǰ�û���Ϣ
	 * 
	 * @return
	 */
	public void reloadUserOnline() {
		String sessionID = IngleApplication.getSessionId();
		User user = IngleApplication.getInstance().currentUserInfo;
		getUserInfo(sessionID, user);
	}

	/**
	 * 
	 * @param sendToaddressName
	 * @param sendToaddressCode
	 */
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

	/**
	 * ��ȡ��½�������Ƽ�
	 */
	private boolean GetAppServer() {
		try {
			String entranceUrlString = sharedPreferencesHelper
					.getPreferenceValue(PreferenceKeys.Sys_LoginServer
							.toString());

			if (entranceUrlString == null || entranceUrlString.equals("")) {// ���û��������(��һ�Σ�֮�󶼿��Դ������ļ���ȡ)
				entranceUrlString = Configuration.getDefaultServer();
			}
			entranceUrlString = entranceUrlString
					+ StandardizationDataHelper.getServerInterface(
							entranceUrlString, InterfaceType.initialUrl);
			String jsonResult = RemoteCaller.GetAppServerIP(entranceUrlString);

			if ("601".equals(jsonResult)) { // TODO ��ʻ�
				sendMessage(
						MSG_LOGIN_NETWORK_NONE,
						ToastHelper
								.getStringFromResources(R.string.requestNetError));
				return false;
			} else {
				String[] ips = null;
				String[] temp = null;
				if (StringUtils.isNotBlank(jsonResult)) {
					JSONObject obj = new JSONObject(jsonResult);
					temp = obj.get("AppServerIp").toString().split(",");

					JSONObject obj1 = (JSONObject) obj.get("AppServerIp");
					HashMap<String, String> hsHashMap = toHashMap(obj1);

					Configuration.setHashMapAppAddress(hsHashMap);
					return true;
				} else {
					return false;
				}
			}
		} catch (JSONException e) {
			Logger.e(e);
			sendMessage(FAIL_LOGIN, e.getMessage());
			return false;
		} catch (XmcException e) {
			Logger.e(e);
			sendMessage(FAIL_LOGIN, e.getMessage());
			return false;
		} catch (Exception e) {
			Logger.e(e);
			sendMessage(FAIL_LOGIN, e.getMessage());
			return false;
		}
	}

	/**
	 * ��json����ת����Map
	 * 
	 * @param jsonObject
	 *            json����
	 * @return HashMap����
	 * @throws JSONException
	 */
	private static HashMap<String, String> toHashMap(JSONObject jsonObject)
			throws JSONException {
		HashMap<String, String> result = new HashMap<String, String>();
		@SuppressWarnings("unchecked")
		Iterator<String> iterator = jsonObject.keys();
		String key = null;
		String value = null;
		while (iterator.hasNext()) {
			key = iterator.next();
			value = jsonObject.getString(key);
			result.put(key, value);
		}
		return result;
	}

	/**
	 * ������ʾ�Ի���
	 * 
	 * @param strTitle
	 *            �Ի������
	 * @param strMessage
	 *            ��ʾ����
	 */
	private void showAlertDialog(String strTitle, String strMessage) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(strMessage);
		builder.setTitle(strTitle);
		builder.setPositiveButton(R.string.confirm_button,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		AlertDialog alertDialog = builder.create();

		// ��������Ի����ܱ��û���[���ؼ�]��ȡ���,�����Է�������û�����KeyEvent.KEYCODE_SEARCH,�Ի����ǻ�Dismiss��
		alertDialog.setCancelable(false);
		// ��������alertDialog.setCancelable(false);
		// ��������û�����KeyEvent.KEYCODE_SEARCH,�Ի����ǻ�Dismiss��,�����setOnKeyListener���þ��������û�����KeyEvent.KEYCODE_SEARCH
		alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_SEARCH) {
					return true;
				} else {
					return false; // Ĭ�Ϸ��� false
				}
			}
		});

		alertDialog.show();
	}
}
