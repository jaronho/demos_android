package com.gsclub.strategy.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StockSettleDetailBean implements Serializable {

    /**
     * symbol : 603222.SS
     * sname : 济民制药
     * total_period : 1
     * real_buy_price : 9.7900
     * real_trade_num : 1000
     * real_sale_price : 9.7500
     * profit_loss : -39.9900
     * profit_loss_ratio : 0.0000
     * total_deposit : 1000.0000
     * total_coop_fee : 50.0000
     * order_id : 1533884370008145
     * period : 1
     * deposit : 1000.0000
     * coop_fee : 50.0000
     * buy_time : 2018-08-13 17:26
     * sale_time : 2018-08-15 17:58
     * sale_desc : 客户下指令出售
     * hold_period : 2
     * defer_period : 0
     * add_deposit : 0
     * defer_coop_fee : 0
     * deduction_deposit : -39.9900
     * return_deposit : 910.01
     */

    private String symbol;
    private String sname;
    @SerializedName("total_period")
    private int totalPeriod;//总的持股周期
    @SerializedName("real_buy_price")
    private String realOneStockBuyPrice;//买入价格
    @SerializedName("real_trade_num")
    private int stockBuyNumber;//股票数量
    @SerializedName("real_sale_price")
    private String realOneStockSalePrice;//卖出价格
    @SerializedName("profit_loss")
    private String profitLoss;//盈亏金额
    @SerializedName("profit_loss_ratio")
    private String profitLossRatio;//盈亏比率
    @SerializedName("total_deposit")
    private String totalDeposit; //总押金
    @SerializedName("total_coop_fee")
    private String totalCoopFee;//合作费
    @SerializedName("order_id")
    private String oid;//订单编号
    private String period;//初始持股周期
    private String deposit;//初始押金
    @SerializedName("coop_fee")
    private String originCoopFee;//初始合作费
    @SerializedName("buy_time")
    private String buyTime;//买入时间
    @SerializedName("sale_time")
    private String saleTime; //卖出时间
    @SerializedName("sale_desc")
    private String saleType;//卖出方式
    @SerializedName("hold_period")
    private String holdPeriod;//持股天数
    @SerializedName("defer_period")
    private String deferredPeriod; //递延天数
    @SerializedName("add_deposit")
    private String addDeposit;//增加押金
    @SerializedName("defer_coop_fee")
    private String deferredCoopFee;//递延合作费
    @SerializedName("deduction_deposit")
    private String deductionDeposit;//返回保证金
    @SerializedName("return_deposit")
    private String returnDeposit;
    @SerializedName("defer_period_desc")
    private String deferPeriodDesc;

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

    public int getTotalPeriod() {
        return totalPeriod;
    }

    public void setTotalPeriod(int totalPeriod) {
        this.totalPeriod = totalPeriod;
    }

    public String getRealOneStockBuyPrice() {
        return realOneStockBuyPrice;
    }

    public void setRealOneStockBuyPrice(String realOneStockBuyPrice) {
        this.realOneStockBuyPrice = realOneStockBuyPrice;
    }

    public int getStockBuyNumber() {
        return stockBuyNumber;
    }

    public void setStockBuyNumber(int stockBuyNumber) {
        this.stockBuyNumber = stockBuyNumber;
    }

    public String getRealOneStockSalePrice() {
        return realOneStockSalePrice;
    }

    public void setRealOneStockSalePrice(String realOneStockSalePrice) {
        this.realOneStockSalePrice = realOneStockSalePrice;
    }

    public String getProfitLoss() {
        return profitLoss;
    }

    public void setProfitLoss(String profitLoss) {
        this.profitLoss = profitLoss;
    }

    public String getProfitLossRatio() {
        return profitLossRatio;
    }

    public void setProfitLossRatio(String profitLossRatio) {
        this.profitLossRatio = profitLossRatio;
    }

    public String getTotalDeposit() {
        return totalDeposit;
    }

    public void setTotalDeposit(String totalDeposit) {
        this.totalDeposit = totalDeposit;
    }

    public String getTotalCoopFee() {
        return totalCoopFee;
    }

    public void setTotalCoopFee(String totalCoopFee) {
        this.totalCoopFee = totalCoopFee;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getOriginCoopFee() {
        return originCoopFee;
    }

    public void setOriginCoopFee(String originCoopFee) {
        this.originCoopFee = originCoopFee;
    }

    public String getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(String buyTime) {
        this.buyTime = buyTime;
    }

    public String getSaleTime() {
        return saleTime;
    }

    public void setSaleTime(String saleTime) {
        this.saleTime = saleTime;
    }

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }

    public String getHoldPeriod() {
        return holdPeriod;
    }

    public void setHoldPeriod(String holdPeriod) {
        this.holdPeriod = holdPeriod;
    }

    public String getDeferredPeriod() {
        return deferredPeriod;
    }

    public void setDeferredPeriod(String deferredPeriod) {
        this.deferredPeriod = deferredPeriod;
    }

    public String getAddDeposit() {
        return addDeposit;
    }

    public void setAddDeposit(String addDeposit) {
        this.addDeposit = addDeposit;
    }

    public String getDeferredCoopFee() {
        return deferredCoopFee;
    }

    public void setDeferredCoopFee(String deferredCoopFee) {
        this.deferredCoopFee = deferredCoopFee;
    }

    public String getDeductionDeposit() {
        return deductionDeposit;
    }

    public void setDeductionDeposit(String deductionDeposit) {
        this.deductionDeposit = deductionDeposit;
    }

    public String getReturnDeposit() {
        return returnDeposit;
    }

    public void setReturnDeposit(String returnDeposit) {
        this.returnDeposit = returnDeposit;
    }

    public String getDeferPeriodDesc() {
        return deferPeriodDesc;
    }

    public void setDeferPeriodDesc(String deferPeriodDesc) {
        this.deferPeriodDesc = deferPeriodDesc;
    }
}
