package com.cuc.miti.phone.xmc.ui;

import java.io.File;
import java.net.URI;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.utils.TimeFormatHelper;
import com.cuc.miti.phone.xmc.R;

public class FileManagerActivity extends BaseActivity {

	private String rootpath = "/";// StandardizationDataHelper.getSDCardStorePath();
	private String currentPath = "/";

	private TextView textViewPath_FileM;

	private ListView listViewFileList_FileM = null;

	private FilelistAdapter adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.file_manager);

		listViewFileList_FileM = (ListView) findViewById(R.id.listViewFileList_FileM);
		textViewPath_FileM = (TextView) findViewById(R.id.textViewPath_FileM);

		// �趨����¼�
		listViewFileList_FileM
				.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {

						File file = adapter.getItem(position);

						if (file.canRead()) {

							if (file.isDirectory()) {

								currentPath = file.getPath();
								textViewPath_FileM.setText(file.getName());

								File[] files = file.listFiles();

								adapter.setFiles(files);
							} else {
								
								setResult(RESULT_OK,
										(new Intent()).setData(Uri.fromFile(file)));
								finish();
							}
						}
					}
				});

		getFileDir(currentPath);
		
		IngleApplication.getInstance().addActivity(this);
	}

	public void getFileDir(String filePath) {

		currentPath = filePath;

		textViewPath_FileM.setText(currentPath);

		File f = new File(filePath);

		File[] files = f.listFiles();

		adapter = new FilelistAdapter(files, this);

		listViewFileList_FileM.setAdapter(adapter);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// KeyEvent.KEYCODE_BACK��?�ز���.
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (rootpath.equals(currentPath)) {
				setResult(RESULT_CANCELED);
				finish();
			}
			else{
				File file = new File(currentPath);
				File parent = file.getParentFile();
				currentPath = parent.getPath();
				textViewPath_FileM.setText(parent.getName());
				File[] files = parent.listFiles();
				adapter.setFiles(files);
			}
		}
		return true;
	}

	class FilelistAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		private List<File> files;
		private Context mContext;

		public void setFiles(File[] files) {
			translate(files);

			adapter.notifyDataSetChanged();
		}

		private void translate(File[] files) {
			if (files == null)
				files = new File[] {};
			else {
				this.files = new ArrayList<File>();

				List<File> folders = new ArrayList<File>();
				List<File> tempFiles = new ArrayList<File>();

				for (int i = 0; i < files.length; i++) {

					if (!files[i].canRead())
						continue;

					if (files[i].isDirectory())
						folders.add(files[i]);
					else {
						tempFiles.add(files[i]);
					}
				}

				this.files.addAll(folders);
				this.files.addAll(tempFiles);
			}
		}

		public FilelistAdapter(File[] files, Context context) {
			super();

			translate(files);

			this.mContext = context;

			inflater = LayoutInflater.from(context);
		}

		public int getCount() {
			if (null != files) {
				return files.size();
			} else {
				return 0;
			}
		}

		public File getItem(int position) {
			return files.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.listview_item_for_files, parent, false);
			}

			// ��ʼ����ͼ�еĿؼ�
			TextView textViewTitle_FileMLI = (TextView) convertView
					.findViewById(R.id.textViewTitle_FileMLI);
			TextView textViewCount_FileMLI = (TextView) convertView
					.findViewById(R.id.textViewCount_FileMLI);
			TextView textViewTime_FileMLI = (TextView) convertView
					.findViewById(R.id.textViewTime_FileMLI);
			ImageView imageViewfile_FileMLI = (ImageView) convertView
					.findViewById(R.id.imageViewfile_FileMLI);

			// ��ȡָ��λ�õĸ������
			File file = this.files.get(position);

			String fileDeails = "";

			// ͼ��
			if (!file.isDirectory()) {
				imageViewfile_FileMLI.setImageBitmap(BitmapFactory
						.decodeResource(this.mContext.getResources(),
								R.drawable.phone_filemanager_file));

				fileDeails = String.valueOf(file.length() / 1024).concat(" KB");
			} else {
				imageViewfile_FileMLI.setImageBitmap(BitmapFactory
						.decodeResource(this.mContext.getResources(),
								R.drawable.phone_filemanager_folder));

				File[] list = file.listFiles();
				if (list != null)
					fileDeails = String.valueOf(list.length).concat(" ��");
			}

			// �����ļ�����Ϣ
			String name = file.getName();
			if(name.length() > 14)
				name = name.substring(0, 7).concat("......").concat(name.substring(name.length() - 7));
			textViewTitle_FileMLI.setText(name);

			// ����Ŀ¼��������Ŀ
			textViewCount_FileMLI.setText(fileDeails);

			// �����޸�ʱ����Ϣ
			Date time = new Date(file.lastModified());
			textViewTime_FileMLI.setText(TimeFormatHelper.getShortFormatTime(
					time, "yyyy-MM-dd"));

			return convertView;
		}

	}
}
