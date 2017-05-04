package com.example.classes;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ManufLianXiJsonUtil {
	public static ManufLianXi getManufLianXi(String string){
		ManufLianXi manufLianXi=null;
		if (string==null||string.equals("null")) {
			return null;
		}
			JSONObject jSONObject1;
			try {
				manufLianXi=new ManufLianXi();
				jSONObject1 = new JSONObject(string);
				JSONObject jsonObject0=new JSONObject(jSONObject1.getString("Data"));
				manufLianXi.setManufAddress(jsonObject0.getString("ManufAddress"));
				manufLianXi.setManufPhone(jsonObject0.getString("ManufPhone"));
				manufLianXi.setWorkTime(jsonObject0.getString("WorkTime"));
				List<String> qqList=new ArrayList<String>();
				JSONArray jsonArray=new JSONArray(jsonObject0.getString("QQList"));
				if (jsonObject0.getString("QQList").equals("null")) {
					manufLianXi.setQQList(qqList);
				}else {
					for (int i = 0; i < jsonArray.length(); i++) {
						qqList.add(jsonArray.getString(i));
					}
					manufLianXi.setQQList(qqList);
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		return manufLianXi;
	}
	
}
