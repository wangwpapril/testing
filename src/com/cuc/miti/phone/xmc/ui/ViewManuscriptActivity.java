package com.cuc.miti.phone.xmc.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.adapter.EditManuscriptAccessoriesAdapter;
import com.cuc.miti.phone.xmc.domain.Accessories;
import com.cuc.miti.phone.xmc.domain.Manuscripts;
import com.cuc.miti.phone.xmc.domain.Enums.ManuscriptStatus;
import com.cuc.miti.phone.xmc.domain.Enums.OperationType;
import com.cuc.miti.phone.xmc.logic.AccessoriesService;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.MediaHelper;
import com.cuc.miti.phone.xmc.utils.TimeFormatHelper;
import com.cuc.miti.phone.xmc.R;

public class ViewManuscriptActivity extends ManuscriptDetails {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		this.initialize();
		
		IngleApplication.getInstance().addActivity(this);
	}

	/**
	 * ��ʼ������
	 */
	private void initialize() {
		super.initializeButtons();

		this.iniManuscript();


		
		//�������б༭����
		disableControls();
	}
	
	/**
	 * �󶨸��������б?Gridview��, ��Ҫ����adapter�пؼ��Ŀ���״̬�����Ǹ����еķ���
	 */
	@Override
	protected void bindAccessoriesList(List<Accessories> accessories) {
		adapter = new EditManuscriptAccessoriesAdapter(accessories, this, false);
		gridViewAccerioes_editM.setAdapter(adapter);

		// ��ʼ��gridview�����¼�
		gridViewAccerioes_editM
				.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						Accessories acc = adapter.getAccessories().get(position);
						startActivitys(acc,
								MediaHelper.checkFileType(acc.getUrl()),
								REQUEST_ACCESSORIES_EDIT, OperationType.View);
					}
				});
	}

	/**
	 * ��ʼ����Ҫ�󶨵��ڴ�������
	 */
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
			//�󶨸����б�
			this.bindAccessoriesList(accessories);

			if (manuscripts != null) {
				this.editTextTitle_editM.setText(manuscripts.getTitle().trim());
				this.editTextContent_editM.setText(manuscripts.getContents().trim());
				//Add by SQ  ��������ж�λ��Ϣ���༭�����λ��ť��ɫ
				if(manuscripts.getLocation()!=null && !manuscripts.getLocation().equals("0,0")&&!manuscripts.getLocation().equals("")){
					iBtnGetLocationAdd_editM.setBackgroundResource(R.drawable.got_location_960x540);
				}
				//Added By SongQing 2013.1.22 �����ؽ���ť
				if(manuscripts.getManuscriptsStatus().equals(ManuscriptStatus.Sent)){		//˵�����ѷ����
					this.btnReBuild_editM.setVisibility(View.VISIBLE);
					this.btnReBuild_editM.setOnClickListener(new OnClickListener() {	
						public void onClick(View v) {
							rebuildManuscript(manuscripts.getM_id());
						}
					});
				}
			}
		}
		
		//����ҳ������
		textViewPageTitle_editM.setText(this.getResources().getString(R.string.titleV_editM));
	}
	
	@Override
	protected void back(){
		setResult(RESULT_OK);
		finish();
	}
	
	@Override
	protected void enterManuscriptTemplate(){
		Intent intent = new Intent(this, EditTemplateActivity.class);

		Bundle mybundle = new Bundle();
		mybundle.putParcelable("manuTemplateInfo",
				this.manuscripts.getManuscriptTemplate());
		mybundle.putString("requestType", "View");
		intent.putExtras(mybundle);

		startActivityForResult(intent, REQUEST_MANUSCRIPTTEMPLATE_GET);
		// �����л����������ұ߽��룬����˳�
		overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
	}
	
	/**
	 * ������༭��صĿؼ�
	 */
	private void disableControls(){
		this.imageBtnAccessorie_editM.setVisibility(View.INVISIBLE);
		this.imageBtnSave_editM.setVisibility(View.INVISIBLE);
		this.imageBtnSend_editM.setVisibility(View.INVISIBLE);
		this.iBtnGetLocationAdd_editM.setVisibility(View.INVISIBLE);
		this.iatButton.setVisibility(View.INVISIBLE);
		this.editTextContent_editM.setKeyListener(null);
		this.btnManuTextCounter_editM.setClickable(false);
//		editTextContent_editM.setFocusable(false);
//		editTextContent_editM.setFocusableInTouchMode(false);
		this.editTextTitle_editM.setKeyListener(null);
//		editTextTitle_editM.setFocusable(false);
//		editTextTitle_editM.setFocusableInTouchMode(false);
	
		if(this.timer != null){
			this.timer.cancel();
			this.timer = null;
		}
	}
	
	/**
	 * �ؽ����
	 * 
	 * @param manuscriptID
	 * @return
	 */
	private void rebuildManuscript(String manuscriptID) {
		Manuscripts mu = service.getManuscripts(manuscriptID);

		mu.setM_id(UUID.randomUUID().toString());
		mu.setCreatetime(TimeFormatHelper.getFormatTime(new Date()));
		mu.getManuscriptTemplate().setCreatetime(TimeFormatHelper.getFormatTime(new Date()));
		mu.setManuscriptsStatus(ManuscriptStatus.Editing);

		List<Accessories> accs = rebuildManuscriptAccs(manuscriptID, mu.getM_id());

		boolean result = CopyManuscript(mu, accs);

		if (result == false) {
			rollback(mu);
		} else {
			String id = mu.getM_id();

			Intent intent = new Intent(this, EditManuscriptsActivity.class);
			intent.putExtra("id", id);
			startActivity(intent);
		}
	}
	
	private List<Accessories> rebuildManuscriptAccs(String manuscriptID, String newID) {
		AccessoriesService accService = new AccessoriesService(this);

		List<Accessories> accs = accService.getAccessoriesListByMID(manuscriptID);

		for (Accessories acc : accs) {
			acc.setM_id(newID);
			acc.setA_id(UUID.randomUUID().toString());
			try {
				String tempUrlString = MediaHelper.copy2TempStore(acc.getUrl());
				acc.setUrl(tempUrlString);
				acc.setOriginalName(MediaHelper.getFileName(tempUrlString));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return accs;
	}

	private boolean CopyManuscript(Manuscripts mu, List<Accessories> accs) {

		// �½����
		boolean result = false;
		try {
			result = service.addManuscripts(mu);
		} catch (Exception e) {
			Logger.e(e);
			result = false;
		}

		AccessoriesService accService = new AccessoriesService(this);

		// �½������������
		if (result) {
			boolean resultAcc = false;
			for (Accessories acc : accs) {
				try {
					// ���������������ʱ�ļ�Ŀ¼��
					MediaHelper.copy2TempStore(acc.getUrl());

					resultAcc = true;
				} catch (IOException e) {
					Logger.e(e);
					resultAcc = false;
				}
				// ���ɹ����½��������
				if (resultAcc)
					resultAcc = accService.addAccessories(acc);

				// ��ʧ�ܣ����
				if (!resultAcc) {
					result = false;
					break;
				}
			}
		}

		return result;
	}
	
	/**
	 * �ع�
	 * 
	 * @param mu
	 * @return
	 */
	private boolean rollback(Manuscripts mu) {

		boolean result = false;
		try {
			result = service.deleteById(mu.getM_id());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.e(e);
			result = false;
		}
		return result;
	}

}
