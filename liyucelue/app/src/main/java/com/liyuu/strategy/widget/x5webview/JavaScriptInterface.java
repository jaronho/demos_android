package com.liyuu.strategy.widget.x5webview;

import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.liyuu.strategy.app.App;
import com.liyuu.strategy.model.bean.ShareInfoBean;
import com.liyuu.strategy.presenter.mine.WebviewPresenter;
import com.liyuu.strategy.ui.mine.webview.WebviewRightMode;
import com.liyuu.strategy.util.LogUtil;
import com.liyuu.strategy.util.SystemUtil;


public class JavaScriptInterface {
    private WebviewPresenter mPresenter;

    public JavaScriptInterface(WebviewPresenter presenter) {
        super();
        mPresenter = presenter;
    }

    /*
     * 打印网页内容
     */
    @JavascriptInterface
    public void showSource(String html) {
        LogUtil.d("temp*************" + html);
    }

    @JavascriptInterface
    public void copyUrl(String url) {
        LogUtil.d("复制到剪切板 " + url);
        if (!TextUtils.isEmpty(url))
            SystemUtil.copyToClipBoard(App.getInstance(), url);
    }

    @JavascriptInterface
    public void titleRightWithUrl(String showText, String url) {
        LogUtil.d("showText " + showText + " url " + url);
        if (mPresenter != null)
            mPresenter.setTitleRightTextView(WebviewRightMode.URL, showText, url);
    }

    /**
     * 第三方分享
     *
     * @param actId 活动id
     */
    @JavascriptInterface
    public void shareThePage(String actId) {
        mPresenter.getShareData(actId);
    }

}

