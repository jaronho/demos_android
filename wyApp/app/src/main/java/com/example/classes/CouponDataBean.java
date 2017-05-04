package com.example.classes;

/**
 * Created by NY on 2016/12/23.
 * 点击抢优惠券图片返回数据
 */

public class CouponDataBean {
    /**
     * IsLogin : false
     * Data : null
     * Result : false
     * Message : 您未登陆，不能进行此操作
     */
    /**
     * Data : null
     * Result : false
     * Message : 您已领取该优惠券,不能重复领取
     */

    private boolean IsLogin;
    private Object Data;
    private boolean Result;
    private String Message;

    public boolean isIsLogin() {
        return IsLogin;
    }

    public void setIsLogin(boolean IsLogin) {
        this.IsLogin = IsLogin;
    }

    public Object getData() {
        return Data;
    }

    public void setData(Object Data) {
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

    /**
     * Data : null
     * Result : false
     * Message : 您已领取该优惠券,不能重复领取
     */


}
