package com.cuc.miti.phone.xmc.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cuc.miti.phone.xmc.domain.Keywords;
import com.cuc.miti.phone.xmc.domain.Language;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.utils.Logger;

public class LanguageDao {
	private static SQLiteHelper sqlHelper;
	private static final int VERSION = 1;
	
	public LanguageDao(Context context){
		sqlHelper = new SQLiteHelper(context,VERSION);
	}
	
	/**
	 * ���һ����¼
	 * @param  
	 * @return
	 */
	public boolean addLanguage(Language language){
		int count = 0;
		if(language!=null){
			String sql = "insert into Language (l_id,code,name,[language],[order]) values (?,?,?,?,?)";
			Object[] params = {language.getL_id(),language.getCode(),language.getName(),language.getLanguage(),language.getOrder()};
			count = sqlHelper.insert(sql, params);
		}
		return count==1?true:false;		
	}
	
	/**
	 * ɾ��һ�����
	 * @param name
	 * @return
	 */	
	public boolean deleteLanguage(String name){
		int count = 0;
		if(name!=null){
			String sql = "delete from Language where name = ?";
			String[] params = {name};
			count = sqlHelper.delete(sql, params);
		}
		return count==1?true:false;	
	}
	
	/**
	 * ɾ������
	 * @return 
	 */
	public boolean deleteAllLanguage(){
		String sql = "delete from Language";
		sqlHelper.deleteAll(sql);
		return true;

	}
	
	/**
	 * ����
	 * @param 
	 * @return
	 */
	public boolean updateLanguage(Language language){
		int count = 0;
		if(language!=null){
			String sql = "update Language set l_id=?, code =?,[language]=?,[order]=? where name =?";
			Object[] params = {language.getL_id(),language.getCode(),language.getLanguage(),language.getOrder(),language.getName()};
			count = sqlHelper.update(sql, params);
		}
		return count==1?true:false;
	}
	
	/**
	 * ��ȡһ����¼
	 */
	public Language getLanguage(String name){
		Language language = new Language();
		if(name!=null){
			String sql = "select * from Language where name = ?";
			String[] params = {name};
			Cursor cursor = sqlHelper.findQuery(sql, params);
			if(cursor!=null && cursor.getCount()>0){	
				language.setL_id(cursor.getString(cursor.getColumnIndex("l_id")));
				language.setCode(cursor.getString(cursor.getColumnIndex("code")));
				language.setOrder(cursor.getString(cursor.getColumnIndex("order")));
				language.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				language.setName(cursor.getString(cursor.getColumnIndex("name")));
			}
			cursor.close();
		}
		return language;		
	}
	
	/**
	 * ��ȡ�б�
	 */
	public List<Language> getLanguageList(){
		
		List<Language> languageList = new ArrayList<Language>();
		String sql = "select * from Language";
		Cursor cursor = sqlHelper.findQuery(sql);
		if(cursor!=null && cursor.getCount()>0){
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				Language language = new Language();
				language.setL_id(cursor.getString(cursor.getColumnIndex("l_id")));
				language.setCode(cursor.getString(cursor.getColumnIndex("code")));
				language.setOrder(cursor.getString(cursor.getColumnIndex("order")));
				language.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				language.setName(cursor.getString(cursor.getColumnIndex("name")));
				languageList.add(language);				
			}
		}
		cursor.close();
		return languageList;
	}
	
	/**
	 * ��ݴ�����������ͻ�ȡ�б���Ϣ
	 */
	public List<Language> getLanguageList(String language){
		
		List<Language> languageList = new ArrayList<Language>();
		String sql = "select * from Language where language=? order by l_id ";
		String[] params = {language};
		Cursor cursor = sqlHelper.findQuery(sql, params);
		if(cursor!=null && cursor.getCount()>0){
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				Language languageItem = new Language();
				languageItem.setL_id(cursor.getString(cursor.getColumnIndex("l_id")));
				languageItem.setCode(cursor.getString(cursor.getColumnIndex("code")));
				languageItem.setOrder(cursor.getString(cursor.getColumnIndex("order")));
				languageItem.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				languageItem.setName(cursor.getString(cursor.getColumnIndex("name")));
				languageList.add(languageItem);				
			}
		}
		cursor.close();
		return languageList;
	}
	
	/**
	 * ��ȡָ��ҳ��ķ�ҳ���,���ص�ǰҳ���������
	 * SQL : Select * From TABLE_NAME Limit 0,10; 
	 * ��ʾ��TABLE_NAME���ȡ��ݣ��ӵ�0����ݿ�ʼ��ȥ10�� 
	 * @param pager ��ҳ����(CurrentPage-��ǰҳ��PageSize-ÿҳ����totalNum-������<����ֵ>)
	 * @return
	 */
	public List<Language> getLanguageByPage(Pager pager){
		
		List<Language> languageList = new ArrayList<Language>();
		if(pager!= null){
			String sql = "select * from Language limit ?,?";
			int firstResult = (pager.getCurrentPage()-1)*pager.getPageSize();
			int maxResult = pager.getPageSize();
			String[] params = {String.valueOf(firstResult),String.valueOf(maxResult)};
			Cursor cursor = sqlHelper.findQuery(sql, params);
			if(cursor!=null && cursor.getCount()>0){
				for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
					Language language = new Language();
					language.setL_id(cursor.getString(cursor.getColumnIndex("l_id")));
					language.setCode(cursor.getString(cursor.getColumnIndex("code")));
					language.setOrder(cursor.getString(cursor.getColumnIndex("order")));
					language.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
					language.setName(cursor.getString(cursor.getColumnIndex("name")));
					languageList.add(language);				
				}
			}
			cursor.close();
		}
		return languageList;
	}
	
	/**
	 * ��ȡ������
	 */	
	public long getLanguageCount(){
		String sql = "select count(*) from Language ";
		Cursor cursor = sqlHelper.findQuery(sql);
		
		cursor.moveToFirst();
		long returnvalue=cursor.getLong(0);
		cursor.close();
		return returnvalue;
	}

	
	public boolean multiInsert(List<Language> languageList){
		
		boolean returnValue = false;
		if(languageList ==null || languageList.size() ==0){
			return returnValue;
		}
		
		SQLiteDatabase sda = sqlHelper.GetSQLiteDatabase();	
		try {
			sda.beginTransaction();	
			for(Language l:languageList)
			{
				ContentValues cValues=new ContentValues();
				cValues.put("l_id", l.getL_id());
				cValues.put("code", l.getCode());
				cValues.put("name", l.getName());
				cValues.put("language", l.getLanguage());
				cValues.put("[order]", l.getOrder());			//order�ǹؼ��֣����������
				
				sda.insert("Language", null, cValues);		
			}				
			sda.setTransactionSuccessful();
			
			returnValue = true;
		}catch(Exception ex){
			Logger.e(ex);	
		}
		finally {
			
			sda.endTransaction();
			if(sda!=null){
				sda.close();
				}
		}
		
		return returnValue;
	}
	
}
