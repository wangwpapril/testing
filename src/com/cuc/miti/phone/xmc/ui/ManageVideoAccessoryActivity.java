package com.cuc.miti.phone.xmc.ui;

import java.io.File;
import java.util.Date;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Accessories;
import com.cuc.miti.phone.xmc.domain.Enums.OperationType;
import com.cuc.miti.phone.xmc.logic.AccessoriesService;
import com.cuc.miti.phone.xmc.utils.MediaHelper;
import com.cuc.miti.phone.xmc.utils.TimeFormatHelper;
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
import android.widget.MediaController;
import android.widget.VideoView;

public class ManageVideoAccessoryActivity extends BaseActivity implements
		OnClickListener {

	private Accessories accessorie = null;
	private ImageButton imageBtnSubmit_MVideoA = null;
	private ImageButton imageBtnBack_MVideoA = null;
	private EditText editTextTitle_MVideoA = null;
	private EditText editTextContent_MVideoA = null;
	private OperationType operationType = OperationType.None;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// ȥ�������
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.manage_video_accessory);

		Bundle myBundle = this.getIntent().getExtras();
		if (myBundle != null) {

			accessorie = (Accessories) myBundle.getParcelable("acc");
			operationType = OperationType.valueOf(this.getIntent()
					.getStringExtra("operation"));
		}

		Initialize();

		IngleApplication.getInstance().addActivity(this);
	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.imageBtnSubmit_MVideoA:

			saveAcc();

			Intent intent = new Intent(this, EditManuscriptsActivity.class);
			Bundle mybundle = new Bundle();
			mybundle.putParcelable("acc", accessorie);
			intent.putExtras(mybundle);

			setResult(RESULT_OK, intent);
			finish();
			break;
		case R.id.imageBtnBack_MVideoA:
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

	private void Initialize() {

		imageBtnSubmit_MVideoA = (ImageButton) findViewById(R.id.imageBtnSubmit_MVideoA);
		imageBtnSubmit_MVideoA.setOnClickListener(this);
		imageBtnSubmit_MVideoA.setOnTouchListener(TouchEffect.TouchDark);

		imageBtnBack_MVideoA = (ImageButton) findViewById(R.id.imageBtnBack_MVideoA);
		imageBtnBack_MVideoA.setOnClickListener(this);
		imageBtnBack_MVideoA.setOnTouchListener(TouchEffect.TouchDark);

		if (accessorie == null)
			return;

		editTextTitle_MVideoA = (EditText) findViewById(R.id.editTextTitle_MVideoA);
		String title = accessorie.getTitle();
		// 2012-7-27Ĭ�ϰ�ͼƬ�����Ϊ��������
		// String tempTitle=accessorie.getOriginalName().substring(0,
		// accessorie.getOriginalName().lastIndexOf("."));
		// if ("".equals(title))
		// {
		// editTextTitle_MVideoA.setText(tempTitle.toCharArray(), 0,
		// tempTitle.length());
		// }else{
		// editTextTitle_MVideoA.setText(title.toCharArray(), 0,
		// title.length());
		// }
		// Modify By SongQing.20120808
		String userNameString = IngleApplication
				.getInstance().getCurrentUser();
		Date nowDate = new Date();
		String dateNowString = TimeFormatHelper.getShortFormatTime(nowDate,
				"MMddHHmmssSSS");
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("(").append(userNameString).append("_")
				.append(dateNowString).append(")");
		if ("".equals(title)) {
			editTextTitle_MVideoA.setText(sBuilder.toString());
		} else {
			editTextTitle_MVideoA.setText(title);
		}
		editTextContent_MVideoA = (EditText) findViewById(R.id.editTextContent_MVideoA);
		String des = accessorie.getDesc();
		if (des != null)
			editTextContent_MVideoA.setText(des.toCharArray(), 0, des.length());

		String path = accessorie.getUrl();
		VideoView videoView_MVideoA = (VideoView) findViewById(R.id.videoView_MVideoA);
		videoView_MVideoA.setVideoPath(path);
		videoView_MVideoA.setMediaController(new MediaController(this));
		videoView_MVideoA.start();
		videoView_MVideoA.requestFocus();

		// 2012-7-31��ӣ�ʹ�������ѡ����ͼƬ�󼴷���ԭ��ҳ��

		saveAcc();

		// ����ǲ鿴״̬���������еĿؼ����ɱ༭
		if (operationType == OperationType.View)
			disableControls();
	}

	private void disableControls() {
		this.imageBtnSubmit_MVideoA.setVisibility(View.GONE);
		this.editTextTitle_MVideoA.setFocusable(false);
		this.editTextContent_MVideoA.setFocusable(false);
	}

	private void saveAcc() {
		accessorie.setTitle(editTextTitle_MVideoA.getText().toString().trim());
		accessorie.setDesc(editTextContent_MVideoA.getText().toString().trim());

		AccessoriesService service = new AccessoriesService(this);
		// ���浽��ݿ�
		if (operationType == OperationType.Add) {
			service.addAccessories(this.accessorie);

			Intent intent = new Intent(this, EditManuscriptsActivity.class);
			Bundle mybundle = new Bundle();
			mybundle.putParcelable("acc", accessorie);
			intent.putExtras(mybundle);

			setResult(RESULT_OK, intent);
			finish();
		} else if (operationType == OperationType.Update) {
			service.updateAccessories(this.accessorie);
		}
	}
}
