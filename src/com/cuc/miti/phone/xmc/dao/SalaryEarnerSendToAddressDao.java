package com.cuc.miti.phone.xmc.dao;

import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.SalaryEarnerSendToAddress;

import android.content.Context;
import android.database.Cursor;


public class SalaryEarnerSendToAddressDao {
	private static SQLiteHelper sqlHelper;
	private static final int VERSION = 1;
	
	public SalaryEarnerSendToAddressDao(Context context){
		sqlHelper = new SQLiteHelper(context,VERSION);
	}
	/**
	 * ���һ����¼ name:�����ַ��
	 * @param salaryEarnerSendToAddress
	 * @return
	 */
	public boolean addSalaryEarnerSendToAddress(SalaryEarnerSendToAddress salaryEarnerSendToAddress){
		int count = 0;
		
		if(salaryEarnerSendToAddress != null){
			String sql = "insert into SalaryEarnerSendToAddress (code,loginname,name,[language],[order]) values(?,?,?,?,?)";
			Object[] params = {salaryEarnerSendToAddress.getCode(),salaryEarnerSendToAddress.getLoginname(),salaryEarnerSendToAddress.getName(),salaryEarnerSendToAddress.getLanguage(),salaryEarnerSendToAddress.getOrder()};
			count = sqlHelper.insert(sql, params);			
		}		
		return count==1?true:false;		
	}
	
	/**
	 * ɾ��һ�����
	 * @param name
	 * @return
	 */	
	public boolean deleteSalaryEarnerSendToAddress(String loginname){
		int count = 0;
		
		if(loginname != null){
		String sql = "delete from SalaryEarnerSendToAddress where loginname = ?";
		String[] params = {loginname};
		count = sqlHelper.delete(sql, params);
		}
		return count==1?true:false;		
	}
	
	/**
	 * ɾ������
	 * @return 
	 */
	public boolean deleteAllSalaryEarnerSendToAddress(){
		String sql = "delete from SalaryEarnerSendToAddress";
		sqlHelper.deleteAll(sql);
		return true;

	}
	
	/**
	 * ����
	 * @param salaryEarnerSendToAddress
	 * @return
	 */
	public boolean updateSalaryEarnerSendToAddress(SalaryEarnerSendToAddress salaryEarnerSendToAddress){
		int count = 0;
		if(salaryEarnerSendToAddress != null){
			String sql = "update SalaryEarnerSendToAddress set code=?,name=?, [language] = ?,[order]=? where loginname = ?";
			Object[] params = {salaryEarnerSendToAddress.getCode(),salaryEarnerSendToAddress.getName(),salaryEarnerSendToAddress.getLanguage(),salaryEarnerSendToAddress.getOrder(),salaryEarnerSendToAddress.getLoginname()};
			count = sqlHelper.update(sql, params);
		}
		return count ==1 ? true:false;
	}
	
	/**
	 * ��ѯһ����Ϣ
	 * @param name
	 * @return
	 */
	public SalaryEarnerSendToAddress getSalaryEarnerSendToAddress(String loginname){
		String sql = "select * from SalaryEarnerSendToAddress where loginname = ? ";
		String[] params = {loginname};
		Cursor cursor = sqlHelper.findQuery(sql,params);		
		SalaryEarnerSendToAddress salaryEarnerSendToAddress = null;
		
		if(cursor != null&& cursor.getCount()>0){
			salaryEarnerSendToAddress = new SalaryEarnerSendToAddress();
			salaryEarnerSendToAddress.setCode(cursor.getString(cursor.getColumnIndex("code")));
			salaryEarnerSendToAddress.setLoginname(cursor.getString(cursor.getColumnIndex("loginname")));
			salaryEarnerSendToAddress.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
			salaryEarnerSendToAddress.setName(cursor.getString(cursor.getColumnIndex("name")));
			salaryEarnerSendToAddress.setOrder(cursor.getString(cursor.getColumnIndex("order")));
		}
		cursor.close();
		return salaryEarnerSendToAddress;
	}
	
	/**
	 * ���id��ѯһ����Ϣ
	 * @param name
	 * @return
	 */
	public SalaryEarnerSendToAddress getSalaryEarnerSendToAddressById(int id){
		SalaryEarnerSendToAddress salaryEarnerSendToAddress = new SalaryEarnerSendToAddress();
		if(id>0){
			String sql = "select * from SalaryEarnerSendToAddress where ssta_id = ? ";
			String[] params = {String.valueOf(id)};
			Cursor cursor = sqlHelper.findQuery(sql,params);		
				
			if(cursor != null&& cursor.getCount()>0){	
				salaryEarnerSendToAddress.setSsta_id(cursor.getColumnIndex("ssta_id"));
				salaryEarnerSendToAddress.setCode(cursor.getString(cursor.getColumnIndex("code")));
				salaryEarnerSendToAddress.setLoginname(cursor.getString(cursor.getColumnIndex("loginname")));
				salaryEarnerSendToAddress.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				salaryEarnerSendToAddress.setName(cursor.getString(cursor.getColumnIndex("name")));
				salaryEarnerSendToAddress.setOrder(cursor.getString(cursor.getColumnIndex("order")));
			}		
			cursor.close();
		}
		return salaryEarnerSendToAddress;
	}
	
	/**
	 * 
	 * ��ѯ�б�
	 */
	public List<SalaryEarnerSendToAddress> getSalaryEarnerSendToAddressList(){
		String sql = "select * from SalaryEarnerSendToAddress";
		Cursor cursor = sqlHelper.findQuery(sql);
		 
		
		List<SalaryEarnerSendToAddress> salaryEarnerSendToAddressList =null;
		
		if(cursor!= null&& cursor.getCount()>0){
			salaryEarnerSendToAddressList = new ArrayList<SalaryEarnerSendToAddress> ();//����
			
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				SalaryEarnerSendToAddress salaryEarnerSendToAddress = new SalaryEarnerSendToAddress();
				salaryEarnerSendToAddress.setCode(cursor.getString(cursor.getColumnIndex("code")));
				salaryEarnerSendToAddress.setLoginname(cursor.getString(cursor.getColumnIndex("loginname")));
				salaryEarnerSendToAddress.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				salaryEarnerSendToAddress.setName(cursor.getString(cursor.getColumnIndex("name")));
				salaryEarnerSendToAddress.setOrder(cursor.getString(cursor.getColumnIndex("order")));
				salaryEarnerSendToAddressList.add(salaryEarnerSendToAddress);
			}
			
		}
		cursor.close();
		return salaryEarnerSendToAddressList;
		
	}
	
	/**��ȡ������	 
	 * 
	 */
	public long getSalaryEarnerSendToAddressCount()
	{
		String sql = "select count(*) from SalaryEarnerSendToAddress";
		
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
	public List<SalaryEarnerSendToAddress> getSalaryEarnerSendToAddressByPage(Pager pager){
		List<SalaryEarnerSendToAddress> salaryEarnerSendToAddressList = null;
		
		
		if(pager !=null){
			String sql = "select * from SalaryEarnerSendToAddress limit ?,?";
			int firstResult = ((pager.getCurrentPage()-1) * pager.getPageSize());		//�ӵڼ�����ݿ�ʼ��ѯ
			int maxResult = pager.getPageSize();	//ÿҳ��ʾ��������¼
			String[] params = {String.valueOf(firstResult),String.valueOf(maxResult)};
			Cursor cursor = sqlHelper.findQuery(sql, params);

			if(cursor != null && cursor.getCount()>0){
				salaryEarnerSendToAddressList = new ArrayList<SalaryEarnerSendToAddress>();
				for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
		         {
					SalaryEarnerSendToAddress salaryEarnerSendToAddress = new SalaryEarnerSendToAddress();
					salaryEarnerSendToAddress.setCode(cursor.getString(cursor.getColumnIndex("code")));
					salaryEarnerSendToAddress.setLoginname(cursor.getString(cursor.getColumnIndex("loginname")));
					salaryEarnerSendToAddress.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
					salaryEarnerSendToAddress.setName(cursor.getString(cursor.getColumnIndex("name")));
					salaryEarnerSendToAddress.setOrder(cursor.getString(cursor.getColumnIndex("order")));
					
					salaryEarnerSendToAddressList.add(salaryEarnerSendToAddress);
		         }
				
			}	
			cursor.close();
		}		
		return salaryEarnerSendToAddressList;
	}

}
