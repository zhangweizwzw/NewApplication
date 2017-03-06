package com.bj.yt.newapplication.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.bj.yt.newapplication.bean.GpsBean;

import java.util.List;

import static com.bj.yt.newapplication.util.Locationutil.locationListener;

/**
 * Created by admin on 2017/3/2.
 */

public class GpsUtil {
    private static String TAG = "GpsUtil";

    /**
     * 判断是否有GPS模块
     * @param context
     * @return
     */
    public static boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        Log.e(TAG, "没有Gps芯片");
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    // 判断GPS模块是否开启，如果没有则开启
    public static boolean isGPSOpen(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
    }
}
