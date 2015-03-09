package com.cuc.miti.phone.xmc.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.cuc.miti.phone.xmc.logic.AccessoriesService;
import com.cuc.miti.phone.xmc.logic.ManuscriptTemplateService;
import com.cuc.miti.phone.xmc.logic.ManuscriptsService;
import com.cuc.miti.phone.xmc.logic.MessageService;
import com.cuc.miti.phone.xmc.domain.Enums.AccessoryType;
import com.cuc.miti.phone.xmc.utils.StandardizationDataHelper;
import com.cuc.miti.phone.xmc.R;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

/**
 * �����е���ݿ�(res/raw/)���뵽/data/data/(package name)/ Ŀ¼��
 * 
 * @author SongQing
 * 
 */
public class DBManager {
	private final int BUFFER_SIZE = 40000;

	public static final String DB_NAME = "mc.db"; // ��ݿ��ļ���
	public static final String PACKGE_NAME = "com.cuc.miti.phone.xmc";
	public static final String DB_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKGE_NAME + "/" + "databases"; // ���ֻ�������ݿ��λ��
//	 public static final String DB_PATH = "/mnt/sdcard/" + PACKGE_NAME + "/"
//	 + "databases"; // ���ֻ�������ݿ��λ��

	public static final String FILE_NAME = "topiclist.cnml-MediaType-1.xml"; // �ļ���
	public static final String FILE_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKGE_NAME + "/" + "files"; // ���ֻ������ļ���λ��

	public static final String FILE_NAME1 = "topiclist.cnml-XH_NewsCategory-7.xml"; // �ļ���
	public static final String FILE_PATH1 = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKGE_NAME + "/" + "files"; // ���ֻ������ļ���λ��

	public static final String FILE_NAME2 = "topiclist.cnml-XH_InternalInternational-4.xml"; // �ļ���
	public static final String FILE_PATH2 = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKGE_NAME + "/" + "files"; // ���ֻ������ļ���λ��

	public static final String FILE_NAME3 = "topiclist.cnml-Department-85.xml"; // �ļ���
	public static final String FILE_PATH3 = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKGE_NAME + "/" + "files"; // ���ֻ������ļ���λ��

	public static final String FILE_NAME4 = "topiclist.cnml-Language-7.xml"; // �ļ���
	public static final String FILE_PATH4 = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKGE_NAME + "/" + "files"; // ���ֻ������ļ���λ��

	public static final String FILE_NAME5 = "topiclist.cnml-WorldLocationCategory-1.xml"; // �ļ���
	public static final String FILE_PATH5 = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKGE_NAME + "/" + "files"; // ���ֻ������ļ���λ��

	public static final String FILE_NAME6 = "topiclist.cnml-XH_GeographyCategory-5.xml"; // �ļ���
	public static final String FILE_PATH6 = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKGE_NAME + "/" + "files"; // ���ֻ������ļ���λ��

	public static final String FILE_NAME7 = "topiclist.cnml-Priority-1.xml"; // �ļ���
	public static final String FILE_PATH7 = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKGE_NAME + "/" + "files"; // ���ֻ������ļ���λ��

	private Context context;

	public DBManager(Context context) {
		this.context = context;
	}

	/**
	 * ������ݿ�(�ֻ���δ��װ�ɼ�ϵͳ�������)
	 * 
	 * @param dbfile
	 * @return
	 * @throws Exception
	 */
	public void createDatabase() throws Exception {
		try {
			// �ж���ݿ��ļ��Ƿ���ڣ�����������ִ�е��룬����ֱ�Ӵ���ݿ�
			// �����ж���ݿ���ļ�Ŀ¼�Ƿ���ڣ��������򴴽�
			File fileDirectory = new File(DB_PATH);
			if (!fileDirectory.exists()) { // ������
				fileDirectory.mkdirs(); // Ŀ¼����
			}

			File file = new File(DB_PATH + "/mc.db");
			if (!file.exists()) {
				InputStream is = this.context.getResources().openRawResource(
						R.raw.mc); // �������ݿ�
				FileOutputStream fos = new FileOutputStream(DB_PATH + "/"
						+ DB_NAME);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
		} catch (FileNotFoundException ex) {
			throw ex;
		} catch (IOException ex) {
			throw ex;
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * ɾ��������ݿ�
	 */
	public void deleteDatabase() {
		// ��ɾ����ݿ��ļ�
		File file = new File(DB_PATH + "/" + DB_NAME);
		if (file.exists()) {
			file.delete();
		}

		file = new File(DB_PATH + "/" + "mc.db-journal"); // ��ݿ�������ʱ�ļ�������������жϣ����ļ��п��ܱ�����������Ҫɾ��
		if (file.exists()) {
			file.delete();
		}
		// ��ɾ����ݿ��ļ����ڵ�Ŀ¼
		File fileDirectory = new File(DB_PATH);
		if (fileDirectory.exists()) {
			fileDirectory.delete();
		}
	}

	/**
	 * ɾ��������ݿ�
	 */
	public void deleteFile() {
		// ��ɾ����ݿ��ļ�
		File file = new File(FILE_PATH + "/" + FILE_NAME);
		if (file.exists()) {
			file.delete();
		}
		// ��ɾ����ݿ��ļ����ڵ�Ŀ¼
		File fileDirectory = new File(FILE_PATH);
		if (fileDirectory.exists()) {
			fileDirectory.delete();
		}

	}

	/**
	 * ������
	 */
	public void cleanData() {
		// ManuscriptTemplateService mtService=new
		// ManuscriptTemplateService(context);
		ManuscriptsService mService = new ManuscriptsService(context);
		AccessoriesService accService = new AccessoriesService(context);

		// mtService.deleteAllManuscriptTemplate();
		mService.deleteAllManuscripts();
		accService.deleteAllAccessories();

		// �������·��
		String temp_path = StandardizationDataHelper
				.getAccessoryFileTempStorePath();
		File temp_file = new File(temp_path);
		deleteAll(temp_file);

		// ������·��
		String acc_path = StandardizationDataHelper
				.getAccessoryFileUploadStorePath(AccessoryType.Cache);
		File acc_file = new File(acc_path);
		deleteAll(acc_file);
	}

	/**
	 * ɾ��һ��Ŀ¼����������ļ�
	 */
	public void deleteAll(File file) {

		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].exists()) {
				files[i].delete();
			}
		}
	}
}
