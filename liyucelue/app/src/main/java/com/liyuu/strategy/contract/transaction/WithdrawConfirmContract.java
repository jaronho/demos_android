package com.liyuu.strategy.contract.transaction;

import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;

public interface WithdrawConfirmContract {

    interface View extends BaseView {
        void doSuccess();
        void doFail(String message);
    }

    interface Presenter extends BasePresenter<View> {
        void confirmCashOrder(String money, String trading_password);
    }
}
