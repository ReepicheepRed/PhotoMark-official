package me.jessyan.mvparms.photomark.mvp.ui.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.utils.UiUtils;
import com.martin.poster.Layer;
import com.martin.poster.Model;
import com.martin.poster.ModelView;
import com.martin.poster.OnLayerSelectListener;
import com.martin.poster.PosterView;
import com.paginate.Paginate;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import common.AppComponent;
import common.WEActivity;
import me.jessyan.mvparms.photomark.R;
import me.jessyan.mvparms.photomark.app.utils.FileUtils.SavePicture;
import me.jessyan.mvparms.photomark.di.component.DaggerPosterEditComponent;
import me.jessyan.mvparms.photomark.di.module.PosterEditModule;
import me.jessyan.mvparms.photomark.mvp.contract.PosterEditContract;
import me.jessyan.mvparms.photomark.mvp.model.api.service.PhotoManager;
import me.jessyan.mvparms.photomark.mvp.model.entity.Font;
import me.jessyan.mvparms.photomark.mvp.model.entity.Poster;
import me.jessyan.mvparms.photomark.mvp.presenter.PosterEditPresenterImpl;
import me.jessyan.mvparms.photomark.mvp.ui.widget.AndroidBug5497Workaround;
import me.jessyan.mvparms.photomark.mvp.ui.widget.PopPhoto;
import me.jessyan.mvparms.photomark.mvp.ui.widget.PopPoster;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;


/**
 * Created by zhiPeng.S on 2017/5/9.
 */

public class PosterEditActivity extends WEActivity<PosterEditPresenterImpl>implements PosterEditContract.View,OnLayerSelectListener,PhotoManager.Callback {

    @BindView(R.id.iPoster_recently_rv)
    RecyclerView mRecyclerView ;

    private Paginate mPaginate;
    private boolean isLoadingMore;
    private RxPermissions mRxPermissions;

    private Layer selectedLayer;

    @BindView(R.id.posterView)
    PosterView posterView;
    private TextView curEditText;

    @BindView(R.id.headline)
    TextView title;
    @BindView(R.id.right)
    TextView saveTv;
    @BindView(R.id.back_iv)
    ImageView back;

    private PopPhoto popPhoto;
    private PopPoster popPoster;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions(this);
        DaggerPosterEditComponent
                .builder()
                .appComponent(appComponent)
                .posterEditModule(new PosterEditModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_poster_edit, null, false);
    }

    @Override
    protected void initData() {
        AndroidBug5497Workaround.assistActivity(this);
        panelHideHeight = getResources().getDimensionPixelSize(R.dimen.height_180px);
        title.setText(R.string.poster);
        saveTv.setText(R.string.save);
        saveTv.setTypeface(Typeface.MONOSPACE);
        back.setOnClickListener(this);
        popPhoto = new PopPhoto(this);
        popPoster = new PopPoster(this);
        initPoster();
        mPresenter.requestPoster();
        mPresenter.requestFont();
    }

    private void initPoster(){
        resetPoster();
        mPresenter.initPoster((Poster) getIntent().getSerializableExtra("poster"));
    }

    @Override
    public void resetPoster(){
        posterView.reset();
        mPresenter.clearLayers();
    }

    @Override
    @OnClick({R.id.right,R.id.poster_float})
    public void onClick(View v) {
        if(v instanceof TextView && v.getId() != R.id.right){
            curEditText = (TextView) v;
            popPoster.showAtLocation(getCurrentFocus(), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
            popPoster.setCurText(curEditText);

        }

        switch (v.getId()){
            case R.id.back_iv:
                killMyself();
                break;
            case R.id.poster_float:
                if(isPanelShowing)
                    hideStylePanel();
                else
                    showStylePanel();
                break;
            case R.id.right:
                new SavePicture(this).execute(posterView.getResult());
                break;
        }
    }

    private void initRecycleView() {
        configRecycleView(mRecyclerView, new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
    }

    private void configRecycleView(RecyclerView recyclerView
            , RecyclerView.LayoutManager layoutManager
    ) {
        recyclerView.setLayoutManager(layoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void setAdapter(DefaultAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
        initRecycleView();
    }

    @Override
    public void startLoadMore() {
        isLoadingMore = true;
    }

    @Override
    public void endLoadMore() {
        isLoadingMore = false;
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }



    @Override
    public void showLoading() {
        Timber.tag(TAG).w("showLoading");
        Observable.just(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {

                });
    }

    @Override
    public void hideLoading() {
        Timber.tag(TAG).w("hideLoading");
    }

    @Override
    public void showMessage(String message) {
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(Intent intent) {
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public void onSelected(Layer layer) {
        selectedLayer = layer;
        popPhoto.showAtLocation(getCurrentFocus(), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
    }

    @Override
    public void dismiss(Layer layer) {
        if (selectedLayer == layer) {
            selectedLayer = null;
            posterView.dissMenu();
        }
    }



    @Override
    public void setPosterViewSize(Poster poster) {
        ViewGroup.LayoutParams params = posterView.getLayoutParams();
        int width = UiUtils.dip2px(Integer.valueOf(poster.getIntro().getWidth())/2.0f);
        int height = UiUtils.dip2px(Integer.valueOf(poster.getIntro().getHeight())/2.0f);
        try {
            if(mPresenter.isZoomOut()){
                float ratio = 1.0f - panelHideHeight/height;
                params.height = (int)(height * ratio);
                params.width = (int)(width * ratio);
            }else {
                params.width = width;
                params.height = height;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private Model curMode;

    @Override
    public void showPosterView(Model model) {
        float ratio = posterView.getHeight() * 1.0f / posterView.getWidth();
        posterView.setModel(model,ratio);
        posterView.getModelView().setOnLayerSelectListener(this);
        curMode = model;
    }

    @Override
    public void addView(View view, RelativeLayout.LayoutParams layoutParams) {
        view.setOnClickListener(this);
        posterView.addView(view,layoutParams);
        if(curEditText == null && view instanceof TextView)
            curEditText = (TextView)view;
    }


    private boolean isPanelShowing = false;
    private float panelHideHeight;
    
    @Override
    public void showStylePanel() {
        if(mRecyclerView!=null&& !isPanelShowing){
            isPanelShowing = true;
            ObjectAnimator transAnim =  ObjectAnimator.ofFloat(mRecyclerView,"translationY",0,-panelHideHeight);
            transAnim.setDuration(500);
            transAnim.start();
            zoomOutPoster();
        }
    }

    @Override
    public void hideStylePanel() {
        if(mRecyclerView!=null&&isPanelShowing){
            ObjectAnimator transAnim =  ObjectAnimator.ofFloat(mRecyclerView,"translationY",-panelHideHeight,0);
            transAnim.setDuration(500);
            transAnim.start();
            isPanelShowing = false;
            zoomInPoster();
        }
    }

    @Override
    public boolean isStylePanelShowing() {
        return isPanelShowing;
    }

    @Override
    public void zoomInPoster() {
        mPresenter.setZoomOut(false);
        initPoster();
//        ViewGroup.LayoutParams params = posterView.getLayoutParams();
//        float ratio = (panelHideHeight + params.height) / params.height;
//        params.height = (int)(params.height*ratio);
//        params.width = (int)(params.width*ratio);
//        if(curMode != null)
//            showPosterView(curMode);
        ObjectAnimator.ofFloat(posterView,"translationY",-panelHideHeight/2.0f,0).setDuration(500).start();
    }

    @Override
    public void zoomOutPoster() {
        mPresenter.setZoomOut(true);
        initPoster();
//        ViewGroup.LayoutParams params = posterView.getLayoutParams();
//        float ratio = 1.0f - panelHideHeight/params.height;
//        params.height = (int)(params.height*ratio);
//        params.width = (int)(params.width*ratio);
//        if(curMode != null)
//            showPosterView(curMode);
        ObjectAnimator.ofFloat(posterView,"translationY",0,-panelHideHeight/2.0f).setDuration(500).start();
    }

    @Override
    public void setFont(List<Font> fonts) {
        if(popPoster != null)
            popPoster.setFonts(fonts);
    }

    @Override
    public void photo(String path){
        try {
            Glide.with(this).load(path).asBitmap().centerCrop().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                    ModelView modelView = posterView.getModelView();
                    selectedLayer.resetLayer(bitmap,bitmap);
                    selectedLayer.caculateDrawLayer(modelView.getWidth() * 1.0f / 720);
//                    modelView.invalidate();
                    modelView.releaseAllFocus();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoManager.onActivityResult(requestCode, resultCode, data,this,this);
    }
}
