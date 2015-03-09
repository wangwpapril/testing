package com.cuc.miti.phone.xmc.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.cuc.miti.phone.xmc.R;
import com.cuc.miti.phone.xmc.adapter.FtpUsernameAdapter;
import com.cuc.miti.phone.xmc.domain.Enums.AccessoryType;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.utils.StandardizationDataHelper;
import com.cuc.miti.phone.xmc.utils.ToastHelper;
import com.cuc.miti.phone.xmc.utils.XMLDataHandle;

public class FtpUsernameActivity extends BaseActivity implements OnClickListener{
	
	private Button addftpname;
	private ImageButton iBtnBack;
	private ListView listView;
	private EditText edaddftpindenti,edaddftpusername; 
	private String etindentification="",etftpusername="";
	List<KeyValueData> objs = new ArrayList<KeyValueData>();
	List<KeyValueData> objs1 =null; 
	private FtpUsernameAdapter adapter;
	
	String fileName = "ftpusername";
	private static final int CONTEXT_CUSTOM_DELETEALL_ITEM_MSG= 11;//ɾ����Ϣ		
	private KeyValueData mItem = null;
	InputStream is = null;
	int state = 0;//0��ʾ�������1��ʾ�༭״̬
	int position = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.ftp_username_choose);
		this.initialize();
	}
	
	private void initialize(){
	    addftpname = (Button)findViewById(R.id.addftpname);
	    addftpname.setOnClickListener(this);
	    iBtnBack = (ImageButton)findViewById(R.id.iBtnBack_ftpusername);
	    iBtnBack.setOnClickListener(this);
	    listView = (ListView)findViewById(R.id.ftpnameListView);
	    
	    edaddftpindenti = (EditText)findViewById(R.id.edaddftpindenti);
	    edaddftpusername = (EditText)findViewById(R.id.edaddftpusername);
	    openXML();
	}
	
	private void writeXML(){
		try {
			XMLDataHandle.Serializer("ftpusername",objs);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void openXML(){
    	String path = StandardizationDataHelper.getAccessoryFileUploadStorePath(AccessoryType.Cache);
    	File dataFile = null;
		dataFile = new File(path + "/" + fileName + ".xml");
		if(!dataFile.exists()){
			return ;
		}
		try {
			is = new FileInputStream(dataFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

//		List<KeyValueData> kvList = null;
		try {
			objs = XMLDataHandle.parserFtpUsername(is);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
	    	adapter = new FtpUsernameAdapter(this,objs);
	    	listView.setAdapter(adapter);
	    	
		    listView.setOnItemClickListener(new OnItemClickListener(){

				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					// TODO Auto-generated method stub
					state = 1;
					KeyValueData data = objs.get(arg2);
					position = arg2;
					edaddftpindenti.setText(data.getKey().toString());
					edaddftpusername.setText(data.getValue().toString());
					
				}
		    	
		    });
	    	
			//��ListViewע��Context Menu����ϵͳ��⵽�û�����ĳ��Ԫʱ������Context Menu����
			registerForContextMenu(listView);
	}
	

	public void onClick(View arg0) {
		switch(arg0.getId()){
			case R.id.addftpname:
				etindentification = edaddftpindenti.getText().toString().trim();
				etftpusername = edaddftpusername.getText().toString().trim();
				
//				for(int i=0;i<objs.size();i++){
//					KeyValueData keyvalue= objs.get(i);etindentification!= keyvalue.getKey().toString() && etftpusername!= keyvalue.getValue().toString()&&
				if(state==0){	
				if(!etindentification.equals("")&&!etftpusername.equals("")){//����Ϊ��
				  for(KeyValueData kv : objs){
					  if(kv.getKey().equals(etindentification)|| kv.getValue().equals(etftpusername)){
						  ToastHelper.showToast(ToastHelper.getStringFromResources(R.string.FtpCodeSignExist), Toast.LENGTH_SHORT);
						  return;
					  }
				  }
				    
					KeyValueData addone = new KeyValueData(etindentification,etftpusername);
					objs.add(addone);
					try {
						this.writeXML();
						this.openXML();						
					} catch (Exception e) {
						e.printStackTrace();
					}
					edaddftpindenti.setText("");
					edaddftpusername.setText("");
					edaddftpindenti.requestFocus();
				}else{
					ToastHelper.showToast(ToastHelper.getStringFromResources(R.string.FtpCodeSignInputAgain), Toast.LENGTH_SHORT);
				}
				}else if(state==1){
					
					objs.remove(position);
					try {
						XMLDataHandle.Serializer("ftpusername",objs);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					KeyValueData addone = new KeyValueData(etindentification,etftpusername);
					objs.add(addone);
					try {
						this.writeXML();
						this.openXML();
					} catch (Exception e) {
						e.printStackTrace();
					}
					state= 0;
					edaddftpindenti.setText("");
					edaddftpusername.setText("");
				}				
				break;
			case R.id.iBtnBack_ftpusername:
				setResult(RESULT_OK);
				this.finish();
				break;
			default:
				break;
		}
		
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
			 menu.add(Menu.NONE,CONTEXT_CUSTOM_DELETEALL_ITEM_MSG,Menu.NONE,ToastHelper.getStringFromResources(R.string.delete_MTA));	
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		 AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();	        
			switch(item.getItemId()){	
	         	case CONTEXT_CUSTOM_DELETEALL_ITEM_MSG:   //ɾ����Ϣ
	         		deleteMessageAll(info.id);			//info.id����ListView��������RowId	   

	            	break;	         		
	         	default:
	         		break;
	         }
	         return super.onContextItemSelected(item); 
	}

	private void deleteMessageAll(final long rowId) {
		if(rowId>=0){
			//TODO ��ʻ�
            new AlertDialog.Builder(this).setTitle(R.string.delete_confirm_title_MTA).setPositiveButton(R.string.confirm_button, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                	mItem = (KeyValueData) adapter.getItem((int)rowId);
                	if(mItem!=null){
                		try {
							adapter.deleteItem((int)rowId);
						} catch (Exception e) {
							e.printStackTrace();
						} 
    					openXML();
            		}	
                }
            })
            .setNegativeButton(R.string.cancel_button, null)
            .show();
        }
	}	
}
