package com.gsclub.strategy.contract.login;

import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.model.bean.UserIndexBean;

/**
 * Created by hlw on 2018/5/22.
 * 登录操作接口
 */

public interface RegisterContract {

    interface View extends BaseView {

        //获取验证码后，进行倒计时
        void setVerificationCodeText(boolean isEnable, String text);

        //登录成功后，跳转至昵称填写界面（将服务器返回的xin）
        void intentToNickAct();

        //新手福利通知购买股票
        void doNewsBuying(UserIndexBean data);

    }

    interface Presenter extends BasePresenter<View> {

        //获取验证码
        void getVerificationCode(String phoneNumber);

        //注册
        void register(String phoneNumber, String smsCode, String password);

    }
}
