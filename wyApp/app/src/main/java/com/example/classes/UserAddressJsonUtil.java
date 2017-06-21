package com.example.classes;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserAddressJsonUtil {
	public static List<UserAddress> getUserAddressList(String string){
		List<UserAddress> userAddresses = null;
		JSONArray jsonArray;
		try {
			userAddresses=new ArrayList<UserAddress>();
			if (string==null||string.equals("null")) {
				return userAddresses=null;
			}
			
			JSONObject jSONObject1=new JSONObject(string);
			if (jSONObject1.getString("Data")==null||jSONObject1.getString("Data").equals("null")) {
				return userAddresses=null;
			}
			jsonArray=new JSONArray(jSONObject1.getString("Data"));
			for (int i = 0; i < jsonArray.length(); i++) {
				UserAddress userAddress=new UserAddress();
				JSONObject jsonObject=jsonArray.getJSONObject(i);
				userAddress.setId(jsonObject.getInt("Id"));
				userAddress.setCity_Name(jsonObject.getString("City_Name"));
				userAddress.setCounty_Name(jsonObject.getString("County_Name"));
				userAddress.setProvince_Name(jsonObject.getString("Province_Name"));
				userAddress.setReceive_Address(jsonObject.getString("Receive_Address"));
				userAddress.setReceive_Name(jsonObject.getString("Receive_Name"));
				userAddress.setReceive_Phone(jsonObject.getString("Receive_Phone"));
				userAddress.setProvince_Id(jsonObject.getInt("Province_Id"));
				userAddress.setCity_Id(jsonObject.getInt("City_Id"));
				userAddress.setCounty_Id(jsonObject.getInt("County_Id"));
				userAddress.setState(jsonObject.getInt("State"));
//				if (jsonObject.getString("Town_Id") != null 
//						|| !jsonObject.getString("Town_Id").equals("") 
//						|| !jsonObject.getString("Town_Id").equals("0")) {
					
					userAddress.setTown_Id(jsonObject.getInt("Town_Id"));
					userAddress.setTown_Name(jsonObject.getString("Town_Name"));
//					if (jsonObject.getString("Vallage_Id") != null 
//							|| !jsonObject.getString("Vallage_Id").equals("")
//							|| !jsonObject.getString("Vallage_Id").equals("0")) {
						userAddress.setVaillage_Id(jsonObject.getInt("Vallage_Id"));
						userAddress.setVaillage_Name(jsonObject.getString("Vallage_Name"));
						
//					}
//				}
				userAddresses.add(userAddress);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userAddresses;
	}
	public static UserAddress getUserAddress(String string){
		UserAddress userAddresses = null;
		try {
			userAddresses=new UserAddress();
			if (string==null||string.equals("null")) {
				return userAddresses=null;
			}
			
			JSONObject jSONObject1=new JSONObject(string);
			if (jSONObject1.getString("Data")==null||jSONObject1.getString("Data").equals("null")) {
				return userAddresses=null;
			}
			JSONObject jSONObject2=new JSONObject(jSONObject1.getString("Data"));
			JSONObject jSONObject3 = new JSONObject(jSONObject2.getString("Address"));
			
			userAddresses.setId(jSONObject3.getInt("Id"));
			userAddresses.setCity_Name(jSONObject3.getString("City_Name"));
			userAddresses.setCounty_Name(jSONObject3.getString("County_Name"));
			userAddresses.setProvince_Name(jSONObject3.getString("Province_Name"));
			userAddresses.setReceive_Address(jSONObject3.getString("Receive_Address"));
			userAddresses.setReceive_Name(jSONObject3.getString("Receive_Name"));
			userAddresses.setReceive_Phone(jSONObject3.getString("Receive_Phone"));
			userAddresses.setProvince_Id(jSONObject3.getInt("Province_Id"));
			userAddresses.setCity_Id(jSONObject3.getInt("City_Id"));
			userAddresses.setCounty_Id(jSONObject3.getInt("County_Id"));
			userAddresses.setState(jSONObject3.getInt("State"));
//			if (jSONObject3.getString("Town_Id") != null || !jSONObject3.getString("Town_Id").equals("") || !jSONObject3.getString("Town_Id").equals("0")) {
				
				userAddresses.setTown_Id(jSONObject3.getInt("Town_Id"));
				userAddresses.setTown_Name(jSONObject3.getString("Town_Name"));
//				if (jSONObject3.getString("Vallage_Id") != null || !jSONObject3.getString("Vallage_Id").equals("") || !jSONObject3.getString("Vallage_Id").equals("0")) {
					userAddresses.setVaillage_Id(jSONObject3.getInt("Vallage_Id"));
					userAddresses.setVaillage_Name(jSONObject3.getString("Vallage_Name"));
					
//				}
//			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userAddresses;
	}
}
