package com.cuc.miti.phone.xmc.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Accessories;
import com.cuc.miti.phone.xmc.domain.Enums.AccessoryType;
import com.cuc.miti.phone.xmc.domain.Enums.LoginStatus;
import com.cuc.miti.phone.xmc.domain.Enums.OperationType;
import com.cuc.miti.phone.xmc.domain.Enums.XmcBool;
import com.cuc.miti.phone.xmc.domain.ManuscriptTemplate;
import com.cuc.miti.phone.xmc.domain.Manuscripts;
import com.cuc.miti.phone.xmc.domain.User;
import com.cuc.miti.phone.xmc.logic.ManuscriptTemplateService;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.MediaHelper;
import com.cuc.miti.phone.xmc.utils.SendManuscriptsHelper;
import com.cuc.miti.phone.xmc.utils.ToastHelper;
import com.cuc.miti.phone.xmc.R;

public class AddManuscriptsActivity extends ManuscriptDetails {

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

		Intent intent = this.getIntent();
		String s = intent.getStringExtra("URI_IMG");
		if (s != null) {
			Uri uri = Uri.parse(s);
			super.addAcc(uri);
		} else {
			ArrayList<Uri> urilist = intent
					.getParcelableArrayListExtra("LIST_URI_IMG");

			if (urilist != null) {
				for (Uri uri : urilist) {
					super.addAcc(uri);
				}
			}
		}

		super.bindAccessoriesList(null);

		super.add();

	}

	/**
	 * ��ʼ���ڴ��еĸ������
	 */
	private void iniManuscript() {
		manuscripts = new Manuscripts();

		// ������ĸ��guid
		manuscripts.setM_id(UUID.randomUUID().toString());

		id = manuscripts.getM_id();

		// ��ȡ��ǰ�û���Ϣ
		User user = IngleApplication.getInstance().currentUserInfo;

		manuscripts.setGroupcode(user.getUserattribute().getGroupCode());
		manuscripts.setGroupnameC(user.getUserattribute().getGroupNameC());
		manuscripts.setGroupnameE(user.getUserattribute().getGroupNameE());
		manuscripts.setUsernameC(user.getUserattribute().getUserNameC());
		manuscripts.setUsernameE(user.getUserattribute().getUserNameE());
		manuscripts.setLoginname(user.getUsername());

		// ���ø�ǩģ��
		ManuscriptTemplateService mtService = new ManuscriptTemplateService(
				this);
		ManuscriptTemplate template = null;
		// ����û��Ĭ�ϵĸ�ǩģ�壬���ʼ��һ���µ�
		if (IngleApplication.getMtTemplate() != null) {
			template = IngleApplication.getMtTemplate();
		} else {
			template = mtService.getDefaultManuscriptTemplate(
					IngleApplication.getInstance()
							.getCurrentUser(), XmcBool.True.getValue());
		}
		if (template == null)
			template = new ManuscriptTemplate();

		manuscripts.setManuscriptTemplate(template);
		manuscripts.setAuthor(template.getAuthor());

		manuscripts.getManuscriptTemplate().setComefromDept(
				user.getUserattribute().getGroupNameC());
		manuscripts.getManuscriptTemplate().setComefromDeptID(
				user.getUserattribute().getGroupCode());

		this.editTextTitle_editM.setText(template.getDefaulttitle());
		this.editTextContent_editM.setText(template.getDefaultcontents());

		// ����ҳ������
		textViewPageTitle_editM.setText(this.getResources().getString(
				R.string.titleA_editM));
	}

	/**
	 * ʵ�ָ����е��˳��߼���ɾ�����ø��
	 */
	@Override
	protected void back() {
		super.setManuscriptInformations();

		// У��������Ϣ�Ƿ���Ҫ����
		boolean result = SendManuscriptsHelper.validateForBack(
				this.manuscripts, adapter.getAccessories());

		// �ж�������½�������£�û�б��⡢���ݡ���������ɾ��˸��
		if (result == false && isSaved == false) {
			try {
				service.deleteById(this.manuscripts.getM_id());
				setResult(RESULT_OK);
				ToastHelper.showToast("", Toast.LENGTH_SHORT);
				finish();
			} catch (IOException e) {
				Logger.e(e);
			}
			// �˳���Activity
			setResult(RESULT_OK);
			finish();
		} else {
			if (this.manuscripts.getTitle().equals("")) {
				this.manuscripts
						.setTitle(ToastHelper
								.getStringFromResources(R.string.value_manu_notitle_default));
				if (this.editTextTitle_editM != null) {
					this.editTextTitle_editM
							.setText(R.string.value_manu_notitle_default);
				}
			}
			if (this.save()) {
				setResult(RESULT_OK);
				finish();
			}
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
		} else if (keyCode == KeyEvent.KEYCODE_HOME) {
			setManuscriptInformations();
			saveManuscriptBeforeClose();
			// ģ��HOME��
			Intent intent = new Intent(Intent.ACTION_MAIN);

			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			intent.addCategory(Intent.CATEGORY_HOME);

			this.startActivity(intent);
			return true;
		}
		return false;
	}

	// ��׽HOME��
	@Override
	public void onAttachedToWindow() {
		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
		super.onAttachedToWindow();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
