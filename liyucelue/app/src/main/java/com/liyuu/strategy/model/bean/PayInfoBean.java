package com.liyuu.strategy.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PayInfoBean implements Serializable {

    /**
     * oid_partner : 201408071000001539
     * sign_type : MD5
     * valid_order : 10080
     * busi_partner : 101001
     * no_order : 1534319294694159
     * dt_order : 20180815154814
     * name_goods : 鲤鱼理财
     * money_order : 0.01
     * notify_url : http://mapi.jiyoucai.com/callback/paycallback/lianlianCallBack
     * no_agree :
     * risk_item : {"frmsWareCategory":2009,"user_info_mercht_userno":13,"user_info_dt_register":19700101080000,"user_info_full_name":"叶良辰","user_info_id_no":350203199008130215,"userInfoIdentifyType":1,"userInfoIdentifyState":1}
     * sign : 1c3a26bcd5ab69e2ee433714c8bbd363
     * user_name : 叶良辰
     * bank_no : 6236681930002222236
     * card_id : 350203199008130215
     * money : 100
     * user_id : 13
     */

    @SerializedName("oid_partner")
    private String oidPartner;
    @SerializedName("sign_type")
    private String signType;
    @SerializedName("valid_order")
    private String validOrder;
    @SerializedName("busi_partner")
    private String busiPartner;
    @SerializedName("no_order")
    private String noOrder;
    @SerializedName("dt_order")
    private String dtOrder;
    @SerializedName("name_goods")
    private String nameGoods;
    @SerializedName("money_order")
    private String moneyOrder;
    @SerializedName("notify_url")
    private String notifyUrl;
    @SerializedName("no_agree")
    private String noAgree;
    @SerializedName("risk_item")
    private String riskItem;
    private String sign;
    @SerializedName("user_name")
    private String userName;
    @SerializedName("bank_no")
    private String bankNo;
    @SerializedName("card_id")
    private String cardId;
    private String money;
    @SerializedName("user_id")
    private String userId;

    public String getOidPartner() {
        return oidPartner;
    }

    public void setOidPartner(String oidPartner) {
        this.oidPartner = oidPartner;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getValidOrder() {
        return validOrder;
    }

    public void setValidOrder(String validOrder) {
        this.validOrder = validOrder;
    }

    public String getBusiPartner() {
        return busiPartner;
    }

    public void setBusiPartner(String busiPartner) {
        this.busiPartner = busiPartner;
    }

    public String getNoOrder() {
        return noOrder;
    }

    public void setNoOrder(String noOrder) {
        this.noOrder = noOrder;
    }

    public String getDtOrder() {
        return dtOrder;
    }

    public void setDtOrder(String dtOrder) {
        this.dtOrder = dtOrder;
    }

    public String getNameGoods() {
        return nameGoods;
    }

    public void setNameGoods(String nameGoods) {
        this.nameGoods = nameGoods;
    }

    public String getMoneyOrder() {
        return moneyOrder;
    }

    public void setMoneyOrder(String moneyOrder) {
        this.moneyOrder = moneyOrder;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getNoAgree() {
        return noAgree;
    }

    public void setNoAgree(String noAgree) {
        this.noAgree = noAgree;
    }

    public String getRiskItem() {
        return riskItem;
    }

    public void setRiskItem(String riskItem) {
        this.riskItem = riskItem;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
