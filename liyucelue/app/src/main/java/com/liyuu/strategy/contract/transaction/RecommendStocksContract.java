package com.liyuu.strategy.contract.transaction;

import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;
import com.liyuu.strategy.model.bean.StockBean;
import com.liyuu.strategy.ui.transaction.adapter.RecommendStocksAdapter;

import java.util.List;

public interface RecommendStocksContract {

    interface View extends BaseView {
        void loadData(List<StockBean> data);
        void doRefreshFinish();
    }

    interface Presenter extends BasePresenter<View> {
        void getStockList(RecommendStocksAdapter adapter);
    }
}
