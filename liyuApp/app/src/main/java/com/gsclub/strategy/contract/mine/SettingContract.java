package com.gsclub.strategy.contract.mine;

import android.content.Context;

import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.model.bean.AppUpdateBean;

public interface SettingContract {

    interface View extends BaseView {
        void logoutSuccess();

        void showUpdate(boolean isFirst, AppUpdateBean bean);
    }

    interface Presenter extends BasePresenter<SettingContract.View> {
        void logout();

        //查询更新
        void checkUpdata(boolean isFirst);
    }
}
