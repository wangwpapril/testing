package com.cuc.miti.phone.xmc.utils.uploadtask;

public interface UploadTaskJobListener {

	/**
	 * Callback invoked when an upload finishes ,success
	 * @param job
	 */
	public void uploadSuccess(UploadTaskJob job);
	
	/**
	 * Callback invoked when an upload finishes ,failed
	 * @param job
	 */
	public void uploadFailed(UploadTaskJob job);
	
	/**
	 * Callback invoked when an upload starts
	 */
	public void uploadStarted(UploadTaskJob job);
	
	/**
	 * Callback invoked when an upload cancelled
	 * @param job
	 */
	public void uploadCancelled(UploadTaskJob job);
	
	/**
	 * Callback invoked when an upload paused
	 * @param job
	 */
	public void uploadPaused(UploadTaskJob job);
	
	/**
	 * Callback invoked when an upload restart
	 * @param job
	 */
	public void uploadRestart(UploadTaskJob job);
	
	/**
	 * Callback invoked when an upload continue
	 * @param job
	 */
	public void uploadContinued(UploadTaskJob job);
	
	public void uploading(UploadTaskJob job);
	
	public void uploadingProgress(int progress);
}
