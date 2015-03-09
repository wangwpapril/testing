package com.cuc.miti.phone.xmc.dao;

import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.ServerAddress;

import android.content.Context;
import android.database.Cursor;


public class ServerAddressDao {
	private static SQLiteHelper sqlHelper;
	private static final int VERSION = 1;
	
	public ServerAddressDao(Context context){
		sqlHelper = new SQLiteHelper(context,VERSION);
	}
	/**
	 * ���һ����¼
	 * @param serverAddress
	 * @return
	 */
	public boolean addServerAddress(ServerAddress serverAddress){
		int count = 0;
		
		if(serverAddress != null){
			String sql = "insert into ServerAddress (code,name,[language],[order]) values(?,?,?,?)";
			Object[] params = {serverAddress.getCode(),serverAddress.getName(),serverAddress.getLanguage(),serverAddress.getOrder()};
			count = sqlHelper.insert(sql, params);			
		}		
		return count==1?true:false;		
	}
	
	/**
	 * ɾ��һ�����
	 * @param name
	 * @return
	 */	
	public boolean deleteServerAddress(String name){
		int count = 0;
		
		if(name != null){
		String sql = "delete from ServerAddress where name = ?";
		String[] params = {name};
		count = sqlHelper.delete(sql, params);
		}
		return count==1?true:false;		
	}
	
	/**
	 * ɾ������
	 * @return 
	 */
	public boolean deleteAllServerAddress(){
		String sql = "delete from ServerAddress";
		sqlHelper.deleteAll(sql);
		return true;

	}
	
	/**
	 * ����
	 * @param serverAddress
	 * @return
	 */
	public boolean updateServerAddress(ServerAddress serverAddress){
		int count = 0;
		if(serverAddress != null){
			String sql = "update ServerAddress set code = ?,[language] = ?,[order]=? where name = ?";
			Object[] params = {serverAddress.getLanguage(),serverAddress.getName()};
			count = sqlHelper.update(sql, params);
		}
		return count ==1 ? true:false;
	}
	
	/**
	 * ��ѯһ����Ϣ
	 * @param name
	 * @return
	 */
	public ServerAddress getServerAddress(String name){
		ServerAddress serverAddress = null;
		if(name != null){
			String sql = "select * from ServerAddress where name = ? ";
			String[] params = {name};
			Cursor cursor = sqlHelper.findQuery(sql,params);		
			
			
			if(cursor != null&& cursor.getCount()>0){
				serverAddress = new ServerAddress();
				serverAddress.setCode(cursor.getString(cursor.getColumnIndex("code")));
				serverAddress.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				serverAddress.setName(cursor.getString(cursor.getColumnIndex("name")));
				serverAddress.setOrder(cursor.getString(cursor.getColumnIndex("order")));
			}
			cursor.close();
		}
		return serverAddress;
	}
	
	/**
	 * ���id��ѯһ����Ϣ
	 * @param name
	 * @return
	 */
	public ServerAddress getServerAddressById(int id){
		ServerAddress serverAddress = null;
		if(id>0){
			String sql = "select * from ServerAddress where name = ? ";
			String[] params = {String.valueOf(id)};
			Cursor cursor = sqlHelper.findQuery(sql,params);			
			
			if(cursor != null&& cursor.getCount()>0){
				serverAddress = new ServerAddress();
				serverAddress.setCode(cursor.getString(cursor.getColumnIndex("code")));
				serverAddress.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				serverAddress.setName(cursor.getString(cursor.getColumnIndex("name")));
				serverAddress.setOrder(cursor.getString(cursor.getColumnIndex("order")));
			}
			cursor.close();
		}
		return serverAddress;
	}
	
	/**
	 * 
	 * ��ѯ�б�
	 */
	public List<ServerAddress> getServerAddressList(){
		String sql = "select * from ServerAddress";
		Cursor cursor = sqlHelper.findQuery(sql);

		
		List<ServerAddress> serverAddressList =null;
		
		if(cursor!= null&& cursor.getCount()>0){
			serverAddressList = new ArrayList<ServerAddress> ();//����
			
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				ServerAddress serverAddress = new ServerAddress();
				serverAddress.setCode(cursor.getString(cursor.getColumnIndex("code")));
				serverAddress.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				serverAddress.setName(cursor.getString(cursor.getColumnIndex("name")));
				serverAddress.setOrder(cursor.getString(cursor.getColumnIndex("order")));
				serverAddressList.add(serverAddress);
			}
			
		}
		cursor.close();
		return serverAddressList;
		
	}
	/**
	 * ��ȡ������
	 */
	public long getServerAddressCount(){
		
		String sql = "select count(*) from ServerAddress";
		
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
	public List<ServerAddress> getServerAddressByPage(Pager pager){
		List<ServerAddress> serverAddressList = null;
		
		if(pager !=null){
			String sql = "select * from ServerAddress limit ?,?";
			int firstResult = ((pager.getCurrentPage()-1) * pager.getPageSize());		//�ӵڼ�����ݿ�ʼ��ѯ
			int maxResult = pager.getPageSize();	//ÿҳ��ʾ��������¼
			String[] params = {String.valueOf(firstResult),String.valueOf(maxResult)};
			Cursor cursor = sqlHelper.findQuery(sql, params);

			if(cursor != null && cursor.getCount()>0){
				serverAddressList = new ArrayList<ServerAddress>();
				for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
		         {
					ServerAddress serverAddress = new ServerAddress();
					serverAddress.setCode(cursor.getString(cursor.getColumnIndex("code")));
					serverAddress.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
					serverAddress.setName(cursor.getString(cursor.getColumnIndex("name")));
					serverAddress.setOrder(cursor.getString(cursor.getColumnIndex("order")));
					
					serverAddressList.add(serverAddress);
					}
				
			}	
			cursor.close();
		}		
		return serverAddressList;
	}

}
