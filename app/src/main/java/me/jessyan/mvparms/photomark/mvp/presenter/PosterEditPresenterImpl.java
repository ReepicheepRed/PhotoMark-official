package me.jessyan.mvparms.photomark.mvp.presenter;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.jess.arms.base.AppManager;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.DeviceUtils;
import com.jess.arms.utils.RxUtils;
import com.jess.arms.utils.UiUtils;
import com.martin.poster.Layer;
import com.martin.poster.Model;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.jessyan.mvparms.photomark.R;
import me.jessyan.mvparms.photomark.app.service.DownloadService;
import me.jessyan.mvparms.photomark.mvp.contract.PosterEditContract;
import me.jessyan.mvparms.photomark.mvp.model.entity.BaseJson;
import me.jessyan.mvparms.photomark.mvp.model.entity.Download;
import me.jessyan.mvparms.photomark.mvp.model.entity.Font;
import me.jessyan.mvparms.photomark.mvp.model.entity.PAtt;
import me.jessyan.mvparms.photomark.mvp.model.entity.Poster;
import me.jessyan.mvparms.photomark.mvp.ui.adapter.PosterRecentlyAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static me.jessyan.mvparms.photomark.mvp.model.api.service.PosterService.MESSAGE_PROGRESS;

/**
* Created by zhiPeng.S on 2017/05/05
*/

public class PosterEditPresenterImpl extends BasePresenter<PosterEditContract.Model,PosterEditContract.View>
        implements PosterEditContract.Presenter,DefaultAdapter.OnRecyclerViewItemClickListener<Poster> {
    private RxErrorHandler mErrorHandler;
    private AppManager mAppManager;
    private Application mApplication;
    private List<Poster> data = new ArrayList<>();
    private DefaultAdapter mAdapter;
    private int lastUserId = 1;

    private RequestManager requestManager;
    private List<Layer> layers = new ArrayList<>();
    private List<Bitmap> bitmaps = new ArrayList<>();
    private Bitmap cover;
    static final int MaxCoverWidth = 456;
    static final int MaxLayerWidth = 400;
    private Poster mPoster;
    private List<TextView> textViews = new ArrayList<>();
    @Inject
    public PosterEditPresenterImpl(PosterEditContract.Model model, PosterEditContract.View rootView, RxErrorHandler handler
            , AppManager appManager, Application application) {
        super(model, rootView);
        this.mApplication = application;
        this.mErrorHandler = handler;
        this.mAppManager = appManager;
        mAdapter = new PosterRecentlyAdapter(data);
        mAdapter.setOnItemClickListener(this);
        mRootView.setAdapter(mAdapter);//设置Adapter
    }

    public void requestPoster() {
        requestPoster(true);
    }
    public void requestPoster(final boolean pullToRefresh) {
        mModel.getPoster(mApplication)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 1))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(() -> {
                    if (pullToRefresh)
                        mRootView.showLoading();//显示上拉刷新的进度条
                    else
                        mRootView.startLoadMore();
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> {
                    if (pullToRefresh)
                        mRootView.hideLoading();//隐藏上拉刷新的进度条
                    else
                        mRootView.endLoadMore();
                })
                .compose(RxUtils.bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseJson<List<Poster>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<List<Poster>> result) {
                        data.addAll(result.getData());
                        mAdapter.notifyDataSetChanged();//通知更新数据
                    }
                });
    }

    public void initPoster(Poster poster){
        mPoster = poster;
        mRootView.setPosterViewSize(poster);
        new ShowPosterTask().execute();
    }

    private void loadImages(Poster poster){
        requestManager = Glide.with(mApplication);
        try {
            cover = requestManager.load(poster.getIntro().getBackgroundsrc()).asBitmap().into(MaxCoverWidth, MaxCoverWidth).get();
            int size = poster.getAtts() == null ? 0 : poster.getAtts().size();
            for (int i = 0; i < size; i++) {
                PAtt pAtt = poster.getAtts().get(i);
                if(pAtt.isIsimage()){
//                    Bitmap bitmap = requestManager.load(R.mipmap.ic_upload).asBitmap().into(
//                            UiUtils.dip2px((float) pAtt.getWidth()/2.0f),
//                            UiUtils.dip2px((float) pAtt.getHeight()/2.0f)).get();
                    Bitmap bitmap = requestManager.load(R.mipmap.ic_upload).asBitmap().into(MaxLayerWidth,MaxLayerWidth).get();
                    bitmaps.add(bitmap);
                }
            }

            float scale = DeviceUtils.getDensity(mApplication);
//        float ratio = 720f/600f/scale; // 600f is posterView width
            float ratio = 720f/Float.valueOf(poster.getIntro().getWidth())/scale; // 600f is posterView width

            for (int i = 0; i < bitmaps.size(); i++) {
                List<String> position = poster.getAtts().get(i).getPosition();
                int[] pos_LT = trimLayout(position.get(0));
                int[] pos_RT = trimLayout(position.get(1));
                int[] pos_RB = trimLayout(position.get(2));
                int[] pos_LB = trimLayout(position.get(3));

                layers.add(new Layer(bitmaps.get(i), 0)
                        .markPoint(UiUtils.dip2px(pos_LT[0] * ratio), UiUtils.dip2px(pos_LT[1] * ratio))
                        .markPoint(UiUtils.dip2px(pos_RT[0] * ratio), UiUtils.dip2px(pos_RT[1] * ratio))
                        .markPoint(UiUtils.dip2px(pos_RB[0] * ratio), UiUtils.dip2px(pos_RB[1] * ratio))
                        .markPoint(UiUtils.dip2px(pos_LB[0] * ratio), UiUtils.dip2px(pos_LB[1] * ratio))
                        .build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearLayers(){
        layers.clear();
    }

    private boolean zoomOut;

    public void setZoomOut(boolean zoomOut) {
        this.zoomOut = zoomOut;
    }

    public boolean isZoomOut() {
        return zoomOut;
    }

    private void loadTexts(Poster poster){
        float scale = 0.5f;
        int pHeight = Integer.valueOf(poster.getIntro().getHeight());
        if(zoomOut) scale = (pHeight-180)/2.0f/pHeight;
        List<PAtt> list = poster.getAtts();
        int size = list == null ? 0 : list.size();
        for (int i = 0; i < size; i++) {
            PAtt pAtt = list.get(i);
            if(pAtt.isIsimage())continue;
            int fid = pAtt.getFontid();
            if(fid != 1 && fid != 2 && fid != 3){
                Intent intent = new Intent(mApplication, DownloadService.class);
                intent.putExtra("fontUrl",pAtt.getFontlink());
//                mApplication.startService(intent);
            }

//            int width = pAtt.getAwidth(), height = pAtt.getAheight();
            int width = (int) pAtt.getWidth(), height = (int) pAtt.getHeight();

            int LGravity = pAtt.getLayoutgravity(), gravity = pAtt.getGravity();
            int topMargin = pAtt.getTopmargin();
            int leftMargin = pAtt.getLeftmargin(), rightMargin = pAtt.getRightmargin();
            int textSize = pAtt.getFontsize();
            int color = Color.parseColor(pAtt.getTextcolor());

            TextView textView = new TextView(mApplication);
            textView.setBackgroundResource(R.mipmap.ic_red_line);
//            textView.setBackgroundColor(Color.GREEN);
            textView.setTextSize(textSize * scale);
            textView.setTextColor(color);
            textView.setText(pAtt.getTextstring());
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setIncludeFontPadding(false);


            switch (gravity) {
                case 0:
                    textView.setGravity(Gravity.LEFT);
                    break;
                case 1:
                    textView.setGravity(Gravity.CENTER_HORIZONTAL);
                    break;
                case 2:
                    textView.setGravity(Gravity.RIGHT);
                    break;
                case 3:
                    textView.setGravity(Gravity.CENTER_VERTICAL);
                    break;
                case 4:
                    textView.setGravity(Gravity.CENTER);
                    break;
                case 5:
                    textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
                    break;
                case 6:
                    textView.setGravity(Gravity.BOTTOM);
                    break;
                case 7:
                    textView.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
                    break;
                case 8:
                    textView.setGravity(Gravity.BOTTOM|Gravity.RIGHT);
                    break;
            }

            if (width != MATCH_PARENT && width != WRAP_CONTENT) width = UiUtils.dip2px(width * scale);
            if (height != MATCH_PARENT && height != WRAP_CONTENT) height = UiUtils.dip2px(height * scale);

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
            switch (LGravity) {
                case 0:
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, -1);
                    break;
                case 1:
                    layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, -1);
                    break;
                case 2:
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, -1);
                    break;
            }
            layoutParams.topMargin = UiUtils.dip2px(topMargin * scale);
            layoutParams.leftMargin = UiUtils.dip2px(leftMargin * scale);

            mRootView.addView(textView,layoutParams);
            textViews.add(textView);
        }      
    }

    private void loadTextFont(Poster poster){

    }

    private LocalBroadcastManager bManager;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(MESSAGE_PROGRESS)) {

                Download download = intent.getParcelableExtra("download");

                if (download.getProgress() == 100) {


                }
            }
        }
    };

    private void registerReceiver() {

        bManager = LocalBroadcastManager.getInstance(mApplication);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_PROGRESS);
        bManager.registerReceiver(broadcastReceiver, intentFilter);

    }

    @Override
    public void onItemClick(View view, Poster data, int position) {
        mRootView.resetPoster();
        initPoster(data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mAdapter = null;
        this.data = null;
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mApplication = null;
    }

    public static int[] trimLayout(String str){
        int[] position = new int[2];
        String[] posStr = str.substring(1,str.length()-1).split(",");
        position[0] = Integer.valueOf(posStr[0]);
        position[1] = Integer.valueOf(posStr[1]);
        return position;
    }

    private class ShowPosterTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            loadImages(mPoster);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Model model = new  Model(cover, layers);
            if (mRootView == null) return;
            mRootView.showPosterView(model);
            loadTexts(mPoster);
            handler.postDelayed(runnable,2*1000);
        }
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            for (TextView textView : textViews){
                textView.setBackgroundColor(Color.TRANSPARENT);
            }
            textViews.clear();
        }
    };

    public void requestFont(){
        mModel.getFont()
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 1))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(() -> {
//                    if (pullToRefresh)
//                        mRootView.showLoading();//显示上拉刷新的进度条
//                    else
//                        mRootView.startLoadMore();
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> {
//                    if (pullToRefresh)
//                        mRootView.hideLoading();//隐藏上拉刷新的进度条
//                    else
//                        mRootView.endLoadMore();
                })
                .compose(RxUtils.bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseJson<List<Font>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<List<Font>> result) {
                        mRootView.setFont(result.getData());
                    }
                });
    }
}