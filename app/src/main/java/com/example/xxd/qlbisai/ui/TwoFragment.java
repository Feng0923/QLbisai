package com.example.xxd.qlbisai.ui;


import java.util.Timer;
import java.util.TimerTask;

        import android.app.Activity;
        import android.app.AlertDialog;
        import android.app.AlertDialog.Builder;
        import android.content.DialogInterface;
        import android.content.DialogInterface.OnClickListener;
        import android.media.MediaPlayer;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TableLayout;
        import android.widget.TextView;
        import android.widget.Toast;

import com.example.xxd.qlbisai.R;

public class TwoFragment extends Fragment {
    private Button startButton;
    EditText timeEt;
    ImageView settingBtn, musicBtn;
    AlertDialog timeAd;
    LinearLayout tl;
    private TextView time;
    private int timelength = 25 * 60;
    private String timeText;
    private boolean startflag = false;
    private boolean stopflag = false;
    public boolean isfirst=true;
    private ImageView set;
    private boolean isstart=false;
    public  boolean ishave=true;
    private int index_item = 0;
    private int index_music = 0;
    private String[] musics = { "铃声一", "铃声二", "铃声三", "铃声四", "铃声五" };
    private int[] music_id = { R.raw.music1, R.raw.music2, R.raw.music3,
            R.raw.music4, R.raw.music5 };
    MediaPlayer mPlayer;
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.secondfragment,container,false);

        startButton = (Button) view.findViewById(R.id.start);
        settingBtn = (ImageView) view.findViewById(R.id.setting);
        musicBtn = (ImageView) view.findViewById(R.id.music);
        time = (TextView) view.findViewById(R.id.time);
        set = (ImageView) view.findViewById(R.id.setting);
        mPlayer = MediaPlayer.create(getContext(), music_id[index_music]);
        tl = (LinearLayout) inflater.inflate(R.layout.name, null);
        timeEt = (EditText) tl.findViewById(R.id.time_et);
        mPlayer.setLooping(true);
        startButton.setTag("starting");
        if(isfirst) {
            time.setText("25:00");
            startButton.setText("开始番茄");
        }else {
            time.setText(timeText);
        }

        if(startButton.getTag().equals("stoping")){
            startButton.setText("结束番茄");
        }else {
            if(isstart){
                startButton.setText("停止番茄");
            }else
                startButton.setText("开始番茄");
        }

        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (startButton.getTag().equals("stoping")) {
                    mPlayer.pause();
                    startButton.setTag("starting");
                    timelength = 25 * 60;
                    startButton.setText("开始番茄");

                    return;
                }
                if (startflag) {
                    isstart=false;
                    startButton.setText("开始番茄");
                    startButton.setTag("starting");
                    startflag = false;
                } else {
                    isstart=true;
                    startButton.setText("停止番茄");

                    startButton.setTag("going");
                    startflag = true;
                }

            }
        });



        musicBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showMusicDig();

            }
        });
        settingBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                timeAd.show();

            }
        });
        final Handler myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x123) {
                    if (timelength > 0) {
                        timelength--;
                        isfirst=false;
                    } else {
                        mPlayer.start();
                        startButton.setText("结束番茄");
                        startButton.setTag("stoping");
                        startflag = false;
                    }

                    String mm = timelength / 60 + "";
                    String ss = timelength % 60 + "";
                    if (Integer.parseInt(ss) < 10) {
                        ss = "0" + ss;
                    }
                    if (Integer.parseInt(mm) < 10) {
                        mm = "0" + mm;
                    }
                    timeText = mm + ":" + ss;
                    time.setText(timeText);

                }
            }
        };


        if(ishave) {
            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {
                    if (startflag) {
                        myHandler.sendEmptyMessage(0x123);
                    }

                }
            }, 0, 1000);
            ishave=false;
        }



        timeAd = new AlertDialog.Builder(getContext())
                .setTitle("设置番茄分钟数")
                .setView(tl)
                .setPositiveButton("确定",
                        new android.content.DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                String timeStr = timeEt.getText().toString()
                                        .trim();
                                try {
                                    int timeInt = Integer.parseInt(timeStr);
                                    if (timeInt <= 0 || timeInt > 1000000) {
                                        Toast.makeText(getContext(),
                                                "请输入正确的分钟数", Toast.LENGTH_SHORT)
                                                .show();
                                    }else{
                                        timelength=timeInt;
                                        String mm = timelength / 60 + "";
                                        String ss = timelength % 60 + "";
                                        if (Integer.parseInt(ss) < 10) {
                                            ss = "0" + ss;
                                        }
                                        if (Integer.parseInt(mm) < 10) {
                                            mm = "0" + mm;
                                        }
                                        timeText = mm + ":" + ss;
                                        time.setText(timeText);
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(getContext(),
                                            "请输入正确的分钟数", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }

                        })
                .setNeutralButton("取消",
                        new android.content.DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                timeAd.dismiss();

                            }
                        }).create();


        return view;
    }




    private void showMusicDig() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setTitle(
                "选择铃声").setSingleChoiceItems(musics, index_item,
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        index_item = which;
                        index_music = which;

                    }
                });
        setPositiveButton(builder);
        setNegativeButton(builder).create().show();

    }

    private AlertDialog.Builder setPositiveButton(AlertDialog.Builder builder) {
        return builder.setPositiveButton("确定",
                new android.content.DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPlayer = MediaPlayer.create(getContext(),
                                music_id[index_music]);
                        mPlayer.setLooping(true);
                    }
                });
    }

    private AlertDialog.Builder setNegativeButton(AlertDialog.Builder builder) {
        return builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
    }

}
