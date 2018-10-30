package com.gsclub.strategy.contract.home;

import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.model.bean.SimulatedInfoBean;

public interface SimulatedTradingContract {

    interface View extends BaseView {

        void showUserSimulatedInfo(SimulatedInfoBean data);

    }

    interface Presenter extends BasePresenter<View> {

        void getUserSimulatedInfo();

    }
}
