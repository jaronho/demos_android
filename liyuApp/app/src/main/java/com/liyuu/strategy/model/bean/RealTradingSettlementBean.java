package com.liyuu.strategy.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RealTradingSettlementBean implements Serializable {

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


        //"oid": 8,
        //"symbol": "000028.SZ",
        //"sname": "中国联通",
        //"total_period": 1,
        //"loss_price": "50.00",  //止损价
        //"real_buy_price": "54.3100",  //买入价
        //"real_sale_price": "55.0000",  //卖出价
        //"real_trade_num": 900,  //股数
        //"total_deposit": "5000.0000",  //总押金
        //"profit_loss": "621.0000",  //盈亏
        //"status": 1,  //  //1盈利 2亏损
        //"c_time": 1534319830,
        //"settle_time": "2018-08-15 15:57",  //结算时间
        //"hold_day": 2,  //持股天数
        //"loss_money": 0  //亏损抵扣

        private String oid;
        private String symbol;
        private String sname;
        @SerializedName("total_period")
        private String totalPeriod;
        @SerializedName("loss_price")
        private String lossPrice;
        @SerializedName("real_buy_price")
        private String realBuyPrice;
        @SerializedName("real_sale_price")
        private String realSalePrice;
        @SerializedName("real_trade_num")
        private String realTradeNum;
        @SerializedName("total_deposit")
        private String totalDeposit;
        @SerializedName("profit_loss")
        private String profitLoss;
        private String status;
        @SerializedName("opt_time")
        private String optTime;
        @SerializedName("hold_day")
        private String holdDay;
        @SerializedName("loss_money")
        private String lossMoney;
        @SerializedName("text_color")
        private String textColor;

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
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

        public String getTotalPeriod() {
            return totalPeriod;
        }

        public void setTotalPeriod(String totalPeriod) {
            this.totalPeriod = totalPeriod;
        }

        public String getLossPrice() {
            return lossPrice;
        }

        public void setLossPrice(String lossPrice) {
            this.lossPrice = lossPrice;
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

        public String getRealTradeNum() {
            return realTradeNum;
        }

        public void setRealTradeNum(String realTradeNum) {
            this.realTradeNum = realTradeNum;
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

        public String getOptTime() {
            return optTime;
        }

        public void setOptTime(String optTime) {
            this.optTime = optTime;
        }

        public String getHoldDay() {
            return holdDay;
        }

        public void setHoldDay(String holdDay) {
            this.holdDay = holdDay;
        }

        public String getLossMoney() {
            return lossMoney;
        }

        public void setLossMoney(String lossMoney) {
            this.lossMoney = lossMoney;
        }

        public String getTextColor() {
            return textColor;
        }

        public void setTextColor(String textColor) {
            this.textColor = textColor;
        }
    }
}
