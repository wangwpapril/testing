package com.cuc.miti.phone.xmc.utils;

import java.io.FileOutputStream;
import java.io.OutputStream;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Globals;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceKeys;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.provider.MediaStore;

public class Utils {
	private static Context mContext = IngleApplication.getInstance();
	static SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(
			mContext);

	public static void notifyMediaScanToFindThePic(String path) {

		// Uri uri = Uri.parse(path);
		// Intent localIntent = new
		// Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
		// uri);
		// mContext.sendBroadcast(localIntent);
		// Save the name and description of a video in a ContentValues map.
		ContentValues values = new ContentValues(2);
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		values.put(MediaStore.Images.Media.DATA, path);

		ContentResolver contentResolver = mContext.getContentResolver();
		Uri base = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		// Add a new record (identified by uri)
		Uri newUri = contentResolver.insert(base, values);
		mContext.sendBroadcast(new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
	}

	public static void notifyMediaScanToFindTheVideo(String path) {

		// Uri uri = Uri.parse(path);
		// Intent localIntent = new
		// Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
		// uri);
		// mContext.sendBroadcast(localIntent);
		// Save the name and description of a video in a ContentValues map.
		ContentValues values = new ContentValues(2);
		values.put(MediaStore.Video.Media.MIME_TYPE, "video/*");
		values.put(MediaStore.Video.Media.DATA, path);

		ContentResolver contentResolver = mContext.getContentResolver();
		Uri base = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
		// Add a new record (identified by uri)
		Uri newUri = contentResolver.insert(base, values);
		mContext.sendBroadcast(new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
	}

	public static void notifyMediaScanToFindTheAudio(String path) {

		// Uri uri = Uri.parse(path);
		// Intent localIntent = new
		// Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
		// uri);
		// mContext.sendBroadcast(localIntent);
		// Save the name and description of a video in a ContentValues map.
		ContentValues values = new ContentValues(2);
		values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/*");
		values.put(MediaStore.Audio.Media.DATA, path);

		ContentResolver contentResolver = mContext.getContentResolver();
		Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		// Add a new record (identified by uri)
		Uri newUri = contentResolver.insert(base, values);
		mContext.sendBroadcast(new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
	}

	// ͼƬѹ������
	public static String CompressBitmap(String fileURLTDC, String fileURLTD) {
			OutputStream os;
			try {

				BitmapFactory.Options options = new BitmapFactory.Options();
				// options.inJustDecodeBounds = true;
				// ��ȡ���ͼƬ�Ŀ�͸ߣ�ע��˴���bitmapΪnull
				// Bitmap bitmap = BitmapFactory.decodeFile(fileURLTD, options);

				String compressRatio = sharedPreferencesHelper
						.GetUserPreferenceValue(PreferenceKeys.User_UploadPicCompressRatio
								.toString());
				if(compressRatio==null||compressRatio=="")
					compressRatio="50";
				int ratio = 100 / Integer.parseInt(compressRatio);
				if(Integer.parseInt(compressRatio)==100)
					return fileURLTD;//��ѹ��
				options.inSampleSize = ratio;
				os = new FileOutputStream(fileURLTDC);

				// int outputwidth = options.outWidth/ratio;
				// int outputheight = options.outHeight/ratio;

				options.inJustDecodeBounds = false;
				Bitmap resizedBitmap = BitmapFactory.decodeFile(fileURLTD, options);
				// resizedBitmap = ThumbnailUtils.extractThumbnail(resizedBitmap,
				// outputwidth,outputheight,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
				resizedBitmap.compress(CompressFormat.JPEG, Integer.parseInt(compressRatio), os);
				return fileURLTDC;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return fileURLTD;

		}
}
