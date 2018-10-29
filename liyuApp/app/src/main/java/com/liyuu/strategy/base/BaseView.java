package com.liyuu.strategy.base;

/**
 * View基类
 */
public interface BaseView {

    void showErrorMsg(String msg);

    void showLoading();

    void hideLoading();

    void finishUI();

    void initUI();
}
