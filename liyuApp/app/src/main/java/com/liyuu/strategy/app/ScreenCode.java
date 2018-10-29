package com.liyuu.strategy.app;

import android.support.annotation.IntDef;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.SOURCE)
@IntDef({ScreenCode.WIDTH, ScreenCode.HEIGHT})
public @interface ScreenCode {
    /**
     * 获取宽度
     */
    int WIDTH = 1;
    /**
     * 获取高度
     */
    int HEIGHT = 2;
}
