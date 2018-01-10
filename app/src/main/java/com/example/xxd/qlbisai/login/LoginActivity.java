package com.example.xxd.qlbisai.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xxd.qlbisai.MainActivity;
import com.example.xxd.qlbisai.R;
import com.example.xxd.qlbisai.myBean.MyUserBean;

import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by xxd on 2017/7/19.
 */

public class LoginActivity extends AppCompatActivity {
    private EditText number_edt;
    private EditText password_edt;
    private ProgressDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyUserBean user = BmobUser.getCurrentUser(this,MyUserBean.class);

        if(user!=null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        number_edt = (EditText) findViewById(R.id.Login_TextView_phone);
        password_edt = (EditText) findViewById(R.id.Login_EditText_password);
        loadingDialog = new ProgressDialog(LoginActivity.this);
        loadingDialog.setMessage("正在登录, 请稍候...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(true);
        number_edt.setText(getIntent().getStringExtra("name"));

    }

    public void back(View view){
        finish();
    }


    public void dele(View view){
        number_edt.setText("");
    }

    public void delet(View view){
        password_edt.setText("");
    }


    public void sigin(View view){
        Intent intent=new Intent(this,ZhuceActivity.class);
        startActivity(intent);
    }



    public void Login(View view){


        final String number=number_edt.getText().toString();
        final String password=password_edt.getText().toString();
        if(number.length()!=11){
            Toast.makeText(LoginActivity.this,"电话号码不正确",Toast.LENGTH_SHORT).show();
            return;
        }
        loadingDialog.show();
        BmobUser bu2 = new BmobUser();
        bu2.setUsername(number);
        bu2.setPassword(password);
        bu2.login(this, new SaveListener() {
            @Override
            public void onSuccess() {
                loadingDialog.dismiss();
                Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();;
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtra("phone",number);
                intent.putExtra("password",password);
                startActivity(intent);
                finish();

            }
            @Override
            public void onFailure(int code, String msg) {
                loadingDialog.dismiss();
                Toast.makeText(LoginActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();;
            }
        });



    }




    public void forget(View view){
        Intent intent=new Intent(this,ForgetActivity.class);
        startActivity(intent);
    }

}
