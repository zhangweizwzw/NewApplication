package com.bj.yt.newapplication.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.bj.yt.newapplication.LoginActivity;
import com.bj.yt.newapplication.common.MyApplication;
import com.bj.yt.newapplication.config.MessageEvent;
import com.bj.yt.newapplication.config.NewsBean;
import com.bj.yt.newapplication.config.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;

/**
 * Created by admin on 2017/3/2.
 */

public class TimeReceiver extends BroadcastReceiver {
    private final String TAG="TimeReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("repeating")){
            Log.i(TAG,"过了10秒钟");
           initData();
        }
    }

    private void initData() {
        //用户登录
        OkHttpUtils.post().url(Strings.REQUEST_URL+"checkNews")
                .addParams("id","0")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                if(response.equals("true")){
                    Log.i(TAG,"有新消息");
                    getNews();
                }else{
                    Log.i(TAG,"没有新消息");
                }

            }
        });
    }

    public void getNews() {
        //用户登录
        OkHttpUtils.post().url(Strings.REQUEST_URL+"getNews")
                .addParams("id","0")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                Gson gson=new Gson();
                NewsBean newsBean=new NewsBean();
//                newsBean=gson.fromJson(response,NewsBean.class);
                 Type listType=new TypeToken<List<NewsBean>>(){}.getType();
                MyApplication.newsList=gson.fromJson(response,listType);
//                MyApplication.newsList.add(newsBean);

                System.out.println("22222222222=="+response);
                EventBus.getDefault().post(new MessageEvent("newmessage"));
            }
        });
    }
}

//    private void initData() {
//        //用户登录
//        OkHttpUtils.post().url(Strings.REQUEST_URL)
//                .addParams("id","123456")
//                .addParams("lat","40.019367")
//                .addParams("lon","116.475648")
//                .build().execute(new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e) {
//
//            }
//
//            @Override
//            public void onResponse(String response) {
//
//            }
//        });
//    }
