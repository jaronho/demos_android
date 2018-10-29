package com.liyuu.strategy.presenter.transaction;

import com.liyuu.strategy.app.UrlConfig;
import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.contract.transaction.TradingUnLogContract;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.model.bean.ActivityImagesBean;
import com.liyuu.strategy.widget.rx.CommonSubscriber;
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
