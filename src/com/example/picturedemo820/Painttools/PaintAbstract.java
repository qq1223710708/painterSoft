package com.example.picturedemo820.Painttools;



import com.example.picturedemo820.Interface.Shapable;
import com.example.picturedemo820.Interface.ShapesInterface;
import com.example.picturedemo820.Interface.ToolInterface;
import com.example.picturedemo820.Shapes.Curv;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;

public class PaintAbstract implements ToolInterface,Shapable{

	protected FirstCurrentPosition mFirstCurrentPosition = null;
	protected Paint mPenPaint = null;

	protected ShapesInterface currentShape = null;

	protected int penSize;
	protected Style style;

	private Path mPath = null;

	private int mCurrentX = 0;
	private int mCurrentY = 0;

	public PaintAbstract() {
		this(0, 0);
	}

	protected PaintAbstract(int penSize, int penColor) {
		this(penSize, penColor, Paint.Style.STROKE,255);
	}

	protected PaintAbstract(int penSize, int penColor, Paint.Style style,int alpha) {
		super();
		initPaint(penSize, penColor, style,alpha);
		mFirstCurrentPosition = new FirstCurrentPosition();
		currentShape = new Curv(this);
		mPath = new Path();
	}

	protected void initPaint(int penSize, int penColor, Paint.Style style,int alpha) {
		mPenPaint = new Paint();
		mPenPaint.setStrokeWidth(penSize);
		mPenPaint.setColor(penColor);
		this.penSize = penSize;
		this.style = style;
		mPenPaint.setDither(true);
		mPenPaint.setAlpha(alpha);
		mPenPaint.setAntiAlias(true);
		mPenPaint.setStyle(style);
		mPenPaint.setStrokeJoin(Paint.Join.ROUND);
		mPenPaint.setStrokeCap(Paint.Cap.ROUND);
		mPenPaint.setTextSize(50);
	}

	/**
	 * 保存起始位置
	 */
	private void saveDownPoint(float x, float y) {
		mFirstCurrentPosition.firstX = x;
		mFirstCurrentPosition.firstY = y;
	}

	@Override
	public void draw(Canvas canvas) {

		if (canvas != null) {
			mFirstCurrentPosition.currentX = mCurrentX;
			mFirstCurrentPosition.currentY = mCurrentY;

			currentShape.draw(canvas, mPenPaint);

		}
	}

	@Override
	public Path getPath() {
		return mPath;
	}

	@Override
	public FirstCurrentPosition getFirstLastPoint() {
		return mFirstCurrentPosition;
	}

	@Override
	public void setShap(ShapesInterface shape) {
		currentShape = shape;
	}

	@Override
	public void touchDown(int x, int y) {
		saveDownPoint(x, y);
		// 每次down的时候都要将path清空，并且重新设置起点
		mPath.reset();
		// 重新设置起点
		mPath.moveTo(x, y);
		savePoint(x, y);
	}

	/**
	 * 将当前的点保存起来
	 */
	private void savePoint(int x, int y) {
		mCurrentX = x;
		mCurrentY = y;
	}

	@Override
	public void touchMove(int x, int y) {
		mPath.quadTo(mCurrentX, mCurrentY, (x + mCurrentX) / 2,(y + mCurrentY) / 2);
		savePoint(x, y);
	}

	@Override
	public void touchUp(int x, int y) {
		mPath.lineTo(x, y);
	}

	@Override
	public boolean hasDraw() {
		return false;
	}

}
