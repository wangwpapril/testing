package com.cuc.miti.phone.xmc.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cuc.miti.phone.xmc.domain.Accessories;
import com.cuc.miti.phone.xmc.logic.AccessoriesService;
import com.cuc.miti.phone.xmc.utils.MediaHelper;
import com.cuc.miti.phone.xmc.R;

public class ChooseAccessoriesAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Accessories> accessories;
	private Context mContext;
	private boolean enable;

	public ChooseAccessoriesAdapter(List<Accessories> accs, Context context) {
		super();
		accessories = accs;
		if (accessories == null)
			accessories = new ArrayList<Accessories>();
		for (Accessories acc : accessories) {
			acc.setChoose(false);
		}
		this.mContext = context;

		inflater = LayoutInflater.from(context);

	}

	public int getCount() {
		if (null != accessories) {
			return accessories.size();
		} else {
			return 0;
		}
	}

	public Object getItem(int position) {
		return accessories.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;

		convertView = inflater.inflate(R.layout.choose_accessories_item, null);
		viewHolder = new ViewHolder();
		viewHolder.title = (TextView) convertView
				.findViewById(R.id.textViewItemTitle_CA);
		viewHolder.image = (ImageView) convertView
				.findViewById(R.id.imageViewItem_CA);
		viewHolder.imageChoose = (ImageButton) convertView
				.findViewById(R.id.imageViewItemChoose_CA);
		convertView.setTag(viewHolder);

		final Accessories acc = accessories.get(position);

		// item�ֵ
		String titleString = acc.getTitle();

		if (titleString.length() > 10) {
			titleString = titleString.substring(0, 10);
		}
		viewHolder.title.setText(titleString);
		if (acc.getImage() == null) {
			acc.setImage(MediaHelper.createItemImage(acc.getUrl(),
					this.mContext, 100, 70));
		}
		viewHolder.image.setImageBitmap(acc.getImage());
		if (acc.isChoose())
			viewHolder.imageChoose.setVisibility(View.VISIBLE);
		else
			viewHolder.imageChoose.setVisibility(View.GONE);
		viewHolder.image.setOnClickListener(new OnClickListener() {
			public void onClick(final View arg0) {

				if (acc.isChoose()) {
					acc.setChoose(false);
					viewHolder.imageChoose.setVisibility(View.GONE);
				} else {
					acc.setChoose(true);
					viewHolder.imageChoose.setVisibility(View.VISIBLE);
				}
			}
		});

		return convertView;
	}

	// ======================�Ը������в���==============================
	/**
	 * ����µĸ������󵽸����б���
	 * 
	 * @param temp
	 */
	public void addCurrentAccessories(Accessories temp) {
		if (this.accessories == null)
			accessories = new ArrayList<Accessories>();

		temp.setImage(MediaHelper.createItemImage(temp.getUrl(), this.mContext,
				100, 70));
		accessories.add(temp);

		// if (EditManuscriptsActivity.this.accessories.size() > 0)
		// llContent_editM.setLayoutParams(new LinearLayout.LayoutParams(
		// LayoutParams.FILL_PARENT, 600));

		// ֪ͨgridview��ݷ���仯
		this.notifyDataSetChanged();
	}

	/**
	 * ��ݸ����༭ҳ�淵��ֵ���µ�ǰ�����б��ж�Ӧ��ҳ��
	 * 
	 * @param acc
	 */
	public void updateCurrentAccessories(Accessories acc) {

		for (int i = 0; i < this.accessories.size(); i++) {
			Accessories temp = this.accessories.get(i);
			if (acc.getA_id().equals(temp.getA_id())) {
				temp.setTitle(acc.getTitle());
				temp.setDesc(acc.getDesc());
				temp.setImage(MediaHelper.createItemImage(temp.getUrl(),
						this.mContext, 100, 70));
				break;
			}
		}

		// if (EditManuscriptsActivity.this.accessories.size() > 0)
		// llContent_editM.setLayoutParams(new LinearLayout.LayoutParams(
		// LayoutParams.FILL_PARENT, 600));

		// ֪ͨgridview��ݷ���仯
		this.notifyDataSetChanged();
	}

	/**
	 * �Ӹ����б���ɾ�����еĸ�������
	 * 
	 * @param id
	 */
	public void removeAccessoriesById(int index) {
		if (index >= 0) {
			// ɾ����ݿ��е��б��ļ�
			Accessories tempAcc = this.accessories.get(index);
			AccessoriesService service = new AccessoriesService(this.mContext);
			boolean result = service.deleteAccessoriesByID(tempAcc.getA_id());
			// ���ɹ���ɾ����ʱ�����б��е��ļ�
			if (result) {
				MediaHelper.deleteFile(tempAcc.getUrl());
				this.accessories.remove(index);
				// ֪ͨgridview��ݷ���仯
				this.notifyDataSetChanged();
			}
		}
	}

	class ViewHolder {
		public TextView title;
		public ImageView image;
		public ImageButton imageChoose;
	}
}
