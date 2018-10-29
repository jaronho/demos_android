package com.liyuu.strategy.ui.stock.other.bean;

import java.io.Serializable;

/**
 * author：ajiang
 * mail：1025065158@qq.com
 * blog：http://blog.csdn.net/qqyanjiang
 */
public class KLineBean implements Serializable {
    public String date;
    public float open;
    public float close;
    public float high;
    public float low;
    public float vol;
    public String minute;

    @Override
    public String toString() {
        return "KLineBean{" +
                "date='" + date + '\'' +
                ", open=" + open +
                ", close=" + close +
                ", high=" + high +
                ", low=" + low +
                ", vol=" + vol +
                ", minute='" + minute + '\'' +
                '}';
    }
}
