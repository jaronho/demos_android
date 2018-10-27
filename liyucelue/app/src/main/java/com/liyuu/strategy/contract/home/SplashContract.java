package com.liyuu.strategy.contract.home;


import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;

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
