package com.cuc.miti.phone.xmc.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.conn.ConnectTimeoutException;

import com.cuc.miti.phone.xmc.R;
import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.XmcException;
import com.cuc.miti.phone.xmc.domain.Enums.NetStatus;
import com.cuc.miti.phone.xmc.http.Configuration;
import com.cuc.miti.phone.xmc.http.RemoteCaller;
import com.cuc.miti.phone.xmc.utils.DeviceInfoHelper;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.StandardizationDataHelper;
import com.cuc.miti.phone.xmc.utils.ToastHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class VersionUpdateActivity extends Activity{
	private static final int MSG_VALIDATE_VERSION_FINISH = 5;
	private static final int MSG_VALIDATE_VERSION_ERROR = 6;
	private static final int MSG_VERSION_UPDATE_START = 7;
	private static final int MSG_NETWORK_TIMEOUT = -1;
	private static int NORMAL_TIMEOUT = 6000; // ��ͨ��������ʱ
	private static String apkUrl = ""; // ϵͳ��װ��·��
	private static String savePath = ""; // ���ذ�װ·��
	private static String saveFileName = ""; // ����·����(�����ļ���)
	private ProgressDialog dialog = null;
	private AlertDialog dialogAlert = null;
	/* �������֪ͨuiˢ�µ�handler��msg���� */
	private ProgressBar mProgress;
	private static final int DOWN_UPDATE = 12;
	private static final int DOWN_OVER = 13;
	protected static final int MSG_SHOWNOTICEDIALOG = 14;
	protected static final int MSG_SHOWNOTICEDIALOGOPTION = 15;
	private int progress;
	private boolean interceptFlag = false;
	private AlertDialog.Builder builder = null;
	private MyHandler mHandler = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		apkUrl = Configuration.getSystemUpgradeUrl();
		savePath = StandardizationDataHelper.GetBaseDataFileStorePath();
		saveFileName = savePath
				+ apkUrl.substring(apkUrl.lastIndexOf("/"), apkUrl.length());

		this.setUpViews();
		compareVerson();
	}
	/**
	 * Handler class implementation to handle the message
	 * 
	 * @author SongQing
	 * 
	 */
	private class MyHandler extends Handler {

		// This method is used to handle received messages
		public void handleMessage(Message msg) {
			// switch to identify the message by its code
			switch (msg.what) {
			
			case MSG_SHOWNOTICEDIALOG:
				showNoticeDialog();
				break;
			case MSG_SHOWNOTICEDIALOGOPTION:
				showNoticeDialogOption();
				break;
			case MSG_VERSION_UPDATE_START:
				showDownloadDialog();
				break;
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				break;
			case DOWN_OVER:
				installApk();
				VersionUpdateActivity.this.finish();
				break;
			case MSG_VALIDATE_VERSION_FINISH:
				if (msg.obj != null) {
					// Toast.makeText(SystemConfigActivity.this,msg.obj.toString(),
					// Toast.LENGTH_SHORT).show();
					ToastHelper.showToast(msg.obj.toString(),
							Toast.LENGTH_SHORT);
				}
				VersionUpdateActivity.this.finish();
				break;
			case MSG_VALIDATE_VERSION_ERROR:
				if (msg.obj != null) {
					// Toast.makeText(SystemConfigActivity.this,msg.obj.toString(),
					// Toast.LENGTH_SHORT).show();
					ToastHelper.showToast(msg.obj.toString(),
							Toast.LENGTH_SHORT);
				}
				VersionUpdateActivity.this.finish();
				break;
			
			case MSG_NETWORK_TIMEOUT:
				// Toast.makeText(SystemConfigActivity.this,"��������ʱ,������������!",
				// Toast.LENGTH_SHORT).show();
				ToastHelper.showToast(ToastHelper
						.getStringFromResources(R.string.requestNetTimeout),
						Toast.LENGTH_SHORT);
				if (dialogAlert != null) {
					dialogAlert.dismiss();
				}
				VersionUpdateActivity.this.finish();
				break;
			default:
				break;
			}

			if (dialog != null) {
				dialog.dismiss();
			}
		}
	}
	/**
	 * ��ʼ��ҳ��ؼ�
	 */
	private void setUpViews() {
		mHandler = new MyHandler();
	}
	/**
	 * �汾У�飬���������Ҫ���ǿ����
	 */
	private void compareVerson() {
		new Thread(new Runnable() {

			public void run() {
				try {
					synchronized (IngleApplication.mLock) {
						while (IngleApplication
								.getNetStatus() == NetStatus.Disable) {
							sendMessage(
									MSG_VALIDATE_VERSION_FINISH,
									ToastHelper
											.getStringFromResources(R.string.network_disconnected));
							IngleApplication.mLock.wait();
						}
					}
				} catch (Exception e) {
					Logger.e(e);
					e.printStackTrace();
				}

				try {
					String[] versionServer = RemoteCaller
							.getAndroidAppVersion();
					String[] versionLocal = DeviceInfoHelper
							.getAppVersionName(VersionUpdateActivity.this);
					Float versionLow = Float.parseFloat(versionServer[0]);
					Float versionHigh = Float.parseFloat(versionServer[1]);
					Float versionCurrent = Float.parseFloat(versionLocal[1]);
					apkUrl = versionServer[2];

					if (versionCurrent < versionLow) // ��ǰ�汾������Ͱ汾ʱ���������
					{
						sendMessage(MSG_SHOWNOTICEDIALOG, null);
					} else if (versionCurrent < versionHigh) // ��ǰ�汾������߰汾ʱ��ѡ�����
					{
						sendMessage(MSG_SHOWNOTICEDIALOGOPTION, null);
					} else {
						sendMessage(MSG_VALIDATE_VERSION_FINISH, ToastHelper
								.getStringFromResources(R.string.noNewVersion));
					}

				} catch (XmcException e) {
					if (e.getMessage().equals(XmcException.TIMEOUT)) {
						sendMessage(MSG_NETWORK_TIMEOUT, e.getMessage());
					} else {
						sendMessage(MSG_VALIDATE_VERSION_ERROR, e.getMessage());
					}
				} catch (Exception e) {
					Logger.e(e);
					sendMessage(MSG_VALIDATE_VERSION_ERROR, e.getMessage());
				}
			}
		}).start();
	}

	/**
	 * ǿ����Ի���(ֻ��ȷ����û��ȡ��)
	 */
	public void showNoticeDialog() {
		builder = new AlertDialog.Builder(VersionUpdateActivity.this);
		builder.setTitle(R.string.updateSysVersionDialogTitle)
				.setPositiveButton(
						R.string.updateSysVersionDialogPositiveButton,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								sendMessage(MSG_VERSION_UPDATE_START, null);
							}
							// }).setMessage("�汾��ͣ�������").setCancelable(false).create().show();
						})
				.setMessage(R.string.updateSysVersionDialogAbsoluteMsg)
				.setCancelable(false);
		dialogAlert = builder.create();
		dialogAlert.show();
	}

	/**
	 * ��ѡ��Ի���
	 */
	public void showNoticeDialogOption() {
		builder = new AlertDialog.Builder(VersionUpdateActivity.this);
		dialogAlert = builder
				.setTitle(R.string.updateSysVersionDialogTitle)
				.setPositiveButton(
						R.string.updateSysVersionDialogPositiveButton,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								sendMessage(MSG_VERSION_UPDATE_START, null);
							}
						})
				.setNegativeButton(R.string.cancel_button,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								sendMessage(MSG_VALIDATE_VERSION_FINISH, null);
							}
							// }).setMessage("���°汾������").setCancelable(false).create().show();
						})
				.setMessage(R.string.updateSysVersionDialogOpotionMsg)
				.setCancelable(false).create();
		dialogAlert.show();
	}

	/**
	 * ���ؽ�ȶԻ���
	 */
	public void showDownloadDialog() {
		builder = new AlertDialog.Builder(VersionUpdateActivity.this);
		builder.setTitle(R.string.updateSysVersionDialogTitle);
		final LayoutInflater inflater = LayoutInflater
				.from(VersionUpdateActivity.this);
		View v = inflater.inflate(R.layout.progress_system_update, null);
		mProgress = (ProgressBar) v
				.findViewById(R.id.progressBar_SystemUpdate_Splash);
		// builder.setView(v).setCancelable(false).create().show();
		builder.setView(v).setCancelable(false);
		dialogAlert = builder.create();
		dialogAlert.show();
		DownloadAPKThread();
	}

	/**
	 * ����ϵͳ������߳�
	 */
	private void DownloadAPKThread() {
		new Thread(new Runnable() {
			public void run() {
				try {
					URL url = new URL(apkUrl);

					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setConnectTimeout(NORMAL_TIMEOUT);
					conn.setReadTimeout(NORMAL_TIMEOUT);
					conn.connect();

					int length = conn.getContentLength();
					InputStream is = conn.getInputStream();

					File file = new File(savePath);
					if (!file.exists()) {
						file.mkdir();
					}
					String apkFile = saveFileName;
					File ApkFile = new File(apkFile);
					FileOutputStream fos = new FileOutputStream(ApkFile);

					int count = 0;
					byte buf[] = new byte[1024];

					do {
						int numread = is.read(buf);
						count += numread;
						progress = (int) (((float) count / length) * 100);
						// ���½��
						sendMessage(DOWN_UPDATE, null);
						if (numread <= 0) {
							// �������֪ͨ��װ
							sendMessage(DOWN_OVER, null);

							break;
						}
						fos.write(buf, 0, numread);
					} while (!interceptFlag);// ���ȡ���ֹͣ����.

					fos.close();
					is.close();
				} catch (ConnectTimeoutException e) {
					Logger.e(e);
					sendMessage(MSG_NETWORK_TIMEOUT, e.getMessage());
				} catch (InterruptedIOException e) {
					Logger.e(e);
					sendMessage(MSG_NETWORK_TIMEOUT, e.getMessage());
				} catch (Exception e) {
					Logger.e(e);
					sendMessage(MSG_VALIDATE_VERSION_ERROR, e.getMessage());
				}
			}

		}).start();
	}

	/**
	 * ��װapk
	 * 
	 * @param url
	 */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		VersionUpdateActivity.this.startActivity(i);
	}

	/**
	 * �ֻ�����¼����
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// KeyEvent.KEYCODE_BACK��?�ز���.
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return true;
	}

	private void showLoadingDialog() {
		String message = ToastHelper.getStringFromResources(R.string.updating);
		dialog = ProgressDialog.show(VersionUpdateActivity.this, "", message,
				true);
	}

	/**
	 * ��Handler����������Ϣ
	 * 
	 * @param what
	 * @param obj
	 */
	private void sendMessage(int what, Object obj) {
		Message msg = mHandler.obtainMessage();
		msg.what = what;
		msg.obj = obj;
		mHandler.sendMessageDelayed(msg, 500);
	}
}
