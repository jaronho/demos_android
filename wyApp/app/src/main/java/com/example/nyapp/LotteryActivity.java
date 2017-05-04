package com.example.nyapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.classes.BaseBean;
import com.example.classes.ConfigureBean;
import com.example.classes.User;
import com.example.util.GsonUtils;
import com.example.util.MyMsgDialog;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyProgressDialog;
import com.example.util.MyToastUtil;
import com.example.util.UrlContact;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;
import java.util.TreeMap;

import ASimpleCache.org.afinal.simplecache.ACache;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class LotteryActivity extends BaseActivity {

    @BindView(R.id.web_lottery)
    WebView mWebLottery;
    @BindView(R.id.activity_lottery)
    RelativeLayout mActivityLottery;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;
    private String mUrl;
    private String mTitle;
    private String loginString;
    private ACache mCache;
    private User user;
    private String mUser_name = "";
    private MyMsgDialog mLoginDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery);
        ButterKnife.bind(this);
        //声明WebSettings子类
        initSettings(mWebLottery);
        initWebView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLogin()) {
            initData();
        } else {
            mLoginDialog = new MyMsgDialog(this, "系统提示！", "请登录您的账号与密码！",
                    new MyMsgDialog.ConfirmListener() {
                        @Override
                        public void onClick() {
                            mLoginDialog.dismiss();
                            Intent intent = new Intent(LotteryActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    },
                    new MyMsgDialog.CancelListener() {
                        @Override
                        public void onClick() {
                            mLoginDialog.dismiss();
                            finish();
                        }
                    });
            mLoginDialog.setCancelable(false);
            mLoginDialog.show();

        }

    }

    private void initData() {
        MyProgressDialog.show(LotteryActivity.this, true, true);
        Map<String, String> map = new TreeMap<>();
        map.put("loginKey", mUser_name);
        map.put("deviceId", MyApplication.sUdid);
        map.put("toWhere", "choujiang");
        map.put("platformName", "Android");
        MyOkHttpUtils
                .getData(UrlContact.URL_TAKE_LOTTERY, map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToastUtil.showLongMessage(e.toString());
                        MyProgressDialog.cancel();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        MyProgressDialog.cancel();
                        if (response != null) {
                            Gson gson = new Gson();
                            BaseBean baseBean = gson.fromJson(response, BaseBean.class);
                            if (baseBean.isResult()) {
                                String lotteryUrl = baseBean.getData();
                                String message = baseBean.getMessage();
                                String title = null;
                                String msg = null;
                                if (message.contains("|")) {
                                    String[] str = message.split("\\|");
                                    title = str[0];
                                    if (str.length == 2) {
                                        msg = str[1];
                                    }
                                }
                                if (lotteryUrl != null && !TextUtils.isEmpty(lotteryUrl)) {
                                    mUrl = lotteryUrl;
                                    mTitle = title;
                                    initView();
                                    loadingUrl();
                                } else {
                                    finish();
                                    MyToastUtil.showShortMessage(msg);
                                }
                            }
                        }
                    }
                });
    }

    private boolean isLogin() {
        mCache = ACache.get(this);
        loginString = mCache.getAsString("loginString");
        if (loginString == null) {
            loginString = "false";
            mCache.put("loginString", loginString);
            return false;
        } else {
            if (loginString.equals("true")) {
                user = (User) mCache.getAsObject("user");
                mUser_name = user.getUser_Name();
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivityLottery.removeView(mWebLottery);
        mWebLottery.destroy();
    }

    private void loadingUrl() {
        Map<String, String> map = new TreeMap<>();
        map.put("key", "CompelRefresh");
        MyOkHttpUtils
                .getData(UrlContact.URL_CONFIGURE, map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToastUtil.showShortMessage("服务器连接失败!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null) {
                            ConfigureBean configureBean = GsonUtils.getInstance().fromJson(response, ConfigureBean.class);
                            if (configureBean.isFlag()) {
                                if (mUrl.contains("&")) {
                                    mUrl = mUrl + "&v=" + configureBean.getMessage();
                                } else if (mUrl.contains(".html")) {
                                    mUrl = mUrl + "?v=" + configureBean.getMessage();
                                }
                                // 设置Web视图
                                mWebLottery.loadUrl(mUrl);
                            } else {
                                // 设置Web视图
                                mWebLottery.loadUrl(mUrl);
                            }
                        }
                    }
                });
    }

    private void initWebView() {
        mWebLottery.setWebChromeClient(new WebChromeClient());
        mWebLottery.setWebViewClient(new MyWebViewClient());
    }

    private void initSettings(WebView webLottery) {


        WebSettings webSettings = webLottery.getSettings();
        webLottery.requestFocus();

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

        //支持插件
        webSettings.setPluginState(WebSettings.PluginState.ON);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(false); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setAllowContentAccess(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //不使用缓存，只从网络获取数据。
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
    }

    @Override
    public void initView() {
        if (mTitle.equals("现场抽大奖")) {
            mRlTitle.setVisibility(View.GONE);
        }
        mTvTitle.setText(mTitle);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("http://m.16899.com/")) {
                finish();
            } else {
                initSettings(view);
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //设定加载开始的操作
            MyProgressDialog.show(LotteryActivity.this, true, true);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //设定加载结束的操作
            MyProgressDialog.cancel();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();    //表示等待证书响应
            // handler.cancel();      //表示挂起连接，为默认方式
            // handler.handleMessage(null);    //可做其他处理
        }
    }

    @OnClick({R.id.layout_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebLottery.canGoBack()) {
            mWebLottery.goBack();
        } else {
            finish();
        }
        return true;
    }
}
