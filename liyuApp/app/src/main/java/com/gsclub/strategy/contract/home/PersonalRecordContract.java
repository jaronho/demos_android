package com.gsclub.strategy.contract.home;

import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.model.bean.RankUserBean;

public interface PersonalRecordContract {

    interface View extends BaseView {
        void showRankUserMessage(RankUserBean data);
    }

    interface Presenter extends BasePresenter<View> {
        void getUserDetail(int rankId);
    }
}
