package com.gsclub.strategy.presenter.transaction;

import com.gsclub.strategy.app.UrlConfig;
import com.gsclub.strategy.base.RxPresenter;
import com.gsclub.strategy.contract.transaction.TradingUnLogContract;
import com.gsclub.strategy.model.DataManager;
import com.gsclub.strategy.model.bean.ActivityImagesBean;
import com.gsclub.strategy.widget.rx.CommonSubscriber;
import javax.inject.Inject;

public class TradingUnLogPresenter extends RxPresenter<TradingUnLogContract.View> implements TradingUnLogContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public TradingUnLogPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getActivityImages() {
        String url = UrlConfig.Index_getActivityImages;
        post(mDataManager.getActivityImages(url), new CommonSubscriber<ActivityImagesBean>(mView, url) {
            @Override
            public void onNext(ActivityImagesBean bean) {
                super.onNext(bean);
                mView.showActivity(bean);
            }
        });
    }
}
