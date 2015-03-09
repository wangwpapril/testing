package com.cuc.miti.phone.xmc.dao;

import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.domain.EmployeeSendToAddress;
import com.cuc.miti.phone.xmc.domain.Pager;

import android.content.Context;
import android.database.Cursor;

public class EmployeeSendToAddressDao {
	private static SQLiteHelper sqlHelper;
	private static final int VERSION =1;
	
	public EmployeeSendToAddressDao(Context context){
		sqlHelper = new SQLiteHelper(context,VERSION);
	}
	/**
	 * ���һ����¼
	 */
	public  boolean addEmployeeSendToAddress(EmployeeSendToAddress employeeSendToAddress){
		int count = 0;
		if(employeeSendToAddress != null ){
			String sql = "insert into EmployeeSendToAddress(code,loginname,name,language,[order],type,pinyin,headchar) values (?,?,?,?,?,?,?,?)";
			Object[] params = {employeeSendToAddress.getCode(),
											employeeSendToAddress.getLoginname(),
											employeeSendToAddress.getName(),
											employeeSendToAddress.getLanguage(),
											employeeSendToAddress.getOrder(),
											"self",
											employeeSendToAddress.getPinyin(),
											employeeSendToAddress.getHeadchar()};
			count = sqlHelper.insert(sql, params);
		}
		return count ==1 ?true:false;		
	}
	
	/**
	 * ɾ��һ����¼
	 * @param name
	 * @return
	 */
	public boolean deleteEmployeeSendToAddress(String name){
		int count = 0;

		if(name!= null){
			String sql = "delete from EmployeeSendToAddress where loginname = ?";
			String[] params = {name};
			count = sqlHelper.delete(sql, params);
		}
		return count ==1 ?true:false;
		
	}
	
	/**
	 * ɾ������
	 * @return 
	 */
	public boolean deleteAllEmployeeSendToAddress(){
		String sql = "delete from EmployeeSendToAddress";
		sqlHelper.deleteAll(sql);
		return true;

	}
	
	/**
	 * ����һ����¼
	 * @param employeeSendToAddress
	 * @return
	 */
	public boolean updateEmployeeSendToAddress(EmployeeSendToAddress employeeSendToAddress){
		int count = 0;
		if(employeeSendToAddress != null){
			String sql = "update EmployeeSendToAddress set code=?,[order]=?,name=?,language=?,pinyin=?,headchar=? where loginname =?";
			Object[] params = {employeeSendToAddress.getCode(),
											employeeSendToAddress.getOrder(),
											employeeSendToAddress.getName(),
											employeeSendToAddress.getLanguage(),
											employeeSendToAddress.getPinyin(),
											employeeSendToAddress.getHeadchar(),
											employeeSendToAddress.getLoginname()
											};
			count = sqlHelper.update(sql, params);
		}
		return count ==1?true:false;
	}
	
	/**
	 * �鿴һ����¼
	 */
	public EmployeeSendToAddress getEmployeeSendToAddress(String name){
		
		EmployeeSendToAddress employeeSendToAddress = null;
		if(name != null){
			String sql = "select * from EmployeeSendToAddress where loginname = ?";
			String[] params = {name};
			Cursor cursor = sqlHelper.findQuery(sql, params);
			 employeeSendToAddress = new EmployeeSendToAddress();
			
			if(cursor!=null && cursor.getCount()>0){
				employeeSendToAddress.setCode(cursor.getString(cursor.getColumnIndex("code")));
				employeeSendToAddress.setLoginname(cursor.getString(cursor.getColumnIndex("loginname")));
				employeeSendToAddress.setOrder(cursor.getString(cursor.getColumnIndex("order")));
				employeeSendToAddress.setName(cursor.getString(cursor.getColumnIndex("name")));
				employeeSendToAddress.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				employeeSendToAddress.setPinyin(cursor.getString(cursor.getColumnIndex("pinyin")));
				employeeSendToAddress.setHeadchar(cursor.getString(cursor.getColumnIndex("headchar")));

			}
			cursor.close();
		}		

		return employeeSendToAddress;		
	}
	
	/**
	 * ���id�鿴һ����¼
	 */
	public EmployeeSendToAddress getEmployeeSendToAddressById(int id){
		
		EmployeeSendToAddress employeeSendToAddress = null;
		if(id>0){
			String sql = "select * from EmployeeSendToAddress where esa_id = ?";
			String[] params = {String.valueOf(id)};
			Cursor cursor = sqlHelper.findQuery(sql, params);
			 employeeSendToAddress = new EmployeeSendToAddress();
			
			if(cursor!=null && cursor.getCount()>0){
				employeeSendToAddress.setEsa_id(cursor.getColumnIndex("esa_id"));
				employeeSendToAddress.setCode(cursor.getString(cursor.getColumnIndex("code")));
				employeeSendToAddress.setLoginname(cursor.getString(cursor.getColumnIndex("loginname")));
				employeeSendToAddress.setOrder(cursor.getString(cursor.getColumnIndex("order")));
				employeeSendToAddress.setName(cursor.getString(cursor.getColumnIndex("name")));
				employeeSendToAddress.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				employeeSendToAddress.setPinyin(cursor.getString(cursor.getColumnIndex("pinyin")));
				employeeSendToAddress.setHeadchar(cursor.getString(cursor.getColumnIndex("headchar")));

			}
			cursor.close();
			
		}		

		return employeeSendToAddress;		
	}
	
	/**
	 * �鿴������¼
	 */
	public List<EmployeeSendToAddress> getEmployeeSendToAddressList(){
		List<EmployeeSendToAddress> employeeSendToAddressList = new ArrayList<EmployeeSendToAddress>();
			String sql = "select* from EmployeeSendToAddress";
			Cursor cursor = sqlHelper.findQuery(sql);
			
			if(cursor!=null && cursor.getCount()>0){
				for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){//isAfterLastʲô��˼
					EmployeeSendToAddress employeeSendToAddress = new EmployeeSendToAddress();
					employeeSendToAddress.setCode(cursor.getString(cursor.getColumnIndex("code")));
					employeeSendToAddress.setLoginname(cursor.getString(cursor.getColumnIndex("loginname")));
					employeeSendToAddress.setOrder(cursor.getString(cursor.getColumnIndex("order")));
					employeeSendToAddress.setName(cursor.getString(cursor.getColumnIndex("name")));
					employeeSendToAddress.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
					employeeSendToAddress.setPinyin(cursor.getString(cursor.getColumnIndex("pinyin")));
					employeeSendToAddress.setHeadchar(cursor.getString(cursor.getColumnIndex("headchar")));

					employeeSendToAddressList.add(employeeSendToAddress);
				}
			}
			cursor.close();
		
		
		return employeeSendToAddressList;
		
	}
	
	/**
	 * ������������ȡ��ǰ�û����Զ��巢���ַ�б�
	 * @param language
	 * @param loginname
	 * @param queryString
	 * @return
	 */
	public List<EmployeeSendToAddress> getEmployeeSendToAddressList(String language,String loginname,String queryString){
		List<EmployeeSendToAddress> employeeSendToAddressList = new ArrayList<EmployeeSendToAddress>();
		
		String sql ="";
		if(queryString.equals("")){
			sql = "select * from EmployeeSendToAddress where language='" + language +  "'  and loginname='" + loginname + "' order by [order]";
		}else{
			sql = "select * from EmployeeSendToAddress where language='" + language +  "'  and loginname='" + loginname + "' and (name like '" + queryString + "%' or pinyin like '" + queryString + "%' or headchar like '" + queryString + "%')  order by [order]";
		}

		Cursor cursor = sqlHelper.findQuery(sql);		
			
			if(cursor!=null && cursor.getCount()>0){
				for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){//isAfterLastʲô��˼
					EmployeeSendToAddress employeeSendToAddress = new EmployeeSendToAddress();
					employeeSendToAddress.setCode(cursor.getString(cursor.getColumnIndex("code")));
					employeeSendToAddress.setLoginname(cursor.getString(cursor.getColumnIndex("loginname")));
					employeeSendToAddress.setOrder(cursor.getString(cursor.getColumnIndex("order")));
					employeeSendToAddress.setName(cursor.getString(cursor.getColumnIndex("name")));
					employeeSendToAddress.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
					employeeSendToAddress.setPinyin(cursor.getString(cursor.getColumnIndex("pinyin")));
					employeeSendToAddress.setHeadchar(cursor.getString(cursor.getColumnIndex("headchar")));

					employeeSendToAddressList.add(employeeSendToAddress);
				}
			}
			cursor.close();
		return employeeSendToAddressList;
		
	}
	
	/**��ȡ������	 
	 * 
	 */
	public long getEmployeeSendToAddressCount()
	{
		String sql = "select count(*) from EmployeeSendToAddress";
		
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
	public List<EmployeeSendToAddress> getEmployeeSendToAddressByPage(Pager pager){
		
		List<EmployeeSendToAddress> employeeSendToAddressList = null;
		if(pager != null){			
				String sql = "select * from EmployeeSendToAddress limit ?,?";
				int firstResult = (pager.getCurrentPage()-1)*pager.getPageSize();
				int maxResult = pager.getPageSize();
				String[] params = {String.valueOf(firstResult),String.valueOf(maxResult)};
				Cursor cursor = sqlHelper.findQuery(sql, params);
				if(cursor!=null && cursor.getCount()>0){
					employeeSendToAddressList = new ArrayList<EmployeeSendToAddress>();
					
					for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
						EmployeeSendToAddress employeeSendToAddress = new EmployeeSendToAddress();
						employeeSendToAddress.setCode(cursor.getString(cursor.getColumnIndex("code")));
						employeeSendToAddress.setLoginname(cursor.getString(cursor.getColumnIndex("loginname")));
						employeeSendToAddress.setOrder(cursor.getString(cursor.getColumnIndex("order")));
						employeeSendToAddress.setName(cursor.getString(cursor.getColumnIndex("name")));
						employeeSendToAddress.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
						employeeSendToAddress.setPinyin(cursor.getString(cursor.getColumnIndex("pinyin")));
						employeeSendToAddress.setHeadchar(cursor.getString(cursor.getColumnIndex("headchar")));

						employeeSendToAddressList.add(employeeSendToAddress);
					}
					
				}
				cursor.close();
		}
		return employeeSendToAddressList;
		
	}
}
