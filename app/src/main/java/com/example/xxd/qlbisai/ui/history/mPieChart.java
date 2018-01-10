package com.example.xxd.qlbisai.ui.history;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

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
 * Created by 梁胜峰1 on 2017/8/2.
 */

public class mPieChart extends Fragment implements NumberPickerView.OnValueChangeListener{
    View v;
    private PieChart pie;
    private NumberPickerView np_yeaer,np_month,np_day;
    private int year,month,day;
    String [] s={"上班",
            "学习",
            "阅读",
            "运动",
            "玩游戏",
            "看电视",
            "睡觉"  };
    Integer[] colors ;
    private MyUserBean user;
    float[] time = new float[7];
    private Context context;
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.pie,null);
        context=getActivity();
        pie = (PieChart) v.findViewById(R.id.chart_pie);
        user= BmobUser.getCurrentUser(context,MyUserBean.class);
        colors = new Integer[] {
                getResources().getColor(R.color.red),
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.yellow),
                getResources().getColor(R.color.green),
                getResources().getColor(R.color.blue),
                getResources().getColor(R.color.colorPrimaryDark),
                getResources().getColor(R.color.purse)
        };
        initView();
        getData();
        return v;
    }
    public void setData(String [] s){
        this.s = s;
    }
    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 3){
                newColors = new ArrayList<Integer>();
                initPieChart();
            }else if(msg.what == 4){
                getData();
            }else if(msg.what == 5){
                Toast.makeText(context,"无数据",Toast.LENGTH_SHORT).show();
            }
        }
    };
    public void getData() {

        BmobQuery<DayBean> query = new BmobQuery<DayBean>();
        query.addWhereEqualTo("userName",user.getUsername());
        String str = year+""+month+""+day+"";
        query.addWhereEqualTo("time",str);
        final ProgressDialog p = new ProgressDialog(context);
        p.setMessage("正在加载");
        p.setCanceledOnTouchOutside(false);
        p.show();
        query.findObjects(context, new FindListener<DayBean>() {
            @Override
            public void onSuccess(List<DayBean> list) {
                if(list.size()>0){
                    ItemBean[] items = list.get(0).getItems();
                    for(int i = 0;i<items.length;i++){
                        time[i] = items[i].getHowLong();
                    }
                    Message  m = new Message();
                    m.what = 3;
                    handler.sendMessage(m);
                }else {
                    Message  m = new Message();
                    m.what = 5;
                    handler.sendMessage(m);
                }
                p.dismiss();
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(context,"网络异常",Toast.LENGTH_SHORT).show();
                p.dismiss();
            }
        });

    }
    private void initView() {
        getCurrentTime();
        np_yeaer = (NumberPickerView) v.findViewById(R.id.np_year);
        np_month = (NumberPickerView) v.findViewById(R.id.np_month);
        np_day = (NumberPickerView) v.findViewById(R.id.np_day);
        np_yeaer.setWrapSelectorWheel(false);
        np_month.setWrapSelectorWheel(false);
        np_day.setWrapSelectorWheel(false);
        np_yeaer.setOnValueChangedListener(this);
        np_month.setOnValueChangedListener(this);
        np_day.setOnValueChangedListener(this);
        Calendar c = Calendar.getInstance();
        String[] y = new String[33];
        for(int i = 0;i<y.length;i++){
            y[i] = ""+(i+year+1900);
        }
        np_yeaer.refreshByNewDisplayedValues(y);
        np_yeaer.setMinValue(2017);
        np_yeaer.setMaxValue(2049);
        Log.d("dd",year+"");
        int newYear =  year+1900;
        String[] m = new String[12];
        for(int i=0;i<m.length;i++){
            m[i]=""+(i+1);
        }
        np_month.refreshByNewDisplayedValues(m);
        np_month.setMinValue(01);
        np_month.setMaxValue(12);
        np_month.setValue(month+1);
//        np_month.setFormatter(new mFormatter("月");
        String[] d = new String[31];
        for(int i = 0;i<d.length;i++){
            d[i]=""+(i+1);
        }

        np_day.refreshByNewDisplayedValues(d);
        np_day.setMinValue(1);
        np_day.setMaxValue(31);
        np_day.setValue(day);
    }
    public void getCurrentTime() {
        Date d = new Date();
        year = d.getYear();
        month = d.getMonth();
        day = d.getDate();
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
    ArrayList<Integer> newColors ;
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
//        DisplayMetrics m = getResources().getDisplayMetrics();
//        float px = 5*(m.densityDpi/160f);
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
            case  R.id.np_day:
                day = newVal;
                Log.d("day",day+"");
                break;
        }


        Message m = new Message();
        m.what = 4;
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
