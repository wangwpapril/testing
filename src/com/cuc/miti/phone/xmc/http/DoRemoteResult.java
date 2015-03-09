package com.cuc.miti.phone.xmc.http;

import com.cuc.miti.phone.xmc.IngleApplication;

import android.util.Log;

public class DoRemoteResult {

	public static String[] doResult(String result) {

		Log.i(IngleApplication.TAG, result);
		
		String [] resultStrings  = null;
		String [] tempStrings1 = null; 
		String [] tempStrings2 = null;
		if(result !=null){
			if(result.indexOf("||") >0){
				tempStrings1 =result.split("\\|\\|");
				
				if(tempStrings1[1].indexOf("^") >0){
					tempStrings2 = tempStrings1[1].split("\\^");
					resultStrings = new String[tempStrings2.length + 1];
					resultStrings[0] = tempStrings1[0];
					
					for (int i = 0; i < tempStrings2.length; i++) {
						resultStrings[i + 1] = tempStrings2[i];
					}					
				}else{
					resultStrings = tempStrings1;
				}
			}else{
				resultStrings= new String[] { result };
			}
		}
		
		return resultStrings;
	}
	
	
	
//	String[] ss = null;
//	
//	if(result==null)
//		return null;
//
//	if (result.indexOf("||") > 0) {
//		String[] ss1 = result.split("\\|\\|");
//
//		if (ss1[1].indexOf("^") > 0) {
//
//			String[] ss2 = ss1[1].split("\\^");
//
//			ss = new String[ss2.length + 1];
//			ss[0] = ss1[0];
//
//			for (int i = 0; i < ss2.length; i++) {
//				ss[i + 1] = ss2[i];
//			}
//
//		} else {
//			return ss1;
//		}
//	} else {
//		return ss = new String[] { result };
//	}
//	
//	return ss;
}
