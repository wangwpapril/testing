package com.cuc.miti.phone.xmc.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.cuc.miti.phone.xmc.R;
import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.adapter.EditManuscriptAccessoriesAdapter;
import com.cuc.miti.phone.xmc.domain.Accessories;
import com.cuc.miti.phone.xmc.domain.Enums.AccessoryType;
import com.cuc.miti.phone.xmc.domain.Enums.ManuscriptStatus;
import com.cuc.miti.phone.xmc.domain.Enums.OperationType;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceKeys;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.domain.ManuscriptTemplate;
import com.cuc.miti.phone.xmc.domain.Manuscripts;
import com.cuc.miti.phone.xmc.domain.PositionInfo;
import com.cuc.miti.phone.xmc.logic.AccessoriesService;
import com.cuc.miti.phone.xmc.logic.ManuscriptsService;
import com.cuc.miti.phone.xmc.utils.BaiduLocationHelper;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.MediaHelper;
import com.cuc.miti.phone.xmc.utils.SendManuscriptsHelper;
import com.cuc.miti.phone.xmc.utils.SharedPreferencesHelper;
import com.cuc.miti.phone.xmc.utils.StandardizationDataHelper;
import com.cuc.miti.phone.xmc.utils.ToastHelper;
import com.cuc.miti.phone.xmc.utils.TouchEffect;
import com.cuc.miti.phone.xmc.utils.Utils;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;

public class ManuscriptDetails extends BaseActivity implements OnClickListener,
		OnTouchListener, OnGestureListener, RecognizerDialogListener {
	protected String id = "";// ��ǰ���ID

	protected Manuscripts manuscripts = null;// ��ǰ�༭�еĸ������
	protected LinearLayout llContent_editM = null;
	protected ManuscriptsService service = null;
	protected ImageButton imageBtnAccessorie_editM = null;
	protected ImageButton imageBtnSave_editM = null;
	protected ImageButton imageBtnBack_editM = null;
	protected EditText editTextContent_editM = null;
	protected Button btnManuTextCounter_editM = null;
	protected EditText editTextTitle_editM = null;
	protected ImageView iViewTempletSwitcher_editM = null;
	protected ImageButton iBtnGetLocationAdd_editM = null;
	protected ImageButton imageBtnSend_editM = null;
	protected TextView textViewPageTitle_editM = null;
	protected Button btnReBuild_editM = null;
	private Boolean locationBoolean = false; // ��¼��λ��
	private TextView textViewAccerioes_editM;

	// ��������Ի���
	private RecognizerDialog iatDialog;
	protected ImageButton iatButton;
	private SharedPreferences mSharedPreferences;

	private ProgressDialog dialog = null;

	protected static final int REQUEST_CAPTURE_IMAGE = 1;// �����requestCode
	protected static final int REQUEST_CAPTURE_VIDEO = 2;// ����������requestCode
	protected static final int REQUEST_CAPTURE_SOUND = 3;// ¼����requestCode
	protected static final int REQUEST_SELECT_MEDIAFILE = 4;// ý����requestCode
	protected static final int REQUEST_SELECT_COMPLEXFILE = 11;// ý����requestCode
	protected static final int REQUEST_ACCESSORIES_EDIT = 5;// �����༭��requestCode
	protected static final int REQUEST_ACCESSORIES_ADD = 6;// ������ӵ�requestCode
	protected static final int REQUEST_MANUSCRIPTTEMPLATE_GET = 7;// ������ӵ�requestCode
	private static final int REQUEST_SELECT_IMAGE = 8;// ������ӵ�ѡ���
	private static final int REQUEST_SELECT_VIDEO = 9;// ������ӵ�ѡ���
	private static final int REQUEST_SELECT_SOUND = 10;// ¼����requestCode
	private static final int REQUEST_CHOOSE_ACC = 12;// ����ѡ����ɵ�requestCode

	// �첽������Ϣ����---��ʱ����
	protected static final int MESSAGE_SAVE_MANUSCRIPT = 100;
	private GestureDetector mGestureDetector; // �����¼�

	private BaiduLocationHelper locationHelper;

	// ���嶨ʱ��
	protected Timer timer;
	private TimerTask task;

	protected String accSrc = ""; // ��Ƭ�洢·��
	protected Uri originalUri = null;// ��Ƭ�洢uri

	protected GridView gridViewAccerioes_editM = null;// �����б���ֿؼ�

	protected EditManuscriptAccessoriesAdapter adapter = null;// �����б�Adapter

	protected boolean isSaved = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// ȥ�������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.edit_manuscripts);
		service = new ManuscriptsService(this);
		iniAutoSave();
		IngleApplication.getInstance().addActivity(this);

		locationHelper = new BaiduLocationHelper(this);
		// �򿪶�λ
		locationHelper.startLocationClient();
	}

	@Override
	protected void onDestroy() {
		if (task != null) {
			task.cancel();
			task = null;
		}
		// �رն�ʱ������
		if (timer != null) {
			timer.cancel();
			timer = null;
		}

		// �رն�λ
		locationHelper.stopLocationClient();
		super.onDestroy();
	}

	@Override
	public void onPause() {

		if (task != null) {
			task.cancel();
			task = null;
		}
		// �رն�ʱ������
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		super.onPause();
	}

	@Override
	public void onStop() {

		if (task != null) {
			task.cancel();
			task = null;
		}
		// �رն�ʱ������
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		super.onStop();
	}

	/**
	 * ��ʼ���Զ����涨ʱ��
	 */
	private void iniAutoSave() {
		// ������ʱ������������ÿ��30�뱣��һ��
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				Message message = new Message();
				message.what = MESSAGE_SAVE_MANUSCRIPT;
				handler.sendMessage(message);
			}
		};
		this.setAutoSaveInterval();
	}

	private void setAutoSaveInterval() {
		// �������л�ȡ�Զ�����ʱ����
		SharedPreferencesHelper helper = new SharedPreferencesHelper(this);

		int interval = 0;
		try {
			String strInterval = helper
					.GetUserPreferenceValue(PreferenceKeys.User_AutoSaveInterval
							.toString());
			if (!strInterval.equals("0"))
				interval = Integer.parseInt(strInterval);
		} catch (Exception e) {
			interval = 0;
		}

		if (interval != 0) {
			interval = interval * 1000;
			timer.schedule(task, interval, interval);
		}

	}

	/**
	 * ί�У���ݶ�ʱ����֪ͨ���б���������
	 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// ���浱ǰ���
			if (msg.what == MESSAGE_SAVE_MANUSCRIPT) {
				setManuscriptInformations();
				boolean result = SendManuscriptsHelper.validateForAutoSave(
						manuscripts, adapter.getAccessories());

				if (result == true) {
					result = save(false);
					if (result) {
						ToastHelper.showToast((getResources()
								.getString(R.string.manu_autosaved)),
								Toast.LENGTH_SHORT);
					}
				}
			}

			super.handleMessage(msg);
		}

	};

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		// ��ʼ�������Ĳ˵���
		menu.add(Menu.NONE, REQUEST_CAPTURE_IMAGE, 0, R.string.TackPicture);
		menu.add(Menu.NONE, REQUEST_CAPTURE_VIDEO, 0, R.string.RecordVideo);
		menu.add(Menu.NONE, REQUEST_CAPTURE_SOUND, 0, R.string.RecordVoice);
		menu.add(Menu.NONE, REQUEST_SELECT_IMAGE, 0, R.string.Picture);
		menu.add(Menu.NONE, REQUEST_SELECT_VIDEO, 0, R.string.Video);
		menu.add(Menu.NONE, REQUEST_SELECT_SOUND, 0, R.string.Voice);
		menu.add(Menu.NONE, REQUEST_SELECT_COMPLEXFILE, 0, R.string.ComplexFile);
	}

	@Override
	public boolean onContextItemSelected(MenuItem menu) {
		try {

			switch (menu.getItemId()) {
			case REQUEST_CAPTURE_IMAGE:
				// ��������ͷ����
				cameraMethod();
				break;
			case REQUEST_CAPTURE_VIDEO:
				// ��������ͷ¼��
				videoMethod();
				break;
			case REQUEST_CAPTURE_SOUND:
				// ����¼������¼��
				soundRecorderMethod();
				break;
			case REQUEST_SELECT_IMAGE:
				// ����ý���
				selectMediaFile(MediaStore.Images.Media.INTERNAL_CONTENT_URI);
				break;
			case REQUEST_SELECT_VIDEO:// ����ý���
				selectMediaFile(MediaStore.Video.Media.INTERNAL_CONTENT_URI);
				break;
			case REQUEST_SELECT_SOUND:
				// ����ý���
				selectMediaFile(MediaStore.Audio.Media.INTERNAL_CONTENT_URI);
				break;
			case REQUEST_SELECT_COMPLEXFILE:
				selectComplexFile();
				break;
			default:
				break;
			}

		} catch (Exception e) {
			Logger.e(e);
		}
		return super.onContextItemSelected(menu);
	}

	// =====================================�¼�==========================

	/**
	 * ��ť����¼�
	 */
	public void onClick(View v) {

		switch (v.getId()) {
		// ���������Ӹ����˵�
		case R.id.imageBtnAccessorie_editM:
			v.showContextMenu();
			break;
		// ��������Ϣ
		case R.id.imageBtnSave_editM:
			save();
			break;
		// �����ϼ�
		case R.id.imageBtnBack_editM:
			back();
			break;
		case R.id.imageBtnVoice_editM: // �����������
			showIatDialog();
			break;
		// ��������������
		case R.id.btnManuTextCounter_editM:
			AlertDialog.Builder builder = new AlertDialog.Builder(
					ManuscriptDetails.this);
			builder.setTitle(R.string.manuContent_delete_alert_title);
			builder.setMessage(R.string.manuContent_delete_alert_content);
			builder.setPositiveButton(R.string.confirm_button,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {
							editTextContent_editM.setText("");
						}
					});
			builder.setNegativeButton(R.string.cancel_button, null);
			builder.show();

			break;
		// ����
		case R.id.imageBtnSend_editM:

			showLoadingDialog();

			setManuscriptInformations();

			if (!this.save()) {// ����ǰ����()
				closeLoadingDialog();
			}
			// // ����ǰ���а汾У��
			// KeyValueData resultMessage_version = new KeyValueData("", "");
			// boolean result_version = SendManuscriptsHelper.validateVersion(
			// resultMessage_version, this);
			// if (result_version == true) {
			KeyValueData resultMessage = new KeyValueData("", "");

			boolean result = SendManuscriptsHelper.validateForReleManuscript(
					manuscripts, resultMessage, this);

			if (result == true) {
				// ��ֲ�����
				if (sent()) {
					IngleApplication
							.setMtTemplate(manuscripts.getManuscriptTemplate());
					ToastHelper.showToast(
							this.getResources().getString(
									R.string.validateFinished),
							Toast.LENGTH_SHORT);
					setResult(RESULT_OK);
					finish();
				} else {
					ToastHelper.showToast(
							this.getResources().getString(
									R.string.manu_failtosend),
							Toast.LENGTH_SHORT);
				}
			} else {// ��ʾ����Ϸ���������ԭ��(����/����/��ǩ��)
				ToastHelper.showToast(resultMessage.getValue(),
						Toast.LENGTH_SHORT);
			}
			// }
			// else {// ��ʾ����Ϸ���������ԭ��(�汾)
			// ToastHelper.showToast(resultMessage_version.getValue(),
			// Toast.LENGTH_SHORT);
			// }

			closeLoadingDialog();

			break;
		// �����ǩҳ
		case R.id.iViewTempletSwitcher_editM:
			enterManuscriptTemplate();
			break;
		// ��ȡ��λ��Ϣ
		case R.id.iBtnGetLocationAdd_editM:
			// Update By SongQing.2013.11.27 Ϊ��������ֻ��������������޷���ȡ��λ��Ϣ����ΪBaidu��ʽ��ȡ
			getLocation();

			// if (locationBoolean == false) {
			// LocationHelper locationHelper =
			// newLocationHelper(ManuscriptDetails.this);
			// String location = "0,0";
			// try {
			// location = locationHelper.GetLocation();
			// } catch (Exception e) {
			// Logger.e(e);
			// }
			//
			// if (location != "0,0") {
			// // �ҵ���ǰ�ľ�γ����Ϣ����ʾ��λͼ��
			// // iViewLocationStatus.setVisibility(View.VISIBLE);
			// ToastHelper.showToast(location, Toast.LENGTH_SHORT);
			// manuscripts.setLocation(location);
			// // manuscriptsService.updateManuscripts(manuItem);
			// locationBoolean = true;
			// // ��ͼ��任���Ѷ�λ��ͼ��
			// iBtnGetLocationAdd_editM
			// .setBackgroundResource(R.drawable.got_location_960x540);
			// } else {
			// ToastHelper.showToast(
			// this.getResources().getString(
			// R.string.canNotGetLocation),
			// Toast.LENGTH_SHORT);
			// }
			// } else {
			// manuscripts.setLocation("0,0");
			// locationBoolean = false;
			// // �Ѷ�λ��ͼ��任��ԭ����ͼ��
			// iBtnGetLocationAdd_editM
			// .setBackgroundResource(R.drawable.get_location_960x540);
			// }
			break;
		default:
			break;
		}
	}

	/**
	 * �����ǩҳ
	 */
	protected void enterManuscriptTemplate() {
		Intent intent = new Intent(this, EditTemplateActivity.class);

		Bundle mybundle = new Bundle();
		mybundle.putParcelable("manuTemplateInfo",
				this.manuscripts.getManuscriptTemplate());
		if (this.manuscripts.getManuscriptsStatus().equals(
				ManuscriptStatus.Editing)) {
			mybundle.putString("requestType", "ManuscriptEdit");
		} else {
			mybundle.putString("requestType", "View"); // ����ֻ��
		}

		intent.putExtras(mybundle);

		startActivityForResult(intent, REQUEST_MANUSCRIPTTEMPLATE_GET);
		// �����л����������ұ߽��룬����˳�
		overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
	}

	// ======================================��ʼ������============================
	/**
	 * ��ʼ����ť
	 */
	protected void initializeButtons() {
		// ��ʼ����Ӹ�����ť
		imageBtnAccessorie_editM = (ImageButton) findViewById(R.id.imageBtnAccessorie_editM);
		imageBtnAccessorie_editM.setOnClickListener(this);
		imageBtnAccessorie_editM.setOnTouchListener(TouchEffect.TouchDark);

		imageBtnBack_editM = (ImageButton) findViewById(R.id.imageBtnBack_editM);
		imageBtnBack_editM.setOnClickListener(this);
		imageBtnBack_editM.setOnTouchListener(TouchEffect.TouchDark);

		// ��ʼ����������ť
		imageBtnSave_editM = (ImageButton) findViewById(R.id.imageBtnSave_editM);
		imageBtnSave_editM.setOnClickListener(this);
		imageBtnSave_editM.setOnTouchListener(TouchEffect.TouchDark);
		// ע�������Ĳ˵�
		registerForContextMenu(imageBtnAccessorie_editM);

		// ��ʼ�������ı���
		editTextContent_editM = (EditText) findViewById(R.id.editTextContent_editM);
		editTextContent_editM.addTextChangedListener(new TextChangedWatcher());

		// �ؽ���ť
		btnReBuild_editM = (Button) findViewById(R.id.btnRebuild_editM);

		iBtnGetLocationAdd_editM = (ImageButton) findViewById(R.id.iBtnGetLocationAdd_editM);
		iBtnGetLocationAdd_editM.setOnClickListener(this);
		iBtnGetLocationAdd_editM.setOnTouchListener(TouchEffect.TouchDark);

		// ��ʼ������������ͳ���¼�
		btnManuTextCounter_editM = (Button) findViewById(R.id.btnManuTextCounter_editM);
		btnManuTextCounter_editM.setOnClickListener(this);

		// ��ǩģ���л���ť
		iViewTempletSwitcher_editM = (ImageView) findViewById(R.id.iViewTempletSwitcher_editM);
		iViewTempletSwitcher_editM.setOnClickListener(this);
		// iViewTempletSwitcher_editM.setOnTouchListener(this);

		// ��ʼ������
		editTextTitle_editM = (EditText) findViewById(R.id.editTextTitle_editM);

		// ��ʼ�����Ͱ�ť
		imageBtnSend_editM = (ImageButton) findViewById(R.id.imageBtnSend_editM);
		imageBtnSend_editM.setOnClickListener(this);
		imageBtnSend_editM.setOnTouchListener(TouchEffect.TouchDark);

		// ��ʼ������gridview
		gridViewAccerioes_editM = (GridView) findViewById(R.id.gridViewAccerioes_editM);

		llContent_editM = (LinearLayout) findViewById(R.id.llContent_editM);

		// ��ʼ������
		textViewPageTitle_editM = (TextView) findViewById(R.id.textViewPageTitle_editM);

		mGestureDetector = new GestureDetector((OnGestureListener) this);

		// ��ʼ���������밴ť
		iatButton = (ImageButton) findViewById(R.id.imageBtnVoice_editM);
		iatButton.setOnClickListener(this);
		iatButton.setOnTouchListener(TouchEffect.TouchDark);

		// ��ʼ����������Ի���
		iatDialog = new RecognizerDialog(ManuscriptDetails.this, "appid="
				+ getString(R.string.app_id));
		iatDialog.setListener(this);

		textViewAccerioes_editM = (TextView) findViewById(R.id.textViewAccerioes_editM);
		// textViewAccerioes_editM.setVisibility(View.VISIBLE);
		mSharedPreferences = this.getSharedPreferences(this.getPackageName(),
				Context.MODE_PRIVATE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String ouputPath = "";
		String end = "";
		String fileURLFD = "";
		String fileURLFDC = "";
		try {
			if (resultCode == Activity.RESULT_OK) {
				// ���У�����ж������պ�Ĳ�������¼·������ɸ������󣬽�����ϸ��Ϣҳ����б༭
				AccessoryType accType = AccessoryType.Cache;
				Accessories acc = null;

				switch (requestCode) {
				// ���ͼƬ�ļ���Ĵ��?����Ϣ����ҳ
				case REQUEST_CAPTURE_IMAGE:
					ouputPath = accSrc;
					if (ouputPath.equals(""))
						return;
					end = ouputPath.substring(ouputPath.lastIndexOf(".") + 1,
							ouputPath.length()).toLowerCase();
					fileURLFD = ouputPath;
					fileURLFDC = fileURLFD.substring(0,
							fileURLFD.lastIndexOf("."))
							+ "_resized." + end;
					ouputPath = Utils.CompressBitmap(fileURLFDC, fileURLFD);
					if (ouputPath.equals(fileURLFDC))
						MediaHelper.deleteFile(fileURLFD);
					Utils.notifyMediaScanToFindThePic(ouputPath);
					// ���·����ý������������ɸ�������
					accType = MediaHelper.checkFileType(ouputPath);

					acc = populateAccessorie(ouputPath, accType);
					startActivitys(acc, accType, REQUEST_ACCESSORIES_ADD,
							OperationType.Add);
					break;
				// �����Ƶ�ļ���Ĵ��?����Ϣ����ҳ
				case REQUEST_CAPTURE_VIDEO:
					// ��������ļ���Ĵ��?����Ϣ����ҳ
				case REQUEST_CAPTURE_SOUND:
					// ���ý����ļ���Ĵ��?����Ϣ����ҳ
				case REQUEST_SELECT_MEDIAFILE:
					// ����ļ�ϵͳ�ļ���Ĵ��?����Ϣ����ҳ
				case REQUEST_SELECT_COMPLEXFILE:
					ouputPath = fetchMediaPath(data);
					ouputPath = MediaHelper.copy2TempStore(ouputPath);
					if (ouputPath.equals(""))
						return;
					end = ouputPath.substring(ouputPath.lastIndexOf(".") + 1,
							ouputPath.length()).toLowerCase();
					if (end.equals("jpg") || end.equals("png")
							|| end.equals("jpeg")) {
						fileURLFD = ouputPath;
						fileURLFDC = fileURLFD.substring(0,
								fileURLFD.lastIndexOf("."))
								+ "_resized." + end;
						ouputPath = Utils.CompressBitmap(fileURLFDC, fileURLFD);
						if (ouputPath.equals(fileURLFDC))
							MediaHelper.deleteFile(fileURLFD);
						// Utils.notifyMediaScanToFindThePic(ouputPath);
					}
					// ���·����ý������������ɸ�������
					accType = MediaHelper.checkFileType(ouputPath);

					acc = populateAccessorie(ouputPath, accType);
					startActivitys(acc, accType, REQUEST_ACCESSORIES_ADD,
							OperationType.Add);
					break;
				// ������Ӹ�����Ĵ���
				case REQUEST_ACCESSORIES_ADD:
					Bundle addBundle = data.getExtras();
					if (addBundle != null) {
						// �õ���Ӻ�ĸ�������
						acc = (Accessories) addBundle.getParcelable("acc");

						adapter.addCurrentAccessories(acc);

						this.save();
					}
					break;
				// �����޸ĸ�����Ϣ��Ĵ���
				case REQUEST_ACCESSORIES_EDIT:
					Bundle editBundle = data.getExtras();
					if (editBundle != null) {
						// �õ��޸ĺ�ĸ�������
						acc = (Accessories) editBundle.getParcelable("acc");

						adapter.updateCurrentAccessories(acc);

						this.save();
					}
					break;
				// �����ǩҳ��Ĵ�����
				case REQUEST_MANUSCRIPTTEMPLATE_GET:
					Bundle templateBundle = data.getExtras();
					if (templateBundle != null) {
						String editOrViewString = templateBundle
								.getString("EditOrView");
						if (editOrViewString != null
								&& editOrViewString.equals("View")) { // �����ViewTemplate����Ҫ����
							break;
						}
						// �õ���ǩ����
						ManuscriptTemplate template = (ManuscriptTemplate) templateBundle
								.getParcelable("manuTemplateInfo");

						// ����Ĭ�ϸ�ǩ���⡢���ݡ����ߡ���ǩģ��
						if (template != null) {
							String defaultTitle = template.getDefaulttitle();
							if (defaultTitle != null
									&& editTextTitle_editM.getText().toString()
											.trim().equals("")) {
								String title = editTextTitle_editM.getText()
										.toString();
								title = defaultTitle + title;
								editTextTitle_editM.setText(title);
							}
							String defaultContent = template
									.getDefaultcontents();
							if (defaultContent != null
									&& editTextContent_editM.getText()
											.toString().trim().equals("")) {
								String content = editTextContent_editM
										.getText().toString();
								content = defaultContent + content;
								editTextContent_editM.setText(content);
							}

							this.manuscripts.setManuscriptTemplate(template);
							this.manuscripts.setAuthor(template.getAuthor());

							this.save();
						}
					}
					break;
				case REQUEST_CHOOSE_ACC:
					Bundle accsBundle = data.getExtras();
					if (accsBundle != null) {
						ArrayList<Accessories> tempAccs = accsBundle
								.getParcelableArrayList("ACCS");
						if (tempAccs != null) {
							adapter.setAccessories(tempAccs);
							adapter.notifyDataSetChanged();
						}
					}
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			Logger.e(e);
		}
	}

	/**
	 * ���Intent������ݻ�ȡѡ���ý���ļ��ĵ�ַ�� ��ý�����ѡȡ���ļ���URI����SD����ֱ��ѡȡ���ļ��Ǿ��·��
	 * 
	 * @param data
	 * @return
	 */
	private String fetchMediaPath(Intent data) {
		String ouputPath = "";

		Uri uri = data.getData();
		if (uri == null)
			return "";
		// �ж��Ƿ�URI���͵����
		boolean isUri = MediaHelper.isUri(uri.toString());

		if (isUri == true) {

			Cursor cursor = this.getContentResolver().query(uri, null, null,
					null, null);

			if (cursor.moveToNext()) {
				ouputPath = cursor.getString(cursor.getColumnIndex("_data"));
			}
			cursor.close();
		} else {
			ouputPath = uri.getPath();
		}

		return ouputPath;
	}

	/**
	 * �����µ�Active������ý�����
	 * 
	 * @param ouputPath
	 * @param requestCode
	 */
	protected void startActivitys(Accessories acc, AccessoryType accType,
			int requestCode, OperationType opType) {
		Class<?> intentClass = null;
		switch (accType) {
		case Picture:
			intentClass = ManagePicAccessoryActivity.class;
			break;
		case Video:
			intentClass = ManageVideoAccessoryActivity.class;
			break;
		case Voice:
			intentClass = ManageVoiceAccessoryActivity.class;
			break;
		case Complex:
			intentClass = ManageComplexAccessoryActivity.class;
			break;
		default:
			break;
		}

		if (intentClass == null)
			return;

		try {
			// �����µĸ���������ͼ
			Intent intent = new Intent(this, intentClass);
			Bundle mybundle = new Bundle();
			mybundle.putParcelable("acc", acc);
			intent.putExtras(mybundle);

			intent.putExtra("operation", opType.toString());

			startActivityForResult(intent, requestCode);
		} catch (Exception e) {
			Logger.e(e);
		}

	}

	// ==========================�����ش����������ӡ�ȷ�ϡ�����洢��===================
	/**
	 * ���浱ǰ�༭�еĸ������, ��ҪУ��true������ҪУ��false
	 */
	protected boolean save(boolean validate) {
		try {
			if (adapter.getAccessories().size() > 0)
				textViewAccerioes_editM.setVisibility(View.GONE);
			else
				textViewAccerioes_editM.setVisibility(View.VISIBLE);

			// �������������ĵ�ֵ���õ�����manuscript��
			setManuscriptInformations();

			// ��ʽ����ǰ�Ƿ�У�飬����Զ�����ɲ�У��
			if (validate == true) {
				KeyValueData message = new KeyValueData("", "");
				boolean validateResult = SendManuscriptsHelper.validataForSave(
						this.manuscripts, message);

				if (!validateResult) {
					ToastHelper.showToast(message.getValue(),
							Toast.LENGTH_SHORT);
					return false;
				}
			}

			if (service.updateManuscripts(this.manuscripts)) {
				AccessoriesService service_acc = new AccessoriesService(this);
				for (Accessories acc : adapter.getAccessories())
					service_acc.updateAccessories(acc);
				if (validate != false) {
					isSaved = true;
				}
				ToastHelper.showToast(ToastHelper
						.getStringFromResources(R.string.manuSuccessToSave),
						Toast.LENGTH_SHORT);
				return true;
			} else {
				ToastHelper.showToast(
						this.getResources().getString(R.string.manuSavedError),
						Toast.LENGTH_SHORT);
				return false;
			}

		} catch (Exception e) {
			ToastHelper
					.showToast(ToastHelper
							.getStringFromResources(R.string.manuSavedError),
							Toast.LENGTH_SHORT);
			Logger.e(e);
			return false;
		}
	}

	/**
	 * ���浱ǰ�༭�еĸ��������ҪУ��
	 */
	protected boolean save() {
		return this.save(true);
	}

	/**
	 * ��ӵ�ǰ�༭�еĸ������
	 */
	protected boolean add() {
		try {
			// ���ø�������ֵ
			setManuscriptInformations();
			// ��ӵ���ݿ�
			Manuscripts ma = service.getManuscripts(this.manuscripts.getM_id());
			if (ma != null)
				return false;
			boolean result = service.addManuscripts(this.manuscripts);

			return result;
		} catch (Exception e) {
			Logger.e(e);
			return false;
		}
	}

	/**
	 * ���ظ���ҳ��
	 */
	protected void back() {
	}

	/**
	 * ���͸��
	 */
	protected boolean sent() {
		try {
			return service.sendNormalManuscripts(this.manuscripts,
					this.adapter.getAccessories());
		} catch (Exception e) {
			Logger.e(e);
			return false;
		}

	}

	protected void setManuscriptInformations() {
		if (editTextTitle_editM != null)
			this.manuscripts.setTitle(this.editTextTitle_editM.getText()
					.toString().trim());
		if (editTextContent_editM != null)
			this.manuscripts.setContents(this.editTextContent_editM.getText()
					.toString().trim());
	}

	/**
	 * �󶨸��������б?Gridview��
	 */
	protected void bindAccessoriesList(List<Accessories> accessories) {
		adapter = new EditManuscriptAccessoriesAdapter(accessories, this);
		gridViewAccerioes_editM.setAdapter(adapter);
		if (adapter.getAccessories().size() > 0)
			textViewAccerioes_editM.setVisibility(View.GONE);
		else
			textViewAccerioes_editM.setVisibility(View.VISIBLE);
		// ��ʼ��gridview�����¼�
		gridViewAccerioes_editM
				.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						Accessories acc = adapter.getAccessories()
								.get(position);
						startActivitys(acc,
								MediaHelper.checkFileType(acc.getUrl()),
								REQUEST_ACCESSORIES_EDIT, OperationType.Update);
					}
				});
		// ��ʼ��gridview����¼�
		gridViewAccerioes_editM
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						Accessories acc = adapter.getAccessories()
								.get(position);
						startActivitys(acc,
								MediaHelper.checkFileType(acc.getUrl()),
								REQUEST_ACCESSORIES_EDIT, OperationType.Update);
					}

					public boolean onItemLongClick(AdapterView<?> parent,
							View v, int position, long id) {
						final Accessories acc = adapter.getAccessories().get(
								position);
						new AlertDialog.Builder(ManuscriptDetails.this)
								.setTitle(getString(R.string.choose))
								.setIcon(android.R.drawable.ic_dialog_info)
								.setItems(
										new String[] {
												getString(R.string.oneKeySynchro),
												getString(R.string.chooseNeedSynchro) },
										new DialogInterface.OnClickListener() {

											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
												if (which == 0) {
													oneKeySynchro(acc, adapter
															.getAccessories());
													adapter.notifyDataSetChanged();
													ToastHelper.showToast(
															"ͬ�����",
															Toast.LENGTH_SHORT);

												} else if (which == 1)
													chooseToSynchro(
															acc,
															adapter.getAccessories());

											}
										})
								.setNegativeButton(getString(R.string.cancel),
										null).show();
						return true;
					}
				});
	}

	/**
	 * һ��ͬ��
	 */
	public void oneKeySynchro(Accessories acc, List<Accessories> accessories) {
		String title = acc.getTitle();
		String desc = acc.getDesc();
		if (title != null && desc != null) {
			for (int i = 0; i < accessories.size(); i++) {
				accessories.get(i).setTitle(title);
				accessories.get(i).setDesc(desc);
			}
		}

	}

	/**
	 * ѡ��ͬ��
	 */
	public void chooseToSynchro(Accessories acc, List<Accessories> accessories) {
		ArrayList<Accessories> accs = new ArrayList<Accessories>(accessories);

		Intent intent = new Intent(this, AttachmentChooseActivity.class);
		intent.putExtra("ACC", acc);
		intent.putParcelableArrayListExtra("ACCESSORIES", accs);
		startActivityForResult(intent, REQUEST_CHOOSE_ACC);

	}

	// ========================����ϵͳ���ܽ��ж�ý�帽������=============================

	/**
	 * ��Ƭ
	 */
	private void cameraMethod() {
		Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		accSrc = StandardizationDataHelper.getAccessoryFileTempStorePath()
				+ "//" + System.currentTimeMillis() + ".jpg";

		originalUri = Uri.fromFile(new File(accSrc));
		imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, originalUri);

		// imageCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult(imageCaptureIntent, REQUEST_CAPTURE_IMAGE);
	}

	/**
	 * ��Ƶ
	 */
	private void videoMethod() {
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

		// intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

		startActivityForResult(intent, REQUEST_CAPTURE_VIDEO);
	}

	/**
	 * ¼��
	 */
	private void soundRecorderMethod() {
		// Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		// intent.setType("audio/amr");
		// startActivityForResult(intent, REQUEST_CAPTURE_SOUND);

		Intent intent = new Intent(this, RecorderActivity.class);
		startActivityForResult(intent, REQUEST_CAPTURE_SOUND);

		// Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		// intent.setType("audio/amr");
		// intent.setClassName("com.android.soundrecorder",
		// "com.android.soundrecorder.SoundRecorder");
		// startActivityForResult(intent, REQUEST_CAPTURE_SOUND);
	}

	/**
	 * ��ý���ѡ��ý���ļ�
	 */
	private void selectMediaFile(Uri internalContentUri) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		// new Intent(Intent.ACTION_GET_CONTENT);
		// intent.setType("image/*");
		// intent.setData(Uri.parse("content://media/internal/images/media"));
		intent.setData(internalContentUri);
		/* ȡ��Ƭ�󷵻ر����� */
		startActivityForResult(intent, REQUEST_SELECT_MEDIAFILE);
	}

	private void selectComplexFile() {
		Intent intent = new Intent(this, FileManagerActivity.class);
		startActivityForResult(intent, REQUEST_SELECT_COMPLEXFILE);
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
	private Accessories populateAccessorie(String accPath, AccessoryType accType) {

		Accessories accessorie = new Accessories();
		accessorie.setA_id(UUID.randomUUID().toString());
		accessorie.setM_id(this.manuscripts.getM_id());
		accessorie.setType(accType.toString());

		try {
			accessorie.setOriginalName(MediaHelper.getFileName(accPath));
			accessorie.setSize(MediaHelper.getFileSize(accPath));
			accessorie.setCreatetime(new Date().toString());
			accessorie.setUrl(accPath);
		} catch (IOException ex) {
			accessorie.setOriginalName("");
			accessorie.setSize("");
			ToastHelper.showToast(
					this.getResources().getString(R.string.filePathError),
					Toast.LENGTH_SHORT);
			Logger.e(ex);
		}

		// ͼƬ·��
		accessorie.setUrl(accPath);

		return accessorie;
	}

	/**
	 * ���EditText�е����ֱ仯
	 * 
	 * @author SongQing
	 * 
	 */
	class TextChangedWatcher implements TextWatcher {
		public void afterTextChanged(Editable s) {
			int number = s.length();
			btnManuTextCounter_editM.setText("  " + number);
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}
	}

	/**
	 * ��ʾ��Ϣ
	 */
	private void showLoadingDialog() {
		String message = this.getResources().getString(
				R.string.manuscripi_operation_send_waiting);
		dialog = ProgressDialog.show(this, "", message, true);
	}

	private void closeLoadingDialog() {
		if (dialog != null)
			dialog.dismiss();
	}

	/*----------------------------------------------���津�����������¼� Begin-------------------------------------------------------*/
	/**
	 * �����¼����
	 */
	public boolean onTouch(View v, MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

	public boolean onDown(MotionEvent arg0) {
		return false;
	}

	private int verticalMinDistance = 100; // ˮƽ��С�Ļ�������
	private int minVelocity = 0;

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		mGestureDetector.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * @e1 ���������ƶ��¼�
	 * @e2 ��ǰ���Ƶ���ƶ��¼�
	 * @velocityX ÿ��x�᷽���ƶ�������
	 * @velocityY ÿ��y�᷽���ƶ�������
	 */
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > verticalMinDistance
				&& Math.abs(velocityX) > minVelocity) { // ��������
			enterManuscriptTemplate();
		} else if (e2.getX() - e1.getX() > verticalMinDistance
				&& Math.abs(velocityX) > minVelocity) { // ��������

		}
		return false;
	}

	public void onLongPress(MotionEvent e) {

	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	public void onShowPress(MotionEvent e) {

	}

	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	/*----------------------------------------------���津�����������¼� End-------------------------------------------------------*/

	public void onEnd(SpeechError arg0) {
	}

	/*----------------------------------------------�������� Begin-------------------------------------------------------*/
	public void onResults(ArrayList<RecognizerResult> results, boolean isLast) {
		StringBuilder builder = new StringBuilder();
		for (RecognizerResult recognizerResult : results) {
			builder.append(recognizerResult.text);
		}
		int index = editTextContent_editM.getSelectionStart();
		if (index < 0 || index > editTextContent_editM.length())
			editTextContent_editM.append(builder);
		else
			editTextContent_editM.getEditableText().insert(index, builder);
	}

	public void showIatDialog() {
		String engine = mSharedPreferences.getString(
				getString(R.string.preference_key_iat_engine),
				getString(R.string.preference_default_iat_engine));

		String area = null;

		iatDialog.setEngine(engine, area, null);

		String rate = mSharedPreferences.getString(
				getString(R.string.preference_key_iat_rate),
				getString(R.string.preference_default_iat_rate));
		if (rate.equals("rate8k"))
			iatDialog.setSampleRate(RATE.rate8k);
		else if (rate.equals("rate11k"))
			iatDialog.setSampleRate(RATE.rate11k);
		else if (rate.equals("rate16k"))
			iatDialog.setSampleRate(RATE.rate16k);
		else if (rate.equals("rate22k"))
			iatDialog.setSampleRate(RATE.rate22k);
		iatDialog.show();
	}

	// ��Ӹ���
	public void addAcc(Uri uri) {

		if (uri == null)
			return;
		// �ж��Ƿ�URI���͵����
		boolean isUri = MediaHelper.isUri(uri.toString());
		String ouputPath = "";
		if (isUri == true) {

			Cursor cursor = this.getContentResolver().query(uri, null, null,
					null, null);

			if (cursor.moveToNext()) {
				ouputPath = cursor.getString(cursor.getColumnIndex("_data"));
			}
			cursor.close();
		} else {
			ouputPath = uri.getPath();
		}

		if (ouputPath.equals(""))
			return;
		try {
			ouputPath = MediaHelper.copy2TempStore(ouputPath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		AccessoryType accType = AccessoryType.Cache;
		Accessories acc = null;
		// ���·����ý������������ɸ�������
		accType = MediaHelper.checkFileType(ouputPath);

		acc = populateAccessorie(ouputPath, accType);
		startActivitys(acc, accType, REQUEST_ACCESSORIES_ADD, OperationType.Add);

	}

	/**
	 * �ڸ���༭ҳ��ر�ʱ�Ƿ���Ҫ������
	 */
	public void saveManuscriptBeforeClose() {
		// У��������Ϣ�Ƿ���Ҫ����
		boolean result = SendManuscriptsHelper.validateForBack(
				this.manuscripts, adapter.getAccessories());

		// �ж�������½�������£�û�б��⡢���ݡ���������ɾ��˸��
		if (result == false && isSaved == false) {
			try {
				service.deleteById(this.manuscripts.getM_id());
			} catch (IOException e) {
				Logger.e(e);
			}
		} else {
			if (this.manuscripts.getTitle().isEmpty()) {
				this.manuscripts
						.setTitle(ToastHelper
								.getStringFromResources(R.string.value_manu_notitle_default));
				if (this.editTextTitle_editM != null) {
					this.editTextTitle_editM
							.setText(R.string.value_manu_notitle_default);
				}
			}
			this.save();
		}
	}

	/**
	 * ��ȡ��λ
	 */
	private void getLocation() {
		if (locationBoolean == false) {
			PositionInfo positionInfo = null;
			String location = "0,0";
			try {
				positionInfo = locationHelper.getCurrentLocation();

				if (positionInfo.getLatitude() != 0
						&& positionInfo.getLongitude() != 0) {
					location = String.valueOf(positionInfo.getLatitude()) + ","
							+ String.valueOf(positionInfo.getLongitude());
				}
			} catch (Exception e) {
				Logger.e(e);
			}

			if (location != "0,0") {
				// �ҵ���ǰ�ľ�γ����Ϣ����ʾ��λͼ��
				// iViewLocationStatus.setVisibility(View.VISIBLE);
				if (!TextUtils.isEmpty(positionInfo.getAddress())) {
					ToastHelper.showToast(positionInfo.getAddress(),
							Toast.LENGTH_SHORT);
				} else {
					ToastHelper.showToast(location, Toast.LENGTH_SHORT);
				}
				manuscripts.setLocation(location);
				// manuscriptsService.updateManuscripts(manuItem);
				locationBoolean = true;
				// ��ͼ��任���Ѷ�λ��ͼ��
				iBtnGetLocationAdd_editM
						.setBackgroundResource(R.drawable.got_location_960x540);
			} else {
				ToastHelper
						.showToast(
								this.getResources().getString(
										R.string.canNotGetLocation),
								Toast.LENGTH_SHORT);
			}
		} else {
			manuscripts.setLocation("0,0");
			locationBoolean = false;
			// �Ѷ�λ��ͼ��任��ԭ����ͼ��
			iBtnGetLocationAdd_editM
					.setBackgroundResource(R.drawable.get_location_960x540);
		}
	}

}
