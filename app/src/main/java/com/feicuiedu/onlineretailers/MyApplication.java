package com.feicuiedu.onlineretailers;

import android.app.Application;
import android.content.Context;

/**
 * Created by zqc on 2017/1/9.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        //获取Context
        context = getApplicationContext();
    }
    //返回
    public static Context getContextObject(){
        return context;
    }
}
