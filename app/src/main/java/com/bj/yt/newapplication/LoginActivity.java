package com.bj.yt.newapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


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
        btn_reset= (Button) findViewById(R.id.btn_reset);
        btn_login= (Button) findViewById(R.id.btn_login);
        et_username= (EditText) findViewById(R.id.et_username);
        et_password= (EditText) findViewById(R.id.et_password);
        btn_reset.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        title_center= (TextView) findViewById(R.id.title_center);
        title_center.setText("登录");

          //假数据测试
//        String username="0";
//        String str="{\"id\":\"0\",\"list\":[{\"id\":7,\"submitTime\":\"2017-03-02T03:18:23.152+0000\",\"sendTime\":null,\"submitUserId\":5,\"acceptUserId\":0,\"context\":\"15\"},{\"id\":8,\"submitTime\":\"2017-03-02T03:18:23.152+0000\",\"sendTime\":null,\"submitUserId\":55,\"acceptUserId\":0,\"context\":\"4654\"},{\"id\":5,\"submitTime\":\"2017-03-02T03:18:23.152+0000\",\"sendTime\":null,\"submitUserId\":5,\"acceptUserId\":0,\"context\":\"c\"}]}";
//        LoginBean lbean=new LoginBean();
//        Gson gson=new Gson();
//        lbean=gson.fromJson(str,LoginBean.class);
//        if("0".equals(lbean.getId())){
//            SharedPreferences share=getSharedPreferences("info",MODE_PRIVATE);
//            MyApplication.useraccount=username;//保存用户名
//            share.edit().putString("userName",username).putString("password","123456").commit();
//
//            newsList=new ArrayList<NewsBean>();
//            newsList=lbean.getList();
//            MyApplication.newsList.addAll(newsList);
//            Log.i(TAG,"有消息");
//            EventBus.getDefault().post(new MessageEvent("loginNewmessage"));//通知消息页面更新
//
//            startActivity(new Intent(LoginActivity.this,MainActivity.class));
//            finish();
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
                final String username=et_username.getText().toString().trim();
                final String password=et_password.getText().toString().trim();
                //用户名或密码为空
                if (TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, Strings.LOGIN_NULL,Toast.LENGTH_SHORT).show();
                }else{
                    //用户登录
                    OkHttpUtils
                            .post()
                            .url(Strings.REQUEST_URL+"login")
                            .addParams("userName",username)
                            .addParams("password",password)
                            .build()
                            .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            Toast.makeText(LoginActivity.this,Strings.LOGIN_Fail,Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String response) {
                            LoginBean lbean=new LoginBean();
                            Gson gson=new Gson();
                            lbean=gson.fromJson(response,LoginBean.class);
                            if("0".equals(lbean.getId())) {
                                SharedPreferences share = getSharedPreferences("info", MODE_PRIVATE);
                                share.edit().putString("userName", username).putString("password", password).commit();
                                MyApplication.useraccount = username;//保存用户名

                                //通知消息集合并页面更新
                                newsList = new ArrayList<NewsBean>();
                                newsList = lbean.getList();
                                MyApplication.newsList.addAll(newsList);
                                Log.i(TAG, "有消息");
                                EventBus.getDefault().post(new MessageEvent("loginNewmessage"));

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();

                            }else{
                                Toast.makeText(LoginActivity.this,Strings.LOGIN_Fail,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

                break;
        }
    }
}
