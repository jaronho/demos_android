package com.gsclub.strategy.contract.mine;

import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.model.bean.FeedbackImageBean;
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
