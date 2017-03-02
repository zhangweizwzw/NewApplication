package com.bj.yt.newapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bj.yt.newapplication.R;
import com.bj.yt.newapplication.config.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MessageFragment extends BaseFragment {
    private  View view;
    private TextView title_center;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message, container, false);

        initView();
        return view;
    }

    @Subscribe
    public void onEventMainThread(MessageEvent event){
        System.out.println("hhhhhhhhhhhhhhhhh");
        if(event.message.toString().equals("newmessage")){
            System.out.println("接收到新消息");
        }
    }

    private void initView() {
        title_center= (TextView)view.findViewById(R.id.title_center);
        title_center.setText("消息");
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
