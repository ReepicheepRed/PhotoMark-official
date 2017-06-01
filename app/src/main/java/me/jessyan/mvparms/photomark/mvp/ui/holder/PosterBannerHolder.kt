package me.jessyan.mvparms.photomark.mvp.ui.holder

import android.view.View
import android.widget.ImageView
import butterknife.BindView
import com.jess.arms.base.BaseHolder
import com.jess.arms.widget.imageloader.ImageLoader
import com.jess.arms.widget.imageloader.glide.GlideImageConfig
import common.WEApplication
import me.jessyan.mvparms.photomark.R
import me.jessyan.mvparms.photomark.mvp.model.entity.Banner


/**
 * Created by jess on 9/4/16 12:56
 * Contact with jess.yan.effort@gmail.com
 */
class PosterBannerHolder(itemView: View) : BaseHolder<Banner>(itemView) {
    @BindView(R.id.imageView) @JvmField var icon : ImageView? = null
    private val mImageLoader: ImageLoader//用于加载图片的管理类,默认使用glide,使用策略模式,可替换框架
    private val mApplication: WEApplication

    init {
        //可以在任何可以拿到Application的地方,拿到AppComponent,从而得到用Dagger管理的单例对象
        mApplication = itemView.context.applicationContext as WEApplication
        mImageLoader = mApplication.appComponent.imageLoader()

    }

    override fun setData(data: Banner) {
        mImageLoader.loadImage(mApplication, GlideImageConfig
                .builder()
                .url(data.img)
                .imagerView(icon)
                .build())

    }

    override fun onClick(view: View) {
        super.onClick(view)
    }
}
