package com.cuc.miti.phone.xmc.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cuc.miti.phone.xmc.domain.MessageForOa;
import com.cuc.miti.phone.xmc.utils.TimeFormatHelper;
import com.cuc.miti.phone.xmc.R;

public class MessageListForOaAdapter extends BaseAdapter{

	private LayoutInflater mInflater; // ������findViewById,������layout�ļ����µ�xml�����ļ���ʵ��
	private List<MessageForOa> messageList; //��Ϣ�б�
	
	public MessageListForOaAdapter(List<MessageForOa> messageList, Context context){
		super();
		this.messageList = messageList;
		if (messageList == null){
			messageList = new ArrayList<MessageForOa>();
		}
		
		this.mInflater = LayoutInflater.from(context);
	}
	
	/**
	 * ��ȡ��OA��Ϣ��������
	 */
	public int getCount() {
		if (null != messageList) {
			return messageList.size();
		} else {
			return 0;
		}
	}
	
	/**
	 * ���position��ȡ��Ӧ��OA��Ϣ
	 */
	public MessageForOa getItem(int position) {
		return messageList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	
	/**
	 * ������Ϣ�б�󶨶���
	 * @param messageList
	 */
	public void updateList(List<MessageForOa> messageList) {
		this.messageList = messageList;
	}
	
	/**
	 * ������Ϣ�б�󶨶���
	 * @param tempList
	 */
	public void setMessageList(List<MessageForOa> tempList ) {
		if (tempList == null)
			this.messageList = new ArrayList<MessageForOa>();
		else
			this.messageList = tempList;
	}
	
	/**
	 * Removes all elements from this List, leaving it empty.
	 */
	public void clearCache(){
		this.messageList.clear();
	}
	
	/**
	 * ���б�i��λ�ú���׷����Ϣ�б�
	 * @param i
	 * @param tempList
	 */
	public void addMessageList(int i, List<MessageForOa> tempList) {
		this.messageList.addAll(i, tempList);
		
	}
	public void addMessageList(List<MessageForOa> tempList) {
		this.messageList.addAll(tempList);	
	}
	public int getMessageListLastItemID(){
		if(this.messageList !=null && this.messageList.size()>0){
			return this.messageList.get(this.messageList.size()-1).getId();
		}else{
			return -1;
		}
	}
	
	/**
	 * ����һ���µ�ListView Item����
	 */
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		// ȡ��layout�ļ����µ�listview_item_message�����ļ���ʵ��
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listview_item_message_oa, parent,false );
		}
		
		if (position >= this.messageList.size()){return convertView;}
				
		// ��Ϣ����
		TextView title = (TextView) convertView.findViewById(R.id.textViewItemTitle_MOA) ;
		// ��Ϣ����
		TextView writer =(TextView) convertView.findViewById(R.id.textViewItemWriter_MOA);
		// ��Ϣ��λ
		TextView department = (TextView) convertView.findViewById(R.id.textViewItemDepartment_MOA);
		// ��Ϣ����ʱ��
		TextView publishtime =  (TextView) convertView.findViewById(R.id.textViewItemPublishtime_MOA);
		
		MessageForOa mItem = messageList.get(position);
		if(mItem == null){return null;}
		
		if(mItem.getInfo_title() !=null){
			title.setText(mItem.getInfo_title());
		}
		if(mItem.getWriter() !=null){
			writer.setText(mItem.getWriter());
		}
		if(mItem.getDepartment()!=null){
			department.setText(mItem.getDepartment());
		}
		if(mItem.getPublishtime()!=null){
			publishtime.setText(mItem.getPublishtime());
		}
		
		return convertView;	
	}
}
