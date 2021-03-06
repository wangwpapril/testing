package com.cuc.miti.phone.xmc.ui;

 

import com.cuc.miti.phone.xmc.R;
import com.cuc.miti.phone.xmc.adapter.MyAdapter;
import com.cuc.miti.phone.xmc.ui.control.WrapSlidingDrawer;
import com.cuc.miti.phone.xmc.ui.control.WrappingSlidingDrawer;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
@SuppressWarnings("deprecation")
public class SlidingdrawerActivity extends BaseActivity {
      
      private GridView gridview;
      private WrappingSlidingDrawer slidingdrawer;
      private ImageView imageview;
      private int[] icons={R.drawable.editingmanuscripts_button,R.drawable.editingmanuscripts_button,
                            R.drawable.editingmanuscripts_button,R.drawable.editingmanuscripts_button,
                            R.drawable.editingmanuscripts_button,R.drawable.editingmanuscripts_button,
                            R.drawable.editingmanuscripts_button,R.drawable.editingmanuscripts_button,R.drawable.editingmanuscripts_button};
      private String[] items={"华仔","发哥","雅芝","柏芝","周星星","jindegege","老毛","老毕","涵涵"};
          
        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main2);
            gridview = (GridView)findViewById(R.id.gridview);
            slidingdrawer = (WrappingSlidingDrawer)findViewById(R.id.sd);
            imageview=(ImageView)findViewById(R.id.imageview);
            MyAdapter adapter=new MyAdapter(this,items,icons);//通过构造函数实例化一个MyAdapter对象，这个MyAdapter对象必须继承BaseAdapter类
            gridview.setAdapter(adapter);
            slidingdrawer.setOnDrawerOpenListener(new WrappingSlidingDrawer.OnDrawerOpenListener()//打开抽屉
            {
              @Override
              public void onDrawerOpened()
              {
                  imageview.setImageResource(R.drawable.manu_template_logo);//打开抽屉事件
              }
            });
            slidingdrawer.setOnDrawerCloseListener(new WrappingSlidingDrawer.OnDrawerCloseListener()
            {
              @Override
              public void onDrawerClosed()
              {
                  imageview.setImageResource(R.drawable.ic_launcher);//关闭抽屉事件
              }
            });
        }
    }