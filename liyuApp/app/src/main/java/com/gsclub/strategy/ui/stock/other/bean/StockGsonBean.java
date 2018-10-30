package com.gsclub.strategy.ui.stock.other.bean;

import java.io.Serializable;

/**
 * Created by hlw on 2018/1/11.
 */

public class StockGsonBean implements Serializable {
    //[201801050931, 2.49, 2.51, 959500, 2405117]
    private String time;
    private String last_px;
    private String avg_px;
    private String business_amount;
    private String business_balance;

    public StockGsonBean() {

    }

    public StockGsonBean(String time, String last_px, String avg_px, String business_amount, String business_balance) {
        this.time = time;
        this.last_px = last_px;
        this.avg_px = avg_px;
        this.business_amount = business_amount;
        this.business_balance = business_balance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLast_px() {
        return last_px;
    }

    public void setLast_px(String last_px) {
        this.last_px = last_px;
    }

    public String getAvg_px() {
        return avg_px;
    }

    public void setAvg_px(String avg_px) {
        this.avg_px = avg_px;
    }

    public String getBusiness_amount() {
        return business_amount;
    }

    public void setBusiness_amount(String business_amount) {
        this.business_amount = business_amount;
    }

    public String getBusiness_balance() {
        return business_balance;
    }

    public void setBusiness_balance(String business_balance) {
        this.business_balance = business_balance;
    }

    @Override
    public String toString() {
        return "StockGsonBean{" +
                "time='" + time + '\'' +
                ", last_px='" + last_px + '\'' +
                ", avg_px='" + avg_px + '\'' +
                ", business_amount='" + business_amount + '\'' +
                ", business_balance='" + business_balance + '\'' +
                '}';
    }
}
