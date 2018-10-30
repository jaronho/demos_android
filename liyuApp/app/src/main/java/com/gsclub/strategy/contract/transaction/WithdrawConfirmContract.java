package com.gsclub.strategy.contract.transaction;

import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;

public interface WithdrawConfirmContract {

    interface View extends BaseView {
        void doSuccess();
        void doFail(String message);
    }

    interface Presenter extends BasePresenter<View> {
        void confirmCashOrder(String money, String trading_password);
    }
}
