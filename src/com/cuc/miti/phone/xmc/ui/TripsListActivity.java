package com.cuc.miti.phone.xmc.ui;

import java.util.List;

import com.cuc.miti.phone.xmc.R;
import com.cuc.miti.phone.xmc.adapter.TripsListAdapter;
import com.cuc.miti.phone.xmc.models.Destination;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;


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
		if(datas == null){
//			getCategoryListTop();
		}else{
			tripsListAdapter = new TripsListAdapter(
					datas, context);
			listView.setAdapter(tripsListAdapter);
		}
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
				Intent i = new Intent();
				i.setClass(context, WelcomeActivity.class);
	//			i.putExtra(IntentKeys.KEY_CATEGROY_SUBS, datas.get(arg2));
				context.startActivity(i);
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
