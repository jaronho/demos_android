package com.gsclub.strategy.contract.transaction;


import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.model.bean.StockSettleDetailBean;

public interface SettlementDetailContract {

    interface View extends BaseView {

        void showStockSettleDetail(StockSettleDetailBean data);
    }

    interface Presenter extends BasePresenter<View> {

        //获取订单结算详情
        void getStockSettleDetail(String oid);
    }
}
