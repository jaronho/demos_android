package com.gsclub.strategy.presenter.login;

import com.gsclub.strategy.app.UrlConfig;
import com.gsclub.strategy.base.RxPresenter;
import com.gsclub.strategy.component.RxBus;
import com.gsclub.strategy.contract.login.LoginContract;
import com.gsclub.strategy.model.DataManager;
import com.gsclub.strategy.model.bean.UserIndexBean;
import com.gsclub.strategy.util.CommonUtil;
import com.gsclub.strategy.util.ToastUtil;
import com.gsclub.strategy.util.WebCookieUtil;
import com.gsclub.strategy.widget.rx.CommonSubscriber;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class LoginPresenter extends RxPresenter<LoginContract.View> implements LoginContract.Presenter {
    private DataManager mDataManager;

    @Inject
    LoginPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void login(String phone, String password) {
        if (!CommonUtil.isCorrectPhone(phone)) {
            return;
        }
        if (!CommonUtil.isCorrectPwd(password)) {
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("tel", phone);
        params.put("pwd", password);
        String url = UrlConfig.User_login;
        post(mDataManager.fetchLoginInfo(url, params),
                new CommonSubscriber<UserIndexBean>(mView, url) {
                    @Override
                    public void onNext(UserIndexBean data) {
                        if (data == null) return;
                        mDataManager.setUserInfo(data);
                        ToastUtil.showMsg("登录成功");
                        if ("1".equals(data.getMark())) {
                            mView.intentToNickAct();
                        }
                        RxBus.get().send(RxBus.Code.USER_LOGIN_SUCCESS_WITH_USERINDEXBEAN, data);
                        RxBus.get().send(RxBus.Code.USER_LOGIN_SUCCESS_FROM_NEWS_WELFARE);// 从注册页面过来登录后直接跳转到我的资产，需将新手福利页面finish
                        if (data.getActivityInfo() != null)
                            RxBus.get().send(RxBus.Code.USER_LOGIN_SUCCESS_WITH_ACTIVITY_DIALOG_INFO, data.getActivityInfo());
                        mView.finishUI();

                        //登录成功保存用户的cookie 中session_id
                        WebCookieUtil.saveCookie();
                    }
                });
    }

    @Override
    public void detachView() {
        super.detachView();
    }
}
