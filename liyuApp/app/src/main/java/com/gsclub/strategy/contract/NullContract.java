package com.gsclub.strategy.contract;


import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;

public interface NullContract {

    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter<View> {

    }
}
