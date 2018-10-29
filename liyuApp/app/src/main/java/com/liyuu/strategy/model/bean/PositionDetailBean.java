package com.liyuu.strategy.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PositionDetailBean implements Serializable {


    /**
     * market_price : 5431.00
     * sname : 中国联通
     * symbol : 000028.SZ
     * end_time : 2018-08-09 23:50
     * total_deposit : 1000.00
     * last_loss_price : 4.00
     * total_period : 1
     * period_auto_status : 0
     * period : 1
     * buy_time : 2018-08-08 14:38
     * real_buy_price : 54.31
     * current_price : 54.00
     * profit_loss : 0.00
     * profit_loss_per : 0.00
     * balance_money : 100000.00
     * min_stop_loss : 0.20
     * max_stop_loss : 6.80
     */
    @SerializedName("market_price")
    private String marketPrice;//市值
    private String sname;
    private String symbol;
    @SerializedName("end_time")
    private String endTime;//持仓到期时间
    @SerializedName("total_deposit")
    private String totalDeposit;//当前押金
    //    @SerializedName("real_use_deposit")
//    private double realUseDeposit;//真实使用押金（用户修改调高止损价后该值小于总押金）
    @SerializedName("last_loss_price")
    private String lastLossPrice;//当前止损价
    @SerializedName("total_period")
    private String totalPeriod;//当前持仓类型
    @SerializedName("period_auto_status")
    private String periodAutoStatus;//递延状态  0未设置 1开启 2关闭
    private String period;//持股周期T+1
    @SerializedName("buy_time")
    private String buyTime;//买入时间
    @SerializedName("real_buy_price")
    private String realBuyPrice; //真实买入价
    @SerializedName("current_price")
    private String currentPrice; //现价
    @SerializedName("profit_loss")
    private String profitLoss; //浮动盈亏
    @SerializedName("profit_loss_per")
    private String profitLossPer;//浮动盈亏百分比
    @SerializedName("balance_money")
    private String balanceMoney;// 余额
    @SerializedName("min_stop_loss")
    private String minStopLoss;//最小止损线
    @SerializedName("real_trade_num")
    private String realTradeNum;//持股数量
    @SerializedName("max_stop_ratio")
    private String maxStopRatio;//押金最大亏损比率 k值
    @SerializedName("is_activity")
    private String isActivity;//是否是新手福利
    @SerializedName("is_add")
    private String isCanAddDeposit;//是否能添加押金 is_add  1可追加  2不可追加
    @SerializedName("max_loss_price")
    private String maxStopLossPrice;//股票最高可调止损价（由服务器计算得到）
//    @SerializedName("is_defer")
//    private String isCanDefer;//1为可以递延  2为不可递延
//    @SerializedName("cannot_defer_desc")
//    private String cannotDeferDesc;//不可以递延时，提示给用户的信息
    @SerializedName("en_defer_desc")
    private String enDeferDesc;
    @SerializedName("un_defer_desc")
    private String unDeferDesc;

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
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

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTotalDeposit() {
        return totalDeposit;
    }

    public void setTotalDeposit(String totalDeposit) {
        this.totalDeposit = totalDeposit;
    }

    public String getLastLossPrice() {
        return lastLossPrice;
    }

    public void setLastLossPrice(String lastLossPrice) {
        this.lastLossPrice = lastLossPrice;
    }

    public String getTotalPeriod() {
        return totalPeriod;
    }

    public void setTotalPeriod(String totalPeriod) {
        this.totalPeriod = totalPeriod;
    }

    public String getPeriodAutoStatus() {
        return periodAutoStatus;
    }

    public void setPeriodAutoStatus(String periodAutoStatus) {
        this.periodAutoStatus = periodAutoStatus;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(String buyTime) {
        this.buyTime = buyTime;
    }

    public String getRealBuyPrice() {
        return realBuyPrice;
    }

    public void setRealBuyPrice(String realBuyPrice) {
        this.realBuyPrice = realBuyPrice;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getProfitLoss() {
        return profitLoss;
    }

    public void setProfitLoss(String profitLoss) {
        this.profitLoss = profitLoss;
    }

    public String getProfitLossPer() {
        return profitLossPer;
    }

    public void setProfitLossPer(String profitLossPer) {
        this.profitLossPer = profitLossPer;
    }

    public String getBalanceMoney() {
        return balanceMoney;
    }

    public void setBalanceMoney(String balanceMoney) {
        this.balanceMoney = balanceMoney;
    }

    public String getMinStopLoss() {
        return minStopLoss;
    }

    public void setMinStopLoss(String minStopLoss) {
        this.minStopLoss = minStopLoss;
    }

    public String getRealTradeNum() {
        return realTradeNum;
    }

    public void setRealTradeNum(String realTradeNum) {
        this.realTradeNum = realTradeNum;
    }

    public String getMaxStopRatio() {
        return maxStopRatio;
    }

    public void setMaxStopRatio(String maxStopRatio) {
        this.maxStopRatio = maxStopRatio;
    }

    public String getIsActivity() {
        return isActivity;
    }

    public void setIsActivity(String isActivity) {
        this.isActivity = isActivity;
    }

    public String getIsCanAddDeposit() {
        return isCanAddDeposit;
    }

    public void setIsCanAddDeposit(String isCanAddDeposit) {
        this.isCanAddDeposit = isCanAddDeposit;
    }

    public String getMaxStopLossPrice() {
        return maxStopLossPrice;
    }

    public void setMaxStopLossPrice(String maxStopLossPrice) {
        this.maxStopLossPrice = maxStopLossPrice;
    }

    public String getEnDeferDesc() {
        return enDeferDesc;
    }

    public void setEnDeferDesc(String enDeferDesc) {
        this.enDeferDesc = enDeferDesc;
    }

    public String getUnDeferDesc() {
        return unDeferDesc;
    }

    public void setUnDeferDesc(String unDeferDesc) {
        this.unDeferDesc = unDeferDesc;
    }

    //    public String getIsCanDefer() {
//        return isCanDefer;
//    }
//
//    public void setIsCanDefer(String isCanDefer) {
//        this.isCanDefer = isCanDefer;
//    }
//
//    public String getCannotDeferDesc() {
//        return cannotDeferDesc;
//    }
//
//    public void setCannotDeferDesc(String cannotDeferDesc) {
//        this.cannotDeferDesc = cannotDeferDesc;
//    }
}
