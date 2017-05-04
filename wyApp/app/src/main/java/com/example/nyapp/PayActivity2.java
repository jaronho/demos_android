package com.example.nyapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.example.classes.ConfigureBean;
import com.example.classes.User;
import com.example.util.ConnectionWork;
import com.example.util.GsonUtils;
import com.example.util.HttpconnectionUtil;
import com.example.util.HttpconnectionUtil.ReturnResult2;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyProgressDialog;
import com.example.util.MyToastUtil;
import com.example.util.UrlContact;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.callback.StringCallback;

import net.sourceforge.simcpux.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import ASimpleCache.org.afinal.simplecache.ACache;
import okhttp3.Call;

public class PayActivity2 extends BaseActivity {
    private WebView webview;
    private String urlString, productId;
    private LinearLayout layout_back;
    private IWXAPI api;
    private ASimpleCache.org.afinal.simplecache.ACache mCache;
    private User user;
    private String url;
    private Button wxpay;
    private int count = 0;

    // private boolean isPaySupported = false;
    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pay2);
        urlString = getIntent().getStringExtra("payurl");
        productId = getIntent().getStringExtra("productId");
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                startOrderActivity();
                break;
        }
        return true;
    }

    private void startOrderActivity() {
        Intent intent = new Intent(PayActivity2.this, MyDingDanActivity.class);
        intent.putExtra("state", 0);
        intent.putExtra("title", "我的订单");
        startActivity(intent);
    }

    @Override
    public void initView() {
        // 微信支付
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        mCache = ACache.get(this);
        user = (User) mCache.getAsObject("user");
        wxpay = (Button) findViewById(R.id.wxpay);

        url = UrlContact.URL_STRING + "/PaySubmit/PayIndex?orderid=" + productId + "&paytype=5&Type=4&loginKey="
                + user.getUser_Name() + "&deviceId=" + MyApplication.sUdid;
        // isPaySupported = api.getWXAppSupportAPI() >=
        // Build.PAY_SUPPORTED_SDK_INT;
        webview = (WebView) findViewById(R.id.webView1);
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        layout_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startOrderActivity();
            }
        });
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        // 加载需要显示的网页
        webview.requestFocus();
        settings.setAllowFileAccess(true);// 设置允许访问文件数据

        settings.setDomStorageEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");

        //设置自适应屏幕，两者合用
        settings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        settings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        settings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        settings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        // 设置支持JavaScript脚本
        if (ConnectionWork.isConnect(PayActivity2.this)) {
            // webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        // 设置支持各种不同的设备
        settings.setUserAgentString(
                "Mozilla/5.0 (Linux; U; Android 2.3.7; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1 NyWebViewer");

        // 设置Web视图
        webview.setWebViewClient(new HelloWebViewClient());
    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            //接受证书
            handler.proceed();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("WeChatPayButton")) {//WeChatPayButton PaySubmit
                // api.isWXAppSupportAPI();//是否安装
                // api.isWXAppInstalled();//是否支持
                if (api.isWXAppSupportAPI()) {
                    if (api.isWXAppInstalled()) {
                        getWXpay();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity2.this);
                        builder.setTitle(R.string.app_tip);
                        builder.setMessage("请更新微信客户端");
                        builder.setPositiveButton("确定", null);
                        builder.show();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity2.this);
                    builder.setTitle(R.string.app_tip);
                    builder.setMessage("请安装微信");
                    builder.setPositiveButton("确定", null);
                    builder.show();
                }

                return true;
            }

            view.loadUrl(url);
            return true;
        }
    }

    // 获取微信支付的数据
    public void getWXpay() {
        MyProgressDialog.show(PayActivity2.this, true, true);
        Map<String, String> params2 = new HashMap<String, String>();
        HttpconnectionUtil.uploadFile(PayActivity2.this, new ReturnResult2() {

            @Override
            public void getResult(String result) {
                // TODO Auto-generated method stub
                MyProgressDialog.cancel();
                try {
                    if (result.equals("error")) {
                        Toast.makeText(getApplicationContext(), "网络断开连接！", Toast.LENGTH_SHORT).show();
                    }
                    if (result.equals("")) {
                        Toast.makeText(getApplicationContext(), "服务器异常！", Toast.LENGTH_SHORT).show();
                    }
                    if (!result.equals("") && !result.equals("error")) {
                        if (result != null && result.length() > 0) {
                            String content = new String(result);
                            content = JSONTokener(content);
                            JSONObject json = new JSONObject(content);
                            if (null != json && !json.has("retcode")) {
                                PayReq req = new PayReq();
                                // req.appId = "wxf8b4f85f3a794e77"; //
                                // ������appId
                                String appid = json.getString("appid");
                                IWXAPI wxApi = WXAPIFactory.createWXAPI(PayActivity2.this, appid, true);
                                wxApi.registerApp(appid);
                                req.appId = json.getString("appid");
                                req.partnerId = json.getString("partnerid");
                                req.prepayId = json.getString("prepayid");
                                req.nonceStr = json.getString("noncestr");
                                req.timeStamp = json.getString("timestamp");
                                req.packageValue = "Sign=WXPay";
                                req.sign = json.getString("sign");
                                req.extData = "app data"; // optional
                                // Toast.makeText(WXPayActivity.this, "正常调起支付",
                                // Toast.LENGTH_SHORT).show();
                                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                                api.sendReq(req);
                            } else {
                                Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
                                Toast.makeText(PayActivity2.this, "返回错误" + json.getString("retmsg"), Toast.LENGTH_SHORT)
                                        .show();
                            }
                        } else {
                            Log.d("PAY_GET", "服务器请求错误");
                            Toast.makeText(PayActivity2.this, "服务器请求错误", Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }, Method.GET, url, params2);
    }

    public static String JSONTokener(String in) {
        // consume an optional byte order mark (BOM) if it exists
        if (in != null && in.startsWith("\ufeff")) {
            in = in.substring(1);
        }
        return in;
    }
}
