package com.cuc.miti.phone.xmc.dao;

import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.domain.Accessories;
import com.cuc.miti.phone.xmc.domain.Pager;

import android.content.Context;
import android.database.Cursor;


public class AccessoriesDao {
	private static SQLiteHelper sqlHelper;
	private static final int VERSION = 1;
	
	public AccessoriesDao(Context context){
		sqlHelper = new SQLiteHelper(context,VERSION);
	}
	/**
	 * ���һ����¼
	 * @param accessories  
	 * @return
	 */
	public boolean addAccessories(Accessories accessories){
		int count = 0;
		
		if(accessories != null){
			String sql = "insert into Accessories (a_id, m_id,createtime,title,[desc],[size],[type],originalName,info,url) values(?,?,?,?,?,?,?,?,?,?)";
			Object[] params = {accessories.getA_id(), accessories.getM_id(),accessories.getCreatetime(),accessories.getTitle(),accessories.getDesc(),accessories.getSize(),accessories.getType(),accessories.getOriginalName(),accessories.getInfo(),accessories.getUrl()};
			count = sqlHelper.insert(sql, params);			
		}		
		return count==1?true:false;		
	}
	
	/**
	 * ɾ��һ�����
	 * @param name
	 * @return
	 */	
	public boolean deleteAccessories(String name){
		int count = 0;
		
		if(name != null){
		String sql = "delete from Accessories where originalName = ?";
		String[] params = {name};
		count = sqlHelper.delete(sql, params);
		}
		return count==1?true:false;		
	}
	
	/**
	 * ɾ��һ�����
	 * @param name
	 * @return
	 */	
	public boolean deleteAccessoriesByID(String id){
		int count = 0;
		
		if(id != null){
		String sql = "delete from Accessories where a_id = ?";
		String[] params = {id};
		count = sqlHelper.delete(sql, params);
		}
		return count==1?true:false;		
	}
	
	/**
	 * ��ݸ��IDɾ���������и���
	 * @param name
	 * @return
	 */	
	public boolean deleteAccessoriesByMID(String m_id){
		int count = 0;
		
		if(m_id != null){
		String sql = "delete from Accessories where m_id = ?";
		String[] params = {m_id};
		count = sqlHelper.delete(sql, params);
		}
		return count==1?true:false;		
	}
	
	/**
	 * ɾ������
	 * @return 
	 */
	public boolean deleteAllAccessories(){
		String sql = "delete from Accessories";
	    sqlHelper.deleteAll(sql);
		return true;		
	}
	
	/**
	 * ����
	 * @param accessories
	 * @return
	 */
	public boolean updateAccessories(Accessories accessories){
		int count = 0;
		if(accessories != null){
			String sql = "update Accessories set originalName =?, createtime=? ,title = ? ,desc=? ,size=? ,type=? ,info=? ,url=?, m_id=? where a_id = ?";
			Object[] params = {accessories.getOriginalName(),accessories.getCreatetime(),accessories.getTitle(),accessories.getDesc(),accessories.getSize(),accessories.getType(),accessories.getInfo(),accessories.getUrl(), accessories.getM_id(),accessories.getA_id()};
			count = sqlHelper.update(sql, params);
		}
		return count ==1 ? true : false;
	}
	
	/**
	 * ��ѯһ����Ϣ
	 * @param name
	 * @return
	 */
	public Accessories getAccessories(String a_id){
		Accessories accessories = new Accessories();;
		if(a_id != null){
			String sql = "select * from Accessories where a_id = ? ";
			String[] params = {a_id};
			Cursor cursor = sqlHelper.findQuery(sql,params);
					
			if(cursor != null&& cursor.getCount()>0){
				accessories.setA_id((cursor.getString(cursor.getColumnIndex("a_id"))));
				accessories.setM_id(cursor.getString(cursor.getColumnIndex("m_id")));
				accessories.setCreatetime(cursor.getString(cursor.getColumnIndex("createtime")));
				accessories.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				accessories.setDesc(cursor.getString(cursor.getColumnIndex("desc")));
				accessories.setType(cursor.getString(cursor.getColumnIndex("type")));
				accessories.setOriginalName(cursor.getString(cursor.getColumnIndex("originalName")));
				accessories.setInfo(cursor.getString(cursor.getColumnIndex("info")));
				accessories.setUrl(cursor.getString(cursor.getColumnIndex("url")));
				accessories.setUrl(cursor.getString(cursor.getColumnIndex("size")));
			}
			cursor.close();
		}
		
		return accessories;
	  
	}
	
	/**
	 * ��ݸ��Id����ѯ��������б�����
	 * @param m_id
	 * @return
	 */
	public long getAccessoriesCount(String m_id){
		String sql = "select count(*) from Accessories where m_id = '" + m_id + "'";
		
		return sqlHelper.rowCount(sql);
	}
	
	/**
	 * 
	 * ��ݸ��Id����ѯ��������б�
	 */
	public List<Accessories> getAccessoriesListByMID(String m_id){
		List<Accessories> accessoriesList =new ArrayList<Accessories>();
		String sql = "select * from Accessories where m_id = ?";
		
		String[] params = {m_id};
		
		Cursor cursor = sqlHelper.findQuery(sql, params);

		if(cursor!= null&& cursor.getCount()>0){			
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				Accessories accessories = new Accessories();
				accessories.setA_id((cursor.getString(cursor.getColumnIndex("a_id"))));
				accessories.setM_id(cursor.getString(cursor.getColumnIndex("m_id")));
				accessories.setCreatetime(cursor.getString(cursor.getColumnIndex("createtime")));
				accessories.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				accessories.setDesc(cursor.getString(cursor.getColumnIndex("desc")));
				accessories.setType(cursor.getString(cursor.getColumnIndex("type")));
				accessories.setOriginalName(cursor.getString(cursor.getColumnIndex("originalName")));
				accessories.setInfo(cursor.getString(cursor.getColumnIndex("info")));
				accessories.setUrl(cursor.getString(cursor.getColumnIndex("url")));
				accessories.setSize(cursor.getString(cursor.getColumnIndex("size")));
				accessoriesList.add(accessories);
			}		
			cursor.close();
		}
		return accessoriesList;
		
	}
	
	
//	/**
//	 * ���id���¼
//	 */
//	public Accessories getAccessoriesById(int id){
//		Accessories accessories = null;
//		if(id>0){
//			String sql = "select * from Accessories where a_id = ? ";
//			String[] params = {String.valueOf(id)};
//			Cursor cursor = sqlHelper.findQuery(sql,params);
//					
//			if(cursor != null&& cursor.getCount()>0){
//				accessories = new Accessories();
//				accessories.setA_id(cursor.getString(cursor.getColumnIndex("a_id")));
//				accessories.setM_id(cursor.getString(cursor.getColumnIndex("m_id")));
//				accessories.setCreatetime(cursor.getString(cursor.getColumnIndex("createtime")));
//				accessories.setTitle(cursor.getString(cursor.getColumnIndex("title")));
//				accessories.setDesc(cursor.getString(cursor.getColumnIndex("desc")));
//				accessories.setType(cursor.getString(cursor.getColumnIndex("type")));
//				accessories.setOriginalName(cursor.getString(cursor.getColumnIndex("originalName")));
//				accessories.setInfo(cursor.getString(cursor.getColumnIndex("info")));
//				accessories.setUrl(cursor.getString(cursor.getColumnIndex("url")));
//			}
//		}
//		return accessories;
//	}
	
	/**
	 * 
	 * ��ѯ�б�
	 */
	public List<Accessories> getAccessoriesList(){
		List<Accessories> accessoriesList =new ArrayList<Accessories>();
		String sql = "select * from Accessories";
		Cursor cursor = sqlHelper.findQuery(sql);

		if(cursor!= null&& cursor.getCount()>0){			
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				Accessories accessories = new Accessories();
				accessories.setA_id((cursor.getString(cursor.getColumnIndex("a_id"))));
				accessories.setM_id(cursor.getString(cursor.getColumnIndex("m_id")));
				accessories.setCreatetime(cursor.getString(cursor.getColumnIndex("createtime")));
				accessories.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				accessories.setDesc(cursor.getString(cursor.getColumnIndex("desc")));
				accessories.setType(cursor.getString(cursor.getColumnIndex("type")));
				accessories.setOriginalName(cursor.getString(cursor.getColumnIndex("originalName")));
				accessories.setInfo(cursor.getString(cursor.getColumnIndex("info")));
				accessories.setUrl(cursor.getString(cursor.getColumnIndex("url")));
				accessories.setSize(cursor.getString(cursor.getColumnIndex("size")));
				accessoriesList.add(accessories);
			}	
			
			cursor.close();
		}
		return accessoriesList;
		
	}
	
	/**
	 * ��ȡָ��ҳ��ķ�ҳ���,���ص�ǰҳ���������
	 * SQL : Select * From TABLE_NAME Limit 0,10; 
	 * ��ʾ��TABLE_NAME���ȡ��ݣ��ӵ�0����ݿ�ʼ��ȥ10�� 
	 * @param pager ��ҳ����(CurrentPage-��ǰҳ��PageSize-ÿҳ����totalNum-������<����ֵ>)
	 * @return
	 */
	public List<Accessories> getAccessoriesByPage(Pager pager){
	
		List<Accessories> accessoriesList = null;                   
		
		if(pager != null){
			String sql = "select * from Accessories limit ?,?";
			int firstResult = (pager.getCurrentPage()-1)*pager.getPageSize();
			int maxResult = pager.getPageSize();
			String[] params = {String.valueOf(firstResult),String.valueOf(maxResult)};
			Cursor cursor = sqlHelper.findQuery(sql, params);
			accessoriesList = new ArrayList<Accessories>();
			if(cursor != null&& cursor.getCount()>0){
				for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
					Accessories accessories = new Accessories();
					accessories.setM_id(cursor.getString(cursor.getColumnIndex("m_id")));
					accessories.setCreatetime(cursor.getString(cursor.getColumnIndex("createtime")));
					accessories.setTitle(cursor.getString(cursor.getColumnIndex("title")));
					accessories.setDesc(cursor.getString(cursor.getColumnIndex("desc")));
					accessories.setType(cursor.getString(cursor.getColumnIndex("type")));
					accessories.setOriginalName(cursor.getString(cursor.getColumnIndex("originalName")));
					accessories.setInfo(cursor.getString(cursor.getColumnIndex("info")));
					accessories.setUrl(cursor.getString(cursor.getColumnIndex("url")));
					accessories.setSize(cursor.getString(cursor.getColumnIndex("size")));
					accessoriesList.add(accessories);
				}
			}	
			cursor.close();
		}
		
		return accessoriesList;
		
	}
	
	/**
	 * ��ȡ������
	 */
	public long getAccessoriesCount()
	{
		String sql = "select count(*) from Accessories";
		
		Cursor cursor = sqlHelper.findQuery(sql,null);
		cursor.moveToFirst();
		long temp=cursor.getLong(0);
		cursor.close();
		return temp;
	}


}
