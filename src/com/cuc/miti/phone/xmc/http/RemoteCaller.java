package com.cuc.miti.phone.xmc.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Enums.MessageType;
import com.cuc.miti.phone.xmc.domain.Enums.MsgOwnerType;
import com.cuc.miti.phone.xmc.domain.LocationDomain;
import com.cuc.miti.phone.xmc.domain.ManuscriptTemplate;
import com.cuc.miti.phone.xmc.domain.MessageForOa;
import com.cuc.miti.phone.xmc.domain.MessageForUs;
import com.cuc.miti.phone.xmc.domain.PositionInfo;
import com.cuc.miti.phone.xmc.domain.XmcException;
import com.cuc.miti.phone.xmc.utils.DeviceInfoHelper;
import com.cuc.miti.phone.xmc.utils.Encrypt;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.TimeFormatHelper;
import com.cuc.miti.phone.xmc.utils.XMLDataHandle;
import com.google.mitijson.Gson;

/**
 * Զ�̵��ýӿ���
 * 
 * @author shiqing
 * 
 */
public class RemoteCaller {
	private static int msgForOa_totalCount = 0; // OA��Ϣ����
	static HttpClient httpClient = new HttpClient();
	private static int LOGIN_TIMEOUT = 5000; // ��¼��ʱ
	private static int NORMAL_TIMEOUT = 6000; // ��ͨ��������ʱ
	private static int MANUSCRIPT_TIMEOUT = 10000; // ����ϴ��ύ��ؽӿڳ�ʱ����Ϊ10��

	public static List<String> addressAll=null;

	/**
	 * @Description �û���¼
	 * @param userName
	 * @param password
	 * @return
	 * @throws XmcException
	 */
	public static String Login(String userName, String password)
			throws XmcException {
		String returnValueString = "";
		try {
			PostParameter[] postParameters = new PostParameter[] {
					new PostParameter("ua", "sys.login"),
					new PostParameter("loginname", userName),
					new PostParameter("loginpswd", password) };

			returnValueString = httpClient.doPost(postParameters, Configuration
					.getBaseUrl(), LOGIN_TIMEOUT);
		} catch (Exception e) {
			exceptionTypeJudge(e);
		}

		return returnValueString;

	}

	/**
	 * @Description �û���¼(��ҪIMEI��֤)
	 * @param userName
	 * @param password
	 * @param deviceIMEI
	 * @return
	 * @throws XmcException
	 */
	public static String Login(String userName, String password,
			String deviceIMEI,String clientVersion) throws XmcException {
		String returnValueString = "";
		try {
			
			PostParameter[] postParameters = new PostParameter[] {
					new PostParameter("ua", "sys.login"),
					new PostParameter("loginname", userName),
					new PostParameter("loginpswd", Encrypt.toMD5(password)),
					new PostParameter("encryptMethod", 1),//0=���� 1=md5 2=Base64 3=SHA >=4����չ
					new PostParameter("vType", "device.imei"),
					new PostParameter("vData", deviceIMEI),
					new PostParameter("SystemId","eNews.AndroidPhone"),
					new PostParameter("ClientVersion",clientVersion)};

			returnValueString = httpClient.doPost(postParameters, Configuration
					.getBaseUrl(), LOGIN_TIMEOUT);
		} catch (Exception e) {
			exceptionTypeJudge(e);
		}

		return returnValueString;
	}

	/**
	 * @Description �ύ��λ��Ϣ
	 * @param userName
	 * @param sessionId
	 * @param lct
	 * @param location
	 * @param memo
	 * @param source
	 * @return
	 * @throws XmcException
	 */
	public static String[] Location(String userName, String sessionId,
			PositionInfo lct, String location, String memo, String source)
			throws XmcException {
		String[] returnValueString = {"",""};
		
		try {
			if(lct!=null){
			LocationDomain locationDomain = new LocationDomain(userName,
					sessionId, "upload", lct.getLatitude(), lct.getLongitude(),
					lct.getAltitude(), location, memo, source);

			Gson gson = new Gson();
			String parasString = gson.toJson(locationDomain);

			HttpClient httpClient = new HttpClient();
		
			String JSONResult = httpClient.post(parasString, Configuration
					.getlocationUrl()
					+ "?ua=location", NORMAL_TIMEOUT);	
			if (StringUtils.isNotBlank(JSONResult)) {

				JSONObject jb = new JSONObject(JSONResult);
				returnValueString[0]=(String) jb.get("status");
				returnValueString[1]=(String) jb.get("message");

			}
		}
		} catch (Exception e) {
			exceptionTypeJudge(e);
		}

		return returnValueString;
	}

	/**
	 * @Description �ϴ���һ�� ��������̿ռ䣬�ο�V16
	 * @param sessionId
	 * @param length
	 * @return
	 * @throws XmcException
	 */
	public static String FileUpNew(String sessionId, int length)
			throws XmcException {
		String returnValueString = "";
		try {
			PostParameter[] postParameters = new PostParameter[] {
					new PostParameter("ua", "file.upnew"),
					new PostParameter("sss", sessionId),
					new PostParameter("len", String.valueOf(length)) };

			returnValueString = httpClient.doPost(postParameters, Configuration
					.getBaseUrl(), MANUSCRIPT_TIMEOUT);
		} catch (Exception e) {
			exceptionTypeJudge(e);
		}

		return returnValueString;
	}

	/**
	 * 
	 * 
	 * @Description �ϴ��ڶ������ֿ��ϴ����ο�V16
	 * @param sessionId
	 * @param fId
	 * @param checkCode
	 * @param range
	 * @param fileData
	 * @return
	 * @throws XmcException
	 */
	public static String FileUpPartBlock(String sessionId, String fId,
			String checkCode, String range, byte[] fileData)
			throws XmcException {
		String returnValueString = "";
		try {
			PostParameter[] formPostParams = new PostParameter[] {
					new PostParameter("sss", sessionId),
					new PostParameter("ua", "file.uppartblock") };
			PostParameter[] contentPostParams = new PostParameter[] {
					new PostParameter("checkCode", checkCode),
					new PostParameter("checkFlag", "0"),
					new PostParameter("fid", fId),
					new PostParameter("range", range) };
			returnValueString = httpClient.multipartPost(formPostParams,
					contentPostParams, fileData);
		} catch (Exception e) {
			exceptionTypeJudge(e);
		}

		return returnValueString;
	}

	/**
	 * @Description �ϴ���̵��� ��ȷ�ϡ��ο�V16
	 * @param sessionId
	 * @param fId
	 * @param length
	 * @param checkCode
	 * @return
	 * @throws XmcException
	 */
	public static String FileUpConfirm(String sessionId, String fId,
			int length, String checkCode) throws XmcException {
		String returnValueString = "";
		try {
			PostParameter[] postParameters = new PostParameter[] {
					new PostParameter("ua", "file.upconfirm"),
					new PostParameter("sss", sessionId),
					new PostParameter("fid", fId),
					new PostParameter("len", String.valueOf(length)),
					new PostParameter("checkCode", checkCode),
					new PostParameter("checkFlag", "0"),
					new PostParameter("compressFlag", "0"),
					new PostParameter("encryptFlag", "0"),
					new PostParameter("uncompressLen", String.valueOf(length)),
					new PostParameter("unencryptLen", String.valueOf(length)) };

			returnValueString = httpClient.doPost(postParameters, Configuration
					.getBaseUrl(), MANUSCRIPT_TIMEOUT);
		} catch (Exception e) {
			exceptionTypeJudge(e);
		}
		return returnValueString;
	}

	/**
	 * @Description �ύ������ο�V16
	 * @param sessionId
	 * @param fidXml
	 * @param fidAttachlist
	 * @return
	 * @throws XmcException
	 */
	public static String SaveNews(String sessionId, String fidXml,
			String fidAttachlist) throws XmcException {
		String returnValueString = "";
		String[] versionStrings = DeviceInfoHelper
				.getAppVersionName(IngleApplication
						.getInstance());
		String mainAppVersion;
		if (versionStrings != null) {
			mainAppVersion = versionStrings[1];
		} else {
			mainAppVersion = "1.101";
		}
		try {
			PostParameter[] postParameters = new PostParameter[] {
					new PostParameter("ua", "process.savenews"),
					new PostParameter("sss", sessionId),
					new PostParameter("fid-xml", fidXml),
					new PostParameter("fid-attachlist", fidAttachlist),
					new PostParameter("vMainApp", mainAppVersion),
					new PostParameter("vDbDesign", "1.101"),
					new PostParameter("vDbConfig", "1.101") };

			returnValueString = httpClient.doPost(postParameters, Configuration
					.getBaseUrl(), MANUSCRIPT_TIMEOUT);
		} catch (Exception e) {
			exceptionTypeJudge(e);
		}

		return returnValueString;
	}

	/**
	 * @Description ��ȡ�û�Ȩ�ޡ������ַ���û���Ϣ
	 * @param sessionId
	 * @param loginName
	 * @return 0||XML
	 * @throws XmcException
	 */
	public static String GetUserInfo(String sessionId, String loginName)
			throws XmcException {
		String returnValueString = "";
		try {
			PostParameter[] getParameters = new PostParameter[] {
					new PostParameter("ua", "get.userinfo"),
					new PostParameter("sss", sessionId),
					new PostParameter("loginname", loginName) };

			returnValueString = httpClient.doGet(getParameters, Configuration
					.getBaseUrl(), NORMAL_TIMEOUT);
		} catch (Exception e) {
			exceptionTypeJudge(e);
		}

		return returnValueString;
	}

	/**
	 * @Description ��ȡ��Ҫ���µĻ�����б�
	 * @param topicList
	 *            ���л���ݵ�XML�ļ����ļ����ö��ŷָ�����{"filename,filename,filename"}
	 * @return ��Ҫ���и��µ��б?�ö��ŷָ�����{"filename,filename,filename"}
	 * @throws XmcException
	 */
	public static String GetTopicList(String topicList) throws XmcException {
		String returnValueString = "";
		try {
			PostParameter[] postParameters = new PostParameter[] {
					new PostParameter("ua", "gettopiclist"),
					new PostParameter("topiclist", topicList) };

			returnValueString = httpClient.doPost(postParameters, Configuration
					.getSinosoftUrl(), NORMAL_TIMEOUT);
		} catch (Exception e) {
			exceptionTypeJudge(e);
		}
		return returnValueString;
	}

	/**
	 * ��ȡ�Ƽ�����ѵ�½������
	 * 
	 * @param entranceServerUrl
	 *            ������ڵ�ַ(Ĭ������һ����Ҳ���ڵ�½ҳ������)
	 * @return
	 * @throws XmcException
	 */
	public static String GetAppServerIP(String entranceServerUrl)
			throws XmcException {
		String returnValueString = "";
		try {
			PostParameter[] postParameters = new PostParameter[] { new PostParameter(
					"ua", "get.ipnodeaddress") };
			returnValueString = httpClient.doGet(postParameters,
					entranceServerUrl, NORMAL_TIMEOUT);
		} catch (Exception e) {
			exceptionTypeJudge(e);
		}
		// return
		// httpClient.doGet(postParameters,Configuration.getInitialUrl());
		return returnValueString;
	}

	/**
	 * @Description ��ȡĳ�û��ĸ�ǩģ��
	 * @param sessionId
	 * @param loginName
	 * @return ��ǩģ���б�
	 * @throws XmcException
	 */
	public static List<ManuscriptTemplate> GetTemplate(String sessionId,
			String loginName) throws XmcException {
		List<ManuscriptTemplate> returnValue = null;

		try {
			PostParameter[] postParameters = new PostParameter[] {
					new PostParameter("ua", "gettemplate"),
					new PostParameter("sss", sessionId),
					new PostParameter("loginname", loginName) };

			String JSONResult = httpClient.doPost(postParameters, Configuration
					.getSinosoftUrl(), NORMAL_TIMEOUT);

			returnValue = XMLDataHandle.getTemplatesFromJson(JSONResult);
		} catch (XmlPullParserException e) {
			exceptionTypeJudge(e);
		} catch (IOException e) {
			exceptionTypeJudge(e);
		} catch (Exception e) {
			exceptionTypeJudge(e);
		}

		return returnValue;
	}

	/**
	 * @Description ͨ��http���󣬷��ؿͻ�����ƥ��ķ�����IP
	 * @return
	 * @throws XmcException
	 */
	public static String[] GetServerURL() throws XmcException {

		String[] returnValue = null;
		try {
			PostParameter[] getParameters = new PostParameter[] { new PostParameter(
					"ua", "getserverurl") };

			String JSONResult = httpClient.doGet(getParameters, Configuration
					.getSinosoftUrl(), NORMAL_TIMEOUT);
			if (StringUtils.isNotBlank(JSONResult)) {
				JSONObject obj = new JSONObject(JSONResult);
				returnValue = obj.get("serverIp").toString().split(",");
			}
		} catch (JSONException e) {
			exceptionTypeJudge(e);
		} catch (Exception e) {
			exceptionTypeJudge(e);
		}

		return returnValue;
	}

	/**
	 * ��ȡOA��Ϣ�б�
	 * 
	 * @param sessionId
	 * @param limit
	 *            Ҫ��ȡ����Ϣ����
	 * @param start
	 *            �ӵڼ�����ʼ��ȡ
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<MessageForOa> getMessageListForOa(String sessionId,
			int limit, int start) throws XmcException {

		ArrayList<MessageForOa> returnValue = null;
		try {
			PostParameter[] postParameters = new PostParameter[] {
					new PostParameter("ua", "getOainfoList"),
					new PostParameter("sss", sessionId),
					new PostParameter("limit", limit),
					new PostParameter("id", start) };

			// TimeFormatHelper.convertLongToDate("0000000000000");
			// String
			// JSONResult=httpClient.doPost(postParameters,"http://202.84.17.51/ms/oainfo_list.do",NORMAL_TIMEOUT);

			String JSONResult = httpClient.doGet(postParameters, Configuration
					.getOAInfoListUrl(), NORMAL_TIMEOUT);
			if (StringUtils.isNotBlank(JSONResult)) {

				returnValue = new ArrayList<MessageForOa>();

				JSONObject jb = new JSONObject(JSONResult);

				JSONArray arr = new JSONArray(jb.getJSONArray("infoList")
						.toString());

				setMsgForOa_totalCount(jb.getInt("totalCount"));

				for (int i = 0; i < arr.length(); i++) {

					MessageForOa msg = new MessageForOa();
					JSONObject obj = arr.getJSONObject(i);
					msg.setId(obj.getInt("id"));
					msg.setInfo_title(obj.getString("info_title"));
					msg.setWriter(obj.getString("writer"));
					msg.setDepartment(obj.getString("department"));
					msg.setPublishtime(obj.getString("publishtime"));
					msg.setInfocontent(obj.getString("infocontent"));

					returnValue.add(msg);
				}
			}
		} catch (Exception e) {
			exceptionTypeJudge(e);
		}

		return returnValue;
	}

	/**
	 * �����Ϣid��ȡOA��Ϣ
	 * 
	 * @param sessionId
	 * @param id
	 *            ��Ϣ����id
	 * @return
	 * @throws Exception
	 */
	public static MessageForOa getMessageForOa(String sessionId, int id)
			throws XmcException {
		MessageForOa msg = null;
		try {
			PostParameter[] postParameters = new PostParameter[] {
					new PostParameter("ua", "getOainfoById"),
					new PostParameter("sss", sessionId),
					new PostParameter("id", id) };

			// TimeFormatHelper.convertLongToDate("0000000000000");
			// String
			// JSONResult=httpClient.doPost(postParameters,"http://202.84.17.51/ms/oainfo_getInfoById.do",NORMAL_TIMEOUT);
			String JSONResult = httpClient.doGet(postParameters, Configuration
					.getOAInfoItemUrl(), NORMAL_TIMEOUT);
			if (StringUtils.isNotBlank(JSONResult)) {

				JSONObject obj = new JSONObject(JSONResult);
				msg = new MessageForOa();
				msg.setId(obj.getInt("id"));
				msg.setInfo_title(obj.getString("info_title"));
				msg.setWriter(obj.getString("writer"));
				msg.setDepartment(obj.getString("department"));
				msg.setPublishtime(obj.getString("publishtime"));
				msg.setInfocontent(obj.getString("infocontent"));
			}
		} catch (Exception e) {
			exceptionTypeJudge(e);
		}

		return msg;
	}

	/**
	 * ��ȡ��Ϣ
	 * 
	 * @param sessionId
	 * @param loginname
	 * @param lasttime
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<MessageForUs> getMessage(String sessionId,
			String loginname, String lasttime) throws XmcException {

		ArrayList<MessageForUs> returnValue = null;
		try {
			PostParameter[] postParameters = new PostParameter[] {
					new PostParameter("ua", "getmessage"),
					new PostParameter("sss", sessionId),
					new PostParameter("loginname", loginname),
					new PostParameter("lasttime", lasttime) };

			TimeFormatHelper.convertLongToDate("0000000000000");

			String JSONResult = httpClient.doPost(postParameters, Configuration
					.getSinosoftUrl(), NORMAL_TIMEOUT);
			if (StringUtils.isNotBlank(JSONResult)) {

				returnValue = new ArrayList<MessageForUs>();

				JSONArray arr = new JSONArray(JSONResult);

				for (int i = 0; i < arr.length(); i++) {

					MessageForUs msg = new MessageForUs();
					JSONObject obj = arr.getJSONObject(i);
					msg.setId(String.valueOf(obj.getInt("id")));
					msg.setMsgContent(obj.getString("msgContent"));
					msg.setMsgOwner(obj.getString("msgOwner"));
					msg.setMsgOwnerType(MsgOwnerType.parseFromValue(String
							.valueOf(obj.getInt("msgOwnerType"))));
					msg.setMsgSendOrReceiveTime(obj.getString("msgSendTime"));
					msg.setMsgFrom(obj.getString("msgFrom"));
					msg.setMsgType(MessageType.parseFromValue(String
							.valueOf(obj.getInt("msgType"))));

					returnValue.add(msg);
				}
			}
		} catch (Exception e) {
			exceptionTypeJudge(e);
		}

		return returnValue;
	}

	/**
	 * ��ȡ��Ϣ
	 * 
	 * @param sessionId
	 * @param loginname
	 * @param lasttime
	 * @return
	 * @throws Exception
	 */
	public static boolean sendMessage(String sessionId, MessageType msgType,
			String msgOwner, MsgOwnerType msgOwnerType, String msgContent)
			throws XmcException {

		boolean returnValue = false;
		try {
			PostParameter[] postParameters = new PostParameter[] {
					new PostParameter("ua", "sendmessage"),
					new PostParameter("sss", sessionId),
					new PostParameter("msgtype", msgType.getValue()),
					new PostParameter("msgowner", msgOwner),
					new PostParameter("msgownertype", msgOwnerType.getValue()),
					new PostParameter("msgcontent", msgContent) };

			String result = httpClient.doGet(postParameters, Configuration
					.getSinosoftUrl(), NORMAL_TIMEOUT);

			returnValue = "0\n".equals(result) ? true : false;

		} catch (Exception e) {
			exceptionTypeJudge(e);
		}

		return returnValue;
	}

	/**
	 * ��ȡ�������ϵ�Android�ͻ��˷���汾����Ҫ�� ����0.2,��android�ͻ��˰汾�ȶԣ����ڵ���0.2�İ汾�����Է���
	 * 0_VersionLowest 1_VersionCurrent
	 * 
	 * @throws XmcException
	 */
	public static String[] getAndroidAppVersion() throws XmcException {
		String[] returnValue = null;

		try {
			PostParameter[] postParameters = new PostParameter[] { new PostParameter(
					"ua", "get.versioncontrol") };

			String xmlString = httpClient.doGet(postParameters, Configuration
					.getBaseUrl(), NORMAL_TIMEOUT);

			if (StringUtils.isNotBlank(xmlString)) {
				returnValue = XMLDataHandle.getAndroidAppVersion(xmlString);
			}
		} catch (Exception e) {
			exceptionTypeJudge(e);
		}

		return returnValue;
	}

	/**
	 * �����������֮��ĻỰ״̬(SessionId������)
	 * 
	 * @param sessionId
	 * @throws XmcException
	 */
	public static void KeepAlive(String sessionId) throws XmcException {
		try {
			PostParameter[] postParameters = new PostParameter[] {
					new PostParameter("ua", "sys.keepalive"),
					new PostParameter("sss", sessionId) };

			httpClient.doPost(postParameters, Configuration.getBaseUrl(),
					NORMAL_TIMEOUT);
		} catch (Exception e) {
			exceptionTypeJudge(e);
		}
	}

	/**
	 * ����OA��Ϣ��ǰ������
	 * 
	 * @param msgForOa_totalCount
	 */
	public static void setMsgForOa_totalCount(int msgForOa_totalCount) {
		RemoteCaller.msgForOa_totalCount = msgForOa_totalCount;
	}

	/**
	 * ����OA��Ϣ��ǰ������
	 * 
	 * @return
	 */
	public static int getMsgForOa_totalCount() {
		return msgForOa_totalCount;
	}

	/**
	 * �쳣�ж�
	 * 
	 * @param exception
	 * @throws XmcException
	 */
	private static void exceptionTypeJudge(Exception exception)
			throws XmcException {
		Logger.e(exception);
		if (exception.getMessage().equals(
				XmcException.CONNECT_TIMEOUT_EXCEPTION)
				|| exception.getMessage().equals(
						XmcException.INTERRUPTED_IO_EXCEPTION)) {
			throw new XmcException(XmcException.TIMEOUT);
		} else {
			throw new XmcException(exception.getMessage());
		}
	}
	/**
	 * У�鵱ǰ��¼�û����޼��ļ�����Ȩ��
	 * sss=sessionid
     * loginname=username�û���¼��
     * return: {'status':'true/false','address':'hkhgc,hkhge,mxmss,nbbff'}
	 */
	public static boolean validateInstantUpload(String sessionId,String loginName) throws XmcException {

		String status = null;
		
		try {
			PostParameter[] postParameters = new PostParameter[] {
					new PostParameter("ua", "isValidate"),
					new PostParameter("sss", sessionId),
					new PostParameter("loginname", loginName), 
					};

			String jsonResult=httpClient.doGet(postParameters,Configuration.getInstantUploadUrl(),NORMAL_TIMEOUT);
			//String jsonResult="{'status':'true','address':'hkhgc,hkhge,mxmss,nbbff'}";
			//String jsonResult="{'status':'true','address':'hqfsh,mxzfs'}";
			//{'status':'false','starttime':'','endtime':'','address':''}//
			//http://202.84.17.51/m/client/i?ua=isValidate&loginname=testapple&sss=ac7ca2be9adbc3d59a511f534a18cbbfc6b27099
			
			String[] address = null;
			addressAll=null;
			if (StringUtils.isNotBlank(jsonResult)) {
				JSONObject obj = new JSONObject(jsonResult);
				status = obj.get("status").toString();
				
				if("".equals(obj.get("address").toString())==false){
					addressAll=new ArrayList<String>();
					address = obj.get("address").toString().split(",");
					for(int i=0;i<address.length;i++)
					{
						addressAll.add(address[i]);
					}
				}
			}
			
		} catch(Exception e){
			exceptionTypeJudge(e);
		}

		return Boolean.parseBoolean(status);
	}
}
