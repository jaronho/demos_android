package com.gsclub.strategy.contract.transaction;


import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.model.bean.ActivityImagesBean;

public interface TradingUnLogContract {

    interface View extends BaseView {
        void showActivity(ActivityImagesBean bean);
    }

    interface Presenter extends BasePresenter<View> {
        void getActivityImages();
    }
}
