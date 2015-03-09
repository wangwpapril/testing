package com.cuc.miti.phone.xmc.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.content.Context;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.dao.UploadTaskDao;
import com.cuc.miti.phone.xmc.domain.Enums.NetStatus;
import com.cuc.miti.phone.xmc.domain.Enums.UploadTaskStatus;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.StandToUploadManuscripts;
import com.cuc.miti.phone.xmc.domain.UploadTask;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.TimeFormatHelper;
import com.cuc.miti.phone.xmc.utils.uploadtask.FileUploadTaskHelper;
import com.cuc.miti.phone.xmc.utils.uploadtask.UploadTaskJob;

/**
 * ����ϴ�ҵ���߼���
 * @author SongQing
 *
 */
public class UploadTaskService {

	private String currentUser;
	private UploadTaskDao uploadTaskDao;
	
	/**
	 * ���캯����UI��context
	 * @param context
	 */
	public UploadTaskService(Context context) {
		this.uploadTaskDao = new UploadTaskDao(context);
		this.currentUser = IngleApplication.getInstance().currentUserInfo.getUsername();
	}
	public UploadTaskService(Context context,String username) {
		this.uploadTaskDao = new UploadTaskDao(context);
		this.currentUser = username;
	}
	
	/**
	 * ���һ���ϴ��������
	 * @param uploadTask
	 * @return
	 */
	public int add(UploadTask uploadTask){		
		return uploadTaskDao.add(uploadTask);
	}

	/**
	 * ���һ���ϴ��������
	 * @param urls    String[]  0_�����ļ���url   1_���xml��url
	 * @param manuscriptId
	 * @param priority
	 * @return
	 */
	public int add(String[] urls,String manuscriptId,String priority){
		UploadTask uploadTask = new UploadTask();
		
		uploadTask.setXmlurl(urls[1]);
		uploadTask.setCreatetime(TimeFormatHelper.getGMTTime(new Date()));
		uploadTask.setFileurl(urls[0]);
		uploadTask.setLoginname(this.currentUser);
		uploadTask.setManuscriptId(manuscriptId);
		uploadTask.setMessage("");
		uploadTask.setPriority(priority);
		uploadTask.setStatus(UploadTaskStatus.Waiting);
		return uploadTaskDao.add(uploadTask);
	}
	
	/**
	 * �����ϴ��������
	 * @param uploadTask
	 * @return
	 */
	public boolean update(UploadTask uploadTask){
		return uploadTaskDao.update(uploadTask);
	}
	
	/**
	 * �����ϴ�����״̬
	 * @param status
	 * @return
	 */
	public boolean setStatus(int id,UploadTaskStatus status){
		return uploadTaskDao.setStatus(id, status);
	}
	
	/**
	 * ��ȡһ���ϴ�����
	 * @param id
	 * @return
	 */
	public UploadTask get(int id){
		return uploadTaskDao.get(id);
	}
	
	/**
	 * ɾ��ĳһ���ϴ�����
	 * @param id
	 * @return
	 */
	public boolean delete(int id){
		return uploadTaskDao.delete(id);
	}
	
	/**
	 * ��ȡ�ϴ������б�(ĳһ�û�������״̬�б�)
	 * ���������û�����Զ�����
	 * @return
	 */
	public List<UploadTask> getListByPage(Pager pager){
		return uploadTaskDao.getListByPage(pager, this.currentUser);
	}
	
	/**
	 * ��ȡ����ĳһ״̬���ϴ������б�
	 * @return
	 */
	public List<UploadTask> getListWithStatusByPage(Pager pager,UploadTaskStatus status){
		return uploadTaskDao.getListWithStatuByPage(pager, status, this.currentUser);
	}
	
	/**
	 * ��ȡ����ĳ����״̬���ϴ������б�
	 * @param statusList
	 * @return
	 */
	public List<UploadTask> getListExceptStatusByPage(Pager pager,List<UploadTaskStatus> statusList){
		return uploadTaskDao.getListExceptStatusByPage(pager, statusList, this.currentUser);
	}
	
	/**
	 * ��ȡ����ҳ��ĳ�ʼ��ʾ
	 * (��״̬�������᷵�س�Cancel��Finished״̬֮�������״̬����)
	 * @param pager
	 * @return
	 */
	public List<UploadTask> getListForStandToManuscript(Pager pager){
		List<UploadTaskStatus> statusList = new ArrayList<UploadTaskStatus>();
		statusList.add(UploadTaskStatus.Cancel);
		statusList.add(UploadTaskStatus.Finished);
		return uploadTaskDao.getListExceptStatusByPage(pager, statusList, this.currentUser);
	}
	
	/**
	 * ��ȡ����ҳ��ĳ�ʼ��ʾ
	 * (��״̬�������᷵�س�Cancel��Finished״̬֮�������״̬����)
	 * @param pager
	 * @return
	 */
	public List<UploadTask> getListForStandToManuscript(){
		List<UploadTaskStatus> statusList = new ArrayList<UploadTaskStatus>();
		Pager pager = new Pager();
		pager.setCurrentPage(1);
		pager.setPageSize(999);
		statusList.add(UploadTaskStatus.Cancel);
		statusList.add(UploadTaskStatus.Finished);
		return uploadTaskDao.getListExceptStatusByPage(pager,statusList, this.currentUser);
	}
	
	
/******************************Update By SongQing ,For �ϵ�������仯�Զ���ʼ�ϴ���ͬʱ�ϴ��������Ϊ3��******************************/
	
	private static final int MAX_NUMBER_OF_UPLOADTASKS = 3;								//�������ͬʱ3������

	/**
	 * �ж��Ƿ���Լ��뵽�����������
	 */
	public void judgeStart(UploadTaskJob uploadTaskJob){
		
		//û������
		if(NetStatus.Disable.equals(IngleApplication.getNetStatus())){			
			return;
		}
		
		//û��sessionId�����������ߵ�¼
		String sessionIdString = IngleApplication.getSessionId();
		if(sessionIdString==null || sessionIdString.equals("")){					
			return;
		}

		ArrayList<UploadTaskJob> uploadTaskJobs = IngleApplication.getInstance().getUploadings();
		if(uploadTaskJobs == null){
			uploadTaskJobs = new ArrayList<UploadTaskJob>();
		}		
		
		//˵�������������������
		if(uploadTaskJobs.size() < MAX_NUMBER_OF_UPLOADTASKS){			
			IngleApplication.getInstance().getUploadings().add(uploadTaskJob);
			uploadTaskJob.start();
		}
	}
	
	/**
	 * �ӵȴ�����л�ȡ��Ҫ���ص�����
	 */
	public void getUploadTaskForStart(){
		ArrayList<UploadTaskJob> uploadTaskJobs = IngleApplication.getInstance().getQueuedUploads();
		if(uploadTaskJobs!=null && uploadTaskJobs.size()>0){
			for(UploadTaskJob uJob: uploadTaskJobs){
				if(uJob.getMUploadTask().getStatus().equals(UploadTaskStatus.Waiting)||uJob.getMUploadTask().getStatus().equals(UploadTaskStatus.FailedContinue)){
					judgeStart(uJob);
				}
			}
		}
	}
	
	/**
	 * ����ݿ���ж�ȡ���еȴ����ص����񣬲����뵽ȫ�ֵĵȴ����������
	 */
	public void loadUploadTaskIntoWaitingQueue(){
		List<UploadTask> uploadTaskDBListALL = null;
		List<UploadTaskJob> uploadTaskJobList = null;
		
		UploadTaskJob uploadTaskJob = null;			//�����������
		
		try{
			//��ȡ��ݿ��Cancel��Finished״̬֮������
			uploadTaskDBListALL = getListForStandToManuscript();
			uploadTaskJobList = IngleApplication.getInstance().getQueuedUploads();
		}catch(Exception e){
			Logger.e(e);
		}

		if(uploadTaskDBListALL !=null ){//�д���
			if(uploadTaskJobList == null || uploadTaskJobList.size()==0){			//�����û��˳�������ڴ���ķ��Ͷ����Ѿ�Ϊ����
				//������������������ض�����
				for(UploadTask tu:uploadTaskDBListALL){
					uploadTaskJob = new UploadTaskJob(tu);
					FileUploadTaskHelper.addToUploads(uploadTaskJob);
				}
			}
		}
	}

}
