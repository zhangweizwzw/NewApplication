package com.bj.yt.newapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bj.yt.newapplication.R;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_reset,btn_login;
    private TextView title_center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        btn_reset= (Button) findViewById(R.id.btn_reset);
        btn_login= (Button) findViewById(R.id.btn_login);
        btn_reset.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        title_center= (TextView) findViewById(R.id.title_center);
        title_center.setText("登录");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_reset:

                break;
            case R.id.btn_login:
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                break;
        }
    }
}
