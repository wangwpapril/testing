package com.cuc.miti.phone.xmc.dao;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;

import com.cuc.miti.phone.xmc.domain.Language;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.SendToAddress;
import com.cuc.miti.phone.xmc.utils.ChineseToPinyinHelper;
import com.cuc.miti.phone.xmc.utils.Logger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SendToAddressDao {
	private static SQLiteHelper sqlHelper;
	private static final int VERSION = 1;
	
	public SendToAddressDao(Context context){
		sqlHelper = new SQLiteHelper(context,VERSION);
	}
	/**
	 * ���һ����¼
	 * @param sendToAddress
	 * @return
	 */
	public boolean addSendToAddress(SendToAddress sendToAddress){
		int count = 0;
		
		if(sendToAddress != null){
			String sql = "insert into SendToAddress (code,name,[language],[order],[type],pinyin,headchar) values(?,?,?,?,?,?,?)";
			Object[] params = {sendToAddress.getCode(),
					sendToAddress.getName(),
					sendToAddress.getLanguage(),
					sendToAddress.getOrder(),
					sendToAddress.getType(),
					sendToAddress.getPinyin(),
					sendToAddress.getHeadchar()};
			count = sqlHelper.insert(sql, params);			
		}		
		return count==1?true:false;		
	}
	
	/**
	 * ɾ��һ�����
	 * @param name
	 * @return
	 */	
	public boolean deleteSendToAddress(String name){
		int count = 0;
		
		if(name != null){
		String sql = "delete from SendToAddress where name = ?";
		String[] params = {name};
		count = sqlHelper.delete(sql, params);
		}
		return count==1?true:false;		
	}
	
	/**
	 * ɾ������
	 * @return 
	 */
	public boolean deleteAllSendToAddress(){
		String sql = "delete from SendToAddress";
		sqlHelper.deleteAll(sql);
		return true;

	}
	
	/**
	 * ����
	 * @param sendToAddress
	 * @return
	 */
	public boolean updateSendToAddress(SendToAddress sendToAddress){
		int count = 0;
		if(sendToAddress != null){
			String sql = "update SendToAddress set code= ?, [language] = ?,[order]= ?,[type]=?,pinyin=?,headchar=? where name = ?";
			Object[] params = {sendToAddress.getCode(),
											sendToAddress.getLanguage(),
											sendToAddress.getOrder(),
											sendToAddress.getType(),
											sendToAddress.getPinyin(),
											sendToAddress.getName()};
			count = sqlHelper.update(sql, params);
		}
		return count ==1 ? true:false;
	}
	
	/**
	 * ��ѯһ����Ϣ
	 * @param name
	 * @return
	 */
	public SendToAddress getSendToAddress(String name){
		SendToAddress sendToAddress = new SendToAddress();
		if(name!=null){
			String sql = "select * from SendToAddress where name = ? and language='zh-CHS'";
			String[] params = {name};
			Cursor cursor = sqlHelper.findQuery(sql,params);		
			if(cursor != null&& cursor.getCount()>0){				
				sendToAddress.setCode(cursor.getString(cursor.getColumnIndex("code")));
				sendToAddress.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				sendToAddress.setName(cursor.getString(cursor.getColumnIndex("name")));
				sendToAddress.setOrder(cursor.getString(cursor.getColumnIndex("order")));
				sendToAddress.setType(cursor.getString(cursor.getColumnIndex("type")));
				sendToAddress.setPinyin(cursor.getString(cursor.getColumnIndex("pinyin")));
				sendToAddress.setHeadchar(cursor.getString(cursor.getColumnIndex("headchar")));

			}
			cursor.close();
		}
		return sendToAddress;
	}
	
	/**
	 * ���id��ѯһ����Ϣ
	 * @param name
	 * @return
	 */
	public SendToAddress getSendToAddressById(int id){
		SendToAddress sendToAddress = new SendToAddress();
		if(id>0){
			String sql = "select * from SendToAddress where sta_id = ? ";
			String[] params = {String.valueOf(id)};
			Cursor cursor = sqlHelper.findQuery(sql,params);		
			if(cursor != null&& cursor.getCount()>0){
				sendToAddress.setSta_id(cursor.getColumnIndex("sta_id"));
				sendToAddress.setCode(cursor.getString(cursor.getColumnIndex("code")));
				sendToAddress.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				sendToAddress.setName(cursor.getString(cursor.getColumnIndex("name")));
				sendToAddress.setOrder(cursor.getString(cursor.getColumnIndex("order")));
				sendToAddress.setType(cursor.getString(cursor.getColumnIndex("type")));
				sendToAddress.setPinyin(cursor.getString(cursor.getColumnIndex("pinyin")));
				sendToAddress.setHeadchar(cursor.getString(cursor.getColumnIndex("headchar")));
			}
			cursor.close();
		}
		return sendToAddress;
	}
	
	/**
	 * 
	 * ��ѯ�б�
	 */
	public List<SendToAddress> getSendToAddressList(){
		String sql = "select * from SendToAddress";
		Cursor cursor = sqlHelper.findQuery(sql);

		List<SendToAddress> sendToAddressList =null;
		
		if(cursor!= null&& cursor.getCount()>0){
			sendToAddressList = new ArrayList<SendToAddress> ();//����
			
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				SendToAddress sendToAddress = new SendToAddress();
				sendToAddress.setCode(cursor.getString(cursor.getColumnIndex("code")));
				sendToAddress.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				sendToAddress.setName(cursor.getString(cursor.getColumnIndex("name")));
				sendToAddress.setOrder(cursor.getString(cursor.getColumnIndex("order")));
				sendToAddress.setType(cursor.getString(cursor.getColumnIndex("type")));
				sendToAddress.setPinyin(cursor.getString(cursor.getColumnIndex("pinyin")));
				sendToAddress.setHeadchar(cursor.getString(cursor.getColumnIndex("headchar")));
				
				sendToAddressList.add(sendToAddress);
			}
			
		}
		cursor.close();
		return sendToAddressList;
		
	}
	
	/**
	 * ��ݴ��������ֵ�����з�������ķ����ַ�б�
	 * @param language
	 * @return
	 */
	public List<SendToAddress> getSendToAddressList(String language,String queryString){
		
		List<SendToAddress> sendToAddressList =null;

		String sql ="";
		if(queryString.equals("")){
			sql = "select * from SendToAddress where language='" + language +  "'" + " order by [order]";
		}else{
			sql = "select * from SendToAddress where language='" + language +  "'" + " and (name like '" + queryString + "%' or pinyin like '" + queryString + "%' or headchar like '" + queryString + "%')  order by [order]";
		}
		
		Cursor cursor = sqlHelper.findQuery(sql);		
		
		if(cursor!= null&& cursor.getCount()>0){
			sendToAddressList = new ArrayList<SendToAddress> ();//����
			
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				SendToAddress sendToAddress = new SendToAddress();
				sendToAddress.setCode(cursor.getString(cursor.getColumnIndex("code")));
				sendToAddress.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				sendToAddress.setName(cursor.getString(cursor.getColumnIndex("name")));
				sendToAddress.setOrder(cursor.getString(cursor.getColumnIndex("order")));
				sendToAddress.setType(cursor.getString(cursor.getColumnIndex("type")));
				sendToAddress.setPinyin(cursor.getString(cursor.getColumnIndex("pinyin")));
				sendToAddress.setHeadchar(cursor.getString(cursor.getColumnIndex("headchar")));

				sendToAddressList.add(sendToAddress);
			}
			
		}
		cursor.close();
		return sendToAddressList;
		
	}
	
	/**��ȡ������	 
	 * 
	 */
	public long getSendToAddressCount()
	{
		String sql = "select count(*) from SendToAddress";
		
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
	public List<SendToAddress> getSendToAddressByPage(Pager pager){
		List<SendToAddress> sendToAddressList = null;
		SendToAddress sendToAddress = null;
		
		if(pager !=null){
			String sql = "select * from SendToAddress limit ?,?";
			int firstResult = ((pager.getCurrentPage()-1) * pager.getPageSize());		//�ӵڼ�����ݿ�ʼ��ѯ
			int maxResult = pager.getPageSize();	//ÿҳ��ʾ��������¼
			String[] params = {String.valueOf(firstResult),String.valueOf(maxResult)};
			Cursor cursor = sqlHelper.findQuery(sql, params);

			if(cursor != null && cursor.getCount()>0){
				sendToAddressList = new ArrayList<SendToAddress>();
				for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
		         {
					sendToAddress = new SendToAddress();
					sendToAddress.setCode(cursor.getString(cursor.getColumnIndex("code")));
					sendToAddress.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
					sendToAddress.setName(cursor.getString(cursor.getColumnIndex("name")));
					sendToAddress.setOrder(cursor.getString(cursor.getColumnIndex("order")));
					sendToAddress.setType(cursor.getString(cursor.getColumnIndex("type")));
					sendToAddress.setPinyin(cursor.getString(cursor.getColumnIndex("pinyin")));
					sendToAddress.setHeadchar(cursor.getString(cursor.getColumnIndex("headchar")));

					sendToAddressList.add(sendToAddress);
		         }
				
			}	
			cursor.close();
		}		
		return sendToAddressList;
	}
	public SendToAddress getSendToAddressByCode(String language, String code) {
		SendToAddress sendToAddress = new SendToAddress();

		String sql ="";
		sql = "select * from SendToAddress where language='" + language +  "' and code='"+code+ "' order by [order]";
		
			Cursor cursor = sqlHelper.findQuery(sql,null);		
			if(cursor != null&& cursor.getCount()>0){
				sendToAddress.setSta_id(cursor.getColumnIndex("sta_id"));
				sendToAddress.setCode(cursor.getString(cursor.getColumnIndex("code")));
				sendToAddress.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
				sendToAddress.setName(cursor.getString(cursor.getColumnIndex("name")));
				sendToAddress.setOrder(cursor.getString(cursor.getColumnIndex("order")));
				sendToAddress.setType(cursor.getString(cursor.getColumnIndex("type")));
				sendToAddress.setPinyin(cursor.getString(cursor.getColumnIndex("pinyin")));
				sendToAddress.setHeadchar(cursor.getString(cursor.getColumnIndex("headchar")));
			}
	
		return sendToAddress;
	}
	/**
	 *  �������������ݿ�
	 * @param sendToAddresseList
	 * @return
	 */
	public boolean multiInsert(List<SendToAddress> sendToAddresseList){
		
		boolean returnValue = false;
		if(sendToAddresseList ==null || sendToAddresseList.size() ==0){
			return returnValue;
		}
		
		SQLiteDatabase sda = sqlHelper.GetSQLiteDatabase();	
		try {
			sda.beginTransaction();	
			for(SendToAddress stAddress : sendToAddresseList)
			{
				ContentValues cValues=new ContentValues();
				//cValues.put("l_id", l.getL_id());
				cValues.put("code", stAddress.getCode());
				cValues.put("name", stAddress.getName());
				cValues.put("language", stAddress.getLanguage());
				cValues.put("[order]", stAddress.getOrder());			//order�ǹؼ��֣����������
				cValues.put("type", "T");						//TODO ��֪���ã�����д��
				cValues.put("pinyin", ChineseToPinyinHelper.getPinYin(stAddress.getName()));		//����ƴ��������Ǻ���,�򲻱�
				cValues.put("headchar", ChineseToPinyinHelper.getPinYinHeadChar(stAddress.getName()));
				
				sda.insert("SendToAddress", null, cValues);		
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
