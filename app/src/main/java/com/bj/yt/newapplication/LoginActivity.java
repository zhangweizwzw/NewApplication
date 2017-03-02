package com.bj.yt.newapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bj.yt.newapplication.config.Strings;
import com.bj.yt.newapplication.config.UserBean;
import com.bj.yt.newapplication.utils.JasonUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_reset,btn_login;
    private TextView title_center;
    private EditText et_username,et_password;
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_reset:
                et_username.setText("");
                et_password.setText("");
                break;
            case R.id.btn_login:
                String username=et_username.getText().toString().trim();
                String password=et_password.getText().toString().trim();
                //用户名或密码为空
                if (TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, Strings.LOGIN_NULL,Toast.LENGTH_SHORT).show();
                }else{
                    //用户登录
                    OkHttpUtils
                            .post()
                            .url(Strings.LOGIN_URL+"login")
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
                            Map<String,List<UserBean>> map= JasonUtils.getUserJson(response);
                            if (map!=null){
                                Set set=map.keySet();
                                Iterator iter = set.iterator();
                                while (iter.hasNext()) {
                                    String key = (String) iter.next();
                                    if (key!=null&&key.equals("0")){
                                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                    }else{
                                        Toast.makeText(LoginActivity.this,Strings.LOGIN_Fail,Toast.LENGTH_SHORT).show();
                                    }
                                }
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
