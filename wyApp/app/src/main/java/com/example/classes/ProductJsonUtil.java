package com.example.classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ProductJsonUtil {
	public static ArrayList<ProductJson> getProductJsons(String string){
		ArrayList<ProductJson> listJsons = null;
		JSONArray jsonArray;
		try {
			listJsons=new ArrayList<ProductJson>();
			if (string==null||string.equals("null")) {
				return listJsons;
			}
			
			JSONObject jSONObject1=new JSONObject(string);
			JSONObject jSONObject2=new JSONObject(jSONObject1.getString("Data"));
			jsonArray =jSONObject2.getJSONArray("V_SearchProduct");
			for (int i = 0; i < jsonArray.length(); i++) {
				ProductJson productJson=new ProductJson();
				JSONObject jsonObject=jsonArray.getJSONObject(i);
				productJson.setPro_Name(jsonObject.getString("Pro_Name"));
				productJson.setManuf_Name(jsonObject.getString("Manuf_Name"));
				productJson.setId(jsonObject.getInt("Id"));
				productJson.setDeal(jsonObject.getInt("deal"));
				productJson.setPic_Url(jsonObject.getString("Pic_Url"));
				if (jsonObject.getDouble("Price") == 0) {
					productJson.setPrice(0);
				} else {
					productJson.setPrice(jsonObject.getDouble("Price"));
				}
				productJson.setShow_Price(jsonObject.getString("Show_Price"));
//				productJson.setPrice(jsonObject.getDouble("Price"));
				productJson.setType(jsonObject.getString("Type"));
				productJson.setSpec(jsonObject.getString("Spec"));
				productJson.setCommon_Name(jsonObject.getString("Common_Name"));
				productJson.setTotal_Content(jsonObject.getString("Total_Content"));
				productJson.setDosageform(jsonObject.getString("Dosageform"));
				productJson.setIs_Stockout(jsonObject.getBoolean("Is_Stockout"));
//				productJson.setShowMarket(jsonObject.getBoolean("ShowMarket"));
				listJsons.add(productJson);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listJsons;
	}
	public static Productshaixuan getProductshaixuan(String string){
		Productshaixuan listJsons = null;
		try {
			listJsons=new Productshaixuan();
			if (string==null||string.equals("null")) {
				return listJsons;
			}
			
			JSONObject jSONObject1=new JSONObject(string);
			JSONObject jSONObject2=new JSONObject(jSONObject1.getString("Data"));
			JSONArray	jsonArray=jSONObject2.getJSONArray("Manuf_Namelist");
			ArrayList<String> Manuf_Namelist=new ArrayList<String>();
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject=jsonArray.getJSONObject(i);
				Manuf_Namelist.add(jsonObject.getString("Manuf_Name"));
			}
			listJsons.setManuf_Namelist(Manuf_Namelist);

			ArrayList<String> Dosageformlist=new ArrayList<String>();
			JSONArray	jsonArray2=jSONObject2.getJSONArray("Dosageformlist");
			for (int i = 0; i < jsonArray2.length(); i++) {
				JSONObject jsonObject=jsonArray2.getJSONObject(i);
				Dosageformlist.add(jsonObject.getString("Dosageform"));
			}
			listJsons.setDosageformlist(Dosageformlist);

			ArrayList<String> Typelist=new ArrayList<String>();
			JSONArray	jsonArray3=jSONObject2.getJSONArray("Typelist");

			for (int i = 0; i < jsonArray3.length(); i++) {
				JSONObject jsonObject=jsonArray3.getJSONObject(i);
				Typelist.add(jsonObject.getString("Type"));
			}
			listJsons.setTypelist(Typelist);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listJsons;
	}

}
