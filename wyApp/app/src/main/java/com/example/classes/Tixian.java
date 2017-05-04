package com.example.classes;

import java.util.List;

public class Tixian {
	private  boolean IsFillAccount;
	private  boolean IsBindMobile;
	private List<TixianItem> tixianItems;
	private String BindAccountMessage;
	
	
	public String getBindAccountMessage() {
		return BindAccountMessage;
	}
	public void setBindAccountMessage(String bindAccountMessage) {
		BindAccountMessage = bindAccountMessage;
	}
	public List<TixianItem> getTixianItems() {
		return tixianItems;
	}
	public void setTixianItems(List<TixianItem> tixianItems) {
		this.tixianItems = tixianItems;
	}
	public boolean isIsFillAccount() {
		return IsFillAccount;
	}
	public void setIsFillAccount(boolean isFillAccount) {
		IsFillAccount = isFillAccount;
	}
	public boolean isIsBindMobile() {
		return IsBindMobile;
	}
	public void setIsBindMobile(boolean isBindMobile) {
		IsBindMobile = isBindMobile;
	}
	
}
