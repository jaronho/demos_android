package com.gsclub.strategy.presenter.login;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;

import com.gsclub.strategy.app.UrlConfig;
import com.gsclub.strategy.base.RxPresenter;
import com.gsclub.strategy.component.RxBus;
import com.gsclub.strategy.contract.login.RegisterContract;
import com.gsclub.strategy.http.GsonUtil;
import com.gsclub.strategy.model.DataManager;
import com.gsclub.strategy.model.bean.ProtocolBean;
import com.gsclub.strategy.model.bean.UserIndexBean;
import com.gsclub.strategy.model.bean.UserRegisterBean;
import com.gsclub.strategy.util.CommonUtil;
import com.gsclub.strategy.util.LogUtil;
import com.gsclub.strategy.util.ToastUtil;
import com.gsclub.strategy.util.WebCookieUtil;
import com.gsclub.strategy.widget.rx.CommonSubscriber;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class RegisterPresenter extends RxPresenter<RegisterContract.View> implements RegisterContract.Presenter {
    private DataManager mDataManager;

    @Inject
    RegisterPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getVerificationCode(String phoneNumber) {
        if (!CommonUtil.isCorrectPhone(phoneNumber)) return;
        Map<String, Object> params = new HashMap<>();
        params.put("tel", phoneNumber);
        params.put("sms_type", "1");//1.注册账号 2.找回密码
        String url = UrlConfig.User_sendsms;
        post(mDataManager.fetchVerificationCodeInfo(url, params), new CommonSubscriber<Object>(mView, url) {
            @Override
            public void onNext(Object bean) {
                ToastUtil.showMsg("获取验证码成功");
                time = 60;
                countDownTimer.start();
            }
        });
    }

    @Override
    public void register(final String phoneNumber, String smsCode, final String password) {
        if (!CommonUtil.isCorrectPhone(phoneNumber)) return;
        if (!CommonUtil.isCorrectPwd(password)) return;
        Map<String, Object> params = new HashMap<>();
        params.put("tel", phoneNumber);
        params.put("sms_code", smsCode);
        params.put("pwd", password);
        String url = UrlConfig.User_reg;
        post(mDataManager.register(url, params), new CommonSubscriber<UserIndexBean>(mView, url) {
            @Override
            public void onNext(UserIndexBean data) {
                ToastUtil.showMsg("注册成功");
                if (data == null) return;
                mDataManager.setUserInfo(data);
                mView.doNewsBuying(data);
                if ("1".equals(data.getMark())) {
                    mView.intentToNickAct();
                }
                RxBus.get().send(RxBus.Code.USER_LOGIN_SUCCESS_WITH_USERINDEXBEAN, data);
                mView.finishUI();

                //登录成功保存用户的cookie 中session_id
                WebCookieUtil.saveCookie();
//                try {
//                    JSONObject jsonObj = new JSONObject(GsonUtil.getInstance().toJson(data));
//                    String open_id = jsonObj.optString("open_id");
//                    loginByOpenId(open_id, phoneNumber);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });
    }

//    private void loginByOpenId(String open_id, String tel) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("open_id", open_id);
//        params.put("tel", tel);
//        String url = UrlConfig.User_loginByOpenId;
//        post(mDataManager.fetchLoginInfo(url, params), new CommonSubscriber<UserIndexBean>(mView, url) {
//            @Override
//            public void onNext(UserIndexBean data) {
//                super.onNext(data);
//                if (data == null) return;
//                mDataManager.setUserInfo(data);
//                mView.doNewsBuying(data);
//                if ("1".equals(data.getMark())) {
//                    mView.intentToNickAct();
//                }
//                RxBus.get().send(RxBus.Code.USER_LOGIN_SUCCESS_WITH_USERINDEXBEAN, data);
//                mView.finishUI();
//
//                //登录成功保存用户的cookie 中session_id
//                WebCookieUtil.saveCookie();
//            }
//        });
//    }

    private int time;
    private CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
        @SuppressLint("DefaultLocale")
        @Override
        public void onTick(long millisUntilFinished) {
            mView.setVerificationCodeText(false, String.format("(%ds)重新获取", --time));
        }

        @Override
        public void onFinish() {
            mView.setVerificationCodeText(true, "重新获取");
        }
    };

    @Override
    public void detachView() {
        super.detachView();
        countDownTimer.cancel();
    }

}
