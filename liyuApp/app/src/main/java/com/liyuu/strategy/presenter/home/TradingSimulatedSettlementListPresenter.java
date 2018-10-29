package com.liyuu.strategy.presenter.home;

import com.liyuu.strategy.app.UrlConfig;
import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.contract.home.TradingSimulatedSettlementListContract;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.model.SimulatedTradingSettlementBean;
import com.liyuu.strategy.widget.rx.CommonSubscriber;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class TradingSimulatedSettlementListPresenter extends RxPresenter<TradingSimulatedSettlementListContract.View>
        implements TradingSimulatedSettlementListContract.Presenter {

    private final static int PAGE_SIZE = 10;
    private DataManager mDataManager;
    private int page = 1;

    @Inject
    TradingSimulatedSettlementListPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getTradingSimulatedSettlementData(final boolean isRefresh) {
        if (isRefresh)
            page = 1;

        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("page_size", PAGE_SIZE);

        post(mDataManager.getSimulatedTradingSettlementData(UrlConfig.Simulated_simulatedSettleList, map),
                new CommonSubscriber<SimulatedTradingSettlementBean>(mView, UrlConfig.Simulated_simulatedSettleList) {
                    @Override
                    public void onNext(SimulatedTradingSettlementBean data) {
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

                        mView.showTradingRealSettlementList(isRefresh, data.getList());

                        page++;
                    }
                });
    }
}
