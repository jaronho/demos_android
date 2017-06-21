package com.example.classes;

/**
 * Created by NY on 2017/3/13.
 * 代金券列表
 */

public class CouponListBean {

    private int Id;
    private String Code;
    private String Begin_Date;
    private String End_Date;
    private String Remark;
    private String Order_No;

    private double Need_Money;//我的代金券
    private double Need_CouponMoney;//我的代金券
    private double Money;//我的代金券

    private double Coupon_Money;//支付可用代金券
    private double Low_Money;//支付可用代金券

    private boolean isShowRemark;

    public boolean isShowRemark() {
        return isShowRemark;
    }

    public void setShowRemark(boolean showRemark) {
        isShowRemark = showRemark;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public String getBegin_Date() {
        return Begin_Date;
    }

    public void setBegin_Date(String Begin_Date) {
        this.Begin_Date = Begin_Date;
    }

    public String getEnd_Date() {
        return End_Date;
    }

    public void setEnd_Date(String End_Date) {
        this.End_Date = End_Date;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public String getOrder_No() {
        return Order_No;
    }

    public void setOrder_No(String order_No) {
        Order_No = order_No;
    }

    public double getNeed_Money() {
        return Need_Money;
    }

    public void setNeed_Money(double Need_Money) {
        this.Need_Money = Need_Money;
    }

    public double getNeed_CouponMoney() {
        return Need_CouponMoney;
    }

    public void setNeed_CouponMoney(double Need_CouponMoney) {
        this.Need_CouponMoney = Need_CouponMoney;
    }

    public double getMoney() {
        return Money;
    }

    public void setMoney(double Money) {
        this.Money = Money;
    }

    public double getCoupon_Money() {
        return Coupon_Money;
    }

    public void setCoupon_Money(double coupon_Money) {
        Coupon_Money = coupon_Money;
    }

    public double getLow_Money() {
        return Low_Money;
    }

    public void setLow_Money(double low_Money) {
        Low_Money = low_Money;
    }
}
