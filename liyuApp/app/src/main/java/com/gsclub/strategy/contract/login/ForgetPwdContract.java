package com.gsclub.strategy.contract.login;

import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;

/**
 * 修改密码操作接口
 */

public interface ForgetPwdContract {

    interface View extends BaseView {
        //获取验证码后，进行倒计时
        void setVerificationCodeText(boolean isEnable, String text);
        //验证完手机号后，进入重置密码环节
        void setPwdView();
    }

    interface Presenter extends BasePresenter<ForgetPwdContract.View> {
        //获取验证码
        void getVerificationCode(String phone);

        //修改密码第一步
        void editPwd(String phone, String verification, String pwd, String rePwd);
    }
}
