package com.liyuu.strategy.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SimulatedInfoBean implements Serializable {

    /**
     * id : 3
     * uid : 3
     * total_money : 100000.0000
     * balance_money : 100000.0000
     * freeze_money : 0.0000
     * c_time : 1532932174
     * u_time : 1532932174
     * market_price : 0
     * profit_loss : 0
     */

    private String id;
    private String uid;
    @SerializedName("total_money")
    private String totalMoney;
    @SerializedName("balance_money")
    private String balanceMoney;
    @SerializedName("freeze_money")
    private String freezeMoney;
    @SerializedName("c_time")
    private String cTime;
    @SerializedName("u_time")
    private String uTime;
    @SerializedName("market_price")
    private String marketPrice;
    @SerializedName("profit_loss")
    private String profitLoss;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getBalanceMoney() {
        return balanceMoney;
    }

    public void setBalanceMoney(String balanceMoney) {
        this.balanceMoney = balanceMoney;
    }

    public String getFreezeMoney() {
        return freezeMoney;
    }

    public void setFreezeMoney(String freezeMoney) {
        this.freezeMoney = freezeMoney;
    }

    public String getcTime() {
        return cTime;
    }

    public void setcTime(String cTime) {
        this.cTime = cTime;
    }

    public String getuTime() {
        return uTime;
    }

    public void setuTime(String uTime) {
        this.uTime = uTime;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getProfitLoss() {
        return profitLoss;
    }

    public void setProfitLoss(String profitLoss) {
        this.profitLoss = profitLoss;
    }
}
