package com.liyuu.strategy.contract;


import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;

public interface NullContract {

    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter<View> {

    }
}
