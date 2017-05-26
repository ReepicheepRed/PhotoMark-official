package me.jessyan.mvparms.photomark.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

import me.jessyan.mvparms.photomark.R;
import me.jessyan.mvparms.photomark.mvp.model.entity.PType;
import me.jessyan.mvparms.photomark.mvp.ui.holder.PosterTypeItemHolder;

/**
 * Created by jess on 9/4/16 12:57
 * Contact with jess.yan.effort@gmail.com
 */
public class PosterTypeAdapter extends DefaultAdapter<PType> {
    public PosterTypeAdapter(List<PType> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<PType> getHolder(View v) {
        return new PosterTypeItemHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_poster_type;
    }
}
