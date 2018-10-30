package com.gsclub.strategy.contract.stock;

import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;

public interface KLineStockContract {

    interface View extends BaseView {
        void loadKLine(String data);
    }

    interface Presenter extends BasePresenter<View> {
        /**
         * 获取k线数据
         *
         * @param symbol    股票代码
         * @param klineType k线类型
         * @param dataCount 每次获取的数据最高条数
         */
        void queryKLine(String symbol, int klineType, int dataCount);
    }
}