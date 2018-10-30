package com.gsclub.strategy.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class IncomeListBean implements Serializable {

    /**
     * list : [{"id":16,"rate":"68.20","profit":"4800.00","nickname":"","headimg":""},{"id":18,"rate":"5.00","profit":"100.00","nickname":"订单","headimg":""},{"id":17,"rate":"18.80","profit":"80.00","nickname":"信息","headimg":""}]
     * mydata : {"profit":0,"rank":0,"rate":0,"s_day_time":"2018-07-23","e_day_time":"2018-08-02"}
     */

    private MydataBean mydata;
    private List<ListBean> list;
    private String tip;

    public MydataBean getMydata() {
        return mydata;
    }

    public void setMydata(MydataBean mydata) {
        this.mydata = mydata;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public static class MydataBean implements Serializable {
        /**
         * profit : 0
         * rank : 0
         * rate : 0
         * s_day_time : 2018-07-23
         * e_day_time : 2018-08-02
         */

        private String profit;//我的盈利
        private String rank;  //我的排名
        private String rate;  //我的收益率
        @SerializedName("s_day_time")
        private String startDayTime;
        @SerializedName("e_day_time")
        private String endDayTime;

        public String getProfit() {
            return profit;
        }

        public void setProfit(String profit) {
            this.profit = profit;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getStartDayTime() {
            return startDayTime;
        }

        public void setStartDayTime(String startDayTime) {
            this.startDayTime = startDayTime;
        }

        public String getEndDayTime() {
            return endDayTime;
        }

        public void setEndDayTime(String endDayTime) {
            this.endDayTime = endDayTime;
        }
    }

    public static class ListBean implements Serializable {
        /**
         * id : 16
         * rate : 68.20
         * profit : 4800.00
         * nickname :
         * headimg :
         */

        private int id;
        private String rate;//收益率
        private String profit;//收益金额
        private String nickname;//用户姓名
        private String headimg;//用户姓名

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getProfit() {
            return profit;
        }

        public void setProfit(String profit) {
            this.profit = profit;
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
    }
}
