package com.example.xxd.qlbisai.myBean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 梁胜峰1 on 2017/8/6.
 */

public class ItemBean extends BmobObject {
    public ItemBean(String name,int color){
        this.name = name;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    private int color;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public float getHowLong() {
        return howLong;
    }

    public void setHowLong(float howLong) {
        this.howLong = howLong;
    }

    private float howLong = 0;

    public DayBean getDayBean() {
        return dayBean;
    }

    public void setDayBean(DayBean dayBean) {
        this.dayBean = dayBean;
    }

    private DayBean dayBean;
}
