package com.cuc.miti.phone.xmc.utils;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Manuscripts;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

public class SyncImageLoader {
	private Object lock = new Object();

	private boolean mAllowLoad = true;

	private boolean firstLoad = true;

	private int mStartLoadLimit = 0;

	private int mStopLoadLimit = 0;

	private Context context = null;

	final Handler handler = new Handler();

	public interface OnImageLoadListener {
		public void onImageLoad(int position, Bitmap bitmap, ImageView imageView);
	}

	public SyncImageLoader(Context context) {
		this.context = context;
	}

	public void setLoadLimit(int startLoadLimit, int stopLoadLimit) {
		if (startLoadLimit > stopLoadLimit) {
			return;
		}
		mStartLoadLimit = startLoadLimit;
		mStopLoadLimit = stopLoadLimit;
	}

	public void restore() {
		mAllowLoad = true;
		firstLoad = true;
	}

	public void lock() {
		mAllowLoad = false;
		firstLoad = false;
	}

	public void unlock() {
		mAllowLoad = true;
		synchronized (lock) {
			lock.notifyAll();
		}
	}

	public void loadImage(int t, final Manuscripts mu, OnImageLoadListener listener,
			ImageView imageView) {
		final OnImageLoadListener mListener = listener;
		final String manuscriptId = mu.getM_id();
		final int mt = t;
		final ImageView mImageView = imageView;

		new Thread(new Runnable() {

			public void run() {
				if (!mAllowLoad) {
					Log.i(IngleApplication.TAG,
							"prepare to load");
					synchronized (lock) {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

				if (mAllowLoad && firstLoad) {
					loadImage(mu, mListener, mImageView, mt);
				}

				else if (mAllowLoad && mt >= mStartLoadLimit
						&& mt <= mStopLoadLimit) {
					loadImage(mu, mListener, mImageView, mt);
				}
			}

		}).start();
	}

	private void loadImage(final Manuscripts mu,
			final OnImageLoadListener mListener, final ImageView imageView, final int position) {

		try {
			
			Bitmap tempBitmap = mu.getPreViewImage();
			
			final Bitmap bitmap = tempBitmap == null ? MediaHelper.getManuscriptPreview(mu.getM_id(),
					context) : tempBitmap;
			
			handler.post(new Runnable() {
				public void run() {
					if (mAllowLoad) {
						mListener.onImageLoad(position, bitmap, imageView);
					}
				}
			});
		} catch (Exception e) {
			Logger.e(e);
		}
	}

}
