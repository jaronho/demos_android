package com.example.classes;

import org.json.JSONException;
import org.json.JSONObject;

public class MyGPSUtil {
		public static MyGPS getGps(String string){
			MyGPS gps = new MyGPS();
			if (gps.equals("null")||gps == null) {
				return gps;
			} 
			try {
				JSONObject jsonObject1 = new JSONObject(string);
				JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Data"));
				
				gps.setProvinceName(jsonObject2.getString("provinceName"));
				gps.setCityName(jsonObject2.getString("cityName"));
				gps.setCountyName(jsonObject2.getString("countyName"));
				gps.setProId(jsonObject2.getInt("provinceId"));
				gps.setCityId(jsonObject2.getInt("cityId"));
				gps.setCouId(jsonObject2.getInt("countyId"));
			
			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return gps;
		}

}
