package com.gsclub.strategy.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.gsclub.strategy.app.AppConfig;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;

public class PhotoUtil {

    /**
     * 将资源路径下的图片资源压缩后返回对应的Bitmap对象
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int requiredWidth, int requiredHeight) {
        // 第一次解析将inJustDecodeBounds置为true用以获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用calculateInSampleSize计算inSampleSize的值
        options.inSampleSize = calculateInSampleSize(options, requiredWidth, requiredHeight);
        // 将inJustDecodeBounds置为false,再次解析
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 将磁盘上的FileDescriptor代表的图片文件压缩后返回对应的Bitmap对象
     */
    public static Bitmap decodeSampledBitmapFromDisk(FileDescriptor descriptor, int requiredWidth, int requiredHeight) {
        // 第一次解析将inJustDecodeBounds置为true用以获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Rect outPadding = new Rect(0, 0, requiredWidth, requiredHeight);
        BitmapFactory.decodeFileDescriptor(descriptor, outPadding, options);
        // 调用calculateInSampleSize计算inSampleSize的值
        options.inSampleSize = calculateInSampleSize(options, requiredWidth, requiredHeight);
        // 将inJustDecodeBounds置为false,再次解析
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(descriptor, outPadding,
                options);
    }

    public static Bitmap decodeSampledBitmap(FileInputStream is, int requiredWidth, int requiredHeight) {
        // 第一次解析将inJustDecodeBounds置为true用以获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);
        // 调用calculateInSampleSize计算inSampleSize的值
        options.inSampleSize = calculateInSampleSize(options, requiredWidth, requiredHeight);
        // 将inJustDecodeBounds置为false,再次解析
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(is, null, options);
    }

    /**
     * 将磁盘上的imagePath路径下的图片文件压缩后返回对应的Bitmap对象
     */
    public static Bitmap decodeSampledBitmapFromDisk(String imagePath, int requiredWidth, int requiredHeight) {
        // 第一次解析将inJustDecodeBounds置为true用以获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        // 调用calculateInSampleSize计算inSampleSize的值
        options.inSampleSize = calculateInSampleSize(options, requiredWidth, requiredHeight);
        // 将inJustDecodeBounds置为false,再次解析
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imagePath, options);
    }

    /**
     * 根据传入的BitmapFactory.Options对象和图片需要的宽和高计算出压缩比例
     *
     * @param options        BitmapFactory.Options对象
     * @param requiredWidth  需要的图片的宽度
     * @param requiredHeight 需要的图片的高度
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int requiredWidth, int requiredHeight) {
        // 根据BitmapFactory.Options对象获取原图片的高度和宽度
        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;
        if (width > requiredWidth || height > requiredHeight) {
            // 计算出实际的宽高比率
            final int widthRatio = Math.round((float) width
                    / (float) requiredWidth);
            final int heightRatio = Math.round((float) height
                    / (float) requiredHeight);
            // 选择宽高比率最小的作为inSampleSize的值,保证最后图片的宽和高大于等于需要的宽和高
            inSampleSize = widthRatio < heightRatio ? widthRatio : heightRatio;
        }
        return inSampleSize;
    }

    /**
     * 根据Uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    public static String getRealPathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= 19) { // api >= 19
            return getRealPathFromUriAboveApi19(context, uri);
        } else { // api < 19
            return getRealPathFromUriBelowAPI19(context, uri);
        }
    }

    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private static String getRealPathFromUriBelowAPI19(Context context, Uri uri) {
        return getDataColumn(context, uri, null, null);
    }

    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @SuppressLint("NewApi")
    private static String getRealPathFromUriAboveApi19(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) { // MediaProvider
                // 使用':'分割
                String id = documentId.split(":")[1];

                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};
                filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())){
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     * @return
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * 兼容android 7.0的权限处理
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
