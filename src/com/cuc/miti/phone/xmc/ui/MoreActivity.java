package com.cuc.miti.phone.xmc.ui;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.UserAttribute;
import com.cuc.miti.phone.xmc.domain.Enums.BaseDataType;
import com.cuc.miti.phone.xmc.utils.ToastHelper;
import com.cuc.miti.phone.xmc.utils.TouchEffect;
import com.cuc.miti.phone.xmc.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MoreActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout rLayoutoutEmployeeSendToAddressConfig; // �û������ַ�б�����
	private RelativeLayout rLayoutTemplateConfig; // �û���ǩ����
	private RelativeLayout rLayoutLocationConfig; // ��λ����
	private RelativeLayout rLayoutFTPConfig; // ���ļ�������
	private TextView textViewEmployeeSendToAddressConfig;
	private TextView textViewTemplateConfig;
	private TextView textViewLocationConfig;
	private TextView textViewFTPConfig;
	private ImageButton iBtnBack;
//	private UserAttribute userAttribute;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// �����ޱ���
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.more);

		this.initialize();

		IngleApplication.getInstance().addActivity(this);
	}

	/**
	 * ��ʼ��
	 */
	private void initialize() {
		this.setUpViews();
//		userAttribute = IngleApplication.getInstance().currentUserInfo
//				.getUserattribute();
	}

	private void setUpViews() {
		rLayoutoutEmployeeSendToAddressConfig = (RelativeLayout) findViewById(R.id.relativeLayoutEmplayeeSendToConfig_More);
		rLayoutTemplateConfig = (RelativeLayout) findViewById(R.id.relativeLayoutTemplateConfig_More);
		rLayoutLocationConfig = (RelativeLayout) findViewById(R.id.relativeLayoutLocation_More);
		rLayoutFTPConfig = (RelativeLayout) findViewById(R.id.relativeLayoutFTP_More);

		textViewEmployeeSendToAddressConfig = (TextView) findViewById(R.id.textViewEmployeeSendToConfig_More);
		textViewEmployeeSendToAddressConfig.setText(R.string.title_ESTA);
		textViewTemplateConfig = (TextView) findViewById(R.id.textViewTemplateConfig_More);
		textViewTemplateConfig.setText(R.string.titleA_MTA);
		textViewLocationConfig = (TextView) findViewById(R.id.textViewLocation_More);
		textViewLocationConfig.setText(R.string.titleA_LCT);
		textViewFTPConfig = (TextView) findViewById(R.id.textViewFTP_More);
		textViewFTPConfig.setText(R.string.title_UploadPicFM);

		rLayoutoutEmployeeSendToAddressConfig.setOnClickListener(this);

		rLayoutTemplateConfig.setOnClickListener(this);

		rLayoutLocationConfig.setOnClickListener(this);

		rLayoutFTPConfig.setOnClickListener(this);

		iBtnBack = (ImageButton) findViewById(R.id.iBtnBack_More);
		iBtnBack.setOnClickListener(this);
		iBtnBack.setOnTouchListener(TouchEffect.TouchDark);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * ҳ��ؼ�����¼�
	 */
	public void onClick(View v) {
		Intent mIntent;
		Bundle mBundle;
		switch (v.getId()) {
		case R.id.relativeLayoutEmplayeeSendToConfig_More:

			mIntent = new Intent(MoreActivity.this,
					EmployeeSendToAddress2Activity.class);
			mBundle = new Bundle();
			mBundle.putString("listKey",
					BaseDataType.CustomReleAddress.toString());
			mBundle.putInt("selectMode", ListView.CHOICE_MODE_MULTIPLE);
			startActivity(mIntent);

			break;
		case R.id.relativeLayoutTemplateConfig_More:
			mIntent = new Intent(MoreActivity.this,
					ManagementTemplateActivity.class);
			mBundle = new Bundle();
			mBundle.putString("requestType", "Manage");
			mIntent.putExtras(mBundle);
			startActivity(mIntent);
			break;
		case R.id.relativeLayoutLocation_More:
			mIntent = new Intent(MoreActivity.this,
					LocationServiceActivity.class);
			startActivity(mIntent);
			break;
		case R.id.relativeLayoutFTP_More:
			mIntent = new Intent(MoreActivity.this, InstantUploadActivity.class);
			startActivity(mIntent);
			break;
		case R.id.iBtnBack_More:
			exit();
			break;
		default:
			break;
		}
	}

	/**
	 * ������˴��?��
	 */
	private void exit() {
		finish(); // Do nothing
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// KeyEvent.KEYCODE_BACK��?�ز���.
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
		}
		return false;
	}
}
