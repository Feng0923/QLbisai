package com.example.xxd.qlbisai;

import android.app.Application;

import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

/**
 * Created by xxd on 2017/8/2.
 */

public class BmobApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this,"fea98934167ca8a5a1f5100cd514534e");
        BmobConfig config =new BmobConfig.Builder(this)
                .setApplicationId("fea98934167ca8a5a1f5100cd514534e")
                .setConnectTimeout(10)
                .setUploadBlockSize(1024*1024)
                .setFileExpiration(2500)
                .build();
        Bmob.initialize(config);
        BmobSMS.initialize(this,"fea98934167ca8a5a1f5100cd514534e");
    }
}
