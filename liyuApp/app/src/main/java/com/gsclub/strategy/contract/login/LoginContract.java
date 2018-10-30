package com.gsclub.strategy.contract.login;


import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.model.bean.UserRegisterBean;

/**
 * Created by hlw on 2018/5/22.
 * 登录操作接口
 */

public interface LoginContract {

    interface View extends BaseView {
        //登录成功后，跳转至昵称填写界面（将服务器返回的xin）
        void intentToNickAct();
    }

    interface Presenter extends BasePresenter<View> {
        void login(String phone, String password);
    }
}
