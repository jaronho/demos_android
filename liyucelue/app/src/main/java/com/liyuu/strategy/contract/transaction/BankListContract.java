package com.liyuu.strategy.contract.transaction;

import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;
import com.liyuu.strategy.model.bean.BankBean;

import java.util.List;

public interface BankListContract {

    interface View extends BaseView {
        void loadData(List<BankBean> data);
    }

    interface Presenter extends BasePresenter<View> {
        void getBankList();
    }
}
