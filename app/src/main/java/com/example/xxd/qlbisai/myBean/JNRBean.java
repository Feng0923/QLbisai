package com.example.xxd.qlbisai.myBean;

import java.util.Date;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by xxd on 2017/7/29.
 */

public class JNRBean extends BmobObject {
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private Date date;

    public int getIndex() {
        return index;
    }

    public BmobFile getJnrb() {
        return jnrb;
    }

    public void setJnrb(BmobFile jnrb) {
        this.jnrb = jnrb;
    }

    private BmobFile jnrb;

    public void setIndex(int index) {
        this.index = index;
    }

    private int index;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;
}
