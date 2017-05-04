package com.example.classes;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TiXianJsonUtil {
	public static Tixian getTixian(String string){
		Tixian tixian;
			
		tixian=new Tixian();
		if (string==null||string.equals("null")) {
			return null;
		}
		
		JSONObject jSONObject1;
		try {
			jSONObject1 = new JSONObject(string);
			JSONObject jSONObject2=new JSONObject(jSONObject1.getString("Data"));
			tixian.setIsBindMobile(jSONObject2.getBoolean("IsBindMobile"));
			tixian.setIsFillAccount(jSONObject2.getBoolean("IsFillAccount"));
			tixian.setBindAccountMessage(jSONObject2.getString("BindAccountMessage"));
			List<TixianItem> tixianItems=new ArrayList<TixianItem>();
			if (jSONObject2.getString("Items").equals("null")) {
				tixianItems=null;
			}else {
				JSONArray jsonArray=new JSONArray(jSONObject2.getString("Items"));
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject3=jsonArray.getJSONObject(i);
					TixianItem tixianItem=new TixianItem();
					tixianItem.setAddTime(jsonObject3.getString("AddTime"));
					tixianItem.setEndTime(jsonObject3.getString("EndTime"));
					tixianItem.setState(jsonObject3.getString("State"));
					tixianItem.setTakeMoney(jsonObject3.getDouble("TakeMoney"));
					tixianItems.add(tixianItem);
				}
				tixian.setTixianItems(tixianItems);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	
	return tixian;
	}
}
