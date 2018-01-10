package com.example.xxd.qlbisai.myBean;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;

/**
 * Created by 梁胜峰1 on 2017/7/25.
 */

public class AffairBean extends BmobObject {
    private static final long serialVersionUID = 1L;

    public MyUserBean getName() {
        return name;
    }

    public void setName(MyUserBean name) {
        this.name = name;
    }

    private MyUserBean name;
    public AffairBean(){};
    public AffairBean(String[] affair, int color){
        this.color = color;
        this.root = affair[0];
        for(int i = 1;i<affair.length;i++){
            children.add(affair[i]);
        }
    }
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    private int color;
    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    private String root;

    public ArrayList<String> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<String> children) {
        this.children = children;
    }

    private ArrayList<String> children = new ArrayList<String>();

}
