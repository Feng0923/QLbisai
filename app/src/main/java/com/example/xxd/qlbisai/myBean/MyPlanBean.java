package com.example.xxd.qlbisai.myBean;

import java.util.Date;

import cn.bmob.v3.BmobObject;

/**
 * Created by xxd on 2017/8/5.
 */

public class MyPlanBean extends BmobObject {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        private String date;
        public ThingBean[][] getThings() {
            return things;
        }

        public void setThings(ThingBean[][] things) {
            this.things = things;
        }
        private ThingBean[][] things;
}
