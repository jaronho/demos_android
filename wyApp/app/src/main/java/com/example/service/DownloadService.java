package com.example.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;

import com.example.classes.BaseEventBean;
import com.example.nyapp.R;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyToastUtil;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;

import okhttp3.Call;

/**
 * 用来下载app的服务
 */
public class DownloadService extends IntentService {
    private NotificationManager mNotifyManager;
    private Builder mBuilder;
    private File apkFile;
    private LocalBroadcastManager lbm;
    private BroadcastReceiver receiver;
    private int oldProgress = 0;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        lbm = LocalBroadcastManager.getInstance(this);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("com.notification.click")) {
                    Intent installAPKIntent = new Intent(Intent.ACTION_VIEW);
                    installAPKIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//安装完成后打开新的apk
                    installAPKIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri fileUri = FileProvider.getUriForFile(DownloadService.this, "com.example.nyapp.fileprovider", apkFile);

                        installAPKIntent.setDataAndType(fileUri, "application/vnd.android.package-archive");
                    } else {
                        installAPKIntent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                    }

                    DownloadService.this.startActivity(installAPKIntent);
                    android.os.Process.killProcess(android.os.Process.myPid());
                }

            }
        };
        lbm.registerReceiver(receiver, new IntentFilter("com.notification.click"));

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new Builder(this);

        String appName = getString(getApplicationInfo().labelRes);
        int icon = getApplicationInfo().icon;

        mBuilder.setContentTitle(appName).setSmallIcon(icon);

        EventBus.getDefault().post(new BaseEventBean(0,0));

        //开始下载新的apk
        String urlStr = intent.getStringExtra("url");
        MyOkHttpUtils
                .getData(urlStr,new HashMap<String, String>())
                .build()
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "nongyi.apk") {
                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        int newProgress = (int) (100 * progress);
                        if (newProgress != oldProgress) {
                            updateProgress(newProgress);
                        }
                        oldProgress = newProgress;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToastUtil.showShortMessage(e.toString());
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        //下载完成
                        apkFile = response;

                        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
                        mBuilder.setContentText(getString(R.string.download_success)).setProgress(0, 0, false);
                        mNotifyManager.notify(0, mBuilder.build());
                        mNotifyManager.cancelAll();

                        //下载完成后直接安装打开
                        Intent installAPKIntent = new Intent(Intent.ACTION_VIEW);
                        installAPKIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//安装完成后打开新的apk
                        installAPKIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Uri fileUri = FileProvider.getUriForFile(DownloadService.this, "com.example.nyapp.fileprovider", apkFile);

                            installAPKIntent.setDataAndType(fileUri, "application/vnd.android.package-archive");
                        } else {
                            installAPKIntent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                        }

                        DownloadService.this.startActivity(installAPKIntent);
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }

                });
    }


    private void updateProgress(int progress) {
        EventBus.getDefault().post(new BaseEventBean(0,progress));
        //"正在下载:" + progress + "%"
        mBuilder.setContentText(this.getString(R.string.download_progress, progress)).setProgress(100, progress, false);

        //setContentIntent如果不设置在4.0+上没有问题，在4.0以下会报异常
        PendingIntent pendingintent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(pendingintent);
        mNotifyManager.notify(0, mBuilder.build());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lbm.unregisterReceiver(receiver);
    }
}
