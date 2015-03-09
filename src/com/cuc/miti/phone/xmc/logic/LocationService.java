package com.cuc.miti.phone.xmc.logic;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.cuc.miti.phone.xmc.R;
import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.PositionInfo;
import com.cuc.miti.phone.xmc.domain.XmcException;
import com.cuc.miti.phone.xmc.http.RemoteCaller;
import com.cuc.miti.phone.xmc.utils.DeviceInfoHelper;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.ToastHelper;

public class LocationService {
	private Context context;

	public static final int MSG_LOGIN = 1;
	public static final int FAIL_LOGIN = 2;

	private ProgressDialog dialog = null;

	public LocationService(Context context) {
		this.context = context;
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case MSG_LOGIN:

				((Activity) context).finish();

				break;
			case FAIL_LOGIN:

				break;
			}

			// ToastHelper.showToast((String)msg.obj, Toast.LENGTH_SHORT);

			super.handleMessage(msg);

		}
	};

	private void sendMessage(int what, Object obj) {
		Message msg = mHandler.obtainMessage();
		msg.what = what;
		msg.obj = obj;
		mHandler.sendMessage(msg);
	}

	private void showLoadingDialog() {
		String message = ToastHelper
				.getStringFromResources(R.string.sending_LCT);
		dialog = ProgressDialog.show(context, "", message, true);
	}

	/**
	 * ���Ͷ�λ����
	 */
	public void location(PositionInfo location, String content,String locationdescribe) {
		showLoadingDialog();
		final String memo = content;
		final String des = locationdescribe;
		final PositionInfo loc = location;
		new Thread(new Runnable() {
			String[] ss;

			public void run() {
				try {
					if (loc != null) {
						IngleApplication.getInstance()
								.getDeviceInfoHelper();
						String source = "AndroidPhone"
								+ DeviceInfoHelper.getAppVersionName(context)[1];
						ss = RemoteCaller
								.Location(IngleApplication
										.getInstance().getCurrentUser(),
										IngleApplication
												.getSessionId(), loc, des,
										memo, source);

						if ("success".equals(ss[0])) {
							ToastHelper.showToast("���ͳɹ�", Toast.LENGTH_SHORT);
							sendMessage(MSG_LOGIN, "");

						} else {
							ToastHelper.showToast("����ʧ��", Toast.LENGTH_SHORT);
							// sendMessage(FAIL_LOGIN, ss[1]);
						}
					} else {
						ToastHelper.showToast("����ʧ��", Toast.LENGTH_SHORT);
						// sendMessage(FAIL_LOGIN, "");

					}
					if (dialog != null) {
						dialog.dismiss();
					}
				} catch (XmcException e) {

					Logger.e(e);
					sendMessage(FAIL_LOGIN, e.getMessage());

				}
			}
		}).start();
	}
}
