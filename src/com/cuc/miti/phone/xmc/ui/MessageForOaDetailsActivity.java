package com.cuc.miti.phone.xmc.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Enums.NetStatus;
import com.cuc.miti.phone.xmc.domain.MessageForOa;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.R;

public class MessageForOaDetailsActivity extends BaseActivity implements OnClickListener {
	private ImageButton iBtnBack;
	private TextView textViewMsgTitle;
	private WebView webView;
	private MessageForOa msg;
	private static final int MSG_UPDATE = 0;
	protected static final int MSG_UPDATE_FAILED = 10;
	MessageHandler mHandler=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args =this.getIntent().getExtras();
		 if (args != null) {
			 msg = (MessageForOa)args.getParcelable("messageOA");
		 }
		setContentView(R.layout.messageforoa_content);
		
		this.initialize();
		
		IngleApplication.getInstance().addActivity(this);
	}
	private void initialize() {	
		mHandler=new MessageHandler();
	
		this.setUpViews();
		this.initializeWebView();		
	}
	private void initializeWebView() {
		getMessageThread();
//		webView.setInitialScale(60);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setSupportZoom(true); 
//		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		webView.getSettings().setUseWideViewPort(true);  
		webView.getSettings().setLoadWithOverviewMode(true);  
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL); // webView: ��WebView��ʵ��
//		webView.getSettings().setDefaultZoom(ZoomDensity.CLOSE);//Ĭ������ģʽ
		
	}
	private void setUpViews() {
		iBtnBack = (ImageButton)findViewById(R.id.iBtnBackTH_MOA);
		iBtnBack.setOnClickListener(this);
		textViewMsgTitle = (TextView)findViewById(R.id.textViewMsgTitle_MOA);
		webView = (WebView)findViewById(R.id.webView_MOA);
		
	}
	public void updateMsgWebView() {
		if(msg!=null){
//			textViewMsgTitle.setText(msg.getInfo_title());
			textViewMsgTitle.setText(msg.getInfo_title());
			
			webView.loadUrl(msg.getInfocontent() + "?sss=" + IngleApplication.getSessionId());
			
//			webView.loadUrl(messageService.getWebContent());
			//webView.loadUrl("http://www.baidu.com/s?ie=utf-8&bs=webview+%E5%B1%9E%E6%80%A7&f=8&rsv_bp=1&wd=webview+android%3A%3A&rsv_sug3=4&rsv_sug1=4&rsv_sug4=28&inputT=2155");
		}
	}
	 private class MessageHandler extends Handler {

			

			// This method is used to handle received messages
			public void handleMessage(Message msg) {
				updateMsgWebView();
				}
	}
	 private void sendMessage(int what, Object obj) {
			Message msg = mHandler.obtainMessage();
			msg.what = what;
			msg.obj = obj;
			mHandler.sendMessageDelayed(msg, 500);
	}
	private void getMessageThread(){
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
			
//				msg=messageService.getMessageByIDFromRemote(mId);
				sendMessage(MSG_UPDATE,null);
			}
		}).start();
	}
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.iBtnBackTH_MOA:
				setResult(RESULT_OK);
				finish();
				break;
			default:
				break;
			}
		
	}

}
