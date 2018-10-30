package com.gsclub.strategy.contract.home;

import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.model.bean.RankTradeBean;

import java.util.List;

public interface PersonalRecord5DYieldContract {

    interface View extends BaseView {
        void showRecordList(List<RankTradeBean> datas);
    }

    interface Presenter extends BasePresenter<View> {
        void getRecordList(int typ, int rankId, int page);
    }
}
