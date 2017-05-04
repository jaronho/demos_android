package com.example.classes;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyShopperJson {
	public static ArrayList<MyShopper> getMyShopper(String string){
		ArrayList<MyShopper> myShoppers = new ArrayList<MyShopper>();
		if (string==null||string.equals("null")) {
			
			return myShoppers;
		}
		JSONObject jsonObject1;
		try {
			jsonObject1 = new JSONObject(string);
			JSONArray jsonArray = new JSONArray(jsonObject1.getString("Data"));
			for (int i = 0; i < jsonArray.length(); i++) {
				MyShopper myShopper = new MyShopper();
				JSONObject jsonObject2 = jsonArray.getJSONObject(i);
				myShopper.setMoney(jsonObject2.getDouble("Money"));
				myShopper.setUser_Name(jsonObject2.getString("User_Name"));
				myShoppers.add(myShopper);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		return myShoppers;
	}

}
