package com.cuc.miti.phone.xmc.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cuc.miti.phone.xmc.R;
import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.PositionInfo;
import com.cuc.miti.phone.xmc.logic.LocationService;
import com.cuc.miti.phone.xmc.utils.BaiduLocationHelper;
import com.cuc.miti.phone.xmc.utils.ToastHelper;

public class LocationServiceActivity extends BaseActivity implements	OnClickListener {
	private static final int MESSAGE_AUTO_GET_LOCATION = 1; // ��ʱ��ȡλ����Ϣ
	private static final int MESSAGE_AUTO_STOP_LOCATION = 2; // ֹͣ��ȡλ����Ϣ

	private ImageButton iBtnBack_LCT;
	private EditText editTextContent_LCT;
	private EditText editTextLocation_LCT;

	private ProgressBar progressBar_LCT;
	private TextView textLocation_LCT;
	private Button btnSend_LCT;
	private ImageButton btnLocation_LCT;
	private Boolean isLocated;
	private BaiduLocationHelper locationHelper;
//	private LocationHelper locationHelper;
//	private Location loc;
	private PositionInfo positionInfo;
	// ���嶨ʱ��
	protected Timer timer = new Timer();
	private TimerTask taskLoctaion;
	private TimerTask taskStop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_service);
		this.initialize();

	}

	private void initialize() {

		iBtnBack_LCT = (ImageButton) findViewById(R.id.iBtnBack_LCT);
		btnLocation_LCT = (ImageButton) findViewById(R.id.btnLocation_LCT);
		progressBar_LCT = (ProgressBar) findViewById(R.id.progressBar_LCT);
		editTextContent_LCT = (EditText) findViewById(R.id.editTextContent_LCT);
		editTextLocation_LCT = (EditText) findViewById(R.id.editTextLocation_LCT);
		textLocation_LCT = (TextView) findViewById(R.id.textLocation_LCT);
		btnSend_LCT = (Button) findViewById(R.id.btnSend_LCT);
		btnSend_LCT.setOnClickListener(this);
		iBtnBack_LCT.setOnClickListener(this);
		btnLocation_LCT.setOnClickListener(this);
//		locationHelper = new LocationHelper(this);
		locationHelper = new BaiduLocationHelper(IngleApplication.getInstance());
		locationHelper.startLocationClient();
		iniSearching();
	}

	/* (non-Javadoc)
	 * @see com.cuc.miti.phone.xmc.ui.BaseActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();		
		locationHelper.stopLocationClient();
	}

	/**
	 * ��ʼ���Զ����涨ʱ��
	 */
	private void iniSearching() {
		// ������ʱ������������ÿ��30�뱣��һ��
		taskLoctaion = new TimerTask() {
			@Override
			public void run() {
				Message message = new Message();
				message.what = MESSAGE_AUTO_GET_LOCATION;
				handler.sendMessage(message);
			}
		};
		taskStop = new TimerTask() {
			@Override
			public void run() {
				Message message = new Message();
				message.what = MESSAGE_AUTO_STOP_LOCATION;
				handler.sendMessage(message);
			}
		};
		searching();

		timer.schedule(taskLoctaion, 1000, 3000);
		timer.schedule(taskStop, 16000);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iBtnBack_LCT:
			setResult(RESULT_OK);
			finish();
			break;
		case R.id.btnSend_LCT:
			if (isLocated)
				submit();
			else
				ToastHelper.showToast("δ��ȡ��������Ϣ�����ܷ���", Toast.LENGTH_SHORT);
			break;
		case R.id.btnLocation_LCT:
			iniSearching();
			break;

		}

	}

	/**
	 * ί�У���ݶ�ʱ����֪ͨ���б���������
	 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// ���浱ǰ���
			if (msg.what == MESSAGE_AUTO_GET_LOCATION) {
	
				positionInfo = locationHelper.getCurrentLocation();
				if(positionInfo!=null){
					searchOK();
					taskLoctaion.cancel();
				}
//				loc = locationHelper.getCurrentLocation();
//				if (loc != null) {
//					searchOK();
//					taskLoctaion.cancel();
//
//				}
			} else if (msg.what == MESSAGE_AUTO_STOP_LOCATION) {
				if(positionInfo==null){
//				if (loc == null) {
					taskLoctaion.cancel();
					noSearching();
				}

			}
			super.handleMessage(msg);
		}

	};

	private void submit() {
		String content = editTextContent_LCT.getText().toString();
		String location = editTextLocation_LCT.getText().toString();
		LocationService locationService = new LocationService(this);
//		locationService.location(loc, content,location);
		locationService.location(positionInfo, content, location);

	}

	/**
	 * ���ڻ�ȡλ����Ϣ
	 */
	private void searching() {
		progressBar_LCT.setVisibility(View.VISIBLE);
		textLocation_LCT.setText(R.string.titleLocation_LCT);
		isLocated = false;
//		loc= null;
		positionInfo = null;
	}

	/**
	 * δ��ȡ��λ����Ϣ
	 */
	private void noSearching() {
		progressBar_LCT.setVisibility(View.INVISIBLE);
		textLocation_LCT.setText(R.string.noFindLocation_LCT);
		isLocated = false;
	}

	/**
	 * ��ȡ��λ����Ϣ
	 */
	private void searchOK() {
		progressBar_LCT.setVisibility(View.INVISIBLE);
		textLocation_LCT.setText(R.string.findLocation_LCT);
		isLocated = true;
	}
}
