package com.example.picturedemo820.Thread;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

import com.example.picturedemo820.FindTask;
import com.example.picturedemo820.Gloabals.Gloabals;

import android.content.Intent;
import android.os.Looper;
import android.widget.Toast;

public class LoginThread implements Runnable {

	private FindTask fandtask;
	private String data;
	private ArrayList<String> courseList;
	private ArrayList<String> course_List;
	UrlEncodedFormEntity entity;
	String username;
	String pwd;

	public LoginThread(FindTask activity, UrlEncodedFormEntity entity) {
		this.fandtask = activity;
		this.entity = entity;
	}

	@Override
	public void run() {

		boolean b1 = isServerAvailable();
		if (b1 == true) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			data = "";
			try {

				HttpPost post = new HttpPost(Gloabals.TASKNUMBER_SERVLET_URL);
				post.setEntity(entity);

				HttpResponse response = httpClient.execute(post, localContext);

				HttpEntity responseEntity = response.getEntity();
				data = EntityUtils.toString(responseEntity);
				System.out.println("-->" + data + "<--");

				List<String> ketangList = KetangListParser.getKetangList(data);
				String[] ketangArray = new String[ketangList.size()];
				ketangList.toArray(ketangArray);
				fandtask.setFindtaskTextView(ketangArray[0]);
				

				/*if (ketangArray[0].equals("2")) {
					Looper.prepare();
					Toast.makeText(this.mainActivity, "用户名不存在",
							Toast.LENGTH_SHORT).show();
					Looper.loop();
				}

				if (ketangArray[0].equals("3")) {
					this.mainActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							
							Intent intent = new Intent();
							intent.setClassName(LoginThread.this.mainActivity,
									"com.buu.xh.test2.Add_courseNameActivity");
							intent.putExtra("username", username);
							LoginThread.this.mainActivity.startActivity(intent);
						}
					});
				}

				if (ketangArray[0].equals("4")) {
					Looper.prepare();
					Toast.makeText(this.mainActivity, "密码错误",
							Toast.LENGTH_SHORT).show();
					Looper.loop();
				}*/
			}
			catch (ClientProtocolException e) {
				Looper.prepare();
				Toast.makeText(this.fandtask, "无法连接到服务器",
						Toast.LENGTH_SHORT).show();
				Looper.loop();

			} catch (IOException e) {
				Looper.prepare();
				Toast.makeText(this.fandtask, "无法连接到服务器",
						Toast.LENGTH_SHORT).show();
				Looper.loop();

			}

		} else {
			Looper.prepare();
			Toast.makeText(this.fandtask, "无法连接到服务器", Toast.LENGTH_SHORT)
					.show();
			Looper.loop();
		}
	}

	//ping服务器，如果pin延迟小于20 则能连接到服务器，如果大于20，则显示无法连接到服务器。
	public boolean isServerAvailable() {
		boolean b = false;
		String pingip;
		try {
			String ipAddress = Gloabals.ip;
			Process p = Runtime.getRuntime().exec(
					"ping -c 1-w 100 " + ipAddress);
			int status = p.waitFor();
			System.out.println(status);
			if (status <= 20) {
				b = true;
			} else {
				pingip = "Fail: IP addr not reachable";
				b = false;
			}
		} catch (IOException e) {
			pingip = "Fail: IOException";
			b = false;
		} catch (InterruptedException e) {
			pingip = "Fail: InterruptedException";
			b = false;
		}
		return b;
	}
}
