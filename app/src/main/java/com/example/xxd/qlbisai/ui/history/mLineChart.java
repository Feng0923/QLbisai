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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xxd.qlbisai.R;
import com.example.xxd.qlbisai.myBean.DayBean;
import com.example.xxd.qlbisai.myBean.ItemBean;
import com.example.xxd.qlbisai.myBean.MyUserBean;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.carbswang.android.numberpickerview.library.NumberPickerView;

/**
 * Created by 梁胜峰1 on 2017/8/3.
 */

public class mLineChart extends Fragment implements NumberPickerView.OnValueChangeListener{
    private LineChart lineChart;
//    private NumberPickerView np_weeks;
//    private PickerView pickerView;
    private ListView lv_weeks;
    private TextView tv_monday,tv_sunday;
    private NumberPickerView np;
    String [] s={"上班",
            "学习",
            "阅读",
            "运动",
            "玩游戏",
            "看电视",
            "睡觉"  };
    Integer[] colors;


    private Context context;
    private MyUserBean user;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.line,null);
        context=getActivity();
        user = BmobUser.getCurrentUser(context,MyUserBean.class);
        lineChart = (LineChart) v.findViewById(R.id.chart_line);
        np = (NumberPickerView) v.findViewById(R.id.np);
        p = new ProgressDialog(context);
        colors = new Integer[] {
                getResources().getColor(R.color.red),
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.yellow),
                getResources().getColor(R.color.green),
                getResources().getColor(R.color.blue),
                getResources().getColor(R.color.colorPrimaryDark),
                getResources().getColor(R.color.purse)
        };
        initPickerView();
        return v;
    }
    String year,month,day,week;
    ProgressDialog p;
    int j = 0;
//    Integer[] c = {0,1,2,3,4,5,6};
    ArrayList<Integer> current     ;//= new ArrayList<Integer>();
    ArrayList<Integer> newColors  ; //= new ArrayList<Integer>();
    ArrayList<String>  newLabels  ; //= new ArrayList<String>();
    private final Handler handler = new Handler(){

        public void handleMessage(Message msg) {
            if(msg.what == 7){
                current    = new ArrayList<Integer>();
                newColors  = new ArrayList<Integer>();
                newLabels  = new ArrayList<String>();
                i=0;
                j=0;
                getData();
            }else if(msg.what == 8){
                p.setMessage("正在加载");
                p.setCanceledOnTouchOutside(false);
                p.show();
            }else if(msg.what == 9){
                i++;
                if(i<7){
                    getData();
                }else if(i==7){
                    nofifyDataChange();
                    p.dismiss();
                }

            }else if(msg.what == 10){
                i++;
                j++;
                if(j == 7){
                    Toast.makeText(context,"暂无数据",Toast.LENGTH_SHORT).show();
                }else if(i == 7){
                    nofifyDataChange();
                    p.dismiss();
                }else{
                    getData();
                }

            }
        }
    };

    ArrayList<String> weeks = new ArrayList<String>();
    String[] weeks2 = new String[50];
    int position;
    private void initPickerView() {
        Calendar calendar = new GregorianCalendar(2017,7,1);
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        for(int i = 0 ;i < weeks2.length;i++){
            calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
            String monday = formater.format(calendar.getTime());
            calendar.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
            String sunday = formater.format(calendar.getTime());
            String week = monday+" - "+sunday;
            weeks.add(week);
            weeks2[i]=week;
            calendar.add(Calendar.WEEK_OF_YEAR,+1);
        }
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        String monday = formater.format(c.getTime());
        c.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
        String sunday = formater.format(c.getTime());
        String week = monday+" - "+sunday;
        position = weeks.indexOf(week)-1;
        np.refreshByNewDisplayedValues(weeks2);
        np.setValue(position);
        np.setOnValueChangedListener(this);
        Message m = new Message();
        m.what = 7;
        handler.sendMessage(m);
    }

    ArrayList<String> mondays = new ArrayList<String>(),sundays = new ArrayList<String>();

    float[][] f = new float[7][7];
    int i = 0;
    String[] dates = new String[7];
    public void getData() {
        if(i==0) {
            week = np.getContentByCurrValue().substring(0,10);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
            Date d = formatter.parse(week);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
                for(int i = 0 ; i<7;i++){
                    week = formatter.format(c.getTime());
                    year = week.substring(0, 4);
                    month = week.substring(5, 7);
                    day = week.substring(8, 10);
                    int year = Integer.parseInt(this.year) - 1900;
                    int month = Integer.parseInt(this.month) - 1;
                    int day = Integer.parseInt(this.day);
                    dates[i] = year+""+month+""+day+"";
                    c.add(Calendar.DATE,1);
             }
            }catch (Exception e){};
            Log.d("year", year + "");
            Log.d("month", month + "");
            Log.d("day", day + "");
            Message m = new Message();
            m.what = 8 ;
            handler.sendMessage(m);
        }


            BmobQuery<DayBean> query = new BmobQuery<DayBean>();
            query.addWhereEqualTo("userName", user.getUsername());
            query.addWhereEqualTo("time",dates[i]);
            query.findObjects(context, new FindListener<DayBean>() {


                @Override
                public void onSuccess(List<DayBean> list) {
                    if(list.size()>0){
                        ItemBean[] items = list.get(0).getItems();
                        for(int j = 0;j<items.length;j++){
                                f[i][j] = items[j].getHowLong();
                        }
                        Message  m = new Message();
                        m.what = 9;
                        handler.sendMessage(m);
                    }else {
                        Message  m = new Message();
                        m.what = 10;
                        handler.sendMessage(m);
                    }
                }

                @Override
                public void onError(int i, String s) {
                    Message  m = new Message();
                    m.what = 10;
                    handler.sendMessage(m);
                }

            });

    }

    public void nofifyDataChange(){
        check();
        lineChart.setData(getLineData());
        setLook();
        lineChart.invalidate();
    }

    private void check() {
        for(int i = 0 ; i <f.length;i++){
            float add  = 0;
            for(int j = 0 ; j<f[i].length;j++){
                add += f[j][i];
            }
            if(add!=0){
                current.add(i);
                newColors.add(colors[i]);
                newLabels.add(s[i]);
            }
        }
    }

    private void setLook(){
        lineChart.setDrawBorders(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(false);
        lineChart.setDescription(null);
        lineChart.setHighlightPerDragEnabled(true);
        lineChart.fitScreen();
        lineChart.setDrawBorders(false);

        Legend ml = lineChart.getLegend();
        ml.setForm(Legend.LegendForm.LINE);
        ml.setFormSize(15f);
//        ml.setYEntrySpace(3f);
//        ml.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        ml.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        ml.setComputedLabels(newLabels);
//        ml.setComputedColors(newColors);
        ml.setEnabled(true);;
        ml.setTextSize(10F);
        ml.setXEntrySpace(15f);
        ml.setWordWrapEnabled(true);
        ml.resetCustom();

        XAxis x = lineChart.getXAxis();
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
//        x.setTextSize(10f);
//        x.setValues(xValues);
        x.setAvoidFirstLastClipping(true);
        x.setDrawGridLines(false);
//        x.setLabelsToSkip(10);

        YAxis y1 = lineChart.getAxisLeft();
        y1.setTextSize(8f);

        YAxis y2 = lineChart.getAxisRight();
        y2.setEnabled(false);
        lineChart.animateX(20);

    }

    ArrayList<String > xValues;
    private LineData getLineData() {
        /* x轴 */
        String[] xWeeks = {"周一","周二","周三","周四","周五","周六","周日"};
        xValues = new ArrayList<String>();
        for(int i = 0;i < xWeeks.length;i++){
            xValues.add(xWeeks[i]);
        }
        /* y轴 */
        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        for(int i = 0;i< current.size();i++) {
            ArrayList<Entry> yValues = new ArrayList<Entry>();
            for (int j = 0; j < f[i].length; j++) {
                yValues.add(new Entry(f[j][current.get(i)], j));
                Log.d("caocao",""+current.get(i)+j+"-"+f[j][current.get(i)]+"");
            }
            LineDataSet lineDataSet = new LineDataSet(yValues,newLabels.get(i));
            lineDataSet.setValueTextSize(9f);
            lineDataSet.setColor(newColors.get(i));
//            lineDataSet.setLabel(newLabels.get(i));
            sets.add(lineDataSet);
        }
        LineData data = new LineData(xValues,sets);
        return data;
    }


    @Override
    public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {
            Message m = new Message();
            m.what = 7;
            handler.sendMessage(m);
    }
}
