package com.liyuu.strategy.presenter.mine;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.text.TextUtils;

import com.liyuu.strategy.app.SPKeys;
import com.liyuu.strategy.app.UrlConfig;
import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.component.RxBus;
import com.liyuu.strategy.contract.mine.EditPhoneContract;
import com.liyuu.strategy.http.GsonUtil;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.util.CommonUtil;
import com.liyuu.strategy.util.LogUtil;
import com.liyuu.strategy.util.PreferenceUtils;
import com.liyuu.strategy.util.ToastUtil;
import com.liyuu.strategy.widget.rx.CommonSubscriber;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import io.reactivex.functions.Function;

public class EditPhonePresenter extends RxPresenter<EditPhoneContract.View> implements EditPhoneContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public EditPhonePresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getVerificationCode(String phone, int tel_type) {
        if(!CommonUtil.isCorrectPhone(phone)) return;
        Map<String, Object> params = new HashMap<>();
        params.put("tel", phone);
        params.put("sms_type", "1");// 1短信2语音
        params.put("tel_type", String.valueOf(tel_type));// 手机号类型: 5原手机号 6新手机号
        String url = UrlConfig.User_sendMsgCode;
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
    public void verifyPhone(final String phone, final String verification, final int tel_type) {
        if(!CommonUtil.isCorrectPhone(phone)) return;
        Map<String, Object> params = new HashMap<>();
        params.put("tel", phone);
        if (!TextUtils.isEmpty(verification)) {
            params.put("sms_code", verification);
        }
        params.put("tel_type", String.valueOf(tel_type));// 手机号类型: 5原手机号 6新手机号
        String url = UrlConfig.User_chgTel;
        post(mDataManager.editPassword(url, params), new CommonSubscriber<Object>(mView, url) {
                    @Override
                    public void onNext(Object bean) {
                        LogUtil.i(bean.toString());
                        if(tel_type == 5) {
                            countDownTimer.cancel();
                            mView.setVerificationCodeText(true, "获取验证码");
                            mView.setNewPhone();
                        } else {
                            PreferenceUtils.put(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_PHONE_NUMBER, phone);
                            ToastUtil.showMsg("修改手机号成功");//重置密码
                            try {
                                JSONObject obj = new JSONObject(GsonUtil.toString(bean));
                                PreferenceUtils.put(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_NICKNAME, obj.optString("nickname"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } finally {
                                RxBus.get().send(RxBus.Code.USER_TEL_REFRESH);
                                RxBus.get().send(RxBus.Code.EDIT_NICKNAME_SUCCESS);
                                mView.finishUI();
                            }
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
