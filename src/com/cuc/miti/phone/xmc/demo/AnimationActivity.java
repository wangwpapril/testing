package com.cuc.miti.phone.xmc.demo;

import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.domain.Enums.AccessoryType;
import com.cuc.miti.phone.xmc.logic.ManuscriptTemplateService;
import com.cuc.miti.phone.xmc.ui.LoginActivity;
import com.cuc.miti.phone.xmc.ui.SystemConfigActivity;
import com.cuc.miti.phone.xmc.utils.StandardizationDataHelper;
import com.cuc.miti.phone.xmc.utils.ToastHelper;
import com.cuc.miti.phone.xmc.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AnimationActivity extends Activity{

	private ViewPager mPager;			//ҳ������
	private ImageView cursor;			// ����ͼƬ
	private ImageButton newManuBtn;	//�½����
	private ImageButton systemSettingBtn;		//ϵͳ����
	private ImageButton messageBtn;		//�鿴��Ϣ
	private TextView tv1,tv2; 				// ҳ��ͷ��
	private List<View> listViews;		// Tabҳ���б�
	private int offset = 0; 					// ����ͼƬƫ����
	private int currIndex = 0;				// ��ǰҳ�����
	private int bmpW = 0;					// ����ͼƬ���
	private int screenW = 0 ;				//��Ļ���
	
	ManuscriptTemplateService mtServices = new ManuscriptTemplateService(AnimationActivity.this);
	
	/**
	 * ��ʼ��
	 */
	private void SetUpViews(){
		newManuBtn = (ImageButton)findViewById(R.id.newManuscript);
		systemSettingBtn = (ImageButton)findViewById(R.id.configBtn);
		messageBtn = (ImageButton)findViewById(R.id.messageBtn);
		mPager = (ViewPager)findViewById(R.id.vPager);
		cursor = (ImageView)findViewById(R.id.cursor);
		tv1 = (TextView)findViewById(R.id.tvManuMan);
		tv2 = (TextView)findViewById(R.id.tvFaster);
	}
	
	private void InitViewPager(){
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();
		listViews.add(mInflater.inflate(R.layout.list_manuscripts, null));
		listViews.add(mInflater.inflate(R.layout.faster_manuscripts, null));
//		mPager.setAdapter(new ViewPagerAdapter(listViews));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	
	/**
	 * ҳ���л�����
	 * @author SongQing
	 *
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			Animation animation = null;
			switch (arg0) {
				case 0:
					if (currIndex == 1) {
						animation = new TranslateAnimation(screenW/2, 0, 0, 0);
					 } 
					break;
				case 1:
					if (currIndex == 0) {
						animation = new TranslateAnimation(0, screenW/2, 0, 0);
					} 
					break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}
		
	}
	/**
	 * ��ʼ������
	 */
	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a).getWidth();// ��ȡͼƬ���
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenW = dm.widthPixels;// ��ȡ�ֱ��ʿ��
		offset = (screenW / 2 - bmpW)/2;// ����ƫ����
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// ���ö�����ʼλ��
		}
	
	private void SetUpOnClickListener(){
		newManuBtn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*Toast toast = Toast.makeText(AnimationActivity.this, StandardizationDataHelper.getAccessoryFileUploadStorePath(AccessoryType.Cache),Toast.LENGTH_LONG);
				toast.show();*/
				ToastHelper.showToast(StandardizationDataHelper.getAccessoryFileUploadStorePath(AccessoryType.Cache),Toast.LENGTH_SHORT);
			}
		});	
		
		systemSettingBtn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AnimationActivity.this,SystemConfigActivity.class);				
				startActivity(intent);
			}
		});
		
		messageBtn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mtServices.TestAdd();
			}
		});
		
		tv1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mPager.setCurrentItem(1);
			}
		});
		
		tv2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mPager.setCurrentItem(0);
			}
		});
		
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.animation);
		SetUpViews();
		InitViewPager();
		SetUpOnClickListener();
		InitImageView();		
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
