package me.jessyan.mvparms.photomark.mvp.model;
import android.content.Context;

import com.jess.arms.mvp.BaseModel;

import java.util.List;

import javax.inject.Inject;

import me.jessyan.mvparms.photomark.app.utils.Preferences;
import me.jessyan.mvparms.photomark.mvp.contract.PosterEditContract;
import me.jessyan.mvparms.photomark.mvp.model.api.cache.CacheManager;
import me.jessyan.mvparms.photomark.mvp.model.api.service.ServiceManager;
import me.jessyan.mvparms.photomark.mvp.model.entity.BaseJson;
import me.jessyan.mvparms.photomark.mvp.model.entity.Font;
import me.jessyan.mvparms.photomark.mvp.model.entity.Poster;
import rx.Observable;

/**
* Created by zhiPeng.S on 2017/05/10
*/

public class PosterEditModelImpl extends BaseModel<ServiceManager, CacheManager> implements PosterEditContract.Model{

    @Inject
    public PosterEditModelImpl(ServiceManager serviceManager, CacheManager cacheManager) {
        super(serviceManager, cacheManager);
    }

    @Override
    public Observable<BaseJson<List<Poster>>> getPoster(Context context) {
        BaseJson<List<Poster>> json = new BaseJson<>();
        json.setData(Preferences.instance().readPoster(context));
        return Observable.just(json);
    }

    @Override
    public Observable<BaseJson<List<Font>>> getFont() {
        return mServiceManager.getPosterService().getFont();
    }
}