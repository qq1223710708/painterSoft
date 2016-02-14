package com.example.picturedemo820.PaintView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/*import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;*/

import com.example.picturedemo820.MainActivity;
import com.example.picturedemo820.Interface.Shapable;
import com.example.picturedemo820.Interface.ShapesInterface;
import com.example.picturedemo820.Interface.ToolInterface;
import com.example.picturedemo820.Painttools.Constituency;
import com.example.picturedemo820.Painttools.Eraser;
import com.example.picturedemo820.Painttools.FilleColor;
import com.example.picturedemo820.Painttools.MoveBitmap;
import com.example.picturedemo820.Painttools.PicPaint;
import com.example.picturedemo820.Painttools.PicpaintConstituency;
import com.example.picturedemo820.Painttools.PlainPen;
import com.example.picturedemo820.Painttools.PaintConstants.DEFAULT;
import com.example.picturedemo820.Painttools.PaintConstants.ERASER_SIZE;
import com.example.picturedemo820.Painttools.PaintConstants.PEN_TYPE;
import com.example.picturedemo820.Painttools.PaintConstants.SHAP;
import com.example.picturedemo820.Shapes.Circle;
import com.example.picturedemo820.Shapes.Curv;
import com.example.picturedemo820.Shapes.Line;
import com.example.picturedemo820.Shapes.Oval;
import com.example.picturedemo820.Shapes.Rectangle;
import com.example.picturedemo820.Shapes.Square;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

public class PaintView extends View {

	private MainActivity mainactivity;

	boolean canvasIsCreated = false;

	private Canvas mCanvas = null;

//	private Path mPath = null;

//	private int mCurrentx, mCurrenty;

	private boolean isTouchUp = false;

	private boolean ishasdrawPicpaint = false;

	/* Bitmap 的配置 */
	private Bitmap mBitmap = null;
	private Bitmap mOrgBitMap = null;
//	private Bitmap ConBitmap = null;

	private int mBitmapWidth = 0;
	private int mBitmapHeight = 0;

	private int mBackGroundColor = Color.WHITE;

	/* paint 的配置 */
	private Paint mBitmapPaint = null;

	private int mPenColor = Color.BLACK;
	private int mPenSize = 5;
	private Paint.Style mStyle = Paint.Style.STROKE;

	private int mPenAlpha = 255;

	private ToolInterface mCurrentPainter = null;
	// private ToolInterface mdeleteCoorr = null;

	int mPaintType = PEN_TYPE.PLAIN_PEN;

	private int mCurrentShapeType = 0;

	private int mEraserSize = ERASER_SIZE.SIZE_1;

	private ShapesInterface mCurrentShape = null;

	private PaintPadUndoStack mUndoStack = null;
	private static final int STACK_SIZE = 20;

	/*
	 * 选区的配置
	 */
	private boolean isHasConstituency = false;// 没有选区的时候是false
//	private boolean isindash = false;
	// 选区开始做标
	private int Cstartx;
	private int Cstarty;
	// 选区的当前做标
	private int mCurrentConstx;
	private int mCurrentConsty;
	// 存放选区四个顶点做标的二维数组
	private int[][] pointCorrdinate = new int[4][2];
	// 存放bitmap全部颜色的数组
	private int[][] bitmapRGB;
	public Bitmap conBitmap;

	// 图片画笔
	public Bitmap PicpaintBitmap;
	private Bitmap tempBitmap;
	private boolean reflection = false;

	// 填充颜色的设置
	private int mFilleColor = Color.BLACK;

	private Stack<Point> mStacks = new Stack<Point>();
	private int mBorderColor = -1;
	private boolean hasBorderColor = false;
	private ArrayList<int[]> PicpaintCoordinateList = new ArrayList();

	//图层配置
	private static final int LAYER_FLAGS = Canvas.MATRIX_SAVE_FLAG
			| Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
			| Canvas.FULL_COLOR_LAYER_SAVE_FLAG
			| Canvas.CLIP_TO_LAYER_SAVE_FLAG;

//	private Paint paint;
	public List<Map<String,Object>> layerList= new ArrayList<Map<String,Object>>();
	Map<String,Object> map= new HashMap<String,Object>();

	int bitmapw,
		bitmaph;
	Bitmap mBitmap1;
	Bitmap mCurrentBitmap;//当前图层正在操作的Bitmap


	public PaintView(Context context) {
		super(context);
		mainactivity = (MainActivity) context;
		init();
	}

	public void init() {
		mCanvas = new Canvas();
		//mPath = new Path();
		mPaintType = PEN_TYPE.PLAIN_PEN;
		mCurrentShapeType = SHAP.CURV;
		mUndoStack = new PaintPadUndoStack(this, STACK_SIZE);
		createnewPaint();
	}

	// 创建画笔
	public void createnewPaint() {
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);	// 实例化画笔，并设置抗锯齿标志
		ToolInterface tool = null;

		switch (mPaintType) {
			case PEN_TYPE.PLAIN_PEN:
				tool = new PlainPen(mPenSize, mPenColor, mStyle, mPenAlpha);
				break;

			case PEN_TYPE.ERASER:
				tool = new Eraser(mEraserSize);
				break;
			// case PEN_TYPE.BLUR:
			// tool = new BlurPen(mPenSize, mPenColor, mStyle);
			// break;
			// case PEN_TYPE.EMBOSS:
			// tool = new EmbossPen(mPenSize,mPenColor, mStyle);
			// break;
			case PEN_TYPE.CONSTITUENCY:
				tool = new Constituency(getBackGroundColor(), mBitmap);
				break;
			case PEN_TYPE.DRAWBITMAP:
				tool = new MoveBitmap(conBitmap);
				break;
			case PEN_TYPE.PICPAINT:
				tempBitmap = PicpaintBitmap;
				tool = new PicPaint(PicpaintBitmap, mPenAlpha, reflection);
				break;
			case PEN_TYPE.FilleColor:
				tool = new FilleColor();
				break;
			case PEN_TYPE.PicpaintConstituency:
				tool = new PicpaintConstituency(getBackGroundColor());
				break;
			default:
				break;
		}
		mCurrentPainter = tool;
		setShape();
	}

	// 设置画笔画出的图形的具体形状
	private void setShape() {
		if (mCurrentPainter instanceof Shapable) {
			switch (mCurrentShapeType) {
				case SHAP.CURV:
					mCurrentShape = new Curv((Shapable) mCurrentPainter);
					break;
				case SHAP.LINE:
					mCurrentShape = new Line((Shapable) mCurrentPainter);
					break;
				case SHAP.SQUARE:
					mCurrentShape = new Square((Shapable) mCurrentPainter);
					break;
				case SHAP.RECT:
					mCurrentShape = new Rectangle((Shapable) mCurrentPainter);
					break;
				case SHAP.CIRCLE:
					mCurrentShape = new Circle((Shapable) mCurrentPainter);
					break;
				case SHAP.OVAL:
					mCurrentShape = new Oval((Shapable) mCurrentPainter);
					break;
				default:
					break;
			}
			((Shapable) mCurrentPainter).setShap(mCurrentShape);
		}
	}

	@Override
	public void onDraw(Canvas cv) {
		cv.drawColor(mBackGroundColor);
		// 在外部绘制的方法只有一种，就是先在bitmap上绘制，然后加载到cv

		System.out.println("layerList.size==="+layerList.size());

		//if(mBitmap1==null){
		//cv.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
		//}else{
		//cv.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
		for(int i=0;i<layerList.size();i++){
			cv.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
			cv.drawBitmap((Bitmap) layerList.get(i).get("layer_image"), 0, 0, mBitmapPaint);
		}
		//}

		//cv.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
		// TouchUp使用BitMap的canvas进行绘制，也就不用再View上绘制了
		// TouchUp使用BitMap的canvas进行绘制，也就不用再View上绘制了
		if (!isTouchUp) {
			// 平时都只在view的cv上临时绘制
			// earaser不能再cv上绘制，需要直接绘制在bitmap上
			if (mPaintType != PEN_TYPE.ERASER) {
				mCurrentPainter.draw(cv);
			}
		}
	}

	/**
	 * 当此事件发生时，创建Bitmap并setCanvas
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		DEFAULT.viewwidth = w;
		DEFAULT.viewheight = h;

		if (!canvasIsCreated) {
			mBitmapWidth = w;
			mBitmapHeight = h;
			creatCanvasBitmap(w, h);
			canvasIsCreated = true;
		}
	}

	/**
	 * 创建bitMap同时获得其canvas
	 */
	private void creatCanvasBitmap(int w, int h) {
		bitmapw = w;
		bitmaph = h;
		mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

		map.put("layer_image", mBitmap);
		map.put("layer_name", "图层-"+layerList.size());
		layerList.add(map);
		mCurrentBitmap = mBitmap;
		mCanvas.setBitmap(mBitmap);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		isTouchUp = false;
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				createnewPaint();
				mCurrentPainter.touchDown(x, y);

				mUndoStack.clearRedo();

				if (mPaintType == PEN_TYPE.CONSTITUENCY) {// 选区模式下
					//选区模式的时候，建立一个选区就新建立一个图层，图层对应一个bitmap，在结束选区的时候，删除对应的bitmap，

					createConsitituencyNewBitmap();

					if (isHasConstituency && !isInConstituency(x, y)) {// 落点不在选区范围内
						clearConstituency();
					}
					if (isHasConstituency && isInConstituency(x, y)) {// 落点在选取范围内
						drawConstituency();
					}
				}

				if (mPaintType == PEN_TYPE.FilleColor) {// 填色
					FilleColor(x, y);
				}

				if(mPaintType == PEN_TYPE.PICPAINT && ishasdrawPicpaint){
					System.out.println("ishasdrawPicpaint=="+ishasdrawPicpaint);
					System.out.println("isInPicpaintCoordinate=="+isInPicpaintCoordinate(x,y));

					if(isInPicpaintCoordinate(x,y)){//已有图片画笔 ，判断落点是否在图片内
						MovePicpaint();
					}else if(!isInPicpaintCoordinate(x,y)){
						isNotInPicpaintCoordinate(x,y);
					}
				}


				invalidate();
				break;
			case MotionEvent.ACTION_MOVE:
				mCurrentPainter.touchMove(x, y);
				if (mPaintType == PEN_TYPE.ERASER) {
					mCurrentPainter.draw(mCanvas);
				}
				if (mPaintType == PEN_TYPE.DRAWBITMAP) {
					//clearAllConstituency();
				}
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				mCurrentConstx = x;
				mCurrentConsty = y;

				if (mPaintType == PEN_TYPE.CONSTITUENCY) {// 获得选区的四个顶点坐标
					isHasConstituency = true;
					getConsCoordinate();
				}

				if (mPaintType == PEN_TYPE.DRAWBITMAP) {
					mPaintType = PEN_TYPE.CONSTITUENCY;
				}

				mCurrentPainter.touchUp(x, y);
				// 只有在up的时候才在bitmap上画图，最终显示在view上
				mCurrentPainter.draw(mCanvas);

				mUndoStack.push(mCurrentPainter);

				if (mPaintType == PEN_TYPE.PICPAINT) {// 图片画笔下，显示图片效果按钮
					mainactivity.onClickPicWhenUpPicpaint(x, y);
					mPaintType = PEN_TYPE.CONSTITUENCY;
					getPicPaintCoordinate(x, y, PicpaintBitmap);
					//addConstituencynoUndo();
					addConstituency();
					ishasdrawPicpaint = true;

					isHasConstituency = true;
				}

				invalidate();
				isTouchUp = true;
				break;
		}
		return true;
	}

	// 扩大图片画笔画出的图片
	public void ChangeSizeBigOnPicpaint(int x, int y) {

		Matrix matrix = new Matrix();
		matrix.preScale(1.25f, 1.25f);
		undo();
		undo();
		PicpaintBitmap = Bitmap.createBitmap(PicpaintBitmap, 0, 0,
				PicpaintBitmap.getWidth(), PicpaintBitmap.getHeight(), matrix,
				true);
		createnewPaint();
		mCurrentPainter.touchDown(x, y);
		mCurrentPainter.touchUp(x, y);
		mCurrentPainter.draw(mCanvas);
		getPicPaintCoordinate(x, y, PicpaintBitmap);
		mUndoStack.push(mCurrentPainter);
		addConstituency();
		invalidate();
	}

	// 缩小图片画笔画出的图片
	public void ChangeSizeSmallOnPicpaint(int x, int y) {
		int w = PicpaintBitmap.getWidth();
		int h = PicpaintBitmap.getHeight();
		undo();
		undo();

		Matrix matrix = new Matrix();
		matrix.preScale(0.75f, 0.75f);
		PicpaintBitmap = Bitmap.createBitmap(PicpaintBitmap, 0, 0, w, h,
				matrix, true);

		createnewPaint();
		mCurrentPainter.touchDown(x, y);
		mCurrentPainter.touchUp(x, y);
		mCurrentPainter.draw(mCanvas);

		getPicPaintCoordinate(x, y, PicpaintBitmap);
		mUndoStack.push(mCurrentPainter);

		addConstituency();

		invalidate();
	}

	// 旋转图片画笔画出的图片
	public void RotateOnPicpaint(int x, int y) {
		tempBitmap = PicpaintBitmap;
		Matrix matrix = new Matrix();
		matrix.preRotate(90);
		undo();
		mUndoStack.push(mCurrentPainter);
		PicpaintBitmap = Bitmap.createBitmap(PicpaintBitmap, 0, 0,
				PicpaintBitmap.getWidth(), PicpaintBitmap.getHeight(), matrix,
				true);
		createnewPaint();
		mCurrentPainter.touchDown(x, y);
		mCurrentPainter.touchUp(x, y);
		mUndoStack.push(mCurrentPainter);
		mCurrentPainter.draw(mCanvas);
		addConstituency();
		invalidate();
	}

	// 倒影
	public void ReflectionOnPicpaint(int x, int y) {
		tempBitmap = PicpaintBitmap;
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);
		undo();
		PicpaintBitmap = Bitmap.createBitmap(PicpaintBitmap, 0, 0,
				PicpaintBitmap.getWidth(), PicpaintBitmap.getHeight(), matrix,
				true);

		reflection = true;
		createnewPaint();

		mCurrentPainter.touchDown(x, y);
		mCurrentPainter.touchUp(x, y);
		mUndoStack.push(mCurrentPainter);
		mCurrentPainter.draw(mCanvas);
		reflection = false;
		invalidate();
	}

	// 为每一次图片画笔变换后的图片添加虚线
	private void addConstituency() {
		//setCurrentPainterType(PEN_TYPE.CONSTITUENCY);
		createnewPaint();
		int cindex = PicpaintCoordinateList.size();

		mCurrentPainter.touchDown(pointCorrdinate[0][0],
				pointCorrdinate[0][1]);
		mCurrentPainter.touchUp(pointCorrdinate[3][0],
				pointCorrdinate[3][1]);
		mCurrentPainter.draw(mCanvas);
		mUndoStack.push(mCurrentPainter);
		//setCurrentPainterType(PEN_TYPE.PICPAINT);
	}


	// 实时获取每一次每一个图片画笔所在的位置
	public void getPicPaintCoordinate(int x, int y, Bitmap mbitmap) {
		/*PicpaintCoordinateList.clear();
		int[] Picpaintc = new int[4];

		Picpaintc[0] = (int) x - (mbitmap.getWidth() / 2);
		Picpaintc[1] = (int) y - (mbitmap.getHeight() / 2);
		
		Picpaintc[2] = (int) x + (mbitmap.getWidth() / 2);
		Picpaintc[3] = (int) y + (mbitmap.getHeight() / 2);
		PicpaintCoordinateList.add(Picpaintc); */

		pointCorrdinate[0][0] = (int) x - (mbitmap.getWidth() / 2);
		pointCorrdinate[0][1] = (int) y - (mbitmap.getHeight() / 2);

		pointCorrdinate[1][0] = (int) x + (mbitmap.getWidth() / 2);
		pointCorrdinate[1][1] = (int) y - (mbitmap.getHeight() / 2);

		pointCorrdinate[2][0] = (int) x - (mbitmap.getWidth() / 2);
		pointCorrdinate[2][1] =	(int) y + (mbitmap.getHeight() / 2);

		pointCorrdinate[3][0] =(int) x + (mbitmap.getWidth() / 2);
		pointCorrdinate[3][1] =(int) y + (mbitmap.getHeight() / 2);

	}

	//判断落点是否在图片中   
	private boolean isInPicpaintCoordinate(int x,int y){
		boolean b= false;
		/*if(x>=PicpaintCoordinateList.get(0)[0]&&x<=PicpaintCoordinateList.get(0)[2]&&y>=PicpaintCoordinateList.get(0)[1]&&y<=PicpaintCoordinateList.get(0)[3]){
			b = true;
		}*/
		if(x>=pointCorrdinate[0][0]&&x<=pointCorrdinate[3][0]&&y>=pointCorrdinate[0][1]&&y<=pointCorrdinate[3][1]){
			b = true;
		}
		return b;
	}

	public void createConsitituencyNewBitmap(){
		mCurrentBitmap = Bitmap.createBitmap(bitmapw,bitmaph,Bitmap.Config.ARGB_8888);
		mCanvas.setBitmap(mCurrentBitmap);

		System.out.println(123);


		//int sc = mCanvas.saveLayerAlpha(0, 0, bitmapw, bitmaph, 0x88, LAYER_FLAGS);

		//mCanvas.restore();

		map= new HashMap<String,Object>();
		map.put("layer_image", mCurrentBitmap);
		map.put("layer_name", "选区");
		layerList.add(map);

		invalidate();

	}

	//图片画笔模式下    落点不在选区之内
	private void isNotInPicpaintCoordinate(int x,int y){
		undo();
		PicpaintBitmap = tempBitmap;
	}

	//图片画笔下   落点在选取之内 可以移动  即创建一个新的画笔
	private void MovePicpaint(){
		//PicpaintBitmap = Bitmap.createBitmap(PicpaintBitmap, 0, 0,PicpaintBitmap.getWidth(), PicpaintBitmap.getHeight());
		System.out.println("PicpaintCoordinateList.get(0)[0] - 2=="+PicpaintCoordinateList.get(0)[0]);
		for (int i = PicpaintCoordinateList.get(0)[0] - 2; i < PicpaintCoordinateList.get(0)[2] + 2; i++) {
			for (int j = PicpaintCoordinateList.get(0)[1] - 2; j < PicpaintCoordinateList.get(0)[3] + 2; j++) {
				mBitmap.setPixel(i, j, mBackGroundColor);
			}
		}
	}

	//导入图片的时候创建新的图层，并将资源读取的bitmap显示在对用图层 
	public void setOtherCanvas(){
		mBitmap1 = Bitmap.createBitmap(bitmapw,  bitmaph, Bitmap.Config.ARGB_8888);
		mCanvas.setBitmap(mBitmap1);
		mCurrentBitmap = mBitmap1;

		String picpath = "/mnt/sdcard/PictureDemo/Bitmap/图层-1101.txt";

		File file = new File(picpath);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		FileInputStream fi = null;
		try {
			fi = new FileInputStream(file);

			System.out.println("fi=="+fi);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		byte[] buffer = null;
		byte[] b = new byte[1024];
		int n;
		try {
			while((n = fi.read(b)) != -1){
				bos.write(b,0,n);
			}
			buffer = bos.toByteArray();
			System.out.println("buffer=="+buffer);
			fi.close();
			bos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		Bitmap bg = null;
		if(buffer.length != 0){
			System.out.println("buffer=="+buffer);
			bg = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
		}

		System.out.println("bg=="+bg);

		mCanvas.setBitmap(mBitmap1);

		int sc = mCanvas.saveLayerAlpha(0, 0, bitmapw, bitmaph, 0x88, LAYER_FLAGS);
		mCanvas.restore();
		map= new HashMap<String,Object>();
		map.put("layer_image", mBitmap1);
		map.put("layer_name", "图层-"+layerList.size());
		layerList.add(map);

		PicpaintBitmap = bg;
		mPaintType = PEN_TYPE.PICPAINT;
		createnewPaint();
		mCurrentPainter.touchDown(PicpaintBitmap.getWidth()/2, PicpaintBitmap.getHeight()/2);
		mCurrentPainter.touchUp(PicpaintBitmap.getWidth()/2, PicpaintBitmap.getHeight()/2);
		mCurrentPainter.draw(mCanvas);
		invalidate();
		mPaintType = PEN_TYPE.PLAIN_PEN;
		mCurrentShapeType = SHAP.CURV;
		createnewPaint();
	}

	//新建一个图层
	public void addNewLayer(){
		mBitmap1 = Bitmap.createBitmap(bitmapw,  bitmaph, Bitmap.Config.ARGB_8888);
		mCanvas.setBitmap(mBitmap1);
		mCurrentBitmap = mBitmap1;
		//int sc = mCanvas.saveLayer(0, 0, mBitmap1.getWidth(), mBitmap1.getHeight(), null, LAYER_FLAGS);
		int sc = mCanvas.saveLayerAlpha(0, 0, bitmapw, bitmaph, 0x88, LAYER_FLAGS);

		mCanvas.restore();

		map= new HashMap<String,Object>();
		map.put("layer_image", mBitmap1);
		map.put("layer_name", "图层-"+layerList.size());
		layerList.add(map);

		invalidate();

	}

	//ListView点击事件，选择了一个图层，得到该图层对应的Bitmap，
	public void setOnClickListViewPosition(int position){
		mCurrentBitmap = (Bitmap) layerList.get(position).get("layer_image");
		mCanvas.setBitmap(mCurrentBitmap);
	}

	//ListView 长按事件，删除对应图层对应的Bitmap
	public void deleteListViewPositionLayer(int position){
		System.out.println("layerList.size()=="+layerList.size());
		/*if(position>=1){
			mCurrentBitmap = (Bitmap) layerList.get(position-1).get("layer_image");
			mCanvas.setBitmap(mCurrentBitmap);
		}*/
		System.out.println("position=="+position);
		layerList.remove(position);

		if(layerList.size()==0){
			mBitmap = Bitmap.createBitmap(bitmapw, bitmaph, Bitmap.Config.ARGB_8888);

			map.put("layer_image", mBitmap);
			map.put("layer_name", "图层-"+layerList.size()+1);
			layerList.add(map);

			mCanvas.setBitmap(mBitmap);
			setCurrentPainterType(PEN_TYPE.PLAIN_PEN);
			createnewPaint();
		}

		invalidate();
		System.out.println("layerList.size()=="+layerList.size());
	}



	// 填充颜色方法
	private void FilleColor(int x, int y) {

		int pixel = mCurrentBitmap.getPixel(x, y);
		int mcolor = getFilleColor();
		if (pixel == mcolor) {
			return;
		}

		int w = mCurrentBitmap.getWidth();
		int h = mCurrentBitmap.getHeight();

		int[] pixels = new int[w * h];

		mCurrentBitmap.getPixels(pixels, 0, w, 0, 0, w, h);

		// 填色
		fillColor(pixels, w, h, pixel, mcolor, x, y);
		mCurrentBitmap.setPixels(pixels, 0, w, 0, 0, w, h);

	}

	/*
	 * pixels mBitmap上的像素数组 w mBitmap 的宽度 h mBitmap 的高度 pixel 当前点的颜色 newColor
	 * 将要填充的颜色 cx 点击的横坐标 cy 点击的纵坐标
	 */
	public void fillColor(int[] pixels, int w, int h, int pixel, int newColor,
						  int cx, int cy) {
		mStacks.push(new Point(cx, cy));
		while (!mStacks.isEmpty()) {
			Point seed = mStacks.pop();
			int count = fillLineLeft(pixels, pixel, w, h, newColor, seed.x,
					seed.y);
			int left = seed.x - count + 1;
			count = fillLineRight(pixels, pixel, w, h, newColor, seed.x + 1,
					seed.y);
			int right = seed.x + count;

			if (seed.y - 1 >= 0) {
				findSeedInNewLine(pixels, pixel, w, h, seed.y - 1, left, right);
			}
			if (seed.y + 1 < h) {
				findSeedInNewLine(pixels, pixel, w, h, seed.y + 1, left, right);
			}
		}
	}

	/**
	 * 在新行找种子节点
	 *
	 * @param pixels
	 * @param pixel
	 * @param w
	 * @param h
	 * @param i
	 * @param left
	 * @param right
	 */
	private void findSeedInNewLine(int[] pixels, int pixel, int w, int h,
								   int i, int left, int right) {
		/**
		 * 获得该行的开始索引
		 */
		int begin = i * w + left;
		/**
		 * 获得该行的结束索引
		 */
		int end = i * w + right;

		boolean hasSeed = false;

		int rx = -1, ry = -1;

		ry = i;
		/**
		 * 从end到begin，找到种子节点入栈（AAABAAAB，则B前的A为种子节点）
		 */
		while (end >= begin) {
			if (pixels[end] == pixel) {
				if (!hasSeed) {
					rx = end % w;
					mStacks.push(new Point(rx, ry));
					hasSeed = true;
				}
			} else {
				hasSeed = false;
			}
			end--;
		}
	}

	/**
	 * 往右填色，返回填充的个数
	 *
	 * @return
	 */
	private int fillLineRight(int[] pixels, int pixel, int w, int h,
							  int newColor, int x, int y) {
		int count = 0;

		while (x < w) {
			// 拿到索引
			int index = y * w + x;
			if (needFillPixel(pixels, pixel, index)) {
				pixels[index] = newColor;
				count++;
				x++;
			} else {
				break;
			}

		}
		return count;
	}

	/**
	 * 往左填色，返回填色的数量值
	 *
	 * @return
	 */
	private int fillLineLeft(int[] pixels, int pixel, int w, int h,
							 int newColor, int x, int y) {
		int count = 0;
		while (x >= 0) {
			// 计算出索引
			int index = y * w + x;

			if (needFillPixel(pixels, pixel, index)) {
				pixels[index] = newColor;
				count++;
				x--;
			} else {
				break;
			}
		}
		return count;
	}

	private boolean needFillPixel(int[] pixels, int pixel, int index) {
		if (hasBorderColor) {
			return pixels[index] != mBorderColor;
		} else {
			return pixels[index] == pixel;
		}
	}

	// 选区模式下，点击做标在选取之内
	public void drawConstituency() {

		conBitmap = Bitmap.createBitmap(mBitmap, pointCorrdinate[0][0] + 1,
				pointCorrdinate[0][1] + 1, pointCorrdinate[1][0]
						- pointCorrdinate[0][0] - 2, pointCorrdinate[2][1]
						- pointCorrdinate[0][1] - 2);

		/*
		 * for (int i = pointCorrdinate[0][0] - 2; i < pointCorrdinate[1][0] +
		 * 2; i++) { for (int j = pointCorrdinate[0][1] - 2; j <
		 * pointCorrdinate[2][1] + 2; j++) { mBitmap.setPixel(i, j,
		 * mBackGroundColor); } }
		 */

		mPaintType = PEN_TYPE.DRAWBITMAP;
		createnewPaint();
		// isHasConstituency = false;
	}

	// 清空选区，置为背景色
	public void clearAllConstituency() {
		for (int i = pointCorrdinate[0][0] - 2; i < pointCorrdinate[1][0] + 2; i++) {
			for (int j = pointCorrdinate[0][1] - 2; j < pointCorrdinate[2][1] + 2; j++) {
				mBitmap.setPixel(i, j, mBackGroundColor);
			}
		}
	}

	// 设置画笔形状，这决定画笔画出的图形的形状 在setShape（）函数中使用mCurrentShapeType
	// 通过setshap()决定currentShape
	public void setCurrentShapType(int type) {
		switch (type) {
			case SHAP.CURV:
				mCurrentShapeType = type;// mCurrentShapeType
				break;
			case SHAP.CIRCLE:
				mCurrentShapeType = type;
				break;
			case SHAP.LINE:
				mCurrentShapeType = type;
				break;
			case SHAP.OVAL:
				mCurrentShapeType = type;
				break;
			case SHAP.SQUARE:
				mCurrentShapeType = type;
				break;
			case SHAP.RECT:
				mCurrentShapeType = type;
				break;
		}
	}

	// 设置当前画笔类型，是画笔还是 橡皮等
	public void setCurrentPainterType(int type) {
		switch (type) {
			case PEN_TYPE.BLUR:
			case PEN_TYPE.PLAIN_PEN:
				mPaintType = PEN_TYPE.PLAIN_PEN;
				break;
			case PEN_TYPE.EMBOSS:
			case PEN_TYPE.ERASER:
				mPaintType = type;
				break;
			case PEN_TYPE.CONSTITUENCY:
				mPaintType = type;
				break;
			case PEN_TYPE.DRAWBITMAP:
				mPaintType = type;
				break;
			case PEN_TYPE.PICPAINT:
				mPaintType = type;
				break;
			case PEN_TYPE.FilleColor:
				mPaintType = type;
				break;
			case PEN_TYPE.PicpaintConstituency:
				mPaintType = type;
				break;
			default:
				mPaintType = PEN_TYPE.PLAIN_PEN;
				break;
		}
	}

	/**
	 * 创建图像画笔准本的biemap
	 */
	public void setForePicpaintBitMap(Bitmap bitmap) {
		if (bitmap != null) {
			recycleMBitmap();
			recycleOrgBitmap();
		}

		PicpaintBitmap = bitmap;
		mPaintType = PEN_TYPE.PICPAINT;
		createnewPaint();

		//mCurrentPainter.touchDown(PicpaintBitmap.getWidth()*2, PicpaintBitmap.getHeight()*2);
		mCurrentPainter.touchUp(PicpaintBitmap.getWidth()*2, PicpaintBitmap.getHeight()*2);

		mCurrentPainter.draw(mCanvas);

		map= new HashMap<String,Object>();
		map.put("layer_image", PicpaintBitmap);
		map.put("layer_name", "图层-"+layerList.size());
		layerList.add(map);

		invalidate();

	}

	// 清屏
	public void clearSceen() {
		recycleMBitmap();
		recycleOrgBitmap();
		resetState();
		PicpaintCoordinateList.clear();
		isHasConstituency = false;
		ishasdrawPicpaint = false;
//		isindash = false;
		creatCanvasBitmap(mBitmapWidth, mBitmapHeight);
		invalidate();

	}

	/**
	 * 回收原始图片
	 */
	private void recycleOrgBitmap() {
		if (mOrgBitMap != null && !mOrgBitMap.isRecycled()) {
			mOrgBitMap.recycle();
			mOrgBitMap = null;
		}
	}

	/**
	 * 回收图片
	 */
	private void recycleMBitmap() {
		if (mBitmap != null && !mBitmap.isRecycled()) {
			mBitmap.recycle();
			mBitmap = null;
		}
	}

	/**
	 * 更改背景颜色
	 */
	public void setBackGroundColor(int color) {
		mBackGroundColor = color;
		invalidate();
	}

	/**
	 * 得到背景颜色
	 */
	public int getBackGroundColor() {
		return mBackGroundColor;
	}

	/**
	 * 得到当前画笔类型
	 */
	public int getCurretnPenType() {
		return mPaintType;
	}

	/**
	 * 改变当前画笔的大小
	 */
	public void setPenSize(int size) {
		mPenSize = size;
	}

	/**
	 * 设置画笔粗细
	 */
	public void setCurretnPenStorker(int x) {
		mPenSize = x;
	}

	/**
	 * 改变画笔的颜色，在创建新笔的时候就能使用了
	 */
	public void setPenColor(int color) {
		mPenColor = color;
	}

	/**
	 * 得到penColor
	 */
	public int getPenColor() {
		return mPenColor;
	}

	/**
	 * 改变填充的颜色
	 */
	public void setFilleColor(int color) {
		mFilleColor = color;
	}

	/**
	 * 得到填充的颜色
	 */
	public int getFilleColor() {
		return mFilleColor;
	}

	/**
	 * 得到penAlpha
	 */
	public int getPaintAlpha() {
		return mPenAlpha;
	}

	/**
	 * 设置penAlpha
	 */
	public void setPaintAlpha(int t) {
		mPenAlpha = t;
	}

	/**
	 * 更改图层的透明度Alpha
	 */
	public void setmCurrentBitmapAlpha(int t,int c) {
		System.out.println("t=="+t);
		System.out.println("c=="+c);

		Bitmap changealpha = (Bitmap)layerList.get(c).get("layer_image");
		int[] argb = new int[changealpha.getWidth()*changealpha.getHeight()];
		changealpha.getPixels(argb, 0, changealpha.getWidth(), 0, 0, changealpha.getWidth(), changealpha.getHeight());


		t = t*255/100;

		System.out.println("1t=="+t);

		for(int i=0;i>argb.length;i++){
			argb[i] = (t << 24) |(argb[i]&0x00FFFFFF);
		}
		changealpha = Bitmap.createBitmap(argb, changealpha.getWidth(), changealpha.getHeight(), Config.ARGB_8888);

		System.out.println("layerList.size"+layerList.size());

		map.put("layer_image", changealpha);
		map.put("layer_name", "图层-"+c);
		layerList.remove(c);

		System.out.println("layerList.size"+layerList.size());

		layerList.add(map);

		System.out.println("layerList.size"+layerList.size());

		invalidate();
	}

	/*
	 * 得到当前的mBitmap
	 */
	public Bitmap getmBitmap(){
		return mCurrentBitmap;
	}

	/**
	 * 设置橡皮模式下画笔粗细
	 */
	public void setEraserSize(int x) {
		mEraserSize = x;
	}

	/**
	 * 得到选区四点的做标牌
	 */
	public void getConsCoordinate() {

		if (mCurrentConstx < Cstartx && mCurrentConsty < Cstarty) {

			System.out.println(1001);
			pointCorrdinate[0][0] = mCurrentConstx;
			pointCorrdinate[0][1] = mCurrentConsty;

			pointCorrdinate[1][0] = Cstartx;
			pointCorrdinate[1][1] = mCurrentConsty;

			pointCorrdinate[2][0] = mCurrentConstx;
			pointCorrdinate[2][1] = Cstarty;

			pointCorrdinate[3][0] = Cstartx;
			pointCorrdinate[3][1] = Cstarty;

		} else if (mCurrentConstx > Cstartx && mCurrentConsty < Cstarty) {
			System.out.println(1002);
			pointCorrdinate[0][0] = Cstartx;
			pointCorrdinate[0][1] = mCurrentConsty;

			pointCorrdinate[1][0] = mCurrentConstx;
			pointCorrdinate[1][1] = mCurrentConsty;

			pointCorrdinate[2][0] = Cstartx;
			pointCorrdinate[2][1] = Cstarty;

			pointCorrdinate[3][0] = mCurrentConstx;
			pointCorrdinate[3][1] = Cstarty;

		} else if (mCurrentConstx < Cstartx && mCurrentConsty > Cstarty) {
			System.out.println(1003);
			pointCorrdinate[0][0] = mCurrentConstx;
			pointCorrdinate[0][1] = Cstarty;

			pointCorrdinate[1][0] = Cstartx;
			pointCorrdinate[1][1] = Cstarty;

			pointCorrdinate[2][0] = mCurrentConstx;
			pointCorrdinate[2][1] = mCurrentConsty;

			pointCorrdinate[3][0] = Cstartx;
			pointCorrdinate[3][1] = mCurrentConsty;

		} else if (mCurrentConstx > Cstartx && mCurrentConsty > Cstarty) {
			System.out.println(1004);

			pointCorrdinate[0][0] = Cstartx;
			pointCorrdinate[0][1] = Cstarty;

			pointCorrdinate[1][0] = mCurrentConstx;
			pointCorrdinate[1][1] = Cstarty;

			pointCorrdinate[2][0] = Cstartx;
			pointCorrdinate[2][1] = mCurrentConsty;

			pointCorrdinate[3][0] = mCurrentConstx;
			pointCorrdinate[3][1] = mCurrentConsty;

		}

	}

	// 存放当前bitmap上的所有颜色
	public void saveBitmapColor() {
		bitmapRGB = new int[mBitmap.getWidth()][mBitmap.getHeight()];
		// int color;
		for (int i = 0; i < mBitmap.getWidth(); i++) {
			for (int j = 0; j < mBitmap.getHeight(); j++) {
				// color = mBitmap.getPixel(i, j);
				bitmapRGB[i][j] = mBitmap.getPixel(i, j);
			}
		}
	}

	// 判断点击的点是否在已有选区内是返回true，不是返回false
	public boolean isInConstituency(int x, int y) {
		if (x > pointCorrdinate[0][0] && x < pointCorrdinate[1][0]
				&& y > pointCorrdinate[0][1] && y < pointCorrdinate[2][1]) {
			return true;
		}
		return false;
	}

	// 当点击选区外按钮或者点击位置不在现有选区内时，清除现有选区
	public void clearConstituency() {
		System.out.println(155);
		undo();
//		isindash = false;
	}

	/**
	 * 得到当前view的截图
	 */
	public Bitmap getSnapShoot() {
		// 获得当前的view的图片
		setDrawingCacheEnabled(true);
		buildDrawingCache(true);
		Bitmap bitmap = getDrawingCache(true);
		Bitmap bmp = BitMapUtils.duplicateBitmap(bitmap);
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
		// 将缓存清理掉
		setDrawingCacheEnabled(false);
		return bmp;
	}

	public void undo() {
		if (null != mUndoStack) {
			mUndoStack.undo();
		}
	}

	public void redo() {
		if (null != mUndoStack) {
			mUndoStack.redo();
		}
	}

	public boolean canUndo() {
		return mUndoStack.canUndo();
	}

	public boolean canRedo() {
		return mUndoStack.canRedo();
	}

	/**
	 * 创建临时背景，没有备份
	 */
	protected void setTempForeBitmap(Bitmap tempForeBitmap) {
		if (null != tempForeBitmap) {
			recycleMBitmap();
			mBitmap = BitMapUtils.duplicateBitmap(tempForeBitmap);
			if (null != mBitmap && null != mCanvas) {
				mCanvas.setBitmap(mBitmap);
				invalidate();
			}
		}
	}

	/**
	 * 创建画布上的背景图片，同时留有备份
	 */
	public void setForeBitMap(Bitmap bitmap) {
		if (bitmap != null) {
			recycleMBitmap();
			recycleOrgBitmap();
		}
		mBitmap = BitMapUtils.duplicateBitmap(bitmap, getWidth(), getHeight());
		mOrgBitMap = BitMapUtils.duplicateBitmap(mBitmap);
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
		invalidate();
	}

	/**
	 * 重置状态
	 */
	public void resetState() {
		setCurrentPainterType(PEN_TYPE.PLAIN_PEN);
		setPenColor(DEFAULT.PEN_COLOR);
		setBackGroundColor(DEFAULT.BACKGROUND_COLOR);
		mUndoStack.clearAll();
	}

	//对bitmap进行放大操作
	public Bitmap big(Bitmap bitmap){
		Matrix matrix = new Matrix();
		float w = bitmapw/bitmap.getWidth();
		float h = bitmaph/bitmap.getHeight();
		matrix.postScale(w, h);
		Bitmap bigBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return bigBitmap;
	}

	//对bitmap进行缩小操作
	public Bitmap small(Bitmap bitmap){
		Matrix matrix = new Matrix();
		float w = bitmap.getWidth()/bitmapw;
		float h = bitmap.getHeight()/bitmaph;
		matrix.postScale(w, h);
		Bitmap bigBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return bigBitmap;
	}

	/*
	 * ===================================内部类开始=================================
	 * 内部类，负责undo、redo
	 */
	public class PaintPadUndoStack {
		private int m_stackSize = 0;
		private PaintView mPaintView = null;
		private ArrayList<ToolInterface> mUndoStack = new ArrayList<ToolInterface>();
		private ArrayList<ToolInterface> mRedoStack = new ArrayList<ToolInterface>();
		private ArrayList<ToolInterface> mOldActionStack = new ArrayList<ToolInterface>();

		public PaintPadUndoStack(PaintView paintView, int stackSize) {
			mPaintView = paintView;
			m_stackSize = stackSize;
		}

		/**
		 * 将painter存入栈中
		 */
		public void push(ToolInterface penTool) {
			if (null != penTool) {
				// 如果undo已经存满
				if (mUndoStack.size() == m_stackSize && m_stackSize > 0) {

					System.out.println("1");
					// 得到最远的画笔
					ToolInterface removedTool = mUndoStack.get(0);
					// 所有的笔迹增加
					mOldActionStack.add(removedTool);
					mUndoStack.remove(0);
					System.out.println("m_stackSize==" + m_stackSize);
				}

				mUndoStack.add(penTool);
				System.out.println("mUndoStack.size==" + mUndoStack.size());
			}
		}

		/**
		 * 清空所有
		 */
		public void clearAll() {
			mRedoStack.clear();
			mUndoStack.clear();
			mOldActionStack.clear();
		}

		/**
		 * undo
		 */
		public void undo() {
			if (canUndo() && null != mPaintView) {
				ToolInterface removedTool = mUndoStack
						.get(mUndoStack.size() - 1);
				mRedoStack.add(removedTool);
				mUndoStack.remove(mUndoStack.size() - 1);

				if (null != mOrgBitMap) {
					// Set the temporary fore bitmap to canvas.
					// 当载入文件时保存了一份,现在要重新绘制出来
					mPaintView.setTempForeBitmap(mPaintView.mOrgBitMap);
				} else {
					// 如果背景不存在，则重新创建一份背景
					mPaintView.creatCanvasBitmap(mPaintView.mBitmapWidth,
							mPaintView.mBitmapHeight);
				}

				Canvas canvas = mPaintView.mCanvas;

				// First draw the removed tools from undo stack.
				for (ToolInterface paintTool : mOldActionStack) {
					paintTool.draw(canvas);
				}

				for (ToolInterface paintTool : mUndoStack) {
					paintTool.draw(canvas);
				}

				mPaintView.invalidate();
			}
		}

		/**
		 * redo
		 */
		public void redo() {
			if (canRedo() && null != mPaintView) {
				ToolInterface removedTool = mRedoStack
						.get(mRedoStack.size() - 1);
				mUndoStack.add(removedTool);
				mRedoStack.remove(mRedoStack.size() - 1);

				if (null != mOrgBitMap) {
					// Set the temporary fore bitmap to canvas.
					mPaintView.setTempForeBitmap(mPaintView.mOrgBitMap);
				} else {
					// Create a new bitmap and set to canvas.
					mPaintView.creatCanvasBitmap(mPaintView.mBitmapWidth,
							mPaintView.mBitmapHeight);
				}

				Canvas canvas = mPaintView.mCanvas;
				// 所有以前的笔迹都存放在removedStack中
				// First draw the removed tools from undo stack.
				for (ToolInterface sketchPadTool : mOldActionStack) {
					sketchPadTool.draw(canvas);
				}
				// 不管怎样都是从撤销里面绘制，重做只是暂时的存储
				for (ToolInterface sketchPadTool : mUndoStack) {
					sketchPadTool.draw(canvas);
				}
				mPaintView.invalidate();
			}
		}

		public boolean canUndo() {
			return (mUndoStack.size() > 0);
		}

		public boolean canRedo() {
			return (mRedoStack.size() > 0);
		}

		public void clearRedo() {
			mRedoStack.clear();
		}

		@Override
		public String toString() {
			return "canUndo" + canUndo();
		}
	}
	/* ==================================内部类结束 ================================= */

}
