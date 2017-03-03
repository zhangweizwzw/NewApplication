package com.bj.yt.newapplication.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bj.yt.newapplication.R;
import com.bj.yt.newapplication.common.MyApplication;
import com.bj.yt.newapplication.config.MessageEvent;
import com.bj.yt.newapplication.util.Locationutil;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class LocationFragment extends BaseFragment {
    private final String TAG="LocationFragment";
    private  View view;
    private TextView title_center;
    private MapView map;
    private GraphicsLayer graphicsLayer;
    private Double lat,lon;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    initData();
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_location, container, false);

        //获取经纬度信息
        Locationutil.goLocation(getActivity());

        initView();
        return view;
    }

    private void initView() {
        map = (MapView) view.findViewById(R.id.map);
        title_center= (TextView)view.findViewById(R.id.title_center);
        title_center.setText("位置");
    }

//    public void isGps() {
//        if(GpsUtil.hasGPSDevice(getActivity())){
//            map.addLayer(new ArcGISTiledMapServiceLayer("http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer"));
//            map.setOnStatusChangedListener(new OnStatusChangedListener() {
//                public void onStatusChanged(Object source, STATUS status) {
//                    if (source == map && status == STATUS.INITIALIZED) {
//
//                        LocationDisplayManager ldm = map.getLocationDisplayManager();
//                        ldm.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);
//                        ldm.start();
//                        //移动到当前位置
//                        ShowLocation(MyApplication.newlon,MyApplication.newlat);
//                    }
//                }
//            });
//            graphicsLayer = new GraphicsLayer();
//            map.addLayer(graphicsLayer);
//        }else{
//            initData();
//        }
//    }



    private void initData() {
        System.out.println("yyyyyyyyyyyy");
        /**
         * 获取地图和XML布局的初始程度
         *添加动态层MapView
         */
        map.addLayer(new ArcGISTiledMapServiceLayer("http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer"));
        map.setOnStatusChangedListener(new OnStatusChangedListener() {
            public void onStatusChanged(Object source, STATUS status) {
                if (source == map && status == STATUS.INITIALIZED) {

                    LocationDisplayManager ldm = map.getLocationDisplayManager();
                    ldm.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);
                    ldm.start();
                    //移动到当前位置
                    System.out.println("llllllllll:"+MyApplication.newlon);
                    ShowLocation(MyApplication.newlon,MyApplication.newlat);
                }
            }
        });
        graphicsLayer = new GraphicsLayer();
        map.addLayer(graphicsLayer);
    }

    @Subscribe
    public void onEventMainThread(MessageEvent event){
        if(event.message.toString().equals("locationUpdate")){
            System.out.println("aaaaaaaaaaaaaaa");

            Message message=new Message();
            message.what=1;
            handler.sendMessage(message);

        }
    }

    //将地图移动到当前位置
    public void ShowLocation(double locx,double locy) {
        PictureMarkerSymbol locationPH = new PictureMarkerSymbol(getActivity().getResources().getDrawable(R.mipmap.touming));
        Point wgspoint = new Point(locx, locy);
        Point mapPoint = (Point) GeometryEngine.project(wgspoint, SpatialReference.create(4326), map.getSpatialReference());
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
    public void onPause() {
        super.onPause();
        map.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        map.unpause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

}


//    //地图触摸事件
//    class MyTouchListener extends MapOnTouchListener {
//        MultiPath poly;
//        String type = "POINT";
//        Point startPoint =null;
//
//        public MyTouchListener(Context context, MapView view) {
//            super(context, view);
//        }
//
//        public void setType(String geometryType) {
//            this.type = geometryType;
//        }
//
//        public String getType() {
//            return this.type;
//        }
//
//        //获取点击位置坐标
//        public boolean onSingleTap(MotionEvent e) {
//            if (type.length() > 1 && type.equalsIgnoreCase("POINT")) {
//                graphicsLayer.removeAll();
//                Graphic graphic = new Graphic(map.toMapPoint(new Point(e.getX(), e.getY())), new SimpleMarkerSymbol(Color.BLUE, 25, SimpleMarkerSymbol.STYLE.CIRCLE));
//                Point pa = map.toMapPoint(e.getX(), e.getY());
//                graphicsLayer.addGraphic(graphic);
//                //转化为投影坐标
//                Point po=new Point(pa.getX(), pa.getY());
//                points.add(po);
//                drawPolyline();
//                return true;
//            }
//            return false;
//        }
//    }

//    //划线
//    private void drawPolyline() {
//        String editingmode="POLYLINE";
//        if (graphicsLayer == null) {
//            graphicsLayer = new GraphicsLayer();
//            map.addLayer(graphicsLayer);
//        }
//        if (points.size() <= 1)
//            return;
//        Graphic graphic;
//        MultiPath multipath;
//        multipath = new Polyline();
//        multipath.startPath(points.get(0));
//        for (int i = 1; i < points.size(); i++) {
//            multipath.lineTo(points.get(i));
//        }
//        System.out.println("aaaaaaaaaaa==>DrawPolyline: Array coutn = "+points.size());
//        graphic = new Graphic(multipath, new SimpleLineSymbol(Color.RED, 4));
//        graphicsLayer.addGraphic(graphic);
//    }
