package com.gsclub.strategy.util;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.gsclub.strategy.app.App;
import com.gsclub.strategy.app.AppConfig;
import com.gsclub.strategy.app.SPKeys;

import java.util.List;
import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class WebCookieUtil {
    public static void saveCookie() {
        //登录成功保存用户的cookie 中id
        List<Cookie> cookies =
                App.getInstance()
                        .getAppComponent()
                        .getCookiesManager()
                        .loadForRequest(HttpUrl.parse(AppConfig.API_IP));

        for (Cookie cookie : cookies) {
            LogUtil.e(cookie.toString());
            if ("PHPSESSID".equals(cookie.name())) {
                //缓存登录时候的cookie信息，供webview使用
                PreferenceUtils.put(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_COOKIE_SESSION_ID, cookie.value());
                PreferenceUtils.put(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_COOKIE_DOMAIN, cookie.domain());
                return;
            }
        }
    }

    public static void setCookieToWebView(Context context, String url) {
        String sessionValue = PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_COOKIE_SESSION_ID);
        String domain = PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_COOKIE_DOMAIN);
        if (!TextUtils.isEmpty(sessionValue)) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                com.tencent.smtt.sdk.CookieSyncManager.createInstance(context);
            }
            com.tencent.smtt.sdk.CookieManager cookieManager = com.tencent.smtt.sdk.CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();
            StringBuffer sb = new StringBuffer();
            sb.append("PHPSESSID=").append(sessionValue).append("; path=/; domain=").append(domain);
//            LogUtil.d("同步前cookie：" + sb.toString());
            cookieManager.setCookie(url, sb.toString());
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                com.tencent.smtt.sdk.CookieSyncManager.getInstance().sync();
            } else {
                com.tencent.smtt.sdk.CookieManager.getInstance().flush();
            }
            String newCookie = cookieManager.getCookie(url);
            LogUtil.d("同步后cookie：" + newCookie);
        }
    }
}
