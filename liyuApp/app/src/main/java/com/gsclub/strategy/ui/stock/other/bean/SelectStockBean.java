package com.gsclub.strategy.ui.stock.other.bean;

import java.io.Serializable;

/**
 * Created by hlw on 2018/1/5.
 */

public class SelectStockBean implements Serializable {
    private String itemName;

    private float number;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public float getNumber() {
        return number;
    }

    public void setNumber(float number) {
        this.number = number;
    }
}
