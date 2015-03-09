package com.cuc.miti.phone.xmc.ui;

import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.R;
import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.adapter.ChooseAccessoriesAdapter;
import com.cuc.miti.phone.xmc.adapter.EditManuscriptAccessoriesAdapter;
import com.cuc.miti.phone.xmc.domain.Accessories;
import com.cuc.miti.phone.xmc.domain.Enums.OperationType;
import com.cuc.miti.phone.xmc.logic.ManuscriptsService;
import com.cuc.miti.phone.xmc.utils.MediaHelper;
import com.cuc.miti.phone.xmc.utils.ToastHelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class AttachmentChooseActivity extends BaseActivity implements
		OnClickListener {
	protected GridView gridViewAccerioes_attachC = null;// �����б���ֿؼ�

	protected ChooseAccessoriesAdapter adapter = null;// �����б�Adapter

	private ImageButton imageBtnBack_attachC;
	private ImageButton imageBtnFinish_attachC;
	private Accessories acc;
	private List<Accessories> accessories;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// ȥ�������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.attachment_choose);
		initialize();
		setupViews();
		IngleApplication.getInstance().addActivity(this);
	}

	private void setupViews() {
		gridViewAccerioes_attachC = (GridView) findViewById(R.id.gridViewAccerioes_attachC);
		imageBtnBack_attachC = (ImageButton) findViewById(R.id.imageBtnBack_attachC);
		imageBtnFinish_attachC = (ImageButton) findViewById(R.id.imageBtnFinish_attachC);
		imageBtnBack_attachC.setOnClickListener(this);
		imageBtnFinish_attachC.setOnClickListener(this);

		adapter = new ChooseAccessoriesAdapter(this.accessories, this);
		gridViewAccerioes_attachC.setAdapter(adapter);
		// ��ʼ��gridview�����¼�
		gridViewAccerioes_attachC
				.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						Accessories acc = accessories.get(position);

					}
				});
	}

	private void initialize() {
		Intent intent = getIntent();
		acc = (Accessories) intent.getParcelableExtra("ACC");
		accessories = intent.getParcelableArrayListExtra("ACCESSORIES");

	}

	public void onClick(View v) {
		switch (v.getId()) {
		// �����ϼ�
		case R.id.imageBtnBack_attachC:
			ToastHelper.showToast("ȡ��ͬ��", Toast.LENGTH_SHORT);
			setResult(RESULT_OK);
			this.finish();
			break;
		// ȷ��ѡ��
		case R.id.imageBtnFinish_attachC:
			chooseFinish();
			break;
		}
	}

	/**
	 * �ύ
	 */
	private void chooseFinish() {
		changeAccs();
		// ���÷���ֵ
		Intent intent = new Intent();
		Bundle mybundle = new Bundle();
		mybundle.putParcelableArrayList("ACCS", new ArrayList<Accessories>(
				accessories));
		intent.putExtras(mybundle);
		ToastHelper.showToast("ͬ�����", Toast.LENGTH_SHORT);
		setResult(RESULT_OK, intent);
		finish();
	}

	/**
	 * ͬ�������˵��
	 */
	private void changeAccs() {
		String title = acc.getTitle();
		String desc = acc.getDesc();
		if (title != null && desc != null) {
			for (int i = 0; i < accessories.size(); i++) {
				if (accessories.get(i).isChoose()) {
					accessories.get(i).setTitle(title);
					accessories.get(i).setDesc(desc);
				}
			}
		}
	}
}
