package me.jessyan.mvparms.photomark.mvp.model.api.service;

import java.util.List;

import me.jessyan.mvparms.photomark.mvp.model.entity.BaseJson;
import me.jessyan.mvparms.photomark.mvp.model.entity.PAtt;
import me.jessyan.mvparms.photomark.mvp.model.entity.PList;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 存放关于海报的一些api
 * Created by jess on 8/5/16 12:05
 * contact with jess.yan.effort@gmail.com
 */
public interface PosterEditService {

    String HEADER_API_VERSION = "Accept: application/vnd.github.v3+json";

//    @Headers({HEADER_API_VERSION})
//    @GET("/Poster/PosterList.ashx")
//    Observable<BaseJson<List<PList>>> getPosters(@Query("type") int type, @Query("per_page") int perPage);

    @GET("/Poster/PosterList.ashx")
    Observable<BaseJson<List<PList>>> getPosters(@Query("type") int type);

    @GET("/Poster/PosterDetial.ashx")
    Observable<BaseJson<List<PAtt>>> getPAtt(@Query("pid") int pid);
}
