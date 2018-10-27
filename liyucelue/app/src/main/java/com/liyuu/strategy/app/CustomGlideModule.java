package com.liyuu.strategy.app;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * glide 配置文件(*必须放着根目录下)
 * 编译过后生成GlideApp(替代Glide类)
 * /build未生成GlideApp可通过Build->Make Project生成
 */

@GlideModule
public final class CustomGlideModule extends AppGlideModule {

    public CustomGlideModule() {
        super();
    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
        builder.setMemoryCache(new LruResourceCache(16 * 1024 * 1024));
        builder.setDiskCache(new ExternalPreferredCacheDiskCacheFactory(context, 100 * 1024 * 1024));
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        super.registerComponents(context, glide, registry);
        registry.append(String.class, InputStream.class, new CustomBaseGlideUrlLoader.Factory());
    }

    @Override
    public boolean isManifestParsingEnabled() {
//        return super.isManifestParsingEnabled();
        //不使用清单配置的方式,减少初始化时间
        return false;
    }


    static class CustomBaseGlideUrlLoader extends BaseGlideUrlLoader<String> {
        private static final ModelCache<String, GlideUrl> urlCache =
                new ModelCache<>(150);
        /**
         * Url的匹配规则
         */
        private static final Pattern PATTERN = Pattern.compile("__w-((?:-?\\d+)+)__");

        public CustomBaseGlideUrlLoader(ModelLoader<GlideUrl, InputStream> concreteLoader, ModelCache<String, GlideUrl> modelCache) {
            super(concreteLoader, modelCache);
        }

        /**
         * If the URL contains a special variable width indicator (eg "__w-200-400-800__")
         * we get the buckets from the URL (200, 400 and 800 in the example) and replace
         * the URL with the best bucket for the requested width (the bucket immediately
         * larger than the requested width).
         * <p>
         * 控制加载的图片的大小
         */
        @Override
        protected String getUrl(String model, int width, int height, Options options) {
            Matcher m = PATTERN.matcher(model);
            int bestBucket = 0;
            if (m.find()) {
                String[] found = m.group(1).split("-");
                for (String bucketStr : found) {
                    bestBucket = Integer.parseInt(bucketStr);
                    if (bestBucket >= width) {
                        // the best bucket is the first immediately bigger than the requested width
                        break;
                    }
                }
                if (bestBucket > 0) {
                    model = m.replaceFirst("w" + bestBucket);
                }
            }
            return model;
        }

        @Override
        public boolean handles(String s) {
            return true;
        }

        /**
         * 工厂来构建CustormBaseGlideUrlLoader对象
         */
        public static class Factory implements ModelLoaderFactory<String, InputStream> {
            @Override
            public ModelLoader<String, InputStream> build(MultiModelLoaderFactory multiFactory) {
                return new CustomBaseGlideUrlLoader(multiFactory.build(GlideUrl.class, InputStream.class), urlCache);
            }

            @Override
            public void teardown() {

            }
        }
    }
}
