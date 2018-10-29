package com.liyuu.strategy.contract.mine;


import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;
import com.liyuu.strategy.model.bean.RankTradeBean;
import com.liyuu.strategy.model.bean.RankUserBean;

import java.util.List;

/**
 * Created by hlw on 2018/5/22.
 */

public interface MineRecordContract {

    interface View extends BaseView {
        void showRecord(RankUserBean data);
        void showHistory(List<RankTradeBean> data);
    }

    interface Presenter extends BasePresenter<View> {
        void getRecord();
        void getHistory(int page);
    }
}
