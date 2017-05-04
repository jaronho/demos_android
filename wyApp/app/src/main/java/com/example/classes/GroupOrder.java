package com.example.classes;

import java.util.List;

public class GroupOrder {
	private int Id;
    private String Group_No;
    private String Total_Money;
    private String Pay_Money;
    private String ToPay_Money;
    private String Total_Freight;
    private String Coupon_Money;
    private int Type;
    private int User_Id;
    private String Add_Date;
    private String Remark;
    private int iCount;
    private List<Orders> orders;
    private String User_Name;
    
	
	
	public String getUser_Name() {
		return User_Name;
	}
	public void setUser_Name(String user_Name) {
		User_Name = user_Name;
	}
	public List<Orders> getOrders() {
		return orders;
	}
	public void setOrders(List<Orders> orders) {
		this.orders = orders;
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getGroup_No() {
		return Group_No;
	}
	public void setGroup_No(String group_No) {
		Group_No = group_No;
	}
	public String getTotal_Money() {
		return Total_Money;
	}
	public void setTotal_Money(String total_Money) {
		Total_Money = total_Money;
	}
	public String getPay_Money() {
		return Pay_Money;
	}
	public void setPay_Money(String pay_Money) {
		Pay_Money = pay_Money;
	}
	public String getToPay_Money() {
		return ToPay_Money;
	}
	public void setToPay_Money(String toPay_Money) {
		ToPay_Money = toPay_Money;
	}
	public String getTotal_Freight() {
		return Total_Freight;
	}
	public void setTotal_Freight(String total_Freight) {
		Total_Freight = total_Freight;
	}
	public String getCoupon_Money() {
		return Coupon_Money;
	}
	public void setCoupon_Money(String coupon_Money) {
		Coupon_Money = coupon_Money;
	}
	public int getType() {
		return Type;
	}
	public void setType(int type) {
		Type = type;
	}
	public int getUser_Id() {
		return User_Id;
	}
	public void setUser_Id(int user_Id) {
		User_Id = user_Id;
	}
	public String getAdd_Date() {
		return Add_Date;
	}
	public void setAdd_Date(String add_Date) {
		Add_Date = add_Date;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	public int getiCount() {
		return iCount;
	}
	public void setiCount(int iCount) {
		this.iCount = iCount;
	}
    
    
}
