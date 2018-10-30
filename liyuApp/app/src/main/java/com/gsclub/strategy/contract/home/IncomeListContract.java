package com.gsclub.strategy.contract.home;

import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.model.bean.IncomeListBean;

import java.util.List;

public interface IncomeListContract {

    interface View extends BaseView {
        void loadIncomeListInfo(IncomeListBean data);
    }

    interface Presenter extends BasePresenter<View> {
        void getIncome(int incomeType);
    }
}
