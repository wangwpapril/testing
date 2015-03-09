package com.cuc.miti.phone.xmc.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xbill.DNS.MDRecord;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.preference.Preference;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Enums.NetStatus;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceKeys;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceType;
import com.cuc.miti.phone.xmc.R;

public class DeviceInfoHelper {

	private TelephonyManager mTelephonyManager;
	private Context mContext;
	private static String ANDROID_DEFAULT_DEVICE_ID ="Android.IMEI.2012";
	private static final int ERROR = -1;

	public DeviceInfoHelper() {
		this.mTelephonyManager = (TelephonyManager) IngleApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
		this.mContext = IngleApplication.getInstance();
	}
	
	/**
	 * ��ȡ��ǰAndroid�ն���·һ�ĵ绰����
	 * @return
	 */
	public String getPhoneNumberString(){
		return mTelephonyManager.getLine1Number()==null?"":mTelephonyManager.getLine1Number();
	}
	
	/**
	 *  Ψһ���豸ID��   
	 *  GSM�ֻ�� IMEI �� CDMA�ֻ�� MEID,û��3Gģ���Pad����android�豸��
	 * @return Return null if device ID is not available.   
	 */
	public String getDeviceId(){
		String phoneDeviceID = mTelephonyManager.getDeviceId();
		String padDeviceID = Secure.getString(this.mContext.getContentResolver(), Secure.ANDROID_ID);
		if( phoneDeviceID != null){
			return phoneDeviceID;
		}else if(padDeviceID!= null ){
			return padDeviceID;
		}else{
			return ANDROID_DEFAULT_DEVICE_ID;
		}		
	}
	public String translateDeviceId(){
		String deviceId=getDeviceId();
		if(deviceId.length()>16){
			deviceId=Encrypt.toMD5(deviceId).substring(5, 20);
		}
		return deviceId;
		
	}
	/**
	 *  �豸������汾�ţ�   
	 *  ���磺the IMEI/SV(software version) for GSM phones.  
	 * @return Return null if the software version is not available.   
	 */
	public String getDeviceSoftwareVersion(){
		return mTelephonyManager.getDeviceSoftwareVersion();
	}
	
	/**
	 * �豸����Ļ�ֱ���
	 * @return  ��800x480
	 */
	public String getDisplayMetrics(){
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager mWindowManager = (WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE);
		mWindowManager.getDefaultDisplay().getMetrics(dm);  
		
		return dm.heightPixels + "x" + dm.widthPixels;   
	}
	
	/**
	 * ���ص�ǰ�ֻ���ͺ�
	 * @return �� Mailstone
	 */
	public String getDeviceModel(){
		return  android.os.Build.MODEL;
	}
	
	/**
	 * ��ȡ�豸ϵͳSDK�汾��
	 * @return eg:8
	 */
	public static String getDeviceVersionSDK(){
		return  android.os.Build.VERSION.SDK;
	} 
	
	
	/**
	 * ��ȡ�豸ϵͳ�����汾��
	 * @return   eg:2.3.3
	 */
	public static String getDeviceVersionRelease(){
		return  android.os.Build.VERSION.RELEASE;
	} 	
	
	/**
	 * ���ص�ǰ����汾��
	 * @param context
	 * @return  android:versionCode="1" 	android:versionName="1.0" {1,1.0}
	 */
	public static String[] getAppVersionName(Context context) {     
	   String version[] = new String[2];
	   try {     
	       // ---get the package info---     
	       PackageManager pm = context.getPackageManager();     
	       PackageInfo pi = pm.getPackageInfo(context.getPackageName(),0);     
	       version[0] = String.valueOf(pi.versionCode);
	       version[1] = pi.versionName;   
	              
	    } catch (Exception e) {     
	        Log.e("VersionInfo", "Exception", e); 
	        Logger.e(e);
	    }  
	   return version;
	} 
	
	/**
	 * ��ȡ��ǰ����״̬
	 */
	public NetStatus getNetStatus(){
		ConnectivityManager connMgr = null;
		NetworkInfo activeInfo = null;
		
		try {
			connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
			activeInfo = connMgr.getActiveNetworkInfo(); 			//��ǰ�������������ж�
		} catch (Exception e) {
			Logger.e(e);
		}
		
		if (activeInfo != null && activeInfo.isConnected()) {	//����������(����ʲô��������)
			switch (activeInfo.getType()) {
			case ConnectivityManager.TYPE_MOBILE:			//�ƶ�����
				return NetStatus.MOBILE;
			case ConnectivityManager.TYPE_WIFI:				//Wi-Fi����
				return NetStatus.WIFI;
			default:	
				return NetStatus.Disable;
			}
		}
		else{		//���粻����
			return NetStatus.Disable;
		}
	}
	
	 /**
     * ��ȡ�ֻ��ڲ�ʣ��洢�ռ�
     * @return
     */
    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * ��ȡ�ֻ��ڲ��ܵĴ洢�ռ�
     * @return
     */
    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    /**
     * ��ȡSDCARDʣ��洢�ռ�
     * @return
     */
    public static long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return ERROR;
        }
    }

    /**
     * ��ȡSDCARD�ܵĴ洢�ռ�
     * @return
     */
    public static long getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return ERROR;
        }
    }
    
    /**
     * SDCARD�Ƿ����
     */
    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }
    
    public static String GetBuildProproperties(String propertiesName){
    	try {
    		InputStream is = new BufferedInputStream(new FileInputStream(new File("/system/build.prop")));
    		BufferedReader br = new BufferedReader(new InputStreamReader(is));
    		String strTemp = "";
    		while ((strTemp = br.readLine()) != null){// ����ļ�û�ж��������
    			if (strTemp.indexOf(propertiesName) != -1){
    				return strTemp.substring(strTemp.indexOf("=") + 1);
    			}
    		}
    		br.close();
    		is.close();
    		return null;
		} catch (Exception e) {
				 e.printStackTrace();
				 return null;
		}
	}

    public boolean validateServerAddressForSet(String ipStr,Preference pLoginServer,SharedPreferencesHelper sharedPreferencesHelper,Context context) {
		//IP��ַ����
		Pattern p=Pattern.compile("(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d)){3}");
		Matcher m=p.matcher(ipStr);
		String ipRight="";
		//�������
		Pattern pp=Pattern.compile("[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\\.?");
		Matcher mm=pp.matcher(ipStr);
		
		int result=0;
		result+=ipStr.indexOf(".com");
		result+=ipStr.indexOf(".net");
		result+=ipStr.indexOf(".org");
		result+=ipStr.indexOf(".gov");
		result+=ipStr.indexOf(".edu");
		result+=ipStr.indexOf(".mil");
		result+=ipStr.indexOf(".int");	
		
		if(result==-7){
			if(m.find())
			{
				if(!ipStr.endsWith("/")){
					ipRight=ipStr.substring(0,m.start()).toLowerCase()+m.group()+ipStr.substring(m.end(),ipStr.length())+"/";
				}else
				{
					ipRight=ipStr.substring(0,m.start()).toLowerCase()+m.group()+ipStr.substring(m.end(),ipStr.length());	
				}
				
				pLoginServer.setSummary(ipRight);
				
				sharedPreferencesHelper.SaveCommonPreferenceSettings(PreferenceKeys.Sys_LoginServer.toString(),PreferenceType.String,ipRight);
				return false;			//ʹ���Լ��ı��棬����ϵͳ����
				 
			}else{
				ToastHelper.showToast(ToastHelper.getStringFromResources(R.string.addressError), Toast.LENGTH_SHORT);
				return false;
			}
		}else{
			if(mm.find())
			{
				if(!ipStr.endsWith("/")){
					ipRight=ipStr.substring(0,mm.start()).toLowerCase()+mm.group()+ipStr.substring(mm.end(),ipStr.length())+"/";
				}else
				{
					ipRight=ipStr.substring(0,mm.start()).toLowerCase()+mm.group()+ipStr.substring(mm.end(),ipStr.length());	
				}
				pLoginServer.setSummary(ipRight);
				
				sharedPreferencesHelper.SaveCommonPreferenceSettings(PreferenceKeys.Sys_LoginServer.toString(),PreferenceType.String,ipRight);
				return false;			//ʹ���Լ��ı��棬����ϵͳ����
			}else{
				ToastHelper.showToast(ToastHelper.getStringFromResources(R.string.addressError), Toast.LENGTH_SHORT);
				return false;
			}
		}
		
		
	}
    
    public String validateServerAddressForLogin(String ipStr,Context context) {
    	//IP��ַ����
		Pattern p=Pattern.compile("(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d)){3}");
		Matcher m=p.matcher(ipStr);
		String ipRight="";
		//�������
		Pattern pp=Pattern.compile("[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\\.?");
		Matcher mm=pp.matcher(ipStr);
		
		int result=0;
		result+=ipStr.indexOf(".com");
		result+=ipStr.indexOf(".net");
		result+=ipStr.indexOf(".org");
		result+=ipStr.indexOf(".gov");
		result+=ipStr.indexOf(".edu");
		result+=ipStr.indexOf(".mil");
		result+=ipStr.indexOf(".int");	
		
		if(result==-7){
			if(m.find())
			{
				if(!ipStr.endsWith("/")){
					ipRight=ipStr.substring(0,m.start()).toLowerCase()+m.group()+ipStr.substring(m.end(),ipStr.length())+"/";
				}else
				{
					ipRight=ipStr.substring(0,m.start()).toLowerCase()+m.group()+ipStr.substring(m.end(),ipStr.length());	
				}
			}else
			{
				ToastHelper.showToast(ToastHelper.getStringFromResources(R.string.addressError), Toast.LENGTH_SHORT);
				
			}
		}else{
			if(mm.find())
			{
				if(!ipStr.endsWith("/")){
					ipRight=ipStr.substring(0,mm.start()).toLowerCase()+mm.group()+ipStr.substring(mm.end(),ipStr.length())+"/";
				}else
				{
					ipRight=ipStr.substring(0,mm.start()).toLowerCase()+mm.group()+ipStr.substring(mm.end(),ipStr.length());	
				}
			}else
			{
				ToastHelper.showToast(ToastHelper.getStringFromResources(R.string.addressError), Toast.LENGTH_SHORT);
				
			}
		}
		
		return  ipRight;
	}
}
