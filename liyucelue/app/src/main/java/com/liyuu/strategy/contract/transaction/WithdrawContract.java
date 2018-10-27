package com.liyuu.strategy.contract.transaction;


import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;
import com.liyuu.strategy.model.bean.CashIndexBean;

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
