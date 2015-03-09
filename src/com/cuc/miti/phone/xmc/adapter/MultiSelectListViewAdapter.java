package com.cuc.miti.phone.xmc.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * ����ListView��ÿһ��Item�Ĳ��ֺͻ��ƹ���
 * �̳���BaseAdapter
 * @author SongQing
 *
 */
public class MultiSelectListViewAdapter extends BaseAdapter {

	private static Context mContext;
	private List<KeyValueData> sourceItemList;						//�����ݵ�List
//	private static HashMap<Integer,View> map;					//������¼checkbox��ѡ��״��	
	private LayoutInflater mInflater = null;								//�������벼��xml
	private List<Boolean> mChecked;
	private List<KeyValueData> checkedItemList;					//ѡ����List
	private boolean hasCheckBox = true;								//�Ƿ�ӵ�и�ѡ��
	
	public MultiSelectListViewAdapter(Context context){
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
//		map = new HashMap<Integer, View>();
		sourceItemList = new ArrayList<KeyValueData>();
		mChecked = new ArrayList<Boolean>();
		setCheckedItemList(new ArrayList<KeyValueData>());
	}
	public int getCount() {
		return sourceItemList.size();
	}

	public Object getItem(int position) {
		return sourceItemList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	
	/**
	 * ���ص�ǰ�󶨵�ԭʼ����б�
	 * @param sourceItemList
	 */
	public List<KeyValueData> getSourceItemList(){
		return sourceItemList;
	}
	
	/**
	 * ������Ҫѡ��ҳ��󶨵�ԭʼ����б�
	 * @param sourceItemList
	 */
	public void setSourceItemList(List<KeyValueData> sourceItemList){
		this.sourceItemList = sourceItemList;
	}
	public List<Boolean> getSelectedItems(){
		return mChecked;
	} 
	
	/**
	 * ����ԭʼ��ݶ�Ӧ�Ĺ�ѡ״̬�б�
	 * @param mChecked
	 */
	public void setSelectedItems(List<Boolean> mChecked){
		this.mChecked = mChecked;
	}
	public List<KeyValueData> getCheckedItemList() {
		return checkedItemList;
	}
	public void setCheckedItemList(List<KeyValueData> checkedItemList) {
		this.checkedItemList = checkedItemList;
	}
	
	/**
     * �����Ƿ�ӵ�и�ѡ��
     * @param hasCheckBox
     */
    public void setCheckBox(boolean hasCheckBox){
    	this.hasCheckBox = hasCheckBox;
    }
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder=null;
		
		//if( map.get(position) ==null ){
			//��ȡViewHolder����
			holder = new ViewHolder();
			//���벼�ֲ���ֵ��convertView
			convertView = mInflater.inflate(R.layout.listview_item_for_multiselect,null);
			
			holder.tViewItemKey = (TextView)convertView.findViewById(R.id.textViewListViewItemKey_lvITFMS);
			holder.tViewItemValue = (TextView)convertView.findViewById(R.id.textViewListViewItemValue_lvITFMS);
			holder.cBoxItemSelect = (CheckBox)convertView.findViewById(R.id.checkBoxListViewItem_lvITFMS);
		
			final int p = position;
//			map.put(position, convertView);
			// �Ƿ���ʾ��ѡ��
			if(!hasCheckBox){
				holder.cBoxItemSelect.setVisibility(View.INVISIBLE);
			}
			else{
				holder.cBoxItemSelect.setOnClickListener(new View.OnClickListener() {				
					public void onClick(View v) {
						 CheckBox cb = (CheckBox)v; 
						 mChecked.set(p, cb.isChecked());
						 if(cb.isChecked()){
							 checkedItemList.add(sourceItemList.get(p));
						 }else{
							 checkedItemList.remove(sourceItemList.get(p));
						 }
						 
					}
				});
			}
			// Ϊview���ñ�ǩ            
			convertView.setTag(holder);		
//		}
//		else{
//			//���converView�Ѿ����ڣ���ֱ��ȡ��holder			
//			convertView = map.get(position);
//			holder = (ViewHolder)convertView.getTag();
//		}

		// ����list��TextView����ʾ
		holder.tViewItemKey.setText(sourceItemList.get(position).getKey());
		// ����list��TextView����ʾ��Ӧ��id
		holder.tViewItemValue.setText(sourceItemList.get(position).getValue());
		// ���mChecked��ֵ������checkbox��ѡ��״��
		if(hasCheckBox){
			int index = checkedItemList.indexOf(sourceItemList.get(position));
			if(index>-1){
					holder.cBoxItemSelect.setChecked(true);
				}else{
					holder.cBoxItemSelect.setChecked(false);
				}
			//holder.cBoxItemSelect.setChecked(mChecked.get(position));
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
		//ѡ���Keyֵ(��ʾ��)
        TextView tViewItemKey = null;         
        //ѡ���Valueֵ(id��)
        TextView tViewItemValue = null;
        //��ѡ�����ѡ
        CheckBox  cBoxItemSelect = null; 
        //��ѡ�����ѡ
        RadioButton rBtnItemSelect = null;
    }
}
