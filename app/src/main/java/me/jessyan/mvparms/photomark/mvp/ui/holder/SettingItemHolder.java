package me.jessyan.mvparms.photomark.mvp.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.widget.imageloader.ImageLoader;

import butterknife.BindView;
import common.WEApplication;
import me.jessyan.mvparms.photomark.R;

/**
 * Created by jess on 9/4/16 12:56
 * Contact with jess.yan.effort@gmail.com
 */
public class SettingItemHolder extends BaseHolder<String> {

    @BindView(R.id.iSetting)
    TextView mName;

    private ImageLoader mImageLoader;//用于加载图片的管理类,默认使用glide,使用策略模式,可替换框架
    private final WEApplication mApplication;

    public SettingItemHolder(View itemView) {
        super(itemView);
        //可以在任何可以拿到Application的地方,拿到AppComponent,从而得到用Dagger管理的单例对象
        mApplication = (WEApplication) itemView.getContext().getApplicationContext();
        mImageLoader = mApplication.getAppComponent().imageLoader();

    }

    @Override
    public void setData(String data) {
        mName.setText(data);
    }
}
