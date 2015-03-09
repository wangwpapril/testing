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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MessageSecondAdapter extends BaseAdapter {

	private LayoutInflater mInflater; // ������findViewById,������layout�ļ����µ�xml�����ļ���ʵ��
	private List<MessageForUs> messageList; // ��Ϣ�б�
	private Context mContext;
	// �Ƿ��ڱ༭��״̬
	private boolean editState;
	private int checkboxViewImage;
	private int uncheckboxViewImage;
	private SyncImageLoader syncImageLoader;
	private ListView mListView;
	// private ImageView imageViewCheck_MessageMCAItem;

	private MessageType messageType;
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

	public MessageSecondAdapter(List<MessageForUs> messageList,
			Context context, int checkView, int uncheckView) {
		super();
		this.messageList = messageList;
		if (messageList == null)
			messageList = new ArrayList<MessageForUs>();
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.checkboxViewImage = checkView;
		this.uncheckboxViewImage = uncheckView;
		messageService = new MessageService(context);

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

		convertView = mInflater.inflate(R.layout.listview_item_message_second,
				null);

		// TextView tFromOrTo = (TextView)
		// convertView.findViewById(R.id.textViewFromOrTo_MCA) ;
		// ��Ϣʱ��
		TextView tViewItemDate = (TextView) convertView
				.findViewById(R.id.textViewListViewItemTextSC_MCA);
		// ��Ϣ����
		TextView tViewItemContent = (TextView) convertView
				.findViewById(R.id.textViewListViewItemContentSC_MCA);
		// ��Ϣ����
		TextView tViewItemAuthor = (TextView) convertView
				.findViewById(R.id.textViewListViewItemMsgFromSC_MCA);
		// ��Ϣ�Ѷ�|δ��
		ImageView iViewItemNew = (ImageView) convertView
				.findViewById(R.id.imageViewListViewItemReadOrNotSC_MCA);

		MessageForUs mItem = messageList.get(position);
		int status = 0;

		switch (messageList.get(position).getMsgType()) {
		case InstantMsg:
		case XMPPMsg:
			List<MessageForUs> mList = messageService.getMessageByMsgOwner(
					mItem.getMsgType(), mItem.getMsgFrom(), mItem
							.getLoginName());
			MessageForUs temp = mItem;
			if (mList != null) {
				for (MessageForUs mf : mList) {
					if (Long.parseLong(mf.getMsgSendOrReceiveTime()) > Long
							.parseLong(temp.getMsgSendOrReceiveTime())) {
						status = 1;
						temp = mf;
					}
				}
			}
			// ����Ǳ��˻ظ�Ϊ����
			if (status == 1 || temp.getMsgFrom().equals(temp.getLoginName())) {
				tViewItemAuthor.setText(temp.getMsgOwner());
			} else {
				tViewItemAuthor.setText(temp.getMsgFrom());
			}

			List<MessageForUs> messageForUsList = messageService
					.getMessageByMsgFrom(mItem.getMsgType(),
							mItem.getMsgFrom(), mItem.getLoginName());

			for (MessageForUs mfu : messageForUsList) {
				if (ReadOrNotType.New.toString().equals(
						mfu.getReadOrNotType().toString())) {
					iViewItemNew.setBackgroundResource(R.drawable.msg_new);
				}
			}
			tViewItemDate.setText(TimeFormatHelper.convertLongToDate(temp
					.getMsgSendOrReceiveTime()));
			tViewItemContent
					.setText(temp.getMsgContent().trim().length() > 10 ? temp
							.getMsgContent().trim().substring(0, 10) : temp
							.getMsgContent().trim());

			break;

		case SystemMsg:
			tViewItemAuthor.setText(mItem.getMsgFrom());

			if (ReadOrNotType.New.toString().equals(
					mItem.getReadOrNotType().toString())) {
				iViewItemNew.setBackgroundResource(R.drawable.msg_new);
			}
			tViewItemDate.setText(TimeFormatHelper.convertLongToDate(mItem
					.getMsgSendOrReceiveTime()));
			tViewItemContent
					.setText(mItem.getMsgContent().trim().length() > 10 ? mItem
							.getMsgContent().trim().substring(0, 10) : mItem
							.getMsgContent().trim());
			break;
		default:
			break;

		}

		/*
		 * if(SendOrReceiveType.Send.toString().equals(messageTypeItem.getSendOrReceiveType
		 * ().toString())) {
		 * tViewItemAuthor.setText(messageTypeItem.getMsgOwner()); }else {
		 * tViewItemAuthor.setText(messageTypeItem.getMsgFrom()); }
		 * 
		 * 
		 * 
		 * 
		 * if(SendOrReceiveType.Send.toString().equals(mItem.getSendOrReceiveType
		 * ().toString())){ //tFromOrTo.setText(R.string.messageTo_MCA);
		 * 
		 * if(MsgSendStatus.Success.toString().equals(mItem.getMsgSendStatus().
		 * toString())) { //iViewItemNew.setBackgroundResource(R.drawable.save);
		 * }else {
		 * iViewItemNew.setBackgroundResource(R.drawable.msg_send_failure); }
		 * 
		 * }else{ //tFromOrTo.setText(R.string.messageFrom_MCA); }
		 */

		ImageView imageViewCheck_MessageMCAItem = (ImageView) convertView
				.findViewById(R.id.imageViewCheck_MessageMCAItem);
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

		/*
		 * ViewHolder mHolder;
		 * 
		 * mHolder = new ViewHolder(); mHolder.tFromOrTo = (TextView)
		 * convertView.findViewById(R.id.textViewFromOrTo_MCA);
		 * mHolder.tViewItemDate = (TextView) convertView
		 * .findViewById(R.id.textViewListViewItemTextSC_MCA);
		 * mHolder.tViewItemContent = (TextView) convertView
		 * .findViewById(R.id.textViewListViewItemContentSC_MCA);
		 * mHolder.tViewItemAuthor = (TextView) convertView
		 * .findViewById(R.id.textViewListViewItemMsgFromSC_MCA);
		 * 
		 * mHolder.iViewItemNew=(ImageView)convertView
		 * .findViewById(R.id.imageViewListViewItemReadOrNotSC_MCA);
		 * 
		 * MessageForUs messageTypeItem = messageList.get(position);
		 * 
		 * mHolder.tViewItemDate.setText(TimeFormatHelper.convertLongToDate(
		 * messageTypeItem.getMsgSendOrReceiveTime()));
		 * mHolder.tViewItemContent.
		 * setText(messageTypeItem.getMsgContent().length
		 * ()>10?messageTypeItem.getMsgContent().substring(0,
		 * 10):messageTypeItem.getMsgContent());
		 * 
		 * if(SendOrReceiveType.Send.toString().equals(messageTypeItem.
		 * getSendOrReceiveType().toString())) {
		 * mHolder.tViewItemAuthor.setText(messageTypeItem.getMsgOwner()); }else
		 * { mHolder.tViewItemAuthor.setText(messageTypeItem.getMsgFrom()); }
		 * if(SendOrReceiveType.Send.toString().equals(messageTypeItem.
		 * getSendOrReceiveType().toString())){
		 * mHolder.tFromOrTo.setText(R.string.messageTo_MCA);
		 * 
		 * if(MsgSendStatus.Success.toString().equals(messageTypeItem.
		 * getMsgSendStatus().toString())) {
		 * mHolder.iViewItemNew.setBackgroundResource(R.drawable.save); }else {
		 * mHolder.iViewItemNew.setBackgroundResource(R.drawable.
		 * phone_common_removeaccessories); }
		 * 
		 * }else{ mHolder.tFromOrTo.setText(R.string.messageFrom_MCA); }
		 * 
		 * 
		 * 
		 * 
		 * 
		 * if(ReadOrNotType.New.toString().equals(messageTypeItem.getReadOrNotType
		 * ().toString())) {
		 * mHolder.iViewItemNew.setBackgroundResource(R.drawable.msg_new); }
		 * //else //{ //
		 * mHolder.iViewItemNew.setBackgroundResource(R.drawable.msg_read); //}
		 * 
		 * convertView.setTag(mHolder);
		 */

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
		// ������or���͸�
		TextView tFromOrTo = null;
		// ��Ϣʱ��
		TextView tViewItemDate = null;
		// ��Ϣ����
		TextView tViewItemContent = null;
		// ��Ϣ����
		TextView tViewItemAuthor = null;
		// ��Ϣ�Ѷ�|δ��
		ImageView iViewItemNew = null;

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
		if (messageList != null) {
			int count = this.messageList.size();

			for (int i = 0; i < count; i++) {
				MessageForUs ma = this.messageList.get(i);

				if (ma.isCheck()) {
					try {
						switch (messageList.get(i).getMsgType()) {
						case InstantMsg:
						case XMPPMsg:
						case SystemMsg:
							messageService.deleteMessageByMsgFromOwner(ma
									.getMsgFrom(), ma.getMsgOwner());
							break;

						case RecommendMsg:
							messageService.deleteMessageById(ma.getMsg_id());
							break;
						default:
							break;
						}
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

	public void setMessageList(List<MessageForUs> tempList) {
		if (tempList == null)
			this.messageList = new ArrayList<MessageForUs>();
		else
			this.messageList = tempList;
		this.notifyDataSetChanged();
	}

}
