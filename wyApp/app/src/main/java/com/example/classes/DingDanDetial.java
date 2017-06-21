package com.example.classes;

import java.util.List;

public class DingDanDetial {
	private String OrderNo;
	private String PayState;
	private String UserName;
	private String ReceivePhone;
	private String AddDate;
	private String ReceiveName;
	private String Address;
	private String DeliveryType;
	private boolean IsInvoice;
	private String InvoiceCategory;
	private String InvoiceTitle;
	private String InvoiceType;
	private String Remark;
	private List<SubOrders> subOrders;
	private double TotalPrice;
	private double PrePrice;
	private int FinalPrice;
	private double Freight;
	private double CouponPrice;
	private double Free_Price;
	private String DeliveryDate;
	private String ReceiveDate;
	private String ReturnsProDate;
	private String DeliveryPhone;
	private String DeliveryName;
	
	
	
	public String getDeliveryPhone() {
		return DeliveryPhone;
	}
	public void setDeliveryPhone(String deliveryPhone) {
		DeliveryPhone = deliveryPhone;
	}
	public String getDeliveryName() {
		return DeliveryName;
	}
	public void setDeliveryName(String deliveryName) {
		DeliveryName = deliveryName;
	}
	private int Type;
	public int getType() {
		return Type;
	}
	public void setType(int type) {
		Type = type;
	}
	
	
	public String getDeliveryDate() {
		return DeliveryDate;
	}
	public void setDeliveryDate(String deliveryDate) {
		DeliveryDate = deliveryDate;
	}
	public String getReceiveDate() {
		return ReceiveDate;
	}
	public void setReceiveDate(String receiveDate) {
		ReceiveDate = receiveDate;
	}
	public String getReturnsProDate() {
		return ReturnsProDate;
	}
	public void setReturnsProDate(String returnsProDate) {
		ReturnsProDate = returnsProDate;
	}
	public String getOrderNo() {
		return OrderNo;
	}
	public void setOrderNo(String orderNo) {
		OrderNo = orderNo;
	}
	public String getPayState() {
		return PayState;
	}
	public void setPayState(String payState) {
		PayState = payState;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getReceivePhone() {
		return ReceivePhone;
	}
	public void setReceivePhone(String receivePhone) {
		ReceivePhone = receivePhone;
	}
	public String getAddDate() {
		return AddDate;
	}
	public void setAddDate(String addDate) {
		AddDate = addDate;
	}
	public String getReceiveName() {
		return ReceiveName;
	}
	public void setReceiveName(String receiveName) {
		ReceiveName = receiveName;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getDeliveryType() {
		return DeliveryType;
	}
	public void setDeliveryType(String deliveryType) {
		DeliveryType = deliveryType;
	}
	public boolean isIsInvoice() {
		return IsInvoice;
	}
	public void setIsInvoice(boolean isInvoice) {
		IsInvoice = isInvoice;
	}
	public String getInvoiceCategory() {
		return InvoiceCategory;
	}
	public void setInvoiceCategory(String invoiceCategory) {
		InvoiceCategory = invoiceCategory;
	}
	public String getInvoiceTitle() {
		return InvoiceTitle;
	}
	public void setInvoiceTitle(String invoiceTitle) {
		InvoiceTitle = invoiceTitle;
	}
	public String getInvoiceType() {
		return InvoiceType;
	}
	public void setInvoiceType(String invoiceType) {
		InvoiceType = invoiceType;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	public List<SubOrders> getSubOrders() {
		return subOrders;
	}
	public void setSubOrders(List<SubOrders> subOrders) {
		this.subOrders = subOrders;
	}
	public double getTotalPrice() {
		return TotalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		TotalPrice = totalPrice;
	}
	public double getPrePrice() {
		return PrePrice;
	}
	public void setPrePrice(Double prePrice) {
		PrePrice = prePrice;
	}
	public int getFinalPrice() {
		return FinalPrice;
	}
	public void setFinalPrice(int finalPrice) {
		FinalPrice = finalPrice;
	}
	public double getFreight() {
		return Freight;
	}
	public void setFreight(double freight) {
		Freight = freight;
	}
	public double getCouponPrice() {
		return CouponPrice;
	}
	public void setCouponPrice(Double couponPrice) {
		CouponPrice = couponPrice;
	}
	
	public double getFree_Price() {
		return Free_Price;
	}
	public void setFree_Price(double free_Price) {
		Free_Price = free_Price;
	}
	
	public double getOrderTotalPrice() {
		return OrderTotalPrice;
	}
	public void setOrderTotalPrice(double orderTotalPrice) {
		OrderTotalPrice = orderTotalPrice;
	}
	private double OrderTotalPrice;
	@Override
	public String toString() {
		return "DingDanDetial [OrderNo=" + OrderNo + ", PayState=" + PayState
				+ ", UserName=" + UserName + ", ReceivePhone=" + ReceivePhone
				+ ", AddDate=" + AddDate + ", ReceiveName=" + ReceiveName
				+ ", Address=" + Address + ", DeliveryType=" + DeliveryType
				+ ", IsInvoice=" + IsInvoice + ", InvoiceCategory="
				+ InvoiceCategory + ", InvoiceTitle=" + InvoiceTitle
				+ ", InvoiceType=" + InvoiceType + ", Remark=" + Remark
				+ ", subOrders=" + subOrders + ", TotalPrice=" + TotalPrice
				+ ", PrePrice=" + PrePrice + ", FinalPrice=" + FinalPrice
				+ ", Freight=" + Freight + ", CouponPrice=" + CouponPrice
				+ ", DeliveryDate=" + DeliveryDate + ", ReceiveDate="
				+ ReceiveDate + ", ReturnsProDate=" + ReturnsProDate
				+ ", OrderTotalPrice=" + OrderTotalPrice + "]";
	}
	
}
