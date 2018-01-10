package com.example.xxd.qlbisai.ui.history;

import android.app.Activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;


import android.os.Bundle;



import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.widget.TextView;



import com.example.xxd.qlbisai.R;


import java.util.ArrayList;

import java.util.List;




/**
 * Created by 梁胜峰1 on 2017/8/2.
 */

public class Charts extends AppCompatActivity implements View.OnClickListener{
    private Context context = this;
    private TextView tv_day,tv_week,tv_month;
    private int year,month,day;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private List<TextView> tabs = new ArrayList<TextView>();
    private mFragmentsManager fm;
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.chart);
        initView();
//        test();
    }

    private void initView() {
        tv_day = (TextView) findViewById(R.id.tv_day);
        tv_day.setTag(0);
        tv_month = (TextView) findViewById(R.id.tv_month);
        tv_month.setTag(2);
        tv_week = (TextView) findViewById(R.id.tv_week);
        tv_week.setTag(1);
        tabs.add(tv_day);
        tabs.add(tv_week);
        tabs.add(tv_month);
        tv_day.setOnClickListener(this);
        tv_month.setOnClickListener(this);
        tv_week.setOnClickListener(this);
        initFrag();
    }
    private void initFrag() {
        Fragment f1 = new mPieChart();
        fragments.add(f1);
        Fragment f2 = new mLineChart();
        fragments.add(f2);
        Fragment f3 = new mMonthChart();
        fragments.add(f3);
        fm = new mFragmentsManager(this,fragments,R.id.fl_chartPanel);
    }
//    private void test() {
//        String s = "2017-08-32";
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
//        try {
//            Date d = format.parse(s);
//            Toast.makeText(context,d.toString(),Toast.LENGTH_LONG).show();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }



    @Override
    public void onClick(View v) {
        TextView t = (TextView) v;
        for(int i = 0; i<tabs.size();i++){
            TextView tv = tabs.get(i);
            tv.setTextColor(getResources().getColor(R.color.blue));
            tv.setBackgroundColor(getResources().getColor(R.color.grayless));
        }
        t.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        t.setTextColor(getResources().getColor(R.color.white));
        Integer position = (int) t.getTag();
       /* if(position.equals(2)){
            ((mLineChart) fragments.get(position-1)).setData(new String []{"1","2","3"},new int[]{getResources().getColor(R.color.red),
                    getResources().getColor(R.color.yellow),
                    getResources().getColor(R.color.colorPrimary)},
                    new float[][]{
                            {0f,1f,0f,1f,1.5f,3.5f,0f},
                            {1.5f,0f,1.5f,0f,0f,1f,3f},
                            {0f,0f,0f,0f,0f,2f,1.8f}
                    });
            ((mLineChart) fragments.get(position-1)).nofifyDataChange();
        }*/
        fm.show(position);
    }

    private  class mFragmentsManager{
        private Activity a;
        private List<Fragment> list;
        private int location;
        private int  current = 0;
        public mFragmentsManager(Activity a,List<Fragment> list,int location){
            this.a = a;
            this.list = list;
            this.location = location;
            defaultShow();
        }
        private void defaultShow() {
            a.getFragmentManager().beginTransaction().add(location, fragments.get(0))
                    .show(fragments.get(0))
                    .commit();
        }
        private void show(int position){
            if(position < list.size()) {
                if(current != position) {
                    FragmentTransaction transaction = a.getFragmentManager().beginTransaction();
                    transaction.remove(fragments.get(current));
                    transaction.add(location, fragments.get(position));
                    transaction.show(fragments.get(position));
                    transaction.commit();
                    current = position;
                }
            }
        }
    }
}
