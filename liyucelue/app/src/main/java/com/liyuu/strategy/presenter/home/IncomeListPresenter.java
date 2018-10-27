package com.liyuu.strategy.presenter.home;

import com.liyuu.strategy.app.UrlConfig;
import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.contract.home.IncomeListContract;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.model.bean.IncomeListBean;
import com.liyuu.strategy.widget.rx.CommonSubscriber;

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
