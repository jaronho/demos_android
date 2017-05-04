package com.example.classes;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductCartJsonUtil {
	public static List<List<ProductCart>> getProductJsons(String string) {
		List<List<ProductCart>> lists = null;
		List<ProductCart> listJsons = null;
		List<ProductCart> listJsons1 = null;

		JSONArray jsonArray1, jsonArray11;

		try {
			lists = new ArrayList<List<ProductCart>>();
			listJsons = new ArrayList<ProductCart>();
			listJsons1 = new ArrayList<ProductCart>();
			if (string == null || string.equals("null")) {
				return lists;
			}
			JSONObject jSONObject1 = new JSONObject(string);
			JSONObject jSONObject2 = new JSONObject(jSONObject1.getString("Data"));
			jsonArray1 = jSONObject2.getJSONArray("Product");
			for (int i = 0; i < jsonArray1.length(); i++) {
				ProductCart productJson = new ProductCart();
				JSONObject jsonObject = jsonArray1.getJSONObject(i);
				productJson.setPro_Name(jsonObject.getString("Pro_Name"));
				productJson.setManuf_Name(jsonObject.getString("Manuf_Name"));
				productJson.setPro_Id(jsonObject.getInt("Pro_Id"));
				productJson.setPic_Url(jsonObject.getString("Pic_Url"));
				productJson.setPrice(jsonObject.getDouble("Price"));
				productJson.setType(jsonObject.getString("Type"));
				productJson.setSpec(jsonObject.getString("Spec"));
				productJson.setCount(jsonObject.getInt("Count"));
				productJson.setIs_Presell(jsonObject.getBoolean("Is_Presell"));
				productJson.setTimeStamp(jsonObject.getString("TimeStamp"));
				productJson.setCommon_Name(jsonObject.getString("Common_Name"));
				productJson.setTotal_Content(jsonObject.getString("Total_Content"));
				productJson.setDosageform(jsonObject.getString("Dosageform"));
				productJson.setFree_Price(jsonObject.getDouble("Free_Price"));
				productJson.setMarketing_Type(jsonObject.getInt("Marketing_Type"));
				JSONArray jsonArray2 = new JSONArray(jsonObject.getString("priceList"));
				List<Price> list = new ArrayList<Price>();
				for (int j = 0; j < jsonArray2.length(); j++) {
					Price price = new Price();
					JSONObject jsonObject3 = jsonArray2.getJSONObject(j);
					price.setMaximum(jsonObject3.getInt("Maximum"));
					price.setMinimum(jsonObject3.getInt("Minimum"));
					price.setPrice(jsonObject3.getDouble("Price"));
					list.add(price);
				}
				productJson.setPriceList(list);
				listJsons.add(productJson);
			}
			jsonArray11 = jSONObject2.getJSONArray("InvalidProduct");
			for (int i = 0; i < jsonArray11.length(); i++) {
				ProductCart productJson = new ProductCart();
				JSONObject jsonObject = jsonArray11.getJSONObject(i);
				productJson.setPro_Name(jsonObject.getString("Pro_Name"));
				productJson.setManuf_Name(jsonObject.getString("Manuf_Name"));
				productJson.setPro_Id(jsonObject.getInt("Pro_Id"));
				productJson.setPic_Url(jsonObject.getString("Pic_Url"));

				productJson.setPrice(jsonObject.getDouble("Price"));

				productJson.setType(jsonObject.getString("Type"));
				productJson.setSpec(jsonObject.getString("Spec"));
				productJson.setCount(jsonObject.getInt("Count"));
				productJson.setCommon_Name(jsonObject.getString("Common_Name"));
				productJson.setTotal_Content(jsonObject.getString("Total_Content"));
				productJson.setDosageform(jsonObject.getString("Dosageform"));
				productJson.setFree_Price(jsonObject.getDouble("Free_Price"));
				productJson.setMarketing_Type(jsonObject.getInt("Marketing_Type"));
				JSONArray jsonArray2 = new JSONArray(jsonObject.getString("priceList"));
				List<Price> list = new ArrayList<Price>();
				for (int j = 0; j < jsonArray2.length(); j++) {
					Price price = new Price();
					JSONObject jsonObject3 = jsonArray2.getJSONObject(j);
					price.setMaximum(jsonObject3.getInt("Maximum"));
					price.setMinimum(jsonObject3.getInt("Minimum"));
					price.setPrice(jsonObject3.getDouble("Price"));
					list.add(price);
				}
				productJson.setPriceList(list);
				listJsons1.add(productJson);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lists.add(listJsons);
		lists.add(listJsons1);
		return lists;
	}

	

}
