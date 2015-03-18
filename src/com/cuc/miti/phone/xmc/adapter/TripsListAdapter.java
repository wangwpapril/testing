package com.cuc.miti.phone.xmc.adapter;

import java.util.List;

import com.cuc.miti.phone.xmc.R;
import com.cuc.miti.phone.xmc.models.Destination;
import com.cuc.miti.phone.xmc.ui.BaseActivity1;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 *
 * @author wangyanpeng
 */

public class TripsListAdapter extends MyBaseAdapter {
	private List<Destination> datas;


	public TripsListAdapter(List<Destination> datas,
			BaseActivity1 context) {
		this.datas = datas;
		this.context = context;
		super.init();
	}

	@Override
	public int getCount() {
		return datas == null ? 0 : datas.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		LogUtil.d(position + "");
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.catagory_top_item,
					null);

			holder.ivIcon = (ImageView) convertView
					.findViewById(R.id.category_top_item_iv);
			holder.tvName = (TextView) convertView
					.findViewById(R.id.category_top_item_name);
			holder.tvDesc = (TextView) convertView
					.findViewById(R.id.category_item_top_desc);
			holder.llBottomLine = convertView
					.findViewById(R.id.category_bottom_gray_line);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(position == datas.size()-1){
			holder.llBottomLine.setVisibility(View.VISIBLE);
		}else{
			holder.llBottomLine.setVisibility(View.GONE);
		}
		Destination model = datas.get(position);
		holder.tvName.setText(model.name);
		holder.tvDesc.setText(model.type);
		final ImageView imageView = holder.ivIcon;
		imageView.setTag(model.imageFlag.version3.sourceUrl);
//		ImageLoader.DisplayImage(null, context, imageView);
		ImageLoader.DisplayImage(model.imageFlag.version3.sourceUrl, context, imageView);
		convertView.setTag(holder);
		return convertView;
	}

	private class ViewHolder {
		public ImageView ivIcon;
		public TextView tvName;
		public TextView tvDesc;
		public View llBottomLine;
	}

}
