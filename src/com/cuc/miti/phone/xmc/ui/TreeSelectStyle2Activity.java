package com.cuc.miti.phone.xmc.ui;

import java.util.List;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.adapter.TreeSelectListViewStyle2Adapter;
import com.cuc.miti.phone.xmc.adapter.TreeSelectListViewStyle2Adapter.NodeClickListener;
import com.cuc.miti.phone.xmc.adapter.TreeSelectListViewStyle2Adapter.NodeClicktEvent;
import com.cuc.miti.phone.xmc.domain.TreeNode;
import com.cuc.miti.phone.xmc.domain.Enums.BaseDataType;
import com.cuc.miti.phone.xmc.logic.NewsCategoryService;
import com.cuc.miti.phone.xmc.logic.RegionService;
import com.cuc.miti.phone.xmc.utils.TouchEffect;
import com.cuc.miti.phone.xmc.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class TreeSelectStyle2Activity extends BaseActivity implements
		OnItemClickListener, OnClickListener, NodeClickListener {

	private TextView tvPageTitle;
	private ListView lvBaseData;
	private ImageButton iBtnBack;
	private ImageButton iBtnStoreChoose;

	private String rootCode = "0";

	private TreeSelectListViewStyle2Adapter adapter = null;
	private String title = ""; // ҳ�����
	private String listKey = ""; // ���Դ��Ӧ���������
	private TreeNode currentNode = null;
	private NewsCategoryService ncServices;
	private RegionService gServices;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ȥ�������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tree_select_basedata);

		this.setUpViews();

		this.init();

		this.getListSource(currentNode.getValue());

		this.initializeListView();
		
		IngleApplication.getInstance().addActivity(this);
	}

	private void init() {
		Bundle mBundle = this.getIntent().getExtras();
		if (mBundle != null) {
			listKey = mBundle.getString("listKey");
		}

		switch (BaseDataType.valueOf(listKey)) {
		case NewsCategory:
			currentNode = new TreeNode(BaseDataType.valueOf(listKey).getValue(), rootCode);
			break;
		case GeographyCategory:
			currentNode = new TreeNode(BaseDataType.valueOf(listKey).getValue(), rootCode);
			break;
		}

	}

	private void getListSource(String code) {

		if (listKey != null) {
			List<TreeNode> tnList = null;
			switch (BaseDataType.valueOf(listKey)) {
			case NewsCategory:
				ncServices = new NewsCategoryService(
						TreeSelectStyle2Activity.this);

				if (currentNode.getChildren() == null
						|| currentNode.getChildren().size() == 0)
					tnList = ncServices.getBaseDataForBind(code);
				else
					tnList = currentNode.getChildren();

				if (tnList != null && tnList.size() > 0) {

					currentNode.setIcon(R.drawable.icon_department); // ����ͼ��
					for (TreeNode tn : tnList) {
						tn.setParent(currentNode);

						List<TreeNode> subList = ncServices
								.getBaseDataForBind(tn.getValue());
						if (subList != null && subList.size() > 0) {
							for (TreeNode temp : subList) {
								temp.setParent(tn);
								tn.add(temp);
							}
						}
						currentNode.add(tn);
					}
				}

				break;
			case GeographyCategory:
				gServices = new RegionService(TreeSelectStyle2Activity.this);

				if (currentNode.getChildren() == null
						|| currentNode.getChildren().size() == 0)
					tnList = gServices.getBaseDataForBind(code);
				else
					tnList = currentNode.getChildren();

				if (tnList != null && tnList.size() > 0) {

					currentNode.setIcon(R.drawable.icon_department); // ����ͼ��
					for (TreeNode tn : tnList) {
						tn.setParent(currentNode);

						List<TreeNode> subList = gServices
								.getBaseDataForBind(tn.getValue());
						if (subList != null && subList.size() > 0) {
							for (TreeNode temp : subList) {
								temp.setParent(tn);
								tn.add(temp);
							}
						}
						currentNode.add(tn);
					}
				}
				break;
			}

			tvPageTitle.setText(currentNode.getText());
		}
	}

	/**
	 * ��ʼ��ҳ��ؼ�
	 */
	private void setUpViews() {
		iBtnBack = (ImageButton) findViewById(R.id.iBtnBack_TSB);
		iBtnBack.setOnClickListener(this);
		iBtnBack.setOnTouchListener(TouchEffect.TouchDark);
		iBtnStoreChoose = (ImageButton) findViewById(R.id.iBtnStoreChoose_TSB);
		iBtnStoreChoose.setOnClickListener(this);
		iBtnStoreChoose.setVisibility(View.INVISIBLE);
		iBtnStoreChoose.setOnTouchListener(TouchEffect.TouchDark);

		tvPageTitle = (TextView) findViewById(R.id.textViewTitle_TSB);
		lvBaseData = (ListView) findViewById(R.id.listViewSelectBaseData_TSB);
		lvBaseData.setOnItemClickListener(this);
		//lvBaseData.setOnItemLongClickListener(this);
	}

	/**
	 * ��ʼ��listView��ѡ��
	 */
	private void initializeListView() {
		if (currentNode != null) {
			adapter = new TreeSelectListViewStyle2Adapter(
					currentNode.getChildren(), this);

			adapter.setNodeClickListener(this);

			lvBaseData.setAdapter(adapter);

		}
	}

	public void onClick(View v) {
		int id = v.getId();
		switch (v.getId()) {
		case R.id.iBtnBack_TSB:
			if (rootCode.equals(currentNode.getValue())) {
				setResult(RESULT_CANCELED);
				finish();
			} else {
				currentNode = currentNode.getParent();
				getListSource(currentNode.getValue());
				adapter.setNodes(currentNode.getChildren());
			}
			break;
		default:
			break;
		}
	}

	// public boolean onItemLongClick(AdapterView<?> parent, View v, int
	// position, long id) {
	// currentNode = adapter.getItem(position);
	//
	// if(currentNode!=null){//�д˽ڵ�
	// Intent intent = new Intent();
	// Bundle mBundle = new Bundle();
	// mBundle.putString("selectName", currentNode.getText());
	// mBundle.putString("selectId", currentNode.getValue());
	// intent.putExtras(mBundle);
	// setResult(Activity.RESULT_OK, intent); //���÷������
	// finish();
	// }
	// return true;
	// }

	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

		currentNode = adapter.getItem(position);

		if (currentNode != null) {// �д˽ڵ�
			Intent intent = new Intent();
			Bundle mBundle = new Bundle();
					
			mBundle.putString("selectId", currentNode.getValue());
			String tempNameString = "";
			while(currentNode.getParent()!=null){
				tempNameString =  "_" + currentNode.getText() + tempNameString;
				currentNode = currentNode.getParent();
			}
			if(tempNameString.length() >0){
				tempNameString = tempNameString.substring(1, tempNameString.length());
			}
			mBundle.putString("selectName", tempNameString);
			//mBundle.putString("selectName", currentNode.getText());
			intent.putExtras(mBundle);
			setResult(Activity.RESULT_OK, intent); // ���÷������
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// KeyEvent.KEYCODE_BACK��?�ز���.
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (rootCode.equals(currentNode.getValue())) {
				setResult(RESULT_CANCELED);
				finish();
			} else {
				currentNode = currentNode.getParent();
				getListSource(currentNode.getValue());
				adapter.setNodes(currentNode.getChildren());
			}
		}
		return true;
	}

	/**
	 * �����б�ڵ�ݽ�����Ϣ
	 * @param e
	 */
	public void onNodeClick(NodeClicktEvent e) {

		currentNode = e.currentNode;
		// currentNode = adapter.getItem(position);

		if (!currentNode.isLeaf()) {

			getListSource(currentNode.getValue());

			adapter.setNodes(currentNode.getChildren());
		}
	}
}
