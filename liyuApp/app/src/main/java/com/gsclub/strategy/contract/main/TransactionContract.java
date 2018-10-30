package com.gsclub.strategy.contract.main;

import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.model.bean.UserTradeInfoBean;

public interface TransactionContract {
    interface View extends BaseView {

        void showUserTradeInfo(UserTradeInfoBean data);

    }

    interface Presenter extends BasePresenter<View> {
        //获取用户账户信息
        void getUserTradeInfo();
    }
}
