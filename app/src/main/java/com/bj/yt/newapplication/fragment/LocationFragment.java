package com.bj.yt.newapplication.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClient;
import com.bj.yt.newapplication.R;
import com.bj.yt.newapplication.common.MyApplication;
import com.bj.yt.newapplication.config.MessageEvent;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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

        return view;
    }

    private void initView() {
        map = (MapView) view.findViewById(R.id.map);
        title_center = (TextView) view.findViewById(R.id.title_center);
        title_center.setText("位置");
    }
    /**
     * 接收位置跟新发过来的广播
     * @param event
     */
    @Subscribe
    public void onEventMainThread(MessageEvent event){
        if(event.message.toString().equals("locationUpdate")){
            Log.i(TAG,"位置页面位置更新");
            initDatea();
        }
    }

    public void initDatea(){
        map.addLayer(new ArcGISTiledMapServiceLayer("http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer"));
//        map.addLayer(new ArcGISTiledMapServiceLayer("http://cache1.arcgisonline.cn/ArcGIS/rest/services/ChinaOnlineCommunity/MapServer"));
        map.setOnStatusChangedListener(new OnStatusChangedListener() {
            public void onStatusChanged(Object source, STATUS status) {

                Log.i(TAG,"位置页面定位纬度==="+MyApplication.newlat);
                Log.i(TAG,"位置页面定位经度==="+MyApplication.newlon);
                ShowLocation(MyApplication.newlon, MyApplication.newlat);
            }
        });
        graphicsLayer = new GraphicsLayer();
        map.addLayer(graphicsLayer);
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

    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

}
