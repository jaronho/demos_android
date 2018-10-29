package com.liyuu.strategy.presenter.main;

import com.liyuu.strategy.app.UrlConfig;
import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.contract.main.TransactionContract;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.model.bean.UserTradeInfoBean;
import com.liyuu.strategy.util.LogUtil;
import com.liyuu.strategy.widget.rx.CommonSubscriber;

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
