package com.cuc.miti.phone.xmc.ui;

import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.adapter.MultiSelectListViewAdapter;
import com.cuc.miti.phone.xmc.domain.EmployeeSendToAddress;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.domain.UserAttribute;
import com.cuc.miti.phone.xmc.domain.Enums.BaseDataType;
import com.cuc.miti.phone.xmc.domain.Enums.NetStatus;
import com.cuc.miti.phone.xmc.domain.SendToAddress;
import com.cuc.miti.phone.xmc.logic.ComeFromAddressService;
import com.cuc.miti.phone.xmc.logic.EmployeeSendToAddressService;
import com.cuc.miti.phone.xmc.logic.KeywordsService;
import com.cuc.miti.phone.xmc.logic.PlaceService;
import com.cuc.miti.phone.xmc.logic.SendToAddressService;
import com.cuc.miti.phone.xmc.logic.UserService;
import com.cuc.miti.phone.xmc.ui.MultiSelectActivity.TextChangedWatcher;
import com.cuc.miti.phone.xmc.utils.TouchEffect;
import com.cuc.miti.phone.xmc.R;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

/**
 * �û������ַ_С��ַ������ ֻ�е��û�����ʽԱ�������ã����򷵻���ҳ������ʾ
 * 
 * @author SongQing
 * 
 */
public class EmployeeSendToAddress2Activity extends BaseActivity implements
		OnClickListener {

	private List<KeyValueData> sourceItemList = null; // ��Ҫ�󶨵����Դ
	private List<KeyValueData> selectItemList = null; // �Ѿ�ѡ���ѡ��
	private MultiSelectListViewAdapter multiSelectListViewAdapter;

	private EmployeeSendToAddressService mEmployeeSendToAddressService; // �û�С��ַ
	private SendToAddressService sendToAddressService; // ��ַ�ܱ�
	private TextView tvPageTitle;
	private ListView lvBaseData;
	private ImageButton iBtnBack;
	private ImageButton iBtnSelect;
	private EditText etSearch; // ������
	private String listKey = "";
	private static String currentUser;
	private List<Boolean> mChecked = null;
	private List<KeyValueData> mCheckedValue = null; // ��ֵ�ԣ�Key��id��value�Ǹ�id���checked״̬
	private UserAttribute userAttribute;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ȥ�������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.multi_select_basedata);

		this.initialize();
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

	/**
	 * ��ʼ��
	 */
	private void initialize() {

		this.mEmployeeSendToAddressService = new EmployeeSendToAddressService(
				this);
		this.sendToAddressService = new SendToAddressService(this);
		currentUser = IngleApplication.getInstance()
				.getCurrentUser();
		userAttribute = IngleApplication.getInstance().currentUserInfo
				.getUserattribute();

		this.setUpViews();
		this.getListSource();
		this.initializeCheckedData();
		this.initializeListView();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iBtnBack_MSB:
			setResult(RESULT_CANCELED);
			finish();
			break;
		case R.id.iBtnStoreChoose_MSB:
			this.storeSelectValue();
			setResult(RESULT_OK);
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
		iBtnSelect = (ImageButton) findViewById(R.id.iBtnStoreChoose_MSB);
		// iBtnSelect.setBackgroundResource(R.drawable.draftlabel_submit_960x540);
		iBtnSelect.setOnClickListener(this);
		iBtnSelect.setOnTouchListener(TouchEffect.TouchDark);
		tvPageTitle = (TextView) findViewById(R.id.textViewTitle_MSB);
		tvPageTitle.setText(R.string.title_ESTA);
		etSearch = (EditText) findViewById(R.id.editTextSearch_MSB);
		etSearch.addTextChangedListener(new TextChangedWatcher());
		lvBaseData = (ListView) findViewById(R.id.listViewSelectBaseData_MSB);
		multiSelectListViewAdapter = new MultiSelectListViewAdapter(
				EmployeeSendToAddress2Activity.this);

	}

	/**
	 * ��ʼ��listView��ѡ��
	 */
	private void initializeListView() {
		if (sourceItemList == null || sourceItemList.size() == 0) {
			sourceItemList = new ArrayList<KeyValueData>();
		}
		multiSelectListViewAdapter.setSourceItemList(sourceItemList);
		lvBaseData.setAdapter(multiSelectListViewAdapter);
		multiSelectListViewAdapter.setCheckBox(true);

		multiSelectListViewAdapter.notifyDataSetChanged();
	}

	/**
	 * ��ȡ��Ҫ�����Դ�������Ϣ
	 */
	private void getListSource() {

		sourceItemList = sendToAddressService
				.getAllBaseData(
						IngleApplication.getInstance().currentUserInfo,
						"");

		Bundle mBundle = this.getIntent().getExtras();
		if (mBundle != null) {
			listKey = mBundle.getString("listKey");

			if (listKey != null) {
				// TODO ��ʻ�
				tvPageTitle.setText(BaseDataType.valueOf(listKey).getValue());
			}
		}
	}

	/**
	 * ��ȡ�û��Զ����ַ�б��ʼ��CheckBoxѡ��״̬��¼�б�mChecked
	 */
	private void initializeCheckedData() {

		selectItemList = sendToAddressService
				.getCombineBaseDataForBind(
						IngleApplication.getInstance().currentUserInfo,
						"");
		// ���ԭʼ��ԭʼ��ݵ�������ʼ����¼ѡ��״̬��List<Boolean> mChecked ��ֵȫΪĬ�ϳ�ʼֵ:false
		mChecked = new ArrayList<Boolean>();
		mCheckedValue = new ArrayList<KeyValueData>();
		for (int i = 0; i < sourceItemList.size(); i++) {
			mChecked.add(false);
		}

		if (selectItemList != null && selectItemList.size() > 0) {
			for (KeyValueData kv : selectItemList) {
				int tempPosition = sourceItemList.indexOf(kv);
				mChecked.set(tempPosition, true);
				mCheckedValue.add(kv);
			}
		}
		multiSelectListViewAdapter.setSelectedItems(mChecked);
		multiSelectListViewAdapter.setCheckedItemList(mCheckedValue);
	}

	/**
	 * ��ݱ����ѡ�����б?����checked��ֵ
	 */
	private void setCheckedData(List<KeyValueData> checkedList) {
		if (sourceItemList == null) {
			sourceItemList = new ArrayList<KeyValueData>();
		}
		mChecked = new ArrayList<Boolean>();
		for (int i = 0; i < sourceItemList.size(); i++) {
			mChecked.add(false);
		}

		if (checkedList != null && checkedList.size() > 0) {
			for (KeyValueData kv : checkedList) {
				int tempPosition = sourceItemList.indexOf(kv);
				if (tempPosition != -1) {
					mChecked.set(tempPosition, true);
				}
			}
		}

		multiSelectListViewAdapter.setSourceItemList(sourceItemList);
		multiSelectListViewAdapter.setSelectedItems(mChecked);
		multiSelectListViewAdapter.notifyDataSetChanged();
	}

	/**
	 * ��ȡѡ���ѡ��
	 * 
	 * @return
	 */
	private void getSelectedValue() {

		// mChecked = multiSelectListViewAdapter.getSelectedItems();
		// selectItemList = new ArrayList<KeyValueData>();
		// for (int i = 0; i < mChecked.size(); i++) {
		// if (mChecked.get(i)) {
		// selectItemList.add(sourceItemList.get(i));
		// }
		// }
	}

	/**
	 * ����ѡ��
	 */
	private void storeSelectValue() {
		List<KeyValueData> selectedList = multiSelectListViewAdapter
				.getCheckedItemList();

		List<SendToAddress> sList = sendToAddressService.getSendToAddressList();
		List<KeyValueData> chooseList = userAttribute.getChoosedAddressList();
		mEmployeeSendToAddressService.deleteAllEmployeeSendToAddress();
		userAttribute.cleanChoosedAddress();
		EmployeeSendToAddress employeeSendToAddress = null;
		for (KeyValueData kv : selectedList) {
			if (chooseList != null)
				for (KeyValueData kvs : chooseList) {
					if (kvs.equals(kv))
						userAttribute.addChoosedAddress(kvs);
				}
			if (sList != null)
				for (SendToAddress st : sList) {
					if (st.getCode().equals(kv.getValue())
							&& st.getName().equals(kv.getKey())) {
						employeeSendToAddress = new EmployeeSendToAddress();
						employeeSendToAddress.setCode(st.getCode());
						employeeSendToAddress.setLanguage(st.getLanguage());
						employeeSendToAddress.setLoginname(currentUser);
						employeeSendToAddress.setName(st.getName());
						employeeSendToAddress.setOrder(st.getOrder());

						mEmployeeSendToAddressService
								.addEmployeeSendToAddress(employeeSendToAddress);
					}
				}
		}

	}

	private void BindListView(String queryString) {
		sourceItemList = sendToAddressService
				.getAllBaseData(
						IngleApplication.getInstance().currentUserInfo,
						queryString);

		Bundle mBundle = this.getIntent().getExtras();
		if (mBundle != null) {
			listKey = mBundle.getString("listKey");

			if (listKey != null) {
				// TODO ��ʻ�
				tvPageTitle.setText(BaseDataType.valueOf(listKey).getValue());
			}
		}

	}

	/**
	 * ���EditText�е����ֱ仯
	 * 
	 * @author SongQing
	 * 
	 */
	class TextChangedWatcher implements TextWatcher {

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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// KeyEvent.KEYCODE_BACK��?�ز���.
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(RESULT_CANCELED);
			finish();
		}
		return true;
	}

}
