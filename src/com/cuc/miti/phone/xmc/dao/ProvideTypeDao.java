package com.cuc.miti.phone.xmc.dao;

import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.Place;
import com.cuc.miti.phone.xmc.domain.ProvideType;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ProvideTypeDao {
	private static SQLiteHelper sqlHelper;
	private static final int VERSION = 1;
	
	public ProvideTypeDao(Context context){
		sqlHelper = new SQLiteHelper(context,VERSION);
	}
	/**
	 * ���һ����¼
	 * @param provideType
	 * @return
	 */
	public boolean addProvideType(ProvideType provideType){
		int count = 0;
		
		if(provideType != null){
			String sql = "insert into ProvideType(pt_id,code,name,[language]) values(?,?,?,?)";
			Object[] params = {provideType.getPt_id(),provideType.getCode(),provideType.getName(),provideType.getLanguage()};
			count = sqlHelper.insert(sql, params);			
		}		
		return count==1?true:false;		
	}
	
	/**
	 * ɾ��һ�����
	 * @param name
	 * @return
	 */	
	public boolean deleteProvideType(String name){
		int count = 0;
		
		if(name != null){
		String sql = "delete from ProvideType where name = ?";
		String[] params = {name};
		count = sqlHelper.delete(sql, params);
		}
		return count==1?true:false;		
	}
	
	/**
	 * ɾ������
	 * @return 
	 */
	public boolean deleteAllProvideType(){
		String sql = "delete from ProvideType";
		sqlHelper.deleteAll(sql);
		return true;

	}
	
	/**
	 * ����
	 * @param provideType
	 * @return
	 */
	public boolean updateProvideType(ProvideType provideType){
		int count = 0;
		if(provideType != null){
			String sql = "update ProvideType set pt_id=?, code = ?,[language] = ? where name = ?";
			Object[] params = {provideType.getPt_id(),provideType.getCode(),provideType.getLanguage(),provideType.getName()};
			count = sqlHelper.update(sql, params);
		}
		return count ==1 ? true:false;
	}
	
	/**
	 * ��ѯһ����Ϣ
	 * @param name
	 * @return
	 */
	public ProvideType getProvideType(String name){
		ProvideType provideType = null;
		if(name != null){
			String sql = "select * from ProvideType where name = ? ";
			String[] params = {name};
			Cursor cursor = sqlHelper.findQuery(sql,params);		
			
			
			if(cursor != null&& cursor.getCount()>0){
				provideType = new ProvideType();
				provideType.setPt_id(cursor.getString(cursor.getColumnIndex("pt_id")));
				provideType.setCode(cursor.getString(cursor.getColumnIndex("code")));
				provideType.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				provideType.setName(cursor.getString(cursor.getColumnIndex("name")));
			}
			cursor.close();
		}
		return provideType;
	}
	
	/**
	 * 
	 * ��ѯ�б�
	 */
	public List<ProvideType> getProvideTypeList(){
		String sql = "select * from ProvideType";
		Cursor cursor = sqlHelper.findQuery(sql);
		
		
		List<ProvideType> provideTypeList =null;
		
		if(cursor!= null&& cursor.getCount()>0){
			provideTypeList = new ArrayList<ProvideType> ();//����
			
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				
				ProvideType provideType = new ProvideType() ;
				
				provideType.setPt_id(cursor.getString(cursor.getColumnIndex("pt_id")));
				provideType.setCode(cursor.getString(cursor.getColumnIndex("code")));
				provideType.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				provideType.setName(cursor.getString(cursor.getColumnIndex("name")));
				
				provideTypeList.add(provideType);
			}
			
		}
		cursor.close();
		return provideTypeList;
		
	}
	
	/**
	 * 
	 * ��ݴ�����������ͻ�ȡ�б���Ϣ
	 */
	public List<ProvideType> getProvideTypeList(String language){
		String sql = "select * from ProvideType where language=? order by pt_id";
		String[] params = {language};
		Cursor cursor = sqlHelper.findQuery(sql,params);		
		
		
		List<ProvideType> provideTypeList =null;
		
		if(cursor!= null&& cursor.getCount()>0){
			provideTypeList = new ArrayList<ProvideType> ();//����
			
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				
				ProvideType provideType = new ProvideType() ;
				
				provideType.setPt_id(cursor.getString(cursor.getColumnIndex("pt_id")));
				provideType.setCode(cursor.getString(cursor.getColumnIndex("code")));
				provideType.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				provideType.setName(cursor.getString(cursor.getColumnIndex("name")));
				
				provideTypeList.add(provideType);
			}
			
		}
		cursor.close();
		return provideTypeList;
		
	}
	
	/**
	 * ��ȡ������
	 */
	public long getProvideTypeCount(){
		
		String sql = "select count(*) from ProvideType";
		
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
	public List<ProvideType> getProvideTypeByPage(Pager pager){
		List<ProvideType> provideTypeList = null;
		
		if(pager !=null){
			String sql = "select * from ProvideType limit ?,?";
			int firstResult = ((pager.getCurrentPage()-1) * pager.getPageSize());		//�ӵڼ�����ݿ�ʼ��ѯ
			int maxResult = pager.getPageSize();	//ÿҳ��ʾ��������¼
			String[] params = {String.valueOf(firstResult),String.valueOf(maxResult)};
			Cursor cursor = sqlHelper.findQuery(sql, params);

			if(cursor != null && cursor.getCount()>0){
				provideTypeList = new ArrayList<ProvideType>();
				for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
		         {
					ProvideType provideType = new ProvideType();
					provideType.setPt_id(cursor.getString(cursor.getColumnIndex("pt_id")));
					provideType.setCode(cursor.getString(cursor.getColumnIndex("code")));
					provideType.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
					provideType.setName(cursor.getString(cursor.getColumnIndex("name")));
					provideTypeList.add(provideType);
		         }
				
			}	
			cursor.close();
		}		
		
		return provideTypeList;
	}

	public boolean multiInsert(List<ProvideType> provideTypeList){
		
		if(provideTypeList !=null && provideTypeList.size() >0){
			
		
		SQLiteDatabase sda = sqlHelper.GetSQLiteDatabase();
		
		sda.beginTransaction();
		
		//For
		
		
		
		try {
			for(ProvideType pt:provideTypeList)
			{
				ContentValues cValues=new ContentValues();
				cValues.put("pt_id", pt.getPt_id());
				cValues.put("code", pt.getCode());
				cValues.put("name", pt.getName());
				cValues.put("language", pt.getLanguage());
				
				sda.insert("ProvideType", null, cValues);
				
			}
			
			sda.setTransactionSuccessful();
		} finally {
			
			sda.endTransaction();
		}
		
		sda.close();
		
		
		return true;
		}else {
			return false;
		}
		
	}
	
	
}
