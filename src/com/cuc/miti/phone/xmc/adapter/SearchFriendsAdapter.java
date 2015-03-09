package com.cuc.miti.phone.xmc.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cuc.miti.phone.xmc.domain.UserBean;
import com.cuc.miti.phone.xmc.R;

public class SearchFriendsAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;														//������findViewById,������layout�ļ����µ�xml�����ļ���ʵ��		
	private List<UserBean> usersList;				//�����б�
	private Context mContext;	
	//private AccessoryType mAccessoryType;											//�����б��и�������
	
	/**
	 * ���캯��
	 * @param context
	 */
	public SearchFriendsAdapter(List<UserBean> usersList , Context context ){
		super();
		this.usersList = usersList;
		if (usersList == null)
			usersList = new ArrayList<UserBean>();
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
	
	public void updateList(List<UserBean> usersList){
		this.usersList = usersList;
	}
	

	
	/**
	 *  ����һ���µ�ListView Item����
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder;
			//ȡ��layout�ļ����µ�gridview_item_f_manuscripts�����ļ���ʵ��	
			convertView = mInflater.inflate(R.layout.listview_item_searchfriends, null);
			mHolder = new ViewHolder();
			mHolder.tViewItemName = (TextView)convertView.findViewById(R.id.textViewItemName_lvSF); 
			mHolder.tViewItemUsername = (TextView)convertView.findViewById(R.id.textViewItemUserName_lvSF); 
			
			UserBean usersListItem = usersList.get(position);
					
			mHolder.tViewItemName.setText(usersListItem.getName());
			mHolder.tViewItemUsername.setText(usersListItem.getUserName());
			
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
		
        TextView tViewItemName = null; 
        TextView tViewItemUsername = null;  
        
        
        }
}
