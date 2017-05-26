package me.jessyan.mvparms.photomark.mvp.model.api.cache;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.rx_cache.DynamicKey;
import io.rx_cache.EvictProvider;
import io.rx_cache.LifeCache;
import io.rx_cache.Reply;
import me.jessyan.mvparms.photomark.mvp.model.entity.BaseJson;
import me.jessyan.mvparms.photomark.mvp.model.entity.PAtt;
import me.jessyan.mvparms.photomark.mvp.model.entity.PList;
import me.jessyan.mvparms.photomark.mvp.model.entity.User;
import rx.Observable;

/**
 * Created by jess on 8/30/16 13:53
 * Contact with jess.yan.effort@gmail.com
 */
public interface CommonCache {

    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<Reply<List<User>>> getUsers(Observable<List<User>> oUsers, DynamicKey idLastUserQueried, EvictProvider evictProvider);

    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<Reply<BaseJson<List<PList>>>> getPosters(Observable<BaseJson<List<PList>>> oUsers, DynamicKey idLastUserQueried, EvictProvider evictProvider);

    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<Reply<BaseJson<List<PAtt>>>> getPAtt(Observable<BaseJson<List<PAtt>>> oUsers, DynamicKey idLastUserQueried, EvictProvider evictProvider);
}
