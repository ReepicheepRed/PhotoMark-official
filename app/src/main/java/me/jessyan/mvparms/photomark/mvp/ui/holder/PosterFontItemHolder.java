package me.jessyan.mvparms.photomark.mvp.ui.holder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.widget.imageloader.ImageLoader;

import butterknife.BindView;
import common.WEApplication;
import me.jessyan.mvparms.photomark.R;
import me.jessyan.mvparms.photomark.app.service.DownloadService;
import me.jessyan.mvparms.photomark.mvp.model.entity.Download;
import me.jessyan.mvparms.photomark.mvp.model.entity.Font;

import static me.jessyan.mvparms.photomark.mvp.model.api.service.PosterService.MESSAGE_PROGRESS;

/**
 * Created by jess on 9/4/16 12:56
 * Contact with jess.yan.effort@gmail.com
 */
public class PosterFontItemHolder extends BaseHolder<Font> {
    @BindView(R.id.iPoster_font_tv)
    TextView mName;

    @BindView(R.id.iPoster_font_pb)
    private ProgressBar progress;

    private Font curFont;

    private ImageLoader mImageLoader;//用于加载图片的管理类,默认使用glide,使用策略模式,可替换框架
    private final WEApplication mApplication;

    private LocalBroadcastManager bManager;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(MESSAGE_PROGRESS)) {

                Download download = intent.getParcelableExtra("download");
                progress.setProgress(download.getProgress());
//                if (download.getProgress() == 100) {
//
//                    progress_text.setText("File Download Complete");
//
//                } else {
//
//                    progress_text.setText(
//                            StringUtils.getDataSize(download.getCurrentFileSize())
//                                    + "/" +
//                                    StringUtils.getDataSize(download.getTotalFileSize()));
//
//                }
            }
        }
    };

    private void registerReceiver() {

        bManager = LocalBroadcastManager.getInstance(mApplication);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_PROGRESS);
        bManager.registerReceiver(broadcastReceiver, intentFilter);

    }

    public PosterFontItemHolder(View itemView) {
        super(itemView);
        //可以在任何可以拿到Application的地方,拿到AppComponent,从而得到用Dagger管理的单例对象
        mApplication = (WEApplication) itemView.getContext().getApplicationContext();
        mImageLoader = mApplication.getAppComponent().imageLoader();
        registerReceiver();
        progress.setOnClickListener(this);
    }

    @Override
    public void setData(Font data) {
        mName.setText(data.getFont());
        curFont = data;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.iPoster_font_pb:
                Intent intent = new Intent(mApplication, DownloadService.class);
                intent.putExtra("fontUrl",curFont.getFontsrc());
                mApplication.startService(intent);
                break;
        }
    }

    public void unregisterReceiver() {
        //解除注册时，使用注册时的manager解绑
        bManager.unregisterReceiver(broadcastReceiver);
    }
}
