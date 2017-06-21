package com.example.classes;

import java.util.List;

public class DaiGouMingXi {
	private boolean IsBindMobile;
	private boolean IsFillAccount;
	private boolean CanIntoMoney;
	private String PurchasingState;
	private List<Daigou> items;
	private List<Statistics> Statistics ;
	private String title;//申请代购人页显示的内容
	private int btnCount;//显示按钮个个数
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getBtnCount() {
		return btnCount;
	}
	public void setBtnCount(int btnCount) {
		this.btnCount = btnCount;
	}
	public Boolean getCanIntoMoney() {
		return CanIntoMoney;
	}
	public void setCanIntoMoney(Boolean canIntoMoney) {
		CanIntoMoney = canIntoMoney;
	}
	public List<Statistics> getStatistics() {
		return Statistics;
	}
	public void setStatistics(List<Statistics> statistics) {
		Statistics = statistics;
	}
	public List<Daigou> getItems() {
		return items;
	}
	public void setItems(List<Daigou> items) {
		this.items = items;
	}
	public boolean isIsBindMobile() {
		return IsBindMobile;
	}
	public void setIsBindMobile(boolean isBindMobile) {
		IsBindMobile = isBindMobile;
	}
	public boolean isIsFillAccount() {
		return IsFillAccount;
	}
	public void setIsFillAccount(boolean isFillAccount) {
		IsFillAccount = isFillAccount;
	}
	public String getPurchasingState() {
		return PurchasingState;
	}
	public void setPurchasingState(String purchasingState) {
		PurchasingState = purchasingState;
	}
	

}
