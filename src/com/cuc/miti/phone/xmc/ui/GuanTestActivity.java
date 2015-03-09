package com.cuc.miti.phone.xmc.ui;


import java.util.UUID;

import com.cuc.miti.phone.xmc.domain.Enums.OperationType;
import com.cuc.miti.phone.xmc.domain.Enums.XmcBool;
import com.cuc.miti.phone.xmc.domain.ManuscriptTemplate;
import com.cuc.miti.phone.xmc.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GuanTestActivity extends BaseActivity implements OnClickListener{
	
	private Button editTemplateButtongw,manageEditTemplateButtongw,manuEditTemplateButtongw,manuEditNoneTemplateButtongw;
	private ManuscriptTemplate manuTemplate = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guanwei_function_list);		
		this.setUpViews();

	}
	
	private void setUpViews(){	
		editTemplateButtongw = (Button)findViewById(R.id.addemplateButtongw);		
		editTemplateButtongw.setOnClickListener(this);
		manageEditTemplateButtongw = (Button)findViewById(R.id.manageEditTemplateButtongw);		
		manageEditTemplateButtongw.setOnClickListener(this);
		manuEditTemplateButtongw = (Button)findViewById(R.id.manuEditTemplateButtongw);		
		manuEditTemplateButtongw.setOnClickListener(this);	
		manuEditNoneTemplateButtongw = (Button)findViewById(R.id.manuEditTemplateButtongw);		
		manuEditNoneTemplateButtongw.setOnClickListener(this);	
	}


	public void onClick(View v) {
		Intent mIntent;
		Bundle mBundle;
		switch(v.getId()){
		case R.id.addemplateButtongw:
			mIntent= new Intent(GuanTestActivity.this,EditTemplateActivity.class);
			mBundle= new Bundle();
			mBundle.putInt("switcherUsing", View.INVISIBLE);
			mIntent.putExtras(mBundle);
			startActivity(mIntent);
			break;
		case R.id.manageEditTemplateButtongw:
			mIntent = new Intent(GuanTestActivity.this,EditTemplateActivity.class);
			mBundle= new Bundle();
			mIntent.putExtras(mBundle);
			startActivity(mIntent);
			break;
		case R.id.manuEditTemplateButtongw:
			mIntent = new Intent(GuanTestActivity.this,EditTemplateActivity.class);
			mBundle= new Bundle();
			manuTemplate = new ManuscriptTemplate();
			mBundle.putParcelable("manuTemplateInfo", manuTemplate);
			mIntent.putExtras(mBundle);
			startActivityForResult(mIntent, 1);
			break;
		case R.id.manuEditNoneTemplateButtongw:
			mIntent = new Intent(GuanTestActivity.this,EditTemplateActivity.class);
			mBundle= new Bundle();
			mIntent.putExtras(mBundle);
			startActivityForResult(mIntent, 2);
			break;
		default:
			break;
		}		
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
				case 1:	//�и�ǩ��Ϣ�ĸ���༭
					if (mBundle != null) {
						manuTemplate = mBundle.getParcelable("manuScriptInfo");
					}
					break;
				case 2:  //�޸�ǩ��Ϣ�ĸ���༭
					if (mBundle != null) {
						manuTemplate = mBundle.getParcelable("manuScriptInfo");
					}
					break;
				default:
					break;
			}
		}	
	}
	
	
	
}
