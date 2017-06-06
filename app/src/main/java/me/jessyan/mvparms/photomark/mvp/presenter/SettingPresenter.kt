package me.jessyan.mvparms.photomark.mvp.presenter

import android.app.Application
import android.view.View
import com.jess.arms.base.AppManager
import com.jess.arms.base.DefaultAdapter
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.mvp.BasePresenter
import com.jess.arms.utils.UiUtils.makeText
import com.jess.arms.widget.imageloader.ImageLoader
import me.jessyan.mvparms.photomark.mvp.contract.SettingContract
import me.jessyan.mvparms.photomark.mvp.ui.adapter.SettingAdapter
import me.jessyan.rxerrorhandler.core.RxErrorHandler
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
 * Created by zhiPeng.S on 2017/6/6.
 */

@ActivityScope
class SettingPresenter @Inject
constructor(model: SettingContract.Model, rootView: SettingContract.View, private var mErrorHandler: RxErrorHandler?, private var mApplication: Application?, private var mImageLoader: ImageLoader?, private var mAppManager: AppManager?) : BasePresenter<SettingContract.Model, SettingContract.View>(model, rootView),DefaultAdapter.OnRecyclerViewItemClickListener<String> {

    var adapter : DefaultAdapter<*>? = null
    val items  = listOf("Email Us", "Rate & Review", "Share", "Clean cache", "Facebook", "Instagram", "Twitter")

    init {
        adapter = SettingAdapter(items)
        adapter?.setOnItemClickListener(this)
        mRootView.setAdapter(adapter!!)
    }

    override fun onItemClick(view: View?, data: String?, position: Int) {
        makeText(data)
    }

    override fun onDestroy() {
        super.onDestroy()
        this.mErrorHandler = null
        this.mAppManager = null
        this.mImageLoader = null
        this.mApplication = null
    }

}