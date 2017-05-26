package me.jessyan.mvparms.photomark.mvp.presenter;
import android.app.Application;
import android.content.Intent;
import android.view.View;

import com.jess.arms.base.AppManager;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.jessyan.mvparms.photomark.app.utils.Preferences;
import me.jessyan.mvparms.photomark.mvp.contract.PosterContract;
import me.jessyan.mvparms.photomark.mvp.model.entity.BaseJson;
import me.jessyan.mvparms.photomark.mvp.model.entity.PAtt;
import me.jessyan.mvparms.photomark.mvp.model.entity.PList;
import me.jessyan.mvparms.photomark.mvp.model.entity.PType;
import me.jessyan.mvparms.photomark.mvp.model.entity.Poster;
import me.jessyan.mvparms.photomark.mvp.ui.activity.PosterEditActivity;
import me.jessyan.mvparms.photomark.mvp.ui.adapter.PosterAdapter;
import me.jessyan.mvparms.photomark.mvp.ui.adapter.PosterTypeAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
* Created by zhiPeng.S on 2017/05/05
*/

public class PosterPresenterImpl extends BasePresenter<PosterContract.Model,PosterContract.View>
        implements PosterContract.Presenter,DefaultAdapter.OnRecyclerViewItemClickListener {
    private RxErrorHandler mErrorHandler;
    private AppManager mAppManager;
    private Application mApplication;
    private List<PList> data = new ArrayList<>();
    private List<PType> types = new ArrayList<>();
    private DefaultAdapter mAdapter;
    private PosterTypeAdapter adapter;
    private int lastUserId = 1;

    private PList curPList;
    private PType curPType;
    private int curType = 1;

    @Inject
    public PosterPresenterImpl(PosterContract.Model model, PosterContract.View rootView, RxErrorHandler handler
            , AppManager appManager, Application application) {
        super(model, rootView);
        this.mApplication = application;
        this.mErrorHandler = handler;
        this.mAppManager = appManager;
        mAdapter = new PosterAdapter(data);
        mAdapter.setOnItemClickListener(this);
        mRootView.setAdapter(mAdapter);//设置Adapter

        adapter = new PosterTypeAdapter(types);
        adapter.setOnItemClickListener(this);
        mRootView.setTypeAdapter(adapter);//设置Adapter

    }

    public void requestPoster(final boolean pullToRefresh) {
        mModel.getPoster(curType,pullToRefresh)
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
                .subscribe(new ErrorHandleSubscriber<BaseJson<List<PList>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<List<PList>> result) {
                        if(data.size() > 0)
                            lastUserId = data.get(data.size() - 1).getPid();//记录最后一个id,用于下一次请求
                        if (pullToRefresh) data.clear();//如果是上拉刷新则晴空列表
                        data.addAll(result.getData());
                        mAdapter.notifyDataSetChanged();//通知更新数据
                    }
                });
    }

    private void requestPAttribute(int pid){
        mModel.getPAtt(pid,true)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 1))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(() -> {

                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> {

                })
                .compose(RxUtils.bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseJson<List<PAtt>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<List<PAtt>> result) {
                        Poster poster = new Poster();
                        poster.setIntro(curPList);
                        poster.setAtts(result.getData());
                        Preferences.instance().savePoster(mApplication,poster);
                        Intent intent = new Intent(mApplication, PosterEditActivity.class);
                        intent.putExtra("poster",poster);
                        mRootView.launchActivity(intent);
                    }
                });
    }

    public void requestPosterType() {
        mModel.getPType()
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
                .subscribe(new ErrorHandleSubscriber<BaseJson<List<PType>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<List<PType>> result) {
                        types.addAll(result.getData());
                        for (PType type: types) {
                            if(type.getTypeid() == curType){
                                int index = types.indexOf(type);
                                type.setSelected(true);
                                types.set(index,type);
                                curPType = type;
                            }
                        }
                        adapter.notifyDataSetChanged();//通知更新数据
                    }
                });
    }

    @Override
    public void onItemClick(View view, Object data, int position) {
        if(data instanceof PList){
            curPList = (PList)data;
            requestPAttribute(curPList.getPid());
            return;
        }

        int index;
        if (curPType != null) {
            index = types.indexOf(curPType);
            curPType.setSelected(false);
            types.set(index,curPType);
        }
        curPType = (PType)data;
        index = types.indexOf(curPType);
        curPType.setSelected(true);
        types.set(index,curPType);
        adapter.notifyDataSetChanged();
        curType = curPType.getTypeid();
        requestPoster(true);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mAdapter = null;
        this.adapter = null;
        this.data = null;
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mApplication = null;
    }
}