package com.example.xxd.qlbisai.myBean;

import cn.bmob.v3.BmobObject;

/**
 * Created by xxd on 2017/7/27.
 */

public class LogcatBean extends BmobObject{

    public String getLogcat() {
        return logcat;
    }

    public void setLogcat(String logcat) {
        this.logcat = logcat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String logcat;
    private String name;

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    private Boolean delete;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    private String color;
}
