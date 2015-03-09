package com.cuc.miti.phone.xmc.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Accessories;
import com.cuc.miti.phone.xmc.domain.ManuscriptTemplate;
import com.cuc.miti.phone.xmc.domain.Manuscripts;
import com.cuc.miti.phone.xmc.domain.Enums.XmcBool;
import com.cuc.miti.phone.xmc.logic.ManuscriptsService;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.TimeFormatHelper;
import com.cuc.miti.phone.xmc.utils.ToastHelper;
import com.cuc.miti.phone.xmc.utils.XMLDataHandle;
import com.cuc.miti.phone.xmc.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class WyjTestActivity extends Activity implements OnClickListener {

	Button btnXMLSerializer;
	Button btnOperationLog;
	Button btnSystemLog;
	Button btnAddMC;
	Button btnAddPicButton;
	Button btnAddVideo;
	Button btnEditingList_wyj;
	Button btnEliList_wyj;
	Button btnSendToList_wyj;
	Button btnAddVoice;

	private static final int RESULT_CAPTURE_IMAGE = 1;// �����requestCode
	private static final int REQUEST_CODE_TAKE_VIDEO = 2;// ����������requestCode
	private static final int RESULT_CAPTURE_RECORDER_SOUND = 3;// ¼����requestCode

	private String strImgPath = "";// ��Ƭ�ļ����·��
	private String strVideoPath = "";// ��Ƶ�ļ��ľ��·��
	private String strRecorderPath = "";// ¼���ļ��ľ��·��

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.wyjtest);

		// IngleApplication.getInstance().getActivities().add(this);

		// test
		btnXMLSerializer = (Button) findViewById(R.id.btnXMLSerializer);
		btnXMLSerializer.setOnClickListener(this);

		btnOperationLog = (Button) findViewById(R.id.btnOperationLog);
		btnOperationLog.setOnClickListener(this);

		btnSystemLog = (Button) findViewById(R.id.btnSystemLog);
		btnSystemLog.setOnClickListener(this);
		
		btnAddMC = (Button) findViewById(R.id.btnAddMC);
		btnAddMC.setOnClickListener(this);
		
		btnAddVideo = (Button) findViewById(R.id.btnAddVideo);
		btnAddVideo.setOnClickListener(this);
		
		btnAddPicButton = (Button) findViewById(R.id.btnAddPic);
		btnAddPicButton.setOnClickListener(this);
		
		btnEditingList_wyj = (Button) findViewById(R.id.btnEditingList_wyj);
		btnEditingList_wyj.setOnClickListener(this);
		
		btnEliList_wyj = (Button) findViewById(R.id.btnEliList_wyj);
		btnEliList_wyj.setOnClickListener(this);
		
		btnSendToList_wyj = (Button) findViewById(R.id.btnSendToList_wyj);
		btnSendToList_wyj.setOnClickListener(this);
		
		btnAddVoice = (Button) findViewById(R.id.btnAddVoice);
		btnAddVoice.setOnClickListener(this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnXMLSerializer:

			Manuscripts manuscript = new Manuscripts();
			Accessories accessories = new Accessories();
			try {
				XMLDataHandle.Serializer(Build(manuscript), Build(accessories),
						"TestXMLData");
			} catch (Exception e) {
				Logger.e(e);
			}

			break;
		case R.id.btnOperationLog:
			Logger.o(Logger.OperationMessage.CreateM);
			break;
		case R.id.btnSystemLog:
			int a = 5;
			int b = 0;
			int c;
			try {
				c = a / b;
			} catch (Exception e) {
				Logger.e(e);
			}
			break;
		default:
			break;

		case R.id.btnAddPic:
			Intent intent = new Intent(this, ManagePicAccessoryActivity.class);
			startActivity(intent);
			break;
		case R.id.btnAddVideo:
			TimeZone zone =  TimeZone.getDefault();
			String timeString = TimeFormatHelper.getGMTTime(new Date());
			//Toast.makeText(this, timeString, Toast.LENGTH_LONG).show();
			ToastHelper.showToast(timeString,Toast.LENGTH_SHORT);
			break;
		case R.id.btnAddVoice:
			ManuscriptsService service = new ManuscriptsService(this);
			service.deleteAllManuscripts();
			break;
		case R.id.btnAddMC:
			Intent editIntent = new Intent(this, AddManuscriptsActivity.class);
			editIntent.putExtra("id", "");
			startActivity(editIntent);
			break;
		case R.id.btnEditingList_wyj:
			Intent editingIntent = new Intent(this, EditingManuscriptsActivity.class);
			startActivity(editingIntent);
			break;
		case R.id.btnEliList_wyj:
			Intent edliIntent = new Intent(this, EliminationManuscriptsActivity.class);
			startActivity(edliIntent);
			break;
		case R.id.btnSendToList_wyj:
//			Intent sendIntent = new Intent(this, FileManagerActivity.class);
//			startActivity(sendIntent);
			System.exit(0);
			break;
		}
	}

	private Accessories Build(Accessories accessories) {
		accessories = new Accessories();
		accessories.setInfo("W=120,H=240,R=72");
		accessories.setSize("1024000");
		accessories.setOriginalName("IMG_1203.JPG");

		accessories.setTitle("1.JPG");
		return accessories;
	}

	private Manuscripts Build(Manuscripts manuscript) {

		manuscript = new Manuscripts();
		manuscript.setReceiveTime("20061218.094246.930+0800");
		manuscript.setReletime("20061218.094246.930+0800");
		
		manuscript.setManuscriptTemplate(new ManuscriptTemplate());
		
		manuscript.getManuscriptTemplate().setCreatetime("20061218.084246.930+0800");
		manuscript.setNewsid("XxjdvhC000001_20071126_ENPSN0");
		manuscript.setTitle("����");
		manuscript.getManuscriptTemplate().setKeywords("�ؼ��");
		manuscript.getManuscriptTemplate().setLanguage("����");
		manuscript.getManuscriptTemplate().setLanguageID("zh-CN");
		manuscript.getManuscriptTemplate().setPriorityID("8");
		manuscript.getManuscriptTemplate().setPriority("��ͨ��");
		manuscript.getManuscriptTemplate().setProvtype("�������");
		manuscript.getManuscriptTemplate().setProvtypeID("02");
		manuscript.getManuscriptTemplate().setDoctype("ҽҩ����");
		manuscript.getManuscriptTemplate().setDoctypeID("11");
		manuscript.getManuscriptTemplate().setComefromDept("�Ϻ�����,������");
		manuscript.getManuscriptTemplate().setComefromDeptID("shfsh,xjtec");
		manuscript.getManuscriptTemplate().setHappenplace("�¼������");
		manuscript.getManuscriptTemplate().setReportplace("�����ص�");
		manuscript.getManuscriptTemplate().setSendarea("����ص�");
		manuscript.getManuscriptTemplate().setRegion("[����]����");
		manuscript.getManuscriptTemplate().setRegionID("CN");
		manuscript.getManuscriptTemplate().setAddress("��������,��̫���ı༭��");
		manuscript.getManuscriptTemplate().setAddressID("xjadb,hkhgc");
		manuscript.getManuscriptTemplate().setIs3Tnews(XmcBool.False);
		manuscript.setContents("δ�����ʽ����ĸ�����ģ�δ����ͳ�Ʒ����д˽ڵ�");

		manuscript.getManuscriptTemplate().setLoginname("u1");
		manuscript.setUsernameC("�û�һ");
		manuscript.setUsernameE("user 1");
		manuscript.setGroupcode("Xxjtec");
		manuscript.setGroupnameC("������");
		manuscript.setGroupnameE("jishuju");

		return manuscript;
	}

	private void cameraMethod() {
		Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		strImgPath = Environment.getExternalStorageDirectory().toString()
				+ "/CONSDCGMPIC/";// �����Ƭ���ļ���
		String fileName = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date()) + ".jpg";// ��Ƭ����

		File out = new File(strImgPath);
		if (!out.exists()) {
			out.mkdirs();
		}
		out = new File(strImgPath, fileName);
		strImgPath = strImgPath + fileName;// ����Ƭ�ľ��·��
		Uri uri = Uri.fromFile(out);
		imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		imageCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult(imageCaptureIntent, RESULT_CAPTURE_IMAGE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if (resultCode == Activity.RESULT_OK) {
				if (RESULT_CAPTURE_IMAGE == requestCode) {

					//Toast.makeText(this, strImgPath, Toast.LENGTH_SHORT).show();
					ToastHelper.showToast(strImgPath,Toast.LENGTH_SHORT);
				}
				if (requestCode == REQUEST_CODE_TAKE_VIDEO) {

					Uri uriVideo = data.getData();
					Cursor cursor = this.getContentResolver().query(uriVideo,
							null, null, null, null);

					if (cursor.moveToNext()) {

						strVideoPath = cursor.getString(cursor
								.getColumnIndex("_data"));
						/*Toast.makeText(this, strVideoPath, Toast.LENGTH_SHORT)
								.show();*/
						ToastHelper.showToast(strVideoPath,Toast.LENGTH_SHORT);

					}
					cursor.close();
				}
				if (requestCode == RESULT_CAPTURE_RECORDER_SOUND) {
					Uri uriRecorder = data.getData();
					Cursor cursor = this.getContentResolver().query(
							uriRecorder, null, null, null, null);

					if (cursor.moveToNext()) {
						strRecorderPath = cursor.getString(cursor
								.getColumnIndex("_data"));
						/*Toast.makeText(this, strRecorderPath,
								Toast.LENGTH_SHORT).show();*/
						ToastHelper.showToast(strRecorderPath,Toast.LENGTH_SHORT);
					}
					cursor.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void videoMethod() {
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
		startActivityForResult(intent, REQUEST_CODE_TAKE_VIDEO);
	}

	private void soundRecorderMethod() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("audio/amr");
		startActivityForResult(intent, RESULT_CAPTURE_RECORDER_SOUND);
	}
}
