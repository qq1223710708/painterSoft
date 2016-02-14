package com.example.picturedemo820.Shapes;

import com.example.picturedemo820.Interface.Shapable;
import com.example.picturedemo820.Painttools.PaintConstants.DEFAULT;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Rectangle extends ShapeAbstract {
	
	private Paint mRPaint = null;
	private int color;
	private int lefttopx;
	private int lefttopy;
	private int leftbottomx;
	private int leftbottomy;
	private int righttopx;
	private int righttopy;
	private int rightbottomx;
	private int rightbottoy;

	public Rectangle (Shapable paintTool) {
		super(paintTool);
		mRPaint = new Paint();
		mRPaint.setStrokeWidth(2);
		mRPaint.setAntiAlias(true);
		mRPaint.setStyle(Paint.Style.STROKE);
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		super.draw(canvas, paint);
		canvas.drawRect(x1, y1, x2, y2, paint);
		
		lefttopx = (int)x1;
		lefttopy = DEFAULT.viewheight-(int)y1;
		
		leftbottomx = (int)x1;
		leftbottomy = DEFAULT.viewheight-(int)y2;
		
		righttopx = (int)x2;
		righttopy = DEFAULT.viewheight-(int)y1;
		
		rightbottomx = (int)x2;
		rightbottoy = DEFAULT.viewheight-(int)y2;
		
		color = paint.getColor();
		mRPaint.setColor(color);
		
//		if (y2 > y1 && x2 > x1) {
//			canvas.drawText("("+lefttopx+", "+lefttopy+")", x1, y1-10, mRPaint);
//			canvas.drawText("("+leftbottomx+", "+leftbottomy+")", x1, y2+20, mRPaint);
//			canvas.drawText("("+righttopx+", "+righttopy+")", x2-50, y1-10, mRPaint);
//			canvas.drawText("("+rightbottomx+", "+rightbottoy+")", x2-50, y2+20, mRPaint);
//		}
//
//		if(y2 < y1 && x2 > x1){
//			canvas.drawText("("+lefttopx+", "+lefttopy+")", x1, y1+20, mRPaint);
//			canvas.drawText("("+leftbottomx+", "+leftbottomy+")", x1, y2-10, mRPaint);
//			canvas.drawText("("+righttopx+", "+righttopy+")", x2-50, y1+20, mRPaint);
//			canvas.drawText("("+rightbottomx+", "+rightbottoy+")", x2-50, y2-10, mRPaint);
//		}
//
//
//		if(y2 < y1 && x2 < x1){
//			canvas.drawText("("+lefttopx+", "+lefttopy+")", x1-50, y1+20, mRPaint);
//			canvas.drawText("("+leftbottomx+", "+leftbottomy+")", x1-50, y2-10, mRPaint);
//			canvas.drawText("("+righttopx+", "+righttopy+")", x2, y1+20, mRPaint);
//			canvas.drawText("("+rightbottomx+", "+rightbottoy+")", x2, y2-10, mRPaint);
//		}
//
//		if(y2 > y1 && x2 < x1){
//			canvas.drawText("("+lefttopx+", "+lefttopy+")", x1-50, y1-10, mRPaint);
//			canvas.drawText("("+leftbottomx+", "+leftbottomy+")", x1-50, y2+20, mRPaint);
//			canvas.drawText("("+righttopx+", "+righttopy+")", x2, y1-10, mRPaint);
//			canvas.drawText("("+rightbottomx+", "+rightbottoy+")", x2, y2+20, mRPaint);
//		}
		/*
		
		canvas.drawText("("+lefttopx+", "+lefttopy+")", x1, y1-10, mRPaint);
		canvas.drawText("("+leftbottomx+", "+leftbottomy+")", x1, y2+20, mRPaint);
		canvas.drawText("("+righttopx+", "+righttopy+")", x2-50, y1-10, mRPaint);
		canvas.drawText("("+rightbottomx+", "+rightbottoy+")", x2-50, y2+20, mRPaint);*/
	}

	@Override
	public String toString() {
		return "rectangle";
	}
}
