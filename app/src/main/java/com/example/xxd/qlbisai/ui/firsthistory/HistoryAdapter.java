package com.example.xxd.qlbisai.ui.firsthistory;

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
import com.example.xxd.qlbisai.myBean.LogcatBean;
import com.example.xxd.qlbisai.myBean.ThingBean;
import com.example.xxd.qlbisai.ui.LogCat.LogcatAdapter;

import java.util.List;

/**
 * Created by xxd on 2017/8/10.
 */

public class HistoryAdapter extends BaseAdapter {
    private List<String> times;
    private List<ThingBean> lista;
    private List<ThingBean> listb;
    private Context context;

    public HistoryAdapter(List<String> times, List<ThingBean> lista, List<ThingBean> listb, Context context){
        this.times=times;
        this.lista=lista;
        this.listb=listb;
        this.context=context;
    }
    @Override
    public int getCount() {
        return times.size();
    }

    @Override
    public Object getItem(int position) {
        return times.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        final String sss=times.get(position);
        final ThingBean thingBean_before = lista.get(position);
        final ThingBean thingBean_next= listb.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.history_item, null);
            viewHolder.ll=(LinearLayout)convertView.findViewById(R.id.ll_history);
            viewHolder.time=(TextView)convertView.findViewById(R.id.tv_time);
            viewHolder.tv_before=(TextView)convertView.findViewById(R.id.tv_before);
            viewHolder.tv_next=(TextView)convertView.findViewById(R.id.tv_next);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Activity ac=(Activity) context;
        DisplayMetrics dm = new DisplayMetrics();
        ac.getWindowManager().getDefaultDisplay().getMetrics(dm);

        ViewGroup.LayoutParams para;
        para = viewHolder.ll.getLayoutParams();


        para.height = dm.heightPixels*1/15;
        viewHolder.ll.setLayoutParams(para);

        viewHolder.time.setText(times.get(position));

        viewHolder.tv_before.setText(thingBean_before.getDone());

        viewHolder.tv_before.setBackgroundColor(thingBean_before.getColor());

        viewHolder.tv_next.setText(thingBean_next.getDone());

        viewHolder.tv_next.setBackgroundColor(thingBean_next.getColor());


        return convertView;

    }

    final static class ViewHolder {
        TextView time;
        TextView tv_before;
        TextView tv_next;
        LinearLayout ll;
    }
}
