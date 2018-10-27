package com.liyuu.strategy.ui.dialog;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


import com.liyuu.strategy.R;
import com.liyuu.strategy.app.SPKeys;
import com.liyuu.strategy.model.bean.AppUpdateBean;
import com.liyuu.strategy.ui.DialogActivity;
import com.liyuu.strategy.util.FileUtil;
import com.liyuu.strategy.util.LogUtil;
import com.liyuu.strategy.util.PreferenceUtils;
import com.liyuu.strategy.util.StorageUtil;
import com.liyuu.strategy.util.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017-08-26.
 */

public class UpdateAppDialog extends Dialog {

    private static final int DOWN_ID = 5058;

    private AppUpdateBean param;
    private CheckBox mCheck;
    private Button mSure;
    private Context context;

    NotificationManager mNotificationManager;


    public UpdateAppDialog(@NonNull Context context, AppUpdateBean p) {
        super(context, R.style.ThemeDeviceAlertDialogStyle);// android.R.style.Theme_DeviceDefault_Dialog_NoActionBar  AlertDialogStyle
        this.context = context;
        param = p;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update_app);

        TextView version = ButterKnife.findById(this, R.id.version);
        version.setText("版本号：" + param.ver_num);

        TextView description = ButterKnife.findById(this, R.id.text);
        description.setText(param.desc);

        final Button cancel = ButterKnife.findById(this, R.id.btn_cancel);
        mSure = ButterKnife.findById(this, R.id.btn_sure);
        mCheck = ButterKnife.findById(this, R.id.igonre_version);
        if (param.must_update == 1) {
            cancel.setVisibility(View.GONE);
            mSure.setText("立即更新");
            mSure.setBackgroundResource(R.drawable.blue_btn_background_bottom_corner);
            mCheck.setVisibility(View.GONE);
            setCanceledOnTouchOutside(false);
            setCancelable(false);
        } else if (param.not_show_ignore) {
            param.not_show_ignore = false;
            mCheck.setVisibility(View.GONE);
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheck.isChecked()) {
                    PreferenceUtils.put(SPKeys.FILE_COMMON, SPKeys.COMMON_IGNORE_VERSION, param.ver_num);
                }
                dismiss();
//                DialogActivity.dismissAndFinish(UpdateAppDialog.this, context);
            }
        });
        mSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheck.isChecked()) {
                    PreferenceUtils.put(SPKeys.FILE_COMMON, SPKeys.COMMON_IGNORE_VERSION, param.ver_num);
                } else {
                    String url = param.update_url;
//                String url = "http://www.lingyangs.com/app/down";
                    update(url);
                }
                dismiss();
//                if(!"3".equals(param[0])) {
//                DialogActivity.dismissAndFinish(UpdateAppDialog.this, context);
//                }
            }
        });
    }

    private void update(String url) {
        LogUtil.d("down url", url);
        //将本地下载新版本注释
        if (StorageUtil.isExternalRootDir()) {
            new FileDownTask().execute(url);
        } else {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
            getContext().startActivity(intent);
        }
    }

    /**
     * 静默检测版本信息
     */
    public static void checkUpdata(AppUpdateBean data) {
        int isUpdate = data.is_update;
        if (isUpdate == 2) {
            PreferenceUtils.put(SPKeys.FILE_COMMON, SPKeys.COMMON_NEW_VERSION, false);
            return;
        }
        PreferenceUtils.put(SPKeys.FILE_COMMON, SPKeys.COMMON_NEW_VERSION, true);
        String nVersion = data.ver_num;
        if (isUpdate == 1) {
            if (TextUtils.isEmpty(nVersion)) return;
            String igVersion = PreferenceUtils.getString(SPKeys.FILE_COMMON, SPKeys.COMMON_IGNORE_VERSION);
            if (nVersion.equals(igVersion)) return;
        }
        DialogActivity.start(DialogActivity.DIALOG_UPDATE, data);
    }

    /**
     * 主动显示版本信息
     *
     * @param data
     */
    public static void update(AppUpdateBean data) {
        int isUpdate = data.is_update;
        if (isUpdate == 2) {
            PreferenceUtils.put(SPKeys.FILE_COMMON, SPKeys.COMMON_NEW_VERSION, false);
//            ToastUtil.showMsg("当前已是最新版本！");
            return;
        }
        data.not_show_ignore = true;
        PreferenceUtils.put(SPKeys.FILE_COMMON, SPKeys.COMMON_NEW_VERSION, true);
        DialogActivity.start(DialogActivity.DIALOG_UPDATE, data);
    }

    private class FileDownTask extends AsyncTask<String, Integer, File> {

        @Override
        protected void onPreExecute() {
            mSure.setEnabled(false);
            mNotificationManager = (NotificationManager)
                    getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        }

        @Override
        protected File doInBackground(String... strings) {
            Request request = new Request.Builder().url(strings[0]).get().build();
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            FileOutputStream fileOutputStream = null;
            InputStream is = null;
            File destFile = null;
            try {
                Response response = client.build().newCall(request).execute();
                if (response.isSuccessful()) {
                    long contentLength = response.body().contentLength();
                    long downloadLength = 0;
                    is = response.body().byteStream();

                    destFile = new File(StorageUtil.getRootDir(), "strategy.apk");
                    LogUtil.d("down path", destFile.getAbsolutePath());

                    fileOutputStream = new FileOutputStream(destFile);
                    byte[] buffer = new byte[1024 * 4];//缓冲数组4kB
                    int len;
                    while ((len = is.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
                        downloadLength += len;
                        float f = (float) downloadLength / contentLength;
                        showNotification(getContext(), destFile, (int) (f * 100));
                    }
                    fileOutputStream.flush();
                    return destFile;
                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(File file) {
            mSure.setEnabled(true);
            if (file == null) {
                ToastUtil.showMsg("文件下载失败，请稍后重试");
                mNotificationManager.cancel(DOWN_ID);
                return;
            }
            getContext().startActivity(getIntent(file));
        }
    }

    private void showNotification(Context context, File file, int progress) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);// 设置图标
        builder.setWhen(System.currentTimeMillis());// 设置通知来到的时间
        if (progress == 100) {
            mNotificationManager.cancel(DOWN_ID); //在个别机型上不能显示100%的进度
            builder.setContentTitle("下载完成");
        } else {
            builder.setContentTitle("下载进度");// 设置通知的标题
        }
        builder.setContentText(progress + "%");// 设置通知的内容
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        builder.setOngoing(true);
        builder.setAutoCancel(true);
        builder.setProgress(100, progress, false);

        Intent intent = progress == 100 ? getIntent(file) : new Intent();
        Notification mNotification = builder.build();
        PendingIntent contentIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
        mNotification.contentIntent = contentIntent;
        mNotificationManager.notify(DOWN_ID, mNotification);
    }

    private Intent getIntent(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri data = FileUtil.fromFile(getContext(), intent, file);
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        return intent;
    }
}
