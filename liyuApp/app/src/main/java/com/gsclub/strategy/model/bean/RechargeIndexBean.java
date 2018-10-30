package com.gsclub.strategy.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RechargeIndexBean implements Serializable {

    /**
     * logo : http://static.huijindou.com//public/uploads/bank/abmfyi.jpg
     * bank_name : 中国建设银行
     * one_limit : 50000
     * day_limit : 200000
     * bank_limit : 单笔5万，单日20万
     * desc : 请输入充值金额，需大于100元
     * echo_money : 100
     * recharge_fee_per : 0.00
     * bank_no : 2236
     */

    private String logo;
    @SerializedName("bank_name")
    private String bankName;
    @SerializedName("one_limit")
    private String oneLimit;
    @SerializedName("day_limit")
    private String dayLimit;
    @SerializedName("bank_limit")
    private String bankLimit;
    private String desc;
    @SerializedName("echo_money")
    private int echoMoney;
    @SerializedName("recharge_fee_per")
    private String rechargeFeePer;
    @SerializedName("bank_no")
    private String bankNo;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getOneLimit() {
        return oneLimit;
    }

    public void setOneLimit(String oneLimit) {
        this.oneLimit = oneLimit;
    }

    public String getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(String dayLimit) {
        this.dayLimit = dayLimit;
    }

    public String getBankLimit() {
        return bankLimit;
    }

    public void setBankLimit(String bankLimit) {
        this.bankLimit = bankLimit;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getEchoMoney() {
        return echoMoney;
    }

    public void setEchoMoney(int echoMoney) {
        this.echoMoney = echoMoney;
    }

    public String getRechargeFeePer() {
        return rechargeFeePer;
    }

    public void setRechargeFeePer(String rechargeFeePer) {
        this.rechargeFeePer = rechargeFeePer;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }
}
