package com.example.classes;

import org.json.JSONException;
import org.json.JSONObject;

public class ProStockDetailJsonUtil {
	 
	public static ProStockDetail getProStock(String string){
		ProStockDetail proStock = new ProStockDetail();
		if (proStock.equals("null") || proStock == null) {
			return proStock;
		}
		
		try {
			JSONObject jsonObject1 = new JSONObject(string);
			JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Data"));
			
			proStock.setStock(jsonObject2.getBoolean("IsStock"));
			proStock.setProId(jsonObject2.getInt("ProId"));
			proStock.setRemark(jsonObject2.getString("Remark"));
			proStock.setStockName(jsonObject2.getString("StockName"));
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return proStock;
		
	}

}
