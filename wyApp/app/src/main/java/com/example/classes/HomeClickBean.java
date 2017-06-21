package com.example.classes;

/**
 * Created by NY on 2017/2/26.
 * 首页点击事件
 */

public class HomeClickBean {
    private int type;
    private String url;
    private String title;

    public HomeClickBean(int type, String url, String title) {
        this.type = type;
        this.url = url;
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }
}
