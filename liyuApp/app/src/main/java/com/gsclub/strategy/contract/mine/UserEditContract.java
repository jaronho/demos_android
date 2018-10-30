package com.gsclub.strategy.contract.mine;

import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

/**
 * 用户资料修改界面
 */

public interface UserEditContract {

    interface View extends BaseView {
        void refreshHeader();
        void selectPhoto();
        void takePhoto();
    }

    interface Presenter extends BasePresenter<UserEditContract.View> {
        void updataHeader(File bitmap);
        void checkPermissions(RxPermissions rxPermissions);
        void checkSelectPermissions(RxPermissions rxPermissions);
    }
}
