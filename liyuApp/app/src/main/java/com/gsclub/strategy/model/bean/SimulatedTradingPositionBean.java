package com.gsclub.strategy.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SimulatedTradingPositionBean implements Serializable {

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

//        "oid": 14,  //订单号
//        "real_buy_price": "54.3100",  //真实买入价
//        "real_trade_num": 100,  //买入股数
//        "symbol": "000028.SZ",  //股票编码
//        "sname": "中国联通",  //股票名称
//        "buy_time": "2018-08-10 09:38",  //买入时间
//        "enablesell_time": 1533776400,
//        "market_price": "5431.00",  //市值
//        "current_price": 54.31,  //现价
//        "profit_loss": "0.00",  //浮动盈亏
//        "profit_loss_per": "0", //涨跌幅
//        "deposit": "1000.0000",  //押金
//        "loss_price": "4.0000",  //止损价
//        "last_loss_price": "4.0000",//最新止损价
//        "sale_status": 2   //2可卖出 1新购

        @SerializedName("buy_time")
        private String optTime;
        private String oid;
        @SerializedName("real_buy_price")
        private String realBuyPrice;
        @SerializedName("real_trade_num")
        private String realTradeNum;
        private String symbol;
        private String sname;
        @SerializedName("market_price")
        private String marketPrice;
        @SerializedName("current_price")
        private String currentPrice;
        @SerializedName("profit_loss")
        private String profitLoss;
        @SerializedName("text_color")
        private String textColor;
        @SerializedName("profit_loss_per")
        private String profitLossPer;
        private String deposit;
        @SerializedName("loss_price")
        private String lossPrice;
        @SerializedName("sale_status")
        private String saleStatus;

        public String getOptTime() {
            return optTime;
        }

        public void setOptTime(String optTime) {
            this.optTime = optTime;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getRealBuyPrice() {
            return realBuyPrice;
        }

        public void setRealBuyPrice(String realBuyPrice) {
            this.realBuyPrice = realBuyPrice;
        }

        public String getRealTradeNum() {
            return realTradeNum;
        }

        public void setRealTradeNum(String realTradeNum) {
            this.realTradeNum = realTradeNum;
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

        public String getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(String marketPrice) {
            this.marketPrice = marketPrice;
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

        public String getTextColor() {
            return textColor;
        }

        public void setTextColor(String textColor) {
            this.textColor = textColor;
        }

        public String getProfitLossPer() {
            return profitLossPer;
        }

        public void setProfitLossPer(String profitLossPer) {
            this.profitLossPer = profitLossPer;
        }

        public String getDeposit() {
            return deposit;
        }

        public void setDeposit(String deposit) {
            this.deposit = deposit;
        }

        public String getLossPrice() {
            return lossPrice;
        }

        public void setLossPrice(String lossPrice) {
            this.lossPrice = lossPrice;
        }

        public String getSaleStatus() {
            return saleStatus;
        }

        public void setSaleStatus(String saleStatus) {
            this.saleStatus = saleStatus;
        }
    }
}
