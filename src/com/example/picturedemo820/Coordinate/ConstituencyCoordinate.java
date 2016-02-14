package com.example.picturedemo820.Coordinate;

import android.graphics.Bitmap;

public class ConstituencyCoordinate {
	private int cx;
	private int cy;
	private int[] cxcy;
	private Bitmap bitmap;
	
	public ConstituencyCoordinate(int x,int y,Bitmap bitmap){
		cx = x;
		cy = y; 
		this.bitmap = bitmap;
	}
	
	public int[] getViewCoordinate(int x, int y){
		cxcy = new int[2];
		return cxcy;
	}
	
	

}
