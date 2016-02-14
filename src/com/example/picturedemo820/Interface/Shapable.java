package com.example.picturedemo820.Interface;

import com.example.picturedemo820.Painttools.FirstCurrentPosition;

import android.graphics.Path;


public interface Shapable {
	public Path getPath();

	public FirstCurrentPosition getFirstLastPoint();

	void setShap(ShapesInterface shape);
}
