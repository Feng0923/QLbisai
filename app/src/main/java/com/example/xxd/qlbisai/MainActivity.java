package com.example.xxd.qlbisai;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.example.xxd.qlbisai.myBean.MyPlanBean;
import com.example.xxd.qlbisai.myBean.MyUserBean;
import com.example.xxd.qlbisai.ui.FourFragment;
import com.example.xxd.qlbisai.ui.oneFragment;
import com.example.xxd.qlbisai.ui.ThreeFragment;
import com.example.xxd.qlbisai.ui.TwoFragment;

import java.util.Date;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class MainActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private ImageView[] guides;
    com.example.xxd.qlbisai.ui.oneFragment oneFragment;
    TwoFragment twoFragment;
    ThreeFragment thereFragment;
    FourFragment fourFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);
        initView();
        initGuide();
    }



    private void initGuide() {
        guides=new ImageView[linearLayout.getChildCount()];
        for(int i=0;i<guides.length;i++){
            guides[i]=(ImageView)linearLayout.getChildAt(i);
            guides[i].setOnClickListener(new MyOnClick(i));
        }
        guides[0].setImageResource(R.mipmap.calandar_select);
    }

    private void initView() {
        oneFragment=new oneFragment();
        twoFragment=new TwoFragment();
        thereFragment=new ThreeFragment();
        fourFragment=new FourFragment();
        replaceFragment(oneFragment);
        linearLayout=(LinearLayout)findViewById(R.id.ll_tap);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_main,fragment);
        transaction.commit();
    }

    public void setIndex(int index){
        guides[0].setImageResource(R.mipmap.calandar);
        guides[1].setImageResource(R.mipmap.daojishi);
        guides[2].setImageResource(R.mipmap.plan);
        guides[3].setImageResource(R.mipmap.other);
        switch(index){
            case 0:
                guides[0].setImageResource(R.mipmap.calandar_select);
                replaceFragment(oneFragment);
                break;
            case 1:
                guides[1].setImageResource(R.mipmap.daojishi_select);
                replaceFragment(twoFragment);
                break;
            case 2:
                guides[2].setImageResource(R.mipmap.plan_select);
                replaceFragment(thereFragment);
                break;
            case 3:
                guides[3].setImageResource(R.mipmap.other_select);
                replaceFragment(fourFragment);
                break;

        }

    }



    class MyOnClick implements View.OnClickListener{
        private int in;
        public MyOnClick(int index){
            this.in=index;
        }

        @Override
        public void onClick(View v) {
            setIndex(in);
        }
    }


}
