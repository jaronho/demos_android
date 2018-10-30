package com.gsclub.strategy.presenter.home;

import com.gsclub.strategy.app.UrlConfig;
import com.gsclub.strategy.base.RxPresenter;
import com.gsclub.strategy.contract.home.PersonalRecordContract;
import com.gsclub.strategy.model.DataManager;
import com.gsclub.strategy.model.bean.RankUserBean;
import com.gsclub.strategy.widget.rx.CommonSubscriber;

import java.util.HashMap;

import javax.inject.Inject;

public class PersonalRecordPresenter extends RxPresenter<PersonalRecordContract.View> implements PersonalRecordContract.Presenter {

    private DataManager mDataManager;

    @Inject
    PersonalRecordPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getUserDetail(int rankId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("rank_id", rankId);
        String url = UrlConfig.Rank_rankInfo;
        post(mDataManager.fetchIncomeTypesInfo(url, map),
                new CommonSubscriber<RankUserBean>(mView, url) {
                    @Override
                    public void onNext(RankUserBean data) {
                        mView.showRankUserMessage(data);
                    }
                });
    }
}