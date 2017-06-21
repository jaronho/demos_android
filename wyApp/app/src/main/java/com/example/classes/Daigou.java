package com.example.classes;

import java.util.List;

public class Daigou {
	private String orderNo;
	private String State;
	private double Money;
	private double ProMoney;
	private List<String> urList;
	private int type;
//	private Products products;
	private List<Products1> products1;
	private String InvaitedUser;//下级代购员用户名
	
	
	
	
	
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public List<Products1> getProducts1() {
		return products1;
	}
	public void setProducts1(List<Products1> products1) {
		this.products1 = products1;
	}
//	public Products getProducts() {
//		return products;
//	}
//	public void setProducts(Products products) {
//		this.products = products;
//	}
	public String getInvaitedUser() {
		return InvaitedUser;
	}
	public void setInvaitedUser(String invaitedUser) {
		InvaitedUser = invaitedUser;
	}
//	public Products getProducts() {
//		return products;
//	}
//	public void setProducts(Products products) {
//		this.products = products;
//	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public double getMoney() {
		return Money;
	}
	public void setMoney(double money) {
		Money = money;
	}
	public double getProMoney() {
		return ProMoney;
	}
	public void setProMoney(double proMoney) {
		ProMoney = proMoney;
	}
	public List<String> getUrList() {
		return urList;
	}
	public void setUrList(List<String> urList) {
		this.urList = urList;
	}
	
}
