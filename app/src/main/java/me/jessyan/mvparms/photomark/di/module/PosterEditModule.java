package me.jessyan.mvparms.photomark.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.photomark.mvp.contract.PosterEditContract;
import me.jessyan.mvparms.photomark.mvp.model.PosterEditModelImpl;

/**
 * Created by jess on 9/4/16 11:10
 * Contact with jess.yan.effort@gmail.com
 */
@Module
public class PosterEditModule {
    private PosterEditContract.View view;

    /**
     * 构建UserModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     * @param view
     */
    public PosterEditModule(PosterEditContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    PosterEditContract.View providePosterEditView(){
        return this.view;
    }

    @ActivityScope
    @Provides
    PosterEditContract.Model providePosterEditModel(PosterEditModelImpl model){
        return model;
    }
}
