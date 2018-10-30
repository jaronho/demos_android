package com.gsclub.strategy.presenter.transaction;


import com.gsclub.strategy.app.UrlConfig;
import com.gsclub.strategy.base.RxPresenter;
import com.gsclub.strategy.contract.NullContract;
import com.gsclub.strategy.contract.transaction.PayAccountManageContract;
import com.gsclub.strategy.model.DataManager;
import com.gsclub.strategy.model.bean.UserBankBean;
import com.gsclub.strategy.widget.rx.CommonSubscriber;

import javax.inject.Inject;

public class PayAccountManagePresenter extends RxPresenter<PayAccountManageContract.View> implements PayAccountManageContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public PayAccountManagePresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getBankCardInfo() {
        String url = UrlConfig.UserBank_getUserBankInfo;
        post(mDataManager.getBankCardInfo(url), new CommonSubscriber<UserBankBean>(mView, url) {
            @Override
            public void onNext(UserBankBean bean) {
                super.onNext(bean);
                mView.setBankCardInfo(bean);
            }
        });
    }
}
