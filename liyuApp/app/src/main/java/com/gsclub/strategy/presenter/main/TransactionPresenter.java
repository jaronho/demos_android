package com.gsclub.strategy.presenter.main;

import com.gsclub.strategy.app.UrlConfig;
import com.gsclub.strategy.base.RxPresenter;
import com.gsclub.strategy.contract.main.TransactionContract;
import com.gsclub.strategy.model.DataManager;
import com.gsclub.strategy.model.bean.UserTradeInfoBean;
import com.gsclub.strategy.util.LogUtil;
import com.gsclub.strategy.widget.rx.CommonSubscriber;

import javax.inject.Inject;

public class TransactionPresenter extends RxPresenter<TransactionContract.View> implements TransactionContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public TransactionPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(TransactionContract.View view) {
        super.attachView(view);
    }

    @Override
    public void getUserTradeInfo() {
        String url = UrlConfig.Trade_accountDetail;
        post(false, mDataManager.fetchUserTradeInfo(url),
                new CommonSubscriber<UserTradeInfoBean>(mView, url) {
                    @Override
                    public void onNext(UserTradeInfoBean data) {
                        super.onNext(data);
                        if (data != null)
                            mView.showUserTradeInfo(data);
                    }
                });
    }
}
