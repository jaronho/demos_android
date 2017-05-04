package com.example.classes;

import java.text.DecimalFormat;
import java.util.List;


public class Orders {
	private int Id;
	private String Order_No;
	private double Price;
	private double Free_Price;
	private String State;
	private String Service_Phone;
	private List<Integer> buttons;
	private List<SubOrders> subOrders;
	private String Send_Name;
	
	
	public String getSend_Name() {
		return Send_Name;
	}
	public void setSend_Name(String send_Name) {
		Send_Name = send_Name;
	}
	DecimalFormat ddf1 = new DecimalFormat("#.00");
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getOrder_No() {
		return Order_No;
	}
	public void setOrder_No(String order_No) {
		Order_No = order_No;
	}
	public double getPrice() {
		return Price;
	}
	public void setPrice(double price) {
		Price = price;
	}
	
	
	
	public double getFree_Price() {
		return Free_Price;
	}
	public void setFree_Price(Double free_Price) {
		Free_Price = free_Price;
	}
	public String getService_Phone() {
		return Service_Phone;
	}
	public void setService_Phone(String service_Phone) {
		Service_Phone = service_Phone;
	}
	public List<Integer> getIntegers() {
		return buttons;
	}
	public void setIntegers(List<Integer> integers) {
		this.buttons = integers;
	}
	public List<SubOrders> getSubOrders() {
		return subOrders;
	}
	public void setSubOrders(List<SubOrders> subOrders) {
		this.subOrders = subOrders;
	}
	
}
