package com.example.picturedemo820.Painttools;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;

import com.example.picturedemo820.Interface.ToolInterface;
import com.example.picturedemo820.PaintView.PaintView;

public class PicpaintConstituency implements ToolInterface {

	private Paint mDasedPaint = new Paint();
	private Paint mLinePaint = new Paint();
	private int mCurrentX = 0;
	private int mCurrentY = 0;
	private int startx = 0;
	private int starty = 0;
	private Path mPath = new Path();
	private boolean onHasDrow = false;
	private Bitmap mBitmap;

	private int mArrayColor[] = null;

	private int[][] pc = new int[4][2];
	private int[][] canvascolor;

	public PicpaintConstituency(int color) {
		mDasedPaint.setStrokeWidth(1);
		if (color == Color.WHITE) {
			mDasedPaint.setColor(Color.BLACK);
		} else {
			mDasedPaint.setColor(Color.WHITE);
		}
		mDasedPaint.setDither(true);
		mDasedPaint.setAntiAlias(true);
		mDasedPaint.setStyle(Paint.Style.STROKE);

		mLinePaint.setStrokeWidth(2);
		if (color == Color.WHITE) {
			mLinePaint.setColor(Color.BLACK);
		} else {
			mLinePaint.setColor(Color.WHITE);
		}
		mLinePaint.setDither(true);
		mLinePaint.setAntiAlias(true);
		mLinePaint.setStyle(Paint.Style.STROKE);
	}

	@Override
	public void draw(Canvas canvas) {
		if (null != canvas) {
			PathEffect effects = new DashPathEffect(new float[] { 20, 20, 20, 20 },1);
			mDasedPaint.setPathEffect(effects);
			canvas.drawRect(startx, starty, mCurrentX, mCurrentY, mDasedPaint);

			canvas.drawRect((mCurrentX+startx)/2-20, starty-20, (mCurrentX+startx)/2+20, starty, mLinePaint);//上
			canvas.drawRect(startx-20, (mCurrentY+starty)/2-20, startx, (mCurrentY+starty)/2+20, mLinePaint);//左
			canvas.drawRect(mCurrentX, (mCurrentY+starty)/2-20, mCurrentX+20, (mCurrentY+starty)/2+20, mLinePaint);//右
			canvas.drawRect((mCurrentX+startx)/2-20, mCurrentY, (mCurrentX+startx)/2+20, mCurrentY+20, mLinePaint);//下
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
