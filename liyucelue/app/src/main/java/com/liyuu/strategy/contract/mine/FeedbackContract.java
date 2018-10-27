package com.liyuu.strategy.contract.mine;

import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;
import com.liyuu.strategy.model.bean.FeedbackImageBean;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

public interface FeedbackContract {

    interface View extends BaseView {
        void selectPhoto();
        void takePhoto();
        void uploadSuccess(FeedbackImageBean bean);
    }

    interface Presenter extends BasePresenter<FeedbackContract.View> {
        void upLoadPhoto(File image);
        void feedback(String content, String contact, String picture);
        void checkPermissions(RxPermissions rxPermissions);
        void checkSelectPermissions(RxPermissions rxPermissions);
    }
}
