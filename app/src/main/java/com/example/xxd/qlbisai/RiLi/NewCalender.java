package com.example.xxd.qlbisai.RiLi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xxd.qlbisai.R;
import com.example.xxd.qlbisai.ui.firsthistory.HistroyActivity;
import com.example.xxd.qlbisai.ui.firsthistory.PlanActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by xxd on 2017/7/19.
 */
public class NewCalender extends LinearLayout {
    private ImageView btn_pre,btn_next;
    private TextView txt_date;
    private GridView grid;
    private Context context;
    private Calendar curDate=Calendar.getInstance();
    public NewCalender(Context context) {
        super(context);
    }

    public NewCalender(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initControl(context);
    }

    public NewCalender(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context);
    }

    private void initControl(Context context){
        this.context=context;
        bindControl(context);
        bindControlEvent();
        Date now=new Date();
        Log.d("xxdxxd Date", String.valueOf(now.getDate()));
        Log.d("xxdxxd Mon", String.valueOf(now.getMonth()));
        Log.d("xxdxxd Year", String.valueOf(now.getYear()));
        renderCalendar();
    }

    private void bindControlEvent() {
        btn_pre.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                curDate.add(Calendar.MONTH,-1);
                renderCalendar();
            }
        });

        btn_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                curDate.add(Calendar.MONTH,+1);
                renderCalendar();
            }


        });
    }

    private void renderCalendar() {
        SimpleDateFormat sdf=new SimpleDateFormat("MMMM yyy");
        txt_date.setText(sdf.format(curDate.getTime()));

        ArrayList<Date> cells=new ArrayList<>();
        Calendar calendar=(Calendar)curDate.clone();
        calendar.set(Calendar.DAY_OF_MONTH,1);
        int prevDays=calendar.get(Calendar.DAY_OF_WEEK)-1;
        calendar.add(Calendar.DAY_OF_MONTH,-prevDays);
        int maxCellCount=6*7;
        while (cells.size()<maxCellCount){
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH,1);

        }

        grid.setAdapter(new CalendarAdapter(getContext(),cells));


    }

    private void bindControl(Context context) {
        LayoutInflater inflater=LayoutInflater.from(context);
        inflater.inflate(R.layout.calender_view,this,true);

        btn_pre=(ImageView)findViewById(R.id.btn_prev);
        btn_next=(ImageView)findViewById(R.id.btn_next);
        txt_date=(TextView)findViewById(R.id.txt_date);
        grid=(GridView)findViewById(R.id.calendar_grid);
    }

    private class CalendarAdapter extends ArrayAdapter<Date>{
        LayoutInflater inflater;
        public CalendarAdapter(@NonNull Context context,ArrayList<Date> dates) {
            super(context, R.layout.calendar_text_day,dates);
            inflater=LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Date date = getItem(position);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.calendar_text_day, parent, false);

            }

            int day = date.getDate();
            ((CalendarDayTextView) convertView).setText(String.valueOf(day));
            Date now=new Date();
            boolean isTheSameMonth=false;
            if(now.getMonth()>date.getMonth()){
                ((CalendarDayTextView) convertView).setBefore(true);
            }
            if(now.getMonth()<date.getMonth()){
                ((CalendarDayTextView) convertView).setNext(true);
            }
            if(date.getMonth()==now.getMonth()){
                isTheSameMonth=true;
                if(date.getDate()<now.getDate())
                    ((CalendarDayTextView) convertView).setBefore(true);
                if(date.getDate()>now.getDate())
                    ((CalendarDayTextView) convertView).setNext(true);
            }

            if(now.getDate()==date.getDate()&&now.getMonth()==date.getMonth()&&now.getYear()==date.getYear()){
                ((CalendarDayTextView) convertView).setTextColor(Color.parseColor("#ff0000"));
                ((CalendarDayTextView) convertView).setTody(true);
            }else if(isTheSameMonth){
                ((CalendarDayTextView) convertView).setTextColor(Color.parseColor("#000000"));
            }else{
                ((CalendarDayTextView) convertView).setTextColor(Color.parseColor("#666666"));
            }


            convertView.setOnClickListener(new MyOnClickListener((CalendarDayTextView)convertView,date));
            return convertView;
        }



    }


    class MyOnClickListener implements OnClickListener{
        private CalendarDayTextView t;
        private Date date;
        public MyOnClickListener(CalendarDayTextView t,Date date){
            this.t=t;
            this.date=date;
        }



        @Override
        public void onClick(View v) {
            if(t.isNext()){
                Intent intent=new Intent(context, PlanActivity.class);
                intent.putExtra("date",date.getYear()+""+date.getMonth()+""+date.getDate());
                intent.putExtra("month",date.getMonth()+1+"月");
                intent.putExtra("day",date.getDate()+"");
                Calendar calendar=Calendar.getInstance();
                calendar.set(Calendar.YEAR, date.getYear()+1900);//先指定年份
                calendar.set(Calendar.MONTH, date.getMonth());//再指定月份 Java月份从0开始算
                calendar.set(Calendar.DAY_OF_MONTH, date.getDate());
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                switch (dayOfWeek) {
                    case 1:
                        intent.putExtra("week","周日");
                        break;
                    case 2:
                        intent.putExtra("week","周一");
                        break;
                    case 3:
                        intent.putExtra("week","周二");
                        break;
                    case 4:
                        intent.putExtra("week","周三");
                        break;
                    case 5:
                        intent.putExtra("week","周四");
                        break;
                    case 6:
                        intent.putExtra("week","周五");
                        break;
                    case 7:
                        intent.putExtra("week","周六");
                        break;

                }
                context.startActivity(intent);

            }
            if(t.isBefore()){
                Intent intent=new Intent(context, HistroyActivity.class);
                intent.putExtra("date",date.getYear()+""+date.getMonth()+""+date.getDate());
                context.startActivity(intent);


            }
            if(t.isTody()){
                Intent intent=new Intent(context, HistroyActivity.class);
                intent.putExtra("date",date.getYear()+""+date.getMonth()+""+date.getDate());
                context.startActivity(intent);

            }

        }
    }


}
