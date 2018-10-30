package com.gsclub.strategy.contract.transaction;

import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.model.bean.BillListBean;
import com.gsclub.strategy.ui.transaction.adapter.BillListAdapter;

import java.util.List;

public interface BillListContract {

    interface View extends BaseView {
        void loadData(List<BillListBean.ListBean> data);
        void doRefreshFinish();
    }

    interface Presenter extends BasePresenter<View> {
        void getList(int type, BillListAdapter adapter);
    }
}
