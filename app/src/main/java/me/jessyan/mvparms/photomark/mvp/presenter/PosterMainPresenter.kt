package me.jessyan.mvparms.photomark.mvp.presenter

import android.app.Application
import android.content.Intent
import android.view.View
import com.jess.arms.base.AppManager
import com.jess.arms.base.DefaultAdapter
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.mvp.BasePresenter
import com.jess.arms.utils.RxUtils
import com.jess.arms.widget.imageloader.ImageLoader
import me.jessyan.mvparms.photomark.mvp.contract.PosterMainContract
import me.jessyan.mvparms.photomark.mvp.model.entity.*
import me.jessyan.mvparms.photomark.mvp.ui.activity.PosterEditActivity
import me.jessyan.mvparms.photomark.mvp.ui.adapter.PosterBannerAdapter
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber
import me.jessyan.rxerrorhandler.handler.RetryWithDelay
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import javax.inject.Inject


/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */


/**
 * Created by zhiPeng.S on 2017/5/31.
 */

@ActivityScope
class PosterMainPresenter @Inject
constructor(model: PosterMainContract.Model, rootView: PosterMainContract.View, private var mErrorHandler: RxErrorHandler?, private var mApplication: Application?, private var mImageLoader: ImageLoader?, private var mAppManager: AppManager?) : BasePresenter<PosterMainContract.Model, PosterMainContract.View>(model, rootView),DefaultAdapter.OnRecyclerViewItemClickListener<Banner> {
    private var data: MutableList<Banner>? = ArrayList()
    private var adapter: DefaultAdapter<*>? = null
    private var poster : Poster? = Poster()

    init {
        adapter = PosterBannerAdapter(data)
        adapter!!.setOnItemClickListener(this)
        mRootView.setAdapter(adapter!!)//设置Adapter
    }

    fun requestBanner(){
        mModel.obtainBanner()
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 1))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe({

                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate({

                })
                .compose(RxUtils.bindToLifecycle<BaseJson<List<Banner>>>(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(object : ErrorHandleSubscriber<BaseJson<List<Banner>>>(mErrorHandler) {
                    override fun onNext(result: BaseJson<List<Banner>>) {
                        data?.clear()
                        data?.addAll(result.data)
                        adapter!!.notifyDataSetChanged()//通知更新数据
                    }
                })
    }

    fun requestPoster(pid : Int){
        mModel.getPoster(pid,true)
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 1))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe({

                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate({

                })
                .compose(RxUtils.bindToLifecycle<BaseJson<List<PList>>>(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(object : ErrorHandleSubscriber<BaseJson<List<PList>>>(mErrorHandler) {
                    override fun onNext(result: BaseJson<List<PList>>) {
                        poster?.intro = result.data[0]
                        if (poster?.atts != null) mRootView.launchActivity(Intent(mApplication,PosterEditActivity::class.java).putExtra("poster",poster))
                    }
                })
    }

    fun requestAttribute(pid : Int){
        mModel.getPAtt(pid,true)
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 1))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe({

                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate({

                })
                .compose(RxUtils.bindToLifecycle<BaseJson<List<PAtt>>>(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(object : ErrorHandleSubscriber<BaseJson<List<PAtt>>>(mErrorHandler) {
                    override fun onNext(result: BaseJson<List<PAtt>>) {
                        poster?.atts = result.data
                        if (poster?.intro != null) mRootView.launchActivity(Intent(mApplication,PosterEditActivity::class.java).putExtra("poster",poster))
                    }
                })
    }

    override fun onItemClick(view: View?, data: Banner?, position: Int) {
        requestPoster(data!!.pid)
        requestAttribute(data.pid)
    }

    override fun onDestroy() {
        super.onDestroy()
        this.mErrorHandler = null
        this.mAppManager = null
        this.mImageLoader = null
        this.mApplication = null
    }

}