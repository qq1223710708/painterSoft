package com.example.picturedemo820.Painttools;

import com.example.picturedemo820.Interface.ToolInterface;

import android.graphics.Paint;


//普通画笔
public class PlainPen extends PaintAbstract implements ToolInterface {
	public PlainPen(int size, int penColor) {
		this(size,penColor,Paint.Style.STROKE,255);
	}

	public PlainPen(int size, int penColor,Paint.Style style,int alpha) {
		super(size, penColor,style,alpha);
	}
}
