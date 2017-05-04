package com.example.classes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

public class UserJsonUtil {
	public static User getUser(String string){
		 User user;
	
			 user=new User();
			if (string==null||string.equals("null")) {
				return user;
			}
			
			JSONObject jSONObject1;
			try {
				jSONObject1 = new JSONObject(string);
				JSONObject jSONObject2=new JSONObject(jSONObject1.getString("Data"));
				String nameString=jSONObject2.getString("User_Name");
				String area_Name = jSONObject2.getString("Area_Name");
				try {
					nameString = URLEncoder.encode(nameString, "UTF-8");
					area_Name = URLEncoder.encode(area_Name , "UTF-8");
				
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				user.setUser_Name(nameString);
				user.setMobile(jSONObject2.getString("Mobile"));
				user.setPermit_Type(jSONObject2.getString("Permit_Type"));
				user.setPurchasing_State(jSONObject2.getString("Purchasing_State"));
				user.setArea_Name(area_Name); 
				user.setProvince_Id(jSONObject2.getInt("Province_Id"));
				user.setCounty_Id(jSONObject2.getInt("County_Id"));
				user.setCity_Id(jSONObject2.getInt("City_Id"));
				user.setTown_Id(jSONObject2.getInt("Town_Id"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		
		return user;
	}
	
}
