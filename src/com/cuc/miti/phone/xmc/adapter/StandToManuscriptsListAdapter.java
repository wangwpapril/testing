package com.cuc.miti.phone.xmc.adapter;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cuc.miti.phone.xmc.R;
import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Enums.UploadTaskStatus;
import com.cuc.miti.phone.xmc.domain.Manuscripts;
import com.cuc.miti.phone.xmc.domain.StandToUploadManuscripts;
import com.cuc.miti.phone.xmc.domain.UploadTask;
import com.cuc.miti.phone.xmc.logic.ManuscriptsService;
import com.cuc.miti.phone.xmc.logic.UploadTaskService;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.MediaHelper;
import com.cuc.miti.phone.xmc.utils.SyncImageLoader;
import com.cuc.miti.phone.xmc.utils.TimeFormatHelper;

/**
 * ���������Ͷ��й���ҳ���ListView��ÿһ��Item�Ĳ��ֺͻ��ƹ����������������ʵʱ���±仯���� �̳���BaseAdapter
 * 
 * @author SongQing
 * 
 */
public class StandToManuscriptsListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater = null; // ��������XML����
	private List<StandToUploadManuscripts> standToUploadManuscriptsList; // �ϴ������е���������б�
	private boolean editState = false; // �Ƿ��ڱ༭��״̬

	private ListView mListView;
	private SyncImageLoader syncImageLoader;
	private Bitmap defaultLoadingBitmap;

	ManuscriptsService manuscriptsService = new ManuscriptsService(
			this.mContext);
	UploadTaskService mUploadTaskService = new UploadTaskService(this.mContext);

	/**
	 * StandToManuscriptAdapter���캯��
	 * 
	 * @param context
	 * @param rootNode
	 *            ��ڵ�
	 */
	public StandToManuscriptsListAdapter(Context context, ListView listview) {
		this.mContext = context;
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.standToUploadManuscriptsList = new ArrayList<StandToUploadManuscripts>();

		mListView = listview;
		syncImageLoader = new SyncImageLoader(this.mContext);
		mListView.setOnScrollListener(onScrollListener);

		defaultLoadingBitmap = BitmapFactory.decodeResource(
				this.mContext.getResources(), R.drawable.phone_bigpicholder);
	}

	/**
	 * ��ȡ�󶨵�����
	 */
	public int getCount() {
		return standToUploadManuscriptsList.size();
	}

	/**
	 * ��ȡ���б���ָ��λ�õĶ���
	 */
	public StandToUploadManuscripts getItem(int position) {
		if (standToUploadManuscriptsList == null
				|| standToUploadManuscriptsList.size() <= position)
			return null;
		return standToUploadManuscriptsList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		convertView = mInflater.inflate(
				R.layout.standto_manuscripts_listview_item, parent, false);

		// ��ʼ����ͼ�еĿؼ�
		TextView textViewTitle_StandTMAItem = (TextView) convertView
				.findViewById(R.id.textViewTitle_StandTMAItem);
		TextView textViewTime_StandTMAItem = (TextView) convertView
				.findViewById(R.id.textViewTime_StandTMAItem);
		TextView textViewContent_StandTMAItem = (TextView) convertView
				.findViewById(R.id.textViewContent_StandTMAItem);
		TextView textView_size_StandTMAItem = (TextView) convertView
				.findViewById(R.id.textView_size_StandTMAItem);
		TextView textView_time_StandTMAItem = (TextView) convertView
				.findViewById(R.id.textView_time_StandTMAItem);
		TextView textView_timeTitle_StandTMAItem = (TextView) convertView
				.findViewById(R.id.textView_timeTitle_StandTMAItem);
		ImageView imageViewTitle_StandTMAItem = (ImageView) convertView
				.findViewById(R.id.imageViewTitle_StandTMAItem);
		ImageView imageViewCheck_StandTMAItem = (ImageView) convertView
				.findViewById(R.id.imageViewCheck_StandTMAItem);

		ProgressBar progressBarSendProgress_StandTMAItem = (ProgressBar) convertView
				.findViewById(R.id.progressBar_sendProgress_StandTMAItem);

		// ��ݱ༭״̬���ø���Ķ�ѡ���Ƿ����
		if (this.editState)
			imageViewCheck_StandTMAItem.setVisibility(View.VISIBLE);
		else {
			imageViewCheck_StandTMAItem.setVisibility(View.GONE);
		}

		// ��ȡָ��λ�õ��ϴ��������
		StandToUploadManuscripts stumItem = this.standToUploadManuscriptsList
				.get(position);

		// ���ñ�����Ϣ
		textViewTitle_StandTMAItem.setText(stumItem.getManuscripts().getTitle()
				.length() > 8 ? stumItem.getManuscripts().getTitle()
				.substring(0, 8) : stumItem.getManuscripts().getTitle());
		// ���ô���ʱ����Ϣ
		textViewTime_StandTMAItem
				.setText(TimeFormatHelper.getShortFormatTime(
						TimeFormatHelper.parse(stumItem.getManuscripts()
								.getManuscriptTemplate().getCreatetime()),
						"HH:mm M.d"));
		// ����������Ϣ
		textViewContent_StandTMAItem.setText(stumItem.getManuscripts()
				.getContents().length() > 22 ? stumItem.getManuscripts()
				.getContents().substring(0, 22) : stumItem.getManuscripts()
				.getContents());

		if (stumItem.getManuscripts().isHasAccessories() == false)
			imageViewTitle_StandTMAItem.setVisibility(View.INVISIBLE);
		else {
			imageViewTitle_StandTMAItem.setVisibility(View.VISIBLE);
			imageViewTitle_StandTMAItem.setImageBitmap(defaultLoadingBitmap);
		}

		if (stumItem.getManuscripts().getPreViewImage() == null) {
			syncImageLoader.loadImage(position, stumItem.getManuscripts(),
					imageLoadListener, imageViewTitle_StandTMAItem);
		} else {
			imageViewTitle_StandTMAItem.setImageBitmap(stumItem
					.getManuscripts().getPreViewImage());
		}

		// �ж��Ƿ�ѡ��
		if (stumItem.getManuscripts().isCheck())
			imageViewCheck_StandTMAItem.setImageBitmap(BitmapFactory
					.decodeResource(this.mContext.getResources(),
							R.drawable.phone_draftstate_standtosent_check));
		else {
			imageViewCheck_StandTMAItem.setImageBitmap(BitmapFactory
					.decodeResource(this.mContext.getResources(),
							R.drawable.phone_draftstate_standtosent_editcheck));
		}

		// �����ļ������С��ʱ��
		DecimalFormat df = new DecimalFormat("#0.00"); // ��ʽ������λС��
		int totalSize = stumItem.getUploadTaskJob().getMUploadTask()
				.getTotalsize();// �ļ���С���ֽڣ�
		if (totalSize == 0) {
			totalSize = prepareUploadData(stumItem.getUploadTaskJob()
					.getMUploadTask());
		}
		double size = totalSize * 1.00;
		if (size < 1024)
			textView_size_StandTMAItem.setText(totalSize + "B");
		else if (size < 1024 * 1024) {
			size /= 1024;
			textView_size_StandTMAItem.setText(df.format(size) + "KB");
		} else {
			size /= 1024 * 1024;
			textView_size_StandTMAItem.setText(df.format(size) + "MB");
		}
		int remainingSize = totalSize
				- stumItem.getUploadTaskJob().getMUploadTask()
						.getUploadedsize();
		if (stumItem.getUploadTaskJob().getMUploadTask().getStatus()
				.equals(UploadTaskStatus.Sending)
				&& remainingSize >= 0) {
			textView_timeTitle_StandTMAItem.setVisibility(View.VISIBLE);
			textView_time_StandTMAItem.setVisibility(View.VISIBLE);
			long timeForEachBytes = stumItem.getUploadTaskJob()
					.getUploadAsyncTask().getEachBytesTransTime();
			long remainingTime = 0;
			if (timeForEachBytes != -1) {
				remainingTime = (remainingSize
						/ stumItem.getUploadTaskJob().getMUploadTask()
								.getBlocksize() * timeForEachBytes) / 1000;
				if (remainingTime < 60)
					textView_time_StandTMAItem.setText(String
							.valueOf(remainingTime)
							+ mContext.getString(R.string.second));
				else if ((remainingTime < 60 * 60))
					textView_time_StandTMAItem.setText(String
							.valueOf(remainingTime / 60)
							+ mContext.getString(R.string.minute));
				else
					textView_time_StandTMAItem.setText(String
							.valueOf(remainingTime / 3600)
							+ mContext.getString(R.string.hour));
			} else {
				textView_timeTitle_StandTMAItem.setVisibility(View.GONE);
				textView_time_StandTMAItem.setVisibility(View.GONE);
			}

		} else {
			textView_timeTitle_StandTMAItem.setVisibility(View.GONE);
			textView_time_StandTMAItem.setVisibility(View.GONE);
		}
		// �����ϴ�״̬�����
		progressBarSendProgress_StandTMAItem.setMax(100);
		progressBarSendProgress_StandTMAItem.setProgress(stumItem
				.getUploadTaskJob().getMUploadTask().getProgress());

		return convertView;
	}

	/**
	 * ��ȡ�ϴ��ĸ���ܴ�С
	 * 
	 * @param uploadTask
	 * @return
	 */
	private int prepareUploadData(UploadTask uploadTask) {
		List<String> filePaths = new ArrayList<String>();
		filePaths.add(0, uploadTask.getFileurl()); // ��ȡ����ļ�����_���XML_����
		filePaths.add(1, uploadTask.getXmlurl()); // ��ȡ����ļ�����

		int totalSize = 0;
		for (String path : filePaths) {
			if (path != null) {
				totalSize += (int) new File(path).length();
			}
		}
		uploadTask.setTotalsize(totalSize); // �������ظ���ܴ�С

		return totalSize;
	}

	/**
	 * ����һ���������������첽����ͼƬ�Ļص�
	 */
	SyncImageLoader.OnImageLoadListener imageLoadListener = new SyncImageLoader.OnImageLoadListener() {

		public void onImageLoad(int position, Bitmap bitmap, ImageView imageView) {
			if (getItem(position) != null) {
				Manuscripts mu = getItem(position).getManuscripts();

				if (mu.getPreViewImage() == null && bitmap != null) {
					mu.setPreViewImage(bitmap);

					imageView.setImageBitmap(mu.getPreViewImage());
				}
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

		}
	};

	/**
	 * ɾ��ѡ����
	 */
	private void removeSelectedItems(List<StandToUploadManuscripts> tempList) {
		this.standToUploadManuscriptsList.removeAll(tempList);
		this.notifyDataSetChanged();
	}

	/**
	 * �����б���ָ��λ����
	 * 
	 * @param position
	 */
	public void updateItem(int position) {
		ManuscriptsService service = new ManuscriptsService(mContext);

		Manuscripts ma = standToUploadManuscriptsList.get(position)
				.getManuscripts();

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
		StandToUploadManuscripts stumItem = this.standToUploadManuscriptsList
				.get(position);
		stumItem.getManuscripts()
				.setCheck(!stumItem.getManuscripts().isCheck());
		this.notifyDataSetChanged();
	}

	/**
	 * ѡ��/ȡ�� ������
	 * 
	 * @param check
	 */
	public void checkAll(boolean check) {
		for (StandToUploadManuscripts stumItem : this.standToUploadManuscriptsList) {
			stumItem.getManuscripts().setCheck(check);
		}
		this.notifyDataSetChanged();
	}

	/**
	 * �����б���
	 * 
	 * @param ma
	 */
	public void addItemRange(List<StandToUploadManuscripts> list) {
		if (list != null)
			this.standToUploadManuscriptsList.addAll(list);
		this.notifyDataSetChanged();
	}

	/**
	 * ������ʼ
	 * 
	 * @param position
	 */
	public void startSelectedItems() {
		// ��ȡѡ�еĸ����
		int count = this.standToUploadManuscriptsList.size();
		for (int i = 0; i < count; i++) {
			StandToUploadManuscripts stumItem = this.standToUploadManuscriptsList
					.get(i);
			if (stumItem.getManuscripts().isCheck()) {
				start(stumItem);
			}
		}
	}

	/**
	 * �����Ƴ�
	 */
	public void removeSelectedItems() {
		// ��ȡѡ�еĸ����
		int count = this.standToUploadManuscriptsList.size();
		for (int i = 0; i < count; i++) {
			StandToUploadManuscripts stumItem = this.standToUploadManuscriptsList
					.get(i);
			if (stumItem.getManuscripts().isCheck()) {
				remove(stumItem);
			}
		}
		// if(count!=0){
		// this.notifyDataSetChanged();
		// }

	}

	/**
	 * ������ͣ
	 */
	public void pauseSelectedItems() {
		// ��ȡѡ�еĸ����
		int count = this.standToUploadManuscriptsList.size();
		for (int i = 0; i < count; i++) {
			StandToUploadManuscripts stumItem = this.standToUploadManuscriptsList
					.get(i);
			if (stumItem.getManuscripts().isCheck()) {
				pause(stumItem);
			}
		}
	}

	/**
	 * �������¿�ʼ
	 */
	public void restartSelectedItems() {
		// ��ȡѡ�еĸ����
		int count = this.standToUploadManuscriptsList.size();
		for (int i = 0; i < count; i++) {
			StandToUploadManuscripts stumItem = this.standToUploadManuscriptsList
					.get(i);
			if (stumItem.getManuscripts().isCheck()) {
				restart(stumItem);
			}
		}
		// if(count!=0)
		// {
		// this.notifyDataSetChanged();
		// }
	}

	/**
	 * ��ʼ�ϴ����(һ���ǵ��û����ò��Զ��ϴ���ʱ�������ť��������)
	 * 
	 * @param position
	 */
	public void startItem(int position) {
		StandToUploadManuscripts stumItem = this.standToUploadManuscriptsList
				.get(position);

		if (stumItem != null) {
			start(stumItem);
		}

		this.notifyDataSetChanged();
	}

	/**
	 * ������ͣ
	 * 
	 * @param position
	 */
	public void pauseItem(int position) {

		StandToUploadManuscripts stumItem = this.standToUploadManuscriptsList
				.get(position);

		if (stumItem != null) {
			pause(stumItem);
		}
		this.notifyDataSetChanged();
	}

	/**
	 * �����Ƴ�
	 * 
	 * @param position
	 */
	public void removeItem(int position) {
		StandToUploadManuscripts stumItem = this.standToUploadManuscriptsList
				.get(position);

		if (stumItem != null) {
			remove(stumItem);
		}

		this.notifyDataSetChanged();
	}

	/**
	 * �������¿�ʼ
	 * 
	 * @param position
	 */
	public void restartItem(int position) {
		StandToUploadManuscripts stumItem = this.standToUploadManuscriptsList
				.get(position);

		if (stumItem != null) {
			restart(stumItem);
		}

		this.notifyDataSetChanged();
	}

	public void setEditState(boolean editState) {
		this.editState = editState;
		this.notifyDataSetChanged();
	}

	public boolean isEditState() {
		return editState;
	}

	public List<StandToUploadManuscripts> getManuscripts() {
		return this.standToUploadManuscriptsList;
	}

	public void setManuscripts(List<StandToUploadManuscripts> tempList) {
		if (tempList == null)
			this.standToUploadManuscriptsList = new ArrayList<StandToUploadManuscripts>();
		else
			this.standToUploadManuscriptsList = tempList;
		this.notifyDataSetChanged();
	}

	/**
	 * ���¿�ʼ����ϴ�
	 * 
	 * @param standToUploadManuscripts
	 */
	private void restart(StandToUploadManuscripts standToUploadManuscripts) {

		try {
			if (standToUploadManuscripts.getUploadTaskJob()
					.getUploadAsyncTask() != null) {
				standToUploadManuscripts.getUploadTaskJob()
						.getUploadAsyncTask().pause(); // һ��Ҫ��Pause����cancel
				standToUploadManuscripts.getUploadTaskJob()
						.getUploadAsyncTask().cancel(true); // ֪ͨ�ļ��ϴ��������ȡ�������
			}

			// standToUploadManuscripts.getUploadTaskJob().getUploadAsyncTask().restart();
		} catch (Exception e) {
			Logger.e(e);
		}
	}

	/**
	 * ȡ�����ϴ��������ڱ����б�
	 * 
	 * @param standToUploadManuscripts
	 */
	private void remove(StandToUploadManuscripts standToUploadManuscripts) {

		try {
			if (standToUploadManuscripts.getUploadTaskJob().getUploadAsyncTask() != null) {
				standToUploadManuscripts.getUploadTaskJob().getUploadAsyncTask().pause(); // һ��Ҫ��Pause����cancel
				standToUploadManuscripts.getUploadTaskJob().getUploadAsyncTask().cancel(true);// ֪ͨ�ļ��ϴ��������ȡ�������
			}

			IngleApplication.getInstance()
					.getQueuedUploads()
					.remove(standToUploadManuscripts.getUploadTaskJob());

			manuscriptsService.Editing(standToUploadManuscripts
					.getManuscripts().getM_id()); // ������ݿ��иø��״̬Ϊ�ڱ�
			UploadTask uploadTask = standToUploadManuscripts.getUploadTaskJob()
					.getMUploadTask();

			mUploadTaskService.delete(uploadTask.getId()); // ͬ��������ݿ��������Ϣ

		} catch (Exception e) {
			Logger.e(e);
		}
	}

	/**
	 * ��ͣ����ϴ�(�������û�����������������)
	 * 
	 * @param standToUploadManuscripts
	 */
	private void pause(StandToUploadManuscripts standToUploadManuscripts) {
		try {
			if (standToUploadManuscripts.getUploadTaskJob().getUploadAsyncTask() != null) {
				standToUploadManuscripts.getUploadTaskJob().getUploadAsyncTask().pause(); // һ��Ҫ��Pause����cancel
				standToUploadManuscripts.getUploadTaskJob().getUploadAsyncTask().cancel(true); // ֪ͨ�ļ��ϴ��������ȡ�������
			}
		} catch (Exception e) {
			Logger.e(e);
		}
	}

	/**
	 * ��ʼ����ϴ�(����߳���Sleep�ľ�Continued)
	 * 
	 * @param standToUploadManuscripts
	 */
	private void start(StandToUploadManuscripts standToUploadManuscripts) {
		try {
			standToUploadManuscripts.getUploadTaskJob().getUploadAsyncTask().continued(); // ֪ͨ�ļ��ϴ�������лָ���������ϴ�
		} catch (Exception e) {
			Logger.e(e);
		}
	}
}
