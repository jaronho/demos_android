package com.gsclub.strategy.contract.transaction;

import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.model.bean.BankBean;

import java.util.List;

public interface BankListContract {

    interface View extends BaseView {
        void loadData(List<BankBean> data);
    }

    interface Presenter extends BasePresenter<View> {
        void getBankList();
    }
}
