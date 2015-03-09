package com.cuc.miti.phone.xmc.dao;

import com.cuc.miti.phone.xmc.utils.Logger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * ��ݲ����������װ
 * 
 * @author SongQing
 * 
 */
public class SQLiteHelper extends SQLiteOpenHelper {

	public Context context;
	public int version;
	private static final String DB_NAME = "mc.db"; // ��ݿ��ļ���
//	 private static final String DB_NAME =
//	 "/mnt/sdcard/com.cuc.miti.phone.xmc/databases/mc.db";

	// private SQLiteHelper helper;
	private static SQLiteDatabase sda;
	private static SQLiteHelper instance;

	public static synchronized SQLiteHelper getHelper(Context context,
			int version) {
		if (instance == null) {
			instance = new SQLiteHelper(context, version);
		}
		return instance;
	}

	/**
	 * 
	 * @param context
	 *            �����Ļ���
	 * @param dbname
	 *            Ҫ��������ݿ����
	 * @param version
	 *            Ҫ��������ݿ�汾
	 */
	public SQLiteHelper(Context context, int version) {
		super(context, DB_NAME, null, version);
		this.context = context;
		this.version = version;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		// db.execSQL(TABLESQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// db.execSQL("DROP table if exists " + TABLENAME );
		// onCreate(db);
	}

	/**
	 * ����ݿ�ʱ������ݿ������ ��ȡд��ʧ��ʱ�ͻ�ȡ����
	 */
	public void open() {
		try {
			if (sda == null || !sda.isOpen()) {
				// helper = new SQLiteHelper(this.context, this.version);
				sda = this.getWritableDatabase();
			}
		} catch (Exception e) {
			sda = this.getReadableDatabase();
		}
	}

	/**
	 * �ر�д��ݿ����
	 */
	public void close() {
		// if(sda != null){
		// sda.close();
		// sda = null ;
		// }
	}

	public void beginTransaction() {
		open();
		sda.beginTransaction();
	}

	public void endTransaction() {
		open();
		sda.setTransactionSuccessful();
		sda.endTransaction();
	}

	public void insertTransaction(String sql, Object[] params) {
		try {
			sda.execSQL(sql, params);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param sql
	 *            ִ�в��������sql���
	 * @param params
	 *            ��������ֶη�װ��һ��Object[]��
	 * @return �ɹ�����1
	 */
	public int insert(String sql, Object[] params) {
		open();
		try {
			sda.execSQL(sql, params);

		} catch (SQLException e) {
			e.printStackTrace();
			return e.hashCode();
		} finally {
			close();
		}
		return 1;
	}

	/**
	 * Ҫ�󷵻���id��������
	 * 
	 * @param table
	 *            ����
	 * @param nullColumnHack
	 *            ��values����Ϊ�ջ�������û�����ݵ�ʱ�򣬻Ὣ�����Զ�����Ϊnull�ٲ���
	 * @param values
	 *            ContentValues��������һ��map.ͨ���ֵ�Ե���ʽ�洢ֵ��
	 * @return �����е�id
	 */
	public int insert(String table, String nullColumnHack, ContentValues values) {
		open();
		try {
			return (int) sda.insert(table, nullColumnHack, values);

		} catch (SQLException e) {
			e.printStackTrace();
			return e.hashCode();
		} finally {
			close();
		}
	}

	/**
	 * 
	 * @param sql
	 *            ִ�и��²�����sql���
	 * @param params
	 *            ��������ֶη�װ��һ��Object[]��
	 * @return �ɹ�����1
	 */
	public int update(String sql, Object[] params) {
		open();
		try {
			sda.execSQL(sql, params);
		} catch (SQLException e) {
			return e.hashCode();
		} finally {
			close();
		}
		return 1;
	}

	/**
	 * 
	 * @param sql
	 *            ִ�а�������ѯ������sql���
	 * @param selectionArgs��������ֶη�װ��һ��String
	 *            []��
	 * @return ����һ���α����
	 */
	public Cursor findQuery(String sql, String[] selectionArgs) {
		open();
		Cursor cursor = sda.rawQuery(sql, selectionArgs);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		close();
		return cursor;
	}

	/**
	 * 
	 * @param sql
	 *            ִ�в�ѯ������sql���
	 * @return ����һ���α����
	 */
	public Cursor findQuery(String sql) {
		open();
		Cursor cursor = sda.rawQuery(sql, null);

		if (cursor != null) {
			cursor.moveToFirst();
		}
		close();
		return cursor;
	}

	/**
	 * 
	 * @param sql
	 *            ִ��ɾ�������sql���
	 */
	public void deleteAll(String sql) {
		open();
		sda.execSQL(sql);
		close();
	}

	/**
	 * 
	 * @param sql
	 *            ִ�а�����ɾ�������sql���
	 * @param id
	 *            ����Ҫɾ���id
	 * @return �ɹ�����1
	 */
	public int deleteOne(String sql, int id) {
		open();
		Object[] params = { id };
		try {
			sda.execSQL(sql, params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return e.hashCode();
		} finally {
			close();
		}
		return 1;
	}

	/**
	 * 
	 * @param sql
	 *            ִ�а�����ɾ�������sql���
	 * @param params
	 *            ����Ҫɾ�������
	 * @return �ɹ�����1
	 */
	public int delete(String sql, Object[] params) {
		open();
		try {
			sda.execSQL(sql, params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return e.hashCode();
		} finally {
			close();
		}
		return 1;
	}

	/**
	 * 
	 * @param sql
	 *            ��ѯ���sql
	 * @return ���ز�ѯ��¼����
	 */
	public int rowCount(String sql) {
		open();
		try {
			Cursor cursor = findQuery(sql);
			int returnvalue = cursor.getCount();
			cursor.close();
			return returnvalue;
		} catch (SQLException e) {
			Logger.e(e);
			return -1;
		} finally {
			close();
		}
	}

	public SQLiteDatabase GetSQLiteDatabase() {

		open();
		return sda;
	}
}
