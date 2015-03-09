package com.cuc.miti.phone.xmc.ui;

import java.util.List;

import com.cuc.miti.phone.xmc.adapter.TreeSelectListViewAdapter;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.domain.TreeNode;
import com.cuc.miti.phone.xmc.domain.Enums.BaseDataType;
import com.cuc.miti.phone.xmc.logic.NewsCategoryService;
import com.cuc.miti.phone.xmc.logic.RegionService;
import com.cuc.miti.phone.xmc.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * �Զ���Adapter����Ҫ���������нṹ���ṹ��չ��
 * 
 * @author SongQing
 * 
 */
public class TreeSelectActivity extends BaseActivity implements
		OnItemClickListener, OnItemLongClickListener, OnClickListener {

	private TextView tvPageTitle;
	private ListView lvBaseData;
	private ImageButton iBtnBack;
	private ImageButton iBtnStoreChoose;

	private TreeSelectListViewAdapter treeSelectListViewAdapter = null;
	private String title = ""; // ҳ�����
	private String listKey = ""; // ���Դ��Ӧ���������
	private TreeNode treeNodes = null;
	private NewsCategoryService ncServices;
	private RegionService gServices;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ȥ�������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tree_select_basedata);

		this.getListSource();
		this.setUpViews();
		this.initializeListView();
	}

	/**
	 * ��ȡͨ��Intent Bundle�������Ҫ�����Դ�������Ϣ ���� ���ԴList �� ���Դ�����
	 */
	private void getListSource() {
		Bundle mBundle = this.getIntent().getExtras();
		if (mBundle != null) {
			listKey = mBundle.getString("listKey");
		}

		if (listKey != null) {
			List<TreeNode> tnList = null;
			switch (BaseDataType.valueOf(listKey)) {
			case NewsCategory:
				ncServices = new NewsCategoryService(TreeSelectActivity.this);

				/* treeNodes = ncServices.getBaseDataForBind(); �ݹ�ȡ��ȫ����� */
				tnList = ncServices.getBaseDataForBind("0");
				if (tnList != null && tnList.size() > 0) {
					treeNodes = new TreeNode("����", "0"); // ���ø�ڵ�
					treeNodes.setIcon(R.drawable.icon_department); // ����ͼ��
					for (TreeNode tn : tnList) {
						tn.setParent(treeNodes);
						treeNodes.add(tn);
					}
				}
				title = BaseDataType.valueOf(listKey).getValue();
				break;
			case GeographyCategory:
				gServices = new RegionService(TreeSelectActivity.this);
				tnList = gServices.getBaseDataForBind("0");
				if (tnList != null && tnList.size() > 0) {
					treeNodes = new TreeNode("����", "0"); // ���ø�ڵ�
					treeNodes.setIcon(R.drawable.icon_department); // ����ͼ��
					for (TreeNode tn : tnList) {
						tn.setParent(treeNodes);
						treeNodes.add(tn);
					}
				}
				title = BaseDataType.valueOf(listKey).getValue();
				break;
			}
		}
	}

	/**
	 * ��ʼ��listView��ѡ��
	 */
	private void initializeListView() {
		if (treeNodes != null) {
			treeSelectListViewAdapter = new TreeSelectListViewAdapter(
					TreeSelectActivity.this, treeNodes);
			// ����������Ƿ���ʾ��ѡ��
			treeSelectListViewAdapter.setCheckBox(false);
			// ����չ�����۵�ʱͼ��
			treeSelectListViewAdapter.setExpandedCollapsedIcon(
					R.drawable.tree_ex, R.drawable.tree_ec);
			// ����Ĭ��չ������
			treeSelectListViewAdapter.setExpandLevel(2);
			lvBaseData.setAdapter(treeSelectListViewAdapter);
		}
	}

	/**
	 * ��ʼ��ҳ��ؼ�
	 */
	private void setUpViews() {
		iBtnBack = (ImageButton) findViewById(R.id.iBtnBack_TSB);
		iBtnBack.setOnClickListener(this);
		iBtnStoreChoose = (ImageButton) findViewById(R.id.iBtnStoreChoose_TSB);
		iBtnStoreChoose.setOnClickListener(this);
		iBtnStoreChoose.setVisibility(View.INVISIBLE);
		tvPageTitle = (TextView) findViewById(R.id.textViewTitle_TSB);
		tvPageTitle.setText(this.title);
		lvBaseData = (ListView) findViewById(R.id.listViewSelectBaseData_TSB);
		lvBaseData.setOnItemClickListener(this);
		lvBaseData.setOnItemLongClickListener(this);
	}

	/**
	 * ListView�е�������Ӧ�¼�
	 */
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (listKey != null) {
			List<TreeNode> tnList = null;
			int listPosition = 0;
			// ��ȡ��ǰ�����Item�����TreeNodeʵ��
			TreeNode node = (TreeNode) ((TreeSelectListViewAdapter) parent
					.getAdapter()).getItem(position);
			if (node != null) {// �д˽ڵ�
				List<TreeNode> childrenNode = node.getChildren();
				if (childrenNode == null || childrenNode.size() == 0) { // û���ӽڵ�
					switch (BaseDataType.valueOf(listKey)) {
					case NewsCategory:
						tnList = ncServices.getBaseDataForBind(node.getValue());
						if (tnList != null && tnList.size() > 0) {
							for (TreeNode tn : tnList) {
								tn.setParent(node);
								node.add(tn);
							}
							node.setExpanded(false);
							listPosition = ((TreeSelectListViewAdapter) parent
									.getAdapter()).getItemPosition(node);
							((TreeSelectListViewAdapter) parent.getAdapter())
									.removeNode(listPosition);
							((TreeSelectListViewAdapter) parent.getAdapter())
									.addNode(listPosition, node);
						}
						break;
					case GeographyCategory:
						tnList = gServices.getBaseDataForBind(node.getValue());
						if (tnList != null && tnList.size() > 0) {
							for (TreeNode tn : tnList) {
								tn.setParent(node);
								node.add(tn);
							}
							node.setExpanded(false);
							listPosition = ((TreeSelectListViewAdapter) parent
									.getAdapter()).getItemPosition(node);
							((TreeSelectListViewAdapter) parent.getAdapter())
									.removeNode(listPosition);
							((TreeSelectListViewAdapter) parent.getAdapter())
									.addNode(listPosition, node);
						}
						break;
					}
				}
			}
		}
		// ��仰д�������
		((TreeSelectListViewAdapter) parent.getAdapter())
				.ExpandOrCollapse(position);
	}

	/**
	 * ListView�е�������¼���Ӧ
	 */
	public boolean onItemLongClick(AdapterView<?> arg0, View view,
			int position, long arg3) {
		// ��ȡ��ǰ�����Item�����TreeNodeʵ��
		TreeNode node = (TreeNode) treeSelectListViewAdapter.getItem(position);
		if (node != null) {// �д˽ڵ�
			KeyValueData kvItem = new KeyValueData();
			Intent intent = new Intent();
			Bundle mBundle = new Bundle();
			mBundle.putString("selectName", node.getText());
			mBundle.putString("selectId", node.getValue());
			intent.putExtras(mBundle);
			setResult(Activity.RESULT_OK, intent); // ���÷������
			finish();
		}
		return true;
	}

	/**
	 * ҳ��ؼ��������Ӧ�¼�
	 */
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
			// mChecked = multiSelectListViewAdapter.getSelectedItems();
			// mBundle.putCharSequenceArray("selected", getSelectedValue());
			// intent.putExtras(mBundle);
			setResult(Activity.RESULT_OK, intent); // ���÷������
			finish();
			break;
		default:
			break;
		}
	}
}