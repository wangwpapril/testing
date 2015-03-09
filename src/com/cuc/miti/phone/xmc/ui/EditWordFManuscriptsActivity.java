package com.cuc.miti.phone.xmc.ui;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Enums.AccessoryType;
import com.cuc.miti.phone.xmc.domain.Enums.ManuscriptStatus;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceKeys;
import com.cuc.miti.phone.xmc.domain.Enums.TemplateType;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.domain.ManuscriptTemplate;
import com.cuc.miti.phone.xmc.domain.Manuscripts;
import com.cuc.miti.phone.xmc.domain.PositionInfo;
import com.cuc.miti.phone.xmc.domain.UploadTask;
import com.cuc.miti.phone.xmc.domain.User;
import com.cuc.miti.phone.xmc.logic.ManuscriptTemplateService;
import com.cuc.miti.phone.xmc.logic.ManuscriptsService;
import com.cuc.miti.phone.xmc.logic.UploadTaskService;
import com.cuc.miti.phone.xmc.utils.BaiduLocationHelper;
import com.cuc.miti.phone.xmc.utils.LocationHelper;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.ManuscriptIDCreator;
import com.cuc.miti.phone.xmc.utils.SendManuscriptsHelper;
import com.cuc.miti.phone.xmc.utils.SharedPreferencesHelper;
import com.cuc.miti.phone.xmc.utils.TimeFormatHelper;
import com.cuc.miti.phone.xmc.utils.ToastHelper;
import com.cuc.miti.phone.xmc.utils.TouchEffect;
import com.cuc.miti.phone.xmc.utils.XMLDataHandle;
import com.cuc.miti.phone.xmc.utils.uploadtask.FileUploadTaskHelper;
import com.cuc.miti.phone.xmc.utils.uploadtask.UploadTaskJob;
import com.cuc.miti.phone.xmc.R;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;

/**
 * ���ֿ�ѶActivity���û�����ҳ��������ֿ�Ѷ����ת����ҳ
 * ��Ҫ���ܣ����ֿ�Ѷ������ı༭����ǩ�鿴����ǩ�༭����ǩ����
 * @author SongQing
 *
 */
public class EditWordFManuscriptsActivity extends BaseActivity implements RecognizerDialogListener,OnTouchListener,OnGestureListener,OnClickListener,Serializable{

	private static final long serialVersionUID = 1L;
	private ImageView iViewTempletSwitcher;						//ģ���л�ImageView
	private ImageView iViewLocationStatus;							//��λ״̬ImageView
	private ImageButton iBtnBack;											//����ImageButton
	private ImageButton iBtnSendManu;								//����ImageButton
	private ImageButton iBtnSaveLocalization;						//���ر���ImageButton
	private ImageButton iBtnGetLocationAdd;						//��ȡ��ǰλ�þ�γ����ϢImageButton
	private Button btnTextCounter;										//�������ͳ��Button
	private EditText etTitle;														//�������
	private EditText etContent;												//�������
	private int wordsCounter = 0;											//����ͳ�Ʊ���
	private String currentUser ="";											//��ǰ�û�
	private Boolean locationBoolean =false;							//��¼��λ��

//	private LocationHelper locationHelper;	
	private BaiduLocationHelper locationHelper;
	private ManuscriptsService manuscriptsService = null;
	private ManuscriptTemplateService manuscriptTemplateService = null;
	private UploadTaskService uploadTaskService = null;
	private ManuscriptIDCreator manuscriptIDCreator = null;
	private Manuscripts manuItem;
	private ManuscriptTemplate manuscriptTemplate;
	private GestureDetector mGestureDetector;     							//�����¼�  
	
	//��������Ի���
	private RecognizerDialog iatDialog;
	private ImageButton iatButton;
	private SharedPreferences mSharedPreferences;
	
	// ���嶨ʱ��
	protected Timer timer = new Timer();
	private TimerTask task;
	
	private static final int REQUEST_EDIT_TEMPLATE = 0;								//�鿴�ͱ༭ģ��
	private static final int MESSAGE_AUTO_SAVE_MANUSCRIPT = 4;			//��ʱ����������
	protected boolean isSaved = false;															//����Ƿ��Ѿ������־λ
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ�������
		setContentView(R.layout.edit_word_f_manuscripts);	
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
	
	/**
	 * ��ʼ��
	 */
	private void initialize(){
		manuscriptsService = new ManuscriptsService(EditWordFManuscriptsActivity.this);
		manuscriptTemplateService = new ManuscriptTemplateService(EditWordFManuscriptsActivity.this);	
		manuscriptIDCreator = new ManuscriptIDCreator(EditWordFManuscriptsActivity.this);
		uploadTaskService = new UploadTaskService(EditWordFManuscriptsActivity.this);
		currentUser = IngleApplication.getInstance().getCurrentUser();
		
		this.initializeManuscript();							
		this.setUpViews();
		this.iniAutoSave();
		
	}
	/**
	 * ��ʼ��ҳ��ؼ�
	 */
	private void setUpViews(){	
		iViewLocationStatus = (ImageView)findViewById(R.id.iViewlocationStatus_EWordFM);
		iViewTempletSwitcher = (ImageView)findViewById(R.id.iViewTempletSwitcher_EWordFM);
		iBtnBack = (ImageButton)findViewById(R.id.iBtnBack_EWordFM);
		iBtnSendManu = (ImageButton)findViewById(R.id.iBtnSendManu_EWordFM);
		iBtnSaveLocalization = (ImageButton)findViewById(R.id.iBtnSaveLocalization_EWordFM);
		iBtnGetLocationAdd=(ImageButton)findViewById(R.id.iBtnGetLocationAdd_EWordFM);
		btnTextCounter = (Button)findViewById(R.id.btnManuTextCounter_EWordFM);
		btnTextCounter.setOnClickListener(this);
		btnTextCounter.setText("  "+ wordsCounter);
		
		etTitle = (EditText)findViewById(R.id.editTextManuTitle_EWordFM);
		etContent = (EditText)findViewById(R.id.editTextManuContent_EWordFM);	
		etContent.addTextChangedListener(new TextChangedWatcher());		
		
		etTitle.setText(manuscriptTemplate.getDefaulttitle());											//�Ӹ�ǩ�л�ȡ��Ĭ�ϱ���
		etContent.setText(manuscriptTemplate.getDefaultcontents());							//�Ӹ�ǩ�л�ȡ��Ĭ������
		
		iViewLocationStatus.setVisibility(View.INVISIBLE);			//Ĭ��Ϊ���ɼ�(�Ѿ�����)
		
		iViewTempletSwitcher.setOnClickListener(this);
		//iViewTempletSwitcher.setOnTouchListener(this);
		mGestureDetector = new GestureDetector((OnGestureListener) this);    
		iBtnBack.setOnClickListener(this);
		iBtnBack.setOnTouchListener(TouchEffect.TouchDark);
		iBtnSendManu.setOnClickListener(this);
		iBtnSendManu.setOnTouchListener(TouchEffect.TouchDark);
		iBtnGetLocationAdd.setOnClickListener(this);
		iBtnGetLocationAdd.setOnTouchListener(TouchEffect.TouchDark);
		iBtnSaveLocalization.setOnClickListener(this);
		iBtnSaveLocalization.setOnTouchListener(TouchEffect.TouchDark);

		
		// ��ʼ���������밴ť
		iatButton=(ImageButton) findViewById(R.id.iBtnVoice_EWordFM);
		iatButton.setOnClickListener(this);
		iatButton.setOnTouchListener(TouchEffect.TouchDark);
		// ��ʼ����������Ի���
		iatDialog = new RecognizerDialog(EditWordFManuscriptsActivity.this, "appid=" + getString(R.string.app_id));
		iatDialog.setListener(this);
		
		mSharedPreferences = this.getSharedPreferences(this.getPackageName(),this.MODE_PRIVATE);

		
	}
	
	/**
	 * ��ʼ����ǩģ����Ϣ
	 */
	private void initializeManuscriptTemplate(){
		//TODO  ��ȡ��ǩģ���ֵ(��ǩģ��Ӧ��ֱ��ȥĬ�ϵ����ֿ�Ѷģ��)����ȡ��ǰ�û��������ֵ����GroupName��
		manuscriptTemplate = new ManuscriptTemplate();	
		manuscriptTemplate = this.manuscriptTemplateService.getManuscriptTemplateSystem(this.currentUser, TemplateType.WORD.toString());
	}
	
	/**
	 * ��ʼ�������Ϣ(�½����_д����ݿ�)
	 */
	private void initializeManuscript(){
		
		try{
			manuItem = new Manuscripts();
			manuItem.setM_id(UUID.randomUUID().toString());					//���Ψһ�ı�ʶID
			
			//��ȡ��ǰ�û���Ϣ
			User user = IngleApplication.getInstance().currentUserInfo;
			manuItem.setGroupcode(user.getUserattribute().getGroupCode());
			manuItem.setGroupnameC(user.getUserattribute().getGroupNameC());
			manuItem.setGroupnameE(user.getUserattribute().getGroupNameE());
			manuItem.setUsernameC(user.getUserattribute().getUserNameC());
			manuItem.setUsernameE(user.getUserattribute().getUserNameE());
			manuItem.setLoginname(user.getUsername());
			
			initializeManuscriptTemplate();						//��ݿ�Ѷ���ͳ�ʼ����ǩģ��
			manuItem.setManuscriptTemplate(manuscriptTemplate);
			
			manuItem.setManuscriptsStatus(ManuscriptStatus.Editing);				//����"�ڱ�"״̬
			manuItem.setAuthor(manuscriptTemplate.getAuthor());
			
			manuItem.getManuscriptTemplate().setComefromDept(user.getUserattribute().getGroupNameC());
			manuItem.getManuscriptTemplate().setComefromDeptID(user.getUserattribute().getGroupCode());
			
				
			manuscriptsService.addManuscripts(manuItem);
			
		}catch(Exception e){
			Logger.e(e);
		}
		
	}
	
	/**
	 * ��ʼ���Զ����涨ʱ��
	 */
	private void iniAutoSave() {
		// ������ʱ������������ÿ��30�뱣��һ��
		task = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message message = new Message();
				message.what = MESSAGE_AUTO_SAVE_MANUSCRIPT;
				handler.sendMessage(message);
			}
		};

		// �������л�ȡ�Զ�����ʱ����
		SharedPreferencesHelper helper = new SharedPreferencesHelper(this);

		int interval = 0;
		try {
			String strInterval = helper.GetUserPreferenceValue(PreferenceKeys.User_AutoSaveInterval.toString());
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
				manuItem.setTitle(etTitle.getText().toString().trim());
				manuItem.setContents(etContent.getText().toString().trim());
				
				if(SendManuscriptsHelper.validateForAutoSave(manuItem, null)){
					saveManuscript(false);						//�Զ�����ʱ��һ����Ҫ�û��������
				}
			}
			super.handleMessage(msg);
		}
	};
	
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
		if(this.etTitle.getText().toString().trim().length() ==0){
			this.showMessage(getResources().getString(R.string.manuTitleNotNull));
			return false;
		}else{
			
			//�����뷢����ص���Ϣ,�緢��š�����ʱ���
			setStandToManuscriptInfo(manuItem);
			
			//������
			if(!saveManuscript(true)){
				return false;
			}
			
			KeyValueData message = new KeyValueData("","");
			//if(!SendManuscriptsHelper.validateForSendWithoutCheckNetWork(manuItem, message, this)){
			if(!SendManuscriptsHelper.validateForReleManuscript(manuItem, message, this)){
				this.showMessage(message.getValue());
				return false;
			}		
			
			//��Ӹ���ϴ����񣬲����±�����ݿ���״̬Ϊ"��"
			return this.upload();				
		}
	}
	
	/**
	 * �˳���ҳ��ʱִ�к���(������֤�����浱ǰ�༭���)
	 */
	private void exit(){
		
		manuItem.setTitle(this.etTitle.getText().toString().trim());
		manuItem.setContents(this.etContent.getText().toString().trim());
		
		//У��������Ϣ�Ƿ���Ҫ����
		boolean result = SendManuscriptsHelper.validateForBack(manuItem,null);					//���ֿ�Ѷ�޸���

		// �ж�������½�������£�û�б��⡢������ɾ��˸��
		if (result == false && isSaved == false) {
			try {
				manuscriptsService.deleteById(manuItem.getM_id());
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
	 * ����ǰ�����뷢����ص���Ϣ
	 * 
	 * @param mu
	 */
	private void setStandToManuscriptInfo(Manuscripts mu) {

		AccessoryType accType = AccessoryType.Text;
		mu.setNewstype(accType.getValue());
		mu.setNewstypeID(accType.toString());

		String createid = manuscriptIDCreator.generateCreateID(mu);
		mu.setCreateid(createid);
		mu.setReleid(createid);
		mu.setNewsid(createid);
		mu.setReletime(TimeFormatHelper.getFormatTime(new Date()));
		mu.setReceiveTime(TimeFormatHelper.getFormatTime(new Date()));
	}

	/**
	 * �����������(���ֿ�Ѷ����û�и��������Բ���Ҫ���и�����)
	 */
	private boolean upload(){
		try {
			String path = XMLDataHandle.Serializer(manuItem, null, manuItem.getReleid());
			
			final String[] urls = { null, path };
//			UploadJob uploadJob = new UploadJob(urls);
//			FileUploadHelper.addToUploads(uploadJob);	
		
			manuscriptsService.updateManuscripts(manuItem);
			
			UploadTask uploadTask = this.addUploadTask(urls);
			UploadTaskJob uploadTaskJob = new UploadTaskJob(uploadTask);
			
			FileUploadTaskHelper.addToUploads(uploadTaskJob);

			// �޸�״̬λ����Ϊ����
			manuscriptsService.StandTo(manuItem.getM_id());
			this.showMessage(getResources().getString(R.string.validateFinished));
			return true;
		} catch (Exception ex) {
			Logger.e(ex);
			this.showMessage(getResources().getString(R.string.manu_failtosend));
			return false;
		}
	}
	
	/**
	 * ����ʱ���һ���ϴ�������񲢲�����ݿ�
	 * @param urls
	 * @return
	 */
	private UploadTask addUploadTask(String[] urls){
		int utId = uploadTaskService.add(urls, manuItem.getM_id(), manuItem.getManuscriptTemplate().getPriorityID());		
		return uploadTaskService.get(utId);
	}
	
	/**
	 * ��������ݵ�������ݿ⣬,��ҪУ������true������ҪУ�鴫��false
	 * @return
	 */
	private boolean saveManuscript(boolean validate){
		try {
			manuItem.setTitle(this.etTitle.getText().toString().trim());
			manuItem.setContents(this.etContent.getText().toString().trim());
			
			if (validate == true) {
				KeyValueData message = new KeyValueData("", "");
				boolean validateResult = SendManuscriptsHelper.validataForSave(this.manuItem, message);

				if (validateResult == false) {
					showMessage(message.getValue());
					return false;
				}
			}
			
			manuItem.setManuscriptsStatus(ManuscriptStatus.Editing);
			
			if(manuscriptsService.updateManuscripts(manuItem)){
				isSaved = true;
				showMessage(getResources().getString(R.string.manuAlreadySaved));			//TODO ��ʻ�
				return true;
			}else{
				showMessage(getResources().getString(R.string.manuSavedError));				//TODO ��ʻ�
				return false;
			}			
		} catch (Exception e) {
			Logger.e(e);
			showMessage(getResources().getString(R.string.manuSavedError));				//TODO ��ʻ�
			return false;
		}
	}
	
	/**
	 * ��ʾ�����Ϣ
	 * 
	 * @param message
	 */
	private void showMessage(String message) {
		//Toast.makeText(EditWordFManuscriptsActivity.this, message, Toast.LENGTH_SHORT).show();
		ToastHelper.showToast(message,Toast.LENGTH_SHORT);
	}
	
	/**
	 * ҳ������Ӧ�¼�
	 */
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.iBtnBack_EWordFM:								//����
				exit();			
				break;
			case R.id.iBtnSaveLocalization_EWordFM:				//���ر���
				this.saveManuscript(true);
				break;
			case R.id.btnManuTextCounter_EWordFM:
				AlertDialog.Builder builder=new AlertDialog.Builder(EditWordFManuscriptsActivity.this);
		    	builder.setTitle(R.string.manuContent_delete_alert_title);
		    	builder.setMessage(R.string.manuContent_delete_alert_content);
		    	builder.setPositiveButton(R.string.confirm_button,new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface arg0, int arg1) {
						etContent.setText("");
					}
				});
		    	builder.setNegativeButton(R.string.cancel_button, null);
		    	builder.show();
		    	break;
			case R.id.iBtnGetLocationAdd_EWordFM:
				getLocation();
//				if(locationBoolean == false){
//					locationHelper = new LocationHelper(EditWordFManuscriptsActivity.this);
//					String location = locationHelper.GetLocation();
//					
//					if(location != "0,0"){
//						//�ҵ���ǰ�ľ�γ����Ϣ����ʾ��λͼ��
//						//Toast.makeText(EditWordFManuscriptsActivity.this, location, Toast.LENGTH_SHORT).show();
//						ToastHelper.showToast(location,Toast.LENGTH_SHORT);
//						//iViewLocationStatus.setVisibility(View.VISIBLE);	
//									
//						manuItem.setLocation(location);
//						//manuscriptsService.updateManuscripts(manuItem);
//						locationBoolean=true;
//						//��ͼ��任���Ѷ�λ��ͼ��
//						iBtnGetLocationAdd.setBackgroundResource(R.drawable.got_location_960x540);
//					}else{
//						showMessage(getResources().getString(R.string.canNotGetLocation));	
//					}
//				}else{
//					manuItem.setLocation("0,0");
//					locationBoolean=false;
//					//�Ѷ�λ��ͼ��任��ԭ����ͼ��
//					iBtnGetLocationAdd.setBackgroundResource(R.drawable.get_location_960x540);
//				}
				
				break;
//			case R.id.iBtnSaveOnline_EWordFM:
//				//TODO
//				this.showMessage("������...");
//				break;
			case R.id.iBtnVoice_EWordFM:					//�����������
				showIatDialog();
				break;
			case R.id.iBtnSendManu_EWordFM:
				if(sendManuscript()){
					setResult(RESULT_OK);
					this.finish();
				}
				break;					
			case R.id.iViewTempletSwitcher_EWordFM:
				enterManuscriptTemplate();
				break;
			default:
				break;
		}		
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Bundle mBundle = null;
		if (resultCode == Activity.RESULT_OK) {	
			switch (requestCode) {
			case REQUEST_EDIT_TEMPLATE:
				mBundle = data.getExtras();
				manuscriptTemplate = mBundle.getParcelable("manuTemplateInfo");
				manuItem.setManuscriptTemplate(manuscriptTemplate);
				manuItem.setAuthor(manuscriptTemplate.getAuthor());
				break;
			default:
				break;
			}
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
	 * ���EditText�е����ֱ仯
	 * @author SongQing
	 *
	 */
	class TextChangedWatcher implements TextWatcher{

		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			int number= s.length();

			btnTextCounter.setText("  "+number);
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	/**
	 * �����ǩҳ
	 */
	private void enterManuscriptTemplate(){
		Intent mIntent =null;
		mIntent = new Intent(EditWordFManuscriptsActivity.this,EditTemplateActivity.class);
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	/*----------------------------------------------���津�����������¼� End-------------------------------------------------------*/

	/*----------------------------------------------�������� Begin-------------------------------------------------------*/
	public void onResults(ArrayList<RecognizerResult> results, boolean isLast) {
		StringBuilder builder = new StringBuilder();
		for (RecognizerResult recognizerResult : results) {
			builder.append(recognizerResult.text);
		}
		int index = etContent.getSelectionStart();
		if(index<0||index>etContent.length())
			etContent.append(builder);
		else
			etContent.getEditableText().insert(index,builder);
	}
	
	public void showIatDialog()
	{
		String engine = mSharedPreferences.getString(
				getString(R.string.preference_key_iat_engine),
				getString(R.string.preference_default_iat_engine));

		String area = null;

		iatDialog.setEngine(engine, area, null);

		String rate = mSharedPreferences.getString(
				getString(R.string.preference_key_iat_rate),
				getString(R.string.preference_default_iat_rate));
		if(rate.equals("rate8k"))
			iatDialog.setSampleRate(RATE.rate8k);
		else if(rate.equals("rate11k"))
			iatDialog.setSampleRate(RATE.rate11k);
		else if(rate.equals("rate16k"))
			iatDialog.setSampleRate(RATE.rate16k);
		else if(rate.equals("rate22k"))
			iatDialog.setSampleRate(RATE.rate22k);
		iatDialog.show();
	}

	public void onEnd(SpeechError arg0) {
		
	}
	
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
				//Toast.makeText(EditWordFManuscriptsActivity.this, location, Toast.LENGTH_SHORT).show();
				if(!TextUtils.isEmpty(positionInfo.getAddress())){
					showMessage(positionInfo.getAddress());
				}else{
					showMessage(location);
				}
				//iViewLocationStatus.setVisibility(View.VISIBLE);	
							
				manuItem.setLocation(location);
				//manuscriptsService.updateManuscripts(manuItem);
				locationBoolean=true;
				//��ͼ��任���Ѷ�λ��ͼ��
				iBtnGetLocationAdd.setBackgroundResource(R.drawable.got_location_960x540);
			}else{
				showMessage(getResources().getString(R.string.canNotGetLocation));	
			}
		}else{
			manuItem.setLocation("0,0");
			locationBoolean=false;
			//�Ѷ�λ��ͼ��任��ԭ����ͼ��
			iBtnGetLocationAdd.setBackgroundResource(R.drawable.get_location_960x540);
		}
	}

}
