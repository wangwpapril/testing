package com.cuc.miti.phone.xmc.dao;

import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.Place;
import com.cuc.miti.phone.xmc.utils.ChineseToPinyinHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class PlaceDao {
	private static SQLiteHelper sqlHelper;
	private static final int VERSION = 1;
	
	public PlaceDao(Context context){
		sqlHelper = new SQLiteHelper(context,VERSION);
	}
	/**
	 * ���һ����¼
	 * @param placeType
	 * @return
	 */
	public  boolean addPlace(Place place){
		int count = 0;
		
		if(place != null){
			String sql = "insert into Place (p_id,code,name,[language],[desc],pinyin,headchar) values(?,?,?,?,?,?,?)";
			Object[] params = {place.getP_id(),place.getCode(),place.getName(),place.getLanguage(),place.getDesc(),place.getPinyin(),place.getHeadchar()};
			count = sqlHelper.insert(sql, params);						
		}		
		return count==1?true:false;		
	}
	
	/**
	 * ɾ��һ�����
	 * @param name
	 * @return
	 */	
	public boolean deletePlace(String name){
		int count = 0;
		
		if(name != null){
		String sql = "delete from Place where name = ?";
		String[] params = {name};
		count = sqlHelper.delete(sql, params);
		}
		return count==1?true:false;		
	}
	
	/**
	 * ɾ������
	 * @return 
	 */
	public boolean deleteAllPlace(){
		String sql = "delete from Place";
		sqlHelper.deleteAll(sql);
		return true;

	}
	
	/**
	 * ����
	 * @param place
	 * @return
	 */
	public boolean updatePlace(Place place){
		int count = 0;
		if(place != null){
			String sql = "update Place set p_id=?, code = ?,[language] = ?,[desc]=?,pinyin=?,headchar=?  where name = ?";
			Object[] params = {place.getP_id(),place.getCode(),place.getLanguage(),place.getDesc(),place.getPinyin(),place.getHeadchar(),place.getName()};
			count = sqlHelper.update(sql, params);
		}
		return count ==1 ? true:false;
	}
	
	/**
	 * ��ѯһ����Ϣ
	 * @param name
	 * @return
	 */
	public Place getPlace(String name){
		Place place= null;
		if(name != null){
			String sql = "select * from Place where name = ? ";
			String[] params = {name};
			Cursor cursor = sqlHelper.findQuery(sql,params);		
		
			
			if(cursor != null&& cursor.getCount()>0){
				place = new Place();
				place.setP_id(cursor.getString(cursor.getColumnIndex("p_id")));
				place.setCode(cursor.getString(cursor.getColumnIndex("code")));
				place.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				place.setName(cursor.getString(cursor.getColumnIndex("name")));
				place.setDesc(cursor.getString(cursor.getColumnIndex("desc")));
				place.setPinyin(cursor.getString(cursor.getColumnIndex("pinyin")));
				place.setHeadchar(cursor.getString(cursor.getColumnIndex("headchar")));
			}
			cursor.close();
		}
		return place;
	}
	
	/**
	 * 
	 * ��ѯ�б�
	 */
	public List<Place> getPlaceList(){
		String sql = "select * from Place";
		Cursor cursor = sqlHelper.findQuery(sql);
		
		
		List<Place> placeList =null;
		
		if(cursor!= null&& cursor.getCount()>0){
			placeList = new ArrayList<Place> ();//����
			
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				Place place = new Place();
				place.setP_id(cursor.getString(cursor.getColumnIndex("p_id")));
				place.setCode(cursor.getString(cursor.getColumnIndex("code")));
				place.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				place.setName(cursor.getString(cursor.getColumnIndex("name")));
				place.setDesc(cursor.getString(cursor.getColumnIndex("desc")));
				place.setPinyin(cursor.getString(cursor.getColumnIndex("pinyin")));
				place.setHeadchar(cursor.getString(cursor.getColumnIndex("headchar")));

				placeList.add(place);
			}
			
		}
		cursor.close();
		return placeList;
		
	}
	
	/**
	 * 
	 * ��ݴ�����������ͻ�ȡ�б���Ϣ
	 */
	public List<Place> getPlaceList(String language,String queryString){
		String sql ="";
		if(queryString.equals("")){
			sql = "select * from Place where language='" + language +  "'" + " order by p_id";
		}else{
			sql = "select * from Place where language='" + language +  "'" + " and (name like '" + queryString + "%' or pinyin like '" + queryString + "%' or headchar like '" + queryString + "%')  order by p_id";
		}

		Cursor cursor = sqlHelper.findQuery(sql);		
				
		List<Place> placeList =null;
		
		if(cursor!= null&& cursor.getCount()>0){
			placeList = new ArrayList<Place> ();//����
			
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				Place place = new Place();
				place.setP_id(cursor.getString(cursor.getColumnIndex("p_id")));
				place.setCode(cursor.getString(cursor.getColumnIndex("code")));
				place.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				place.setName(cursor.getString(cursor.getColumnIndex("name")));
				place.setDesc(cursor.getString(cursor.getColumnIndex("desc")));
				place.setPinyin(cursor.getString(cursor.getColumnIndex("pinyin")));
				place.setHeadchar(cursor.getString(cursor.getColumnIndex("headchar")));

				placeList.add(place);
			}
			
		}
		cursor.close();
		return placeList;
		
	}
	
	/**
	 * ��ȡ������
	 */
	public long getPlaceCount(){
		
		String sql = "select count(*) from Place";
		
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
	public List<Place> getPlaceByPage(Pager pager){
		List<Place> placeList = null;
		
		if(pager !=null){
			String sql = "select * from Place limit ?,?";
			int firstResult = ((pager.getCurrentPage()-1) * pager.getPageSize());		//�ӵڼ�����ݿ�ʼ��ѯ
			int maxResult = pager.getPageSize();	//ÿҳ��ʾ��������¼
			String[] params = {String.valueOf(firstResult),String.valueOf(maxResult)};
			Cursor cursor = sqlHelper.findQuery(sql, params);

			if(cursor != null && cursor.getCount()>0){
				placeList = new ArrayList<Place>();
				for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
		         {
					Place place = new Place();
					place.setP_id(cursor.getString(cursor.getColumnIndex("p_id")));
					place.setCode(cursor.getString(cursor.getColumnIndex("code")));
					place.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
					place.setName(cursor.getString(cursor.getColumnIndex("name")));
					place.setDesc(cursor.getString(cursor.getColumnIndex("desc")));
					place.setPinyin(cursor.getString(cursor.getColumnIndex("pinyin")));
					place.setHeadchar(cursor.getString(cursor.getColumnIndex("headchar")));

					placeList.add(place);
		         }
				
			}	
			cursor.close();
		}		
		return placeList;
	}
	
	public boolean multiInsert(List<Place> placeList){
		
		if(placeList !=null && placeList.size() >0){
			
		
		SQLiteDatabase sda = sqlHelper.GetSQLiteDatabase();
		
		sda.beginTransaction();
		
		//For
		
		
		
		try {
			for(Place p:placeList)
			{
				ContentValues cValues=new ContentValues();
				cValues.put("p_id", p.getP_id());
				cValues.put("code", p.getCode());
				cValues.put("name", p.getName());
				cValues.put("language", p.getLanguage());
				cValues.put("desc", p.getDesc());
				//cValues.put("pinyin", p.getPinyin());
				cValues.put("pinyin", ChineseToPinyinHelper.getPinYin(p.getName()));
				cValues.put("headchar", ChineseToPinyinHelper.getPinYinHeadChar(p.getName()));
				
				sda.insert("Place", null, cValues);			
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
