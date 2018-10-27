package com.liyuu.strategy.contract.home;

import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;
import com.liyuu.strategy.model.bean.SimulatedTradingPositionBean;

import java.util.List;

public interface TradingSimulatedPositionListContract {

    interface View extends BaseView {
        //显示获取到的列表数据
        void showTradingSimulatedPositionList(boolean isRefresh, List<SimulatedTradingPositionBean.ListBean> datas);

        //下拉刷新数据为空时显示空界面
        void showEmptyPage();

        //是由允许下拉加载更多（有后续数据可以加载）
        void enableLoadMore(boolean isEnableLoadMore);

        //停止上拉加载/下拉刷新动画
        void stopRefreshLayoutAnim();

        void saleSuccess();

        //刷新股票最新价格
        void refreshStockLastPrice(String symbol, double lastPrice);
    }

    interface Presenter extends BasePresenter<View> {

        /**
         * 获取真实交易--委托列表数据
         *
         * @param isRefresh 是否重新加载数据（刷新数据）
         */
        void getTradingSimulatedPositionData(boolean isRefresh);

        /**
         * 出售股票
         *
         * @param oid    订单id
         * @param symbol 股票代码
         */
        void sellOrder(String oid, String symbol);

        /**
         * 刷新股票最新价格
         *
         * @param symbol 股票代码
         */
        void refreshStockLastPrice(String symbol);
    }
}
