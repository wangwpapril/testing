package com.cuc.miti.phone.xmc.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.domain.*;
import com.cuc.miti.phone.xmc.logic.AccessoriesService;
import com.cuc.miti.phone.xmc.logic.ManuscriptsService;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.MediaHelper;
import com.cuc.miti.phone.xmc.utils.SendManuscriptsHelper;
import com.cuc.miti.phone.xmc.utils.SyncImageLoader;
import com.cuc.miti.phone.xmc.utils.TimeFormatHelper;
import com.cuc.miti.phone.xmc.utils.ToastHelper;
import com.cuc.miti.phone.xmc.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ManuscriptsListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<Manuscripts> manuscripts;
	// �Ƿ��ڱ༭��״̬
	private boolean editState = false;
	private int checkboxViewImage;
	private int uncheckboxViewImage;

	private ListView mListView;
	private SyncImageLoader syncImageLoader;
	private Bitmap defaultLoadingBitmap;

	public ManuscriptsListAdapter(Context mContext, int checkView,
			int uncheckView, ListView listview) {
		this.mContext = mContext;
		this.manuscripts = new ArrayList<Manuscripts>();
		mInflater = LayoutInflater.from(mContext);
		this.checkboxViewImage = checkView;
		this.uncheckboxViewImage = uncheckView;

		mListView = listview;
		syncImageLoader = new SyncImageLoader(this.mContext);
		mListView.setOnScrollListener(onScrollListener);

		defaultLoadingBitmap = BitmapFactory.decodeResource(
				this.mContext.getResources(), R.drawable.phone_bigpicholder);
	}

	public int getCount() {

		return manuscripts.size();
	}

	public Manuscripts getItem(int position) {

		return manuscripts.get(position);
	}

	public long getItemId(int position) {

		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.editing_manuscripts_listview_item, parent, false);
		}

		// ��ʼ����ͼ�еĿؼ�
		TextView textViewTitle_EditMAItem = (TextView) convertView
				.findViewById(R.id.textViewTitle_EditMAItem);
		TextView textViewTime_EditMAItem = (TextView) convertView
				.findViewById(R.id.textViewTime_EditMAItem);
		TextView textViewContent_EditMAItem = (TextView) convertView
				.findViewById(R.id.textViewContent_EditMAItem);
		ImageView imageViewTitle_EditMAItem = (ImageView) convertView
				.findViewById(R.id.imageViewTitle_EditMAItem);

		ImageView imageViewCheck_EditMAItem = (ImageView) convertView
				.findViewById(R.id.imageViewCheck_EditMAItem);

		// ��ݱ༭״̬���ø���Ķ�ѡ���Ƿ����
		if (this.editState)
			imageViewCheck_EditMAItem.setVisibility(View.VISIBLE);
		else {
			imageViewCheck_EditMAItem.setVisibility(View.GONE);
		}

		// ��ȡָ��λ�õĸ������
		Manuscripts ma = this.manuscripts.get(position);

		// ���ñ�����Ϣ
		textViewTitle_EditMAItem.setText(ma.getTitle().length() > 8 ? ma
				.getTitle().substring(0, 8) : ma.getTitle());

		// ========================��ݸ��״̬����ʱ����Ϣ
		switch (ma.getManuscriptsStatus()) {
		case Elimination:
			// ������̭ʱ����Ϣ
			textViewTime_EditMAItem.setText(TimeFormatHelper.getShortFormatTime(
					TimeFormatHelper.parse(ma.getRereletime()), "HH:mm M.d"));
			break;
		case StandTo:
			// ����ǩ��ʱ����Ϣ
			textViewTime_EditMAItem.setText(TimeFormatHelper.getShortFormatTime(
					TimeFormatHelper.parse(ma.getReletime()), "HH:mm M.d"));
			break;
		case Sent:
			// ���÷���ʱ����Ϣ
			textViewTime_EditMAItem.setText(TimeFormatHelper.getShortFormatTime(
					TimeFormatHelper.parse(ma.getSenttime()), "HH:mm M.d"));
			break;
		case Editing:
			// ���ô���ʱ����Ϣ
			textViewTime_EditMAItem.setText(TimeFormatHelper.getShortFormatTime(
					TimeFormatHelper.parse(ma.getCreatetime()), "HH:mm M.d"));
			break;
		default:
			break;
		}

		// ����������Ϣ
		textViewContent_EditMAItem.setText(ma.getContents().length() > 22 ? ma
				.getContents().substring(0, 22) : ma.getContents());

		if (ma.isHasAccessories() == false)
			imageViewTitle_EditMAItem.setVisibility(View.INVISIBLE);
		else {
			imageViewTitle_EditMAItem.setVisibility(View.VISIBLE);
			imageViewTitle_EditMAItem.setImageBitmap(defaultLoadingBitmap);
		}

		if (ma.getPreViewImage() == null)
			syncImageLoader.loadImage(position, ma, imageLoadListener,
					imageViewTitle_EditMAItem);
		else
			imageViewTitle_EditMAItem.setImageBitmap(ma.getPreViewImage());

		// �ж��Ƿ�ѡ��
		if (ma.isCheck())
			imageViewCheck_EditMAItem.setImageBitmap(BitmapFactory
					.decodeResource(this.mContext.getResources(),
							checkboxViewImage));
		else {
			imageViewCheck_EditMAItem.setImageBitmap(BitmapFactory
					.decodeResource(this.mContext.getResources(),
							uncheckboxViewImage));
		}

		return convertView;
	}

	/**
	 * ����һ���������������첽����ͼƬ�Ļص�
	 */
	SyncImageLoader.OnImageLoadListener imageLoadListener = new SyncImageLoader.OnImageLoadListener() {

		public void onImageLoad(int position, Bitmap bitmap, ImageView imageView) {

			Manuscripts mu = getItem(position);

			if (mu.getPreViewImage() == null && bitmap != null) {
				mu.setPreViewImage(bitmap);

				imageView.setImageBitmap(mu.getPreViewImage());
			}
		}
	};

	/**
	 * ��ȡ������ʾ��item��ţ���֪ͨ���Խ��м���ͼƬ
	 */
	public void loadImage() {
		int start = mListView.getFirstVisiblePosition();
		int end = mListView.getLastVisiblePosition();
		if (end >= getCount()) {
			end = getCount() - 1;
		}
		syncImageLoader.setLoadLimit(start, end);
		syncImageLoader.unlock();
	}

	/**
	 * ���������¼���ֻ����listview������ʱ�Ž��м���
	 */
	AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {

		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case AbsListView.OnScrollListener.SCROLL_STATE_FLING:

				syncImageLoader.lock();
				break;
			case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:

				loadImage();
				// loadImage();
				break;
			case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				syncImageLoader.lock();
				break;

			default:
				break;
			}
		}

		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * ɾ����
	 * 
	 * @param position
	 */
	public void removeItem(int position) {
		this.manuscripts.remove(position);
		this.notifyDataSetChanged();
	}

	/**
	 * ɾ��ѡ����
	 */
	private void removeSelectedItems(List<Manuscripts> tempList) {

		this.manuscripts.removeAll(tempList);

		this.notifyDataSetChanged();
	}

	/**
	 * �����б���ָ��λ����
	 * 
	 * @param position
	 */
	public void updateItem(int position) {

		ManuscriptsService service = new ManuscriptsService(this.mContext);

		Manuscripts ma = this.manuscripts.get(position);

		Manuscripts temp = service.getManuscripts(ma.getM_id());

		// ���ﲻ�������������ʾ�ã�ֻ���±���������
		ma.setTitle(temp.getTitle());
		ma.setContents(temp.getContents());
		ma.setHasAccessories(temp.isHasAccessories());

		// ���Ԥ��ͼ
		ma.setPreViewImage(MediaHelper.getManuscriptPreview(ma.getM_id(),
				this.mContext));

		this.notifyDataSetChanged();
	}

	/**
	 * ѡ���б��е�ָ��λ����
	 * 
	 * @param position
	 *            ָ��λ��
	 */
	public void checkItem(int position) {
		Manuscripts ma = this.manuscripts.get(position);
		ma.setCheck(!ma.isCheck());
		this.notifyDataSetChanged();
	}

	/**
	 * ѡ��/ȡ�� ������
	 * 
	 * @param check
	 */
	public void checkAll(boolean check) {
		for (Manuscripts ma : this.getManuscripts()) {
			ma.setCheck(check);
		}
		this.notifyDataSetChanged();
	}

	/**
	 * �����б���
	 * 
	 * @param ma
	 */
	public void addItemRange(List<Manuscripts> list) {
		if (list != null)
			this.manuscripts.addAll(list);
		this.notifyDataSetChanged();
	}

	/**
	 * ��̭����ѡ�и��
	 */
	public void eliminationSelectedItems() {
		ManuscriptsService service = new ManuscriptsService(this.mContext);

		List<Manuscripts> tempList = new ArrayList<Manuscripts>();

		// ��ȡѡ�еĸ����
		int count = this.manuscripts.size();
		for (int i = 0; i < count; i++) {
			Manuscripts ma = this.manuscripts.get(i);
			if (ma.isCheck()) {

				try {
					// boolean result = service.deleteById(ma.getM_id());
					boolean result = service.elimination(ma.getM_id());
					if (result)
						tempList.add(ma);
				} catch (Exception e) {
					Logger.e(e);
				}
			}
		}
	}

	/**
	 * ��̭ѡ�и��
	 * 
	 * @param index
	 */
	public void eliminationItem(int position) {

		Manuscripts ma = this.manuscripts.get(position);

		if (ma != null) {
			ManuscriptsService service = new ManuscriptsService(this.mContext);

			try {
				// boolean result = service.deleteById(ma.getM_id());
				boolean result = service.elimination(ma.getM_id());
				// if(result)
				// this.removeItem(position);
			} catch (Exception e) {
				Logger.e(e);
			}
		}
	}

	/**
	 * ɾ������ѡ�и��
	 */
	public void deleteSelectedItems() {
		ManuscriptsService service = new ManuscriptsService(this.mContext);

		// ��ȡѡ�еĸ����
		int count = this.manuscripts.size();
		for (int i = 0; i < count; i++) {
			Manuscripts ma = this.manuscripts.get(i);
			if (ma.isCheck()) {

				try {
					boolean result = service.deleteById(ma.getM_id());
				} catch (Exception e) {
					Logger.e(e);
				}
			}
		}
	}

	/**
	 * ����ɾ����
	 * 
	 * @param position
	 */
	public void deleteItem(int position) {
		Manuscripts ma = this.manuscripts.get(position);

		if (ma != null) {
			ManuscriptsService service = new ManuscriptsService(this.mContext);

			try {
				boolean result = service.deleteById(ma.getM_id());
			} catch (Exception e) {
				Logger.e(e);
			}
		}
	}

	/**
	 * �ָ����
	 * 
	 * @param position
	 */
	public void recoverItem(int position) {
		Manuscripts ma = this.manuscripts.get(position);

		if (ma != null) {
			ManuscriptsService service = new ManuscriptsService(this.mContext);

			try {
				boolean result = service.Editing(ma.getM_id());
			} catch (Exception e) {
				Logger.e(e);
			}
		}
	}

	/**
	 * �ָ�ѡ�и��
	 * 
	 * @param position
	 */
	public void recoverSelectedItem() {
		ManuscriptsService service = new ManuscriptsService(this.mContext);

		// ��ȡѡ�еĸ����
		int count = this.manuscripts.size();
		for (int i = 0; i < count; i++) {
			Manuscripts ma = this.manuscripts.get(i);
			if (ma.isCheck()) {

				try {
					boolean result = service.Editing(ma.getM_id());
				} catch (Exception e) {
					Logger.e(e);
				}
			}
		}
	}

	/**
	 * ���͸��
	 * 
	 * @param position
	 */
	public void sentItem(int position) {
		Manuscripts mu = this.manuscripts.get(position);

		if (mu != null) {
			ManuscriptsService service = new ManuscriptsService(this.mContext);

			try {
				mu = service.getManuscripts(mu.getM_id());
				AccessoriesService acc_service = new AccessoriesService(
						this.mContext);
				List<Accessories> accs = acc_service.getAccessoriesListByMID(mu
						.getM_id());

				KeyValueData message = new KeyValueData("", "");
				boolean result = SendManuscriptsHelper.validateForReleManuscript(mu,
						message, this.mContext);

				if (result == true) {
					service.sendManuscripts(mu, accs);
				} else {
					//Toast.makeText(this.mContext, "����ʧ�ܣ���������༭У������Ϣ",Toast.LENGTH_LONG).show();
					ToastHelper.showToast(ToastHelper.getStringFromResources(R.string.failedToSendValidateAgain),Toast.LENGTH_SHORT);
				}
			} catch (Exception e) {
				Logger.e(e);
			}
		}
	}

	/**
	 * ����ѡ�и��
	 */
	public int sentSelectedItems() {
		ManuscriptsService service = new ManuscriptsService(this.mContext);

		int success = 0;

		// ��ȡѡ�еĸ����
		int count = this.manuscripts.size();
		int checkedcount = 0;				//ѡ�и������

		KeyValueData message = new KeyValueData("", "");
		
		boolean result = false;

//		boolean result = SendManuscriptsHelper.validateNetwork(message,this.mContext);
//
//		if (result == false) {
//			Toast.makeText(this.mContext, "���粻���ã��޷����ͣ�", Toast.LENGTH_LONG)
//					.show();
//			return count;
//		}

		for (int i = 0; i < count; i++) {
			Manuscripts mu = this.manuscripts.get(i);
			if (mu.isCheck()) {
				
				checkedcount++;
				try {
					mu = service.getManuscripts(mu.getM_id());
					AccessoriesService acc_service = new AccessoriesService(this.mContext);
					List<Accessories> accs = acc_service.getAccessoriesListByMID(mu.getM_id());

					message = new KeyValueData("", "");
					result = SendManuscriptsHelper.validateForReleManuscript(mu, message,this.mContext);

					if (result == true) {
						service.sendManuscripts(mu, accs);
						success++;
					}
				} catch (Exception e) {
					Logger.e(e);
				}
			}
		}

		String successMessage = String.valueOf(success) + ToastHelper.getStringFromResources(R.string.succeedToSendManu);

		int error = checkedcount - success;
		String errorMessage = (error == 0) ? "" : "," + String.valueOf(error)
				+ ToastHelper.getStringFromResources(R.string.failedToSendManusValidateAgain);

		/*Toast.makeText(this.mContext, successMessage + errorMessage,
				Toast.LENGTH_LONG).show();*/
		ToastHelper.showToast(successMessage + errorMessage,Toast.LENGTH_SHORT);
		return success;
	}

	public void setEditState(boolean editState) {
		this.editState = editState;
		this.notifyDataSetChanged();
	}

	public boolean isEditState() {
		return editState;
	}

	public List<Manuscripts> getManuscripts() {
		return this.manuscripts;
	}

	public void setManuscripts(List<Manuscripts> tempList) {
		if (tempList == null)
			this.manuscripts = new ArrayList<Manuscripts>();
		else
			this.manuscripts = tempList;
		syncImageLoader.restore();
		this.notifyDataSetChanged();
	}
}
