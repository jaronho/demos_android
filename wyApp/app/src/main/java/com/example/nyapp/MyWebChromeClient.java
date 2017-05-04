package com.example.nyapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebChromeClient.CustomViewCallback;

@SuppressLint("ShowToast")
public class MyWebChromeClient extends WebChromeClient{

	private WebChromeClient mWrappedClient;
	protected MyWebChromeClient(WebChromeClient wrappedClient) {
        mWrappedClient = wrappedClient;
    }
    /** } */
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        mWrappedClient.onProgressChanged(view, newProgress);
    }
    /** } */
    @Override
    public void onReceivedTitle(WebView view, String title) {
        mWrappedClient.onReceivedTitle(view, title);
    }
    /** } */
    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        mWrappedClient.onReceivedIcon(view, icon);
    }
    /** } */
    @Override
    public void onReceivedTouchIconUrl(WebView view, String url,
            boolean precomposed) {
        mWrappedClient.onReceivedTouchIconUrl(view, url, precomposed);
    }
    /** } */
    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        mWrappedClient.onShowCustomView(view, callback);
    }
    /** } */
    @Override
    public void onHideCustomView() {
        mWrappedClient.onHideCustomView();
    }
    /** } */
    @Override
    public boolean onCreateWindow(WebView view, boolean dialog,
            boolean userGesture, Message resultMsg) {
        return mWrappedClient.onCreateWindow(view, dialog, userGesture, resultMsg);
    }
    /** } */
    @Override
    public void onRequestFocus(WebView view) {
        mWrappedClient.onRequestFocus(view);
    }
    /** } */
    @Override
    public void onCloseWindow(WebView window) {
        mWrappedClient.onCloseWindow(window);
    }
    /** } */
    @Override
    public boolean onJsAlert(WebView view, String url, String message,
            final JsResult result) {
    	/* AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());  
         
	        builder.setTitle("温馨提示")  
	                .setMessage(message)
	                .setPositiveButton("确定", null);  
	                  
	        // 不需要绑定按键事�? 
	        // 屏蔽keycode等于84之类的按�? 
	        builder.setOnKeyListener(new OnKeyListener() {  
	            public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {  
	                Log.v("onJsAlert", "keyCode==" + keyCode + "event="+ event);  
	                return true;  
	            }  
	        });  
	        // 禁止响应按back键的事件  
	        builder.setCancelable(false);  
	        AlertDialog dialog = builder.create();  
	        dialog.show();  
	        result.confirm();// 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容�?  
	        return true;*/
        return mWrappedClient.onJsAlert(view, url, message, result);
    }
    /** } */
    @Override
    public boolean onJsConfirm(WebView view, String url, String message,
            final JsResult result) {
    	// TODO Auto-generated method stub
    			final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());  
    	        builder.setTitle("温馨提示 ")  
    	                .setMessage(message)
    	                .setPositiveButton("确定",new OnClickListener() {  
    	                            public void onClick(DialogInterface dialog,int which) {  
    	                                result.confirm();  
    	                            }  
    	                        })  
    	                .setNeutralButton("取消", new OnClickListener() {  
    	                    public void onClick(DialogInterface dialog, int which) {  
    	                        result.cancel();  
    	                    }  
    	                });  
    	        builder.setOnCancelListener(new OnCancelListener() {  
    	            @Override  
    	            public void onCancel(DialogInterface dialog) {  
    	                result.cancel();  
    	            }  
    	        });  
    	  
    	        // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题  
    	        builder.setOnKeyListener(new OnKeyListener() {  
    	            @Override  
    	            public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {  
    	                Log.v("onJsConfirm", "keyCode==" + keyCode + "event="+ event);  
    	                return true;  
    	            }  
    	        });  
    	        // 禁止响应按back键的事件  
    	         builder.setCancelable(false);  
    	        AlertDialog dialog = builder.create();  
    	        dialog.show();  
    			return true;
//        return mWrappedClient.onJsConfirm(view, url, message, result);
    }
    /** } */
    @Override
    public boolean onJsPrompt(WebView view, String url, String message,
            String defaultValue, JsPromptResult result) {
        return mWrappedClient.onJsPrompt(view, url, message, defaultValue, result);
    }
    /** } */
    @Override
    public boolean onJsBeforeUnload(WebView view, String url, String message,
            JsResult result) {
        return mWrappedClient.onJsBeforeUnload(view, url, message, result);
    }
    /** } */
    @Override
    public void onExceededDatabaseQuota(String url, String databaseIdentifier,
            long currentQuota, long estimatedSize, long totalUsedQuota,
            WebStorage.QuotaUpdater quotaUpdater) {
        mWrappedClient.onExceededDatabaseQuota(url, databaseIdentifier, currentQuota,
                estimatedSize, totalUsedQuota, quotaUpdater);
    }
    /** } */
    @Override
    public void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota,
            WebStorage.QuotaUpdater quotaUpdater) {
        mWrappedClient.onReachedMaxAppCacheSize(spaceNeeded, totalUsedQuota, quotaUpdater);
    }
    /** } */
    @Override
    public void onGeolocationPermissionsShowPrompt(String origin,
            GeolocationPermissions.Callback callback) {
        mWrappedClient.onGeolocationPermissionsShowPrompt(origin, callback);
    }
    /** } */
    @Override
    public void onGeolocationPermissionsHidePrompt() {
        mWrappedClient.onGeolocationPermissionsHidePrompt();
    }
    /** } */
    @Override
    public boolean onJsTimeout() {
        return mWrappedClient.onJsTimeout();
    }
    /** } */
    @Override
    @Deprecated
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
        mWrappedClient.onConsoleMessage(message, lineNumber, sourceID);
    }
    /** } */
    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        return mWrappedClient.onConsoleMessage(consoleMessage);
    }
    /** } */
    @Override
    public Bitmap getDefaultVideoPoster() {
        return mWrappedClient.getDefaultVideoPoster();
    }
    /** } */
    @Override
    public View getVideoLoadingProgressView() {
        return mWrappedClient.getVideoLoadingProgressView();
    }
    /** } */
    @Override
    public void getVisitedHistory(ValueCallback<String[]> callback) {
        mWrappedClient.getVisitedHistory(callback);
    }
    /** } */
    
    public void openFileChooser(ValueCallback<Uri> uploadFile) {
        ((MyWebChromeClient) mWrappedClient).openFileChooser(uploadFile);
    }
    public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType)
    {
      openFileChooser(uploadFile);
    }

    public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType,
        String capture)
    {
      openFileChooser(uploadFile);
    }
}
