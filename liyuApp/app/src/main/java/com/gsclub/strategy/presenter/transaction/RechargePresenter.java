package com.gsclub.strategy.presenter.transaction;

import com.gsclub.strategy.app.UrlConfig;
import com.gsclub.strategy.base.RxPresenter;
import com.gsclub.strategy.contract.transaction.RechargeContract;
import com.gsclub.strategy.model.DataManager;
import com.gsclub.strategy.model.bean.PayInfoBean;
import com.gsclub.strategy.model.bean.RechargeIndexBean;
import com.gsclub.strategy.widget.rx.CommonSubscriber;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class RechargePresenter extends RxPresenter<RechargeContract.View> implements RechargeContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public RechargePresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getRechargeIndex() {
        String url = UrlConfig.Account_recharge_index;
        post(mDataManager.getRechargeIndex(url), new CommonSubscriber<RechargeIndexBean>(mView, url) {
            @Override
            public void onNext(RechargeIndexBean data) {
                super.onNext(data);
                mView.loadData(data);
            }
        });
    }

    @Override
    public void getRechargeOrder(String money) {
        Map<String, Object> params = new HashMap<>();
        params.put("money", money);
        String url = UrlConfig.Account_recharge_order;
        post(mDataManager.getRechargeOrder(url, params), new CommonSubscriber<PayInfoBean>(mView, url) {
            @Override
            public void onNext(PayInfoBean data) {
                super.onNext(data);
                mView.recharge(data);
            }
        });
    }

    @Override
    public void checkRechargeOrder(String trade_no) {
        Map<String, Object> params = new HashMap<>();
        params.put("trade_no", trade_no);
        String url = UrlConfig.Account_chk_recharge_order;
        post(mDataManager.customPost(url, params), new CommonSubscriber<Object>(mView, url) {
            @Override
            public void onNext(Object bean) {
                super.onNext(bean);
                mView.doSuccess();
            }
        });
    }
}
