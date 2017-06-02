package me.jessyan.mvparms.photomark.app.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.File;

import me.jessyan.mvparms.photomark.R;
import me.jessyan.mvparms.photomark.app.download.DownloadProgressListener;
import me.jessyan.mvparms.photomark.app.utils.StringUtils;
import me.jessyan.mvparms.photomark.mvp.model.api.DownloadAPI;
import me.jessyan.mvparms.photomark.mvp.model.api.service.PosterService;
import me.jessyan.mvparms.photomark.mvp.model.entity.Download;
import rx.Subscriber;

/**
 * Created by JokAr on 16/7/5.
 */
public class DownloadService extends IntentService {
    private static final String TAG = "DownloadService";

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;

    int downloadCount = 0;

//    private String apkUrl = "http://download.fir.im/v2/app/install/5818acbcca87a836f50014af?download_token=a01301d7f6f8f4957643c3fcfe5ba6ff";


    public DownloadService() {
        super("DownloadService");
    }

    private File outputFile;

    @Override
    protected void onHandleIntent(Intent intent) {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Download")
                .setContentText("Downloading File")
                .setAutoCancel(true);

        notificationManager.notify(0, notificationBuilder.build());

        download(intent.getStringExtra("fontUrl"));
    }

    private void download(String apkUrl) {
        DownloadProgressListener listener = (bytesRead, contentLength, done) -> {
            //不频繁发送通知，防止通知栏下拉卡顿
            int progress = (int) ((bytesRead * 100) / contentLength);
            if ((downloadCount == 0) || progress > downloadCount) {
                Download download = new Download();
                download.setTotalFileSize(contentLength);
                download.setCurrentFileSize(bytesRead);
                download.setProgress(progress);

                sendNotification(download);
            }
        };
        outputFile = new File(Environment.getExternalStorageDirectory() + "/PhotoMark/font/", apkUrl.split("/")[apkUrl.split("/").length-1]);

        if (outputFile.exists()) {
            outputFile.delete();
        }


        String baseUrl = StringUtils.getHostName(apkUrl);

        new DownloadAPI(baseUrl, listener).downloadAPK(apkUrl, outputFile, new Subscriber() {
            @Override
            public void onCompleted() {
                downloadCompleted(apkUrl);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                downloadCompleted(apkUrl);
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(Object o) {

            }
        });
    }

    private void downloadCompleted(String url) {
        Download download = new Download();
        download.setProgress(100);
        download.setFileUrl(url);
        sendIntent(download);

        notificationManager.cancel(0);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setContentText("File Downloaded");
        notificationManager.notify(0, notificationBuilder.build());

        //安装apk
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
//        intent.setDataAndType(Uri.fromFile(outputFile), "application/vnd.android.package-archive");
//        startActivity(intent);
    }

    private void sendNotification(Download download) {

        sendIntent(download);
        notificationBuilder.setProgress(100, download.getProgress(), false);
        notificationBuilder.setContentText(
                StringUtils.getDataSize(download.getCurrentFileSize()) + "/" +
                        StringUtils.getDataSize(download.getTotalFileSize()));
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendIntent(Download download) {
        Intent intent = new Intent(PosterService.MESSAGE_PROGRESS);
        intent.putExtra("download", download);
        LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }
}
