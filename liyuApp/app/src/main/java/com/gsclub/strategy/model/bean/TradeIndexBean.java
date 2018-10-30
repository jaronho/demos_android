package com.gsclub.strategy.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TradeIndexBean implements Serializable {

    /**
     * balance_money : 0.00
     * deposit_list : [{"name":"1千元","value":1000},{"name":"2千元","value":2000},{"name":"5千元","value":5000},{"name":"1万元","value":10000},{"name":"2万元","value":20000},{"name":"5万元","value":50000}]
     * level_type : A类股票
     * gearing : 10.00
     * max_stop_line : 6.80
     * coop_val : null
     * partner_uid : 9
     * desc : <div style=\"size: 14px;color: #999999\">• 操盘资金由第三方跟投人提供<br/>• 技术服务费按成交金额的 0.5%收取<br/>• 点击买入即表示同意<a style=\"color: #ff8200\" href=\"http://www.baidu.com\">《沪、深A股交易合作协议》</a></div>
     */
    @SerializedName("balance_money")
    private String balanceMoney;//可用余额
    @SerializedName("level_type")
    private String levelType; //风控股票类型
    private String gearing;//杠杆
    @SerializedName("max_stop_ratio")
    private String maxStopLine;//止损线，返回的为百分比数，需收到除以100
    @SerializedName("coop_val")
    private String coopVal;//合作费率
    @SerializedName("partner_uid")
    private String partnerUid;//搭档uid
    private List<RemarkBean> remark;//
    @SerializedName("deposit_list")
    private List<DepositListBean> depositList;//押金列表

    public String getBalanceMoney() {
        return balanceMoney;
    }

    public void setBalanceMoney(String balanceMoney) {
        this.balanceMoney = balanceMoney;
    }

    public String getLevelType() {
        return levelType;
    }

    public void setLevelType(String levelType) {
        this.levelType = levelType;
    }

    public String getGearing() {
        return gearing;
    }

    public void setGearing(String gearing) {
        this.gearing = gearing;
    }

    public String getMaxStopLine() {
        return maxStopLine;
    }

    public void setMaxStopLine(String maxStopLine) {
        this.maxStopLine = maxStopLine;
    }

    public String getCoopVal() {
        return coopVal;
    }

    public void setCoopVal(String coopVal) {
        this.coopVal = coopVal;
    }

    public String getPartnerUid() {
        return partnerUid;
    }

    public void setPartnerUid(String partnerUid) {
        this.partnerUid = partnerUid;
    }

    public List<DepositListBean> getDepositList() {
        return depositList;
    }

    public void setDepositList(List<DepositListBean> depositList) {
        this.depositList = depositList;
    }

    public List<RemarkBean> getRemark() {
        return remark;
    }

    public void setRemark(List<RemarkBean> remark) {
        this.remark = remark;
    }

    public static class DepositListBean implements Serializable {
        /**
         * name : 1千元
         * value : 1000
         */

        private String name;
        private int value;

        private boolean isSelect = false;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }
    }

    public static class RemarkBean implements Serializable {
        /**
         * desc : • 点击买入即表示同意《沪、深A股交易合作协议》
         * text : 《沪、深A股交易合作协议》
         * url : www.baidu.com
         */

        private String desc;
        private String text;
        private String url;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
