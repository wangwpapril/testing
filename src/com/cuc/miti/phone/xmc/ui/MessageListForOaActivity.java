package com.cuc.miti.phone.xmc.ui;

import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.ui.control.PullDownListView;
import com.cuc.miti.phone.xmc.adapter.MessageListForOaAdapter;
import com.cuc.miti.phone.xmc.domain.MessageForOa;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.logic.MessageForOaService;
import com.cuc.miti.phone.xmc.logic.MessageService;
import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Enums.NetStatus;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.ToastHelper;
import com.cuc.miti.phone.xmc.R;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MessageListForOaActivity extends BaseActivity implements OnClickListener,PullDownListView.OnRefreshListener{
	private PullDownListView mListView;

	private LinearLayout linearLayoutProgress;
	private LinearLayout linearLayoutContent;
	
	private final int MSG_UPDATE = 0;
	private final int MSG_REFRESH = 1;
	private final int MSG_MORE = 2;
	private final int MSG_UPDATE_FAILED = 3;
	private static final int REQUEST_UPDATE_VIEW = 4;
	
	private TextView textViewTitleSC;
	private TextView textViewCount_MOA;
	private Button ibtnRefreshMessage_MOA;
	private ImageButton iBtnBack;
	
	private MessageForOaService messageService;
	private MessageListForOaAdapter adapter;

	private String sessionId = "";
	private String messageNomorepageEnd;
	private int mid;
	private int limit,start; //OA��Ϣÿҳ��ʾ�������ʼ����
	//private Pager pager=null;
	private List<MessageForOa> msgList;
	private MessageHandler mHandler=null;
	
	private boolean mDualFragments = false;
	private int mCurPosition = -1;
	private static final int PAGESIZE= 5;
	

	
	Intent mIntent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messageforoa);
		
		this.initialize();
		
		IngleApplication.getInstance().addActivity(this);
	}
	
	private void initialize() {		
		sessionId=IngleApplication.getSessionId();
		messageService=new MessageForOaService(MessageListForOaActivity.this);
		
		limit= PAGESIZE;
		start= -1;				//-1��ʾ�����µ�һ����ʼȡ��ȡPAGESIZE����ݣ����һ����ԭ�����
		messageNomorepageEnd=getResources().getString(R.string.oa_details_nomorepage_end);
		
		//pager = Pager.getDefault();
		mHandler=new MessageHandler();

		this.setUpViews();
		this.initializeListView();		
	}
	private void initializeListView() {
		try {	
			msgList=new ArrayList<MessageForOa>();
			onUpdate();
		} catch (Exception e) {
			Logger.e(e);
		}
		adapter=new MessageListForOaAdapter(msgList, MessageListForOaActivity.this);
		mListView.setAdapter(adapter);
		//textViewCount_MOA.setText(String.valueOf(pager.getTotalNum()));
		
		// �趨����¼�
		mListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				mIntent = new Intent(MessageListForOaActivity.this,MessageForOaDetailsActivity.class);
				mid = adapter.getItem(position-1).getId();
				Bundle iBundle= new Bundle();
//				iBundle.putInt("messageID", mid);
				iBundle.putParcelable("messageOA", adapter.getItem(position-1));
				mIntent.putExtras(iBundle);
				startActivityForResult(mIntent,REQUEST_UPDATE_VIEW);
			}
		});
		linearLayoutContent.setVisibility(View.INVISIBLE);
		linearLayoutProgress.setVisibility(View.VISIBLE);
		
	}

	private void setUpViews() {
		textViewTitleSC=(TextView)findViewById(R.id.textViewTitleSC_MOA);
		textViewTitleSC.setText(R.string.oa_message);
		textViewCount_MOA=(TextView)findViewById(R.id.textViewCount_MOA);
		
		ibtnRefreshMessage_MOA = (Button)findViewById(R.id.btnRefreshSC_MOA);
		ibtnRefreshMessage_MOA.setOnClickListener(this);
		
		iBtnBack = (ImageButton)findViewById(R.id.iBtnBackSC_MOA);
		iBtnBack.setOnClickListener(this);
		
		mListView = (PullDownListView)findViewById(R.id.lvMessageSC_MOA);
		mListView.setonRefreshListener((PullDownListView.OnRefreshListener) this);
		linearLayoutContent = (LinearLayout) findViewById(R.id.linearLayoutContent_MOA);
		linearLayoutProgress = (LinearLayout) findViewById(R.id.linearLayoutProgress_MOA);	
		
	}


	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btnRefreshSC_MOA:  //���ˢ��
			linearLayoutContent.setVisibility(View.INVISIBLE);
			linearLayoutProgress.setVisibility(View.VISIBLE);
			onUpdate();
			break;
		case R.id.iBtnBackSC_MOA:
			setResult(RESULT_OK);
			finish();
			break;
		default:
			break;
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == Activity.RESULT_OK) {	
			switch(requestCode){
				case REQUEST_UPDATE_VIEW:			
					//initializeListView();
					onUpdate();
					break;
				default:				
					break;
			}
		}
	}
	/**
	 * ˢ����Ϣ�б�(�����ñ����ڴ��е���Ϣ��Ĭ�ϰ��շ�ҳ����ȡ��)
	 */
	private void onUpdate(){
		new Thread(new Runnable() {			
			public void run() {
				
				try {
					synchronized (IngleApplication.mLock) {
						while (IngleApplication.getNetStatus() == NetStatus.Disable) {
							sendMessage(MSG_UPDATE_FAILED,null);
							IngleApplication.mLock.wait(); 
						}	
					}
				}catch (Exception e) {
					Logger.e(e);
					e.printStackTrace();
				}
				//pager.setCurrentPage(1);
				//List<MessageForOa> tempList = messageService.getMessageListFromRemote(pager, limit,  start); //�������ȡ��Ӧ��startҳ��limit����OA��Ϣ�б�
				List<MessageForOa> tempList = messageService.getMessageListFromRemote( limit,  start); //�������ȡ��Ӧ��startҳ��limit����OA��Ϣ�б�

				sendMessage(MSG_UPDATE,tempList);
			}
		}).start();
	}

	public void onMore() {
		new Thread(new Runnable() {
			
			public void run() {
				try {
					synchronized (IngleApplication.mLock) {
					while (IngleApplication.getNetStatus() == NetStatus.Disable) {
						sendMessage(MSG_UPDATE_FAILED,null);
						IngleApplication.mLock.wait(); 
					}
					
					}
				}catch (Exception e) {
					Logger.e(e);
					e.printStackTrace();
				}
				
				//List<MessageForOa> tempList = messageService.getMessageListFromRemote(pager, limit,  adapter.getMessageListLastItemID());
				List<MessageForOa> tempList = messageService.getMessageListFromRemote(limit,  adapter.getMessageListLastItemID());

				if(tempList!=null && tempList.size()>0){
					sendMessage(MSG_MORE,tempList);
				}else{
					sendMessage(MSG_MORE,null);
				}
				
				
//				int currentPage = pager.getCurrentPage();
//				if(currentPage < pager.getTotalPageCount())
//				{
//					pager.setCurrentPage(++currentPage);
//					start=pager.getCurrentPage();   //����ÿ��ȡ��Ϣ����ʼλ��
//					List<MessageForOa> tempList = messageService.getMessageListFromRemote(pager, limit,  start);
//					sendMessage(MSG_MORE,tempList);
//				}
//				else sendMessage(MSG_MORE,null);
				
			}
		}).start();
		
	}

	public void onRefresh() {
		new Thread(new Runnable() {
			
			public void run() {
				try {
					synchronized (IngleApplication.mLock) {
					while (IngleApplication.getNetStatus() == NetStatus.Disable) {
						sendMessage(MSG_UPDATE_FAILED,null);
						IngleApplication.mLock.wait(); 
					}
					
					}
				}catch (Exception e) {
					Logger.e(e);
					e.printStackTrace();
				}
//				messageService.getMessageListFromRemote(pager, 1,  -1); //�������ȡ��Ӧ��startҳ��limit����OA��Ϣ�б�
//				int num=messageService.getMsg_totalCount()-adapter.getItem(1).getId();
//				if(num==0){//����û�и��µ����
//					sendMessage(MSG_UPDATE,null);	
//					return;
//				}
				//List<MessageForOa> tempList = messageService.getMessageListFromRemote(pager, limit,  start); //�������ȡ��Ӧ��startҳ��limit����OA��Ϣ�б�
				List<MessageForOa> tempList = messageService.getMessageListFromRemote(limit,  start); //�������ȡ��Ӧ��startҳ��limit����OA��Ϣ�б�

				if(adapter.isEmpty()==false&&tempList!=null&&tempList.get(0).getId()==adapter.getItem(0).getId()) 
					sendMessage(MSG_UPDATE,null);						
				else  sendMessage(MSG_UPDATE,tempList);					
			}
	
		}).start();	
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// KeyEvent.KEYCODE_BACK��?�ز���.
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(RESULT_OK);
			finish();
		}
		return true;
	}
	/**
	 * This method is used to handle received messages
	 * @author SongQing
	 *
	 */
	private class MessageHandler extends Handler {
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
				//ˢ���б?�����¸����б?��գ�
				case MSG_UPDATE: {
					if (msg.obj != null) {
		
						adapter.clearCache();
		
						List<MessageForOa> tempList = (List<MessageForOa>) msg.obj;
						adapter.setMessageList(tempList);
						adapter.notifyDataSetChanged();
						textViewCount_MOA.setText(String.valueOf(adapter.getCount()));
		
						linearLayoutContent.setVisibility(View.VISIBLE);
						//Toast.makeText(MessageListForOaActivity.this,R.string.oa_details_news_updated,Toast.LENGTH_SHORT).show();
					}else{
						//Toast.makeText(MessageListForOaActivity.this,R.string.oa_details_nonews,Toast.LENGTH_SHORT).show();
						ToastHelper.showToast(MessageListForOaActivity.this.getResources().getString(R.string.oa_details_nonews),Toast.LENGTH_SHORT);
					}
					linearLayoutProgress.setVisibility(View.GONE);
		
					// ��ݼ������;
					mListView.onRefreshComplete();
					mListView.onUpdateComplete();
//					Toast.makeText(fragmentView.getContext(),R.string.oa_details_nonews,Toast.LENGTH_SHORT).show();
					break;
				}
				//����ˢ�£����б�ǰ������������
				case MSG_REFRESH: {
					if (msg.obj != null) {
						List<MessageForOa> tempList = (List<MessageForOa>) msg.obj;
						adapter.addMessageList(0, tempList);
						adapter.notifyDataSetChanged();
						textViewCount_MOA.setText(String.valueOf(adapter.getCount()));
					}
					// ������������
					mListView.onRefreshComplete();
					break;
				}
				//�鿴������Ϣ�������б����
				case MSG_MORE: {
					if (msg.obj != null) {
						List<MessageForOa> tempList = (List<MessageForOa>) msg.obj;
						adapter.addMessageList(tempList);
						adapter.notifyDataSetChanged();
						textViewCount_MOA.setText(String.valueOf(adapter.getCount()));
						// �������ȡ������
						mListView.onMoreComplete();
					} else {
						/*Toast.makeText(MessageListForOaActivity.this, messageNomorepageEnd,
								Toast.LENGTH_SHORT).show();*/
						ToastHelper.showToast(messageNomorepageEnd,Toast.LENGTH_SHORT);
						
						// �������޸�����Ϣ
						mListView.noMore();
					}
					
					break;	
				}
				case MSG_UPDATE_FAILED:
					
					linearLayoutContent.setVisibility(View.VISIBLE);
					linearLayoutProgress.setVisibility(View.INVISIBLE);
					//Toast.makeText(MessageListForOaActivity.this,"��Ϣ����ʧ�ܣ�",Toast.LENGTH_SHORT).show();
					ToastHelper.showToast(getResources().getString(R.string.msgUpdateFailed_MCA),Toast.LENGTH_SHORT);
					mListView.onRefreshComplete();
					mListView.onUpdateComplete();
					mListView.noMore();
					break;	
				default:
					break;			
			}		
		}
	}

	private void sendMessage(int what, Object obj) {
		Message msg = mHandler.obtainMessage();
		msg.what = what;
		msg.obj = obj;
		mHandler.sendMessageDelayed(msg, 500);
	}
}
