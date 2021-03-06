package com.gsclub.strategy.contract.main;

import android.content.Context;

import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;

public interface MainContract {
    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter<View> {
        void getWebSets();

        //初始化信鸽推送
        void initXGPush(Context context);

        //查询更新
        void checkUpdata();
    }
}
