package com.gsclub.strategy.contract.stock;

import android.content.Context;

import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.ui.stock.other.bean.SelectStockBean;

import java.util.List;

public interface StockContract {

    interface View extends BaseView {
        void showDefaultData(List<SelectStockBean> one, List<SelectStockBean> two);

        void showStockRealData(String data);

//        void showStockPay(List<StockDetailPayBean.ItemBean> datas);

//        void startToPayFrozenAct(StockOrderDetailBean bean);

//        void showPayLateList(List<StockLatePayListBean.SonBean> datas);

        void setPayStateAndNotifyMsg(int state, String prohibitMsg);

//        void showBigOrder(List<StockLatePayListBean.SonBean> datas);

//        void showDefaultSelect(int position, StockDetailPayBean.ItemBean bean);
    }

    interface Presenter extends BasePresenter<View> {
        void getDefaultData(Context context);

        void getStockReal(String symbol);

        //获取该股票的风控数据
//        void getStockPayData(String symbol);

        //找搭档下单
//        void findCooperation(StockDetailPayBean.ItemBean bean, String period, String stockSymbol, String stockName);

//        void getStockPayLatestList(String stockCode);

        //获取k线图类型
        int getKlineType(int position);
    }
}
