package com.example.nyapp;

import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.classes.ConfigureBean;
import com.example.util.ConnectionWork;
import com.example.util.GsonUtils;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyToastUtil;
import com.example.util.UrlContact;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;

public class ZhuanTiActivity extends BaseActivity {
	private WebView webview;
	private String urlString;
	private LinearLayout layout_back, layout_webview;
	private TextView text_title;
	private String titele;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pay2);
		urlString = getIntent().getStringExtra("payurl");
		titele = getIntent().getStringExtra("title");
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
		text_title = (TextView) findViewById(R.id.text_title);
		text_title.setText(titele);
		layout_webview = (LinearLayout) findViewById(R.id.layout_webview);
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		layout_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		webview.setWebChromeClient(new MyWebChromeClient(new WebChromeClient()));
		webview.getSettings().setJavaScriptEnabled(true);
		// 加载需要显示的网页
		webview.requestFocus();
		webview.getSettings().setAllowFileAccess(true);// 设置允许访问文件数据

		webview.getSettings().setBuiltInZoomControls(true);// 设置支持缩放
		if (ConnectionWork.isConnect(ZhuanTiActivity.this)) {
		} else {
			webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		}

		// 设置支持各种不同的设备
		webview.getSettings().setUserAgentString(
				"Mozilla/5.0 (Linux; U; Android 2.3.7; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1 NyWebViewer");

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
			view.loadUrl(url);
			view.getSettings().setUseWideViewPort(true);
			view.getSettings().setLoadWithOverviewMode(true);
			return true;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		layout_webview.removeView(webview);
		webview.destroy();
	}
}
