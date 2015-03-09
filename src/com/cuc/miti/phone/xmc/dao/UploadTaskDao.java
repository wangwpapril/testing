package com.cuc.miti.phone.xmc.dao;

import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.UploadTask;
import com.cuc.miti.phone.xmc.domain.Enums.UploadTaskStatus;
import com.cuc.miti.phone.xmc.utils.Logger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * ���������ݿ������
 * @author SongQing
 *
 */
public class UploadTaskDao {

	private static SQLiteHelper sqlHelper;
	private static final int VERSION = 1;
	private final static byte[] _writeLock = new byte[0];
	 
	public UploadTaskDao(Context context) {
		sqlHelper = new SQLiteHelper(context, VERSION);
	}
	
	/**
	 * ���һ���ϴ��������
	 * @param uploadTask
	 * @return
	 */
	public int add(UploadTask uploadTask){		
		int id = 0 ;
		if(uploadTask != null){			
			ContentValues cValues = new ContentValues();
			cValues.put("createtime", uploadTask.getCreatetime());
			cValues.put("finishtime", uploadTask.getFinishtime());
			cValues.put("m_id", uploadTask.getManuscriptId());
			cValues.put("status", uploadTask.getStatus().toString());
			cValues.put("lastblocknum", uploadTask.getLastblocknum());
			cValues.put("progress", uploadTask.getProgress());
			cValues.put("message", uploadTask.getMessage());
			cValues.put("priority", uploadTask.getPriority());
			cValues.put("loginname", uploadTask.getLoginname());
			cValues.put("repeattimes", uploadTask.getRepeattimes());
			cValues.put("totalsize", uploadTask.getTotalsize());
			cValues.put("blocksize", uploadTask.getBlocksize());
			cValues.put("uploadedsize", uploadTask.getUploadedsize());
			cValues.put("fileurl", uploadTask.getFileurl());
			cValues.put("xmlurl", uploadTask.getXmlurl());
			cValues.put("fileid", uploadTask.getFileid());
		
			id = sqlHelper.insert("UploadTask","finishtime,message,priority,fileurl,fileid", cValues);
		}
			return id;
	}
	
	/**
	 * �����ϴ��������
	 * @param uploadTask
	 * @return
	 */
	public boolean update(UploadTask uploadTask){
		synchronized(_writeLock){

			int count = 0 ;
			if(uploadTask != null){
				StringBuilder sbBuilder = new StringBuilder();
				String sql = sbBuilder.append("update UploadTask set").
							   append(" createtime=?,finishtime=?,m_id=?,status=?,lastblocknum=?,progress=?,message=?,priority=?,").
							   append("loginname=?,totalsize=?,repeattimes=?,blocksize=?,uploadedsize=?,fileurl=?,xmlurl=?,fileid=?").
							   append(" where ut_id = ?").toString();
				Object[] params = {uploadTask.getCreatetime(),
											uploadTask.getFinishtime(),
											uploadTask.getManuscriptId(),
											uploadTask.getStatus(),
											uploadTask.getLastblocknum(),
											uploadTask.getProgress(),
											uploadTask.getMessage(),
											uploadTask.getPriority(),
											uploadTask.getLoginname(),
											uploadTask.getTotalsize(),
											uploadTask.getRepeattimes(),
											uploadTask.getBlocksize(),
											uploadTask.getUploadedsize(),
											uploadTask.getFileurl(),
											uploadTask.getXmlurl(),
											uploadTask.getFileid(),
											uploadTask.getId()};
				count = sqlHelper.update(sql, params);
			}
				return count==1?true:false;
		}
	}
	
	/**
	 * �����ϴ�����״̬
	 * @param status
	 * @return
	 */
	public boolean setStatus(int id,UploadTaskStatus status){
		int count = 0 ;
		if(id >0 && status!=null){
			StringBuilder sbBuilder = new StringBuilder();
			String sql = sbBuilder.append("update UploadTask set").
						   append(" status=?").
						   append(" where ut_id = ?").toString();
			Object[] params = {status,id};
			count = sqlHelper.update(sql, params);
		}
			return count==1?true:false;
	}
	
	/**
	 * ��ȡһ���ϴ�����
	 * @param id
	 * @return
	 */
	public UploadTask get(int id){
		UploadTask uploadTask = null;

		String sql = "select * from UploadTask where ut_id = " + String.valueOf(id);
		
		Cursor cursor = sqlHelper.findQuery(sql);
		if (cursor != null && cursor.getCount() > 0) {
			uploadTask = new UploadTask();
			commonMethod(cursor, uploadTask);
			}
		cursor.close();
		return uploadTask;
	}
	
	/**
	 * ɾ��ĳһ���ϴ�����
	 * @param id
	 * @return
	 */
	public boolean delete(int id){
		int count = 0;
		String sql = "delete from UploadTask where ut_id=?";
		count = sqlHelper.deleteOne(sql, id);
		
		return count==1?true:false;		
	}
	
	/**
	 * ��ȡ�ϴ������б�(ĳһ�û�������״̬�б�)
	 * ���������û�����Զ�����
	 * @return
	 */
	public List<UploadTask> getListByPage(Pager pager,String loginname){
		List<UploadTask> uploadTasksList = null;
		if (pager != null && !loginname.equals("")) {
			int firstResult = ((pager.getCurrentPage() - 1) * pager
					.getPageSize()); // �ӵڼ�����ݿ�ʼ��ѯ
			int maxResult = pager.getPageSize();

			// ��ѯ����
			String sql1 = "select * from UploadTask where loginname = '" + loginname + "' order by createtime desc";
			// ��ҳ
			String sql2 = sql1 + " limit " + String.valueOf(firstResult) + ","
					+ String.valueOf(maxResult);
			Cursor cursor = sqlHelper.findQuery(sql2);

			if (cursor != null && cursor.getCount() > 0) {
				uploadTasksList = new ArrayList<UploadTask>();
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
					UploadTask uploadTask = new UploadTask();
					commonMethod(cursor, uploadTask);
				}
				pager.setTotalNum(sqlHelper.rowCount(sql1));
			}else{
				pager.setCurrentPage(1);
				pager.setTotalNum(0);
			}
			cursor.close();
		}	
		return uploadTasksList;
	}
	
	/**
	 * ��ȡ����ĳһ״̬���ϴ������б�
	 * @return
	 */
	public List<UploadTask> getListWithStatuByPage(Pager pager,UploadTaskStatus status,String loginname){
		List<UploadTask> uploadTasksList = null;
		if (pager != null && !loginname.equals("") && status!=null) {
			int firstResult = ((pager.getCurrentPage() - 1) * pager
					.getPageSize()); // �ӵڼ�����ݿ�ʼ��ѯ
			int maxResult = pager.getPageSize();

			// ��ѯ����
			String sql1 = "select * from UploadTask where loginname = '" + loginname +"' and status= '" + status.toString() + "' order by createtime desc";
			// ��ҳ
			String sql2 = sql1 + " limit " + String.valueOf(firstResult) + ","
					+ String.valueOf(maxResult);
			Cursor cursor = sqlHelper.findQuery(sql2);

			if (cursor != null && cursor.getCount() > 0) {
				uploadTasksList = new ArrayList<UploadTask>();
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
					UploadTask uploadTask = new UploadTask();
					commonMethod(cursor, uploadTask);
				}
				pager.setTotalNum(sqlHelper.rowCount(sql1));
			}else{
				pager.setCurrentPage(1);
				pager.setTotalNum(0);
			}
			cursor.close();
		}	
		return uploadTasksList;
	}
	
	/**
	 * ��ȡ��ĳһ״̬֮����ϴ������б�
	 * @param statusList
	 * @return
	 */
	public List<UploadTask> getListExceptStatusByPage(Pager pager,List<UploadTaskStatus> statusList,String loginname){
		List<UploadTask> uploadTasksList = null;

		if(loginname ==null){
			loginname = IngleApplication.getInstance().getCurrentUser();
		}
		if (pager != null && !loginname.equals("")) {
			String statusSqlString = "";
			if(statusList != null && statusList.size()>0){
				for(UploadTaskStatus uStatus : statusList){
					statusSqlString +=  " and status <>" + "'" + uStatus.toString() + "'" ; 
				}
			}
			int firstResult = ((pager.getCurrentPage() - 1) * pager
					.getPageSize()); // �ӵڼ�����ݿ�ʼ��ѯ
			int maxResult = pager.getPageSize();

			// ��ѯ����
			String sql1 = "select * from UploadTask where loginname = '" + loginname +"' "+ statusSqlString +" order by createtime desc";
			// ��ҳ
			String sql2 = sql1 + " limit " + String.valueOf(firstResult) + ","
					+ String.valueOf(maxResult);
			Cursor cursor = sqlHelper.findQuery(sql2);
			if (cursor != null && cursor.getCount() > 0) {
				uploadTasksList = new ArrayList<UploadTask>();
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
					UploadTask uploadTask = new UploadTask();
					commonMethod(cursor, uploadTask);
					uploadTasksList.add(uploadTask);
				}
				pager.setTotalNum(sqlHelper.rowCount(sql1));
			}else{
				pager.setCurrentPage(1);
				pager.setTotalNum(0);
			}
			cursor.close();
		}	
		return uploadTasksList;
	}

	/**
	 * �������������ڸ��cursor��ɶ�Ӧ��ʵ�������
	 * @param cursor
	 * @param uploadTask
	 */
	private void commonMethod(Cursor cursor,UploadTask uploadTask){
		uploadTask.setCreatetime(cursor.getString(cursor.getColumnIndex("createtime")));
		uploadTask.setFinishtime(cursor.getString(cursor.getColumnIndex("finishtime")));
		uploadTask.setManuscriptId(cursor.getString(cursor.getColumnIndex("m_id")));
		uploadTask.setStatus(UploadTaskStatus.valueOf(cursor.getString(cursor.getColumnIndex("status"))));
		uploadTask.setLastblocknum(cursor.getInt(cursor.getColumnIndex("lastblocknum")));
		uploadTask.setProgress(cursor.getInt(cursor.getColumnIndex("progress")));
		uploadTask.setMessage(cursor.getString(cursor.getColumnIndex("message")));
		uploadTask.setPriority(cursor.getString(cursor.getColumnIndex("priority")));
		uploadTask.setLoginname(cursor.getString(cursor.getColumnIndex("loginname")));
		uploadTask.setTotalsize(cursor.getInt(cursor.getColumnIndex("totalsize")));
		uploadTask.setRepeattimes(cursor.getInt(cursor.getColumnIndex("repeattimes")));
		uploadTask.setBlocksize(cursor.getInt(cursor.getColumnIndex("blocksize")));
		uploadTask.setUploadedsize(cursor.getInt(cursor.getColumnIndex("uploadedsize")));
		uploadTask.setFileurl(cursor.getString(cursor.getColumnIndex("fileurl")));
		uploadTask.setXmlurl(cursor.getString(cursor.getColumnIndex("xmlurl")));
		uploadTask.setFileid(cursor.getString(cursor.getColumnIndex("fileid")));
		uploadTask.setId(cursor.getInt(cursor.getColumnIndex("ut_id")));
	} 
}
