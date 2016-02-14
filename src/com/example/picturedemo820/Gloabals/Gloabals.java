package com.example.picturedemo820.Gloabals;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Gloabals {
	// 不要使用 localhost 和 127.0.0.1
	// 因为这个域名、地址代表 android device 本身

	public static final String ip = "10.11.47.98";
	//public static final String UPPHTOT_NOTE_URL = "http://10.11.47.98:8080/KetangBiji/ReceivePhotoServlet";
	//public static final String TASKNUMBER_SERVLET_URL= "http://10.11.47.98:8080/KetangBiji/ReceiveTaskNumber";
	public static final String UPPHTOT_NOTE_URL = "http://10.12.31.144:8080/PictureDemo820/ReceivePhotoServlet";
	public static final String TASKNUMBER_SERVLET_URL= "http://10.12.31.144:8080/PictureDemo820/ReceiveTaskNumber";
	
	public static String taskInt = null;
	public static String taskPicUrl = null;
	public static Bitmap findtaskbitmap = null;
	
}