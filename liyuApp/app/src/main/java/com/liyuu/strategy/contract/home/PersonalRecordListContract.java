package com.liyuu.strategy.contract.home;

import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;
import com.liyuu.strategy.model.bean.IncomeListBean;
import com.liyuu.strategy.model.bean.RankTradeBean;

import java.util.List;

public interface PersonalRecordListContract {

    interface View extends BaseView {
        void showRecordList(List<RankTradeBean> datas);
    }

    interface Presenter extends BasePresenter<View> {
        void getRecordList(int type, int rankId, int page);
    }
}
