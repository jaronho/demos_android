package com.liyuu.strategy.presenter.transaction;

import com.liyuu.strategy.app.SPKeys;
import com.liyuu.strategy.app.UrlConfig;
import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.component.RxBus;
import com.liyuu.strategy.contract.home.SplashContract;
import com.liyuu.strategy.http.GsonUtil;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.model.bean.UserIndexBean;
import com.liyuu.strategy.util.PreferenceUtils;
import com.liyuu.strategy.util.WebCookieUtil;
import com.liyuu.strategy.widget.rx.CommonSubscriber;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class SplashPresenter extends RxPresenter<SplashContract.View> implements SplashContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public SplashPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void activityControl() {
        String url = UrlConfig.User_activityControl;
        post(false, mDataManager.customPost(url), new CommonSubscriber<Object>(mView, url) {
            @Override
            public void onNext(Object o) {
                super.onNext(o);
                try {
                    JSONObject obj = new JSONObject(GsonUtil.toString(o));
                    PreferenceUtils.put(SPKeys.FILE_COMMON, SPKeys.ACTIVITY_STATUS, obj.optInt("activity_status"));
                    PreferenceUtils.put(SPKeys.FILE_COMMON, SPKeys.JUMP_URL, obj.optString("jumpurl"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onComplete() {
                super.onComplete();
                mView.doActivityControl();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mView.doActivityControl();
            }

            @Override
            public void onMsg(int code, String msg) {
            }
        });
    }

    @Override
    public void loginByOpenId(String open_id, String tel) {
        Map<String, Object> params = new HashMap<>();
        params.put("open_id", open_id);
        params.put("tel", tel);
        String url = UrlConfig.User_loginByOpenId;
        post(false, mDataManager.fetchLoginInfo(url, params), new CommonSubscriber<UserIndexBean>(mView, url) {
            @Override
            public void onNext(UserIndexBean data) {
                super.onNext(data);
                if (data == null) return;
                mDataManager.setUserInfo(data);
//                ToastUtil.showMsg("登录成功");
                RxBus.get().send(RxBus.Code.USER_LOGIN_SUCCESS_WITH_USERINDEXBEAN, data);

                //登录成功保存用户的cookie 中session_id
                WebCookieUtil.saveCookie();
            }

            @Override
            public void onComplete() {
                super.onComplete();
                mView.loginFinish();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mView.loginFinish();
            }
        });
    }
}
