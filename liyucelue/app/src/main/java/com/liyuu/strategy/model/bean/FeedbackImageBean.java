package com.liyuu.strategy.model.bean;

import java.io.Serializable;

public class FeedbackImageBean implements Serializable {
    private String url;// 绝对地址，用于加载图片用
    private String save_path;// 相对地址，用于上传后端用

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSave_path() {
        return save_path;
    }

    public void setSave_path(String save_path) {
        this.save_path = save_path;
    }
}
