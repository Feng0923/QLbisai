package com.example.xxd.qlbisai.ui;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.xxd.qlbisai.R;
import com.example.xxd.qlbisai.login.CompleteActivity;
import com.example.xxd.qlbisai.login.LoginActivity;
import com.example.xxd.qlbisai.myBean.MyUserBean;
import com.example.xxd.qlbisai.utils.ImageUtils;
import com.example.xxd.qlbisai.utils.ImageUtilsChange;

import java.io.File;
import java.util.Date;

import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by xxd on 2017/7/26.
 */

public class ChangeActivity extends AppCompatActivity {
    private EditText name_edt,age_edt,city_edg;
    private RadioGroup groip;
    private RadioButton man_rb,woman_rb;
    private java.util.Calendar cal;
    private int year,month,day;
    private int sex;
    private String name;
    private String birthday;
    private String  city;
    private Date date;
    private ImageView img_head;
    MyUserBean user;
    private ProgressDialog loadingDialog;
    private ImageUtilsChange imageUtils = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_change);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
         user = BmobUser.getCurrentUser(this,MyUserBean.class);
        imageUtils=new ImageUtilsChange(this);
        initview();
    }

    private void initview() {
        name_edt=(EditText)findViewById(R.id.name_edt);
        age_edt=(EditText)findViewById(R.id.age_edt);
        city_edg=(EditText)findViewById(R.id.city_edt);
        groip=(RadioGroup)findViewById(R.id.group);
        man_rb=(RadioButton)findViewById(R.id.button1);
        woman_rb=(RadioButton)findViewById(R.id.button2);
        img_head=(ImageView)findViewById(R.id.img_head);
        Intent intent=getIntent();
        name_edt.setText(intent.getStringExtra("name"));
        age_edt.setText(intent.getStringExtra("age"));
        city_edg.setText(intent.getStringExtra("city"));
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("正在加载, 请稍候...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(true);


        BmobFile bb=user.getHead();
        if(bb!=null){
            loadingDialog.show();
            final File saveFile = new File(Environment.getExternalStorageDirectory(), bb.getFilename());
            bb.download(this, saveFile, new DownloadFileListener() {
                @Override
                public void onSuccess(String s) {
                    Log.d("qwt88", "onSuccess: ");
                    Bitmap b=BitmapFactory.decodeFile(saveFile.getPath());
                    img_head.setImageBitmap(b);
                    DisplayMetrics dm=new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(dm);
                    ViewGroup.LayoutParams params=img_head.getLayoutParams();
                    params.height=dm.widthPixels*2/9;
                    params.width=dm.widthPixels*2/9;
                    img_head.setLayoutParams(params);
                    loadingDialog.dismiss();

                }
                @Override
                public void onFailure(int i, String s) {
                    img_head.setImageResource(R.mipmap.head);
                    DisplayMetrics dm=new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(dm);
                    ViewGroup.LayoutParams params=img_head.getLayoutParams();
                    params.height=dm.widthPixels*2/9;
                    params.width=dm.widthPixels*2/9;
                    img_head.setLayoutParams(params);
                    loadingDialog.dismiss();
                }
            });
        }else {
            img_head.setImageResource(R.mipmap.head);
            DisplayMetrics dm=new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            ViewGroup.LayoutParams params=img_head.getLayoutParams();
            params.height=dm.widthPixels*2/9;
            params.width=dm.widthPixels*2/9;
            img_head.setLayoutParams(params);
        }


        if(intent.getStringExtra("sex").equals("1")){
            man_rb.setChecked(true);
        }else {
            woman_rb.setChecked(true);
        }
        getDate();
        age_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        age_edt.setText(String.valueOf(ChangeActivity.this.year-year));
                        date=new Date(year,month,dayOfMonth);
                    }
                };
                DatePickerDialog dialog=new DatePickerDialog(ChangeActivity.this,DatePickerDialog.THEME_HOLO_LIGHT,listener,year,month,day);
                dialog.show();

            }
        });

    }



    private void getDate(){
        cal= java.util.Calendar .getInstance();
        year=cal.get(cal.YEAR);
        month=cal.get(cal.MONTH);
        day=cal.get(cal.DAY_OF_MONTH);
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
        if(!birthday.equals("")&&!name.equals("")&&!city.equals("")&&(sex==0||sex==1)){
            final MyUserBean bu = new MyUserBean();
            if(ImageUtilsChange.photoUri!=null){
                File f=ImageUtilsChange.picFile;
                final BmobFile bmobFile=new BmobFile(f);
                bmobFile.uploadblock(this, new UploadFileListener() {

                    @Override
                    public void onSuccess() {
                        bu.setHead(bmobFile);
                        bu.setCity(city);
                        bu.setName(name);
                        bu.setAge(birthday);
                        bu.setSex(sex);
                        if(date==null) {
                        }else {
                            bu.setDate(date);
                        }
                        MyUserBean user=MyUserBean.getCurrentUser(ChangeActivity.this,MyUserBean.class);
                        bu.update(ChangeActivity.this, user.getObjectId(), new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(ChangeActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                setResult(RESULT_OK, intent);
                                finish();
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                Toast.makeText(ChangeActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ChangeActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                });

            }else {
                bu.setCity(city);
                bu.setName(name);
                bu.setAge(birthday);
                bu.setSex(sex);
                if(date==null) {
                }else {
                    bu.setDate(date);
                }
                MyUserBean user=MyUserBean.getCurrentUser(this,MyUserBean.class);
                bu.update(this, user.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(ChangeActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(ChangeActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                });
            }


        }
        else
            Toast.makeText(this, "请完善信息", Toast.LENGTH_SHORT).show();

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
                            img_head.setImageBitmap(bm_icon);
                            DisplayMetrics dm=new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(dm);
                            ViewGroup.LayoutParams params=img_head.getLayoutParams();
                            params.height=dm.widthPixels*2/9;
                            params.width=dm.widthPixels*2/9;
                            img_head.setLayoutParams(params);
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
