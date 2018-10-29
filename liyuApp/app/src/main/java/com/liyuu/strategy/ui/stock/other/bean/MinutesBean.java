package com.liyuu.strategy.ui.stock.other.bean;


import java.io.Serializable;

public class MinutesBean implements Serializable{
    public String time;
    public float cjprice;
    public long cjnum;
    public float avprice = Float.NaN;
    public float per;
    public float cha;
    public float total;
    public int color = 0xff000000;

    @Override
    public String toString() {
        return "MinutesBean{" +
                "time='" + time + '\'' +
                ", cjprice=" + cjprice +
                ", cjnum=" + cjnum +
                ", avprice=" + avprice +
                ", per=" + per +
                ", cha=" + cha +
                ", total=" + total +
                ", color=" + color +
                '}';
    }
}
