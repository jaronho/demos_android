package com.example.classes;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddressPovicJsonUtil {
	 public static List<AddressPovic>  getAddressPovic(String string) {
		 List<AddressPovic> addressPovics;
		 addressPovics = new ArrayList<AddressPovic>();
		 JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(string);
			JSONArray jsonArray=new JSONArray(jsonObject.getString("Data"));
			for (int i = 0; i < jsonArray.length(); i++) {
				AddressPovic addressPovic = new AddressPovic();
				addressPovic.setId(jsonArray.getJSONObject(i).getInt("Id"));
				addressPovic.setName(jsonArray.getJSONObject(i).getString("Name"));
				addressPovics.add(addressPovic);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return addressPovics;
	}

}
