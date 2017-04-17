package com.bj.yt.newapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bj.yt.newapplication.bean.LoginBean;
import com.bj.yt.newapplication.bean.NewsBean;
import com.bj.yt.newapplication.common.MyApplication;
import com.bj.yt.newapplication.config.MessageEvent;
import com.bj.yt.newapplication.config.Strings;
import com.bj.yt.newapplication.receiver.LocationReceiver;
import com.bj.yt.newapplication.receiver.MessageReceiver;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;


public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private String TAG="LoginActivity";
    private Button btn_reset,btn_login;
    private TextView title_center;
    private EditText et_username,et_password;
    private List<NewsBean> newsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

    }

    private void initView() {
        btn_reset = (Button) findViewById(R.id.btn_reset);
        btn_login = (Button) findViewById(R.id.btn_login);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_reset.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        title_center = (TextView) findViewById(R.id.title_center);
        title_center.setText("登录");

//        //假数据测试
//        String username = "0";
//        String str = "{\"id\":\"0\",\"list\":[{\"id\":7,\"submitTime\":\"2017-03-02T03:18:23.152+0000\",\"sendTime\":null,\"submitUserId\":5,\"acceptUserId\":0,\"context\":\"15\"},{\"id\":8,\"submitTime\":\"2017-03-02T03:18:23.152+0000\",\"sendTime\":null,\"submitUserId\":55,\"acceptUserId\":0,\"context\":\"4654\"},{\"id\":5,\"submitTime\":\"2017-03-02T03:18:23.152+0000\",\"sendTime\":null,\"submitUserId\":5,\"acceptUserId\":0,\"context\":\"c\"}]}";
//        LoginBean lbean = new LoginBean();
//        Gson gson = new Gson();
//        lbean = gson.fromJson(str, LoginBean.class);
//        if ("0".equals(lbean.getId())) {
//            SharedPreferences share = getSharedPreferences("info", MODE_PRIVATE);
//            MyApplication.useraccount = username;//保存用户名
//            share.edit().putString("userName", username).putString("password", "").commit();
//
//            newsList = new ArrayList<NewsBean>();
//            newsList = lbean.getList();
//            MyApplication.newsList.addAll(newsList);
//            Log.i(TAG, "有消息");
//            EventBus.getDefault().post(new MessageEvent("loginNewmessage"));//通知消息页面更新
//
//            startLocationAlarmManager();//开启定位
//            startMsgAlarmManager();//开启检查消息
//
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//            finish();
//        }
    }

    /**
     * 发送定位周期广播
     */
    private void startLocationAlarmManager() {
        Intent intent =new Intent(LoginActivity.this, LocationReceiver.class);
        intent.setAction("sendLocation");
        PendingIntent sender=PendingIntent.getBroadcast(LoginActivity.this, 0, intent, 0);
        //开始时间
        long firstime= SystemClock.elapsedRealtime();

        AlarmManager am=(AlarmManager)getSystemService(ALARM_SERVICE);
        //5秒一个周期，不停的发送广播
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime, 30*1000, sender);
    }

    /**
     * 发送检查消息更新周期广播
     */
    private void startMsgAlarmManager() {
        Intent intent =new Intent(LoginActivity.this, MessageReceiver.class);
        intent.setAction("isNewMessage");
        PendingIntent sender=PendingIntent.getBroadcast(LoginActivity.this, 0, intent, 0);
        //开始时间
        long firstime=SystemClock.elapsedRealtime();

        AlarmManager am=(AlarmManager)getSystemService(ALARM_SERVICE);
        //5秒一个周期，不停的发送广播
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime, 30*1000, sender);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_reset:
                et_username.setText("");
                et_password.setText("");
                break;
            case R.id.btn_login:
//                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                final String useraccount=et_username.getText().toString().trim();
                final String password=et_password.getText().toString().trim();
                //用户名或密码为空
                if (TextUtils.isEmpty(useraccount)||TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, Strings.LOGIN_NULL,Toast.LENGTH_SHORT).show();
                }else{
                    //用户登录

                    OkHttpUtils
                            .post()
                            .url(Strings.REQUEST_URL+"login")
                            .addParams("userName",useraccount)
                            .addParams("password",password)
                            .build()
                            .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            Toast.makeText(LoginActivity.this,Strings.LOGIN_Fail,Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String response) {
                            Log.i(TAG,"登录返回结果："+response );
                            LoginBean lbean=new LoginBean();
                            Gson gson=new Gson();
                             lbean=gson.fromJson(response,LoginBean.class);
                            if("success".equals(lbean.getCode())) {
                                SharedPreferences share = getSharedPreferences("info", MODE_PRIVATE);
                                share.edit().putString("useraccount", useraccount)
                                .putString("password", password)
                                .putString("id",lbean.getId())
                                .putString("username",lbean.getUsername())
                                .commit();

                                MyApplication.useraccount = useraccount;//保存用户名
                                MyApplication.id=lbean.getId();
                                MyApplication.username=lbean.getUsername();

                                //通知消息集合并页面更新
                                newsList = new ArrayList<NewsBean>();
                                newsList = lbean.getList();
                                MyApplication.newsList.addAll(newsList);
                                Log.i(TAG, "有消息");
                                EventBus.getDefault().post(new MessageEvent("loginNewmessage"));

                                startLocationAlarmManager();//开启定位
                                startMsgAlarmManager();//开启检查消息

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();


                            }else{
                                Toast.makeText(LoginActivity.this,lbean.getCode(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

                break;
        }
    }
}
