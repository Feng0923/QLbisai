package com.example.xxd.qlbisai.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.xxd.qlbisai.R;
import com.example.xxd.qlbisai.myBean.AffairBean;
import com.example.xxd.qlbisai.myBean.MyUserBean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


public class AffairControler {
    private Context context;
    private  Handler handler;
    public void get() {
        MyUserBean bu = BmobUser.getCurrentUser(context,MyUserBean.class);
        BmobQuery<AffairBean> query = new BmobQuery<AffairBean>();
        query.addWhereEqualTo("name",bu);
            query.findObjects(context,new FindListener<AffairBean>() {
                @Override
                public void onSuccess(List<AffairBean> list) {
                        afs = list;
                        Message m = new Message();
                        m.what = 2;
                        handler.sendMessageAtTime(m,888);
                }

                @Override
                public void onError(int i, String s) {
                    Toast.makeText(context, "失败", Toast.LENGTH_SHORT).show();
                }

            });
        }

    public void setAfs(ArrayList<AffairBean> afs) {
        this.afs = afs;
    }

    public List<AffairBean> getAfs() {
        return afs;
    }

    private List<AffairBean> afs = new ArrayList<AffairBean>();
    public AffairControler(Context context,Handler hander){
        this.context = context;
        this.handler = hander;
//        setColor();
//        quest();
    }

    private void setColor() {
        colors = new int[]{
                context.getResources().getColor(R.color.red),
                context.getResources().getColor(R.color.colorAccent),
                context.getResources().getColor(R.color.yellow),
                context.getResources().getColor(R.color.green),
                context.getResources().getColor(R.color.blue),
                context.getResources().getColor(R.color.colorPrimaryDark),
                context.getResources().getColor(R.color.purse)
        };
    }
    private void quest() {
        for(int i = 0;i<labels.length;i++){
            AffairBean affair = new AffairBean(labels[i],colors[i]);
            afs.add(affair);
            affair.save(context,new SaveListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(context,"success",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.makeText(context,"failed",Toast.LENGTH_SHORT).show();
                }


            });
        }
    }


    String[][] labels  = new String[][]{
            {"上班", "主要工作", "业余工作", "其他工作"},
            {"学习", "读书", "作业", "其他学习"},
            {"阅读", "散文", "名著", "其他阅读"},
            {"运动","健身","慢跑"},
            {"玩游戏","LOL","CF","DNF","其他游戏"},
            {"看电视"},
            {"睡觉"}
    };
    int[]  colors ;
    public void update(ArrayList<String> l,AffairBean affair){
                MyUserBean bu = BmobUser.getCurrentUser(context,MyUserBean.class);
                BmobQuery<AffairBean> query = new BmobQuery<AffairBean>();
                AffairBean affair_ = new AffairBean();
                affair_.setColor(affair.getColor());
                affair_.setRoot(affair.getRoot());
                affair_.setChildren(l);
                affair_.update(context,affair.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(context,"success",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }


                });
    }
}
