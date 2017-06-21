package com.example.classes;

public class Ordercals {
		private int Order_Id;
		private double Freight;
		private String WorkStation_Freight;
		private String Reminder;
		private String Order_No;
		private String Manuf_Name;
		private double Total_price;
		private double Coupon_Price;
		private double ToPay_Price;
		private String PayInfo;
		private String Remark;
		private boolean RefreButton;
		public boolean isNextButton() {
			return NextButton;
		}
		public void setNextButton(boolean nextButton) {
			NextButton = nextButton;
		}
		private boolean NextButton;
		public String getReminder() {
			return Reminder;
		}
		public void setReminder(String reminder) {
			Reminder = reminder;
		}
		public String getWorkStation_Freight() {
			return WorkStation_Freight;
		}
		public void setWorkStation_Freight(String workStation_Freight) {
			WorkStation_Freight = workStation_Freight;
		}
		
		
		public boolean isRefreButton() {
			return RefreButton;
		}
		public void setRefreButton(boolean refreButton) {
			RefreButton = refreButton;
		}
		public String getRemark() {
			return Remark;
		}
		public void setRemark(String remark) {
			Remark = remark;
		}
		public int getOrder_Id() {
			return Order_Id;
		}
		public void setOrder_Id(int order_Id) {
			Order_Id = order_Id;
		}
		public double getFreight() {
			return Freight;
		}
		public void setFreight(double freight) {
			Freight = freight;
		}
		public String getOrder_No() {
			return Order_No;
		}
		public void setOrder_No(String order_No) {
			Order_No = order_No;
		}
		public String getManuf_Name() {
			return Manuf_Name;
		}
		public void setManuf_Name(String manuf_Name) {
			Manuf_Name = manuf_Name;
		}
		public double getTotal_price() {
			return Total_price;
		}
		public void setTotal_price(double total_price) {
			Total_price = total_price;
		}
		public double getCoupon_Price() {
			return Coupon_Price;
		}
		
		private int Type;
		public int getType() {
			return Type;
		}
		public void setType(int type) {
			Type = type;
		}
		private String Free_Price;
		public String getFree_Price() {
			return Free_Price;
		}
		public void setFree_Price(String free_Price) {
			Free_Price = free_Price;
		}
		public void setCoupon_Price(Double coupon_Price) {
			Coupon_Price = coupon_Price;
		}
		public double getToPay_Price() {
			return ToPay_Price;
		}
		public void setToPay_Price(Double toPay_Price) {
			ToPay_Price = toPay_Price;
		}
		public String getPayInfo() {
			return PayInfo;
		}
		public void setPayInfo(String payInfo) {
			PayInfo = payInfo;
		}
		
}
