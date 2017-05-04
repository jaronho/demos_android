package com.example.nyapp;

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
import android.widget.TextView;

import com.example.util.ConnectionWork;
import com.example.util.UrlContact;

public class DaiGouGuiZeActivity extends BaseActivity {
    private WebView webview;
    private String urlString, title;
    private LinearLayout layout_back;
    private TextView text_name;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.daigouguize);
        text_name = (TextView) findViewById(R.id.text_name);
        urlString = getIntent().getStringExtra("payurl");
        title = getIntent().getStringExtra("title");
        text_name.setText(title);
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        layout_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        webview = (WebView) findViewById(R.id.webView1);
        webview.getSettings().setJavaScriptEnabled(true);
        // 加载需要显示的网页
        webview.requestFocus();
        webview.getSettings().setAllowFileAccess(true);// 设置允许访问文件数据

        webview.getSettings().setBuiltInZoomControls(true);// 设置支持缩放
        // webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);//
        // 设置支持缩放
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);

        // 设置支持JavaScript脚本
        if (ConnectionWork.isConnect(DaiGouGuiZeActivity.this)) {
//			webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            webview.getSettings().setCacheMode(
                    WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        // 设置支持各种不同的设备
        webview.getSettings()
                .setUserAgentString(
                        "Mozilla/5.0 (iPad; U; CPU OS 3_2 like Mac OS X;en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B334bSafari/531.21.10");
//	      webview.loadUrl("http://192.168.1.99:8097/Pro/Detail?proId="+Product_detaileActivity.id); 
        //设置Web视图
        webview.loadUrl(urlString);

        webview.setWebViewClient(new HelloWebViewClient());
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub

    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();    //表示等待证书响应
            // handler.cancel();      //表示挂起连接，为默认方式
            // handler.handleMessage(null);    //可做其他处理
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//			url.equals(UrlContact.WEB_URL_STRING+"/")||url.equals(UrlContact.WEB_URL_STRING+"/Home/Index")
            if (url.equals(UrlContact.WEB_URL_STRING + "/") || url.equals(UrlContact.WEB_URL_STRING + "/Home/Index")) {
                finish();
            } else {
                view.loadUrl(url);

            }

            view.getSettings().setUseWideViewPort(true);
            view.getSettings().setLoadWithOverviewMode(true);

            return true;
        }


    }

}
