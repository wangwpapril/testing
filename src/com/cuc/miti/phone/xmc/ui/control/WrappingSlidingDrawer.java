package com.cuc.miti.phone.xmc.ui.control;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SlidingDrawer;

public class WrappingSlidingDrawer extends SlidingDrawer {

    public WrappingSlidingDrawer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        final String xmlns="http://schemas.android.com/apk/res/android";
        int orientation = attrs.getAttributeIntValue(xmlns, "orientation", ORIENTATION_VERTICAL);
        mTopOffset = attrs.getAttributeIntValue(xmlns, "topOffset", 0);
        mVertical = (orientation == SlidingDrawer.ORIENTATION_VERTICAL);
    }

    public WrappingSlidingDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        
        int attrOrientation = android.R.attr.orientation;
        int attrTopOffset = android.R.attr.topOffset;

        int[] attrIds = new int [] {attrOrientation, attrTopOffset}; 

        TypedArray a = context.obtainStyledAttributes(attrs, attrIds);
        int orientation = a.getInt(0, SlidingDrawer.ORIENTATION_VERTICAL);
        mTopOffset = (int) a.getDimension(1, 0);
        a.recycle(); 

        mVertical = (orientation == SlidingDrawer.ORIENTATION_VERTICAL);
        
 /*       final String xmlns="http://schemas.android.com/apk/res/android";
        int orientation = attrs.getAttributeIntValue(xmlns, "orientation", ORIENTATION_VERTICAL);
        mTopOffset = attrs.getAttributeIntValue(xmlns, "topOffset", 0);
        mVertical = (orientation == SlidingDrawer.ORIENTATION_VERTICAL);*/
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize =  MeasureSpec.getSize(widthMeasureSpec);

        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize =  MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.UNSPECIFIED || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            throw new RuntimeException("SlidingDrawer cannot have UNSPECIFIED dimensions");
        }

        final View handle = getHandle();
        final View content = getContent();
        measureChild(handle, widthMeasureSpec, heightMeasureSpec);

        if (mVertical) {
            int height = heightSpecSize - handle.getMeasuredHeight() - mTopOffset;
            content.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, heightSpecMode));
            heightSpecSize = handle.getMeasuredHeight() + mTopOffset + content.getMeasuredHeight();
            widthSpecSize = content.getMeasuredWidth();
            if (handle.getMeasuredWidth() > widthSpecSize) widthSpecSize = handle.getMeasuredWidth();
        }
        else {
            int width = widthSpecSize - handle.getMeasuredWidth() - mTopOffset;
            getContent().measure(MeasureSpec.makeMeasureSpec(width, widthSpecMode), heightMeasureSpec);
            widthSpecSize = handle.getMeasuredWidth() + mTopOffset + content.getMeasuredWidth();
            heightSpecSize = content.getMeasuredHeight();
            if (handle.getMeasuredHeight() > heightSpecSize) heightSpecSize = handle.getMeasuredHeight();
        }
        
   /*     if (mVertical) {
            int height = heightSpecSize - handle.getMeasuredHeight() - mTopOffset;
            content.measure(MeasureSpec.makeMeasureSpec(widthSpecSize, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
            heightSpecSize = handle.getMeasuredHeight() + mTopOffset + content.getMeasuredHeight();
            widthSpecSize = content.getMeasuredWidth();
            if (handle.getMeasuredWidth() > widthSpecSize) widthSpecSize = handle.getMeasuredWidth();
        } else {
            int width = widthSpecSize - handle.getMeasuredWidth() - mTopOffset;
            content.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(heightSpecSize, MeasureSpec.UNSPECIFIED));
            widthSpecSize = handle.getMeasuredWidth() + mTopOffset + content.getMeasuredWidth();
            heightSpecSize = content.getMeasuredHeight();
            if (handle.getMeasuredHeight() > heightSpecSize) heightSpecSize = handle.getMeasuredHeight();
        }  */     

        setMeasuredDimension(widthSpecSize, heightSpecSize);
    }

    private boolean mVertical;
    private int mTopOffset;
}
