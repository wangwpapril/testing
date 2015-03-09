package com.cuc.miti.phone.xmc.utils;

import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Criteria;
import android.os.Bundle;

/**
 * ��ȡ�û�λ����Ϣ������
 * ˵�����÷����ܹ����ʵʱ�������λ��״̬�������Ԥ�����úõ��ж��������Զ���ѡ��ѵ�λ�û�ȡ��ʽ
 * @author SongQing
 *
 */
public class LocationHelper {
	
	private Context context;										//�����Ļ���
	private LocationManager locationManager; 		//λ�ù������
	private String currentProvider;								//λ�û�ȡ���������ṩ�����
	private Location currentLocation;							//λ����ݶ���
	
	public LocationHelper(Context context){
		this.context = context;
		this.Initialize();
	}
	
	private  void Initialize(){
		//��ݴ���Activity Contextʵ��ϵͳ�������LocationManager
		locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		
		//����һ��criteria���󣬸ö����ƶ�����Ԥ���λ�÷�����ѡ����
		Criteria  criteria = new Criteria();
		
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);			//���ô��Ծ�ȷ��
		criteria.setAltitudeRequired(true);    								//������Ҫ���غ�����Ϣ
        criteria.setBearingRequired(false); 								//���ò���Ҫ���ط�λ��Ϣ
        criteria.setCostAllowed(true);    									//������������ʷ� 
        criteria.setPowerRequirement(Criteria.POWER_LOW);	//���õ�����ĵȼ�
        criteria.setSpeedRequired(false); 								 	//�����Ƿ���Ҫ�����ٶ���Ϣ
        
        //������õ�Criteria���󣬻�ȡ���ϴ˱�׼��provider����
        currentProvider = locationManager.getBestProvider(criteria, true);
	}

	/**
	 * ��ȡ��ǰ��λ��
	 * @return   latitude,longitude
	 */
	public String GetLocation(){
		//��ݵ�ǰprovider�����ȡ���һ��λ����Ϣ
		try {
			currentLocation = locationManager.getLastKnownLocation(currentProvider);
		} catch (Exception e) {
			Logger.e(e);
			return "0,0";
		}
        
        //���λ����ϢΪnull�����������λ����Ϣ
        if(currentLocation == null){
        	locationManager.requestLocationUpdates(currentProvider, 0, 0, locationListener);
        }
        
        currentLocation = locationManager.getLastKnownLocation(currentProvider);
        if(currentLocation != null){
        	String latitudeStr = String.valueOf(currentLocation.getLatitude());
        	String longitudeStr = String.valueOf(currentLocation.getLongitude());
        	Logger.d("LocationHelper �� " + "Latitude_" + currentLocation.getLatitude());
        	Logger.d("LocationHelper �� " + "Longitude_" + currentLocation.getLongitude());        		 
        	return  latitudeStr + "," + longitudeStr;
        }else{
        	return "0,0";
        }
	}
	public Location getCurrentLocation(){
		try {
			currentLocation = locationManager.getLastKnownLocation(currentProvider);
		} catch (Exception e) {
			Logger.e(e);
			return null;
		}
        
        //���λ����ϢΪnull�����������λ����Ϣ
        if(currentLocation == null){
        	locationManager.requestLocationUpdates(currentProvider, 0, 0, locationListener);
        }
        
        currentLocation = locationManager.getLastKnownLocation(currentProvider);
		return currentLocation;
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
//			for(int i=0; i<list.size(); i++){
//				address address = list.get(i); 
//				Toast.makeText(MainActivity.this, address.getCountryName() + address.getAdminArea() + address.getFeatureName(), Toast.LENGTH_LONG).show();
		} catch (Exception ex) {
			Logger.e(ex);
			return null;
		}
	}
	/**
	 * ����λ�ü�����
	 */
	private LocationListener locationListener = new LocationListener(){
		
		//λ�÷���ı�ʱ����
		public void onLocationChanged(Location location) {
			Logger.d("LocationHelper : " + "onLocationChanged" );
			Logger.d("LocationHelper : " + "onLocationChanged Latitude" + location.getLatitude() );
			Logger.d("LocationHelper : " + "onLocationChanged location" + location.getLongitude() );		 
		}
		
		//providerʧЧʱ����
		public void onProviderDisabled(String provider) {
			Logger.d("LocationHelper : " + "onProviderDisabled" );
		}	
		
		//provider����ʱ����
		public void onProviderEnabled(String provider) {
			Logger.d("LocationHelper : " + "onProviderEnabled");	 
		}
		
		 //״̬�ı�ʱ����
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Logger.d("LocationHelper : " + "onStatusChanged");	 
		}
	};
	
}
