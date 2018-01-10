package com.example.xxd.qlbisai.login;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.xxd.qlbisai.R;
import com.example.xxd.qlbisai.myBean.AffairBean;
import com.example.xxd.qlbisai.myBean.MyUserBean;
import com.example.xxd.qlbisai.utils.ImageUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;


/**
 * Created by xxd on 2017/7/19.
 */

public class CompleteActivity extends AppCompatActivity {
    private ProgressDialog loadingDialog;
    private EditText name_edt,age_edt,city_edg;
    private RadioGroup groip;
    int sex;
    private String name;
    private String birthday;
    private String  city;
    private String number;
    private String password;
    private Date date;
    private Bitmap head;

    private ImageView headImage;
    private ImageUtils imageUtils = null;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_complete);
        initView();
    }

    private java.util.Calendar cal;
    private int year,month,day;
    private void getDate(){
        cal= java.util.Calendar .getInstance();
        year=cal.get(cal.YEAR);
        month=cal.get(cal.MONTH);
        day=cal.get(cal.DAY_OF_MONTH);
    }

    private void initView() {
        imageUtils=new ImageUtils(this);
        headImage=(ImageView)findViewById(R.id.imageButton) ;
        name_edt=(EditText)findViewById(R.id.name_edt);
        age_edt=(EditText)findViewById(R.id.age_edt);
        city_edg=(EditText)findViewById(R.id.city_edt);
        groip=(RadioGroup)findViewById(R.id.group);
        loadingDialog = new ProgressDialog(CompleteActivity.this);
        loadingDialog.setMessage("正在注册, 请稍候...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(true);
        getDate();
        number=getIntent().getStringExtra("phone");
        password=getIntent().getStringExtra("password");


        age_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        age_edt.setText(String.valueOf(CompleteActivity.this.year-year));
                        date=new Date(year,month,dayOfMonth);
                    }
                };
                DatePickerDialog dialog=new DatePickerDialog(CompleteActivity.this,DatePickerDialog.THEME_HOLO_LIGHT,listener,year,month,day);
                dialog.show();

            }
        });

    }



    public void selectHead(View view){

        new AlertDialog.Builder(this)//
                    .setTitle("选择头像")//

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





    public void send(View view){


        name=name_edt.getText().toString();
        birthday=age_edt.getText().toString();
        city=city_edg.getText().toString();

        switch(groip.getCheckedRadioButtonId()){
            case R.id.button1 :
                sex=1;
                break;
            case R.id.button2 :
                sex=0;
                break;
        }

//        Log.e("aaaaaaaa",birthday+name+school+password+phonenumber+invitenumber+sex+path);
        if(!birthday.equals("")&&!name.equals("")&&!city.equals("")&&(sex==0||sex==1)){
            loadingDialog.show();
            final MyUserBean bu = new MyUserBean();
            setColors();
            if(ImageUtils.photoUri!=null){
                File f=ImageUtils.picFile;
                final BmobFile bmobFile=new BmobFile(f);
                bmobFile.uploadblock(this, new UploadFileListener() {

                    @Override
                    public void onSuccess() {
                        //bmobFile.getFileUrl(context)--返回的上传文件的完整地址
                        Log.d("898989", "onsuccess: "+bmobFile.getFileUrl(CompleteActivity.this));
                        bu.setHead(bmobFile);
                        bu.setUsername(number);
                        bu.setMobilePhoneNumber(number);
                        bu.setPassword(password);
                        bu.setCity(city);
                        bu.setName(name);
                        bu.setAge(birthday);
                        bu.setSex(sex);
                        bu.setDate(date);

                        bu.signUp(CompleteActivity.this, new SaveListener() {
                            @Override
                            public void onSuccess() {
                                loadingDialog.dismiss();
                                Log.d("66666", "onSuccess: ");
                                quest(bu);
                                Toast.makeText(CompleteActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                setResult(RESULT_OK, intent);
                                finish();
                                //通过BmobUser.getCurrentUser(context)方法获取登录成功后的本地用户信息
                            }
                            @Override
                            public void onFailure(int code, String msg) {
                                loadingDialog.dismiss();
                                // TODO Auto-generated method stub
                                Log.d("666666", "onFailure: "+msg);
                                Toast.makeText(CompleteActivity.this,"改号码已被注册",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(CompleteActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    }
                });

            }else{
                bu.setUsername(number);
                bu.setMobilePhoneNumber(number);
                bu.setPassword(password);
                bu.setCity(city);
                bu.setName(name);
                bu.setAge(birthday);
                bu.setSex(sex);
                bu.setDate(date);


                bu.signUp(CompleteActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        loadingDialog.dismiss();
                        Log.d("66666", "onSuccess: ");
                        quest(bu);
                        Toast.makeText(CompleteActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                        //通过BmobUser.getCurrentUser(context)方法获取登录成功后的本地用户信息
                    }
                    @Override
                    public void onFailure(int code, String msg) {
                        loadingDialog.dismiss();
                        // TODO Auto-generated method stub
                        Log.d("666666", "onFailure: "+msg);
                        Toast.makeText(CompleteActivity.this,"改号码已被注册",Toast.LENGTH_SHORT).show();
                    }
                });

            }


        }

        else
            Toast.makeText(this, "请完善信息", Toast.LENGTH_SHORT).show();

    }
    private void quest(MyUserBean bu) {
        for(int i = 0;i<labels.length;i++){
            AffairBean affair = new AffairBean(labels[i],colors[i]);
            List<AffairBean> afs = new ArrayList<AffairBean>();
            afs.add(affair);
            affair.setName(bu);
            affair.save(this,new SaveListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(CompleteActivity.this,"success",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.makeText(CompleteActivity.this,"failed",Toast.LENGTH_SHORT).show();
                }


            });
        }
    }
    int[] colors;
    String[][] labels  = new String[][]{
            {"上班", "主要工作", "业余工作", "其他工作"},
            {"学习", "读书", "作业", "其他学习"},
            {"阅读", "散文", "名著", "其他阅读"},
            {"运动","健身","慢跑"},
            {"玩游戏","LOL","CF","DNF","其他游戏"},
            {"看电视"},
            {"睡觉"}
    };
    private void setColors() {
            colors = new int[]{
                    getResources().getColor(R.color.red),
                    getResources().getColor(R.color.colorAccent),
                    getResources().getColor(R.color.yellow),
                    getResources().getColor(R.color.green),
                    getResources().getColor(R.color.blue),
                    getResources().getColor(R.color.colorPrimaryDark),
                    getResources().getColor(R.color.purse)
            };
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
                            headImage.setImageBitmap(bm_icon);
                            DisplayMetrics dm=new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(dm);
                            ViewGroup.LayoutParams params=headImage.getLayoutParams();
                            params.height=dm.widthPixels*2/9;
                            params.width=dm.widthPixels*2/9;
                            headImage.setLayoutParams(params);
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

}
