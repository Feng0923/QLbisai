package com.example.xxd.qlbisai.login;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xxd.qlbisai.R;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.ResetPasswordByCodeListener;


/**
 * Created by xxd on 2017/7/19.
 */

public class ForgetActivity extends AppCompatActivity {
    private EditText ed_phoneNumber,ed_yanma,ed_newPassword;
    private ProgressDialog loadingDialog;
    private Button button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_forgot);

        initView();
    }

    private void initView() {
        ed_phoneNumber=(EditText)findViewById(R.id.change_number);
        ed_yanma=(EditText)findViewById(R.id.change_ma);
        ed_newPassword=(EditText)findViewById(R.id.change_password);
        button=(Button)findViewById(R.id.Sendcode);
        loadingDialog = new ProgressDialog(ForgetActivity.this);
        loadingDialog.setMessage("正在请求服务器, 请稍候...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(true);
    }

    public void getCode(View view){
        String number=ed_phoneNumber.getText().toString();
        if(number.length()!=11){
            Toast.makeText(ForgetActivity.this,"电话号码有误",Toast.LENGTH_SHORT).show();
            return;
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                int i=60;
                while (i>0){
                    i=i-1;
                    final int finalI = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            button.setText(finalI +"s后再次获取");
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        button.setClickable(true);
                        button.setText("获取验证码");
                    }
                });


            }
        }).start();

        BmobSMS.requestSMSCode(ForgetActivity.this, number,"Timer", new RequestSMSCodeListener() {

            @Override
            public void done(Integer smsId,BmobException ex) {
                // TODO Auto-generated method stub
                if(ex==null){//验证码发送成功
                    Log.i("smile", "短信id："+smsId);//用于查询本次短信发送详情
                }
            }
        });
    }

    public void cgPassword(View view){


        String number=ed_phoneNumber.getText().toString();
        String password=ed_newPassword.getText().toString();
        String ma=ed_yanma.getText().toString();
        if(number.length()!=11){
            Toast.makeText(ForgetActivity.this,"电话号码有误",Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.length()==0){
            Toast.makeText(ForgetActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }

        if(ma.length()==0){
            Toast.makeText(ForgetActivity.this,"验证码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        loadingDialog.show();

        BmobUser.resetPasswordBySMSCode(ForgetActivity.this, ma,password, new ResetPasswordByCodeListener() {

            @Override
            public void done(cn.bmob.v3.exception.BmobException e) {
                if (e == null) {
                    loadingDialog.dismiss();
                    Toast.makeText(ForgetActivity.this, "密码重置成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    loadingDialog.dismiss();
                    Toast.makeText(ForgetActivity.this, "重置失败：code =" + e.getErrorCode() + ",msg = " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            ;
        });
    }




    public void back(View view){
        finish();
    }
}
