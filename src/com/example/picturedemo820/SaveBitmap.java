package com.example.picturedemo820;

import com.example.picturedemo820.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class SaveBitmap extends Dialog implements DialogInterface.OnClickListener{

	private EditText mEditText;
	public SaveBitmap(Context context) {
		super(context);
		System.out.println(101);
		
		//mBitmap = bitmap;
		AlertDialog.Builder mBulider = new Builder(context);
		 LayoutInflater layoutInflater =LayoutInflater.from(context); 
		 View DialogView = layoutInflater.inflate(R.layout.savebitmap,null);
		 mBulider.setTitle("是否保存图片？？？");
		 mBulider.setView(DialogView);
		 mBulider.setPositiveButton("确认", this);
		 mBulider.setNegativeButton("取消", this);
	}
	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		 case DialogInterface.BUTTON_POSITIVE:            //点击“确定”进入登录界面
			 //BitMapUtils.saveToSdCard(path, mBitmap);
			 break;
		case DialogInterface.BUTTON_NEUTRAL:            //点击“退出”
			this.dismiss();
			break;
		}
	}

}
