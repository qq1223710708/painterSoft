package com.example.picturedemo820.Shapes;

import com.example.picturedemo820.Interface.Shapable;

import android.graphics.Canvas;
import android.graphics.Paint;


public class Curv extends ShapeAbstract  {


	public Curv(Shapable paintTool) {
		super(paintTool);
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		super.draw(canvas, paint);

		canvas.drawPath(mPath, paint);
	}

	@Override
	public String toString() {
		return "curv";
	}
}
