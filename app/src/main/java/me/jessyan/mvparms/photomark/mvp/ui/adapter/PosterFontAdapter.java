package me.jessyan.mvparms.photomark.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

import me.jessyan.mvparms.photomark.R;
import me.jessyan.mvparms.photomark.mvp.model.entity.Font;
import me.jessyan.mvparms.photomark.mvp.ui.holder.PosterFontItemHolder;

/**
 * Created by jess on 9/4/16 12:57
 * Contact with jess.yan.effort@gmail.com
 */
public class PosterFontAdapter extends DefaultAdapter<Font> {
    public PosterFontAdapter(List<Font> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<Font> getHolder(View v) {
        return new PosterFontItemHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_poster_font;
    }
}
