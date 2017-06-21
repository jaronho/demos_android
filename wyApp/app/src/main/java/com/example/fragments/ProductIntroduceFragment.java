package com.example.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.nyapp.MyApplication;
import com.example.nyapp.R;
import com.example.util.ConnectionWork;
import com.example.util.UrlContact;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * 产品详情
 * 产品介绍
 */

public class ProductIntroduceFragment extends Fragment {
    @BindView(R.id.webView1)
    WebView mWebView1;
    private int mId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment4, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mId = bundle.getInt("Id");
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        if (mId != 0) {
            WebSettings settings = mWebView1.getSettings();
//            mWebView1.requestFocus();
            settings.setJavaScriptEnabled(true);
            settings.setAllowFileAccess(true);

            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            settings.setUseWideViewPort(true); //将图片调整到适合webview的大小
            settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

            settings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
            settings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
            settings.setDisplayZoomControls(false); //隐藏原生的缩放控件
            if (ConnectionWork.isConnect(MyApplication.sContext)) {
                settings.setCacheMode(WebSettings.LOAD_DEFAULT);
            } else {
                settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            }
            mWebView1.loadUrl(UrlContact.URL_PRODUCT_INTRODUCE + mId);
        }

    }
}