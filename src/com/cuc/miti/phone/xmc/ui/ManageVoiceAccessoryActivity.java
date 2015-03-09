package com.cuc.miti.phone.xmc.ui;

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
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

public class ManageVoiceAccessoryActivity extends BaseActivity implements
		OnClickListener {
	
	private Accessories accessorie = null;
	private ImageButton imageBtnSubmit_MVoiceA = null;
	private ImageButton imageBtnBack_MVoiceA = null;
	private EditText editTextTitle_MVoiceA = null;
	private EditText editTextContent_MVoiceA = null;
	private OperationType operationType = OperationType.None;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// ȥ�������
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.manage_voice_accessory);

		Bundle myBundle = this.getIntent().getExtras();
		if (myBundle != null) {

			accessorie = (Accessories) myBundle.getParcelable("acc");
			operationType = OperationType.valueOf(this.getIntent().getStringExtra(
			"operation"));
		}

		Initialize();
		
		IngleApplication.getInstance().addActivity(this);
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

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.imageBtnSubmit_MVoiceA:
			saveAcc();

			Intent intent = new Intent(this, EditManuscriptsActivity.class);
			Bundle mybundle = new Bundle();
			mybundle.putParcelable("acc", accessorie);
			intent.putExtras(mybundle);

			setResult(RESULT_OK, intent);
			finish();
			break;
		case R.id.imageBtnBack_MVoiceA:
			if (operationType == OperationType.Add) {
				MediaHelper.deleteFile(accessorie.getUrl());
			}
			setResult(RESULT_CANCELED);
			finish();
		default:
			break;
		}
	}

	private void Initialize() {

		imageBtnSubmit_MVoiceA = (ImageButton) findViewById(R.id.imageBtnSubmit_MVoiceA);
		imageBtnSubmit_MVoiceA.setOnClickListener(this);
		imageBtnSubmit_MVoiceA.setOnTouchListener(TouchEffect.TouchDark);

		imageBtnBack_MVoiceA = (ImageButton) findViewById(R.id.imageBtnBack_MVoiceA);
		imageBtnBack_MVoiceA.setOnClickListener(this);
		imageBtnBack_MVoiceA.setOnTouchListener(TouchEffect.TouchDark);

		if (accessorie == null)
			return;

		editTextTitle_MVoiceA = (EditText) findViewById(R.id.editTextTitle_MVoiceA);
		String title = accessorie.getTitle();
		//2012-7-27Ĭ�ϰ�ͼƬ�����Ϊ��������
//		String tempTitle=accessorie.getOriginalName().substring(0, accessorie.getOriginalName().lastIndexOf("."));
//		if ("".equals(title))
//		{
//			editTextTitle_MVoiceA.setText(tempTitle.toCharArray(), 0, tempTitle.length());
//		}else{
//			editTextTitle_MVoiceA.setText(title.toCharArray(), 0, title.length());
//		}
		//Modify By SongQing.20120808
		String userNameString = IngleApplication.getInstance().getCurrentUser();
		Date nowDate = new Date();
		String dateNowString = TimeFormatHelper.getShortFormatTime(nowDate,"MMddHHmmssSSS");
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("(").append(userNameString).append("_").append(dateNowString).append(")");
		if ("".equals(title)){
			editTextTitle_MVoiceA.setText(sBuilder.toString());
		}else{
			editTextTitle_MVoiceA.setText(title);
		}

		editTextContent_MVoiceA = (EditText) findViewById(R.id.editTextContent_MVoiceA);
		String des = accessorie.getDesc();
		if (des != null)
			editTextContent_MVoiceA.setText(des.toCharArray(), 0, des.length());

		String path = accessorie.getUrl();
		VideoView videoView_MVoiceA = (VideoView) findViewById(R.id.videoView_MVoiceA);
		videoView_MVoiceA.setVideoPath(path);
		videoView_MVoiceA.setMediaController(new MediaController(this));
		videoView_MVoiceA.start();
		videoView_MVoiceA.requestFocus();

		
		//2012-7-31��ӣ�ʹ�������ѡ����ͼƬ�󼴷���ԭ��ҳ��
		
		saveAcc();
		
		//����ǲ鿴״̬���������еĿؼ����ɱ༭
		if(operationType == OperationType.View)
			disableControls();
	}
	
	private void disableControls(){
		this.imageBtnSubmit_MVoiceA.setVisibility(View.GONE);
		this.editTextTitle_MVoiceA.setFocusable(false);
		this.editTextContent_MVoiceA.setFocusable(false);
	}

	private void saveAcc(){
		accessorie.setTitle(editTextTitle_MVoiceA.getText().toString().trim());
		accessorie.setDesc(editTextContent_MVoiceA.getText().toString().trim());
		
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
}
