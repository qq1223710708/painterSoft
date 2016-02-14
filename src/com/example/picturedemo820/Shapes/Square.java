package com.example.picturedemo820.Shapes;

import com.example.picturedemo820.Interface.Shapable;
import com.example.picturedemo820.Painttools.PaintConstants.DEFAULT;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Square extends ShapeAbstract {

	private Paint mSPaint = null;
	private int color;

	private int lefttopx;
	private int lefttopy;
	private int leftbottomx;
	private int leftbottomy;
	private int righttopx;
	private int righttopy;
	private int rightbottomx;
	private int rightbottoy;

	public Square(Shapable paintTool) {
		super(paintTool);
		mSPaint = new Paint();
		mSPaint.setStrokeWidth(2);
		mSPaint.setAntiAlias(true);
		mSPaint.setStyle(Paint.Style.STROKE);
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		super.draw(canvas, paint);

		color = paint.getColor();
		mSPaint.setColor(color);

		if ((y2 > y1 && x2 > x1) || (y2 < y1 && x2 < x1)) {
			if (Math.abs(x2 - x1) > Math.abs(y2 - y1)) {
				canvas.drawRect(x1, y1, x1 + y2 - y1, y2, paint);

				lefttopx = (int) x1;
				lefttopy = DEFAULT.viewheight - (int) y1;

				leftbottomx = (int) x1;
				leftbottomy = DEFAULT.viewheight - (int) y2;

				righttopx = (int) (x1 + y2 - y1);
				righttopy = DEFAULT.viewheight - (int) y1;

				rightbottomx = (int) (x1 + y2 - y1);
				rightbottoy = DEFAULT.viewheight - (int) y2;

//				canvas.drawText("(" + lefttopx + ", " + lefttopy + ")", x1,
//						y1 - 10, mSPaint);
//				canvas.drawText("(" + leftbottomx + ", " + leftbottomy + ")",
//						x1, y2 + 20, mSPaint);
//				canvas.drawText("(" + righttopx + ", " + righttopy + ")", x1
//						+ y2 - y1 - 50, y1 - 10, mSPaint);
//				canvas.drawText("(" + rightbottomx + ", " + rightbottoy + ")",
//						(x1 + y2 - y1) - 50, y2 + 20, mSPaint);

			} else {
				canvas.drawRect(x1, y1, x2, y1 + x2 - x1, paint);

				lefttopx = (int) x1;
				lefttopy = DEFAULT.viewheight - (int) y1;

				leftbottomx = (int) x1;
				leftbottomy = DEFAULT.viewheight - (int) (y1 + x2 - x1);

				righttopx = (int) x2;
				righttopy = DEFAULT.viewheight - (int) y1;

				rightbottomx = (int) x2;
				rightbottoy = DEFAULT.viewheight - (int) (y1 + x2 - x1);

//				canvas.drawText("(" + lefttopx + ", " + lefttopy + ")", x1,
//						y1 - 10, mSPaint);
//				canvas.drawText("(" + leftbottomx + ", " + leftbottomy + ")",
//						x1, (y1 + x2 - x1) + 20, mSPaint);
//				canvas.drawText("(" + righttopx + ", " + righttopy + ")",
//						x2 - 50, y1 - 10, mSPaint);
//				canvas.drawText("(" + rightbottomx + ", " + rightbottoy + ")",
//						x2 - 50, (y1 + x2 - x1) + 20, mSPaint);

			}
		} else {
			if (Math.abs(x2 - x1) > Math.abs(y2 - y1)) {
				canvas.drawRect(x1, y1, x1 + y1 - y2, y2, paint);

				lefttopx = (int) x1;
				lefttopy = DEFAULT.viewheight - (int) y1;

				leftbottomx = (int) x1;
				leftbottomy = DEFAULT.viewheight - (int) y2;

				righttopx = (int) (x1 + y1 - y2);
				righttopy = DEFAULT.viewheight - (int) y1;

				rightbottomx = (int) (x1 + y1 - y2);
				rightbottoy = DEFAULT.viewheight - (int) y2;

//				canvas.drawText("(" + lefttopx + ", " + lefttopy + ")",
//						x1 - 50, y1 - 10, mSPaint);
//				canvas.drawText("(" + leftbottomx + ", " + leftbottomy + ")",
//						x1 - 50, y2 + 20, mSPaint);
//				canvas.drawText("(" + righttopx + ", " + righttopy + ")", (x1
//						+ y1 - y2), y1 - 10, mSPaint);
//				canvas.drawText("(" + rightbottomx + ", " + rightbottoy + ")",
//						(x1 + y1 - y2), y2 + 20, mSPaint);

			} else {
				canvas.drawRect(x1, y1, x2, y1 + x1 - x2, paint);

				lefttopx = (int) x1;
				lefttopy = DEFAULT.viewheight - (int) y1;

				leftbottomx = (int) x1;
				leftbottomy = DEFAULT.viewheight - (int) (y1 + x1 - x2);

				righttopx = (int) x2;
				righttopy = DEFAULT.viewheight - (int) y1;

				rightbottomx = (int) x2;
				rightbottoy = DEFAULT.viewheight - (int) (y1 + x1 - x2);

//				canvas.drawText("(" + lefttopx + ", " + lefttopy + ")",
//						x1 - 50, y1 - 10, mSPaint);
//				canvas.drawText("(" + leftbottomx + ", " + leftbottomy + ")",
//						x1 - 50, (y1 + x1 - x2) + 20, mSPaint);
//				canvas.drawText("(" + righttopx + ", " + righttopy + ")", x2,
//						y1 - 10, mSPaint);
//				canvas.drawText("(" + rightbottomx + ", " + rightbottoy + ")",
//						x2, (y1 + x1 - x2) + 20, mSPaint);
			}
		}
	}

	@Override
	public String toString() {
		return "Square";
	}
}
