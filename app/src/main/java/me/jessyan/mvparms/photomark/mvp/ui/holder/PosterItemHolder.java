package me.jessyan.mvparms.photomark.mvp.ui.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.utils.UiUtils;
import com.jess.arms.widget.imageloader.ImageLoader;
import com.jess.arms.widget.imageloader.glide.GlideImageConfig;

import butterknife.BindView;
import common.WEApplication;
import me.jessyan.mvparms.photomark.R;
import me.jessyan.mvparms.photomark.mvp.model.entity.PList;
/**
 * Created by jess on 9/4/16 12:56
 * Contact with jess.yan.effort@gmail.com
 */
public class PosterItemHolder extends BaseHolder<PList> {

    @BindView(R.id.iPoster_iv)
    ImageView mAvater;

    private ImageLoader mImageLoader;//用于加载图片的管理类,默认使用glide,使用策略模式,可替换框架
    private final WEApplication mApplication;

    public PosterItemHolder(View itemView) {
        super(itemView);
        //可以在任何可以拿到Application的地方,拿到AppComponent,从而得到用Dagger管理的单例对象
        mApplication = (WEApplication) itemView.getContext().getApplicationContext();
        mImageLoader = mApplication.getAppComponent().imageLoader();

    }

    @Override
    public void setData(PList data) {
        int width = 0,height = 0;
        try {
            width = Integer.valueOf(data.getWidth());
            height = Integer.valueOf(data.getHeight());
        }catch (Exception e){
            e.printStackTrace();
        }

        ViewGroup.LayoutParams params = mAvater.getLayoutParams();
        if(width == height){
            params.height = UiUtils.dip2px(228/2.0f);
            params.width = UiUtils.dip2px(228/2.0f);
        }else {
            params.height = UiUtils.dip2px(404/2.0f);
            params.width = UiUtils.dip2px(228/2.0f);
        }
        mAvater.setLayoutParams(params);
        mImageLoader.loadImage(mApplication, GlideImageConfig
                .builder()
                .url(data.getThumbnailsrc())
                .imagerView(mAvater)
                .build());
    }
}
