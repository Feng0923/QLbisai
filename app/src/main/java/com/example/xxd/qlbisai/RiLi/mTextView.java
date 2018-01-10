package com.example.xxd.qlbisai.RiLi;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;

import com.example.xxd.qlbisai.R;


public class mTextView extends android.support.v7.widget.AppCompatTextView {
    private boolean change = true;
    private String text;
    private int background;
    public mTextView(Context context,String text) {
        super(context);
        this.text = text;
        init();
    }

    private void init() {
        this.setGravity(Gravity.CENTER);
        this.setBackground(getResources().getDrawable(R.drawable.things));
        setText(text);
    }
    public void setColor(int resId){
        if(change) {
            GradientDrawable p = (GradientDrawable) getBackground();
            p.setColor(resId);
            background = resId;
        }
    }

    public void setText(String text) {
        if(change){
            super.setText(text);
        }
    }

    public int getColor(){
        return background;
    }
   public void  register(int i,int j){
       if(change) {
           GradientDrawable p = (GradientDrawable) getBackground();
           p.setColor(getResources().getColor(R.color.gray));
           setText("");
       }
    }
    public void setChange(boolean b){
        change = b;
    }
}
