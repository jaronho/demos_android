package com.example.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.widget.ImageView;

import com.example.nyapp.MyApplication;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by NY on 2016/11/16.
 */

public enum  ImageViewUtil {
    INSTANCE;
    /**
     * 标准配置
     */
    private DisplayImageOptions normalOptions;
    /**
     * 圆形配置
     */
    private DisplayImageOptions circleOptions;
    /**
     * 圆角图片配置
     */
    private DisplayImageOptions roundedOptions;


    private BitmapDisplayer simpleBitmapDisplayer;
    private BitmapDisplayer circleBitmapDisplayer;
    private BitmapDisplayer roundedBitmapDisplayer;

//    private int onLoadingImageResId;
//    private int onEmptyImageResId;
//    private int onFailedImageResId;


    /**
     * 构造方法 参数初始化 单例形式 只会初始化一次 避免不必要的资源开支
     */
    private ImageViewUtil() {
        //初始化 全局默认图片
//        onLoadingImageResId = R.drawable.splash;
//        onEmptyImageResId = R.drawable.splash;
//        onFailedImageResId = R.drawable.splash;

        simpleBitmapDisplayer = new SimpleBitmapDisplayer();
        normalOptions = getOption(simpleBitmapDisplayer);

        circleBitmapDisplayer = new CircleBitmapDisplayer();
        circleOptions = getOption(circleBitmapDisplayer);

        //圆角图片 圆角半径dp
        int cornerRadiusDp = 10;
        //圆角大小通过 dp2px转换 使得 不同分辨率设备上呈现一致显示效果
        roundedBitmapDisplayer = new RoundedBitmapDisplayer(dip2px(MyApplication.getContextObject(),cornerRadiusDp));
        roundedOptions = getOption(roundedBitmapDisplayer);

    }

    /**
     * 重构 抽取出的通用生成Option方法
     * @param //onLoadingImageResId
     * @param //onEmptyImageResId
     * @param //onFailedImageResId
     * @param bitmapDisplayer normal 或圆形、圆角 bitmapDisplayer
     *
     * @return
     */
    private DisplayImageOptions getOption(BitmapDisplayer bitmapDisplayer) {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc()
                .considerExifParams(true)
                .displayer(bitmapDisplayer)
                .build();
    }

//    private DisplayImageOptions getOption(int onLoadingImageResId, int onEmptyImageResId, int onFailedImageResId, BitmapDisplayer bitmapDisplayer) {
//        return new DisplayImageOptions.Builder()
//                .showImageOnLoading(onLoadingImageResId)
//                .showImageForEmptyUri(onEmptyImageResId)
//                .showImageOnFail(onFailedImageResId)
//                .cacheInMemory(true)
//                .cacheOnDisc()
//                .considerExifParams(true)
//                .displayer(bitmapDisplayer)
//                .build();
//    }


    public void loadImageView(ImageView iv, String url) {
        ImageLoader.getInstance().displayImage(url, iv, normalOptions);
    }

    public void loadCircleImageView(ImageView iv, String url) {
        ImageLoader.getInstance().displayImage(url, iv, circleOptions);
    }

    public void loadRoundedImageView(ImageView iv, String url) {
        ImageLoader.getInstance().displayImage(url, iv, roundedOptions);
    }


    /**
     * dip px 转换工具类 将圆角进行转换 以实现不同分辨率设备上呈现相同效果
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }

    /**
     * 初始化方法
     * @param context
     */
    public void init(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.discCacheFileNameGenerator(new Md5FileNameGenerator());
        config.discCacheSize(50 * 1024 * 1024); // 80 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    /**
     * Save image to the SD card
     *
     * @param photoBitmap
     * @param photoName
     * @param path
     */
    public static String savePhoto(Bitmap photoBitmap, String path,
                                   String photoName) {
        String localPath = null;
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File photoFile = new File(path, photoName + ".jpg");
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100,
                            fileOutputStream)) { // 转换完成
                        localPath = photoFile.getPath();
                        fileOutputStream.flush();
                    }
                }
            } catch (FileNotFoundException e) {
                photoFile.delete();
                localPath = null;
                e.printStackTrace();
            } catch (IOException e) {
                photoFile.delete();
                localPath = null;
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                        fileOutputStream = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return localPath;
    }

    /**
     * 转换图片成圆形
     *
     * @param bitmap
     *            传入Bitmap对象
     * @param tempUri
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap, Uri tempUri) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            left = 0;
            top = 0;
            right = width;
            bottom = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);// 设置画笔无锯齿

        canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas
        paint.setColor(color);

        // 以下有两种方法画圆,drawRounRect和drawCircle
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);//
        // 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
        canvas.drawCircle(roundPx, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
        canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

        return output;
    }
}
