package com.liyuu.strategy.contract.stock;

import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;
import com.liyuu.strategy.ui.stock.other.bean.StockGsonBean;

import java.util.List;

public interface FiveDayContract {

    interface View extends BaseView {
        void loadBarAndLineChartView(List<String> xDataList, List<List<StockGsonBean>> lists);
    }

    interface Presenter extends BasePresenter<View> {
        void getFiveDayLine(String symbolName);
    }
}
