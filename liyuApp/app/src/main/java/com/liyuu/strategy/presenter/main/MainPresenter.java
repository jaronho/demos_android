package com.liyuu.strategy.presenter.main;

import android.content.Context;

import com.liyuu.strategy.BuildConfig;
import com.liyuu.strategy.app.SPKeys;
import com.liyuu.strategy.app.UrlConfig;
import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.contract.main.MainContract;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.model.bean.AppUpdateBean;
import com.liyuu.strategy.model.bean.WebSetBean;
import com.liyuu.strategy.ui.dialog.UpdateAppDialog;
import com.liyuu.strategy.util.CommonUtil;
import com.liyuu.strategy.util.LogUtil;
import com.liyuu.strategy.util.PreferenceUtils;
import com.liyuu.strategy.widget.rx.CommonSubscriber;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter {
    private DataManager mDataManager;

    @Inject
    MainPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(MainContract.View view) {
        super.attachView(view);
    }

    @Override
    public void getWebSets() {
        post(mDataManager.getInfo(UrlConfig.Other_webset), new CommonSubscriber<WebSetBean>(mView) {
            @Override
            public void onNext(WebSetBean data) {
                PreferenceUtils.put(SPKeys.FILE_URL, SPKeys.URL_TUTORIAL, data.getTutorial());
                PreferenceUtils.put(SPKeys.FILE_URL, SPKeys.URL_INVITE, data.getInvite());
                PreferenceUtils.put(SPKeys.FILE_URL, SPKeys.URL_COOPERATION, data.getCooperation());
                PreferenceUtils.put(SPKeys.FILE_URL, SPKeys.URL_PRIVACY, data.getPrivacy());
                PreferenceUtils.put(SPKeys.FILE_URL, SPKeys.URL_SERVICE, data.getService());
                PreferenceUtils.put(SPKeys.FILE_URL, SPKeys.CUSTOMER_WE_CHAT, data.getCustomer_wechat());
            }
        });
    }

    @Override
    public void initXGPush(Context context) {

        XGPushConfig.enableDebug(context, BuildConfig.SHOW_LOG);
//        XGPushConfig.setHuaweiDebug(BuildConfig.SHOW_LOG);

        //设置魅族、小米、华为APPID和APPKEY
//        XGPushConfig.enableOtherPush(this, true);
//        XGPushConfig.setMzPushAppId(this, "1000120");
//        XGPushConfig.setMzPushAppKey(this, "a53cd513c15542b0924a216e60f2c6ce");
//
//        XGPushConfig.setMiPushAppId(getApplicationContext(), "2882303761517486656");
//        XGPushConfig.setMiPushAppKey(getApplicationContext(), "5931748647656");

        XGPushManager.registerPush(context, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                //token在设备卸载重装的时候有可能会变
                LogUtil.d("TPush", "注册成功，设备token为：" + data);
                PreferenceUtils.put(SPKeys.FILE_COMMON, SPKeys.XG_PUSH_TOKEN, data.toString());
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                LogUtil.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });
    }

    @Override
    public void checkUpdata() {
        Map<String, Object> params = new HashMap<>();
        params.put("ver_num", CommonUtil.getAppVersionName());
        String url = UrlConfig.Other_appUpdate;
        post(mDataManager.checkUpdata(url, params), new CommonSubscriber<AppUpdateBean>(mView, url) {
            @Override
            public void onNext(AppUpdateBean data) {
                UpdateAppDialog.checkUpdata(data);
            }

            @Override
            public void onMsg(int code, String msg) {
            }
        });
    }

}
