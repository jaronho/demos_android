package com.liyuu.strategy.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CashIndexBean implements Serializable {

    /**
     * bank_no : 0146
     * bank_name : 中国工商银行
     * logo : http://static.huijindou.com/public/uploads/images/20170622/a2f1aed1922fc02a648a576636a96425.jpg
     * balance_money : 84127.52
     * cash_fee : 2
     */

    @SerializedName("bank_no")
    private String bankNo;
    @SerializedName("bank_name")
    private String bankName;
    private String logo;
    @SerializedName("enable_cash_money")
    private String enableCashMoney;
    @SerializedName("cash_fee")
    private String cashFee;
    private String desc;

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getEnableCashMoney() {
        return enableCashMoney;
    }

    public void setEnableCashMoney(String enableCashMoney) {
        this.enableCashMoney = enableCashMoney;
    }

    public String getCashFee() {
        return cashFee;
    }

    public void setCashFee(String cashFee) {
        this.cashFee = cashFee;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
