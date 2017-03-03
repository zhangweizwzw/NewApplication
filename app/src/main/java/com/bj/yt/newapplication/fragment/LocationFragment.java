package com.bj.yt.newapplication.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.bj.yt.newapplication.R;
import com.bj.yt.newapplication.common.MyApplication;
import com.bj.yt.newapplication.service.LocationService;
import com.bj.yt.newapplication.util.GpsUtil;
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
    private final String TAG="LocationFragment";
    private  View view;
    private TextView title_center;
    private MapView map;
    private GraphicsLayer graphicsLayer;
    private LocationService locationService;
    private Double lat,lon;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    MeLocation();
                break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_location, container, false);

        initView();
        isGps();

        return view;
    }

    public void isGps() {
        if(GpsUtil.hasGPSDevice(getActivity())){
            map.addLayer(new ArcGISTiledMapServiceLayer("http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer"));
            map.setOnStatusChangedListener(new OnStatusChangedListener() {
                public void onStatusChanged(Object source, STATUS status) {
                    if (source == map && status == STATUS.INITIALIZED) {

                        LocationDisplayManager ldm = map.getLocationDisplayManager();
                        ldm.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);
                        ldm.start();
                        //移动到当前位置
//                        ShowLocation(ldm.getLocation().getLongitude(),ldm.getLocation().getLatitude());
                    }

                }
            });
            graphicsLayer = new GraphicsLayer();
            map.addLayer(graphicsLayer);
        }else{
            initData();
        }
    }

    private void initView() {
        map = (MapView) view.findViewById(R.id.map);
        title_center= (TextView)view.findViewById(R.id.title_center);
        title_center.setText("位置");
    }

    private void initData() {
        // -----------location config ------------
        locationService = ((MyApplication)getActivity().getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getActivity().getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
        locationService.start();// 启动定位SDK
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
                    ShowLocation(lon,lat);
                }
            }
        });
        graphicsLayer = new GraphicsLayer();
        map.addLayer(graphicsLayer);
    }

    public void MeLocation(){
        map.setOnStatusChangedListener(new OnStatusChangedListener() {
            public void onStatusChanged(Object source, STATUS status) {
                if (source == map && status == STATUS.INITIALIZED) {

                    LocationDisplayManager ldm = map.getLocationDisplayManager();
                    ldm.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);
                    ldm.start();
                    //移动到当前位置
                    ShowLocation(lon,lat);
                }
            }
        });
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

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bLocation) {
            // TODO Auto-generated method stub
            if (null != bLocation && bLocation.getLocType() != BDLocation.TypeServerError) {
                Log.i(TAG,"latitude:"+bLocation.getLatitude());
                Log.i(TAG,"longitude:"+bLocation.getLongitude());

                //解决百度地图获取位置偏移
                double x = bLocation.getLongitude();
                double y = bLocation.getLatitude();
                double z = Math.sqrt(x*x+y*y) + 0.00002 *Math.sin(y*Math.PI) ;
                double temp =Math.atan2(y, x)  + 0.000003 * Math.cos(x*Math.PI);
                lon = z * Math.cos(temp) + 0.0065;
                lat = z * Math.sin(temp) + 0.006;

//                lat=bLocation.getLatitude();
//                lon=bLocation.getLongitude();

                Message message=new Message();
                message.what=1;
                handler.sendMessage(message);
            }
        }

        public void onConnectHotSpotMessage(String s, int i){
        }
    };

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
