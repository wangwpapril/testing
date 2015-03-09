package com.cuc.miti.phone.xmc.ui;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.adapter.ManuscriptsListAdapter;
import com.cuc.miti.phone.xmc.domain.Enums.ManuscriptStatus;
import com.cuc.miti.phone.xmc.domain.Manuscripts;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.logic.ManuscriptsService;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.TouchEffect;
import com.cuc.miti.phone.xmc.R;

public class EditingManuscriptsActivity extends Activity implements
		OnClickListener {

	// private List<Manuscripts> manuscripts = null;
	private ManuscriptsService service = null;
	private String titleCondition = "";
	private ManuscriptStatus currentStatus = ManuscriptStatus.Editing;
	private Pager pager = null;
	private ManuscriptsListAdapter adapter;

	private ListView listViewManuscripts_EditMA;
	private ImageButton ibtnCheckManuscripts_EditMA;
	private ImageButton ibtnRemoveManuscripts_EditMA;
	private ImageButton ibtnSendManuscripts_EditMA;
	private ImageButton imageBtnBack_EditMA;
	private Button btnEdit_EditMA;
	private TextView textViewCount_EditMA;
	private TextView textViewTip_EditMA;

	private Button btnPre_EditMA;
	private Button btnNext_EditMA;
	private TextView textviewPageNum_EditMA;
	
	private final int REQUEST_OPERATION_EDIT = 0;
	private final int REQUEST_OPERATION_ELIMINATION = 1;
	private final int REQUEST_OPERATION_SEND = 2;

	private final int MESSAGE_BIND_LIST = 0;


	private ProgressDialog dialog = null;

	// �Ƿ�����˱༭״̬
	private boolean editState = false;
	// �Ƿ�ȫѡ
	private boolean checkState = false;

	private static int selectedPosition = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// ȥ�������
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.editing_manuscripts);

		initialize();
		
		IngleApplication.getInstance().addActivity(this);

	}

	private void initialize() {

		selectedPosition = -1;

		initializeButtons();

		initializeListView();
	}

	/**
	 * ��ʼ��listView�б�
	 */
	private void initializeListView() {
		// ��ȡ��ͼ�е�listview�������
		listViewManuscripts_EditMA = (ListView) findViewById(R.id.listViewManuscripts_EditMA);

		service = new ManuscriptsService(this);

		// ��ʼ����ҳ����
		pager = Pager.getDefault();

		// ��ʼ��adpter
		adapter = new ManuscriptsListAdapter(this,
				R.drawable.phone_draftstate_editingdraft_check,
				R.drawable.phone_draftstate_editingdraft_editcheck, listViewManuscripts_EditMA);

		// ע��adapter
		listViewManuscripts_EditMA.setAdapter(adapter);

		showLoadingDialog();

		loadMoreDataThread();


		// ��������Ŀ��
		textViewCount_EditMA.setText(String.valueOf(pager.getTotalNum()));

		//���ó��������˵��¼�
		setContextMenu(true);
		
		// �趨����¼�
		listViewManuscripts_EditMA.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						if (editState) { // �ж��Ƿ��ڸ���б�༭״̬��
							adapter.checkItem(position); // �ǣ�ֻ�ܽ��и��ѡ��
						} else { // �񣬵������ܸ�Ϊ�༭���

							Intent intent = new Intent(
									EditingManuscriptsActivity.this,
									EditManuscriptsActivity.class);
							intent.putExtra("id", adapter.getItem(position)
									.getM_id());
							startActivityForResult(intent,
									REQUEST_OPERATION_EDIT);

							selectedPosition = position;
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
			listViewManuscripts_EditMA.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
				public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
					menu.setHeaderTitle(R.string.contextmenu_operation_tip);
					menu.add(0, REQUEST_OPERATION_ELIMINATION, 0,
							R.string.manuscript_operation_elimination);
					menu.add(0, REQUEST_OPERATION_SEND, 0,
							R.string.manuscript_operation_send);
				}
			});
		}else{
			listViewManuscripts_EditMA.setOnCreateContextMenuListener(null);
		}
	}

	/**
	 * ��ʼ�����水ť
	 */
	private void initializeButtons() {

		btnEdit_EditMA = (Button) findViewById(R.id.btnEdit_EditMA);
		btnEdit_EditMA.setOnClickListener(this);

		ibtnCheckManuscripts_EditMA = (ImageButton) findViewById(R.id.ibtnCheckManuscripts_EditMA);
		ibtnCheckManuscripts_EditMA.setOnClickListener(this);

		imageBtnBack_EditMA = (ImageButton) findViewById(R.id.imageBtnBack_EditMA);
		imageBtnBack_EditMA.setOnClickListener(this);
		imageBtnBack_EditMA.setOnTouchListener(TouchEffect.TouchDark);

		ibtnRemoveManuscripts_EditMA = (ImageButton) findViewById(R.id.ibtnRemoveManuscripts_EditMA);
		ibtnRemoveManuscripts_EditMA.setOnClickListener(this);
		ibtnRemoveManuscripts_EditMA.setOnTouchListener(TouchEffect.TouchLight);

		ibtnSendManuscripts_EditMA = (ImageButton) findViewById(R.id.ibtnSendManuscripts_EditMA);
		ibtnSendManuscripts_EditMA.setOnClickListener(this);
		ibtnSendManuscripts_EditMA.setOnTouchListener(TouchEffect.TouchLight);

		textViewCount_EditMA = (TextView) findViewById(R.id.textViewCount_EditMA);

		btnPre_EditMA = (Button) findViewById(R.id.btnPre_EditMA);
		btnPre_EditMA.setOnClickListener(this);
		

		btnNext_EditMA = (Button) findViewById(R.id.btnNext_EditMA);
		btnNext_EditMA.setOnClickListener(this);

		textViewTip_EditMA = (TextView) findViewById(R.id.textViewTip_EditMA);

		textviewPageNum_EditMA = (TextView) findViewById(R.id.textviewPageNum_EditMA);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		final int position = (int) info.id;

		Manuscripts ma = adapter.getItem(position);
		try {

			switch (item.getItemId()) {
			// case REQUEST_OPERATION_EDIT:
			// // ����༭ҳ
			// Intent intent = new Intent(this, EditManuscriptsActivity.class);
			// intent.putExtra("id", ma.getM_id());
			// startActivityForResult(intent, REQUEST_OPERATION_EDIT);
			//
			// selectedPosition = position;
			// break;
			case REQUEST_OPERATION_ELIMINATION:
				// ��̭�˸��
				adapter.eliminationItem(position);

				if (adapter.getCount() == 0) {
					int temp = pager.getCurrentPage();
					pager.setCurrentPage(temp == 1 ? 1 : --temp);

				}

				loadMoreDataThread();

				break;
			case REQUEST_OPERATION_SEND:
				// ���͸��
				adapter.sentItem(position);

				if (adapter.getCount() == 0) {
					int temp = pager.getCurrentPage();
					pager.setCurrentPage(temp == 1 ? 1 : --temp);

				}

				loadMoreDataThread();

				break;
			default:
				break;
			}

		} catch (Exception e) {
			Logger.e(e);
		}

		return super.onContextItemSelected(item);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {

			switch (requestCode) {
			case REQUEST_OPERATION_EDIT:
				if (selectedPosition >= 0) {
					String mId = adapter.getItem(selectedPosition).getM_id();

					Manuscripts mu = service.getManuscripts(mId);
					if (mu == null)
						return;

					if (mu.getManuscriptsStatus() == ManuscriptStatus.Editing)
						adapter.updateItem(selectedPosition);
					else if (mu.getManuscriptsStatus() == ManuscriptStatus.StandTo) {
						if (adapter.getCount() == 0) {
							int temp = pager.getCurrentPage();
							pager.setCurrentPage(temp == 1 ? 1 : --temp);

						}

						loadMoreDataThread();
					}
				}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * ���ظ������߳�
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
	 * ��̭����߳�
	 */
	private void eliminationDataThread() {
		new Thread(new Runnable() {

			public void run() {
				adapter.eliminationSelectedItems();

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

	/**
	 * ���͸������߳�
	 */
	private void sentDataThread() {
		new Thread(new Runnable() {

			public void run() {
				Looper.prepare();
				
				adapter.sentSelectedItems();

				if (adapter.getCount() == 0) {
					int temp = pager.getCurrentPage();
					pager.setCurrentPage(temp == 1 ? 1 : --temp);

				}

				List<Manuscripts> tempList = loadMoreData();

				Message message = new Message();
				message.obj = tempList;
				message.what = MESSAGE_BIND_LIST;
				handler.sendMessage(message);
				
				Looper.loop();
			}
		}).start();
	}

	/**
	 * �������
	 * 
	 * @return
	 */
	private List<Manuscripts> loadMoreData() {
		try {
			List<Manuscripts> tempList = service.getManuscriptsByPage(pager,
					titleCondition, currentStatus,
					IngleApplication.getInstance()
							.getCurrentUser(), false);
			return tempList;
		} catch (Exception e) {
			Logger.e(e);
		}
		return null;
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

				textViewCount_EditMA
						.setText(String.valueOf(pager.getTotalNum()));
				textviewPageNum_EditMA.setText(String.valueOf(pager
						.getCurrentPage() + "/" + pager.getTotalPageCount()));

				if (tempList == null || tempList.size() == 0)
					textViewTip_EditMA.setVisibility(View.VISIBLE);
				else
					textViewTip_EditMA.setVisibility(View.GONE);

				// �ָ���ѡ״̬
				checkState = false;
				setCheckState(checkState);

				if (dialog != null)
					dialog.dismiss();

			} 

			super.handleMessage(msg);
		}
	};

	public void onClick(View v) {
		int currentPage = 0;
		switch (v.getId()) {
		// ���ñ༭״̬����ť����
		case R.id.btnEdit_EditMA:
			editState = !editState;
			if (editState) {
				this.ibtnCheckManuscripts_EditMA.setVisibility(View.VISIBLE);
				this.ibtnRemoveManuscripts_EditMA.setVisibility(View.VISIBLE);
				this.ibtnSendManuscripts_EditMA.setVisibility(View.VISIBLE);
				setContextMenu(false);
				this.btnEdit_EditMA.setText(R.string.editButton_false_manuscriptList);
			} else {
				this.ibtnCheckManuscripts_EditMA.setVisibility(View.GONE);
				this.ibtnRemoveManuscripts_EditMA.setVisibility(View.GONE);
				this.ibtnSendManuscripts_EditMA.setVisibility(View.GONE);
				setContextMenu(true);
				this.btnEdit_EditMA.setText(R.string.editButton_true_manuscriptList);
			}
			adapter.setEditState(editState);
			break;
		// �����ѡ��ť����ѡ���ø��
		case R.id.ibtnCheckManuscripts_EditMA:

			setCheckState(!checkState);
			adapter.checkAll(checkState);
			break;
		// ��̭
		case R.id.ibtnRemoveManuscripts_EditMA:

			if(this.pager.getTotalNum()==0){
				showAlertDialog(getResources().getString(R.string.noManuOperator));
			}else{
				showLoadingDialog();		
			}
			eliminationDataThread();
			break;
		// ����
		case R.id.ibtnSendManuscripts_EditMA:

			if(this.pager.getTotalNum()==0){
				showAlertDialog(getResources().getString(R.string.noManuOperator));
			}else{
				showSendingDialog();				
			}
			sentDataThread();
			break;
		// ����
		case R.id.imageBtnBack_EditMA:
			setResult(RESULT_CANCELED);
			finish();
			break;
		// ��һҳ
		case R.id.btnPre_EditMA:
			currentPage = this.pager.getCurrentPage();
			this.pager.setCurrentPage(currentPage == 1 ? 1 : --currentPage);

			if(this.pager.getTotalPageCount() == currentPage){
				showAlertDialog(getResources().getString(R.string.currentPageIsFirst));
			}else{
				showLoadingDialog();	
			}
			// ���¼����б���
			loadMoreDataThread();

			listViewManuscripts_EditMA.setSelection(0);
			break;
		// ��һҳ
		case R.id.btnNext_EditMA:
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
		
			listViewManuscripts_EditMA.setSelection(0);
			break;
		default:
			break;
		}
	}

	private void setCheckState(boolean checkState) {

		this.checkState = checkState;

		// ����ͼ�Ķ�ѡ��ťͼƬ
		if (checkState)
			ibtnCheckManuscripts_EditMA.setImageBitmap(BitmapFactory
					.decodeResource(this.getResources(),
							R.drawable.phone_draftstate_editingdraft_check));
		else
			ibtnCheckManuscripts_EditMA
					.setImageBitmap(BitmapFactory.decodeResource(
							this.getResources(),
							R.drawable.phone_draftstate_editingdraft_editcheck));
	}

	private void showAlertDialog(String message){		
		dialog = ProgressDialog.show(this, "", message, true);
	}

	private void showLoadingDialog() {
		String message = this.getResources().getString(R.string.manuscripi_operation_more_waiting);
		dialog = ProgressDialog.show(this, "", message, true);
	}
	
	private void showSendingDialog() {
		String message = this.getResources().getString(R.string.manuscripi_operation_send_waiting);
		dialog = ProgressDialog.show(this, "", message, true);
	}
}
