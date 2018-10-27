package com.liyuu.strategy.contract.login;


import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;
import com.liyuu.strategy.model.bean.UserRegisterBean;

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
