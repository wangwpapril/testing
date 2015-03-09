package com.cuc.miti.phone.xmc.ui;

import java.io.File;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Accessories;
import com.cuc.miti.phone.xmc.domain.Enums.OperationType;
import com.cuc.miti.phone.xmc.logic.AccessoriesService;
import com.cuc.miti.phone.xmc.utils.MediaHelper;
import com.cuc.miti.phone.xmc.utils.TouchEffect;
import com.cuc.miti.phone.xmc.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ManageComplexAccessoryActivity extends BaseActivity implements
OnClickListener{

	private Accessories accessorie = null;
	private ImageButton imageBtnSubmit_MComplexA = null;
	private ImageButton imageBtnBack_MComplexA = null;
	private EditText editTextTitle_MComplexA = null;
	private EditText editTextContent_MComplexA = null;
	private OperationType operationType = OperationType.None;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// ȥ�������
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.manage_complex_accessory);

		Bundle myBundle = this.getIntent().getExtras();
		if (myBundle != null) {

			accessorie = (Accessories) myBundle.getParcelable("acc");

		}

		operationType = OperationType.valueOf(this.getIntent().getStringExtra(
				"operation"));

		Initialize();
		IngleApplication.getInstance().addActivity(this);
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageBtnSubmit_MComplexA:
			//���渽��
			saveAcc();

			// ���÷���ֵ
			Intent intent = new Intent(this, EditManuscriptsActivity.class);
			Bundle mybundle = new Bundle();
			mybundle.putParcelable("acc", accessorie);
			intent.putExtras(mybundle);

			setResult(RESULT_OK, intent);
			finish();
			break;
		case R.id.imageBtnBack_MComplexA:
			if (operationType == OperationType.Add) {
				MediaHelper.deleteFile(accessorie.getUrl());
			}
			setResult(RESULT_CANCELED);
			finish();
		default:
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// KeyEvent.KEYCODE_BACK��?�ز���.
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (operationType == OperationType.Add) {
				MediaHelper.deleteFile(accessorie.getUrl());
			}
			setResult(RESULT_OK);
			finish();
		}
		return true;
	}
	
	/**
	 * ���渽������
	 */
	private void saveAcc() {
		// ���ø�������ı���
		accessorie.setTitle(editTextTitle_MComplexA.getText().toString());
		// ���ø������������
		accessorie.setDesc(editTextContent_MComplexA.getText().toString());

		AccessoriesService service = new AccessoriesService(this);
		// ���浽��ݿ�
		if (operationType == OperationType.Add)
		{
			service.addAccessories(this.accessorie);
			
			Intent intent = new Intent(this, EditManuscriptsActivity.class);
			Bundle mybundle = new Bundle();
			mybundle.putParcelable("acc", accessorie);
			intent.putExtras(mybundle);
			
			setResult(RESULT_OK, intent);
			finish();
		}else if (operationType == OperationType.Update)
			{
				service.updateAccessories(this.accessorie);
			}
	}

	/**
	 * ��ʼ����ؿؼ�
	 */
	private void Initialize() {

		imageBtnSubmit_MComplexA = (ImageButton) findViewById(R.id.imageBtnSubmit_MComplexA);
		imageBtnSubmit_MComplexA.setOnClickListener(this);
		imageBtnSubmit_MComplexA.setOnTouchListener(TouchEffect.TouchDark);

		imageBtnBack_MComplexA = (ImageButton) findViewById(R.id.imageBtnSubmit_MComplexA);
		imageBtnBack_MComplexA.setOnClickListener(this);
		imageBtnBack_MComplexA.setOnTouchListener(TouchEffect.TouchDark);

		if (accessorie == null)
			return;

		// ���⸳ֵ
		editTextTitle_MComplexA = (EditText) findViewById(R.id.editTextTitle_MComplexA);
		String title = accessorie.getTitle();
		//2012-7-27Ĭ�ϰ�ͼƬ�����Ϊ��������
		String tempTitle=accessorie.getOriginalName().substring(0, accessorie.getOriginalName().lastIndexOf("."));
		if ("".equals(title))
		{
			editTextTitle_MComplexA.setText(tempTitle.toCharArray(), 0, tempTitle.length());
		}else{
			editTextTitle_MComplexA.setText(title.toCharArray(), 0, title.length());
		}
		

		// ������Ϣ��ֵ
		editTextContent_MComplexA = (EditText) findViewById(R.id.editTextContent_MComplexA);
		String des = accessorie.getDesc();
		if (des != null)
			editTextContent_MComplexA.setText(des.toCharArray(), 0, des.length());
		
		//2012-7-31��ӣ�ʹ�������ѡ����ͼƬ�󼴷���ԭ��ҳ��
		
		saveAcc();
		
		//����ǲ鿴״̬���������еĿؼ����ɱ༭
		if(operationType == OperationType.View)
			disableControls();
	}
	
	private void disableControls(){
		this.imageBtnSubmit_MComplexA.setVisibility(View.GONE);
		//this.editTextTitle_MComplexA.setEnabled(false);
		this.editTextTitle_MComplexA.setFocusable(false);
		this.editTextContent_MComplexA.setFocusable(false);
	}
}
