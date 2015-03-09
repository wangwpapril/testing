package com.cuc.miti.phone.xmc.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cuc.miti.phone.xmc.domain.ComeFromAddress;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.Place;
import com.cuc.miti.phone.xmc.utils.ChineseToPinyinHelper;
import com.cuc.miti.phone.xmc.utils.Logger;

public class ComeFromAddressDao {
	private static SQLiteHelper sqlHelper;
	private static final int VERSION = 1;

	public ComeFromAddressDao(Context context) {
		sqlHelper = new SQLiteHelper(context, VERSION);
	}

	/**
	 * ���һ����¼
	 * 
	 * @param
	 * @return
	 */
	public boolean addComeFromAddress(ComeFromAddress comeFromAddress) {
		int count = 0;
		if (comeFromAddress != null) {
			String sql = "insert into ComeFromAddress (ca_id,code,name,[language],[desc],pinyin,headchar) values (?,?,?,?,?,?,?)";
			Object[] params = { comeFromAddress.getCa_id(),
					comeFromAddress.getCode(), comeFromAddress.getName(),
					comeFromAddress.getLanguage(), comeFromAddress.getDesc(),
					comeFromAddress.getPinyin(), comeFromAddress.getHeadchar() };
			count = sqlHelper.insert(sql, params);
		}
		return count == 1 ? true : false;
	}

	/**
	 * ɾ��һ�����
	 * 
	 * @param name
	 * @return
	 */
	public boolean deleteComeFromAddress(String name) {
		int count = 0;
		if (name != null) {
			String sql = "delete from ComeFromAddress where name = ?";
			String[] params = { name };
			count = sqlHelper.delete(sql, params);
		}
		return count == 1 ? true : false;
	}

	/**
	 * ɾ������
	 * 
	 * @return
	 */
	public boolean deleteAllComeFromAddress() {
		String sql = "delete from ComeFromAddress";
		sqlHelper.deleteAll(sql);
		return true;
	}

	/**
	 * ����
	 * 
	 * @param
	 * @return
	 */
	public boolean updateComeFromAddress(ComeFromAddress comeFromAddress) {
		int count = 0;
		if (comeFromAddress != null) {
			String sql = "update ComeFromAddress set ca_id=?, code =?,[language]=?,[desc]=?,pinyin=?,headchar=? where name =?";
			Object[] params = { comeFromAddress.getCa_id(),
					comeFromAddress.getCode(), comeFromAddress.getLanguage(),
					comeFromAddress.getDesc(), comeFromAddress.getPinyin(),
					comeFromAddress.getHeadchar(), comeFromAddress.getName() };
			count = sqlHelper.update(sql, params);
		}
		return count == 1 ? true : false;
	}

	/**
	 * ��ȡһ����¼
	 */
	public ComeFromAddress getComeFromAddress(String name) {
		ComeFromAddress comeFromAddress = new ComeFromAddress();
		if (name != null) {
			String sql = "select * from ComeFromAddress where name = ?";
			String[] params = { name };
			Cursor cursor = sqlHelper.findQuery(sql, params);
			if (cursor != null && cursor.getCount() > 0) {
				comeFromAddress.setCa_id(cursor.getString(cursor
						.getColumnIndex("ca_id")));
				comeFromAddress.setCode(cursor.getString(cursor
						.getColumnIndex("code")));
				comeFromAddress.setDesc(cursor.getString(cursor
						.getColumnIndex("desc")));
				comeFromAddress.setLanguage(cursor.getString(cursor
						.getColumnIndex("language")));
				comeFromAddress.setName(cursor.getString(cursor
						.getColumnIndex("name")));
				comeFromAddress.setPinyin(cursor.getString(cursor
						.getColumnIndex("pinyin")));
				comeFromAddress.setHeadchar(cursor.getString(cursor
						.getColumnIndex("headchar")));
			}
			cursor.close();
		}
		return comeFromAddress;
	}

	/**
	 * ��ȡ�б�
	 */
	public List<ComeFromAddress> getComeFromAddressList() {

		List<ComeFromAddress> comeFromAddressList = new ArrayList<ComeFromAddress>();
		String sql = "select * from ComeFromAddress";
		Cursor cursor = sqlHelper.findQuery(sql);
		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				ComeFromAddress comeFromAddress = new ComeFromAddress();
				comeFromAddress.setCa_id(cursor.getString(cursor
						.getColumnIndex("ca_id")));
				comeFromAddress.setCode(cursor.getString(cursor
						.getColumnIndex("code")));
				comeFromAddress.setDesc(cursor.getString(cursor
						.getColumnIndex("desc")));
				comeFromAddress.setLanguage(cursor.getString(cursor
						.getColumnIndex("language")));
				comeFromAddress.setName(cursor.getString(cursor
						.getColumnIndex("name")));
				comeFromAddress.setPinyin(cursor.getString(cursor
						.getColumnIndex("pinyin")));
				comeFromAddress.setHeadchar(cursor.getString(cursor
						.getColumnIndex("headchar")));

				comeFromAddressList.add(comeFromAddress);
			}
		}
		cursor.close();
		return comeFromAddressList;
	}

	/**
	 * ��ݴ�����������ͻ�ȡ�б���Ϣ
	 */
	public List<ComeFromAddress> getComeFromAddressList(String language) {

		List<ComeFromAddress> comeFromAddressList = new ArrayList<ComeFromAddress>();
		String sql = "select * from ComeFromAddress where language = ? order by ca_id";
		String[] params = { language };
		Cursor cursor = sqlHelper.findQuery(sql, params);
		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				ComeFromAddress comeFromAddress = new ComeFromAddress();
				comeFromAddress.setCa_id(cursor.getString(cursor
						.getColumnIndex("ca_id")));
				comeFromAddress.setCode(cursor.getString(cursor
						.getColumnIndex("code")));
				comeFromAddress.setDesc(cursor.getString(cursor
						.getColumnIndex("desc")));
				comeFromAddress.setLanguage(cursor.getString(cursor
						.getColumnIndex("language")));
				comeFromAddress.setName(cursor.getString(cursor
						.getColumnIndex("name")));
				comeFromAddress.setPinyin(cursor.getString(cursor
						.getColumnIndex("pinyin")));
				comeFromAddress.setHeadchar(cursor.getString(cursor
						.getColumnIndex("headchar")));

				comeFromAddressList.add(comeFromAddress);
			}
		}
		cursor.close();
		return comeFromAddressList;
	}

	/**
	 * ��ݴ�����������ͻ�ȡ�б���Ϣ
	 */
	public List<ComeFromAddress> getComeFromAddressList(String language,
			String queryString) {
		List<ComeFromAddress> comeFromAddressList = new ArrayList<ComeFromAddress>();
		String sql = "";
		if (queryString.equals("")) {
			sql = "select * from ComeFromAddress where language='" + language
					+ "'" + " order by ca_id";
		} else {
			sql = "select * from ComeFromAddress where language='" + language
					+ "'" + " and (name like '" + queryString
					+ "%' or pinyin like '" + queryString
					+ "%' or headchar like '" + queryString
					+ "%')  order by ca_id";
		}

		Cursor cursor = sqlHelper.findQuery(sql);

		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				ComeFromAddress comeFromAddress = new ComeFromAddress();
				comeFromAddress.setCa_id(cursor.getString(cursor
						.getColumnIndex("ca_id")));
				comeFromAddress.setCode(cursor.getString(cursor
						.getColumnIndex("code")));
				comeFromAddress.setDesc(cursor.getString(cursor
						.getColumnIndex("desc")));
				comeFromAddress.setLanguage(cursor.getString(cursor
						.getColumnIndex("language")));
				comeFromAddress.setName(cursor.getString(cursor
						.getColumnIndex("name")));
				comeFromAddress.setPinyin(cursor.getString(cursor
						.getColumnIndex("pinyin")));
				comeFromAddress.setHeadchar(cursor.getString(cursor
						.getColumnIndex("headchar")));

				comeFromAddressList.add(comeFromAddress);
			}
		}
		cursor.close();
		return comeFromAddressList;
	}

	/**
	 * ��ȡָ��ҳ��ķ�ҳ���,���ص�ǰҳ��������� SQL : Select * From TABLE_NAME Limit 0,10;
	 * ��ʾ��TABLE_NAME���ȡ��ݣ��ӵ�0����ݿ�ʼ��ȥ10��
	 * 
	 * @param pager
	 *            ��ҳ����(CurrentPage-��ǰҳ��PageSize-ÿҳ����totalNum-������<����ֵ>)
	 * @return
	 */
	public List<ComeFromAddress> getComeFromAddressByPage(Pager pager) {

		List<ComeFromAddress> comeFromAddressList = new ArrayList<ComeFromAddress>();
		if (pager != null) {
			String sql = "select * from ComeFromAddress limit ?,?";
			int firstResult = (pager.getCurrentPage() - 1)
					* pager.getPageSize();
			int maxResult = pager.getPageSize();
			String[] params = { String.valueOf(firstResult),
					String.valueOf(maxResult) };
			Cursor cursor = sqlHelper.findQuery(sql, params);
			if (cursor != null && cursor.getCount() > 0) {
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					ComeFromAddress comeFromAddress = new ComeFromAddress();
					comeFromAddress.setCa_id(cursor.getString(cursor
							.getColumnIndex("ca_id")));
					comeFromAddress.setCode(cursor.getString(cursor
							.getColumnIndex("code")));
					comeFromAddress.setDesc(cursor.getString(cursor
							.getColumnIndex("desc")));
					comeFromAddress.setLanguage(cursor.getString(cursor
							.getColumnIndex("language")));
					comeFromAddress.setName(cursor.getString(cursor
							.getColumnIndex("name")));
					comeFromAddress.setPinyin(cursor.getString(cursor
							.getColumnIndex("pinyin")));
					comeFromAddress.setHeadchar(cursor.getString(cursor
							.getColumnIndex("headchar")));

					comeFromAddressList.add(comeFromAddress);
				}
			}
			cursor.close();
		}
		return comeFromAddressList;
	}

	/**
	 * ��ȡ������
	 */
	public long getComeFromAddressCount() {
		String sql = "select count(*) from ComeFromAddress ";
		Cursor cursor = sqlHelper.findQuery(sql);	
		
		cursor.moveToFirst();
		long returnvalue=cursor.getLong(0);
		cursor.close();
		return returnvalue;
	}

	public boolean multiInsert(List<ComeFromAddress> comeFromAddressList) {

		if (comeFromAddressList != null && comeFromAddressList.size() > 0) {
			SQLiteDatabase sda = sqlHelper.GetSQLiteDatabase();
			try {
				sda.beginTransaction();
				for (ComeFromAddress cfa : comeFromAddressList) {
					ContentValues cValues = new ContentValues();
					cValues.put("ca_id", cfa.getCa_id());
					cValues.put("code", cfa.getCode());
					cValues.put("name", cfa.getName());
					cValues.put("language", cfa.getLanguage());
					cValues.put("desc", cfa.getDesc());
					cValues.put("pinyin", ChineseToPinyinHelper.getPinYin(cfa
							.getName()));
					cValues.put("headchar", ChineseToPinyinHelper
							.getPinYinHeadChar(cfa.getName()));

					sda.insert("ComeFromAddress", null, cValues);

				}
				sda.setTransactionSuccessful();

			} catch (Exception ex) {
				Logger.e(ex);
			} finally {
				sda.endTransaction();
				if (sda != null) {
					sda.close();
				}
			}
			return true;
		} else {
			return false;
		}

	}

}
