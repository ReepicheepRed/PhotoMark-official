package me.jessyan.mvparms.photomark.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

import me.jessyan.mvparms.photomark.R;
import me.jessyan.mvparms.photomark.mvp.ui.holder.SettingItemHolder;

/**
 * Created by jess on 9/4/16 12:57
 * Contact with jess.yan.effort@gmail.com
 */
public class SettingAdapter extends DefaultAdapter<String> {

    public SettingAdapter(List<String> infos) {
        super(infos);

    }

    @Override
    public BaseHolder<String> getHolder(View v) {
        return new SettingItemHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_setting;
    }
}
