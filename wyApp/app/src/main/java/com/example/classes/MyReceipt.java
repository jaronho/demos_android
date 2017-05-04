package com.example.classes;

public class MyReceipt {
	private String orderId;//订单id
	private String Order_No;//订单号
	private String OrderState;//订单状态
	private String TakeTime;//确认收货时间
	private String Price;//实际付款
	private String ReceiptState;//发票状态
	private String HandleTime;//处理时间
	private String TicketNumber;//运单号
	public boolean checked=false;//未被选中状态
	private String Color;//状态颜色
	private Boolean ClickButton;
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getColor() {
		return Color;
	}
	public void setColor(String color) {
		Color = color;
	}
	public Boolean getClickButton() {
		return ClickButton;
	}
	public void setClickButton(Boolean clickButton) {
		ClickButton = clickButton;
	}
	
	public String getOrder_No() {
		return Order_No;
	}
	public void setOrder_No(String order_No) {
		Order_No = order_No;
	}
	public String getOrderState() {
		return OrderState;
	}
	public void setOrderState(String orderState) {
		OrderState = orderState;
	}
	public String getTakeTime() {
		return TakeTime;
	}
	public void setTakeTime(String takeTime) {
		TakeTime = takeTime;
	}
	public String getPrice() {
		return Price;
	}
	public void setPrice(String price) {
		Price = price;
	}
	public String getReceiptState() {
		return ReceiptState;
	}
	public void setReceiptState(String receiptState) {
		ReceiptState = receiptState;
	}
	public String getHandleTime() {
		return HandleTime;
	}
	public void setHandleTime(String handleTime) {
		HandleTime = handleTime;
	}
	public String getTicketNumber() {
		return TicketNumber;
	}
	public void setTicketNumber(String ticketNumber) {
		TicketNumber = ticketNumber;
	}
	
	
}
