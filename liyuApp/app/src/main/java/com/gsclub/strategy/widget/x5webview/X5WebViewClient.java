package com.gsclub.strategy.widget.x5webview;

import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Created by hlw on 2018/3/5.
 * 对x5webview中的部分标签进行处理
 */

public class X5WebViewClient extends WebViewClient {


    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        // 步骤2：根据协议的参数，判断是否是所需要的url
        // 一般根据scheme（协议格式） & authority（协议名）判断（前两个参数）
        //假定传入进来的 url = "js://webview?arg1=111&arg2=222"（同时也是约定好的需要拦截的）
//        if (!url.contains("stock_prefix"))
//            return true;
//
//        Uri uri = Uri.parse(url);
//        // 如果url的协议 = 预先约定的 js 协议
//        // 就解析往下解析参数
//        if (uri.getScheme().equals("http")) {
//            // 如果 authority  = 预先约定协议里的 webview，即代表都符合约定的协议
//            // 所以拦截url,下面JS开始调用Android需要的方法
//            Set<String> collection = uri.getQueryParameterNames();
//            String stockSymbol = null;
//
//            for (String s : collection) {
//                if ("stock_prefix".equals(s))
//                    stockSymbol = uri.getQueryParameter(s);
//            }
//
//            if (TextUtils.isEmpty(stockSymbol))
//                return true;
//
//            if (!TextUtils.isEmpty(stockSymbol))
//                RxBus.get().send(EventCode.SKIP_TO_SELECT_STOCK_ACTIVITY, stockSymbol);
//        }
        return true;
//            return super.shouldOverrideUrlLoading(webView, url);
    }

    @Override
    public void onPageFinished(WebView webView, String s) {
        super.onPageFinished(webView, s);

//        WebViewUtils.loadJS(webView);
    }
}
