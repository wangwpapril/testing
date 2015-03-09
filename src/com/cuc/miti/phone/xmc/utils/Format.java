package com.cuc.miti.phone.xmc.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Format {

	/**
	 * @Destription ȥ���ַ��еĿո񡢻س������С��Ʊ��
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str){
		
		Pattern pattern=Pattern.compile("\\s*|\t|\r|\n");
		Matcher matcher=pattern.matcher(str);
		
		return matcher.replaceAll("");
	}
}
