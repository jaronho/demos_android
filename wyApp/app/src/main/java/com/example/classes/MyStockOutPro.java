package com.example.classes;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MyStockOutPro implements Serializable{
	
	private boolean  HasStockOut;//是否库存不足
	private List<String> StockOutPro;//缺货商品id
//	private Map<String, String> listStock;
//	private List<ProdctStock> listStock;
	private Map<String, String> listStock;//选择商品id对应的数量
	private List<LogisticsDetial> logistics;//物流信息
	private Boolean AlertInviteTip;//是否提示无代购收益,为true时提示

	public Boolean getAlertInviteTip() {
		return AlertInviteTip;
	}

	public void setAlertInviteTip(Boolean alertInviteTip) {
		AlertInviteTip = alertInviteTip;
	}

	public List<LogisticsDetial> getLogistics() {
		return logistics;
	}

	public void setLogistics(List<LogisticsDetial> logistics) {
		this.logistics = logistics;
	}

	public Map<String, String> getListStock() {
		return listStock;
	}

	public void setListStock(Map<String, String> listStock) {
		this.listStock = listStock;
	}

	public boolean isHasStockOut() {
		return HasStockOut;
	}
	
	public void setHasStockOut(boolean hasStockOut) {
		HasStockOut = hasStockOut;
	}
	public List<String> getStockOutPro() {
		return StockOutPro;
	}
	public void setStockOutPro(List<String> stockOutPro) {
		StockOutPro = stockOutPro;
	}
	
}
