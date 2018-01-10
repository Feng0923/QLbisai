package com.example.xxd.qlbisai.ui.jnr;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.a.a.I;
import com.example.xxd.qlbisai.R;
import com.example.xxd.qlbisai.login.LoginActivity;
import com.example.xxd.qlbisai.myBean.JNRBean;
import com.example.xxd.qlbisai.myBean.LogcatBean;
import com.example.xxd.qlbisai.myBean.MyUserBean;
import com.example.xxd.qlbisai.ui.LogCat.LogcatActivity;
import com.example.xxd.qlbisai.ui.LogCat.LogcatAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by xxd on 2017/7/29.
 */

public class JNRActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ImageView imageView;
    private ListView listView;
    private TextView tv_day,tv_title,tv_descrip;
    private JNRAdapter jnrAdapter;
    private ProgressDialog loadingDialog;
    MyUserBean user;

    private List<JNRBean> lists;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = BmobUser.getCurrentUser(this,MyUserBean.class);
        setContentView(R.layout.activity_jnr);
        if (getSupportActionBar()!=null)
            getSupportActionBar().hide();
        tv_day=(TextView)findViewById(R.id.tv_day);
        tv_title=(TextView)findViewById(R.id.tv_title);
        tv_descrip=(TextView)findViewById(R.id.tv_discrip);
        imageView=(ImageView)findViewById(R.id.add_jnr);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(JNRActivity.this,JNRAddActivity.class);
                startActivityForResult(intent,1);


            }
        });
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);

                ViewGroup.LayoutParams para;
                para = imageView.getLayoutParams();
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        para.height=dm.heightPixels*1/10;
                        para.width=dm.widthPixels*1/6;
                        imageView.setLayoutParams(para);
                        break;
                    case MotionEvent.ACTION_UP:
                        para.height=dm.heightPixels*1/11;
                        para.width=dm.widthPixels*1/7;
                        imageView.setLayoutParams(para);
                        break;
                }
                return false;
            }
        });
        listView=(ListView)findViewById(R.id.lv_jnr);
        lists=new ArrayList<JNRBean>();
        listView.setOnItemClickListener(this);
        loadingDialog = new ProgressDialog(JNRActivity.this);
        loadingDialog.setMessage("正在加载, 请稍候...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(true);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadData();

    }

    private void loadData() {
        loadingDialog.show();
                BmobQuery<JNRBean> query = new BmobQuery<JNRBean>();
                query.addWhereEqualTo("name", user.getUsername());
                query.setLimit(50);
                query.findObjects(JNRActivity.this, new FindListener<JNRBean>() {
                    @Override
                    public void onSuccess(List<JNRBean> object) {
                        lists.clear();
                        for(JNRBean person:object){
                            lists.add(person);
                        }
                        jnrAdapter=new JNRAdapter(JNRActivity.this,lists,1);
                        listView.setAdapter(jnrAdapter);
                        Message msg =new Message();
                        msg.what=1;
                        mUIHandler.sendMessage(msg);
                        Log.d("11111", "onSuccess: "+lists.size());

                    }
                    @Override
                    public void onError(int code, String msg) {
                        loadingDialog.dismiss();
                        Toast.makeText(JNRActivity.this,"网络连接异常",Toast.LENGTH_SHORT).show();
                    }
                });



    }

    public void back(View view){
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Intent intent=new Intent(this,JNRChangeActivity.class);
        intent.putExtra("title",lists.get(position).getTitle());
        intent.putExtra("id",lists.get(position).getObjectId());
        Date d=lists.get(position).getDate();
        intent.putExtra("time",d.getYear()+1900+"-"+(d.getMonth()+1)+"-"+d.getDay());
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        long time1 = cal.getTimeInMillis();
        cal.setTime(d);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);
        int i=Integer.parseInt(String.valueOf(between_days));
        intent.putExtra("day",i+"");
        switch (lists.get(position).getIndex()){
            case 0:
                intent.putExtra("index",0);
                BmobFile bb=lists.get(position).getJnrb();
                final File saveFile = new File(Environment.getExternalStorageDirectory(), bb.getFilename());
                if(saveFile.exists()){
                    intent.putExtra("path",saveFile.getPath());
                }else {
                    bb.download(this, saveFile, new DownloadFileListener() {
                        @Override
                        public void onSuccess(String s) {
                            intent.putExtra("path",saveFile.getPath());
                        }
                        @Override
                        public void onFailure(int i, String s) {
                            intent.putExtra("path","no");
                        }
                    });
                }
                break;
            case 1:
                intent.putExtra("index",1);
                break;
            case 2:
                intent.putExtra("index",2);
                break;
            case 3:
                intent.putExtra("index",3);
                break;
            case 4:
                intent.putExtra("index",4);
                break;
            case 5:
                intent.putExtra("index",5);
                break;
            case 6:
                intent.putExtra("index",6);
                break;
        }
        startActivityForResult(intent,1);


    }


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


    private Handler mUIHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:{
                    if(lists.size()>0){
                        tv_title.setText(lists.get(0).getTitle());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(new Date());
                        long time1 = cal.getTimeInMillis();
                        cal.setTime(lists.get(0).getDate());
                        long time2 = cal.getTimeInMillis();
                        long between_days=(time2-time1)/(1000*3600*24);
                        int i=Integer.parseInt(String.valueOf(between_days));
                        tv_day.setText(i+"");
                        Date date=lists.get(0).getDate();
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, date.getYear()+1900);//先指定年份
                        calendar.set(Calendar.MONTH, date.getMonth());//再指定月份 Java月份从0开始算
                        calendar.set(Calendar.DAY_OF_MONTH, date.getDate());
                        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                        switch (dayOfWeek) {
                            case 1:
                                tv_descrip.setText("目标日:"+(date.getMonth()+1)+"月"+date.getDate()+","+(date.getYear()+1900)+"(周日)");
                                break;
                            case 2:
                                tv_descrip.setText("目标日:"+(date.getMonth()+1)+"月"+date.getDate()+","+(date.getYear()+1900)+"(周一)");
                                break;
                            case 3:
                                tv_descrip.setText("目标日:"+(date.getMonth()+1)+"月"+date.getDate()+","+(date.getYear()+1900)+"(周二)");
                                break;
                            case 4:
                                tv_descrip.setText("目标日:"+(date.getMonth()+1)+"月"+date.getDate()+","+(date.getYear()+1900)+"(周三)");
                                break;
                            case 5:
                                tv_descrip.setText("目标日:"+(date.getMonth()+1)+"月"+date.getDate()+","+(date.getYear()+1900)+"(周四)");
                                break;
                            case 6:
                                tv_descrip.setText("目标日:"+(date.getMonth()+1)+"月"+date.getDate()+","+(date.getYear()+1900)+"(周五)");
                                break;
                            case 7:
                                tv_descrip.setText("目标日:"+(date.getMonth()+1)+"月"+date.getDate()+","+(date.getYear()+1900)+"(周六)");
                                break;

                        }
                        tv_day.setTextSize(80);
                        loadingDialog.dismiss();
                    }else {
                        tv_day.setText("快设置纪念日吧");
                        tv_day.setTextSize(40);
                    }
                    break;
                }
            }

        }

    };
}


