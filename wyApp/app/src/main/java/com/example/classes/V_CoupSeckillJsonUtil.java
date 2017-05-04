package com.example.classes;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class V_CoupSeckillJsonUtil {
	public static ArrayList<V_CoupSeckill> getV_CoupSeckills(String string){
		ArrayList<V_CoupSeckill> v_CoupSeckills = null;
		JSONArray jsonArray;
		try {
			v_CoupSeckills=new ArrayList<V_CoupSeckill>();
			if (string==null||string.equals("null")) {
				return v_CoupSeckills=null;
			}
			
			JSONObject jSONObject1=new JSONObject(string);
			if (jSONObject1.getString("Data").equals("null")||jSONObject1.getString("Data")==null) {
				return v_CoupSeckills=null;
			}
			jsonArray=new JSONArray(jSONObject1.getString("Data"));
			for (int i = 0; i < jsonArray.length(); i++) {
				V_CoupSeckill v_CoupSeckill=new V_CoupSeckill();
				JSONObject jsonObject=jsonArray.getJSONObject(i);
				v_CoupSeckill.setBeginDate(jsonObject.getString("BeginDate"));
				v_CoupSeckill.setCurrentDate(jsonObject.getString("CurrentDate"));
				v_CoupSeckill.setEndDate(jsonObject.getString("EndDate"));
				JSONObject jsonObject2=new JSONObject(jsonObject.getString("V_CoupSeckill"));
				v_CoupSeckill.setMoney(jsonObject2.getInt("Money"));
				v_CoupSeckill.setRule_Group_Id(jsonObject2.getInt("Rule_Group_Id"));
				v_CoupSeckill.setName(jsonObject2.getString("Name"));
				v_CoupSeckill.setStock(jsonObject2.getInt("Stock"));
				v_CoupSeckills.add(v_CoupSeckill);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return v_CoupSeckills;
	}
}
