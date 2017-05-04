package com.example.classes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FreePriceUtil {
	
	public static Map<String,FreePriceJson> getProductIds(List<FreePriceJson> res){
		Map map = new HashMap<String,String>();
		for (FreePriceJson freePriceJson : res) {
			map.put(freePriceJson.getProId() ,freePriceJson);
		}
		return map;
	}
	public static Map<String,FreePriceJson> getFreePriceJsons(String result){
		Map<String,FreePriceJson> res = new HashMap<String,FreePriceJson>();
		try {
			if (result==null||result.equals("null")) {
				return null;
			}
			
			JSONObject root=new JSONObject(result);
			JSONArray data=new JSONArray(root.getString("Data"));
			for (int i = 0; i < data.length(); i++) {
				FreePriceJson freePriceJson = new FreePriceJson();
				freePriceJson.setCount(data.getJSONObject(i).getInt("count"));
				freePriceJson.setDiscountRange(data.getJSONObject(i).getDouble("discountRange"));
				int id = data.getJSONObject(i).getInt("proId");
				freePriceJson.setProId(id);
				res.put(id+"", freePriceJson);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
}
