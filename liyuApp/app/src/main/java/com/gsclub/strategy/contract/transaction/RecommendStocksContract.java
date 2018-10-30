package com.gsclub.strategy.contract.transaction;

import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.model.bean.StockBean;
import com.gsclub.strategy.ui.transaction.adapter.RecommendStocksAdapter;

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
