package me.jessyan.mvparms.photomark.mvp.ui.adapter;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

import me.jessyan.mvparms.photomark.R;
import me.jessyan.mvparms.photomark.mvp.ui.holder.PosterTextColorItemHolder;

import static com.jess.arms.utils.UiUtils.getResources;

/**
 * Created by jess on 9/4/16 12:57
 * Contact with jess.yan.effort@gmail.com
 */
public class PosterTextColorAdapter extends DefaultAdapter<Integer> {
    private int count = 12;
    private TypedArray colors;
    public PosterTextColorAdapter(List<Integer> infos) {
        super(infos);
        colors = getResources().obtainTypedArray(R.array.color_array);
        for (int i = 0; i < count; i++) {
            mInfos.add(colors.getColor(i, Color.WHITE));
        }

    }

    @Override
    public int getItemCount() {
        return count;
    }

    @Override
    public BaseHolder<Integer> getHolder(View v) {
        return new PosterTextColorItemHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_poster_color;
    }
}
