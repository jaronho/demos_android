package com.gsclub.strategy.presenter.transaction;


import android.annotation.SuppressLint;
import android.os.CountDownTimer;

import com.gsclub.strategy.R;
import com.gsclub.strategy.app.SPKeys;
import com.gsclub.strategy.app.UrlConfig;
import com.gsclub.strategy.base.RxPresenter;
import com.gsclub.strategy.contract.transaction.SetTradingPasswordContract;
import com.gsclub.strategy.contract.transaction.WithdrawContract;
import com.gsclub.strategy.model.DataManager;
import com.gsclub.strategy.util.CommonUtil;
import com.gsclub.strategy.util.PreferenceUtils;
import com.gsclub.strategy.util.ToastUtil;
import com.gsclub.strategy.widget.rx.CommonSubscriber;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class SetTradingPasswordPresenter extends RxPresenter<SetTradingPasswordContract.View> implements SetTradingPasswordContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public SetTradingPasswordPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getVerificationCode() {
        Map<String, Object> params = new HashMap<>();
        String phone = PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_PHONE_NUMBER);
        params.put("tel", phone);
        params.put("sms_type", "1");// 1短信2语音
        params.put("tel_type", "7");
        String url = UrlConfig.User_sendPaypwdMsgCode;
        post(mDataManager.sendMsgCode(url, params), new CommonSubscriber<Object>(mView, url) {
            @Override
            public void onNext(Object bean) {
                ToastUtil.showMsg("获取验证码成功");
                time = 60;
                countDownTimer.start();
            }
        });
    }

    @Override
    public void changeTradePass(String sms_code, String pay_pwd) {
        if(pay_pwd.length()<8) {
            ToastUtil.showMsg(R.string.input_trading_password);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        String phone = PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_PHONE_NUMBER);
        params.put("tel", phone);
        params.put("sms_code", sms_code);
        params.put("pay_pwd", pay_pwd);
        String url = UrlConfig.User_changeTradePass;
        post(mDataManager.changeTradePass(url, params), new CommonSubscriber<Object>(mView, url) {
            @Override
            public void onNext(Object bean) {
                ToastUtil.showMsg("交易密码操作成功");
                PreferenceUtils.put(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_HAS_PAY_PWD, true);
                mView.finishUI();
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
