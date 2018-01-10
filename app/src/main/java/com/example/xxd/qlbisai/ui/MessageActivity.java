package com.example.xxd.qlbisai.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.xxd.qlbisai.R;

/**
 * Created by xxd on 2017/7/27.
 */

public class MessageActivity extends AppCompatActivity {
    private TextView tv_name,tv_sex,tv_age,tv_birthday,tv_city;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        initView();
    }

    private void initView() {
        tv_name=(TextView)findViewById(R.id.tv_name);
        tv_sex=(TextView)findViewById(R.id.tv_sex);
        tv_age=(TextView)findViewById(R.id.tv_age);
        tv_birthday=(TextView)findViewById(R.id.tv_birthday);
        tv_city=(TextView)findViewById(R.id.tv_city);
        Intent i=getIntent();
        tv_name.setText(i.getStringExtra("name"));
        tv_sex.setText(i.getStringExtra("sex"));
        tv_age.setText(i.getStringExtra("age"));
        tv_birthday.setText(i.getStringExtra("birthday"));
        tv_city.setText(i.getStringExtra("city"));

    }

    public void back(View view){
        finish();
    }

}
