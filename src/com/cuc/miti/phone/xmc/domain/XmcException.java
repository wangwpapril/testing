package com.cuc.miti.phone.xmc.domain;

public class XmcException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	
	
	public static final String CONNECT_TIMEOUT_EXCEPTION= "ConnectionTimeout";				//�ͻ����Ѿ��������������socket���ӣ����Ƿ�������û�д���ͻ��˵�����
	public static final String INTERRUPTED_IO_EXCEPTION = "ConnectionTimeout";					//����ͷ���������socket���ӣ����Ǻܳ�ʱ���ڶ�û�н���socket����
	public static final String TIMEOUT = "Timeout";
	
	public XmcException(String msg){
		super(msg);
	}
	
//	public XmcException(String msg,int statusCode){
//		super(msg);
//	}
	
	public XmcException(String msg,Exception cause){
		super(msg,cause);
	}
	
//	public XmcException(String msg,Exception cause,int statusCode){
//		super(msg,cause);
//	}
	
	
}
