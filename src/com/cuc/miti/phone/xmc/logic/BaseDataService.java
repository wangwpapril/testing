package com.cuc.miti.phone.xmc.logic;


import android.content.Context;
import android.support.v4.content.Loader.ForceLoadContentObserver;

import com.cuc.miti.phone.xmc.domain.Enums.BaseDataFileType;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceType;
import com.cuc.miti.phone.xmc.domain.XmcException;
import com.cuc.miti.phone.xmc.http.RemoteCaller;
import com.cuc.miti.phone.xmc.utils.SharedPreferencesHelper;

/**
 * �������ز�����
 * @author SongQing
 *
 */
public class BaseDataService {

	private Context context;
	private SharedPreferencesHelper spHelper;
	
	public BaseDataService(Context context){
		this.context = context;
	}
	
	/**
	 * ��ȡ��ǰϵͳ��ʹ�õĻ���ݵ��ļ����ַ�(��SharePreference�л�ȡ)
	 * @return (xxxx,xxxx,xxxx,xxxx,xxxx)
	 */
	public String GetCurrentBDFilesNameString(){
		
		StringBuilder sb = new StringBuilder();
		spHelper = new SharedPreferencesHelper(context);
		
		for(BaseDataFileType bd : BaseDataFileType.values()){ 
			String tempStr = spHelper.getPreferenceValue(bd.toString());
			if(tempStr !=""){
				sb.append(spHelper.getPreferenceValue(bd.toString()));
				sb.append(",");
			}	
		}
		if(sb.length() !=0){
			sb.delete(sb.length()-1, sb.length());
		}
		
		return sb.toString();
	}
	
	/**
	 * ����ϵͳʹ�û�������°��ļ������(�洢��SharePreference)
	 * @param fileType   ���ļ��ķ��࣬��Department��Language��
	 * @param fileName ���³ɹ����ļ���
	 * @return
	 */
	public boolean SetCurrentBDFileName(BaseDataFileType fileType ,String fileName){
		boolean returnValue = false;
		if(fileType != null && fileName !=null && fileName!=""){
			spHelper = new SharedPreferencesHelper(context);
			
			returnValue = spHelper.SaveCommonPreferenceSettings(fileType.toString(), PreferenceType.String,fileName);
		}
		return returnValue;
	}
}
