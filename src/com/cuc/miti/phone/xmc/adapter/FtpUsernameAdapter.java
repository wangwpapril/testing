package com.cuc.miti.phone.xmc.adapter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cuc.miti.phone.xmc.R;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.utils.XMLDataHandle;

public class FtpUsernameAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<KeyValueData> objs= null;
	KeyValueData data = null;
	private TextView tvOfFtpIdentication,tvOfFtpUsername;
	
	
	public FtpUsernameAdapter(Context mContext,List<KeyValueData> objs) {
		this.objs = objs;
		if(objs == null){
			objs = new ArrayList<KeyValueData>();
		}
		mInflater = LayoutInflater.from(mContext);
	}

	public int getCount() {
		return objs.size();
	}

	public Object getItem(int position) {
		return objs.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.ftp_username_listview , null);			
		}
		
		int[] colors = { Color.WHITE, Color.rgb(219, 238, 244) };//RGB��ɫ  
		convertView.setBackgroundColor(colors[position % 2]);// ÿ��item֮����ɫ��ͬ  

		tvOfFtpIdentication = (TextView)convertView.findViewById(R.id.tvOfFtpIdentication);
		tvOfFtpUsername =(TextView)convertView.findViewById(R.id.tvOfFtpUsername);
		
		data = objs.get(position);
		
		tvOfFtpIdentication.setText(data.getKey().toString());
		tvOfFtpUsername.setText(data.getValue().toString());
		
		return convertView;
	}
	
	
	/**
	 * ɾ��ftp�û�
	 * 
	 * @param position
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws IllegalArgumentException 
	 * @throws FileNotFoundException 
	 */
	
	public void deleteItem(int position) throws FileNotFoundException, IllegalArgumentException, IllegalStateException, IOException {
		objs.remove(position);
		XMLDataHandle.Serializer("ftpusername",objs);
	}

}
