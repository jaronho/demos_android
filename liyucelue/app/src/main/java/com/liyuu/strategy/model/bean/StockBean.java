package com.liyuu.strategy.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StockBean implements Serializable {

    /**
     * prod_name : 博瑞传播
     * symbol : 600880.SS
     * last_px : 3.9
     * px_change_rate : 0.26
     */

    @SerializedName("prod_name")
    private String prodName;
    private String symbol;
    @SerializedName("last_px")
    private String lastPx;
    @SerializedName("px_change_rate")
    private String pxChangeRate;
    @SerializedName("px_change")
    private String pxChange;

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getLastPx() {
        return lastPx;
    }

    public void setLastPx(String lastPx) {
        this.lastPx = lastPx;
    }

    public String getPxChangeRate() {
        return pxChangeRate;
    }

    public void setPxChangeRate(String pxChangeRate) {
        this.pxChangeRate = pxChangeRate;
    }

    public String getPxChange() {
        return pxChange;
    }

    public void setPxChange(String pxChange) {
        this.pxChange = pxChange;
    }
}
