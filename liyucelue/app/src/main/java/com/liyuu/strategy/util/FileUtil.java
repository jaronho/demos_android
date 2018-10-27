package com.liyuu.strategy.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.liyuu.strategy.app.AppConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

    public static String loadAssetsTxt(Context context, String fileName) {

        try {
            InputStream is = context.getAssets().open(fileName);
            int lenght = is.available();
            byte[] buffer = new byte[lenght];
            is.read(buffer);
            String result = new String(buffer, "utf8");
            is.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 兼容android 7.0的权限处理
     *
     * @param context
     * @param intent
     * @param file
     * @return
     */
    public static Uri fromFile(Context context, Intent intent, File file) {
        Uri data;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            data = FileProvider.getUriForFile(context, AppConfig.AUTHROES, file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(file);
        }
        return data;
    }
}
