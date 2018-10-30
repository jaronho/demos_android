package com.gsclub.strategy.presenter.login;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.text.TextUtils;
import com.gsclub.strategy.app.UrlConfig;
import com.gsclub.strategy.base.RxPresenter;
import com.gsclub.strategy.component.RxBus;
import com.gsclub.strategy.contract.login.ForgetPwdContract;
import com.gsclub.strategy.model.DataManager;
import com.gsclub.strategy.util.CommonUtil;
import com.gsclub.strategy.util.LogUtil;
import com.gsclub.strategy.util.ToastUtil;
import com.gsclub.strategy.widget.rx.CommonSubscriber;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

public class ForgetPwdPresenter extends RxPresenter<ForgetPwdContract.View> implements ForgetPwdContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public ForgetPwdPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getVerificationCode(String phone) {
        if(!CommonUtil.isCorrectPhone(phone)) return;
        Map<String, Object> params = new HashMap<>();
        params.put("tel", phone);
        params.put("sms_type", "2");//1.注册账号 2.找回密码
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
    public void editPwd(String phone, final String verification, String pwd, String rePwd) {
        if(!CommonUtil.isCorrectPhone(phone)) return;
        Map<String, Object> params = new HashMap<>();
        if (!TextUtils.isEmpty(verification)) {// 手机验证
            params.put("sms_code", verification);
        } else {// 重置密码
            if(!CommonUtil.isCorrectPwd(pwd)) return;
            if(!CommonUtil.isCorrectPwd(rePwd)) return;
            if (!pwd.equals(rePwd)) {
                ToastUtil.showMsg("两次密码不一致");
                return;
            }
            params.put("pwd", pwd);
            params.put("repwd", rePwd);
        }
        params.put("tel", phone);
        String url = UrlConfig.User_userEdit;
        post(mDataManager.editPassword(url, params), new CommonSubscriber<Object>(mView, url) {
                    @Override
                    public void onNext(Object bean) {
                        LogUtil.i(bean.toString());
                        if (!TextUtils.isEmpty(verification)) {// 手机验证
                            mView.setPwdView();
                        }else {
                            ToastUtil.showMsg("重置密码成功");// 重置密码
                            RxBus.get().send(RxBus.Code.FORGET_PWD_RESET_SUCCESS);
                            mView.finishUI();
                        }
                    }
                });
    }


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
