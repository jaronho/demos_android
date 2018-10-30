package com.gsclub.strategy.contract.transaction;


import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.model.bean.CashIndexBean;

public interface WithdrawContract {

    interface View extends BaseView {
        void loadData(CashIndexBean data);
        void doNext(String money);
    }

    interface Presenter extends BasePresenter<View> {
        void getCashIndex();
        void getCashOrder(String money);
    }
}
