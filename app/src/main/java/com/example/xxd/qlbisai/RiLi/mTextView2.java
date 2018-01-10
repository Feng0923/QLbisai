package com.example.xxd.qlbisai.RiLi;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.xxd.qlbisai.R;


public class mTextView2 extends android.support.v7.widget.AppCompatTextView {
    private int backgroundColor;
    private String s;
    private String ss;
    private Context context;
    public mTextView2(Context context) {
        super(context);
        this.context = context;
        init();
    }
   public mTextView2(Context context,AttributeSet d){
        super(context,d);
       this.context = context;
        init();
    }
    private void init() {
        this.setGravity(Gravity.CENTER_VERTICAL);
        this.setPadding(40,0,0,0);
        setBackground(getResources().getDrawable(R.drawable.shape));
    }

    public mTextView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setColor( int color) {
        GradientDrawable p = (GradientDrawable) this.getBackground();
        p.setColor(color);
        this.backgroundColor = color;
    }
    public int getColor(){
        return backgroundColor;
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if(visibility == View.VISIBLE){
            Animation anim = AnimationUtils.loadAnimation(context,R.anim.first);
            this.setAnimation(anim);
        }
    }
}
