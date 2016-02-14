package com.example.picturedemo820.util;

import com.example.picturedemo820.Painttools.PaintConstants;
import com.example.picturedemo820.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/13.
 */
public class Const {
    public static final String CURRENT_PAINTER_TYPE = "currentPainterType";
    public static final String CURRENT_SHAPE_TYPE = "currentShapeType";

    public static final Map<Integer, Integer> ERASE_SIZE_CHOICE = new HashMap<Integer, Integer>() {
        {
            put(R.id.eraserfive, PaintConstants.ERASER_SIZE.SIZE_1);
            put(R.id.eraserten, PaintConstants.ERASER_SIZE.SIZE_2);
            put(R.id.eraserfifteen, PaintConstants.ERASER_SIZE.SIZE_3);
            put(R.id.erasertwenty, PaintConstants.ERASER_SIZE.SIZE_4);
            put(R.id.eraserthirty, PaintConstants.ERASER_SIZE.SIZE_5);
        }
    };
    public static final Map<Integer, Integer> PEN_SIZE_CHOICE = new HashMap<Integer, Integer>() {
        {
            put(R.id.one, PaintConstants.PEN_SIZE.SIZE_1);
            put(R.id.five, PaintConstants.PEN_SIZE.SIZE_2);
            put(R.id.five, PaintConstants.PEN_SIZE.SIZE_3);
            put(R.id.ten, PaintConstants.PEN_SIZE.SIZE_4);
            put(R.id.fifteen, PaintConstants.PEN_SIZE.SIZE_5);
        }
    };
    public static final Map<Integer, Map<String, Integer>> PEN_TYPE_CHOICE = new HashMap<Integer, Map<String, Integer>>() {
        {
            put(R.id.Curve, new HashMap<String, Integer>() {
                {
                    put(CURRENT_PAINTER_TYPE, PaintConstants.PEN_TYPE.PLAIN_PEN);
                    put(CURRENT_SHAPE_TYPE, PaintConstants.SHAP.CURV);
                }
            });
            put(R.id.Circle, new HashMap<String, Integer>() {
                {
                    put(CURRENT_PAINTER_TYPE, PaintConstants.PEN_TYPE.PLAIN_PEN);
                    put(CURRENT_SHAPE_TYPE, PaintConstants.SHAP.CIRCLE);
                }
            });
            put(R.id.Line, new HashMap<String, Integer>() {
                {
                    put(CURRENT_PAINTER_TYPE, PaintConstants.PEN_TYPE.PLAIN_PEN);
                    put(CURRENT_SHAPE_TYPE, PaintConstants.SHAP.LINE);
                }
            });
            put(R.id.Square, new HashMap<String, Integer>() {
                {
                    put(CURRENT_PAINTER_TYPE, PaintConstants.PEN_TYPE.PLAIN_PEN);
                    put(CURRENT_SHAPE_TYPE, PaintConstants.SHAP.SQUARE);
                }
            });
            put(R.id.Rect, new HashMap<String, Integer>() {
                {
                    put(CURRENT_PAINTER_TYPE, PaintConstants.PEN_TYPE.PLAIN_PEN);
                    put(CURRENT_SHAPE_TYPE, PaintConstants.SHAP.RECT);
                }
            });
            put(R.id.Oval, new HashMap<String, Integer>() {
                {
                    put(CURRENT_PAINTER_TYPE, PaintConstants.PEN_TYPE.PLAIN_PEN);
                    put(CURRENT_SHAPE_TYPE, PaintConstants.SHAP.OVAL);
                }
            });
            put(R.id.paintPicpaint, new HashMap<String, Integer>() {
                {
                    put(CURRENT_PAINTER_TYPE, PaintConstants.PEN_TYPE.PICPAINT);
                }
            });
        }
    };

}
