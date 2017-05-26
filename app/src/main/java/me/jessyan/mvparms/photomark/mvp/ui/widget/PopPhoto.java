package me.jessyan.mvparms.photomark.mvp.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.BaseFragment;

import me.jessyan.mvparms.photomark.R;
import me.jessyan.mvparms.photomark.mvp.model.api.service.PhotoManager;


public class PopPhoto extends PopupWindow implements OnClickListener {

    private TextView takePhotoBtn, pickPhotoBtn, cancelBtn;
    private View mMenuView;
    private boolean isAvatar;

    private BaseActivity activity;
    private BaseFragment fragment;

    @SuppressLint("InflateParams")
    private PopPhoto(Context context){
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pop_photo, null);
        takePhotoBtn = (TextView) mMenuView.findViewById(R.id.pop_take_photo_tv);
        pickPhotoBtn = (TextView) mMenuView.findViewById(R.id.pop_album_tv);
        cancelBtn = (TextView) mMenuView.findViewById(R.id.pop_cancel_tv);

        this.setContentView(mMenuView);
        this.setClippingEnabled(false);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.PopupAnimation);
        ColorDrawable dw = new ColorDrawable(0x99000000);
        this.setBackgroundDrawable(dw);
        mMenuView.setOnTouchListener((v, event) -> {
            int height = mMenuView.findViewById(R.id.pop_layout).getTop();
            int y = (int) event.getY();
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (y < height) {
                    dismiss();
                }
            }
            return true;
        });
    }


    public PopPhoto(Context context, OnClickListener itemsOnClick) {
        this(context);
        cancelBtn.setOnClickListener(itemsOnClick);
        pickPhotoBtn.setOnClickListener(itemsOnClick);
        takePhotoBtn.setOnClickListener(itemsOnClick);
    }

    public PopPhoto(BaseActivity activity) {
        this(activity.getBaseContext());
        this.activity = activity;
        cancelBtn.setOnClickListener(this);
        pickPhotoBtn.setOnClickListener(this);
        takePhotoBtn.setOnClickListener(this);
    }

    public PopPhoto(BaseFragment fragment) {
        this(fragment.getContext());
        this.fragment = fragment;
        cancelBtn.setOnClickListener(this);
        pickPhotoBtn.setOnClickListener(this);
        takePhotoBtn.setOnClickListener(this);
    }

    public void setAvatar(boolean avatar) {
        isAvatar = avatar;
    }

    @Override
    public void onClick(View v) {
        if(isShowing()) dismiss();
        if(isAvatar)
            clickForAvatar(v);
        else
            click(v);
    }

    private void click(View v){
        switch (v.getId()){
            case R.id.pop_take_photo_tv:// 拍照
                if(fragment != null)
                    PhotoManager.getInstance().takePhoto(fragment);
                else
                    PhotoManager.getInstance().takePhoto(activity);
                break;
            case R.id.pop_album_tv:// 相册选择图片
                if(fragment != null)
                    PhotoManager.getInstance().pickPhoto(fragment);
                else
                    PhotoManager.getInstance().pickPhoto(activity);
                break;
            case R.id.pop_cancel_tv:// 取消
                break;
        }
    }

    private void clickForAvatar(View v){
        switch (v.getId()){
            //为弹出窗口实现点击事件
            case R.id.pop_take_photo_tv:// 拍照
                if(fragment != null)
                    PhotoManager.getInstance().takeAvatarPhoto(fragment);
                else
                    PhotoManager.getInstance().takeAvatarPhoto(activity);
                break;
            case R.id.pop_album_tv:// 相册选择图片
                if(fragment != null)
                    PhotoManager.getInstance().pickAvatarPhoto(fragment);
                else
                    PhotoManager.getInstance().pickAvatarPhoto(activity);
                break;
            case R.id.pop_cancel_tv:// 取消
                break;
        }
    }

}
