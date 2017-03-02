package com.bj.yt.newapplication.fragment;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.bj.yt.newapplication.R;
import com.bj.yt.newapplication.adapter.ChatMsgViewAdapter;
import com.bj.yt.newapplication.common.MyApplication;
import com.bj.yt.newapplication.config.MessageEvent;
import com.bj.yt.newapplication.config.NewsBean;
import com.bj.yt.newapplication.util.Dateutil;
import com.bj.yt.newapplication.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Date;

public class MessageFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private TextView title_center;
    private ListView msglist;
    private ChatMsgViewAdapter chatMsgViewAdapter;
    private Button btn_send;
    private EditText sendmsg_edit;

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
//            System.out.println("接收到新消息");
            chatMsgViewAdapter.notifyDataSetChanged();
            msglist.smoothScrollToPosition(MyApplication.newsList.size() - 1);
        }
    }

    private void initView() {
        title_center= (TextView)view.findViewById(R.id.title_center);
        title_center.setText("消息");
        msglist= (ListView) view.findViewById(R.id.msglist);
        chatMsgViewAdapter =new ChatMsgViewAdapter(getActivity(), MyApplication.newsList);
        msglist.setAdapter(chatMsgViewAdapter);
        btn_send= (Button) view.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        sendmsg_edit= (EditText) view.findViewById(R.id.sendmsg_edit);
        msglist.smoothScrollToPosition(MyApplication.newsList.size() - 1);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_send:
                String msgstr=sendmsg_edit.getText().toString().trim();
                if(msgstr.isEmpty()){
                    ToastUtil.showToast(getActivity(),"发送信息不能为空！");
                }else{
                    sendMessage(msgstr);
                }
                break;
        }
    }

    private void sendMessage(String msg) {
        NewsBean newsBean=new NewsBean();
        newsBean.setSubmitTime(Dateutil.getStrDate());
        newsBean.setContext(msg);
        newsBean.setIsme(true);
        MyApplication.newsList.add(newsBean);
//        chatMsgViewAdapter.notifyDataSetChanged();
        chatMsgViewAdapter =new ChatMsgViewAdapter(getActivity(), MyApplication.newsList);
        msglist.setAdapter(chatMsgViewAdapter);
        ToastUtil.showToast(getActivity(),MyApplication.newsList.get(MyApplication.newsList.size()-1).getContext());
        msglist.smoothScrollToPosition(MyApplication.newsList.size() - 1);
//        for (int i=0;i<MyApplication.newsList.size();i++){
//            System.out.println("5555555555555555555=="+MyApplication.newsList.get);
//        }
            //清空输入框
        sendmsg_edit.setText("");
    }
}
//    private int id;
//    private String submitTime;
//    private Object sendTime;
//    private int submitUserId;
//    private int acceptUserId;
//    private String context;
//    private boolean isme;