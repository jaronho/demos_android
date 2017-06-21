package com.example.classes;

public class Products {
	private  int id;
	private String Name;//产品
	private String Spec;//规格
	private double price;//单价
//	private Object count;//数量
	private int count;
	private String scale;//收益比例
	private double money;//代购金额
	private double lucreMoney;//代购收益
	
	
	
public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	//	public Object getCount() {
//		return count;
//	}
//	public void setCount(Object count) {
//		this.count = count;
//	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getSpec() {
		return Spec;
	}
	public void setSpec(String spec) {
		Spec = spec;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public double getLucreMoney() {
		return lucreMoney;
	}
	public void setLucreMoney(double lucreMoney) {
		this.lucreMoney = lucreMoney;
	}
	
	
	

}
