package com.example.view;

import com.example.nyapp.MyApplication;

/**
 * 异常处理
 */
public class CrashApplication extends MyApplication {

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());


    }
}
