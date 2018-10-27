package com.liyuu.strategy.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UserOrderListBean implements Serializable {

    /**
     * page : 1
     * all_page : 1
     * list : [{"symbol":"000028.SZ","sname":"中国联通","period":1,"loss_price":"4.00","stock_buy_price":"54.31","stock_sale_price":"54.31","stock_num":100,"deposit":"1000.00","profit_loss":"0.00","status":1,"c_time":1533636264,"settle_time":"16小时前","hold_day":2}]
     */

    private String page;
    @SerializedName("all_page")
    private int allPage;
    private List<ListBean> list;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public int getAllPage() {
        return allPage;
    }

    public void setAllPage(int allPage) {
        this.allPage = allPage;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable {
        private String symbol;
        private String sname;
        private String period;//持仓类型T+1
        @SerializedName("loss_price")
        private String lossPrice;//止损价
        @SerializedName("real_buy_price")
        private String stockBuyPrice;//买入价
        @SerializedName("real_sale_price")
        private String stockSalePrice;//卖出价
        @SerializedName("stock_num")
        private int stockNum;//股数
        private String deposit;//押金
        @SerializedName("total_deposit")
        private String totalDeposit;
        @SerializedName("profit_loss")
        private String profitLoss;//盈亏金额
        private String status;//TradeType.Commission(1.等待买入  2.委托买入挂单中 3部分买入中)   1为盈  2为亏
        @SerializedName("c_time")
        private String cTime;
        @SerializedName("settle_time")
        private String settleTime;//结算时间
        @SerializedName("hold_day")
        private String holdDay;//持股天数
        @SerializedName("sale_status")
        private String saleStatus;//1卖出2新购
        @SerializedName("buy_time")
        private String buyTime;
        @SerializedName("enablesell_time")
        private String enablesellTime;//可卖出时间
        @SerializedName("market_price")
        private String marketPrice;//市值
        @SerializedName("profit_loss_per")
        private String profitLossPer;//涨跌幅
        private String oid;//订单编号
        @SerializedName("real_trade_num")
        private String realTradenum;//实际买入股数
        private String type;//委托类型
        private String mark;//状态
        @SerializedName("opt_time")
        private String optTime;
        @SerializedName("current_price")
        private String currentPrice;//当前价
        @SerializedName("loss_money")
        private String lossMoney;//亏损抵扣

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

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public String getLossPrice() {
            return lossPrice;
        }

        public void setLossPrice(String lossPrice) {
            this.lossPrice = lossPrice;
        }

        public String getStockBuyPrice() {
            return stockBuyPrice;
        }

        public void setStockBuyPrice(String stockBuyPrice) {
            this.stockBuyPrice = stockBuyPrice;
        }

        public String getStockSalePrice() {
            return stockSalePrice;
        }

        public void setStockSalePrice(String stockSalePrice) {
            this.stockSalePrice = stockSalePrice;
        }

        public int getStockNum() {
            return stockNum;
        }

        public void setStockNum(int stockNum) {
            this.stockNum = stockNum;
        }

        public String getDeposit() {
            return deposit;
        }

        public void setDeposit(String deposit) {
            this.deposit = deposit;
        }

        public String getTotalDeposit() {
            return totalDeposit;
        }

        public void setTotalDeposit(String totalDeposit) {
            this.totalDeposit = totalDeposit;
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

        public String getcTime() {
            return cTime;
        }

        public void setcTime(String cTime) {
            this.cTime = cTime;
        }

        public String getSettleTime() {
            return settleTime;
        }

        public void setSettleTime(String settleTime) {
            this.settleTime = settleTime;
        }

        public String getHoldDay() {
            return holdDay;
        }

        public void setHoldDay(String holdDay) {
            this.holdDay = holdDay;
        }

        public String getSaleStatus() {
            return saleStatus;
        }

        public void setSaleStatus(String saleStatus) {
            this.saleStatus = saleStatus;
        }

        public String getBuyTime() {
            return buyTime;
        }

        public void setBuyTime(String buyTime) {
            this.buyTime = buyTime;
        }

        public String getEnablesellTime() {
            return enablesellTime;
        }

        public void setEnablesellTime(String enablesellTime) {
            this.enablesellTime = enablesellTime;
        }

        public String getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(String marketPrice) {
            this.marketPrice = marketPrice;
        }

        public String getProfitLossPer() {
            return profitLossPer;
        }

        public void setProfitLossPer(String profitLossPer) {
            this.profitLossPer = profitLossPer;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getRealTradenum() {
            return realTradenum;
        }

        public void setRealTradenum(String realTradenum) {
            this.realTradenum = realTradenum;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        public String getOptTime() {
            return optTime;
        }

        public void setOptTime(String optTime) {
            this.optTime = optTime;
        }

        public String getCurrentPrice() {
            return currentPrice;
        }

        public void setCurrentPrice(String currentPrice) {
            this.currentPrice = currentPrice;
        }

        public String getLossMoney() {
            return lossMoney;
        }

        public void setLossMoney(String lossMoney) {
            this.lossMoney = lossMoney;
        }
    }
}
