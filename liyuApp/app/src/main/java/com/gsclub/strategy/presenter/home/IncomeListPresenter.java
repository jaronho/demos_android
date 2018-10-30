package com.gsclub.strategy.presenter.home;

import com.gsclub.strategy.app.UrlConfig;
import com.gsclub.strategy.base.RxPresenter;
import com.gsclub.strategy.contract.home.IncomeListContract;
import com.gsclub.strategy.model.DataManager;
import com.gsclub.strategy.model.bean.IncomeListBean;
import com.gsclub.strategy.widget.rx.CommonSubscriber;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class IncomeListPresenter extends RxPresenter<IncomeListContract.View> implements IncomeListContract.Presenter {

    private DataManager mDataManager;

    @Inject
    public IncomeListPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }


    @Override
    public void getIncome(int incomeType) {
        String url = UrlConfig.Rank_rankList;
        Map<String, Object> params = new HashMap<>();
        params.put("type", String.valueOf(incomeType));
        post(false, mDataManager.getIncomeList(url, params), new CommonSubscriber<IncomeListBean>(mView, url) {
            @Override
            public void onNext(IncomeListBean data) {
                mView.loadIncomeListInfo(data);
            }
        });
    }
}
