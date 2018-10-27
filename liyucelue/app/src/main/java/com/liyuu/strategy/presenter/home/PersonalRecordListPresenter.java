package com.liyuu.strategy.presenter.home;

import com.liyuu.strategy.app.UrlConfig;
import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.contract.home.PersonalRecordListContract;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.model.bean.RankTradeBean;
import com.liyuu.strategy.widget.rx.CommonSubscriber;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class PersonalRecordListPresenter extends RxPresenter<PersonalRecordListContract.View> implements PersonalRecordListContract.Presenter {

    private DataManager mDataManager;

    @Inject
    public PersonalRecordListPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }


    @Override
    public void getRecordList(int type, int rankId, int page) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);//1.5日 2.全部
        params.put("rank_id", rankId);
        params.put("page", page);
        String url = UrlConfig.Rank_settleInfo;
        post(false, mDataManager.fetchRankTradeInfo(url, params),
                new CommonSubscriber<List<RankTradeBean>>(mView, url) {
                    @Override
                    public void onNext(List<RankTradeBean> rankTradeBeans) {
                        super.onNext(rankTradeBeans);
                        mView.showRecordList(rankTradeBeans);
                    }
                });
    }
}
