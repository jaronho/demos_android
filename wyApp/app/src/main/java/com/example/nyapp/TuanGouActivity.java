package com.example.nyapp;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.classes.ConfigureBean;
import com.example.classes.ShoppingCartBean;
import com.example.util.GsonUtils;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyProgressDialog;
import com.example.util.MyToastUtil;
import com.example.util.UrlContact;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;

public class TuanGouActivity extends BaseActivity {
    private WebView webview;
    private String urlString;
    private LinearLayout layout_back;
    private TextView text_title;
    private RelativeLayout head;
    private String title;
    private double zongjia = 0;
    private double freePrice = 0;
    private List<ShoppingCartBean.DataBean.ProductBean> items;
    private String index = "";
    private int type;
    private LinearLayout layout_webview;
    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;
    String stringUrl = UrlContact.URL_STRING + "/api/order/GetOffsetRule";
    private String mLoginKey;
    private String mDeviceId;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.tuangou);
        urlString = getIntent().getStringExtra("payurl");
        title = getIntent().getStringExtra("title");
        type = getIntent().getIntExtra("type", 0);
        mLoginKey = getIntent().getStringExtra("loginKey");
        mDeviceId = getIntent().getStringExtra("deviceId");
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
        head = (RelativeLayout) findViewById(R.id.title);
        text_title = (TextView) findViewById(R.id.text_title);
        layout_webview = (LinearLayout) findViewById(R.id.layout_webview);
        text_title.setText(title);
        if (type == 7) {
            head.setVisibility(View.GONE);
        } else {
            head.setVisibility(View.VISIBLE);
        }
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        layout_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (type == 1) {
                    PayActivity.isShowBalanceDialog = 3;
                }
                finish();
            }
        });

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        // 加载需要显示的网页
        webview.requestFocus();
        settings.setAllowFileAccess(true);// 设置允许访问文件数据
        settings.setPluginState(WebSettings.PluginState.ON);//

        settings.setDomStorageEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");

        //设置自适应屏幕，两者合用
        settings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        settings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        settings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        settings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        // 设置缓存模式
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // 设置支持各种不同的设备
        settings.setUserAgentString(
                "Mozilla/5.0 (Linux; U; Android 2.3.7; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1 NyWebViewer");

        webview.setWebChromeClient(new MyWebChromeClient(new WebChromeClient()) {

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                TuanGouActivity.this.startActivityForResult(Intent.createChooser(i, getString(R.string.app_name)), TuanGouActivity.FILECHOOSER_RESULTCODE);
            }
        });
        webview.setWebViewClient(new HelloWebViewClient());


//        webview.loadUrl("http://m.16899.com/UserCenter/PurProtocolDetail?protocolId=1010");


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();    //表示等待证书响应
            // handler.cancel();      //表示挂起连接，为默认方式
            // handler.handleMessage(null);    //可做其他处理
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            String path = url;
            if (url.contains("JumpNyCart")) {

                try {
                    // 读取获取的网址信息，并进行分割
                    String mytext2 = java.net.URLDecoder.decode(path, "utf-8");
                    String s1 = mytext2.substring(mytext2.indexOf("|") + 1,
                            mytext2.length());
                    String[] s2 = s1.split("-");
                    String id = s2[0];
                    String count = s2[1];
                    Map<String, String> params = new HashMap<String, String>();
                    String jsonString = "[{\"Id\":" + id + ",\"Count\":"
                            + count + ",\"type\":3}]";
                    params.put("CartItem", jsonString);

                    MyOkHttpUtils
                            .postData(UrlContact.URL_SHOPPING_CART, params)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    MyToastUtil.showShortMessage("网络连接失败！");
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    if (response != null) {
                                        Gson gson = new Gson();
                                        ShoppingCartBean shoppingCartBean = gson.fromJson(response, ShoppingCartBean.class);
                                        if (shoppingCartBean != null) {
                                            if (shoppingCartBean.isResult()) {
                                                ShoppingCartBean.DataBean dataBean = shoppingCartBean.getData();
                                                if (dataBean != null) {
                                                    items = dataBean.getProduct();
                                                    zongjia = 0;
                                                    freePrice = 0;
                                                    for (int i = 0; i < items.size(); i++) {
                                                        zongjia += items.get(i).getPrice() * items.get(i).getCount();
                                                        freePrice += items.get(i).getFree_Price();
                                                    }
                                                    ShoppingCarActivity.items2 = items;

                                                    Intent intent = new Intent(TuanGouActivity.this, DingdanActivity.class);
                                                    intent.putExtra("from", "activity");
                                                    intent.putExtra("index", index);
                                                    intent.putExtra("zongjia", zongjia - freePrice);
                                                    intent.putExtra("_freePrice", freePrice);
                                                    startActivity(intent);
                                                }
                                            } else {
                                                MyToastUtil.showShortMessage(shoppingCartBean.getMessage());
                                            }
                                        }
                                    }
                                }
                            });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else if (url.contains("JumpNyProDetail")) {
                String mytext2;
                try {
                    mytext2 = java.net.URLDecoder.decode(path, "utf-8");
                    String s1[] = mytext2.split("\\|");
                    String id = s1[1];
                    Intent intent = new Intent(TuanGouActivity.this, ProductDetailActivity.class);
                    intent.putExtra("id", Integer.valueOf(id));
                    intent.putExtra("name", "");
                    intent.putExtra("type", "");
                    startActivity(intent);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            } else if (url.contains("InviteBegin")) {
                Intent intent = new Intent(TuanGouActivity.this, MyInvitationActivity.class);
                startActivity(intent);
            } else if (url.equals(UrlContact.WEB_URL_STRING + "/")
                    || url.equals(UrlContact.WEB_URL_STRING + "/Home/Index")
                    || url.equals(UrlContact.WEB_URL_STRING + "/home/index")
                    || url.equals(UrlContact.WEB_URL_STRING + "/home/Index.html")
                    || url.equals(UrlContact.WEB_URL_STRING + "/UserCenter.html")
                    || url.equals(UrlContact.WEB_URL_STRING + "/UserCenter/Index")) {
                finish();
            } else if (url.contains("/User/Login")) {//http://m.16899.com/User/Login
                Intent intent = new Intent(TuanGouActivity.this, LoginActivity.class);
                startActivity(intent);
            } else if (url.startsWith("mailto:") || url.startsWith("tel:") || url.startsWith("geo:")) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            } else if (url.contains("ye=")) {
                String text;
                String balanceMoney;
                try {
                    text = java.net.URLDecoder.decode(path, "utf-8");
                    String s1[] = text.split("ye=");
                    balanceMoney = s1[1];
                    PayActivity.balanceMoney = balanceMoney;
                    finish();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            } else if (url.contains("PurProtocolDetail")) {
                //view.postUrl("http://192.168.80.192:8099/UserCenter/PurProtocolDetail?protocolId=33&loginKey=15962692042&deviceId=21de4829-e262-3d4b-b5ad-086eaba8c2cf", null);
                view.loadUrl(url + "&loginKey=" + mLoginKey + "&deviceId=" + mDeviceId);
            } else if (url.contains("/Product/FarmDetail/")) {
                //http://ttm.16899.com/Product/FarmDetail/1727.html
                String id = url.split("/Product/FarmDetail/")[1].split("\\.html")[0];
                Intent intent = new Intent(TuanGouActivity.this, ProductDetailActivity.class);
                intent.putExtra("id", Integer.valueOf(id));
                intent.putExtra("name", "");
                intent.putExtra("type", "");
                intent.putExtra("isSecKill", 2);
                startActivity(intent);
            } else if (url.contains("/Product/Detail/")) {
                //http://m.16899.com/Product/Detail/1725.html
                String id = url.split("/Product/Detail/")[1].split("\\.html")[0];
                Intent intent = new Intent(TuanGouActivity.this, ProductDetailActivity.class);
                intent.putExtra("id", Integer.valueOf(id));
                intent.putExtra("name", "");
                intent.putExtra("type", "");
                startActivity(intent);
            } else if (url.contains("/Product/list.html?searchText=%E5%A4%8D%E5%90%88%E8%82%A5")) {
                //http://ttm.16899.com/Product/list.html?searchText=%E5%A4%8D%E5%90%88%E8%82%A5
                Intent intent = new Intent(TuanGouActivity.this, ProductListActivity.class);
                intent.putExtra("type", "复合肥");
                intent.putExtra("from", "search");
                startActivity(intent);
            } else if (url.contains("/user/login.html?returnurl=/home/special.html")) {
                Intent intent = new Intent(TuanGouActivity.this, LotteryActivity.class);
                startActivity(intent);
            } else if (url.contains("/Product/list.html?searchText=%E4%B8%80%E5%93%81%E4%BC%97%E5%88%9B")) {
                //http://ttm.16899.com/Product/list.html?searchText=%E4%B8%80%E5%93%81
                Intent intent = new Intent(TuanGouActivity.this, ProductListActivity.class);
                intent.putExtra("type", "一品众创");
                intent.putExtra("from", "search");
                startActivity(intent);
            } else {
                view.loadUrl(url);
            }

            return true;

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //设定加载开始的操作
            MyProgressDialog.show(TuanGouActivity.this, true, true);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //设定加载结束的操作
            MyProgressDialog.cancel();
        }

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (type == 1) {
                PayActivity.isShowBalanceDialog = 3;
            }
            if (webview.canGoBack()) {
                if (webview.getUrl().equals("http://sys.16899.com/Home/Login?returnUrl=http://sys.16899.com/")) {
                    finish();
                } else {

                    webview.goBack();
                }
            } else {
                finish();
            }
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        layout_webview.removeView(webview);
        webview.destroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
