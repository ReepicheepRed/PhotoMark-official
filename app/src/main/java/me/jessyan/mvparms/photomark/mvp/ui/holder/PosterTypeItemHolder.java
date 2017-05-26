package me.jessyan.mvparms.photomark.mvp.ui.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.widget.imageloader.ImageLoader;

import butterknife.BindView;
import common.WEApplication;
import kotlin.jvm.JvmField;
import me.jessyan.mvparms.photomark.R;
import me.jessyan.mvparms.photomark.mvp.model.entity.PType;
import rx.Observable;

/**
 * Created by jess on 9/4/16 12:56
 * Contact with jess.yan.effort@gmail.com
 */
public class PosterTypeItemHolder extends BaseHolder<PType> {
    @BindView(R.id.iPoster_type_tv)
    @JvmField
    TextView mName;

    private ImageLoader mImageLoader;//用于加载图片的管理类,默认使用glide,使用策略模式,可替换框架
    private final WEApplication mApplication;

    public PosterTypeItemHolder(View itemView) {
        super(itemView);
        //可以在任何可以拿到Application的地方,拿到AppComponent,从而得到用Dagger管理的单例对象
        mApplication = (WEApplication) itemView.getContext().getApplicationContext();
        mImageLoader = mApplication.getAppComponent().imageLoader();

    }

    @Override
    public void setData(PType data) {
        if(data.isSelected())
            mName.setTextColor(Color.WHITE);
        else
            mName.setTextColor(mApplication.getResources().getColor(R.color.gray_70_c));
        Observable.just(data.getName()).subscribe(RxTextView.text(mName));
    }
}
