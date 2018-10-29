package com.liyuu.strategy.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BillListBean implements Serializable {

    /**
     * page : 1
     * all_page : 3
     * list : [{"type":5,"money":"1000.00","c_time":"2018-08-44 11:03:44","mark":"提现"},{"type":11,"money":"100.00","c_time":"2018-08-44 11:03:44","mark":"服务费"},{"type":11,"money":"100.00","c_time":"2018-08-44 11:03:44","mark":"服务费"}]
     */

    private int page;
    @SerializedName("all_page")
    private int allPage;
    private List<ListBean> list;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
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
        /**
         * type : 5
         * money : 1000.00
         * c_time : 2018-08-44 11:03:44
         * mark : 提现
         */

        private int type;
        private String money;
        @SerializedName("c_time")
        private String cTime;
        private String mark;
        private String status;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getcTime() {
            return cTime;
        }

        public void setcTime(String cTime) {
            this.cTime = cTime;
        }

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
