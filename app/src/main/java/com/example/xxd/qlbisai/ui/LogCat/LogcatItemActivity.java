package com.example.xxd.qlbisai.ui.LogCat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xxd.qlbisai.R;
import com.example.xxd.qlbisai.myBean.LogcatBean;
import com.example.xxd.qlbisai.myBean.MyUserBean;

import java.util.Date;

import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by xxd on 2017/7/27.
 */

public class LogcatItemActivity extends AppCompatActivity {
    private EditText editText;
    private TextView tv_month;
    MyUserBean user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_item);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        user = BmobUser.getCurrentUser(this,MyUserBean.class);
        editText=(EditText)findViewById(R.id.edt_logcat);
        tv_month=(TextView)findViewById(R.id.tv_month);
        if(getIntent().getStringExtra("what").equals("have")){
            editText.setText(getIntent().getStringExtra("message"));
            tv_month.setText(getIntent().getStringExtra("time"));
        }else {
            Date date=new Date();
            Log.d("???", "onCreate: ");
            tv_month.setText(date.getYear()+1900+"年"+date.getMonth()+"月"+date.getDate()+"日");
        }
    }


    public void ok(View view){
        if(getIntent().getStringExtra("what").equals("have")){

            LogcatBean logcatBean = new LogcatBean();
            logcatBean.setLogcat(editText.getText().toString());
            logcatBean.update(this, getIntent().getStringExtra("id"), new UpdateListener() {

                @Override
                public void onSuccess() {
                    Toast.makeText(LogcatItemActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }

                @Override
                public void onFailure(int code, String msg) {
                    Toast.makeText(LogcatItemActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                }
            });

        }else {
            LogcatBean logcatBean = new LogcatBean();
            logcatBean.setLogcat(editText.getText().toString());
            logcatBean.setName(user.getUsername());
            logcatBean.setDelete(false);
            logcatBean.save(this, new SaveListener() {

                @Override
                public void onSuccess() {
                    Toast.makeText(LogcatItemActivity.this,"保存成功",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }

                @Override
                public void onFailure(int code, String arg0) {
                    Toast.makeText(LogcatItemActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    public void back(View view){
        finish();
    }

}
