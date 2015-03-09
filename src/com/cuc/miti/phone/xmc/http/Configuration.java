package com.cuc.miti.phone.xmc.http;

import java.util.HashMap;
import java.util.Properties;

/*
 * ������Ϣ
 */

public class Configuration {

	private static Properties defaultProperties;
	private static HashMap<String, String> hashMapAppAddress; // �ӷ��������صĽ����½��������ַ�б?�������ӣ��ɹ���½��ѡ��
	private static String DEFAULT_SERVER = "http://202.84.17.49/"; // Ĭ�Ϸ�����(��ʽ)2013-6-13Ӧ�»���Ҫ����50��Ϊ49

	static {
		init();
	}

	static void init() {

		defaultProperties = new Properties();
		defaultProperties.setProperty("xmc.http.retryCount", "1");
		defaultProperties.setProperty("xmc.http.initialUrl", DEFAULT_SERVER
				+ "client/wu"); // �»�����ʽ�ӿڣ�SPLASHҳ��ʱ��ȡ��¼������URL�б�
		defaultProperties.setProperty("xmc.http.baseUrl", DEFAULT_SERVER
				+ "client/c"); // �»���ӿ� ��¼����ȡ�û���Ϣ������
		defaultProperties.setProperty("xmc.http.sinosoftUrl", DEFAULT_SERVER
				+ "client/v2c"); // �»����п���ӿ� ���ȡ�û���ǩ��Ϣ������ݸ��¡���Ϣ����
		defaultProperties.setProperty("xmc.http.TopicDownloadUrl",
				DEFAULT_SERVER + "client/v2c"); // ����ļ�ͬ���ӿ�
		defaultProperties.setProperty("xmc.http.systemUpgradeUrl",
				DEFAULT_SERVER + "download/xinhuamobile/XinHuaManuscript.apk"); // ϵͳ��·��
		defaultProperties.setProperty("xmc.http.OAInfoListUrl", DEFAULT_SERVER
				+ "client/v2c"); // OA��Ϣ�б��ȡ�ӿ�
		defaultProperties.setProperty("xmc.http.OAInfoItemUrl", DEFAULT_SERVER
				+ "client/v2c"); // OA��Ϣ��Ŀ��ȡ�ӿ�
		// defaultProperties.setProperty("xmc.http.testUrl",
		// "http://10.2.1.100/s/client/i"); // ��Ϣ����ǩ����������ַ������ݸ���
		defaultProperties.setProperty("xmc.http.locationUrl", DEFAULT_SERVER
				+ "client/v2c"); // ��λ����
		defaultProperties.setProperty("xmc.http.instantUploadUrl",
				DEFAULT_SERVER + "client/v2c"); // ���ļ���Ȩ��У��
		defaultProperties.setProperty("xmc.http.recommendMessageUrl",
				DEFAULT_SERVER + "sys/ui"); // ���ļ���Ȩ��У��
	}

	/**
	 * ��ȡ��ǰĬ�ϵķ�������ַ
	 * 
	 * @return
	 */
	public static String getDefaultServer() {
		return DEFAULT_SERVER;
	}

	public static String getRecommendMessageUrl() {
		return getProperties("xmc.http.recommendMessageUrl");
	}

	public static String getInstantUploadUrl() {
		return getProperties("xmc.http.instantUploadUrl");
	}

	public static String getSinosoftUrl() {
		return getProperties("xmc.http.sinosoftUrl");
	}

	public static String getlocationUrl() {
		return getProperties("xmc.http.locationUrl");
	}

	public static String getInitialUrl() {
		return getProperties("xmc.http.initialUrl");
	}

	public static String getBaseUrl() {
		return getProperties("xmc.http.baseUrl");
	}

	public static String getSystemUpgradeUrl() {
		return getProperties("xmc.http.systemUpgradeUrl");
	}

	public static String getOAInfoListUrl() {
		return getProperties("xmc.http.OAInfoListUrl");
	}

	public static String getOAInfoItemUrl() {
		return getProperties("xmc.http.OAInfoItemUrl");
	}

	public static void setRecommendMessageUrl(String urlStr) {
		defaultProperties.setProperty("xmc.http.recommendMessageUrl", urlStr);
	}

	public static void setTopicDownloadUrl(String urlStr) {
		defaultProperties.setProperty("xmc.http.TopicDownloadUrl", urlStr);
	}

	public static void setlocationUrl(String urlStr) {
		defaultProperties.setProperty("xmc.http.locationUrl", urlStr);
	}

	public static void setInstantUploadUrl(String urlStr) {
		defaultProperties.setProperty("xmc.http.instantUploadUrl", urlStr);
	}

	public static String getTopicDownloadUrl() {
		return getProperties("xmc.http.TopicDownloadUrl");
	}

	public static void setInitialUrl(String urlStr) {
		defaultProperties.setProperty("xmc.http.initialUrl", urlStr);
	}

	public static void setBaseUrl(String urlStr) {
		defaultProperties.setProperty("xmc.http.baseUrl", urlStr);
	}

	public static void setSinosoftUrl(String urlStr) {
		defaultProperties.setProperty("xmc.http.sinosoftUrl", urlStr);
	}

	public static void setOAInfoListUrl(String urlStr) {
		defaultProperties.setProperty("xmc.http.OAInfoListUrl", urlStr);
	}

	public static void setOAInfoItemUrl(String urlStr) {
		defaultProperties.setProperty("xmc.http.OAInfoItemUrl", urlStr);
	}

	// public static String getTestUrl() {
	// return getProperties("xmc.http.testUrl");
	// }

	public static int getRetryCount() {
		return Integer.parseInt(getProperties("xmc.http.retryCount"));
	}

	public static String getProperties(String name) {
		return defaultProperties.getProperty(name);

	}

	public static HashMap<String, String> getHashMapAppAddress() {
		return hashMapAppAddress;
	}

	public static void setHashMapAppAddress(
			HashMap<String, String> hashMapAppAddress) {
		Configuration.hashMapAppAddress = hashMapAppAddress;
	}
}
