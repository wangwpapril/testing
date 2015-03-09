package com.cuc.miti.phone.xmc.ui;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.logic.ManuscriptsService;
import com.cuc.miti.phone.xmc.utils.StandardizationDataHelper;
import com.cuc.miti.phone.xmc.utils.TimeFormatHelper;
import com.cuc.miti.phone.xmc.utils.ToastHelper;
import com.cuc.miti.phone.xmc.utils.TouchEffect;
import com.cuc.miti.phone.xmc.R;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class RecorderActivity extends BaseActivity implements OnClickListener {

	public enum MediaRecorderState {
		RECORDING, STOP, PLAY, UNSTART;
	}

	private MediaRecorderState mRecordState;
	private MediaRecorder mRecorder;
	private String recfilename;
	private ImageButton ibtnRecordOperation;
	private ImageButton imageBtnBack_editM;

	private TextView txtTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// ȥ�������
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.recorder);

		IngleApplication.getInstance().addActivity(this);

		mRecordState = MediaRecorderState.UNSTART;

		setupView();
	}

	private void setupView() {

		ibtnRecordOperation = (ImageButton) findViewById(R.id.ibtnRecordOperation);
		ibtnRecordOperation.setOnClickListener(this);
		ibtnRecordOperation.setOnTouchListener(TouchEffect.TouchDark);

		imageBtnBack_editM = (ImageButton) findViewById(R.id.imageBtnBack_editM);
		imageBtnBack_editM.setOnClickListener(this);
		imageBtnBack_editM.setOnTouchListener(TouchEffect.TouchDark);

		txtTime = (TextView) findViewById(R.id.txtTime);
		// Date date = new Date(milliseconds);
		// String dateString = TimeFormatHelper.getFormatTimeV2(date);
		// txtTime.setText(dateString);
	}

	private void startRecording() {
		mRecordState = MediaRecorderState.RECORDING;

		recfilename = StandardizationDataHelper.getAccessoryFileTempStorePath()
				+ "//" + System.currentTimeMillis() + ".amr";
		File file = new File(recfilename);
		if (file.exists()) {
			if (file.delete())
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
		} else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		mRecorder = new MediaRecorder();

		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
		mRecorder.setOutputFile(file.getAbsolutePath());
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		try {
			mRecorder.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			mRecorder.start();
		} catch (Exception e) {
			/*Toast.makeText(getApplicationContext(),
					"Error :: " + e.getMessage(), Toast.LENGTH_LONG).show();*/
			ToastHelper.showToast("Error :: " + e.getMessage(),Toast.LENGTH_SHORT);
		}

	}

	private void stopRecord() {

		if (mRecorder != null) {
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
		}
	}

	private Timer timer;
	private long milliseconds = 0;

	private void start() {

		if (timer != null)
			timer.cancel();

		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				mHandler.sendEmptyMessage(1);
				milliseconds = milliseconds + 1000;
			}
		}, 0, 1000);
	}

	private void stop() {
		if (timer != null)
			timer.cancel();
	}

	private final Handler mHandler = new Handler() {
		public void handleMessage(Message message) {
			super.handleMessage(message);
			switch (message.what) {
			case 1:
				// promotionImages.setSelection(promotionImages.getSelectedItemPosition()
				// + 1);
				Date date = new Date(milliseconds);
				String dateString = TimeFormatHelper.getFormatTimeV2(date);
				txtTime.setText(dateString);
				break;
			}
		}
	};

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ibtnRecordOperation:

			if (mRecordState == MediaRecorderState.UNSTART) {
				ibtnRecordOperation
						.setBackgroundResource(R.drawable.stopbutton);
				startRecording();
				start();
			} else if (mRecordState == MediaRecorderState.RECORDING) {
				ibtnRecordOperation
						.setBackgroundResource(R.drawable.recordbutton);
				stopRecord();

				stop();

				Intent in = new Intent();
				in.setData(Uri.parse(recfilename));
				setResult(RESULT_OK, in);
				finish();
			}

			break;

		case R.id.imageBtnBack_editM:
			finish();
			break;

		default:
			break;
		}
	}
}
