package me.jessyan.mvparms.photomark.mvp.ui.activity


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import butterknife.BindView
import butterknife.OnClick
import com.jess.arms.utils.Preconditions.checkNotNull
import com.jess.arms.utils.UiUtils
import common.AppComponent
import common.WEActivity
import kotlinx.android.synthetic.main.actionbar_base.*
import me.jessyan.mvparms.photomark.R
import me.jessyan.mvparms.photomark.di.component.DaggerSaveComponent
import me.jessyan.mvparms.photomark.di.module.SaveModule
import me.jessyan.mvparms.photomark.mvp.contract.SaveContract
import me.jessyan.mvparms.photomark.mvp.presenter.SavePresenter

/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */

/**
 * Created by zhiPeng.S on 2017/5/18.
 */

class SaveActivity : WEActivity<SavePresenter>(), SaveContract.View {

    @BindView(R.id.save_iv) @JvmField var preview :ImageView? = null

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerSaveComponent
                .builder()
                .appComponent(appComponent)
                .saveModule(SaveModule(this)) //请将SaveModule()第一个首字母改为小写
                .build()
                .inject(this)
    }

    override fun initView(): View {
        return LayoutInflater.from(this).inflate(R.layout.activity_save, null, false)
    }

    override fun initData() {
        mPresenter.showPicture(preview, intent.getStringArrayListExtra("pictures"))
        headline.text = getString(R.string.save_title)
        right.text =  getString(R.string.home)

    }

    @OnClick(R.id.save_another_btn, R.id.back_iv,R.id.right)
    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.back_iv -> killMyself()
            R.id.right -> launchActivity(Intent(this, PosterMainActivity::class.java))
            R.id.save_another_btn -> launchActivity(Intent(this, PosterActivity::class.java))
        }
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