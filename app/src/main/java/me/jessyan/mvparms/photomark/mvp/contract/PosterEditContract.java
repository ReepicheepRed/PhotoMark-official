package me.jessyan.mvparms.photomark.mvp.contract;

import android.content.Context;
import android.widget.RelativeLayout;

import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.mvp.BaseView;
import com.jess.arms.mvp.IModel;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;

import me.jessyan.mvparms.photomark.mvp.model.entity.BaseJson;
import me.jessyan.mvparms.photomark.mvp.model.entity.Font;
import me.jessyan.mvparms.photomark.mvp.model.entity.Poster;
import rx.Observable;

/**
 * Created by zhiPeng.S on 2017/5/5.
 */

public class PosterEditContract {

    public interface View extends BaseView{
        void setAdapter(DefaultAdapter adapter);
        void startLoadMore();
        void endLoadMore();

        RxPermissions getRxPermissions();

        void setPosterViewSize(Poster poster);
        void showPosterView(com.martin.poster.Model model);
        void addView(android.view.View view, RelativeLayout.LayoutParams layoutParams);
        void showStylePanel();
        void hideStylePanel();
        boolean isStylePanelShowing();
        void zoomInPoster();
        void zoomOutPoster();
        void setFont(List<Font> fonts);
        void resetPoster();
    }

    public interface Presenter{
    }

    public interface Model extends IModel{
        Observable<BaseJson<List<Poster>>> getPoster(Context context);
        Observable<BaseJson<List<Font>>> getFont();
    }


}