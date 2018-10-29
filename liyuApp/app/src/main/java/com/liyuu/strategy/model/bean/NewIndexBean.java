package com.liyuu.strategy.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NewIndexBean implements Serializable {
    private String hold_day;  //持仓类型
    @SerializedName("level_type")
    private String levelType;  //股票类型
    private String gearing;  //杠杆
    private String deposit;  //押金
    private String bbin;  //体验金
    private String desc;  //
    @SerializedName("max_stop_line")
    private String maxStopLine; // 止损线
    @SerializedName("max_stop_ratio")
    private String maxStopRatio;  //止损比率

    public String getHold_day() {
        return hold_day;
    }

    public void setHold_day(String hold_day) {
        this.hold_day = hold_day;
    }

    public String getLevelType() {
        return levelType;
    }

    public void setLevelType(String levelType) {
        this.levelType = levelType;
    }

    public String getGearing() {
        return gearing;
    }

    public void setGearing(String gearing) {
        this.gearing = gearing;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMaxStopLine() {
        return maxStopLine;
    }

    public void setMaxStopLine(String maxStopLine) {
        this.maxStopLine = maxStopLine;
    }

    public String getMaxStopRatio() {
        return maxStopRatio;
    }

    public void setMaxStopRatio(String maxStopRatio) {
        this.maxStopRatio = maxStopRatio;
    }

    public String getBbin() {
        return bbin;
    }

    public void setBbin(String bbin) {
        this.bbin = bbin;
    }
}
