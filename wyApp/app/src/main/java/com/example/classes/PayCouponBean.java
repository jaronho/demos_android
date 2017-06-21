package com.example.classes;

import java.util.List;

/**
 * Created by NY on 2017/3/13.
 * 支付可用代金券
 */

public class PayCouponBean {

    private boolean Result;
    private String Message;
    private List<CouponListBean> Data;

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

    public List<CouponListBean> getData() {
        return Data;
    }

    public void setData(List<CouponListBean> Data) {
        this.Data = Data;
    }
}
