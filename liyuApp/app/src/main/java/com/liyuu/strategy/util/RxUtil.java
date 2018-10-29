package com.liyuu.strategy.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.liyuu.strategy.app.AppConfig;
import com.liyuu.strategy.http.GsonUtil;
import com.liyuu.strategy.http.exception.ApiException;
import com.liyuu.strategy.http.response.EnDataResponse;
import com.liyuu.strategy.http.response.StrategyHttpResponse;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxUtil {

    /**
     * 统一线程处理
     */
    public static <T> FlowableTransformer<T, T> rxSchedulerHelper() {    //compose简化线程
        return new FlowableTransformer<T, T>() {
            @Override
            public Flowable<T> apply(Flowable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 统一返回结果处理
     *
     * @param <T> 返回解析的实体类
     */
    public static <T> FlowableTransformer<StrategyHttpResponse<T>, T> handleHPResult() {   //compose判断结果
        return new FlowableTransformer<StrategyHttpResponse<T>, T>() {
            @Override
            public Flowable<T> apply(Flowable<StrategyHttpResponse<T>> httpResponseFlowable) {
                return httpResponseFlowable.flatMap(new Function<StrategyHttpResponse<T>, Flowable<T>>() {
                    @Override
                    public Flowable<T> apply(StrategyHttpResponse<T> response) {
                        //需要根据不同的code进行相应的处理
                        if (response.getCode() == 1000) {
                            return createData(response.getData());
                        } else if (response.getCode() == 1098) {
                            UserInfoUtil.doSingleSignOn();
                        } else if (response.getCode() == 1099) {
                            UserInfoUtil.doLogIn();
                        } else if(response.getCode() == 1001 && "未登录".equals(response.getMsg())) {
                            UserInfoUtil.logout();
                        }
                        return Flowable.error(new ApiException(response.getMsg(), response.getCode()));
                    }
                });
            }
        };
    }

    /**
     * 生成Flowable
     */
    private static <T> Flowable<T> createData(final T t) {
        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> emitter) throws Exception {
                try {
                    emitter.onNext(t);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        }, BackpressureStrategy.BUFFER);
    }
}
