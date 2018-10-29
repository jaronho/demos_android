package com.liyuu.strategy.model.bean.start;

import android.content.Context;

import java.io.Serializable;

/**
 * {@link com.liyuu.strategy.ui.transaction.activity.BuyingActivity#start(Context, BuyingActivityStartBean)}
 * 传入的参数为直接显示的参数，不做它用
 */
public class BuyingActivityStartBean implements Serializable {
    private String symbol;
    private String sName;
    private String lastPrice;
    private String pxChange;
    private String pxChangeRate;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(String lastPrice) {
        this.lastPrice = lastPrice;
    }

    public String getPxChange() {
        return pxChange;
    }

    public void setPxChange(String pxChange) {
        this.pxChange = pxChange;
    }

    public String getPxChangeRate() {
        return pxChangeRate;
    }

    public void setPxChangeRate(String pxChangeRate) {
        this.pxChangeRate = pxChangeRate;
    }
}
