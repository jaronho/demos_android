package com.liyuu.strategy.contract.transaction;


import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;
import com.liyuu.strategy.model.bean.ActivityImagesBean;

public interface TradingUnLogContract {

    interface View extends BaseView {
        void showActivity(ActivityImagesBean bean);
    }

    interface Presenter extends BasePresenter<View> {
        void getActivityImages();
    }
}
