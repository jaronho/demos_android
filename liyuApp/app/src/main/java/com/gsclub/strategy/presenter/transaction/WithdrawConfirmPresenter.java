package com.gsclub.strategy.presenter.transaction;

import com.gsclub.strategy.app.UrlConfig;
import com.gsclub.strategy.base.RxPresenter;
import com.gsclub.strategy.contract.transaction.WithdrawConfirmContract;
import com.gsclub.strategy.model.DataManager;
import com.gsclub.strategy.ui.transaction.activity.RechargeResultActivity;
import com.gsclub.strategy.widget.rx.CommonSubscriber;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class WithdrawConfirmPresenter extends RxPresenter<WithdrawConfirmContract.View> implements WithdrawConfirmContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public WithdrawConfirmPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void confirmCashOrder(String money, String trading_password) {
        Map<String, Object> params = new HashMap<>();
        params.put("money", money);
        params.put("paypwd", trading_password);
        String url = UrlConfig.Account_cash_order;
        post(mDataManager.getCashOrder(url, params), new CommonSubscriber<Object>(mView, url) {
            @Override
            public void onNext(Object o) {
                super.onNext(o);
                mView.doSuccess();
            }

            @Override
            public void onMsg(int code, String msg) {
                mView.doFail(msg);
            }
        });
    }
}
