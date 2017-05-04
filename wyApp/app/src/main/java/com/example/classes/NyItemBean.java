package com.example.classes;

/**
 * Created by NY on 2017/3/9.
 * 我的农一的列表展示
 */

public class NyItemBean {
    private int type;
    private int icon;
    private String title;

    public NyItemBean(int type, int icon, String title) {
        this.type = type;
        this.icon = icon;
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }
}
