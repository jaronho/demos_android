package com.liyuu.strategy.contract.home;

import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;
import com.liyuu.strategy.model.bean.SimulatedInfoBean;

public interface SimulatedTradingContract {

    interface View extends BaseView {

        void showUserSimulatedInfo(SimulatedInfoBean data);

    }

    interface Presenter extends BasePresenter<View> {

        void getUserSimulatedInfo();

    }
}
