package com.gsclub.strategy.ui.mine.webview;

import android.support.annotation.IntDef;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public @interface WebviewRightMode {

    /**
     * 没有右键
     */
    int NONE = 1;

    /**
     * url跳转右键
     * params 1.mode 2.showText(右键显示的文字) 3.url(跳转的url)
     */
    int URL = 2;

}
