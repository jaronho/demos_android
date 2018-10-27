package com.liyuu.strategy.model.bean;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SearchStockBean extends RealmObject implements Serializable {

    @PrimaryKey
    private long addTime;//主键（数据添加的时间戳）
    private String sname;//股票名称
    private String symbol;//股票代码

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
