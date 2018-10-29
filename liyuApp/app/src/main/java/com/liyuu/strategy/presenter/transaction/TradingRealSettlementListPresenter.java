package com.liyuu.strategy.presenter.transaction;

import com.liyuu.strategy.app.UrlConfig;
import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.contract.transaction.TradingRealPositionListContract;
import com.liyuu.strategy.contract.transaction.TradingRealSettlementListContract;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.model.bean.RealTradingPositionBean;
import com.liyuu.strategy.model.bean.RealTradingSettlementBean;
import com.liyuu.strategy.model.bean.SaleStockOrderBean;
import com.liyuu.strategy.widget.rx.CommonSubscriber;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class TradingRealSettlementListPresenter extends RxPresenter<TradingRealSettlementListContract.View>
        implements TradingRealSettlementListContract.Presenter {

    private final static int PAGE_SIZE = 10;
    private DataManager mDataManager;
    private int page = 1;

    @Inject
    TradingRealSettlementListPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getTradingRealSettlementData(final boolean isRefresh) {
        if (isRefresh)
            page = 1;

        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("page_size", PAGE_SIZE);

        post(mDataManager.getRealTradingSettlementData(UrlConfig.Trade_settleList, map),
                new CommonSubscriber<RealTradingSettlementBean>(mView, UrlConfig.Trade_settleList) {
                    @Override
                    public void onNext(RealTradingSettlementBean data) {
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
