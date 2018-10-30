package com.gsclub.strategy.contract.transaction;


import android.content.Context;

import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.model.bean.TradeIndexBean;

public interface BuyingContract {

    interface View extends BaseView {
        //显示股票购买相关数据
        void showTradeIndex(TradeIndexBean data);

        //显示股票最新信息
        void showStockRealData(String originData, String stockName, double stockPrice, double pricedouble, double pricePrecent);

        void setStockNumberTextView(double stockPrice);

        void setStockNumber(int stockNumber);

        void setUserTurnover(double userTurnoverMoney);

        void setStopLossTextView(double tradeStopLossMoney, double stopPrecent);

        void setStockStopLossPriceTextView(double stopLossLossPrice);

        void setUserPayMoney(String text);

        void startToMainActivity();

        void startToSimulatedTradingActivity();

        void showMinute(String data);

        void showContinueMode(boolean isShowSwitch, int switchResouce, String switchMessage, String continueMessage);
    }

    interface Presenter extends BasePresenter<View> {
        //获取当前股票详情
        void getStockReal(String symbol, String fields);

        //交易下单
        void getTradeIndex(String symbol);

        /**
         * 用户确认押金
         *
         * @param stockPrice              当前股票价格
         * @param tradeBuyStockMoney      用户押金
         * @param gearing                 杆杠系数
         * @param tradeMaxStopLossPrecent 股票交易止损系数
         */
        void userSelectMargin(double stockPrice, double tradeBuyStockMoney, double gearing, double tradeMaxStopLossPrecent);

        /**
         * 用户更改
         *
         * @param action                  动作 1：+100 0：不变（用于股票价格变化刷新数据）-1：-100
         * @param stockPrice              股票价格
         * @param tradeBuyStockMoney      定金
         * @param tradeMaxStopLossPrecent 股票交易止损系数
         */
        void changeStockPayNumber(int action, double stockPrice, double tradeBuyStockMoney, double tradeMaxStopLossPrecent);

        /**
         * 模拟下单
         *
         * @param period        持股周期
         * @param tradeAllMondy 交易本金
         * @param tradeMoney    押金
         * @param stockNum      股票数量
         * @param stopLoss      止损线
         * @param lossPrice     止损价
         * @param symbol        股票代码
         * @param stockName     股票名称
         */
        void createSimulatedOrder(int period, double tradeAllMondy, double tradeMoney, int stockNum, double stopLoss,
                                  double lossPrice, String symbol, String stockName);

        /**
         * 创建订单
         *
         * @param symbol           股票编码
         * @param stockName        股票名称
         * @param deposit          押金
         * @param periodAutoStatus 是否开启递延  1开启 2关闭
         * @param stockNum         购买股票数量
         */
        void createOrder(String symbol, String stockName, double deposit, int periodAutoStatus, int stockNum);

        /**
         * 获取分时图数据
         */
        void getMinuteStock(String stockCode);

        /**
         * 加载递延模块文字数据
         */
        void loadContinueModeData(Context context, boolean isNotContinue);
    }
}
