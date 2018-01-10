package com.example.xxd.qlbisai.utils;

/**
 * Created by xxd on 2017/8/4.
 */



import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;

import com.example.xxd.qlbisai.login.CompleteActivity;
import com.example.xxd.qlbisai.ui.jnr.JNRAddActivity;

public class JNRImageUtils {

    public final static int ACTIVITY_RESULT_CAMERA = 0001;//选择 拍照 的返回码
    public final static int ACTIVITY_RESULT_ALBUM = 0002;//选择 相册 的返回码

    public  Uri photoUri;// 图片路径的URI
    private Uri tempUri;

    public  File picFile;// 图片文件

    private Context context;



    // 构造方法
    public JNRImageUtils(Context context) {
        super();
        this.context = context;
    }

    // 选择拍照的方式
    public void byCamera() {
        try {
            // 创建文件夹
            File uploadFileDir = new File(
                    Environment.getExternalStorageDirectory(), "/icon");

            if (!uploadFileDir.exists()) {
                uploadFileDir.mkdirs();
            }
            // 创建图片，以当前系统时间命名
            picFile = new File(uploadFileDir,
                    SystemClock.currentThreadTimeMillis() + ".png");
            if (!picFile.exists()) {
                picFile.createNewFile();
            }
            // 获取到图片路径
            tempUri = Uri.fromFile(picFile);

            // 启动Camera的Intent，传入图片路径作为存储路径
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
            //启动Intent
            ((JNRAddActivity) context).startActivityForResult(cameraIntent,
                    ACTIVITY_RESULT_CAMERA);

            System.out.println("-->tempUri : " + tempUri.toString()
                    + "-->path:" + tempUri.getPath());
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 选择相册的方式
    public void byAlbum() {
        try {
            // 创建文件夹
            File pictureFileDir = new File(
                    Environment.getExternalStorageDirectory(), "/icon");
            if (!pictureFileDir.exists()) {
                pictureFileDir.mkdirs();
            }
            // 创建图片，以当前系统时间命名
            picFile = new File(pictureFileDir,
                    SystemClock.currentThreadTimeMillis() + ".png");
            if (!picFile.exists()) {
                picFile.createNewFile();
            }
            // 获取到图片路径
            tempUri = Uri.fromFile(picFile);

//             获得剪辑图片的Intent
            final Intent intent = cutImageByAlbumIntent();
            ((JNRAddActivity) context).startActivityForResult(intent,
                    ACTIVITY_RESULT_ALBUM);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 调用图片剪辑程序的Intent
    private Intent cutImageByAlbumIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        //设定宽高比
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
        //设定剪裁图片宽高
//        intent.putExtra("outputX", 480);
//        intent.putExtra("outputY", 480);

        intent.putExtra("noFaceDetection", true);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        return intent;
    }

    //通过相机拍照后进行剪辑
    public void cutImageByCamera() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(tempUri, "image/*");
        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", 1000);
//        intent.putExtra("outputY", 1000);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        ((JNRAddActivity) context).startActivityForResult(intent,
                ACTIVITY_RESULT_ALBUM);
    }

    // 对图片进行编码成Bitmap
    public Bitmap decodeBitmap() {
        Bitmap bitmap = null;
        try {
            if (tempUri != null) {
                Log.d("00000", "decodeBitmap: "+tempUri);
                photoUri = tempUri;
                bitmap = BitmapFactory.decodeStream(context
                        .getContentResolver().openInputStream(photoUri));
                Intent intent=new Intent("com.xxd.path");
                intent.putExtra("path",picFile.getPath());
                context.sendBroadcast(intent);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
}