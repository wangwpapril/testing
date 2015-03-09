package com.cuc.miti.phone.xmc.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.transform.Templates;

import android.util.Log;

/**
 * �ӽ�����
 * @author SongQing
 *
 */
public class Encrypt {
	
	/**
	 * MD5���ܣ�32λ
	 * @param plaintext
	 * @return
	 */
	public static String toMD5(String plaintext){
		 MessageDigest md5 = null;
		 try {
	            md5 = MessageDigest.getInstance("MD5");
	      } catch (Exception e) {
	    	  e.printStackTrace();
	    	  return "";
	      }
		 char[] charArray = plaintext.toCharArray();
		 byte[] byteArray = new byte[charArray.length];
		 for (int i = 0; i < charArray.length; i++) {
			 byteArray[i] = (byte) charArray[i];
		 }
		 byte[] md5Bytes = md5.digest(byteArray);
		 StringBuffer hexValue = new StringBuffer();
		 for (int i = 0; i < md5Bytes.length; i++) {
			 int val = ((int) md5Bytes[i]) & 0xff;
			 if (val < 16) {
				 hexValue.append("0");
			 }
			 hexValue.append(Integer.toHexString(val));
		 }
		 return hexValue.toString();
	}
	
	public static String toMd5(byte[] bytes) {

		char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
		char str[]=new char[16*2];
		
		try {
			MessageDigest md=MessageDigest.getInstance("MD5");
			md.update(bytes);
			
			byte tmp[]=md.digest();
			
			int k=0;
			for(int i=0;i<16;i++){
				byte byte0=tmp[i];
				str[k++]=hexDigits[byte0>>>4&0xf];
				str[k++]=hexDigits[byte0&0xf];
			}

		} catch (NoSuchAlgorithmException e) {
			Log.v("he--------------------------------ji", "toMd5(): " + e);
			throw new RuntimeException(e);
			// 05-20 09:42:13.697: ERROR/hjhjh(256):
			// 5d5c87e61211ab7a4847f7408f48ac
		}
		
		return new String(str);
	}
	
	/**
	 * MD5У�������
	 * @param filename
	 * @return
	 */
	public static String toMd5(String filePath) {
		InputStream inputStream;

		byte[] buffer = new byte[1024];
		int numRead = 0;
		
		MessageDigest md5;
		
		try{
			inputStream = new FileInputStream(filePath);
			md5 = MessageDigest.getInstance("MD5");
			while((numRead=inputStream.read(buffer)) > 0) {
				md5.update(buffer,0,numRead);
				}
			inputStream.close();
			
			return toHexString(md5.digest()); 
		} catch (Exception e) {
			Logger.e(e);
			return null;
		}
	}

	public static String toHexString(byte[] b) {
		char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(hexDigits[(b[i] & 0xf0) >>> 4]);
			sb.append(hexDigits[b[i] & 0x0f]);
		}
		return sb.toString();
	}



}
