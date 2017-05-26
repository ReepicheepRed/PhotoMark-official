package me.jessyan.mvparms.photomark.mvp.contract;

import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.mvp.BaseView;
import com.jess.arms.mvp.IModel;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;

import me.jessyan.mvparms.photomark.mvp.model.entity.BaseJson;
import me.jessyan.mvparms.photomark.mvp.model.entity.PAtt;
import me.jessyan.mvparms.photomark.mvp.model.entity.PList;
import me.jessyan.mvparms.photomark.mvp.model.entity.PType;
import rx.Observable;

/**
 * Created by zhiPeng.S on 2017/5/5.
 */

public class PosterContract {
    
public interface View extends BaseView{
    void setAdapter(DefaultAdapter adapter);
    void setTypeAdapter(DefaultAdapter adapter);
    void startLoadMore();
    void endLoadMore();

    RxPermissions getRxPermissions();
}

public interface Presenter{
}

public interface Model extends IModel{
    Observable<BaseJson<List<PList>>> getPoster(int type, boolean update);
    Observable<BaseJson<List<PAtt>>> getPAtt(int pid, boolean update);
    Observable<BaseJson<List<PType>>> getPType();
}


}