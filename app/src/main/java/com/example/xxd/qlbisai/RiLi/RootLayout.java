package com.example.xxd.qlbisai.RiLi;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.xxd.qlbisai.R;
import com.example.xxd.qlbisai.myBean.AffairBean;
import com.example.xxd.qlbisai.ui.AffairControler;

import java.util.ArrayList;

/**
 * Created by 梁胜峰1 on 2017/7/24.
 */

public class RootLayout extends LinearLayout implements View.OnClickListener {
    private Context context;
    private TextView tv_color,tv_root,tv_edit;
    ImageView iv_add;
    private ListView lv_child;
    private ArrayList<String> children ;
    private LinearLayout ll_addPanel;
    private EditText et_edit;
    private TextView tv_enter;
    private ChildrenAdapter adapter;
    boolean isDrop = false;
    private String affairId;
    private int color;
//    private String text;
/*
    public RootLayout(Context context, int color, String[] text) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.affairsadapter,this);

        this.context = context;
        this.color = color;
//        this.text = text[0];

        tv_color = (TextView) findViewById(R.id.tv_colorOvl);
        setColor(color);

        tv_root = (TextView) findViewById(R.id.tv_root);
        setText(text[0]);
        tv_root.setOnClickListener(this);

        tv_add = (TextView) findViewById(R.id.tv_add);
        tv_add.setOnClickListener(this);

        lv_child = (ListView) findViewById(R.id.lv_child);
        setChildren(text);
        adapter = new ChildrenAdapter(context,children);
        lv_child.setAdapter(adapter);
        Utility.setListViewHeightBaseOnchildren(lv_child);

        ll_addPanel = (LinearLayout) findViewById(R.id.ll_edit);
        tv_edit = (TextView) findViewById(R.id.tv_edit);
        et_edit = (EditText) findViewById(R.id.et_add);
        tv_enter = (TextView) findViewById(R.id.tv_enter);
        tv_enter.setOnClickListener(this);

    }*/
    private AffairControler affairControler;
    private String root;
    private AffairBean affair;
    public RootLayout(Context context, AffairBean affair, AffairControler affairControler){
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.affairsadapter,this);

        this.context = context;
        this.affair = affair;
        this.affairControler = affairControler;
        this.color = affair.getColor();
        this.root = affair.getRoot();
        this.children = affair.getChildren();

        tv_color = (TextView) findViewById(R.id.tv_colorOvl);
        setColor(color);

        tv_root = (TextView) findViewById(R.id.tv_root);
        setText(root);
        tv_root.setOnClickListener(this);

        iv_add = (ImageView) findViewById(R.id.iv_add);
        iv_add.setOnClickListener(this);

        lv_child = (ListView) findViewById(R.id.lv_child);

        adapter = new ChildrenAdapter(context,children);
        lv_child.setAdapter(adapter);
        Utility.setListViewHeightBaseOnchildren(lv_child);

        ll_addPanel = (LinearLayout) findViewById(R.id.ll_edit);
        tv_edit = (TextView) findViewById(R.id.tv_edit);
        et_edit = (EditText) findViewById(R.id.et_add);
        tv_enter = (TextView) findViewById(R.id.tv_enter);
        tv_enter.setOnClickListener(this);
    }

    private void setChildren(String[] text) {
        for(int i=1;i<text.length;i++){
            children.add(text[i]);
        }
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        GradientDrawable g = (GradientDrawable) tv_color.getBackground();
        g.setColor(color);
    }

    public void setText(String text){
        tv_root.setText(text);
    }


    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.iv_add:
                ll_addPanel.setVisibility(View.VISIBLE);
                GradientDrawable p = (GradientDrawable) tv_edit.getBackground();
                p.setColor(color);
                break;
            case R.id.tv_root:
                if(!isDrop) {
                    lv_child.setVisibility(View.VISIBLE);
                    setAnim();
                    isDrop = true;
                }else{
                    lv_child.setVisibility(View.GONE);
                    isDrop = false;
                }
                break;
            case R.id.tv_enter:
                String add = String.valueOf(et_edit.getText());
                if(!add.isEmpty()){
                    children.add(add);
                    adapter.notifyDataSetChanged();
                    Utility.setListViewHeightBaseOnchildren(lv_child);
                    affairControler.update(children,affair);
                }
                ll_addPanel.setVisibility(View.GONE);
                InputMethodManager m = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                m.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
                et_edit.setText("");
                break;
        }
    }

    private void setAnim() {
        Animation a = AnimationUtils.loadAnimation(context,R.anim.first);
    }

    private static class Utility{
        public static void setListViewHeightBaseOnchildren(ListView listView){
            ListAdapter adapter = listView.getAdapter();
            if(adapter == null){
                return;
            }
            int totalHeight = 0;
            for(int i = 0;i<adapter.getCount();i++){
                View listItem = adapter.getView(i,null,listView);
                listItem.measure(0,0);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight+(listView.getDividerHeight()*(adapter.getCount()-1));
            listView.setLayoutParams(params);
        }
    }
    private class ChildrenAdapter extends BaseAdapter{
        private Context context;
        private ArrayList<String> children;

        public ChildrenAdapter(Context context, ArrayList<String> text){
            this.context = context;
            this.children = text;
        }
        @Override
        public int getCount() {
            return children.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
//            TextView tv_childColor,tv_child;

            Holder holder = null;
            if(convertView == null){
                convertView = View.inflate(context,R.layout.childadapter,null);
                holder = new Holder();

                holder.ll_childrenPanel = (LinearLayout) convertView.findViewById(R.id.ll_child);
                holder.tv_childColor = (TextView) convertView.findViewById(R.id.tv_childcolor);
                holder.tv_child = (TextView) convertView.findViewById(R.id.tv_child);
                holder.iv_child = (ImageView) convertView.findViewById(R.id.iv_child);
//                tv_childColor = (TextView) convertView.findViewById(R.id.tv_childcolor);
//                tv_child = (TextView) convertView.findViewById(R.id.tv_child);

                convertView.setTag(holder);
            }else{
                holder = (Holder) convertView.getTag();
            }
//            holder.ll_childrenPanel.setOnTouchListener(new Move(position));
            Animation a = AnimationUtils.loadAnimation(context,R.anim.first);
            holder.ll_childrenPanel.setAnimation(a);
            holder.iv_child.setOnClickListener(new Move_(position));

           GradientDrawable g = (GradientDrawable) holder.tv_childColor.getBackground();
//            GradientDrawable g = (GradientDrawable)tv_childColor.getBackground();
            g.setColor(color);

            holder.tv_child.setText(children.get(position));
//            tv_child.setText(children.get(position));



            return convertView;



        }
        private class Move_ implements OnClickListener{
            private int position;
            Move_(int position){
                this.position = position;
            }
            @Override
            public void onClick(View v) {
                children.remove(position);
                notifyDataSetChanged();
                Utility.setListViewHeightBaseOnchildren(lv_child);
                affairControler.update(children,affair);
            }
        }
        private class Move implements OnTouchListener{
            private int position;
            Move(int position){
                this.position = position;
            }
            public boolean onTouch(View v, MotionEvent event) {
                int x;
                int action = event.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        x= (int) event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getX();
                        if(Math.abs(dx)>280){
                            children.remove(position);
                            notifyDataSetChanged();
                            Utility.setListViewHeightBaseOnchildren(lv_child);
                            affairControler.update(children,affair);
                        }
                        break;
                }
                return true;
            }
        }
        private class Holder{
            LinearLayout ll_childrenPanel;
            TextView tv_childColor,tv_child;
            ImageView iv_child;
        }
    }
}
