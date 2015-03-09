package com.cuc.miti.phone.xmc.ui;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
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

public class EliminationManuscriptsActivity extends BaseActivity implements
		OnClickListener {

	private ListView listViewManuscripts_EliMA;
	private ImageButton ibtnCheckManuscripts_EliMA;
	private ImageButton ibtnRemoveManuscripts_EliMA;
	private ImageButton ibtnRecoverManuscripts_EliMA;
	private ImageButton imageBtnBack_EliMA;
	private Button btnEdit_EliMA;
	private TextView textViewCount_EliMA;
	private TextView textViewTip_EliMA;

	private Button btnPre_EliMA;
	private Button btnNext_EliMA;
	private TextView textviewPageNum_EliMA;

	private ManuscriptsListAdapter adapter;
	private ManuscriptsService service = null;
	private String titleCondition = "";
	private ManuscriptStatus currentStatus = ManuscriptStatus.Elimination;
	private Pager pager = null;

	//private static int selectedPosition = -1;
	// �Ƿ�����˱༭״̬
	private boolean editState = false;
	// �Ƿ�ȫѡ
	private boolean checkState = false;

	private final int REQUEST_OPERATION_VIEW = 0;
	private final int REQUEST_OPERATION_RECOVER = 1;
	private final int REQUEST_OPERATION_DELETE = 2;
	
	private final int MESSAGE_BIND_LIST = 0;
	

	private ProgressDialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// ȥ�������
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.elimination_manuscripts);

		initialize();
		IngleApplication.getInstance().addActivity(this);
	}

	public void onClick(View v) {
		int currentPage = 0;
		switch (v.getId()) {
		// ���ñ༭״̬����ť����
		case R.id.btnEdit_EliMA:
			editState = !editState;
			if (editState) {
				this.ibtnCheckManuscripts_EliMA.setVisibility(View.VISIBLE);
				this.ibtnRemoveManuscripts_EliMA.setVisibility(View.VISIBLE);
				this.ibtnRecoverManuscripts_EliMA.setVisibility(View.VISIBLE);
				setContextMenu(false);
				this.btnEdit_EliMA.setText(R.string.editButton_false_manuscriptList);
			} else {
				this.ibtnCheckManuscripts_EliMA.setVisibility(View.GONE);
				this.ibtnRemoveManuscripts_EliMA.setVisibility(View.GONE);
				this.ibtnRecoverManuscripts_EliMA.setVisibility(View.GONE);
				setContextMenu(true);
				this.btnEdit_EliMA.setText(R.string.editButton_true_manuscriptList);
			}
			adapter.setEditState(editState);
			break;
		// �����ѡ��ť����ѡ���ø��
		case R.id.ibtnCheckManuscripts_EliMA:

			//���ù�ѡ״̬
			setCheckState(!this.checkState);
			
			adapter.checkAll(checkState);
			
			break;
		case R.id.ibtnRemoveManuscripts_EliMA:
			
			if(this.pager.getTotalNum()==0){
				showAlertDialog(getResources().getString(R.string.noManuOperator));
			}else{
				showLoadingDialog();			
			}
			// ɾ����
			deleteDataThread();
			break;
		case R.id.ibtnRecoverManuscripts_EliMA:

			if(this.pager.getTotalNum()==0){
				showAlertDialog(getResources().getString(R.string.noManuOperator));
			}else{
				showLoadingDialog();
			}
			// �ָ����
			recoverDataThread();
			break;
		case R.id.imageBtnBack_EliMA:
			setResult(RESULT_CANCELED);
			finish();
			break;
		// ��һҳ
		case R.id.btnPre_EliMA:
			currentPage = this.pager.getCurrentPage();
			this.pager.setCurrentPage(currentPage == 1 ? 1 : --currentPage);

			if(this.pager.getTotalPageCount() == currentPage){
				showAlertDialog(getResources().getString(R.string.currentPageIsFirst));
			}else{
				showLoadingDialog();			
			}
			// ���¼����б���
			loadMoreDataThread();
			
			listViewManuscripts_EliMA.setSelection(0);
			break;
		// ��һҳ
		case R.id.btnNext_EliMA:
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
			
			listViewManuscripts_EliMA.setSelection(0);
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

		Manuscripts ma = adapter.getItem(position);
		try {

			switch (item.getItemId()) {
//			case REQUEST_OPERATION_VIEW:
//				// �鿴���
//				Intent intent = new Intent(this, ViewManuscriptActivity.class);
//				intent.putExtra("id", adapter.getItem(position).getM_id());
//				startActivityForResult(intent, REQUEST_OPERATION_VIEW);
//				break;
			case REQUEST_OPERATION_RECOVER:
				// �ָ����
				adapter.recoverItem(position);

				loadMoreDataThread();
				break;
			case REQUEST_OPERATION_DELETE:

				showLoadingDialog();

				// ����ɾ��˸��
				adapter.deleteItem(position);

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
		btnEdit_EliMA = (Button) findViewById(R.id.btnEdit_EliMA);
		btnEdit_EliMA.setOnClickListener(this);

		// ��ʼ����ѡ��ť
		ibtnCheckManuscripts_EliMA = (ImageButton) findViewById(R.id.ibtnCheckManuscripts_EliMA);
		ibtnCheckManuscripts_EliMA.setOnClickListener(this);

		// ��ʼ�����ذ�ť
		imageBtnBack_EliMA = (ImageButton) findViewById(R.id.imageBtnBack_EliMA);
		imageBtnBack_EliMA.setOnClickListener(this);

		// ��ʼ������ɾ��ť
		ibtnRemoveManuscripts_EliMA = (ImageButton) findViewById(R.id.ibtnRemoveManuscripts_EliMA);
		ibtnRemoveManuscripts_EliMA.setOnClickListener(this);
		ibtnRemoveManuscripts_EliMA.setOnTouchListener(TouchEffect.TouchDark);

		// ��ʼ���ָ���ť
		ibtnRecoverManuscripts_EliMA = (ImageButton) findViewById(R.id.ibtnRecoverManuscripts_EliMA);
		ibtnRecoverManuscripts_EliMA.setOnClickListener(this);
		ibtnRecoverManuscripts_EliMA.setOnTouchListener(TouchEffect.TouchDark);

		// ��ʼ���ı�����ť
		textViewCount_EliMA = (TextView) findViewById(R.id.textViewCount_EliMA);

		btnPre_EliMA = (Button) findViewById(R.id.btnPre_EliMA);
		btnPre_EliMA.setOnClickListener(this);

		btnNext_EliMA = (Button) findViewById(R.id.btnNext_EliMA);
		btnNext_EliMA.setOnClickListener(this);

		textviewPageNum_EliMA = (TextView) findViewById(R.id.textviewPageNum_EliMA);

		textViewTip_EliMA = (TextView) findViewById(R.id.textViewTip_EliMA);
	}

	/**
	 * ��ʼ��listView�б�
	 */
	private void initializeListView() {
		// ��ȡ��ͼ�е�listview�������
		listViewManuscripts_EliMA = (ListView) findViewById(R.id.listViewManuscripts_EliMA);

		service = new ManuscriptsService(this);

		// ��ʼ����ҳ����
		pager = Pager.getDefault();

		// ��ʼ��adpter
		adapter = new ManuscriptsListAdapter(this,
				R.drawable.phone_draftstate_deletedraft_deletecheck,
				R.drawable.phone_draftstate_deletedraft_editcheck, listViewManuscripts_EliMA);

		// ע��adapter
		listViewManuscripts_EliMA.setAdapter(adapter);

		showLoadingDialog();

		loadMoreDataThread();

		// ��������Ŀ��
		textViewCount_EliMA.setText(String.valueOf(pager.getTotalNum()));

		// �趨���������˵��¼�
		setContextMenu(true);
		
		// �趨����¼�
		listViewManuscripts_EliMA
				.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						if (editState) {	//�ж��Ƿ��ڸ���б�༭״̬��
							adapter.checkItem(position); //�ǣ�ֻ�ܽ��и��ѡ��
						}
						else {	// �񣬵������ܸ�Ϊ�鿴���
							
							Intent intent = new Intent(EliminationManuscriptsActivity.this, ViewManuscriptActivity.class);
							intent.putExtra("id", adapter.getItem(position).getM_id());
							startActivityForResult(intent, REQUEST_OPERATION_VIEW);
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
			listViewManuscripts_EliMA.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

				public void onCreateContextMenu(ContextMenu menu, View v,
						ContextMenuInfo menuInfo) {
					menu.setHeaderTitle(R.string.contextmenu_operation_tip);
					//menu.add(0, REQUEST_OPERATION_VIEW, 0, R.string.manuscript_operation_view);
					menu.add(0, REQUEST_OPERATION_RECOVER, 0, R.string.manuscript_operation_recover);
					menu.add(0, REQUEST_OPERATION_DELETE, 0, R.string.manuscript_operation_delete);
				}

			});
		}else{
			listViewManuscripts_EliMA.setOnCreateContextMenuListener(null);
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
	 * @return
	 */
	private List<Manuscripts> loadMoreData(){
		List<Manuscripts> tempList = service.getManuscriptsByPage(
				pager, titleCondition, currentStatus, IngleApplication.getInstance().getCurrentUser(), false);
		return tempList;
	}
	
	/**
	 * �ָ�����߳�
	 */
	private void recoverDataThread() {
		new Thread(new Runnable() {

			public void run() {
				adapter.recoverSelectedItem();
				
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
	 * ɾ����
	 */
	private void deleteDataThread(){
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

				textViewCount_EliMA
						.setText(String.valueOf(pager.getTotalNum()));
				textviewPageNum_EliMA.setText(String.valueOf(pager
						.getCurrentPage() + "/" + pager.getTotalPageCount()));

				if (tempList == null || tempList.size() == 0)
					textViewTip_EliMA.setVisibility(View.VISIBLE);
				else
					textViewTip_EliMA.setVisibility(View.GONE);
				
				//�ָ���ѡ״̬
				checkState = false;
				setCheckState(checkState);

				if (dialog != null)
					dialog.dismiss();
			}
			super.handleMessage(msg);
		}
	};
	
	private void setCheckState(boolean checkState){
		
		this.checkState = checkState;
		
		// ����ͼ�Ķ�ѡ��ťͼƬ
		if (checkState)
			ibtnCheckManuscripts_EliMA
					.setImageBitmap(BitmapFactory.decodeResource(
							this.getResources(),
							R.drawable.phone_draftstate_deletedraft_deletecheck));
		else
			ibtnCheckManuscripts_EliMA
					.setImageBitmap(BitmapFactory.decodeResource(
							this.getResources(),
							R.drawable.phone_draftstate_deletedraft_editcheck));
	}

	private void showLoadingDialog() {
		String message = this.getResources().getString(
				R.string.manuscripi_operation_more_waiting);
		dialog = ProgressDialog.show(this, "", message, true);
	}
	
	private void showAlertDialog(String message){		
		dialog = ProgressDialog.show(this, "", message, true);
	}
}
