package com.liyuu.strategy.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RankTradeBean implements Serializable {

    /**
     * id : 6
     * total_deposit : 400.00
     * symbol :
     * sname :
     * profit_loss : 100.00
     * status : 1
     * real_trade_num : 0
     * period : 0
     * real_buy_price : 0.00
     * real_sale_price : 0.00
     * loss_price : 0.00
     * settle_time : 1532500132
     * loss_deduction : 0
     */

    private String oid;//订单oid
    private String id;
    @SerializedName("total_deposit")
    private String totalDeposit;//总押金
    private String symbol;
    private String sname;//股票名称
    @SerializedName("profit_loss")
    private String profitLoss;//交易盈亏（亏损为负数）
    private String status; //盈亏情况 1.盈利 2.亏损
    @SerializedName("real_trade_num")
    private String realTradeNum;//交易股数
    private String period;//持有天数
    @SerializedName("real_buy_price")
    private String realBuyPrice; //买入价
    @SerializedName("real_sale_price")
    private String realSalePrice;//卖出价
    @SerializedName("loss_price")
    private String lossPrice;//止损价
    @SerializedName("settle_time")
    private String settleTime;//结算时间
    @SerializedName("settle_day")
    private String settleDay;
    @SerializedName("loss_deduction")
    private String lossDeduction;//亏损抵扣
    @SerializedName("text_color")
    private String textColor;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTotalDeposit() {
        return totalDeposit;
    }

    public void setTotalDeposit(String totalDeposit) {
        this.totalDeposit = totalDeposit;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getProfitLoss() {
        return profitLoss;
    }

    public void setProfitLoss(String profitLoss) {
        this.profitLoss = profitLoss;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRealTradeNum() {
        return realTradeNum;
    }

    public void setRealTradeNum(String realTradeNum) {
        this.realTradeNum = realTradeNum;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getRealBuyPrice() {
        return realBuyPrice;
    }

    public void setRealBuyPrice(String realBuyPrice) {
        this.realBuyPrice = realBuyPrice;
    }

    public String getRealSalePrice() {
        return realSalePrice;
    }

    public void setRealSalePrice(String realSalePrice) {
        this.realSalePrice = realSalePrice;
    }

    public String getLossPrice() {
        return lossPrice;
    }

    public void setLossPrice(String lossPrice) {
        this.lossPrice = lossPrice;
    }

    public String getSettleTime() {
        return settleTime;
    }

    public void setSettleTime(String settleTime) {
        this.settleTime = settleTime;
    }

    public String getSettleDay() {
        return settleDay;
    }

    public void setSettleDay(String settleDay) {
        this.settleDay = settleDay;
    }

    public String getLossDeduction() {
        return lossDeduction;
    }

    public void setLossDeduction(String lossDeduction) {
        this.lossDeduction = lossDeduction;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }
}
