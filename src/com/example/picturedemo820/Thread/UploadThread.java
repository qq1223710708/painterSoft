package com.example.picturedemo820.Thread;

import java.io.File;

import com.example.picturedemo820.MainActivity;
import com.example.picturedemo820.Gloabals.Gloabals;


public class UploadThread implements Runnable{
	
	private MainActivity mainActivity;
	private File file;
	String username;
	
	public UploadThread(MainActivity activity,File file,String username) {
		this.mainActivity = activity;
		this.file = file;
		this.username = username;
	}
	
	
	
	@Override
	public void run() {
		
		int request = UploadUtil.uploadFile(file,Gloabals.UPPHTOT_NOTE_URL);
	}

}
