package com.gsclub.strategy.presenter.transaction;


import com.gsclub.strategy.app.UrlConfig;
import com.gsclub.strategy.base.RxPresenter;
import com.gsclub.strategy.contract.transaction.SettlementDetailContract;
import com.gsclub.strategy.model.DataManager;
import com.gsclub.strategy.model.bean.StockSettleDetailBean;
import com.gsclub.strategy.widget.rx.CommonSubscriber;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class SettlementDetailPresenter extends RxPresenter<SettlementDetailContract.View>
        implements SettlementDetailContract.Presenter {
    private DataManager mDataManager;

    @Inject
    SettlementDetailPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getStockSettleDetail(String oid) {
        Map<String, Object> params = new HashMap<>();
        params.put("oid", oid);
        String url = UrlConfig.Trade_settleContent;
        post(mDataManager.fetchStockSettleDetail(url, params),
                new CommonSubscriber<StockSettleDetailBean>(mView, url) {
                    @Override
                    public void onNext(StockSettleDetailBean o) {
                        super.onNext(o);
                        if (o != null)
                            mView.showStockSettleDetail(o);
                    }
                });
    }
}
