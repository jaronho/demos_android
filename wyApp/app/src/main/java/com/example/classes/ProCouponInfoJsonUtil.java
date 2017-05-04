package com.example.classes;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProCouponInfoJsonUtil {

	public static ArrayList<ProCouponInfo> getProCouponJsons(String string){
		
		ArrayList<ProCouponInfo> couponList = new ArrayList<ProCouponInfo>();
			if (string == null || string.equals("null")) {
				return couponList;
			}
			try {
				JSONObject jsonObject = new JSONObject(string);
				JSONArray jsonArray = new JSONArray(jsonObject.getString("Data"));
				
				for (int i = 0; i < jsonArray.length(); i++) {
					ProCouponInfo couponInfo = new ProCouponInfo();
					JSONObject jsonObject2 = jsonArray.getJSONObject(i);
					couponInfo.setContent(jsonObject2.getString("Content"));
					couponInfo.setCouponName(jsonObject2.getString("Name"));
					couponInfo.setFlag(jsonObject2.getInt("flag"));
					couponList.add(couponInfo);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
		
		return couponList;
		
	}
}
