package com.cuc.miti.phone.xmc.services;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Enums.UploadTaskStatus;
import com.cuc.miti.phone.xmc.logic.ManuscriptsService;
import com.cuc.miti.phone.xmc.logic.UploadTaskService;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.XmcNotification;
import com.cuc.miti.phone.xmc.utils.uploadtask.UploadTaskJob;
import com.cuc.miti.phone.xmc.utils.uploadtask.UploadTaskJobListener;

public class FileUploadService extends Service {

	public static final String ACTION_ADD_TO_UPLOAD = "add_to_upload";
	public static final String EXTRA_UPLOADJOB = "uploadJob";
	public static final int MSG_START_UPLOAD=3;
	public static final String SER_KEY="Object.Serializable";

	public static Handler mainHandler, uploadThreadHandler;
	XmcNotification notification;
	private UploadTaskService uploadTaskService;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		notification=new XmcNotification(this);
		uploadTaskService = new UploadTaskService(this);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);

		if (intent == null)
			return;

		String action = intent.getAction();
		if (ACTION_ADD_TO_UPLOAD.equals(action)) {			
			UploadTaskJob uploadTaskJob = (UploadTaskJob)intent.getSerializableExtra(SER_KEY);
			addToUploadQueue(uploadTaskJob);
		}
	}
	
	/**
	 * ����������������������(��Ϊ�ȴ����_QueuedUploads�ͷ��Ͷ���_Uploading)
	 * @param uploadTaskJob
	 */
	public void addToUploadQueue(UploadTaskJob uploadTaskJob){
		if(uploadTaskJob!=null){
			uploadTaskJob.setListener(mUploadTaskJobListener);
			getQueuedUploads().add(uploadTaskJob);		//����ȴ����
			
			uploadTaskService.judgeStart(uploadTaskJob);							//�ж��Ƿ��ܹ����뷢�Ͷ���
		}
	}
	
	/**
	 * ��������״̬����
	 */
	private UploadTaskJobListener mUploadTaskJobListener=new UploadTaskJobListener() {
		
		/**
		 * �ϴ���ʼ
		 */
		public void uploadStarted(UploadTaskJob job) {			
			notification.showNotification(XmcNotification.NOTIFICATION_UPLOADING);
		}
		
		/**
		 * �ϴ��ָ�
		 */
		public void uploadContinued(UploadTaskJob job) {
			UploadTaskService uploadTaskService = new UploadTaskService(FileUploadService.this);

			if( (job.getMUploadTask().getStatus().equals(UploadTaskStatus.FailedContinue)||
					job.getMUploadTask().getStatus().equals(UploadTaskStatus.Paused)||
					job.getMUploadTask().getStatus().equals(UploadTaskStatus.Sending)) 						//������������Ҫ���״̬
					&& !job.getMUploadTask().getMessage().equals("") 
					&& !job.getMUploadTask().getFileid().equals("")){													//���ұ�֤Message�ж��SessionID��Ϊ�պ�fileID��ֵ

			}else{																//������
				job.getMUploadTask().setFileid("");
				job.getMUploadTask().setLastblocknum(-1);
				job.getMUploadTask().setProgress(0);
				job.getMUploadTask().setRepeattimes(1);
				job.getMUploadTask().setUploadedsize(0);
			}
			
			job.getMUploadTask().setStatus(UploadTaskStatus.Waiting);
			
			uploadTaskService.update(job.getMUploadTask());
			
			//�ж������ϴ��ж����Ƿ������粻����������
			uploadTaskService.judgeStart(job);			
		}
		
		/**
		 * ����ϴ��ɹ�
		 */
		public void uploadSuccess(UploadTaskJob job) {
			getQueuedUploads().remove(job);
			getCompletedDownloads().add(job);
			getUploadings().remove(job);
			
			//���سɹ���������ݿ�Manuscript��������״̬Ϊ"�ѷ�"
			ManuscriptsService manuscriptsService = new ManuscriptsService(FileUploadService.this);
			manuscriptsService.Send(job.getMUploadTask().getManuscriptId());
			
			//��ȡ�ȴ�״̬(��Ҫ��״̬)�����񣬲���ʼ�ϴ�
			uploadTaskService.getUploadTaskForStart();
			
			notification.cancelNotification(XmcNotification.NOTIFICATION_UPLOADING);
			notification.showNotification(XmcNotification.NOTIFICATION_UPLOADED);
		}
		
		/**
		 * ����ϴ�ȡ��
		 */
		public void uploadCancelled(UploadTaskJob job) {
			
			try {
				//����ȡ������������������е������Ƴ��ͬʱҲҪ�ӵȴ���������Ƴ���ͣ���⣩
				if(!job.getMUploadTask().getStatus().equals(UploadTaskStatus.Paused)){	
					getQueuedUploads().remove(job);
					
					if(getUploadings().contains(job)){
						getUploadings().remove(job);	
					}
				}
			} catch (Exception e) {
				Logger.e(e);
			}
			
			notification.cancelNotification(XmcNotification.NOTIFICATION_UPLOADING);			
			
			//��ȡ�ȴ�״̬(��Ҫ��״̬)�����񣬲���ʼ�ϴ�
			uploadTaskService.getUploadTaskForStart();
		}

		/**
		 * ����ϴ���ͣ
		 */
		public void uploadPaused(UploadTaskJob job) {
			try {
				//���µȴ�������������е�����״̬Ϊ"��ͣ"
				ArrayList<UploadTaskJob> uploadTaskJobs = getQueuedUploads();
				getQueuedUploads().get(uploadTaskJobs.indexOf(job)).getMUploadTask().setStatus(UploadTaskStatus.Paused);
				
				//�Ƴ�����������������е�����
				if(getUploadings().contains(job)){
					getUploadings().remove(job);	
				}
			} catch (Exception e) {
				
			}
			
			//������ݿ�������е�����״̬
			UploadTaskService uploadTaskService = new UploadTaskService(FileUploadService.this);
			job.getMUploadTask().setStatus(UploadTaskStatus.Paused);
			uploadTaskService.update(job.getMUploadTask());
			
			//��ȡ�ȴ�״̬(��Ҫ��״̬)�����񣬲���ʼ�ϴ�
			uploadTaskService.getUploadTaskForStart();
			
			//notification
			notification.cancelNotification(XmcNotification.NOTIFICATION_BACK);			
		}
		
		public void uploadRestart(UploadTaskJob job) {
			
			//����ȫ�ֱ����Ĵ���������
			ArrayList<UploadTaskJob> uploadTaskJobs = getQueuedUploads();
			getQueuedUploads().get(uploadTaskJobs.indexOf(job)).getMUploadTask().setStatus(UploadTaskStatus.Waiting);
			getQueuedUploads().get(uploadTaskJobs.indexOf(job)).getMUploadTask().setFileid("");
			getQueuedUploads().get(uploadTaskJobs.indexOf(job)).getMUploadTask().setLastblocknum(-1);
			getQueuedUploads().get(uploadTaskJobs.indexOf(job)).getMUploadTask().setProgress(0);
			getQueuedUploads().get(uploadTaskJobs.indexOf(job)).getMUploadTask().setRepeattimes(1);
			getQueuedUploads().get(uploadTaskJobs.indexOf(job)).getMUploadTask().setUploadedsize(0);
			
			//������ݿ�������е�����״̬
			UploadTaskService uploadTaskService = new UploadTaskService(FileUploadService.this);
			job.getMUploadTask().setStatus(UploadTaskStatus.Waiting);
			job.getMUploadTask().setFileid("");
			job.getMUploadTask().setLastblocknum(-1);
			job.getMUploadTask().setProgress(0);
			job.getMUploadTask().setRepeattimes(1);
			job.getMUploadTask().setUploadedsize(0);
			uploadTaskService.update(job.getMUploadTask());
			
			//��ȡ�ȴ�״̬(��Ҫ��״̬)�����񣬲���ʼ�ϴ�
			uploadTaskService.getUploadTaskForStart();
				
		}

		/**
		 * ����ϴ�ʧ��
		 */
		public void uploadFailed(UploadTaskJob job) {
			
			getUploadings().remove(job);
			//��ȡ�ȴ�״̬(��Ҫ��״̬)�����񣬲���ʼ�ϴ�
			uploadTaskService.getUploadTaskForStart();
			
			notification.cancelNotification(XmcNotification.NOTIFICATION_UPLOADING);
			notification.showNotification(XmcNotification.NOTIFICATION_UPLOAD_FAILED);
		}

		public void uploading(UploadTaskJob job) {			
			int progress=job.getMUploadTask().getProgress();
		}

		public void uploadingProgress(int progress) {			
		}		
	};
	
	/**
	 * ��ȡ�����ϴ�����Ķ���
	 * @return
	 */
	public ArrayList<UploadTaskJob> getUploadings(){
		return IngleApplication.getInstance().getUploadings();
	}
	
	/**
	 * ��ȡ�����������(�����״̬)
	 * @return
	 */
	public ArrayList<UploadTaskJob> getQueuedUploads(){
		return IngleApplication.getInstance().getQueuedUploads();
	}
	
	/**
	 * ��ȡ��ɵ��ϴ��������
	 * @return
	 */
	public ArrayList<UploadTaskJob> getCompletedDownloads(){
		return IngleApplication.getInstance().getCompletedUploads();
	}
}
