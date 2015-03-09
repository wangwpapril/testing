package com.cuc.miti.phone.xmc.utils.uploadtask;

import java.io.Serializable;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.UploadTask;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

public class UploadTaskJob implements Serializable{

	/**  
	* @Fields serialVersionUID : TODO
	*/  
	
	private static final long serialVersionUID = 1L;
	private UploadAsyncTask mUploadAsyncTask;
	private UploadTaskJobListener mListener;
	private UploadTask mUploadTask;

	/**
	 * ���캯��
	 * @param uploadTask
	 */
	public UploadTaskJob(UploadTask uploadTask) {
		this.mUploadTask = uploadTask;
	}

	/**
	 * ��ʼ
	 */
	public void start() {
		mUploadAsyncTask = new UploadAsyncTask(this);
		mUploadAsyncTask.execute();
	}
	
	public AsyncTask.Status getAsyncStatus(){
		return mUploadAsyncTask.getStatus();
	}
	
	/**
	 * ��ȡ
	 * @return
	 */
	public UploadAsyncTask getUploadAsyncTask(){
		return mUploadAsyncTask;
	}

	public void pause() {
		// TODO UploadTask.pause()
	}

	public void cancel() {
		this.notifyUploadPaused();
	}
	
	public void continued() {
		// TODO UploadTask.cancel()
	}

	public void setListener(UploadTaskJobListener listener) {
		mListener = listener;
	}

	public void setMUploadTask(UploadTask uploadTask){
		this.mUploadTask = uploadTask;
	}
		
	public UploadTask getMUploadTask(){
		return this.mUploadTask;
	}

	/**
	 * ֪ͨListener�������ϴ���ʼ����ʼ�����Ϊ0
	 */
	public void notifyUploadStarted() {
		if (mListener != null)
			mListener.uploadStarted(this);
		this.mUploadTask.setProgress(this.getMUploadTask().getProgress());		
	}
	
	/**
	 * ֪ͨListener���������
	 */
	public void notifyUploadContinued() {
		if (mListener != null)
			mListener.uploadContinued(this);
		this.mUploadTask.setProgress(this.getMUploadTask().getProgress());		
	}

	/**
	 * ֪ͨListener�������ϴ�����(��Ϊ����״̬��ʧ�ܺͳɹ�)
	 * @param result
	 */
	public void notifyUploadEnded(Boolean result) {
		if (mListener != null)
			if (result) {
				mListener.uploadSuccess(this);
				this.mUploadTask.setProgress(100);
			}else {
				mListener.uploadFailed(this);
			}	
	}
	
	/**
	 * ֪ͨListener�������ϴ���ȡ��(�Ӵ�תΪ�ڱ�)
	 */
	public void notifyUploadCancelled(){
		if (mListener != null){
			mListener.uploadCancelled(this);
		}
	}
	
	/**
	 * ֪ͨListener�������ϴ�����ͣ
	 */
	public void notifyUploadPaused(){
		if (mListener != null){
			mListener.uploadPaused(this);
		}
	}
	
	/**
	 * ֪ͨListener�������ϴ����¿�ʼ
	 */
	public void notifyUploadRestart(){
		if (mListener != null){
			mListener.uploadRestart(this);
			this.mUploadTask.setProgress(0);
		}
	}
	
	/**
	 * ֪ͨListener�������ϴ���ȵı仯
	 */
	public void notifyUploadProgress(int progress){
		if(mListener !=null){
			mListener.uploadingProgress(progress);
			this.mUploadTask.setProgress(progress);
		}
	}
	
	public void notifyUpload(){
		
		if(mListener!=null){
			mListener.uploading(this);
		}
	}

	
}
