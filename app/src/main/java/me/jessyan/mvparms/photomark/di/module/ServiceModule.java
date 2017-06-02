package me.jessyan.mvparms.photomark.di.module;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.photomark.mvp.model.api.service.CommonService;
import me.jessyan.mvparms.photomark.mvp.model.api.service.PosterService;
import me.jessyan.mvparms.photomark.mvp.model.api.service.UserService;
import retrofit2.Retrofit;

/**
 * Created by zhiyicx on 2016/3/30.
 */
@Module
public class ServiceModule {

    @Singleton
    @Provides
    CommonService provideCommonService(Retrofit retrofit) {
        return retrofit.create(CommonService.class);
    }

    @Singleton
    @Provides
    UserService provideUserService(Retrofit retrofit) {
        return retrofit.create(UserService.class);
    }

    @Singleton
    @Provides
    PosterService providePosterService(Retrofit retrofit) {
        return retrofit.create(PosterService.class);
    }



}
