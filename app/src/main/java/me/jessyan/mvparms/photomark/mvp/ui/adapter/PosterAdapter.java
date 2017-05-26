package me.jessyan.mvparms.photomark.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

import me.jessyan.mvparms.photomark.R;
import me.jessyan.mvparms.photomark.mvp.model.entity.PList;
import me.jessyan.mvparms.photomark.mvp.ui.holder.PosterItemHolder;

/**
 * Created by jess on 9/4/16 12:57
 * Contact with jess.yan.effort@gmail.com
 */
public class PosterAdapter extends DefaultAdapter<PList> {
    public PosterAdapter(List<PList> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<PList> getHolder(View v) {
        return new PosterItemHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_poster;
    }
}
