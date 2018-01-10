package com.example.xxd.qlbisai.ui.jnr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xxd.qlbisai.R;
import com.example.xxd.qlbisai.myBean.JNRBean;
import com.example.xxd.qlbisai.ui.oneFragment;

import cn.bmob.v3.listener.DeleteListener;

/**
 * Created by xxd on 2017/7/31.
 */

public class JNRChangeActivity extends AppCompatActivity {
    private TextView title,day,time;
    private ProgressDialog loadingDialog;
    Intent intent;
    private ImageView big_bb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_change_jnr);
        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();
        intent=getIntent();
        initView();

    }

    private void initView() {
        big_bb=(ImageView)findViewById(R.id.big_bb);
        loadingDialog = new ProgressDialog(JNRChangeActivity.this);
        loadingDialog.setMessage("正在删除, 请稍候...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(true);
        title=(TextView)findViewById(R.id.tv_title);
        day=(TextView)findViewById(R.id.tv_day);
        time=(TextView)findViewById(R.id.tv_time);
        title.setText(intent.getStringExtra("title"));
        day.setText(intent.getStringExtra("day"));
        time.setText(intent.getStringExtra("time"));
        switch (intent.getIntExtra("index",1)){
            case 0:
                if(intent.getStringExtra("path").equals("no")){
                    big_bb.setImageResource(R.drawable.bb_0);
                }else {
                    Bitmap b= BitmapFactory.decodeFile(intent.getStringExtra("path"));
                    big_bb.setImageBitmap(b);
                }
                break;
            case 1:
                big_bb.setImageResource(R.drawable.bb_0);
                break;
            case 2:
                big_bb.setImageResource(R.drawable.bb_1);
                break;
            case 3:
                big_bb.setImageResource(R.drawable.bb_2);
                break;
            case 4:
                big_bb.setImageResource(R.drawable.bb_3);
                break;
            case 5:
                big_bb.setImageResource(R.drawable.bb_4);
                break;
            case 6:
                big_bb.setImageResource(R.drawable.bb_5);
                break;
        }

    }

    public void delete(View view){
        loadingDialog.show();
        JNRBean jnrbean = new JNRBean();
        jnrbean.setObjectId(intent.getStringExtra("id"));
        jnrbean.delete(JNRChangeActivity.this, new DeleteListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Intent intent1 = new Intent();
                setResult(RESULT_OK, intent1);
                finish();
                loadingDialog.dismiss();
                Log.i("bmob","删除成功");
                oneFragment.update=true;
            }
            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                loadingDialog.dismiss();
                Log.i("bmob","删除失败："+msg);
            }
        });
    }

    public void back(View view){
        finish();
    }
}
