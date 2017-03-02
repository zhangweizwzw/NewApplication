package com.bj.yt.newapplication.utils;

import com.bj.yt.newapplication.config.UserBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wxixis on 2017/3/2.
 */

public class JasonUtils {
    public static Map<String,List<UserBean>> getUserJson(String s){
        Map<String,List<UserBean>> map=new HashMap<>();
        JSONObject jsonObject=null;
        try {
            /**
             * {"id":"0",
             * "list":[
             * {"id":7,
             * "submitTime":"2017-03-02T03:18:23.152+0000",
             * "sendTime":null,
             * "submitUserId":5,
             * "acceptUserId":0,
             * "context":"15"},
             * {"id":8,"submitTime":"2017-03-02T03:18:23.152+0000",
             * "sendTime":null,
             * "submitUserId":55,
             * "acceptUserId":0,"context":"4654"},
             * {"id":5,"submitTime":"2017-03-02T03:18:23.152+0000",
             * "sendTime":null,
             * "submitUserId":5,
             * "acceptUserId":0,
             * "context":"c"}
             * ]
             * }
             */
            jsonObject=new JSONObject(s);
            String idString=jsonObject.optString("id");
            JSONArray jsonArray=jsonObject.optJSONArray("list");
            List<UserBean> userList=new ArrayList<>();
            for (int i = 0; i <jsonArray.length() ; i++) {
                JSONObject jsonObject2=jsonArray.optJSONObject(i);
                UserBean userBean=new UserBean();
                int id=jsonObject2.optInt("id");
                String submitTime=jsonObject2.optString("submitTime");
                String sendTime=jsonObject2.optString("sendTime");
                int submitUserId=jsonObject2.optInt("submitUserId");
                int acceptUserId=jsonObject2.optInt("acceptUserId");
                String context=jsonObject2.optString("context");
                userBean.setId(id);
                userBean.setSubmitTime(submitTime);
                userBean.setSendTime(sendTime);
                userBean.setSubmitUserId(submitUserId);
                userBean.setAcceptUserId(acceptUserId);
                userBean.setContext(context);
                userList.add(userBean);
            }
            map.put(idString,userList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return map;
    }
}
