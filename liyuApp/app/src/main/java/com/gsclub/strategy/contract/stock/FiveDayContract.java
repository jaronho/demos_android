package com.gsclub.strategy.contract.stock;

import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.ui.stock.other.bean.StockGsonBean;

import java.util.List;

public interface FiveDayContract {

    interface View extends BaseView {
        void loadBarAndLineChartView(List<String> xDataList, List<List<StockGsonBean>> lists);
    }

    interface Presenter extends BasePresenter<View> {
        void getFiveDayLine(String symbolName);
    }
}
