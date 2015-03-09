package com.cuc.miti.phone.xmc.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.cuc.miti.phone.xmc.R;
import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.adapter.FlashMAccessoryAdapter;
import com.cuc.miti.phone.xmc.domain.Accessories;
import com.cuc.miti.phone.xmc.domain.Enums.AccessoryType;
import com.cuc.miti.phone.xmc.domain.Enums.ManuscriptStatus;
import com.cuc.miti.phone.xmc.domain.Enums.OperationType;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceKeys;
import com.cuc.miti.phone.xmc.domain.Enums.TemplateType;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.domain.ManuscriptTemplate;
import com.cuc.miti.phone.xmc.domain.Manuscripts;
import com.cuc.miti.phone.xmc.domain.PositionInfo;
import com.cuc.miti.phone.xmc.domain.User;
import com.cuc.miti.phone.xmc.logic.ManuscriptTemplateService;
import com.cuc.miti.phone.xmc.logic.ManuscriptsService;
import com.cuc.miti.phone.xmc.utils.BaiduLocationHelper;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.MediaHelper;
import com.cuc.miti.phone.xmc.utils.SendManuscriptsHelper;
import com.cuc.miti.phone.xmc.utils.SharedPreferencesHelper;
import com.cuc.miti.phone.xmc.utils.StandardizationDataHelper;
import com.cuc.miti.phone.xmc.utils.ToastHelper;
import com.cuc.miti.phone.xmc.utils.TouchEffect;
import com.cuc.miti.phone.xmc.utils.Utils;

public class EditPicFManuscriptsActivity extends BaseActivity implements OnTouchListener,OnGestureListener,OnClickListener{

	private ImageView iViewTempletSwitcher;							//ģ���л�ImageView
	private ImageView iViewLocationStatus;								//��λ״̬ImageView
	private ImageButton iBtnBack;												//����ImageButton
	private ImageButton iBtnSendManu;									//����ImageButton
	private ImageButton iBtnSaveLocalization;							//���ر���ImageButton
	private ImageButton iBtnSaveOnline;									//���籣��ImageButton
	private ImageButton iBtnGetLocationAdd;							//��ȡ��ǰλ�þ�γ����ϢImageButton
	private EditText etTitle;															//�������
	private GridView gvPics;														//��Ƶ�����б�
	private String currentUser ="";												//��ǰ�û�
	private Boolean locationBoolean =false;								//��¼��λ��
	
	private List<Accessories> accessories = null;						// ��ǰ��Ѷ�����б�
	private Accessories accessory = null;
	private Manuscripts picFlashManuscript = null;
	private ManuscriptTemplate manuscriptTemplate;
	private static final int REQUEST_CAPTURE_IMAGE = 1;		// ���յ�requestCode
	private static final int REQUEST_SELECT_IMAGE = 2;			//ѡ��Ƭ��requestCode
	private static final int REQUEST_ACCESSORIES_EDIT = 3;		// �����༭��requestCode
	private static final int REQUEST_ACCESSORIES_ADD = 4;		// ������ӵ�requestCode	
	private static final int REQUEST_EDIT_TEMPLATE = 0;								//�鿴�ͱ༭ģ��	
	private static final int MESSAGE_AUTO_SAVE_MANUSCRIPT = 5;			//��ʱ����������
	
//	private LocationHelper locationHelper;
	private BaiduLocationHelper locationHelper;
	
	private FlashMAccessoryAdapter adapter;
	private ManuscriptsService manuscriptsService = null;
	private ManuscriptTemplateService manuscriptTemplateService = null;
	
	private GestureDetector mGestureDetector;     							//�����¼�  
	private static String accSrc = "";													 		// ��Ƭ�洢·��
	private Uri originalUri = null;														// ��Ƭ�洢uri
	// ���嶨ʱ��
	protected Timer timer = new Timer();
	private TimerTask task;
	protected boolean isSaved = false;															//����Ƿ��Ѿ������־λ
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.edit_pic_f_manuscripts);
		this.initialize();
		IngleApplication.getInstance().addActivity(this);
		
		locationHelper = new BaiduLocationHelper(IngleApplication.getInstance());
		//�򿪶�λ
		locationHelper.startLocationClient();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();		
		// �رն�ʱ������
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		
		//�رն�λ
		locationHelper.stopLocationClient();
	}
	
	@Override 
    public void onConfigurationChanged(Configuration config) { 
		super.onConfigurationChanged(config); 
    } 
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	/**
	 * ��ʼ��
	 */
	private void initialize() {
		manuscriptsService = new ManuscriptsService(EditPicFManuscriptsActivity.this);
		manuscriptTemplateService = new ManuscriptTemplateService(EditPicFManuscriptsActivity.this);	
		currentUser = IngleApplication.getInstance().getCurrentUser();
		this.initializeManuscript();
		this.setUpViews();
		this.initializeGridView();		
		this.iniAutoSave();
	}
	
	/**
	 * ��ʼ���Զ����涨ʱ��
	 */
	private void iniAutoSave() {
		// ������ʱ������������ÿ��30�뱣��һ��
		task = new TimerTask() {
			@Override
			public void run() {
				Message message = new Message();
				message.what = MESSAGE_AUTO_SAVE_MANUSCRIPT;
				handler.sendMessage(message);
			}
		};

		// �������л�ȡ�Զ�����ʱ����
		SharedPreferencesHelper helper = new SharedPreferencesHelper(this);

		int interval = 0;
		try {
			String strInterval = helper
					.GetUserPreferenceValue(PreferenceKeys.User_AutoSaveInterval
							.toString());
			if (!strInterval.equals("0"))
				interval = Integer.parseInt(strInterval);
		} catch (Exception e) {
			interval = 0;
		}

		if (interval != 0) {
			interval = interval * 1000;
			timer.schedule(task, 30000, interval);
		}
	}

	/**
	 * ί�У���ݶ�ʱ����֪ͨ���б���������
	 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// ���浱ǰ���
			if (msg.what == MESSAGE_AUTO_SAVE_MANUSCRIPT) {
				picFlashManuscript.setTitle(etTitle.getText().toString().trim());
				if(SendManuscriptsHelper.validateForAutoSave(picFlashManuscript, accessories)){
					saveManuscript(false);				//�Զ�����ʱ��һ����Ҫ�û��������
				}
			}
			super.handleMessage(msg);
		}

	};

	/**
	 * ��ʼ��ҳ��ؼ�
	 */
	private void setUpViews(){	
		iViewTempletSwitcher = (ImageView)findViewById(R.id.iViewTempletSwitcher_EPicFM);
		iViewLocationStatus = (ImageView)findViewById(R.id.iViewlocationStatus_EPicFM);
		iBtnBack = (ImageButton)findViewById(R.id.iBtnBack_EPicFM);
		iBtnSendManu = (ImageButton)findViewById(R.id.iBtnSendManu_EPicFM);
		iBtnSaveLocalization = (ImageButton)findViewById(R.id.iBtnSaveLocalization_EPicFM);
		iBtnSaveOnline = (ImageButton)findViewById(R.id.iBtnSaveOnline_EPicFM);
		iBtnGetLocationAdd=(ImageButton)findViewById(R.id.iBtnGetLocationAdd_EPicFM);
		etTitle = (EditText)findViewById(R.id.editTextManuTitle_EPicFM);
		gvPics = (GridView)findViewById(R.id.gridViewPics_EPicFM);
		
		etTitle.setText(manuscriptTemplate.getDefaulttitle());											//�Ӹ�ǩ�л�ȡ��Ĭ�ϱ���
		
		mGestureDetector = new GestureDetector((OnGestureListener) this);    
		iViewTempletSwitcher.setOnClickListener(this);
		iViewLocationStatus.setVisibility(View.INVISIBLE);			//Ĭ��Ϊ���ɼ�
		iBtnBack.setOnClickListener(this);
		iBtnBack.setOnTouchListener(TouchEffect.TouchDark);
		iBtnSendManu.setOnClickListener(this);
		iBtnSendManu.setOnTouchListener(TouchEffect.TouchDark);
		iBtnSaveLocalization.setOnClickListener(this);
		iBtnSaveLocalization.setOnTouchListener(TouchEffect.TouchDark);
		iBtnSaveOnline.setOnClickListener(this);
		iBtnSaveOnline.setOnTouchListener(TouchEffect.TouchDark);
		iBtnGetLocationAdd.setOnClickListener(this);
		iBtnGetLocationAdd.setOnTouchListener(TouchEffect.TouchDark);
	}
	
	/**
	 * ��ʼ����Ƶ�б�GridView
	 */
	private void initializeGridView(){
		//���������Ӱ�ť���ð�ťһֱ�����б��ĩβ
		accessory = new Accessories("", "", "", "", "", "", "", "", "","");
		this.accessories = new ArrayList<Accessories>();
		this.accessories.add(accessory);
		
		adapter = new FlashMAccessoryAdapter(this.accessories, EditPicFManuscriptsActivity.this,AccessoryType.Picture);
		gvPics.setAdapter(adapter);
		// ע�������Ĳ˵�
		registerForContextMenu(gvPics);
		
		// ��ʼ��gridview�����¼�
		gvPics.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v,int position, long id) {
						if(position == accessories.size()-1){
							//cameraMethod();
							// ��ʾ�����Ĳ˵�
							v.showContextMenu();
						}
						else{
							Accessories acc = accessories.get(position);

							startActivitys(acc,MediaHelper.checkFileType(acc.getUrl()), REQUEST_ACCESSORIES_EDIT,OperationType.Update);
						}
					}
		});
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		// ��ʼ�������Ĳ˵���
		menu.add(Menu.NONE, REQUEST_CAPTURE_IMAGE, 0, R.string.TackPicture);
		menu.add(Menu.NONE, REQUEST_SELECT_IMAGE, 0, R.string.MediaStore);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem menu) {
		try {

			switch (menu.getItemId()) {
			case REQUEST_CAPTURE_IMAGE:
				// ��������ͷ����
				cameraMethod();
				break;		
			case REQUEST_SELECT_IMAGE:
				// ����ý���
				selectMediaFile();
				break;
			default:
				break;
			}
		} catch (Exception e) {
			Logger.e(e);
		}
		return super.onContextItemSelected(menu);
	}
	
	/**
	 * ��ý���ѡ��ý���ļ�
	 */
	private void selectMediaFile() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setDataAndType(Uri.parse("content://media/internal/images/media"),"image/*");
		 /* ʹ��Intent.ACTION_GET_CONTENT���Action */
       // intent.setAction(Intent.ACTION_GET_CONTENT); 
		//intent.setData(Uri.parse("image/*"));
		/* ȡ��Ƭ�󷵻ر����� */
		startActivityForResult(intent, REQUEST_SELECT_IMAGE);
	}
	
	/**
	 * ��Ƭ
	 */
	private void cameraMethod() {
		Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		accSrc = StandardizationDataHelper.getAccessoryFileTempStorePath()
				+ "//" + System.currentTimeMillis() + ".jpg";

		originalUri = Uri.fromFile(new File(accSrc));
		imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, originalUri);

		// imageCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult(imageCaptureIntent, REQUEST_CAPTURE_IMAGE);
	}
	
	/**
	 * ��ʼ����ǩģ����Ϣ
	 */
	private void initializeManuscriptTemplate(){
		//TODO  ��ȡ��ǩģ���ֵ(��ǩģ��Ӧ��ֱ��ȥĬ�ϵ����ֿ�Ѷģ��)����ȡ��ǰ�û��������ֵ����GroupName��
		manuscriptTemplate = new ManuscriptTemplate();	
		manuscriptTemplate = this.manuscriptTemplateService.getManuscriptTemplateSystem(this.currentUser, TemplateType.PICTURE.toString());
	}
	
	/**
	 * ��ʼ���������
	 */
	private void initializeManuscript() {
		
		try{
			picFlashManuscript = new Manuscripts();
			picFlashManuscript.setM_id(UUID.randomUUID().toString());					//���Ψһ�ı�ʶID
			
			//��ȡ��ǰ�û���Ϣ
			User user = IngleApplication.getInstance().currentUserInfo;
			picFlashManuscript.setGroupcode(user.getUserattribute().getGroupCode());
			picFlashManuscript.setGroupnameC(user.getUserattribute().getGroupNameC());
			picFlashManuscript.setGroupnameE(user.getUserattribute().getGroupNameE());
			picFlashManuscript.setUsernameC(user.getUserattribute().getUserNameC());
			picFlashManuscript.setUsernameE(user.getUserattribute().getUserNameE());
			picFlashManuscript.setLoginname(user.getUsername());
			
			initializeManuscriptTemplate();						//��ݿ�Ѷ���ͳ�ʼ����ǩģ��
			picFlashManuscript.setManuscriptTemplate(manuscriptTemplate);
			
			picFlashManuscript.setManuscriptsStatus(ManuscriptStatus.Editing);				//����"�ڱ�"״̬
			picFlashManuscript.setAuthor(manuscriptTemplate.getAuthor());
			
			picFlashManuscript.getManuscriptTemplate().setComefromDept(user.getUserattribute().getGroupNameC());
			picFlashManuscript.getManuscriptTemplate().setComefromDeptID(user.getUserattribute().getGroupCode());
				
			manuscriptsService.addManuscripts(picFlashManuscript);
			
		}catch(Exception e){
			Logger.e(e);
		}
	}
	
	/**
	 * ���Intent������ݻ�ȡѡ���ý���ļ��ĵ�ַ�� ��ý�����ѡȡ���ļ���URI����SD����ֱ��ѡȡ���ļ��Ǿ��·��
	 * 
	 * @param data
	 * @return
	 */
	private String fetchMediaPath(Intent data) {
		String ouputPath = "";

		Uri uri = data.getData();

		// �ж��Ƿ�URI���͵����
		boolean isUri = MediaHelper.isUri(uri.toString());

		if (isUri == true) {

			Cursor cursor = this.getContentResolver().query(uri, null, null,
					null, null);

			if (cursor.moveToNext()) {
				ouputPath = cursor.getString(cursor.getColumnIndex("_data"));
			}
			cursor.close();
		} else {
			ouputPath = uri.getPath();
		}

		return ouputPath;
	}
	
	/**
	 * ���Accessories����
	 * 
	 * @param accPath
	 *            ����·��
	 * @param accType
	 *            ��������
	 * @return ��������
	 */
	private Accessories populateAccessorie(String accPath, AccessoryType accType) {
		
		Accessories accessorie = new Accessories();
		accessorie.setA_id(UUID.randomUUID().toString());
		accessorie.setM_id(this.picFlashManuscript.getM_id());
		accessorie.setType(accType.toString());

		try {
			accessorie.setOriginalName(MediaHelper.getFileName(accPath));
			accessorie.setSize(MediaHelper.getFileSize(accPath));
			accessorie.setCreatetime(new Date().toString());
			accessorie.setUrl(accPath);
		} catch (IOException ex) {
			accessorie.setOriginalName("");
			accessorie.setSize("");
			Logger.e(ex);

			showMessage(getResources().getString(R.string.filePathError));
		}

		// ͼƬ·��
		accessorie.setUrl(accPath);

		return accessorie;
	}
	
	protected void netSave() {

	}
	
	/**
	 * ��������ݵ�������ݿ⣬��ҪУ������true������ҪУ�鴫��false
	 * @return
	 */
	private boolean saveManuscript(boolean validate){
		try {
			picFlashManuscript.setTitle(this.etTitle.getText().toString().trim());
			
			if (validate == true) {
				KeyValueData message = new KeyValueData("", "");
				boolean validateResult = SendManuscriptsHelper.validataForSave(this.picFlashManuscript, message);

				if (validateResult == false) {
					showMessage(message.getValue());
					return false;
				}
			}
			
			picFlashManuscript.setManuscriptsStatus(ManuscriptStatus.Editing);
			
			if(manuscriptsService.updateManuscripts(picFlashManuscript)){
				isSaved = true;
				showMessage(getResources().getString(R.string.manuAlreadySaved));			//TODO ��ʻ�
				return true;
			}else{
				showMessage(getResources().getString(R.string.manuSavedError));			//TODO ��ʻ�
				return false;
			}			
		} catch (Exception e) {
			Logger.e(e);
			showMessage(getResources().getString(R.string.manuSavedError));			//TODO ��ʻ�
			return false;
		}
	}
	
	/**
	 * ���壬�����¼�����
	 * 1.�������״��
	 * 2.������
	 * 3.��ָ��Ϊһ��������Ӧһ�����(�����ļ�+xml)�������������淶�������xml����ɱ�׼��ʽ��xml�ļ�
	 * 4.һ������+һ��xml Ϊһ�� UploadJob
	 * 5.��UploadJob�ŵ�Application��
	 * 6.�ı���ݿ��и�����ж�Ӧ�����״̬�ֶ�Ϊ�������С�
	 * 7.����ɹ����޸ĸ�����ж�Ӧ�����״̬�ֶ�Ϊ���ѷ��͡�
	 */
	private boolean sendManuscript(){
		if(accessories.size()==1){
			this.showMessage(getResources().getString(R.string.manuMustHaveAcc));
			return false;
		}else {
			
			if(this.etTitle.getText().toString().trim().length()>0){
				if(!saveManuscript(true)){			//������
					return false;
				}
				
				KeyValueData message = new KeyValueData("", "");
				if(!SendManuscriptsHelper.validateForReleManuscript(picFlashManuscript, message, this)){		//������ǰ��ʽУ��
					this.showMessage(message.getValue());
					return false;
				}
				
				//����
				try {
					return manuscriptsService.sendManuscripts(this.picFlashManuscript,this.getUploadAccessories());
				}catch (Exception e) {
					Logger.e(e);
					return false;
				}
			}
			else{
				this.showMessage(getResources().getString(R.string.manuTitleNotNull));
				return false;
			}
		}	
	}
	
	/**
	 * �˳���ҳ��ʱִ�к���(������֤�����浱ǰ�༭���)
	 */
	private void exit(){
		
		picFlashManuscript.setTitle(this.etTitle.getText().toString().trim());
		
		//У��������Ϣ�Ƿ���Ҫ����
		boolean result = false;
		if(this.accessories==null  || this.accessories.size()==1){					//ͼƬ��ѶĬ�Ͼ���һ����Ӱ�ť��Ϊ������������Ҫ�ж�
			result = SendManuscriptsHelper.validateForBack(picFlashManuscript,null);
		}else{
			result = SendManuscriptsHelper.validateForBack(picFlashManuscript,this.accessories);		
		}

		// �ж�������½�������£�û�б��⡢������ɾ��˸��
		if (result == false && isSaved == false) {
			try {
				manuscriptsService.deleteById(picFlashManuscript.getM_id());
			} catch (IOException e) {
					Logger.e(e);}
		} else {
			if(result == true){
				saveManuscript(true);					
			}
		}	
		//�˳���Activity
		setResult(RESULT_OK);
		finish();
	}
	
	/**
	 * �����������
	 */
	private List<Accessories> getUploadAccessories(){
		List<Accessories> tempList = new ArrayList<Accessories>();
		for(int i = 0; i< this.accessories.size()-1;i++){
			tempList.add(this.accessories.get(i));
		}
		
		return tempList;
	}
	
	/**
	 * ��ʾ�����Ϣ
	 * 
	 * @param message
	 */
	private void showMessage(String message) {
		//Toast.makeText(EditPicFManuscriptsActivity.this, message, Toast.LENGTH_SHORT).show();
		ToastHelper.showToast(message,Toast.LENGTH_SHORT);
	}
	
	/**
	 * �����µ�Active������ý�����
	 * 
	 * @param ouputPath
	 * @param requestCode
	 */
	private void startActivitys(Accessories acc, AccessoryType accType, int requestCode,OperationType opType) {
		try {
			// �����µĸ���������ͼ
			Intent intent = new Intent(this, ManagePicAccessoryActivity.class);
			Bundle mybundle = new Bundle();
			mybundle.putParcelable("acc", acc);
			intent.putExtras(mybundle);
			intent.putExtra("operation", opType.toString());
			startActivityForResult(intent, requestCode);
		} catch (Exception e) {
			Logger.e(e);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String ouputPath = "";
		String end = "";
		String fileURLFD  = "";
		String fileURLFDC = "";
		try {
			if (resultCode == Activity.RESULT_OK) {		
				AccessoryType accType = AccessoryType.Cache;
				Accessories acc = null;
				Bundle mBundle = null;
				switch(requestCode){
					case REQUEST_CAPTURE_IMAGE:
						ouputPath = accSrc;
						if (ouputPath.equals(""))
							return;
						end = ouputPath.substring(ouputPath.lastIndexOf(".") + 1,
								ouputPath.length()).toLowerCase();
						fileURLFD = ouputPath;
						fileURLFDC = fileURLFD.substring(0, fileURLFD.lastIndexOf("."))
								+ "_resized." + end;
						ouputPath = Utils.CompressBitmap(fileURLFDC, fileURLFD);
						if (ouputPath.equals(fileURLFDC))
							MediaHelper.deleteFile(fileURLFD);
						Utils.notifyMediaScanToFindThePic(ouputPath);
						// ���·����ý������������ɸ�������
						accType = MediaHelper.checkFileType(ouputPath);

						acc = populateAccessorie(ouputPath, accType);
						startActivitys(acc, accType, REQUEST_ACCESSORIES_ADD,
								OperationType.Add);
						break;
					case  REQUEST_SELECT_IMAGE:
						ouputPath = fetchMediaPath(data);
						ouputPath = MediaHelper.copy2TempStore(ouputPath);
						if (ouputPath.equals(""))
							return;
						end = ouputPath.substring(ouputPath.lastIndexOf(".") + 1,
								ouputPath.length()).toLowerCase();
						if (end.equals("jpg") || end.equals("png") || end.equals("jpeg")){
							fileURLFD = ouputPath;
							fileURLFDC = fileURLFD.substring(0, fileURLFD.lastIndexOf("."))
									+ "_resized." + end;
							ouputPath = Utils.CompressBitmap(fileURLFDC, fileURLFD);
							if (ouputPath.equals(fileURLFDC))
								MediaHelper.deleteFile(fileURLFD);
							//Utils.notifyMediaScanToFindThePic(ouputPath);
						}
						// ���·����ý������������ɸ�������
						accType = MediaHelper.checkFileType(ouputPath);

						acc = populateAccessorie(ouputPath, accType);
						startActivitys(acc, accType, REQUEST_ACCESSORIES_ADD,	OperationType.Add);
						break;
					case REQUEST_ACCESSORIES_ADD:
						mBundle = data.getExtras();
						if (mBundle != null) {
							// �õ���Ӻ�ĸ�������
							acc = (Accessories) mBundle.getParcelable("acc");

							adapter.addCurrentAccessories(acc);
						}
						break;
					case REQUEST_ACCESSORIES_EDIT:
						mBundle = data.getExtras();
						if (mBundle != null) {
							// �õ��޸ĺ�ĸ�������
							acc = (Accessories) mBundle.getParcelable("acc");

							adapter.updateCurrentAccessories(acc);
						}
						break;
					case REQUEST_EDIT_TEMPLATE:
						mBundle = data.getExtras();
						manuscriptTemplate = mBundle.getParcelable("manuTemplateInfo");
						
						picFlashManuscript.setManuscriptTemplate(manuscriptTemplate);
						picFlashManuscript.setAuthor(manuscriptTemplate.getAuthor());
						break;
					default:
						break;	
				}
			}
		} catch (Exception e) {
			Logger.e(e);
		}
	}
	
	/**
	 * ҳ��ؼ��������
	 */
	public void onClick(View view) {
				switch(view.getId()){
					case R.id.iViewTempletSwitcher_EPicFM:
						enterManuscriptTemplate();
						break;
					case R.id.iBtnBack_EPicFM:
						exit();
						break;
					case R.id.iBtnGetLocationAdd_EPicFM:
						getLocation();
//						if(locationBoolean == false){
//							locationHelper = new LocationHelper(EditPicFManuscriptsActivity.this);
//							String location = locationHelper.GetLocation();
//							if(location != "0,0"){
//								//�ҵ���ǰ�ľ�γ����Ϣ����ʾ��λͼ��
//								//Toast.makeText(EditPicFManuscriptsActivity.this, location, Toast.LENGTH_SHORT).show();
//								ToastHelper.showToast(location,Toast.LENGTH_SHORT);
//								//iViewLocationStatus.setVisibility(View.VISIBLE);	
//								showMessage(location);
//								picFlashManuscript.setLocation(location);
//								//manuscriptsService.updateManuscripts(manuItem);
//								locationBoolean=true;
//								//��ͼ��任���Ѷ�λ��ͼ��
//								iBtnGetLocationAdd.setBackgroundResource(R.drawable.got_location_960x540);
//							}else{
//								showMessage(getResources().getString(R.string.canNotGetLocation));	
//							}
//						}else{
//							picFlashManuscript.setLocation("0,0");
//							locationBoolean=false;
//							//�Ѷ�λ��ͼ��任��ԭ����ͼ��
//							iBtnGetLocationAdd.setBackgroundResource(R.drawable.get_location_960x540);
//						}
						break;
					case R.id.iBtnSendManu_EPicFM:
						if(sendManuscript()){
							setResult(RESULT_OK);
							this.finish();
						}
						break;
					case R.id.iBtnSaveLocalization_EPicFM:
						//	Logger.o(OperationMessage.SaveMLocalization);
						//if(picFlashManuscript==null){initializeManuscript();}		
						this.saveManuscript(true);
						break;
					case R.id.iBtnSaveOnline_EPicFM:
						showMessage(getResources().getString(R.string.stillBuilding));
						break;
					default:
						break;
				}	
	}
	
	/**
	 * �ֻ�����¼����
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// KeyEvent.KEYCODE_BACK��?�ز���.
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
		}
		return true;
	}
	
	/**
	 * �����ǩҳ
	 */
	private void enterManuscriptTemplate(){
		Intent mIntent =null;
		mIntent = new Intent(EditPicFManuscriptsActivity.this,EditTemplateActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putString("requestType", "ManuscriptEdit");
		mBundle.putParcelable("manuTemplateInfo", manuscriptTemplate);				//�ѵ�ǰ��ģ�����ݴ���ģ��༭չʾҳ��(EditTemplateActivity)
		mIntent.putExtras(mBundle);
		startActivityForResult(mIntent, REQUEST_EDIT_TEMPLATE);
		//�����л����������ұ߽��룬����˳�
		overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
	}

	/*----------------------------------------------���津�����������¼� Begin-------------------------------------------------------*/
	/**
	 * �����¼����
	 */
	public boolean onTouch(View v, MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

	public boolean onDown(MotionEvent arg0) {
		return false;
	}
	
	private int verticalMinDistance = 100;  				//ˮƽ��С�Ļ�������
	private int minVelocity         = 0;  					

	@Override  
	public boolean dispatchTouchEvent(MotionEvent ev) {  
		mGestureDetector.onTouchEvent(ev);  
		// scroll.onTouchEvent(ev);  
		return super.dispatchTouchEvent(ev);  
	}  

	
	/**
	 * @e1 ���������ƶ��¼�  
	 * @e2 ��ǰ���Ƶ���ƶ��¼�
	 * @velocityX ÿ��x�᷽���ƶ�������
	 * @velocityY ÿ��y�᷽���ƶ�������
	 */
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {  //��������
			enterManuscriptTemplate();
		}else if (e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {  //��������
			
		}
		return false;  
	}

	public void onLongPress(MotionEvent e) {		
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,float distanceY) {
		return false;
	}

	public void onShowPress(MotionEvent e) {
	
	}

	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
	/*----------------------------------------------���津�����������¼� End-------------------------------------------------------*/
	
	/**
	 * ��ȡ��λ��Ϣ
	 */
	private void getLocation(){
		
		if(locationBoolean == false){
			PositionInfo positionInfo = null;
			String location = "0,0";
			try {
				positionInfo = locationHelper.getCurrentLocation();
				
				if(positionInfo.getLatitude() !=0 && positionInfo.getLongitude()!=0){
					location = String.valueOf(positionInfo.getLatitude()) + "," + String.valueOf(positionInfo.getLongitude());
				}
			} catch (Exception e) {
				Logger.e(e);
			}
			if(location != "0,0"){
				//�ҵ���ǰ�ľ�γ����Ϣ����ʾ��λͼ��
				//Toast.makeText(EditPicFManuscriptsActivity.this, location, Toast.LENGTH_SHORT).show();
				if(!TextUtils.isEmpty(positionInfo.getAddress())){
					showMessage(positionInfo.getAddress());
				}else{
					showMessage(location);
				}
				//iViewLocationStatus.setVisibility(View.VISIBLE);	
				
				picFlashManuscript.setLocation(location);
				//manuscriptsService.updateManuscripts(manuItem);
				locationBoolean=true;
				//��ͼ��任���Ѷ�λ��ͼ��
				iBtnGetLocationAdd.setBackgroundResource(R.drawable.got_location_960x540);
			}else{
				showMessage(getResources().getString(R.string.canNotGetLocation));	
			}
		}else{
			picFlashManuscript.setLocation("0,0");
			locationBoolean=false;
			//�Ѷ�λ��ͼ��任��ԭ����ͼ��
			iBtnGetLocationAdd.setBackgroundResource(R.drawable.get_location_960x540);
		}
	}
}
