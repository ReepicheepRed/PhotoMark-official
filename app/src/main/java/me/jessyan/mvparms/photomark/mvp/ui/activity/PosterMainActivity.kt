package me.jessyan.mvparms.photomark.mvp.ui.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.jess.arms.base.DefaultAdapter
import com.jess.arms.utils.Preconditions.checkNotNull
import com.jess.arms.utils.UiUtils
import com.view.jameson.library.CardScaleHelper
import common.AppComponent
import common.WEActivity
import kotlinx.android.synthetic.main.activity_main.*
import me.jessyan.mvparms.photomark.R
import me.jessyan.mvparms.photomark.app.util.BlurBitmapUtils
import me.jessyan.mvparms.photomark.app.util.ViewSwitchUtils
import me.jessyan.mvparms.photomark.di.component.DaggerPosterMainComponent
import me.jessyan.mvparms.photomark.di.module.PosterMainModule
import me.jessyan.mvparms.photomark.mvp.contract.PosterMainContract
import me.jessyan.mvparms.photomark.mvp.presenter.PosterMainPresenter
import me.jessyan.mvparms.photomark.mvp.ui.adapter.CardAdapter
import java.util.*

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

class PosterMainActivity : WEActivity<PosterMainPresenter>(), PosterMainContract.View {
    private var mBlurView: ImageView? = null
    private val mList = ArrayList<Int>()
    private var mCardScaleHelper: CardScaleHelper? = null
    private var mBlurRunnable: Runnable? = null
    private var mLastPos = -1

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerPosterMainComponent
                .builder()
                .appComponent(appComponent)
                .posterMainModule(PosterMainModule(this)) //请将PosterMainModule()第一个首字母改为小写
                .build()
                .inject(this)
    }

    override fun initView(): View {
        return LayoutInflater.from(this).inflate(R.layout.activity_main, null, false)
    }

    override fun initData() {
        mPresenter.requestBanner()
    }

    override fun setAdapter(adapter: DefaultAdapter<*>) {
        main_banner_rv.adapter = adapter
        configRecycleView(main_banner_rv,LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false))
    }

    private fun configRecycleView(recyclerView: RecyclerView?, layoutManager: RecyclerView.LayoutManager
    ) {
        recyclerView?.layoutManager = layoutManager
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView?.setHasFixedSize(true)
        recyclerView?.itemAnimator = DefaultItemAnimator()
        // mRecyclerView绑定scale效果
        mCardScaleHelper = CardScaleHelper()
        mCardScaleHelper!!.currentItemPos = 2
        mCardScaleHelper!!.attachToRecyclerView(main_banner_rv)

//        initBlurBackground()
    }

    private fun init() {
        for (i in 0..9) {
            mList.add(R.drawable.pic4)
            mList.add(R.drawable.pic5)
            mList.add(R.drawable.pic6)
        }

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        main_banner_rv.layoutManager = linearLayoutManager
        main_banner_rv.adapter = CardAdapter(mList)
        // mRecyclerView绑定scale效果
        mCardScaleHelper = CardScaleHelper()
        mCardScaleHelper!!.currentItemPos = 2
        mCardScaleHelper!!.attachToRecyclerView(main_banner_rv)

        initBlurBackground()
    }

    private fun initBlurBackground() {
        mBlurView = findViewById(R.id.blurView) as ImageView
        main_banner_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    notifyBackgroundChange()
                }
            }
        })

        notifyBackgroundChange()
    }

    private fun notifyBackgroundChange() {
        if (mLastPos == mCardScaleHelper!!.currentItemPos) return
        mLastPos = mCardScaleHelper!!.currentItemPos
        val resId = mList[mCardScaleHelper!!.currentItemPos]
        mBlurView!!.removeCallbacks(mBlurRunnable)
        mBlurRunnable = Runnable {
            val bitmap = BitmapFactory.decodeResource(resources, resId)
            ViewSwitchUtils.startSwitchBackgroundAnim(mBlurView, BlurBitmapUtils.getBlurBitmap(mBlurView!!.context, bitmap, 15))
        }
        mBlurView!!.postDelayed(mBlurRunnable, 500)
    }

    override fun onClick(v: View) {
        val intent = Intent()
        when (v.id) {
            R.id.main_setting_iv -> intent.setClass(this, SettingActivity::class.java)
            R.id.main_poster_btn -> intent.setClass(this, PosterActivity::class.java)
        }
        launchActivity(intent)
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun showMessage(message: String) {
        checkNotNull(message)
        UiUtils.SnackbarText(message)
    }

    override fun launchActivity(intent: Intent) {
        checkNotNull(intent)
        UiUtils.startActivity(intent)
    }

    override fun killMyself() {
        finish()
    }


}