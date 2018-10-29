package com.liyuu.strategy.contract.home;

import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;
import com.liyuu.strategy.model.bean.RankUserBean;

public interface PersonalRecordContract {

    interface View extends BaseView {
        void showRankUserMessage(RankUserBean data);
    }

    interface Presenter extends BasePresenter<View> {
        void getUserDetail(int rankId);
    }
}
