package com.liyuu.strategy.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RealTradingCommissionBean implements Serializable {

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

//        "oid": 15,  //订单id
//        "sname": "中国联通",  //股票名称
//        "symbol": "000028.SZ",  //股票编码
//        "real_trade_num": 0,  //实际买入股数
//        "stock_num": 100,   //委托买入股数
//        "status": 1,  //1.等待买入  2.委托买入挂单中 3.部分买入中
//        "deposit": "1000.0000",   //委托押金
//        "loss_price": "4.0000",  //委托止损价
//        "type": "买入",   //委托类型
//        "mark": "等待买入",   //状态
//        "opt_time": "2018-08-10 09:38"  //委托时间

        private int oid;
        @SerializedName("stock_buy_price")
        private String stockBuyPrice;
        private String sname;
        private String symbol;
        @SerializedName("real_trade_num")
        private String realTradeNum;
        @SerializedName("stock_num")
        private String stockNum;
        private String status;
        private String deposit;
        @SerializedName("loss_price")
        private String lossPrice;
        private String type;
        @SerializedName("opt_time")
        private String optTime;
        private String mark;
        @SerializedName("real_buy_price")
        private String realBuyPrice;

        public int getOid() {
            return oid;
        }

        public void setOid(int oid) {
            this.oid = oid;
        }

        public String getStockBuyPrice() {
            return stockBuyPrice;
        }

        public void setStockBuyPrice(String stockBuyPrice) {
            this.stockBuyPrice = stockBuyPrice;
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

        public String getRealTradeNum() {
            return realTradeNum;
        }

        public void setRealTradeNum(String realTradeNum) {
            this.realTradeNum = realTradeNum;
        }

        public String getStockNum() {
            return stockNum;
        }

        public void setStockNum(String stockNum) {
            this.stockNum = stockNum;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getOptTime() {
            return optTime;
        }

        public void setOptTime(String optTime) {
            this.optTime = optTime;
        }

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        public String getRealBuyPrice() {
            return realBuyPrice;
        }

        public void setRealBuyPrice(String realBuyPrice) {
            this.realBuyPrice = realBuyPrice;
        }
    }
}
