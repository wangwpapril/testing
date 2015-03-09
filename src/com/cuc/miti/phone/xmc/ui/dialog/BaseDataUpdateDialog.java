package com.cuc.miti.phone.xmc.ui.dialog;

import com.cuc.miti.phone.xmc.R;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BaseDataUpdateDialog extends Dialog{
	
	public TextView progTextView,fileNameTextView;
	public ProgressBar progressBar;

	public BaseDataUpdateDialog(Activity context){
		super(context);		
		init();
	}
	
	public BaseDataUpdateDialog(Activity context,int theme){
		super(context,theme);
		init();
	}
	
	public BaseDataUpdateDialog(Activity context,boolean cancelable,OnCancelListener cancelListener){
		super(context,cancelable,cancelListener);
		init();
	}
	
	private void init(){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.xml_update_dialog);
		
		progTextView=(TextView)findViewById(R.id.textViewProg_XMLUDIA);
		progressBar=(ProgressBar)findViewById(R.id.ProgressBarProg_XMLUDIA);
		fileNameTextView =(TextView)findViewById(R.id.textViewFileName_XMLUDIA);
	}
}
