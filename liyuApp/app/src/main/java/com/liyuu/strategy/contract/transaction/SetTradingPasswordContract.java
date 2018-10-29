package com.liyuu.strategy.contract.transaction;


import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;

public interface SetTradingPasswordContract {

    interface View extends BaseView {
        //获取验证码后，进行倒计时
        void setVerificationCodeText(boolean isEnable, String text);

    }

    interface Presenter extends BasePresenter<View> {
        //获取验证码
        void getVerificationCode();

        void changeTradePass(String sms_code, String pay_pwd);
    }
}
