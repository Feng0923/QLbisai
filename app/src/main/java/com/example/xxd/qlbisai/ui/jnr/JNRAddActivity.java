package com.example.xxd.qlbisai.ui.jnr;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.xxd.qlbisai.R;
import com.example.xxd.qlbisai.RiLi.MyNumberPicker;
import com.example.xxd.qlbisai.myBean.JNRBean;
import com.example.xxd.qlbisai.myBean.MyUserBean;
import com.example.xxd.qlbisai.ui.oneFragment;
import com.example.xxd.qlbisai.utils.ImageUtils;
import com.example.xxd.qlbisai.utils.JNRImageUtils;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by xxd on 2017/7/29.
 */

public class JNRAddActivity extends AppCompatActivity {
    private MyNumberPicker np_year,np_month,np_day;
    private Date date;
    private EditText edt;
    private ProgressDialog loadingDialog;
    int year;
    int month;
    private int index=1;
    private ImageView big_img;
    MyUserBean user;
    private ImageView[] imgs;
    private String path;
    int day;
    private JNRImageUtils imageUtils;
    private IntentFilter intentFilter;
    private FilePathReceiver receiver;
    Calendar calendar = Calendar.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_jnr_add);
        user = BmobUser.getCurrentUser(this,MyUserBean.class);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        intentFilter=new IntentFilter();
        intentFilter.addAction("com.xxd.path");
        receiver=new FilePathReceiver();
        registerReceiver(receiver,intentFilter);
        initView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void initView() {
        imageUtils=new JNRImageUtils(this);
        imgs=new ImageView[7];
        imgs[0]=(ImageView)findViewById(R.id.jnr_bb_0);
        imgs[1]=(ImageView)findViewById(R.id.jnr_bb_1);
        imgs[2]=(ImageView)findViewById(R.id.jnr_bb_2);
        imgs[3]=(ImageView)findViewById(R.id.jnr_bb_3);
        imgs[4]=(ImageView)findViewById(R.id.jnr_bb_4);
        imgs[5]=(ImageView)findViewById(R.id.jnr_bb_5);
        imgs[6]=(ImageView)findViewById(R.id.jnr_bb_6);
        big_img=(ImageView)findViewById(R.id.big_bb);
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);



        for(int i=0;i<imgs.length;i++){
            ViewGroup.LayoutParams params=imgs[i].getLayoutParams();
            params.height=dm.widthPixels*7/24;
            params.width=dm.widthPixels*7/24;
            imgs[i].setLayoutParams(params);
        }
        loadingDialog = new ProgressDialog(JNRAddActivity.this);
        loadingDialog.setMessage("正在保存, 请稍候...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(true);
        edt=(EditText)findViewById(R.id.edt_title) ;
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        np_year=(MyNumberPicker)findViewById(R.id.np_year);
        np_month=(MyNumberPicker)findViewById(R.id.np_month);
        np_day=(MyNumberPicker)findViewById(R.id.np_day);
        np_year.setNumberPickerDividerColor(np_year,Color.parseColor("#ffffff"));
        np_month.setNumberPickerDividerColor(np_month,Color.parseColor("#ffffff"));
        np_day.setNumberPickerDividerColor(np_day,Color.parseColor("#ffffff"));
        np_year.setWrapSelectorWheel(false);
        np_month.setWrapSelectorWheel(false);
        np_day.setWrapSelectorWheel(false);
        np_year.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                year=newVal;
                setMaxDay();
            }
        });
        np_month.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                month=newVal;
                setMaxDay();
            }
        });
        np_day.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                day=newVal;
                setMaxDay();
            }
        });
        np_year.setMinValue(1901);
        np_year.setMaxValue(2049);
        np_year.setValue(year);
        np_month.setMinValue(1);
        np_month.setMaxValue(12);
        np_month.setValue(month);
        np_day.setMinValue(1);
        setMaxDay();
        np_day.setValue(day);
        Log.d("22222", "initView: "+day);


    }


    public void setMaxDay(){
        calendar.set(Calendar.YEAR, year);//先指定年份
        calendar.set(Calendar.MONTH, month - 1);//再指定月份 Java月份从0开始算
        int daysCountOfMonth = calendar.getActualMaximum(Calendar.DATE);//获取指定年份中指定月份有几天
        np_day.setMaxValue(daysCountOfMonth);
    }

    public void finish(View view){
        if(edt.getText().toString().length()==0){
            Toast.makeText(JNRAddActivity.this,"标题不能为空！",Toast.LENGTH_SHORT).show();
            return;
        }
        if(edt.length()>10){
            Toast.makeText(JNRAddActivity.this,"标题长度最大为10噢！",Toast.LENGTH_SHORT).show();
            return;
        }
        char[] c=edt.getText().toString().toCharArray();
        boolean f=true;
        for(int i=0;i<c.length;i++){
            if(c[i]!=' ')
                f=false;
        }
        if(f)
            return;
        loadingDialog.show();
        date=new Date(year-1900,month-1,day);
        final JNRBean jnrbean= new JNRBean();
        if(index==0){
            File file=new File(path);
            final BmobFile bmobFile=new BmobFile(file);
            bmobFile.uploadblock(this, new UploadFileListener() {

                @Override
                public void onSuccess() {
                    //bmobFile.getFileUrl(context)--返回的上传文件的完整地址
                    Log.d("898989", "onsuccess: "+bmobFile.getFileUrl(JNRAddActivity.this));
                    jnrbean.setTitle(edt.getText().toString());
                    jnrbean.setName(user.getUsername());
                    jnrbean.setDate(date);
                    jnrbean.setIndex(index);
                    jnrbean.setJnrb(bmobFile);

                    jnrbean.save(JNRAddActivity.this, new SaveListener() {

                        @Override
                        public void onSuccess() {
                            loadingDialog.dismiss();
                            Toast.makeText(JNRAddActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                            oneFragment.update=true;
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }

                        @Override
                        public void onFailure(int code, String arg0) {
                            loadingDialog.dismiss();
                            Toast.makeText(JNRAddActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onProgress(Integer value) {
                    // 返回的上传进度（百分比）
                }

                @Override
                public void onFailure(int code, String msg) {
                    Log.d("898989", "onFailure: "+msg);
                    Toast.makeText(JNRAddActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                }
            });

        }else {
            jnrbean.setTitle(edt.getText().toString());
            jnrbean.setName(user.getUsername());
            jnrbean.setDate(date);
            jnrbean.setIndex(index);
            jnrbean.save(this, new SaveListener() {

                @Override
                public void onSuccess() {
                    loadingDialog.dismiss();
                    Toast.makeText(JNRAddActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                    oneFragment.update=true;
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }

                @Override
                public void onFailure(int code, String arg0) {
                    loadingDialog.dismiss();
                    Toast.makeText(JNRAddActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void addIV(View view){
        new AlertDialog.Builder(this)//
                .setTitle("选择背景")//

                .setNegativeButton("相册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        imageUtils.byAlbum();
                    }
                })

                .setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String status = Environment.getExternalStorageState();
                        if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否存在SD卡
                            imageUtils.byCamera();
                        }


                    }
                }).show();

    }

    public void changeIV(View view){
        switch (view.getId()){
            case R.id.jnr_bb_1:
                index=1;
                big_img.setImageResource(R.drawable.bb_0);
                break;
            case R.id.jnr_bb_2:
                index=2;
                big_img.setImageResource(R.drawable.bb_1);
                break;
            case R.id.jnr_bb_3:
                index=3;
                big_img.setImageResource(R.drawable.bb_2);
                break;
            case R.id.jnr_bb_4:
                index=4;
                big_img.setImageResource(R.drawable.bb_3);
                break;
            case R.id.jnr_bb_5:
                index=5;
                big_img.setImageResource(R.drawable.bb_4);
                break;
            case R.id.jnr_bb_6:
                index=6;
                big_img.setImageResource(R.drawable.bb_5);
                break;
        }

    }

    public void back(View view){
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        switch (requestCode) {
            case ImageUtils.ACTIVITY_RESULT_CAMERA: // 拍照
                try {
                    if (resultCode == -1) {
                        imageUtils.cutImageByCamera();
                    } else {
                        // 因为在无任何操作返回时，系统依然会创建一个文件，这里就是删除那个产生的文件
                        if (imageUtils.picFile != null) {
                            imageUtils.picFile.delete();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case ImageUtils.ACTIVITY_RESULT_ALBUM:
                try {
                    if (resultCode == -1) {

                        Bitmap bm_icon = imageUtils.decodeBitmap();
                        if (bm_icon != null) {
                            big_img.setImageBitmap(bm_icon);
                            index=0;
                        }
                    } else {
                        // 因为在无任何操作返回时，系统依然会创建一个文件，这里就是删除那个产生的文件
                        if (imageUtils.picFile != null) {
                            imageUtils.picFile.delete();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    class FilePathReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            path=intent.getStringExtra("path");
            Log.d("789456", "onReceive: "+path);
        }
    }
}
