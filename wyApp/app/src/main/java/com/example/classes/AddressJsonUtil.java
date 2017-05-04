package com.example.classes;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddressJsonUtil {
	public static List<Address> getAddresses(String string) throws JSONException{
		List<Address> addresses;
		addresses=new ArrayList<Address>();
		JSONObject jsonObject=new JSONObject(string);

		JSONArray jsonArray=new JSONArray(jsonObject.getString("SubAreas"));
		for (int i = 0; i < jsonArray.length(); i++) {
			Address address=new Address();
			JSONObject jsonObject2=jsonArray.getJSONObject(i);
			address.setId(jsonObject2.getInt("Id"));
			address.setArea_Name(jsonObject2.getString("Area_Name"));
			JSONArray jsonArray2=new JSONArray(jsonObject2.getString("SubAreas"));

			List<Address1> address1s=new ArrayList<Address1>();
			for (int j = 0; j < jsonArray2.length(); j++) {
				Address1 address1=new Address1();
				JSONObject jsonObject3=jsonArray2.getJSONObject(j);
				address1.setId(jsonObject3.getInt("Id"));
				address1.setArea_Name(jsonObject3.getString("Area_Name"));
				JSONArray jsonArray3=new JSONArray(jsonObject3.getString("SubAreas"));
				List<Address2> address2s=new ArrayList<Address2>();

				for (int k = 0; k < jsonArray3.length(); k++) {
					Address2 address2=new Address2();
					JSONObject jsonObject4=jsonArray3.getJSONObject(k);
					address2.setId(jsonObject4.getInt("Id"));
					address2.setArea_Name(jsonObject4.getString("Area_Name"));
					address2s.add(address2);
				}
				address1.setList(address2s);
				address1s.add(address1);
			}
			address.setList(address1s);
			addresses.add(address);

		}
		return addresses;
	}
	

}
