package com.example.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.nyapp.R;
import com.example.view.GlideCircleTransform;
import com.example.view.GlideRoundTransform;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by NY on 2016/12/19.
 *
 */

public class MyGlideUtils {
    public static void loadImage(Context context, String url, int erroImg, int emptyImg, ImageView iv) {
        //原生 API
        Glide.with(context)
                .load(url)
                .placeholder(emptyImg)
                .error(erroImg)
                .into(iv);
    }

    public static void loadImage(Context context, String url, ImageView iv) {
        //原生 API
        Glide.with(context)
                .load(url)
                .crossFade()
//                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.img_non_shop)
                .error(R.drawable.img_non_shop)
                .into(iv);
    }

    public static void loadHeadImage(Context context, String url, ImageView iv) {
        //原生 API
        Glide.with(context)
                .load(url)
                .crossFade()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.img_unlogin)
                .error(R.drawable.img_unlogin)
                .into(iv);
    }
    public static void loadNativeImage(Context context, String url, ImageView iv) {
        //原生 API
        Glide.with(context)
                .load(url)
                .crossFade()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(iv);
    }

    public static void loadGifImage(Context context, String url, ImageView iv) {
        Glide.with(context)
                .load(url)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.img_non_shop)
                .error(R.drawable.img_non_shop)
                .into(iv);
    }


    public static void loadCircleImage(Context context, String url, ImageView iv) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.img_unlogin)
                .error(R.drawable.img_unlogin)
                .transform(new GlideCircleTransform(context))
                .into(iv);
    }
    public static void loadCircleBitmap(Context context, Bitmap bitmap, ImageView iv) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes=baos.toByteArray();

        Glide.with(context)
                .load(bytes)
                .placeholder(R.drawable.img_unlogin)
                .error(R.drawable.img_unlogin)
                .transform(new GlideCircleTransform(context))
                .into(iv);
    }

    public static void loadRoundCornerImage(Context context, String url, ImageView iv) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.img_non_shop)//等待时的图片
                .error(R.drawable.img_non_shop)//加载失败后的图片
                .transform(new GlideRoundTransform(context,10))
                .into(iv);
    }


    public static void loadImage(Context context, final File file, final ImageView imageView) {
        Glide.with(context)
                .load(file)
                .into(imageView);


    }

    public static void loadImage(Context context, final int resourceId, final ImageView imageView) {
        Glide.with(context)
                .load(resourceId)
                .into(imageView);
    }

}
