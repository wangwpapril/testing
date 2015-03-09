package com.cuc.miti.phone.xmc.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cuc.miti.phone.xmc.domain.NewsType;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.Place;

public class NewsTypeDao {
	private static SQLiteHelper sqlHelper;
	private static final int VERSION = 1;
	
	public NewsTypeDao(Context context){
		sqlHelper = new SQLiteHelper(context,VERSION);
	}
	/**
	 * ���һ����¼
	 * @param newsType
	 * @return
	 */
	public boolean addNewsType(NewsType newsType){
		int count = 0;
		
		if(newsType != null){
			String sql = "insert into NewsType (nt_id,code,name,[language]) values(?,?,?,?)";
			Object[] params = {newsType.getNt_id(),newsType.getCode(),newsType.getName(),newsType.getLanguage()};
			count = sqlHelper.insert(sql, params);			
		}		
		return count==1?true:false;		
	}
	
	/**
	 * ɾ��һ�����
	 * @param name
	 * @return
	 */	
	public boolean deleteNewsType(String name){
		int count = 0;
		
		if(name != null){
		String sql = "delete from NewsType where name = ?";
		String[] params = {name};
		count = sqlHelper.delete(sql, params);
		}
		return count==1?true:false;		
	}
	
	/**
	 * ɾ������
	 * @return 
	 */
	public boolean deleteAllNewsType(){
		String sql = "delete from NewsType";
		sqlHelper.deleteAll(sql);
		return true;

	}
	
	/**
	 * ����
	 * @param newsType
	 * @return
	 */
	public boolean updateNewsType(NewsType newsType){
		int count = 0;
		if(newsType != null){
			String sql = "update NewsType set nt_id=?, code=?, [language] = ? where name = ?";
			Object[] params = {newsType.getNt_id(),newsType.getCode(),newsType.getLanguage(),newsType.getName()};
			count = sqlHelper.update(sql, params);
		}
		return count ==1 ? true:false;
	}
	
	/**
	 * ��ѯһ����Ϣ
	 * @param name
	 * @return
	 */
	public NewsType getNewsType(String name){
		NewsType newsType = null;
		if(name != null){
			String sql = "select * from NewsType where name = ? ";
			String[] params = {name};
			Cursor cursor = sqlHelper.findQuery(sql,params);		
	
			
			if(cursor != null&& cursor.getCount()>0){
				newsType = new NewsType();
				newsType.setNt_id(cursor.getString(cursor.getColumnIndex("nt_id")));
				newsType.setCode(cursor.getString(cursor.getColumnIndex("code")));
				newsType.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				newsType.setName(cursor.getString(cursor.getColumnIndex("name")));
			}
			cursor.close();
		}
		return newsType;
	}
	
	/**
	 * 
	 * ��ѯ�б�
	 */
	public List<NewsType> getNewsTypeList(){
		String sql = "select * from NewsType";
		Cursor cursor = sqlHelper.findQuery(sql);
		
		List<NewsType> newsTypeList =null;
		
		if(cursor!= null&& cursor.getCount()>0){
			newsTypeList = new ArrayList<NewsType> ();//����
			
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				NewsType newsType = new NewsType();
				newsType.setNt_id(cursor.getString(cursor.getColumnIndex("nt_id")));
				newsType.setCode(cursor.getString(cursor.getColumnIndex("code")));
				newsType.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				newsType.setName(cursor.getString(cursor.getColumnIndex("name")));
				newsTypeList.add(newsType);
			}
			
		}
		cursor.close();
		return newsTypeList;
		
	}
	
	/**
	 * ��ݴ�����������ͻ�ȡ�б���Ϣ
	 * @param language
	 * @return
	 */
	public List<NewsType> getNewsTypeList(String language){
		String sql = "select * from NewsType where language=? order by nt_id";
		String[] params = {language};
		Cursor cursor = sqlHelper.findQuery(sql,params);		
		
		List<NewsType> newsTypeList =null;
		
		if(cursor!= null&& cursor.getCount()>0){
			newsTypeList = new ArrayList<NewsType> ();//����
			
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				NewsType newsType = new NewsType();
				newsType.setNt_id(cursor.getString(cursor.getColumnIndex("nt_id")));
				newsType.setCode(cursor.getString(cursor.getColumnIndex("code")));
				newsType.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				newsType.setName(cursor.getString(cursor.getColumnIndex("name")));
				newsTypeList.add(newsType);
			}
			
		}
		cursor.close();
		return newsTypeList;
		
	}
	/**
	 * ��ȡ������
	 */
	public long getNewsTypeCount(){
		
		String sql = "select count(*) from NewsType";
		
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
	public List<NewsType> getNewsTypeByPage(Pager pager){
		List<NewsType> newsTypeList = null;
		
		if(pager !=null){
			String sql = "select * from NewsType limit ?,?";
			int firstResult = ((pager.getCurrentPage()-1) * pager.getPageSize());		//�ӵڼ�����ݿ�ʼ��ѯ
			int maxResult = pager.getPageSize();	//ÿҳ��ʾ��������¼
			String[] params = {String.valueOf(firstResult),String.valueOf(maxResult)};
			Cursor cursor = sqlHelper.findQuery(sql, params);

			if(cursor != null && cursor.getCount()>0){
				newsTypeList = new ArrayList<NewsType>();
				for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
		         {
					NewsType newsType = new NewsType();
					newsType.setNt_id(cursor.getString(cursor.getColumnIndex("nt_id")));
					newsType.setCode(cursor.getString(cursor.getColumnIndex("code")));
					newsType.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
					newsType.setName(cursor.getString(cursor.getColumnIndex("name")));
					newsTypeList.add(newsType);
		         }
				
			}	
			cursor.close();
		}		
		return newsTypeList;
	}
	
	public boolean multiInsert(List<NewsType> newsTypeList){
		
		if(newsTypeList !=null && newsTypeList.size() >0){
			
		
		SQLiteDatabase sda = sqlHelper.GetSQLiteDatabase();
		
		sda.beginTransaction();
		
		//For
		
		
		
		try {
			for(NewsType nt:newsTypeList)
			{
				ContentValues cValues=new ContentValues();
				cValues.put("nt_id", nt.getNt_id());
				cValues.put("code", nt.getCode());
				cValues.put("name", nt.getName());
				cValues.put("language", nt.getLanguage());
				
				sda.insert("NewsType", null, cValues);
				
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
