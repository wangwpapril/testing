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

public class EditManuscriptAccessoriesAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Accessories> accessories;
	private Context mContext;
	private boolean enable;

	public EditManuscriptAccessoriesAdapter(List<Accessories> accs,
			Context context, boolean enable) {
		super();
		accessories = accs;
		if (accessories == null)
			accessories = new ArrayList<Accessories>();

		this.mContext = context;

		inflater = LayoutInflater.from(context);

		this.enable = enable;
	}

	public EditManuscriptAccessoriesAdapter(List<Accessories> accs,
			Context context) {
		this(accs, context, true);
	}

	public int getCount() {
		if (null != accessories) {
			return accessories.size();
		} else {
			return 0;
		}
	}

	public List<Accessories> getAccessories() {
		return accessories;
	}

	public void setAccessories(List<Accessories> tempAccs) {
		accessories = tempAccs;
	}

	public Object getItem(int position) {
		return accessories.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		// if (convertView == null) {
		// ��ʼ��Item�����
		convertView = inflater.inflate(
				R.layout.edit_manuscript_accessories_item, null);
		viewHolder = new ViewHolder();
		viewHolder.title = (TextView) convertView
				.findViewById(R.id.textViewItemTitle_EMA);
		viewHolder.image = (ImageView) convertView
				.findViewById(R.id.imageViewItem_EMA);
		viewHolder.imageRemove = (ImageButton) convertView
				.findViewById(R.id.imageViewItemRemove_EMA);
		convertView.setTag(viewHolder);
		// } else {
		// viewHolder = (ViewHolder) convertView.getTag();
		// }

		Accessories acc = accessories.get(position);

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

		if (this.enable) {

			viewHolder.imageRemove.setFocusable(false);
			viewHolder.imageRemove.setClickable(false);
			viewHolder.imageRemove.setTag(position);

			viewHolder.imageRemove.setOnClickListener(new OnClickListener() {
				public void onClick(final View arg0) {

					AlertDialog.Builder builder = new AlertDialog.Builder(
							mContext);
					builder.setTitle("��ʾ");
					builder.setMessage("�Ƿ�ɾ��˸�����");
					builder.setPositiveButton("��",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface arg1,
										int arg2) {
									ImageButton btnRemove = (ImageButton) arg0;
									int id = Integer.parseInt(btnRemove
											.getTag().toString());
									removeAccessoriesById(id);
								}
							});
					builder.setNegativeButton("��",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {

									dialog.cancel();
								}
							});
					builder.show();
				}
			});
		} else {
			viewHolder.imageRemove.setVisibility(View.GONE);
		}
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

	// ���ÿؼ��Ƿ����
	public void setEnable(boolean enable) {
		this.enable = enable;
		this.notifyDataSetChanged();
	}

	public boolean isEnable() {
		return enable;
	}

	class ViewHolder {
		public TextView title;
		public ImageView image;
		public ImageButton imageRemove;
	}
}
