package com.example.classes;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DingdanDetialJsonUtil {
	public static DingDanDetial getDingDanDetials(String string){
		DingDanDetial danDetial = null;
//		JSONArray jsonArray;
		try {
			danDetial=new DingDanDetial();
			if (string==null||string.equals("null")) {
				return danDetial;
			}
			
			JSONObject jSONObject1=new JSONObject(string);
			JSONObject jsonObject3=new JSONObject(jSONObject1.getString("Data"));
				danDetial.setAddDate(jsonObject3.getString("AddDate"));
				danDetial.setAddress(jsonObject3.getString("Address"));
				danDetial.setCouponPrice(jsonObject3.getDouble("CouponPrice"));
				danDetial.setDeliveryDate(jsonObject3.getString("DeliveryDate"));
				danDetial.setDeliveryType(jsonObject3.getString("DeliveryType"));	
				danDetial.setDeliveryName(jsonObject3.getString("DeliveryName"));
				danDetial.setDeliveryPhone(jsonObject3.getString("DeliveryPhone"));
//				danDetial.setFinalPrice(jsonObject3.getInt("FinalPrice"));
				danDetial.setFreight(jsonObject3.getDouble("Freight"));
				danDetial.setInvoiceCategory(jsonObject3.getString("InvoiceCategory"));
				danDetial.setInvoiceTitle(jsonObject3.getString("InvoiceTitle"));
				danDetial.setInvoiceType(jsonObject3.getString("InvoiceType"));
				danDetial.setIsInvoice(jsonObject3.getBoolean("IsInvoice"));
				danDetial.setOrderNo(jsonObject3.getString("OrderNo"));
				danDetial.setOrderTotalPrice(jsonObject3.getDouble("OrderTotalPrice"));
				danDetial.setPayState(jsonObject3.getString("PayState"));
//				danDetial.setPrePrice(jsonObject3.getInt("PrePrice"));
				danDetial.setReceiveDate(jsonObject3.getString("ReceiveDate"));
				danDetial.setReceiveName(jsonObject3.getString("ReceiveName"));
				danDetial.setReceivePhone(jsonObject3.getString("ReceivePhone"));
				danDetial.setRemark(jsonObject3.getString("Remark"));
				danDetial.setReturnsProDate(jsonObject3.getString("ReturnsProDate"));
				danDetial.setTotalPrice(jsonObject3.getDouble("TotalPrice"));
				
				
				danDetial.setType(jsonObject3.getInt("Type"));
				
				danDetial.setFree_Price(jsonObject3.getDouble("Free_Price"));
				
				
				
				danDetial.setUserName(jsonObject3.getString("UserName"));
				List<SubOrders> subOrders=new ArrayList<SubOrders>();
				JSONArray jsonArray2=new JSONArray(jsonObject3.getString("Products"));
				for (int j = 0; j < jsonArray2.length(); j++) {
					SubOrders subOrders2=new SubOrders();
					JSONObject jsonObject4=jsonArray2.getJSONObject(j);
					subOrders2.setCount(jsonObject4.getInt("Count"));
					subOrders2.setId(jsonObject4.getInt("Id"));
					subOrders2.setSpec(jsonObject4.getString("Spec"));
					subOrders2.setName(jsonObject4.getString("Name"));
					subOrders2.setPrice(jsonObject4.getDouble("Price"));
					subOrders2.setPicUrl(jsonObject4.getString("PicUrl"));
					subOrders.add(subOrders2);
				}
				danDetial.setSubOrders(subOrders);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return danDetial;
	}
}
