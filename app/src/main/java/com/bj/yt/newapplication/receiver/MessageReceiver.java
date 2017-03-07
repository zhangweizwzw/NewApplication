package com.bj.yt.newapplication.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bj.yt.newapplication.common.MyApplication;
import com.bj.yt.newapplication.config.MessageEvent;
import com.bj.yt.newapplication.bean.NewsBean;
import com.bj.yt.newapplication.config.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by admin on 2017/3/2.
 */

public class MessageReceiver extends BroadcastReceiver {
    private final String TAG="MessageReceiver";
    public List<NewsBean> nlist;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("isNewMessage")){
            Log.i(TAG,"消息定时器：30秒钟");
            //真实数据
           initData();

//            // 假数据
//            nlist=new ArrayList<NewsBean>();
//            String response="[{\"id\":3,\"submitTime\":\"2017-03-02T10:01:12.581+0000\",\"sendTime\":null,\"submitUserId\":3,\"acceptUserId\":0,\"context\":\"a\"},{\"id\":1,\"submitTime\":\"2017-03-02T10:01:12.581+0000\",\"sendTime\":null,\"submitUserId\":1,\"acceptUserId\":0,\"context\":\"aaaaa\"},{\"id\":2,\"submitTime\":\"2017-03-02T10:01:12.581+0000\",\"sendTime\":null,\"submitUserId\":2,\"acceptUserId\":0,\"context\":\"bbbbbb\"},{\"id\":4,\"submitTime\":\"2017-03-02T10:01:12.581+0000\",\"sendTime\":null,\"submitUserId\":4,\"acceptUserId\":0,\"context\":\"b\"},{\"id\":5,\"submitTime\":\"2017-03-02T10:01:12.581+0000\",\"sendTime\":null,\"submitUserId\":5,\"acceptUserId\":0,\"context\":\"c\"},{\"id\":11,\"submitTime\":\"2017-03-02T10:01:12.581+0000\",\"sendTime\":null,\"submitUserId\":11,\"acceptUserId\":0,\"context\":\"11\"},{\"id\":7,\"submitTime\":\"2017-03-02T10:01:12.581+0000\",\"sendTime\":null,\"submitUserId\":5,\"acceptUserId\":0,\"context\":\"15\"},{\"id\":8,\"submitTime\":\"2017-03-02T10:01:12.581+0000\",\"sendTime\":null,\"submitUserId\":55,\"acceptUserId\":0,\"context\":\"4654\"},{\"id\":9,\"submitTime\":\"2017-03-02T10:01:12.581+0000\",\"sendTime\":null,\"submitUserId\":9,\"acceptUserId\":0,\"context\":\"599\"},{\"id\":10,\"submitTime\":\"2017-03-02T10:01:12.581+0000\",\"sendTime\":null,\"submitUserId\":55,\"acceptUserId\":0,\"context\":\"888\"},{\"id\":6,\"submitTime\":\"2017-03-02T10:01:12.583+0000\",\"sendTime\":null,\"submitUserId\":6,\"acceptUserId\":0,\"context\":\"56\"}]";
//            Gson gson=new Gson();
//            Type listType=new TypeToken<List<NewsBean>>(){}.getType();
//            nlist=gson.fromJson(response,listType);
//            MyApplication.newsList.addAll(nlist);
//
//            EventBus.getDefault().post(new MessageEvent("newmessage"));
        }
    }

    /**
     * 检查消息是否有跟新
     */
    private void initData() {
        OkHttpUtils.post().url(Strings.REQUEST_URL+"checkNews")
            .addParams("id",MyApplication.id)
            .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                Log.i(TAG,"是否有新消息："+response);
                if(response.equals("true")){
                    Log.i(TAG,"有新消息");
                    getNews();
                }else{
                    Log.i(TAG,"没有新消息");
                }

            }
        });
    }

    /**
     * 如果消息有跟新
     * 获取跟新消息内容
     */
    public void getNews() {
        OkHttpUtils.post().url(Strings.REQUEST_URL+"getNews")
                .addParams("id",MyApplication.id)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                Gson gson=new Gson();
                Type listType=new TypeToken<List<NewsBean>>(){}.getType();
                nlist=new ArrayList<NewsBean>();
                nlist=gson.fromJson(response,listType);
                MyApplication.newsList.addAll(nlist);

                Log.i(TAG,"请求最新消息结果："+response);
                EventBus.getDefault().post(new MessageEvent("newmessage"));
            }
        });
    }
}
