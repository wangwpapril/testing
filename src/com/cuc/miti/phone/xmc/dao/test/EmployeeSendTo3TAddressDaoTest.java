package com.cuc.miti.phone.xmc.dao.test;

import java.io.InputStream;
import java.util.List;

import com.cuc.miti.phone.xmc.dao.EmployeeSendTo3TAddressDao;
import com.cuc.miti.phone.xmc.dao.EmployeeSendToAddressDao;
import com.cuc.miti.phone.xmc.dao.SalaryEarnerSendToAddressDao;
import com.cuc.miti.phone.xmc.dao.SendToAddressDao;
import com.cuc.miti.phone.xmc.dao.UserDao;
import com.cuc.miti.phone.xmc.domain.CommonXMLData;
import com.cuc.miti.phone.xmc.domain.EmployeeSendTo3TAddress;
import com.cuc.miti.phone.xmc.domain.EmployeeSendToAddress;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.SalaryEarnerSendToAddress;
import com.cuc.miti.phone.xmc.domain.SendToAddress;
import com.cuc.miti.phone.xmc.domain.User;
import com.cuc.miti.phone.xmc.utils.Encrypt;
import com.cuc.miti.phone.xmc.utils.XMLDataHandle;


import android.test.AndroidTestCase;
import android.util.Log;

public class EmployeeSendTo3TAddressDaoTest extends AndroidTestCase {


	private static final String TAG="EmployeeSendTo3TAddressServices";
	private static final String TAG1 ="EmployeeSendToAddress";
	private static final String TAG2 = "SendToAddress";
	private static final String TAG3 = "SalaryEarnerSendToAddress";
	private static final String TAGXML = "XMLDataHandle";
	
	public void testAddEmployeeSendTo3TAddress() {
		EmployeeSendTo3TAddressDao employeeSendTo3TAddressDao=new EmployeeSendTo3TAddressDao(this.getContext());
		
		for(int i =0;i<=5;i++){
			EmployeeSendTo3TAddress employeeSendTo3TAddress=new EmployeeSendTo3TAddress();
			
			employeeSendTo3TAddress.setCode("code" + i);
			employeeSendTo3TAddress.setLoginname("loginname" + i);
			employeeSendTo3TAddress.setOrder("order" + i);
			
			employeeSendTo3TAddressDao.addEmployeeSendTo3TAddress(employeeSendTo3TAddress);
		}
	
		List<EmployeeSendTo3TAddress> employeeSendTo3TAddresses=employeeSendTo3TAddressDao.getEmployeeSendTo3TAddressList();
		
		for(EmployeeSendTo3TAddress e:employeeSendTo3TAddresses)
		{
			Log.i(TAG, e.toString());
		}
	}
	
	
	public void testAddUser() {
		UserDao userDao=new UserDao(this.getContext());
		
		for(int i =6;i<=8;i++){
			User user=new User();
			
			user.setPassword(Encrypt.toMD5("pass" + i));
			user.setUsername("name" + i);
			
			userDao.addUser(user);
		}
	
		List<User> users=userDao.getUserList();
		
		for(User e:users)
		{
			Log.i(TAG, e.toString());
		}
	}

	public void testDeleteEmployeeSendTo3TAddress() {
		EmployeeSendTo3TAddressDao employeeSendTo3TAddressDao=new EmployeeSendTo3TAddressDao(this.getContext());
		
		EmployeeSendTo3TAddress employeeSendTo3TAddress=employeeSendTo3TAddressDao.getEmployeeSendTo3TAddress("loginname0");
		
		Log.i(TAG, employeeSendTo3TAddress.toString());
		
		employeeSendTo3TAddressDao.deleteEmployeeSendTo3TAddress("loginname0");
		Log.i(TAG, employeeSendTo3TAddress.toString());
	}

	public void testUpdateEmployeeSendTo3TAddress() {
	
		EmployeeSendTo3TAddressDao employeeSendTo3TAddressDao=new EmployeeSendTo3TAddressDao(this.getContext());
		
		EmployeeSendTo3TAddress employeeSendTo3TAddress=employeeSendTo3TAddressDao.getEmployeeSendTo3TAddress("loginname0");
		
		employeeSendTo3TAddress.setCode("updateC");
		employeeSendTo3TAddress.setOrder("UPDATEo");
		employeeSendTo3TAddressDao.updateEmployeeSendTo3TAddress(employeeSendTo3TAddress);
		Log.i(TAG, employeeSendTo3TAddress.toString());
		
		
	}


	public void testGetEmployeeSendTo3TAddressByPage() {
		

		EmployeeSendTo3TAddressDao employeeSendTo3TAddressDao=new EmployeeSendTo3TAddressDao(this.getContext());
		
		Pager pager=new Pager();
		pager.setCurrentPage(2);
		pager.setPageSize(3);
		pager.setTotalNum(Integer.parseInt(employeeSendTo3TAddressDao.getEmployeeSendTo3TAddressCount()+""));
		
		
		
		List<EmployeeSendTo3TAddress> employeeSendTo3TAddresses=employeeSendTo3TAddressDao.getEmployeeSendTo3TAddressByPage(pager);
		
		for(EmployeeSendTo3TAddress e:employeeSendTo3TAddresses)
		{
			Log.i(TAG, e.toString());
		}
	}
	
	
	//��һ����
	
	public void testGetEmployeeSendToAddressList()
	{
		EmployeeSendToAddressDao employeeSendToAddressDao=new EmployeeSendToAddressDao(this.getContext());
		
		List<EmployeeSendToAddress> employeeSendToAddresses=employeeSendToAddressDao.getEmployeeSendToAddressList();
		
		for(EmployeeSendToAddress e:employeeSendToAddresses)
		{
			Log.i(TAG1, e.toString());
		}
	}
	
	public void testAddEmployeeSendToAddress() {
		EmployeeSendToAddressDao employeeSendToAddressDao=new EmployeeSendToAddressDao(this.getContext());
		
		for(int i =0;i<=5;i++){
			EmployeeSendToAddress employeeSendToAddress=new EmployeeSendToAddress();
			
			employeeSendToAddress.setCode("code" + i);
			employeeSendToAddress.setLoginname("loginname" + i);
			employeeSendToAddress.setOrder("order" + i);
			
			employeeSendToAddressDao.addEmployeeSendToAddress(employeeSendToAddress);
		}
	
		List<EmployeeSendToAddress> employeeSendToAddresses=employeeSendToAddressDao.getEmployeeSendToAddressList();
		
		for(EmployeeSendToAddress e:employeeSendToAddresses)
		{
			Log.i(TAG1, e.toString());
		}
	}
	 public void testUpdateDeleEmployeeSendToAddress() {
		EmployeeSendToAddressDao employeeSendToAddressDao=new EmployeeSendToAddressDao(this.getContext());
		
		EmployeeSendToAddress employeeSendToAddress=employeeSendToAddressDao.getEmployeeSendToAddress("loginname0");
		
		Log.i(TAG1, employeeSendToAddress.toString());
	
		employeeSendToAddress.setCode("codeU");
		employeeSendToAddress.setOrder("orderU");
		
		employeeSendToAddressDao.updateEmployeeSendToAddress(employeeSendToAddress);
		
		Log.i(TAG1, employeeSendToAddress.toString());
		
		employeeSendToAddressDao.deleteEmployeeSendToAddress("loginname0");
		Log.i(TAG1, employeeSendToAddress.toString());
		
	}
	
	
	public void testGetEmployeeSendToAddressByPage() {
		

		EmployeeSendToAddressDao employeeSendToAddressDao=new EmployeeSendToAddressDao(this.getContext());
		
		Pager pager=new Pager();
		pager.setCurrentPage(2);
		pager.setPageSize(3);
		pager.setTotalNum(Integer.parseInt(employeeSendToAddressDao.getEmployeeSendToAddressCount()+""));
		
		
		Log.i(TAG1, pager.getTotalNum()+"");
		
		List<EmployeeSendToAddress> employeeSendToAddresses=employeeSendToAddressDao.getEmployeeSendToAddressByPage(pager);
		
		for(EmployeeSendToAddress e:employeeSendToAddresses)
		{
			Log.i(TAG1, e.toString());
		}
	}
	
	
	//��һ����
	
	public void testGetSendToAddressList()
	{
		SendToAddressDao employeeSendToAddressDao=new SendToAddressDao(this.getContext());
		
		List<SendToAddress> employeeSendToAddresses=employeeSendToAddressDao.getSendToAddressList("zh-CHS","");
		
		for(SendToAddress e:employeeSendToAddresses)
		{
			Log.i(TAG2, e.toString());
		}
	}
	
	public void testAddSendToAddress() {
		SendToAddressDao employeeSendToAddressDao=new SendToAddressDao(this.getContext());
		
		for(int i =0;i<=5;i++){
			SendToAddress employeeSendToAddress=new SendToAddress();
			
			employeeSendToAddress.setCode("code" + i);
			employeeSendToAddress.setLanguage("language" + i);
			employeeSendToAddress.setOrder("order" + i);
			employeeSendToAddress.setName("name"+i);
			
			employeeSendToAddressDao.addSendToAddress(employeeSendToAddress);
		}
	
		List<SendToAddress> employeeSendToAddresses=employeeSendToAddressDao.getSendToAddressList("zh-CHS","");
		
		for(SendToAddress e:employeeSendToAddresses)
		{
			Log.i(TAG2, e.toString());
		}
	}

	public void testUpdateDeleSendToAddress() {
		SendToAddressDao employeeSendToAddressDao=new SendToAddressDao(this.getContext());
		
		SendToAddress employeeSendToAddress=employeeSendToAddressDao.getSendToAddress("name0");
		
		Log.i(TAG2, employeeSendToAddress.toString());
	
		employeeSendToAddress.setCode("codeU");
		employeeSendToAddress.setOrder("orderU");
		employeeSendToAddress.setLanguage("languageU");
		
		employeeSendToAddressDao.updateSendToAddress(employeeSendToAddress);
		
		Log.i(TAG2, employeeSendToAddress.toString());
		
		employeeSendToAddressDao.deleteSendToAddress("name0");
		
		List<SendToAddress> employeeSendToAddresses=employeeSendToAddressDao.getSendToAddressList("zh-CHS","");
		
		for(SendToAddress e:employeeSendToAddresses)
		{
			Log.i(TAG2, e.toString());
		}
	}
	
	
	public void testGetSendToAddressByPage() {
		

		SendToAddressDao employeeSendToAddressDao=new SendToAddressDao(this.getContext());
		
		Pager pager=new Pager();
		pager.setCurrentPage(2);
		pager.setPageSize(3);
		pager.setTotalNum(Integer.parseInt(employeeSendToAddressDao.getSendToAddressCount()+""));
		
		
		Log.i(TAG2, pager.getTotalNum()+"");
		
		List<SendToAddress> employeeSendToAddresses=employeeSendToAddressDao.getSendToAddressByPage(pager);
		
		for(SendToAddress e:employeeSendToAddresses)
		{
			Log.i(TAG2, e.toString());
		}
	}
	
	public void testAddSalaryEarnerSendToAddress() {
		SalaryEarnerSendToAddressDao salaryEarnerSendToAddressDao=new SalaryEarnerSendToAddressDao(this.getContext());
		
		for(int i =0;i<=5;i++){
			SalaryEarnerSendToAddress employeeSendToAddress=new SalaryEarnerSendToAddress();
			
			employeeSendToAddress.setCode("code" + i);
			employeeSendToAddress.setLanguage("language" + i);
			employeeSendToAddress.setOrder("order" + i);
			employeeSendToAddress.setName("name"+i);
			employeeSendToAddress.setLoginname("loginname"+i);
			
			salaryEarnerSendToAddressDao.addSalaryEarnerSendToAddress(employeeSendToAddress);
		}
	
		List<SalaryEarnerSendToAddress> employeeSendToAddresses=salaryEarnerSendToAddressDao.getSalaryEarnerSendToAddressList();
		
		for(SalaryEarnerSendToAddress e:employeeSendToAddresses)
		{
			Log.i(TAG3, e.toString());
		}
	}

	public void testUpdateDeleSalaryEarnerSendToAddress() {
		SalaryEarnerSendToAddressDao salaryEarnerSendToAddressDao=new SalaryEarnerSendToAddressDao(this.getContext());
		
		SalaryEarnerSendToAddress employeeSendToAddress=salaryEarnerSendToAddressDao.getSalaryEarnerSendToAddress("loginname0");
		
		Log.i(TAG3, employeeSendToAddress.toString());
	
		employeeSendToAddress.setCode("codeU");
		employeeSendToAddress.setOrder("orderU");
		employeeSendToAddress.setLanguage("languageU");
		employeeSendToAddress.setLoginname("loginnameU");
		employeeSendToAddress.setName("naemU");
		
		salaryEarnerSendToAddressDao.updateSalaryEarnerSendToAddress(employeeSendToAddress);
		
		Log.i(TAG3, employeeSendToAddress.toString());
		
		salaryEarnerSendToAddressDao.deleteSalaryEarnerSendToAddress("loginnameU");
		
		List<SalaryEarnerSendToAddress> employeeSendToAddresses=salaryEarnerSendToAddressDao.getSalaryEarnerSendToAddressList();
		
		for(SalaryEarnerSendToAddress e:employeeSendToAddresses)
		{
			Log.i(TAG3, e.toString());
		}
	}
	
	
	public void testGetSalaryEarnerSendToAddressByPage() {
		

		SalaryEarnerSendToAddressDao salaryEarnerSendToAddressDao=new SalaryEarnerSendToAddressDao(this.getContext());
		
		Pager pager=new Pager();
		pager.setCurrentPage(2);
		pager.setPageSize(3);
		pager.setTotalNum(Integer.parseInt(salaryEarnerSendToAddressDao.getSalaryEarnerSendToAddressCount()+""));
		
		
		Log.i(TAG3, pager.getTotalNum()+"");
		
		List<SalaryEarnerSendToAddress> SalaryEarnerSendToAddress=salaryEarnerSendToAddressDao.getSalaryEarnerSendToAddressByPage(pager);
		
		for(SalaryEarnerSendToAddress e:SalaryEarnerSendToAddress)
		{
			Log.i(TAG3, e.toString());
		}
	}
	
	
	
	
	
	//��xml�Ĳ���
	
	
	public void testPullXMl()throws Exception
	{
		XMLDataHandle parser=new XMLDataHandle();
		
		InputStream inputStream=getClass().getClassLoader().getResourceAsStream("topiclist.cnml-MediaType-1.xml");
		
		//List<CommonXMLData> commonXMLDatas=parser.parser(inputStream);
	
		List<CommonXMLData> commonXMLDatas=parser.parser(inputStream);
		
		for(CommonXMLData cm:commonXMLDatas)
		{
			Log.i(TAGXML, cm.toString());
		}
		
	}
	
	
	
	public void testPullXMl2()throws Exception
	{
		XMLDataHandle parser=new XMLDataHandle();
		
		InputStream inputStream=getClass().getClassLoader().getResourceAsStream("topiclist.cnml-Department-85.xml");
		
		List<CommonXMLData> commonXMLDatas=parser.parser(inputStream);
		
		for(CommonXMLData cm:commonXMLDatas)
		{
			Log.i(TAGXML, cm.toString());
		}
		
	}
	
	
	
	
	
}
