package com.example.xxd.qlbisai.myBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by 梁胜峰1 on 2017/7/22.
 */

public class DayBean extends BmobObject{
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String userName;
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time;

    public ItemBean[] getItems() {
        return items;
    }

    public void setItems(ItemBean[] items) {
        this.items = items;
    }

    private ItemBean[] items;
    public DayBean(String time){
        this.time = time;
    }
}
