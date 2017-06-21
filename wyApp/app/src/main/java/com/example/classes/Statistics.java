package com.example.classes;

public class Statistics {
	private double allMoney;//总代购金额 
	private double allRevenue;//总代购收益
	private double canCashout;//未提现
	private double cashedOut;//已提现
	private double cashingOut;//提现中
	private double freeze;//冻结
	private double todayRevenue;//今日新增代购收益
	private double cashedEnter;//转入余额

	public double getCashedEnter() {
		return cashedEnter;
	}

	public void setCashedEnter(double cashedEnter) {
		this.cashedEnter = cashedEnter;
	}

	public double getAllMoney() {
		return allMoney;
	}
	public void setAllMoney(double allMoney) {
		this.allMoney = allMoney;
	}
	public double getAllRevenue() {
		return allRevenue;
	}
	public void setAllRevenue(double allRevenue) {
		this.allRevenue = allRevenue;
	}
	public double getCanCashout() {
		return canCashout;
	}
	public void setCanCashout(double canCashout) {
		this.canCashout = canCashout;
	}
	public double getCashedOut() {
		return cashedOut;
	}
	public void setCashedOut(double cashedOut) {
		this.cashedOut = cashedOut;
	}
	public double getCashingOut() {
		return cashingOut;
	}
	public void setCashingOut(double cashingOut) {
		this.cashingOut = cashingOut;
	}
	public double getFreeze() {
		return freeze;
	}
	public void setFreeze(double freeze) {
		this.freeze = freeze;
	}
	public double getTodayRevenue() {
		return todayRevenue;
	}
	public void setTodayRevenue(double todayRevenue) {
		this.todayRevenue = todayRevenue;
	}

}
