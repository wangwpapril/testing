/**
 * 
 */
package com.cuc.miti.phone.xmc.domain;

import java.security.PublicKey;

import org.apache.http.impl.conn.DefaultClientConnection;

import com.cuc.miti.phone.xmc.utils.ToastHelper;
import com.cuc.miti.phone.xmc.R;

/**
 * @author SongQing
 *
 */
public class Enums {

	/**
	 * ����ϴ�����״̬(����ManuscriptStatus�е�StandTo����״̬)
	 * @author SongQing<p>
	 * 
	 * <������>�������ʽ��ʼ����ʱ���޸�������ݿ��е�״̬Ϊ������<p>
	 * <ʧ��>����������г�������쳣�����·���ֹͣ���޸�������ݿ��е�״̬Ϊ����ʧ�ܣ�
	 * ���͹���У��û������ͣ��ť�����˳�����Ҳ����ʧ�ܴ��?��һ�δ򿪰��նϵ�����<p>
	 * <�ȴ�>�������Ϊ��״̬��û�п�ʼ���͵�֮��Ĺ�̽����ȴ�<p>
	 * <���>����������<p>
	 * <ȡ��>�ϴ������˹�ȡ����������ص��༭״̬���������ڱ����б�<p>
	 */
	public enum UploadTaskStatus{
		Sending("������"),
		FailedRestart("ʧ�����ش�"),
		Paused("��ͣ"),
		Waiting("�ȴ�"),
		Finished("���"),
		FailedContinue("ʧ������"),
		Cancel("ȡ��"),
		Failed("ʧ��"),
		Restart("�ش�");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		UploadTaskStatus(String value) {
				this.svalue = value;
		}
	}
	/**
	 * ���״̬
	 * @author SongQing<p>
	 * <��>�����������ͣ�������֤ͨ��֮����޸�������ݿ��е�״̬Ϊ��<p>
	 * <�༭��>���½����������һֱ���������֮����һֱ���ڱ༭��״̬<p>
	 * <��̭>����ӱ༭��״̬����ת��Ϊ��̭<p>
	 * <�ѷ�>������ͳɹ�������ش���ţ���������ݿ�newsId�ֶκ͸��״̬Ϊ�ѷ�<p>
	 */
	public enum ManuscriptStatus{
		StandTo("��"), 
		Editing("�༭��"), 
		Elimination("��̭"),
		Sent("�ѷ�");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		ManuscriptStatus(String value) {
				this.svalue = value;
		}
	}
	
	/**
	 * �Զ��岼���ͣ������沼��0��1
	 * @author wenyujun
	 *
	 */
	public enum XmcBool{
		False("0"), True("1"), Error("2");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		public static XmcBool parseFromValue(String value){
			try {
				int temp = Integer.parseInt(value);
				return XmcBool.values()[temp];
			} catch (Exception e) {
				return XmcBool.Error;
			}
			
		}
		
		XmcBool(String value) {
				this.svalue = value;
		}
	}
	
	/**
	 * �����������
	 * @author SongQing
	 *
	 */
	public enum AccessoryType{
		Picture("ͼƬ"), 
		Video("��Ƶ"), 
		Voice("����"),
		Complex("�����ĵ�"),
		Graph("ͼ��"),
		Text("����"),
		Cache("Cache");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		AccessoryType(String value) {
				this.svalue = value;
		}
	}
	
	/**
	 * ��־����
	 * @author SongQing
	 *
	 */
	public enum LogType{
		System("ϵͳ��־"), 
		Operation("�û�������־");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		LogType(String value) {
				this.svalue = value;
		}
	}
	
	/**
	 * Preference��ݴ洢������
	 * @author SongQing
	 *
	 */
	public enum PreferenceType{
		Boolean("������"),
		String("�ַ���"),
		Int("����"),
		Float("������"),
		Long("������");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		PreferenceType(String value) {
				this.svalue = value;
		}
	} 
	
	/**
	 * ͨ�ò�������
	 * @author wenyujun
	 *
	 */
	public enum OperationType{
		Add("���"), Update("����"), Delete("ɾ��"), View("�鿴"), None("��");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		OperationType(String value) {
				this.svalue = value;
		}
	}
	
	public enum BaseDataFileType{
		/*Department("��Դ"),
		Language("����"),
		Priority("���ȼ�"),
		WorldLocationCategory("�����ص�"),
		XH_GeographyCategory("����"),
		XH_InternalInternational("�������"),
		XH_NewsCategory("�������"),
		SendAddress("�����ַ");*/
		Department(ToastHelper.getStringFromResources(R.string.department_enum)),
		Language(ToastHelper.getStringFromResources(R.string.language_enum)),
		Priority(ToastHelper.getStringFromResources(R.string.priority_enum)),
		WorldLocationCategory(ToastHelper.getStringFromResources(R.string.worldLocationCategory_enum)),
		XH_GeographyCategory(ToastHelper.getStringFromResources(R.string.geographyCategory_enum)),
		XH_InternalInternational(ToastHelper.getStringFromResources(R.string.internalInternational_enum)),
		XH_NewsCategory(ToastHelper.getStringFromResources(R.string.newsCategory_enum)),
		SendAddress(ToastHelper.getStringFromResources(R.string.sendArea_enum));
		
	
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		BaseDataFileType(String value) {
				this.svalue = value;
		}
	}
	
	public enum BaseDataType{
		
		Author(ToastHelper.getStringFromResources(R.string.author_enum)),
		Department(ToastHelper.getStringFromResources(R.string.department_enum)),
		InternalInternational(ToastHelper.getStringFromResources(R.string.internalInternational_enum)),
		NewsCategory(ToastHelper.getStringFromResources(R.string.newsCategory_enum)),
		GeographyCategory(ToastHelper.getStringFromResources(R.string.geographyCategory_enum)),
		SendArea(ToastHelper.getStringFromResources(R.string.sendArea_enum)),
		HappenPlace(ToastHelper.getStringFromResources(R.string.happenPlace_enum)),
		ReportPlace(ToastHelper.getStringFromResources(R.string.reportPlace_enum)),
		Keywords(ToastHelper.getStringFromResources(R.string.keywords_enum)),
		Language(ToastHelper.getStringFromResources(R.string.language_enum)),
		Priority(ToastHelper.getStringFromResources(R.string.priority_enum)),
		ManuscriptsType(ToastHelper.getStringFromResources(R.string.manuscriptsType_enum)),
		ReleAddress(ToastHelper.getStringFromResources(R.string.releAddress_enum)),
		CustomReleAddress(ToastHelper.getStringFromResources(R.string.customReleAddress_enum));
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		BaseDataType(String value) {
				this.svalue = value;
		}
	}
	
	public enum TemplateType{
		NORMAL("�û��Զ���ģ��"),
		WORD("���ֿ�Ѷ"),
		PICTURE("ͼƬ��Ѷ"),
		VOICE("������Ѷ"),
		VIDEO("��Ƶ��Ѷ"),
		INSTANT("���ļ���");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		TemplateType(String value) {
				this.svalue = value;
		}
	}
	
	public enum PreferenceKeys{
		User_DefaultTemplate("Ĭ�ϸ�ǩ"),
		Sys_CurrentUser("��ǰ�û�"),
		Sys_CurrentPassword("��ǰ�û�����"),
		Sys_SystemVersion("��ǰϵͳ�汾"),
		Sys_DeviceID("��ǰ�豸ID"),
		Sys_NetStatus("��ǰ����״̬"),
		Sys_LoginType("��ǰ��¼��ʽ"),
		Sys_BaseData("���»����"),
		Sys_SynManuTemp("��ǩģ��ͬ��"),
		Sys_VersionUp("ϵͳ�汾��"),
		Sys_CleanData("������"),
		Sys_CurrentServer("��ǰ������"),			//��ǰ����ʵ�����ӵķ�������Ҳ��ͨ��ӿڷ������ķ�����
		Sys_LoginServer("��¼������"),//�û����õķ�����
		Sys_VersionLowest("��ǰ��Ͱ汾Ҫ��"),
		Sys_VersionCurrent("��ǰ���°汾"),
		User_FileBlockSize("�ļ��ϴ��ֿ��С"),
		User_AutoSaveInterval("����Զ�����ʱ������"),
		User_AutoSendLocationInterval("��λ�����Զ�����ʱ������"),
		User_ManuscriptSendPolicy("������Ͳ���"),
		User_FailurePolicy("�������ʧ�ܲ���"),
		User_SendByWifi("�������wifiģʽ"),
		User_MessageTime("���ȡ����Ϣʱ��"),
		User_SavePassword("�����û�����"),
		running_state("����״̬"),
		username("��¼��"),
		password("��¼����"),
		portNum("�˿ں�"),
		chrootDir("����Ŀ¼"),
		stayAwake("�Ƿ�һֱ��������״̬"),
		show_password("������ʾ����"),
		instantUpload("���ļ���"),

		Ftp_ChooseUsername("Ftp�û���"),
		User_UploadPicCompressRatio("�ϴ�ͼƬѹ����"),
		InstantMessage_Notification("��Ϣ����"),
		InstantMessage_Notification_Sound("��Ϣ��ʾ��"),
		InstantMessage_Notification_Vibrate("��Ϣ��");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		PreferenceKeys(String value) {
				this.svalue = value;
		}
	}
	
	/**
	 * �����������������
	 * Initial/Login/Normal
	 * @author SongQing
	 *
	 */
	public enum DoPostType{
		Initial("��������"),
		Login("��½"),
		Normal("����");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		DoPostType(String value) {
				this.svalue = value;
		}
	}
	
	/**
	 * �ֻ�����״̬
	 * @author wenyujun
	 *
	 */
	public enum NetStatus
	{
		/*Disable("������"),
		WIFI("WIFI����"),
		MOBILE("�ֻ��������");*/
		Disable(ToastHelper.getStringFromResources(R.string.network_unavailable_enum)),
		WIFI(ToastHelper.getStringFromResources(R.string.network_wifiAvailable_enum)),
		MOBILE(ToastHelper.getStringFromResources(R.string.network_mobileNetAvailable_enum));
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		NetStatus(String value) {
				this.svalue = value;
		}
	}
	
	/**
	 * ��Ϣ���ͣ�ϵͳ��Ϣ(0) ������Ϣ(1) ��ʱ��Ϣ(2) XMPP��Ϣ(3) OA��Ϣ(4)
	 * @author llna
	 *
	 */
	public enum MessageType
	{
		SystemMsg("0"),
		RecommendMsg("1"),
		InstantMsg("2"),
		XMPPMsg("3"),
		OaMsg("4");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		public static MessageType parseFromValue(String value){
			try {
				int temp = Integer.parseInt(value);
				return MessageType.values()[temp];
			} catch (Exception e) {
				return MessageType.SystemMsg;
			}
			
		}

		
		
		MessageType(String value) {
				this.svalue = value;
		}
	}
	/**
	 * ��Ϣ�Ķ������ͣ�������(0) ���� (1) ��Ա(2)
	 */
	public enum Readertype
	{
		All("0"),
		Department("1"),
		Person("2");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		public static Readertype parseFromValue(String value){
			try {
				int temp = Integer.parseInt(value);
				return Readertype.values()[temp];
			} catch (Exception e) {
				return Readertype.Person;
			}
			
		}
		
		Readertype(String value) {
				this.svalue = value;
		}
		
	}
	/**
	 * ��¼״̬(����/����)
	 * @author SongQing
	 *
	 */
	public enum LoginStatus{
		/*online("����"),
		offline("����"),
		none("��ʼ");*/
		
		online(ToastHelper.getStringFromResources(R.string.online_enum)),
		offline(ToastHelper.getStringFromResources(R.string.offline_enum)),
		none(ToastHelper.getStringFromResources(R.string.none_enum));
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		LoginStatus(String value) {
				this.svalue = value;
		}
	
	}
	
	/**
	 * ���ʽӿ�����
	 * @author SongQing
	 *
	 */
	public enum InterfaceType{
		baseUrl("��½�ļ�����"),
		instantUploadUrl("���ļ���"),
		sinosoftUrl("�п���"),
		initialUrl("��ʼ�ӿ�"),
		topicdownloadUrl("���ļ�����"),
		oalistUrl("OA�б�"),
		oaitemUrl("OA��"),
		locationUrl("��λ����");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		InterfaceType(String value) {
				this.svalue = value;
		}
	}
	
	/**
	 * ��Ϣ���������ͣ�ȫ�壨0�� ���� ��1�� ��ɫ ��2�� ���ˣ�3�� Ŀǰֻ����3
	 * @author llna
	 *
	 */
	public enum MsgOwnerType
	{
		All("0"),
		Department("1"),
		Role("2"),
		Person("3");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		public static MsgOwnerType parseFromValue(String value){
			try {
				int temp = Integer.parseInt(value);
				return MsgOwnerType.values()[temp];
			} catch (Exception e) {
				return MsgOwnerType.Person;
			}
			
		}
		
		MsgOwnerType(String value) {
				this.svalue = value;
		}
	}
	
	/**
	 * ��Ϣ���ջ�ظ����ͣ����ܣ�0������ ��1��
	 * @author llna
	 *
	 */
	public enum SendOrReceiveType
	{
		Receive("0"),
		Send("1");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		public  static SendOrReceiveType parseFromValue(String value){
			try {
				int temp = Integer.parseInt(value);
				return SendOrReceiveType.values()[temp];
			} catch (Exception e) {
				return SendOrReceiveType.Receive;
			}
			
		}
		
		SendOrReceiveType(String value) {
				this.svalue = value;
		}
	}
	
	/**
	 * ��Ϣ�Ѷ���δ�����ͣ��Ѷ���0��δ�� ��1��
	 * @author llna
	 *
	 */
	public enum ReadOrNotType
	{
		Read("0"),
		New("1");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		public static ReadOrNotType parseFromValue(String value){
			try {
				int temp = Integer.parseInt(value);
				return ReadOrNotType.values()[temp];
			} catch (Exception e) {
				return ReadOrNotType.Read;
			}
			
		}
		
		ReadOrNotType(String value) {
				this.svalue = value;
		}
	}
	
	/**
	 * ��Ϣ����״̬���ɹ���1��ʧ�� ��0��
	 * @author llna
	 *
	 */
	public enum MsgSendStatus
	{
		Success("1"),
		failed("0");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		public static MsgSendStatus parseFromValue(String value){
			try {
				int temp = Integer.parseInt(value);
				return MsgSendStatus.values()[temp];
			} catch (Exception e) {
				return MsgSendStatus.failed;
			}
			
		}
		
		MsgSendStatus(String value) {
				this.svalue = value;
		}
	}
	
}
