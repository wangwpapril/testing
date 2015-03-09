package com.cuc.miti.phone.xmc.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import com.cuc.miti.phone.xmc.domain.Enums.AccessoryType;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceType;
import com.cuc.miti.phone.xmc.domain.Manuscripts;

public class ManuscriptIDCreator {

	private Context context;
	private final String DAILY_COUNT_KEY = "ManuscriptCountDaily";

	public ManuscriptIDCreator(Context context) {
		this.context = context;
	}

	/**
	 * �������ǩ��ID
	 */
	public String generateReleID() {
		return "";
	}
	

	/**
	 * �����������ID
	 * 
	 * @return
	 */
	public String generateCreateID(Manuscripts ma) {

		String systemID = "Mnews";
		String serverNum = "2";
		String language = getLanguage(ma.getManuscriptTemplate()
				.getLanguageID());
		String serialNum = "009000";
		String manuCount = "";

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String today = formatter.format(new Date());
		int count = 0;

		SharedPreferencesHelper helper = new SharedPreferencesHelper(
				this.context);
		manuCount = helper.GetUserPreferenceValue(DAILY_COUNT_KEY);

		if (!manuCount.equals("")) {
			if (today.equals(manuCount.substring(0, 8))) {
				count = Integer.parseInt(manuCount.substring(9));
			}
			else {
				count = 9000;
			}
		}
		else {
			count = 9000;
		}
		count += 1;
		
		String tempCount = String.valueOf(count);
		if(tempCount.length() == 4){
			serialNum = String.valueOf("00").concat(
					tempCount);
		} else if (tempCount.length() == 5) {
			serialNum = String.valueOf("0").concat(
					tempCount);
		} else {
			serialNum = String.valueOf(tempCount);
		}
		

		manuCount = today.concat("-").concat(serialNum);
		
		helper.SaveUserPreferenceSettings(DAILY_COUNT_KEY, PreferenceType.String, manuCount);
		
		//helper.SaveUserPreferenceSettings(DAILY_COUNT_KEY, PreferenceType.String, "20120617-009001");

		String systemToken = "EN";
		String manuType = switchDocType(ma.getNewstypeID());
		String manuLevel = "S";// δ���� S����Ʒ�� F
		String manuFlow = "N";// ���� N
		String version = "0";// ��Ʒ��汾 0 ԭʼ�汾

		String createId = systemID.concat(serverNum).concat(language)
				.concat(serialNum).concat("_").concat(today).concat("_").concat(systemToken).concat(manuType)
				.concat(manuLevel).concat(manuFlow).concat(version);

		return createId;
	}

	private String switchDocType(String docType) {
		if (docType.equals(AccessoryType.Picture.toString())) {
			return "P";
		} else if (docType.equals(AccessoryType.Video.toString())) {
			return "V";
		} else if (docType.equals(AccessoryType.Voice.toString())) {
			return "A";
		} else if (docType.equals(AccessoryType.Complex.toString())) {
			return "M";
		} else if (docType.equals(AccessoryType.Text.toString())) {
			return "T";
		}
		return null;
	}

	private String getLanguage(String languageId) {
		if (languageId.equals("zh-CN"))
			return "C";
		else if (languageId.equals("en"))
			return "E";
		else if (languageId.equals("fr"))
			return "F";
		else if (languageId.equals("es"))
			return "S";
		else if (languageId.equals("ru"))
			return "R";
		else if (languageId.equals("ar"))
			return "A";
		else if (languageId.equals("pt"))
			return "P";
		else if (languageId.equals("ja"))
			return "J";
		else if (languageId.equals("ko"))
			return "K";
		else if (languageId.equals("zh-TW"))
			return "T";

		return "B"; // ������Ϊ��˫��
	}
}
