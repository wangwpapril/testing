package com.cuc.miti.phone.xmc.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Enums.NetStatus;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceKeys;
import com.cuc.miti.phone.xmc.domain.Enums.TemplateType;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.logic.ManuscriptTemplateService;
import com.cuc.miti.phone.xmc.logic.MessageService;
import com.cuc.miti.phone.xmc.services.LocationService;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.ToastHelper;
import com.cuc.miti.phone.xmc.xmpp.ServiceManager;
import com.cuc.miti.phone.xmc.R;

/**
 * ��ҳActivity���û��ɹ���½֮������ҳ ��Ҫ���ܣ��½������������?��Ѷ��ϵͳ���á���Ϣ���ĵ�
 * 
 * @author SongQing
 * 
 */
public class MainActivity extends BaseActivity implements OnClickListener {

	private ViewPager mPager; // ���һ���ҳ��
	private ImageButton iBtnNewManuscript, iBtnSystemConfig, iBtnMessage,
			iBtnMore; // �½������ϵͳ���á���Ϣ���ġ����
	private LinearLayout llMiddle; // Բ��������LinearLayout
	private List<View> listViews; // Tabҳ���б�
	private ImageView[] dots; // �ײ�СԲ��
	private int currentIndex; // ��ǰԲ��λ��

	private MessageService messageService;
	private ManuscriptTemplateService manuscriptTemplateService;
	MainHandler mHandler = null;
	private static final int MSG_UPDATE_MSG_NEW = 1;
	private static final int REQUEST_UPDATE_MsgNewCount = 4;
	private TextView textViewMsgNew;

	/**
	 * ��ʼ��ҳ��ؼ�
	 */
	private void setUpViews() {
		iBtnNewManuscript = (ImageButton) findViewById(R.id.iBtnNewManuscript_Main);
		iBtnSystemConfig = (ImageButton) findViewById(R.id.iBtnSystemConfig_Main);
		iBtnMessage = (ImageButton) findViewById(R.id.iBtnMessage_Main);
		iBtnMore = (ImageButton) findViewById(R.id.iBtnMore_Main);

		iBtnNewManuscript.setOnClickListener(this);
		iBtnSystemConfig.setOnClickListener(this);
		iBtnMessage.setOnClickListener(this);
		iBtnMore.setOnClickListener(this);

		textViewMsgNew = (TextView) findViewById(R.id.textViewMsgNew_Main);

		// iBtnNewManuscript.setOnTouchListener(TouchEffect.TouchDark);
		// iBtnNewManuscript.setOnFocusChangeListener(TouchEffect.buttonOnFocusChangeListener);

		mPager = (ViewPager) findViewById(R.id.vPager_Main);
	}

	/**
	 * ��ʼ����ʾҳ��λ�õ�СԲ��
	 */
	private void initializeDot() {
		llMiddle = (LinearLayout) findViewById(R.id.linearLayoutMiddle_Main);

		dots = new ImageView[2]; // ����ȷ��ֻ������Page������д��

		// ѭ��ȡ��С��ͼƬ
		for (int i = 0; i < 2; i++) {
			dots[i] = (ImageView) llMiddle.getChildAt(i);
			dots[i].setEnabled(true);// ����Ϊ��ɫ
			dots[i].setOnClickListener(this);
			dots[i].setTag(i);// ����λ��tag������ȡ���뵱ǰλ�ö�Ӧ
		}

		currentIndex = 0;
		dots[currentIndex].setEnabled(false);// ����Ϊ��ɫ����ѡ��״̬

	}

	/**
	 * ��ǰ��С���ѡ��
	 */
	private void setCurDot(int positon) {
		if (positon < 0 || positon > 1 || currentIndex == positon) {
			return;
		}

		dots[positon].setEnabled(false);
		dots[currentIndex].setEnabled(true);

		currentIndex = positon;
	}

	/**
	 * ��ʼ��ViewPager�ؼ�
	 */
	private void initViewPager() {
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();
		listViews.add(mInflater.inflate(R.layout.list_manuscripts, null));
		listViews.add(mInflater.inflate(R.layout.faster_manuscripts, null));
		mPager.setAdapter(new ViewPagerAdapter(listViews));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		initialize();

		IngleApplication.getInstance().addActivity(this);
		
		Intent locationIntent = new Intent(IngleApplication.getInstance(), LocationService.class);
		startService(locationIntent);

	}

	/**
	 * ��ʼ��
	 */
	private void initialize() {

		messageService = new MessageService(MainActivity.this);
		manuscriptTemplateService = new ManuscriptTemplateService(
				MainActivity.this);
		mHandler = new MainHandler();

		setUpViews();
		initViewPager();
		initializeDot();
		setNewMessageNumber();

		getMessageThread();

/*		ServiceManager serviceManager = new ServiceManager(this);
		serviceManager.setNotificationIcon(R.drawable.logo);
		serviceManager.startService();
*/	}

	/**
	 * ҳ���л�����
	 * 
	 * @author SongQing
	 * 
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		// ������״̬�ı�ʱ����
		public void onPageScrollStateChanged(int arg0) {

		}

		// ��ǰҳ�汻����ʱ����
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		// ���µ�ҳ�汻ѡ��ʱ����
		public void onPageSelected(int arg0) {
			// ���õײ�С��ѡ��״̬
			setCurDot(arg0);
		}

	}

	/**
	 * ����ҳ��ؼ�����¼�
	 */
	public void onClick(View v) {
		Intent mIntent;
		switch (v.getId()) {
		case R.id.iBtnNewManuscript_Main:
			mIntent = new Intent(this, AddManuscriptsActivity.class);
			mIntent.putExtra("id", "");
			startActivity(mIntent);
			break;
		case R.id.iBtnSystemConfig_Main:
			mIntent = new Intent(MainActivity.this, SystemConfigActivity.class);
			startActivity(mIntent);
			break;
		case R.id.iBtnMessage_Main:
			mIntent = new Intent(MainActivity.this, MessageActivity.class);
			startActivityForResult(mIntent, REQUEST_UPDATE_MsgNewCount);
			break;
		case R.id.iBtnMore_Main:
			mIntent = new Intent(MainActivity.this, MoreActivity.class);
			startActivity(mIntent);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			setNewMessageNumber();
			// switch(requestCode){
			// case REQUEST_UPDATE_MsgNewCount:
			// setNewMessageNumber();
			// break;
			// default:
			// break;
			// }
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			dialog();
			return false;
		}
		return false;
	}

	protected void dialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage(R.string.exitSysDialogMsg_Main);
		builder.setTitle(R.string.exitSysDialogTitle_Main);
		builder.setPositiveButton(R.string.helloButton,
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// ActivityManager am =
						// (ActivityManager)getSystemService(ACTIVITY_SERVICE);
						// am.restartPackage(getPackageName());
						IngleApplication.isStop = true;

						IngleApplication.getInstance()
								.exit();
					}
				});
		builder.setNegativeButton(R.string.cancel_button,
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	/**
	 * �Զ��������� ViewPager
	 */
	private class ViewPagerAdapter extends PagerAdapter implements
			OnClickListener {

		public List<View> mListViews;
		private ImageButton iBtnWordFlash, iBtnVideoFlash, iBtnVoiceFlash,
				iBtnPicFlash, iBtnSentList, iBtnStandToList, iBtnEditingList,
				iBtnEliminationList;

		public ViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		/**
		 * ���ViewPager���ص�View index ��ʼ����ͬView�ϵĿؼ�
		 * 
		 * @param position
		 */
		private void setUpViews(int position) {
			switch (position) {
			case 0:
				iBtnSentList = (ImageButton) mListViews.get(position)
						.findViewById(R.id.iBtnSentManu_Main);
				iBtnStandToList = (ImageButton) mListViews.get(position)
						.findViewById(R.id.iBtnStandToManu_Main);
				iBtnEditingList = (ImageButton) mListViews.get(position)
						.findViewById(R.id.iBtnEditingManu_Main);
				iBtnEliminationList = (ImageButton) mListViews.get(position)
						.findViewById(R.id.iBtnEliminationManu_Main);
				iBtnSentList.setOnClickListener(this);
				iBtnStandToList.setOnClickListener(this);
				iBtnEditingList.setOnClickListener(this);
				iBtnEliminationList.setOnClickListener(this);

				break;
			case 1:
				iBtnVideoFlash = (ImageButton) mListViews.get(position)
						.findViewById(R.id.iBtnVideoFlash_Main);
				iBtnVoiceFlash = (ImageButton) mListViews.get(position)
						.findViewById(R.id.iBtnVoiceFlash_Main);
				iBtnPicFlash = (ImageButton) mListViews.get(position)
						.findViewById(R.id.iBtnPicFlash_Main);
				iBtnWordFlash = (ImageButton) mListViews.get(position)
						.findViewById(R.id.iBtnWordFlashBtn_Main);
				iBtnVideoFlash.setOnClickListener(this);
				iBtnVoiceFlash.setOnClickListener(this);
				iBtnPicFlash.setOnClickListener(this);
				iBtnWordFlash.setOnClickListener(this);

				break;
			default:
				break;
			}
		}

		@Override
		public Object instantiateItem(final View view, int position) {

			this.setUpViews(position);
			((ViewPager) view).addView(mListViews.get(position), 0);
			return mListViews.get(position);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		public void onClick(View v) {
			KeyValueData message = null;
			Intent intent;
			switch (v.getId()) {
			case R.id.iBtnEditingManu_Main:
				intent = new Intent(MainActivity.this,
						EditingManuscriptsActivity.class);
				startActivity(intent);
				break;
			case R.id.iBtnEliminationManu_Main:
				intent = new Intent(MainActivity.this,
						EliminationManuscriptsActivity.class);
				startActivity(intent);
				break;
			case R.id.iBtnSentManu_Main:
				intent = new Intent(MainActivity.this,
						SentManuscriptsActivity.class);
				startActivity(intent);
				break;
			case R.id.iBtnStandToManu_Main:
				intent = new Intent(MainActivity.this,
						StandToManuscriptsActivity.class);
				startActivity(intent);
				break;
			case R.id.iBtnVideoFlash_Main:
				message = new KeyValueData();
				if (manuscriptTemplateService.validateTemplate(
						TemplateType.VIDEO, message)) {
					intent = new Intent(MainActivity.this,
							EditVideoFManuscriptsActivity.class);
					startActivity(intent);
				} else {
					intent = new Intent(MainActivity.this,
							ManagementTemplateActivity.class);
					startActivity(intent);
					// Toast.makeText(MainActivity.this, "��������Ϊ�գ�������ǩ������д����!\n"
					// + message.getValue(), Toast.LENGTH_SHORT).show();
					ToastHelper
							.showToast(
									ToastHelper
											.getStringFromResources(R.string.checkTemplate_Main)
											+ message.getValue(),
									Toast.LENGTH_SHORT);
				}
				break;
			case R.id.iBtnVoiceFlash_Main:
				message = new KeyValueData();
				if (manuscriptTemplateService.validateTemplate(
						TemplateType.VOICE, message)) {
					intent = new Intent(MainActivity.this,
							EditVoiceFManuscriptsActivity.class);
					startActivity(intent);
				} else {
					intent = new Intent(MainActivity.this,
							ManagementTemplateActivity.class);
					startActivity(intent);
					// Toast.makeText(MainActivity.this, "��������Ϊ�գ�������ǩ������д����!\n"
					// + message.getValue(), Toast.LENGTH_SHORT).show();
					ToastHelper
							.showToast(
									ToastHelper
											.getStringFromResources(R.string.checkTemplate_Main)
											+ message.getValue(),
									Toast.LENGTH_SHORT);
				}
				break;
			case R.id.iBtnPicFlash_Main:
				message = new KeyValueData();
				if (manuscriptTemplateService.validateTemplate(
						TemplateType.PICTURE, message)) {
					intent = new Intent(MainActivity.this,
							EditPicFManuscriptsActivity.class);
					startActivity(intent);
				} else {
					intent = new Intent(MainActivity.this,
							ManagementTemplateActivity.class);
					startActivity(intent);
					// Toast.makeText(MainActivity.this, "��������Ϊ�գ�������ǩ������д����!\n"
					// + message.getValue(), Toast.LENGTH_SHORT).show();
					ToastHelper
							.showToast(
									ToastHelper
											.getStringFromResources(R.string.checkTemplate_Main)
											+ message.getValue(),
									Toast.LENGTH_SHORT);
				}
				break;
			case R.id.iBtnWordFlashBtn_Main:
				message = new KeyValueData();
				if (manuscriptTemplateService.validateTemplate(
						TemplateType.WORD, message)) {
					intent = new Intent(MainActivity.this,
							EditWordFManuscriptsActivity.class);
					startActivity(intent);
				} else {
					intent = new Intent(MainActivity.this,
							ManagementTemplateActivity.class);
					startActivity(intent);
					// Toast.makeText(MainActivity.this,
					// "��������Ϊ�գ�������ǩ������д����!\n"+ message.getValue(),
					// Toast.LENGTH_SHORT).show();
					ToastHelper
							.showToast(
									ToastHelper
											.getStringFromResources(R.string.checkTemplate_Main)
											+ message.getValue(),
									Toast.LENGTH_SHORT);
				}
				break;
			default:
				break;
			}
		}

	}

	/**
	 * Handler class implementation to handle the message
	 * 
	 * @author SongQing
	 * 
	 */
	private class MainHandler extends Handler {

		// This method is used to handle received messages
		public void handleMessage(Message msg) {
			// switch to identify the message by its code
			switch (msg.what) {
			case MSG_UPDATE_MSG_NEW:
				setNewMessageNumber();
				break;

			default:
				break;
			}
		}
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

	/**
	 * ������Ϣδ��������ʾ
	 */
	public void setNewMessageNumber() {
		int msgNewCount = messageService
				.getMessageNewCount(IngleApplication
						.getInstance().getCurrentUser());
		if (msgNewCount > 0) {
			textViewMsgNew.setText(String.valueOf(msgNewCount));
		} else {
			textViewMsgNew.setText("");
		}
	}

	private void getMessageThread() {
		new Thread(new Runnable() {
			public void run() {

				try {
					synchronized (IngleApplication.mLock) {
						while (IngleApplication
								.getNetStatus() == NetStatus.Disable) {
							IngleApplication.mLock.wait();
						}
					}
				} catch (Exception e) {
					Logger.e(e);
					e.printStackTrace();
				}
				messageService.getMessageFromRemote();
				sendMessage(MSG_UPDATE_MSG_NEW, null);
			}
		}).start();
	}
}
