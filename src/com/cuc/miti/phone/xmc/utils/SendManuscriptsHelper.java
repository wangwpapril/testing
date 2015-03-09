package com.cuc.miti.phone.xmc.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Accessories;
import com.cuc.miti.phone.xmc.domain.Enums.NetStatus;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceKeys;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceType;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.domain.Manuscripts;
import com.cuc.miti.phone.xmc.domain.User;
import com.cuc.miti.phone.xmc.domain.XmcException;
import com.cuc.miti.phone.xmc.http.RemoteCaller;
import com.cuc.miti.phone.xmc.R;
import com.cuc.miti.phone.xmc.domain.ManuscriptTemplate;
import com.cuc.miti.phone.xmc.ui.VersionUpdateActivity;
import com.cuc.miti.phone.xmc.utils.ToastHelper;

/**
 * ��������࣬��ɸ����֡�����������ӵȹ���
 * 
 * @author SongQing
 * 
 */
public class SendManuscriptsHelper {

	private Context mContext = null;

	public SendManuscriptsHelper(Context context) {
		this.mContext = context;
	}

	/**
	 * У�����XML��Ϣʱ�������������ȷ��
	 * 
	 * @param mu
	 * @param result
	 */
	public static boolean validateForXML(Manuscripts mu, KeyValueData message) {

		boolean result = true;

		message.setValue("");

		if (mu.getTitle().equals("")
				|| mu.getTitle()
						.equals(ToastHelper
								.getStringFromResources(R.string.value_manu_notitle_default))) {
			message.setValue(message
					.getValue()
					.concat(ToastHelper
							.getStringFromResources(R.string.validate_titleNull)));
			result = false;
		}
		if (mu.getAuthor().equals("") || mu.getAuthor().trim().equals("")) {
			message.setValue(message
					.getValue()
					.concat(ToastHelper
							.getStringFromResources(R.string.validate_authorNull)));
			result = false;
		}
		if (mu.getManuscriptTemplate().getKeywords().equals("")
				|| mu.getManuscriptTemplate().getKeywords().trim().equals("")) {
			message.setValue(message
					.getValue()
					.concat(ToastHelper
							.getStringFromResources(R.string.validate_keyWordNull)));
			result = false;
		}
		if (mu.getManuscriptTemplate().getLanguage().equals("")) {
			message.setValue(message
					.getValue()
					.concat(ToastHelper
							.getStringFromResources(R.string.validate_languageNull)));
			result = false;
		}
		if (mu.getManuscriptTemplate().getPriority().equals("")) {
			message.setValue(message
					.getValue()
					.concat(ToastHelper
							.getStringFromResources(R.string.validate_priorityNull)));
			result = false;
		}
		if (mu.getManuscriptTemplate().getProvtype().equals("")) {
			message.setValue(message
					.getValue()
					.concat(ToastHelper
							.getStringFromResources(R.string.validate_provtypeNull)));
			result = false;
		}
		if (mu.getManuscriptTemplate().getDoctype().equals("")) {
			message.setValue(message
					.getValue()
					.concat(ToastHelper
							.getStringFromResources(R.string.validate_doctypeNull)));
			result = false;
		}
		if (mu.getManuscriptTemplate().getComefromDept().equals("")) {
			message.setValue(message
					.getValue()
					.concat(ToastHelper
							.getStringFromResources(R.string.validate_comefromDeptNull)));
			result = false;
		}
		if (mu.getManuscriptTemplate().getRegion().equals("")) {
			message.setValue(message
					.getValue()
					.concat(ToastHelper
							.getStringFromResources(R.string.validate_regionNull)));
			result = false;
		}
		if (mu.getManuscriptTemplate().getAddress().equals("")) {
			message.setValue(message
					.getValue()
					.concat(ToastHelper
							.getStringFromResources(R.string.validate_addressNull)));
			result = false;
		}
		if (mu.getManuscriptTemplate().getReviewstatus().equals("")
				|| mu.getManuscriptTemplate().getReviewstatus().trim()
						.equals("")) {
			message.setValue(message
					.getValue()
					.concat(ToastHelper
							.getStringFromResources(R.string.validate_reviewstatusNull)));
			result = false;
		}

		return result;
	}

	/**
	 * У�����XML��Ϣʱ�������������ȷ��
	 * 
	 * @param mu
	 * @param result
	 */
	public static boolean validateForMTXML(ManuscriptTemplate mu,
			KeyValueData message) {

		boolean result = true;

		message.setValue("");

		if (mu.getAuthor().equals("")) {
			message.setValue(message
					.getValue()
					.concat(ToastHelper
							.getStringFromResources(R.string.validate_authorNull)));
			result = false;
		}
		if (mu.getKeywords().equals("")) {
			message.setValue(message
					.getValue()
					.concat(ToastHelper
							.getStringFromResources(R.string.validate_keyWordNull)));
			result = false;
		}
		if (mu.getLanguage().equals("")) {
			message.setValue(message
					.getValue()
					.concat(ToastHelper
							.getStringFromResources(R.string.validate_languageNull)));
			result = false;
		}
		if (mu.getPriority().equals("")) {
			message.setValue(message
					.getValue()
					.concat(ToastHelper
							.getStringFromResources(R.string.validate_priorityNull)));
			result = false;
		}
		if (mu.getProvtype().equals("")) {
			message.setValue(message
					.getValue()
					.concat(ToastHelper
							.getStringFromResources(R.string.validate_provtypeNull)));
			result = false;
		}
		if (mu.getDoctype().equals("")) {
			message.setValue(message
					.getValue()
					.concat(ToastHelper
							.getStringFromResources(R.string.validate_doctypeNull)));
			result = false;
		}
		if (mu.getComefromDept().equals("")) {
			message.setValue(message
					.getValue()
					.concat(ToastHelper
							.getStringFromResources(R.string.validate_comefromDeptNull)));
			result = false;
		}
		if (mu.getRegion().equals("")) {
			message.setValue(message
					.getValue()
					.concat(ToastHelper
							.getStringFromResources(R.string.validate_regionNull)));
			result = false;
		}
		if (mu.getAddress().equals("")) {
			message.setValue(message
					.getValue()
					.concat(ToastHelper
							.getStringFromResources(R.string.validate_addressNull)));
			result = false;
		}
		if (mu.getReviewstatus().equals("")) {
			message.setValue(message
					.getValue()
					.concat(ToastHelper
							.getStringFromResources(R.string.validate_reviewstatusNull)));
			result = false;
		}

		return result;
	}

	/**
	 * У�鵱ǰϵͳ�İ汾 ����ǰУ���Ƿ�����Ͱ汾Ҫ�󣬵�¼ʱУ���Ƿ������°汾
	 * 
	 * @param message
	 * @param context
	 * @param type
	 * @return
	 * @throws XmcException
	 */
	public static boolean getVersion(KeyValueData message, Context context,
			String type) throws XmcException {
		boolean returnValue = false;
		String[] versionServer = RemoteCaller.getAndroidAppVersion();
		String[] versionLocal = DeviceInfoHelper.getAppVersionName(context);
		if (versionServer == null) {
			return true;
		} else {
			if (type.equals("GetVersionInfo")) {
				message.setValue(versionServer[0] + "," + versionServer[1]);
				returnValue = true;
			} else if (type.equals("SendManuscript")) { // ����У���Ƿ�����Ͱ汾Ҫ��
				if (Float.parseFloat(versionServer[0]) > Float
						.parseFloat(versionLocal[1])) {// ��ǰ�汾��������Ҫ��
					message.setValue(ToastHelper
							.getStringFromResources(R.string.SysVersionTooLow));
				} else {
					returnValue = true;
				}
			} else { // ��½У���Ƿ��и��ڵ�ǰ�汾�����°汾
				if (Float.parseFloat(versionServer[1]) > Float
						.parseFloat(versionLocal[1])) {
					message.setValue(ToastHelper
							.getStringFromResources(R.string.SysVersionLatest)
							+ versionServer[1]
							+ ToastHelper
									.getStringFromResources(R.string.SysVersionUpgradeNow));

				}
				returnValue = true;
			}
		}

		return returnValue;
	}

	/**
	 * У�������Ƿ����
	 * 
	 * @return
	 */
	public static boolean validateNetwork(KeyValueData message, Context context) {

		boolean result = true;

		if (message == null) {
			message = new KeyValueData("", "");
		}

		if (IngleApplication.getNetStatus() == NetStatus.Disable) {
			result = false;
		}

		if (!result) {
			message.setValue(message
					.getValue()
					.concat(ToastHelper
							.getStringFromResources(R.string.network_disconnectedToSend)));
		}
		return result;
	}

	/**
	 * У�鵱ǰϵͳ�汾�Ƿ�����ͷ���Ҫ��
	 * 
	 * @param mu
	 * @param message
	 * @return
	 */
	public static boolean validateVersion(KeyValueData message, Context context) {

		boolean returnValue = false;
		SharedPreferencesHelper shPreferencesHelper = new SharedPreferencesHelper(
				context);
		try {
			String[] versionServer;
			versionServer = RemoteCaller.getAndroidAppVersion();
			shPreferencesHelper.SaveCommonPreferenceSettings(
					PreferenceKeys.Sys_VersionLowest.toString(),
					PreferenceType.String, versionServer[0]);
		} catch (XmcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String lowestVersionString = shPreferencesHelper
				.getPreferenceValue(PreferenceKeys.Sys_VersionLowest.toString());
		String[] versionLocal = DeviceInfoHelper.getAppVersionName(context);

		if (Float.parseFloat(lowestVersionString) > Float
				.parseFloat(versionLocal[1])) {// ��ǰ�汾��������Ҫ��
			ArrayList<Activity> activities = IngleApplication
					.getInstance().getActivities();
			Intent backIntent = new Intent();
			if (activities != null && activities.size() > 0) {
				backIntent = new Intent(activities.get(activities.size() - 1),
						VersionUpdateActivity.class);
				activities.get(activities.size() - 1).startActivity(backIntent);
			}
		} else {
			returnValue = true;
		}

		return returnValue;
	}

	/**
	 * У���û��Ƿ���Ȩ�޷���
	 * 
	 * @param mu
	 * @param message
	 * @return
	 */
	public static boolean validataUserRights(Manuscripts mu,
			KeyValueData message, Context context) {

		User user = IngleApplication.getInstance().currentUserInfo;
		if (user == null || user.getUserattribute() == null) {
			message.setValue(message.getValue()
					.concat(ToastHelper
							.getStringFromResources(R.string.sessionTimeout)));
			return false;
		}

		if (user.getUserattribute().getRightDisabled() == true) {
			message.setValue(message.getValue().concat(
					ToastHelper.getStringFromResources(R.string.noRightToSend)));
			return false;
		}

		return true;
	}

	/**
	 * У���û������ַ
	 * 
	 * @param mu
	 * @param message
	 * @return
	 */
	public static boolean validataReleAddress(Manuscripts mu,
			KeyValueData message) {

		User user = IngleApplication.getInstance().currentUserInfo;

		if (user.getUserattribute().getRightTransferNews() == true) {
			String[] address = mu.getManuscriptTemplate().getAddress()
					.split(",");

			ArrayList<KeyValueData> transferAddress = user.getUserattribute()
					.getTransferAddressList();

			boolean result = false;

			// ��ʼ�ȶ��û��ķ����ַ�Ƿ�����Ȩ����
			for (int i = 0; i < transferAddress.size(); i++) {
				result = false;

				for (int j = 0; j < address.length; j++) {
					if (address[j].equals(transferAddress.get(i).getKey())) {
						result = true;
						break;
					}
				}

				if (result == false) {
					message.setValue(message
							.getValue()
							.concat(ToastHelper
									.getStringFromResources(R.string.noRightToSendByAddress)));
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * ����У��
	 * 
	 * @param mu
	 * @param message
	 * @return
	 */
	public static boolean validataForSave(Manuscripts mu, KeyValueData message) {
		if (mu.getTitle() == null || mu.getTitle().equals("")) {
			message.setValue(message.getValue().concat(
					IngleApplication.getInstance()
							.getResources().getString(R.string.manuTitle_hint)));
			return false;
		}
		return true;
	}

	/**
	 * У���½��˳�ʱ����Ƿ���Ҫ���� <�ж�����(���ⲻΪ��|���Ĳ�Ϊ��|������Ϊ��)>
	 * 
	 * @param mu
	 * @param accs
	 * @return
	 */
	public static boolean validateForBack(Manuscripts mu, List<Accessories> accs) {
		int result = 0;

		if ((mu.getTitle().equals("") || mu.getTitle() == null)
				|| (mu.getTitle().equals(mu.getManuscriptTemplate()
						.getDefaulttitle()))) {

			result++;
		}
		if ((mu.getContents().equals("") || mu.getContents() == null)
				|| (mu.getContents().equals(mu.getManuscriptTemplate()
						.getDefaultcontents()))) {

			result++;
		}
		if (accs == null || accs.size() == 0) {
			result++;
		}

		return result == 3 ? false : true;
	}

	/**
	 * У���Զ�����ʱ����Ƿ���Ա���
	 * 
	 * @param mu
	 * @param accs
	 * @return
	 */
	public static boolean validateForAutoSave(Manuscripts mu,
			List<Accessories> accs) {
		int result = 0;

		if ((mu.getTitle().equals("") || mu.getTitle() == null)) {
			result++;
		}
		if ((mu.getContents().equals("") || mu.getContents() == null)) {
			result++;
		}
		if (accs == null || accs.size() == 0) {
			result++;
		}
		return result == 3 ? false : true;
	}

	/**
	 * ����У��
	 * 
	 * @param mu
	 * @param message
	 * @param context
	 * @return
	 */
	public static boolean validateForSend(Manuscripts mu, KeyValueData message,
			Context context, String uploadType) {

		// ���ȼ�������Ƿ����
		if (!validateNetwork(message, context)) {
			return false;
		}

		// TODO �ò����汾��������Σ�Ŀǰ��ͬ�ⷢ��
		boolean resultVersion = true;
		if (uploadType.equals("New")) {
			resultVersion = validateVersion(message, context);
		}

		// boolean resultXML = validateForXML(mu, message);
		boolean resultRights = validataUserRights(mu, message, context);
		boolean resultAddress = validataReleAddress(mu, message);

		return resultRights & resultAddress & resultVersion;
	}

	/**
	 * Ⱥ��ǰУ�����пɷ��͵ĸ��
	 * 
	 * @param list
	 * @param messages
	 * @param context
	 * @return
	 */
	public static boolean validateForSendWithoutCheckNetWork(Manuscripts mu,
			KeyValueData message, Context context) {

		boolean resultXML = validateForXML(mu, message);
		boolean resultRights = validataUserRights(mu, message, context);
		boolean resultAddress = validataReleAddress(mu, message);

		return resultXML & resultRights & resultAddress;
	}

	/**
	 * ǩ�����У��(��������У�飬�����û�Ȩ��У�飬���������ַУ��)
	 * 
	 * @param list
	 * @param messages
	 * @param context
	 * @return
	 */
	public static boolean validateForReleManuscript(Manuscripts mu,
			KeyValueData message, Context context) {
		return validateForXML(mu, message);
	}

	public static boolean validateForInstantMT(ManuscriptTemplate mt,
			KeyValueData message, Context context) {
		return validateForMTXML(mt, message);
	}

}
