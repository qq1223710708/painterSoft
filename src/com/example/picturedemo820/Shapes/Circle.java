package com.example.picturedemo820.Shapes;

import com.example.picturedemo820.Interface.Shapable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Circle extends ShapeAbstract {
	
	private Paint cPaint = null;
	 private int color;

	public Circle(Shapable paintTool) {
		super(paintTool);
		cPaint = new Paint();
		cPaint.setColor(Color.BLACK);
		cPaint.setStrokeWidth(2);
		cPaint.setAntiAlias(true);
		cPaint.setStyle(Paint.Style.STROKE);
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		if (canvas==null || paint == null) {
			return;
		}
		super.draw(canvas, paint);
		float cx = (x1 + x2)/2;
		float cy = (y1+y2)/2;
		float radius = (float) Math.sqrt(Math.pow(x1 - x2, 2)
				+ Math.pow(y1 - y2, 2))/2;
		canvas.drawCircle(cx, cy, radius, paint);
		color = paint.getColor();
		cPaint.setColor(color);
		canvas.drawPoint(cx, cy, cPaint);
//		canvas.drawText("("+(int)cx+","+(int)cy+"),  r="+(int)radius, cx+5, cy, cPaint);
	}

	@Override
	public String toString() {
		return " circle";
	}
}
