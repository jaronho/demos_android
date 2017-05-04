package com.example.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyStockOutProJsonUtil {
	@SuppressWarnings("unchecked")
	public static MyStockOutPro getStockOutPro(String string) {
		MyStockOutPro myStockOutPro = new MyStockOutPro();
		if (string == null || string.equals("null")) {
			return myStockOutPro;
		}
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(string);
			JSONObject jsonObject2 = new JSONObject(jsonObject.getString("Data"));
			myStockOutPro.setHasStockOut(jsonObject2.getBoolean("HasStockOut"));
			myStockOutPro.setAlertInviteTip(jsonObject2.getBoolean("AlertInviteTip"));
			List<String> list = new ArrayList<String>();
			// List<String> prodctStocks = new ArrayList<String>();
			List<Map<String, String>> maps = new ArrayList<Map<String,String>>();
			
			JSONArray jsarray = new JSONArray(jsonObject2.getString("StockOutPro"));
			JSONArray arrayLogis = new JSONArray(jsonObject2.getString("FreightInfo"));
			JSONObject mapdata = new JSONObject(jsonObject2.getString("StockInfo"));//选择商品id对应的数量
			List<LogisticsDetial> logisDetials = new ArrayList<LogisticsDetial>();
			for (int i = 0; i < jsarray.length(); i++) {
				list.add(jsarray.get(i).toString());
			}
			for (int i = 0; i < arrayLogis.length(); i++) {
				LogisticsDetial logisticsDetial = new LogisticsDetial();
				JSONObject object = arrayLogis.getJSONObject(i);
				logisticsDetial.setId(object.getInt("Id"));
				logisticsDetial.setTitle(object.getString("Title"));
				logisticsDetial.setContent(object.getString("Content"));
				logisticsDetial.setLgistics(object.getString("Freight"));
				logisDetials.add(logisticsDetial);
			}
			myStockOutPro.setLogistics(logisDetials);
			Map<String, String> map = new HashMap<String, String>();;
				Iterator it = mapdata.keys();
					while(it.hasNext()){
						String key = (String)it.next();
						int value = Integer.valueOf(mapdata.getString(key));
						map.put(key, value+"");
					}
			myStockOutPro.setListStock(map);

			myStockOutPro.setStockOutPro(list);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return myStockOutPro;
	}
}
