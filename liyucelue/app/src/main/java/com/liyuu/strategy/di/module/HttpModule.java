package com.liyuu.strategy.di.module;

import com.liyuu.strategy.BuildConfig;
import com.liyuu.strategy.app.App;
import com.liyuu.strategy.app.Constants;
import com.liyuu.strategy.di.cookie.CookiesManager;
import com.liyuu.strategy.di.cookie.PersistentCookieStore;
import com.liyuu.strategy.di.qualifier.StrategyUrl;
import com.liyuu.strategy.http.CustomConverterFactory;
import com.liyuu.strategy.http.api.StrategyApis;
import com.liyuu.strategy.util.SystemUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * HttpModule
 * retrofit okhttpclient 在此初始化
 */

@Module
public class HttpModule {

    /**
     * cookies manager
     */
    @Singleton
    @Provides
    PersistentCookieStore providePersistentCookieStore() {
        return new PersistentCookieStore(App.getInstance());
    }

    @Singleton
    @Provides
    CookiesManager provideCookiesManager(PersistentCookieStore cookieStore) {
        return new CookiesManager(cookieStore);
    }

    /**
     * 单例模式，提供一个Retrofit.Builder对象
     */
    @Singleton
    @Provides
    Retrofit.Builder provideRetrofitBuilder() {
        return new Retrofit.Builder();
    }

    /**
     * 单例模式，提供一个Retrofit.Builder对象（命名为strategy_retrofit）,需通过名称使用
     */
    @Singleton
    @Provides
    @Named("strategy_retrofit")
    Retrofit.Builder provideSTRetrofitBuilder() {
        return new Retrofit.Builder();
    }

    @Singleton
    @Provides
    @StrategyUrl
        //使用@Qualifier 解决依赖迷失
    Retrofit provideStrategyRetrofit(@Named("strategy_retrofit") Retrofit.Builder builder, OkHttpClient client) {
        return createRetrofit(builder, client, CustomConverterFactory.create(), StrategyApis.HOST);
    }

    @Singleton
    @Provides
    StrategyApis provideStrategyService(@StrategyUrl Retrofit retrofit) {
        return retrofit.create(StrategyApis.class);
    }

    @Singleton
    @Provides
    OkHttpClient.Builder provideOkHttpBuilder(CookiesManager manager) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.cookieJar(manager);
        return client;
    }

    @Singleton
    @Provides
    OkHttpClient provideClient(OkHttpClient.Builder builder) {
        File cacheFile = new File(Constants.PATH_CACHE);
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!SystemUtil.isNetworkConnected()) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                if (SystemUtil.isNetworkConnected()) {
                    int maxAge = 0;
                    // 有网络时, 不缓存, 最大保存时长为0
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")
                            .build();
                } else {
                    // 无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                return response;
            }
        };
//        Interceptor apikey = new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
//                request = request.newBuilder()
//                        .addHeader("apikey",Constants.KEY_API)
//                        .build();
//                return chain.proceed(request);
//            }
//        }
//        设置统一的请求头部参数
//        builder.addInterceptor(apikey);
        //debug模式下打印日志
//        if (BuildConfig.SHOW_LOG) {
//            builder.addInterceptor(getHttpLoggingInterceptor());
//        }
        //设置缓存
        builder.addNetworkInterceptor(cacheInterceptor);
        builder.addInterceptor(cacheInterceptor);
        builder.cache(cache);
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        // TODO: 2018/6/11 正式打包需开启
        builder.retryOnConnectionFailure(false);
        return builder.build();
    }

//    private Retrofit createRetrofit(Retrofit.Builder builder, OkHttpClient client, String url) {
//        return createRetrofit(builder, client, CustomConverterFactory.create(), url);
//    }

    private Retrofit createRetrofit(Retrofit.Builder builder, OkHttpClient client, Converter.Factory converterFactory, String url) {
        return builder
                .baseUrl(url)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(converterFactory)
                .build();
    }

    /**
     * 日志输出
     */
    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
        //日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                if (BuildConfig.DEBUG) {
                    System.out.println(message);
//                    try {
//                        JSONObject jsonObject;
//                        if (message.contains("opact")) {
//                            jsonObject = new JSONObject(message);
//                            String url = jsonObject.optString("opact");
//                            System.out.println("liyuu request => " + url + "  " + message + "\n");
//                        } else if (message.contains("en_data")) {
//                            jsonObject = new JSONObject(message);
//                            String en_data = jsonObject.optString("en_data");
//                            String newResPonse = AES.decrypt(en_data, "a*jyxga#+wdfa%nd");
//                            System.out.println("liyuu response=> " + newResPonse + "\n");
//                        } else {
//                            System.out.println("liyuu => " + message + "\n");
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        System.out.println("liyuu => " + message + "\n");
//                    }

//                    if (message.contains("<!DOCTYPE html>"))
//                        HttpErrorActivity.start(App.getInstance(), message);
                }
            }
        });
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }

}
