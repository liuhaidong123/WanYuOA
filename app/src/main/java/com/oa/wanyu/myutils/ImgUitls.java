package com.oa.wanyu.myutils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by liuhaidong on 2017/5/3.
 */

public class ImgUitls {


    public static int getWith(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width3 = dm.widthPixels;
       // Log.e("宽度",width3+"");
        return width3;

    }

    public static int getHeight(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int height3 = dm.heightPixels;
       // Log.e("宽度",height3+"");
        return height3;

    }
}
