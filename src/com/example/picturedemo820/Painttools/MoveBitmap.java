package com.example.picturedemo820.Painttools;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.picturedemo820.Interface.ToolInterface;
import com.example.picturedemo820.PaintView.PaintView;
import com.example.picturedemo820.Painttools.PaintConstants.PEN_TYPE;

public class MoveBitmap implements ToolInterface{
	private Bitmap mBitmap;
	private Paint mBitmapPaint = new Paint();
	private int left;
	private int top;
	private int mCurrentX = 0;
	private int mCurrentY = 0;
	private int startx = 0;
	private int starty = 0;
	
	public MoveBitmap(Bitmap bitmap) {
		mBitmap = bitmap;
		mBitmapPaint.setStrokeWidth(1);
		mBitmapPaint.setDither(true);
		mBitmapPaint.setAntiAlias(true);
		mBitmapPaint.setStyle(Paint.Style.STROKE);
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(mBitmap, mCurrentX-(mBitmap.getWidth()/2), mCurrentY-(mBitmap.getHeight()/2), mBitmapPaint);
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
		
		return true;
	}

}
