package com.gsclub.strategy.model.bean;

import java.io.Serializable;

public class HomePayMessageBean implements Serializable {

    /**
     * oid : 10
     * uid : 3
     * sname : 中国联通
     * real_trade_num : 900
     * real_buy_price : 54.3100
     * symbol : 000028.SZ
     * money : 5
     * buy_time : 2018-08-10 14:56
     * nickname : 订单
     * headimg : http://static.huijindou.com/0
     * market : 4.8879
     * deposit : 0.5
     * gearing : 10
     * desc : <div style="font-size: 14px;color: #666666"> 投入<a style="color: #ff8400">0.5w元</a>保证金，以<a style="color: #ff8400">1:10倍</a>杠杆，获得<a style="color: #ff8400">5w元</a>资金，买入900股中国联通，买入市值4.8879万元</div>
     */

    private String oid;
    private String uid;
    private String sname;
    private String real_trade_num;
    private String real_buy_price;
    private String symbol;
    private String money;
    private String buy_time;
    private String nickname;
    private String headimg;
    private String market;
    private String deposit;
    private String gearing;
    private String desc;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getReal_trade_num() {
        return real_trade_num;
    }

    public void setReal_trade_num(String real_trade_num) {
        this.real_trade_num = real_trade_num;
    }

    public String getReal_buy_price() {
        return real_buy_price;
    }

    public void setReal_buy_price(String real_buy_price) {
        this.real_buy_price = real_buy_price;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getBuy_time() {
        return buy_time;
    }

    public void setBuy_time(String buy_time) {
        this.buy_time = buy_time;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getGearing() {
        return gearing;
    }

    public void setGearing(String gearing) {
        this.gearing = gearing;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
