package com.martin.poster;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Martin on 2016/8/24 0024.
 */
public class PosterView extends RelativeLayout {

    private final String TAG = getClass().getSimpleName();

    private ModelView modelView;
    private LayoutParams modelParams;
    private View menu;
    private LayoutParams menuParams;
    private int height, width;

    public PosterView(Context context) {
        super(context);
        viewInit();
    }

    public PosterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        viewInit();
    }

    private void viewInit() {
        modelView = new ModelView(getContext());
//        modelView.setBackgroundColor(Color.RED);
        modelParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(modelView, modelParams);
    }

    /**
     * 滤镜菜单初始化,并设置宽高
     *
     * @param menu
     * @param menuWidth
     */
    public void addMenuInit(View menu, int menuWidth, int menuHeight) {
        this.menu = menu;
        menuParams = new LayoutParams(menuWidth, menuHeight);
        addView(menu, menuParams);
        menu.setVisibility(GONE);
    }

    public void dissMenu() {
        if (null != menu)
            menu.setVisibility(GONE);
    }

    public void showMenu(Layer layer) {
        Layer.MenuPoint menuPoint = layer.getFrontMenuPoint(getHeight(), menuParams.height);
        PointF pointf = menuPoint.point;
        if (pointf.x + menuParams.width >= width) {
            pointf.x = width - menuParams.width;
        }
        if (menuPoint.direction == 1) {
            pointf.y = pointf.y - menuParams.height;
        }
        menuParams.setMargins((int) pointf.x, (int) pointf.y, 0, 0);
        menu.setLayoutParams(menuParams);
        if (null != menu)
            menu.setVisibility(VISIBLE);

    }

    public void setModel(Model model) {
        if (null != modelView) {
            modelView.setModel(model);
        }
    }

    /**
     * requestLayout
     * @author Reepicheep
     * Created at 2017/5/11 13:26
     */
    public void setModel(Model model,float ratio) {
        if (null != modelView) {
            modelView.setModel(model,ratio);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        width = getWidth();
        height = getHeight();
    }


    public ModelView getModelView() {
        return modelView;
    }

    public Bitmap getResult() {
        modelView.releaseAllFocus();
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        this.draw(canvas);
        return  bitmap;
//        return modelView.getResult();
    }

    /**
     * for zooming
     * @author Reepicheep
     * Created at 2017/5/25 13:16
     */
    public void  reset(){
        removeAllViews();
        viewInit();
    }

}
