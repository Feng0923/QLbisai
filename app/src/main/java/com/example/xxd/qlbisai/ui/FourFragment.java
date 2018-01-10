package com.example.xxd.qlbisai.ui;



import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.a.a.I;
import com.example.xxd.qlbisai.MainActivity;
import com.example.xxd.qlbisai.R;
import com.example.xxd.qlbisai.login.ForgetActivity;
import com.example.xxd.qlbisai.login.LoginActivity;
import com.example.xxd.qlbisai.myBean.MyUserBean;
import com.example.xxd.qlbisai.ui.LogCat.LogcatActivity;
import com.example.xxd.qlbisai.ui.firsthistory.HistroyActivity;
import com.example.xxd.qlbisai.ui.history.Charts;
import com.example.xxd.qlbisai.ui.jnr.JNRActivity;

import java.util.Date;

import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.c.V;

import static android.app.Activity.RESULT_OK;

/**
 * Created by xxd on 2017/7/22.
 */

public class FourFragment extends Fragment implements View.OnClickListener{
    private View view;
    private Button my_logcat,my_date,my_plan,my_history,my_remind,my_change_message,my_finish;
    private ImageView my_message;
    private TextView tv_phone_number;
    MyUserBean user;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.forthfragment,container,false);
        user = BmobUser.getCurrentUser(getActivity(),MyUserBean.class);
        initView();
        return view;
    }

    private void initView() {
        my_logcat=(Button)view.findViewById(R.id.logcat_btn);
        my_logcat.setOnClickListener(this);
        my_date=(Button)view.findViewById(R.id.date_btn);
        my_date.setOnClickListener(this);
        my_history=(Button)view.findViewById(R.id.history_btn);
        my_history.setOnClickListener(this);
        my_plan=(Button)view.findViewById(R.id.plan_btn);
        my_plan.setOnClickListener(this);
        my_change_message=(Button)view.findViewById(R.id.message_btn);
        my_change_message.setOnClickListener(this);
        my_finish=(Button)view.findViewById(R.id.finish_btn);
        my_finish.setOnClickListener(this);
        my_message=(ImageView)view.findViewById(R.id.message_img);
        my_message.setOnClickListener(this);
        tv_phone_number=(TextView)view.findViewById(R.id.phone_number_tv);
        tv_phone_number.setText(user.getUsername());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logcat_btn:
                Intent intent3=new Intent(getActivity(), LogcatActivity.class);
                startActivity(intent3);

                break;
            case R.id.date_btn:
                Intent intent4=new Intent(getActivity(), JNRActivity.class);
                startActivity(intent4);

                break;
            case R.id.plan_btn:
                Date date2=new Date();
                Intent intent888=new Intent(getContext(), HistroyActivity.class);
                intent888.putExtra("date",date2.getYear()+""+date2.getMonth()+""+date2.getDate());
                startActivity(intent888);

                break;
            case R.id.history_btn:
                Intent intent5=new Intent(getActivity(), Charts.class);
                startActivity(intent5);

                break;
            case R.id.message_btn:
                Intent intent1 = new Intent(getActivity(), ChangeActivity.class);
                intent1.putExtra("name",user.getName());
                intent1.putExtra("age",user.getAge());
                intent1.putExtra("city",user.getCity());
                String s= String.valueOf(user.getSex());
                intent1.putExtra("sex",s);
                startActivityForResult(intent1,1);
                break;
            case R.id.finish_btn:
                AlertDialog.Builder bdr=new AlertDialog.Builder(getActivity());
                bdr.setTitle("确认切换账号？");
                bdr.setNegativeButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyUserBean.logOut(getActivity());
                        oneFragment.hava.clear();
                        oneFragment.update=true;
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.putExtra("name",user.getUsername());
                        startActivity(intent);
                        getActivity().finish();
                        ;
                    }
                });
                bdr.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }) ;
                bdr.show();


                break;
            case R.id.message_img:
                Intent intent2 = new Intent(getActivity(), MessageActivity.class);
                intent2.putExtra("name",user.getName());
                intent2.putExtra("age",user.getAge());
                intent2.putExtra("city",user.getCity());
                Integer i=user.getSex();
                String sex;
                if(i==1){
                    sex="男";
                }else
                    sex="女";
                intent2.putExtra("sex",sex);

                Date date=user.getDate();
                String d=date.getMonth()+1+"月"+date.getDate()+"日";
                Log.d("111222", "onClick: ");
                intent2.putExtra("birthday",d);
                startActivity(intent2);


                break;

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    user = BmobUser.getCurrentUser(getActivity(),MyUserBean.class);
                }
        }
    }
}
