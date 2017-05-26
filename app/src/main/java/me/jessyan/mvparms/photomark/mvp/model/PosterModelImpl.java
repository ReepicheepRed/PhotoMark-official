package me.jessyan.mvparms.photomark.mvp.model;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BaseModel;

import java.util.List;

import javax.inject.Inject;

import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.Reply;
import me.jessyan.mvparms.photomark.mvp.contract.PosterContract;
import me.jessyan.mvparms.photomark.mvp.model.api.cache.CacheManager;
import me.jessyan.mvparms.photomark.mvp.model.api.service.ServiceManager;
import me.jessyan.mvparms.photomark.mvp.model.entity.BaseJson;
import me.jessyan.mvparms.photomark.mvp.model.entity.PAtt;
import me.jessyan.mvparms.photomark.mvp.model.entity.PList;
import me.jessyan.mvparms.photomark.mvp.model.entity.PType;
import rx.Observable;
import rx.functions.Func1;

/**
* Created by zhiPeng.S on 2017/05/05
*/
@ActivityScope
public class PosterModelImpl extends BaseModel<ServiceManager, CacheManager> implements PosterContract.Model{
    public static final int USERS_PER_PAGE = 10;

    @Inject
    public PosterModelImpl(ServiceManager serviceManager, CacheManager cacheManager) {
        super(serviceManager, cacheManager);
    }

    @Override
    public Observable<BaseJson<List<PList>>> getPoster(int type,boolean update) {
        Observable<BaseJson<List<PList>>> data = mServiceManager.getPosterService().getPosters(type);
        //使用rxcache缓存,上拉刷新则不读取缓存,加载更多读取缓存
        return mCacheManager.getCommonCache()
                .getPosters(data
                        , new DynamicKey(type)
                        , new EvictDynamicKey(update))
                .flatMap(new Func1<Reply<BaseJson<List<PList>>>, Observable<BaseJson<List<PList>>>>() {
                    @Override
                    public Observable<BaseJson<List<PList>>> call(Reply<BaseJson<List<PList>>> listReply) {
                        return Observable.just(listReply.getData());
                    }
                });
    }

    @Override
    public Observable<BaseJson<List<PAtt>>> getPAtt(int pid, boolean update) {
        Observable<BaseJson<List<PAtt>>> data = mServiceManager.getPosterService().getPAtt(pid);
        //使用rxcache缓存,上拉刷新则不读取缓存,加载更多读取缓存
        return mCacheManager.getCommonCache()
                .getPAtt(data
                        , new DynamicKey(pid)
                        , new EvictDynamicKey(update))
                .flatMap(new Func1<Reply<BaseJson<List<PAtt>>>, Observable<BaseJson<List<PAtt>>>>() {
                    @Override
                    public Observable<BaseJson<List<PAtt>>> call(Reply<BaseJson<List<PAtt>>> listReply) {
                        return Observable.just(listReply.getData());
                    }
                });
    }

    @Override
    public Observable<BaseJson<List<PType>>> getPType() {
        return mServiceManager.getPosterService().getPType();
    }
}