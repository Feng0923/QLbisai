package com.example.xxd.qlbisai.ui;

import android.app.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.example.xxd.qlbisai.myBean.DayBean;
import com.example.xxd.qlbisai.myBean.ItemBean;
import com.example.xxd.qlbisai.myBean.JNRBean;
import com.example.xxd.qlbisai.myBean.LogcatBean;
import com.example.xxd.qlbisai.myBean.MyPlanBean;
import com.example.xxd.qlbisai.myBean.MyUserBean;
import com.example.xxd.qlbisai.myBean.ThingBean;
import com.example.xxd.qlbisai.ui.LogCat.LogcatItemActivity;
import com.example.xxd.qlbisai.ui.jnr.JNRActivity;
import com.example.xxd.qlbisai.ui.jnr.JNRAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


public class ThreeFragment extends Fragment {
    private TextView tv_month,tv_day,tv_week,tv_affairEdit ;
    public mTextView[][] t = new mTextView[18][3];
    private GridLayout gl_timePanel;
    private LinearLayout ll_labelPanel;
    private ScrollView sl1,sl2;
    private static DayBean dayBean ;
    private View v;
    boolean flag=true;
    public  boolean isget=false;
    private boolean isgetday=false;

    private AffairControler affairControler;
    private mTextView2 tv_work,tv_move;
   public static View.OnTouchListener listener;
    private boolean isfirst = false;
    private String editorPage = "com.example.xxd.qlbisai.ui.AffairsCategory";
    public MyPlanBean planBean=new MyPlanBean() ;
    public MyUserBean user;

    public ThingBean[][] things = new ThingBean[t.length][t[0].length-1];

    private Context context=getContext();



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getContext();
        user=BmobUser.getCurrentUser(getContext(),MyUserBean.class);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.thirdfragment,null);

        initView();

        if(flag){
            flag=false;
            Date d=new Date();
            BmobQuery<MyPlanBean> query = new BmobQuery<MyPlanBean>();
            query.addWhereEqualTo("date", d.getYear()+""+d.getMonth()+""+d.getDate()+"");
            query.addWhereEqualTo("name", user.getUsername());
            query.setLimit(50);
            query.findObjects(context, new FindListener<MyPlanBean>() {
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
                    Toast.makeText(context,"网络连接异常",Toast.LENGTH_SHORT).show();
                }
            });

            BmobQuery<DayBean>  query22 = new BmobQuery<>();
            query22.addWhereEqualTo("userName",user.getUsername());
            query22.addWhereEqualTo("time",d.getYear()+""+d.getMonth()+""+d.getDate()+"");
            query22.findObjects(context, new FindListener<DayBean>() {
                @Override
                public void onSuccess(List<DayBean> list) {
                    if(list.size()>0){
                        isgetday=true;
                    }
                    if(list!=null){
                        List<DayBean> lists=new ArrayList<DayBean>();
                        for(DayBean person:list){
                            lists.add(person);
                        }

                        if(lists.size()==0){
//                            isgetday = false;
                        }else {
                            dayBean=lists.get(0);
                            isgetday=true;
                        }


                    }


                    if(list.size()>0){
                        ThreeFragment.this.dayBean = list.get(0);

                    }else {

                    }
                }
                public void onError(int i, String s) {
                    Log.d("qwtqwt", "onError: "+s);
//                    isgetday=false;
                }
            });





        }else {
            if(things!=null) {
                update();
            }
        }



        return v;
    }

    private void initView() {
        setWH();
        setlistener();
        setItems();
        tv_month = (TextView) v.findViewById(R.id.tv_month);
        tv_day = (TextView) v.findViewById(R.id.tv_day);
        tv_week = (TextView) v.findViewById(R.id.tv_week);
        initTime();

        tv_affairEdit = (TextView) v.findViewById(R.id.tv_affairEdit);

        gl_timePanel = (GridLayout) v.findViewById(R.id.gl_timePanel);
        ll_labelPanel = (LinearLayout) v.findViewById(R.id.ll_labelPanel);
        tv_move = (mTextView2) v.findViewById(R.id.tv_move);
        sl1 = (ScrollView) v.findViewById(R.id.sl1);
        sl2 = (ScrollView) v.findViewById(R.id.sl2);
        setGirdList();
        affairControler = new AffairControler(context, handler);
        setLabelList();

        tv_affairEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName(context,editorPage);
                startActivityForResult(intent,1);
            }
        });


    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        Date date=new Date();
        tv_month.setText(date.getMonth()+1+"月");
        tv_day.setText(date.getDate()+"");
        calendar.set(Calendar.YEAR, date.getYear()+1900);//先指定年份
        calendar.set(Calendar.MONTH, date.getMonth());//再指定月份 Java月份从0开始算
        calendar.set(Calendar.DAY_OF_MONTH, date.getDate());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case 1:
                tv_week.setText("周日");
                break;
            case 2:
                tv_week.setText("周一");
                break;
            case 3:
                tv_week.setText("周二");
                break;
            case 4:
                tv_week.setText("周三");
                break;
            case 5:
                tv_week.setText("周四");
                break;
            case 6:
                tv_week.setText("周五");
                break;
            case 7:
                tv_week.setText("周六");
                break;

        }
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
        ItemBean[] items = new ItemBean[7];
        this.items = items;
        setColor();
        for(int i = 0;i<items.length;i++){
            ItemBean item = new ItemBean(s[i],colors[i]);
            items[i] = item;
        }
        for(int i = 0;i<t.length;i++){
            for(int j = 1 ;j<t[i].length;j++){
                if(things[i][j-1]!=null) {
                    String text = things[i][j - 1].getDone();
                    int color = things[i][j - 1].getColor();
                    if (!text.isEmpty()) {
                        t[i][j].setText(text);
                        t[i][j].setColor(color);
                        setDayDate(color,0.5f);
                    }
                }
            }
        }
    }



        ProgressDialog  p;
    private void setLabelList() {
        affairControler.get();
        p = new ProgressDialog(context);
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
                    mLabel m = new mLabel(context,l.get(i),ThreeFragment.class.getName());
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

        Activity ac=(Activity) context;
        DisplayMetrics dm = new DisplayMetrics();
        ac.getWindowManager().getDefaultDisplay().getMetrics(dm);


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
                    t[i][j] = new mTextView(context,time+" : 00");
                    time++;
                    t[i][j].setLayoutParams(params_);
                    t[i][j].setColor(0);
                    gl_timePanel.addView(t[i][j]);
                } else {
                    ViewGroup.LayoutParams params_ = new ViewGroup.LayoutParams(width2,height);
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams(params_);
                    params.topMargin=5;
                    t[i][j]=new mTextView(context,"");
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
                t[i][j].setText((String) tv_move.getText());
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
        update();
        Log.d("ninini", "onDestroy: ");
        if(isget){
            if(planBean.getObjectId()==null){
                Toast.makeText(context,"网络异常",Toast.LENGTH_SHORT).show();
                return;
            }
            MyPlanBean myPlanBean = new MyPlanBean();
            myPlanBean.setThings(things);
            myPlanBean.setName(user.getUsername());
            myPlanBean.update(context, planBean.getObjectId(), new UpdateListener() {

                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(int code, String msg) {
                    Toast.makeText(context,"网络异常",Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Date d=new Date();
            MyPlanBean myPlanBean = new MyPlanBean();
            myPlanBean.setThings(things);
            myPlanBean.setDate(d.getYear()+""+d.getMonth()+""+d.getDate()+"");
            myPlanBean.setName(user.getUsername());
            myPlanBean.save(context, new SaveListener() {

                @Override
                public void onSuccess() {
                    isget=true;
                    flag=true;

                }

                @Override
                public void onFailure(int code, String arg0) {
                    Toast.makeText(context,"网络异常",Toast.LENGTH_SHORT).show();
                }
            });
        }

        if(isgetday){
            Date d = new Date();
            DayBean dayBean = new DayBean(d.getYear()+""+d.getMonth()+""+d.getDate()+"");
            dayBean.setItems(items);
            dayBean.update(context, ThreeFragment.this.dayBean.getObjectId(), new UpdateListener() {
                @Override
                public void onSuccess() {
//                            Toast.makeText(context,"su2",Toast.LENGTH_SHORT).show();
                }

                public void onFailure(int i, String s) {
//                            Toast.makeText(context,"fa2",Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Date d = new Date();
            DayBean dayBean = new DayBean(d.getYear()+""+d.getMonth()+""+d.getDate()+"");
            dayBean.setUserName(user.getUsername());
            dayBean.setItems(items);
            dayBean.save(context, new SaveListener() {
                public void onSuccess() {
                    isgetday=true;
                }

                public void onFailure(int i, String s) {

                }
            } );

        }
    }
    String [] s={"上班",
            "学习",
            "阅读",
            "运动",
            "玩游戏",
            "看电视",
            "睡觉"  };
    private ItemBean[] items = new ItemBean[7];
    private void setDayDate(int color,float howLong){
        for(int i = 0;i<items.length;i++){
            if(color == items[i].getColor()){
                float oldTime = items[i].getHowLong();
                float newTime = oldTime + howLong;
                items[i].setHowLong(newTime);
            }
        }
    }
    int[] colors;
    private void setColor(){
        colors = new int[] {
                getActivity().getResources().getColor(R.color.red),
                getActivity().getResources().getColor(R.color.colorAccent),
                getActivity().getResources().getColor(R.color.yellow),
                getActivity().getResources().getColor(R.color.green),
                getActivity().getResources().getColor(R.color.blue),
                getActivity().getResources().getColor(R.color.colorPrimaryDark),
                getActivity().getResources().getColor(R.color.purse)
        };
    }








    private void setItems(){
        setColor();
        for(int i = 0;i<items.length;i++){
            ItemBean item = new ItemBean(s[i],colors[i]);
            items[i] = item;
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==((Activity)context).RESULT_OK){
                    affairControler.get();
                }
        }
    }
}
