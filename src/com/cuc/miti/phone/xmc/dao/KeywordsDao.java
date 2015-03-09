package com.cuc.miti.phone.xmc.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.cuc.miti.phone.xmc.domain.Keywords;
import com.cuc.miti.phone.xmc.domain.Pager;

public class KeywordsDao {
	private static SQLiteHelper sqlHelper;
	private static final int VERSION = 1;
	
	public KeywordsDao(Context context){
		sqlHelper = new SQLiteHelper(context,VERSION);
	}

	/**
	 * ���һ����¼
	 * @param  
	 * @return
	 */
	public boolean addKeywords(Keywords keywords){
		int count = 0;
		if(keywords!=null){
			String sql = "insert into Keywords (k_id,code,name,[language],pinyin,headchar) values (?,?,?,?,?,?)";
			Object[] params = {keywords.getK_id(),keywords.getCode(),keywords.getName(),keywords.getLanguage(),keywords.getHeadchar()};
			count = sqlHelper.insert(sql, params);
		}
		return count==1?true:false;		
	}
	
	/**
	 * ɾ��һ�����
	 * @param name
	 * @return
	 */	
	public boolean deleteKeywords(String name){
		int count = 0;
		if(name!=null){
			String sql = "delete from Keywords where name = ?";
			String[] params = {name};
			count = sqlHelper.delete(sql, params);
		}
		return count==1?true:false;	
	}
	
	/**
	 * ɾ������
	 * @return 
	 */
	public boolean deleteAllKeywords(){
		String sql = "delete from Keywords";
		sqlHelper.deleteAll(sql);
		return true;

	}
	
	/**
	 * ����
	 * @param 
	 * @return
	 */
	public boolean updateKeywords(Keywords keywords){
		int count = 0;
		if(keywords!=null){
			String sql = "update Keywords set k_id=?, code =?,[language]=?,pinyin=?,headchar=? where name =?";
			Object[] params = {keywords.getK_id(),keywords.getCode(),keywords.getLanguage(),keywords.getPinyin(),keywords.getHeadchar(),keywords.getName()};
			count = sqlHelper.update(sql, params);
		}
		return count==1?true:false;
	}
	
	/**
	 * ��ȡһ����¼
	 */
	public Keywords getKeywords(String name){
		Keywords keywords = new Keywords();
		if(name!=null){
			String sql = "select * from Keywords where name = ?";
			String[] params = {name};
			Cursor cursor = sqlHelper.findQuery(sql, params);
			if(cursor!=null && cursor.getCount()>0){	
				keywords.setK_id(cursor.getString(cursor.getColumnIndex("k_id")));
				keywords.setCode(cursor.getString(cursor.getColumnIndex("code")));
				keywords.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				keywords.setName(cursor.getString(cursor.getColumnIndex("name")));
				keywords.setPinyin(cursor.getString(cursor.getColumnIndex("pinyin")));
				keywords.setHeadchar(cursor.getString(cursor.getColumnIndex("headchar")));

			}
			cursor.close();
		}
		return keywords;		
	}
	
	/**
	 * ��ȡ�б�
	 */
	public List<Keywords> getKeywordsList(){
		
		List<Keywords> keywordsList = new ArrayList<Keywords>();
		String sql = "select * from Keywords";
		Cursor cursor = sqlHelper.findQuery(sql);
		if(cursor!=null && cursor.getCount()>0){
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				Keywords keywords = new Keywords();
				keywords.setK_id(cursor.getString(cursor.getColumnIndex("k_id")));
				keywords.setCode(cursor.getString(cursor.getColumnIndex("code")));
				keywords.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				keywords.setName(cursor.getString(cursor.getColumnIndex("name")));
				keywords.setPinyin(cursor.getString(cursor.getColumnIndex("pinyin")));
				keywords.setHeadchar(cursor.getString(cursor.getColumnIndex("headchar")));

				keywordsList.add(keywords);				
			}
		}
		cursor.close();
		return keywordsList;
	}
	
	/**
	 * ��ݴ�����������ͻ�ȡ�б���Ϣ
	 * @param language
	 * @param queryString
	 * @return
	 */
	public List<Keywords> getKeywordsList(String language,String queryString){
		
		List<Keywords> keywordsList = new ArrayList<Keywords>();
		
		String sql ="";
		if(queryString.equals("")){
			sql = "select * from Keywords where language='" + language +  "'" + " order by k_id";
		}else{
			sql = "select * from Keywords where language='" + language +  "'" + " and (name like '" + queryString + "%' or pinyin like '" + queryString + "%' or headchar like '" + queryString + "%')  order by k_id";
		}
		Cursor cursor = sqlHelper.findQuery(sql);		

		if(cursor!=null && cursor.getCount()>0){
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				Keywords keywords = new Keywords();
				keywords.setK_id(cursor.getString(cursor.getColumnIndex("k_id")));
				keywords.setCode(cursor.getString(cursor.getColumnIndex("code")));
				keywords.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				keywords.setName(cursor.getString(cursor.getColumnIndex("name")));
				keywords.setPinyin(cursor.getString(cursor.getColumnIndex("pinyin")));
				keywords.setHeadchar(cursor.getString(cursor.getColumnIndex("headchar")));

				keywordsList.add(keywords);				
			}
		}
		cursor.close();
		return keywordsList;
	}
	
	/**
	 * ��ȡָ��ҳ��ķ�ҳ���,���ص�ǰҳ���������
	 * SQL : Select * From TABLE_NAME Limit 0,10; 
	 * ��ʾ��TABLE_NAME���ȡ��ݣ��ӵ�0����ݿ�ʼ��ȥ10�� 
	 * @param pager ��ҳ����(CurrentPage-��ǰҳ��PageSize-ÿҳ����totalNum-������<����ֵ>)
	 * @return
	 */
	public List<Keywords> getKeywordsByPage(Pager pager){
		
		List<Keywords> keywordsList = new ArrayList<Keywords>();
		if(pager!= null){
			String sql = "select * from Keywords limit ?,?";
			int firstResult = (pager.getCurrentPage()-1)*pager.getPageSize();
			int maxResult = pager.getPageSize();
			String[] params = {String.valueOf(firstResult),String.valueOf(maxResult)};
			Cursor cursor = sqlHelper.findQuery(sql, params);
			if(cursor!=null && cursor.getCount()>0){
				for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
					Keywords keywords = new Keywords();
					keywords.setK_id(cursor.getString(cursor.getColumnIndex("k_id")));
					keywords.setCode(cursor.getString(cursor.getColumnIndex("code")));
					keywords.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
					keywords.setName(cursor.getString(cursor.getColumnIndex("name")));
					keywords.setPinyin(cursor.getString(cursor.getColumnIndex("pinyin")));
					keywords.setHeadchar(cursor.getString(cursor.getColumnIndex("headchar")));

					keywordsList.add(keywords);				
				}
			}
			cursor.close();
		}
		return keywordsList;
	}
	
	/**
	 * ��ȡ������
	 */	
	public long getKeywordsCount(){
		String sql = "select count(*) from Keywords ";
		Cursor cursor = sqlHelper.findQuery(sql);
		
		cursor.moveToFirst();
		long returnvalue=cursor.getLong(0);
		cursor.close();
		return returnvalue;
	}

}
