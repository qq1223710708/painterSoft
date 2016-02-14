package com.example.picturedemo820.Interface;


import android.graphics.Canvas;


/*
 * 所有画笔类都应当实现这个接口
 */
public interface ToolInterface {
	public void draw(Canvas canvas);

	public void touchDown(int x, int y);

	public void touchMove(int x, int y);

	public void touchUp(int x, int y);

	public boolean hasDraw();
}
