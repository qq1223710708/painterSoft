package com.example.picturedemo820.Shapes;

import com.example.picturedemo820.Interface.Shapable;
import com.example.picturedemo820.Painttools.PaintConstants.DEFAULT;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


public class Line extends ShapeAbstract {
	int startx=0;
	int starty=0;
	int stopx=0;
	int stopy=0;
	
	private Paint mLPaint = null;
    private int color;

	public Line(Shapable paintTool) {
		super(paintTool);
		mLPaint = new Paint();
		mLPaint.setStrokeWidth(2);
		mLPaint.setAntiAlias(true);
		mLPaint.setStyle(Paint.Style.STROKE);
	}

	@Override
	public void draw(Canvas canvas,Paint paint) {
		super.draw(canvas, paint);
		color = paint.getColor();
		mLPaint.setColor(color);
		startx = (int)x1;
		starty = DEFAULT.viewheight-(int)y1;
		stopx = (int)x2;
		stopy = DEFAULT.viewheight-(int)y2;
		canvas.drawLine(x1, y1, x2, y2, paint);
//		canvas.drawText("("+startx+","+starty+")", x1, y1-10, mLPaint);
//		canvas.drawText("("+stopx+","+stopy+")", x2, y2+10, mLPaint);
	}

	@Override
	public String toString() {
		return " line";
	}
}
