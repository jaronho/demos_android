package com.example.classes;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrdercalsJsonUtil {
	public static ArrayList<Ordercals> getOrdercalss(String string){
		ArrayList<Ordercals> ordercals = null;
		JSONArray jsonArray;
		try {
			ordercals=new ArrayList<Ordercals>();
			if (string==null||string.equals("null")) {
				return ordercals;
			}
			
			JSONObject jSONObject1=new JSONObject(string);
			jsonArray=new JSONArray(jSONObject1.getString("Data"));
			for (int i = 0; i < jsonArray.length(); i++) {
				Ordercals ordercals2=new Ordercals();
				JSONObject jsonObject=jsonArray.getJSONObject(i);
				ordercals2.setCoupon_Price(jsonObject.getDouble("Coupon_Price"));
				ordercals2.setFreight(jsonObject.getDouble("Freight"));
				if (jsonObject.getString("WorkStation_Freight") == null || jsonObject.getString("WorkStation_Freight").equals("null")) {
					ordercals2.setWorkStation_Freight("0");
				} else {

					ordercals2.setWorkStation_Freight(jsonObject.getString("WorkStation_Freight"));
				}
				ordercals2.setRefreButton(jsonObject.getBoolean("RefreButton"));
				ordercals2.setManuf_Name(jsonObject.getString("Manuf_Name"));
				ordercals2.setOrder_Id(jsonObject.getInt("Order_Id"));
				ordercals2.setOrder_No(jsonObject.getString("Order_No"));
				ordercals2.setPayInfo(jsonObject.getString("PayInfo"));
				ordercals2.setToPay_Price(jsonObject.getDouble("ToPay_Price"));
				ordercals2.setTotal_price(jsonObject.getDouble("Total_price"));
				ordercals2.setRemark(jsonObject.getString("Remark"));
				ordercals2.setReminder(jsonObject.getString("Reminder"));
				ordercals2.setNextButton(jsonObject.getBoolean("NextButton"));
				if (jsonObject.getString("Free_Price") == null || jsonObject.getString("Free_Price").equals("null")) {
					ordercals2.setFree_Price("0");
					
				} else {

					ordercals2.setFree_Price(jsonObject.getString("Free_Price"));
				}
				ordercals2.setType(jsonObject.getInt("Type"));
				ordercals.add(ordercals2);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ordercals;
	}
}
