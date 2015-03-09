package com.cuc.miti.phone.xmc.ui;

import java.util.Date;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cuc.miti.phone.xmc.R;
import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Enums.BaseDataType;
import com.cuc.miti.phone.xmc.domain.Enums.TemplateType;
import com.cuc.miti.phone.xmc.domain.Enums.XmcBool;
import com.cuc.miti.phone.xmc.domain.ManuscriptTemplate;
import com.cuc.miti.phone.xmc.logic.ManuscriptTemplateService;
import com.cuc.miti.phone.xmc.utils.TimeFormatHelper;
import com.cuc.miti.phone.xmc.utils.ToastHelper;
import com.cuc.miti.phone.xmc.utils.TouchEffect;


public class EditTemplateActivity extends BaseActivity implements OnTouchListener,OnGestureListener,OnClickListener{
	
	private ImageButton iBtnBack,											//���˰�ť
								iBtnStoreTemplate,								//�����ǩģ�尴ť
								iBtnMultiSelectDepartment,					//��Դѡ��ť
								iBtnInternalInternational,						//�������ѡ��ť
								iBtnNewsCategory,								//����ѡ��ť
								iBtnGeographyCategory,						//����ѡ��ť
								iBtnSendArea,										//����ص�ѡ��ť
								iBtnHappenPlace,									//�·��ص�ѡ��ť
								iBtnReportPlace,									//�����ص�ѡ��ť
								iBtnKeywords,										//�ؼ���
								iBtnSingleSelectAuthor,							//����
								
								iBtnLanguage,										//����
								iBtnPriority,											//���ȼ�
								iBtnManuscriptsType,							//�������(3T��/δ����)
								iBtnSendToAddress;								//�����ַ
															
	private Button btnSelectContentAttributeTab,					//Tabҳ�л�_�����������
						btnSelectSendAttributeTab,							//Tabҳ�л�_�����������
						btnSaveAs,													//����ǩ
						btnAdoptTemplate;										//���ø�ǩ
	
	private ScrollView scrollViewContent,								//�����������ScrollView
							scrollViewSend;											//�����������ScrollView
	
	private ImageView iViewTempletSwitcher;						//��ǩ�л��ظ����ť(ֻ���ڸ���༭ʱ�ų���)
	
	private RelativeLayout rLayoutTemplateName,rLayoutTemplateDefaultTitle,rLayoutTemplateDefaultContent;	//ģ����ơ�Ĭ�ϱ��⡢Ĭ�����ģ���Ҫ������������������
	private View viewTemplateName,viewDefaultContent,viewDefaultTitle;
	
	private EditText etTemplateName,									//������ ->��������
						etTemplateAuthor,										//����->Ĭ�ϰ���Ӣ�����Ҽ���ɾ���޸�
						etTemplateDepartment,								//��Դ ->ListView��ѡ���ҽ�ֹ����¼�룬ֻ�� et.setKeyListener(null); 
						etTemplateInternalInternational,					//������� ->listView��ѡ����ֹ�������룬ֻ�� et.setKeyListener(null); 
						etTemplateNewsCategory,							//���� -> ��ѡ������ֹ�������룬ֻ�� et.setKeyListener(null); 
						etTemplateGeographyCategory,					//���� -> ��ѡ������ֹ�������룬ֻ�� et.setKeyListener(null); 
						etTemplateSendArea,									//����ص� ->ListView��ѡ��������������޸�(ѡ����Զ�Ϊ�·��ص�ͱ����ص㸳ֵ)
						etTemplateHappenPlace,								//�·��ص� ->ListView��ѡ��������������޸�
						etTemplateReportPlace,								//�����ص� ->ListView��ѡ��������������޸�
						etTemplateKeywords,									//�ؼ��� ->ListView��ѡ��������������޸�
						etTemplateReviewStatus,								//������� ->��������
						etTemplateDefaultTitle,									//Ĭ�ϱ���
						etTemplateDefaultContent,							//Ĭ������
					
						etTemplateLanguage,									//����
						etTemplatePriority,										//���ȼ�
						etTemplateManuscriptsType,							//�������(3T/δ����)
						etTemplateSendToAddress;							//�����ַ
	
	private TextView 	tvTemplateAuthor,									//����->Ĭ�ϰ���Ӣ�����Ҽ���ɾ���޸�
						tvTemplateDepartment,								//��Դ ->ListView��ѡ���ҽ�ֹ����¼�룬ֻ�� tv.setKeyListener(null); 
						tvTemplateInternalInternational,					//������� ->listView��ѡ����ֹ�������룬ֻ�� tv.setKeyListener(null); 
						tvTemplateNewsCategory,							//���� -> ��ѡ������ֹ�������룬ֻ�� tv.setKeyListener(null); 
						tvTemplateGeographyCategory,					//���� -> ��ѡ������ֹ�������룬ֻ�� tv.setKeyListener(null); 
						tvTemplateSendArea,									//����ص� ->ListView��ѡ��������������޸�(ѡ����Զ�Ϊ�·��ص�ͱ����ص㸳ֵ)
						tvTemplateHappenPlace,								//�·��ص� ->ListView��ѡ��������������޸�
						tvTemplateReportPlace,								//�����ص� ->ListView��ѡ��������������޸�
						tvTemplateKeywords,									//�ؼ��� ->ListView��ѡ��������������޸�
						tvTemplateLanguage,									//����
						tvTemplatePriority,										//���ȼ�
						tvTemplateManuscriptsType,							//�������(3T/δ����)
						tvTemplateSendToAddress,							//�����ַ
						tvTemplateDefaultTitle;									//Ĭ�ϱ���
	
	private EditText etTemplateNameForSaveAS;								// Dialog�������ģ�����
	private GestureDetector mGestureDetector;     							//�����¼�  
	private boolean onTouchKey = true;											//����OnTouch�Ƿ�Ҫ��������

	private static final int REQUEST_DEPARTMENT = 1;			
	private static final int REQUEST_INTERNALINTERNATIONAL = 2;
	private static final int REQUEST_NEWSCATEGORY = 3;
	private static final int REQUEST_GEOGRAPHYCATEGORY = 4;
	private static final int REQUEST_SENDAREA= 5;
	private static final int REQUEST_HAPPENPLACE = 6;
	private static final int REQUEST_REPORTPLACE = 7;
	private static final int REQUEST_KEYWORDS = 8;
	private static final int REQUEST_LANGUAGE = 9;
	private static final int REQUEST_PRIORITY = 10;
	private static final int REQUEST_MAUNSCRIPTSTYPE = 11;
	private static final int REQUEST_SENDTOADDRESS = 12;
	private static final int REQUEST_ADOPT_TEMPLATE = 13;
	private static final int REQUEST_AUTHOR = 14;
	
	private ManuscriptTemplateService mtService = new ManuscriptTemplateService(EditTemplateActivity.this);
	private ManuscriptTemplate mManuTemplate = null;
	private ManuscriptTemplate oManuTemplate = null;					//��ʼ�ĸ�ǩ���ݣ���Ҫ���������û��˳���ҳ�ǽ����Ƿ��������޸ĵ��ж�
	private int switcherUsing = View.VISIBLE;
	private static String currentUserName = "";
	private static String manageType = "";								//ר��������ʶ�½���ǩ��������֤switcher�����֣�����֧�Ÿ�ǩ�����߼���
	private static String editOrView="";									//������ʾ�ǲ鿴������Ǳ༭��ǩ���������ز�ͬ��ֵ���ϼ�ҳ�棬�Ӷ��ж��Ƿ���Ҫ����
			
	
	/* (non-Javadoc)
	 * @see com.cuc.miti.phone.xmc.ui.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_template);		
		this.initialize();		
		IngleApplication.getInstance().addActivity(this);

	}
	
	/* (non-Javadoc)
	 * @see com.cuc.miti.phone.xmc.ui.BaseActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	public void onClick(View v) {
		Intent mIntent = null;
		Bundle mBundle = null;
		String[] tempSelectedList;
		
		switch(v.getId()){
			case R.id.btnSelectContentAttributeTab_ETA:					//����������
				scrollViewContent.setVisibility(View.VISIBLE);
				scrollViewSend.setVisibility(View.INVISIBLE);
				btnSelectContentAttributeTab.setBackgroundResource(R.drawable.rounded_button_gray);
				btnSelectContentAttributeTab.setTextAppearance(EditTemplateActivity.this, R.style.template_tab_selected);
				btnSelectSendAttributeTab.setBackgroundResource(R.drawable.rounded_button_blue);
				btnSelectSendAttributeTab.setTextAppearance(EditTemplateActivity.this, R.style.template_tab_unselected);
				break;
			case R.id.btnSelectSendAttributeTab_ETA:							//�����������
				scrollViewContent.setVisibility(View.INVISIBLE);
				scrollViewSend.setVisibility(View.VISIBLE);
				btnSelectContentAttributeTab.setBackgroundResource(R.drawable.rounded_button_blue);
				btnSelectContentAttributeTab.setTextAppearance(EditTemplateActivity.this, R.style.template_tab_unselected);
				btnSelectSendAttributeTab.setBackgroundResource(R.drawable.rounded_button_gray);
				btnSelectSendAttributeTab.setTextAppearance(EditTemplateActivity.this, R.style.template_tab_selected);
				break;	
			case R.id.iBtnBack_ETA:														//�������
				back();
				//�����л�����������߽��룬�ұ��˳�
//				setResult(RESULT_CANCELED);
//				finish();	
				break;
			case R.id.iBtnStoreTemplate_ETA:										//��������ǩ
				store();
				break;
			case R.id.btnAdoptTemplate_ETA:										//������ø�ǩ
				mIntent = new Intent(EditTemplateActivity.this,ManagementTemplateActivity.class);				
				mBundle = new Bundle();
				
				mBundle.putString("requestType", "Adopt");
				mIntent.putExtras(mBundle);
				startActivityForResult(mIntent, REQUEST_ADOPT_TEMPLATE);
				break;
			case R.id.btnSaveAs_ETA:													//������
				DialogForSaveAs();
				
				break;	
			case R.id.textViewDepartment_ETA:
			case R.id.iBtnMultiSelectDepartment_ETA:							//��Դ ->ListView��ѡ���ҽ�ֹ����¼�룬ֻ�� et.setKeyListener(null);
				mIntent = new Intent(EditTemplateActivity.this,MultiSelectActivity.class);
				mBundle = new Bundle();
				mBundle.putString("listKey", BaseDataType.Department.toString());
				mBundle.putInt("selectMode", ListView.CHOICE_MODE_MULTIPLE);
				tempSelectedList = new String[2];
				if(this.etTemplateDepartment.getText().toString().trim() !=""){
					tempSelectedList[0] = this.etTemplateDepartment.getText().toString().trim();
					tempSelectedList[1] = this.etTemplateDepartment.getContentDescription().toString().trim();
				}
				mBundle.putCharSequenceArray("selected",tempSelectedList );
				mIntent.putExtras(mBundle);
				startActivityForResult(mIntent, REQUEST_DEPARTMENT);
				break;
			case R.id.textViewAuthor_ETA:
			case R.id.iBtnSingleSelectAuthor_ETA:					//����
				mIntent = new Intent(EditTemplateActivity.this,MultiSelectActivity.class);
				mBundle = new Bundle();
				mBundle.putString("listKey", BaseDataType.Author.toString());
				mBundle.putInt("selectMode", ListView.CHOICE_MODE_SINGLE);
				if(this.etTemplateAuthor.getText().toString().trim() !=""){
					mBundle.putString("selectName",  this.etTemplateAuthor.getText().toString().trim());
					mBundle.putString("selectId", this.etTemplateAuthor.getContentDescription().toString().trim());
				}
				mIntent.putExtras(mBundle);
				startActivityForResult(mIntent, REQUEST_AUTHOR);
				break;
			case R.id.textViewInternalInternational_ETA:
			case R.id.iBtnSingleSelectInternalInternational_ETA:			//������� ->listView��ѡ����ֹ�������룬ֻ�� et.setKeyListener(null); 
				mIntent = new Intent(EditTemplateActivity.this,MultiSelectActivity.class);
				mBundle = new Bundle();
				mBundle.putString("listKey", BaseDataType.InternalInternational.toString());
				mBundle.putInt("selectMode", ListView.CHOICE_MODE_SINGLE);
				if(this.etTemplateInternalInternational.getText().toString().trim() !=""){
					mBundle.putString("selectName",  this.etTemplateInternalInternational.getText().toString().trim());
					mBundle.putString("selectId", this.etTemplateInternalInternational.getContentDescription().toString().trim());
				}
				mIntent.putExtras(mBundle);
				startActivityForResult(mIntent, REQUEST_INTERNALINTERNATIONAL);
				break;
			case R.id.textViewNewsCategory_ETA:
			case R.id.iBtnSingleSelectNewsCategory_ETA:					//���� -> ��ѡ������ֹ�������룬ֻ�� et.setKeyListener(null); 
				//mIntent = new Intent(EditTemplateActivity.this,TreeSelectActivity.class);
				//�ĳɵݽ�ʽ�����ṹ
				mIntent = new Intent(EditTemplateActivity.this,TreeSelectStyle2Activity.class);
				mBundle = new Bundle();
				mBundle.putString("listKey", BaseDataType.NewsCategory.toString());			
				mIntent.putExtras(mBundle);
				startActivityForResult(mIntent, REQUEST_NEWSCATEGORY);
				break;
			case R.id.textViewGeographyCategory_ETA:
			case R.id.iBtnSingleSelectGeographtCategory_ETA:			//���� -> ��ѡ������ֹ�������룬ֻ�� et.setKeyListener(null); 
				//mIntent = new Intent(EditTemplateActivity.this,TreeSelectActivity.class);
				//�ĳɵݽ�ʽ�����ṹ
				mIntent = new Intent(EditTemplateActivity.this,TreeSelectStyle2Activity.class);
				mBundle = new Bundle();
				mBundle.putString("listKey", BaseDataType.GeographyCategory.toString());			
				mIntent.putExtras(mBundle);
				startActivityForResult(mIntent, REQUEST_GEOGRAPHYCATEGORY);
				break;
			case R.id.textViewSendArea_ETA:
			case R.id.iBtnSingleSelectSendArea_ETA:							//����ص� ->ListView��ѡ��������������޸�(ѡ����Զ�Ϊ�·��ص�ͱ����ص㸳ֵ)
				mIntent = new Intent(EditTemplateActivity.this,MultiSelectActivity.class);
				mBundle = new Bundle();
				mBundle.putString("listKey", BaseDataType.SendArea.toString());
				mBundle.putInt("selectMode", ListView.CHOICE_MODE_SINGLE);
				if(this.etTemplateSendArea.getText().toString().trim() !=""){
					mBundle.putString("selectName",  this.etTemplateSendArea.getText().toString().trim());
					mBundle.putString("selectId", this.etTemplateSendArea.getContentDescription().toString().trim());
				}
				mIntent.putExtras(mBundle);
				startActivityForResult(mIntent, REQUEST_SENDAREA);
				break;
			case R.id.textViewHappenPlace_ETA:
			case R.id.iBtnSingleSelectHappenPlace_ETA:						//�·��ص� ->ListView��ѡ��������������޸�
				mIntent = new Intent(EditTemplateActivity.this,MultiSelectActivity.class);
				mBundle = new Bundle();
				mBundle.putString("listKey", BaseDataType.HappenPlace.toString());
				mBundle.putInt("selectMode", ListView.CHOICE_MODE_SINGLE);
				if(this.etTemplateHappenPlace.getText().toString().trim() !=""){
					mBundle.putString("selectName",  this.etTemplateHappenPlace.getText().toString().trim());
					mBundle.putString("selectId", this.etTemplateHappenPlace.getContentDescription().toString().trim());
				}
				mIntent.putExtras(mBundle);
				startActivityForResult(mIntent, REQUEST_HAPPENPLACE);
				break;
			case R.id.textViewReportPlace_ETA:
			case R.id.iBtnSingleSelectReportPlace_ETA:						//�����ص� ->ListView��ѡ��������������޸�
				mIntent = new Intent(EditTemplateActivity.this,MultiSelectActivity.class);
				mBundle = new Bundle();
				mBundle.putString("listKey", BaseDataType.ReportPlace.toString());
				mBundle.putInt("selectMode", ListView.CHOICE_MODE_SINGLE);
				if(this.etTemplateReportPlace.getText().toString().trim() !=""){
					mBundle.putString("selectName",  this.etTemplateReportPlace.getText().toString().trim());
					mBundle.putString("selectId", this.etTemplateReportPlace.getContentDescription().toString().trim());
				}
				mIntent.putExtras(mBundle);
				startActivityForResult(mIntent, REQUEST_REPORTPLACE);
				break;
			case R.id.textViewKeywords_ETA:
			case R.id.iBtnSingleSelectKeywords_ETA:							//�ؼ��� ->ListView��ѡ��������������޸�
				mIntent = new Intent(EditTemplateActivity.this,MultiSelectActivity.class);
				mBundle = new Bundle();
				mBundle.putString("listKey", BaseDataType.Keywords.toString());
				mBundle.putInt("selectMode", ListView.CHOICE_MODE_SINGLE);
				if(this.etTemplateKeywords.getText().toString().trim() !=""){
					mBundle.putString("selectName",  this.etTemplateKeywords.getText().toString().trim());
					mBundle.putString("selectId", this.etTemplateKeywords.getContentDescription().toString().trim());
				}
				mIntent.putExtras(mBundle);
				startActivityForResult(mIntent, REQUEST_KEYWORDS);
				break;
			case R.id.textViewLanguage_ETA:
			case R.id.iBtnSingleSelectLanguage_ETA:							//���� ->ListView��ѡ
				mIntent = new Intent(EditTemplateActivity.this,MultiSelectActivity.class);
				mBundle = new Bundle();
				mBundle.putString("listKey", BaseDataType.Language.toString());
				mBundle.putInt("selectMode", ListView.CHOICE_MODE_SINGLE);
				if(this.etTemplateLanguage.getText().toString().trim() !=""){
					mBundle.putString("selectName",  this.etTemplateLanguage.getText().toString().trim());
					mBundle.putString("selectId", this.etTemplateLanguage.getContentDescription().toString().trim());
				}
				mIntent.putExtras(mBundle);
				startActivityForResult(mIntent, REQUEST_LANGUAGE);
				break;
			case R.id.textViewPriority_ETA:
			case R.id.iBtnSingleSelectPriority_ETA:								//���ȼ� ->ListView��ѡ
				mIntent = new Intent(EditTemplateActivity.this,MultiSelectActivity.class);
				mBundle = new Bundle();
				mBundle.putString("listKey", BaseDataType.Priority.toString());
				mBundle.putInt("selectMode", ListView.CHOICE_MODE_SINGLE);
				if(this.etTemplatePriority.getText().toString().trim() !=""){
					mBundle.putString("selectName",  this.etTemplatePriority.getText().toString().trim());
					mBundle.putString("selectId", this.etTemplatePriority.getContentDescription().toString().trim());
				}
				mIntent.putExtras(mBundle);
				startActivityForResult(mIntent, REQUEST_PRIORITY);
				break;
			case R.id.textViewManuscriptsType_ETA:
			case R.id.iBtnSingleSelectManuscriptsType_ETA:				//������� ->ListView��ѡ   
				mIntent = new Intent(EditTemplateActivity.this,MultiSelectActivity.class);
				mBundle = new Bundle();
				mBundle.putString("listKey", BaseDataType.ManuscriptsType.toString());
				mBundle.putInt("selectMode", ListView.CHOICE_MODE_SINGLE);
				if(this.etTemplateManuscriptsType.getText().toString().trim() !=""){
					mBundle.putString("selectName",  this.etTemplateManuscriptsType.getText().toString().trim());
					mBundle.putString("selectId", this.etTemplateManuscriptsType.getContentDescription().toString().trim());
				}
				mIntent.putExtras(mBundle);
				startActivityForResult(mIntent, REQUEST_MAUNSCRIPTSTYPE);
				break;
			case R.id.textViewSendToAddress_ETA:
			case R.id.iBtnMultiSelectSendToAddress_ETA:					//�����ַ ->ListView��ѡ
				mIntent = new Intent(EditTemplateActivity.this,MultiSelectActivity.class);
				mBundle = new Bundle();
				mBundle.putString("listKey", BaseDataType.CustomReleAddress.toString());
				mBundle.putInt("selectMode", ListView.CHOICE_MODE_MULTIPLE);
				mBundle.putString("mtType", mManuTemplate.getIssystemoriginal());
				tempSelectedList = new String[2];
				if(this.etTemplateSendToAddress.getText().toString().trim() !=""){
					tempSelectedList[0] = this.etTemplateSendToAddress.getText().toString().trim();
					tempSelectedList[1] = this.etTemplateSendToAddress.getContentDescription().toString().trim();
				}
				mBundle.putCharSequenceArray("selected",tempSelectedList );
				mIntent.putExtras(mBundle);
				startActivityForResult(mIntent, REQUEST_SENDTOADDRESS);
				break;
			case R.id.iViewTempletSwitcher_ETA:
				mIntent = new Intent();
				mBundle = new Bundle();
				mBundle.putParcelable("manuTemplateInfo", this.getManuscriptTemplate());
				mBundle.putString("EditOrView", editOrView);
				mIntent.putExtras(mBundle);				
				setResult(Activity.RESULT_OK, mIntent); //���÷������
				finish();
				//�����л�����������߽��룬�ұ��˳�
				overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
				break;
			default :			
				break;
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == Activity.RESULT_OK) {	
			Bundle mBundle = data.getExtras();
			switch(requestCode){
				case REQUEST_DEPARTMENT:
					if (mBundle != null) {
						String[] selected = (String[]) mBundle.getCharSequenceArray("selected");
						if(selected[0] != null){
							this.etTemplateDepartment.setText(selected[0]);
							this.etTemplateDepartment.setContentDescription(selected[1]);
						}else{
							this.etTemplateDepartment.setText("");
							this.etTemplateDepartment.setContentDescription("");
						}
					}
					break;
				case REQUEST_SENDTOADDRESS:
					if (mBundle != null) {
						String[] selected = (String[]) mBundle.getCharSequenceArray("selected");
						if(selected[0] != null){
							this.etTemplateSendToAddress.setText(selected[0]);
							this.etTemplateSendToAddress.setContentDescription(selected[1]);
						}else{
							this.etTemplateSendToAddress.setText("");
							this.etTemplateSendToAddress.setContentDescription("");
						}
					}
					break;
				case REQUEST_INTERNALINTERNATIONAL:
					if (mBundle != null) {
						String name = mBundle.get("selectName").toString();
						String id = mBundle.get("selectId").toString();
						if(name != null){
							this.etTemplateInternalInternational.setText(name);
							this.etTemplateInternalInternational.setContentDescription(id);
						}else{
							this.etTemplateInternalInternational.setText("");
							this.etTemplateInternalInternational.setContentDescription("");
						}
					}
					break;
				case REQUEST_PRIORITY:
					if (mBundle != null) {
						String name = mBundle.get("selectName").toString();
						String id = mBundle.get("selectId").toString();
						if(name != null){
							this.etTemplatePriority.setText(name);
							this.etTemplatePriority.setContentDescription(id);
						}else{
							this.etTemplatePriority.setText("");
							this.etTemplatePriority.setContentDescription("");
						}
					}
					break;
				case REQUEST_MAUNSCRIPTSTYPE:
					if (mBundle != null) {
						String name = mBundle.get("selectName").toString();
						String id = mBundle.get("selectId").toString();
						if(name != null){
							this.etTemplateManuscriptsType.setText(name);
							this.etTemplateManuscriptsType.setContentDescription(id);
						}else{
							this.etTemplateManuscriptsType.setText("");
							this.etTemplateManuscriptsType.setContentDescription("");
						}
					}
					break;
					
				case REQUEST_NEWSCATEGORY:
					if (mBundle != null) {
						String name = mBundle.get("selectName").toString();
						String id = mBundle.get("selectId").toString();
						if(name != null){
							this.etTemplateNewsCategory.setText(name);
							this.etTemplateNewsCategory.setContentDescription(id);
						}else{
							this.etTemplateNewsCategory.setText("");
							this.etTemplateNewsCategory.setContentDescription("");
						}
					}
					break;
				case REQUEST_GEOGRAPHYCATEGORY:
					if (mBundle != null) {
						String name = mBundle.get("selectName").toString();
						String id = mBundle.get("selectId").toString();
						if(name != null){
							this.etTemplateGeographyCategory.setText(name);
							this.etTemplateGeographyCategory.setContentDescription(id);
						}else{
							this.etTemplateGeographyCategory.setText("");
							this.etTemplateGeographyCategory.setContentDescription("");
						}
					}
					break;
				case REQUEST_LANGUAGE:
					if (mBundle != null) {
						String name = mBundle.get("selectName").toString();
						String id = mBundle.get("selectId").toString();
						if(name != null){
							this.etTemplateLanguage.setText(name);
							this.etTemplateLanguage.setContentDescription(id);
						}else{
							this.etTemplateLanguage.setText("");
							this.etTemplateLanguage.setContentDescription("");
						}
					}
					break;
				case REQUEST_KEYWORDS:
					if (mBundle != null) {
						String name = mBundle.get("selectName").toString();
						String id = mBundle.get("selectId").toString();
						if(name != null){
							this.etTemplateKeywords.setText(name);
							this.etTemplateKeywords.setContentDescription(id);
						}else{
							this.etTemplateKeywords.setText("");
							this.etTemplateKeywords.setContentDescription("");
						}
					}
					break;
				case REQUEST_SENDAREA:
					if (mBundle != null) {
						String name = mBundle.get("selectName").toString();
						String id = mBundle.get("selectId").toString();
						if(name != null){//�����ַ���·���ַ��������ַһ�㶼��һ��ģ�����Ĭ�����һ��ѡ��
							this.etTemplateSendArea.setText(name);
							this.etTemplateSendArea.setContentDescription(id);
							if(this.etTemplateHappenPlace.getText().toString().trim().equals("")){
								this.etTemplateHappenPlace.setText(name);
								this.etTemplateHappenPlace.setContentDescription(id);
							}
							if(this.etTemplateReportPlace.getText().toString().trim().equals("")){
								this.etTemplateReportPlace.setText(name);
								this.etTemplateReportPlace.setContentDescription(id);
							}
						}else{
							this.etTemplateSendArea.setText("");
							this.etTemplateSendArea.setContentDescription("");
							if(this.etTemplateHappenPlace.getText().toString().trim().equals("")){
								this.etTemplateHappenPlace.setText(name);
								this.etTemplateHappenPlace.setText("");
							}
							if(this.etTemplateReportPlace.getText().toString().trim().equals("")){
								this.etTemplateReportPlace.setContentDescription("");
								this.etTemplateReportPlace.setText("");
							}
						}
					}
					break;
				case REQUEST_HAPPENPLACE:
					if (mBundle != null) {
						String name = mBundle.get("selectName").toString();
						String id = mBundle.get("selectId").toString();
						if(name != null){
							this.etTemplateHappenPlace.setText(name);
							this.etTemplateHappenPlace.setContentDescription(id);

						}else{
							this.etTemplateHappenPlace.setText("");
							this.etTemplateHappenPlace.setContentDescription("");

						}
					}
					break;
				case REQUEST_REPORTPLACE:
					if (mBundle != null) {
						String name = mBundle.get("selectName").toString();
						String id = mBundle.get("selectId").toString();
						if(name != null){
							this.etTemplateReportPlace.setText(name);
							this.etTemplateReportPlace.setContentDescription(id);
						}else{
							this.etTemplateReportPlace.setText("");
							this.etTemplateReportPlace.setContentDescription("");
						}
					}
					break;
				case REQUEST_AUTHOR:
					if (mBundle != null) {
						String name = mBundle.get("selectName").toString();
						String id = mBundle.get("selectId").toString();
						if(name != null){
							this.etTemplateAuthor.setText(name);
							this.etTemplateAuthor.setContentDescription(id);
						}else{
							this.etTemplateAuthor.setText("");
							this.etTemplateAuthor.setContentDescription("");
						}
					}
					break;
				case REQUEST_ADOPT_TEMPLATE:
					if(mBundle!=null){
						this.initializeTemplateValueIfExist(mBundle);
					}
					break;
				default :
					break;
			}
		}		
	}

	/**
	 * ��ʼ��
	 */
	private void initialize(){
		currentUserName = IngleApplication.getInstance().getCurrentUser();
		this.setUpViews();
		this.initializeTabLayout();
		this.initializeTemplateValueIfExist(this.getIntentExtras());
		this.setControlValue();
	}
	
	/**
	 * ��ȡ���ϼ�Activity�������Ĳ���
	 */
	private Bundle getIntentExtras(){
		return this.getIntent().getExtras();
	}
	
	/** 
	 * ��ʼ��ҳ��ؼ�
	 */
	private void setUpViews(){
		
		rLayoutTemplateName =(RelativeLayout)findViewById(R.id.relativeLayoutTemplateName_ETA);
		rLayoutTemplateDefaultTitle = (RelativeLayout)findViewById(R.id.relativeLayoutDefaultTitle_ETA);
		rLayoutTemplateDefaultContent = (RelativeLayout)findViewById(R.id.relativeLayoutDefaultContent_ETA);
		viewTemplateName = (View)findViewById(R.id.viewTemplateName_ETA);
		viewDefaultContent = (View)findViewById(R.id.viewDefaultContent_ETA);
		viewDefaultTitle = (View)findViewById(R.id.viewDefaultTitle_ETA);
		
		etTemplateName = (EditText)findViewById(R.id.editTextTemplateName_ETA);										
		etTemplateAuthor = (EditText)findViewById(R.id.editTextAuthor_ETA);			
		
		etTemplateDepartment = (EditText)findViewById(R.id.editTextDepartment_ETA);
		etTemplateDepartment.setKeyListener(null);	
		etTemplateInternalInternational = (EditText)findViewById(R.id.editTextInternalInternational_ETA);												
		etTemplateInternalInternational.setKeyListener(null);		
		etTemplateNewsCategory = (EditText)findViewById(R.id.editTextNewsCategory_ETA);					
		etTemplateNewsCategory.setKeyListener(null);		
		etTemplateGeographyCategory = (EditText)findViewById(R.id.editTextGeographyCategory_ETA);		
		etTemplateGeographyCategory.setKeyListener(null);
		
		etTemplateSendArea = (EditText)findViewById(R.id.editTextSendArea_ETA);															
		etTemplateHappenPlace = (EditText)findViewById(R.id.editTextHappenPlace_ETA);															
		etTemplateReportPlace = (EditText)findViewById(R.id.editTextReportPlace_ETA);													
		etTemplateKeywords = (EditText)findViewById(R.id.editTextKeywords_ETA);														
		etTemplateReviewStatus = (EditText)findViewById(R.id.editTextReviewStatus_ETA);													
		etTemplateDefaultTitle = (EditText)findViewById(R.id.editTextDefaultTitle_ETA);												
		etTemplateDefaultContent = (EditText)findViewById(R.id.editTextDefaultContent_ETA);	
		
		etTemplateLanguage = (EditText)findViewById(R.id.editTextLanguage_ETA);	
		etTemplateLanguage.setKeyListener(null);
		etTemplatePriority = (EditText)findViewById(R.id.editTextPriority_ETA);
		etTemplatePriority.setKeyListener(null);
		etTemplateManuscriptsType = (EditText)findViewById(R.id.editTextManuscriptsType_ETA);
		etTemplateManuscriptsType.setKeyListener(null);
		etTemplateSendToAddress = (EditText)findViewById(R.id.editTextSendToAddress_ETA);
		etTemplateSendToAddress.setKeyListener(null);
		
		tvTemplateAuthor = (TextView)findViewById(R.id.textViewAuthor_ETA);
		tvTemplateAuthor.setOnClickListener(this);
		tvTemplateDepartment = (TextView)findViewById(R.id.textViewDepartment_ETA);
		tvTemplateDepartment.setOnClickListener(this);
		tvTemplateInternalInternational = (TextView)findViewById(R.id.textViewInternalInternational_ETA);
		tvTemplateInternalInternational.setOnClickListener(this);
		tvTemplateNewsCategory = (TextView)findViewById(R.id.textViewNewsCategory_ETA);
		tvTemplateNewsCategory.setOnClickListener(this);
		tvTemplateGeographyCategory = (TextView)findViewById(R.id.textViewGeographyCategory_ETA);
		tvTemplateGeographyCategory.setOnClickListener(this);
		tvTemplateSendArea = (TextView)findViewById(R.id.textViewSendArea_ETA);
		tvTemplateSendArea.setOnClickListener(this);
		tvTemplateHappenPlace = (TextView)findViewById(R.id.textViewHappenPlace_ETA);
		tvTemplateHappenPlace.setOnClickListener(this);
		tvTemplateReportPlace = (TextView)findViewById(R.id.textViewReportPlace_ETA);
		tvTemplateReportPlace.setOnClickListener(this);
		tvTemplateKeywords = (TextView)findViewById(R.id.textViewKeywords_ETA);
		tvTemplateKeywords.setOnClickListener(this);
		tvTemplateLanguage = (TextView)findViewById(R.id.textViewLanguage_ETA);
		tvTemplateLanguage.setOnClickListener(this);
		tvTemplatePriority = (TextView)findViewById(R.id.textViewPriority_ETA);
		tvTemplatePriority.setOnClickListener(this);
		tvTemplateManuscriptsType = (TextView)findViewById(R.id.textViewManuscriptsType_ETA);
		tvTemplateManuscriptsType.setOnClickListener(this);
		tvTemplateSendToAddress = (TextView)findViewById(R.id.textViewSendToAddress_ETA);
		tvTemplateSendToAddress.setOnClickListener(this);
		tvTemplateDefaultTitle = (TextView)findViewById(R.id.textViewDefaultTitle_ETA);
				
		iBtnBack = (ImageButton)findViewById(R.id.iBtnBack_ETA);
		iBtnBack.setOnClickListener(this);
		iBtnBack.setOnTouchListener(TouchEffect.TouchDark);
		iBtnStoreTemplate = (ImageButton)findViewById(R.id.iBtnStoreTemplate_ETA);			
		iBtnStoreTemplate.setOnClickListener(this);
		iBtnStoreTemplate.setOnTouchListener(TouchEffect.TouchDark);
		iBtnMultiSelectDepartment = (ImageButton)findViewById(R.id.iBtnMultiSelectDepartment_ETA);	
		iBtnMultiSelectDepartment.setOnClickListener(this);
		iBtnMultiSelectDepartment.setOnTouchListener(TouchEffect.TouchDark);
		iBtnSingleSelectAuthor = (ImageButton)findViewById(R.id.iBtnSingleSelectAuthor_ETA);
		iBtnSingleSelectAuthor.setOnClickListener(this);
		iBtnSingleSelectAuthor.setOnTouchListener(TouchEffect.TouchDark);
		iBtnInternalInternational = (ImageButton)findViewById(R.id.iBtnSingleSelectInternalInternational_ETA);		
		iBtnInternalInternational.setOnClickListener(this);
		iBtnInternalInternational.setOnTouchListener(TouchEffect.TouchDark);
		iBtnNewsCategory = (ImageButton)findViewById(R.id.iBtnSingleSelectNewsCategory_ETA);
		iBtnNewsCategory.setOnClickListener(this);
		iBtnNewsCategory.setOnTouchListener(TouchEffect.TouchDark);
		iBtnGeographyCategory = (ImageButton)findViewById(R.id.iBtnSingleSelectGeographtCategory_ETA);
		iBtnGeographyCategory.setOnClickListener(this);
		iBtnGeographyCategory.setOnTouchListener(TouchEffect.TouchDark);
		iBtnSendArea = (ImageButton)findViewById(R.id.iBtnSingleSelectSendArea_ETA);	
		iBtnSendArea.setOnClickListener(this);
		iBtnSendArea.setOnTouchListener(TouchEffect.TouchDark);
		iBtnHappenPlace = (ImageButton)findViewById(R.id.iBtnSingleSelectHappenPlace_ETA);		
		iBtnHappenPlace.setOnClickListener(this);
		iBtnHappenPlace.setOnTouchListener(TouchEffect.TouchDark);
		iBtnReportPlace = (ImageButton)findViewById(R.id.iBtnSingleSelectReportPlace_ETA);
		iBtnReportPlace.setOnClickListener(this);
		iBtnReportPlace.setOnTouchListener(TouchEffect.TouchDark);
		iBtnKeywords = (ImageButton)findViewById(R.id.iBtnSingleSelectKeywords_ETA);
		iBtnKeywords.setOnClickListener(this);
		iBtnKeywords.setOnTouchListener(TouchEffect.TouchDark);
		
		iBtnLanguage= (ImageButton)findViewById(R.id.iBtnSingleSelectLanguage_ETA);
		iBtnLanguage.setOnClickListener(this);
		iBtnLanguage.setOnTouchListener(TouchEffect.TouchDark);
		iBtnPriority= (ImageButton)findViewById(R.id.iBtnSingleSelectPriority_ETA);
		iBtnPriority.setOnClickListener(this);
		iBtnPriority.setOnTouchListener(TouchEffect.TouchDark);
		iBtnManuscriptsType= (ImageButton)findViewById(R.id.iBtnSingleSelectManuscriptsType_ETA);
		iBtnManuscriptsType.setOnClickListener(this);
		iBtnManuscriptsType.setOnTouchListener(TouchEffect.TouchDark);
		iBtnSendToAddress= (ImageButton)findViewById(R.id.iBtnMultiSelectSendToAddress_ETA);
		iBtnSendToAddress.setOnClickListener(this);
		iBtnSendToAddress.setOnTouchListener(TouchEffect.TouchDark);
		
		btnSelectContentAttributeTab = (Button)findViewById(R.id.btnSelectContentAttributeTab_ETA);
		btnSelectContentAttributeTab.setOnClickListener(this);
		btnSelectSendAttributeTab = (Button)findViewById(R.id.btnSelectSendAttributeTab_ETA);
		btnSelectSendAttributeTab.setOnClickListener(this);
		btnSaveAs = (Button)findViewById(R.id.btnSaveAs_ETA);
		btnSaveAs.setOnClickListener(this);
		btnAdoptTemplate = (Button)findViewById(R.id.btnAdoptTemplate_ETA);
		btnAdoptTemplate.setOnClickListener(this);
		
		scrollViewContent = (ScrollView)findViewById(R.id.scrollViewContentAttribute_ETA);
		scrollViewSend = (ScrollView)findViewById(R.id.scrollViewSendAttribute_ETA);
		
		mGestureDetector = new GestureDetector((OnGestureListener) this);    
		iViewTempletSwitcher = (ImageView)findViewById(R.id.iViewTempletSwitcher_ETA);
		iViewTempletSwitcher.setOnClickListener(this);	
	}
	
	/**
	 * ��ʼ������ҳ�����ʱĬ����"content"Tabѡ��
	 */
	private void initializeTabLayout(){
		scrollViewContent.setVisibility(View.VISIBLE);
		scrollViewSend.setVisibility(View.INVISIBLE);
		btnSelectContentAttributeTab.setBackgroundResource(R.drawable.rounded_button_gray);
		btnSelectContentAttributeTab.setTextAppearance(EditTemplateActivity.this, R.style.template_tab_selected);
		btnSelectSendAttributeTab.setBackgroundResource(R.drawable.rounded_button_blue);
		btnSelectSendAttributeTab.setTextAppearance(EditTemplateActivity.this, R.style.template_tab_unselected);
	}
	
	/**
	 * �༭��ǩʱ����Ҫ����ݿ��б���Ķ�Ӧ��ǩ��ݳ�ʼ��ҳ��ؼ���ֵ
	 */
	private void initializeTemplateValueIfExist(Bundle mBundle){
		switcherUsing = mBundle.getInt("switcherUsing");			//���û�д�ֵ����Ĭ�Ϸ���0���൱��View.VISIABLE��ֵ
		String mtId = mBundle.getString("mtId");
		Parcelable p = mBundle.getParcelable("manuTemplateInfo");
		iViewTempletSwitcher.setVisibility(switcherUsing);
		//iViewTempletSwitcher.setVisibility(View.GONE);
		if(switcherUsing == View.INVISIBLE){			//����л���ť����Ҫʹ�ã�������Ҳ��Ҫ��ֹ
			onTouchKey = false;
		}
		if(manageType.equals("New")){
			iViewTempletSwitcher.setVisibility(View.INVISIBLE);
		}
		
		String requestType = mBundle.getString("requestType");
		if(requestType.equals("Manage")){			//�ӹ���ҳ�����
			if(mtId !=null){//��ǩ����ҳ�ı༭��ǩ����(����Ҫ�������ð�ť)
				btnAdoptTemplate.setVisibility(View.GONE);
				mtService = new ManuscriptTemplateService(EditTemplateActivity.this);
				mManuTemplate = mtService.getManuscriptTemplateById(mtId);
				//Added By SongQing �ж����Ϊϵͳ��ǩ�������޸ĸ�ǩ���
				if(!mManuTemplate.getIssystemoriginal().equals(TemplateType.NORMAL.toString())){
					etTemplateName.setOnLongClickListener(null);
					etTemplateName.setKeyListener(null);
					etTemplateName.setCursorVisible(false);
				}
				
				oManuTemplate = mtService.getManuscriptTemplateById(mtId);
				this.setControlValue();
				manageType="";
			}else{		//�½���ǩ
				mManuTemplate = new ManuscriptTemplate();
				newManuscriptTemplateObject();
				manageType = "New";			//�������½���ǩ����
				oManuTemplate = new ManuscriptTemplate();
			}
			editOrView = "Edit";
		}else if(requestType.equals("ManuscriptEdit")){			//�Ӹ��ҳ�����༭
			if(p==null){
				newManuscriptTemplateObject();	
			}
			else{
				mManuTemplate = (ManuscriptTemplate)p;
				this.setControlValue();	
			}
			iBtnStoreTemplate.setVisibility(View.INVISIBLE);
			
			rLayoutTemplateName.setVisibility(View.GONE);
			viewTemplateName.setVisibility(View.GONE);
			rLayoutTemplateDefaultTitle.setVisibility(View.GONE);
			viewDefaultTitle.setVisibility(View.GONE);
			rLayoutTemplateDefaultContent.setVisibility(View.GONE);
			viewDefaultContent.setVisibility(View.GONE);
			editOrView = "Edit";
		}else if(requestType.equals("Adopt")){					//��ǩ����
			//btnAdoptTemplate.setVisibility(View.GONE);
			//btnSaveAs.setVisibility(View.GONE);
			//Added By SongQing.2013.11.26 ����½���ǩ �����ã�Ȼ��������ҳ�������ذ�ť�޷�Ӧ���⡣
			if(mtId.equals("0")){
				return;
			}
			mtService = new ManuscriptTemplateService(EditTemplateActivity.this);
			mManuTemplate = mtService.getManuscriptTemplateById(mtId);
			//Added By SongQing �ж����Ϊϵͳ��ǩ�������޸ĸ�ǩ���
			if(!mManuTemplate.getIssystemoriginal().equals(TemplateType.NORMAL)){
				etTemplateName.setOnLongClickListener(null);
				etTemplateName.setKeyListener(null);
				etTemplateName.setCursorVisible(false);
			}
			this.setControlValue();
			editOrView = "Edit";
		}else	if(requestType != null && requestType.equals("View")){	//����鿴
			mManuTemplate = (ManuscriptTemplate)p;
			rLayoutTemplateName.setVisibility(View.GONE);
			viewTemplateName.setVisibility(View.GONE);
			rLayoutTemplateDefaultTitle.setVisibility(View.GONE);
			viewDefaultTitle.setVisibility(View.GONE);
			rLayoutTemplateDefaultContent.setVisibility(View.GONE);
			viewDefaultContent.setVisibility(View.GONE);
			this.disableControls();
			editOrView = "View";
		}
		else{}
	}

	/**
	 * ���ڴ����½�һ����ǩ����
	 */
	private void newManuscriptTemplateObject() {
		
		mManuTemplate.setCreatetime(TimeFormatHelper.getFormatTime(new Date())) ;
		mManuTemplate.setMt_id(UUID.randomUUID().toString());
		mManuTemplate.setIsdefault(XmcBool.False);
		mManuTemplate.setIssystemoriginal(TemplateType.NORMAL.toString());	
		mManuTemplate.setLoginname(IngleApplication.getInstance().getCurrentUser());
	}
	
	/**
	 * ģ�����Ϊ�Ի���
	 */
	private void DialogForSaveAs(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		etTemplateNameForSaveAS = new EditText(EditTemplateActivity.this);
		builder.setTitle(R.string.saveAsTemplateName)
				.setView(etTemplateNameForSaveAS)
				.setPositiveButton(R.string.confirm_button,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,int which) {
								String messageString=verifyTemplateName(etTemplateNameForSaveAS.getText().toString().trim(),"SaveAs");
								
								if(messageString.equals("")){
									ManuscriptTemplate mtTemplate = getManuscriptTemplate();
									//Added By SongQing.����½���ǩ����£�������Ϊʱ��mManuTemplate.getMt_id()��ȡ����mt�����
									if(mtTemplate == null){
										mtTemplate = mManuTemplate.clone();
									}
									mtTemplate.setIsdefault(XmcBool.False);
									mtTemplate.setIssystemoriginal(TemplateType.NORMAL.toString());
									mtTemplate.setMt_id(UUID.randomUUID().toString());
									mtTemplate.setIs3Tnews(XmcBool.False);
									mtTemplate.setLoginname(currentUserName);
									mtTemplate.setName(etTemplateNameForSaveAS.getText().toString().trim());
									if(mtService.addManuscriptTemplate(mtTemplate)){
										/*ToastHelper.showToast("���ɹ�",Toast.LENGTH_SHORT);*/
										showMessage(getResources().getString(R.string.saveAsSuccess));
										Intent mIntent = new Intent();
										Bundle mBundle = new Bundle();
										mBundle.putParcelable("manuTemplateInfo",mManuTemplate);
										mIntent.putExtras(mBundle);				
										setResult(Activity.RESULT_OK, mIntent); //���÷������
										EditTemplateActivity.this.finish();
									}
								}else{
									//Toast.makeText(EditTemplateActivity.this,	messageString,Toast.LENGTH_SHORT).show();
									showMessage(messageString);
								}
							}
						})
				.setNegativeButton(R.string.cancel_button,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();

							}
						}).create().show();
	}
	

	/**
	 * ��ʾ�����Ϣ
	 * 
	 * @param message
	 */
	private void showMessage(String message) {
		/*Toast.makeText(fragmentView.getContext(), message, Toast.LENGTH_SHORT).show();*/
		ToastHelper.showToast(message, Toast.LENGTH_SHORT);
	}
	
	/**
	 * ��ݵ�ǰ��mManuTemplate��������ҳ��ؼ���ֵ
	 */
	private void setControlValue(){
		//Added By SongQing������ǿ�Ѷ���ǩ��Ĭ�ϱ�����Ҫ��ɺ����Ա�ʾ����
		if(!mManuTemplate.getIssystemoriginal().equals(TemplateType.NORMAL.toString())){
			this.tvTemplateDefaultTitle.setTextColor(Color.RED);
		}
		this.etTemplateSendToAddress.setText(mManuTemplate.getAddress());
		this.etTemplateSendToAddress.setContentDescription(mManuTemplate.getAddressID());
		this.etTemplateAuthor.setText(mManuTemplate.getAuthor());
		this.etTemplateDepartment.setText(mManuTemplate.getComefromDept());		
		this.etTemplateDepartment.setContentDescription(mManuTemplate.getComefromDeptID());
		
		if(this.etTemplateDepartment.getText().toString().trim().equals("") ){
			this.etTemplateDepartment.setText(IngleApplication.getInstance().currentUserInfo.getUserattribute().getGroupNameC());				
			this.etTemplateDepartment.setContentDescription(IngleApplication.getInstance().currentUserInfo.getUserattribute().getGroupCode());
		}
		if(this.etTemplateDefaultContent !=null &&etTemplateDefaultTitle!=null)
		{
			this.etTemplateDefaultContent.setText(mManuTemplate.getDefaultcontents());
			this.etTemplateDefaultTitle.setText(mManuTemplate.getDefaulttitle());
		}
		this.etTemplateNewsCategory.setText(mManuTemplate.getDoctype());
		this.etTemplateNewsCategory.setContentDescription(mManuTemplate.getDoctypeID());
		this.etTemplateHappenPlace.setText(mManuTemplate.getHappenplace());
		this.etTemplateKeywords.setText(mManuTemplate.getKeywords());
		this.etTemplateLanguage.setText(mManuTemplate.getLanguage());
		this.etTemplateLanguage.setContentDescription(mManuTemplate.getLanguageID());
		if(this.etTemplateName !=null)
		{
			this.etTemplateName.setText(mManuTemplate.getName());
		}
		this.etTemplatePriority.setText(mManuTemplate.getPriority());
		this.etTemplatePriority.setContentDescription(mManuTemplate.getPriorityID());
		this.etTemplateInternalInternational.setText(mManuTemplate.getProvtype());
		this.etTemplateInternalInternational.setContentDescription(mManuTemplate.getProvtypeID());
		this.etTemplateGeographyCategory.setText(mManuTemplate.getRegion());
		this.etTemplateGeographyCategory.setContentDescription(mManuTemplate.getRegionID());
		this.etTemplateManuscriptsType.setText("δ��");
		this.etTemplateManuscriptsType.setContentDescription("δ��");
		this.etTemplateReportPlace.setText(mManuTemplate.getReportplace());
		this.etTemplateReviewStatus.setText(mManuTemplate.getReviewstatus());
		this.etTemplateSendArea.setText(mManuTemplate.getSendarea());
	}

	/**
	 * 	��֤��ǩ����Ƿ����
	 * @param name
	 * @param saveType
	 * @return
	 */
	private String verifyTemplateName(String name,String saveType){
		if(name ==null || name.equals("")){
			return  getResources().getString(R.string.templateNameNotNull);
		}
		ManuscriptTemplate mTemplate = mtService.getManuscriptTemplateById(mManuTemplate.getMt_id());
	
		if(mtService.isNameExsit(name, currentUserName)){
			if(	mTemplate==null ){		//˵�����½���ǩ
				return getResources().getString(R.string.templateNameExist);
			}else if( saveType.equals("SaveAs") ){
				return getResources().getString(R.string.templateNameExist);
			}else{
				return "";
			}
		}else{
			return "";
		}
	}
	
	/**
	 * ��ҳ��ؼ��л�ȡ���ģ����Ϣ
	 * @return
	 */
	private ManuscriptTemplate getManuscriptTemplate(){
		mManuTemplate.setAddress(this.etTemplateSendToAddress.getText().toString().trim());
		mManuTemplate.setAddressID(this.etTemplateSendToAddress.getContentDescription().toString());
		mManuTemplate.setAuthor(this.etTemplateAuthor.getText().toString());
		mManuTemplate.setComefromDept(this.etTemplateDepartment.getText().toString().trim());
		mManuTemplate.setComefromDeptID(this.etTemplateDepartment.getContentDescription().toString());
		//		mManuTemplate.setCreatetime(this.);
		if(this.etTemplateDefaultContent !=null && this.etTemplateDefaultTitle !=null){
			mManuTemplate.setDefaultcontents(this.etTemplateDefaultContent.getText().toString().trim());
			mManuTemplate.setDefaulttitle(this.etTemplateDefaultTitle.getText().toString().trim());
		}
		mManuTemplate.setDoctype(this.etTemplateNewsCategory.getText().toString());
		mManuTemplate.setDoctypeID(this.etTemplateNewsCategory.getContentDescription().toString());
		mManuTemplate.setHappenplace(this.etTemplateHappenPlace.getText().toString());
		//TODO ����ж��ظ�����Ϳ���ѡ����3T/δ���壬����Ҫ���etTemplateManuscriptsType��ֵ���ж��Ƿ�Ϊ3T���
		//mManuTemplate.setIs3Tnews(XmcBool.False);
		//�ڸ�ǩ�����������Ƿ�ΪĬ�ϸ�ǩ
		//mManuTemplate.setIsdefault(XmcBool.False);
		//ϵͳĬ�ϸ�ǩ�ǲ���ɾ����½���
		//	mManuTemplate.setIssystemoriginal("0");
		mManuTemplate.setKeywords(this.etTemplateKeywords.getText().toString().trim());
		mManuTemplate.setLanguage(this.etTemplateLanguage.getText().toString().trim());
		mManuTemplate.setLanguageID(this.etTemplateLanguage.getContentDescription().toString());		
		//mManuTemplate.setMt_id();
		if(this.etTemplateName !=null){
			mManuTemplate.setName(this.etTemplateName.getText().toString().trim());
		}
		mManuTemplate.setPriority(this.etTemplatePriority.getText().toString().trim());
		mManuTemplate.setPriorityID(this.etTemplatePriority.getContentDescription().toString().trim());
		mManuTemplate.setProvtype(this.etTemplateInternalInternational.getText().toString().trim());
		mManuTemplate.setProvtypeID(this.etTemplateInternalInternational.getContentDescription().toString().trim());
		mManuTemplate.setRegion(this.etTemplateGeographyCategory.getText().toString().trim());
		mManuTemplate.setRegionID(this.etTemplateGeographyCategory.getContentDescription().toString().trim());
		mManuTemplate.setReportplace(this.etTemplateReportPlace.getText().toString().trim());
		mManuTemplate.setReviewstatus(this.etTemplateReviewStatus.getText().toString().trim());
		mManuTemplate.setSendarea(this.etTemplateSendArea.getText().toString().trim());
		return mManuTemplate;		
	}
	
	private void disableControls(){
		
		etTemplateName.setEnabled(false);
		etTemplateAuthor.setEnabled(false);	
		
		etTemplateDepartment.setEnabled(false);
		etTemplateInternalInternational.setEnabled(false);
		etTemplateNewsCategory.setEnabled(false);
		etTemplateGeographyCategory.setEnabled(false);
		
		etTemplateSendArea.setEnabled(false);
		etTemplateHappenPlace.setEnabled(false);
		etTemplateKeywords.setEnabled(false);
		etTemplateReviewStatus.setEnabled(false);
		etTemplateDefaultTitle.setEnabled(false);
		etTemplateDefaultContent.setEnabled(false);
		
		tvTemplateAuthor.setEnabled(false);
		tvTemplateDepartment.setEnabled(false);
		tvTemplateInternalInternational.setEnabled(false);
		tvTemplateNewsCategory.setEnabled(false);
		tvTemplateGeographyCategory.setEnabled(false);
		tvTemplateSendArea.setEnabled(false);
		tvTemplateHappenPlace.setEnabled(false);
		tvTemplateReportPlace.setEnabled(false);
		tvTemplateKeywords.setEnabled(false);
		
		tvTemplateLanguage = (TextView)findViewById(R.id.textViewLanguage_ETA);
		tvTemplateLanguage.setEnabled(false);
		tvTemplatePriority = (TextView)findViewById(R.id.textViewPriority_ETA);
		tvTemplatePriority.setEnabled(false);
		tvTemplateManuscriptsType = (TextView)findViewById(R.id.textViewManuscriptsType_ETA);
		tvTemplateManuscriptsType.setEnabled(false);
		tvTemplateSendToAddress = (TextView)findViewById(R.id.textViewSendToAddress_ETA);
		tvTemplateSendToAddress.setEnabled(false);
		
		etTemplateLanguage = (EditText)findViewById(R.id.editTextLanguage_ETA);	
		etTemplateLanguage.setKeyListener(null);
		etTemplatePriority = (EditText)findViewById(R.id.editTextPriority_ETA);
		etTemplatePriority.setKeyListener(null);
		etTemplateManuscriptsType = (EditText)findViewById(R.id.editTextManuscriptsType_ETA);
		etTemplateManuscriptsType.setKeyListener(null);
		etTemplateSendToAddress = (EditText)findViewById(R.id.editTextSendToAddress_ETA);
		etTemplateSendToAddress.setKeyListener(null);
				

		iBtnStoreTemplate.setVisibility(View.INVISIBLE);	
		
		iBtnMultiSelectDepartment.setVisibility(View.INVISIBLE);	
		iBtnSingleSelectAuthor.setVisibility(View.INVISIBLE);	
		iBtnInternalInternational.setVisibility(View.INVISIBLE);	
		iBtnNewsCategory.setVisibility(View.INVISIBLE);	
		iBtnGeographyCategory.setVisibility(View.INVISIBLE);	
		iBtnSendArea.setVisibility(View.INVISIBLE);	
		iBtnHappenPlace.setVisibility(View.INVISIBLE);	
		iBtnReportPlace.setVisibility(View.INVISIBLE);	
		iBtnKeywords.setVisibility(View.INVISIBLE);	

		iBtnLanguage.setVisibility(View.INVISIBLE);	
		iBtnPriority.setVisibility(View.INVISIBLE);	
		iBtnManuscriptsType.setVisibility(View.INVISIBLE);	
		iBtnSendToAddress.setVisibility(View.INVISIBLE);	
		
		btnSaveAs .setVisibility(View.INVISIBLE);	
		btnAdoptTemplate.setVisibility(View.INVISIBLE);	
	}
	
	/**
	 * �����ǩҳ
	 */
	private void exitManuscriptTemplate(){
		Intent mIntent = new Intent();
		Bundle mBundle = new Bundle();
		mBundle.putParcelable("manuTemplateInfo", this.getManuscriptTemplate());
		mBundle.putString("EditOrView", editOrView);
		mIntent.putExtras(mBundle);				
		setResult(Activity.RESULT_OK, mIntent); //���÷������
		finish();
		//�����л�����������߽��룬�ұ��˳�
		overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
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
	
	private int verticalMinDistance = 40;  				//ˮƽ��С�Ļ�������
	private int minVelocity         = 0;  					

	@Override  
	public boolean dispatchTouchEvent(MotionEvent ev) {  
		if(onTouchKey){
			mGestureDetector.onTouchEvent(ev); 
		}
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
		}else if (e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {  //��������
			exitManuscriptTemplate();
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
	
	/**
	 * �Ƚϸ�ǩģ�������Ƿ����޸�
	 * @return
	 */
	private boolean compareTemplate(){
		
		boolean returnValue = true;
		if(!this.oManuTemplate.getAddress().equals(this.mManuTemplate.getAddress())){
			returnValue =  false;
		}
		if(!this.oManuTemplate.getAuthor().equals(this.mManuTemplate.getAuthor())){
			returnValue =  false;
		}
		if(!this.oManuTemplate.getComefromDept().equals(this.mManuTemplate.getComefromDept())){
			returnValue =  false;
		}
		if(!this.oManuTemplate.getDefaultcontents().equals(this.mManuTemplate.getDefaultcontents())){
			returnValue =  false;
		}
		if(!this.oManuTemplate.getDefaulttitle().equals(this.mManuTemplate.getDefaulttitle())){
			returnValue =  false;
		}
		if(!this.oManuTemplate.getDoctype().equals(this.mManuTemplate.getDoctype())){
			returnValue =  false;
		}
		if(!this.oManuTemplate.getHappenplace().equals(this.mManuTemplate.getHappenplace())){
			returnValue =  false;
		}
		if(!this.oManuTemplate.getIs3Tnews().equals(this.mManuTemplate.getIs3Tnews())){
			returnValue =  false;
		}
		if(!this.oManuTemplate.getKeywords().equals(this.mManuTemplate.getKeywords())){
			returnValue =  false;
		}
		if(!this.oManuTemplate.getLanguage().equals(this.mManuTemplate.getLanguage())){
			returnValue =  false;
		}
		if(!this.oManuTemplate.getName().equals(this.mManuTemplate.getName())){
			returnValue =  false;
		}
		if(!this.oManuTemplate.getPriority().equals(this.mManuTemplate.getPriority())){
			returnValue =  false;
		}
		if(!this.oManuTemplate.getProvtype().equals(this.mManuTemplate.getProvtype())){
			returnValue =  false;
		}
		if(!this.oManuTemplate.getRegion().equals(this.mManuTemplate.getRegion())){
			returnValue =  false;
		}
		if(!this.oManuTemplate.getReportplace().equals(this.mManuTemplate.getReportplace())){
			returnValue =  false;
		}
		if(!this.oManuTemplate.getReviewstatus().equals(this.mManuTemplate.getReviewstatus())){
			returnValue =  false;
		}
		if(!this.oManuTemplate.getSendarea().equals(this.mManuTemplate.getSendarea())){
			returnValue =  false;
		}
		return returnValue;		
	}
	/**
	 * ����
	 */
	private void back(){
		
		if(iViewTempletSwitcher.getVisibility()==View.INVISIBLE ||iViewTempletSwitcher.getVisibility()==View.GONE){		//˵���ǹ���ҳ����ĸ�ǩ
			getManuscriptTemplate();
			if(!compareTemplate()){					//����ҳ������ǩҳ
				   new AlertDialog.Builder(this).setTitle(R.string.save_confirm_title_MTA)
					.setPositiveButton(R.string.confirm_button, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							store();
						}  
					}).setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							close();
						}
					}).show();
			}else{
				close();
			}
		}else{						//��ͨ�ĸ���༭�����ǩҳ
			close();
		}
	
	}
	
	/**
	 * ����ģ��
	 */
	private void store(){
		Intent mIntent = new Intent();
		Bundle mBundle = new Bundle();
		boolean returnValue = false;
		mManuTemplate = this.getManuscriptTemplate();
		mManuTemplate.setCreatetime(TimeFormatHelper.getFormatTime(new Date()));
		String messageString = verifyTemplateName(etTemplateName.getText().toString().trim(),"Save");
		
		
		boolean state=true;
		if(mtService.isNameExsit(mManuTemplate.getName(), currentUserName)){//�����ݿ��д���ͬһ��ǩ��ĸ�ǩ��������Ƿ���������ж�
			state=mManuTemplate.getMt_id().equals(mtService.getManuscriptTemplateByName(etTemplateName.getText().toString().trim(), currentUserName).getMt_id());//��ݵ�ǰ��ǩ��ƻ�ȡid�ж��Ƿ���ͬһid
		}
		if(messageString.equals("")){
			if(mtService.getManuscriptTemplateById(mManuTemplate.getMt_id())!=null && manageType.equals("")&&state){//����Ѿ����������
				returnValue = mtService.updateManuscriptTemplate(mManuTemplate);
			}
			else{						
				if(mtService.isNameExsit(mManuTemplate.getName(), currentUserName)){
					//Toast.makeText(EditTemplateActivity.this,	"ģ�����Ѵ���,����������",Toast.LENGTH_SHORT).show();
					showMessage(getResources().getString(R.string.templateNameExist));
					return;
				}else{
					newManuscriptTemplateObject();
					returnValue = mtService.addManuscriptTemplate(mManuTemplate);	
				}
			}
			
			if(returnValue){
				mBundle.putParcelable("manuTemplateInfo",mManuTemplate);
				mIntent.putExtras(mBundle);				
				setResult(Activity.RESULT_OK, mIntent); //���÷������
				finish();
			}
			else{
				setResult(Activity.RESULT_CANCELED, mIntent); //���÷������
				finish();
			}
		}else{
			//Toast.makeText(EditTemplateActivity.this,	messageString,Toast.LENGTH_SHORT).show();
			ToastHelper.showToast(messageString,Toast.LENGTH_SHORT);
		}
		
		manageType = "";
	}
	
	private void close(){
		Intent mIntent = new Intent();
		Bundle mBundle = new Bundle();
		mBundle.putParcelable("manuTemplateInfo", getManuscriptTemplate());
		mBundle.putString("EditOrView", editOrView);
		mIntent.putExtras(mBundle);				
		setResult(Activity.RESULT_OK, mIntent); //���÷������
		finish();			
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// KeyEvent.KEYCODE_BACK��?�ز���.
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back();
		}
		return true;
	}	
}
