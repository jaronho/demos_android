package com.gsclub.strategy.contract.stock;

import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.model.bean.SearchStockBean;

import java.util.List;

public interface SearchStockContract {

    interface View extends BaseView {
        void setStocksData(List<SearchStockBean> stocks);

        void showSearchStockHistory(List<SearchStockBean> datas);

        void hideRecyclerView();
    }

    interface Presenter extends BasePresenter<View> {
        //搜索股票
        void searchStocks(String symbol);

        //获取股票搜索记录
        void getLocalSearchStockHistory();

        //清除本地搜索记录
        void clearLocalSearchHistory();

        //插入查询历史数据
        void insertData(SearchStockBean bean);
    }
}
