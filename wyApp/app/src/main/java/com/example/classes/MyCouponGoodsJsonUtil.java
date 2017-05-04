package com.example.classes;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyCouponGoodsJsonUtil {

	public static ArrayList<MyCouponGoods> getProductBriefJsons(String string) {
		ArrayList<MyCouponGoods> productList = new ArrayList<MyCouponGoods>();
		try {
			if (string == null || string.equals("null")) {
				return productList;
			}
			JSONObject jSONObject1 = new JSONObject(string);
			JSONArray jsonArray2=new JSONArray(jSONObject1.getString("Data"));
			
			for (int i = 0; i < jsonArray2.length(); i++) {
				MyCouponGoods productJson = new MyCouponGoods();
				JSONObject jSONObject2=jsonArray2.getJSONObject(i);
				productJson.setTotal_Content(jSONObject2.getString("Total_Content"));
				productJson.setCommon_Name(jSONObject2.getString("Common_Name"));
				productJson.setDosageform(jSONObject2.getString("Dosageform"));
				productJson.setPro_Name(jSONObject2.getString("Pro_Name"));
				productJson.setPic_Url(jSONObject2.getString("Pic_Url"));
				productJson.setStock(jSONObject2.getInt("Stock"));
				productJson.setId(jSONObject2.getInt("Id"));
				productJson.setCate_Name(jSONObject2.getString("Cate_Name"));
				productJson.setSpec(jSONObject2.getString("Spec"));
				productJson.setPrice(jSONObject2.getString("Price"));
				productJson.setSubUnit_Price(jSONObject2.getString("SubUnit_Price"));
				productJson.setSubUnit(jSONObject2.getString("SubUnit"));
				productJson.setShow_Price(jSONObject2.getString("Show_Price"));
				productJson.setUnit(jSONObject2.getString("Unit"));
				productList.add(productJson);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return productList;
	}

}
