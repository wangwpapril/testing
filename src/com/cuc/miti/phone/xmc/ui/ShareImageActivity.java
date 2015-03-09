package com.cuc.miti.phone.xmc.ui;

import java.util.ArrayList;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Enums.LoginStatus;
import com.cuc.miti.phone.xmc.utils.ToastHelper;
import com.cuc.miti.phone.xmc.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

public class ShareImageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		shareImage();
	}

	private void shareImage() {
		
		if (IngleApplication.getLoginStatus() == LoginStatus.none) {
			dialog(getString(R.string.share_title), onYesClickListener, onNoClickListener, getString(R.string.yes),getString(R.string.no));
		} else {
			Intent intent = new Intent(this, AddManuscriptsActivity.class);
			Intent intent_img = this.getIntent();
			if (intent_img != null && intent_img.getAction() != null
					&& intent_img.getAction().equals(Intent.ACTION_SEND)) {
				Bundle extras = intent_img.getExtras();

				if (extras.containsKey("android.intent.extra.STREAM")) {

					Uri uri = (Uri) extras.get("android.intent.extra.STREAM");
					intent.putExtra("URI_IMG", uri.toString());
				}
				
			}
			else if (intent_img != null && intent_img.getAction() != null
					&& intent_img.getAction().equals(Intent.ACTION_SEND_MULTIPLE)) {
				Bundle extras = intent_img.getExtras();

				if (extras.containsKey("android.intent.extra.STREAM")) {

					ArrayList<Uri> uri =  extras.getParcelableArrayList("android.intent.extra.STREAM");
					intent.putParcelableArrayListExtra("LIST_URI_IMG",uri);
				}
				
			}
			startActivity(intent);
			this.finish();
		}
		
	}
	protected void dialog(String message,
			android.content.DialogInterface.OnClickListener listenerYes,
			android.content.DialogInterface.OnClickListener listenerNo,
			String yes, String no) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage(message);
		builder.setTitle(getString(R.string.alert));

		builder.setPositiveButton(yes, listenerYes);
		builder.setNegativeButton(no, listenerNo);
		builder.create().show();
	}
	android.content.DialogInterface.OnClickListener onYesClickListener = new android.content.DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {

			dialog.dismiss();
			openSoftware();
		}
	};
	android.content.DialogInterface.OnClickListener onNoClickListener = new android.content.DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {

			dialog.dismiss();
			finish();
		}
	};
	private void openSoftware(){
		Intent intent = new Intent( this,SplashScreenActivity.class);
		startActivity(intent);
		this.finish();
	}
}
