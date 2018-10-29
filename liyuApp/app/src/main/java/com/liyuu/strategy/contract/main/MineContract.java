package com.liyuu.strategy.contract.main;

import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;

public interface MineContract {

    interface View extends BaseView {
        //根据app是否正在核审对相应view进行隐藏
        void checkViewChange(boolean isChecking);
    }

    interface Presenter extends BasePresenter<View> {

        //判断是否是审核版本
        void checkStatus();
    }
}
