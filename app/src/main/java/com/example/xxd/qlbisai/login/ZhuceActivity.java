package com.example.xxd.qlbisai.login;

import android.content.Intent;
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
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

/**
 * Created by xxd on 2017/7/19.
 */

public class ZhuceActivity extends AppCompatActivity {
    private Button button;
    private EditText phone_edt,yz_edt,password_edt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_zhuce123);
        initView();
    }

    private void initView() {
        password_edt=(EditText)findViewById(R.id.editText3);
        yz_edt=(EditText)findViewById(R.id.editText9);
        phone_edt=(EditText)findViewById(R.id.editText);
        button=(Button)findViewById(R.id.getcode);

    }




    public void getMa(View view){
        button.setClickable(false);
        String number=phone_edt.getText().toString();
        if(number.length()!=11){
            Toast.makeText(this,"手机号码长度不对",Toast.LENGTH_SHORT).show();
        }else {

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


            BmobSMS.requestSMSCode(this, number,"Timer", new RequestSMSCodeListener() {
                @Override
                public void done(Integer smsId,BmobException ex) {
                    if(ex==null){//验证码发送成功
                        Log.e("smile", "短信id："+smsId);//用于查询本次短信发送详情
                    }
                }
            });



        }
    }

    public void back(View view){
        finish();
    }
    public void complete(View view){
        final String password=password_edt.getText().toString();
        final String number=phone_edt.getText().toString();
        String ma=yz_edt.getText().toString();
        if(number.length()!=11){
            Toast.makeText(this,"手机号码错误",Toast.LENGTH_SHORT).show();
            return;
        }
        if(ma.length()==0){
            Toast.makeText(this,"验证码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length()==0){
            Toast.makeText(this,"密码不能位空",Toast.LENGTH_SHORT).show();
            return;
        }
        BmobSMS.verifySmsCode(ZhuceActivity.this,number,ma , new VerifySMSCodeListener() {

            @Override
            public void done(BmobException ex) {
                if(ex==null){

                    Toast.makeText(ZhuceActivity.this,"验证通过",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(ZhuceActivity.this,CompleteActivity.class);
                    intent.putExtra("phone",number);
                    intent.putExtra("password",password);
                    startActivityForResult(intent,1);
                }else{
                Toast.makeText(ZhuceActivity.this,"验证失败：code ="+ex.getErrorCode()+",msg = "+ex.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    finish();
                }
        }
    }
}
