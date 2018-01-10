package com.example.xxd.qlbisai.ui.firsthistory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xxd.qlbisai.R;
import com.example.xxd.qlbisai.RiLi.mLabel;
import com.example.xxd.qlbisai.RiLi.mTextView;
import com.example.xxd.qlbisai.RiLi.mTextView2;
import com.example.xxd.qlbisai.myBean.AffairBean;
import com.example.xxd.qlbisai.myBean.MyPlanBean;
import com.example.xxd.qlbisai.myBean.MyUserBean;
import com.example.xxd.qlbisai.myBean.ThingBean;
import com.example.xxd.qlbisai.ui.AffairControler;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by xxd on 2017/8/11.
 */

public class PlanActivity extends AppCompatActivity{

        private String month,day,week;
        private TextView tv_month,tv_day,tv_week,tv_affairEdit ;
        public mTextView[][] t = new mTextView[18][3];
        private GridLayout gl_timePanel;
        private LinearLayout ll_labelPanel;
        private ScrollView sl1,sl2;

        boolean flag=true;
        public  boolean isget=false;
        private AffairControler affairControler;
        private mTextView2 tv_work,tv_move;
        public static View.OnTouchListener listener;
        private String date;

        private String editorPage = "com.example.xxd.qlbisai.ui.AffairsCategory";
        public MyPlanBean planBean=new MyPlanBean() ;
        public MyUserBean user;

        public ThingBean[][] things = new ThingBean[t.length][t[0].length-1];


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.thirdfragment);
        user=BmobUser.getCurrentUser(this,MyUserBean.class);
        date=getIntent().getStringExtra("date");
        initView();

        if(flag){
            flag=false;
            BmobQuery<MyPlanBean> query = new BmobQuery<MyPlanBean>();
            query.addWhereEqualTo("date", date);
            query.addWhereEqualTo("name", user.getUsername());
            query.setLimit(50);
            query.findObjects(this, new FindListener<MyPlanBean>() {
                @Override
                public void onSuccess(List<MyPlanBean> object) {
                    if(object!=null){
                        List<MyPlanBean> lists=new ArrayList<MyPlanBean>();
                        for(MyPlanBean person:object){
                            lists.add(person);
                        }

                        if(lists.size()==0){
                        }else {
                            planBean=lists.get(0);
                            things=planBean.getThings();
                            isget=true;
                            update();
                            Log.d("ninininini", "onSuccess: ");
                        }


                    }

                }
                @Override
                public void onError(int code, String msg) {
                    isget=false;
                    Toast.makeText(PlanActivity.this,"网络连接异常",Toast.LENGTH_SHORT).show();
                }
            });

        }else {
            if(things!=null) {
                update();
            }
        }

    }


        private void initView() {
            setWH();
            setlistener();
            tv_month = (TextView) findViewById(R.id.tv_month);
            tv_day = (TextView) findViewById(R.id.tv_day);
            tv_week = (TextView) findViewById(R.id.tv_week);
            initTime();
            tv_affairEdit = (TextView) findViewById(R.id.tv_affairEdit);

            gl_timePanel = (GridLayout) findViewById(R.id.gl_timePanel);
            ll_labelPanel = (LinearLayout) findViewById(R.id.ll_labelPanel);
            tv_move = (mTextView2) findViewById(R.id.tv_move);
            sl1 = (ScrollView) findViewById(R.id.sl1);
            sl2 = (ScrollView) findViewById(R.id.sl2);
            setGirdList();
            affairControler = new AffairControler(this, handler);
            setLabelList();

            tv_affairEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClassName(PlanActivity.this,editorPage);
                    startActivityForResult(intent,1);
                }
            });


        }

    private void initTime() {
        Intent intent=getIntent();
        tv_month.setText(intent.getStringExtra("month"));
        tv_week.setText(intent.getStringExtra("week"));
        tv_day.setText(intent.getStringExtra("day"));
    }


    public void getResult(){

            for(int i = 0;i<t.length;i++){
                for (int j=1;j<t[i].length;j++){
                    String text = (String)t[i][j].getText();
                    int color = t[i][j].getColor();
                    ThingBean thing = new ThingBean(text,color);
                    things[i][j-1] = thing;
                }
            }

        }







        public void update(){
            setColor();
            for(int i = 0;i<t.length;i++){
                for(int j = 1 ;j<t[i].length;j++){
                    if(things[i][j-1]!=null) {
                        String text = things[i][j - 1].getDone();
                        int color = things[i][j - 1].getColor();
                        if (!text.isEmpty()) {
                            t[i][j].setText(text);
                            t[i][j].setColor(color);

                        }
                    }
                }
            }
        }




    ProgressDialog p;
    private void setLabelList() {
        affairControler.get();
        p = new ProgressDialog(this);
        p.setMessage("正在加载");
        p.setCanceledOnTouchOutside(false);
        p.show();
    }
        private final Handler handler= new Handler(){
            public void handleMessage(Message msg) {
                if(msg.what == 2){
                    List<AffairBean> l = affairControler.getAfs();
                    ll_labelPanel.removeAllViews();
                    for (int i = 0; i < l.size(); i++) {
                        mLabel m = new mLabel(PlanActivity.this,l.get(i),PlanActivity.class.getName());
                        ll_labelPanel.addView(m);
                    }
                    p.dismiss();
                }
            }
        };

        public static View.OnTouchListener getListener(){
            return listener;
        }
        private void setlistener() {
            listener = new View.OnTouchListener() {
                int startX = 0,startY = 0,l;
                float x = 0,y= 0;
                public boolean onTouch(View v, MotionEvent event) {
                    final int X = (int) event.getRawX();
                    final int Y = (int) event.getRawY();
                    int action = event.getAction();
                    setScroll(false);
                    if (action == MotionEvent.ACTION_DOWN) {
                        x = event.getX();
                        y = event.getY();
                        startX = (int) (X - event.getRawX());
                        startY = (int) (Y - event.getRawY());
                        show((mTextView2) v, startX, startY);
                    } else if (action == MotionEvent.ACTION_MOVE) {
                        l = (int) event.getY();
                        int dx = (int) (event.getX() - x);
//                    tv_move.setVisibility(View.VISIBLE);
                        FrameLayout.LayoutParams params3 = (FrameLayout.LayoutParams) tv_move.getLayoutParams();
                        params3.leftMargin = (int) (X - startX);
                        params3.topMargin = (int) (Y - startY);
                        params3.rightMargin = (int) (X - startX);
                        params3.bottomMargin = (int) (Y - startY);
                        tv_move.setLayoutParams(params3);
                        move(tv_move.getTop(), dx, Y - startY);
                    } else if (action == MotionEvent.ACTION_UP) {
                        if(i>=0&&i<t.length&&j>0) {
                            t[i][j].setChange(false);
                            setScroll(true);
                        }
//                    tv_move.setVisibility(View.INVISIBLE);
                    }
                    return true;
                }
            };
        }
        private void setScroll(final boolean b) {
            sl1.requestDisallowInterceptTouchEvent(!b);
            sl2.requestDisallowInterceptTouchEvent(!b);
        }


        private void setWH() {

            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);


            width1 = (dm.widthPixels)/6-30;
            width2 = width1+30;
            height = (dm.heightPixels)/(t.length+1);
        }

        int width1 ;
        int width2 ;
        int height ;
        private void setGirdList() {
            int time = 6;
            for(int i=0;i<t.length;i++) {
                for (int j = 0; j < t[i].length; j++) {
                    if (j == 0) {
                        ViewGroup.LayoutParams params_ = new ViewGroup.LayoutParams(width1,height);
                        t[i][j] = new mTextView(this,time+" : 00");
                        time++;
                        t[i][j].setLayoutParams(params_);
                        t[i][j].setColor(0);
                        gl_timePanel.addView(t[i][j]);
                    } else {
                        ViewGroup.LayoutParams params_ = new ViewGroup.LayoutParams(width2,height);
                        GridLayout.LayoutParams params = new GridLayout.LayoutParams(params_);
                        params.topMargin=5;
                        t[i][j]=new mTextView(this,"");
                        t[i][j].setLayoutParams(params);
                        t[i][j].setColor(getResources().getColor(R.color.gray));
                        t[i][j].setOnTouchListener(new Remove(i,j));
                        gl_timePanel.addView(t[i][j]);
                    }
                }
            }
        }

        class Remove implements View.OnTouchListener{
            int i,j; int x=0,y=0,dx,dy;
            public Remove(int i,int j){
                this.i = i;
                this.j = j;
            }

            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() ==  MotionEvent.ACTION_DOWN){
                    x=(int)event.getX();
                    y=(int)event.getY();
                }else if(event.getAction() == MotionEvent.ACTION_MOVE){
                    dx = (int) (event.getX()-x);
                    dy = (int) (event.getY()-y);
                    if(Math.abs(dx)>50){
                        setScroll(false);
                        t[i][j].setChange(true);
                        if( i<t.length&&i>=0&&j>0) {
                            t[i][j].register(i, j);
                        }
                    }
                    if(Math.abs(dx)<50){
                        setScroll(true);
                    }
                }
                return true;
            }
        }

        public void show(mTextView2 v,int x,int y) {
            int[] location = new int[2];
            v.getLocationInWindow(location);
            tv_move.layout(x,y,x-v.getWidth(),y-v.getHeight());
            tv_move.setHeight(height);
            tv_move.setText(v.getText());
            tv_move.setColor((v).getColor());
        }
        int i=0,j=0;
        private void move(int l, int dx, int dy) {
            if( i<t.length&&i>=0&&j>0) {
                t[i][j].register(i,j);
            }
            i=l/height-1;
            if(dx<-(60)&&dx>-(width2+height)){
                tv_move.setVisibility(View.INVISIBLE);
                j=2;
            }else if(dx<-(width2+height)&&dx>-(2*width2+height)){
//            tv_move.setVisibility(View.INVISIBLE);
                j=1;
            }else if(Math.abs(dx)<60){
                setScroll(true);
//                tv_move.setVisibility(View.INVISIBLE);
                i=0;j=0;
            }
            else{
//            tv_move.setVisibility(View.VISIBLE);
                i=0;j=0;

            }
            if(i<t.length&&i>=0&&j<t[i].length&&j>0) {
                t[i][j].setText((String)tv_move.getText());
                t[i][j].setColor(tv_move.getColor());

            }
        }


        @Override
        public void onPause() {
            super.onPause();
            getResult();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            Log.d("ninini", "onDestroy: ");
            if(isget){
                if(planBean.getObjectId()==null){
                    Toast.makeText(this,"网络异常",Toast.LENGTH_SHORT).show();
                    return;
                }
                MyPlanBean myPlanBean = new MyPlanBean();
                myPlanBean.setThings(things);
                myPlanBean.setName(user.getUsername());
                myPlanBean.update(this, planBean.getObjectId(), new UpdateListener() {

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        Toast.makeText(PlanActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                MyPlanBean myPlanBean = new MyPlanBean();
                myPlanBean.setThings(things);
                myPlanBean.setDate(date);
                myPlanBean.setName(user.getUsername());
                myPlanBean.save(this, new SaveListener() {

                    @Override
                    public void onSuccess() {
                        isget=true;
                        flag=true;

                    }

                    @Override
                    public void onFailure(int code, String arg0) {
                        Toast.makeText(PlanActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
        String [] s={"上班",
                "学习",
                "阅读",
                "运动",
                "玩游戏",
                "看电视",
                "睡觉"  };


        int[] colors;
        private void setColor(){
            colors = new int[] {
                    getResources().getColor(R.color.red),
                    getResources().getColor(R.color.colorAccent),
                    getResources().getColor(R.color.yellow),
                    getResources().getColor(R.color.green),
                    getResources().getColor(R.color.blue),
                    getResources().getColor(R.color.colorPrimaryDark),
                    getResources().getColor(R.color.purse)
            };
        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    affairControler.get();
                }
        }
    }
}
