package com.example.classes;

import java.util.List;

/**
 * Created by NY on 2017/3/11.
 * 我的代金券
 */

public class VoucherBean {


    private DataBean Data;
    private boolean Result;
    private String Message;

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public boolean isResult() {
        return Result;
    }

    public void setResult(boolean Result) {
        this.Result = Result;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public static class DataBean {

        private String CouponUrl;
        private List<CouponListBean> CouponList;

        public String getCouponUrl() {
            return CouponUrl;
        }

        public void setCouponUrl(String CouponUrl) {
            this.CouponUrl = CouponUrl;
        }

        public List<CouponListBean> getCouponList() {
            return CouponList;
        }

        public void setCouponList(List<CouponListBean> CouponList) {
            this.CouponList = CouponList;
        }


    }
}
