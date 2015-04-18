package com.cuc.miti.phone.xmc.ui;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.cuc.miti.phone.xmc.R;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.RenderPriority;

/**
 * 
 * @author wangyanpeng
 */

public class ActivityPayWeb extends BaseActivity1 {
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_web);
		initView();
		try {
			extFolderPermision(getSysFileRoot());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		FileOutputStream outStream;
		this.webView = (WebView) findViewById(R.id.pay_web_webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		webView.getSettings().setRenderPriority(RenderPriority.HIGH);
		// webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

//		DisplayMetrics metrics = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(metrics);
//		int mDensity = metrics.densityDpi;
//		if (mDensity == 120) {
//			webView.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
//		} else if (mDensity == 160) {
//			webView.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
//		} else if (mDensity == 240) {
//			webView.getSettings().setDefaultZoom(ZoomDensity.FAR);
//		}

		int screenDensity = getResources().getDisplayMetrics().densityDpi;
		WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM;
		switch (screenDensity) {
		case DisplayMetrics.DENSITY_LOW:
			zoomDensity = WebSettings.ZoomDensity.CLOSE;
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			zoomDensity = WebSettings.ZoomDensity.MEDIUM;
			break;
		case DisplayMetrics.DENSITY_HIGH:
			zoomDensity = WebSettings.ZoomDensity.FAR;
			break;
		}
		webView.getSettings().setDefaultZoom(zoomDensity);

		webView.setBackgroundColor(Color.LTGRAY);

		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				webView.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

			}

		});
		//
		// webView.setWebChromeClient(new WebChromeClient() {
		// @Override
		// public boolean onJsAlert(WebView view, String url, String message,
		// JsResult result) {
		// result.confirm();
		// return true;
		// }
		//
		// @Override
		// public void onProgressChanged(WebView view, int progress) {
		// }
		// });
//		webView.loadUrl("file:///" + context.getFilesDir().getAbsolutePath()
//				+ "/pay.html");
		webView.loadUrl("https://m.intrepid247.com/weather.html?latitude=37.785834&longitude=-122.406417&country=Andorra&country_code=AND");
	}

	public void extFolderPermision(String filePath) {
		String cmd = "chmod 777 " + filePath;
		try {
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getSysFileRoot() throws Exception {
		return context.getFilesDir().getAbsolutePath();
		// return "/data/data/yseip.boteli/";
	}

	private void initView() {
		super.initTitleView();
	}

	@Override
	protected void initTitle() {
		ivTitleBack.setVisibility(View.VISIBLE);
		ivTitleBack.setOnClickListener(this);
		tvTitleName.setText("支付宝支付");
		ivTitleRight.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		if (v == ivTitleBack) {
			context.finish();
		} 
	}

}
