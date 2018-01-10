package com.example.xxd.qlbisai.RiLi;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.example.xxd.qlbisai.myBean.AffairBean;
import com.example.xxd.qlbisai.ui.ThreeFragment;
import com.example.xxd.qlbisai.ui.firsthistory.PlanActivity;

import java.util.ArrayList;
import java.util.List;


public class mLabel extends LinearLayout implements View.OnClickListener {
//    private String[] s;
    private Context context;
    private int color;
    private boolean isDrop = false;
    mTextView2 m1;
    private String root;
    private List<String> children;
    private String c;
    private List<mTextView2> list = new ArrayList<mTextView2>();
   /* public mLabel(Context context,String[] s,int color) {
        super(context);
        this.setOrientation(VERTICAL);
        this.s = s;
        this.context = context;
        this.color = color;
        init();
    }*/
    public mLabel(Context context,AffairBean affair,String c){
        super(context);
        this.setOrientation(VERTICAL);
        this.context=context;
        this.color = affair.getColor();
        this.root = affair.getRoot();
        this.children = affair.getChildren();
        this.c = c;
        init_affair();
    }

    private void init_affair() {
        m1 = new mTextView2(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT );
        params.gravity = Gravity.CENTER_VERTICAL;
        params.topMargin = 30;
        m1.setColor(color);
        m1.setText(root);
        if(children.size()>0){
            m1.setOnClickListener(this);
        }else{
            if(c == PlanActivity.class.getName()){
                m1.setOnTouchListener(PlanActivity.getListener());
            }else if(c == ThreeFragment.class.getName()){
                m1.setOnTouchListener(ThreeFragment.getListener());
            }

        }
        this.addView(m1);
        m1.setLayoutParams(params);

        for(int i=0;i<children.size();i++){
            mTextView2 m = new mTextView2(context);
            LayoutParams params_ = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT );
            params.gravity = Gravity.CENTER_VERTICAL;
            list.add(m);
            params_.topMargin = 30;
            params_.leftMargin = 60;
            m.setColor(color);
            m.setText(children.get(i));
            m.setVisibility(View.GONE);
            if(c == PlanActivity.class.getName()){
                m.setOnTouchListener(PlanActivity.getListener());
            }else if(c == ThreeFragment.class.getName()){
                m.setOnTouchListener(ThreeFragment.getListener());
            }

            this.addView(m);
            m.setLayoutParams(params_);
        }

    }
/*
    private void init() {
         m1 = new mTextView2(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT );
        params.gravity = Gravity.CENTER_VERTICAL;
        params.topMargin = 30;
        m1.setColor(color);
        m1.setText(s[0]);
        if(s.length>1){
            m1.setOnClickListener(this);
        }else {
            m1.setOnTouchListener(MainActivity.getListener());
        }

         this.addView(m1);
        m1.setLayoutParams(params);

        for(int i=1;i<s.length;i++){
            mTextView2 m = new mTextView2(context);
            LayoutParams params_ = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT );
            params.gravity = Gravity.CENTER_VERTICAL;
            list.add(m);
            params_.topMargin = 30;
            params_.leftMargin = 60;
            m.setColor(color);
            m.setText(s[i]);
            m.setVisibility(View.GONE);
            m.setOnTouchListener(MainActivity.getListener());
           this.addView(m);
            m.setLayoutParams(params_);
        }
    }
*/
    public void onClick(View v) {
        if(!isDrop){
            for(mTextView2 m : list){
                m.setVisibility(View.VISIBLE);
            }
            setDrop(true);
        }else{
            for(mTextView2 m : list){
                m.setVisibility(View.GONE);
            }
            setDrop(false);
        }
    }
    public void setDrop(boolean b){
        isDrop = b;
    }

}
