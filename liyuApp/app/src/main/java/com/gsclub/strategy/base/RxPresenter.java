package com.gsclub.strategy.base;

import com.gsclub.strategy.app.SPKeys;
import com.gsclub.strategy.component.RxBus;
import com.gsclub.strategy.http.response.StrategyHttpResponse;
import com.gsclub.strategy.model.DataManager;
import com.gsclub.strategy.model.bean.WebSetBean;
import com.gsclub.strategy.util.PreferenceUtils;
import com.gsclub.strategy.util.RxUtil;
import com.gsclub.strategy.widget.rx.CommonSubscriber;

import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 基于Rx的Presenter封装,控制订阅的生命周期
 */
public class RxPresenter<T extends BaseView> implements BasePresenter<T> {

    protected T mView;
    protected CompositeDisposable mCompositeDisposable;

    protected void unSubscribe() {
        if (mView != null)
            mView.hideLoading();
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }


    protected <U> void post(Flowable<StrategyHttpResponse<U>> flowable, CommonSubscriber<U> commonSubscriber) {
        addSubscribe(flowable
                .compose(RxUtil.<StrategyHttpResponse<U>>rxSchedulerHelper())
                .compose(RxUtil.<U>handleHPResult())
                .subscribeWith(commonSubscriber));
    }

    protected <U> void post(boolean isShowLoading, Flowable<StrategyHttpResponse<U>> flowable, CommonSubscriber<U> commonSubscriber) {
        addSubscribe(isShowLoading, flowable
                .compose(RxUtil.<StrategyHttpResponse<U>>rxSchedulerHelper())
                .compose(RxUtil.<U>handleHPResult())
                .subscribeWith(commonSubscriber));
    }

    protected void addSubscribe(Disposable subscription) {
        if (mView != null)
            mView.showLoading();
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

    protected void addSubscribe(boolean isShowLoading, Disposable subscription) {
        if (mView != null && isShowLoading)
            mView.showLoading();
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

    protected <U> void addRxBusSubscribe(Class<U> eventType, Consumer<U> act) {
        if (mView != null)
            mView.showLoading();
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(RxBus.get().toDefaultFlowable(eventType, act));
    }

    @Override
    public void attachView(T view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        if (mView != null)
            mView.hideLoading();
        this.mView = null;
        unSubscribe();
    }
}
