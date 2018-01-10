package com.example.xxd.qlbisai.ui.history;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xxd.qlbisai.R;
import com.example.xxd.qlbisai.myBean.DayBean;
import com.example.xxd.qlbisai.myBean.ItemBean;
import com.example.xxd.qlbisai.myBean.MyUserBean;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.carbswang.android.numberpickerview.library.NumberPickerView;

/**
 * Created by 梁胜峰1 on 2017/8/12.
 */

public class mMonthChart extends Fragment implements NumberPickerView.OnValueChangeListener{
    private Context context;

    private View v;
    private NumberPickerView np_year,np_month;
    private PieChart pie;
    private int year,month;
    int[] colors = new int[7];
    private MyUserBean user;
    float[] time = new float[7];
    String [] s={"上班",
            "学习",
            "阅读",
            "运动",
            "玩游戏",
            "看电视",
            "睡觉"  };
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.monthchart,null);
        context = getActivity();
        user= BmobUser.getCurrentUser(context,MyUserBean.class);
        pie = (PieChart) v.findViewById(R.id.chart_pie);
        colors = new int[] {
                getResources().getColor(R.color.red),
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.yellow),
                getResources().getColor(R.color.green),
                getResources().getColor(R.color.blue),
                getResources().getColor(R.color.colorPrimaryDark),
                getResources().getColor(R.color.purse)
        };
        initPicker();
        getData();
        return v;
    }
    private final Handler handler = new Handler(){
        ProgressDialog p;
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 11 ){
                for(int i = 0 ;i < time.length;i++){
                    time[i]=0;
                }
                p = new ProgressDialog(context);
                p.setMessage("正在加载");
                p.setCanceledOnTouchOutside(false);
                p.show();
            }else if(msg.what == 12){
                i++;
                if(i<31){
                    getData();
                }else if(i == 31){
                    initPieChart();
                    p.dismiss();
                }


            }else if(msg.what == 13){
                i=0;
                getData();
            }else if(msg.what == 14){
                for(int i = 0 ;i<time.length;i++){
                    time[i] += 0;
                }
            }
        }
    };
    int i = 0;
    String months[] = new String[31];
    private void getData(){
        if(i == 0){
            for(int i = 0 ; i < 31;i++){
                String date = year+""+month+""+(i+1)+"";
                Log.d("dates",date);
                Log.d("date2",year+"+"+month);
                months[i] = date;
            }
            Message m = new Message();
            m.what = 11 ;
            handler.sendMessage(m);
        }
        BmobQuery<DayBean>  query = new BmobQuery<DayBean>();
        query.addWhereEqualTo("userName", user.getUsername());
        query.addWhereEqualTo("time",months[i]);
        query.findObjects(context, new FindListener<DayBean>() {
            @Override
            public void onSuccess(List<DayBean> list) {
                if(list.size()>0){
                    ItemBean[] items = list.get(0).getItems();
                    if(items !=null){
                        for(int i = 0 ;i<time.length;i++){
                            time[i] += items[i].getHowLong();
                        }
                    }
                }
                Message m = new Message();
                m.what = 12;
                handler.sendMessage(m);
            }

            @Override
            public void onError(int i, String s) {
                Message m = new Message();
                m.what = 14;
                handler.sendMessage(m);
            }
        });
    }


    private void initPicker() {
        getCurrentTime();
        np_year = (NumberPickerView) v.findViewById(R.id.np_year);
        np_month = (NumberPickerView) v.findViewById(R.id.np_month);
        np_year.setOnValueChangedListener(this);
        np_month.setOnValueChangedListener(this);
        String[] y = new String[33];
        for(int i = 0;i<y.length;i++){
            y[i] = ""+(i+year+1900);
        }
        np_year.refreshByNewDisplayedValues(y);
        np_year.setMinValue(2017);
        np_year.setMaxValue(2049);
        np_year.setValue(year+1900);
        String[] m = new String[12];
        for(int i=0;i<m.length;i++){
            m[i]=""+(i+1);
        }
        np_month.refreshByNewDisplayedValues(m);
        np_month.setMinValue(1);
        np_month.setMaxValue(12);
        np_month.setValue(month+1);
        Log.d("dd",month+"");

    }

    public void getCurrentTime() {
        Date d = new Date();
        year = d.getYear();
        month = d.getMonth();
    }
    private void initPieChart() {
        pie.setData(getPieData());
        setLook();
        pie.invalidate();

    }
    private void setLook(){
        pie.setHoleColorTransparent(true);
        pie.setHoleRadius(60f);
        pie.setTransparentCircleRadius(64F);
        pie.setDrawCenterText(true);
        pie.setDrawHoleEnabled(true);
        pie.setRotationAngle(90);
        pie.setRotationEnabled(true);
        pie.setUsePercentValues(true);
        pie.setDescription("");
        Legend ml = pie.getLegend();
        ml.setForm(Legend.LegendForm.CIRCLE);
        ml.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        ml.setTextSize(10);
        ml.setXEntrySpace(7f);
        ml.setYEntrySpace(5f);
        pie.animateXY(1000,1000);
    }
    ArrayList<Integer> newColors = new ArrayList<Integer>();
    private PieData getPieData() {
        ArrayList<String> xValues = new ArrayList<String>();
        int length = 0;
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        for(int i = 0 ;i<time.length;i++){
            float t = time[i];
            if(t!=0){
                yValues.add(new Entry(t,length++));
                xValues.add(s[i]);
                newColors.add(colors[i]);
            }
        }
        PieDataSet pieDataSet = new PieDataSet(yValues,"");
        pieDataSet.setSliceSpace(1.3f);
        pieDataSet.setValueFormatter(new mFormatter());
        pieDataSet.setDrawValues(true);
        pieDataSet.setValueTextSize(13f);
        pieDataSet.setColors(newColors);
        pieDataSet.setSelectionShift(10);
        PieData data = new PieData(xValues,pieDataSet);
        return data;
    }

    @Override
    public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {
        int id = picker.getId();
        switch (id){
            case R.id.np_year:
                year = newVal-1900;
                break;
            case  R.id.np_month:
                month = newVal-1;
                break;
        }


        Message m = new Message();
        m.what = 13;
        handler.sendMessage(m);
    }



    private class mFormatter implements ValueFormatter {
        DecimalFormat m = new DecimalFormat();


        @Override
        public String getFormattedValue(float v, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            int i = (int) v;
            float j = (v*100)%100;
            return i+" %";
        }
    }
}
