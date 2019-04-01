package com.oa.wanyu.myutils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by liuhaidong on 2019/3/20.
 */

public class MyGridView extends GridView {
    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //计算listview或者gridview高度的公式
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        //调用时将计算好的高度传入即可；
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
