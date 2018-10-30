package com.gsclub.strategy.presenter.transaction;

import com.gsclub.strategy.app.UrlConfig;
import com.gsclub.strategy.base.RxPresenter;
import com.gsclub.strategy.contract.transaction.TradingRealCommissionListContract;
import com.gsclub.strategy.model.DataManager;
import com.gsclub.strategy.model.bean.RealTradingCommissionBean;
import com.gsclub.strategy.widget.rx.CommonSubscriber;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class TradingRealCommissionListPresenter extends RxPresenter<TradingRealCommissionListContract.View>
        implements TradingRealCommissionListContract.Presenter {

    private final static int PAGE_SIZE = 10;
    private DataManager mDataManager;
    private int page = 1;

    @Inject
    TradingRealCommissionListPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }


    @Override
    public void getTradingRealCommissionData(final boolean isRefresh) {
        if (isRefresh)
            page = 1;

        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("page_size", PAGE_SIZE);

        post(mDataManager.getRealTradingCommissionData(UrlConfig.Trade_entrustmentList, map),
                new CommonSubscriber<RealTradingCommissionBean>(mView, UrlConfig.Trade_entrustmentList) {
                    @Override
                    public void onNext(RealTradingCommissionBean data) {
                        super.onNext(data);
                        mView.stopRefreshLayoutAnim();

                        boolean isNullData = (data == null || data.getList() == null || data.getList().size() == 0);

                        //下拉刷新为空显示空界面
                        if (isRefresh && isNullData) {
                            mView.showEmptyPage();
                            return;
                        }

                        //再次获取数据为空关闭上拉加载更多
                        if (isNullData) {
                            mView.enableLoadMore(false);
                            return;
                        }

                        boolean isEnableLoadMore = data.getList().size() == PAGE_SIZE;
                        mView.enableLoadMore(isEnableLoadMore);

                        mView.showTradingRealCommissionList(isRefresh, data.getList());

                        page++;

                    }
                });
    }
}
