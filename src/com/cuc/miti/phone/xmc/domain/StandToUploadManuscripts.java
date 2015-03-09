package com.cuc.miti.phone.xmc.domain;

import com.cuc.miti.phone.xmc.utils.uploadtask.UploadTaskJob;

/**
 * ����ϴ���϶��󣬰���������ϴ������������
 * @author SongQing
 *
 */
public class StandToUploadManuscripts {
	
	private UploadTaskJob uploadTaskJob;
	private Manuscripts manuscripts;

	public  StandToUploadManuscripts(){
		this.setUploadTaskJob(null);
		this.setManuscripts(null);
	}

	public UploadTaskJob getUploadTaskJob() {
		return uploadTaskJob;
	}

	public void setUploadTaskJob(UploadTaskJob uploadTaskJob) {
		this.uploadTaskJob = uploadTaskJob;
	}

	public Manuscripts getManuscripts() {
		return manuscripts;
	}

	public void setManuscripts(Manuscripts manuscripts) {
		this.manuscripts = manuscripts;
	}

}
