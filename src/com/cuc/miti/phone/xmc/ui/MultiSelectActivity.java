package com.cuc.miti.phone.xmc.ui;

import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.adapter.MultiSelectListViewAdapter;
import com.cuc.miti.phone.xmc.domain.EmployeeSendToAddress;
import com.cuc.miti.phone.xmc.domain.Enums.BaseDataType;
import com.cuc.miti.phone.xmc.domain.Enums.NetStatus;
import com.cuc.miti.phone.xmc.domain.Enums.TemplateType;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.domain.SendToAddress;
import com.cuc.miti.phone.xmc.domain.User;
import com.cuc.miti.phone.xmc.http.RemoteCaller;
import com.cuc.miti.phone.xmc.logic.ComeFromAddressService;
import com.cuc.miti.phone.xmc.logic.EmployeeSendToAddressService;
import com.cuc.miti.phone.xmc.logic.KeywordsService;
import com.cuc.miti.phone.xmc.logic.LanguageService;
import com.cuc.miti.phone.xmc.logic.NewsPriorityService;
import com.cuc.miti.phone.xmc.logic.NewsTypeService;
import com.cuc.miti.phone.xmc.logic.PlaceService;
import com.cuc.miti.phone.xmc.logic.ProvideTypeService;
import com.cuc.miti.phone.xmc.logic.SendToAddressService;
import com.cuc.miti.phone.xmc.logic.UserService;
import com.cuc.miti.phone.xmc.ui.EditWordFManuscriptsActivity.TextChangedWatcher;
import com.cuc.miti.phone.xmc.utils.TouchEffect;
import com.cuc.miti.phone.xmc.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * ʵ�ֶ�ѡ���ܵ�Activity�������Ҫ������Ҫ�󶨵�source
 * 
 * @author SongQing
 * 
 */
public class MultiSelectActivity extends BaseActivity implements OnClickListener {

	private List<KeyValueData> sourceItemList = null; // ��Ҫ�󶨵����Դ
	private String listKey = ""; // ���Դ��Ӧ���������
	private String title = ""; // ҳ�����
	private List<Boolean> mChecked = null;												//���List�����Ų�ѯ��������ʾ������
	private List<KeyValueData> mCheckedValue = null;							//��ֵ�ԣ�Key��id��value�Ǹ�id���checked״̬
	private MultiSelectListViewAdapter multiSelectListViewAdapter;
	private CharSequence[] selectItems = null;
	private int selectMode = -1;

	private ComeFromAddressService mDepartmentService; // ��Դ
	private LanguageService mLanguageService; // ����
	private ProvideTypeService mProvideTypeService; // ��������
	private KeywordsService mKeywordsService; // �ؼ���
	private NewsPriorityService mNewsPriorityService; // ���ȼ�
	private NewsTypeService mNewsTypeService; // �������
	private PlaceService mPlaceService; // �ص�
	private SendToAddressService mSendToAddressService; // �����ַ
	private EmployeeSendToAddressService employeeSendToAddressService;		//�û��Զ��巢���ַ
	
	private UserService mUserService; // ����
	private TextView tvPageTitle;
	private ListView lvBaseData;
	private ImageButton iBtnBack;
	private ImageButton iBtnStoreChoose;
	private EditText etSearch;									//������
	private RelativeLayout relativeLayoutSearch;		//��������
	//���µ�ǰ�Ǽ��ļ�����ǩģ��
	private int mtType=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.multi_select_basedata);

		this.setUpViews();
		this.getListSource();
		this.initializeCheckedData();
		this.initializeListView();
		
		IngleApplication.getInstance().addActivity(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.ListActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public void onClick(View v) {
		Intent intent;
		Bundle mBundle;
		switch (v.getId()) {
		case R.id.iBtnBack_MSB:
			setResult(RESULT_CANCELED);
			finish();
			break;
		case R.id.iBtnStoreChoose_MSB:
			intent = new Intent();
			mBundle = new Bundle();
			//mChecked = multiSelectListViewAdapter.getSelectedItems();
			List<KeyValueData> selectedList =multiSelectListViewAdapter.getCheckedItemList();
			if(selectedList!=null && selectedList.size()>0){
				mBundle.putCharSequenceArray("selected", getSelectedValue(selectedList));
				intent.putExtras(mBundle);
			}else{
				mBundle.putCharSequenceArray("selected",new String[]{"",""});
				intent.putExtras(mBundle);		
			}
			setResult(Activity.RESULT_OK,intent); // ���÷������
			finish();
			break;
		default:
			break;
		}
	}

	private void setUpViews() {

		iBtnBack = (ImageButton) findViewById(R.id.iBtnBack_MSB);
		iBtnBack.setOnClickListener(this);
		iBtnBack.setOnTouchListener(TouchEffect.TouchDark);
		iBtnStoreChoose = (ImageButton) findViewById(R.id.iBtnStoreChoose_MSB);
		iBtnStoreChoose.setOnClickListener(this);
		iBtnStoreChoose.setOnTouchListener(TouchEffect.TouchDark);
		tvPageTitle = (TextView) findViewById(R.id.textViewTitle_MSB);
		lvBaseData = (ListView) findViewById(R.id.listViewSelectBaseData_MSB);
		etSearch = (EditText)findViewById(R.id.editTextSearch_MSB);
		etSearch.addTextChangedListener(new TextChangedWatcher());		
		relativeLayoutSearch = (RelativeLayout)findViewById(R.id.relativeLayoutSearch_MSB);
		multiSelectListViewAdapter = new MultiSelectListViewAdapter(MultiSelectActivity.this);
	}

	/**
	 * ��ʼ��listView��ѡ��
	 */
	private void initializeListView() {
		if (sourceItemList != null && sourceItemList.size() > 0) {
			multiSelectListViewAdapter.setSourceItemList(sourceItemList);
			lvBaseData.setAdapter(multiSelectListViewAdapter);

			if (selectMode == ListView.CHOICE_MODE_SINGLE) { // ֻ����Ҫ��ѡʱ������ÿһ�еĵ���¼���Ӧ
				multiSelectListViewAdapter.setCheckBox(false); // ���õ�ѡʱcheckbox���ɼ�
				// ��ʼ��gridview�����¼�
				lvBaseData.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						KeyValueData kvItem = sourceItemList.get(position);
						if (kvItem != null) {
							Intent intent = new Intent();
							Bundle mBundle = new Bundle();
							mBundle.putString("selectName", kvItem.getKey());
							mBundle.putString("selectId", kvItem.getValue());
							intent.putExtras(mBundle);
							setResult(Activity.RESULT_OK, intent); // ���÷������
							finish();
						}
					}
				});
			}
		}
	}
	
	

	/**
	 * ��ݴ����ֵ��ʼ��CheckBoxѡ��״̬��¼�б�mChecked
	 */
	private void initializeCheckedData() {

		if (selectMode == ListView.CHOICE_MODE_SINGLE) {
			return;
		}
		if(sourceItemList ==null){
			sourceItemList = new ArrayList<KeyValueData>();
		}
		// ���ԭʼ��ԭʼ��ݵ�������ʼ����¼ѡ��״̬��List<Boolean> mChecked ��ֵȫΪĬ�ϳ�ʼֵ:false
		mChecked = new ArrayList<Boolean>();
		mCheckedValue = new ArrayList<KeyValueData>();
		for (int i = 0; i < sourceItemList.size(); i++) {
			mChecked.add(false);
		}

		if (selectItems[0] != null && selectItems[0].length() > 0) {
			KeyValueData tempData = null;
			String[] keyList = selectItems[0].toString().split(",");
			String[] valueList = selectItems[1].toString().split(",");
			for (int i = 0; i < keyList.length; i++) {
				if (valueList[i] == null) {
					continue;  
				}
				tempData = new KeyValueData(keyList[i], valueList[i]);				
				int tempPosition = sourceItemList.indexOf(tempData);
				if(tempPosition!=-1){
					mChecked.set(tempPosition, true);
					mCheckedValue.add(tempData);
				}
			}
		}
		
		multiSelectListViewAdapter.setCheckedItemList(mCheckedValue);
		multiSelectListViewAdapter.setSelectedItems(mChecked);
	}

	/**
	 * ��ȡͨ��Intent Bundle�������Ҫ�����Դ�������Ϣ ���� ���ԴList �� ���Դ�����
	 */
	private void getListSource() {
		Bundle mBundle = this.getIntent().getExtras();
		NetStatus netStatus;
		if (mBundle != null) {
			listKey = mBundle.getString("listKey");
		}

		selectMode = mBundle.getInt("selectMode");
		//���ļ����з����ַ��Ҫ�仯�������ø�״̬�����б�ʾ��
		if(TemplateType.INSTANT.toString().equals(mBundle.getString("mtType")))
		{
			mtType=1;
		}
		switch (selectMode) {
		case ListView.CHOICE_MODE_MULTIPLE:
			// ��ȡ�Ѿ�ѡ���ѡ����Ҹı��¼ѡ��״̬List<Boolean> mChecked ����Ӧֵ
			selectItems = mBundle.getCharSequenceArray("selected");
			break;
		case ListView.CHOICE_MODE_SINGLE:
			iBtnStoreChoose.setVisibility(View.INVISIBLE);
			break;
		default:
			break;
		}
		
		if (listKey != null) {
			switch (BaseDataType.valueOf(listKey)) {
			case Department:
				mDepartmentService = new ComeFromAddressService(
						MultiSelectActivity.this);
				sourceItemList = mDepartmentService.getBaseDataForBind("");
				title = BaseDataType.valueOf(listKey).getValue();
				break;
			case InternalInternational:
				mProvideTypeService = new ProvideTypeService(
						MultiSelectActivity.this);
				sourceItemList = mProvideTypeService.getBaseDataForBind();
				title = BaseDataType.valueOf(listKey).getValue();
				relativeLayoutSearch.setVisibility(View.GONE);
				break;
			case SendArea:
				mPlaceService = new PlaceService(MultiSelectActivity.this);
				sourceItemList = mPlaceService.getBaseDataForBind("");
				title = BaseDataType.valueOf(listKey).getValue();
				break;
			case HappenPlace:
				mPlaceService = new PlaceService(MultiSelectActivity.this);
				sourceItemList = mPlaceService.getBaseDataForBind("");
				title = BaseDataType.valueOf(listKey).getValue();
				break;
			case ReportPlace:
				mPlaceService = new PlaceService(MultiSelectActivity.this);
				sourceItemList = mPlaceService.getBaseDataForBind("");
				title = BaseDataType.valueOf(listKey).getValue();
				break;
			case Keywords:
				mKeywordsService = new KeywordsService(MultiSelectActivity.this);
				sourceItemList = mKeywordsService.getBaseDataForBind("");
				title = BaseDataType.valueOf(listKey).getValue();
				relativeLayoutSearch.setVisibility(View.GONE);
				break;
			case Language:
				mLanguageService = new LanguageService(MultiSelectActivity.this);
				sourceItemList = mLanguageService.getBaseDataForBind();
				title = BaseDataType.valueOf(listKey).getValue();
				relativeLayoutSearch.setVisibility(View.GONE);
				break;
			case Priority:
				mNewsPriorityService = new NewsPriorityService(
						MultiSelectActivity.this);
				sourceItemList = mNewsPriorityService.getBaseDataForBind();
				title = BaseDataType.valueOf(listKey).getValue();
				relativeLayoutSearch.setVisibility(View.GONE);
				break;
			case ManuscriptsType:
				mNewsTypeService = new NewsTypeService(MultiSelectActivity.this);
				sourceItemList = mNewsTypeService.getBaseDataForBind();
				title = BaseDataType.valueOf(listKey).getValue();
				relativeLayoutSearch.setVisibility(View.GONE);
				break;
			case ReleAddress:				//Ĭ�ϵ�ȫ����ǩ����
				// ��ȡ��ݿ��еķ����ַ
				mSendToAddressService = new SendToAddressService(MultiSelectActivity.this);
				sourceItemList = mSendToAddressService.getBaseDataForBind("");
				title = BaseDataType.valueOf(listKey).getValue();
				relativeLayoutSearch.setVisibility(View.GONE);
				break;
			case CustomReleAddress:				//�û�С��ַ�б�����
				// �ж��Ƿ����ߣ�������ߣ��͸����û���Ϣ���������ַ��Ϣ
				netStatus = IngleApplication.getNetStatus();
				if (netStatus != NetStatus.Disable) {
					UserService userService = new UserService(this);
					userService.reloadUserOnline();
				}
				//��ȫ�û�����ѡ�����ַ�б�
				this.makeUpLossForEmploySendToAddress("");
				// ��ȡ�����ַ�ĺϲ��б�
				mSendToAddressService = new SendToAddressService(MultiSelectActivity.this);
				//����Ǽ��ļ���
				if(1==mtType) 
				{
					if(RemoteCaller.addressAll!=null)
					{
						sourceItemList = mSendToAddressService.getInstantUploadBaseDataForBind(RemoteCaller.addressAll);
					}else{
						sourceItemList=new ArrayList<KeyValueData>();
					}
					
				}else{
				sourceItemList = mSendToAddressService.getCombineBaseDataForBind(IngleApplication.getInstance().currentUserInfo,"");
				}
				title = BaseDataType.valueOf(listKey).getValue();
				break;
			case Author:
				mUserService = new UserService(MultiSelectActivity.this);
				sourceItemList = mUserService.getBaseDataForBind();
				title = BaseDataType.valueOf(listKey).getValue();
				relativeLayoutSearch.setVisibility(View.GONE);
			default:
				break;
			}
		}
		tvPageTitle.setText(this.title);
		
	}

	/**
	 * ��ȡѡ���ѡ��
	 * 
	 * @return ����Ϊ2������:[0]��Key����"��������,���ʷ���"����Ӧ[1]��Value����"Gbjfsh,Gcxfsh"
	 */
	private String[] getSelectedValue(List<KeyValueData> selectedList) {
		String[] selectValue = new String[2];
		StringBuilder sbKey = new StringBuilder();
		StringBuilder sbValue = new StringBuilder();
		
		for(KeyValueData kvData:selectedList){
			sbKey.append(kvData.getKey()).append(",");
			sbValue.append(kvData.getValue()).append(",");
		}
//		for (int i = 0; i < mChecked.size(); i++) {
//			if (mChecked.get(i)) {
//				sbKey.append(sourceItemList.get(i).getKey()).append(",");
//				sbValue.append(sourceItemList.get(i).getValue()).append(",");
//			}
//		}

		if (sbKey.length() != 0) {
			selectValue[0] = sbKey.substring(0, sbKey.length() - 1);
			selectValue[1] = sbValue.substring(0, sbValue.length() - 1);
		}
		return selectValue;
	}
	
	/**
	 * ���û���ǩ�ӷ�������ͬ��ʱ�����ܴ��ڷ�������ǩ�еķ����ַ�뵱ǰ�û��Զ��巢���ַ�б?ƥ��������������Ҫ���в�ȫ
	 * Ҳ����˵����ǩ�еķ����ַ��ǰ�û��Զ��巢���ַ�б���û�У�����Ҫ�Զ����ӵ��û��Զ��巢���ַ�б���
	 */
	private void makeUpLossForEmploySendToAddress(String queryString){
		if(selectItems[0].length()>0){		//˵�����Ѿ�ѡ��õķ����ַ
			String[] nameArrayStrings = selectItems[0].toString().split(",");
			String[] codeArrayStrings = selectItems[1].toString().split(",");
			employeeSendToAddressService = new EmployeeSendToAddressService(MultiSelectActivity.this);
			mSendToAddressService = new SendToAddressService(MultiSelectActivity.this);
			List<EmployeeSendToAddress> employeeSendToAddressList = employeeSendToAddressService.getEmployeeSendToAddressList(queryString);
			
			if(employeeSendToAddressList == null  || employeeSendToAddressList.size() ==0){
				return;
			}else if(employeeSendToAddressList.size() >0){
				boolean existMark = false;
				for(int i=0;i<nameArrayStrings.length;i++){
					for(EmployeeSendToAddress es:employeeSendToAddressList){
						if(es.getName().equals(nameArrayStrings[i]) && es.getCode().equals(codeArrayStrings[i])){
							existMark = true;
							break;
						}
					}
					if(!existMark){		//������û��Զ��巢���ַ�б���û�е�ǰ�ķ����ַ���Ҫ���û��Զ��巢���ַ�б������һ��
						EmployeeSendToAddress employeeSendToAddress = null;
						SendToAddress snAddress = mSendToAddressService.getSendToAddress(nameArrayStrings[i]);
						if( snAddress !=null){
							employeeSendToAddress = new EmployeeSendToAddress();
							employeeSendToAddress.setCode(snAddress.getCode());
							employeeSendToAddress.setName(snAddress.getName());
							employeeSendToAddress.setLanguage(snAddress.getLanguage());
							employeeSendToAddress.setOrder(snAddress.getOrder());
							employeeSendToAddress.setLoginname(IngleApplication.getInstance().getCurrentUser());
							employeeSendToAddressService.addEmployeeSendToAddress(employeeSendToAddress);
						}
					}
					existMark =false;
				}
				return ;
			}else{return;}
		}
	}
	
	/**
	 * ���EditText�е����ֱ仯
	 * @author SongQing
	 *
	 */
	class TextChangedWatcher implements TextWatcher{

		public void afterTextChanged(Editable s) {
			
			BindListView(s.toString().trim());
			
			setCheckedData(multiSelectListViewAdapter.getCheckedItemList());
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {			
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {			
		}
		
	}
	
	/**
	 * ��ݱ����ѡ�����б?����checked��ֵ
	 */
	private void setCheckedData(List<KeyValueData> checkedList){
		if(sourceItemList ==null){
			sourceItemList = new ArrayList<KeyValueData>();
		}
		mChecked = new ArrayList<Boolean>();
		for (int i = 0; i < sourceItemList.size(); i++) {
			mChecked.add(false);
		}
		
		if(checkedList !=null && checkedList.size() > 0){
			for(KeyValueData kv: checkedList){
				int tempPosition = sourceItemList.indexOf(kv);
				if(tempPosition!=-1){
					mChecked.set(tempPosition, true);
				}
			}
		}
		 
		multiSelectListViewAdapter.setSourceItemList(sourceItemList);
		multiSelectListViewAdapter.setSelectedItems(mChecked);
		multiSelectListViewAdapter.notifyDataSetChanged();
	}
		
	private void BindListView(String queryString){
		NetStatus netStatus;

		if (listKey != null) {
			switch (BaseDataType.valueOf(listKey)) {
			case Department:
				mDepartmentService = new ComeFromAddressService(
						MultiSelectActivity.this);
				sourceItemList = mDepartmentService.getBaseDataForBind(queryString);
				title = BaseDataType.valueOf(listKey).getValue();
				break;
			case SendArea:
				mPlaceService = new PlaceService(MultiSelectActivity.this);
				sourceItemList = mPlaceService.getBaseDataForBind(queryString);
				title = BaseDataType.valueOf(listKey).getValue();
				break;
			case HappenPlace:
				mPlaceService = new PlaceService(MultiSelectActivity.this);
				sourceItemList = mPlaceService.getBaseDataForBind(queryString);
				title = BaseDataType.valueOf(listKey).getValue();
				break;
			case ReportPlace:
				mPlaceService = new PlaceService(MultiSelectActivity.this);
				sourceItemList = mPlaceService.getBaseDataForBind(queryString);
				title = BaseDataType.valueOf(listKey).getValue();
				break;
			case Keywords:
				mKeywordsService = new KeywordsService(MultiSelectActivity.this);
				sourceItemList = mKeywordsService.getBaseDataForBind(queryString);
				title = BaseDataType.valueOf(listKey).getValue();
				break;
			case ReleAddress:				//Ĭ�ϵ�ȫ����ǩ����
				// ��ȡ��ݿ��еķ����ַ
				mSendToAddressService = new SendToAddressService(MultiSelectActivity.this);
				sourceItemList = mSendToAddressService.getBaseDataForBind(queryString);
				title = BaseDataType.valueOf(listKey).getValue();
				break;
			case CustomReleAddress:				//�û�С��ַ�б�����
				// �ж��Ƿ����ߣ�������ߣ��͸����û���Ϣ���������ַ��Ϣ
				netStatus = IngleApplication.getNetStatus();
				if (netStatus != NetStatus.Disable) {
					UserService userService = new UserService(this);
					userService.reloadUserOnline();
				}
				//��ȫ�û�����ѡ�����ַ�б�
				this.makeUpLossForEmploySendToAddress(queryString);
				// ��ȡ�����ַ�ĺϲ��б�
				mSendToAddressService = new SendToAddressService(MultiSelectActivity.this);
				//����Ǽ��ļ���
				if(1==mtType)
				{
					if(RemoteCaller.addressAll!=null)
					{
						sourceItemList = mSendToAddressService.getInstantUploadBaseDataForBind(RemoteCaller.addressAll);
					}else{
						sourceItemList=new ArrayList<KeyValueData>();
					}
					
				}else{
					// sourceItemList = mSendToAddressService.getBaseDataForBind();
					sourceItemList = mSendToAddressService.getCombineBaseDataForBind(IngleApplication.getInstance().currentUserInfo,queryString);
					
				}
				title = BaseDataType.valueOf(listKey).getValue();
				break;
			default:
				break;
			}
		}
	}
}