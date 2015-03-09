/*
Copyright 2009 David Revell

This file is part of SwiFTP.

SwiFTP is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

SwiFTP is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with SwiFTP.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.cuc.miti.phone.xmc.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.cuc.miti.phone.xmc.R;
import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Accessories;
import com.cuc.miti.phone.xmc.domain.Defaults;
import com.cuc.miti.phone.xmc.domain.Globals;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.domain.ManuscriptTemplate;
import com.cuc.miti.phone.xmc.domain.Manuscripts;
import com.cuc.miti.phone.xmc.domain.SendToAddress;
import com.cuc.miti.phone.xmc.domain.User;
import com.cuc.miti.phone.xmc.domain.Enums.AccessoryType;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceKeys;
import com.cuc.miti.phone.xmc.domain.Enums.TemplateType;
import com.cuc.miti.phone.xmc.http.RemoteCaller;
import com.cuc.miti.phone.xmc.logic.AccessoriesService;
import com.cuc.miti.phone.xmc.logic.ManuscriptTemplateService;
import com.cuc.miti.phone.xmc.logic.ManuscriptsService;
import com.cuc.miti.phone.xmc.logic.SendToAddressService;
import com.cuc.miti.phone.xmc.ui.MainActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

abstract public class FTPUtil {
	static String TAG = FTPUtil.class.getSimpleName();

	private static final int MSG_Add_One = 1;
	private static List<String> lstFile = new ArrayList<String>(); // ��� List
	// private String PathG;
	private static Context mContext = Globals.getContext();
	private MessageHandler mHandler = new MessageHandler();
	private static ManuscriptsService mservice = new ManuscriptsService(
			mContext);
	public static Manuscripts manuscripts = null; // ��ǰ�༭�еĸ������
	private static List<Accessories> accessories = null; // ��ǰ��������б�
	private static ManuscriptTemplateService mtService = new ManuscriptTemplateService(
			mContext);
	private static ManuscriptTemplate mManuTemplate = null;
	static AccessoriesService accService = new AccessoriesService(mContext);
	static SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(
			mContext);
	static String fileName = "ftpusername";
	static InputStream is = null;
	static List<KeyValueData> objs = new ArrayList<KeyValueData>();
	static String instantAccAuthor = "";
	private static String sessionId = IngleApplication
			.getSessionId();
	private static String loginname = IngleApplication
			.getInstance().getCurrentUser();
	private static SendToAddressService sendToAddressService = new SendToAddressService(
			mContext);

	// @TODO: this method should go into the Application object
	public static boolean isFreeVersion() {
		try {
			return Globals.getContext().getPackageName().contains("free");
		} catch (Exception swallow) {
		}
		return false;
	}

	public static String getAndroidId() {
		ContentResolver cr = Globals.getContext().getContentResolver();
		return Settings.Secure.getString(cr, Settings.Secure.ANDROID_ID);
	}

	/**
	 * Get the SwiFTP version from the manifest.
	 * 
	 * @return The version as a String.
	 */
	public static String getVersion() {
		String packageName = Globals.getContext().getPackageName();
		try {
			return Globals.getContext().getPackageManager()
					.getPackageInfo(packageName, 0).versionName;
		} catch (NameNotFoundException e) {
			Log.e(TAG, "NameNotFoundException looking up SwiFTP version");
			return null;
		}
	}

	public static byte byteOfInt(int value, int which) {
		int shift = which * 8;
		return (byte) (value >> shift);
	}

	public static String ipToString(int addr, String sep) {
		// myLog.l(Log.DEBUG, "IP as int: " + addr);
		if (addr > 0) {
			StringBuffer buf = new StringBuffer();
			buf.append(byteOfInt(addr, 0)).append(sep)
					.append(byteOfInt(addr, 1)).append(sep)
					.append(byteOfInt(addr, 2)).append(sep)
					.append(byteOfInt(addr, 3));
			Log.d(TAG, "ipToString returning: " + buf.toString());
			return buf.toString();
		} else {
			return null;
		}
	}

	public static InetAddress intToInet(int value) {
		byte[] bytes = new byte[4];
		for (int i = 0; i < 4; i++) {
			bytes[i] = byteOfInt(value, i);
		}
		try {
			return InetAddress.getByAddress(bytes);
		} catch (UnknownHostException e) {
			// This only happens if the byte array has a bad length
			return null;
		}
	}

	public static String ipToString(int addr) {
		if (addr == 0) {
			// This can only occur due to an error, we shouldn't blindly
			// convert 0 to string.
			Log.e(TAG, "ipToString won't convert value 0");
			return null;
		}
		return ipToString(addr, ".");
	}

	// This exists to avoid cluttering up other code with
	// UnsupportedEncodingExceptions.
	public static byte[] jsonToByteArray(JSONObject json) throws JSONException {
		try {
			return json.toString().getBytes(Defaults.STRING_ENCODING);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	// This exists to avoid cluttering up other code with
	// UnsupportedEncodingExceptions.
	public static JSONObject byteArrayToJson(byte[] bytes) throws JSONException {
		try {
			return new JSONObject(new String(bytes, Defaults.STRING_ENCODING));
		} catch (UnsupportedEncodingException e) {
			// This will never happen because we use valid encodings
			return null;
		}
	}

	public static void newFileNotify(String path) {
		if (Defaults.do_mediascanner_notify) {
			Log.d(TAG, "Notifying others about new file: " + path);
			new MediaScannerNotifier(Globals.getContext(), path);

		}
	}

	public static void deletedFileNotify(String path) {
		// This might not work, I couldn't find an API call for this.
		if (Defaults.do_mediascanner_notify) {
			Log.d(TAG, "Notifying others about deleted file: " + path);
			new MediaScannerNotifier(Globals.getContext(), path);

		}
	}

	// A class to help notify the Music Player and other media services when
	// a file has been uploaded. Thanks to Dave Sparks in his post to the
	// Android Developers mailing list on 14 Feb 2009.
	private static class MediaScannerNotifier implements
			MediaScannerConnectionClient {
		private final MediaScannerConnection connection;
		private final String path;

		public MediaScannerNotifier(Context context, String path) {
			this.path = path;
			connection = new MediaScannerConnection(context, this);
			connection.connect();
		}

		public void onMediaScannerConnected() {
			connection.scanFile(path, null); // null: we don't know MIME type

		}

		public void onScanCompleted(String path, Uri uri) {
			connection.disconnect();
		}
	}

	public static String[] concatStrArrays(String[] a1, String[] a2) {
		String[] retArr = new String[a1.length + a2.length];
		System.arraycopy(a1, 0, retArr, 0, a1.length);
		System.arraycopy(a2, 0, retArr, a1.length, a2.length);
		return retArr;
	}

	public static void sleepIgnoreInterupt(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}

	/**
	 * ��ʼ���ڴ��еĸ������
	 */
	private static void iniManuscript() {
		manuscripts = new Manuscripts(); // �������
		accessories = new ArrayList<Accessories>(); // ������������б�

		// ������ĸ��guid
		manuscripts.setM_id(UUID.randomUUID().toString());

		// ��ȡ��ǰ�û���Ϣ
		User user = IngleApplication.getInstance().currentUserInfo;
		manuscripts.setGroupcode(user.getUserattribute().getGroupCode());
		manuscripts.setGroupnameC(user.getUserattribute().getGroupNameC());
		manuscripts.setGroupnameE(user.getUserattribute().getGroupNameE());
		manuscripts.setUsernameC(user.getUserattribute().getUserNameC());
		manuscripts.setUsernameE(user.getUserattribute().getUserNameE());
		manuscripts.setLoginname(user.getUsername());

		// ���ø�ǩģ��
		mManuTemplate = mtService.getManuscriptTemplateSystem(
				IngleApplication.getInstance()
						.getCurrentUser(), TemplateType.INSTANT.toString());

		// TODO ����ط�ΪʲôҪ��content��send���Ը�ֵ��������ڣ�
		// this.setContent_SendControlValue();

		manuscripts.setManuscriptTemplate(mManuTemplate);

		// manuscripts.setAuthor(mManuTemplate.getAuthor());

		manuscripts.getManuscriptTemplate().setComefromDept(
				user.getUserattribute().getGroupNameC());
		manuscripts.getManuscriptTemplate().setComefromDeptID(
				user.getUserattribute().getGroupCode());

		// �ڷ���ǰ��Ҫ��У�����û��Լ����õĵ�ַ�Ƿ񻹴�����ϵͳ�������Ȩ�޷�Χ�ڣ��統�û����ü��ļ�����ַʱ�С�address1�����������ʹ�ü��ļ�������ʱ���˵�ַ�Ѳ����ڣ�
		String sendToAddress = manuscripts.getManuscriptTemplate().getAddress();
		String[] address = null;
		List<String> addressList = null;
		List<String> addressAll = RemoteCaller.addressAll;
		StringBuilder strAddress = new StringBuilder();

		if (StringUtils.isNotBlank(sendToAddress)) {

			if ("".equals(sendToAddress) == false) {
				addressList = new ArrayList<String>();
				address = sendToAddress.split(",");
				for (int i = 0; i < address.length; i++) {
					addressList.add(address[i]);
				}
			}
		}

		if (addressList.size() > 0 && addressList != null) {
			manuscripts.getManuscriptTemplate().setAddress("");// ����������Ϊ�գ��ٽ������ݵ����

			SendToAddress sta = null;

			for (int j = 0; j < addressList.size(); j++) {
				for (int k = 0; k < addressAll.size(); k++) {
					sta = sendToAddressService.getSendToAddressByCode("zh-CHS",
							addressAll.get(k));
					if (addressList.get(j).equals(sta.getName())) {
						strAddress.append(addressList.get(j)).append(",");
					}
				}
			}

			if (strAddress != null && (!("").equals(strAddress))) {
				manuscripts.getManuscriptTemplate().setAddress(
						strAddress.substring(0, strAddress.length() - 1)
								.toString());
			}

		} else {
			manuscripts.getManuscriptTemplate().setAddress("");
		}

		// ************************************************end
		if ("".equals(instantAccAuthor)) {
			// instantAccAuthor=user.getUsername();//ԭ�����ô˷����ģ���������Ҫʵ���ܹ��޸����ߵĹ���
			instantAccAuthor = manuscripts.getManuscriptTemplate().getAuthor();
		}
		// ��ǩ�͸����author���ĳɼ��ļ�����author
		manuscripts.getManuscriptTemplate().setAuthor(instantAccAuthor);
		manuscripts.setAuthor(instantAccAuthor);
		manuscripts.setTitle("���ļ���");
		manuscripts.setContents("���ļ���");
	}

//	public static void notifyMediaScanToFindThePic(String path) {
//		// Save the name and description of a video in a ContentValues map.
//		ContentValues values = new ContentValues(2);
//		values.put(MediaStore.Video.Media.MIME_TYPE, "image/jpeg");
//		values.put(MediaStore.Video.Media.DATA, path);
//
//		ContentResolver contentResolver = mContext.getContentResolver();
//		Uri base = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//		// Add a new record (identified by uri)
//		Uri newUri = contentResolver.insert(base, values);
//		mContext.sendBroadcast(new Intent(
//				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
////		Uri uri = Uri.parse(path);
////		Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
////				uri);
////		mContext.sendBroadcast(localIntent);
//	}

	/**
	 * ��⵽FTP�ļ��������µ��ļ�������ɸ��
	 * 
	 * @param path
	 */
	public static void createManuscript(String picPath, String filePath) {

		String authorOfAcc = "";
		char firstChar = picPath.charAt(1);
		if ('_' == firstChar) {
			authorOfAcc = picPath.substring(1, 4).toLowerCase(); // �ϴ�ͼƬ����Ƶ������
		} else {
			authorOfAcc = picPath.substring(0, 3).toLowerCase(); // �ϴ�ͼƬ����Ƶ������
		}

		getAuthorOfAccList(); // ��ȡ�����Ѵ��ڵ������б�

		if (objs != null && objs.size() > 0) {
			for (int i = 0; i < objs.size(); i++) {
				// ����б��д��ڴ��ϴ������û�����Ѹ�ǩ�͸���е��û����Ϊ��author����
				if (authorOfAcc.equals(objs.get(i).getKey())) {
					instantAccAuthor = objs.get(i).getValue();
				}
			}
		}

		iniManuscript();
		mservice.addManuscripts(manuscripts);

		// String filePath=""; //ͼƬδ��ѹ��ǰ������·��
		// String fileURLTD=""; //ͼƬ��temp�ļ����µ�����·��
		// String fileURLTDC=""; //ͼƬ��temp�ļ����µı�ѹ���������·��
		// filePath=PathG+path;

		String fileURLFD = ""; // ͼƬ��ftp�ļ����µ�����·��
		String fileURLFDC = ""; // ͼƬ��ftp�ļ����µı�ѹ���������·��

		String end = picPath.substring(picPath.lastIndexOf(".") + 1,
				picPath.length()).toLowerCase();

		if (end.equals("jpg") || end.equals("png") || end.equals("jpeg")
				|| end.equals("bmp") || end.equals("tiff") || end.equals("ico")
				|| end.equals("gif") || end.equals("wav") || end.equals("mp3")
				|| end.equals("wma")) {
			try {
				// fileURLTD =
				// MediaHelper.copy2TempStoreWithOriginalName(filePath);
				// //��ͼƬ�ȿ�����Temp�ļ����£�Ȼ�����ѹ������
				// fileURLTDC=fileURLTD.substring(0,fileURLTD.lastIndexOf("."))+"_resized."+end;

				fileURLFD = filePath;
				fileURLFDC = fileURLFD.substring(0, fileURLFD.lastIndexOf("."))
						+ "_resized." + end;

				AccessoryType accType = null;
				Accessories acc = null;
				if (!end.equals("wav") && !end.equals("mp3")
						&& !end.equals("wma")) {// ֻ����ͼƬʱ�Ž���ѹ������Ƶ����Ҫ����ѹ������
					fileURLFDC=Utils.CompressBitmap(fileURLFDC, fileURLFD);
					if (fileURLFDC.equals(""))
						return;
					// ���·����ý������������ɸ�������
					accType = MediaHelper.checkFileType(fileURLFDC);
					acc = populateAccessorie(fileURLFDC, accType);
				} else {
					if (fileURLFD.equals(""))
						return;
					// ���·����ý������������ɸ�������
					accType = MediaHelper.checkFileType(fileURLFD);
					acc = populateAccessorie(fileURLFD, accType);

				}
				// ���渽��
				saveAcc(acc);
				accessories.add(acc);
			} catch (Exception e) {
				e.printStackTrace();
			}

			KeyValueData resultMessage = new KeyValueData("", "");
			boolean result = SendManuscriptsHelper.validateForReleManuscript(
					manuscripts, resultMessage, mContext);

			// ȡ�á����ļ�����ȡֵ����Ϊ�������̽����ļ����Ͳ��� FTP ����ģʽ
			String selectedValue = sharedPreferencesHelper
					.GetUserPreferenceValue(PreferenceKeys.instantUpload
							.toString());
			if (result == true) {

				if (!selectedValue.equals("")) {
					if ("true".equals(selectedValue)) {

						if (sent()) {
							showMessage("�����֤��ϣ��ѽ��뷢�Ͷ��У�");
						} else {
							showMessage("����ʧ�ܣ�δ֪����");
						}
					} else {
						// �Ѹ�����浽�ڱ����б���
						save();
						showMessage("δ�������ļ������ܣ�����ѱ��浽�ڱ��б���");
					}
				}
			}
		}
	}

	// ͼƬѹ������
//	private static void CompressBitmap(String fileURLTDC, String fileURLTD) {
//		OutputStream os;
//		try {
//
//			BitmapFactory.Options options = new BitmapFactory.Options();
//			// options.inJustDecodeBounds = true;
//			// ��ȡ���ͼƬ�Ŀ�͸ߣ�ע��˴���bitmapΪnull
//			// Bitmap bitmap = BitmapFactory.decodeFile(fileURLTD, options);
//
//			String compressRatio = sharedPreferencesHelper
//					.GetUserPreferenceValue(PreferenceKeys.User_UploadPicCompressRatio
//							.toString());
//
//			int ratio = 100 / Integer.parseInt(compressRatio);
//
//			options.inSampleSize = ratio;
//			os = new FileOutputStream(fileURLTDC);
//
//			// int outputwidth = options.outWidth/ratio;
//			// int outputheight = options.outHeight/ratio;
//
//			options.inJustDecodeBounds = false;
//			Bitmap resizedBitmap = BitmapFactory.decodeFile(fileURLTD, options);
//			// resizedBitmap = ThumbnailUtils.extractThumbnail(resizedBitmap,
//			// outputwidth,outputheight,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
//			resizedBitmap.compress(CompressFormat.JPEG, 100, os);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}

	private void sendMessage(int what, Object obj) {
		Message msg = mHandler.obtainMessage();
		msg.what = what;
		msg.obj = obj;
		mHandler.sendMessageDelayed(msg, 500);
	}

	private class MessageHandler extends Handler {
		// This method is used to handle received messages
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case MSG_Add_One:
				// Toast.makeText(FragmentView.getContext(),"��Ϣ������ϣ�",Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(mContext, MainActivity.class);
				mContext.startActivity(intent);

				break;
			default:
				break;
			}
		}
	}

	/**
	 * ���͸��
	 */
	protected static boolean sent() {
		try {
			return mservice.sendNormalManuscripts(manuscripts, accessories);
		} catch (Exception e) {
			Logger.e(e);
			return false;
		}

	}

	/**
	 * ������������ҪУ��
	 */
	protected static void save() {
		mservice.updateManuscripts(manuscripts);
	}

	/**
	 * ��ʾ�����Ϣ
	 * 
	 * @param message
	 */
	protected static void showMessage(String message) {
		ToastHelper.showToast(message, Toast.LENGTH_SHORT);
	}

	/**
	 * ���Accessories����
	 * 
	 * @param accPath
	 *            ����·��
	 * @param accType
	 *            ��������
	 * @return ��������
	 */
	private static Accessories populateAccessorie(String accPath,
			AccessoryType accType) {

		Accessories accessorie = new Accessories();
		accessorie.setA_id(UUID.randomUUID().toString());
		accessorie.setM_id(manuscripts.getM_id());
		accessorie.setType(accType.toString());

		try {
			accessorie.setOriginalName(MediaHelper.getFileName(accPath));
			accessorie.setSize(MediaHelper.getFileSize(accPath));
		} catch (IOException ex) {
			accessorie.setOriginalName("");
			accessorie.setSize("");
			Logger.e(ex);

			showMessage(ToastHelper
					.getStringFromResources(R.string.filePathError));
		}

		// ͼƬ·��
		accessorie.setUrl(accPath);

		return accessorie;
	}

	/**
	 * ���渽������
	 */
	private static void saveAcc(Accessories acc) {
		// ���ø�������ı���
		acc.setTitle(ToastHelper
				.getStringFromResources(R.string.title_UploadPicFM));
		// ���ø������������
		acc.setDesc(ToastHelper
				.getStringFromResources(R.string.title_UploadPicFM));
		// ���浽��ݿ�
		accService.addAccessories(acc);
	}

	public static List<String> GetFiles(String Path) // ����Ŀ¼�ļ��� �µ��ļ�
	{
		File[] files = new File(Path).listFiles();
		if (files != null && files.length > 0) {

			for (int i = 0; i < files.length; i++) {
				File f = files[i];

				String end = f
						.getPath()
						.substring(f.getPath().lastIndexOf(".") + 1,
								f.getPath().length()).toLowerCase();

				f.lastModified();

				if (end.equals("jpg") || end.equals("png")
						|| end.equals("jpeg") || end.equals("bmp")
						|| end.equals("tiff") || end.equals("ico")
						|| end.equals("gif")) {
					lstFile.add(f.getPath());
				}
			}
		}
		return lstFile;
	}

	public static void getAuthorOfAccList() {
		String path = StandardizationDataHelper
				.getAccessoryFileUploadStorePath(AccessoryType.Cache);
		File dataFile = null;
		dataFile = new File(path + "/" + fileName + ".xml");
		if (!dataFile.exists()) {
			return;
		}
		try {
			is = new FileInputStream(dataFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// List<KeyValueData> kvList = null;
		try {
			objs = XMLDataHandle.parserFtpUsername(is);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
