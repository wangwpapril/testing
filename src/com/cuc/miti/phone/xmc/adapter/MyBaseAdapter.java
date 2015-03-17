package com.cuc.miti.phone.xmc.adapter;

import com.cuc.miti.phone.xmc.R;
import com.cuc.miti.phone.xmc.ui.BaseActivity1;
import com.cuc.miti.phone.xmc.utils.ImageLoader;

import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;


/** 
 *
 * @author wangyanpeng 
 */

public abstract class MyBaseAdapter extends BaseAdapter {

	protected BaseActivity1 context;
	protected LayoutInflater layoutInflater;
//	protected AsyncImageLoader imageLoader;
	protected ImageLoader ImageLoader;
	protected void init(){
		layoutInflater = LayoutInflater.from(context);
//		imageLoader = new AsyncImageLoader(context);
		if (ImageLoader == null) {
			ImageLoader = new ImageLoader(context, R.drawable.ic_launcher);
		}
	}
	

//	protected void getGoodDetail(String goodId){
//		if (!UserControler.isLogin()) {
//			ToastUtil.show(context, "请先登录", true);
//					CommonMethod.openLogin(context);
//			return;
//		}
/*		RequestGoodDetail rgd = new RequestGoodDetail();
		rgd.goodsId = goodId;
		new GoodDetailControler(context).getGoodDetail(rgd, new IControlerContentCallback() {
			public void handleSuccess(String content){
				try {
//					String result = context.handleContent(content);
					ResponseGoodDetail rgd = ResponseGoodDetail
							.parseJson(content);
					Intent intent = new Intent(context, ActivityGoodDetail.class).
				              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra(IntentKeys.KEY_GOOD_DETAIL, rgd);
					context.startActivity(intent);
				} catch (final KnownException ke) {
					CommonMethod.handleKnownException(context, ke, false);
				} catch (Exception e) {
					e.printStackTrace();
					CommonMethod.handleException(context,e);
				}
			}

			public void handleError(Exception e){
				CommonMethod.handleException(context,e);
			}
		});
	}
*/
}
