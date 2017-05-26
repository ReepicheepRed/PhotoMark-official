package me.jessyan.mvparms.photomark.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.photomark.mvp.contract.PosterContract;
import me.jessyan.mvparms.photomark.mvp.model.PosterModelImpl;

/**
 * Created by jess on 9/4/16 11:10
 * Contact with jess.yan.effort@gmail.com
 */
@Module
public class PosterModule {
    private PosterContract.View view;

    /**
     * 构建UserModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     * @param view
     */
    public PosterModule(PosterContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    PosterContract.View providePosterView(){
        return this.view;
    }

    @ActivityScope
    @Provides
    PosterContract.Model providePosterModel(PosterModelImpl model){
        return model;
    }
}
