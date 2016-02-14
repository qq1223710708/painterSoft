package com.example.picturedemo820.Shapes;

import com.example.picturedemo820.Interface.Shapable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Oval extends ShapeAbstract {

	RectF mRectF;
	private Paint mOPaint = null;
	private int color;
	
	public Oval(Shapable paintTool) {
		super(paintTool);
		mRectF = new RectF();
		mOPaint = new Paint();
		mOPaint.setColor(Color.BLACK);
		mOPaint.setStrokeWidth(2);
		mOPaint.setAntiAlias(true);
		mOPaint.setStyle(Paint.Style.STROKE);
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		super.draw(canvas, paint);
		mRectF.set(x1, y1, x2, y2);
		canvas.drawOval(mRectF, paint);
		color = paint.getColor();
		mOPaint.setColor(color);
		canvas.drawPoint((int)(x1+x2)/2, (int)(y1+y2)/2, paint);
//		canvas.drawText("("+(int)(x1+x2)/2+" , "+(int)(y1+y2)/2+" )",(x1+x2)/2, (y1+y2)/2, mOPaint);
	}

	@Override
	public String toString() {
		return " oval";
	}
}
