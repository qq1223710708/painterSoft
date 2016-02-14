package com.example.picturedemo820.util;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;
import com.example.picturedemo820.Gloabals.Gloabals;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/12.
 */
public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    // 判断是否有网络可用
    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager con = (ConnectivityManager) activity.getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting();
        boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .isConnectedOrConnecting();
        return wifi && internet;
    }

    public static boolean serverCanConnect(){
        return false;
//        不可用，ping 超时不会产生异常
//        String str = "ping -c 1 -i 0.2 -w 1 " + Gloabals.ip;
//        try {
//            Runtime.getRuntime().exec(str);
//            return true;
//        } catch (IOException e) {
//            Log.e(TAG, "Ping服务器异常:", e);
//            return false;
//        }
    }


    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        try {
            Socket socket = new Socket();
            socket.setSoTimeout(800);
            socket.connect(new InetSocketAddress("122.2.2.2", 80));
            System.out.println("socket connect no exception");
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("cost time: " + (end - start) + " ms");
    }


}
