package com.bj.yt.newapplication.common;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;

import com.baidu.mapapi.SDKInitializer;
import com.bj.yt.newapplication.config.NewsBean;
import com.bj.yt.newapplication.config.UserBean;
import com.bj.yt.newapplication.service.LocationService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/3/1.
 */

public class MyApplication extends Application{
    public LocationService locationService;
    public Vibrator mVibrator;
    public static List<NewsBean> newsList=new ArrayList<NewsBean>();

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
