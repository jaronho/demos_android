package com.gsclub.strategy.contract.transaction;

import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.model.bean.RealTradingPositionBean;
import com.gsclub.strategy.model.bean.RealTradingSettlementBean;

import java.util.List;

public interface TradingRealSettlementListContract {

    interface View extends BaseView {
        //显示获取到的列表数据
        void showTradingRealSettlementList(boolean isRefresh, List<RealTradingSettlementBean.ListBean> datas);

        //下拉刷新数据为空时显示空界面
        void showEmptyPage();

        //是由允许下拉加载更多（有后续数据可以加载）
        void enableLoadMore(boolean isEnableLoadMore);

        //停止上拉加载/下拉刷新动画
        void stopRefreshLayoutAnim();
    }

    interface Presenter extends BasePresenter<View> {

        /**
         * 获取真实交易--委托列表数据
         *
         * @param isRefresh 是否重新加载数据（刷新数据）
         */
        void getTradingRealSettlementData(boolean isRefresh);

    }
}
