package com.cuc.miti.phone.xmc.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cuc.miti.phone.xmc.domain.NewsPriority;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.Place;


public class NewsPriorityDao {
	private static SQLiteHelper sqlHelper;
	private static final int VERSION = 1;
	
	public NewsPriorityDao(Context context){
		sqlHelper = new SQLiteHelper(context,VERSION);
	}
	/**
	 * ���һ����¼
	 * @param sendToAddress
	 * @return
	 */
	public boolean addNewsPriority(NewsPriority newsPriority){
		int count = 0;
		
		if(newsPriority != null){
			String sql = "insert into NewsPriority (np_id,code,name,[language]) values(?,?,?,?)";
			Object[] params = {newsPriority.getNp_id(),newsPriority.getCode(),newsPriority.getName(),newsPriority.getLanguage()};
			count = sqlHelper.insert(sql, params);			
		}		
		return count==1?true:false;		
	}
	
	/**
	 * ɾ��һ�����
	 * @param name
	 * @return
	 */	
	public boolean deleteNewsPriority(String name){
		int count = 0;
		
		if(name != null){
		String sql = "delete from NewsPriority where name = ?";
		String[] params = {name};
		count = sqlHelper.delete(sql, params);
		}
		return count==1?true:false;		
	}
	
	/**
	 * ɾ������
	 * @return 
	 */
	public boolean deleteAllNewsPriority(){
		String sql = "delete from NewsPriority";
		sqlHelper.deleteAll(sql);
		return true;

	}
	
	/**
	 * ����
	 * @param newsPriority
	 * @return
	 */
	public boolean updateNewsPriority(NewsPriority newsPriority){
		int count = 0;
		if(newsPriority != null){
			String sql = "update NewsPriority set np_id=?, code = ?,[language] = ? where name = ?";
			Object[] params = {newsPriority.getNp_id(),newsPriority.getCode(),newsPriority.getLanguage(),newsPriority.getName()};
			count = sqlHelper.update(sql, params);
		}
		return count ==1 ? true:false;
	}
	
	/**
	 * ��ѯһ����Ϣ
	 * @param name
	 * @return
	 */
	public NewsPriority getNewsPriority(String name){//Ϊʲô����sendTo**�ࣿ
		NewsPriority newsPriority = null;
		if(name!=null){
			String sql = "select * from NewsPriority where name = ? ";
			String[] params = {name};
			Cursor cursor = sqlHelper.findQuery(sql,params);		

			
			if(cursor != null&& cursor.getCount()>0){
				newsPriority = new 	NewsPriority();
				newsPriority.setNp_id(cursor.getString(cursor.getColumnIndex("np_id")));
				newsPriority.setCode(cursor.getString(cursor.getColumnIndex("code")));
				newsPriority.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				newsPriority.setName(cursor.getString(cursor.getColumnIndex("name")));
			}
			cursor.close();
		}
		return newsPriority;
	}
	
	/**
	 * 
	 * ��ѯ�б�
	 */
	public List<NewsPriority> getNewsPriorityList(){
		String sql = "select * from NewsPriority";
		Cursor cursor = sqlHelper.findQuery(sql);
		
		List<NewsPriority> newsPriorityList =null;
		
		if(cursor!= null&& cursor.getCount()>0){
			newsPriorityList = new ArrayList<NewsPriority> ();//����
			
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				NewsPriority newsPriority = new NewsPriority();
				newsPriority.setNp_id(cursor.getString(cursor.getColumnIndex("np_id")));
				newsPriority.setCode(cursor.getString(cursor.getColumnIndex("code")));
				newsPriority.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				newsPriority.setName(cursor.getString(cursor.getColumnIndex("name")));
				newsPriorityList.add(newsPriority);

			}
		}
		cursor.close();
		return newsPriorityList;
		
	}
	
	/**
	 * ��ݴ�����������ͻ�ȡ�б���Ϣ
	 * @param language
	 * @return
	 */
	public List<NewsPriority> getNewsPriorityList(String language){
		String sql = "select * from NewsPriority where language=? order by np_id";
		String[] params = {language};
		Cursor cursor = sqlHelper.findQuery(sql,params);		
		
		List<NewsPriority> newsPriorityList =null;
		
		if(cursor!= null&& cursor.getCount()>0){
			newsPriorityList = new ArrayList<NewsPriority> ();//����
			
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				NewsPriority newsPriority = new NewsPriority();
				newsPriority.setNp_id(cursor.getString(cursor.getColumnIndex("np_id")));
				newsPriority.setCode(cursor.getString(cursor.getColumnIndex("code")));
				newsPriority.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				newsPriority.setName(cursor.getString(cursor.getColumnIndex("name")));
				newsPriorityList.add(newsPriority);

			}
		}
		cursor.close();
		return newsPriorityList;
		
	}
	/**
	 * ��ȡ������
	 */
	public long getNewsPriorityCount(){
		
		String sql = "select count(*) from NewsPriority";
		
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
	public List<NewsPriority> getNewsPriorityByPage(Pager pager){
		List<NewsPriority> newsPriorityList = null;
		
		if(pager !=null){
			String sql = "select * from NewsPriority limit ?,?";
			int firstResult = ((pager.getCurrentPage()-1) * pager.getPageSize());		//�ӵڼ�����ݿ�ʼ��ѯ
			int maxResult = pager.getPageSize();	//ÿҳ��ʾ��������¼
			String[] params = {String.valueOf(firstResult),String.valueOf(maxResult)};
			Cursor cursor = sqlHelper.findQuery(sql, params);

			if(cursor != null && cursor.getCount()>0){
				newsPriorityList = new ArrayList<NewsPriority>();
				for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
		         {
					NewsPriority newsPriority = new NewsPriority();
					
					newsPriority.setNp_id(cursor.getString(cursor.getColumnIndex("np_id")));
					newsPriority.setCode(cursor.getString(cursor.getColumnIndex("code")));
					newsPriority.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
					newsPriority.setName(cursor.getString(cursor.getColumnIndex("name")));
					newsPriorityList.add(newsPriority);
		         }
				
			}	
			cursor.close();
		}		
		return newsPriorityList;
	}
	
	public boolean multiInsert(List<NewsPriority> newsPriorityList){
		
		if(newsPriorityList !=null && newsPriorityList.size() >0){
			
		
		SQLiteDatabase sda = sqlHelper.GetSQLiteDatabase();
		
		sda.beginTransaction();
		
		//For
		
		
		
		try {
			for(NewsPriority np:newsPriorityList)
			{
				ContentValues cValues=new ContentValues();
				cValues.put("np_id", np.getNp_id());
				cValues.put("code", np.getCode());
				cValues.put("name", np.getName());
				cValues.put("language", np.getLanguage());
				
				sda.insert("NewsPriority", null, cValues);
				
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
