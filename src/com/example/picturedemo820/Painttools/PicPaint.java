package com.example.picturedemo820.Painttools;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;

import com.example.picturedemo820.Interface.ToolInterface;
import com.example.picturedemo820.PaintView.PaintView;

public class PicPaint implements ToolInterface{

	private Bitmap mBitmap;
	private Paint mBitmapPaint = new Paint();
	private Paint mDasedPaint = new Paint();
	private int left;
	private int top;
	private int mCurrentX = 0;
	private int mCurrentY = 0;
	private int startx = 0;
	private int starty = 0;
	private boolean reflection;
	private boolean clickD = false;

	public PicPaint(Bitmap bitmap,int alpha,boolean b) {

		mBitmap = bitmap;

		//LinearGradient shader = new LinearGradient(0,  bitmap.getHeight(), 0, bitmap.getHeight()+ 4, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
		
		/*mDasedPaint.setStrokeWidth(1);
		mDasedPaint.setDither(true);
		mDasedPaint.setAntiAlias(true);
		mDasedPaint.setStyle(Paint.Style.STROKE);
		PathEffect effects = new DashPathEffect(new float[] { 5, 5, 5, 5 },1);
		mDasedPaint.setPathEffect(effects);
		reflection = b;
		if(reflection == true){
			Shader mShader = new LinearGradient(0, mBitmap.getHeight(), 0, mBitmap.getHeight(), 0x70ffffff, 0x00ffffff, TileMode.MIRROR);  
			mBitmapPaint.setShader(mShader);
			//设置叠加模式  
			mBitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));  
            //绘制遮罩效果  
            //mCanvas.drawRect(0, height, width, mReflectedBitmap.getHeight(), mPaint);
		}*/

		mBitmapPaint.setStrokeWidth(1);
		mBitmapPaint.setDither(true);
		mBitmapPaint.setAlpha(alpha);
		mBitmapPaint.setAntiAlias(true);
		mBitmapPaint.setStyle(Paint.Style.STROKE);
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(mBitmap, mCurrentX-(mBitmap.getWidth()/2), mCurrentY-(mBitmap.getHeight()/2), mBitmapPaint);
		
		/*if(reflection==true){
			//Bitmap mReflectedBitmap = Bitmap.createBitmap(mBitmap.getWidth(), (mBitmap.getHeight())*2, Config.ARGB_8888);
			
			canvas.drawBitmap(mBitmap, mCurrentX-(mBitmap.getWidth()/2), mCurrentY+(mBitmap.getHeight()/2), mBitmapPaint);
		}else if(clickD){
			//canvas.drawRect(mCurrentX-(mBitmap.getWidth()/2)-1, mCurrentY-(mBitmap.getHeight()/2)-1, mCurrentX+(mBitmap.getWidth()/2)+1, mCurrentY+(mBitmap.getHeight()/2)+1, mDasedPaint);
		}else{
			canvas.drawBitmap(mBitmap, mCurrentX-(mBitmap.getWidth()/2), mCurrentY-(mBitmap.getHeight()/2), mBitmapPaint);
			//canvas.drawRect(mCurrentX-(mBitmap.getWidth()/2)-1, mCurrentY-(mBitmap.getHeight()/2)-1, mCurrentX+(mBitmap.getWidth()/2)+1, mCurrentY+(mBitmap.getHeight()/2)+1, mDasedPaint);
			//mPaintView.getPicPaintCoordinate((int)mCurrentX-(mBitmap.getWidth()/2),(int)mCurrentY-(mBitmap.getHeight()/2),(int)mCurrentX+(mBitmap.getWidth()/2),(int)mCurrentY+(mBitmap.getHeight()/2));
		}*/
	}

	@Override
	public void touchDown(int x, int y) {
		startx = x;
		starty = y;
		mCurrentX = x;
		mCurrentY = y;
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
		//hasDraw();
	}

	@Override
	public boolean hasDraw() {
		return true;
	}

}
