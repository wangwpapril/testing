package com.cuc.miti.phone.xmc.logic;

import java.util.List;

import com.cuc.miti.phone.xmc.dao.EmployeeSendTo3TAddressDao;
import com.cuc.miti.phone.xmc.domain.EmployeeSendTo3TAddress;
import com.cuc.miti.phone.xmc.domain.Pager;


import android.content.Context;
import android.test.AndroidTestCase;
import android.util.Log;

public class EmployeeSendTo3TAddressService extends AndroidTestCase {
	
	private static final String TAG="EmployeeSendTo3TAddressServices";
	
	private Context context;
	
	private EmployeeSendTo3TAddressDao employeeSendTo3TAddressDao;

	public EmployeeSendTo3TAddressService(Context context) {

		this.context=context;
		this.employeeSendTo3TAddressDao=new EmployeeSendTo3TAddressDao(context);
	}
	
	public boolean addEmployeeSendTo3TAddress(EmployeeSendTo3TAddress employeeSendTo3TAddress){
		return employeeSendTo3TAddressDao.addEmployeeSendTo3TAddress(employeeSendTo3TAddress);
	}
	public boolean deleteEmployeeSendTo3TAddress(String name){
		return employeeSendTo3TAddressDao.deleteEmployeeSendTo3TAddress(name);
	}
	public boolean deleteAllEmployeeSendTo3TAddress(){
		return employeeSendTo3TAddressDao.deleteAllEmployeeSendTo3TAddress();
	}
	public boolean updateEmployeeSendTo3TAddress(EmployeeSendTo3TAddress employeeSendTo3TAddress){
		return employeeSendTo3TAddressDao.updateEmployeeSendTo3TAddress(employeeSendTo3TAddress);
	}
	public EmployeeSendTo3TAddress getEmployeeSendTo3TAddress(String name){
		return employeeSendTo3TAddressDao.getEmployeeSendTo3TAddress(name);
	}
	public List<EmployeeSendTo3TAddress> getEmployeeSendTo3TAddressList(){
		return employeeSendTo3TAddressDao.getEmployeeSendTo3TAddressList();
	}
	public List<EmployeeSendTo3TAddress> getEmployeeSendTo3TAddressByPage(Pager pager){
		return employeeSendTo3TAddressDao.getEmployeeSendTo3TAddressByPage(pager);
	}
	public long getEmployeeSendTo3TAddressCount(){
		return employeeSendTo3TAddressDao.getEmployeeSendTo3TAddressCount();
	}
	public EmployeeSendTo3TAddress getEmployeeSendTo3TAddressById(int id){
		return employeeSendTo3TAddressDao.getEmployeeSendTo3TAddressById(id);
	}
	
	
	public void TestAdd() throws Exception{
		
		EmployeeSendTo3TAddressDao employeeSendTo3TAddressDao=new EmployeeSendTo3TAddressDao(this.getContext());
		
		for(int i =0;i<=5;i++){
			EmployeeSendTo3TAddress employeeSendTo3TAddress=new EmployeeSendTo3TAddress();
			
			employeeSendTo3TAddress.setCode("code" + i);
			employeeSendTo3TAddress.setLoginname("loginname" + i);
			employeeSendTo3TAddress.setName("name" + i);
			employeeSendTo3TAddress.setLanguage("language"+i);
			
			employeeSendTo3TAddressDao.addEmployeeSendTo3TAddress(employeeSendTo3TAddress);
		}
	
		List<EmployeeSendTo3TAddress> employeeSendTo3TAddresses=this.employeeSendTo3TAddressDao.getEmployeeSendTo3TAddressList();
		
		for(EmployeeSendTo3TAddress e:employeeSendTo3TAddresses)
		{
			Log.i(TAG, e.toString());
		}
		
		
	}

	private void TestForInsert(String count) {
			
		
		
		}

	
	public void TestDelete(){
		for(int i =0;i<=5;i++){
			TestForInsert(String.valueOf(i));
		}
	
	}

	private void TestForDelete(String valueOf) {
			// TODO Auto-generated method stub
			
		}
	
	public void TestUpdate(){
		for(int i =0;i<=5;i++){
			TestForInsert(String.valueOf(i));
		}
	
	}

	private void TestForUpdate(String valueOf) {
			// TODO Auto-generated method stub
			
		}
	
	
	
	
}
