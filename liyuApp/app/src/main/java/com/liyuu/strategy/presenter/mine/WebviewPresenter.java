package com.liyuu.strategy.presenter.mine;


import android.support.annotation.NonNull;

import com.liyuu.strategy.app.UrlConfig;
import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.contract.mine.WebviewContract;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.model.bean.ActivityShareBean;
import com.liyuu.strategy.model.bean.ShareInfoBean;
import com.liyuu.strategy.ui.mine.webview.WebviewRightMode;
import com.liyuu.strategy.widget.rx.CommonSubscriber;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;


public class WebviewPresenter extends RxPresenter<WebviewContract.View> implements WebviewContract.Presenter {
    private DataManager mDataManager;

    @Inject
    WebviewPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    /**
     * 设置webview标题右键的相关内容
     *
     * @param datas 第一个值一定是 {@link WebviewRightMode}
     *              每个mode所传的参数必须一致
     */
    @Override
    public void setTitleRightTextView(@NonNull Object... datas) {
        int mode = (int) datas[0];
        switch (mode) {
            case WebviewRightMode.NONE:
                mView.hiddenTitleRight();
                break;
            case WebviewRightMode.URL:
                mView.showTitleRightUrl(datas[1].toString(), datas[2].toString());
                break;
        }

    }

    @Override
    public void getShareData(String actId) {
        Map<String, Object> map = new HashMap<>();
        map.put("activity_id", actId);

        String url = UrlConfig.Activity_activityShare;
        post(false, mDataManager.fetchActivityShareData(url, map),
                new CommonSubscriber<ActivityShareBean>(mView, url) {
                    @Override
                    public void onNext(ActivityShareBean data) {
                        super.onNext(data);
                        if (data != null) {
                            ShareInfoBean bean = new ShareInfoBean();
                            bean.share_url = data.getWxUrl();
                            bean.share_img = data.getWxShareImg();
                            bean.title = data.getWxTitle();
                            bean.desc = data.getWxDesc();
                            mView.showShareDialog(bean);
                        }
                    }
                });
    }
}
