package com.example.xxd.qlbisai.myBean;

import android.graphics.Bitmap;

import java.io.File;
import java.util.Date;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;


/**
 * Created by xxd on 2017/7/22.
 */

public class MyUserBean extends BmobUser{
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private String city;
    private String name;
    private String age;
    private Integer sex;
    private Date date;

    public BmobFile getHead() {
        return headimage;
    }

    public void setHead(BmobFile head) {
        this.headimage = head;
    }

    private BmobFile headimage;

}
