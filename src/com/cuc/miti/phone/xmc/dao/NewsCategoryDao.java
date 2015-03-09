package com.cuc.miti.phone.xmc.dao;

import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.domain.NewsCategory;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.Place;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NewsCategoryDao {

	private static SQLiteHelper sqlHelper;
	private static final int VERSION = 1;
	
	public NewsCategoryDao(Context context){
		sqlHelper = new SQLiteHelper(context,VERSION);
	}
	
	/**
	 * ���һ����¼
	 * @param sendToAddress
	 * @return
	 */
	public boolean addNewsCategory(NewsCategory newsCategory){
		int count = 0;
		
		if(newsCategory != null){
			String sql = "insert into NewsCategory (nc_id,code,name,[language],[desc]) values(?,?,?,?,?)";
			Object[] params = {newsCategory.getNc_id(),newsCategory.getCode(),newsCategory.getName(),newsCategory.getLanguage(),newsCategory.getDesc()};
			count = sqlHelper.insert(sql, params);			
		}		
		return count==1?true:false;		
	}
	
	/**
	 * ɾ��һ�����
	 * @param name
	 * @return
	 */	
	public boolean deleteNewsCategory(String name){
		int count = 0;
		
		if(name != null){
		String sql = "delete from NewsCategory where name = ?";
		String[] params = {name};
		count = sqlHelper.delete(sql, params);
		}
		return count==1?true:false;		
	}
	
	/**
	 * ɾ������
	 * @return 
	 */
	public boolean deleteAllNewsCategory(){
		String sql = "delete from NewsCategory";
		sqlHelper.deleteAll(sql);
		return true;

	}
	
	/**
	 * ����
	 * @param newsPriority
	 * @return
	 */
	public boolean updateNewsCategory(NewsCategory newsCategory){
		int count = 0;
		if(newsCategory != null){
			String sql = "update NewsCategory set nc_id=?, code = ?,[language] = ?,[desc]=? where name = ?";
			Object[] params = {newsCategory.getNc_id(),newsCategory.getCode(),newsCategory.getLanguage(),newsCategory.getDesc(),newsCategory.getName()};
			count = sqlHelper.update(sql, params);
		}
		return count ==1 ? true:false;
	}
	
	/**
	 * ��ѯһ����Ϣ
	 * @param name
	 * @return
	 */
	public NewsCategory getNewsCategory(String name){
		NewsCategory newsCategory = null;
		if(name!=null){
			String sql = "select * from NewsCategory where name = ? ";
			String[] params = {name};
			Cursor cursor = sqlHelper.findQuery(sql,params);		
			
			
			if(cursor != null&& cursor.getCount()>0){
				newsCategory = new 	NewsCategory();
				newsCategory.setNc_id(cursor.getString(cursor.getColumnIndex("nc_id")));
				newsCategory.setCode(cursor.getString(cursor.getColumnIndex("code")));
				newsCategory.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				newsCategory.setName(cursor.getString(cursor.getColumnIndex("name")));
				newsCategory.setDesc(cursor.getString(cursor.getColumnIndex("desc")));
			}
			cursor.close();
		}
		return newsCategory;
	}
	
	/**
	 * 
	 * ��ѯ�б�
	 */
	public List<NewsCategory> getNewsCategoryList(){
		String sql = "select * from NewsCategory";
		Cursor cursor = sqlHelper.findQuery(sql);
		
		List<NewsCategory> newsCategoryList =null;
		
		if(cursor!= null&& cursor.getCount()>0){
			newsCategoryList = new ArrayList<NewsCategory> ();//����
			
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				NewsCategory newsCategory = new NewsCategory();
				newsCategory.setNc_id(cursor.getString(cursor.getColumnIndex("nc_id")));
				newsCategory.setCode(cursor.getString(cursor.getColumnIndex("code")));
				newsCategory.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				newsCategory.setName(cursor.getString(cursor.getColumnIndex("name")));
				newsCategory.setDesc(cursor.getString(cursor.getColumnIndex("desc")));
				newsCategoryList.add(newsCategory);
			}
			
		}
		cursor.close();
		return newsCategoryList;
		
	}
	
	/**
	 * 
	 * @param language  ����
	 * @param codeNumber	����λ��(����������)
	 * @return
	 */
	public List<NewsCategory> getNewsCategoryList(String language,String codeLength){
		String sql = "select * from NewsCategory where language=? and length(code)= cast(? as Integer) order by code";
		String[] params = {language,codeLength};
		Cursor cursor = sqlHelper.findQuery(sql,params);		
		
		List<NewsCategory> newsCategoryList =null;
		
		if(cursor!= null&& cursor.getCount()>0){
			newsCategoryList = new ArrayList<NewsCategory> ();//����
			
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				NewsCategory newsCategory = new NewsCategory();
				newsCategory.setNc_id(cursor.getString(cursor.getColumnIndex("nc_id")));
				newsCategory.setCode(cursor.getString(cursor.getColumnIndex("code")));
				newsCategory.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				newsCategory.setName(cursor.getString(cursor.getColumnIndex("name")));
				newsCategory.setDesc(cursor.getString(cursor.getColumnIndex("desc")));
				newsCategoryList.add(newsCategory);
			}
			
		}
		cursor.close();
		return newsCategoryList;		
	}
	
	/**
	 * 
	 * @param language  ����
	 * @param codeNumber	����λ��(����������)
	 * @return
	 */
	public List<NewsCategory> getNewsCategoryList(String language,String code,String codeLength){
		String sql = "select * from NewsCategory where language=? and code like ? and length(code)= cast(? as Integer) order by code";
		String[] params = {language,code+"%",codeLength};
		Cursor cursor = sqlHelper.findQuery(sql,params);		
		
		List<NewsCategory> newsCategoryList =null;
		
		if(cursor!= null&& cursor.getCount()>0){
			newsCategoryList = new ArrayList<NewsCategory> ();//����
			
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				NewsCategory newsCategory = new NewsCategory();
				newsCategory.setNc_id(cursor.getString(cursor.getColumnIndex("nc_id")));
				newsCategory.setCode(cursor.getString(cursor.getColumnIndex("code")));
				newsCategory.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				newsCategory.setName(cursor.getString(cursor.getColumnIndex("name")));
				newsCategory.setDesc(cursor.getString(cursor.getColumnIndex("desc")));
				newsCategoryList.add(newsCategory);
			}
			
		}
		cursor.close();
		return newsCategoryList;		
	}
	
	/**
	 * ��ȡ������
	 */
	public long getNewsCategoryCount(){
		
		String sql = "select count(*) from NewsCategory";
		
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
	public List<NewsCategory> getNewsCategoryByPage(Pager pager){
		List<NewsCategory> newsCategoryList = null;		
		if(pager !=null){
			String sql = "select * from NewsCategory limit ?,?";
			int firstResult = ((pager.getCurrentPage()-1) * pager.getPageSize());		//�ӵڼ�����ݿ�ʼ��ѯ
			int maxResult = pager.getPageSize();	//ÿҳ��ʾ��������¼
			String[] params = {String.valueOf(firstResult),String.valueOf(maxResult)};
			Cursor cursor = sqlHelper.findQuery(sql, params);

			if(cursor != null && cursor.getCount()>0){
				newsCategoryList = new ArrayList<NewsCategory>();
				for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
		         {
					NewsCategory newsCategory = new NewsCategory();
					newsCategory.setNc_id(cursor.getString(cursor.getColumnIndex("nc_id")));
					newsCategory.setCode(cursor.getString(cursor.getColumnIndex("code")));
					newsCategory.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
					newsCategory.setName(cursor.getString(cursor.getColumnIndex("name")));
					newsCategory.setDesc(cursor.getString(cursor.getColumnIndex("desc")));
					newsCategoryList.add(newsCategory);
				}
			
			}		
			cursor.close();
		}		
		return newsCategoryList;
	}
	
	public boolean multiInsert(List<NewsCategory> newsCategoryList){
		
		if(newsCategoryList !=null && newsCategoryList.size() >0){
			
		
		SQLiteDatabase sda = sqlHelper.GetSQLiteDatabase();
		
		sda.beginTransaction();
		
		//For
		
		
		
		try {
			for(NewsCategory nc:newsCategoryList)
			{
				ContentValues cValues=new ContentValues();
				cValues.put("nc_id", nc.getNc_id());
				cValues.put("code", nc.getCode());
				cValues.put("name", nc.getName());
				cValues.put("language", nc.getLanguage());
				cValues.put("desc", nc.getDesc());
				
				sda.insert("NewsCategory", null, cValues);
				
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
