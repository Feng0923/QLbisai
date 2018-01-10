package com.example.xxd.qlbisai.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import com.example.xxd.qlbisai.R;
import com.example.xxd.qlbisai.RiLi.RootLayout;
import com.example.xxd.qlbisai.myBean.AffairBean;

import java.util.List;

/**
 * Created by 梁胜峰1 on 2017/7/24.
 */

public class AffairsCategory extends Activity {
    private Context context = this;
    private LinearLayout ll_mainPanel;
    private List<AffairBean> l;
    private AffairControler affairControler;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        initView();
    }

    private void initView() {
        ll_mainPanel = (LinearLayout) findViewById(R.id.ll_mainPanel);
        affairControler = new AffairControler(this,handler);
        affairControler.get();
    }
    private final Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            if(msg.what == 2){
                l = affairControler.getAfs();
                for(int i=0;i<l.size();i++){
                    RootLayout rl = new RootLayout(context,l.get(i),affairControler);
                    ll_mainPanel.addView(rl);
                }
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void addRoot() {
//       String[][] s =  new String[][]{{"上班", "主要工作", "业余工作", "其他工作"},
//                {"学习", "读书", "作业", "其他学习"},
//                {"阅读", "散文", "名著", "其他阅读"},
//               {"运动","健身","慢跑"},
//                {"玩游戏","LOL","CF","DNF","其他游戏"},
//                {"看电视"},
//                {"睡觉"}};
//        int[] color = new int[]{
//                getResources().getColor(R.color.red),
//                getResources().getColor(R.color.colorAccent),
//                getResources().getColor(R.color.yellow),
//                getResources().getColor(R.color.green),
//                getResources().getColor(R.color.blue),
//                getResources().getColor(R.color.colorPrimaryDark),
//                getResources().getColor(R.color.purse)
//        };

//        for(int i=0;i<l.size();i++){
//            RootLayout rl = new RootLayout(this,l.get(i));
//            ll_mainPanel.addView(rl);
//        }
    }
}
