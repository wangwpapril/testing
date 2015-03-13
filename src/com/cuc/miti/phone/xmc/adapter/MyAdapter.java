package com.cuc.miti.phone.xmc.adapter;

 

import com.cuc.miti.phone.xmc.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class MyAdapter extends BaseAdapter
{
  private Context context;
  private String[] items;
  private int[] icons;
 
  public MyAdapter(Context context,String[] items,int[] icons) //构造器
  {
    this.context=context;
    this.items=items;
    this.icons=icons;
  }
 
  @Override
  public int getCount()
  {
    return items.length;
  }
 
  @Override
  public Object getItem(int arg0)
  {
    return items[arg0];
  }
 
  @Override
  public long getItemId(int position)
  {
    return position;
  }
 
  @Override
  public View getView(int position, View convertView, ViewGroup parent)
  {
    LayoutInflater factory = LayoutInflater.from(context);
    View v = (View) factory.inflate(R.layout.item, null);//绑定自定义的layout
    ImageView iv = (ImageView) v.findViewById(R.id.icon);
    TextView tv = (TextView) v.findViewById(R.id.text);
    iv.setImageResource(icons[position]);
    tv.setText(items[position]);
    return v;
  }
 
  
}