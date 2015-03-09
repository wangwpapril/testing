package com.cuc.miti.phone.xmc.utils;

import java.io.File;

import android.os.Environment;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Enums.AccessoryType;
import com.cuc.miti.phone.xmc.domain.Enums.InterfaceType;
import com.cuc.miti.phone.xmc.domain.Enums.LogType;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceKeys;
import com.cuc.miti.phone.xmc.http.Configuration;

/**
 * ��������ֻ�ͻ�������ı�׼����Ϣ�����ռȶ���������ļ����ļ�·����
 * 
 * @author SongQing
 * 
 */
public class StandardizationDataHelper {

	private static final String PROGRAM_FOLDER = "com.cuc.miti.phone.xmc"; // ������������Դ�ļ��еĸ�Ŀ¼
	//private static final String PROGRAM_FOLDER = "eNews"; // ������������Դ�ļ��еĸ�Ŀ¼

	private static final String LOG_FOLDER = "Logs"; // ��־�ļ�Ŀ¼��
	private static final String ACCESSORY_FOLDER = "Accessories"; // �ļ�����Ŀ¼��
	private static final String CONFIG_FOLDER = "Configs"; // �������XML�ļ�Ŀ¼��
	private static final String BASEDATA_FOLDER = "BaseDatas"; // �����XML�ļ�Ŀ¼��
	private static final String TEMP_FOLDER = "Temp";
	private static final String FTP_FOLDER = "Ftp";

	/**
	 * ��ȡ��־�ļ�����·��
	 * 
	 * @param logType
	 *            ��־����(Operation/System)
	 * @return
	 */
	public static String getLogFileStorePath(LogType logType) {
		String sdCardsPath = GetSDCardPath();
		if (sdCardsPath == "") {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		String path = sb.append(sdCardsPath).append("//")
				.append(PROGRAM_FOLDER).append("//").append(LOG_FOLDER)
				.append("//").append(logType.toString()).toString();

		if (PathValidate(path)) {
			return path;
		} else {
			return "";
		}
	}

	/**
	 * ��ȡ�����ļ��͸���������ñ���·��
	 * 
	 * @return
	 */
	public static String getAccessoryFileTempStorePath() {
		String sdCardsPath = GetSDCardPath();
		if (sdCardsPath == "") {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		String path = sb.append(sdCardsPath).append("//")
				.append(PROGRAM_FOLDER).append("//").append(TEMP_FOLDER)
				.toString();

		if (PathValidate(path)) {
			return path;
		} else {
			return "";
		}
	}
	/**
	 * ��ȡFtp�ļ������ñ���·��
	 * 
	 * @return
	 */
	public static String getFtpFileFolderStorePath(){
		String sdCardsPath = GetSDCardPath();
		if (sdCardsPath == "") {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		String path = sb.append(sdCardsPath).append("//")
				.append(PROGRAM_FOLDER).append("//").append(FTP_FOLDER)
				.toString();

		if (PathValidate(path)) {
			return path;
		} else {
			return "";
		}
	}
	/**
	 * ��ȡ�����ļ��͸�����汣��·��
	 * 
	 * @param accessoryType
	 *            ��������(Picture/Video/Voice/Complex/Graph)
	 * @return
	 */
	public static String getAccessoryFileUploadStorePath(
			AccessoryType accessoryType) {
		String sdCardsPath = GetSDCardPath();
		if (sdCardsPath == "") {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		String path = sb.append(sdCardsPath).append("//")
				.append(PROGRAM_FOLDER).append("//")
				.append(GetCurrentUserName()).append("//")
				.append(ACCESSORY_FOLDER).append("//")
				.append(accessoryType.toString()).toString();

		if (PathValidate(path)) {
			return path;
		} else {
			return "";
		}
	}

	/**
	 * ��ȡ�����XML�ļ�����·��
	 * 
	 * @return
	 */
	public static String GetBaseDataFileStorePath() {
		String sdCardsPath = GetSDCardPath();
		if (sdCardsPath == "") {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		String path = sb.append(sdCardsPath).append("//")
				.append(PROGRAM_FOLDER).append("//").append(BASEDATA_FOLDER)
				.toString();

		if (PathValidate(path)) {
			return path;
		} else {
			return "";
		}
	}

	/**
	 * ��ȡ�������XML�ļ�Ŀ¼��
	 * 
	 * @return
	 */
	public static String GetConfigFileStorePath() {
		String sdCardsPath = GetSDCardPath();
		if (sdCardsPath == "") {
			return "";
		}

		StringBuilder sb = new StringBuilder();

		String path = sb.append(sdCardsPath).append("//")
				.append(PROGRAM_FOLDER).append("//")
				.append(GetCurrentUserName()).append("//")
				.append(CONFIG_FOLDER).toString();

		if (PathValidate(path)) {
			return path;
		} else {
			return "";
		}
	}

	public static String getSDCardStorePath() {
		String sdCardsPath = GetSDCardPath();

		if (sdCardsPath == "") {
			return "";
		}
		else {
			return sdCardsPath;
		}
	}
	
	
	
	/**
	 * ��ȡ���ӷ������Ľӿڹ���
	 * @param entranceUrl       ��������ڵ�ַ    ��ʽ_http://202.84.17.50/     ����_http://202.84.17.51/m/
	 * @param type				   �ӿ�����
	 * @return
	 */
	public static String getServerInterface(String entranceUrl,InterfaceType type){
		String returnString = "";
		if(entranceUrl !=null && !entranceUrl.equals("")) {
			if(entranceUrl.contains("m")){				//���Խӿ�
				switch(type){
					/*case sinosoftUrl:
						returnString =   "s/client/i";
						break;
					case initialUrl:
						returnString =  "client/wu";
						break;
					case baseUrl:
						returnString =  "client/c";
						break;
					case topicdownloadUrl:
						returnString = "s/topiclist/";
						break;
					case oalistUrl:
						returnString="s/oainfo_list.do";
						break;
					case oaitemUrl:
						returnString = "s/oainfo_getInfoById.do";
						break;
					default:
						break;*/
				case initialUrl:
					returnString =  "client/wu";
					break;
				case baseUrl:
					returnString =  "client/c";
					break;
				case instantUploadUrl:
				case sinosoftUrl:
				case oalistUrl:
				case oaitemUrl:
				case locationUrl:
					returnString =   "client/v2i";
					break;
				case topicdownloadUrl:
					returnString =   "client/v2i?ua=gettopiclist&topiclist=";
					break;
				default:
					break;
				}
			}
			else{
				switch(type){								//��ʽ�ӿ�					
				
				case initialUrl:
					returnString =  "client/wu";
					break;
				case baseUrl:
					returnString =  "client/c";
					break;
				case instantUploadUrl:
				case sinosoftUrl:
				case oalistUrl:
				case oaitemUrl:
				case locationUrl:
					returnString =   "client/v2c";
					break;
				case topicdownloadUrl:
					returnString =   "client/v2c?ua=gettopiclist&topiclist=";
					break;
				default:
					break;}
			}
		}
		
		return returnString;
	}

	/**
	 * �ж��Ƿ��пɶ���SD�����ڣ�������SD��·��
	 * 
	 * @return ����_SD��Ŀ¼ || ������_���ַ�
	 */
	private static String GetSDCardPath() {
		// �ж��ⲿ�洢SD���Ƿ���ڣ����ҿ�д
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {		//˵��������SDCARD
			return Environment.getExternalStorageDirectory().toString(); // ��ȡ�ⲿ�洢SDCard�ĸ�Ŀ¼
		}else{
			if(DeviceInfoHelper.getAvailableInternalMemorySize()> 100 * 1024* 1024){				//�ڲ��洢·������200MB��Ϊ����
				//String pathString = DeviceInfoHelper.GetBuildProproperties("ro.additionalmounts");
				return "//mnt/emmc";
			}else{
				return "";
			}
		}
		//return "";
	}

	/**
	 * ��֤·���Ƿ���ڣ������ھʹ���
	 * 
	 * @param path
	 * @return
	 */
	private static boolean PathValidate(String path) {
		if (path == null || path == "") {
			return false;
		}

		File newfolder = new File(path);
		if (!newfolder.exists()) {
			if (newfolder.mkdirs()) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	/**
	 * ��ȡ��ǰ�û����(ÿ���û������Լ���Ӧ�ĳ���Ŀ¼)
	 * 
	 * @return
	 */
	private static String GetCurrentUserName() {
		return IngleApplication.getInstance()	.getCurrentUser();
	}
	
	/**
	 * �û������ڵ�½�������÷���url��������ɸ��ֽӿڵ�ַ
	 * @param serverUrl    http://202.84.17.51/m/ �� http://202.84.17.50/��
	 */
	public static void setUrlForTest(String serverUrl){
		try {
			SharedPreferencesHelper sharedPreferencesHelper= new SharedPreferencesHelper(IngleApplication.getInstance());
			String baseInterfaceString = StandardizationDataHelper.getServerInterface(sharedPreferencesHelper.getPreferenceValue(PreferenceKeys.Sys_CurrentServer.toString()), InterfaceType.baseUrl);
			if(baseInterfaceString == ""){			//��ֹ�û�û�����÷�������ַ
				Configuration.setBaseUrl("http://202.84.17.51/m/client/c");
			}
			else{
				Configuration.setBaseUrl(serverUrl + baseInterfaceString);
			}
			
			baseInterfaceString = StandardizationDataHelper.getServerInterface(sharedPreferencesHelper.getPreferenceValue(PreferenceKeys.Sys_CurrentServer.toString()), InterfaceType.instantUploadUrl);
			if(baseInterfaceString==""){
				Configuration.setInstantUploadUrl("http://202.84.17.51/m/client/v2i");
			}else{
				Configuration.setInstantUploadUrl(serverUrl + baseInterfaceString);
			}			
			
			baseInterfaceString = StandardizationDataHelper.getServerInterface(sharedPreferencesHelper.getPreferenceValue(PreferenceKeys.Sys_CurrentServer.toString()), InterfaceType.sinosoftUrl);
			if(baseInterfaceString==""){
				Configuration.setSinosoftUrl("http://202.84.17.51/ms/client/i");
			}else{
				Configuration.setSinosoftUrl(serverUrl + baseInterfaceString);
			}
			baseInterfaceString = StandardizationDataHelper.getServerInterface(sharedPreferencesHelper.getPreferenceValue(PreferenceKeys.Sys_CurrentServer.toString()), InterfaceType.topicdownloadUrl);
			if(baseInterfaceString == ""){
				Configuration.setTopicDownloadUrl("http://202.84.17.51/ms/topiclist/");
			}
			else{
				Configuration.setTopicDownloadUrl(serverUrl + baseInterfaceString);
			}
			baseInterfaceString = StandardizationDataHelper.getServerInterface(sharedPreferencesHelper.getPreferenceValue(PreferenceKeys.Sys_CurrentServer.toString()), InterfaceType.oalistUrl);
			if(baseInterfaceString == ""){
				Configuration.setOAInfoListUrl("http://202.84.17.51/ms/oainfo_list.do");
			}
			else{
				Configuration.setOAInfoListUrl(serverUrl + baseInterfaceString);
			}
			baseInterfaceString = StandardizationDataHelper.getServerInterface(sharedPreferencesHelper.getPreferenceValue(PreferenceKeys.Sys_CurrentServer.toString()), InterfaceType.oaitemUrl);
			if(baseInterfaceString == ""){
				Configuration.setOAInfoItemUrl("http://202.84.17.51/ms/oainfo_getInfoById.do");
			}
			else{
				Configuration.setOAInfoItemUrl(serverUrl  + baseInterfaceString);
			}
			baseInterfaceString = StandardizationDataHelper.getServerInterface(sharedPreferencesHelper.getPreferenceValue(PreferenceKeys.Sys_CurrentServer.toString()), InterfaceType.locationUrl);
			if(baseInterfaceString == ""){
				Configuration.setlocationUrl("http://202.84.17.51/m/client/v2c");
			}
			else{
				Configuration.setlocationUrl(serverUrl + baseInterfaceString);
			}
		} catch (Exception e) {
			Logger.e(e);				
		}
	}

}
