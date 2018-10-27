package com.liyuu.strategy.contract.main;

import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;
import com.liyuu.strategy.model.bean.UserTradeInfoBean;

public interface TransactionContract {
    interface View extends BaseView {

        void showUserTradeInfo(UserTradeInfoBean data);

    }

    interface Presenter extends BasePresenter<View> {
        //获取用户账户信息
        void getUserTradeInfo();
    }
}
