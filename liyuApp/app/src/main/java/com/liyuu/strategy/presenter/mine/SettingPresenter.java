package com.liyuu.strategy.presenter.mine;

import android.content.Context;

import com.liyuu.strategy.app.App;
import com.liyuu.strategy.app.UrlConfig;
import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.contract.mine.SettingContract;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.model.bean.AppUpdateBean;
import com.liyuu.strategy.ui.dialog.UpdateAppDialog;
import com.liyuu.strategy.util.CommonUtil;
import com.liyuu.strategy.widget.rx.CommonSubscriber;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;


public class SettingPresenter extends RxPresenter<SettingContract.View> implements SettingContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public SettingPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void logout() {
        String url = UrlConfig.User_logOut;
        post(true, mDataManager.logout(url), new CommonSubscriber<Object>(mView, url) {
            @Override
            public void onNext(Object o) {
                mView.logoutSuccess();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    @Override
    public void checkUpdata(final boolean isFirst) {
        Map<String, Object> params = new HashMap<>();
        params.put("ver_num", CommonUtil.getAppVersionName());
        String url = UrlConfig.Other_appUpdate;
        post(!isFirst, mDataManager.checkUpdata(url, params), new CommonSubscriber<AppUpdateBean>(mView, url) {
            @Override
            public void onNext(AppUpdateBean data) {
                mView.showUpdate(isFirst, data);
            }

            @Override
            public void onMsg(int code, String msg) {
                if(isFirst) return;
                super.onMsg(code, msg);
            }
        });
    }
}