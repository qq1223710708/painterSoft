package com.example.picturedemo820.Shapes;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.picturedemo820.Interface.Shapable;

public class DrawBitmap extends ShapeAbstract{

	DrawBitmap(Shapable paintTool) {
		super(paintTool);
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
		canvas.drawPoint(cx, cy, paint);
		canvas.drawText("("+(int)cx+","+(int)cy+"半径="+(int)radius+")", cx, cy, paint);
	}

}
