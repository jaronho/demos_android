package com.gsclub.strategy.contract.home;


import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;

public interface SplashContract {

    interface View extends BaseView {
        void doActivityControl();
        void loginFinish();
    }

    interface Presenter extends BasePresenter<View> {
        void activityControl();
        void loginByOpenId(String open_id, String tel);
    }
}
