package com.cuc.miti.phone.xmc.ui;

import java.util.List;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.MessageForUs;
import com.cuc.miti.phone.xmc.domain.Enums.MessageType;
import com.cuc.miti.phone.xmc.domain.Enums.ReadOrNotType;
import com.cuc.miti.phone.xmc.logic.MessageService;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.TimeFormatHelper;
import com.cuc.miti.phone.xmc.R;


import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class RecommendMessageActivity extends BaseActivity implements OnClickListener{
	private ImageButton iBtnBack;  //���˰�ť
	private TextView tvMsgFromTHNow_MCA_re,tvContentSC_recommend,tvTimeSC_recommend;  //����Ϣ���û������ġ�����Ϣ��ʱ��

	private String sessionId = "";
	private String loginname = "";
	private MessageService messageService;
	private MessageType messageType;

	MessageForUs messageForUs;
	List<MessageForUs> msgList;

	String msgFrom="";
	int msgID = 0;
	
	//ȡ����ǰ�����˵���Ϣ��Դ��
	private String msgFromStr="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recommend_message_third);
		
		this.initialize();
		
		IngleApplication.getInstance().addActivity(this);
	}	
	
	private void initialize() {		
		sessionId=IngleApplication.getSessionId();
		loginname=IngleApplication.getInstance().getCurrentUser();
		messageService=new MessageService(RecommendMessageActivity.this);

		this.setUpViews();
		this.initializeListView();		
	}

	private void setUpViews() {
		iBtnBack = (ImageButton)findViewById(R.id.iBtnBackTHNow_MCA_re);
		iBtnBack.setOnClickListener(this);
		tvMsgFromTHNow_MCA_re=(TextView)findViewById(R.id.tvMsgFromTHNow_MCA_re);
		tvContentSC_recommend =(TextView)findViewById(R.id.tvContentSC_recommend);
		tvTimeSC_recommend =(TextView)findViewById(R.id.tvTimeSC_recommend);
		
	}

	
	private void initializeListView() {
		try {
			Bundle mBundle = this.getIntent().getExtras();
			msgID=mBundle.getInt("msgID");
			msgFromStr=mBundle.getString("msgFromStr");
			String msgTypeStr=mBundle.getString("msgTypeStr");
			messageType = MessageType.valueOf(msgTypeStr);
			messageForUs =messageService.getMessageById(msgID);	
			
			tvMsgFromTHNow_MCA_re.setText(messageForUs.getMsgFrom());
			tvTimeSC_recommend.setText(TimeFormatHelper.convertLongToDate(messageForUs.getMsgSendOrReceiveTime()));
			tvContentSC_recommend.setText(messageForUs.getMsgContent().trim());	
			
		} catch (Exception e) {
			Logger.e(e);
		}	
	}

	public void onClick(View v) {
		switch(v.getId()){
		//���˰�ť��ʵ�ֶ���Ϣ���Ѷ��ֶθ���
		case R.id.iBtnBackTHNow_MCA_re:
			if(ReadOrNotType.New.toString().equals(messageForUs.getReadOrNotType().toString()))
			{
				messageForUs.setReadOrNotType(ReadOrNotType.Read);
				messageService.updateMessage(messageForUs);
				setResult(RESULT_OK);
				finish();
			}else{
				setResult(RESULT_OK);
				finish();
			}
			break; 
		default:
			break;		
		}		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// KeyEvent.KEYCODE_BACK��?�ز���.
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
				if(ReadOrNotType.New.toString().equals(messageForUs.getReadOrNotType().toString()))
				{					
					messageForUs.setReadOrNotType(ReadOrNotType.Read);
					messageService.updateMessage(messageForUs);
				}
			
			setResult(RESULT_OK);
			finish();
	
		}
		return true;
	}
}
