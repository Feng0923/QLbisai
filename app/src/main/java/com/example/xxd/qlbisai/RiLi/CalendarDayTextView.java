package com.example.xxd.qlbisai.RiLi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * Created by xxd on 2017/7/19.
 */

public class CalendarDayTextView extends android.support.v7.widget.AppCompatTextView {

    private boolean isTody=false;

    public boolean isBefore() {
        return before;
    }

    public void setBefore(boolean before) {
        this.before = before;
    }

    public boolean isNext() {
        return next;
    }

    public void setNext(boolean next) {
        this.next = next;
    }

    private boolean before;
    private boolean next;
    private Paint paint=new Paint();

    public boolean isTody() {
        return isTody;
    }

    public void setTody(boolean tody) {
        isTody = tody;
    }


    public CalendarDayTextView(Context context) {
        super(context);
    }

    public CalendarDayTextView(Context context,AttributeSet attrs) {
        super(context, attrs);
        initControl();
    }

    public CalendarDayTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl();
    }

    private void initControl(){
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#ff0000"));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isTody) {
            canvas.translate(getWidth() / 2, getHeight() / 2);
            canvas.drawCircle(0, 0, getWidth() / 4, paint);
        }
    }
}
