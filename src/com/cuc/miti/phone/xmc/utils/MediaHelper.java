package com.cuc.miti.phone.xmc.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ContentHandler;
import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.domain.Accessories;
import com.cuc.miti.phone.xmc.domain.Enums.AccessoryType;
import com.cuc.miti.phone.xmc.logic.AccessoriesService;
import com.cuc.miti.phone.xmc.ui.EditingManuscriptsActivity;
import com.cuc.miti.phone.xmc.R;
import com.cuc.miti.phone.xmc.R.string;
import com.cuc.miti.phone.xmc.utils.StandardizationDataHelper;

import android.R.integer;
import android.accounts.AccountsException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;

public class MediaHelper {

	private static Bitmap audioBitmap = null;

	/**
	 * ��ȡ�ļ�����
	 * 
	 * @param �ļ�·��
	 * @return
	 */
	public static AccessoryType checkFileType(String name) {

		AccessoryType accType = AccessoryType.Cache;

		String end = name.substring(name.lastIndexOf(".") + 1, name.length())
				.toLowerCase();

		if (end.equals("jpg") || end.equals("png") || end.equals("jpeg")
				|| end.equals("bmp") || end.equals("tiff")|| end.equals("ico")|| end.equals("gif")) {
			accType = AccessoryType.Picture;
		} else if (end.equals("3gp") || end.equals("mov") || end.equals("avi")
				|| end.equals("mpg") || end.equals("mpeg") || end.equals("mp4")) {
			accType = AccessoryType.Video;
		} else if (end.equals("amr") || end.equals("aac") || end.equals("mp3")
				|| end.equals("wav") || end.equals("aif") || end.equals("m4a")
				|| end.equals("mid")) {
			accType = AccessoryType.Voice;
		} else {
			accType = accType.Complex;
		}

		return accType;
	}

	/**
	 * ���ָ����ͼ��·���ʹ�С����ȡ����ͼ �˷���������ô��� 1.
	 * ʹ�ý�С���ڴ�ռ䣬��һ�λ�ȡ��bitmapʵ����Ϊnull��ֻ��Ϊ�˶�ȡ��Ⱥ͸߶ȣ�
	 * �ڶ��ζ�ȡ��bitmap�Ǹ�ݱ���ѹ�����ͼ�񣬵���ζ�ȡ��bitmap����Ҫ������ͼ�� 2.
	 * ����ͼ����ԭͼ������û�����죬����ʹ����2.2�汾���¹���ThumbnailUtils��ʹ �����������ɵ�ͼ�񲻻ᱻ���졣
	 * 
	 * @param imagePath
	 *            ͼ���·��
	 * @param width
	 *            ָ�����ͼ��Ŀ��
	 * @param height
	 *            ָ�����ͼ��ĸ߶�
	 * @return ��ɵ�ͼƬ
	 */
	public static Bitmap getImageThumbnail(String imagePath, int width,
			int height) {
		// Bitmap bitmap = null;
		// BitmapFactory.Options options = new BitmapFactory.Options();
		// options.inJustDecodeBounds = true;
		// options.inSampleSize = 4;
		// // ��ȡ���ͼƬ�Ŀ�͸ߣ�ע��˴���bitmapΪnull
		// bitmap = BitmapFactory.decodeFile(imagePath, options);
		//
		// options.inJustDecodeBounds = false; // ��Ϊ false
		// // �������ű�
		// int h = options.outHeight;
		// int w = options.outWidth;
		// int beWidth = w / width;
		// int beHeight = h / height;
		// int be = 1;
		// if (beWidth < beHeight) {
		// be = beWidth;
		// } else {
		// be = beHeight;
		// }
		// if (be <= 0) {
		// be = 1;
		// }
		// options.inSampleSize = be;
		// // ���¶���ͼƬ����ȡ���ź��bitmap��ע�����Ҫ��options.inJustDecodeBounds ��Ϊ false
		// bitmap = BitmapFactory.decodeFile(imagePath, options);
		// // ����ThumbnailUtils����������ͼ������Ҫָ��Ҫ�����ĸ�Bitmap����
		// bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
		// ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		// return bitmap;
		return getImageThumbnail(imagePath, width, height, true);
	}

	/**
	 * ���ָ����ͼ��·���ʹ�С����ȡ����ͼ �˷���������ô��� 1.
	 * ʹ�ý�С���ڴ�ռ䣬��һ�λ�ȡ��bitmapʵ����Ϊnull��ֻ��Ϊ�˶�ȡ��Ⱥ͸߶ȣ�
	 * �ڶ��ζ�ȡ��bitmap�Ǹ�ݱ���ѹ�����ͼ�񣬵���ζ�ȡ��bitmap����Ҫ������ͼ�� 2.
	 * ����ͼ����ԭͼ������û�����죬����ʹ����2.2�汾���¹���ThumbnailUtils��ʹ �����������ɵ�ͼ�񲻻ᱻ���졣
	 * 
	 * @param imagePath
	 *            ͼ���·��
	 * @param width
	 *            ָ�����ͼ��Ŀ��
	 * @param height
	 *            ָ�����ͼ��ĸ߶�
	 * @param createThumbnail
	 *            �Ƿ��������ͼ
	 * @return ��ɵ�����ͼ
	 */
	public static Bitmap getImageThumbnail(String imagePath, int width,
			int height, boolean createThumbnail) {

		int degree = getExifRotate(imagePath);

		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// ��ȡ���ͼƬ�Ŀ�͸ߣ�ע��˴���bitmapΪnull
		bitmap = BitmapFactory.decodeFile(imagePath, options);

		// �������ű�
		options.inSampleSize = 4;
		if (createThumbnail) {
			int h = options.outHeight;
			int w = options.outWidth;
			int beWidth = w / width;
			int beHeight = h / height;
			int be = 1;
			if (beWidth < beHeight) {
				be = beWidth;
			} else {
				be = beHeight;
			}
			if (be <= 0) {
				be = 1;
			}
			options.inSampleSize = be;
		}
		// ���¶���ͼƬ����ȡ���ź��bitmap��ע�����Ҫ��options.inJustDecodeBounds ��Ϊ false
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(imagePath, options);

		bitmap = rotateBitmap(degree, bitmap);

		if (createThumbnail)
			// ����ThumbnailUtils����������ͼ������Ҫָ��Ҫ�����ĸ�Bitmap����
			bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
					ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	private static Bitmap rotateBitmap(int degree, Bitmap bitmap) {
		if (degree != 0 && bitmap != null) {
			Matrix m = new Matrix();
			m.setRotate(degree);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), m, true);
		}
		return bitmap;
	}

	private static int getExifRotate(String imagePath) {
		ExifInterface exifInterface;
		int degree = 0;
		try {
			exifInterface = new ExifInterface(imagePath);
			int tag = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, -1);

			if (tag == ExifInterface.ORIENTATION_ROTATE_90) {
				degree = 90;
			} else if (tag == ExifInterface.ORIENTATION_ROTATE_180) {
				degree = 180;
			} else if (tag == ExifInterface.ORIENTATION_ROTATE_270) {
				degree = 270;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * ��ȡ��Ƶ������ͼ ��ͨ��ThumbnailUtils������һ����Ƶ������ͼ��Ȼ��������ThumbnailUtils�����ָ����С������ͼ��
	 * �����Ҫ������ͼ�Ŀ�͸߶�С��MICRO_KIND��������Ҫʹ��MICRO_KIND��Ϊkind��ֵ��������ʡ�ڴ档
	 * 
	 * @param videoPath
	 *            ��Ƶ��·��
	 * @param width
	 *            ָ�������Ƶ����ͼ�Ŀ��
	 * @param height
	 *            ָ�������Ƶ����ͼ�ĸ߶ȶ�
	 * @param kind
	 *            ����MediaStore.Images.Thumbnails���еĳ���MINI_KIND��MICRO_KIND��
	 *            ���У�MINI_KIND: 512 x 384��MICRO_KIND: 96 x 96
	 * @return ָ����С����Ƶ����ͼ
	 */
	public static Bitmap getVideoThumbnail(String videoPath, int width,
			int height) {
		Bitmap bitmap = null;
		// ��ȡ��Ƶ������ͼ
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath,
				MediaStore.Images.Thumbnails.MICRO_KIND);

		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	/**
	 * ��ȡͼƬ��������ϸ��Ϣ
	 * 
	 * @param imagePath
	 * @return
	 */
	public static String getImageInfo(String imagePath) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inSampleSize = 1;
		// ��ȡ���ͼƬ�Ŀ�͸ߣ�ע��˴���bitmapΪnull
		bitmap = BitmapFactory.decodeFile(imagePath, options);

		String result = options.outWidth + "," + options.outHeight;

		return result;
	}

	public static String getVideoInfo(String videoPath) {

		// MediaMetadataRetriver retriver = new MediaMetadataRetriver();

		return "null";
	}

	public static String getVoiceInfo(String voicePath) {
		return "null";
	}

	/**
	 * ��ý���ļ���������ʱ�ļ��й��ϴ���
	 * 
	 * @param imagePath
	 */
	public static String copy2TempStore(String sourcePath) throws IOException {

		File sourceFile = new File(sourcePath);

		if (!sourceFile.exists())
			return "";

		// Ǩ���ļ�ǰ����������
		String prefix = sourceFile.getName().substring(
				sourceFile.getName().lastIndexOf(".") + 1);
		String desName = String.valueOf(System.currentTimeMillis()) + "."
				+ prefix;
		;
		String destination = StandardizationDataHelper
				.getAccessoryFileTempStorePath() + "//" + desName;

		File targetFile = new File(destination);

		if (!targetFile.exists()) {
			targetFile.createNewFile();
		}

		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			// �½��ļ���������������л���
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

			// �½��ļ��������������л���
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

			// ��������
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// ˢ�´˻���������
			outBuff.flush();

			return destination;
		} finally {
			// �ر���
			if (inBuff != null)
				inBuff.close();
			if (outBuff != null)
				outBuff.close();
		}
	}
	/**
	 * ��ý���ļ���������ʱ�ļ��й��ϴ���
	 * 
	 * @param imagePath
	 */
	public static String copy2TempStoreWithOriginalName(String sourcePath) throws IOException {

		File sourceFile = new File(sourcePath);

		if (!sourceFile.exists())
			return "";

		// Ǩ���ļ�ǰ����������
		String prefix = sourceFile.getName().substring(sourceFile.getName().lastIndexOf(".") + 1);
		String desName = sourceFile.getName().substring(0,sourceFile.getName().lastIndexOf("."))+"_"+String.valueOf(System.currentTimeMillis()) + "."+ prefix;
		;
		String destination = StandardizationDataHelper.getAccessoryFileTempStorePath() + "//" + desName;

		File targetFile = new File(destination);

		if (!targetFile.exists()) {
			targetFile.createNewFile();
		}

		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			// �½��ļ���������������л���
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

			// �½��ļ��������������л���
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

			// ��������
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// ˢ�´˻���������
			outBuff.flush();

			return destination;
		} finally {
			// �ر���
			if (inBuff != null)
				inBuff.close();
			if (outBuff != null)
				outBuff.close();
		}
	}
	/**
	 * ��ý���ļ���������ʱ�ļ��й��ϴ���
	 * 
	 * @param imagePath
	 */
	public static String move2TempStore(String sourcePath) throws IOException {

		File sourceFile = new File(sourcePath);

		if (!sourceFile.exists())
			return "";

		String destination = StandardizationDataHelper
				.getAccessoryFileTempStorePath() + "//" + sourceFile.getName();

		File targetFile = new File(destination);

		if (!targetFile.exists()) {
			targetFile.createNewFile();
		}

		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			// �½��ļ���������������л���
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

			// �½��ļ��������������л���
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

			// ��������
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// ˢ�´˻���������
			outBuff.flush();

			// ɾ��Դ�ļ�
			sourceFile.delete();

			return destination;
		} finally {
			// �ر���
			if (inBuff != null)
				inBuff.close();
			if (outBuff != null)
				outBuff.close();
		}

	}

	/**
	 * ��ȡý���ļ���
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileName(String path) {

		File file = new File(path);

		if (file.exists())
			return file.getName();
		else
			return "";
	}

	/**
	 * ��ȡ�ļ���С
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static String getFileSize(String path) throws IOException {
		File file = new File(path);

		int fileLen = 0;

		if (file.exists()) {
			FileInputStream fis = new FileInputStream(file);

			fileLen = fis.available();
		}
		return String.valueOf(fileLen);
	}

	/**
	 * ɾ���ļ�
	 * 
	 * @param path
	 */
	public static void deleteFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * �ж��Ƿ�URI��ʽ��·����Ϣ
	 * 
	 * @param uri
	 * @return
	 */
	public static boolean isUri(String uri) {

		uri = uri.toLowerCase();

		if (uri.startsWith("content"))
			return true;
		else
			return false;
	}

	/**
	 * ��ݸ������ʹ���Ԥ��ͼ
	 * 
	 * @param position
	 * @param accPath
	 * @return
	 */
	public static Bitmap createItemImage(String accPath, Context context,
			int width, int height) {

		File file = new File(accPath);

		if (!file.exists())
			return null;

		Bitmap bitmap = null;
		switch (MediaHelper.checkFileType(accPath)) {
		case Picture:
			bitmap = MediaHelper.getImageThumbnail(accPath, width, height);
			break;
		case Video:
			bitmap = MediaHelper.getVideoThumbnail(accPath, width, height);
			break;
		case Voice:
			if (audioBitmap == null)
				bitmap = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.phone_common_voice);
			else {
				bitmap = audioBitmap;
			}
			break;
		case Complex:
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.complex_file);
			break;
		default:
			break;
		}
		return bitmap;
	}

	/**
	 * ��ȡ�����Ԥ��ͼ
	 * 
	 * @param m_id
	 * @param context
	 * @return
	 */
	public static Bitmap getManuscriptPreview(String m_id, Context context) {
		AccessoriesService service = new AccessoriesService(context);

		List<Accessories> list = service.getAccessoriesListByMID(m_id);

		if (list != null && list.size() > 0) {
			Accessories acc = list.get(0);

			return MediaHelper.createItemImage(acc.getUrl(), context, 100, 70);
		}

		return null;
	}
}
