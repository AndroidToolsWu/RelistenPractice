package com.alan.listen.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

/**
 * @author Created by Alan
 * @date 2020/12/14
 */
public class App extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return mContext;
    }
}
