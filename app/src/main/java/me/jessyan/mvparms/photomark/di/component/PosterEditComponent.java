package me.jessyan.mvparms.photomark.di.component;

import com.jess.arms.di.scope.ActivityScope;

import common.AppComponent;
import dagger.Component;
import me.jessyan.mvparms.photomark.di.module.PosterEditModule;
import me.jessyan.mvparms.photomark.mvp.ui.activity.PosterEditActivity;

/**
 * Created by jess on 9/4/16 11:17
 * Contact with jess.yan.effort@gmail.com
 */
@ActivityScope
@Component(modules = PosterEditModule.class,dependencies = AppComponent.class)
public interface PosterEditComponent {
    void inject(PosterEditActivity activity);
}
