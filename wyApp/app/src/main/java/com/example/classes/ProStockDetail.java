package com.example.classes;

public class ProStockDetail {
	
	private int proId;//商品id
	private boolean isStock;
	private String remark;
	private String stockName;
	public int getProId() {
		return proId;
	}
	public void setProId(int proId) {
		this.proId = proId;
	}
	
	public boolean isStock() {
		return isStock;
	}
	public void setStock(boolean isStock) {
		this.isStock = isStock;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	
	
}
