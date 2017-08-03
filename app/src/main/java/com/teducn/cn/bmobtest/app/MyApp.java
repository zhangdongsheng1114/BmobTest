package com.teducn.cn.bmobtest.app;

import android.app.Application;
import android.content.Context;

import cn.bmob.v3.Bmob;

/**
 * Created by tarena on 2017/8/3.
 */

public class MyApp extends Application {

    public static String TAG = "tedu";
    public static Context CONTEXT;

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "8fcfe8ec7c3a566f98a19d0f2d947a0a");
        CONTEXT = this;
    }
}
