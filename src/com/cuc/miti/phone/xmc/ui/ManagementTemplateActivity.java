package com.cuc.miti.phone.xmc.ui;

import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.adapter.ManagementTemplateAdapter;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceType;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.domain.ManuscriptTemplate;
import com.cuc.miti.phone.xmc.domain.TreeNode;
import com.cuc.miti.phone.xmc.domain.Enums.BaseDataType;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceKeys;
import com.cuc.miti.phone.xmc.domain.Enums.TemplateType;
import com.cuc.miti.phone.xmc.logic.ManuscriptTemplateService;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.TouchEffect;
import com.cuc.miti.phone.xmc.utils.Logger.OperationMessage;
import com.cuc.miti.phone.xmc.utils.SharedPreferencesHelper;
import com.cuc.miti.phone.xmc.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceCategory;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * ����ģ��ҳ
 * @author SongQing
 *
 */
public class ManagementTemplateActivity  extends BaseActivity implements OnClickListener {

	private ImageButton iBtnBack;
	private ListView lvSystemplate;																//ϵͳĬ�Ͽ�Ѷģ��ListView
	private ListView lvCustemplate;															//�û��Զ���ģ��ListView
	private List<ManuscriptTemplate> manuscriptTemplateList;					//ϵͳĬ�Ͽ�Ѷģ���б�
	private List<ManuscriptTemplate> manuscriptTemplateCustomList;		//�û��Զ���ģ���б�
	private LinearLayout linearLayoutSystemTemplateHeader;
	private LinearLayout linearLayoutCustomTemplateHeader;
	private ManagementTemplateAdapter adapter;
	private ManagementTemplateAdapter adapterForCustom;
	private String currentUserName = "";																	//��ǰ�û���
	private String requestType = "";																	    	//����ҳ���ԭ��
	
	private static final int REQUEST_CUSTOM_NEWTEMPLATE = 1;							//�½�ģ��
	private static final int REQUEST_CUSTOM_EDITTEMPLATE = 2;							//�༭ģ��
	private static final int REQUEST_SYSTEM_EDITTEMPLATE = 3;								//�༭ģ��
	private static final int CONTEXT_CUSTOM_DELETE_ITEM = 4;								//ɾ��ģ��
	private static final int CONTEXT_CUSTOM_SET_ITEM_DEFAULT = 5;					//����ģ��ΪĬ��
//	private static final String PREFERENCE_DEFAULT_TEMPLATE = "temlplate_select";				//sharepreference�б���Ĭ��ģ��Key
			
	private ManuscriptTemplateService manuscriptTemplateService = null;
	private SharedPreferencesHelper sharedPreferencesHelper = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.manage_template);
		
		this.initialize();
		IngleApplication.getInstance().addActivity(this);
	}
	
	/**
	 * ��ʼ��
	 */
	private void initialize(){
		this.manuscriptTemplateService = new ManuscriptTemplateService(ManagementTemplateActivity.this);
		this.currentUserName = IngleApplication.getInstance().getCurrentUser();
		sharedPreferencesHelper = new SharedPreferencesHelper(ManagementTemplateActivity.this);
		this.getIntentExtras();
		this.setUpViews();
		this.initializeListView();
	}
	
	/**
	 * ��ȡ��һ��Activity�����������
	 */
	private void getIntentExtras(){
		Bundle mBundle = this.getIntent().getExtras();
		if(mBundle!=null){
			this.requestType = mBundle.get("requestType").toString();			//Adopt-ѡ��ģ������ | Manage-����༭ģ��
		}
	}
	
	/**
	 * ��ʼ��ҳ��ؼ�
	 */
	private void setUpViews(){
		iBtnBack = (ImageButton)findViewById(R.id.iBtnBack_MTA);
		lvSystemplate = (ListView)findViewById(R.id.lvSubTitleA_MTA);
		lvCustemplate= (ListView)findViewById(R.id.lvSubTitleB_MTA);
		iBtnBack.setOnClickListener(this);
		iBtnBack.setOnTouchListener(TouchEffect.TouchDark);
		
		//��ʼ��ListView Header ����ʾ
//		Header header= new Header();					//ϵͳģ��Header
//		View sysListView = LayoutInflater.from(this).inflate(R.layout.manage_template_listview_header, null);
//		header.tViewTitle =  (TextView)sysListView.findViewById(R.id.textViewListViewName_MTA);
//		header.btnNew = (Button)sysListView.findViewById(R.id.btnNewTemplate_MTA);
//		header.tViewTitle.setText(R.string.subTitleA_MTA);
//		lvSystemplate.addHeaderView(sysListView,null,false);
		
		linearLayoutSystemTemplateHeader = (LinearLayout)findViewById(R.id.linearLayoutSystemTemplateHeader_MTA);
		linearLayoutCustomTemplateHeader= (LinearLayout)findViewById(R.id.linearLayoutCustomTemplateHeader_MTA);
		
		Header header= new Header();					//ϵͳģ��Header
		RelativeLayout systemRelativeLayout = (RelativeLayout)LayoutInflater.from(this).inflate(R.layout.manage_template_listview_header, null);
		header.tViewTitle =  (TextView)systemRelativeLayout.findViewById(R.id.textViewListViewName_MTA);
		header.btnNew = (Button)systemRelativeLayout.findViewById(R.id.btnNewTemplate_MTA);
		header.tViewTitle.setText(R.string.subTitleA_MTA);
		linearLayoutSystemTemplateHeader.removeAllViewsInLayout();
		linearLayoutSystemTemplateHeader.addView(systemRelativeLayout);
		
		
		
		header= new Header();								//�û��Զ���ģ��Header	
		RelativeLayout customRelativeLayout = (RelativeLayout)LayoutInflater.from(this).inflate(R.layout.manage_template_listview_header, null);
		header.tViewTitle =  (TextView)customRelativeLayout.findViewById(R.id.textViewListViewName_MTA);
		if(!this.requestType.equals("Adopt")){		//������������ø�ǩ���ô�ҳ�棬����Ҫ����������ť
			header.btnNew = (Button)customRelativeLayout.findViewById(R.id.btnNewTemplate_MTA);
			header.btnNew.setVisibility(View.VISIBLE);	
			header.btnNew.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					Intent mIntent = new Intent(ManagementTemplateActivity.this,EditTemplateActivity.class);
					Bundle mBundle = new Bundle();
					mBundle.putInt("switcherUsing", View.INVISIBLE);
					
					mBundle.putString("requestType", "Manage");			//�����Ǳ༭����
					mIntent.putExtras(mBundle);
					startActivityForResult(mIntent, REQUEST_CUSTOM_NEWTEMPLATE);
				}
			});
		}	
		header.tViewTitle.setText(R.string.subTitleB_MTA);		
		
		linearLayoutCustomTemplateHeader.removeAllViewsInLayout();
		linearLayoutCustomTemplateHeader.addView(customRelativeLayout);
		
//		header= new Header();								//�û��Զ���ģ��Header		
//		View customListView = LayoutInflater.from(this).inflate(R.layout.manage_template_listview_header, null);
//		header.tViewTitle =  (TextView)customListView.findViewById(R.id.textViewListViewName_MTA);
//		if(!this.requestType.equals("Adopt")){		//������������ø�ǩ���ô�ҳ�棬����Ҫ����������ť
//			header.btnNew = (Button)customListView.findViewById(R.id.btnNewTemplate_MTA);
//			header.btnNew.setVisibility(View.VISIBLE);	
//			header.btnNew.setOnClickListener(new View.OnClickListener() {
//				
//				public void onClick(View v) {
//					Intent mIntent = new Intent(ManagementTemplateActivity.this,EditTemplateActivity.class);
//					Bundle mBundle = new Bundle();
//					mBundle.putInt("switcherUsing", View.INVISIBLE);
//					mIntent.putExtras(mBundle);
//					startActivityForResult(mIntent, REQUEST_CUSTOM_NEWTEMPLATE);
//				}
//			});
//		}	
//		header.tViewTitle.setText(R.string.subTitleB_MTA);		
//		lvCustemplate.addHeaderView(customListView,null,false);
	}
	
	/**
	 * ListView ���Զ���Header����
	 * @author SongQing
	 *
	 */
	static class Header { 	
		//��ǩTitle TextView
        TextView tViewTitle = null; 
        //��ǩ�½� Button
        Button btnNew = null;         
    }
	
	/**
	 * �ڽ���ģ������ʱ�ô˷�����ʼ��ListView
	 */
	private void initializeListViewForSelect(){
		//ȡ��ϵͳ��ǩ
		manuscriptTemplateList=manuscriptTemplateService.getManuscriptTemplateSystemList(currentUserName,TemplateType.NORMAL.toString());
				
		adapter=new ManagementTemplateAdapter(manuscriptTemplateList, ManagementTemplateActivity.this);
		lvSystemplate.setAdapter(adapter);
		lvSystemplate.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				//��Ϊ����ListView ��Header Ҳռһ�У�����postion��Ҫ��һ
				//String mt_id = manuscriptTemplateList.get(position-1).getMt_id();
				String mt_id = manuscriptTemplateList.get(position).getMt_id();
				Intent mIntent = new Intent(ManagementTemplateActivity.this,EditTemplateActivity.class);
				Bundle mBundle= new Bundle();
				mBundle.putString("mtId", mt_id);
				mBundle.putInt("switcherUsing", View.VISIBLE);
				mBundle.putString("requestType", "Adopt");
				mIntent.putExtras(mBundle);
				setResult(Activity.RESULT_OK, mIntent); //���÷������
				finish();
			}
		});
								
		//ȡ���Զ����ǩ
		manuscriptTemplateCustomList=manuscriptTemplateService.getManuscriptTemplateCustomList(currentUserName,TemplateType.NORMAL.toString());
		adapterForCustom=new ManagementTemplateAdapter(manuscriptTemplateCustomList, ManagementTemplateActivity.this);
		lvCustemplate.setAdapter(adapterForCustom);
						
		lvCustemplate.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {					
				//String mt_id = manuscriptTemplateCustomList.get(position-1).getMt_id();
				String mt_id = manuscriptTemplateCustomList.get(position).getMt_id();
				Intent mIntent = new Intent(ManagementTemplateActivity.this,EditTemplateActivity.class);
				Bundle mBundle= new Bundle();
				mBundle.putString("mtId", mt_id);
				mBundle.putInt("switcherUsing", View.VISIBLE);
				mBundle.putString("requestType", "Adopt");
				mIntent.putExtras(mBundle);
				setResult(Activity.RESULT_OK, mIntent); //���÷������
				finish();
			}
		});	
	}
	
	/**
	 * �ڽ���ģ����?�༭���鿴ʱ�ô˷�����ʼ��ListView
	 */
	private void initializeListViewForEdit(){
		//ȡ��ϵͳ��ǩ
		manuscriptTemplateList=manuscriptTemplateService.getManuscriptTemplateSystemList(currentUserName,TemplateType.NORMAL.toString());
		
		adapter=new ManagementTemplateAdapter(manuscriptTemplateList, ManagementTemplateActivity.this);
		lvSystemplate.setAdapter(adapter);
		lvSystemplate.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
				//��Ϊ����ListView ��Header Ҳռһ�У�����postion��Ҫ��һ
				//String mt_id = manuscriptTemplateList.get(position-1).getMt_id();
				String mt_id = manuscriptTemplateList.get(position).getMt_id();
				Intent mIntent = new Intent(ManagementTemplateActivity.this,EditTemplateActivity.class);
				Bundle mBundle= new Bundle();
				mBundle.putString("mtId", mt_id);
				mBundle.putInt("switcherUsing", View.INVISIBLE);
				mBundle.putString("requestType", "Manage");
				mIntent.putExtras(mBundle);
				startActivityForResult(mIntent,REQUEST_SYSTEM_EDITTEMPLATE);
			}
		});
						
		//ȡ���Զ����ǩ
		manuscriptTemplateCustomList=manuscriptTemplateService.getManuscriptTemplateCustomList(currentUserName,TemplateType.NORMAL.toString());
		adapterForCustom=new ManagementTemplateAdapter(manuscriptTemplateCustomList, ManagementTemplateActivity.this);
		lvCustemplate.setAdapter(adapterForCustom);
				
		lvCustemplate.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {					
				//String mt_id = manuscriptTemplateCustomList.get(position-1).getMt_id();
				String mt_id = manuscriptTemplateCustomList.get(position).getMt_id();
				Intent mIntent = new Intent(ManagementTemplateActivity.this,EditTemplateActivity.class);
				Bundle mBundle= new Bundle();
				mBundle.putString("mtId", mt_id);
				mBundle.putInt("switcherUsing", View.INVISIBLE);
				mBundle.putString("requestType", "Manage");
				mIntent.putExtras(mBundle);
				startActivityForResult(mIntent,REQUEST_CUSTOM_EDITTEMPLATE);
			}
		});
			
		//��ListViewע��Context Menu����ϵͳ��⵽�û�����ĳ��Ԫʱ������Context Menu����
		registerForContextMenu(lvCustemplate);
	}
	
	/**
	 * ��ʼ��ListView
	 */
	private void initializeListView()
	{		
		if(this.requestType.equals("Adopt")){		//������������ø�ǩ���ô�ҳ�棬����Ҫ����������ť
			this.initializeListViewForSelect();
		}
		else{
			this.initializeListViewForEdit();
		}
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		menu.add(Menu.NONE,CONTEXT_CUSTOM_DELETE_ITEM,Menu.NONE,R.string.delete_MTA);
		menu.add(Menu.NONE,CONTEXT_CUSTOM_SET_ITEM_DEFAULT,Menu.NONE,R.string.set_as_default_MTA);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {	
			Bundle mBundle = data.getExtras();
			switch(requestCode){
				case REQUEST_CUSTOM_NEWTEMPLATE:			//�����ģ�巵��
					initializeListViewForEdit();
					adapterForCustom.updateList(manuscriptTemplateService.getManuscriptTemplateCustomList(currentUserName, TemplateType.NORMAL.toString()));
        			adapterForCustom.notifyDataSetChanged();
					break;
				case REQUEST_SYSTEM_EDITTEMPLATE:				//�޸�ϵͳģ�巵��		
					initializeListViewForEdit();
        			adapter.updateList(manuscriptTemplateService.getManuscriptTemplateSystemList(currentUserName, TemplateType.NORMAL.toString()));
        			adapter.notifyDataSetChanged();
        			adapterForCustom.updateList(manuscriptTemplateService.getManuscriptTemplateCustomList(currentUserName, TemplateType.NORMAL.toString()));
        			adapterForCustom.notifyDataSetChanged();
					break;
				case REQUEST_CUSTOM_EDITTEMPLATE:				//�޸��û�ģ�巵��
					initializeListViewForEdit();
					adapterForCustom.updateList(manuscriptTemplateService.getManuscriptTemplateCustomList(currentUserName, TemplateType.NORMAL.toString()));
        			adapterForCustom.notifyDataSetChanged();
        			break;
				default:				
					break;
			}
		}
	}
	
	/**
	 * ҳ��ؼ�����¼���Ӧ
	 */
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.iBtnBack_MTA:
			setResult(RESULT_CANCELED);
			finish();
			break;
		default:
			break;
		}		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// KeyEvent.KEYCODE_BACK��?�ز���.
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(RESULT_CANCELED);
			finish();
		}
		return true;
	}	
	
	/**
	 * 
	 */
	private void deleteManuscriptTemplate(final long rowId){
		if(rowId>=0){
			//TODO ��ʻ�
            new AlertDialog.Builder(this).setTitle(R.string.delete_confirm_title_MTA)
            											.setPositiveButton(R.string.confirm_button, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                	ManuscriptTemplate mtItem = (ManuscriptTemplate) adapterForCustom.getItem((int)rowId);
                	if(mtItem!=null){
                		if(manuscriptTemplateService.deleteManuscriptTemplate(mtItem.getMt_id())){              			
                			adapterForCustom.deleteItem((int)rowId);
                			sharedPreferencesHelper.SaveUserPreferenceSettings(PreferenceKeys.User_DefaultTemplate.toString(), PreferenceType.String, "");
//                			List<ManuscriptTemplate> tempList = manuscriptTemplateService.getManuscriptTemplateCustomList(currentUserName, TemplateType.NORMAL.toString());
//                			if(tempList==null){
//                				tempList = new ArrayList<ManuscriptTemplate>();
//                			}
//                			adapterForCustom.updateList(tempList);
                			adapterForCustom.notifyDataSetChanged();
                		}  
                	}
                }
            })
            .setNegativeButton(R.string.cancel_button, null)
            .show();
        }
		
	}
	
	private void setManuscriptTemplateAsDefault(final long rowId){
		if(rowId>=0){
			//TODO ��ʻ�
            new AlertDialog.Builder(this).setTitle(R.string.set_as_default_confirm_title_MTA)
            											.setPositiveButton(R.string.confirm_button, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                	ManuscriptTemplate mtItem = (ManuscriptTemplate) adapterForCustom.getItem((int)rowId);
                	if(mtItem!=null){
                		if(manuscriptTemplateService.setTemplateAsDefault(mtItem)){
                			sharedPreferencesHelper.SaveUserPreferenceSettings(PreferenceKeys.User_DefaultTemplate.toString(), PreferenceType.String, mtItem.getMt_id());
                			adapterForCustom.updateList(manuscriptTemplateService.getManuscriptTemplateCustomList(currentUserName, TemplateType.NORMAL.toString()));
                			adapterForCustom.notifyDataSetChanged();
                		}  
                	}
                }
            })
            .setNegativeButton(R.string.cancel_button, null)
            .show();
        }
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		//���� AdapterView.AdapterContextMenuInfo����ȡ��Ԫ����Ϣ��
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        
		switch(item.getItemId()){		
		case CONTEXT_CUSTOM_DELETE_ITEM:
         		deleteManuscriptTemplate(info.id);			//info.id����ListView��������RowId
            	break;
         	case CONTEXT_CUSTOM_SET_ITEM_DEFAULT:
         		setManuscriptTemplateAsDefault(info.id);
         		break;
         	default:
         		break;
         }
         return super.onContextItemSelected(item); 
	}
}
