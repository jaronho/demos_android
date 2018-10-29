package com.liyuu.strategy.contract.transaction;


import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;
import com.liyuu.strategy.model.bean.StockSettleDetailBean;

public interface SettlementDetailContract {

    interface View extends BaseView {

        void showStockSettleDetail(StockSettleDetailBean data);
    }

    interface Presenter extends BasePresenter<View> {

        //获取订单结算详情
        void getStockSettleDetail(String oid);
    }
}
