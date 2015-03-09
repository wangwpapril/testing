package com.cuc.miti.phone.xmc.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.adapter.StandToManuscriptsListAdapter;
import com.cuc.miti.phone.xmc.domain.Enums.ManuscriptStatus;
import com.cuc.miti.phone.xmc.domain.Enums.UploadTaskStatus;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.StandToUploadManuscripts;
import com.cuc.miti.phone.xmc.domain.UploadTask;
import com.cuc.miti.phone.xmc.logic.AccessoriesService;
import com.cuc.miti.phone.xmc.logic.ManuscriptsService;
import com.cuc.miti.phone.xmc.logic.UploadTaskService;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.TouchEffect;
import com.cuc.miti.phone.xmc.utils.uploadtask.FileUploadTaskHelper;
import com.cuc.miti.phone.xmc.utils.uploadtask.UploadTaskJob;
import com.cuc.miti.phone.xmc.R;

/**
 * �������?����
 * @author SongQing
 *
 */
public class StandToManuscriptsActivity extends BaseActivity implements OnClickListener {
	
	private ListView listViewManuscripts_StandTMA;
	private ImageButton iBtnCheckManuscripts_StandTMA,
								iBtnStartManuscripts_StandTMA,
								iBtnPauseManuscripts_StandTMA,
								iBtnRemoveManuscripts_StandTMA,
								iBtnRestartManuscripts_StandTMA,
								iBtnBack_StandTMA;
	private Button btnEdit_StandTMA,
						btnPre_StandMA,
						btnNext_StandMA;
	private TextView textViewCount_StandTMA,
						textViewTip_StandTMA,
						textviewPageNum_SMA;

	private ManuscriptsService manuscriptsService = null;
	private AccessoriesService accessoriesService = null;
	private StandToManuscriptsListAdapter standToManuscriptsListAdapter = null;
	private UploadTaskService uploadTaskService = null;
	
	private Pager pager = null;	
	private ProgressDialog dialog = null;
	private boolean editState = false;										// �Ƿ�����˱༭״̬
	private boolean checkState = false;										// �Ƿ�ȫѡ
	private ManuscriptStatus manuscriptStatus =null;				//�����״̬(������ݿ�ĸ��״̬�ֶβ���)
	private static int selectedPosition = -1;								//ѡ���λ��
	private String currentUser = "";											//��ǰ�û�
	private String titleCondition = "";
	private boolean pauseLoadViewForEdit = false;					//���û�����༭ʱ����ͣListView��ˢ��
	
	/* ������ʶ */
	private final int REQUEST_OPERATION_START = 0;
	private final int REQUEST_OPERATION_PAUSE = 1;
	private final int REQUEST_OPERATION_RESTART = 2;
	private final int REQUEST_OPERATION_CANCEL = 3;
	private final int REQUEST_OPERATION_VIEW = 4;
	
	private final int LOAD_MORE_ITEMS = 100;
	private final int PAUSE_UPLOAD_ITEM = 200;
	private final int START_UPLOAD_ITEM = 201;
	private final int REMOVE_UPLOAD_ITEM = 202;
	private final int RESTART_UPLOAD_ITEM = 203;
	Timer timer = new Timer();   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.standto_manuscripts);
		this.initialize();
		timer.schedule(timerTask,1000,1000);
		
		IngleApplication.getInstance().addActivity(this);
	}
	
	@Override
	protected void onStart(){
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//handler.removeCallbacks(runnable);// �رն�ʱ������
		if (timer != null) {
			timer.cancel( );
			timer = null;
			}
	}
	
	/**
	 * ��ʱ������������б�
	 */
	TimerTask timerTask = new TimerTask() {
		
		@Override
		public void run() {
			List<StandToUploadManuscripts> tempList = loadMoreData();
			Message message = new Message();
			message.obj = tempList;
			message.what = LOAD_MORE_ITEMS;
			while(pauseLoadViewForEdit){
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			handler.sendMessage(message); 
		}
	};
	
	/**
	 * ��ʼ��
	 */
	private void initialize() {
		manuscriptsService = new ManuscriptsService(StandToManuscriptsActivity.this);
		accessoriesService = new AccessoriesService(StandToManuscriptsActivity.this);
		uploadTaskService = new UploadTaskService(StandToManuscriptsActivity.this);
		selectedPosition = -1;
		currentUser = IngleApplication.getInstance().getCurrentUser();		
		this.initializeButtons();
		this.initializeListView();
	}

	/**
	 * ��ʼ��listView�б�
	 */
	private void initializeListView() {
		// ��ȡ��ͼ�е�listview�������
		listViewManuscripts_StandTMA = (ListView) findViewById(R.id.listViewManuscripts_StandTMA);
		
		// ��ʼ����ҳ����
		pager = Pager.getDefault();
		
		//��ʼ��Adapter
		standToManuscriptsListAdapter = new StandToManuscriptsListAdapter(StandToManuscriptsActivity.this,listViewManuscripts_StandTMA);

		// ע��adapter
		listViewManuscripts_StandTMA.setAdapter(standToManuscriptsListAdapter);

		// ��������Ŀ��
		textViewCount_StandTMA.setText(String.valueOf(pager.getTotalNum()));

		// �趨���������˵��¼�
		setContextMenu(true);
		
		// �趨����¼�
		listViewManuscripts_StandTMA.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				//TODO �ڷ�������ò鿴
				if (editState) { // �ж��Ƿ��ڸ���б�༭״̬��
					standToManuscriptsListAdapter.checkItem(position); // �ǣ�ֻ�ܽ��и��ѡ��
				} else { // �񣬵������ܸ�Ϊ�༭���

					Intent intent = new Intent(StandToManuscriptsActivity.this,ViewManuscriptActivity.class);
					intent.putExtra("id", standToManuscriptsListAdapter.getItem(position).getManuscripts().getM_id());
					startActivity(intent);			//�鿴���

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
			
			listViewManuscripts_StandTMA	.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
						public void onCreateContextMenu(ContextMenu menu, View v,
								ContextMenuInfo menuInfo) {
							menu.setHeaderTitle(R.string.contextmenu_operation_tip);
							menu.add(0, 0, 0, R.string.manuscript_operation_start);
							menu.add(0, 1, 0,R.string.manuscript_operation_pause);
							menu.add(0, 2, 0, R.string.manuscript_operation_restart);
							menu.add(0, 3, 0, R.string.manuscript_operation_cancel);
						}

					});
		}else{
			listViewManuscripts_StandTMA.setOnCreateContextMenuListener(null);
		}
	}

	/**
	 * ��ʼ�����水ť
	 */
	private void initializeButtons() {

		iBtnCheckManuscripts_StandTMA = (ImageButton) findViewById(R.id.ibtnCheckManuscripts_StandTMA);
		iBtnStartManuscripts_StandTMA = (ImageButton) findViewById(R.id.ibtnStartManuscripts_StandTMA);
		iBtnPauseManuscripts_StandTMA = (ImageButton) findViewById(R.id.ibtnPauseManuscripts_StandTMA);
		iBtnRemoveManuscripts_StandTMA = (ImageButton) findViewById(R.id.ibtnRemoveManuscripts_StandTMA);
		iBtnRestartManuscripts_StandTMA = (ImageButton) findViewById(R.id.ibtnRestartManuscripts_StandTMA);
		iBtnBack_StandTMA = (ImageButton) findViewById(R.id.iBtnBack_StandTMA);
		
		btnEdit_StandTMA = (Button) findViewById(R.id.btnEdit_StandTMA);
		btnPre_StandMA = (Button) findViewById(R.id.btnPre_StandTMA);
		btnNext_StandMA = (Button) findViewById(R.id.btnNext_StandTMA);
		
		iBtnCheckManuscripts_StandTMA.setOnClickListener(this);
		iBtnStartManuscripts_StandTMA.setOnClickListener(this);
		iBtnPauseManuscripts_StandTMA.setOnClickListener(this);
		iBtnRemoveManuscripts_StandTMA.setOnClickListener(this);
		iBtnRestartManuscripts_StandTMA.setOnClickListener(this);
		this.iBtnStartManuscripts_StandTMA.setVisibility(View.GONE);
		this.iBtnPauseManuscripts_StandTMA.setVisibility(View.GONE);
		this.iBtnRemoveManuscripts_StandTMA.setVisibility(View.GONE);
		this.iBtnRestartManuscripts_StandTMA	.setVisibility(View.GONE);
		this.iBtnCheckManuscripts_StandTMA.setVisibility(View.GONE);
		this.btnEdit_StandTMA.setText(R.string.editButton_true_manuscriptList);
		
		iBtnBack_StandTMA.setOnClickListener(this);
		iBtnBack_StandTMA.setOnTouchListener(TouchEffect.TouchDark);
		
		btnEdit_StandTMA.setOnClickListener(this);
		btnPre_StandMA.setOnClickListener(this);
		btnNext_StandMA.setOnClickListener(this);

		textViewCount_StandTMA = (TextView) findViewById(R.id.textViewCount_StandTMA);
		textViewTip_StandTMA = (TextView) findViewById(R.id.textViewTip_StandTMA);
		textviewPageNum_SMA = (TextView) findViewById(R.id.textviewPageNum_StandTMA);

	}

	//*******************************************ҳ�����¼�*******************************************//
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		final int position = (int) info.id;
		
		try {

			switch (item.getItemId()) {
			case REQUEST_OPERATION_START:
				standToManuscriptsListAdapter.startItem(position);
				selectedPosition = position;
				break;
			case REQUEST_OPERATION_CANCEL:
				standToManuscriptsListAdapter.removeItem(position);

				if (standToManuscriptsListAdapter.getCount() == 0) {
					int temp = pager.getCurrentPage();
					pager.setCurrentPage(temp == 1 ? 1 : --temp);
					pager.setTotalNum(0);
				}

				//loadMoreDataThread();

				break;
			case REQUEST_OPERATION_PAUSE:
				standToManuscriptsListAdapter.pauseItem(position);
				break;
			case REQUEST_OPERATION_RESTART:
				standToManuscriptsListAdapter.restartItem(position);
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
//		if (resultCode == RESULT_OK) {
//
//			switch (requestCode) {
//			case REQUEST_OPERATION_EDIT:
//				if (selectedPosition >= 0) {
//					adapter.updateItem(selectedPosition);
//				}
//				break;
//			default:
//				break;
//			}
//		}
	}

	private Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			if (msg.obj != null) {
				List<StandToUploadManuscripts> tempList = null;
				switch(msg.what){
					case REMOVE_UPLOAD_ITEM:
						tempList = (List<StandToUploadManuscripts>) msg.obj;
						setPageViewValue(tempList);
						break;
					case LOAD_MORE_ITEMS:
						tempList = (List<StandToUploadManuscripts>) msg.obj;
						setPageViewValue(tempList);	
						break;
					case START_UPLOAD_ITEM:
						break;
					case RESTART_UPLOAD_ITEM:
						tempList = (List<StandToUploadManuscripts>) msg.obj;
						setPageViewValue(tempList);	
					case PAUSE_UPLOAD_ITEM:
						break;
					default:
						break;
				}
		}
			if (dialog != null){dialog.dismiss();}	
			super.handleMessage(msg);
		}
	};
	
	/**
	 * ����ҳ��ؼ���ֵ
	 */
	private void setPageViewValue(List<StandToUploadManuscripts> standToUploadManuscriptsList){
		if(standToUploadManuscriptsList==null){return;}
		try {
			standToManuscriptsListAdapter.setManuscripts(standToUploadManuscriptsList);
			
			textViewCount_StandTMA.setText(String.valueOf(pager.getTotalNum()));
			textviewPageNum_SMA.setText(String.valueOf(pager.getCurrentPage() + "/" + pager.getTotalPageCount()));

			if (standToUploadManuscriptsList == null || standToUploadManuscriptsList.size() == 0)
				textViewTip_StandTMA.setVisibility(View.VISIBLE);
			else
				textViewTip_StandTMA.setVisibility(View.GONE);
			
			//�ָ���ѡ״̬
			checkState = false;
			setCheckState(checkState);
		} catch (Exception e) {
			Logger.e(e);
		}
	}
	
	/**
	 * ���ظ������߳�
	 */
	private void loadMoreDataThread() {
		new Thread(new Runnable() {

			public void run() {
				List<StandToUploadManuscripts> tempList = loadMoreData();

				Message message = new Message();
				//message.obj = tempList;
				message.what = LOAD_MORE_ITEMS;
				handler.sendMessage(message);
			}
		}).start();
	}

	/**
	 * ��ͣ�����ϴ��еĸ��������ִ���߳�
	 */
	private void pauseDataThread() {
		new Thread(new Runnable() {

			public void run() {
				standToManuscriptsListAdapter.pauseSelectedItems();

				if (standToManuscriptsListAdapter.getCount() == 0) {
					int temp = pager.getCurrentPage();
					pager.setCurrentPage(temp == 1 ? 1 : --temp);

				}

				List<StandToUploadManuscripts> tempList = loadMoreData();

				Message message = new Message();
				message.obj = tempList;
				message.what = PAUSE_UPLOAD_ITEM;
				handler.sendMessage(message);
			}
		}).start();
	}
	
	/**
	 * �ֹ���ʼ�����Ϣ�����أ�����ִ���߳�
	 */
	private void startDataThread(){
		new Thread(new Runnable() {

			public void run() {
				standToManuscriptsListAdapter.startSelectedItems();

				if (standToManuscriptsListAdapter.getCount() == 0) {
					int temp = pager.getCurrentPage();
					pager.setCurrentPage(temp == 1 ? 1 : --temp);

				}

				List<StandToUploadManuscripts> tempList = loadMoreData();

				Message message = new Message();
				message.obj = tempList;
				message.what = START_UPLOAD_ITEM;
				handler.sendMessage(message);
			}
		}).start();
	}
	
	/**
	 * ���¿�ʼ������أ�����ִ���߳�
	 */
	private void restartDataThread(){
		new Thread(new Runnable() {

			public void run() {
				standToManuscriptsListAdapter.restartSelectedItems();

				if (standToManuscriptsListAdapter.getCount() == 0) {
					int temp = pager.getCurrentPage();
					pager.setCurrentPage(temp == 1 ? 1 : --temp);

				}

				List<StandToUploadManuscripts> tempList = loadMoreData();

				Message message = new Message();
				message.obj = tempList;
				message.what = RESTART_UPLOAD_ITEM;
				handler.sendMessage(message);
			}
		}).start();
	}

	/**
	 * ��ԭ���ظ��Ϊ�ڱ�״̬������ִ���߳�
	 */
	private void removeDataThread(){
		new Thread(new Runnable() {

			public void run() {
				standToManuscriptsListAdapter.removeSelectedItems();

				if (standToManuscriptsListAdapter.getCount() == 0) {
					int temp = pager.getCurrentPage();
					pager.setCurrentPage(temp == 1 ? 1 : --temp);

				}

				List<StandToUploadManuscripts> tempList = loadMoreData();

				Message message = new Message();
				message.obj = tempList;
				message.what = REMOVE_UPLOAD_ITEM;
				handler.sendMessage(message);
			}
		}).start();
	}
	
	/**
	 * �������
	 * 
	 * @return
	 */
	private List<StandToUploadManuscripts> loadMoreData() {	
		List<UploadTask> uploadTaskDBListALL = null;
		List<UploadTask> uploadTaskDBListPager = null;
		List<UploadTaskJob> uploadTaskJobList = null;
		
		UploadTaskJob uploadTaskJob = null;			//�����������
		List<StandToUploadManuscripts> standToUploadManuscriptsList = null;	//ListView�󶨶���
		StandToUploadManuscripts standToUploadManuscriptItem = null;
		uploadTaskService = new UploadTaskService(StandToManuscriptsActivity.this);
		
		try{
			//��ȡ��ݿ��Cancel��Finished״̬֮������
			uploadTaskDBListALL = uploadTaskService.getListForStandToManuscript();
			uploadTaskJobList = IngleApplication.getInstance().getQueuedUploads();
		}catch(Exception e){
			Logger.e(e);
		}

		if(uploadTaskDBListALL ==null ){//����Ŀǰû�д���
			standToUploadManuscriptsList = new ArrayList<StandToUploadManuscripts>();
			pager.setTotalNum(0);
			return standToUploadManuscriptsList;
		}
		else{
			uploadTaskDBListPager = uploadTaskService.getListForStandToManuscript(pager);
			standToUploadManuscriptsList = new ArrayList<StandToUploadManuscripts>();
			if(uploadTaskJobList == null || uploadTaskJobList.size()==0){			//�����û��˳�������ڴ���ķ��Ͷ����Ѿ�Ϊ����
				//������������������ض�����
				for(UploadTask tu:uploadTaskDBListPager){
					//--------Ŀǰ�ӿڲ�֧�ֶϵ㣬��������ֹͣ����Ҫ����progress��lastblock���ֶ���Ϣ---------
//					tu.setFileid("");
//					tu.setLastblocknum(-1);
//					tu.setProgress(0);
//					tu.setRepeattimes(1);
//					tu.setUploadedsize(0);
//					tu.setStatus(UploadTaskStatus.Waiting);
					//--------------------------------------------------------------------------------------------------
					
					uploadTaskJob = new UploadTaskJob(tu);					
					
					FileUploadTaskHelper.addToUploads(uploadTaskJob);
					standToUploadManuscriptItem = new StandToUploadManuscripts();
					standToUploadManuscriptItem.setManuscripts(manuscriptsService.getManuscripts(tu.getManuscriptId()));
					standToUploadManuscriptItem.setUploadTaskJob(uploadTaskJob);
					
					standToUploadManuscriptsList.add(standToUploadManuscriptItem);

				}
			}
			else{
				try {
					for(UploadTaskJob job : uploadTaskJobList){
						standToUploadManuscriptItem = new StandToUploadManuscripts();
						standToUploadManuscriptItem.setManuscripts(manuscriptsService.getManuscripts(job.getMUploadTask().getManuscriptId()));
						
						standToUploadManuscriptItem.setUploadTaskJob(job);
						standToUploadManuscriptsList.add(standToUploadManuscriptItem);
					}
				} catch (Exception e) {
					uploadTaskJobList = IngleApplication.getInstance().getQueuedUploads();
					standToUploadManuscriptsList= new ArrayList<StandToUploadManuscripts>();
					for(UploadTaskJob job : uploadTaskJobList){
						standToUploadManuscriptItem = new StandToUploadManuscripts();
						standToUploadManuscriptItem.setManuscripts(manuscriptsService.getManuscripts(job.getMUploadTask().getManuscriptId()));
						
						standToUploadManuscriptItem.setUploadTaskJob(job);
						standToUploadManuscriptsList.add(standToUploadManuscriptItem);
					}
				}
			}
		}
		return standToUploadManuscriptsList;
	}

		
	/**
	 * ����CheckBox�Ĺ�ѡ״̬
	 * @param checkState
	 */
	private void setCheckState(boolean checkState) {

		this.checkState = checkState;

		// ����ͼ�Ķ�ѡ��ťͼƬ
		if (checkState)
			iBtnCheckManuscripts_StandTMA.setImageBitmap(BitmapFactory.decodeResource(
							this.getResources(),R.drawable.phone_draftstate_standtosent_check));
		else
			iBtnCheckManuscripts_StandTMA.setImageBitmap(BitmapFactory.decodeResource(
							this.getResources(),R.drawable.phone_draftstate_standtosent_editcheck));
	}

	/**
	 * ��ʾ��ݼ�����ʾ��
	 */
	private void showLoadingDialog() {
		String message = this.getResources().getString(R.string.manuscripi_operation_more_waiting);
		dialog = ProgressDialog.show(this, "", message, true);	
	}

	/**
	 * ҳ�水ť����¼�
	 */
	public void onClick(View v) {
		int currentPage = 0;
		switch (v.getId()) {
		// ���ñ༭״̬����ť����
		case R.id.btnEdit_StandTMA:
			editState = !editState;
			if (editState) {
				this.iBtnStartManuscripts_StandTMA.setVisibility(View.VISIBLE);
				this.iBtnPauseManuscripts_StandTMA.setVisibility(View.VISIBLE);
				this.iBtnRemoveManuscripts_StandTMA.setVisibility(View.VISIBLE);
				this.iBtnRestartManuscripts_StandTMA	.setVisibility(View.VISIBLE);
				this.iBtnCheckManuscripts_StandTMA.setVisibility(View.VISIBLE);
				this.btnEdit_StandTMA.setText(R.string.editButton_false_manuscriptList);
				setContextMenu(false);
				this.pauseLoadViewForEdit = true;
			} else {
				this.iBtnStartManuscripts_StandTMA.setVisibility(View.GONE);
				this.iBtnPauseManuscripts_StandTMA.setVisibility(View.GONE);
				this.iBtnRemoveManuscripts_StandTMA.setVisibility(View.GONE);
				this.iBtnRestartManuscripts_StandTMA	.setVisibility(View.GONE);
				this.iBtnCheckManuscripts_StandTMA.setVisibility(View.GONE);
				this.btnEdit_StandTMA.setText(R.string.editButton_true_manuscriptList);
				this.pauseLoadViewForEdit = false;
				setContextMenu(true);
			}
			standToManuscriptsListAdapter.setEditState(editState);
			
			break;
		case R.id.ibtnCheckManuscripts_StandTMA:
			setCheckState(!checkState);
			standToManuscriptsListAdapter.checkAll(checkState);
			break;
		case R.id.ibtnPauseManuscripts_StandTMA:
			showLoadingDialog();			
			this.pauseDataThread();
			break;
		case R.id.ibtnStartManuscripts_StandTMA:
			showLoadingDialog();			
			this.startDataThread();
			break;
		case R.id.ibtnRemoveManuscripts_StandTMA:
			showLoadingDialog();			
			this.removeDataThread();
			break;
		case R.id.ibtnRestartManuscripts_StandTMA:
			showLoadingDialog();			
			this.restartDataThread();
			break;
		case R.id.iBtnBack_StandTMA:
			setResult(RESULT_CANCELED);
			
			finish();	
			break;
		// ��һҳ
		case R.id.btnPre_StandTMA:
			currentPage = this.pager.getCurrentPage();
			this.pager.setCurrentPage(currentPage == 1 ? 1 : --currentPage);

			if(this.pager.getTotalPageCount() == currentPage){
				showAlertDialog(getResources().getString(R.string.currentPageIsFirst));
			}else{
				showLoadingDialog();
			}
			// ���¼����б���
			loadMoreDataThread();
			
			listViewManuscripts_StandTMA.setSelection(0);
			break;
		// ��һҳ
		case R.id.btnNext_StandTMA:
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
			
			listViewManuscripts_StandTMA.setSelection(0);
			break;
		default:
			break;
		}
		
	}
	
	private void showAlertDialog(String message){		
		dialog = ProgressDialog.show(this, "", message, true);
	}
}
	