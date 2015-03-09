package com.cuc.miti.phone.xmc.dao;

import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.domain.EditGroup;
import com.cuc.miti.phone.xmc.domain.Pager;

import android.content.Context;
import android.database.Cursor;

public class EditGroupDao {
	private static SQLiteHelper sqlHelper;
	private static final int VERSION = 1;
	
	public EditGroupDao(Context context){
		sqlHelper = new SQLiteHelper(context,VERSION);
	}

	/**
	 * ���һ����¼
	 * @param  
	 * @return
	 */
	public boolean addEditGroup(EditGroup editGroup){
		int count = 0;
		if(editGroup!=null){
			String sql = "insert into EditGroup (eg_id,code,name,[language]) values (?,?,?,?)";
			Object[] params = {editGroup.getEg_id(),editGroup.getCode(),editGroup.getName(),editGroup.getLanguage()};
			count = sqlHelper.insert(sql, params);
		}
		return count==1?true:false;		
	}
	
	/**
	 * ɾ��һ�����
	 * @param name
	 * @return
	 */	
	public boolean deleteEditGroup(String name){
		int count = 0;
		if(name!=null){
			String sql = "delete from EditGroup where name = ?";
			String[] params = {name};
			count = sqlHelper.delete(sql, params);
		}
		return count==1?true:false;	
	}
	
	/**
	 * ɾ������
	 * @return 
	 */
	public boolean deleteAllEditGroup(){
		String sql = "delete from EditGroup";
		sqlHelper.deleteAll(sql);
		return true;
	}
	
	/**
	 * ����
	 * @param 
	 * @return
	 */
	public boolean updateEditGroup(EditGroup editGroup){
		int count = 0;
		if(editGroup!=null){
			String sql = "update EditGroup set eg_id=?, code =?,[language]=? where name =?";
			Object[] params = {editGroup.getEg_id(),editGroup.getCode(),editGroup.getLanguage(),editGroup.getName()};
			count = sqlHelper.update(sql, params);
		}
		return count==1?true:false;
	}
	
	/**
	 * ��ȡһ����¼
	 */
	public EditGroup getEditGroup(String name){
		EditGroup editGroup = new EditGroup();
		if(name!=null){
			String sql = "select * from EditGroup where name = ?";
			String[] params = {name};
			Cursor cursor = sqlHelper.findQuery(sql, params);
			if(cursor!=null && cursor.getCount()>0){	
				editGroup.setEg_id(cursor.getString(cursor.getColumnIndex("eg_id")));
				editGroup.setCode(cursor.getString(cursor.getColumnIndex("code")));
				editGroup.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				editGroup.setName(cursor.getString(cursor.getColumnIndex("name")));
			}
			cursor.close();
		}
		return editGroup;		
	}
	
	/**
	 * ��ȡ�б�
	 */
	public List<EditGroup> getEditGroupList(){
		
		List<EditGroup> editGroupList = new ArrayList<EditGroup>();
		String sql = "select * from EditGroup";
		Cursor cursor = sqlHelper.findQuery(sql);
		if(cursor!=null && cursor.getCount()>0){
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				EditGroup editGroup = new EditGroup();
				editGroup.setEg_id(cursor.getString(cursor.getColumnIndex("eg_id")));
				editGroup.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				editGroup.setCode(cursor.getString(cursor.getColumnIndex("code")));
				editGroup.setName(cursor.getString(cursor.getColumnIndex("name")));
				editGroupList.add(editGroup);				
			}
		}
		cursor.close();
		return editGroupList;
	}
	/**
	 * ��ȡָ��ҳ��ķ�ҳ���,���ص�ǰҳ���������
	 * SQL : Select * From TABLE_NAME Limit 0,10; 
	 * ��ʾ��TABLE_NAME���ȡ��ݣ��ӵ�0����ݿ�ʼ��ȥ10�� 
	 * @param pager ��ҳ����(CurrentPage-��ǰҳ��PageSize-ÿҳ����totalNum-������<����ֵ>)
	 * @return
	 */
	public List<EditGroup> getEditGroupByPage(Pager pager){
		
		List<EditGroup> editGroupList = new ArrayList<EditGroup>();
		if(pager!= null){
			String sql = "select * from EditGroup limit ?,?";
			int firstResult = (pager.getCurrentPage()-1)*pager.getPageSize();
			int maxResult = pager.getPageSize();
			String[] params = {String.valueOf(firstResult),String.valueOf(maxResult)};
			Cursor cursor = sqlHelper.findQuery(sql, params);
			if(cursor!=null && cursor.getCount()>0){
				for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
					EditGroup editGroup = new EditGroup();
					editGroup.setEg_id(cursor.getString(cursor.getColumnIndex("eg_id")));
					editGroup.setCode(cursor.getString(cursor.getColumnIndex("code")));
					editGroup.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
					editGroup.setName(cursor.getString(cursor.getColumnIndex("name")));
					editGroupList.add(editGroup);				
				}
			}
			cursor.close();
		}
		return editGroupList;
	}
	
	/**
	 * ��ȡ������
	 */	
	public long getEditGroupCount(){
		String sql = "select count(*) from EditGroup ";
		Cursor cursor = sqlHelper.findQuery(sql);
		
		cursor.moveToFirst();
		long returnvalue=cursor.getLong(0);
		cursor.close();
		return returnvalue;
	}

}
