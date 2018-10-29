package com.liyuu.strategy.presenter.transaction;

import com.liyuu.strategy.app.UrlConfig;
import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.contract.transaction.WithdrawContract;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.model.bean.CashIndexBean;
import com.liyuu.strategy.widget.rx.CommonSubscriber;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

public class WithdrawPresenter extends RxPresenter<WithdrawContract.View> implements WithdrawContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public WithdrawPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getCashIndex() {
        String url = UrlConfig.Account_cash_index;
        post(mDataManager.getCashIndex(url), new CommonSubscriber<CashIndexBean>(mView, url) {
            @Override
            public void onNext(CashIndexBean cashIndexBean) {
                super.onNext(cashIndexBean);
                mView.loadData(cashIndexBean);
            }
        });
    }

    @Override
    public void getCashOrder(final String money) {
        Map<String, Object> params = new HashMap<>();
        params.put("money", money);
        String url = UrlConfig.Account_cash_order;
        post(mDataManager.getCashOrder(url, params), new CommonSubscriber<Object>(mView, url) {
            @Override
            public void onNext(Object o) {
                super.onNext(o);
                mView.doNext(money);
            }
        });
    }
}
