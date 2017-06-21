package com.example.classes;

import java.util.List;

/**
 * Created by NY on 2017/2/24.
 * 首页产品分类item
 */

public class HomeProductItemBean {
    private String title;
    private List<HomePageBean> pageBeanList;

    public HomeProductItemBean(String title, List<HomePageBean> pageBeanList) {
        this.title = title;
        this.pageBeanList = pageBeanList;
    }

    public String getTitle() {
        return title;
    }

    public List<HomePageBean> getPageBeanList() {
        return pageBeanList;
    }

}
