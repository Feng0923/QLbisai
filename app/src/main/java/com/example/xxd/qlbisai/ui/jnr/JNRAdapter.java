package com.example.xxd.qlbisai.ui.jnr;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xxd.qlbisai.R;
import com.example.xxd.qlbisai.myBean.JNRBean;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by xxd on 2017/7/29.
 */

public class JNRAdapter extends BaseAdapter {

    private List<JNRBean> list;
    private Context context;
    private int i;
    public JNRAdapter(Context context,List<JNRBean> list,int i){
        this.context=context;
        this.list=list;
        this.i=i;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final JNRBean jnrBean = list.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            if(i==1) {
                convertView = LayoutInflater.from(context).inflate(R.layout.jnr_item, null);
            }else {
                convertView = LayoutInflater.from(context).inflate(R.layout.jnr_item2, null);
            }
            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.day = (TextView) convertView.findViewById(R.id.tv_day);
            viewHolder.descrip=(TextView)convertView.findViewById(R.id.tv_discrip);
            viewHolder.ll = (LinearLayout) convertView.findViewById(R.id.ll_jnr);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(jnrBean.getTitle());
        Calendar calendar = Calendar.getInstance();
        ;

        Date date=jnrBean.getDate();
        calendar.set(Calendar.YEAR, date.getYear()+1900);//先指定年份
        calendar.set(Calendar.MONTH, date.getMonth());//再指定月份 Java月份从0开始算
        calendar.set(Calendar.DAY_OF_MONTH, date.getDate());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case 1:
                viewHolder.descrip.setText("目标日:"+(date.getMonth()+1)+"月"+date.getDate()+","+(date.getYear()+1900)+"(周日)");
                break;
            case 2:
                viewHolder.descrip.setText("目标日:"+(date.getMonth()+1)+"月"+date.getDate()+","+(date.getYear()+1900)+"(周一)");
                break;
            case 3:
                viewHolder.descrip.setText("目标日:"+(date.getMonth()+1)+"月"+date.getDate()+","+(date.getYear()+1900)+"(周二)");
                break;
            case 4:
                viewHolder.descrip.setText("目标日:"+(date.getMonth()+1)+"月"+date.getDate()+","+(date.getYear()+1900)+"(周三)");
                break;
            case 5:
                viewHolder.descrip.setText("目标日:"+(date.getMonth()+1)+"月"+date.getDate()+","+(date.getYear()+1900)+"(周四)");
                break;
            case 6:
                viewHolder.descrip.setText("目标日:"+(date.getMonth()+1)+"月"+date.getDate()+","+(date.getYear()+1900)+"(周五)");
                break;
            case 7:
                viewHolder.descrip.setText("目标日:"+(date.getMonth()+1)+"月"+date.getDate()+","+(date.getYear()+1900)+"(周六)");
                break;

        }


        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        long time1 = cal.getTimeInMillis();
        cal.setTime(jnrBean.getDate());
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);
        int i=Integer.parseInt(String.valueOf(between_days));
        viewHolder.day.setText(i+"");

        Activity ac=(Activity) context;
        DisplayMetrics dm = new DisplayMetrics();
        ac.getWindowManager().getDefaultDisplay().getMetrics(dm);

        ViewGroup.LayoutParams para;
        para = viewHolder.ll.getLayoutParams();


        para.height = dm.heightPixels*1/13;
        viewHolder.ll.setLayoutParams(para);



        return convertView;
    }

    final static class ViewHolder {
        TextView day;
        TextView descrip;
        TextView title;
        LinearLayout ll;
    }
}
