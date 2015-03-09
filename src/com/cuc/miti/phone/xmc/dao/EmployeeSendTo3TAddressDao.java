package com.cuc.miti.phone.xmc.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cuc.miti.phone.xmc.domain.EmployeeSendTo3TAddress;
import com.cuc.miti.phone.xmc.domain.Pager;


public class EmployeeSendTo3TAddressDao {
	private static SQLiteHelper sqlHelper;
	private static final int VERSION =1;
	
	public  EmployeeSendTo3TAddressDao(Context context){
		sqlHelper = new SQLiteHelper(context,VERSION);
	}
	/**
	 * ���һ����¼
	 */
	public  boolean addEmployeeSendTo3TAddress(EmployeeSendTo3TAddress employeeSendTo3TAddress){
		int count = 0;
		if(employeeSendTo3TAddress != null ){
			String sql = "insert into EmployeeSendTo3TAddress (code,loginname,name,language,[order]) values (?,?,?,?,?)";
			
			Object[] params = {employeeSendTo3TAddress.getCode(),employeeSendTo3TAddress.getLoginname(),employeeSendTo3TAddress.getName(),employeeSendTo3TAddress.getLanguage(),employeeSendTo3TAddress.getOrder()};
			
			count = sqlHelper.insert(sql, params);
		}
		return count ==1 ?true:false;		
	}
	
	/**
	 * ɾ��һ����¼
	 * @param name
	 * @return
	 */
	public boolean deleteEmployeeSendTo3TAddress(String name){
		int count = 0;

		if(name!= null){
			String sql = "delete from EmployeeSendTo3TAddress where loginname = ?";
			String[] params = {name};
			count = sqlHelper.delete(sql, params);
		}
		return count ==1 ?true:false;
		
	}
	/**
	 * ɾ������
	 * @return 
	 */
	public boolean deleteAllEmployeeSendTo3TAddress(){
		String sql = "delete from EmployeeSendTo3TAddress";
		sqlHelper.deleteAll(sql);
		return true;

	}
	
	/**
	 * ����һ����¼
	 * @param employeeSendTo3TAddress
	 * @return
	 */
	public boolean updateEmployeeSendTo3TAddress(EmployeeSendTo3TAddress employeeSendTo3TAddress){
		int count = 0;
		if(employeeSendTo3TAddress != null){
			String sql = "update EmployeeSendTo3TAddress set code=?,[order]=?,name=?,language=? where loginname =?";
			Object[] params = {employeeSendTo3TAddress.getCode(),employeeSendTo3TAddress.getOrder(),employeeSendTo3TAddress.getName(),employeeSendTo3TAddress.getLanguage(),employeeSendTo3TAddress.getLoginname()};
			count = sqlHelper.update(sql, params);
		}
		return count ==1?true:false;
	}
	
	/**
	 * �鿴һ����¼
	 */
	public EmployeeSendTo3TAddress getEmployeeSendTo3TAddress(String name){
		
		EmployeeSendTo3TAddress employeeSendTo3TAddress = null;
		if(name != null){
			String sql = "select * from EmployeeSendTo3TAddress where loginname = ?";
			String[] params = {name};
			Cursor cursor = sqlHelper.findQuery(sql, params);
			 employeeSendTo3TAddress = new EmployeeSendTo3TAddress();
			
			if(cursor!=null && cursor.getCount()>0){
				employeeSendTo3TAddress.setCode(cursor.getString(cursor.getColumnIndex("code")));
				employeeSendTo3TAddress.setLoginname(cursor.getString(cursor.getColumnIndex("loginname")));
				employeeSendTo3TAddress.setOrder(cursor.getString(cursor.getColumnIndex("order")));
				employeeSendTo3TAddress.setName(cursor.getString(cursor.getColumnIndex("name")));
				employeeSendTo3TAddress.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				
			
			}
			cursor.close();
			
		}		

		return employeeSendTo3TAddress;		
	}
	
	/**
	 * ���id���¼
	 */
	public EmployeeSendTo3TAddress getEmployeeSendTo3TAddressById(int id){
		EmployeeSendTo3TAddress employeeSendTo3TAddress = new EmployeeSendTo3TAddress();
		if(id>0){
			String sql = "select * from EmployeeSendTo3TAddress where es3_id = ?";
			String[] params = {String.valueOf(id)};
			Cursor cursor = sqlHelper.findQuery(sql, params);
						
			if(cursor!=null && cursor.getCount()>0){
				employeeSendTo3TAddress.setEs3_id(cursor.getColumnIndex("es3_id"));
				employeeSendTo3TAddress.setCode(cursor.getString(cursor.getColumnIndex("code")));
				employeeSendTo3TAddress.setLoginname(cursor.getString(cursor.getColumnIndex("loginname")));
				employeeSendTo3TAddress.setOrder(cursor.getString(cursor.getColumnIndex("order")));
				employeeSendTo3TAddress.setName(cursor.getString(cursor.getColumnIndex("name")));
				employeeSendTo3TAddress.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
			
			}
			cursor.close();
		}		

		return employeeSendTo3TAddress;		
	}
	
	/**
	 * �鿴������¼
	 */
	public List<EmployeeSendTo3TAddress> getEmployeeSendTo3TAddressList(){
		List<EmployeeSendTo3TAddress> employeeSendTo3TAddressList = new ArrayList<EmployeeSendTo3TAddress>();
		
			String sql = "select * from EmployeeSendTo3TAddress";
			Cursor cursor = sqlHelper.findQuery(sql);
			if(cursor!=null&& cursor.getCount()>0){
				for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){//isAfterLastʲô��˼
					EmployeeSendTo3TAddress employeeSendTo3TAddress = new EmployeeSendTo3TAddress();
					
					employeeSendTo3TAddress.setCode(cursor.getString(cursor.getColumnIndex("code")));
					employeeSendTo3TAddress.setLoginname(cursor.getString(cursor.getColumnIndex("loginname")));
					employeeSendTo3TAddress.setOrder(cursor.getString(cursor.getColumnIndex("order")));
					employeeSendTo3TAddress.setName(cursor.getString(cursor.getColumnIndex("name")));
					employeeSendTo3TAddress.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
					
					employeeSendTo3TAddressList.add(employeeSendTo3TAddress);
				}
			}
			cursor.close();
		
		
		return employeeSendTo3TAddressList;		
	}
	
	/**��ȡ������	 
	 * 
	 */
	public long getEmployeeSendTo3TAddressCount()
	{
		String sql = "select count(*) from EmployeeSendTo3TAddress";
		
		Cursor cursor = sqlHelper.findQuery(sql,null);
		cursor.moveToFirst();
		long returnvalue=cursor.getLong(0);
		cursor.close();
		return returnvalue;
	}
	
	/**
	 * ��ȡָ��ҳ��ķ�ҳ���,���ص�ǰҳ���������
	 * SQL : Select * From TABLE_NAME Limit 0,10; 
	 * ��ʾ��TABLE_NAME���ȡ��ݣ��ӵ�0����ݿ�ʼ��ȥ10�� 
	 * @param pager ��ҳ����(CurrentPage-��ǰҳ��PageSize-ÿҳ����totalNum-������<����ֵ>)
	 * @return
	 */
	public List<EmployeeSendTo3TAddress> getEmployeeSendTo3TAddressByPage(Pager pager){
		List<EmployeeSendTo3TAddress> employeeSendTo3TAddressList = null;
		EmployeeSendTo3TAddress employeeSendTo3TAddress = null;
		
		if(pager!=null){
			String sql = "select * from EmployeeSendTo3TAddress limit ?,?";
			int firstResult = (pager.getCurrentPage()-1)*pager.getPageSize();
			int maxResult = pager.getPageSize();
			String[] params = {String.valueOf(firstResult),String.valueOf(maxResult)};
			Cursor cursor = sqlHelper.findQuery(sql, params);
			if(cursor!=null&& cursor.getCount()>0){
				employeeSendTo3TAddressList = new ArrayList<EmployeeSendTo3TAddress>();
				for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
					employeeSendTo3TAddress = new EmployeeSendTo3TAddress();
					employeeSendTo3TAddress.setCode(cursor.getString(cursor.getColumnIndex("code")));
					employeeSendTo3TAddress.setLoginname(cursor.getString(cursor.getColumnIndex("loginname")));
					employeeSendTo3TAddress.setOrder(cursor.getString(cursor.getColumnIndex("order")));
					employeeSendTo3TAddress.setName(cursor.getString(cursor.getColumnIndex("name")));
					employeeSendTo3TAddress.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
					
					employeeSendTo3TAddressList.add(employeeSendTo3TAddress);
				}
				
			}
			cursor.close();
		}
		return employeeSendTo3TAddressList ;
		
	}

}
