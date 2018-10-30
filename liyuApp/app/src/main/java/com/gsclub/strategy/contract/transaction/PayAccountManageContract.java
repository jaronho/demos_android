package com.gsclub.strategy.contract.transaction;


import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.model.bean.UserBankBean;

public interface PayAccountManageContract {

    interface View extends BaseView {
        void setBankCardInfo(UserBankBean data);
    }

    interface Presenter extends BasePresenter<View> {
        void getBankCardInfo();
    }
}
