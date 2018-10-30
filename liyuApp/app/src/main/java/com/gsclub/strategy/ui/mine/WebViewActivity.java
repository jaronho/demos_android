package com.gsclub.strategy.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gsclub.strategy.R;
import com.gsclub.strategy.base.BaseActivity;
import com.gsclub.strategy.contract.mine.WebviewContract;
import com.gsclub.strategy.model.bean.ShareInfoBean;
import com.gsclub.strategy.presenter.mine.WebviewPresenter;
import com.gsclub.strategy.ui.dialog.ShareDialog;
import com.gsclub.strategy.util.ToastUtil;
import com.gsclub.strategy.util.WebCookieUtil;
import com.gsclub.strategy.widget.x5webview.JavaScriptInterface;
import com.gsclub.strategy.widget.x5webview.X5WebView;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.socialize.UMShareAPI;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 网页加载activity
 * {@link com.gsclub.strategy.ui.mine.webview.WebviewRightMode} 标题栏右边文字提供的方法
 * {@link JavaScriptInterface} webview提供的js交互方法
 */

public class WebViewActivity extends BaseActivity<WebviewPresenter> implements WebviewContract.View {
    private static final String KEY_WEBVIEW_URL_STR = "key_webview_url_str";
    private final String WENVIEW_JAVASCRIPT_METHOD_NAME = "mobileCall";
    X5WebView mWebView;
    @BindView(R.id.ll_webview)
    LinearLayout llWebview;
    @BindView(R.id.bar_web)
    ProgressBar mProgressBar;
    @BindView(R.id.tv_close)
    TextView mCloseTv;
    @BindView(R.id.tv_right_word)
    TextView tvRightWord;
    //    @BindView(R.id.tv_right)
//    TextView mRightTv;
    private String mUrl;

    public static void start(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_WEBVIEW_URL_STR, url);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void initEventAndData() {
        if (getIntent().getExtras() == null) {
            finishUI();
            return;
        }

        mUrl = getIntent().getExtras().getString(KEY_WEBVIEW_URL_STR);

        mWebView = new X5WebView(this);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mWebView.addJavascriptInterface(new JavaScriptInterface(mPresenter), WENVIEW_JAVASCRIPT_METHOD_NAME);
        mWebView.setDownloadListener(new MyWebViewDownLoadListener());//设置下载监听

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                view.loadUrl("about:blank");
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();//这里校验失败的时候放过
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //在此处2次加载为了防止右上角标题因为某些原因无法成功显示的问题
                if (!TextUtils.isEmpty(showText)) {
                    view.loadUrl("javascript:" + WENVIEW_JAVASCRIPT_METHOD_NAME + ".titleRightWithUrl('" + showText + "','" + rightUrl + "');");
                    showText = null;
                }
                view.loadUrl("javascript:" + WENVIEW_JAVASCRIPT_METHOD_NAME + ".showSource('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                return super.shouldOverrideUrlLoading(webView, url);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                setTitle(title);
                mCloseTv.setVisibility(mWebView.canGoBack() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    //加载完网页进度条消失
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setProgress(newProgress);
                }
            }
        });

        llWebview.addView(mWebView);
        WebCookieUtil.setCookieToWebView(this, mUrl);
        load();
    }

    private void load() {
        if (TextUtils.isEmpty(mUrl)) {
            ToastUtil.showMsg("url不能为空");
            return;
        }
        mWebView.loadUrl(mUrl);
    }

    @OnClick({R.id.tv_close, R.id.tv_right_word})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_close:
                finish();
                break;
            case R.id.tv_right_word:
//                ShareInfoBean bean = new ShareInfoBean();
//                bean.title = "鲤鱼选股";
//                bean.desc = "鲤鱼选股";
//                bean.share_img = "http://static.xg.jiyoucai.com/public/images/share_logo.png";
//                bean.share_url = "http://m.xg.jiyoucai.com/register?data=MTgwNTkwNDkwNDI=";
//                ShareDialog shareDialog = new ShareDialog(this, bean);
//                shareDialog.show();
                if (mWebView != null)
                    mWebView.loadUrl(rightUrl);
                hiddenTitleRight();
                break;
        }
    }

    @Override
    public void onBackPressedSupport() {
        if (mWebView != null && mWebView.canGoBack())
            mWebView.goBack();
        else
            super.onBackPressedSupport();
    }

    @Override
    protected void toolbarBack() {
        if (mWebView != null && mWebView.canGoBack())
            mWebView.goBack();
        else
            super.toolbarBack();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        if (this.mWebView != null) {
            mWebView.removeAllViews();
            // in android 5.1(sdk:21) we should invoke this to avoid memory leak
            // see (https://coolpers.github.io/webview/memory/leak/2015/07/16/android-5.1-webview-memory-leak.html)
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.setTag(null);
            mWebView.clearHistory();
            mWebView.destroy();
        }
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    @Override
    public void hiddenTitleRight() {
        tvRightWord.setVisibility(View.GONE);
    }

    private String rightUrl;
    private String showText;

    @Override
    public void showTitleRightUrl(String showText, final String url) {
        this.rightUrl = url;
        this.showText = showText;
        tvRightWord.setVisibility(View.VISIBLE);
        tvRightWord.setText(showText);
    }

    private ShareDialog showDialog;

    @Override
    public void showShareDialog(ShareInfoBean bean) {
        if (showDialog == null)
            showDialog = new ShareDialog(this, bean);
        showDialog.show();
    }

    /**
     * 启动系统的浏览器进行下载
     */
    private class MyWebViewDownLoadListener implements com.tencent.smtt.sdk.DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

    }
}
