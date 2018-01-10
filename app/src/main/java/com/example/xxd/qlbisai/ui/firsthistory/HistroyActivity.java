package com.example.xxd.qlbisai.ui.firsthistory;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xxd.qlbisai.R;
import com.example.xxd.qlbisai.myBean.MyPlanBean;
import com.example.xxd.qlbisai.myBean.MyUserBean;
import com.example.xxd.qlbisai.myBean.ThingBean;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by xxd on 2017/8/10.
 */

public class HistroyActivity extends AppCompatActivity {
    
    private String date;
    private boolean flag;
    private ThingBean[][] things=new ThingBean[18][2];
    private List<String> times;
    private List<ThingBean> lista;
    private List<ThingBean> listb;
    private ListView listView;
    private HistoryAdapter adapter;
    private TextView ttt;
    private MyUserBean user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        date=getIntent().getStringExtra("date");
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        user= BmobUser.getCurrentUser(this,MyUserBean.class);
        setContentView(R.layout.activity_history);
        date=getIntent().getStringExtra("date");
        initData();
    }

    private void initList() {
        if(flag){

            listView=(ListView)findViewById(R.id.history_lv);
            listView.setVisibility(View.VISIBLE);
            times=new ArrayList<String>();
            lista=new ArrayList<ThingBean>();
            listb=new ArrayList<ThingBean>();
            loadTime();
            loadLista();
            loadListb();
            adapter=new HistoryAdapter(times,lista,listb,this);
            listView.setAdapter(adapter);

        }else {
            ttt=(TextView)findViewById(R.id.tv_history);
            ttt.setVisibility(View.VISIBLE);
        }

    }

    private void loadLista() {
        for(int i=0;i<18;i++){
            if(!things[i][0].getDone().equals("")){
                lista.add(things[i][0]);
            }else {
                lista.add(new ThingBean("无",getResources().getColor(R.color.gray)));
            }
        }
    }

    private void loadListb() {
        for(int i=0;i<18;i++){
            if(!things[i][1].getDone().equals("")){
                listb.add(things[i][1]);
            }else {
                listb.add(new ThingBean("无",getResources().getColor(R.color.gray)));
            }
        }
    }

    private void loadTime() {
        times.add("6:00");
        times.add("7:00");
        times.add("8:00");
        times.add("9:00");
        times.add("10:00");
        times.add("11:00");
        times.add("12:00");
        times.add("13:00");
        times.add("14:00");
        times.add("15:00");
        times.add("16:00");
        times.add("17:00");
        times.add("18:00");
        times.add("19:00");
        times.add("20:00");
        times.add("21:00");
        times.add("22:00");
        times.add("23:00");
    }


    private void initData() {
        BmobQuery<MyPlanBean> query = new BmobQuery<MyPlanBean>();
        query.addWhereEqualTo("date", date);
        query.addWhereEqualTo("name", user.getUsername());
        query.setLimit(50);
        query.findObjects(this, new FindListener<MyPlanBean>() {
            @Override
            public void onSuccess(List<MyPlanBean> object) {

                if(object!=null){
                    List<MyPlanBean> lists=new ArrayList<MyPlanBean>();
                    for(MyPlanBean person:object){
                        lists.add(person);
                    }

                    if(lists.size()==0){
                        flag=false;
                        initList();
                    }else {

                        things=lists.get(0).getThings();
                        flag=true;
                        initList();
                        Log.d("ninininini", "onSuccess: ");
                    }

                }

            }
            @Override
            public void onError(int code, String msg) {


                Toast.makeText(HistroyActivity.this,"网络连接异常",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
