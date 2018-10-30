package com.gsclub.strategy.ui.stock.other.bean;

import java.io.Serializable;

/**
 * Created by hlw on 2018/1/12.
 */

public class StockTradeBean implements Serializable {
    private String tradeLevel;
    private float price;
    private float number;

    public String getTradeLevel() {
        return tradeLevel;
    }

    public void setTradeLevel(String tradeLevel) {
        this.tradeLevel = tradeLevel;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getNumber() {
        return number;
    }

    public void setNumber(float number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "StockTradeBean{" +
                "tradeLevel='" + tradeLevel + '\'' +
                ", price=" + price +
                ", number=" + number +
                '}';
    }
}
