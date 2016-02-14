package com.example.picturedemo820;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.picturedemo820.R;
import com.example.picturedemo820.Gloabals.Gloabals;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FindTask extends Activity implements OnClickListener {

	private Button FindtaskBegin, Findtaskfind, FindtaskRelog;
	private EditText FindtaskFindEditText;
	private TextView FindtaskTextView;
	private ImageView findtaskImageView;
	private LinearLayout FINDTASKLINEARLAYOUT;
	private MainActivity mainActivity;
	
	MyHandler myHandler;
	private Bitmap mfindtaskbitmap;
	private String findtaskint;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.findtask);
		init();
	}

	public void init() {
		this.FindtaskBegin = (Button) findViewById(R.id.FindtaskBegin);
		this.FindtaskBegin.setOnClickListener(this);

		this.FindtaskRelog = (Button) findViewById(R.id.FindtaskRelog);
		this.FindtaskRelog.setOnClickListener(this);

		this.Findtaskfind = (Button) findViewById(R.id.Findtaskfind);
		this.Findtaskfind.setOnClickListener(this);

		this.FindtaskFindEditText = (EditText) findViewById(R.id.FindtaskFindEditText);
		this.FindtaskFindEditText
				.setOnTouchListener(new View.OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
							FindtaskFindEditText.clearFocus();
							InputMethodManager imm = (InputMethodManager) getSystemService(FindTask.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									FindtaskFindEditText.getWindowToken(), 0);
						}
						return false;
					}
				});

		this.FindtaskTextView = (TextView) findViewById(R.id.FindtaskTextView);
		this.FindtaskTextView.setTextColor(Color.BLACK);
		this.FindtaskTextView.setTextSize(20);
		if(Gloabals.taskInt!=null){
			this.FindtaskTextView.setText(Gloabals.taskInt);
		}
		
		Resources res = getResources();
		mfindtaskbitmap = BitmapFactory.decodeResource(res, R.drawable.findtaskpic);
		if(Gloabals.findtaskbitmap == null){
			Gloabals.findtaskbitmap = mfindtaskbitmap;
		}
		

		this.findtaskImageView = (ImageView) findViewById(R.id.findtaskImageView);
		this.findtaskImageView.setBackgroundColor(Color.WHITE);
		this.findtaskImageView.setImageBitmap(Gloabals.findtaskbitmap);

		this.FINDTASKLINEARLAYOUT = (LinearLayout) findViewById(R.id.FINDTASKLINEARLAYOUT);
		this.FINDTASKLINEARLAYOUT.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.FindtaskBegin:
			Intent intent = new Intent();
			intent.setClassName(FindTask.this,
					"com.example.picturedemo820.MainActivity");
			FindTask.this.startActivity(intent);
			break;
		case R.id.FindtaskRelog:
			FindtaskFindEditText.setText("");
			break;
		case R.id.Findtaskfind:
			if (isNetworkAvailable(FindTask.this) == true) {
				new Thread() {
					@Override
					public void run() {
						String course_list;
						String taskNumber = FindtaskFindEditText.getText().toString();
						List<NameValuePair> formParams = new ArrayList<NameValuePair>();
						formParams.add(new BasicNameValuePair("taskNumber", taskNumber));

						UrlEncodedFormEntity entity = null;
						try {
							entity = new UrlEncodedFormEntity(formParams, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}

						HttpClient httpClient = new DefaultHttpClient();
						HttpContext localContext = new BasicHttpContext();
						course_list = "";
						HttpPost post = null;
						try {
							post = new HttpPost(Gloabals.TASKNUMBER_SERVLET_URL);
							post.setEntity(entity);
						} catch (Exception e) {
							Looper.prepare();
							Toast.makeText(FindTask.this, "无法连接到服务器", Toast.LENGTH_SHORT)
									.show();
							Looper.loop();
						} 

						HttpResponse response = null;
						try {
							response = httpClient.execute(post, localContext);
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						HttpEntity responseEntity = response.getEntity();
						try {
							course_list = EntityUtils.toString(responseEntity);
						} catch (ParseException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}

						JSONArray jsonArray = null;
						
						JSONObject jsonobject=null;
						
						try {
							jsonArray = new JSONArray(course_list);
							for(int i=0;i<jsonArray.length();i++){
								jsonobject = jsonArray.getJSONObject(i);
								Gloabals.taskInt = jsonobject.getString("taskInt");
								Gloabals.taskPicUrl = jsonobject.getString("taskPicUrl");
							}
						} catch (JSONException e1) {
							e1.printStackTrace();
						}
						
						System.out.println("taskInt=="+Gloabals.taskInt);
						System.out.println("taskPicUrl=="+Gloabals.taskPicUrl);
						
						Gloabals.findtaskbitmap = getImage(Gloabals.taskPicUrl);
						//mfindtaskbitmap = Gloabals.findtaskbitmap;
						
						Looper mainl = Looper.getMainLooper();
						
						System.out.println("mainl=="+mainl);
						
						myHandler = new MyHandler(mainl);
						
						Message msg = new Message();
						Bundle b = new Bundle();// ������� 
						b.putString("taskInt", Gloabals.taskInt);
						msg.setData(b); 
						myHandler.sendMessage(msg);
					}
				}.start();
			} else {
				Toast.makeText(FindTask.this, "当前没有可用网络！", Toast.LENGTH_LONG)
						.show();
			}
			break;
		case R.id.FINDTASKLINEARLAYOUT:
			InputMethodManager imm = (InputMethodManager) getSystemService(FindTask.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			break;
		}
	}

	public void setFindtaskTextView(String s) {
		FindtaskTextView.setText("\u3000"+s);
	}
	
	
	private class MyHandler extends Handler{
		public MyHandler() { 
			super();
        } 
 
        public MyHandler(Looper looper) { 
            super(looper); 
        } 
        @Override 
        public void handleMessage(Message msg) { 
        	super.handleMessage(msg);
			// 此处可以更新UI
            Bundle b = msg.getData(); 
            Gloabals.taskInt = b.getString("taskInt"); 
            System.out.println("2taskInt=="+Gloabals.taskInt);
            
            FindTask.this.setFindtaskTextView(Gloabals.taskInt);
            if(Gloabals.findtaskbitmap!=null){
            	 FindTask.this.findtaskImageView.setImageBitmap(Gloabals.findtaskbitmap);
            }
           
            
        } 
	}
	
	public Bitmap getImage(String url){
		try {
			URL Url = new URL(url);
			String responseCode = Url.openConnection().getHeaderField(0);
			if (responseCode.indexOf("200") < 0){
				return BitmapFactory.decodeStream(Url.openStream());
			}
		} catch (IOException e) {
			try {
				throw new Exception(e.getMessage());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	// 判断是否有网络可用
	public boolean isNetworkAvailable(Activity activity) {

		ConnectivityManager con = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.isConnectedOrConnecting();
		boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.isConnectedOrConnecting();
		if (wifi | internet) {
			return true;
		} else {
			return false;
		}

	}
}
