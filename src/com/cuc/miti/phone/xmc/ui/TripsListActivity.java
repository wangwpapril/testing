package com.cuc.miti.phone.xmc.ui;

import java.util.List;

import com.cuc.miti.phone.xmc.R;
import com.cuc.miti.phone.xmc.adapter.TripsListAdapter;
import com.cuc.miti.phone.xmc.models.Destination;
import com.cuc.miti.phone.xmc.models.User1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


/**
 * 分类一级列表
 * 
 * @author wangyanpeng
 */
public class TripsListActivity extends BaseActivity1 {

	protected static final String TAG = "TripsListActivity";
	private ListView listView;
	private List<Destination> datas;
	private TripsListAdapter tripsListAdapter;

//	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState){
//		LogUtil.d(TAG + "  onCreate()....");
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_category);
		initView();
		datas = (List<Destination>) getIntent().getSerializableExtra("destinations");
		User1 user = (User1) getIntent().getSerializableExtra("user");
		showPreviewDialog(user);
		
		if(datas == null){
//			getCategoryListTop();
		}else{
			tripsListAdapter = new TripsListAdapter(
					datas, context);
			listView.setAdapter(tripsListAdapter);
		}
	}
	
	private void showPreviewDialog(User1 user1)
	{
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("Preview User..");
		View view = LayoutInflater.from(context).inflate(R.layout.previewresume, null);
		TextView prevText = (TextView)view.findViewById(R.id.previewResumeText);
		
		String tmptext = "";
		
			tmptext += "User id: " + user1.id + 
					"\n" + "User Name: "+ user1.userName +
					"\n" + "Email: " + user1.email +
					"\n" + "First Name: " + user1.firstName +
					"\n" + "Last Name: " + user1.lastName +
					"\n" + "Token: " + user1.token +
					"\n" + "Last Login At: " + user1.lastLoginAt ;
			tmptext += "\n";		
		
		prevText.setText(tmptext);
		
		builder.setView(view);
		
		builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
			}
		});
		
		Dialog dialog = builder.create();
		dialog.show();
	}


	@Override
	protected void onResume() {
		super.onResume();
//		Common.context = this.getParent();
	}

	@Override
	protected void onPause() {
		super.onPause();
//		Common.context = null;
	}

	private void initView(){
		super.initTitleView();
		listView = (ListView) findViewById(R.id.category_list);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
	//			Intent i = new Intent();
	//			i.setClass(context, WelcomeActivity.class);
	//			i.putExtra(IntentKeys.KEY_CATEGROY_SUBS, datas.get(arg2));
	//			context.startActivity(i);
			}
		});
	}
/*	private void getCategoryListTop(){
		ControlerContentTask cct = new ControlerContentTask(
				RequestUtil.getRequestUrl(URL_SUB.CATEGORY_TOP),
				new IControlerContentCallback() {

					public void handleSuccess(String content){
						try {
							datas = ResponseCategoryTop.parseJson(content);
							categroyTopAdapter = new CategoryTopAdapter(
									datas, context);
							listView.setAdapter(categroyTopAdapter);
						} catch (KnownException ke) {
							CommonMethod.handleKnownException(context, ke, false);
						} catch (Exception e) {
							e.printStackTrace();
							CommonMethod.handleException(context,e);
						}
					}

					public void handleError(Exception e){
						CommonMethod.handleException(context,e);
					}
				}, ConnMethod.POST,false);
		cct.execute();
	}
*/
	@Override
	protected void initTitle(){
		ivTitleBack.setVisibility(View.VISIBLE);
		ivTitleBack.setOnClickListener(this);
		tvTitleName.setText("Trips");
//		ivTitleRight.setVisibility(View.VISIBLE);
//		ivTitleBack.setImageResource(R.drawable.title_left_search);
	}

	@Override
	public void onClick(View v){
		if(v == ivTitleBack){
			finish();
		}else if(v == ivTitleRight){
			
		}
	}

}
