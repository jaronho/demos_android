package com.liyuu.strategy.presenter.transaction;


import com.liyuu.strategy.app.UrlConfig;
import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.contract.NullContract;
import com.liyuu.strategy.contract.transaction.PayAccountManageContract;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.model.bean.UserBankBean;
import com.liyuu.strategy.widget.rx.CommonSubscriber;

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
