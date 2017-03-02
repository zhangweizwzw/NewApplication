package com.bj.yt.newapplication.common;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;

import com.baidu.mapapi.SDKInitializer;
import com.bj.yt.newapplication.service.LocationService;

/**
 * Created by admin on 2017/3/1.
 */

public class MyApplication extends Application{
    public LocationService locationService;
    public Vibrator mVibrator;

    @Override
    public void onCreate() {
        super.onCreate();

        /***
        * 初始化定位sdk
         * */
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
    }
}
