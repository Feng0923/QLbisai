package com.example.xxd.qlbisai.ui.LogCat;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xxd.qlbisai.R;
import com.example.xxd.qlbisai.myBean.LogcatBean;


import java.util.List;

/**
 * Created by xxd on 2017/7/28.
 */

public class LogcatAdapter extends BaseAdapter {
    public static boolean flag1=false;

    private List<LogcatBean> list;
    private Context context;
    public LogcatAdapter(List<LogcatBean> list,Context context){
        this.list=list;
        this.context=context;
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
        final LogcatBean logcatBean = list.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.logcat_item, null);
            viewHolder.logcat = (TextView) convertView.findViewById(R.id.tv_message);
            viewHolder.time=(TextView)convertView.findViewById(R.id.tv_time);
            viewHolder.month=(TextView)convertView.findViewById(R.id.tv_month);
            viewHolder.year=(TextView)convertView.findViewById(R.id.tv_year);
            viewHolder.img_fang=(ImageView)convertView.findViewById(R.id.img_fang);
            viewHolder.img_gou=(ImageView)convertView.findViewById(R.id.img_gou);
            viewHolder.ll=(LinearLayout)convertView.findViewById(R.id.item_ll);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        Activity ac=(Activity) context;
        DisplayMetrics dm = new DisplayMetrics();
        ac.getWindowManager().getDefaultDisplay().getMetrics(dm);

        ViewGroup.LayoutParams para;
        para = viewHolder.ll.getLayoutParams();


        para.height = dm.heightPixels*1/10;
        viewHolder.ll.setLayoutParams(para);


        String l=logcatBean.getLogcat();
        if(l.length()<=12){
            viewHolder.logcat.setText(l);
        }else {
            viewHolder.logcat.setText(l.substring(0,12)+"...");
        }
        String s=logcatBean.getUpdatedAt();
        String y=s.substring(0,4)+"年";
        String m;
        if(s.charAt(5)=='0'){
            if(s.charAt(8)=='0'){
                m=s.charAt(6)+"月"+s.charAt(9)+"日";
            }else {
                m=s.charAt(6)+"月"+s.charAt(8)+s.charAt(9)+"日";
            }
        }else {
            if(s.charAt(8)=='0'){
                m=s.charAt(5)+s.charAt(6)+"月"+s.charAt(9)+"日";
            }else {
                m=s.charAt(5)+s.charAt(6)+"月"+s.charAt(8)+s.charAt(9)+"日";
            }

        }
        if(flag1)
            viewHolder.img_fang.setVisibility(View.VISIBLE);
        else
            viewHolder.img_fang.setVisibility(View.INVISIBLE);
        if(!logcatBean.getDelete()){
            viewHolder.img_gou.setVisibility(View.INVISIBLE);
        }
        viewHolder.img_fang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(logcatBean.getDelete()){
                    logcatBean.setDelete(false);
                    viewHolder.img_gou.setVisibility(View.INVISIBLE);
                }else {
                    logcatBean.setDelete(true);
                    viewHolder.img_gou.setVisibility(View.VISIBLE);
                }

            }
        });



        String d=s.substring(10,16);
        viewHolder.year.setText(y);
        viewHolder.month.setText(m);
        viewHolder.time.setText(d);
        return convertView;
    }

    final static class ViewHolder {
        TextView logcat;
        TextView time;
        TextView month;
        TextView year;
        ImageView img_fang,img_gou;
        LinearLayout ll;
    }
}
