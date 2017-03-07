package com.bj.yt.newapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bj.yt.newapplication.bean.LoginBean;
import com.bj.yt.newapplication.bean.NewsBean;
import com.bj.yt.newapplication.common.MyApplication;
import com.bj.yt.newapplication.config.MessageEvent;
import com.bj.yt.newapplication.config.Strings;
import com.bj.yt.newapplication.fragment.LocationFragment;
import com.bj.yt.newapplication.fragment.MessageFragment;
import com.bj.yt.newapplication.fragment.ThreeDFragment;
import com.bj.yt.newapplication.receiver.LocationReceiver;
import com.bj.yt.newapplication.receiver.MessageReceiver;
import com.bj.yt.newapplication.util.Netutil;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private String TAG="MainActivity";
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
    private List<NewsBean> newsList;

    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    isExit = false;
                    break;
                case 1:
                    showCon();
                    break;
                case 2:
                    /**
                     * 是否第一次今入主界面
                     * 判断是否有网络
                     * 没有，弹出设置网络框
                     */
                    if(MyApplication.isFirstMain){
                        if(Netutil.isNetworkAvailable(MainActivity.this)){
                            checkIsLogin();
                        }else{
                            showNetAlert();
                        }
                        MyApplication.isFirstMain=false;
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        initView(); // 初始化界面控件
        setChioceItem(0);   // 初始化页面加载时显示第一个选项卡

        checknet();
    }

    /**
     * 判断服务器是否连接
     */
    private void checknet() {
        OkHttpUtils
                .post()
                .url(Strings.REQUEST_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.i(TAG, "服务器连接失败");

                        mHandler.sendEmptyMessage(1);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "服务器连接成功");
                        mHandler.sendEmptyMessage(2);
                    }
                });
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


    /**
     * 是否登录
     * @return
     */
    public void checkIsLogin(){
        SharedPreferences sharedPreferences=getSharedPreferences("info",MODE_PRIVATE);
        String useraccount=sharedPreferences.getString("useraccount","");
        String password=sharedPreferences.getString("password","");
        String id=sharedPreferences.getString("id","");
        String username=sharedPreferences.getString("username","");
        Log.i(TAG,"获取username:"+username);
        if("".equals(useraccount)){
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
            MyApplication.isFirstMain=false;
        }else{
            //获取登录界面返回的消息
            MyApplication.useraccount=useraccount;
            MyApplication.password=password;
            MyApplication.username=username;
            MyApplication.id=id;
            getLoginMsg();

            startLocationAlarmManager();//开启定位
            startMsgAlarmManager();//开启检查消息
        }

    }

    /**
     * 网络设置对话框
     */
    private void showNetAlert() {
        Log.i("TAG","进入alert设置");
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setTitle("提示");
        alert.setMessage("当前无网络，是否设置网络？");
        alert.setCancelable(false);
        alert.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS), 1);
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

    /**
     * 服务器连接不上
     */
    private void showCon() {
        Log.i("TAG","进入alert设置");
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setTitle("提示");
        alert.setMessage("服务器错误，请检查服务器设置！");
        alert.setCancelable(false);
        alert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        alert.create();
        alert.show();
    }

    /**
     * 设置完网络返回继续判断是否登录
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            checkIsLogin();
        }
    }

    /**
     * 发送定位周期广播
     */
    private void startLocationAlarmManager() {
        Intent intent =new Intent(MainActivity.this, LocationReceiver.class);
        intent.setAction("sendLocation");
        PendingIntent sender=PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        //开始时间
        long firstime=SystemClock.elapsedRealtime();

        AlarmManager am=(AlarmManager)getSystemService(ALARM_SERVICE);
        //5秒一个周期，不停的发送广播
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime, 30*1000, sender);
    }

    /**
     * 发送检查消息更新周期广播
     */
    private void startMsgAlarmManager() {
        Intent intent =new Intent(MainActivity.this, MessageReceiver.class);
        intent.setAction("isNewMessage");
        PendingIntent sender=PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        //开始时间
        long firstime=SystemClock.elapsedRealtime();

        AlarmManager am=(AlarmManager)getSystemService(ALARM_SERVICE);
        //5秒一个周期，不停的发送广播
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime, 30*1000, sender);
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
                location_layout.setBackgroundColor(getResources().getColor(R.color.table_color));
                // 如果fragment为空，则创建一个并添加到界面上
                if (locationFragment == null) {
                    locationFragment = new LocationFragment();
                    fragmentTransaction.add(R.id.frameLayout, locationFragment);
                } else {
                    // 如果不为空，则直接将它显示出来
                    fragmentTransaction.show(locationFragment);
                }
                hideRuan();
                break;
            case 1:
                message_layout.setBackgroundColor(getResources().getColor(R.color.table_color));
                if (messageFragment == null) {
                    messageFragment = new MessageFragment();
                    fragmentTransaction.add(R.id.frameLayout, messageFragment);
                } else {
                    fragmentTransaction.show(messageFragment);
                }
                break;
            case 2:
                threed_layout.setBackgroundColor(getResources().getColor(R.color.table_color));
                if (threeDFragment == null) {
                    threeDFragment = new ThreeDFragment();
                    fragmentTransaction.add(R.id.frameLayout, threeDFragment);
                } else {
                    fragmentTransaction.show(threeDFragment);
                }
                hideRuan();
                break;
        }
        fragmentTransaction.commit();   // 提交
    }

    /**
     * 当选中其中一个选项卡时，其他选项卡重置为默认
     */
    private void clearChioce() {
        //位置
        location_layout.setBackgroundColor(getResources().getColor(R.color.white));
        //消息
        message_layout.setBackgroundColor(getResources().getColor(R.color.white));
        //3D展示
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

    /**
     * 隐藏软键盘
     */
    public void hideRuan(){
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(location_text.getWindowToken(), 0);
    }

    /**
     * 获取登录界面返回的消息
     */
    private void getLoginMsg() {
        //用户登录
        OkHttpUtils
            .post()
            .url(Strings.REQUEST_URL+"login")
            .addParams("userName",MyApplication.id)
            .addParams("submitId",MyApplication.username )
            .addParams("context",MyApplication.password)
            .build()
            .execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                }

                @Override
                public void onResponse(String response) {
                    LoginBean lbean=new LoginBean();
                    Gson gson=new Gson();
                    lbean=gson.fromJson(response,LoginBean.class);
                    if("0".equals(lbean.getId())) {
                        //通知消息集合并页面更新
                        newsList = new ArrayList<NewsBean>();
                        newsList = lbean.getList();
                        MyApplication.newsList.addAll(newsList);
                        Log.i(TAG, "有消息");
                        EventBus.getDefault().post(new MessageEvent("loginNewmessage"));
                    }
                }
            });

        }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

}
