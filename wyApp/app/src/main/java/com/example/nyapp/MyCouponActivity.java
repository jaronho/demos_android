package com.example.nyapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.classes.ConfigureBean;
import com.example.classes.User;
import com.example.util.GsonUtils;
import com.example.util.MyMsgDialog;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyToastUtil;
import com.example.util.UrlContact;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import ASimpleCache.org.afinal.simplecache.ACache;
import okhttp3.Call;

/**
 * 优惠券领取
 */
public class MyCouponActivity extends BaseActivity {

    private ASimpleCache.org.afinal.simplecache.ACache mCache;
    private User user;
    private WebView webview;
    private String urlString;
    private String title;
    private RelativeLayout mRlTitle;
    private MyMsgDialog mDialog;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tuangou);
        urlString = getIntent().getStringExtra("payurl");
        title = getIntent().getStringExtra("title");
        initView();
        initData();

    }

    private void initData() {
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
                                if (urlString.contains("&")) {
                                    urlString = urlString + "&v=" + configureBean.getMessage();
                                } else if (urlString.contains(".html")) {
                                    urlString = urlString + "?v=" + configureBean.getMessage();
                                }
                                // 设置Web视图
                                webview.loadUrl(urlString);
                            } else {
                                // 设置Web视图
                                webview.loadUrl(urlString);
                            }
                        }
                    }
                });
    }

    @Override
    public void initView() {
        webview = (WebView) findViewById(R.id.webView1);
        mCache = ACache.get(this);
        user = (User) mCache.getAsObject("user");
        TextView text_title = (TextView) findViewById(R.id.text_title);
        text_title.setText(title);
        mRlTitle = (RelativeLayout) findViewById(R.id.title);

        LinearLayout layout_back = (LinearLayout) findViewById(R.id.layout_back);
        layout_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        webview.getSettings().setJavaScriptEnabled(true);
        // 加载需要显示的网页
        webview.requestFocus();
        webview.getSettings().setAllowFileAccess(true);// 设置允许访问文件数据

        webview.getSettings().setBuiltInZoomControls(true);// 设置支持缩放
        // webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);//
        // 设置支持缩放
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);

        // 设置支持缓存
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);


        // 设置支持各种不同的设备
        webview.getSettings().setUserAgentString(
                "Mozilla/5.0 (Linux; U; Android 2.3.7; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1 CouponTake");

        webview.setWebViewClient(new HelloWebViewClient());
    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();    //表示等待证书响应
            // handler.cancel();      //表示挂起连接，为默认方式
            // handler.handleMessage(null);    //可做其他处理
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            mCache = ACache.get(getApplicationContext());
            user = (User) mCache.getAsObject("user");
            if (user == null || user.getUser_Name() == null) {
                Intent intent = new Intent(MyCouponActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                if (url.contains("APPCouponTake")) {
                    TakeCoupon(url);

                } else if (url.equals(UrlContact.WEB_URL_STRING + "/")
                        || url.equals(UrlContact.WEB_URL_STRING + "/Home/Index")) {
                    finish();
                } else {
                    view.loadUrl(url);
                }
            }
            mRlTitle.setVisibility(View.GONE);
            view.getSettings().setUseWideViewPort(true);
            view.getSettings().setLoadWithOverviewMode(true);

            return true;

        }

    }

    //抢代金券
    private void TakeCoupon(String url) {
        try {
            String myText = java.net.URLDecoder.decode(url, "utf-8");
            String s1 = myText.substring(myText.indexOf("|") + 1, myText.length());

            Map<String, String> params = new HashMap<>();
            params.put("loginKey", user.getUser_Name());
            params.put("deviceId", MyApplication.sUdid);
            params.put("RuleId", s1);
            MyOkHttpUtils
                    .postData(UrlContact.URL_TAKE_COUPON, params)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            MyToastUtil.showShortMessage("服务器连接失败!");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String message;
                                message = jsonObject.getString("Message");
                                showDialog(message);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void showDialog(String message) {
        mDialog = new MyMsgDialog(this, "温馨提示", message, new MyMsgDialog.ConfirmListener() {
            @Override
            public void onClick() {
                mDialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                user.setPurchasing_State(1 + "");
                user.setPermit_Type(2 + "");
                mCache.put("user", user);
                startActivity(intent);
            }
        }, null);
        mDialog.show();
    }

}
