package com.cuc.miti.phone.xmc.dao;

import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.User;
import com.cuc.miti.phone.xmc.utils.Encrypt;

import android.content.Context;
import android.database.Cursor;

public class UserDao {
	
	private static SQLiteHelper sqlHelper;
	private static final int VERSION = 1;
	
	public UserDao(Context context){
		//sqlHelper = new SQLiteHelper(context, VERSION);
		sqlHelper = SQLiteHelper.getHelper(context, VERSION);
	}
	
	/**
	 * ����һ���û���Ϣ
	 * @param user
	 * @return �ɹ�����true��ʧ�ܷ���false
	 */
	public boolean addUser(User user){
		int count = 0 ;
		if(user != null){
			String sql = "insert into User (loginname,password) values (?,?)";
			Object[] params = {user.getUsername(),user.getPassword()};
			count = sqlHelper.insert(sql, params);
		}
			return count==1?true:false;
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @return �ɹ�����User����ʧ�ܷ���null
	 */
	public User getUser(String username){
		String sql = "select * from User where loginname=?";
		String[] params = {username};
		Cursor cursor = sqlHelper.findQuery(sql,params);
		
		User user = null;
		if(cursor !=null && cursor.getCount()>0){
			user =new User();
			user.setUsername(cursor.getString(cursor.getColumnIndex("loginname")));
			user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
		}
		cursor.close();
		return user;
	}
	
	/**
	 * ����û����жϸ��û��Ƿ��Ѿ������ڱ�����ݿ���
	 * @param username
	 * @return
	 */
	public boolean existUser(String username){
		User user = this.getUser(username);
		return user==null?false:true;
	}
	
	/**
	 * ���´����û��������
	 * @param user
	 * @return �ɹ�����true��ʧ�ܷ���false
	 */
	public boolean updateUser(User user){
		int count = 0;
		if(user!= null){
			String sql = "update User set password=? where loginname=?";
			String[] params = {user.getPassword(),user.getUsername()};
			count = sqlHelper.update(sql, params);
		}
		return count==1?true:false;
	}
	
	/**
	 * ��ȡ��ǰ��������б�����û��б�
	 * @return �ɹ�����List��ʧ�ܷ���null
	 */
	public List<User> getUserList(){
		String sql = "select * from User";
		Cursor cursor = sqlHelper.findQuery(sql);
		List<User> userList = null;
		User user = null;
		
		if(cursor != null && cursor.getCount()>0){
			userList = new ArrayList<User>();
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
	         {
				user = new User();
				user.setU_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("u_id"))));
				user.setUsername(cursor.getString(cursor.getColumnIndex("loginname")));
				user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
				
				userList.add(user);
	         }
			
		}	
		cursor.close();
		return userList;
	}
	
	/**
	 * ��ȡָ��ҳ��ķ�ҳ���,���ص�ǰҳ���������
	 * SQL : Select * From TABLE_NAME Limit 0,10; 
	 * ��ʾ��TABLE_NAME���ȡ��ݣ��ӵ�0����ݿ�ʼ��ȥ10�� 
	 * @param pager ��ҳ����(CurrentPage-��ǰҳ��PageSize-ÿҳ����totalNum-������<����ֵ>)
	 * @return
	 */
	public List<User> getUserByPage(Pager pager){
		List<User> userList = null;
		User user = null;
		
		if(pager !=null){
			String sql = "select loginname,password from User limit ?,?";
			int firstResult = ((pager.getCurrentPage()-1) * pager.getPageSize());		//�ӵڼ�����ݿ�ʼ��ѯ
			int maxResult = pager.getPageSize();	//ÿҳ��ʾ��������¼
			String[] params = {String.valueOf(firstResult),String.valueOf(maxResult)};
			Cursor cursor = sqlHelper.findQuery(sql, params);

			if(cursor != null && cursor.getCount()>0){
				userList = new ArrayList<User>();
				for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
		         {
					user = new User();
					user.setU_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("u_id"))));
					user.setUsername(cursor.getString(cursor.getColumnIndex("loginname")));
					user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
					
					userList.add(user);
		         }
				
			}	
			cursor.close();
		}		
		return userList;
	}
	
	/**
	 * ��ݴ�����û���ɾ����ݿ���е��û���Ϣ
	 * @param username 
	 * @return  �ɹ�����true��ʧ�ܷ���false
	 */
	public boolean deleteUser(String username){
		int count = 0;
		if(username !=null)
		{
			String sql = "delete * from User where loginname=?";
			String[] params = {username};
			
			count = sqlHelper.delete(sql, params);
		}
		
		return count==1?true:false;
	}
	
	/**
	 * ɾ������
	 * @return 
	 */
	public boolean deleteAllUser(){
		String sql = "delete * from User";
		sqlHelper.deleteAll(sql);
		return true;

	}
}
