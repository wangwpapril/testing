package com.cuc.miti.phone.xmc.http;

import java.net.Authenticator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Base64;

public class HttpHelper {

	public static final int OK = 200;// OK: Success!
	public static final int NOT_MODIFIED = 304;
	public static final int BAD_REQUEST = 400;
	public static final int NOT_AUTHORIZED = 401;
	public static final int FORBIDDEN = 403;
	public static final int NOT_FOUND = 404;
	public static final int NOT_ACCEPTABLE = 406;
	public static final int INTERNAL_SERVER_ERROR = 500;
	public static final int BAD_GATEWAY = 502;
	public static final int SERVICE_UNAVAILABLE = 503;
	
	public static final int CONNECT_TIMEOUT_EXCEPTION= 10010;				//�ͻ����Ѿ��������������socket���ӣ����Ƿ�������û�д���ͻ��˵�����
	public static final int INTERRUPTED_IO_EXCEPTION = 10011;				//����ͷ���������socket���ӣ����Ǻܳ�ʱ���ڶ�û�н���socket����
	public static final int TIMEOUT = 10013;
	
	public static final int NoSuchAlgorithmException = 10014;
	public static final int KeyManagementException = 10015;
	
	public static int downloadSize = 0;
	
	public static String TAG = "MITI APPLICATION";
}
