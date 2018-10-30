package com.gsclub.strategy.presenter.mine;


import com.gsclub.strategy.app.UrlConfig;
import com.gsclub.strategy.base.RxPresenter;
import com.gsclub.strategy.contract.mine.MineRecordContract;
import com.gsclub.strategy.model.DataManager;
import com.gsclub.strategy.model.bean.RankTradeBean;
import com.gsclub.strategy.model.bean.RankUserBean;
import com.gsclub.strategy.widget.rx.CommonSubscriber;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


public class MineRecordPresenter extends RxPresenter<MineRecordContract.View> implements MineRecordContract.Presenter {
    private DataManager mDataManager;

    @Inject
    MineRecordPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getRecord() {
        String url = UrlConfig.user_myRecord;
        post(mDataManager.getMyRecord(url),
                new CommonSubscriber<RankUserBean>(mView, url) {
                    @Override
                    public void onNext(RankUserBean data) {
                        super.onNext(data);
                        mView.showRecord(data);
                    }
                });
    }

    @Override
    public void getHistory(int page) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        String url = UrlConfig.user_myHistory;
        post(mDataManager.getMyHistory(url, params),
                new CommonSubscriber<List<RankTradeBean>>(mView, url) {
                    @Override
                    public void onNext(List<RankTradeBean> rankTradeBeans) {
                        super.onNext(rankTradeBeans);
                        mView.showHistory(rankTradeBeans);
                    }
                });
    }
}
