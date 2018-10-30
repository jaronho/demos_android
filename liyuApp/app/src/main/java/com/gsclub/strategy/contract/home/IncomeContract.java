package com.gsclub.strategy.contract.home;

import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.model.bean.IncomeListBean;
import com.gsclub.strategy.model.bean.IncomeTypesBean;

import java.util.List;

public interface IncomeContract {

    interface View extends BaseView {
        void loadTabs(List<IncomeTypesBean> tabs);
    }

    interface Presenter extends BasePresenter<View> {
        void getIncomeType();
    }
}
