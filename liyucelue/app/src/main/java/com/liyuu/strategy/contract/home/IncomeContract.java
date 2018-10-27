package com.liyuu.strategy.contract.home;

import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;
import com.liyuu.strategy.model.bean.IncomeListBean;
import com.liyuu.strategy.model.bean.IncomeTypesBean;

import java.util.List;

public interface IncomeContract {

    interface View extends BaseView {
        void loadTabs(List<IncomeTypesBean> tabs);
    }

    interface Presenter extends BasePresenter<View> {
        void getIncomeType();
    }
}
