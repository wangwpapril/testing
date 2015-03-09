package com.cuc.miti.phone.xmc.dao;

import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.domain.NewsCategory;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.Place;
import com.cuc.miti.phone.xmc.domain.Region;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class RegionDao {
	private static SQLiteHelper sqlHelper;
	private static final int VERSION = 1;
	
	public RegionDao(Context context){
		sqlHelper = new SQLiteHelper(context,VERSION);
	}
	/**
	 * ���һ����¼
	 * @param provideType
	 * @return
	 */
	public boolean addRegion(Region region){
		int count = 0;
		
		if(region != null){
			String sql = "insert into Region (r_id,code,name,[language]) values(?,?,?,?)";
			Object[] params = {region.getR_id(),region.getCode(),region.getName(),region.getLanguage()};
			count = sqlHelper.insert(sql, params);			
		}		
		return count==1?true:false;		
	}
	
	/**
	 * ɾ��һ�����
	 * @param name
	 * @return
	 */	
	public boolean deleteRegion(String name){
		int count = 0;
		
		if(name != null){
		String sql = "delete from Region where name = ?";
		String[] params = {name};
		count = sqlHelper.delete(sql, params);
		}
		return count==1?true:false;		
	}
	
	/**
	 * ɾ������
	 * @return 
	 */
	public boolean deleteAllRegion(){
		String sql = "delete from Region";
		sqlHelper.deleteAll(sql);
		return true;

	}
	
	/**
	 * ����
	 * @param region
	 * @return
	 */
	public boolean updateRegion(Region region){
		int count = 0;
		if(region != null){
			String sql = "update Region set r_id=?, code = ?,[language] = ? where name = ?";
			Object[] params = {region.getR_id(),region.getCode(),region.getLanguage(),region.getName()};
			count = sqlHelper.update(sql, params);
		}
		return count ==1 ? true:false;
	}
	
	/**
	 * ��ѯһ����Ϣ
	 * @param name
	 * @return
	 */
	public Region getRegion(String name){
		Region region = null;
		if(name != null){
			String sql = "select * from Region where name = ? ";
			String[] params = {name};
			Cursor cursor = sqlHelper.findQuery(sql,params);		
			
			
			if(cursor != null&& cursor.getCount()>0){
				region = new Region();
				
				region.setR_id(cursor.getString(cursor.getColumnIndex("r_id")));
				region.setCode(cursor.getString(cursor.getColumnIndex("code")));
				region.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				region.setName(cursor.getString(cursor.getColumnIndex("name")));
			}
			cursor.close();
		}
		return region;
	}
	
	/**
	 * 
	 * ��ѯ�б�
	 */
	public List<Region> getRegionList(){
		String sql = "select * from Region";
		Cursor cursor = sqlHelper.findQuery(sql);
		
		List<Region> regionList =null;
		
		if(cursor!= null&& cursor.getCount()>0){
			regionList = new ArrayList<Region> ();//����
			
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				Region region = new Region();
				region.setR_id(cursor.getString(cursor.getColumnIndex("r_id")));
				region.setCode(cursor.getString(cursor.getColumnIndex("code")));
				region.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				region.setName(cursor.getString(cursor.getColumnIndex("name")));
				regionList.add(region);
			}
			
		}
		cursor.close();
		return regionList;
		
	}
	
	/**
	 * 
	 * ��ݴ�����������ͻ�ȡ�б���Ϣ
	 */
	public List<Region> getRegionList(String language){
		String sql = "select * from Region where language=? order by r_id";
		String[] params = {language};
		Cursor cursor = sqlHelper.findQuery(sql,params);		
		
		List<Region> regionList =null;
		
		if(cursor!= null&& cursor.getCount()>0){
			regionList = new ArrayList<Region> ();//����
			
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				Region region = new Region();
				region.setR_id(cursor.getString(cursor.getColumnIndex("r_id")));
				region.setCode(cursor.getString(cursor.getColumnIndex("code")));
				region.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				region.setName(cursor.getString(cursor.getColumnIndex("name")));
				regionList.add(region);
			}
			
		}
		cursor.close();
		return regionList;
		
	}
	/**
	 * 
	 * @param language  ����
	 * @param codeNumber	����λ��(����������)
	 * @return
	 */
	public List<Region> getRegionList(String language,String codeLength){
		String sql = "select * from Region where language=? and length(code)= cast(? as Integer) order by r_id";
		String[] params = {language,codeLength};
		Cursor cursor = sqlHelper.findQuery(sql,params);		
		
		List<Region> regionList =null;
		
		if(cursor!= null&& cursor.getCount()>0){
			regionList = new ArrayList<Region> ();//����
			
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				Region region = new Region();
				region.setR_id(cursor.getString(cursor.getColumnIndex("r_id")));
				region.setCode(cursor.getString(cursor.getColumnIndex("code")));
				region.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				region.setName(cursor.getString(cursor.getColumnIndex("name")));
				regionList.add(region);
			}
			
		}
		cursor.close();
		return regionList;
	}
	
	/**
	 * 
	 * @param language  ����
	 * @param codeNumber	����λ��(����������)
	 * @return
	 */
	public List<Region> getRegionList(String language,String code,String codeLength){
		String sql = "select * from Region where language=? and code like ? and length(code)= cast(? as Integer) order by r_id";
		String[] params = {language,code+"%",codeLength};
		Cursor cursor = sqlHelper.findQuery(sql,params);		
		
		List<Region> regionList =null;
		
		if(cursor!= null&& cursor.getCount()>0){
			regionList = new ArrayList<Region> ();//����
			
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				Region region = new Region();
				region.setR_id(cursor.getString(cursor.getColumnIndex("r_id")));
				region.setCode(cursor.getString(cursor.getColumnIndex("code")));
				region.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				region.setName(cursor.getString(cursor.getColumnIndex("name")));
				regionList.add(region);
			}
			
		}
		cursor.close();
		return regionList;		
	}
	
	/**
	 * ��ȡ������
	 */
	public long getRegionCount(){
		
		String sql = "select count(*) from Region";
		
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
	public List<Region> getRegionByPage(Pager pager){
		List<Region> regionList = null;
		
		if(pager !=null){
			String sql = "select * from Region limit ?,?";
			int firstResult = ((pager.getCurrentPage()-1) * pager.getPageSize());		//�ӵڼ�����ݿ�ʼ��ѯ
			int maxResult = pager.getPageSize();	//ÿҳ��ʾ��������¼
			String[] params = {String.valueOf(firstResult),String.valueOf(maxResult)};
			Cursor cursor = sqlHelper.findQuery(sql, params);

			if(cursor != null && cursor.getCount()>0){
				regionList = new ArrayList<Region>();
				for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
		         {
					Region region = new Region();
					
					region.setR_id(cursor.getString(cursor.getColumnIndex("r_id")));
					region.setCode(cursor.getString(cursor.getColumnIndex("code")));
					region.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
					region.setName(cursor.getString(cursor.getColumnIndex("name")));
					regionList.add(region);
		         }

			}		
			cursor.close();
		}		
		return regionList;
	}
	public boolean multiInsert(List<Region> regionList){
		
		if(regionList !=null && regionList.size() >0){
			
		
		SQLiteDatabase sda = sqlHelper.GetSQLiteDatabase();
		
		sda.beginTransaction();
		
		//For
		
		
		
		try {
			for(Region r:regionList)
			{
				ContentValues cValues=new ContentValues();
				cValues.put("r_id", r.getR_id());
				cValues.put("code", r.getCode());
				cValues.put("name", r.getName());
				cValues.put("language", r.getLanguage());
				
				sda.insert("Region", null, cValues);
				
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
