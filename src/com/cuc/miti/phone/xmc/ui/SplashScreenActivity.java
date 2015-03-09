package com.cuc.miti.phone.xmc.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Enums.BaseDataFileType;
import com.cuc.miti.phone.xmc.domain.Enums.InterfaceType;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceKeys;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceType;
import com.cuc.miti.phone.xmc.domain.ComeFromAddress;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.domain.Language;
import com.cuc.miti.phone.xmc.domain.NewsCategory;
import com.cuc.miti.phone.xmc.domain.NewsPriority;
import com.cuc.miti.phone.xmc.domain.Place;
import com.cuc.miti.phone.xmc.domain.ProvideType;
import com.cuc.miti.phone.xmc.domain.Region;
import com.cuc.miti.phone.xmc.domain.SendToAddress;
import com.cuc.miti.phone.xmc.domain.XmcException;
import com.cuc.miti.phone.xmc.domain.Enums.NetStatus;
import com.cuc.miti.phone.xmc.http.Configuration;
import com.cuc.miti.phone.xmc.http.RemoteCaller;
import com.cuc.miti.phone.xmc.logic.BaseDataService;
import com.cuc.miti.phone.xmc.logic.ComeFromAddressService;
import com.cuc.miti.phone.xmc.logic.LanguageService;
import com.cuc.miti.phone.xmc.logic.NewsCategoryService;
import com.cuc.miti.phone.xmc.logic.NewsPriorityService;
import com.cuc.miti.phone.xmc.logic.PlaceService;
import com.cuc.miti.phone.xmc.logic.ProvideTypeService;
import com.cuc.miti.phone.xmc.logic.RegionService;
import com.cuc.miti.phone.xmc.logic.SendToAddressService;
import com.cuc.miti.phone.xmc.utils.DBManager;
import com.cuc.miti.phone.xmc.utils.DeviceInfoHelper;
import com.cuc.miti.phone.xmc.utils.Format;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.SendManuscriptsHelper;
import com.cuc.miti.phone.xmc.utils.SharedPreferencesHelper;
import com.cuc.miti.phone.xmc.utils.StandardizationDataHelper;
import com.cuc.miti.phone.xmc.utils.ToastHelper;
import com.cuc.miti.phone.xmc.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Splash screen activity is used to show splash . Here we use handler to show
 * splash for particular time and then disappear automatically.
 * 
 * @author SongQing
 */
public class SplashScreenActivity extends Activity {

	private SplashHandler mHandler=null;
	private SharedPreferencesHelper mSharedPreferencesHelper = null;
	
	private RelativeLayout uploadLayout;
	private TextView fileNameTextView,proTextView;
	private ProgressBar progressBar;
	
	private ComeFromAddressService mComeFromAddressService;
	private LanguageService mLanguageService;
	private PlaceService mPlaceServices;
	private NewsCategoryService mNewsCategoryServices;
	private NewsPriorityService mNewsPriorityService;
	private ProvideTypeService mProvideTypeService;
	private RegionService mRegionService;
	private SendToAddressService mSendToAddressService;
	private BaseDataService mBaseDataService;
	private DeviceInfoHelper dvDeviceInfoHelper;
	
	private static final String TAG = "SplashScreenActivity";	
	private static final int MSG_GETTOPICLIST_ERROR = 1;
	private static final int MSG_GETTOPICLIST_START = 2;
	private static final int MSG_GETTOPICLIST_PRO = 3;
	private static final int MSG_GETTOPICLIST_END = 4;
	private static final int MSG_GETAPPSERVER_ERROR = 5;
	private static final int MSG_GETAPPSERVER_FINISH = 6;
	private static final int MSG_VALIDATE_VERSION_FINISH = 7;
	private static final int MSG_VALIDATE_VERSION_ERROR = 8;
	private static final int MSG_NETWORK_NONE = 0;
	private static final int MSG_NETWORK_TIMEOUT = -1;
	private static final int MSG_VERSION_UPDATE_START=9;
	private static int NORMAL_TIMEOUT = 6000;				//��ͨ��������ʱ
	
   	
    private static String apkUrl = "";							 //ϵͳ��װ��·�� 
    private static String savePath ="";  						//���ذ�װ·��
    private static String saveFileName="";					//����·����(�����ļ���)
    /* �������֪ͨuiˢ�µ�handler��msg���� */
    private ProgressBar mProgress; 
    private static final int DOWN_UPDATE = 10;      
    private static final int DOWN_OVER = 11;
	protected static final int MSG_SHOWNOTICEDIALOG = 12;
	protected static final int MSG_SHOWNOTICEDIALOGOPTION = 13; 
    private int progress;      
    private boolean interceptFlag = false;
    private AlertDialog.Builder builder = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		// �����ޱ���
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ����ȫ��
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.splash);
		IngleApplication.getInstance().addActivity(this);
		this.initialize();
  
	}

	/**
	 * ��ʼ��
	 */
	private void initialize(){
		mHandler = new SplashHandler();
		dvDeviceInfoHelper = new DeviceInfoHelper();
		mSharedPreferencesHelper = new SharedPreferencesHelper(SplashScreenActivity.this);
	
		mComeFromAddressService = new ComeFromAddressService(SplashScreenActivity.this);
		mLanguageService = new LanguageService(SplashScreenActivity.this);
		mPlaceServices = new PlaceService(SplashScreenActivity.this);
		mNewsCategoryServices = new NewsCategoryService(SplashScreenActivity.this);
		mNewsPriorityService = new NewsPriorityService(SplashScreenActivity.this);
		mProvideTypeService = new ProvideTypeService(SplashScreenActivity.this);
		mRegionService = new RegionService(SplashScreenActivity.this);
		mSendToAddressService = new SendToAddressService(SplashScreenActivity.this);
		mBaseDataService = new BaseDataService(SplashScreenActivity.this);
		
		apkUrl = Configuration.getSystemUpgradeUrl();
		//For Test
		//apkUrl = "http://www.happytjp.com/UploadFile/XinhuaManuscriptCollection.apk";
		savePath = StandardizationDataHelper.GetBaseDataFileStorePath();
		saveFileName = savePath +apkUrl.substring(apkUrl.lastIndexOf("/"),apkUrl.length());
		
		this.setUpViews();	
		
		//��װ������һ�����У��Ὣ��ݿⲿ��ϵͳĬ�ϵ�Data\Data\com.cuc.miti.phone.xmc/databasesĿ¼��
		if(createDatabaseIfNewInstall()){			//�����ݿⴴ���ɹ�������Խ��л�ȡ�������Ƽ��ͻ���ݵĸ��µ�������
			 
			//��һ������û������״̬�ĸı䣬Application�е�netStatusΪnull����Ҫ������ȡ��ǰ����״̬����Ϊ�丳ֵ			
			IngleApplication.setNetStatus(dvDeviceInfoHelper.getNetStatus());	
			if(IngleApplication.getNetStatus() != NetStatus.Disable){
				GetAppServer();		//����Ӧ�ø���û����õķ�������ַ��ȡϵͳ�Ƽ��ĵ�¼������URL		
				//validateVersion();			//��ȡ�Ƿ������µĳ���汾
			}else{
				final int welcomeScreenDisplay = 2000;
				Message msg = new Message();
				msg.what = MSG_NETWORK_NONE;
				mHandler.sendMessageDelayed(msg, welcomeScreenDisplay);
			}
		}else{
			//Toast.makeText(SplashScreenActivity.this, R.string.DBCreateError, Toast.LENGTH_LONG).show();
			ToastHelper.showToast(this.getResources().getString(R.string.DBCreateError),Toast.LENGTH_SHORT);
			SplashScreenActivity.this.finish();
		}
		
//		final int welcomeScreenDisplay = 1000;
//		Message msg = new Message();
//		msg.what = 0;
//		mHandler.sendMessageDelayed(msg, welcomeScreenDisplay);
	}
	
	/**
	 * �汾У�飬���������Ҫ���ǿ����
	 */
	private void compareVerson()
	{
		new Thread(new Runnable() {
			
			public void run() {
				try {
					synchronized (IngleApplication.mLock) {
						while (IngleApplication.getNetStatus() == NetStatus.Disable) {
							IngleApplication.mLock.wait();  
						}
					}
				} catch (Exception e) {
					Logger.e(e);
					e.printStackTrace();
				}
				
				try {
					String[] versionServer = RemoteCaller.getAndroidAppVersion();
					String[] versionLocal = DeviceInfoHelper.getAppVersionName(SplashScreenActivity.this);
					Float versionLow=Float.parseFloat(versionServer[0]);
					Float versionHigh=Float.parseFloat(versionServer[1]);
					Float versionCurrent=Float.parseFloat(versionLocal[1]);
					apkUrl = versionServer[2];
					
					mSharedPreferencesHelper.SaveCommonPreferenceSettings(PreferenceKeys.Sys_VersionLowest.toString(), PreferenceType.String, versionServer[0]);
					mSharedPreferencesHelper.SaveCommonPreferenceSettings(PreferenceKeys.Sys_VersionCurrent.toString(), PreferenceType.String, versionServer[1]);
					
					if(versionCurrent<versionLow)					//��ǰ�汾������Ͱ汾ʱ���������
					{        
						sendMessage(MSG_SHOWNOTICEDIALOG,null);	
					}else if(versionCurrent<versionHigh)			//��ǰ�汾������߰汾ʱ��ѡ�����
					{
						sendMessage(MSG_SHOWNOTICEDIALOGOPTION,null);   
					}else{									
						sendMessage(MSG_VALIDATE_VERSION_FINISH,null);
					}
							
				} catch (XmcException e) {
					if(e.getMessage().equals(XmcException.TIMEOUT)){
						sendMessage(MSG_NETWORK_TIMEOUT,e.getMessage());
					}else{
						sendMessage(MSG_VALIDATE_VERSION_ERROR,e.getMessage());
					}					
				}catch (Exception e) {
					Logger.e(e);
					sendMessage(MSG_VALIDATE_VERSION_ERROR,e.getMessage());
				}
			}
		}).start();		
	}
	
	/**
	 * ǿ����Ի���(ֻ��ȷ����û��ȡ��)
	 */
	public void showNoticeDialog(){
		 builder = new AlertDialog.Builder(SplashScreenActivity.this);
		 builder.setTitle(R.string.updateSysVersionDialogTitle).setPositiveButton(R.string.updateSysVersionDialogPositiveButton,new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int which) {				
					sendMessage(MSG_VERSION_UPDATE_START,null);				
				}
			}).setMessage(R.string.updateSysVersionDialogAbsoluteMsg).setCancelable(false).create().show();
		 
		 }	
	
	/**
	 * ��ѡ��Ի���
	 */
	 public void showNoticeDialogOption(){  
		 builder = new AlertDialog.Builder(SplashScreenActivity.this);  
		 builder.setTitle(R.string.updateSysVersionDialogTitle).setPositiveButton(R.string.updateSysVersionDialogPositiveButton,new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int which) {				
					sendMessage(MSG_VERSION_UPDATE_START,null);
					}
			}).setNegativeButton(R.string.updateSysVersionDialogNegativeButton, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
						int which) {
					sendMessage(MSG_VALIDATE_VERSION_FINISH,null);
				}
			}).setMessage(R.string.updateSysVersionDialogOpotionMsg).setCancelable(false).create().show();		
	 } 
	 
	 /**
	  * ���ؽ�ȶԻ���
	  */
	 public void showDownloadDialog(){
		 builder = new AlertDialog.Builder(SplashScreenActivity.this);
	     builder.setTitle(R.string.updateSysVersionDialogTitle);  
	     final LayoutInflater inflater = LayoutInflater.from(SplashScreenActivity.this);  
	     View v = inflater.inflate(R.layout.progress_system_update,null);  
	     mProgress = (ProgressBar)v.findViewById(R.id.progressBar_SystemUpdate_Splash);         
	     builder.setView(v).setCancelable(false).create().show();
	     
	     DownloadAPKThread();
	 } 
  
	 /**
	  * ����ϵͳ������߳�
	  */
	 private void DownloadAPKThread(){
		new Thread(new Runnable() {
			public void run() {  
	          try {  
	        	  URL url = new URL(apkUrl);

	              HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
	              conn.setConnectTimeout(NORMAL_TIMEOUT);
	              conn.setReadTimeout(NORMAL_TIMEOUT);
	              conn.connect();  
	              int length = conn.getContentLength();  
	              InputStream is = conn.getInputStream(); 
	              
	              File file = new File(savePath);  
	             if(!file.exists()){  
	                  file.mkdir();  
	             }  
	             String apkFile = saveFileName;  
	             File ApkFile = new File(apkFile);  
	             FileOutputStream fos = new FileOutputStream(ApkFile); 
	               
	              int count = 0;  
	             byte buf[] = new byte[1024];  
	                
	             do{                  
	                  int numread = is.read(buf);  
	                  count += numread;  
	                  progress =(int)(((float)count / length) * 100);  
	                 //���½��  
	                  sendMessage(DOWN_UPDATE,null);  
	                  if(numread <= 0){      
	                      //�������֪ͨ��װ  
	                	  sendMessage(DOWN_OVER,null);  
	                      
	                      break;  
	                  }  
	                  fos.write(buf,0,numread);  
	             }while(!interceptFlag);//���ȡ���ֹͣ����. 
	               
	              fos.close();  
	              is.close();  
	          }catch(ConnectTimeoutException e){
	        	  Logger.e(e);
	        	  sendMessage(MSG_NETWORK_TIMEOUT,e.getMessage());
			  }catch(InterruptedIOException e){	
				  Logger.e(e);
				  sendMessage(MSG_NETWORK_TIMEOUT,e.getMessage());
			  }catch(Exception e){  
	        	  Logger.e(e);
	              sendMessage(MSG_VALIDATE_VERSION_ERROR,e.getMessage());
	          } 	          
	      } 

	     }).start();  
	   }
	 
	   	/**  
	     * ��װapk  
	     * @param url  
	     */ 
	     private void installApk(){  
	       File apkfile = new File(saveFileName);  
	        if (!apkfile.exists()) {  
	             return;  
	        }      
	        Intent i = new Intent(Intent.ACTION_VIEW);  
	        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");   
	        SplashScreenActivity.this.startActivity(i); 	      
	     }
	
	/**
	 * ��ʼ��ҳ��ؼ�
	 */
	private void setUpViews(){
		uploadLayout=(RelativeLayout)findViewById(R.id.updateLayout_Splash);
		progressBar=(ProgressBar)findViewById(R.id.proBar_Splash);
		proTextView=(TextView)findViewById(R.id.proText_Splash);
		fileNameTextView=(TextView)findViewById(R.id.fileName_Splash);
	}
	
	/**
	 * Handler class implementation to handle the message
	 * 
	 * @author SongQing
	 * 
	 */
	private class SplashHandler extends Handler {

		// This method is used to handle received messages
		public void handleMessage(Message msg) {
			Intent mIntent = null;
			// switch to identify the message by its code
			switch (msg.what) {
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
	                SplashScreenActivity.this.finish();
	                break;  
				case MSG_GETTOPICLIST_START:
					if(msg.obj==null)
						uploadLayout.setVisibility(View.VISIBLE);
					else {
						fileNameTextView.setText(msg.obj.toString());
					}
					break;
				case MSG_GETTOPICLIST_PRO:
					proTextView.setText(msg.obj.toString() + "%");
					progressBar.setProgress(Integer.valueOf(msg.obj.toString()));
					break;
				case MSG_GETTOPICLIST_END:	
					if(msg.obj !=null){
						//Toast.makeText(SplashScreenActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
						ToastHelper.showToast(msg.obj.toString(),Toast.LENGTH_SHORT);
					}
					mIntent = new Intent(SplashScreenActivity.this,LoginActivity.class);
					startActivity(mIntent);
					SplashScreenActivity.this.finish();
					break;
				case MSG_GETTOPICLIST_ERROR:
					if(msg.obj!=null){
						//Toast.makeText(SplashScreenActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
						ToastHelper.showToast(msg.obj.toString(),Toast.LENGTH_SHORT);
					}
					mIntent = new Intent(SplashScreenActivity.this,LoginActivity.class);
					startActivity(mIntent);
					SplashScreenActivity.this.finish();
					break;
				case MSG_GETAPPSERVER_FINISH:
						setBasedataVersionIntoSystemPreference();				//����ϵͳƫ�����û�����ļ��İ汾��(ֻ�ڵ�һ�ΰ�װʱ�Ż�����)
						compareVerson();				//���������
					break;
				case MSG_GETAPPSERVER_ERROR:
					mIntent = new Intent(SplashScreenActivity.this,LoginActivity.class);
					startActivity(mIntent);
					if(msg.obj!=null){
						//Toast.makeText(SplashScreenActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
						ToastHelper.showToast(msg.obj.toString(),Toast.LENGTH_SHORT);
					}
					SplashScreenActivity.this.finish();
					
					break;
				case MSG_VALIDATE_VERSION_FINISH:
					getTopicList();					//���»����
//					GetAppServer();							//��ȡ�Ƽ���½������(�ɹ�����Handler�н���GetTopicList)
					if(msg.obj!=null){
						//Toast.makeText(SplashScreenActivity.this,msg.obj.toString(), Toast.LENGTH_SHORT).show();
						ToastHelper.showToast(msg.obj.toString(),Toast.LENGTH_SHORT);
					}
					break;
				case MSG_VALIDATE_VERSION_ERROR:	
					getTopicList();					//���»����
//					GetAppServer();	
					if(msg.obj!=null){
						//Toast.makeText(SplashScreenActivity.this,msg.obj.toString(), Toast.LENGTH_SHORT).show();
						ToastHelper.showToast(msg.obj.toString(),Toast.LENGTH_SHORT);
					}
					break;
				case MSG_NETWORK_NONE:				//����ݿⲿ��ɹ������������������³���
					super.handleMessage(msg);
					mIntent = new Intent(SplashScreenActivity.this,LoginActivity.class);
					startActivity(mIntent);
					SplashScreenActivity.this.finish();
					break;
				case MSG_NETWORK_TIMEOUT:				//���糬ʱ
					super.handleMessage(msg);
					mIntent = new Intent(SplashScreenActivity.this,LoginActivity.class);
					startActivity(mIntent);
					//Toast.makeText(SplashScreenActivity.this, "��������ʱ,������������!", Toast.LENGTH_SHORT).show();
					ToastHelper.showToast(getResources().getString(R.string.requestNetTimeout),Toast.LENGTH_SHORT);
					SplashScreenActivity.this.finish();
					break;
				default:
					break;
			}

				
			}
		}
	
	/**
	 * У��������°汾
	 */
	private void validateVersion(){
		new Thread(new Runnable() {
			
			public void run() {
				try {
					synchronized (IngleApplication.mLock) {
						while (IngleApplication.getNetStatus() == NetStatus.Disable) {
							IngleApplication.mLock.wait();  
						}
					}
				} catch (Exception e) {
					Logger.e(e);
					e.printStackTrace();
				}
				KeyValueData message = new KeyValueData("","");
				try {//��ȡ�Ƿ�������°汾
					SendManuscriptsHelper.getVersion(message, SplashScreenActivity.this,"GetVersionInfo");				//��ȡ��ǰ�������˵Ŀͻ��˰汾��Ϣ
					if(!message.getValue().equals("")){
						String[] version = message.getValue().split(",");
						mSharedPreferencesHelper.SaveCommonPreferenceSettings(PreferenceKeys.Sys_VersionLowest.toString(), PreferenceType.String, version[0]);
						mSharedPreferencesHelper.SaveCommonPreferenceSettings(PreferenceKeys.Sys_VersionCurrent.toString(), PreferenceType.String, version[1]);
					}
					SendManuscriptsHelper.getVersion(message, SplashScreenActivity.this,"Login");					
				} catch (XmcException e) {
					sendMessage(MSG_VALIDATE_VERSION_ERROR,e.getMessage());
				} 
				if(!message.getValue().equals("")){
					sendMessage(MSG_VALIDATE_VERSION_FINISH,message.getValue());
				}
			}
		}).start();
	}
	/**
	 * ��ȡ��½�������Ƽ�
	 */
	private void GetAppServer(){
		new Thread(new Runnable() {			
			public void run() {
				try {
					synchronized (IngleApplication.mLock) {
						while (IngleApplication.getNetStatus() == NetStatus.Disable) {
							IngleApplication.mLock.wait();  
						}
					}
				} catch (Exception e) {
					Logger.e(e);
					e.printStackTrace();
				}
				try {
					SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(SplashScreenActivity.this);
					String entranceUrlString = sharedPreferencesHelper.getPreferenceValue(PreferenceKeys.Sys_LoginServer.toString());
					
					if(entranceUrlString ==null || entranceUrlString.equals("")){//���û��������(��һ�Σ�֮�󶼿��Դ������ļ���ȡ)
						entranceUrlString = Configuration.getDefaultServer();
						sharedPreferencesHelper.SaveCommonPreferenceSettings(PreferenceKeys.Sys_LoginServer.toString(), PreferenceType.String, entranceUrlString);
					}
					entranceUrlString = entranceUrlString + StandardizationDataHelper.getServerInterface(entranceUrlString, InterfaceType.initialUrl);
					String jsonResult = RemoteCaller.GetAppServerIP(entranceUrlString);
					
					if("601".equals(jsonResult)){	//TODO ��ʻ�
						//sendMessage(MSG_GETAPPSERVER_ERROR,"������ʴ���");
						sendMessage(MSG_NETWORK_NONE,getResources().getString(R.string.requestNetError));
					}else {
						String[] ips = null;
						String[] temp = null;
						if (StringUtils.isNotBlank(jsonResult)) {
							JSONObject obj = new JSONObject(jsonResult);
							temp = obj.get("AppServerIp").toString().split(",");
							
							JSONObject obj1 = (JSONObject) obj.get("AppServerIp");
							HashMap<String,String > hsHashMap =toHashMap(obj1);
									
							Configuration.setHashMapAppAddress(hsHashMap);

							if(hsHashMap !=null && hsHashMap.size()>0){
								Iterator<Entry<String, String>> iterator = hsHashMap.entrySet().iterator();
								while(iterator.hasNext()){
									Map.Entry<String, String> entry = (Map.Entry<String, String>)iterator.next();
									//TODO �������÷��ʷ���������ʽ����ʱ�޸�
									sharedPreferencesHelper.SaveCommonPreferenceSettings(PreferenceKeys.Sys_CurrentServer.toString(), PreferenceType.String, entry.getValue().toString());
									StandardizationDataHelper.setUrlForTest(entry.getValue().toString());			
								}
							}
							sendMessage(MSG_GETAPPSERVER_FINISH,null);
						}
					}					
				}catch (JSONException e) {
					Logger.e(e);
					sendMessage(MSG_GETAPPSERVER_ERROR, e.getMessage());
				} catch (XmcException e) {
					if(e.getMessage().equals(XmcException.TIMEOUT)){
						sendMessage(MSG_NETWORK_TIMEOUT,e.getMessage());
					}else{
						sendMessage(MSG_GETAPPSERVER_ERROR,e.getMessage());
					}					
				}catch (Exception e) {
					Logger.e(e);
					sendMessage(MSG_GETAPPSERVER_ERROR,e.getMessage());
				}
				
			}
		}).start();
	}
	
	/**
	 * ͬ���������Ϣ
	 */
	private void getTopicList() {

		new Thread(new Runnable() {
			
			public void run() {
				 BaseDataService service=new BaseDataService(SplashScreenActivity.this);
				 String topicList=service.GetCurrentBDFilesNameString();
				
				//---------------------------------------------------For Test------------------------------------------------------------------					
//				String topicList = "topiclist.cnml-Department-85.xml,topiclist.cnml-Language-1.xml,topiclist.cnml-MediaType-1.xml,"
//						+ "topiclist.cnml-Priority-1.xml,topiclist.cnml-WorldLocationCategory-1.xml,topiclist.cnml-XH_GeographyCategory-5.xml,"
//						+ "topiclist.cnml-XH_InternalInternational-4.xml,topiclist.cnml-XH_NewsCategory-7.xml";
				//---------------------------------------------------For Test------------------------------------------------------------------	
				
				try {
					synchronized (IngleApplication.mLock) {
						while (IngleApplication.getNetStatus() == NetStatus.Disable) {
							try {
								IngleApplication.mLock.wait();
							} catch (Exception e) {
								Logger.e(e);
								e.printStackTrace();
							}			
						}
					}
					
					String result = RemoteCaller.GetTopicList(topicList);
					
					if (StringUtils.isNotBlank(result)) {
						// ����						
						if("601".equals(result)){	//TODO ��ʻ�
							//sendMessage(MSG_GETTOPICLIST_END,"������ʴ���");
							sendMessage(MSG_NETWORK_NONE,getResources().getString(R.string.requestNetError));
						}else {
							sendMessage(MSG_GETTOPICLIST_START, null);
							
							String[] fileNames = result.trim().split(",");
							
							for (int i = 0; i < fileNames.length; i++) {
								
								sendMessage(MSG_GETTOPICLIST_START, fileNames[i]);
								if(DownloadBaseData(fileNames[i])){//������سɹ����������ݿ�
									updateBasedataInDB(fileNames[i]);
								}
							}
							sendMessage(MSG_GETTOPICLIST_END, getResources().getString(R.string.BaseDataUpdateFinished));
						}

					} else {
						sendMessage(MSG_GETTOPICLIST_END,null );
					}

				} catch (Exception e) {
					Logger.e(e);
					Log.e(TAG, e.getMessage());
					sendMessage(MSG_GETTOPICLIST_ERROR, e.getMessage());
				} 
			}
		}).start();
	}
	
	/**
	 * @Description ʹ��GET������ȡ�����XML�ļ�
	 * @param topicList XML�ļ����ļ���
	 */

	private boolean DownloadBaseData(String fileName) {
		//Eg : http://202.84.17.51/client/v2c?ua=gettopiclist&topiclist=topiclist.SendAddress-6.xml
		
		//��ȡ�ļ�����·��
		String path= Configuration.getTopicDownloadUrl() + fileName;
		String downPath=StandardizationDataHelper.GetBaseDataFileStorePath();
		boolean createDir = new File(downPath).mkdirs();
		if(createDir){
			Log.i(IngleApplication.TAG, "��������·��");
		}

		try {
			Log.i(IngleApplication.TAG, "��ʼ����" + fileName	+ "....");
			URL url = new URL(path);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept-Encoding", "identity"); 
			connection.setConnectTimeout(NORMAL_TIMEOUT);
			connection.setReadTimeout(NORMAL_TIMEOUT);
			connection.setDoOutput(true);
			connection.connect();

			FileOutputStream fStream = new FileOutputStream(new File(downPath,Format.replaceBlank(fileName)));
			InputStream inputStream = connection.getInputStream();
			int fileLength = connection.getContentLength();
			int downloadFileSize = 0;

			byte[] buffer = new byte[1024];
			int length = 0;
			while ((length = inputStream.read(buffer)) > 0) {
				fStream.write(buffer, 0, length);
				downloadFileSize += length;
				sendMessage(MSG_GETTOPICLIST_PRO, downloadFileSize * 100/ fileLength);
			}
			fStream.close();
			
			return true;

		}catch(ConnectTimeoutException e){
      	  	Logger.e(e);
      	  	sendMessage(MSG_NETWORK_TIMEOUT,e.getMessage());
      	  	return false;
		}catch(InterruptedIOException e){	
			Logger.e(e);
			sendMessage(MSG_NETWORK_TIMEOUT,e.getMessage());
			return false;
		}catch (Exception e) {
			Logger.e(e);
			sendMessage(MSG_GETTOPICLIST_ERROR, e.getMessage());
			return false;
		}
	}

	/**
	 * ��Handler����������Ϣ
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
     * ��json����ת����Map 
     * 
     * @param jsonObject json���� 
     * @return HashMap���� 
	 * @throws JSONException 
     */ 
    private static HashMap<String, String> toHashMap(JSONObject jsonObject) throws JSONException 
    { 
    	HashMap<String, String> result = new HashMap<String, String>(); 
        @SuppressWarnings("unchecked")
		Iterator<String> iterator = jsonObject.keys(); 
        String key = null; 
        String value = null; 
        while (iterator.hasNext()) 
        { 
            key = iterator.next(); 
            value = jsonObject.getString(key); 
            result.put(key, value); 
        } 
        return result; 
    } 
    
    /**
     * ��һ�ΰ�װ���ʱ������ݿ�
     */
    private boolean createDatabaseIfNewInstall(){
		try {
			DBManager db =  new DBManager(SplashScreenActivity.this);
			db.createDatabase();
			return true;
		} catch (Exception e) {
			Logger.e(e);
			e.printStackTrace();
			//Toast.makeText(SplashScreenActivity.this, R.string.DBCreateError, Toast.LENGTH_SHORT).show();
			ToastHelper.showToast(this.getResources().getString(R.string.DBCreateError),Toast.LENGTH_SHORT);
			return false;
		}
    }
    
    /**
     * ������װ�ļ�����ݿ⣬��ݿ��еĻ���ݶ������ȵ���ģ�
     * ��ˣ��ڳ����һ�ΰ�װʱ����Ҫ�����Ӧ����ݵİ汾���Ա������
     */
    private void setBasedataVersionIntoSystemPreference(){
    	//�ж��Ƿ��һ�ΰ�װ����ķ�������ȡ��ǰϵͳSystemPreference���Ƿ�����Ӧ��
    	//���û������Ҫ�̶���ӵ�ǰ���ļ��İ汾��
    	String checkString = mSharedPreferencesHelper.getPreferenceValue(BaseDataFileType.Language.toString());
    	if(!StringUtils.isNotBlank(checkString)){		//���Ϊ�գ���˵���ǵ�һ������
    		mSharedPreferencesHelper.SaveCommonPreferenceSettings(BaseDataFileType.Department.toString(), 
    																									PreferenceType.String, 
    																									"topiclist.cnml-Department-85.xml");
    		mSharedPreferencesHelper.SaveCommonPreferenceSettings(BaseDataFileType.Language.toString(), 
																										PreferenceType.String, 
																										"topiclist.cnml-Language-7.xml");
    		mSharedPreferencesHelper.SaveCommonPreferenceSettings(BaseDataFileType.SendAddress.toString(), 
																										PreferenceType.String, 
																										"topiclist.SendAddress-2.xml");
    		mSharedPreferencesHelper.SaveCommonPreferenceSettings(BaseDataFileType.Priority.toString(), 
																										PreferenceType.String, 
																										"topiclist.cnml-Priority-1.xml");
    		mSharedPreferencesHelper.SaveCommonPreferenceSettings(BaseDataFileType.WorldLocationCategory.toString(), 
																										PreferenceType.String, 
																										"topiclist.cnml-WorldLocationCategory-1.xml");
    		mSharedPreferencesHelper.SaveCommonPreferenceSettings(BaseDataFileType.XH_GeographyCategory.toString(), 
																										PreferenceType.String, 
																										"topiclist.cnml-XH_GeographyCategory-5.xml");
    		mSharedPreferencesHelper.SaveCommonPreferenceSettings(BaseDataFileType.XH_InternalInternational.toString(), 
																										PreferenceType.String, 
																										"topiclist.cnml-XH_InternalInternational-4.xml");
    		mSharedPreferencesHelper.SaveCommonPreferenceSettings(BaseDataFileType.XH_NewsCategory.toString(), 
																										PreferenceType.String,
																										"topiclist.cnml-XH_NewsCategory-7.xml");    		
    	}
    }
    
    /**
     * ������ݿ��еĻ����
     * @throws IOException 
     */
    private void updateBasedataInDB(String fileName) throws IOException{
    	
    	InputStream is = null;
    	String downPath=StandardizationDataHelper.GetBaseDataFileStorePath();
    	File dataFile = null;
    	
    	try {

	    	if(fileName.contains(BaseDataFileType.Department.toString())){
	    		
	    		dataFile = new File(downPath + "/" + fileName);
	    		if(!dataFile.exists()){
	    			return ;
	    		}
	    		is = new FileInputStream(dataFile);
				List<ComeFromAddress> comeFromAddressList = mComeFromAddressService.GetComeFromAddressFromXMLFile(is);
				
				if(mComeFromAddressService.deleteAllComeFromAddress()){
					if(mComeFromAddressService.multiInsert(comeFromAddressList)){
						mBaseDataService.SetCurrentBDFileName(BaseDataFileType.Department, fileName);			
					}
				}
	    	}
	    	else if(fileName.contains(BaseDataFileType.Language.toString())){
	    		dataFile = new File(downPath + "/" + fileName);
	    		if(!dataFile.exists()){
	    			return ;
	    		}
	    		is = new FileInputStream(dataFile);
				List<Language> languageList = mLanguageService.GetLanguageFromXMLFile(is);
				
				if(mLanguageService.deleteAllLanguage()){
					if(mLanguageService.multiInsert(languageList)){
						mBaseDataService.SetCurrentBDFileName(BaseDataFileType.Language, fileName);		
					}
				}
	    	}
	    	else if(fileName.contains(BaseDataFileType.Priority.toString())){
	    		dataFile = new File(downPath + "/" + fileName);
	    		if(!dataFile.exists()){
	    			return ;
	    		}
	    		is = new FileInputStream(dataFile);
				List<NewsPriority> newsPriorityList = mNewsPriorityService.GetNewsPriorityFromXMLFile(is);
				
				if(mNewsPriorityService.deleteAllNewsPriority()){
					if(mNewsPriorityService.multiInsert(newsPriorityList)){
						mBaseDataService.SetCurrentBDFileName(BaseDataFileType.Priority, fileName);			
					}
				}		
	    	}
	    	else if(fileName.contains(BaseDataFileType.XH_GeographyCategory.toString())){
	    		dataFile = new File(downPath + "/" + fileName);
	    		if(!dataFile.exists()){
	    			return ;
	    		}
	    		is = new FileInputStream(dataFile);
				List<Region> regionList = mRegionService.GetRegionFromXMLFile(is);
				
				if(mRegionService.deleteAllRegion()){
					if(mRegionService.multiInsert(regionList)){
						mBaseDataService.SetCurrentBDFileName(BaseDataFileType.XH_GeographyCategory, fileName);			
					}
				}	
	    	}
	    	else if(fileName.contains(BaseDataFileType.WorldLocationCategory.toString())){
	    		dataFile = new File(downPath + "/" + fileName);
	    		if(!dataFile.exists()){
	    			return ;
	    		}
	    		is = new FileInputStream(dataFile);
				List<Place> placeList = mPlaceServices.GetPlaceFromXMLFile(is);
				
				if(mPlaceServices.deleteAllPlace()){
					if(mPlaceServices.multiInsert(placeList)){
						mBaseDataService.SetCurrentBDFileName(BaseDataFileType.WorldLocationCategory, fileName);		
					}
				}	
	    	}
	    	else if(fileName.contains(BaseDataFileType.XH_InternalInternational.toString())){
	    		dataFile = new File(downPath + "/" + fileName);
	    		if(!dataFile.exists()){
	    			return ;
	    		}
	    		is = new FileInputStream(dataFile);
				List<ProvideType> provideTypeList = mProvideTypeService.GetProvideTypeFromXMLFile(is);
				
				if(mProvideTypeService.deleteAllProvideType()){
					if(mProvideTypeService.multiInsert(provideTypeList)){
						mBaseDataService.SetCurrentBDFileName(BaseDataFileType.XH_InternalInternational, fileName);			
					}
				}	
	    	}
	    	else if(fileName.contains(BaseDataFileType.XH_NewsCategory.toString())){
	    		dataFile = new File(downPath + "/" + fileName);
	    		if(!dataFile.exists()){
	    			return ;
	    		}
	    		is = new FileInputStream(dataFile);
				List<NewsCategory> newsCategorieList = mNewsCategoryServices.GetNewsCategoryDataFromXMLFile(is);
				
				if(mNewsCategoryServices.deteteAllNewsCategory()){
					if(mNewsCategoryServices.multiInsert(newsCategorieList))
					{
						mBaseDataService.SetCurrentBDFileName(BaseDataFileType.XH_NewsCategory, fileName);		
					}
				}		
	    	}
	    	else if(fileName.contains(BaseDataFileType.SendAddress.toString())){
	    		dataFile = new File(downPath + "/" + fileName);
	    		if(!dataFile.exists()){
	    			return ;
	    		}
	    		is = new FileInputStream(dataFile);
				List<SendToAddress> sendToAddresseList = mSendToAddressService.GetSendToAddressDataFromXMLFile(is);
				
				if(mSendToAddressService.deleteAllSendToAddress()){
					if(mSendToAddressService.multiInsert(sendToAddresseList)){
						mBaseDataService.SetCurrentBDFileName(BaseDataFileType.SendAddress, fileName);		
					}				
				}		
	    	}
	    	else{}
    	} catch (Exception e) {
			Logger.e(e);
		}
    }
}
