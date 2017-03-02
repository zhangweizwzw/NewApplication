package com.bj.yt.newapplication.utils;

import com.bj.yt.newapplication.config.UserBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wxixis on 2017/3/2.
 */

public class JasonUtils {
    public static UserBean getUserJson(String s){
        JSONObject jsonObject=null;
        try {
            jsonObject=new JSONObject(s);
            JSONArray jsonArray=jsonObject.getJSONArray("list");
            for (int i = 0; i <jsonArray.length() ; i++) {
                JSONObject jsonObject2=jsonArray.getJSONObject(i);
                UserBean userBean=new UserBean();
                int id=jsonObject2.getInt("id");
                String submitTime=jsonObject2.getString("submitTime");
                String sendTime=jsonObject2.getString("sendTime");
                int submitUserId=jsonObject2.getInt("submitUserId");
                int acceptUserId=jsonObject2.getInt("acceptUserId");
                String context=jsonObject2.getString("context");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
