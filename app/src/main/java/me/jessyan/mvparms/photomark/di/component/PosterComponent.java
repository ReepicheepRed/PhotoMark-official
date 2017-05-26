package me.jessyan.mvparms.photomark.di.component;

import com.jess.arms.di.scope.ActivityScope;

import common.AppComponent;
import dagger.Component;
import me.jessyan.mvparms.photomark.di.module.PosterModule;
import me.jessyan.mvparms.photomark.mvp.ui.activity.PosterActivity;

/**
 * Created by jess on 9/4/16 11:17
 * Contact with jess.yan.effort@gmail.com
 */
@ActivityScope
@Component(modules = PosterModule.class,dependencies = AppComponent.class)
public interface PosterComponent {
    void inject(PosterActivity activity);
}
