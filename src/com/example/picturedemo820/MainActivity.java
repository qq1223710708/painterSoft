package com.example.picturedemo820;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.example.picturedemo820.util.Const;
import com.example.picturedemo820.util.MyUtil;
import com.example.picturedemo820.util.NetworkUtils;

import com.example.picturedemo820.PaintView.BitMapUtils;
import com.example.picturedemo820.PaintView.ColorPickerDialog;
import com.example.picturedemo820.PaintView.PaintView;
import com.example.picturedemo820.PaintView.SDCardFiles;
import com.example.picturedemo820.PaintView.ColorPickerDialog.OnColorChangedListener;
import com.example.picturedemo820.Painttools.PaintConstants.DEFAULT;
import com.example.picturedemo820.Painttools.PaintConstants.PEN_TYPE;
import com.example.picturedemo820.Thread.UploadThread;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
//        implements OnClickListener
{
    private static final String TAG = MainActivity.class.getSimpleName();

	private PaintView mPaintView = null;

    public static final int LOAD_REQUEST_CODE = 7201;
    private static final int LOAD_PIC_REQUEST_CODE = 720;


	private int currentColor;
	private int getpaintalpha = 255;

	// 通过控制Layout来控制某些变化
	private LinearLayout paintViewLayout = null;

	// Seekbar
	private SeekBar seekbar = null;
	// TextView
	private TextView showpaintalpha = null;
	private TextView showpaintalpha1 = null;

	// button
	private Button pen,
			fillcolor, eraser, clearScreen, redraw, constituency,
			cancel, save, open, alphaBtn,
			greyScale, upTaskBtn, findtask, mine,mainLayer,
			Share,addLayer;
	private Button enlargePicBtn, shrinkPic,
			picEffictivenessBtn, filleColorBtn,
			rotatePicBtn, reflectionPicBtn;
	private Button importBtn;
	
	//ListView
	//图层配置信息
	public List<Map<String,Object>> list_layer = new ArrayList<Map<String,Object>>();
	private ListView layershow;
	MyAdapter adapter;
	

	// RadioGroup
	private RadioGroup penShape = null;
	private RadioGroup penSize = null;
	private RadioGroup eraserSize = null;

	// LinearLayout
	private LinearLayout penSelect = null;			// 画笔选择
	private LinearLayout penSizeSelect = null;		// 画笔尺寸
	private LinearLayout eraserSizeSelect = null;		// 橡皮擦尺寸
	private LinearLayout top = null;
	private LinearLayout penSetAlphaNumber = null;		// 画笔alpha
	private LinearLayout PicEffictivenessLinearLayout = null;		// 绘图区域
	private LinearLayout LinearLayoutLayer = null;    //图层界面	// 下拉框区域

	boolean existsSamePicName = false;
	
	int x, y;

    private SeekBar.OnSeekBarChangeListener aplhaNumberChangerListener =  new AlphaNumberSeekBarChangeListener();
    private OnClickListener screentClickListener = new ScreenClickListener();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}




	public void init() {
		mPaintView = new PaintView(this);

		penSelect = (LinearLayout) findViewById(R.id.penSelect);
		penSizeSelect = (LinearLayout) findViewById(R.id.penSizeSelect);
		top = (LinearLayout) findViewById(R.id.top);
		penSetAlphaNumber = (LinearLayout) findViewById(R.id.penSetAlphaNumber);
		PicEffictivenessLinearLayout = (LinearLayout) findViewById(R.id.PicEffictivenessLinearLayout);
		eraserSizeSelect = (LinearLayout) findViewById(R.id.eraserSizeSelect);
		LinearLayoutLayer = (LinearLayout) findViewById(R.id.LinearLayoutLayer);

		penShape = (RadioGroup) findViewById(R.id.penShape);
		penShape.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				setPaintTypeChoice(checkedId);
			}
		});

		penSize = (RadioGroup) findViewById(R.id.penSize);
		penSize.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				setPaintSizeChoice(checkedId);
			}
		});

		eraserSize = (RadioGroup) findViewById(R.id.eraserSize);
		eraserSize.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				setEraseSizeChoice(checkedId);
			}
		});



		pen = (Button) findViewById(R.id.pen);
		pen.setOnClickListener(screentClickListener);

		paintViewLayout = (LinearLayout) findViewById(R.id.paintViewLayout);
		paintViewLayout.addView(mPaintView);

		fillcolor = (Button) findViewById(R.id.fillcolor);
		fillcolor.setOnClickListener(screentClickListener);

		eraser = (Button) findViewById(R.id.eraser);
		eraser.setOnClickListener(screentClickListener);

		clearScreen = (Button) findViewById(R.id.clearsceen);
		clearScreen.setOnClickListener(screentClickListener);

		constituency = (Button) findViewById(R.id.constituency);
		constituency.setOnClickListener(screentClickListener);

		cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(screentClickListener);

		redraw = (Button) findViewById(R.id.redraw);
		redraw.setOnClickListener(screentClickListener);

		save = (Button) findViewById(R.id.save);
		save.setOnClickListener(screentClickListener);

		open = (Button) findViewById(R.id.open);
		open.setOnClickListener(screentClickListener);

		seekbar = (SeekBar) findViewById(R.id.psetalpha);
		seekbar.setOnSeekBarChangeListener(aplhaNumberChangerListener);

		showpaintalpha = (TextView) findViewById(R.id.showpaintalpha);
		showpaintalpha.setText("0%");

		alphaBtn = (Button) findViewById(R.id.paintsetalpha);
		alphaBtn.setOnClickListener(screentClickListener);


		upTaskBtn = (Button) findViewById(R.id.Uptask);
		upTaskBtn.setOnClickListener(screentClickListener);

		findtask = (Button) findViewById(R.id.Findtask);
		findtask.setOnClickListener(screentClickListener);

		mine = (Button) findViewById(R.id.Mine);
		mine.setOnClickListener(screentClickListener);

		picEffictivenessBtn = (Button) findViewById(R.id.PicEffictiveness);
		picEffictivenessBtn.setOnClickListener(screentClickListener);
		
		mainLayer = (Button) findViewById(R.id.mainLayer);
		mainLayer.setOnClickListener(screentClickListener);
		
		layershow = (ListView) findViewById(R.id.layershow);  //显示图层信息，，，，删除
		getData();
		adapter = new MyAdapter(this);
		layershow.setAdapter(adapter);
		layershow.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println("position=="+position);
				mPaintView.setOnClickListViewPosition(position);
			}
		});
		
		layershow.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				menu.add(0, 0, 0, "删除该图层？");
				menu.add(0,1,0,"更改透明度");
			}
		});
		
		
		addLayer = (Button) findViewById(R.id.addLayer);
		addLayer.setOnClickListener(screentClickListener);
		
		Share = (Button) findViewById(R.id.Share);	//共享按钮，即共享本地画板内容到蓝牙匹配的机器上
		Share.setOnClickListener(screentClickListener);
		
		importBtn = (Button) findViewById(R.id.daoru);
		importBtn.setOnClickListener(screentClickListener);

		filleColorBtn = (Button) findViewById(R.id.FilleColor);
		filleColorBtn.setOnClickListener(screentClickListener);

		enlargePicBtn = (Button) findViewById(R.id.EnlargePic);
		enlargePicBtn.setOnClickListener(screentClickListener);

		shrinkPic = (Button) findViewById(R.id.ShrinkPic);
		shrinkPic.setOnClickListener(screentClickListener);

		rotatePicBtn = (Button) findViewById(R.id.RotatePic);
		rotatePicBtn.setOnClickListener(screentClickListener);

		reflectionPicBtn = (Button) findViewById(R.id.ReflectionPic);
		reflectionPicBtn.setOnClickListener(screentClickListener);

		greyScale = (Button) findViewById(R.id.Greyscale);
		greyScale.setOnClickListener(screentClickListener);

	}

	
	@Override
	public boolean onContextItemSelected(MenuItem item){
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		//info.id得到listview中选择的条目绑定的id
        int id1= info.position;
        Log.d(TAG, "position: " + info.position + "; itemId: " + item.getItemId());
        switch(item.getItemId()){
            case 0:
                deleteMapLayerDialog(id1);
                return true;
            case 1:
                changeMapLayerAlphaDialog(id1);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
	}
	
	public void deleteMapLayerDialog(final int id){
		AlertDialog.Builder builder1 = new Builder(MainActivity.this);
		builder1.setTitle("确定要删除图层吗？");
		builder1.setPositiveButton("确定",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mPaintView.deleteListViewPositionLayer(id);
				adapter.notifyDataSetChanged();
			}
		});
		
		builder1.setNegativeButton("取消", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder1.create().show();
	}
	
	public void changeMapLayerAlphaDialog(final int id){
		AlertDialog.Builder builder2 = new Builder(MainActivity.this);
		LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
		final View view = layoutInflater.inflate(R.layout.toumingdu, null);

		builder2.setView(view);
		final SeekBar psetalpha1 = (SeekBar) view.findViewById(R.id.psetalpha1); 
		psetalpha1.setOnSeekBarChangeListener(aplhaNumberChangerListener);
		showpaintalpha1 = (TextView) view.findViewById(R.id.showpaintalpha1);
		showpaintalpha1.setText("透明度： 0%");
		
		builder2.setTitle("更改图层的透明度");
		builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(MainActivity.this, "更改图层的透明度", Toast.LENGTH_LONG).show();
				mPaintView.setmCurrentBitmapAlpha(getpaintalpha, id);
				adapter.notifyDataSetChanged();
			}
		});
		
		builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
		builder2.create().show();
	}




	/*
	 * 如果是选区模式下 在点击任何一个按钮后 选区消失
	 */
	private void rebackifchoice() {
		if (mPaintView.getCurretnPenType() == PEN_TYPE.CONSTITUENCY) {
			mPaintView.clearConstituency();
		}
	}



	//  让其他布局都隐藏，显示自己的布局
	public void hideotherlayout(LinearLayout l) {
		switch (l.getId()) {
		case R.id.penSetAlphaNumber:
			penSelect.setVisibility(View.INVISIBLE);
			penSizeSelect.setVisibility(View.INVISIBLE);
			PicEffictivenessLinearLayout.setVisibility(View.INVISIBLE);
			LinearLayoutLayer.setVisibility(View.INVISIBLE);
			eraserSizeSelect.setVisibility(View.INVISIBLE);
			break;
		case R.id.penSelect://选择画笔类型，例如曲线
			penSetAlphaNumber.setVisibility(View.INVISIBLE);
			PicEffictivenessLinearLayout.setVisibility(View.INVISIBLE);
			LinearLayoutLayer.setVisibility(View.INVISIBLE);
			eraserSizeSelect.setVisibility(View.INVISIBLE);
			break;
		case R.id.PicEffictivenessLinearLayout:
			penSelect.setVisibility(View.INVISIBLE);
			penSizeSelect.setVisibility(View.INVISIBLE);
			penSetAlphaNumber.setVisibility(View.INVISIBLE);
			LinearLayoutLayer.setVisibility(View.INVISIBLE);
			eraserSizeSelect.setVisibility(View.INVISIBLE);
			break;
		case R.id.eraserSizeSelect:
			penSelect.setVisibility(View.INVISIBLE);
			penSizeSelect.setVisibility(View.INVISIBLE);
			penSetAlphaNumber.setVisibility(View.INVISIBLE);
			penSetAlphaNumber.setVisibility(View.INVISIBLE);
			LinearLayoutLayer.setVisibility(View.INVISIBLE);
			break;
		case R.id.LinearLayoutLayer:
			penSelect.setVisibility(View.INVISIBLE);
			penSizeSelect.setVisibility(View.INVISIBLE);
			PicEffictivenessLinearLayout.setVisibility(View.INVISIBLE);
			penSetAlphaNumber.setVisibility(View.INVISIBLE);
			eraserSizeSelect.setVisibility(View.INVISIBLE);
			break;
		}
	}

	// 隐藏按钮的全部布局
	public void hideallLayout() {
		penSelect.setVisibility(View.INVISIBLE);
		penSizeSelect.setVisibility(View.INVISIBLE);
		PicEffictivenessLinearLayout.setVisibility(View.INVISIBLE);
		penSetAlphaNumber.setVisibility(View.INVISIBLE);
		eraserSizeSelect.setVisibility(View.INVISIBLE);
		LinearLayoutLayer.setVisibility(View.INVISIBLE);
	}


	//获取listview中的数据
	public void getData(){
		list_layer = mPaintView.layerList;
	}




	//删除bitmap文件夹
	public void deleteBitmapfile(String sdDir){
		
		File bitmapfile = new File(sdDir);
		if(bitmapfile.isDirectory()){
			File[] childFiles = bitmapfile.listFiles();
			if(childFiles==null || childFiles.length ==0){
				bitmapfile.delete();
				return;
			}
			for(int i=0;i<childFiles.length;i++){
				childFiles[i].delete();
			}
			bitmapfile.delete();
		}
	}


	/**
	 *载入之后得到路径
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 720:
			if (data != null) {
				Uri uri = data.getData();
				ContentResolver cr = this.getContentResolver();
				try {
					Bitmap bitmap;
					BitmapFactory.Options op = new BitmapFactory.Options();
					op.inJustDecodeBounds = true;
					BitmapFactory.decodeStream(cr.openInputStream(uri), null,
							op);
					int wRatio = (int) Math.ceil(op.outWidth
							/ (float) mPaintView.getWidth());
					int hRatio = (int) Math.ceil(op.outHeight
							/ (float) mPaintView.getHeight());
					// 如果超出指定大小，则缩小相应的比例
					if (wRatio > 1 && hRatio > 1) {
						if (wRatio > hRatio) {
							op.inSampleSize = wRatio;
						} else {
							op.inSampleSize = hRatio;
						}
					}
					op.inJustDecodeBounds = false;
					bitmap = BitmapFactory.decodeStream(
							cr.openInputStream(uri), null, op);
					bitmap = BitmapFactory
							.decodeStream(cr.openInputStream(uri));
					mPaintView.setForeBitMap(bitmap);
					mPaintView.resetState();
					// upDateUndoRedo();
					if (bitmap != null && !bitmap.isRecycled()) {
						bitmap.recycle();
						bitmap = null;
					}
				} catch (Exception e) {
					return;
				}
			}
			break;
		case LOAD_REQUEST_CODE:
			if (data != null) {
				Uri uri = data.getData();
				ContentResolver cr = this.getContentResolver();
				try {
					System.out.println(107);

					Bitmap bitmap;
					BitmapFactory.Options op = new BitmapFactory.Options();
					op.inJustDecodeBounds = true;
					BitmapFactory.decodeStream(cr.openInputStream(uri), null,
							op);
					/*
					 * int wRatio = (int) Math.ceil(op.outWidth / (float)
					 * mPaintView.getWidth()); int hRatio = (int)
					 * Math.ceil(op.outHeight / (float) mPaintView.getHeight());
					 * // 如果超出指定大小，则缩小相应的比例 if (wRatio > 1 && hRatio > 1) { if
					 * (wRatio > hRatio) { op.inSampleSize = wRatio; } else {
					 * op.inSampleSize = hRatio; } } op.inJustDecodeBounds =
					 * false; bitmap = BitmapFactory.decodeStream(
					 * cr.openInputStream(uri), null, op);
					 */
					bitmap = BitmapFactory
							.decodeStream(cr.openInputStream(uri));
					Matrix matrix = new Matrix();
					// float width = mPaintView.getWidth();
					float scaleWidth = 0.0f;
					float scaleHeight = 0.0f;
					float ms = 0.0f;
					if (op.outWidth > mPaintView.getWidth()) {
						scaleWidth = (int) Math.ceil(op.outWidth
								/ (mPaintView.getWidth()));
						scaleHeight = (int) Math.ceil(op.outHeight
								/ (mPaintView.getHeight()));

						ms = (Math.max(scaleWidth, scaleHeight)) / 3;
						// ms = 0.1f;
					}

					System.out.println(108);
					System.out.println("(float)scaleWidth=="
							+ (float) scaleWidth);
					System.out.println(" (float)ms==" + (float) ms);
					matrix.setScale(ms, ms);
					// matrix.setScale(10.0, 10.0);
					Bitmap picBitmap = Bitmap.createBitmap(bitmap, 0, 0,
							op.outWidth, op.outHeight, matrix, true);
					mPaintView.setForePicpaintBitMap(picBitmap);

					System.out.println("picBitmap===" + picBitmap);

					// mPaintView.resetState();
					// upDateUndoRedo();
					if (bitmap != null && !bitmap.isRecycled()) {
						bitmap.recycle();
						bitmap = null;
					}
				} catch (Exception e) {
					return;
				}
			}
			break;
		default:
			break;
		}
	}






    private void setPaintTypeChoice(int checkedId) {
        if(Const.PEN_TYPE_CHOICE.containsKey(checkedId)) {
            Integer painterType = Const.PEN_TYPE_CHOICE.get(checkedId).get(Const.CURRENT_PAINTER_TYPE);
            Integer shapeType = Const.PEN_TYPE_CHOICE.get(checkedId).get(Const.CURRENT_SHAPE_TYPE);

            rebackifchoice();
            if(checkedId == R.id.paintPicpaint) {
                hideallLayout();
                mPaintView.setCurrentPainterType(painterType);
                onClickButtonPicpaint();
            } else {
                mPaintView.setCurrentPainterType(painterType);
                mPaintView.setCurrentShapType(shapeType);
            }
        }
    }
    private void setPaintSizeChoice(int checkedId) {
        if(Const.PEN_SIZE_CHOICE.containsKey(checkedId)) {
            mPaintView.setPenSize(Const.PEN_SIZE_CHOICE.get(checkedId));
        }
    }
    private void setEraseSizeChoice(int checkId) {
        if (Const.ERASE_SIZE_CHOICE.containsKey(checkId)) {
            mPaintView.setEraserSize(Const.ERASE_SIZE_CHOICE.get(checkId));
        }
    }



    // 选择图片作为他的画笔
    public void onClickButtonPicpaint() {
//        sendUpdateBroadCast();
        MyUtil.sendUpdateBroadCast(MainActivity.this);
//		startLoadpicpaint();
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, LOAD_REQUEST_CODE);
    }



    /*
     *  当图片画笔抬起的时候，显示图片的效果布局
     */
    public void onClickPicWhenUpPicpaint(int x, int y) {
        PicEffictivenessLinearLayout.setVisibility(View.VISIBLE);
        MainActivity.this.x = x;
        MainActivity.this.y = y;
    }








    public class MyAdapter extends BaseAdapter{

        private LayoutInflater mInflater;

        public MyAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list_layer.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if(convertView==null){
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.layer_list, null);
                holder.img = (ImageView)convertView.findViewById(R.id.layer_image);
                holder.layername = (TextView)convertView.findViewById(R.id.layer_name);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder)convertView.getTag();
            }

            holder.img.setImageBitmap((Bitmap) list_layer.get(position).get("layer_image"));
            holder.layername.setText((String) list_layer.get(position).get("layer_name"));

            return convertView;
        }
    }

    public final class ViewHolder{
        public ImageView img;
        public TextView layername;
    }



    private class ScreenClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pen:
                    onClickPenListener();
                    break;
                case R.id.fillcolor:
                    onClickButtonColorSelect();
                    break;
                case R.id.eraser:
                    onClickEraserListener();
                    mPaintView.setCurrentPainterType(PEN_TYPE.ERASER);
                    break;
                case R.id.clearsceen:
                    hideallLayout();
                    mPaintView.clearSceen();
                    break;
                case R.id.constituency:
                    hideallLayout();
                    rebackifchoice();
                    mPaintView.setCurrentPainterType(PEN_TYPE.CONSTITUENCY);
                    break;
                case R.id.cancel:
                    hideallLayout();
                    rebackifchoice();
                    mPaintView.undo();
                    break;
                case R.id.redraw:
                    rebackifchoice();
                    hideallLayout();
                    mPaintView.redo();
                    break;
                case R.id.save:
                    hideallLayout();
                    rebackifchoice();
                    onClickSaveButton();
                    break;
                case R.id.open:
                    hideallLayout();
                    rebackifchoice();
                    onClickButtonLoad();
                    break;
                case R.id.paintsetalpha:
                    rebackifchoice();
                    onClickPaintListener();
                    break;
                case R.id.PicEffictiveness:
                    if (mPaintView.getCurretnPenType() == PEN_TYPE.CONSTITUENCY) {
                        mPaintView.PicpaintBitmap = mPaintView.conBitmap;
                        mPaintView.setCurrentPainterType(PEN_TYPE.PICPAINT);
                    }
                    onClickPicEffictiveness();
                    break;
                case R.id.FilleColor:
                    rebackifchoice();
                    hideallLayout();
                    onClickButtonFilleColorSelect();
                    break;

                case R.id.Share:
                    onClickButtonShare();
                    break;

                case R.id.daoru:
                    onClickButtonDaoru();
                    break;

                case R.id.mainLayer:  //显示图层界面按钮
                    onClickButtonMainLayer();
                    break;
                case R.id.addLayer:
                    addLayerButtonClick();
                    break;

                case R.id.Uptask:  // 上传按作业按钮
                    rebackifchoice();
                    hideallLayout();
                    onClickButtonUptask();
                    break;
                case R.id.Findtask:
                    rebackifchoice();
                    hideallLayout();
                    onClickButtonFindtask();
                    break;
                case R.id.Mine:
                    rebackifchoice();
                    hideallLayout();
                    onClickButtonMine();
                    break;

                case R.id.EnlargePic:
                    onClickEnlargePic();
                    break;
                case R.id.ShrinkPic:
                    onClickShrinkPicPic();
                    break;
                case R.id.RotatePic:
                    onClickRotatePicPic();
                    break;
                case R.id.ReflectionPic:

                    onClickReflectionPicPic();
                    break;
                case R.id.Greyscale:
                    //onClickGreyscale();
                    break;
            }
        }

        //添加图层按钮事件
        public void addLayerButtonClick(){
            mPaintView.addNewLayer();
            adapter.notifyDataSetChanged();
        }

        //图层按钮
        public void onClickButtonMainLayer(){
            rebackifchoice();
            hideotherlayout(LinearLayoutLayer);
            if(LinearLayoutLayer.isShown()){
                LinearLayoutLayer.setVisibility(View.INVISIBLE);
            }else{
                LinearLayoutLayer.setVisibility(View.VISIBLE);
            }

        }

        /*
         * 图片放大按钮按下后
         */
        public void onClickEnlargePic() {
            if (mPaintView.getCurretnPenType() == PEN_TYPE.PICPAINT) {
                mPaintView.ChangeSizeBigOnPicpaint(x, y);
            }
        }

        /*
         *图片缩小按钮按下后
         *
         */
        public void onClickShrinkPicPic() {
            mPaintView.ChangeSizeSmallOnPicpaint(x, y);
        }

        /*
         *图片旋转按钮按下后
         */
        public void onClickRotatePicPic() {
            mPaintView.RotateOnPicpaint(x, y);
        }

        /*
         * 图片倒影按钮按下后
         */
        public void onClickReflectionPicPic() {
            mPaintView.ReflectionOnPicpaint(x, y);
        }

        // 上传作业按钮
        public void onClickButtonUptask() {

            if (!NetworkUtils.isNetworkAvailable(MainActivity.this)) {
                Toast.makeText(MainActivity.this, "当前没有可用网络！", Toast.LENGTH_LONG).show();
                return;
            } else if(!NetworkUtils.serverCanConnect()) {
                Toast.makeText(MainActivity.this, "服务器出现了一些问题，暂时不能上传", Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            AlertDialog.Builder builer = new AlertDialog.Builder(MainActivity.this);
            builer.setTitle("是否将提交本次作业");
            builer.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                    Date curdate = new Date(System.currentTimeMillis());
                    String filename = formatter.format(curdate);

                    Log.d(TAG, "upload task filename: " + filename);

//                    String sdDir = getDirPath();
                    String sdDir = MyUtil.getSaveDirPath(MainActivity.this);
                    String file = sdDir + filename + ".jpg";
                    Bitmap bitmap = mPaintView.getSnapShoot();
                    BitMapUtils.saveToSdCard(file, bitmap);
                    Toast.makeText(MainActivity.this, "保存图片成功", Toast.LENGTH_LONG)
                            .show();
                    MyUtil.sendUpdateBroadCast(MainActivity.this);


                    File mFile = new File(file);

                    String username = "xh";

                    UploadThread upload = new UploadThread(MainActivity.this, mFile,username);
                    Thread uploadthread = new Thread(upload);
                    uploadthread.start();
                }
            });
            builer.setNegativeButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builer.create().show();

        }

        // 查找作业按钮
        public void onClickButtonFindtask() {
            if(NetworkUtils.isNetworkAvailable(MainActivity.this)){
                Intent intent = new Intent();
                intent.setClassName(MainActivity.this,
                        com.example.picturedemo820.FindTask.class.getName());
//					"com.example.picturedemo820.FindTask");
                startActivity(intent);
            }else{
                Toast.makeText(MainActivity.this, "当前没有可用网络",
                        Toast.LENGTH_LONG).show();
            }
        }

        // 我的
        public void onClickButtonMine() {

        }

        /**
         * 载入图片
         */
        private void onClickButtonLoad() {
            // 点击Load时要对数据库进行更新
            MyUtil.sendUpdateBroadCast(MainActivity.this);
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 720);
        }

        /*
 * 图片效果按钮
 */
        public void onClickPicEffictiveness() {
            if (PicEffictivenessLinearLayout.isShown()) {
                PicEffictivenessLinearLayout.setVisibility(View.INVISIBLE);
            } else {
                hideotherlayout(PicEffictivenessLinearLayout);
                PicEffictivenessLinearLayout.setVisibility(View.VISIBLE);
            }
        }



        /**
         * color选择界面 如果是Eraser则改为前一个画笔
         */
        private void onClickButtonColorSelect() {
            rebackifchoice();
            new ColorPickerDialog(MainActivity.this, new OnColorChangedListener() {
                @Override
                public void colorChanged(int color) {
                    mPaintView.setPenColor(color);
//				currentColor = color;
                }
            }, mPaintView.getPenColor()).show();
        }

        /**
         * color选择界面 如果是Eraser则改为前一个画笔
         */
        private void onClickButtonFilleColorSelect() {
            new ColorPickerDialog(MainActivity.this, new OnColorChangedListener() {
                @Override
                public void colorChanged(int color) {
                    mPaintView.setFilleColor(color);
                    mPaintView.setCurrentPainterType(PEN_TYPE.FilleColor);
//				currentColor = color;
                }
            }, mPaintView.getFilleColor()).show();
        }

        // 显示显示画笔样式界面
        public void onClickPenListener() {
            rebackifchoice();
            mPaintView.setCurrentPainterType(PEN_TYPE.PLAIN_PEN);
            //pen.setBackgroundColor(Color.RED);
            hideotherlayout(penSelect);
            if (penSelect.isShown() && penSizeSelect.isShown()) {
                penSelect.setVisibility(View.INVISIBLE);
                penSizeSelect.setVisibility(View.INVISIBLE);
            } else {
                penSelect.setVisibility(View.VISIBLE);
                penSizeSelect.setVisibility(View.VISIBLE);
            }
        }

        // 显示橡皮样式界面
        public void onClickEraserListener() {
            rebackifchoice();
            hideotherlayout(eraserSizeSelect);
            if (eraserSizeSelect.isShown()) {
                eraserSizeSelect.setVisibility(View.INVISIBLE);
            } else {
                eraserSizeSelect.setVisibility(View.VISIBLE);
            }
        }

        //显示设置透明度界面
        public void onClickPaintListener() {
            if (penSetAlphaNumber.isShown()) {
                penSetAlphaNumber.setVisibility(View.INVISIBLE);
            } else {
                hideotherlayout(penSetAlphaNumber);
                penSetAlphaNumber.setVisibility(View.VISIBLE);
            }
        }

        //共享按钮事件
        public void onClickButtonShare(){
            hideallLayout();
//            String sdDir = getBitmapDirPath();
            String sdDir = MyUtil.getBitmapDirPath(MainActivity.this);
            deleteBitmapfile(sdDir);
            File sd = new File(sdDir);
            if(!sd.exists()){
                sd.mkdirs();
            }

            byte[] byte1 = null;
            Bitmap newbmp = Bitmap.createBitmap(DEFAULT.viewwidth, DEFAULT.viewheight, Config.ARGB_8888);
            Canvas cv = new Canvas(newbmp);
            for(int i=0;i<mPaintView.layerList.size();i++){
                cv.drawBitmap((Bitmap) mPaintView.layerList.get(i).get("layer_image"), 0, 0,null);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            newbmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte1 = baos.toByteArray();

            String picpath = "/mnt/sdcard/PictureDemo/Bitmap/图层-1101.txt";
            try {
                FileOutputStream fos = new FileOutputStream(picpath);
                fos.write(byte1);
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        //导入按钮事件
        public void onClickButtonDaoru(){
            mPaintView.setOtherCanvas();
            adapter.notifyDataSetChanged();
        }





        // 保存图片
        public void onClickSaveButton() {
            boolean sdCardIsMounted = android.os.Environment
                    .getExternalStorageState().equals(
                            android.os.Environment.MEDIA_MOUNTED);
            if (!sdCardIsMounted) {
                Toast.makeText(MainActivity.this, "请插入内存卡", Toast.LENGTH_SHORT)
                        .show();

            } else {
                AlertDialog.Builder builder = new Builder(MainActivity.this);
                builder.setTitle("是否保存图片？？？");
                final EditText editText = new EditText(MainActivity.this);
                builder.setView(editText);

                builder.setPositiveButton("确认",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String e1 = editText.getText().toString();
                                File mFile = new File(e1);
                                if (!MyUtil.isFileNameOk(mFile)) {
                                    Toast.makeText(MainActivity.this,
                                            "文件名不合法，请重新输入", Toast.LENGTH_LONG)
                                            .show();
                                    dialog.cancel();
                                }
                                if (SDCardFiles.fileNameExists(mFile + ".jpg")) {
                                    AlertDialog.Builder mbuilder = new Builder(
                                            MainActivity.this);
                                    mbuilder.setTitle("注意！！");
                                    mbuilder.setMessage("文件名与现有文件名重复  \n" +
                                            "是否覆盖已有文件");
                                    mbuilder.setPositiveButton("是",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int which) {
                                                    existsSamePicName = true;
                                                }
                                            });
                                    mbuilder.setNegativeButton("否",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int which) {
                                                    existsSamePicName = false;
                                                    dialog.cancel();
                                                    Toast.makeText(
                                                            MainActivity.this,
                                                            "请重新输入文件名",
                                                            Toast.LENGTH_LONG)
                                                            .show();
                                                }
                                            });
                                    mbuilder.create().show();
                                } else {
//                                    String sdDir = getDirPath();
                                    String sdDir = MyUtil.getSaveDirPath(MainActivity.this);
                                    String file = sdDir + e1 + ".jpg";
                                    Bitmap bitmap = mPaintView.getSnapShoot();
                                    BitMapUtils.saveToSdCard(file, bitmap);
                                    Toast.makeText(MainActivity.this, "保存图片成功",
                                            Toast.LENGTH_LONG).show();
//                                    sendUpdateBroadCast();
                                    MyUtil.sendUpdateBroadCast(MainActivity.this);
                                    dialog.cancel();
                                }
                                if (existsSamePicName = true) {
//                                    String sdDir = getDirPath();
                                    String sdDir = MyUtil.getSaveDirPath(MainActivity.this);
                                    String file = sdDir + e1 + ".jpg";
                                    Bitmap bitmap = mPaintView.getSnapShoot();
                                    BitMapUtils.saveToSdCard(file, bitmap);
                                    Toast.makeText(MainActivity.this, "保存图片成功",
                                            Toast.LENGTH_LONG).show();
//                                    sendUpdateBroadCast();
                                    MyUtil.sendUpdateBroadCast(MainActivity.this);
                                    dialog.cancel();
                                }
                                // dialog.cancel();
                            }
                        });
                builder.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.create().show();
            }
        }


    }


    public class AlphaNumberSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            getpaintalpha = seekBar.getProgress();
//        System.out.println("正在拖动,当前值:" + seekBar.getProgress() + "\n");
            showpaintalpha.setText(String.valueOf(getpaintalpha) + "%");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
//        System.out.println("开始拖动,当前值:" + seekBar.getProgress() + "\n");
            getpaintalpha = seekBar.getProgress();
            showpaintalpha.setText(String.valueOf(getpaintalpha) + "%");
        }


        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            switch(seekBar.getId()){
                case R.id.psetalpha:
                    showpaintalpha.setText(seekBar.getProgress() + "%");
                    getpaintalpha = calcuProgress(seekBar.getProgress());
                    mPaintView.setPaintAlpha(getpaintalpha);
                    break;
                case R.id.psetalpha1:
                    getpaintalpha = seekBar.getProgress();
                    showpaintalpha1.setText("透明度：  "+String.valueOf(getpaintalpha) + "%");
                    break;
            }
        }

        private int calcuProgress(int progress) {
            int val = 0;
            if (progress >= 90) {
                val =  255;
            } else if (progress >= 80) {
                val =  220;
            } else if (progress >= 70) {
                val =   200;
            } else if (progress >= 60) {
                val =  180;
            } else if (progress >= 50) {
                val = 150;
            } else if (progress >= 40) {
                val = 120;
            } else if (progress >= 30) {
                val =   90;
            } else if (progress >= 20) {
                val =   60;
            } else if (progress >= 10) {
                val =  30;
            } else if (progress < 10) {
                val =  0;
            }
            return val;
        }
    }
}
