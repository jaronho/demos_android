package com.liyuu.strategy.app;

import android.support.annotation.IntDef;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.SOURCE)
@IntDef({TextViewDrawCode.RIGHT, TextViewDrawCode.LEFT,
        TextViewDrawCode.TOP, TextViewDrawCode.BOTTOM,
        TextViewDrawCode.STRING_LEFT, TextViewDrawCode.STRING_RIGHT, TextViewDrawCode.NONE})
public @interface TextViewDrawCode {
    /**
     * 无绘制/不添加
     */
    int NONE = -1;

    //textview添加图片位置
    /**
     * 在右边绘制图片
     */
    int RIGHT = 1;
    /**
     * 在左边绘制图片
     */
    int LEFT = 2;
    /**
     * 在上方绘制图片
     */
    int TOP = 3;
    /**
     * 在下方绘制图片
     */
    int BOTTOM = 4;


    //string文字添加的位置
    /**
     * 在右边添加文字
     */
    int STRING_RIGHT = 5;
    /**
     * 在左边添加文字
     */
    int STRING_LEFT = 6;
}
