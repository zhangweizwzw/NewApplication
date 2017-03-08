package com.bj.yt.newapplication.receiver;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.bj.yt.newapplication.bean.GpsBean;
import com.bj.yt.newapplication.common.MyApplication;
import com.bj.yt.newapplication.config.Strings;
import com.bj.yt.newapplication.util.EvilTransform;
import com.bj.yt.newapplication.util.GpsUtil;
import com.bj.yt.newapplication.util.Locationutil;
import com.bj.yt.newapplication.util.PositionUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;


import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by admin on 2017/3/2.
 */

public class LocationReceiver extends BroadcastReceiver {
    private final String TAG="LocationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("sendLocation")){
            Log.i(TAG,"定位定时器：30秒钟");
            //获取经纬度
//            if (GpsUtil.hasGPSDevice(context)) {
////                getGPSConfi(context);
//                gpsLocation(context);
//            } else {
                Locationutil.goLocation(context);
//            }

            if(0.0!=MyApplication.newlat){
                sendLocation(MyApplication.newlat,MyApplication.newlon);
            }
        }
    }

    /**
     * 向服务器发送更新消息
     */
    private void sendLocation(double lat,double lon) {
        Log.i(TAG,"上传位置经度为："+lat);
        Log.i(TAG,"上传位置纬度为："+lon);

        OkHttpUtils.post().url(Strings.REQUEST_URL+"saveLocation")

                .addParams("id",MyApplication.id)
                .addParams("username",MyApplication.username)
                .addParams("lat",lat+"")
                .addParams("lon",lon+"")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                Log.i(TAG,"上传最新经纬度结果："+response);
            }
        });
    }



    public void gpsLocation(Context context){
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS获取定位的位置数据
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
    //    updateToNewLocation(location);
        /**服务管理对象的监听器*/
        //参数1：定位的方式   参数2：监听更新间隔时间(ms)  参数3：监听更新的距离(m) 参数4：监听的方法
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 1000, 10, new LocationListener() {
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            public void onProviderEnabled(String provider) {
            }
            public void onProviderDisabled(String provider) {
            }
            public void onLocationChanged(Location location) {
                sendLocation(location.getLatitude(),location.getLongitude());
            }
        });
    }
}