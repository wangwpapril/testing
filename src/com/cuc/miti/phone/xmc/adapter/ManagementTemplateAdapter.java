package com.cuc.miti.phone.xmc.adapter;

import java.util.ArrayList;
import java.util.List;

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

public class ManagementTemplateAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;														//������findViewById,������layout�ļ����µ�xml�����ļ���ʵ��		
	private List<ManuscriptTemplate> manuscriptTemplateList;				//��ǩģ���б�
	private Context mContext;	
	//private AccessoryType mAccessoryType;											//�����б��и�������
	
	/**
	 * ���캯��
	 * @param context
	 */
	public ManagementTemplateAdapter(List<ManuscriptTemplate> manuscriptTemplateList , Context context ){
		super();
		this.manuscriptTemplateList = manuscriptTemplateList;
		if (manuscriptTemplateList == null)
			manuscriptTemplateList = new ArrayList<ManuscriptTemplate>();
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		//this.mAccessoryType = accessoryType;
	}
		
	public int getCount() {
		if (null != manuscriptTemplateList) {
			return manuscriptTemplateList.size();	
		} else {
			return 0;
		}
	}

	public Object getItem(int position) {
		return manuscriptTemplateList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	
	public void deleteItem(int position){
		manuscriptTemplateList.remove(position);
	}
	
	public void updateList(List<ManuscriptTemplate> manuscriptTemplateList){
		this.manuscriptTemplateList = manuscriptTemplateList;
	}
	

	
	/**
	 *  ����һ���µ�ListView Item����
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder;
			//ȡ��layout�ļ����µ�gridview_item_f_manuscripts�����ļ���ʵ��	
//		if (convertView == null) {
//			convertView = mInflater.inflate(
//					R.layout.listview_item_template_m, parent, false);
//		}
			convertView = mInflater.inflate(R.layout.listview_item_template_m, null);
			mHolder = new ViewHolder();
			mHolder.tViewItemName = (TextView)convertView.findViewById(R.id.textViewListViewItemText_lvMTA); 
			mHolder.goIntoItem = (ImageView)convertView.findViewById(R.id.imageViewListViewItemPic_lvMTA);
			
			ManuscriptTemplate manuscriptTemplateItem = manuscriptTemplateList.get(position);
			
			if(manuscriptTemplateItem.getName().length()>12){
				mHolder.tViewItemName.setText(manuscriptTemplateItem.getName().substring(0, 11));	
			}else{
				mHolder.tViewItemName.setText(manuscriptTemplateItem.getName());	
			}
			
			if(manuscriptTemplateItem.getIsdefault().getValue()=="1"){
				mHolder.goIntoItem.setBackgroundResource(R.drawable.login_check_960x540);	
			}
			else{
				mHolder.goIntoItem.setBackgroundResource(R.drawable.phone_common_dropdownlist);	
			}
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
		//��ǩģ������
        TextView tViewItemName = null; 
        //ͼƬ
        ImageView goIntoItem = null; 
        
        }
}
