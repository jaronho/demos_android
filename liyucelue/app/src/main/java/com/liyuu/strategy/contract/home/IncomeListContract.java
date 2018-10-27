package com.liyuu.strategy.contract.home;

import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;
import com.liyuu.strategy.model.bean.IncomeListBean;

import java.util.List;

public interface IncomeListContract {

    interface View extends BaseView {
        void loadIncomeListInfo(IncomeListBean data);
    }

    interface Presenter extends BasePresenter<View> {
        void getIncome(int incomeType);
    }
}
