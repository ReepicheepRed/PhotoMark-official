package me.jessyan.mvparms.photomark.mvp.ui.activity

import android.content.Intent
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.jess.arms.base.DefaultAdapter
import com.jess.arms.utils.Preconditions.checkNotNull
import com.jess.arms.utils.UiUtils
import common.AppComponent
import common.WEActivity
import kotlinx.android.synthetic.main.actionbar_base.*
import kotlinx.android.synthetic.main.activity_setting.*
import me.jessyan.mvparms.photomark.R
import me.jessyan.mvparms.photomark.di.component.DaggerSettingComponent
import me.jessyan.mvparms.photomark.di.module.SettingModule
import me.jessyan.mvparms.photomark.mvp.contract.SettingContract
import me.jessyan.mvparms.photomark.mvp.presenter.SettingPresenter
import me.jessyan.mvparms.photomark.mvp.ui.widget.SettingDecoration

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

class SettingActivity : WEActivity<SettingPresenter>(), SettingContract.View {


    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerSettingComponent
                .builder()
                .appComponent(appComponent)
                .settingModule(SettingModule(this)) //请将SettingModule()第一个首字母改为小写
                .build()
                .inject(this)
    }

    override fun initView(): View {
        return LayoutInflater.from(this).inflate(R.layout.activity_setting, null, false)
    }

    override fun initData() {
        headline.text = getString(R.string.setting)
        back_iv.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v?.id){
            R.id.back_iv -> killMyself()
        }
    }

    override fun setAdapter(adapter: DefaultAdapter<*>) {
        setting_rv.adapter = adapter
        adapter.notifyDataSetChanged()
        configRecycleView(setting_rv,LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false))
    }

    private fun configRecycleView(recyclerView: RecyclerView, layoutManager: RecyclerView.LayoutManager
    ) {
        recyclerView.layoutManager = layoutManager
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(SettingDecoration(this))
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