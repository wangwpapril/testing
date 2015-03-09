package com.cuc.miti.phone.xmc.ui.control;

import java.util.Date;

import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.R;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PullDownListView extends ListView implements OnScrollListener {
	private final static int RELEASE_To_REFRESH = 0;
	private final static int PULL_To_REFRESH = 1;
	private final static int REFRESHING = 2;// ����ˢ��
	private final static int REFRESH_DONE = 3;// ˢ�����
	private final static int LOADING = 4;
	private final int FETCHMORING = 5;
	private final int FETCHMORE_DONE = 6;
	private final int NOMORE = 7;
	private final static int RATIO = 3;
	private LayoutInflater inflater;
	private LinearLayout headView;
	private TextView tipsTextview;
	private TextView lastUpdatedTextView;
	private ImageView arrowImageView;
	private ProgressBar progressBar;

	private LinearLayout footView;
	private TextView textviewMore;
	private ProgressBar progressBarMore;
	private LinearLayout linearLayoutSpliter;

	private int moreState;

	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;
	private boolean isRecored;
	private int headContentWidth;
	private int headContentHeight;
	private int startY;
	private int firstItemIndex;
	private int state;
	private boolean isBack;
	private OnRefreshListener refreshListener;
	private boolean isRefreshable;
	private Context mContext;

	int i = 1;

	public PullDownListView(Context context) {
		super(context);
		init(context);
	}

	public PullDownListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		
		setCacheColorHint(context.getResources().getColor(R.color.transparent));
//		setCacheColorHint(Color.BLACK);
//		setBackgroundColor(Color.BLACK);
		inflater = LayoutInflater.from(context);
		headView = (LinearLayout) inflater.inflate(
				R.layout.scrollover_head, null);
		arrowImageView = (ImageView) headView
				.findViewById(R.id.head_arrowImageView);
		arrowImageView.setMinimumWidth(100);
		arrowImageView.setMinimumHeight(50);
		progressBar = (ProgressBar) headView
				.findViewById(R.id.head_progressBar);
		tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
		lastUpdatedTextView = (TextView) headView
				.findViewById(R.id.head_lastUpdatedTextView);

		measureView(headView);
		headContentHeight = headView.getMeasuredHeight();
		headContentWidth = headView.getMeasuredWidth();
		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		headView.invalidate();
		Log.v("@@@@@@", "width:" + headContentWidth + " height:"
				+ headContentHeight);
		addHeaderView(headView, null, false);
		setOnScrollListener(this);

		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);

		state = REFRESH_DONE;

		footView = (LinearLayout) inflater.inflate(
				R.layout.scrollover_footer, null);
		textviewMore = (TextView) footView.findViewById(R.id.textviewFetchMore);

		textviewMore.setText(R.string.pulldown_footer_more);

		progressBarMore = (ProgressBar) footView.findViewById(R.id.progressBar);
		footView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				moreState = FETCHMORING;

				changeFooterView();

				refreshListener.onMore();
			}
		});

		//addFooterView(footView, null, false);

		linearLayoutSpliter = (LinearLayout) footView
				.findViewById(R.id.linearLayoutSpliter);
		linearLayoutSpliter.setVisibility(View.VISIBLE);

		isRefreshable = false;
	}

	public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2,
			int arg3) {
		firstItemIndex = firstVisiableItem;
	}

	private int ListPos = 0;

	public void onScrollStateChanged(AbsListView arg0, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			ListPos = getFirstVisiblePosition(); // ListPos��¼��ǰ�ɼ��List���˵�һ�е�λ��
		}

	}

	private final Handler mHandler = new Handler();

	private Runnable mScrollTo = new Runnable() {
		public void run() {

			setSelection(ListPos);
		}
	};

	public boolean onTouchEvent(MotionEvent event) {
		if (isRefreshable) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (firstItemIndex == 0 && !isRecored) {
					isRecored = true;
					startY = (int) event.getY();
					Log.v("@@@@@@", "ACTION_DOWN ���ǵ�  " + i++ + "��" + 1);
				}
				break;
			case MotionEvent.ACTION_UP:
				if (state != REFRESHING && state != LOADING) {
					if (state == REFRESH_DONE) {
					}
					if (state == PULL_To_REFRESH) {
						state = REFRESH_DONE;
						Log.v("@@@@@@",
								"ACTION_UP PULL_To_REFRESH and changeHeaderViewByState()"
										+ " ���ǵ�  " + i++ + "��ǰ" + 2);
						changeHeaderViewByState();
						Log.v("@@@@@@",
								"ACTION_UP PULL_To_REFRESH and changeHeaderViewByState() "
										+ "���ǵ�  " + i++ + "����" + 2);
					}
					if (state == RELEASE_To_REFRESH) {
						state = REFRESHING;
						Log.v("@@@@@@",
								"ACTION_UP RELEASE_To_REFRESH changeHeaderViewByState() "
										+ "���ǵ�  " + i++ + "��" + 3);
						changeHeaderViewByState();
						onRefresh();
						Log.v("@@@@@@",
								"ACTION_UP RELEASE_To_REFRESH changeHeaderViewByState()"
										+ " ���ǵ�  " + i++ + "��" + 3);
					}
				}
				isRecored = false;
				isBack = false;
				break;
			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();
				if (!isRecored && firstItemIndex == 0) {
					isRecored = true;
					startY = tempY;
					Log.v("@@@@@@", "ACTION_MOVE ���ǵ�  " + i++ + "��" + 4);
				}
				if (state != REFRESHING && isRecored && state != LOADING) {
					if (state == RELEASE_To_REFRESH) {
						setSelection(0);
						if (((tempY - startY) / RATIO < headContentHeight)
								&& (tempY - startY) > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
							Log.v("@@@@@@", "changeHeaderViewByState() ���ǵ�  "
									+ i++ + "��" + 5);
						} else if (tempY - startY <= 0) {
							state = REFRESH_DONE;
							changeHeaderViewByState();
							Log.v("@@@@@@",
									"ACTION_MOVE RELEASE_To_REFRESH 2  changeHeaderViewByState "
											+ "���ǵ�  " + i++ + "��" + 6);
						}
					}
					if (state == PULL_To_REFRESH) {
						setSelection(0);
						if ((tempY - startY) / RATIO >= headContentHeight) {
							state = RELEASE_To_REFRESH;
							isBack = true;
							Log.v("@@@@@@", "changeHeaderViewByState "
									+ "���ǵ�  " + i++ + "��ǰ" + 7);
							changeHeaderViewByState();
							Log.v("@@@@@@", "changeHeaderViewByState "
									+ "���ǵ�  " + i++ + "����" + 7);
						} else if (tempY - startY <= 0) {
							state = REFRESH_DONE;
							changeHeaderViewByState();
							Log.v("@@@@@@",
									"ACTION_MOVE changeHeaderViewByState PULL_To_REFRESH 2"
											+ " ���ǵ�  " + i++ + "��" + 8);
						}
					}
					if (state == REFRESH_DONE) {
						int top = getFirstVisiblePosition();
						int firstTop = 0;
						if (getChildAt(0) != null)
							firstTop = getChildAt(0).getTop();
						int listPadding = getListPaddingTop();

						if (top <= 0 && firstTop >= listPadding) {
							if (tempY - startY > 0) {
								state = PULL_To_REFRESH;
								Log.v("@@@@@@",
										"ACTION_MOVE DONE changeHeaderViewByState "
												+ "���ǵ�  " + i++ + "��ǰ" + 9);
								changeHeaderViewByState();
								Log.v("@@@@@@",
										"ACTION_MOVE DONE changeHeaderViewByState "
												+ "���ǵ�  " + i++ + "����" + 9);
							}
						}
					}
					if (state == PULL_To_REFRESH) {
						headView.setPadding(0, -1 * headContentHeight
								+ (tempY - startY) / RATIO, 0, 0);
						Log.v("@@@@@@", -1 * headContentHeight
								+ (tempY - startY) / RATIO
								+ "ACTION_MOVE PULL_To_REFRESH 3  ���ǵ�  " + i++
								+ "��" + 10);
					}
					if (state == RELEASE_To_REFRESH) {
						headView.setPadding(0, (tempY - startY) / RATIO
								- headContentHeight, 0, 0);
						Log.v("@@@@@@", "ACTION_MOVE PULL_To_REFRESH 4 ���ǵ�  "
								+ i++ + "��" + 11);
					}
				}
				break;
			}
		}
		return super.onTouchEvent(event);
	}

	private void changeHeaderViewByState() {
		switch (state) {
		case RELEASE_To_REFRESH:
			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(animation);

			tipsTextview.setText(R.string.pulldown_header_up_refresh);

			Log.v("@@@@@@", "RELEASE_To_REFRESH ���ǵ�  " + i++ + "��" + 12
					+ "���ͷ� ˢ��");
			break;
		case PULL_To_REFRESH:
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);
			if (isBack) {
				isBack = false;
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(reverseAnimation);
				// tipsTextview.setText("isBack  is true ������");
			} else {
				// tipsTextview.setText("isBack  is false ������");
			}
			tipsTextview.setText(R.string.pulldown_header_down_refresh);

			Log.v("@@@@@@", "PULL_To_REFRESH ���ǵ�  " + i++ + "��" + 13
					+ "  changeHeaderViewByState()");
			break;
		case REFRESHING:
			headView.setPadding(0, 0, 0, 0);
			progressBar.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);

			tipsTextview.setText(R.string.pulldown_header_refreshing);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			Log.v("@@@@@@", "REFRESHING ���ǵ�  " + i++ + "��"
					+ "���ڼ����� ...REFRESHING");
			break;
		case REFRESH_DONE:
			headView.setPadding(0, -1 * headContentHeight, 0, 0);
			progressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
			arrowImageView.setImageResource(R.drawable.z_arrow_down);

			tipsTextview.setText(R.string.pulldown_header_down_refresh);

			lastUpdatedTextView.setVisibility(View.VISIBLE);
			mHandler.post(mScrollTo);

			// showFooterView();
//			int count = this.getAdapter().getCount();
//			if(count > 8 && getFooterViewsCount() == 0)
//				addFooterView(footView, null, false);
//			else if(count <= 8 && getFooterViewsCount() > 0){
//				removeFooterView(footView);
//			}
			if(getFooterViewsCount() == 0)
			{
				addFooterView(footView, null, false);
				footView.setClickable(true);
			}
			
				
			Log.v("@@@@@@", "DONE ���ǵ�  " + i++ + "��" + "�Ѿ��������- DONE ");
			break;
		}
	}

	private void changeFooterView() {

		switch (moreState) {
		case FETCHMORE_DONE:

			textviewMore.setText(R.string.pulldown_footer_more);

			progressBarMore.setVisibility(INVISIBLE);

			Logger.d("��ǰ״̬��fetch more done");
			break;

		case FETCHMORING:
			progressBarMore.setVisibility(VISIBLE);

			textviewMore.setText(R.string.pulldown_load_more);

			Logger.d("��ǰ״̬��fetch moring");
			break;
			
		case NOMORE:
			
			textviewMore.setText(R.string.oa_details_nomorepage_end);

			progressBarMore.setVisibility(INVISIBLE);
			
			footView.setClickable(false);

			Logger.d("��ǰ״̬��fetch more done");
		}
	}

	public void setonRefreshListener(OnRefreshListener refreshListener) {
		setonRefreshListener(refreshListener, true);
	}
	
	public void setonRefreshListener(OnRefreshListener refreshListener, boolean isRefresh) {
		this.refreshListener = refreshListener;
		isRefreshable = isRefresh;
	}

	public interface OnRefreshListener {
		public void onRefresh();

		public void onMore();

	}
	public void onUpdateComplete()
	{
		textviewMore.setText(R.string.pulldown_footer_more);
		footView.setClickable(true);
		Log.v("@@@@@@", "onUpdateComplete() �����á�����");
	}
	
	public void onRefreshComplete() {
		state = REFRESH_DONE;

		lastUpdatedTextView.setText(mContext.getString(R.string.pulldown_update)	+ new Date().toLocaleString());

		changeHeaderViewByState();
		Log.v("@@@@@@", "onRefreshComplete() �����á�����");
	}

	public void onMoreComplete() {

		moreState = FETCHMORE_DONE;

		changeFooterView();

	}
	public void noMore() {
		
		moreState = NOMORE;
		
		changeFooterView();
	}

	private void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh();
			Log.v("@@@@@@", "onRefresh�����ã����ǵ�  " + i++ + "��");
		}
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	public void setAdapter(BaseAdapter adapter) {
		lastUpdatedTextView.setText(mContext.getString(R.string.pulldown_update) + new Date().toLocaleString() );
		super.setAdapter(adapter);
	}
}