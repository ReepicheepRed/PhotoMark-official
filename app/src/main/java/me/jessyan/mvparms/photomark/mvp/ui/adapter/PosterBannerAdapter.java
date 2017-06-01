package me.jessyan.mvparms.photomark.mvp.ui.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.view.jameson.library.CardAdapterHelper;

import java.util.List;

import me.jessyan.mvparms.photomark.R;
import me.jessyan.mvparms.photomark.mvp.model.entity.Banner;
import me.jessyan.mvparms.photomark.mvp.ui.holder.PosterBannerHolder;

/**
 * Created by jess on 9/4/16 12:57
 * Contact with jess.yan.effort@gmail.com
 */
public class PosterBannerAdapter extends DefaultAdapter<Banner> {
    private CardAdapterHelper mCardAdapterHelper = new CardAdapterHelper();

    public PosterBannerAdapter(List<Banner> infos) {
        super(infos);
    }



    @Override
    public BaseHolder<Banner> getHolder(View v) {
        mCardAdapterHelper.onCreateViewHolder(viewGroup, v);
        return new PosterBannerHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_card_item;
    }

    ViewGroup viewGroup;

    @Override
    public BaseHolder<Banner> onCreateViewHolder(ViewGroup parent, int viewType) {
        viewGroup = parent;
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(BaseHolder<Banner> holder, int position) {
        super.onBindViewHolder(holder, position);
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
    }
}
