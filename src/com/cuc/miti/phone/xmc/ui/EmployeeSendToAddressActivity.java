package com.cuc.miti.phone.xmc.ui;

import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.adapter.MultiSelectListViewAdapter;
import com.cuc.miti.phone.xmc.domain.EmployeeSendToAddress;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.domain.Enums.BaseDataType;
import com.cuc.miti.phone.xmc.domain.Enums.NetStatus;
import com.cuc.miti.phone.xmc.domain.SendToAddress;
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
import com.cuc.miti.phone.xmc.utils.TouchEffect;
import com.cuc.miti.phone.xmc.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/**
 * �û������ַ_С��ַ������
 * ֻ�е��û�����ʽԱ�������ã����򷵻���ҳ������ʾ
 * @author SongQing
 *
 */
public class EmployeeSendToAddressActivity extends BaseActivity implements OnClickListener{

	private List<KeyValueData> sourceItemList = null;				//��Ҫ�󶨵����Դ
	private MultiSelectListViewAdapter multiSelectListViewAdapter;
	
	private EmployeeSendToAddressService mEmployeeSendToAddressService;				//�û�С��ַ
	private SendToAddressService sendToAddressService;												//��ַ�ܱ�
	private TextView tvPageTitle;
	private ListView lvBaseData;
	private ImageButton iBtnBack;
	private ImageButton iBtnSelect;		
	private String listKey ="";
	private static final int ADDRESS_CONFIG = 1;
	private static String currentUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// ȥ�������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.multi_select_basedata);
	
		this.initialize();
		IngleApplication.getInstance().addActivity(this);
	}
	
	/* (non-Javadoc)
	 * @see android.app.ListActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	/**
	 * ��ʼ��
	 */
	private void initialize(){
		
		this.mEmployeeSendToAddressService = new EmployeeSendToAddressService(this);
		this.sendToAddressService = new SendToAddressService(this);
		currentUser = IngleApplication.getInstance().getCurrentUser();
		this.setUpViews();	
		this.getListSource();		
		this.initializeListView();	
		
	}
	
	public void onClick(View v) {
		Intent mIntent;
		Bundle mBundle;
		switch(v.getId()){
			case R.id.iBtnBack_MSB:
				setResult(RESULT_CANCELED);
				finish();	
				break;
			case R.id.iBtnStoreChoose_MSB:
				mIntent = new Intent(EmployeeSendToAddressActivity.this,MultiSelectActivity.class);
				mBundle = new Bundle();
				mBundle.putString("listKey", BaseDataType.ReleAddress.toString());
				mBundle.putInt("selectMode", ListView.CHOICE_MODE_MULTIPLE);
				
				mBundle.putCharSequenceArray("selected", this.getSelectedValue());
				mIntent.putExtras(mBundle);
				startActivityForResult(mIntent, ADDRESS_CONFIG);
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
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == Activity.RESULT_OK) {	
			Bundle mBundle = data.getExtras();
			switch(requestCode){
				case ADDRESS_CONFIG:
					if (mBundle != null) {
						String[] selected = (String[]) mBundle.getCharSequenceArray("selected");
						this.convertArray(selected);
						setUpViews();
						getListSource();
						initializeListView();
					}
					break;					
				default:
					break;
			}
		}
	}

	private void setUpViews(){
		
		iBtnBack = (ImageButton)findViewById(R.id.iBtnBack_MSB);
		iBtnBack.setOnClickListener(this);
		iBtnBack.setOnTouchListener(TouchEffect.TouchDark);
		iBtnSelect =(ImageButton)findViewById(R.id.iBtnStoreChoose_MSB);
		iBtnSelect.setBackgroundResource(R.drawable.word_flash_logo_960x540);
		iBtnSelect.setOnClickListener(this);
		iBtnSelect.setOnTouchListener(TouchEffect.TouchDark);
		tvPageTitle = (TextView)findViewById(R.id.textViewTitle_MSB);
		lvBaseData = (ListView)findViewById(R.id.listViewSelectBaseData_MSB);
		multiSelectListViewAdapter = new MultiSelectListViewAdapter(EmployeeSendToAddressActivity.this);
	}
	
	/**
	 * ��ʼ��listView��ѡ��
	 */
	private void initializeListView(){
		if(sourceItemList==null || sourceItemList.size()==0){	
			sourceItemList= new ArrayList<KeyValueData>();
		}
			multiSelectListViewAdapter.setSourceItemList(sourceItemList);
			lvBaseData.setAdapter(multiSelectListViewAdapter);
			multiSelectListViewAdapter.setCheckBox(false);
			
			multiSelectListViewAdapter.notifyDataSetChanged();
	}
	
	/**
	 * ��ȡ��Ҫ�����Դ�������Ϣ
	 */
	private void getListSource(){
		sourceItemList = mEmployeeSendToAddressService.getBaseDataForBind();
		
		Bundle mBundle = this.getIntent().getExtras();
		if (mBundle != null) {
			listKey = mBundle.getString("listKey");
			
			if (listKey != null) {
				//TODO ��ʻ�
				tvPageTitle.setText(BaseDataType.valueOf(listKey).getValue());
			}
		}	
	}
	
	/**
	 * ��ȡѡ���ѡ��
	 * @return ����Ϊ2������:[0]��Key����"��������,���ʷ���"����Ӧ[1]��Value����"Gbjfsh,Gcxfsh"
	 */
	private String[] getSelectedValue(){
		String[] selectValue = new String[2];
		if(sourceItemList !=null && sourceItemList.size()>0){
			
			StringBuilder sbKey = new StringBuilder();
			StringBuilder sbValue = new StringBuilder();
			for(KeyValueData kv: sourceItemList){
				sbKey.append(kv.getKey()).append(",");
				sbValue.append(kv.getValue()).append(",");		
			}
			
			if(sbKey.length() != 0){
				selectValue[0]=sbKey.substring(0,sbKey.length()-1);
				selectValue[1]=sbValue.substring(0,sbValue.length()-1);
			}		
			
		}
		return selectValue;	
	}	
	
	private void convertArray(String[] selectValues){
		String[] addressNameList = null;
		String[] addressCodeList = null;
		if(selectValues[0]!=null){
			addressNameList = selectValues[0].split(",");
			addressCodeList = selectValues[1].split(",");
			EmployeeSendToAddress employeeSendToAddress=null;
			List<SendToAddress> sList = sendToAddressService.getSendToAddressList();
			mEmployeeSendToAddressService.deleteAllEmployeeSendToAddress();
			for(int i=0;i<addressNameList.length;i++){
				for(SendToAddress st:sList){
					if(st.getCode().equals(addressCodeList[i])&&st.getName().equals(addressNameList[i])){
						employeeSendToAddress = new EmployeeSendToAddress();
						employeeSendToAddress.setCode(st.getCode());
						employeeSendToAddress.setLanguage(st.getLanguage());
						employeeSendToAddress.setLoginname(currentUser);
						employeeSendToAddress.setName(st.getName());
						employeeSendToAddress.setOrder(st.getOrder());
						
						mEmployeeSendToAddressService.addEmployeeSendToAddress(employeeSendToAddress);
					}
				}
			}
		}	
	}
}
