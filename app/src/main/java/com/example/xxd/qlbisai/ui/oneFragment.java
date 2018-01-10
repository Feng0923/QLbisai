package com.example.xxd.qlbisai.ui;



import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.xxd.qlbisai.R;
import com.example.xxd.qlbisai.myBean.JNRBean;
import com.example.xxd.qlbisai.myBean.MyUserBean;
import com.example.xxd.qlbisai.ui.jnr.JNRActivity;
import com.example.xxd.qlbisai.ui.jnr.JNRAdapter;
import com.example.xxd.qlbisai.ui.jnr.JNRChangeActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;

import static android.app.Activity.RESULT_OK;

/**
 * Created by xxd on 2017/7/22.
 */

public class oneFragment extends Fragment implements AdapterView.OnItemClickListener{

    private View view;
    private ListView listView;
    private List<JNRBean> lists;
    private JNRAdapter jnrAdapter;
    private ProgressDialog loadingDialog;
    private MyUserBean user;
    public static boolean update=true;
    public static List<JNRBean> hava=new ArrayList<JNRBean>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.firstfragment,container,false);
        initView();
        return view;
    }

    private void initView() {
        user= BmobUser.getCurrentUser(getActivity(),MyUserBean.class);
        listView=(ListView)view.findViewById(R.id.lv_jinian);
        lists=new ArrayList<JNRBean>();
        listView.setOnItemClickListener(this);
        loadingDialog = new ProgressDialog(getActivity());
        loadingDialog.setMessage("正在加载, 请稍候...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setCancelable(true);
        if(update) {
            loadData();

        }else {
            jnrAdapter=new JNRAdapter(getActivity(),hava,2);
            listView.setAdapter(jnrAdapter);
        }
    }

    private void loadData() {
        loadingDialog.show();
        BmobQuery<JNRBean> query = new BmobQuery<JNRBean>();
        query.addWhereEqualTo("name", user.getUsername());
        query.setLimit(50);
        query.findObjects(getActivity(), new FindListener<JNRBean>() {
            @Override
            public void onSuccess(List<JNRBean> object) {
                lists.clear();
                hava.clear();
                for(JNRBean person:object){
                    lists.add(person);
                    hava.add(person);
                }
                jnrAdapter=new JNRAdapter(getActivity(),lists,2);
                listView.setAdapter(jnrAdapter);
                Log.d("11111", "onSuccess: "+lists.size());
                loadingDialog.dismiss();
                update=false;
            }
            @Override
            public void onError(int code, String msg) {
                loadingDialog.dismiss();
            }
        });

    }




    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Intent intent=new Intent(getActivity(),JNRChangeActivity.class);
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
                    bb.download(getActivity(), saveFile, new DownloadFileListener() {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    loadData();
                }
                break;

        }
    }
}
