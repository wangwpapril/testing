package com.cuc.miti.phone.xmc.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cuc.miti.phone.xmc.IngleApplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



public class IngleUtils {

	private static ConnectivityManager sConnectivityManager;
	public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
//	public static BMapManager mBMapMan;

	public static <T> boolean isEmpty(List<T> list) {
		if (list == null || list.isEmpty()) {
			return true;
		}

		return false;
	}
	
	public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

	public static boolean isConnected(Context context) {
		if (sConnectivityManager == null) { // lazily get ConnectivityManager
			sConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
		}
		NetworkInfo network = sConnectivityManager.getActiveNetworkInfo();

		if (network == null) {
			return false;
		}

		return network.isConnected()
				|| (network.getState() == NetworkInfo.State.CONNECTING);
	}

	public static void setDisplay(Activity context) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		Display display = context.getWindowManager().getDefaultDisplay();
		display.getMetrics(displayMetrics);
//		IngleApplication.setDisplay(display.getWidth(), display.getHeight(),
	//			displayMetrics.densityDpi);
	}

	public static void setNetWork(Context context) {
		if (Build.VERSION.SDK_INT < 14) {
			context.startActivity(new Intent(
					android.provider.Settings.ACTION_WIRELESS_SETTINGS));
		} else {
			context.startActivity(new Intent(
					android.provider.Settings.ACTION_SETTINGS));
		}
	}

	public static void showToast(Context context, int resId) {
		Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
	}

	public static void showToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	public static void setPaintFlags(TextView textView) {
		textView.setPaintFlags(textView.getPaintFlags()
				| Paint.STRIKE_THRU_TEXT_FLAG);
	}

	public static String generaDateStr() {
		return simpleDateFormat.format(new Date());
	}

	/**
	 * Count string 's length.
	 * 
	 * @param str
	 * @return
	 */
	public static int getWordCount(CharSequence str) {
		int length = 0;
		if (str != null) {
			int size = str.length();
			int num = 0;
			for (int i = 0; i < size; i++) {
				final int ascii = Character.codePointAt(str, i);
				if (ascii >= 255) {
					length++;
				} else if (ascii >= 0 && ascii < 255) {
					num++;
					if (num != 2) {
						length++;
					} else {
						num = 0;
					}
				}
			}
		}

		return length;
	}

	// coform qq
	public static boolean isQQ(String qq) {
		Pattern p = Pattern.compile("[1-9][0-9]{4,14}");
		Matcher m = p.matcher(qq);
		boolean isExist = m.matches();
		return isExist;
	}

	// coform tel
	public static boolean isTel(String tel) {
		Pattern p = Pattern
				.compile("((13[0-9])|(15[^4，\\D])|(18[0,5-9]))\\d{8}$");
		Pattern p1 = Pattern
				.compile("^(0[0-9]{2,3})?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?$");
		boolean isExist = false;
		if (p.matcher(tel).matches())
			isExist = true;
		if (p1.matcher(tel).matches())
			isExist = true;
		return isExist;
	}

	public static void phoneCall(final Context context, final String phoneNumber) {
		// the first, Nothing selected
		final int WHICH_SELECTED_ITEM = -1;
		// Separated by one or more spaces
		final String[] phones = phoneNumber.split("\\;");
		// Area code prefix
		String codePrefix = "";

		for (int i = 0; i < phones.length; i++) {
			if (i == 0 && phones[0].contains("-")) {
				codePrefix = phones[i].substring(0, phones[i].indexOf("-") + 1);
			} else if (i != 0 && !phones[i].contains("-")) {
				phones[i] = codePrefix + phones[i];
			}
		}

		if (phones.length > 1) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setSingleChoiceItems(phones, WHICH_SELECTED_ITEM,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							Intent intentDial = new Intent(Intent.ACTION_DIAL,
									Uri.parse("tel:" + phones[which]));
							context.startActivity(intentDial);
						}
					});
			builder.show();
		} else {
			Intent intentDial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
					+ phones[0]));
			context.startActivity(intentDial);
		}
	}
	
	public static String toHexString(byte by) {
		try {
			return Integer.toHexString((0x000000ff & by) | 0xffffff00).substring(6);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String toUTF8(char ch) {
		StringBuffer buffer = new StringBuffer();

		if (ch < 0x80) {
			buffer.append(ch);
		} else if (ch < '\u0800') {
			buffer.append("%" + toHexString((byte) (0xc0 | (ch >> 6))));
			buffer.append("%" + toHexString((byte) (0x80 | (ch & 0x3f))));
		} else {
			buffer.append("%" + toHexString((byte) (0xe0 | (ch >> 12))));
			buffer.append("%"+ toHexString((byte) (0x80 | ((ch >> 6) & 0x3f))));
			buffer.append("%" + toHexString((byte) (0x80 | (ch & 0x3f))));
		}

		return buffer.toString();
	}

	public static String urlEncode(String sUrl) {
		if (sUrl == null) {
			return "";
		}
		StringBuffer StrUrl = new StringBuffer();
		for (int i = 0; i < sUrl.length(); ++i) {
			char c = sUrl.charAt(i);
			switch (c) {
			case ' ':
				StrUrl.append("%20");
				break;
			case '+':
				StrUrl.append("%2b");
				break;
			case '\'':
				StrUrl.append("%27");
				break;
			case '/':
				StrUrl.append("%2F");
				break;
			case '.':
				StrUrl.append("%2E");
				break;
			case '<':
				StrUrl.append("%3c");
				break;
			case '>':
				StrUrl.append("%3e");
				break;
			case '#':
				StrUrl.append("%23");
				break;
			case '%':
				StrUrl.append("%25");
				break;
			case '&':
				StrUrl.append("%26");
				break;
			case '{':
				StrUrl.append("%7b");
				break;
			case '}':
				StrUrl.append("%7d");
				break;
			case '\\':
				StrUrl.append("%5c");
				break;
			case '^':
				StrUrl.append("%5e");
				break;
			case '~':
				StrUrl.append("%73");
				break;
			case '[':
				StrUrl.append("%5b");
				break;
			case ']':
				StrUrl.append("%5d");
				break;
			default:

				if (c < 128)
					StrUrl.append(sUrl.charAt(i));
				else {
					StrUrl.append(toUTF8(c));
				}
				break;
			}
		}

		return StrUrl.toString();
	}

	public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasJellyBean() {
        return false;// Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }
    
    public static byte[] toBytes(int n) {  
        byte[] bytes = new byte[4];  
        for (int i = 0; i < bytes.length; i++) {  
            bytes[i] = (byte) ((n >> (i << 3)) & 0xFF);  
        }  
        return bytes;  
    }  
    public static byte[] toBytes(long n) {  
        byte[] bytes = new byte[8];  
        for (int i = 0; i < bytes.length; i++) {  
            bytes[i] = (byte) ((n >> (i << 3)) & 0xFF);  
        }  
        return bytes;  
    }  
    public static int toInt(byte[] bytes) {  
        int n = 0;  
        for (int i = 0; i < bytes.length; i++) {  
            n = n | ((bytes[i] & 0xff) << (i << 3));  
        }  
        return n;  
    }  
    public static long toLong(byte[] bytes) {  
        long n = 0;  
        for (int i = 0; i < bytes.length; i++) {  
            n = n | ((bytes[i] & 0xffL) << (i << 3));  
        }  
        return n;  
    }  
    public static byte[] toBytes(short n){  
        byte[] bytes=new byte[2];  
        for (int i = 0; i < bytes.length; i++) {  
            bytes[i] = (byte) ((n >> (i << 3)) & 0xFF);  
        }  
        return bytes;  
    }  
    public static byte[] toByte(char c){  
        return toBytes((short)c);  
    }  
    public static short toShort(byte[] bytes){  
        short n = 0;  
        for (int i = 0; i < bytes.length; i++) {  
            n = (short) (n | ((bytes[i] & 0xff) << (i << 3)));  
        }  
        return n;  
    }  
    public static char toChar(byte[] bytes){  
        return (char)toShort(bytes);  
    }  
    
	public static Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
	}
}
