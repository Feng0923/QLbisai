package com.example.xxd.qlbisai.myBean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by 梁胜峰1 on 2017/7/27.
 */

public class ThingBean extends BmobObject {
    private static final long serialVersionUID = 1L;

    public MyPlanBean getPlanBean() {
        return planBean;
    }

    public void setPlanBean(MyPlanBean planBean) {
        this.planBean = planBean;
    }

    private MyPlanBean planBean;
    public ThingBean(String done, int time, int color){
        this.done = done;
        this.color = color;
        this.time = time;
    }
    public ThingBean(String done, int color){
        this.color = color;
        this.done = done;
    }
    public String getDone() {
        return done;
    }

    private String done;

    public int getTime() {
        return time;
    }



    private int time;

    public int getColor() {
        return color;
    }


    private int color;
}
