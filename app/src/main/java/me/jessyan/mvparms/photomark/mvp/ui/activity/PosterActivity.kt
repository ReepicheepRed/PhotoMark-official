package me.jessyan.mvparms.photomark.mvp.ui.activity

import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View

import com.jess.arms.base.DefaultAdapter
import com.jess.arms.utils.UiUtils
import com.paginate.Paginate
import com.tbruyelle.rxpermissions.RxPermissions

import butterknife.BindView
import common.AppComponent
import common.WEActivity
import me.jessyan.mvparms.photomark.R
import me.jessyan.mvparms.photomark.app.utils.Preferences
import me.jessyan.mvparms.photomark.di.component.DaggerPosterComponent
import me.jessyan.mvparms.photomark.di.module.PosterModule
import me.jessyan.mvparms.photomark.mvp.contract.PosterContract
import me.jessyan.mvparms.photomark.mvp.presenter.PosterPresenterImpl
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import timber.log.Timber
import kotlinx.android.synthetic.main.activity_poster.*

/**
 * Created by zhiPeng.S on 2017/5/9.
 */

class PosterActivity : WEActivity<PosterPresenterImpl>(), PosterContract.View, SwipeRefreshLayout.OnRefreshListener {

    private val mPaginate: Paginate? = null
    private var isLoadingMore: Boolean = false
    private var mRxPermissions: RxPermissions? = null

    override fun setupActivityComponent(appComponent: AppComponent) {
        this.mRxPermissions = RxPermissions(this)
        DaggerPosterComponent
                .builder()
                .appComponent(appComponent)
                .posterModule(PosterModule(this))
                .build()
                .inject(this)
    }

    override fun initView(): View {
        return LayoutInflater.from(this).inflate(R.layout.activity_poster, null, false)
    }

    override fun initData() {
        mPresenter.requestPoster(true)
        mPresenter.requestPosterType()
        Preferences.instance().clearRecord(this)
    }

    override fun onClick(v: View) {

    }

    override fun onRefresh() {
        mPresenter.requestPoster(true)
    }

    private fun initRecycleView() {
        poster_srl.setOnRefreshListener(this)
        configRecycleView(poster_rv, StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL))

    }


    private fun configRecycleView(recyclerView: RecyclerView?, layoutManager: RecyclerView.LayoutManager
    ) {
        recyclerView?.layoutManager = layoutManager
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView?.setHasFixedSize(true)
        recyclerView?.itemAnimator = DefaultItemAnimator()
    }

    override fun setAdapter(adapter: DefaultAdapter<*>) {
        poster_rv.adapter = adapter
        initRecycleView()
    }

    override fun setTypeAdapter(adapter: DefaultAdapter<*>) {
        poster_type_rv.adapter = adapter
        configRecycleView(poster_type_rv, LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false))
    }

    override fun startLoadMore() {
        isLoadingMore = true
    }

    override fun endLoadMore() {
        isLoadingMore = false
    }

    override fun getRxPermissions(): RxPermissions? {
        return mRxPermissions
    }


    override fun showLoading() {
        Timber.tag(TAG).w("showLoading")
        Observable.just(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { }
    }

    override fun hideLoading() {
        Timber.tag(TAG).w("hideLoading")
        poster_srl.isRefreshing = false
    }

    override fun showMessage(message: String) {
        UiUtils.SnackbarText(message)
    }

    override fun launchActivity(intent: Intent) {
        UiUtils.startActivity(intent)
    }

    override fun killMyself() {
        finish()
    }


}
