package com.bj.yt.newapplication.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bj.yt.newapplication.R;
import com.bj.yt.newapplication.bean.GpsBean;
import com.bj.yt.newapplication.util.GpsUtil;
import com.bj.yt.newapplication.util.PositionUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;

public class LocationFragment extends BaseFragment {
    private static String TAG = "LocationFragment";
    private View view;
    private TextView title_center;
    private MapView map;
    private GraphicsLayer graphicsLayer;
    private static AMapLocationClient locationClient = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_location, container, false);

        initView();
        if (GpsUtil.hasGPSDevice(getActivity())) {
            initData();
        } else {
            goLocation();
        }

        return view;
    }

    private void initView() {
        map = (MapView) view.findViewById(R.id.map);
        title_center = (TextView) view.findViewById(R.id.title_center);
        title_center.setText("位置");
    }

    //将地图移动到当前位置
    public void ShowLocation(double locx, double locy) {
        PictureMarkerSymbol locationPH = new PictureMarkerSymbol(getActivity().getResources().getDrawable(R.mipmap.location));
        Point wgspoint = new Point(locx, locy);
        Point mapPoint = (Point) GeometryEngine.project(wgspoint,
                SpatialReference.create(4326), map.getSpatialReference());
        graphicsLayer.removeAll();
        graphicsLayer.addGraphic(new Graphic(mapPoint, locationPH));
        map.centerAt(mapPoint, true);// 漫游到当前位置
    }

    /**
     * 初始化地图
     */
    private void initData() {
        map.addLayer(new ArcGISTiledMapServiceLayer("http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer"));
        map.setOnStatusChangedListener(new OnStatusChangedListener() {
            public void onStatusChanged(Object source, STATUS status) {
                if (source == map && status == STATUS.INITIALIZED) {

                    LocationDisplayManager ldm = map.getLocationDisplayManager();
                    ldm.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);
                    ldm.start();
                    //移动到当前位置
                    Log.i(TAG,"有GPS的定位==》"+ldm.getLocation().getLatitude());
                    ShowLocation(ldm.getLocation().getLongitude(), ldm.getLocation().getLatitude());
                }
            }
        });

        graphicsLayer = new GraphicsLayer();
        map.addLayer(graphicsLayer);
    }


    /**
     * 没有高德地图
     */
    public void goLocation() {
        //初始化client
        locationClient = new AMapLocationClient(getActivity().getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);

//        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 默认的定位参数
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private static AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }


    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(final AMapLocation loc) {
            if (null != loc) {
                //解析定位结果
                Log.i(TAG,"222222222222222==="+loc.getLatitude());

                map.addLayer(new ArcGISTiledMapServiceLayer("http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer"));
                map.setOnStatusChangedListener(new OnStatusChangedListener() {
                    public void onStatusChanged(Object source, STATUS status) {
//                        if (source == map && status == STATUS.INITIALIZED) {


                       GpsBean gpsBean= PositionUtil.gcj_To_GpsBean84(loc.getLongitude(), loc.getLatitude());

                        Log.i(TAG,"没有GPS的定位==》"+gpsBean.getWgLon());
//                            ShowLocation(loc.getLongitude(), loc.getLatitude());
                            ShowLocation(gpsBean.getWgLat(), gpsBean.getWgLon());
//                        }
                    }
                });
                graphicsLayer = new GraphicsLayer();
                map.addLayer(graphicsLayer);
            }
        }
    };

}
