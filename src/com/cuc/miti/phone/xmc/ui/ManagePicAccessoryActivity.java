package com.cuc.miti.phone.xmc.ui;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Accessories;
import com.cuc.miti.phone.xmc.domain.Enums.OperationType;
import com.cuc.miti.phone.xmc.logic.AccessoriesService;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.MediaHelper;
import com.cuc.miti.phone.xmc.utils.TimeFormatHelper;
import com.cuc.miti.phone.xmc.utils.TouchEffect;
import com.cuc.miti.phone.xmc.R;

public class ManagePicAccessoryActivity extends BaseActivity implements
		OnClickListener {

	private Accessories accessorie = null;
	private ImageButton imageBtnSubmit_MPicA = null;
	private ImageButton imageBtnBack_MPicA = null;
	private EditText editTextTitle_MPicA = null;
	private EditText editTextContent_MPicA = null;
	private OperationType operationType = OperationType.None;
	Bitmap bitmap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// ȥ�������
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.manage_pic_accessory);

		Bundle myBundle = this.getIntent().getExtras();
		if (myBundle != null) {

			accessorie = (Accessories) myBundle.getParcelable("acc");

		}

		operationType = OperationType.valueOf(this.getIntent().getStringExtra(
				"operation"));

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
		case R.id.imageBtnSubmit_MPicA:
			// ���渽��
			saveAcc();

			// ���÷���ֵ
			Intent intent = new Intent(this, EditManuscriptsActivity.class);
			Bundle mybundle = new Bundle();
			mybundle.putParcelable("acc", accessorie);
			intent.putExtras(mybundle);

			setResult(RESULT_OK, intent);
			finish();
			break;
		case R.id.imageBtnBack_MPicA:
			if (operationType == OperationType.Add) {
				MediaHelper.deleteFile(accessorie.getUrl());
			}
			setResult(RESULT_CANCELED);
			finish();
		default:
			break;
		}
	}

	/**
	 * ���渽������
	 */
	private void saveAcc() {
		// ���ø�������ı���
		accessorie.setTitle(editTextTitle_MPicA.getText().toString().trim());
		// ���ø������������
		accessorie.setDesc(editTextContent_MPicA.getText().toString().trim());

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

		} else if (operationType == OperationType.Update)
			service.updateAccessories(this.accessorie);
	}

	/**
	 * ��ʼ����ؿؼ�
	 */
	private void Initialize() {

		imageBtnSubmit_MPicA = (ImageButton) findViewById(R.id.imageBtnSubmit_MPicA);
		imageBtnSubmit_MPicA.setOnClickListener(this);
		imageBtnSubmit_MPicA.setOnTouchListener(TouchEffect.TouchDark);

		imageBtnBack_MPicA = (ImageButton) findViewById(R.id.imageBtnBack_MPicA);
		imageBtnBack_MPicA.setOnClickListener(this);
		imageBtnBack_MPicA.setOnTouchListener(TouchEffect.TouchDark);

		if (accessorie == null)
			return;

		// ���⸳ֵ
		editTextTitle_MPicA = (EditText) findViewById(R.id.editTextTitle_MPicA);
		String title = accessorie.getTitle();
		// 2012-7-27Ĭ�ϰ�ͼƬ�����Ϊ��������
		// String tempTitle=accessorie.getOriginalName().substring(0,
		// accessorie.getOriginalName().lastIndexOf("."));
		// if ("".equals(title))
		// {
		// editTextTitle_MPicA.setText(tempTitle.toCharArray(), 0,
		// tempTitle.length());
		// }else{
		// editTextTitle_MPicA.setText(title.toCharArray(), 0, title.length());
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
			editTextTitle_MPicA.setText(sBuilder.toString());
		} else {
			editTextTitle_MPicA.setText(title);
		}

		// ������Ϣ��ֵ
		editTextContent_MPicA = (EditText) findViewById(R.id.editTextContent_MPicA);
		String des = accessorie.getDesc();
		if (des != null)
			editTextContent_MPicA.setText(des.toCharArray(), 0, des.length());

		String path = accessorie.getUrl();
		try {
			Bitmap bitmap = MediaHelper.getImageThumbnail(path, 0, 0, false);// BitmapFactory.decodeFile(path);

			// ExifInterface exifInterface = new ExifInterface(path);
			// int tag =
			// exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
			// int degree = 0;
			// if (tag == ExifInterface.ORIENTATION_ROTATE_90) {
			// degree = 90;
			// } else if (tag == ExifInterface.ORIENTATION_ROTATE_180) {
			// degree = 180;
			// } else if (tag == ExifInterface.ORIENTATION_ROTATE_270) {
			// degree = 270;
			// }
			// bitmap = BitmapFactory.decodeFile(path);
			// if (degree != 0 && bitmap != null) {
			// Matrix m = new Matrix();
			// m.setRotate(degree);
			//
			// bitmap=Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
			// bitmap.getHeight(), m, true);
			// }

			// bitmap = BitmapFactory.decodeFile(path);

			ImageView imageViewAccessorie = (ImageView) findViewById(R.id.imageView_MPicA);
			imageViewAccessorie.setImageBitmap(bitmap);
			imageViewAccessorie.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					Intent it = new Intent(Intent.ACTION_VIEW);
					File file = new File(accessorie.getUrl());
					if (file.exists()) {
						it.setDataAndType(Uri.fromFile(file), "image/*");
						startActivity(it);
					}

				}
			});

			// 2012-7-31��ӣ�ʹ�������ѡ����ͼƬ�󼴷���ԭ��ҳ��

			saveAcc();

		} catch (Exception e) {
			Logger.e(e);
		}

		// ����ǲ鿴״̬���������еĿؼ����ɱ༭
		if (operationType == OperationType.View)
			disableControls();
	}

	private void disableControls() {
		this.imageBtnSubmit_MPicA.setVisibility(View.GONE);
		// this.editTextTitle_MPicA.setEnabled(false);
		this.editTextTitle_MPicA.setFocusable(false);
		this.editTextContent_MPicA.setFocusable(false);
	}

	@Override
	public void onDestroy() {
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
		super.onDestroy();
	}
}
