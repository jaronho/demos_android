package com.liyuu.strategy.component;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.liyuu.strategy.R;
import com.liyuu.strategy.app.App;

/**
 * 图片加载
 */
public class ImageLoader {
    private static RequestOptions options =
            new RequestOptions()
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE));


    private static RequestOptions options_head =
            new RequestOptions()
                    .placeholder(R.mipmap.icon_default_header)
                    .error(R.mipmap.icon_default_header)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE));

    private static RequestOptions options_banner =
            new RequestOptions()
                    .dontAnimate()
                    .placeholder(R.mipmap.ic_img_def_banner)
                    .error(R.mipmap.ic_img_def_banner)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE));

    public static void load(Activity activity, String img, ImageView view) {
        if (activity.isFinishing()) return;
        Glide.with(App.getInstance()).load(img)
                .apply(options)
                .into(view);
    }

    public static void load(Context context, String img, ImageView view) {
        if (context == null) return;
        Glide.with(App.getInstance()).load(img)
                .apply(options)
                .into(view);
    }

    public static void load(Context context, String img, ImageView view, int drawableResId) {
        if (context == null) return;
        Glide.with(App.getInstance()).load(img)
                .apply(options.placeholder(drawableResId).error(drawableResId))
                .into(view);
    }

    public static void loadWithListener(Context context, String img, ImageView view, RequestListener<Drawable> requestListener) {
        if (context == null) return;
        Glide.with(App.getInstance()).load(img)
                .listener(requestListener)
                .apply(options)
                .into(view);
    }

    public static void loadHead(Context context, String img, ImageView view) {
        if (context == null) return;
        Glide.with(App.getInstance()).load(img)
                .apply(options_head)
                .into(view);
    }

    public static void loadHead(Fragment fragment, String img, ImageView view) {
        if (fragment.isDetached()) return;
        Glide.with(App.getInstance()).load(img)
                .apply(options_head)
                .into(view);
    }

    public static void loadAd(Context context, String img, ImageView view) {
        if (context == null) return;
        Glide.with(App.getInstance()).load(img)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .into(view);
    }

    public static void loadBanner(Context context, String imgUrl, int arc, ImageView imageView) {
        if (context == null) return;
        Glide.with(App.getInstance())
                .load(imgUrl)
                .apply(options_banner/*.transform(new GlideRoundTransformCenterCrop(arc))*/)
                .into(imageView);
    }

}
