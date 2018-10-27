package com.liyuu.strategy.contract.home;

import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;
import com.liyuu.strategy.model.bean.RankTradeBean;

import java.util.List;

public interface PersonalRecord5DYieldContract {

    interface View extends BaseView {
        void showRecordList(List<RankTradeBean> datas);
    }

    interface Presenter extends BasePresenter<View> {
        void getRecordList(int typ, int rankId, int page);
    }
}
