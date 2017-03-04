package com.bj.yt.newapplication.fragment;

import android.os.Bundle;
import android.util.Log;
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
import com.bj.yt.newapplication.bean.NewsBean;
import com.bj.yt.newapplication.util.Dateutil;
import com.bj.yt.newapplication.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MessageFragment extends BaseFragment implements View.OnClickListener{
    private String TAG="MessageFragment";
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

    private void initView() {
        title_center= (TextView)view.findViewById(R.id.title_center);
        title_center.setText("消息");
        msglist= (ListView) view.findViewById(R.id.msglist);
        btn_send= (Button) view.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        chatMsgViewAdapter =new ChatMsgViewAdapter(getActivity(), MyApplication.newsList);
        msglist.setAdapter(chatMsgViewAdapter);
        msglist.setSelection(msglist.getCount() - 1);//数据移动到最后一行
        sendmsg_edit= (EditText) view.findViewById(R.id.sendmsg_edit);
    }

    /**
     * 接收消息跟新发过来的广播
     * @param event
     */
    @Subscribe
    public void onEventMainThread(MessageEvent event){
        if(event.message.toString().equals("newmessage")){
            Log.i(TAG,"查询消息有更新");
            updateDate();
        }else if(event.message.toString().equals("loginNewmessage")){
            Log.i(TAG,"登录界面消息更新");
            updateDate();
        }
    }

    /**
     * 更新消息
     */
    public void updateDate(){
        chatMsgViewAdapter.notifyDataSetChanged();
        msglist.setSelection(msglist.getCount() - 1);
        ToastUtil.showToast(getActivity(),MyApplication.newsList.get(MyApplication.newsList.size()-1).getContext());
        ToastUtil.showToast(getActivity(),MyApplication.newsList.size()+"");
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

    /**
     * 自己发送消息处理
     * @param msg
     */
    private void sendMessage(String msg) {
        NewsBean newsBean=new NewsBean();
        newsBean.setSubmitTime(Dateutil.getStrDate());
        newsBean.setContext(msg);
        newsBean.setIsme(true);
        MyApplication.newsList.add(newsBean);
        updateDate();
        sendmsg_edit.setText("");
    }
}