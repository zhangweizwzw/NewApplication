package com.bj.yt.newapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bj.yt.newapplication.fragment.LocationFragment;
import com.bj.yt.newapplication.fragment.MessageFragment;
import com.bj.yt.newapplication.fragment.ThreeDFragment;
import com.bj.yt.newapplication.receiver.LocationReceiver;
import com.bj.yt.newapplication.receiver.MessageReceiver;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    // 定义Fragment对象
    private LocationFragment locationFragment;
    private MessageFragment messageFragment;
    private ThreeDFragment threeDFragment;

    // 帧布局对象，用来存放Fragment对象
    private FrameLayout frameLayout;
    // 定义每个选项中的相关控件
    private RelativeLayout location_layout;
    private RelativeLayout message_layout;
    private RelativeLayout threed_layout;

    private TextView location_text;
    private TextView message_text;
    private TextView threed_text;
    // 定义FragmentManager对象管理器
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //判断网络是否连接
        checkNetworkState();
        //判断是否已登录
        SharedPreferences sharedPreferences=getSharedPreferences("info",MODE_PRIVATE);
        if (sharedPreferences==null){
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }
        String un=sharedPreferences.getString("userNaeme","");
        String pw=sharedPreferences.getString("password","");
                Log.i("<<取得的用户名密码<<",un+","+pw);

        fragmentManager = getSupportFragmentManager();
        initView(); // 初始化界面控件
        setChioceItem(0);   // 初始化页面加载时显示第一个选项卡

        startLocationAlarmManager();
        startMsgAlarmManager();
    }


    /**
     * 判断网络是否连接
     */
    private void checkNetworkState() {

        //得到网络连接信息
        ConnectivityManager con=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        //判断网络是否连接

        if (con.getActiveNetworkInfo()==null||!con.getActiveNetworkInfo().isAvailable()){
            Log.i("<<<<<","进入alert设置");
            AlertDialog.Builder alert=new AlertDialog.Builder(this);
            alert.setTitle("提示");
            alert.setMessage("当前无网络");
            alert.setCancelable(false);
            alert.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                }
            });
            alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            alert.create();
            alert.show();
        }
    }

    private void startLocationAlarmManager() {
        Intent intent =new Intent(MainActivity.this, LocationReceiver.class);
        intent.setAction("sendLocation");
        PendingIntent sender=PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        //开始时间
        long firstime=SystemClock.elapsedRealtime();

        AlarmManager am=(AlarmManager)getSystemService(ALARM_SERVICE);
        //5秒一个周期，不停的发送广播
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime, 10*1000, sender);
    }

    private void startMsgAlarmManager() {
        Intent intent =new Intent(MainActivity.this, MessageReceiver.class);
        intent.setAction("isNewMessage");
        PendingIntent sender=PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        //开始时间
        long firstime=SystemClock.elapsedRealtime();

        AlarmManager am=(AlarmManager)getSystemService(ALARM_SERVICE);
        //5秒一个周期，不停的发送广播
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime, 10*1000, sender);
    }


    /**
     * 初始化页面
     */
    private void initView() {

        // 初始化底部导航栏的控件
        location_text = (TextView) findViewById(R.id.location_text);
        message_text = (TextView) findViewById(R.id.message_text);
        threed_text = (TextView) findViewById(R.id.threed_text);

        location_layout = (RelativeLayout) findViewById(R.id.location_layout);
        message_layout = (RelativeLayout) findViewById(R.id.message_layout);
        threed_layout = (RelativeLayout) findViewById(R.id.threed_layout);

        location_layout.setOnClickListener(this);
        message_layout.setOnClickListener(this);
        threed_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.location_layout:
                setChioceItem(0);
                break;
            case R.id.message_layout:
                setChioceItem(1);
                break;
            case R.id.threed_layout:
                setChioceItem(2);
                break;
            default:
                break;
        }
    }

    /**
     * 设置点击选项卡的事件处理
     * @param index 选项卡的标号：0, 1, 2
     */
    private void setChioceItem(int index) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        clearChioce(); // 清空, 重置选项, 隐藏所有Fragment
        hideFragments(fragmentTransaction);

        switch (index) {
            case 0:
//              location_text.setTextColor(dark);
                location_layout.setBackgroundColor(getResources().getColor(R.color.table_color));
                // 如果fragment为空，则创建一个并添加到界面上
                if (locationFragment == null) {
                    locationFragment = new LocationFragment();
                    fragmentTransaction.add(R.id.frameLayout, locationFragment);
                } else {
                    // 如果不为空，则直接将它显示出来
                    fragmentTransaction.show(locationFragment);
                }
                break;
            case 1:
//              message_text.setTextColor(dark);
                message_layout.setBackgroundColor(getResources().getColor(R.color.table_color));
                if (messageFragment == null) {
                    messageFragment = new MessageFragment();
                    fragmentTransaction.add(R.id.frameLayout, messageFragment);
                } else {
                    fragmentTransaction.show(messageFragment);
                }
                break;
            case 2:
//              threed_text.setTextColor(dark);
                threed_layout.setBackgroundColor(getResources().getColor(R.color.table_color));
                if (threeDFragment == null) {
                    threeDFragment = new ThreeDFragment();
                    fragmentTransaction.add(R.id.frameLayout, threeDFragment);
                } else {
                    fragmentTransaction.show(threeDFragment);
                }
                break;
        }
        fragmentTransaction.commit();   // 提交
    }

    /**
     * 当选中其中一个选项卡时，其他选项卡重置为默认
     */
    private void clearChioce() {
        //位置
//      location_text.setTextColor(getResources().getColor(R.color.black));
        location_layout.setBackgroundColor(getResources().getColor(R.color.white));
        //消息
//      message_text.setTextColor(getResources().getColor(R.color.black));
        message_layout.setBackgroundColor(getResources().getColor(R.color.white));
        //3D展示
//      threed_text.setTextColor(getResources().getColor(R.color.black));
        threed_layout.setBackgroundColor(getResources().getColor(R.color.white));
    }
    /**
     * 隐藏Fragment
     * @param fragmentTransaction
     */
    private void hideFragments(FragmentTransaction fragmentTransaction) {
        if (locationFragment != null) {
            fragmentTransaction.hide(locationFragment);
        }
        if (messageFragment != null) {
            fragmentTransaction.hide(messageFragment);
        }
        if (threeDFragment != null) {
            fragmentTransaction.hide(threeDFragment);
        }
    }
}
