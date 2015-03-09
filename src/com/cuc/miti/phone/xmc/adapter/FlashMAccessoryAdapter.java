package com.cuc.miti.phone.xmc.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.domain.Accessories;
import com.cuc.miti.phone.xmc.domain.Enums.AccessoryType;
import com.cuc.miti.phone.xmc.logic.AccessoriesService;
import com.cuc.miti.phone.xmc.utils.MediaHelper;
import com.cuc.miti.phone.xmc.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FlashMAccessoryAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;						//������findViewById,������layout�ļ����µ�xml�����ļ���ʵ��		
	private List<Accessories> accessoriesList;			//�����б�
	private Context mContext;	
	private AccessoryType mAccessoryType;				//�����б��и�������
	AccessoriesService accService;
	
	/**
	 * ���캯��
	 * @param context
	 */
	public FlashMAccessoryAdapter(List<Accessories> accessories , Context context , AccessoryType accessoryType){
		super();
		this.accessoriesList = accessories;
		if (accessoriesList == null)
			accessoriesList = new ArrayList<Accessories>();
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.mAccessoryType = accessoryType;
	}
		
	public int getCount() {
		if (null != accessoriesList) {
			return accessoriesList.size();	
		} else {
			return 0;
		}
	}

	public Object getItem(int position) {
		return accessoriesList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	
	/**
	 * ��ݸ������ʹ���Ԥ��ͼ
	 * 
	 * @param accPath ����·��
	 * @return
	 */
	private Bitmap createItemImage(String accPath) {
		Bitmap bitmap = null;
		switch (MediaHelper.checkFileType(accPath) ){
			case Picture:
				//bitmap = MediaHelper.getImageThumbnail(accPath, 162, 162);
				bitmap = MediaHelper.getImageThumbnail(accPath, 173, 172);
				break;
			case Video:
				//bitmap = MediaHelper.getVideoThumbnail(accPath, 162, 162);
				bitmap = MediaHelper.getVideoThumbnail(accPath, 173, 172);
				break;
			case Voice:
				bitmap = BitmapFactory.decodeResource(
														mContext.getResources(),
														R.drawable.voice_default_f_manuscript);
				break;
			default:
				break;
		}
		return bitmap;
	}

	/**
	 * ����µĸ������󵽸����б���
	 * 
	 * @param temp
	 */
	public void addCurrentAccessories(Accessories temp) {		
		if (this.accessoriesList == null){
			accessoriesList = new ArrayList<Accessories>();
		}
		Bitmap tempBp = createItemImage(temp.getUrl());
		temp.setImage(tempBp);
		//accessoriesList.add(0,temp);
		accessoriesList.add(accessoriesList.size()-1, temp);
//		accService=new AccessoriesService(mContext);
//		accService.addAccessories(temp);
//		
//		Log.i("Accessories",  accService.getAccessories(temp.getA_id()).toString());
		
		// ֪ͨgridview��ݷ���仯
		this.notifyDataSetChanged();
	}

	/**
	 * ��ݸ����༭ҳ�淵��ֵ���µ�ǰ�����б�
	 * 
	 * @param acc
	 */
	public void updateCurrentAccessories(Accessories acc) {

		for (int i = 0; i < this.accessoriesList.size(); i++) {
			Accessories temp = this.accessoriesList.get(i);
			if (acc.getA_id().equals(temp.getA_id())) {
				temp.setTitle(acc.getTitle());
				temp.setDesc(acc.getDesc());
				temp.setImage(createItemImage(temp.getUrl()));
//				accService=new AccessoriesService(mContext);
//				accService.updateAccessories(temp);
//				Log.i("Accessories", accService.getAccessories(temp.getA_id()).toString());
			}
		}

		// ֪ͨgridview��ݷ���仯
		this.notifyDataSetChanged();
	}

	/**
	 * �Ӹ����б���ɾ�����еĸ�������
	 * @param index
	 */
	private void removeAccessoriesById(int index) {
		if (index >= 0) {
			// ɾ����ݿ��е��б��ļ�
			Accessories tempAcc = this.accessoriesList.get(index);
			accService=new AccessoriesService(mContext);
			boolean result = accService.deleteAccessoriesByID(tempAcc.getA_id());
			// ���ɹ���ɾ����ʱ�����б��е��ļ�
			if (result) {
				MediaHelper.deleteFile(tempAcc.getUrl());
				this.accessoriesList.remove(index);
				// ֪ͨgridview��ݷ���仯
				this.notifyDataSetChanged();
			}
		}
	}
	
	/**
	 * ��ȡ��ǰ�ĸ����б�
	 * @return
	 */
	public List<Accessories> getCurrentAccessoriesList(){
		return this.accessoriesList;
	}
	
	/**
	 *  ����һ���µ�GridView Item����
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder;
		//if(convertView == null){
			//ȡ��layout�ļ����µ�gridview_item_f_manuscripts�����ļ���ʵ��	
			convertView = mInflater.inflate(R.layout.gridview_item_f_manuscripts, null);
			mHolder = new ViewHolder();
			mHolder.tViewItemInfo = (TextView)convertView.findViewById(R.id.textViewGridViewItemText_gvIFM); 
			mHolder.thumbnail = (ImageView)convertView.findViewById(R.id.imageViewGridViewItemPic_gvIFM);
			mHolder.removeIcon = (ImageView)convertView.findViewById(R.id.imageViewGridViewItemRemove_gvIFM);
			
			convertView.setTag(mHolder);			
//		}else{
//			mHolder = (ViewHolder)convertView.getTag();
//		}
		Bitmap bitmap = null;
		
		if(position ==(accessoriesList.size() - 1 )){//������Ĭ�ϵ���Ӱ�ť
			switch(this.mAccessoryType){
				case Voice:
					mHolder.thumbnail.setBackgroundResource(R.drawable.add_voice_f_manuscript_800x480);
					break;
				case Video:
					mHolder.thumbnail.setBackgroundResource(R.drawable.add_video_f_manuscript_800x480);
					break;
				case Picture:
					mHolder.thumbnail.setBackgroundResource(R.drawable.add_pic_f_manuscript_800x480);
					break;
				default:
					break;
			}
			mHolder.removeIcon.setVisibility(View.INVISIBLE);
		}else{
			//��ȡ��ǰλ��(position)��Ӧ�ĸ����б��еĸ�������
			Accessories accessoriesItem = accessoriesList.get(position);
			//bitmap = createItemImage(accessoriesItem.getUrl());
			
			mHolder.thumbnail.setImageBitmap(accessoriesItem.getImage());							//��������ͼ
			String tempTitle = accessoriesItem.getTitle();
			if(tempTitle.length()>10){
				tempTitle = tempTitle.substring(0,10);
			}
			mHolder.tViewItemInfo.setText(tempTitle);		//���ü��
			mHolder.removeIcon.setTag(position);										//����ɾ��ť��List�еĶ�Ӧ��
			mHolder.removeIcon.setVisibility(View.VISIBLE);						//����ɾ��ť�ɼ�
			mHolder.removeIcon.setOnClickListener(new View.OnClickListener() {	
				
				public void onClick(final View view) {
					AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
			    	builder.setTitle(R.string.accessory_delete_alert_title);
			    	builder.setMessage(R.string.accessory_delete_alert_content);
			    	builder.setPositiveButton(R.string.confirm_button,new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							ImageView iViewDelete = (ImageView) view;
							int id = Integer.parseInt(iViewDelete.getTag().toString());
							removeAccessoriesById(id);							
						}
					});
			    	builder.setNegativeButton(R.string.cancel_button, null);
			    	builder.show();	
				}
			});
		}
		return convertView;		
	}
	
	/**
	 * ���ٲ���Ҫ�ĵ���findViewById,�ѶԵ��µĿؼ����ô���ViewHolder����,
	 * ����View.setTag(holder)�������view��,�´ξͿ���ֱ��ȡ���ˡ�
	 * @author SongQing
	 *
	 */
	static class ViewHolder { 	
		//����������Ϣ
        TextView tViewItemInfo = null; 
        //����ͼ(����ͼƬ����Ƶ֮�⣬����Ĭ�ϵ�ͼƬ����)
        ImageView thumbnail = null; 
        //���б����Ƴ����ť
        ImageView removeIcon = null;
        }
}
