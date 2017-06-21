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
import android.widget.TextView;

import com.example.util.ConnectionWork;

public class MyTuanGouActivity extends BaseActivity{
	private LinearLayout layout_back;
	private WebView webview;
	private String urlString;
	private TextView text_title;
	private String  titele;
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mytuangou);
		urlString=getIntent().getStringExtra("payurl");
		titele=getIntent().getStringExtra("title");
		text_title=(TextView) findViewById(R.id.text_title);
		text_title.setText(titele);
		layout_back=(LinearLayout) findViewById(R.id.layout_back);
		layout_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
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

		// 设置支持JavaScript脚本
		if (ConnectionWork.isConnect(MyTuanGouActivity.this)) {
//			webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		} else {
			webview.getSettings().setCacheMode(
					WebSettings.LOAD_CACHE_ELSE_NETWORK);
		}
	
		// 设置支持各种不同的设备
		webview.getSettings()
		.setUserAgentString(
				"Mozilla/5.0 (Linux; U; Android 2.3.7; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1 Groupon");
	        //设置Web视图 
	      webview.loadUrl(urlString); 

	      webview.setWebViewClient(new HelloWebViewClient ()); 
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		
	}
	private class HelloWebViewClient extends WebViewClient{
		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
			handler.proceed();    //表示等待证书响应
			// handler.cancel();      //表示挂起连接，为默认方式
			// handler.handleMessage(null);    //可做其他处理
		}
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
	
			
			if (url.contains("AndroidGroupon|177-5-200")) {
				Intent intent = new Intent(MyTuanGouActivity.this,ShoppingCarActivity.class);
				startActivity(intent);
			}else {
				
				view.loadUrl(url);
			}
        	 return true;
		}

		}

		
		}

