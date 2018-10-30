package com.gsclub.strategy.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by hlw on 2018/1/2.
 */

public class OptionalTopStockBean implements Serializable {
    private String symbol;
    @SerializedName("prod_name")
    private String stockName;
    @SerializedName("px_change")
    private double pxChange;
    @SerializedName("px_change_rate")
    private double pxChangeRate;
    @SerializedName("last_px")
    private double lastPx;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public double getPxChange() {
        return pxChange;
    }

    public void setPxChange(double pxChange) {
        this.pxChange = pxChange;
    }

    public double getPxChangeRate() {
        return pxChangeRate;
    }

    public void setPxChangeRate(double pxChangeRate) {
        this.pxChangeRate = pxChangeRate;
    }

    public double getLastPx() {
        return lastPx;
    }

    public void setLastPx(double lastPx) {
        this.lastPx = lastPx;
    }
}
