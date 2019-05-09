package com.oa.wanyu.myApplication;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by liuhaidong on 2019/4/15.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.init(this);
    }
}
