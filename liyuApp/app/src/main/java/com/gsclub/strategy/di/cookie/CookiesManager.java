package com.gsclub.strategy.di.cookie;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * 自动管理Cookies
 */
public class CookiesManager implements CookieJar {
//    public final PersistentCookieStore cookieStore = new PersistentCookieStore(App.getInstance());

    private PersistentCookieStore cookieStore;

    public CookiesManager(PersistentCookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        return cookies;
    }

    public void removeAll() {
        cookieStore.removeAll();
    }


}
