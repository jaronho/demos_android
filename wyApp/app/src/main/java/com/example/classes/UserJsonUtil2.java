package com.example.classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserJsonUtil2 {
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
				JSONObject jsonObject3=new JSONObject(jSONObject2.getString("UserDetail"));
				user.setUser_Name(jsonObject3.getString("User_Name"));
				user.setNick_Name(jsonObject3.getString("Nick_Name"));
				user.setPermit_Type(jsonObject3.getString("Permit_Type"));
				user.setPurchasing_State(jsonObject3.getString("Permit_Type"));
				user.setMobile(jsonObject3.getString("Mobile"));
				user.setEmail(jsonObject3.getString("Email"));
				user.setSex(jsonObject3.getInt("Sex"));
				user.setCountyName(jSONObject2.getString("CountyName"));
				List<String> strings=new ArrayList<String>();
				JSONArray jsonArray=new JSONArray(jSONObject2.getString("JobList"));
				for (int i = 0; i < jsonArray.length(); i++) {
					strings.add(jsonArray.getString(i));
				}
				user.setJobList(strings);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		
		return user;
	}
	
}
