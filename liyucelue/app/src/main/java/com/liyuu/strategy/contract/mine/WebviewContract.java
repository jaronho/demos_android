package com.liyuu.strategy.contract.mine;


import android.content.Context;
import android.support.annotation.NonNull;

import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;
import com.liyuu.strategy.model.bean.ShareInfoBean;
import com.liyuu.strategy.ui.mine.webview.WebviewRightMode;

/**
 * Created by hlw on 2018/5/22.
 */

public interface WebviewContract {

    interface View extends BaseView {

        void hiddenTitleRight();

        void showTitleRightUrl(String showText,String url);

        void showShareDialog(ShareInfoBean bean);
    }

    interface Presenter extends BasePresenter<View> {

        /**
         * 设置webview标题右键的相关内容
         *
         * @param datas 第一个值一定是 {@link WebviewRightMode}
         *              每个mode所传的参数必须一致
         */
        void setTitleRightTextView(@NonNull Object... datas);

        /**
         * 获取到分享数据
         */
        void getShareData(String actId);
    }
}
