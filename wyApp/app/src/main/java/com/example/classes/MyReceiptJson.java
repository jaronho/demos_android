package com.example.classes;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyReceiptJson {
	public static List<MyReceipt> getMyReceipt(String string){
		List<MyReceipt> myReceipts = new ArrayList<MyReceipt>();
		
		try {
			if (string== null || string=="null") {
				return myReceipts;
			}
			JSONObject jsonObject = new JSONObject(string);
//			JSONObject jsonObject2 = new JSONObject(jsonObject.getString("Data"));
			JSONArray array = new JSONArray(jsonObject.getString("Data"));
			for (int i = 0; i < array.length(); i++) {
				MyReceipt myReceipt = new MyReceipt();
				JSONObject object = array.getJSONObject(i);
				myReceipt.setOrderId(object.getString("OrderId"));
				myReceipt.setOrder_No(object.getString("Order_No"));
				myReceipt.setOrderState(object.getString("OrderState"));
				myReceipt.setTakeTime(object.getString("Invoice_Date"));
				myReceipt.setPrice(object.getString("RealPayment"));
				myReceipt.setReceiptState(object.getString("State"));
				myReceipt.setTakeTime(object.getString("Receive_Date"));
				myReceipt.setTicketNumber(object.getString("Tracking_No"));
				myReceipt.setClickButton(object.getBoolean("ClickButton"));
				myReceipt.setColor(object.getString("IosColour"));
				myReceipts.add(myReceipt);
			}
			
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return myReceipts;
		
	}

}
