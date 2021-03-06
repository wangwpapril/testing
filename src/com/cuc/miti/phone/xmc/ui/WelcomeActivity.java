package com.cuc.miti.phone.xmc.ui;

import java.util.LinkedList;

import com.cuc.miti.phone.xmc.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.VideoView;


public class WelcomeActivity extends BaseActivity implements OnClickListener,OnTouchListener,GestureDetector.OnGestureListener{
	private final String FLAG_EXIST = "flag_exist";
	
    GestureDetector mGesture = null; 
	private int verticalMinDistance = 100;
	private int minVelocity = 0;

	
	private ImageView mFirstSpot;
	private ImageView mSecondSpot;
	private ImageView mThirdSpot;
	private ImageView mFourthSpot;
	private ImageView mStartButton;
	private VideoView mVideoView;
	private ViewPager mViewPager;
	private LinkedList<ImageView> mSpotViewList = new LinkedList<ImageView>(); 
	private LinkedList<View> mGuideViewList = new LinkedList<View>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createUI();		
        mGesture = new GestureDetector(this,this); 
        mGesture.setIsLongpressEnabled(false); 

/*		String cid = PreferenceUtils.readStrConfig(Constant.PreferKeys.KEY_GETUI_CLIENTID, getBaseContext(), "0");
		if (!mWebApi.isLogined()) {
			cid = "";
		}
		mWebApi.postClientID(cid, null);
*/		
	}

	void createUI() {
		setContentView(R.layout.welcome);
		mFirstSpot = (ImageView) findViewById(R.id.spot_one);
		mFirstSpot.setImageResource(R.drawable.guide_spot_selected);
		mFirstSpot.setScaleType(ScaleType.CENTER);
		
		mSecondSpot = (ImageView) findViewById(R.id.spot_two);
		mSecondSpot.setImageResource(R.drawable.guide_spot_unselected);
		mSecondSpot.setScaleType(ScaleType.CENTER);
		
		mThirdSpot = (ImageView) findViewById(R.id.spot_three);
		mThirdSpot.setImageResource(R.drawable.guide_spot_unselected);
		mThirdSpot.setScaleType(ScaleType.CENTER);
		
		mFourthSpot = (ImageView) findViewById(R.id.spot_four);
		mFourthSpot.setImageResource(R.drawable.guide_spot_unselected);
		mFourthSpot.setScaleType(ScaleType.CENTER);
		
		mSpotViewList.add(mFirstSpot);
		mSpotViewList.add(mSecondSpot);
		mSpotViewList.add(mThirdSpot);
		mSpotViewList.add(mFourthSpot);
		
		DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
		int screenHeight = displayMetrics.heightPixels;
//		int screenWidth = displayMetrics.widthPixels;
		
		int videoHeight = screenHeight / 2;
		//====================================================================================================
		RelativeLayout guide_first = new RelativeLayout(this);
		guide_first.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		ImageView guide_first_image = new ImageView(this);
		guide_first_image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		Bitmap b = BitmapFactory.decodeResource(this.getResources(), R.drawable.add_new_trip);
		guide_first_image.setImageBitmap(b);
		guide_first_image.setScaleType(ScaleType.FIT_XY);
		guide_first.addView(guide_first_image);
		
/*		mVideoView = new VideoView(this);
		android.widget.RelativeLayout.LayoutParams videoViewParams = new android.widget.RelativeLayout.LayoutParams(videoHeight, videoHeight);
		videoViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		mVideoView.setLayoutParams(videoViewParams);
//		mVideoView.setVideoURI(Uri.parse("android.resource://"+this.getPackageName()+"/"+R.raw.guide_video));
		mVideoView.setOnCompletionListener(mCompletionListener);
		guide_first.addView(mVideoView);
		mVideoView.start();
		
		mStartButton = new ImageView(this);
		android.widget.RelativeLayout.LayoutParams startButtonParams = new android.widget.RelativeLayout.LayoutParams(videoHeight, videoHeight);
		startButtonParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		mStartButton.setLayoutParams(startButtonParams);
//		mStartButton.setImageResource(R.drawable.button_play_bg);
		mStartButton.setImageBitmap(null);
		mStartButton.setScaleType(ScaleType.CENTER);
		mStartButton.setOnClickListener(mStartPlayListener);
		
		guide_first.addView(mStartButton);
*/		
		mGuideViewList.add(guide_first);
		//====================================================================================================
		ImageView guide_second = new ImageView(this);
		guide_second.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		b = BitmapFactory.decodeResource(this.getResources(), R.drawable.add_new_trip);
		guide_second.setImageBitmap(b);
		guide_second.setScaleType(ScaleType.FIT_XY);
		
		mGuideViewList.add(guide_second);
		//====================================================================================================
		ImageView guide_third = new ImageView(this);
		guide_third.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		b = BitmapFactory.decodeResource(this.getResources(), R.drawable.add_new_trip);
		guide_third.setImageBitmap(b);
		guide_third.setScaleType(ScaleType.FIT_XY);
		
		mGuideViewList.add(guide_third);
		//====================================================================================================
		RelativeLayout guide_fourth = new RelativeLayout(this);
		guide_first.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		ImageView guide_fourth_image = new ImageView(this);
		guide_fourth_image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		b = BitmapFactory.decodeResource(this.getResources(), R.drawable.add_new_trip);
		guide_fourth_image.setImageBitmap(b);
		guide_fourth_image.setScaleType(ScaleType.FIT_XY);
		guide_fourth_image.setOnClickListener(mStartAppListener);
		guide_fourth.addView(guide_fourth_image);
		
/*		ImageView startApp = new ImageView(this);
//		int startAppWidth = screenWidth / 2;
		int startAppHeight = screenHeight / 8;
		android.widget.RelativeLayout.LayoutParams startAppParams = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				startAppHeight);
		startAppParams.bottomMargin = startAppHeight;
		startAppParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		startAppParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		startApp.setLayoutParams(startAppParams);
		b = BitmapFactory.decodeResource(this.getResources(), R.drawable.add_new_trip);
		startApp.setImageBitmap(b);
 		startApp.setOnClickListener(mStartAppListener);
		guide_fourth.addView(startApp);
*/		
		mGuideViewList.add(guide_fourth);
		//====================================================================================================
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setAdapter(new MyPagerAdapter());
		mViewPager.setOnPageChangeListener(mPageChangeListener);
	}
	
	private OnClickListener mStartAppListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
//			Intent intent = new Intent(getBaseContext(), SlidingdrawerActivity.class);
			Intent intent = new Intent(getBaseContext(), DraweringActivity.class);			
			intent.putExtras(getIntent());
			startActivity(intent);
			finish();
		}
	};
	
	private OnClickListener mStartPlayListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			if (mVideoView.isPlaying()) {
				mVideoView.pause();
//				mStartButton.setImageResource(R.drawable.button_play_bg);
			} else {
				mVideoView.start();
				mStartButton.setImageBitmap(null);
			}
		}
	};
	
	private OnCompletionListener mCompletionListener = new OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer mp) {
//			mStartButton.setImageResource(R.drawable.button_play_bg);
		}
	};
	
	private OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			for(int i = 0 ; i < mSpotViewList.size() ; i ++){
				if(arg0 == i){
					mSpotViewList.get(i).setImageResource(R.drawable.guide_spot_selected);
				}else{
					mSpotViewList.get(i).setImageResource(R.drawable.guide_spot_unselected);
				}
			}
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private void recycle(){
		if(mSpotViewList == null || mSpotViewList.size() == 0){
			return;
		}
		
		for(int i = 0 ; i < mSpotViewList.size() ; i ++){
			ImageView bg = mSpotViewList.get(i);
			if (bg != null) {
				BitmapDrawable bd = (BitmapDrawable) bg.getDrawable();
				bg.setImageDrawable(null);
				if (bd != null) {
					Bitmap b = bd.getBitmap();
					if (b != null && !b.isRecycled()) {
						b.recycle();
						b = null;
					}
				}
			}
		}
	}
	
	protected void onDestroy() {
		recycle();
		super.onDestroy();
	};
	
	class MyPagerAdapter extends PagerAdapter{

		@Override
		public int getCount() {

			return mSpotViewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			
			return arg0 == arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			View view = mGuideViewList.get(position);
			if(!FLAG_EXIST.equals((String)view.getTag())){
				container.addView(view);
				view.setTag(FLAG_EXIST);
			}
				
			return view;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
//			container.removeView(mGuideViewList.get(position));
		}
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		int i = 1;
		i ++;
		return false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return false;
		
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
////////////////////////////////
		int i;
        float x = e2.getX() - e1.getX();
        float y = e2.getY() - e1.getY();
        //限制必须得划过屏幕的1/4才能算划过
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
         float x_limit = dm.widthPixels / 4;
        float y_limit = dm.heightPixels / 4;
        float x_abs = Math.abs(x);
        float y_abs = Math.abs(y);
        if(x_abs >= y_abs){
            //gesture left or right
            if(x > x_limit || x < -x_limit){
                if(x>0){
                    //right
                    i = 1; 
                }else if(x<=0){
                    //left
                	i = 2; 
                }
            }
        }else{
            //gesture down or up
            if(y > y_limit || y < -y_limit){
                if(y>0){
                    //down
                	i = 3;
                }else if(y<=0){
                    //up
                	i = 4;
                }
            }
        }

        ////////////////////////////////
		
		
		
		float e1y =e1.getY();
		float e2y = e2.getY();
		float yy = Math.abs(velocityY);
		
		if (e1.getY() - e2.getY() > verticalMinDistance
				&& Math.abs(velocityY) > minVelocity) { 
			return false;
		} else if (e2.getY() - e1.getY() > verticalMinDistance
				&& Math.abs(velocityY) > minVelocity) { 
			return false;
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		mGesture.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
