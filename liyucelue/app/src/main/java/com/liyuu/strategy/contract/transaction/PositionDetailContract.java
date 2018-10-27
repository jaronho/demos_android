package com.liyuu.strategy.contract.transaction;


import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;
import com.liyuu.strategy.model.bean.PositionDetailBean;

public interface PositionDetailContract {

    interface View extends BaseView {

        void showPositionDetail(PositionDetailBean data);

        //股票止损价修改成功
        void changeStockStopLossPriceSuccess(String result);

        void changeStockAutoDeferredSuccess(String result);

    }

    interface Presenter extends BasePresenter<View> {

        //获取持仓详情
        void getPositionDetail(String oid);

        //止损价调整
        void changeStockStopLossPrice(String oid, double changeStopLossPrice);

        //股票递延
        void changeStockAutoDeferred(String oid, boolean isDeferred);

    }
}
