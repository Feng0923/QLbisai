package com.example.xxd.qlbisai.ui.LogCat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xxd.qlbisai.R;
import com.example.xxd.qlbisai.myBean.LogcatBean;
import com.example.xxd.qlbisai.myBean.MyUserBean;
import com.example.xxd.qlbisai.ui.RefleshListView.PullDownView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import okhttp3.Call;

/**
 * Created by xxd on 2017/7/27.
 */

public class LogcatActivity extends AppCompatActivity implements PullDownView.OnPullDownListener, AdapterView.OnItemClickListener{
    private static final int WHAT_DID_LOAD_DATA = 0;
    private static final int WHAT_DID_REFRESH = 1;
    private PullDownView mPullDownView;
    private ListView listview;
    private List<LogcatBean> lists;
    private LogcatAdapter logcatAdapter;
    private TextView tv_ok;
    MyUserBean user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = BmobUser.getCurrentUser(this,MyUserBean.class);
        setContentView(R.layout.activity_logcat);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        initList();
    }

    private void initList() {
        mPullDownView = (PullDownView)findViewById(R.id.pdv_log);
        mPullDownView.setOnPullDownListener(this);
        listview = mPullDownView.getListView();
        lists=new ArrayList<LogcatBean>();
        listview.setOnItemClickListener(this);

        tv_ok=(TextView)findViewById(R.id.tv_finish);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_ok.setVisibility(View.INVISIBLE);
                LogcatAdapter.flag1=false;

                for(int i=0;i<lists.size();i++){
                    if(lists.get(i).getDelete()){
                        LogcatBean logcatBean = new LogcatBean();
                        logcatBean.setObjectId(lists.get(i).getObjectId());
                        logcatBean.delete(LogcatActivity.this, new DeleteListener() {
                            @Override
                            public void onSuccess() {
                                // TODO Auto-generated method stub
                                Log.i("bmob","删除成功");
                            }
                            @Override
                            public void onFailure(int code, String msg) {
                                // TODO Auto-generated method stub
                                Log.i("bmob","删除失败："+msg);
                            }
                        });
                    }
                }
                loadData();
            }
        });
        listview.setOnItemClickListener(this);
        loadData();
    }

    public void addLog(View view){
        Intent intent=new Intent(this,LogcatItemActivity.class);
        intent.putExtra("what","no");
        Log.d("???", "addLog: ");
        startActivityForResult(intent,1);

    }

    public void deleteLog(View view){
        tv_ok.setVisibility(View.VISIBLE);
        LogcatAdapter.flag1=true;
        logcatAdapter.notifyDataSetChanged();

    }


    public void back(View view){
        finish();
    }
    
    

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(this,LogcatItemActivity.class);
        intent.putExtra("what","have");
        intent.putExtra("message",lists.get(position).getLogcat());
        intent.putExtra("id",lists.get(position).getObjectId());
        String s=lists.get(position).getUpdatedAt();
        String y=s.substring(0,4)+"年";
        String m;
        if(s.charAt(5)=='0'){
            if(s.charAt(8)=='0'){
                m=s.charAt(6)+"月"+s.charAt(9)+"日";
            }else {
                m=s.charAt(6)+"月"+s.charAt(8)+s.charAt(9)+"日";
            }
        }else {
            if(s.charAt(8)=='0'){
                m=s.charAt(5)+s.charAt(6)+"月"+s.charAt(9)+"日";
            }else {
                m=s.charAt(5)+s.charAt(6)+"月"+s.charAt(8)+s.charAt(9)+"日";
            }

        }
        intent.putExtra("time",y+m);
        startActivityForResult(intent,1);


        
    }
    private void loadData() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                BmobQuery<LogcatBean> query = new BmobQuery<LogcatBean>();
                query.addWhereEqualTo("name", user.getUsername());
                query.setLimit(50);
                query.findObjects(LogcatActivity.this, new FindListener<LogcatBean>() {
                    @Override
                    public void onSuccess(List<LogcatBean> object) {
                        lists.clear();
                        for(LogcatBean person:object){
                            lists.add(person);
                        }
                        logcatAdapter=new LogcatAdapter(lists,LogcatActivity.this);
                        listview.setAdapter(logcatAdapter);
                    }
                    @Override
                    public void onError(int code, String msg) {
                        Toast.makeText(LogcatActivity.this,"网络连接异常",Toast.LENGTH_SHORT).show();
                    }
                });
                Message msg =new Message();
                msg.what=WHAT_DID_LOAD_DATA;
                mUIHandler.sendMessage(msg);

            }
        }).start();
    }

    
    
    @Override
    public void onRefresh() {

        new Thread(new Runnable() {

            @Override
            public void run() {

                BmobQuery<LogcatBean> query = new BmobQuery<LogcatBean>();
                query.addWhereEqualTo("name", user.getUsername());
                query.setLimit(50);
                query.findObjects(LogcatActivity.this, new FindListener<LogcatBean>() {
                    @Override
                    public void onSuccess(List<LogcatBean> object) {
                        lists.clear();
                        for(LogcatBean person:object){
                            lists.add(person);
                        }
                        logcatAdapter=new LogcatAdapter(lists,LogcatActivity.this);
                        listview.setAdapter(logcatAdapter);
                    }
                    @Override
                    public void onError(int code, String msg) {
                        Toast.makeText(LogcatActivity.this,"网络连接异常",Toast.LENGTH_SHORT).show();
                    }
                });
                Message msg =new Message();
                msg.what=WHAT_DID_REFRESH;
                mUIHandler.sendMessage(msg);
            }
        }).start();

    }



    private Handler mUIHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_DID_LOAD_DATA:{
                    Log.e("###########","here") ;

                    mPullDownView.notifyDidLoad();
                    break;
                }
                case WHAT_DID_REFRESH :{

                    // 告诉它更新完毕
                    mPullDownView.notifyDidRefresh();
                    break;
                }

            }

        }

    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    loadData();
                }
                break;

        }
    }

    @Override
    public void finish() {
        super.finish();
        LogcatAdapter.flag1=false;
    }
}
