package com.example.picturedemo820.Painttools;

/*
 * 实现绘画选区的功能。
 * 
 * 
 */
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;

import com.example.picturedemo820.Interface.ToolInterface;
import com.example.picturedemo820.PaintView.PaintView;

public class ChangePictureConstituency implements ToolInterface {

	private Paint mDasedPaint = new Paint();  //空心画笔
	
	private Paint mFullDasedPaint = new Paint();    //实心画笔
	
	
	private int mCurrentX = 0;
	private int mCurrentY = 0;
	private int startx = 0;
	private int starty = 0;
	private Path mPath = new Path();
	private boolean onHasDrow = false;

	private int mArrayColor[] = null;

	private int[][] pc = new int[4][2];
	private int[][] canvascolor;

	public int leftupx;
	public int leftupy; 
	public int rightdownx; 
	public int rightdowny;
	
	public ChangePictureConstituency(int leftupx, int leftupy, int rightdownx, int rightdowny){
		mDasedPaint.setStrokeWidth(1);
		this.leftupx = leftupx;
		this.leftupy = leftupy;
		this.rightdownx = rightdownx;
		this.rightdowny = rightdowny;


		mFullDasedPaint.setDither(true);//抗抖动
		mFullDasedPaint.setAntiAlias(true);//无锯齿
		mFullDasedPaint.setColor(Color.BLUE);//蓝色
		mFullDasedPaint.setAlpha(120);//画出的是120的透明度
		mFullDasedPaint.setStyle(Paint.Style.FILL);//实现实心效果
		
	}

	@Override
	public void draw(Canvas canvas) {
		if (null != canvas) {
			//PathEffect effects = new DashPathEffect(new float[] { 5, 5, 5, 5 },1);
			//mDasedPaint.setPathEffect(effects);
			canvas.drawRect(leftupx, leftupy, mCurrentX, mCurrentY, mFullDasedPaint);
			canvas.drawRect(leftupx-1, leftupy-1, mCurrentX+1, mCurrentY+1, mDasedPaint);
			
		}
	}

	@Override
	public void touchDown(int x, int y) {
		startx = x;
		starty = y;
	}

	@Override
	public void touchMove(int x, int y) {
		mCurrentX = x;
		mCurrentY = y;
	}

	@Override
	public void touchUp(int x, int y) {
		mCurrentX = x;
		mCurrentY = y;

		hasDraw();
	}

	@Override
	public boolean hasDraw() {
		//onHasDrow = true;
		return true;
	}

}
