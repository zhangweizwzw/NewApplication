package com.bj.yt.newapplication.common;

import android.app.Application;

import com.bj.yt.newapplication.bean.NewsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/3/1.
 */

public class MyApplication extends Application{
    //用户信息集合
    public static List<NewsBean> newsList=new ArrayList<NewsBean>();
    //最新纬度
    public static double newlat;
    //用户经度
    public static double newlon;
    //用户账号
    public static String useraccount="0";
    //是否第一次进入主界面
    public static boolean isFirstMain=true;

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
