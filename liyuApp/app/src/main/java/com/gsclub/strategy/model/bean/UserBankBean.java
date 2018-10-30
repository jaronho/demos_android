package com.gsclub.strategy.model.bean;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class UserBankBean implements Serializable {

    /**
     * logo : http://static.huijindou.com//Public/upload/information/2015/03/23/abmfyi.jpg
     * bank_name : 中国建设银行
     * bank_no_four : 2236
     * tip : 如需变动 联系客服电话:1856XXXXXX
     * one_limit : 10000
     * day_limit : 50000
     */

    private String logo;
    @SerializedName("bank_name")
    private String bankName;
    @SerializedName("bank_no_four")
    private String bankNoFour;
    private String tip;
    @SerializedName("one_limit")
    private int oneLimit;
    @SerializedName("day_limit")
    private int dayLimit;
    @SerializedName("bank_status")
    private String bankStatus;
    @SerializedName("real_name")
    private String realName;
    @SerializedName("card_id")
    private String cardId;

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

    public String getBankNoFour() {
        return bankNoFour;
    }

    public void setBankNoFour(String bankNoFour) {
        this.bankNoFour = bankNoFour;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public int getOneLimit() {
        return oneLimit;
    }

    public void setOneLimit(int oneLimit) {
        this.oneLimit = oneLimit;
    }

    public int getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(int dayLimit) {
        this.dayLimit = dayLimit;
    }

    public String getBankStatus() {
        return bankStatus;
    }

    public void setBankStatus(String bankStatus) {
        this.bankStatus = bankStatus;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
}
