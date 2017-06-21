package com.example.classes;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrdersJsonUtil {
	public static List<GroupOrder> getProductJsons(String string) {
		List<GroupOrder> orders = null;
		JSONArray jsonArray;
		JSONObject jsonObjectgroup;
		try {
			orders = new ArrayList<GroupOrder>();
			if (string == null || string.equals("null")) {
				return orders;
			}

			JSONObject jSONObject1 = new JSONObject(string);
			JSONObject jSONObject2 = new JSONObject(jSONObject1.getString("Data"));
			JSONArray arrayOrder = jSONObject2.getJSONArray("Orders");
			
			for (int i = 0; i < arrayOrder.length(); i++) {
				GroupOrder groupOrder = new GroupOrder();
				jsonObjectgroup = arrayOrder.getJSONObject(i).getJSONObject("orderGroup");
				jsonArray = arrayOrder.getJSONObject(i).getJSONArray("OrderItems");
				groupOrder.setId(jsonObjectgroup.getInt("Id"));
				groupOrder.setGroup_No(jsonObjectgroup.getString("Group_No"));
				groupOrder.setTotal_Money(jsonObjectgroup.getString("Total_Money"));
				groupOrder.setPay_Money(jsonObjectgroup.getString("Pay_Money"));
				groupOrder.setToPay_Money(jsonObjectgroup.getString("ToPay_Money"));
				groupOrder.setTotal_Freight(jsonObjectgroup.getString("Total_Freight"));
				groupOrder.setCoupon_Money(jsonObjectgroup.getString("Coupon_Money"));
				groupOrder.setType(jsonObjectgroup.getInt("Type"));
				groupOrder.setUser_Id(jsonObjectgroup.getInt("User_Id"));
				groupOrder.setAdd_Date(jsonObjectgroup.getString("Add_Date"));
				groupOrder.setRemark(jsonObjectgroup.getString("Remark"));
				groupOrder.setiCount(jsonObjectgroup.getInt("iCount"));
				groupOrder.setUser_Name(jsonObjectgroup.getString("User_Name"));
				List<Orders> listOrders = new ArrayList<Orders>();
				for (int j = 0; j < jsonArray.length(); j++) {
					Orders orders2 = new Orders();
					JSONObject jsonObject3 = jsonArray.getJSONObject(j);
					orders2.setId(jsonObject3.getInt("Id"));
					orders2.setOrder_No(jsonObject3.getString("Order_No"));
					orders2.setState(jsonObject3.getString("State"));
					orders2.setPrice(jsonObject3.getDouble("Price"));
					orders2.setService_Phone(jsonObject3.getString("Service_Phone"));
					orders2.setSend_Name(jsonObject3.getString("Send_Name"));
					List<SubOrders> subOrders = new ArrayList<SubOrders>();
					JSONArray jsonArray2 = new JSONArray(jsonObject3.getString("SubOrders"));
					for (int k = 0; k < jsonArray2.length(); k++) {
						SubOrders subOrders2 = new SubOrders();
						JSONObject jsonObject4 = jsonArray2.getJSONObject(k);
						subOrders2.setCount(jsonObject4.getInt("Count"));
						subOrders2.setId(jsonObject4.getInt("Id"));
						subOrders2.setSpec(jsonObject4.getString("Spec"));
						subOrders2.setName(jsonObject4.getString("Name"));
						subOrders2.setPrice(jsonObject4.getDouble("Price"));
						subOrders2.setImageUrl(jsonObject4.getString("ImageUrl"));
						subOrders.add(subOrders2);
					}
					List<Integer> integers = new ArrayList<Integer>();
					JSONArray arrayButton = new JSONArray(jsonObject3.getString("Buttons"));
					for (int k = 0; k < arrayButton.length(); k++) {
						integers.add(arrayButton.getInt(k));
					}
					orders2.setIntegers(integers);
					orders2.setSubOrders(subOrders);
					listOrders.add(orders2);
					groupOrder.setOrders(listOrders);
				}
				orders.add(groupOrder);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return orders;
	}
}
