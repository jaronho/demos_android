package com.liyuu.strategy.contract.transaction;

import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;
import com.liyuu.strategy.model.bean.BillListBean;
import com.liyuu.strategy.ui.transaction.adapter.BillListAdapter;

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
