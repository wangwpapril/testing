package com.cuc.miti.phone.xmc.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Accessories;
import com.cuc.miti.phone.xmc.domain.CommonXMLData;
import com.cuc.miti.phone.xmc.domain.Enums.TemplateType;
import com.cuc.miti.phone.xmc.domain.Enums.XmcBool;
import com.cuc.miti.phone.xmc.domain.ManuscriptTemplate;
import com.cuc.miti.phone.xmc.domain.User;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.domain.SendToAddress;
import com.cuc.miti.phone.xmc.domain.UserAttribute;
import com.cuc.miti.phone.xmc.domain.Enums.AccessoryType;
import com.cuc.miti.phone.xmc.domain.Manuscripts;
import com.cuc.miti.phone.xmc.utils.StandardizationDataHelper;

import android.util.Xml;

public class XMLDataHandle {

	/**
	 * ��������ݵ�xml�ļ�����ȡ���������XML����List
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static List<CommonXMLData> parser(InputStream is) throws Exception {
		List<CommonXMLData> cxmList = null;
		CommonXMLData cxmInfo = null;
		// Hashtable<String, String> languageList = null;
		List<KeyValueData> languageList = null;
		List<KeyValueData> descriptionList = null;
		// KeyValueData keyValueData=null;

		// ��android.util.Xml����һ��XmlPullParserʵ��
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "UTF-8");

		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				cxmList = new ArrayList<CommonXMLData>();
				break;
			case XmlPullParser.START_TAG:
				if (parser.getName().equals("Topic")) {
					cxmInfo = new CommonXMLData();
					// languageList = new Hashtable<String, String>();

					languageList = new ArrayList<KeyValueData>();
					descriptionList = new ArrayList<KeyValueData>();

					cxmInfo.setTopicId(parser.getAttributeValue("", "topicId"));
					cxmInfo.setId(parser.getAttributeValue("", "id"));
					cxmInfo.setDeleteFlag(parser.getAttributeValue("",
							"deleteFlag"));
				} else if (parser.getName().equals("Name")) {

					KeyValueData keyValueData = new KeyValueData();

					String temp = parser.getAttributeValue(0); // ȡ��xml:lang��ֵ
					eventType = parser.next();

					keyValueData.setKey(temp);
					keyValueData.setValue(parser.getText().toString());

					languageList.add(keyValueData);

					// languageList.put(temp, parser.getText().toString());
				} else if (parser.getName().equals("Description")) {

					KeyValueData keyValueData = new KeyValueData();

					String temp = parser.getAttributeValue(0); // ȡ��xml:lang��ֵ
					eventType = parser.next();

					keyValueData.setKey(temp);
					keyValueData.setValue(parser.getText().toString());

					descriptionList.add(keyValueData);

				}
				break;
			case XmlPullParser.END_TAG:
				if (parser.getName().equals("Topic")) {
					cxmInfo.setLanguageList(languageList);
					cxmInfo.setDescriptionList(descriptionList);
					cxmList.add(cxmInfo);
					cxmInfo = null;
					languageList = null;
				}
				break;
			}
			eventType = parser.next();
		}
		return cxmList;
	}
	
	/**
	 * ��������ݵ�xml�ļ�����ȡ���������XML����List
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static List<CommonXMLData> parserSendtoAddress(InputStream is) throws Exception {
		List<CommonXMLData> cxmList = null;
		CommonXMLData cxmInfo = null;
		List<KeyValueData> languageList = null;
		
		// ��android.util.Xml����һ��XmlPullParserʵ��
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "UTF-8");

		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				cxmList = new ArrayList<CommonXMLData>();
				break;
			case XmlPullParser.START_TAG:
				if (parser.getName().equals("Topic")) {
					cxmInfo = new CommonXMLData();
					cxmInfo.setTopicId(parser.getAttributeValue("", "topicId"));
					cxmInfo.setDeleteFlag(parser.getAttributeValue("","deleteFlag"));
				} else if (parser.getName().equals("Name")) {
					eventType = parser.next();
					cxmInfo.setName(parser.getText().toString());
				} else if (parser.getName().equals("Description")) {
					String tempValue = parser.getAttributeValue(0); // ȡ��kind��ֵ
					if(tempValue.equals("Order")){
						eventType = parser.next();
						cxmInfo.setOrder(parser.getText());
					}else if(tempValue.equals("Language")){
						eventType = parser.next();
						languageList = new ArrayList<KeyValueData>();
						KeyValueData language = new KeyValueData();
						language.setKey(parser.getText());
						language.setValue(parser.getText());
						languageList.add(language);
						cxmInfo.setLanguageList(languageList);
					}else{}
				}
				break;
			case XmlPullParser.END_TAG:
				if (parser.getName().equals("Topic")) {
					cxmInfo.setLanguageList(languageList);
					cxmList.add(cxmInfo);
					cxmInfo = null;
					languageList = null;
				}
				break;
			}
			eventType = parser.next();
		}
		return cxmList;
	}
	/**
	 * By GuanWei
	 * ����Ftp�û����xml�ļ�����ȡ���������XML����List
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static List<KeyValueData> parserFtpUsername(InputStream is) throws Exception {
		List<KeyValueData> usernameList = null;
		KeyValueData username = null;
		// ��android.util.Xml����һ��XmlPullParserʵ��
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "UTF-8");
		int eventType = parser.getEventType();
		
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				usernameList = new ArrayList<KeyValueData>();
				break;
			case XmlPullParser.START_TAG:
				if("person".equalsIgnoreCase(parser.getName())){
					username = new KeyValueData();
					break;
				}//parser.getAttributeValue(0
				if(username != null){
					if("identification".equalsIgnoreCase(parser.getName())){
						username.setKey(parser.nextText().toString());
					}else if("ftpuser".equalsIgnoreCase(parser.getName())){
						username.setValue(parser.nextText().toString());
					}
					break;
				}
				break;
			case XmlPullParser.END_TAG:
				if (parser.getName().equals("person")) {
					usernameList.add(username);
					username = null;
				}
				break;
			default:
				break;
			}
			eventType = parser.next();
		}
		if(is != null){
			is.close();
		}
		return usernameList;
	}
	public static UserAttribute ParserUserInfo(InputStream is) throws Exception {

		UserAttribute userAttribute = null;
		ArrayList<SendToAddress> addressList = null;
		//Hashtable<String, String> transferAddressList = null;
		ArrayList<KeyValueData> transferAddressList = null;

		// ��android.util.Xml����һ��XmlPullParserʵ��
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "UTF-8");

		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				userAttribute = new UserAttribute();

				break;
			case XmlPullParser.START_TAG:
				String name = parser.getName();
				if ("ChnFullname".equals(name)) {
					userAttribute.setUserNameC(parser.nextText());
				} else if ("EngFullName".equals(name)) {
					userAttribute.setUserNameE(parser.nextText());
				} else if ("GroupId".equals(name)) {
					userAttribute.setGroupCode(parser.nextText());
				} else if ("ChnGroupName".equals(name)) {
					userAttribute.setGroupNameC(parser.nextText());
				} else if ("EngGroupName".equals(name)) {
					userAttribute.setGroupNameE(parser.nextText());
				} else if ("Disabled".equals(name)) {
					boolean disable = Boolean.parseBoolean(parser.nextText());
					userAttribute.setRightDisabled(disable);
				} else if ("SendNews".equals(name)) {
					boolean sendNews = Boolean.parseBoolean(parser.nextText());
					userAttribute.setRightSendNews(sendNews);
				} else if ("TransferNews".equals(name)) {
					boolean transferNews = Boolean.parseBoolean(parser.nextText());
					userAttribute.setRightTransferNews(transferNews);
				} else if ("ReleNews".equals(name)) {
					boolean releNews = Boolean.parseBoolean(parser.nextText());
					userAttribute.setRightReleNews(releNews);
				} else if ("AddressList".equals(name)) {

					addressList = new ArrayList<SendToAddress>();
					eventType = parser.next();

					for (int i = parser.getEventType();; i = parser.next()) {

						if ((i == XmlPullParser.END_TAG && parser.getName()
								.equals("AddressList"))) {
							userAttribute.setAddressList(addressList);
							break;
						} else if (i == XmlPullParser.START_TAG && parser.getName().equals("item")) {
							SendToAddress sta = new SendToAddress();
							sta.setOrder(parser.getAttributeValue(0));
							sta.setCode(parser.getAttributeValue(1));
							sta.setLanguage(parser.getAttributeValue(2));
							sta.setType(parser.getAttributeValue(3));
							sta.setName(parser.getText());

							addressList.add(sta);
						}
					}

				} else if ("TransferAddressList".equals(name)) {

					transferAddressList = new ArrayList<KeyValueData>();
					eventType = parser.next();
					for (int i = parser.getEventType();; i = parser.next()) {

						if (i == XmlPullParser.END_TAG&& parser.getName().equals("TransferAddressList")) {
							userAttribute.setTransferAddressList(transferAddressList);
							break;
						} else if (i == XmlPullParser.START_TAG && parser.getName().equals("item")) {
							KeyValueData data = new KeyValueData();
							data.setKey(parser.getAttributeValue(0));
							parser.next();
							data.setValue(parser.getText());
							transferAddressList.add(data);
						}
					}
				}
				break;
			case XmlPullParser.END_TAG:

				break;
			}
			eventType = parser.next();
		}
		userAttribute.initializeChoosedAddressList();

		return userAttribute;
	}

	/**
	 * �������Ϣ���л���SD��
	 * 
	 * @param manuscript
	 *            ��ֺ�ĵ����������
	 * @param accessories
	 *            �ø������ĸ���
	 * @param fileName
	 *            ���XML���ļ���
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 */
	public static String Serializer(Manuscripts manuscript,
			Accessories accessories, String fileName)
			throws FileNotFoundException, IOException,
			IllegalArgumentException, IllegalStateException {

		DeviceInfoHelper deviceInfoHelper = new DeviceInfoHelper();
		// ��ȡϵͳ��ʱ�ļ�Ŀ¼
		String path = StandardizationDataHelper
				.getAccessoryFileUploadStorePath(AccessoryType.Cache);

		if (path == "")

			return null;
		// xmlĿ¼ �ж�XML�ļ��Ƿ��Ѿ����ڣ����������һ���յ�
		File newxmlfile = new File(path + "/" + fileName + ".xml");

		try {
			if (!newxmlfile.exists())
				newxmlfile.createNewFile();
		} catch (IOException e) {
			throw e;
		}

		// ����ļ�д��������׼��д��xml��Ϣ
		FileOutputStream fileos = null;

		try {
			fileos = new FileOutputStream(newxmlfile);
		} catch (FileNotFoundException e) {
			throw e;
		}

		// ���xml���
		XmlSerializer serializer = Xml.newSerializer();

		try {
			serializer.setOutput(fileos, "UTF-8");

			serializer.startDocument(null, null);

			serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output",true);

			serializer.startTag(null, "eNews");

			serializer.attribute(null, "version", "v001");

			// 0.01���巽ʽ��ϵͳ��ʶ============================================
			MakeNode(serializer, "SystemId", "eNews.MobileClient.AndroidPhone", null);
				
			// ����˵�IP���ֻ����================================================
			ArrayList<KV> arr = new ArrayList<KV>();
			KV item = new KV("item", "IP^CellphoneNo");
			arr.add(item);
			MakeNode(serializer, "SendFrom", deviceInfoHelper.getPhoneNumberString(), arr);

			// 0.02�����˵��ʺ���Ϣ===============================================
			User user = IngleApplication.getInstance().currentUserInfo;
			if (user != null) {
				serializer.startTag(null, "Uploader");
				MakeNode(serializer, "login", user.getUsername(), null);
				MakeNode(serializer, "cname", user.getUserattribute()
						.getUserNameC(), null);
				MakeNode(serializer, "ename", user.getUserattribute()
						.getUserNameE(), null);
				MakeNode(serializer, "groupid", user.getUserattribute()
						.getGroupCode(), null);
				MakeNode(serializer, "cgroup", user.getUserattribute()
						.getGroupNameC(), null);
				MakeNode(serializer, "egroup", user.getUserattribute()
						.getGroupNameE(), null);
				// TODO uploader��ID��δ֪
				MakeNode(serializer, "id", String.valueOf(user.getU_id()), null);
				serializer.endTag(null, "Uploader");
			}

			// ����ʱ�䣬��ʽ��������.ʱ����+ʱ��==================================
			serializer.startTag(null, "Time");
			// �������ʱ��
			MakeNode(serializer, "ReceiveTime", TimeFormatHelper.getGMTTime(manuscript.getReceiveTime()),	null);
			MakeNode(serializer, "ReleTime", TimeFormatHelper.getGMTTime(manuscript.getReletime()), null);
			MakeNode(serializer, "CreateTime", TimeFormatHelper.getGMTTime(manuscript.getCreatetime()),null);
			//		.getManuscriptTemplate().getCreatetime()), null);
			serializer.endTag(null, "Time");
			
			// newsId	================================================================
			MakeNode(serializer, "NewsId", manuscript.getReleid(), null);
			// ����================================================================
			MakeNode(serializer, "Title", manuscript.getTitle(), null);
			// ����================================================================
			MakeNode(serializer, "Author", manuscript.getAuthor(), null);
			// �ؼ��==============================================================
			MakeNode(serializer, "Keywords", manuscript.getManuscriptTemplate()
					.getKeywords(), null);
			// ����================================================================
			arr = new ArrayList<KV>();
			item = new KV("id", manuscript.getManuscriptTemplate()
					.getLanguageID());
			arr.add(item);
			MakeNode(serializer, "Language", manuscript.getManuscriptTemplate()
					.getLanguage(), arr);

			// ���ȼ�===============================================================
			arr = new ArrayList<KV>();
			item = new KV("id", manuscript.getManuscriptTemplate()
					.getPriorityID());
			arr.add(item);
			MakeNode(serializer, "Priority", manuscript.getManuscriptTemplate()
					.getPriority(), arr);

			// �������==============================================================
			arr = new ArrayList<KV>();
			item = new KV("id", manuscript.getManuscriptTemplate()
					.getProvtypeID());
			arr.add(item);
			MakeNode(serializer, "ProvType", manuscript.getManuscriptTemplate()
					.getProvtype(), arr);

			// ����=================================================================
			arr = new ArrayList<KV>();
			item = new KV("id", manuscript.getManuscriptTemplate()
					.getDoctypeID());
			arr.add(item);
			MakeNode(serializer, "DocType", manuscript.getManuscriptTemplate()
					.getDoctype(), arr);

			// ��Դ
			// SourceInfo=======================================================
			String[] comefromDepts = manuscript.getManuscriptTemplate()
					.getComefromDept().split(",");
			String[] comefromDeptIds = manuscript.getManuscriptTemplate()
					.getComefromDeptID().split(",");

			serializer.startTag(null, "SourceInfo");
			serializer.attribute(null, "count",
					Integer.toString(comefromDepts.length));

			// ѭ����ɸ�Դ
			for (int i = 0; i < comefromDepts.length; i++) {
				arr = new ArrayList<KV>();
				item = new KV("id", comefromDeptIds[i]);
				arr.add(item);

				item = new KV("sn", Integer.toString(i + 1));
				arr.add(item);

				MakeNode(serializer, "item", comefromDepts[i], arr);
			}
			serializer.endTag(null, "SourceInfo");

			// ���ֵص���Ϣ========================================================
			serializer.startTag(null, "Locations");
			// �·��ص�
			MakeNode(serializer, "HappenPlace", manuscript
					.getManuscriptTemplate().getHappenplace(), null);
			// �����ص�
			MakeNode(serializer, "ReportPlace", manuscript
					.getManuscriptTemplate().getReportplace(), null);
			// ����ص�^����ص�
			MakeNode(serializer, "SendArea", manuscript.getManuscriptTemplate()
					.getSendarea(), null);
			// ����
			arr = new ArrayList<KV>();
			item = new KV("id", manuscript.getManuscriptTemplate()
					.getRegionID());
			arr.add(item);
			MakeNode(serializer, "Region", manuscript.getManuscriptTemplate()
					.getRegion(), arr);
			serializer.endTag(null, "Locations");

			// �����ַ=========================================================
			String[] address = manuscript.getManuscriptTemplate().getAddress()
					.split(",");
			String[] addressIds = manuscript.getManuscriptTemplate()
					.getAddressID().split(",");

			serializer.startTag(null, "SendTo");
			serializer.attribute(null, "count",
					Integer.toString(address.length));
			// 3T���
			serializer.attribute(null, "be3t", manuscript
					.getManuscriptTemplate().getIs3Tnews().toString()
					.toLowerCase());
			// ѭ����ɷ����ַ
			for (int i = 0; i < address.length; i++) {
				arr = new ArrayList<KV>();
				item = new KV("id", addressIds[i]);
				arr.add(item);

				item = new KV("sn", Integer.toString(i + 1));
				arr.add(item);

				MakeNode(serializer, "item", address[i], arr);
			}
			serializer.endTag(null, "SendTo");

			// ����==============================================================

			serializer.startTag(null, "Attach");
			if (accessories == null)
				serializer.attribute(null, "count", "0");
			else {
				serializer.attribute(null, "count", "1");

				arr = new ArrayList<KV>();
				item = new KV("sn", "1");
				arr.add(item);
				//item = new KV("info", accessories.getInfo());
				//TODO �ļ���Ϣ
				item = new KV("info", "no informations");
				arr.add(item);
				item = new KV("origin", accessories.getOriginalName());
				arr.add(item);
				item = new KV("size", accessories.getSize());
				arr.add(item);

				int index = accessories.getUrl().lastIndexOf(".");
				String type = accessories.getUrl().substring(index);
				MakeNode(serializer, "item", "1" + type, arr);
			}
			serializer.endTag(null, "Attach");

			// ����==============================================================
			serializer.startTag(null, "Contents");

			serializer.startTag(null, "Text");
			serializer.attribute(null, "wordcount",
					Integer.toString(manuscript.getContents().length()));
			serializer.cdsect(manuscript.getContents());
			serializer.endTag(null, "Text");

			serializer.endTag(null, "Contents");
			
			
			// ����״̬��Ϣ================================================================
			MakeNode(serializer, "ReviewStatus", manuscript.getManuscriptTemplate().getReviewstatus(), null);
						
			// ����λ��GPS��Ϣ================================================================
			MakeNode(serializer, "GeographyPosition", manuscript.getLocation(), null);

			serializer.endTag(null, "eNews");

			serializer.endDocument();

			// ���XML��ݣ����ر��ļ�д����
			serializer.flush();
			fileos.close();
			
			return newxmlfile.getPath();

		} catch (IllegalArgumentException e) {
			throw e;
		} catch (IllegalStateException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
	}

	/**
	 * xml�ڵ㹹�캯��
	 * 
	 * @param serializer
	 * @param name
	 * @param value
	 * @param attributes
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private static void MakeNode(XmlSerializer serializer, String name,
			String value, List<KV> attributes) throws IllegalArgumentException,
			IllegalStateException, IOException {

		serializer.startTag(null, name);

		if (attributes != null) {
			for (KV attribute : attributes) {
				serializer.attribute(null, attribute.getKey(),
						attribute.getValue());
			}
		}
		serializer.text(value);
		serializer.endTag(null, name);
	}
	/**
	 * By GuanWei  ����FTP�û��� 
	 * �������Ϣ���л���SD��
	 * 
	 * @param manuscript
	 *            ��ֺ�ĵ����������
	 * @param accessories
	 *            �ø������ĸ���
	 * @param fileName
	 *            ���XML���ļ���
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 */
	public static String Serializer(String fileName,List<KeyValueData> objs)
			throws FileNotFoundException, IOException,
			IllegalArgumentException, IllegalStateException {
		// ��ȡϵͳ��ʱ�ļ�Ŀ¼
		String path = StandardizationDataHelper
				.getAccessoryFileUploadStorePath(AccessoryType.Cache);

		if (path == "")
			return null;
		// xmlĿ¼ �ж�XML�ļ��Ƿ��Ѿ����ڣ����������һ���յ�
		File newxmlfile = new File(path + "/" + fileName + ".xml");

		try {
			if (!newxmlfile.exists())
				newxmlfile.createNewFile();
		} catch (IOException e) {
			throw e;
		}

		// ����ļ�д��������׼��д��xml��Ϣ
		FileOutputStream fileos = null;

		try {
			fileos = new FileOutputStream(newxmlfile);
		} catch (FileNotFoundException e) {
			throw e;
		}

		// ���xml���
		XmlSerializer serializer = Xml.newSerializer();

		try {
			serializer.setOutput(fileos, "UTF-8");

			serializer.startDocument(null, null);

			serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output",true);

			serializer.startTag(null, "persons");

			serializer.attribute(null, "version", "v001");

			//			By SQ
//			Hashtable<String,String> hs = new Hashtable<String, String>();
//			
//			
//			hs				
//          ����GW/��ޱ
//			ArrayList<KV> arr = new ArrayList<KV>();		
//			KV item = new KV("GW", "��ޱ");
//			arr.add(item);						
			
			List<KeyValueData> list = objs;
			if(list != null && list.size() >0){
				for(int i=0;i<list.size();i++){
					serializer.startTag(null, "person");
					KeyValueData data = null;
					data = list.get(i);
					MakeNodeX(serializer, "identification", data.getKey(), null);
					MakeNodeX(serializer, "ftpuser", data.getValue(), null);
					serializer.endTag(null, "person");
				}	
			}
			serializer.endTag(null, "persons");

			serializer.endDocument();

			// ���XML��ݣ����ر��ļ�д����
			serializer.flush();
			fileos.close();
			
			return newxmlfile.getPath();

		} catch (IllegalArgumentException e) {
			throw e;
		} catch (IllegalStateException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
	}
	//By GuanWei
		private static void MakeNodeX(XmlSerializer serializer, String name,
				String value, List<KeyValueData> attributes) throws IllegalArgumentException,
				IllegalStateException, IOException {

			serializer.startTag(null, name);

			if (attributes != null) {
				for (KeyValueData attribute : attributes) {
					serializer.attribute(null, attribute.getKey(),
							attribute.getValue());
				}
			}
			serializer.text(value);
			serializer.endTag(null, name);
		}
	/**
	 * XML�ڵ���󣨼�ֵ�ԣ�
	 * 
	 * @author wenyujun
	 * 
	 */
	static class KV {
		String key;
		String value;

		public KV(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return this.key;
		}

		public String getValue() {
			return this.value;
		}
	}
	
	/**
	 * �ӷ��������ص�json���л�ȡ�û��������˵ĸ�ǩģ���б�
	 * @param jsonString
	 * @return
	 * @throws IOException 
	 * @throws XmlPullParserException 
	 */
	@SuppressWarnings("null")
	public static List<ManuscriptTemplate> getTemplatesFromJson(String jsonString) throws XmlPullParserException, IOException{
		List<ManuscriptTemplate> maList= null;
		if(StringUtils.isNotBlank(jsonString)){
			try {
				maList = new ArrayList<ManuscriptTemplate>();		
				JSONArray arr = new JSONArray(jsonString);
				for(int i=0;i<arr.length();i++){
					
					ManuscriptTemplate mTemplate=new ManuscriptTemplate();
					JSONObject obj=arr.getJSONObject(i);					
					String tempcontent = obj.getString("templateContent");
					mTemplate = getTemplateFormXML(tempcontent);
					mTemplate.setIsdefault(XmcBool.parseFromValue(obj.getString("beDefault")));
					mTemplate.setName(obj.getString("templateDescription"));
					mTemplate.setMt_id(obj.getString("templateId"));
					mTemplate.setIssystemoriginal(TemplateType.NORMAL.toString());
					
					maList.add(mTemplate);
				}

			} catch (JSONException e) {
				Logger.e(e);
				e.printStackTrace();
			}
		}
		
		
		return maList;
	}
	
	/**
	 * �����ӷ������˻�ȡ��Android�ͻ��˰汾����XML
	 * @param xmlString
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static String[] getAndroidAppVersion(String xmlString) throws XmlPullParserException, IOException{
		String versionString[] = null;
		InputStream is = new ByteArrayInputStream(xmlString.getBytes());
		// ��android.util.Xml����һ��XmlPullParserʵ��
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "UTF-8");
		
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					versionString = new String[]{"0","0",""};
					break;
				case XmlPullParser.START_TAG:
					String name = parser.getName();
					if ("AndroidClient".equals(name)) {
						eventType = parser.next();
					} else if ("VersionLowest".equals(name)) {
						eventType = parser.next();
						versionString[0] = parser.getText();
					} else if ("VersionCurrent".equals(name)) {
						eventType = parser.next();
						versionString[1] = parser.getText();
					} else if ("url".equals(name)) {
						eventType = parser.next();
						versionString[2] = parser.getText();
					} else{}
				case XmlPullParser.END_TAG:
					name = parser.getName();				//��ȡAndroidClient�Ľڵ�����ʶ�����
					if ("AndroidClient".equals(name)) {
						eventType = XmlPullParser.END_DOCUMENT;
					}
					break;
				}
			if(eventType!=XmlPullParser.END_DOCUMENT){
				eventType = parser.next();
			}
		}
		
		return versionString;
	}
		
	/**
	 * �ӷ���˷���json���е�xml��������ǩ����
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 */
	private static ManuscriptTemplate getTemplateFormXML(String templateXML) throws XmlPullParserException, IOException{
		
		ManuscriptTemplate manuscriptTemplate =null;
		InputStream is = new ByteArrayInputStream(templateXML.getBytes());
		// ��android.util.Xml����һ��XmlPullParserʵ��
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "UTF-8");

		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				manuscriptTemplate=new ManuscriptTemplate();

				break;
			case XmlPullParser.START_TAG:
				String name = parser.getName();
				if ("Author".equals(name)) {
					manuscriptTemplate.setAuthor(parser.nextText());
				} else if ("Keywords".equals(name)) {
					manuscriptTemplate.setKeywords(parser.nextText());
				} else if ("ReviewStatus".equals(name)) {
					manuscriptTemplate.setReviewstatus(parser.nextText());
				} else if ("HappenPlace".equals(name)) {
					manuscriptTemplate.setHappenplace(parser.nextText());
				} else if ("Reportplace".equals(name)) {
					manuscriptTemplate.setReportplace(parser.nextText());
				} else if ("Priority".equals(name)) {
					manuscriptTemplate.setPriorityID((parser.getAttributeValue("", "id")));
					manuscriptTemplate.setPriority(parser.nextText());
				} else if ("Language".equals(name)) {
					manuscriptTemplate.setLanguageID((parser.getAttributeValue("", "id")));
					manuscriptTemplate.setLanguage(parser.nextText());
				} else if ("Provtype".equals(name)) {
					manuscriptTemplate.setProvtypeID((parser.getAttributeValue("", "id")));
					manuscriptTemplate.setProvtype(parser.nextText());
				} else if ("Doctype".equals(name)) {
					manuscriptTemplate.setDoctypeID((parser.getAttributeValue("", "id")));
					manuscriptTemplate.setDoctype(parser.nextText());
				} else if ("Releaddress".equals(name)) {
					manuscriptTemplate.setAddressID((parser.getAttributeValue("", "id")));
					manuscriptTemplate.setAddress(parser.nextText());
				} else if ("Region".equals(name)) {
					manuscriptTemplate.setRegionID((parser.getAttributeValue("", "id")));
					manuscriptTemplate.setRegion(parser.nextText());
				}else if(("Title").equals(name)){
					manuscriptTemplate.setDefaulttitle(parser.nextText());
				}
				else if(("Textbody").equals(name)){
					manuscriptTemplate.setDefaultcontents(parser.nextText());
				}
			case XmlPullParser.END_TAG:
				break;
			}
			eventType = parser.next();
		}
		return manuscriptTemplate;
	}
}
