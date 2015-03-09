package com.cuc.miti.phone.xmc.adapter;

import java.util.ArrayList;
import android.util.Log;
import java.util.List;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Accessories;
import com.cuc.miti.phone.xmc.domain.ManuscriptTemplate;
import com.cuc.miti.phone.xmc.domain.MessageForPush;
import com.cuc.miti.phone.xmc.domain.Enums.AccessoryType;
import com.cuc.miti.phone.xmc.domain.Enums.MessageType;
import com.cuc.miti.phone.xmc.domain.Enums.ReadOrNotType;
import com.cuc.miti.phone.xmc.logic.AccessoriesService;
import com.cuc.miti.phone.xmc.logic.ManuscriptTemplateService;
import com.cuc.miti.phone.xmc.logic.MessageService;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.MediaHelper;
import com.cuc.miti.phone.xmc.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter {

	private LayoutInflater mInflater; // ������findViewById,������layout�ļ����µ�xml�����ļ���ʵ��
	// private List<MessageForPush> messageForPushList; //��Ϣ�б�
	private Context mContext;

	private List<String> messageTypeList;
	private MessageService messageService;

	/**
	 * ���캯��
	 * 
	 * @param context
	 */
	/*
	 * public MessageAdapter(List<MessageForPush> messageForPushList , Context
	 * context ){ super(); this.messageForPushList = messageForPushList; if
	 * (messageForPushList == null) messageForPushList = new
	 * ArrayList<MessageForPush>(); this.mContext = context; this.mInflater =
	 * LayoutInflater.from(context); //this.mAccessoryType = accessoryType; }
	 * 
	 * public int getCount() { if (null != messageForPushList) { return
	 * messageForPushList.size(); } else { return 0; } }
	 * 
	 * public Object getItem(int position) { return
	 * messageForPushList.get(position); }
	 * 
	 * public long getItemId(int position) { return position; }
	 * 
	 * public void deleteItem(int position){
	 * messageForPushList.remove(position); }
	 * 
	 * public void updateList(List<MessageForPush> messageForPushList){
	 * this.messageForPushList = messageForPushList; }
	 * 
	 * public View getView(int position, View convertView, ViewGroup parent) {
	 * ViewHolder mHolder; //ȡ��layout�ļ����µ�listview_item_message�����ļ���ʵ��
	 * convertView = mInflater.inflate(R.layout.listview_item_message, null);
	 * mHolder = new ViewHolder(); mHolder.tViewItemName =
	 * (TextView)convertView.findViewById(R.id.textViewListViewItemText_MCA);
	 * mHolder.tViewItemCount =
	 * (TextView)convertView.findViewById(R.id.textViewListViewItemCountText_MCA
	 * ); mHolder.goIntoItem =
	 * (ImageView)convertView.findViewById(R.id.imageViewListViewItemPic_MCA);
	 * MessageForPush messageForPushItem = messageForPushList.get(position); int
	 * count1=0; int count2=0; int count3=0;
	 * if(messageForPushItem.getMsgType().toString()=="SystemMsg") {
	 * count1=count1+1; mHolder.tViewItemName.setText(R.string.systemMsg_MCA);
	 * mHolder.tViewItemCount.setText(count1+""); }else
	 * if(messageForPushItem.getMsgType().toString()=="RecommendMsg") {
	 * count2=count2+1;
	 * mHolder.tViewItemName.setText(R.string.recommendMsg_MCA);
	 * mHolder.tViewItemCount.setText(count2+""); }else
	 * if(messageForPushItem.getMsgType().toString()=="InstantMsg") {
	 * count3=count3+1; mHolder.tViewItemName.setText(R.string.instantMsg_MCA);
	 * mHolder.tViewItemCount.setText(count3+""); }
	 * 
	 * convertView.setTag(mHolder);
	 * 
	 * return convertView; }
	 */

	public MessageAdapter(List<String> messageTypeList, Context context) {
		super();
		this.messageTypeList = messageTypeList;
		if (messageTypeList == null)
			messageTypeList = new ArrayList<String>();
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		messageService=new MessageService(context);
		// this.mAccessoryType = accessoryType;
	}

	public int getCount() {
		if (null != messageTypeList) {
			return messageTypeList.size();
		} else {
			return 0;
		}
	}

	public Object getItem(int position) {
		return messageTypeList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public void deleteItem(int position) {
		messageTypeList.remove(position);
	}

	public void updateList(List<String> messageTypeList) {
		this.messageTypeList = messageTypeList;
	}

	/**
	 * ����һ���µ�ListView Item����
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder;
		// ȡ��layout�ļ����µ�listview_item_message�����ļ���ʵ��
		convertView = mInflater.inflate(R.layout.listview_item_message, null);
		mHolder = new ViewHolder();
		mHolder.tViewItemName = (TextView) convertView
				.findViewById(R.id.textViewListViewItemText_MCA);
		mHolder.tViewItemNewMsg=(ImageView) convertView
		.findViewById(R.id.imageViewListViewItemNewMsg_MCA);
		mHolder.goIntoItem = (ImageView) convertView
				.findViewById(R.id.imageViewListViewItemPic_MCA);
		String messageTypeItem = messageTypeList.get(position);

		if (messageTypeItem != null) {

			if (messageTypeItem.toString().equals("SystemMsg")) {
				mHolder.tViewItemName.setText(R.string.systemMsg_MCA);
								
			} else if (messageTypeItem.toString().equals("RecommendMsg")) {
				mHolder.tViewItemName.setText(R.string.recommendMsg_MCA);
				
			} else if (messageTypeItem.toString().equals("InstantMsg")) {
				mHolder.tViewItemName.setText(R.string.instantMsg_MCA);
				
			}else if (messageTypeItem.toString().equals("XMPPMsg")) {
				mHolder.tViewItemName.setText(R.string.XMPPMsg_MCA);
			}
			else if (messageTypeItem.toString().equals("OaMsg")) {
				mHolder.tViewItemName.setText(R.string.oa_message);
			}
			

			if(messageService.getMessageNewByMsgType(MessageType.valueOf(messageTypeItem),IngleApplication.getInstance().getCurrentUser()))
			{
				mHolder.tViewItemNewMsg.setBackgroundResource(R.drawable.msg_new);
			}
		}
		
		

		convertView.setTag(mHolder);

		return convertView;
	}

	/**
	 * ���ٲ���Ҫ�ĵ���findViewById,�ѶԵ��µĿؼ����ô���ViewHolder����,
	 * ����View.setTag(holder)�������view��,�´ξͿ���ֱ��ȡ���ˡ�
	 * 
	 * @author SongQing
	 * 
	 */
	static class ViewHolder {
		// ��Ϣ��������
		TextView tViewItemName = null;
		ImageView tViewItemNewMsg = null;
		// ͼƬ
		ImageView goIntoItem = null;

	}
}
