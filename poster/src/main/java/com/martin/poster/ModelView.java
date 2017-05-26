package com.martin.poster;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Martin on 2016/7/23 0023.
 */
public class ModelView extends View {

    private Model model;
    private boolean isFirstDraw = true;
    private float viewRatio = 1.0f;
    private Bitmap result;

    public ModelView(Context context) {
        super(context);
    }

    public ModelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setModel(Model model) {
        this.model = model;
        this.model.bindView(this);
        invalidate();
    }

    /**
     * requestLayout
     * @author Reepicheep
     * Created at 2017/5/11 13:25
     */
    public void setModel(Model model,float viewRatio) {
        this.viewRatio = viewRatio;
        this.model = model;
        this.model.bindView(this);
        requestLayout();
    }


    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (viewRatio != 0) {
            setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
            int childWidthSize = getMeasuredWidth();
            int childHeightSize = getMeasuredHeight();
            int scale = childHeightSize / widthMeasureSpec;
            if (viewRatio > scale) {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childHeightSize / viewRatio * 1.0f), MeasureSpec.EXACTLY);
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, MeasureSpec.EXACTLY);

            } else {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
                heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childWidthSize * viewRatio * 1.0f), MeasureSpec.EXACTLY);
            }
            //按比例修改宽高
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null != model) {
            if (getWidth() != 0) {
                if (isFirstDraw) {
                    model.setDrawWidth(getWidth());
                    isFirstDraw = false;
                }
                model.draw(canvas);
            }
        }

    }

    /**
     * for zooming
     * @author Reepicheep
     * Created at 2017/5/24 15:05
     */
    public void resetFirstDraw(){
        isFirstDraw = true;
    }


    public void destoryLayers() {
        if (null != model) {
            model.destroyLayer();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != model) {
            boolean b = model.onTouchEvent(event);
            invalidate();
            return b;
        }

        return super.onTouchEvent(event);
    }

    public void releaseAllFocus(){
        model.releaseAllFocus();
        this.invalidate();
    }

    public Bitmap getResult() {
        model.releaseAllFocus();//去除所有焦点，并刷新视图
        this.invalidate();
        result = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        this.draw(canvas);
        return result;
    }

    public void setOnLayerSelectListener(OnLayerSelectListener onLayerSelectListener) {
        if (null != model) {
            model.setOnLayerSelectListener(onLayerSelectListener);
        }
    }
}
