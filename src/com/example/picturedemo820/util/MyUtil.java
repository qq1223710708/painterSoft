package com.example.picturedemo820.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/2/13.
 */
public class MyUtil {

    public static String getSaveDirPath(Activity activity) {
        File sdcarddir = android.os.Environment.getExternalStorageDirectory();
        String dirString = sdcarddir.getPath() + "/PictureDemo/";
        File filePath = new File(dirString);
        if (!filePath.exists()) {
            // 如果无法创建
            if (!filePath.mkdirs()) {
                Toast.makeText(activity, "请检查内存卡", Toast.LENGTH_SHORT)
                        .show();
            }
            filePath.mkdirs();
        }
        return dirString;
    }

    /**
     *得到共享时刻的存储路径
     */
    public static String getBitmapDirPath(Context context) {
        File sdcarddir = android.os.Environment.getExternalStorageDirectory();
        String dirString = sdcarddir.getPath() + "/PictureDemo/Bitmap/";
        File filePath = new File(dirString);
        if (!filePath.exists()) {
            //如果无法创建
            if (!filePath.mkdirs()) {
                Toast.makeText(context, "请检查内存卡", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        return dirString;
    }


    /**
     * 发送广播，更新sd卡中的数据库
     */
    public static void sendUpdateBroadCast(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent mediaScanIntent = new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//            Uri contentUri = Uri.fromFile(out); //out is your output file
            Uri contentUri = Uri.parse("file://"
                    + Environment.getExternalStorageDirectory());
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);
        } else {
            context.sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://"
                            + Environment.getExternalStorageDirectory())));
        }
//        IntentFilter intentFilter = new IntentFilter(
//                Intent.ACTION_MEDIA_SCANNER_STARTED);
//        intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
//        intentFilter.addDataScheme("file");
//        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
//                Uri.parse("file://"
//                        + Environment.getExternalStorageDirectory()
//                        .getAbsolutePath())));
    }

    public static boolean isFileNameOk(File file) {
        return Pattern.compile("[\\w%+,/=_-]+").matcher(file.getPath())
                .matches();
    }
}
