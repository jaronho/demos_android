package com.liyuu.strategy.contract.transaction;


import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;
import com.liyuu.strategy.model.bean.NewIndexBean;
import com.liyuu.strategy.model.bean.StockBean;

public interface NewsWelfareContract {

    interface View extends BaseView {
        void loadData(StockBean data);

        void showIndex(NewIndexBean data);
        //显示股票最新信息
        void showStockRealData(String stockPrice, String price, String pricePercent);

        void setStockNumberTextView(double stockPrice);

        void setStockNumber(int stockNumber);

        void setUserTurnover(double userTurnoverMoney);

        void setStopLossTextView(double tradeStopLossMoney, double stopPrecent);

        void setStockStopLossPriceTextView(double stopLossLossPrice);

        void showCloseDialog();
    }

    interface Presenter extends BasePresenter<View> {
        void getStockSingle();

        void getNewIndex(String stockSymbol);
        //获取实时股票详情
        void getStockReal(String symbol);

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
         * @param tradeMaxStopLossPercent 股票交易止损系数
         */
        void changeStockPayNumber(int action, double stockPrice, double tradeBuyStockMoney, double tradeMaxStopLossPercent);

        /**
         * 创建订单
         *
         * @param symbol    股票编码
         * @param stockName 股票名称
         * @param deposit   押金
         * @param period    持股周期
         * @param stockNum  购买股票数量
         */
        void createOrder(String symbol, String stockName, double deposit, int period, int stockNum);

        /**
         * 验证登录用户是否已经使用该活动，使用过弹窗
         */
        void checkStatus();
    }
}
