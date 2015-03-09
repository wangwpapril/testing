package com.cuc.miti.phone.xmc.ui;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.R.string;
import android.accessibilityservice.AccessibilityService;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.accessibility.AccessibilityEvent;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.adapter.ManuscriptsListAdapter;
import com.cuc.miti.phone.xmc.domain.Accessories;
import com.cuc.miti.phone.xmc.domain.Manuscripts;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.Enums.ManuscriptStatus;
import com.cuc.miti.phone.xmc.logic.AccessoriesService;
import com.cuc.miti.phone.xmc.logic.ManuscriptsService;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.MediaHelper;
import com.cuc.miti.phone.xmc.utils.TimeFormatHelper;
import com.cuc.miti.phone.xmc.utils.TouchEffect;
import com.cuc.miti.phone.xmc.R;

public class SentManuscriptsActivity extends BaseActivity implements
		OnClickListener {

	private ListView listViewManuscripts_SendTMA;
	private ImageButton ibtnCheckManuscripts_SendTMA;
	private ImageButton ibtnRemoveManuscripts_SendTMA;
	private ImageButton imageBtnBack_SendTMA;
	private Button btnEdit_SendTMA;
	private TextView textViewCount_SendTMA;

	private Button btnPre_SendTMA;
	private Button btnNext_SendTMA;
	private TextView textviewPageNum_SendTMA;
	private TextView textViewTip_SendTMA;

	private ManuscriptsListAdapter adapter;
	private ManuscriptsService service = null;
	private String titleCondition = "";
	private ManuscriptStatus currentStatus = ManuscriptStatus.Sent;
	private Pager pager = null;

	// private static int selectedPosition = -1;
	// �Ƿ�����˱༭״̬
	private boolean editState = false;
	// �Ƿ�ȫѡ
	private boolean checkState = false;

	// ɾ�������ʶ
	private final int REQUEST_OPERATION_DELETE = 1;
	// �ظ������ʶ
	private final int REQUEST_OPERATION_REBUILD = 0;
	// �ظ������ʶ
	private final int REQUEST_OPERATION_VIEW = 2;

	private ProgressDialog dialog = null;

	private final int MESSAGE_BIND_LIST = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// ȥ�������
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.sent_manuscripts);

		initialize();

		IngleApplication.getInstance().addActivity(this);
	}

	public void onClick(View v) {
		int currentPage = 0;
		switch (v.getId()) {
		// ���ñ༭״̬����ť����
		case R.id.btnEdit_SendTMA:
			editState = !editState;
			if (editState) {
				this.ibtnCheckManuscripts_SendTMA.setVisibility(View.VISIBLE);
				this.ibtnRemoveManuscripts_SendTMA.setVisibility(View.VISIBLE);
				this.btnEdit_SendTMA.setText(R.string.editButton_false_manuscriptList);
				setContextMenu(false);
			} else {
				this.ibtnCheckManuscripts_SendTMA.setVisibility(View.GONE);
				this.ibtnRemoveManuscripts_SendTMA.setVisibility(View.GONE);
				this.btnEdit_SendTMA.setText(R.string.editButton_true_manuscriptList);
				setContextMenu(true);
			}
			adapter.setEditState(editState);
			break;
		// �����ѡ��ť����ѡ���ø��
		case R.id.ibtnCheckManuscripts_SendTMA:

			setCheckState(!checkState);

			adapter.checkAll(checkState);
			// ����ͼ�Ķ�ѡ��ťͼƬ
			break;
		case R.id.ibtnRemoveManuscripts_SendTMA:

			if(this.pager.getTotalNum()==0){
				showAlertDialog(getResources().getString(R.string.noManuOperator));
			}else{
				showLoadingDialog();
			}
			// ɾ����
			deleteDataThread();

			break;
		case R.id.imageBtnBack_SendTMA:
			setResult(RESULT_CANCELED);
			finish();
			break;
		// ��һҳ
		case R.id.btnPre_SendTMA:
			currentPage = this.pager.getCurrentPage();
			this.pager.setCurrentPage(currentPage == 1 ? 1 : --currentPage);

			if(this.pager.getTotalPageCount() == currentPage){
				showAlertDialog(getResources().getString(R.string.currentPageIsFirst));
			}else{
				showLoadingDialog();
			}
			// ���¼����б���
			loadMoreDataThread();

			listViewManuscripts_SendTMA.setSelection(0);
			break;
		// ��һҳ
		case R.id.btnNext_SendTMA:
			currentPage = this.pager.getCurrentPage();
			this.pager.setCurrentPage(currentPage == this.pager
					.getTotalPageCount() ? this.pager.getTotalPageCount()
					: ++currentPage);

			if(this.pager.getTotalPageCount() == currentPage){
				showAlertDialog(getResources().getString(R.string.currentPageIsLast));
			}else{
				showLoadingDialog();
			}
			// ���¼����б���
			loadMoreDataThread();

			listViewManuscripts_SendTMA.setSelection(0);
			break;
		default:
			break;
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

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		final int position = (int) info.id;

		try {

			switch (item.getItemId()) {
			case REQUEST_OPERATION_DELETE:

				showLoadingDialog();

				// ��̭�˸��
				adapter.deleteItem(position);

				loadMoreDataThread();

				break;
			case REQUEST_OPERATION_REBUILD:
				// Intent intent = new Intent(this,
				// ViewManuscriptActivity.class);
				// intent.putExtra("id", adapter.getItem(position).getM_id());
				// startActivityForResult(intent, REQUEST_OPERATION_REBUILD);
				this.rebuildManuscript(adapter.getItem(position).getM_id());
				break;
			default:
				break;
			}

		} catch (Exception e) {
			Logger.e(e);
		}

		return super.onContextItemSelected(item);
	}

	/**
	 * ��ʼ������
	 */
	private void initialize() {
		// ��ʼ����ť
		initializeButtons();

		// ��ʼ���б���ؼ�
		initializeListView();
	}

	/**
	 * ��ʼ�����水ť
	 */
	private void initializeButtons() {

		// ��ʼ���༭��ť
		btnEdit_SendTMA = (Button) findViewById(R.id.btnEdit_SendTMA);
		btnEdit_SendTMA.setOnClickListener(this);

		// ��ʼ����ѡ��ť
		ibtnCheckManuscripts_SendTMA = (ImageButton) findViewById(R.id.ibtnCheckManuscripts_SendTMA);
		ibtnCheckManuscripts_SendTMA.setOnClickListener(this);

		// ��ʼ�����ذ�ť
		imageBtnBack_SendTMA = (ImageButton) findViewById(R.id.imageBtnBack_SendTMA);
		imageBtnBack_SendTMA.setOnClickListener(this);
		imageBtnBack_SendTMA.setOnTouchListener(TouchEffect.TouchDark);

		// ��ʼ����̭��ť
		ibtnRemoveManuscripts_SendTMA = (ImageButton) findViewById(R.id.ibtnRemoveManuscripts_SendTMA);
		ibtnRemoveManuscripts_SendTMA.setOnClickListener(this);

		// ��ʼ���ı�����ť
		textViewCount_SendTMA = (TextView) findViewById(R.id.textViewCount_SendTMA);

		btnPre_SendTMA = (Button) findViewById(R.id.btnPre_SendTMA);
		btnPre_SendTMA.setOnClickListener(this);

		btnNext_SendTMA = (Button) findViewById(R.id.btnNext_SendTMA);
		btnNext_SendTMA.setOnClickListener(this);

		textviewPageNum_SendTMA = (TextView) findViewById(R.id.textviewPageNum_SendTMA);

		textViewTip_SendTMA = (TextView) findViewById(R.id.textViewTip_SendTMA);
	}

	/**
	 * ��ʼ��listView�б�
	 */
	private void initializeListView() {
		// ��ȡ��ͼ�е�listview�������
		listViewManuscripts_SendTMA = (ListView) findViewById(R.id.listViewManuscripts_SendTMA);

		service = new ManuscriptsService(this);

		// ��ʼ����ҳ����
		pager = Pager.getDefault();

		// ��ʼ��adpter
		adapter = new ManuscriptsListAdapter(this,
				R.drawable.phone_draftstate_sentdraftcheck,
				R.drawable.phone_draftstate_sentdraft_editcheck,
				listViewManuscripts_SendTMA);

		// ע��adapter
		listViewManuscripts_SendTMA.setAdapter(adapter);

		showLoadingDialog();

		loadMoreDataThread();

		// ��������Ŀ��
		textViewCount_SendTMA.setText(String.valueOf(pager.getTotalNum()));

		// �趨���������˵��¼�
		setContextMenu(true);
		
		// �趨����¼�
		listViewManuscripts_SendTMA
				.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						if (editState) { // �ж��Ƿ��ڸ���б�༭״̬��
							adapter.checkItem(position); // �ǣ�ֻ�ܽ��и��ѡ��
						} else { // �񣬵������ܸ�Ϊ�鿴���

							Intent intent = new Intent(
									SentManuscriptsActivity.this,
									ViewManuscriptActivity.class);
							intent.putExtra("id", adapter.getItem(position)
									.getM_id());
							startActivity(intent);
						}
					}
				});
	}
	
	/**
	 * ���Ʊ�ҳ����趨���������˵��¼���ContextMenu���Ƿ���Ч
	 * @param switcher 
	 */
	private void setContextMenu(boolean switcher){
		if(switcher){
			
			listViewManuscripts_SendTMA.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

				public void onCreateContextMenu(ContextMenu menu, View v,
						ContextMenuInfo menuInfo) {
					menu.setHeaderTitle(R.string.contextmenu_operation_tip);
					menu.add(0, REQUEST_OPERATION_REBUILD, 0,
							R.string.manuscript_operation_rebuild);
					menu.add(0, REQUEST_OPERATION_DELETE, 0,
							R.string.manuscript_operation_delete);
				}

			});
		}else{
			listViewManuscripts_SendTMA.setOnCreateContextMenuListener(null);
		}
	}

	/**
	 * ���ظ�����
	 */
	private void loadMoreDataThread() {
		new Thread(new Runnable() {

			public void run() {
				List<Manuscripts> tempList = loadMoreData();

				Message message = new Message();
				message.obj = tempList;
				message.what = MESSAGE_BIND_LIST;
				handler.sendMessage(message);
			}
		}).start();
	}

	/**
	 * �������
	 * 
	 * @return
	 */
	private List<Manuscripts> loadMoreData() {
		List<Manuscripts> tempList = service.getManuscriptsByPage(pager,
				titleCondition, currentStatus,
				IngleApplication.getInstance()
						.getCurrentUser(), false);
		return tempList;
	}

	/**
	 * ɾ����
	 */
	private void deleteDataThread() {
		new Thread(new Runnable() {

			public void run() {
				adapter.deleteSelectedItems();

				if (adapter.getCount() == 0) {
					int temp = pager.getCurrentPage();
					pager.setCurrentPage(temp == 1 ? 1 : --temp);

				}

				List<Manuscripts> tempList = loadMoreData();

				Message message = new Message();
				message.obj = tempList;
				message.what = MESSAGE_BIND_LIST;
				handler.sendMessage(message);
			}
		}).start();
	}

	Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == MESSAGE_BIND_LIST) {

				List<Manuscripts> tempList = null;

				if (msg.obj != null) {
					tempList = (List<Manuscripts>) msg.obj;
				}

				adapter.setManuscripts(tempList);

				textViewCount_SendTMA.setText(String.valueOf(pager
						.getTotalNum()));
				textviewPageNum_SendTMA.setText(String.valueOf(pager
						.getCurrentPage() + "/" + pager.getTotalPageCount()));

				if (tempList == null || tempList.size() == 0)
					textViewTip_SendTMA.setVisibility(View.VISIBLE);
				else
					textViewTip_SendTMA.setVisibility(View.GONE);

				checkState = false;
				setCheckState(checkState);

				if (dialog != null)
					dialog.dismiss();
			}
			super.handleMessage(msg);
		}
	};

	private void setCheckState(boolean checkState) {

		this.checkState = checkState;

		// ����ͼ�Ķ�ѡ��ťͼƬ
		if (checkState)
			ibtnCheckManuscripts_SendTMA.setImageBitmap(BitmapFactory
					.decodeResource(this.getResources(),
							R.drawable.phone_draftstate_sentdraftcheck));
		else
			ibtnCheckManuscripts_SendTMA.setImageBitmap(BitmapFactory
					.decodeResource(this.getResources(),
							R.drawable.phone_draftstate_sentdraft_editcheck));
	}

	private void showLoadingDialog() {
		String message = this.getResources().getString(
				R.string.manuscripi_operation_more_waiting);
		dialog = ProgressDialog.show(this, "", message, true);
	}
	
	private void showAlertDialog(String message){		
		dialog = ProgressDialog.show(this, "", message, true);
	}

	/**
	 * �ؽ����
	 * 
	 * @param manuscriptID
	 * @return
	 */
	private void rebuildManuscript(String manuscriptID) {
		Manuscripts mu = service.getManuscripts(manuscriptID);

		mu.setM_id(UUID.randomUUID().toString());
		mu.setCreatetime(TimeFormatHelper.getFormatTime(new Date()));
		mu.getManuscriptTemplate().setCreatetime(TimeFormatHelper.getFormatTime(new Date()));
		mu.setManuscriptsStatus(ManuscriptStatus.Editing);

		List<Accessories> accs = rebuildManuscriptAccs(manuscriptID, mu.getM_id());

		boolean result = CopyManuscript(mu, accs);

		if (result == false) {
			rollback(mu);
		} else {
			String id = mu.getM_id();

			Intent intent = new Intent(this, EditManuscriptsActivity.class);
			intent.putExtra("id", id);
			startActivity(intent);
		}
	}

	private List<Accessories> rebuildManuscriptAccs(String manuscriptID, String newID) {
		AccessoriesService accService = new AccessoriesService(this);

		List<Accessories> accs = accService.getAccessoriesListByMID(manuscriptID);

		for (Accessories acc : accs) {
			acc.setM_id(newID);
			acc.setA_id(UUID.randomUUID().toString());
			try {
				String tempUrlString = MediaHelper.copy2TempStore(acc.getUrl());
				acc.setUrl(tempUrlString);
				acc.setOriginalName(MediaHelper.getFileName(tempUrlString));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return accs;
	}

	private boolean CopyManuscript(Manuscripts mu, List<Accessories> accs) {

		// �½����
		boolean result = false;
		try {
			result = service.addManuscripts(mu);
		} catch (Exception e) {
			Logger.e(e);
			result = false;
		}

		AccessoriesService accService = new AccessoriesService(this);

		// �½������������
		if (result) {
			boolean resultAcc = false;
			for (Accessories acc : accs) {
				try {
					// ���������������ʱ�ļ�Ŀ¼��
					MediaHelper.copy2TempStore(acc.getUrl());

					resultAcc = true;
				} catch (IOException e) {
					Logger.e(e);
					resultAcc = false;
				}
				// ���ɹ����½��������
				if (resultAcc)
					resultAcc = accService.addAccessories(acc);

				// ��ʧ�ܣ����
				if (!resultAcc) {
					result = false;
					break;
				}
			}
		}

		return result;
	}

	/**
	 * �ع�
	 * 
	 * @param mu
	 * @return
	 */
	private boolean rollback(Manuscripts mu) {

		boolean result = false;
		try {
			result = service.deleteById(mu.getM_id());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.e(e);
			result = false;
		}
		return result;
	}
}
