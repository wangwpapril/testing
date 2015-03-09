/**
 * �ࣺ�½�/�༭ ���Activity
 * 
 * ˵�����½�������򿪴�Activityʱ����������ݿ����½�һ����¼��
 * 		ͬʱ�����������Ϣ������ʱ������ʱ��������Ϣ��
 * 		�˳��½�/�༭���ʱ����ʾ�������ٱ���һ�Σ�������ޱ��⡢�����ݡ��޸����������ݿ��и����¼ɾ��
 * 		��ӻ�༭������Ҳ����ݿ����¼���ؼ�¼��
 * 
 * ���ߣ�wenyujun
 * 
 * ʱ�䣺20120601
 */
package com.cuc.miti.phone.xmc.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Accessories;
import com.cuc.miti.phone.xmc.logic.AccessoriesService;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.MediaHelper;
import com.cuc.miti.phone.xmc.utils.ToastHelper;
import com.cuc.miti.phone.xmc.R;

public class EditManuscriptsActivity extends ManuscriptDetails {
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		this.initialize();
		IngleApplication.getInstance().addActivity(this);
	}
	
	@Override 
    public void onConfigurationChanged(Configuration config) { 
		super.onConfigurationChanged(config); 
    } 

	private void initialize() {

		super.initializeButtons();

		this.iniManuscript();


	}

	private void iniManuscript() {

		id = this.getIntent().getStringExtra("id");

		if (!id.equals("")) {
			manuscripts = this.service.getManuscripts(id);

			// ����ݿ���ȡ���ø��ID�����ĸ�����Ϣ
			AccessoriesService a_service = new AccessoriesService(this);
			List<Accessories> accessories=null;

			try {
				accessories = a_service.getAccessoriesListByMID(id);
			} catch (Exception e) {
				Logger.e(e);
			}

			if (accessories == null)
				accessories = new ArrayList<Accessories>();
			else {
				// �������ͼ
				for (Accessories acc : accessories) {
					acc.setImage(MediaHelper.createItemImage(acc.getUrl(),
							this, 100, 70));
				}
			}
			super.bindAccessoriesList(accessories);

			if (manuscripts != null) {
				if(!manuscripts.getTitle().equals("")&&!manuscripts.getTitle().equals(ToastHelper.getStringFromResources(R.string.value_manu_notitle_default))){
					this.editTextTitle_editM.setText(manuscripts.getTitle());
				}
				this.editTextContent_editM.setText(manuscripts.getContents());
				//Add by SQ  ��������ж�λ��Ϣ���༭�����λ��ť��ɫ
				if(manuscripts.getLocation()!=null && !manuscripts.getLocation().equals("0,0")&&!manuscripts.getLocation().equals("")){
					iBtnGetLocationAdd_editM.setBackgroundResource(R.drawable.got_location_960x540);
				}
			}
		}

		// ����ҳ������
		textViewPageTitle_editM.setText(this.getResources().getString(R.string.titleE_editM));

	}

	@Override
	protected void back() {
		super.setManuscriptInformations();

		if(this.manuscripts.getTitle().equals("")){
			this.manuscripts.setTitle(ToastHelper.getStringFromResources(R.string.value_manu_notitle_default));
			if(this.editTextTitle_editM != null){
				this.editTextTitle_editM.setText(R.string.value_manu_notitle_default);
			}
		}
		if(this.save()){
			setResult(RESULT_OK);
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// KeyEvent.KEYCODE_BACK��?�ز���.
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back();
		}
		return true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
