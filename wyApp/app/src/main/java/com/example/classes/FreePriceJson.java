package com.example.classes;

import java.io.Serializable;

public class FreePriceJson implements Serializable{
	private int count;
	private double discountRange;
	private int proId;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public double getDiscountRange() {
		return discountRange;
	}
	public void setDiscountRange(double discountRange) {
		this.discountRange = discountRange;
	}
	public int getProId() {
		return proId;
	}
	public void setProId(int proId) {
		this.proId = proId;
	}
	

}
