package com.cuc.miti.phone.xmc.adapter;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.RosterEntry;

import com.cuc.miti.phone.xmc.domain.Accessories;
import com.cuc.miti.phone.xmc.domain.ManuscriptTemplate;
import com.cuc.miti.phone.xmc.domain.Enums.AccessoryType;
import com.cuc.miti.phone.xmc.logic.AccessoriesService;
import com.cuc.miti.phone.xmc.logic.ManuscriptTemplateService;
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

public class ManagementFriendsAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;														//������findViewById,������layout�ļ����µ�xml�����ļ���ʵ��		
	private List<RosterEntry> usersList;				//�����б�
	private Context mContext;	
	//private AccessoryType mAccessoryType;											//�����б��и�������
	
	/**
	 * ���캯��
	 * @param context
	 */
	public ManagementFriendsAdapter(List<RosterEntry> usersList , Context context ){
		super();
		this.usersList = usersList;
		if (usersList == null)
			usersList = new ArrayList<RosterEntry>();
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		//this.mAccessoryType = accessoryType;
	}
		
	public int getCount() {
		if (null != usersList) {
			return usersList.size();	
		} else {
			return 0;
		}
	}

	public Object getItem(int position) {
		return usersList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	
	public void deleteItem(int position){
		usersList.remove(position);
	}
	
	public void updateList(List<RosterEntry> usersList){
		this.usersList = usersList;
	}
	

	
	/**
	 *  ����һ���µ�ListView Item����
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder;
			//ȡ��layout�ļ����µ�gridview_item_f_manuscripts�����ļ���ʵ��	
			convertView = mInflater.inflate(R.layout.listview_item_friends, null);
			mHolder = new ViewHolder();
			mHolder.tViewItemName = (TextView)convertView.findViewById(R.id.textViewListViewItemText_lvMF); 
			
			RosterEntry usersListItem = usersList.get(position);
			//���ڵõ�������ƣ�ͨ�����JID�õ�
			String userName="";
			String user=usersListItem.getUser();
			if(usersListItem.getName()== null)
			{
				userName=user.substring(0,user.indexOf("@"));
			}else{
				userName=usersListItem.getName();
			}
					
			mHolder.tViewItemName.setText(userName);
			
			convertView.setTag(mHolder);			

		return convertView;		
	}
	
	/**
	 * ���ٲ���Ҫ�ĵ���findViewById,�ѶԵ��µĿؼ����ô���ViewHolder����,
	 * ����View.setTag(holder)�������view��,�´ξͿ���ֱ��ȡ���ˡ�
	 * @author SongQing
	 *
	 */
	static class ViewHolder { 	
		//��������
        TextView tViewItemName = null; 
        
        }
}
