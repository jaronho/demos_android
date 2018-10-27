package com.liyuu.strategy.contract.mine;

import android.content.Context;

import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;
import com.liyuu.strategy.model.bean.AppUpdateBean;

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
