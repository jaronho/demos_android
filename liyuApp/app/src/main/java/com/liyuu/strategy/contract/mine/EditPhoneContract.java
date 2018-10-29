package com.liyuu.strategy.contract.mine;

import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;

public interface EditPhoneContract {

    interface View extends BaseView {
        //获取验证码后，进行倒计时
        void setVerificationCodeText(boolean isEnable, String text);
        //验证完原手机号后，进入验证新手机号环节
        void setNewPhone();
    }

    interface Presenter extends BasePresenter<EditPhoneContract.View> {
        //获取验证码
        void getVerificationCode(String phone, int tel_type);

        //验证手机号
        void verifyPhone(String phone, String verification, int tel_type);
    }
}
