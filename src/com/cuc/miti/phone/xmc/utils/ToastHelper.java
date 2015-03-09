package com.cuc.miti.phone.xmc.utils;

import com.cuc.miti.phone.xmc.IngleApplication;

import android.widget.Toast;

public class ToastHelper {
	
	private static Toast toast;
	/**
	 * Toast��ʾ�����Ϣ
	 * 
	 * @param message
	 */
	public static void showToast(String message,int toastLength) {
		if(message.equals("")){	//˵����������ʾToast��
			if(toast!=null){
				toast.cancel();
			}
			return;
		}
		if(toast == null){
			toast = Toast.makeText(IngleApplication.getInstance(), message, toastLength);
		}else{
			toast.setText(message);
		}
		toast.show();
	}
	
	/**
	 * ͨ��ResourceID��ȡ��Ӧ����
	 * @param resId
	 * @return
	 */
	public static String getStringFromResources(int resId){
		return IngleApplication.getInstance().getString(resId);
	}
}
