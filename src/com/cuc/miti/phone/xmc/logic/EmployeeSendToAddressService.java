package com.cuc.miti.phone.xmc.logic;

import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.dao.EmployeeSendToAddressDao;
import com.cuc.miti.phone.xmc.domain.EmployeeSendToAddress;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.SendToAddress;

import android.content.Context;

public class EmployeeSendToAddressService {
	private Context context;
	private EmployeeSendToAddressDao employeeSendToAddressDao;
	
	public EmployeeSendToAddressService(Context context){
		this.context = context;
		this.employeeSendToAddressDao = new EmployeeSendToAddressDao(context);
	}
	
	public boolean addEmployeeSendToAddress(EmployeeSendToAddress employeeSendToAddress){
		return employeeSendToAddressDao.addEmployeeSendToAddress(employeeSendToAddress);
	}
	public boolean deleteEmployeeSendToAddress(String name){
		return employeeSendToAddressDao.deleteEmployeeSendToAddress(name);
	}
	public boolean deleteAllEmployeeSendToAddress(){
		return employeeSendToAddressDao.deleteAllEmployeeSendToAddress();
	}
	public boolean updateEmployeeSendToAddress(EmployeeSendToAddress employeeSendToAddress){
		return employeeSendToAddressDao.updateEmployeeSendToAddress(employeeSendToAddress);
	}
	public EmployeeSendToAddress getEmployeeSendToAddress(String name){
		return employeeSendToAddressDao.getEmployeeSendToAddress(name);
	}
	public List<EmployeeSendToAddress> getEmployeeSendToAddressList(String queryString){
		return employeeSendToAddressDao.getEmployeeSendToAddressList("zh-CHS",IngleApplication.getInstance().getCurrentUser(),queryString);
	}
	public List<EmployeeSendToAddress> getEmployeeSendToAddressByPage(Pager pager){
		return employeeSendToAddressDao.getEmployeeSendToAddressByPage(pager);
	}
	public long getEmployeeSendToAddressCount(){
		return employeeSendToAddressDao.getEmployeeSendToAddressCount();
	}
	public EmployeeSendToAddress getEmployeeSendToAddressById(int id){
		return employeeSendToAddressDao.getEmployeeSendToAddressById(id);
	} 
	
	/**
	 * ��ȡ����ѡ��Ļ���ݶ����б�
	 * @return
	 */
	public List<KeyValueData> getBaseDataForBind(){
		//TODO ��Ҫ��ȡ��ǰ�ֻ��ϵͳ���������ò�ͬ�����ֲ�ѯ��Ŀǰ��д�������ġ�
		String loginnameString = IngleApplication.getInstance().getCurrentUser();
		List<EmployeeSendToAddress> employeeSendToAddressesList = employeeSendToAddressDao.getEmployeeSendToAddressList("zh-CHS",IngleApplication.getInstance().getCurrentUser(),"");
		
		List<KeyValueData> keyValueDataList = null;
		KeyValueData kvDataInfo = null;
		if(employeeSendToAddressesList != null && employeeSendToAddressesList.size()>0){
			keyValueDataList = new ArrayList<KeyValueData>();
			for(EmployeeSendToAddress esta:employeeSendToAddressesList){
				kvDataInfo = new KeyValueData();
				kvDataInfo.setKey(esta.getName());
				kvDataInfo.setValue(esta.getCode());
				
				keyValueDataList.add(kvDataInfo);
			}
		}
		return keyValueDataList;
	}

}
