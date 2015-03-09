package com.cuc.miti.phone.xmc.utils;

import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.PositionInfo;

public class BaiduLocationHelper {

	private Context context;										//�����Ļ���
	private LocationClient locationClient = null;
	private MyLocationListener mListener;
	private static final int UPDATE_TIME = 5000;		//��ȡ��λ��Ϣ��ʱ����
//	private static final String KEY = "sH1oGWhpvZAiB9LbW9dHU2hf";		//Debug
	private static final String KEY = "CFSHlenYprrxkVaX6LdgXlGc";		//Xinhuashe
//	private static final String KEY = "5lEbCEcFl4oGlGffybTfDdtm";
		
	private PositionInfo positionInfo; 
	
	
	/**
	 * ���캯��
	 * @param context
	 */
	public BaiduLocationHelper(Context context){
		this.context = IngleApplication.getInstance();
		positionInfo = new PositionInfo();
		mListener = new MyLocationListener();
		this.initialize();
	}
	
	/**
	 * ��ʼ��
	 */
	private void initialize(){
		//��ݴ���Activity Contextʵ��Baidu����λ�÷������LocationClient
		locationClient = new  LocationClient(this.context);
		
		locationClient.setAK( KEY );				//key:�ڿ�������վ��Ӧ��APP�������AccessKey
			
		//���ö�λ����
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);				//�Ƿ��GPS
		option.setAddrType("all");				//���صĶ�λ�����ַ��Ϣ
        option.setCoorType("bd09ll");		//���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02
        option.setPriority(LocationClientOption.NetWorkFirst);	//���ö�λ���ȼ�
        option.setProdName("eNews");	//���ò�Ʒ����ơ�
        option.setScanSpan(UPDATE_TIME);    //���ö�ʱ��λ��ʱ��������λ����
        
//        option.setPoiNumber(5);    //��෵��POI����   
//        option.setPoiDistance(1000); //poi��ѯ����        
//        option.setPoiExtraInfo(true); //�Ƿ���ҪPOI�ĵ绰�͵�ַ����ϸ��Ϣ        
        locationClient.setLocOption(option);
        
        locationClient.registerLocationListener( mListener );    //ע�������
	}
	
	/**
	 * ��λ������
	 * @author SongQing
	 *  1.�����첽���صĶ�λ��������BDLocation���Ͳ���
	 *  2.�����첽���ص�POI��ѯ��������BDLocation���Ͳ���
	 */
	public class MyLocationListener implements BDLocationListener {

		public void onReceiveLocation(BDLocation location) {
    		
			if (location == null) {
				return;
			}			
			
			if(!String.valueOf(location.getLatitude()).equals("4.9E-324" )){
				positionInfo.setLatitude(location.getLatitude());
				positionInfo.setAddress(location.getAddrStr());
				positionInfo.setAltitude(location.getAltitude());
				positionInfo.setLongitude(location.getLongitude());
			}
			
		}
    	
		public void onReceivePoi(BDLocation location) {
		}
		
	}
	
	/**
	 *  ��ʼ��λ����
	 * @return
	 */
	public boolean startLocationClient(){
		
		if(locationClient!=null){
			locationClient.start();
		}else{
			initialize();
		}
		
		if(locationClient!=null && locationClient.isStarted()){
			/*
			 *�����������ֵ���ڵ���1000��ms��ʱ����λSDK�ڲ�ʹ�ö�ʱ��λģʽ��
			 *����requestLocation( )��ÿ���趨��ʱ�䣬��λSDK�ͻ����һ�ζ�λ��
			 *���λSDK��ݶ�λ���ݷ���λ��û�з���仯���Ͳ��ᷢ����������
			 *������һ�ζ�λ�Ľ�������λ�øı䣬�ͽ�������������ж�λ���õ��µĶ�λ���
			 *��ʱ��λʱ������һ��requestLocation���ᶨʱ����λ���
			 */
			locationClient.requestLocation();
			return true;
		}else{
			return  false;
		}
	}
	
	/**
	 * ֹͣ��λ����
	 */
	public void stopLocationClient(){

		 if (locationClient != null && locationClient.isStarted()) {
			 locationClient.stop();	
		 } 
		 
		 locationClient = null;
	}
	
	/**
	 * ��ȡ��ǰ�ĵ���λ����Ϣ
	 * @return
	 */
	public PositionInfo getCurrentLocation(){
		return positionInfo;
	}
	
	/**
	 * ��ݴ���ľ�γ��ֵ��ȡ��Ӧ��λ����Ϣ
	 * @param latitudeStr 
	 * @param longitudeStr
	 * @return
	 */
	public List<Address> GetGeocoder(String latitudeStr,String longitudeStr){
		Geocoder geoCoder = new Geocoder(context); 
		try {
		 
			int latitude = Integer.parseInt(latitudeStr);
			int longitude = Integer.parseInt(longitudeStr);
			 
			List<Address> listAddress = geoCoder.getFromLocation(latitude, longitude, 2);
			return listAddress;
		} catch (Exception ex) {
			Logger.e(ex);
			return null;
		}
	}
}
