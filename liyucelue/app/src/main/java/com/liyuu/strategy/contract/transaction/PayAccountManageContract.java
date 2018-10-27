package com.liyuu.strategy.contract.transaction;


import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;
import com.liyuu.strategy.model.bean.UserBankBean;

public interface PayAccountManageContract {

    interface View extends BaseView {
        void setBankCardInfo(UserBankBean data);
    }

    interface Presenter extends BasePresenter<View> {
        void getBankCardInfo();
    }
}
