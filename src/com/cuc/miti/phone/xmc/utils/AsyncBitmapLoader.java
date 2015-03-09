package com.cuc.miti.phone.xmc.utils;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import com.cuc.miti.phone.xmc.domain.Manuscripts;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class AsyncBitmapLoader {

	/** 
	     * �ڴ�ͼƬ�����û��� 
	     */  
	    //private HashMap<String, SoftReference<Bitmap>> imageCache = null;  
	      
	    public AsyncBitmapLoader()  
	    {  
	        //imageCache = new HashMap<String, SoftReference<Bitmap>>();  
	    }  
	      
	    public Bitmap loadBitmap(final Manuscripts mu, final ImageView imageView, final String m_id, final Context context, final ImageCallBack imageCallBack)  
	    {  
	          
	        final Handler handler = new Handler()  
	        {  
	            /* (non-Javadoc) 
	             * @see android.os.Handler#handleMessage(android.os.Message) 
	             */  
	            @Override  
	            public void handleMessage(Message msg)  
	            {  
	                // TODO Auto-generated method stub  
	                imageCallBack.imageLoad(imageView, (Bitmap)msg.obj);  
	            }  
	        };  
	          
	        //������ڴ滺���У�Ҳ���ڱ��أ���jvm���յ����������߳�����ͼƬ  
	        new Thread()  
	        {  
	            /* (non-Javadoc) 
	             * @see java.lang.Thread#run() 
	             */  
	            @Override  
	            public void run()  
	            {  
	                // TODO Auto-generated method stub  
//	                InputStream bitmapIs = HttpUtils.getStreamFromURL(imageURL);  
	                  
	                Bitmap bitmap = MediaHelper.getManuscriptPreview(m_id, context);
	                mu.setPreViewImage(bitmap);
	                Message msg = handler.obtainMessage(0, bitmap);  
	                handler.sendMessage(msg);
	            }  
	        }.start();  
	          
	        return null;  
	    }  
	      
	    /** 
	     * �ص��ӿ� 
	     * @author onerain 
	     * 
	     */  
	    public interface ImageCallBack  
	    {  
	        public void imageLoad(ImageView imageView, Bitmap bitmap);  
	    }  

}
