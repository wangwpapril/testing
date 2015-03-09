package com.cuc.miti.phone.xmc.adapter;

import java.util.ArrayList;
import android.util.Log;
import java.util.List;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Manuscripts;
import com.cuc.miti.phone.xmc.domain.MessageForUs;
import com.cuc.miti.phone.xmc.domain.MessageForPush;
import com.cuc.miti.phone.xmc.domain.Enums.AccessoryType;
import com.cuc.miti.phone.xmc.domain.Enums.MessageType;
import com.cuc.miti.phone.xmc.domain.Enums.MsgSendStatus;
import com.cuc.miti.phone.xmc.domain.Enums.ReadOrNotType;
import com.cuc.miti.phone.xmc.domain.Enums.SendOrReceiveType;
import com.cuc.miti.phone.xmc.logic.AccessoriesService;
import com.cuc.miti.phone.xmc.logic.ManuscriptTemplateService;
import com.cuc.miti.phone.xmc.logic.MessageService;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.MediaHelper;
import com.cuc.miti.phone.xmc.utils.SyncImageLoader;
import com.cuc.miti.phone.xmc.utils.TimeFormatHelper;
import com.cuc.miti.phone.xmc.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MessageThirdNowAdapter extends BaseAdapter {

	private LayoutInflater mInflater; // ������findViewById,������layout�ļ����µ�xml�����ļ���ʵ��
	private List<MessageForUs> messageList; //��Ϣ�б�
	private Context mContext;
	MessageService service ;
	
	// �Ƿ��ڱ༭��״̬
	private boolean editState;
	private int checkboxViewImage;
	private int uncheckboxViewImage;
	private SyncImageLoader syncImageLoader;
	private ListView mListView;
	//private ImageView imageViewCheck_MessageMCAItem;
	
	private MessageType messageType;
	private String loginName=IngleApplication.getInstance().getCurrentUser();
	
	public MessageThirdNowAdapter(List<MessageForUs> messageList, Context context,int checkView,
			int uncheckView) {
		super();
		this.messageList = messageList;
		if (messageList == null)
			messageList = new ArrayList<MessageForUs>();
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.checkboxViewImage = checkView;
		this.uncheckboxViewImage = uncheckView;
		this.service = new MessageService(context);
	}

	public int getCount() {
		if (null != messageList) {
			return messageList.size();
		} else {
			return 0;
		}
	}

	public MessageForUs getItem(int position) {
		return messageList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public void deleteItem(int position) {
		messageList.remove(position);
	}

	public void updateList(List<MessageForUs> messageList) {
		this.messageList = messageList;
	}

	/**
	 * ����һ���µ�ListView Item����
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// ȡ��layout�ļ����µ�listview_item_message�����ļ���ʵ��

		
		convertView = mInflater.inflate(R.layout.listview_item_message_thirdnow, null);
		
		
		
		// ��Ϣʱ��
		TextView tViewItemDateSend =(TextView) convertView.findViewById(R.id.textViewDateTHNow_send_MCA);
		TextView tViewItemDateRecieve =(TextView) convertView.findViewById(R.id.textViewDateTHNow_recieve_MCA);
		// ��Ϣ����
		TextView tViewItemContentSend = (TextView) convertView.findViewById(R.id.textViewContentTHNow_send_MCA);
		TextView tViewItemContentRecieve = (TextView) convertView.findViewById(R.id.textViewContentTHNow_recieve_MCA);
		ImageView iViewItemNew=(ImageView) convertView.findViewById(R.id.imageViewListViewItemFailureTHNow_MCA); 
		
		MessageForUs messageItem = messageList.get(position);
		
		//�����û��Լ����͵���Ϣʱ
		if(loginName.equals(messageItem.getMsgFrom()))
		{
			tViewItemDateSend.setVisibility(View.VISIBLE);
			tViewItemContentSend.setVisibility(View.VISIBLE);
			tViewItemDateRecieve.setVisibility(View.GONE);
			tViewItemContentRecieve.setVisibility(View.GONE);
			
			tViewItemDateSend.setText(TimeFormatHelper.convertLongToDate(messageItem.getMsgSendOrReceiveTime()));
			tViewItemContentSend.setText(messageItem.getMsgContent());
		}else{
			//�����û����յ�����Ϣʱ
			
			tViewItemDateSend.setVisibility(View.GONE);
			tViewItemContentSend.setVisibility(View.GONE);
			tViewItemDateRecieve.setVisibility(View.VISIBLE);
			tViewItemContentRecieve.setVisibility(View.VISIBLE);
			tViewItemDateRecieve.setText(TimeFormatHelper.convertLongToDate(messageItem.getMsgSendOrReceiveTime()));
			tViewItemContentRecieve.setText(messageItem.getMsgContent());
			
		}
		
		
		
		
		
		
		if(SendOrReceiveType.Send.toString().equals(messageItem.getSendOrReceiveType().toString())){
			//tFromOrTo.setText(R.string.messageTo_MCA);
			
			if(MsgSendStatus.Success.toString().equals(messageItem.getMsgSendStatus().toString()))
			{
				//iViewItemNew.setBackgroundResource(R.drawable.save);
			}else
			{
				iViewItemNew.setBackgroundResource(R.drawable.msg_send_failure);
			}
			
		}
//		else{
			//tFromOrTo.setText(R.string.messageFrom_MCA);
//		}
		

//		if(ReadOrNotType.New.toString().equals(messageItem.getReadOrNotType().toString()))
//		{
			/*iViewItemNew.setBackgroundResource(R.drawable.msg_new);*/
//		}
		
		ImageView imageViewCheck_MessageMCAItem = (ImageView) convertView.findViewById(R.id.imageViewCheck_MessageMCAItem);
		MessageForUs ma = this.messageList.get(position);
		// ��ݱ༭״̬���ø���Ķ�ѡ���Ƿ����
		if (this.editState)
			imageViewCheck_MessageMCAItem.setVisibility(View.VISIBLE);
		else {
			imageViewCheck_MessageMCAItem.setVisibility(View.GONE);
		}
		// �ж��Ƿ�ѡ��
		if (ma.isCheck())
			imageViewCheck_MessageMCAItem.setImageBitmap(BitmapFactory
					.decodeResource(this.mContext.getResources(),
							checkboxViewImage));
		else {
			imageViewCheck_MessageMCAItem.setImageBitmap(BitmapFactory
					.decodeResource(this.mContext.getResources(),
							uncheckboxViewImage));
		}
		
	
		return convertView;
	}

	
	/**
	 * ѡ���б��е�ָ��λ����
	 * 
	 * @param position
	 *            ָ��λ��
	 */
	public void checkItem(int position) {
		MessageForUs ma = this.messageList.get(position);
		ma.setCheck(!ma.isCheck());
		this.notifyDataSetChanged();
	}

	/**
	 * ѡ��/ȡ�� ������
	 * 
	 * @param check
	 */
	public void checkAll(boolean check) {
		for (MessageForUs ma : this.messageList) {
			ma.setCheck(check);
		}
		this.notifyDataSetChanged();
	}
	/**
	 * ɾ������ѡ�и��
	 */
	public void deleteSelectedItems() {
		
		// ��ȡѡ�еĸ����
		if(messageList!=null){
		int count = this.messageList.size();
		
			for (int i = 0; i < count; i++) {
				MessageForUs ma = this.messageList.get(i);

				if (ma.isCheck()) {
					try {
							service.deleteMessageById(ma.getMsg_id());
					} catch (Exception e) {
						Logger.e(e);
					}
				}
			}
		}
		
	}
	
	public void setEditState(boolean editState) {
		this.editState = editState;
		this.notifyDataSetChanged();
	}

	public boolean isEditState() {
		return editState;
	}
	
	public void setMessageList(List<MessageForUs> tempList ) {
		if (tempList == null)
			this.messageList = new ArrayList<MessageForUs>();
		else
			this.messageList = tempList;
		this.notifyDataSetChanged();
	}
	public void addMessageList(List<MessageForUs> tempList ) {
		if (tempList == null)
			this.messageList = new ArrayList<MessageForUs>();
		else
			this.messageList.addAll(tempList);
		this.notifyDataSetChanged();
	}
	
	
}
