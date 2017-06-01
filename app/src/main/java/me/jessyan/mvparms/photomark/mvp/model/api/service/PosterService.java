package me.jessyan.mvparms.photomark.mvp.model.api.service;

import java.util.List;

import me.jessyan.mvparms.photomark.mvp.model.entity.Banner;
import me.jessyan.mvparms.photomark.mvp.model.entity.BaseJson;
import me.jessyan.mvparms.photomark.mvp.model.entity.Font;
import me.jessyan.mvparms.photomark.mvp.model.entity.PAtt;
import me.jessyan.mvparms.photomark.mvp.model.entity.PList;
import me.jessyan.mvparms.photomark.mvp.model.entity.PType;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 存放关于海报的一些api
 * Created by jess on 8/5/16 12:05
 * contact with jess.yan.effort@gmail.com
 */
public interface PosterService {

    String HEADER_API_VERSION = "Accept: application/vnd.github.v3+json";

//    @Headers({HEADER_API_VERSION})
//    @GET("/Poster/PosterList.ashx")
//    Observable<BaseJson<List<PList>>> getPosters(@Query("type") int type, @Query("per_page") int perPage);

    @GET("/Poster/PosterList.ashx?client=1")
    Observable<BaseJson<List<PList>>> getPosters(@Query("type") int type);

    @GET("/Poster/PosterList.ashx?client=1")
    Observable<BaseJson<List<PList>>> getSpecificPoster(@Query("pid") int pid);

    @GET("/Poster/AndroidPosterDetial.ashx")
    Observable<BaseJson<List<PAtt>>> getPAtt(@Query("pid") int pid);

    @GET("/Poster/PosterTypes.ashx")
    Observable<BaseJson<List<PType>>> getPType();

    @GET("/Poster/FontsList.ashx")
    Observable<BaseJson<List<Font>>> getFont();

    @GET("/Banner/banner.ashx?client=1")
    Observable<BaseJson<List<Banner>>> getBanner();
}
