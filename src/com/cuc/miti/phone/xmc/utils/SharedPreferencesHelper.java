package com.cuc.miti.phone.xmc.utils;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceType;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * �ṩ����ݵĳ־û��洢���洢Ŀ���ǳ����SharedPreference
 * @author SongQing
 *
 */
public class SharedPreferencesHelper{

	private SharedPreferences settings;
	private SharedPreferences userSettings;
	private SharedPreferences.Editor editor ;
	private Context context;
	private String userName;
	
	public SharedPreferencesHelper(Context context){
		this.context = context;
		//��ȡһ�����Preferences ����
		settings=PreferenceManager.getDefaultSharedPreferences(context);
		userName = IngleApplication.getInstance().getCurrentUser();
		userSettings = this.context.getSharedPreferences(userName, Activity.MODE_PRIVATE);
	}
	
	/**
	 * ��ݴ����Preference Key����ϵͳƫ�������ж�Ӧ��Value
	 * @param key    	Preferences Key Ψһ��ʾһ��������
	 * @return
	 */
	public String getPreferenceValue(String key){
		String returnValue = "";
		try {
			returnValue = settings.getString(key, "");
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			Logger.e(ex);
			ex.printStackTrace();
		}		
		return returnValue;
	}
		
	/**
	 * ����ȫ�ֵ�ƫ������(���û��޹�)
	 * @param key		Preferences Key Ψһ��ʾһ��������
	 * @param pType	Preference֧�ֶ�������������ͣ�Boolean/Int/Float/Long/String
	 * @param value	Preference�������Ӧֵ
	 */
	public boolean SaveCommonPreferenceSettings(String key,PreferenceType pType,String value){
		try{
			//settings=this.context.getSharedPreferences("systemuser", Activity.MODE_PRIVATE);
			editor = settings.edit();		
			switch(pType){
				case Boolean :
					editor.putBoolean(key, Boolean.parseBoolean(value));
					break;
				case Int:
					editor.putInt(key, Integer.parseInt(value));
					break;
				case Float:
					editor.putFloat(key, Float.parseFloat(value));
					break;
				case Long:
					editor.putLong(key, Long.parseLong(value));
					break;
				case String:
					editor.putString(key, value);
					break;
				default:
					break;	
			}			
			editor.commit();
			return true;
		}catch(Exception ex){
			Logger.e(ex);
			return false;
		}
	}
	
	
	/**
	 * ��ݴ����Preference Key�����û�ƫ�������ж�Ӧ��Value
	 * @param key    	Preferences Key Ψһ��ʾһ��������
	 * @return
	 */
	public String GetUserPreferenceValue(String key){
		String returnValue = "";
		try {
			returnValue = userSettings.getString(key, "");
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			Logger.e(ex);
			ex.printStackTrace();
		}
		
		return returnValue;
	}
	/**
	 * ���𱣴��û��Լ���ƫ������(ÿ���û�����һ��ר�õ�ƫ�������ļ�)
	 * @param key		     	Preferences Key Ψһ��ʾһ��������
	 * @param pType	     	Preference֧�ֶ�������������ͣ�Boolean/Int/Float/Long/String
	 * @param value			Preference�������Ӧֵ
	 * @return
	 */
	public boolean SaveUserPreferenceSettings(String key,PreferenceType pType,String value){
		try{
			editor = userSettings.edit();		
			switch(pType){
				case Boolean :
					editor.putBoolean(key, Boolean.parseBoolean(value));
					break;
				case Int:
					editor.putInt(key, Integer.parseInt(value));
					break;
				case Float:
					editor.putFloat(key, Float.parseFloat(value));
					break;
				case Long:
					editor.putLong(key, Long.parseLong(value));
					break;
				case String:
					editor.putString(key, value);
					break;
				default:
					break;	
			}	
			editor.commit();
			return true;
		}catch(Exception ex){
			Logger.e(ex);
			return false;
		}
	}
}
